package net.bettercombat.logic;

import com.google.gson.stream.JsonReader;
import net.bettercombat.api.AttributesContainer;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.api.WeaponAttributesHelper;
import net.bettercombat.config.FallbackConfig;
import net.bettercombat.utils.PatternMatching;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.ResourceLocation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WeaponRegistry {
    public static final Map<ResourceLocation, WeaponAttributes> registrations = new HashMap<>();
    public static final Map<ResourceLocation, AttributesContainer> containers = new HashMap<>();
    private static final Map<Item, WeaponAttributes> itemCache = new ConcurrentHashMap<>();

    public static FallbackConfig fallbackConfig = FallbackConfig.createDefault();

    private static final String[] ATTRIBUTE_FILES = {
            "anchor", "axe", "battlestaff", "bow_two_handed_heavy", "bow_two_handed_light",
            "claw", "claymore", "coral_blade", "crossbow_two_handed_heavy", "crossbow_two_handed_light",
            "cutlass", "dagger", "double_axe", "fist", "glaive", "halberd",
            "hammer", "heavy_axe", "katana", "lance", "mace", "pickaxe",
            "rapier", "scythe", "sickle", "soul_knife", "spear", "staff",
            "sword", "trident", "twin_blade", "wand",
            "diamond_sword", "golden_sword", "iron_sword", "netherite_sword", "stone_sword", "wooden_sword"
    };

    public static void register(ResourceLocation itemId, WeaponAttributes attributes) {
        registrations.put(itemId, attributes);
        itemCache.clear();
    }

    public static WeaponAttributes getAttributes(ResourceLocation itemId) {
        return registrations.get(itemId);
    }

    public static WeaponAttributes getAttributes(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return registrations.get(new ResourceLocation("bettercombat", "fist"));
        }

        WeaponAttributes nbtAttributes = WeaponAttributesHelper.readFromNBT(itemStack);
        if (nbtAttributes != null) {
            return nbtAttributes;
        }

        Item item = itemStack.getItem();
        WeaponAttributes cached = itemCache.get(item);
        if (cached != null) {
            return cached;
        }

        ResourceLocation id = item.getRegistryName();
        if (id == null) {
            return null;
        }

        WeaponAttributes attributes = registrations.get(id);
        if (attributes == null) {
            attributes = resolveFallback(item, id);
        }

        if (attributes != null) {
            itemCache.put(item, attributes);
        }
        return attributes;
    }

    public static void initialize() {
        System.out.println("[BetterCombat] Initializing WeaponRegistry...");
        registrations.clear();
        containers.clear();
        itemCache.clear();
        loadContainers();
        System.out.println("[BetterCombat] Loaded " + containers.size() + " attribute containers.");

        // Register vanilla swords explicitly if present
        registerExplicit("minecraft:wooden_sword", "bettercombat:wooden_sword");
        registerExplicit("minecraft:stone_sword", "bettercombat:stone_sword");
        registerExplicit("minecraft:iron_sword", "bettercombat:iron_sword");
        registerExplicit("minecraft:golden_sword", "bettercombat:golden_sword");
        registerExplicit("minecraft:diamond_sword", "bettercombat:diamond_sword");

        // Register generic fist
        AttributesContainer fistContainer = containers.get(new ResourceLocation("bettercombat", "fist"));
        if (fistContainer != null) {
            registrations.put(new ResourceLocation("bettercombat", "fist"), resolveAttributes(new ResourceLocation("bettercombat", "fist"), fistContainer));
        }

        // Apply fallback matching to all registered items
        for (ResourceLocation itemId : Item.REGISTRY.getKeys()) {
            Item item = Item.REGISTRY.getObject(itemId);
            if (item != null && !registrations.containsKey(itemId)) {
                resolveFallback(item, itemId);
            }
        }

        System.out.println("[BetterCombat] Registered weapon attributes for " + registrations.size() + " items.");
    }

    private static void registerExplicit(String itemRegistryName, String attributeName) {
        ResourceLocation itemId = new ResourceLocation(itemRegistryName);
        AttributesContainer container = containers.get(new ResourceLocation(attributeName));
        if (container != null) {
            WeaponAttributes resolved = resolveAttributes(itemId, container);
            if (resolved != null) {
                registrations.put(itemId, resolved);
            }
        }
    }

    private static void loadContainers() {
        ClassLoader loader = WeaponRegistry.class.getClassLoader();
        for (String fileName : ATTRIBUTE_FILES) {
            String path = "assets/bettercombat/weapon_attributes/" + fileName + ".json";
            try (InputStream in = loader.getResourceAsStream(path)) {
                if (in == null) {
                    continue;
                }
                JsonReader reader = new JsonReader(new InputStreamReader(in));
                AttributesContainer container = WeaponAttributesHelper.decode(reader);
                ResourceLocation id = new ResourceLocation("bettercombat", fileName);
                containers.put(id, container);
            } catch (Exception e) {
                System.err.println("[BetterCombat] Failed to parse: " + path);
                e.printStackTrace();
            }
        }
    }

    public static WeaponAttributes resolveAttributes(ResourceLocation itemId, AttributesContainer container) {
        try {
            List<WeaponAttributes> resolutionChain = new ArrayList<>();
            Set<AttributesContainer> visited = new HashSet<>();
            AttributesContainer current = container;
            while (current != null && visited.add(current)) {
                if (current.attributes() != null) {
                    resolutionChain.add(0, current.attributes());
                }
                if (current.parent() != null) {
                    ResourceLocation parentId = new ResourceLocation(current.parent());
                    AttributesContainer next = containers.get(parentId);
                    if (next == current) {
                        break;
                    }
                    current = next;
                } else {
                    current = null;
                }
            }

            WeaponAttributes resolved = null;
            for (WeaponAttributes attributes : resolutionChain) {
                if (resolved == null) {
                    resolved = attributes;
                } else {
                    resolved = WeaponAttributesHelper.override(resolved, attributes);
                }
            }
            return resolved;
        } catch (Exception e) {
            System.err.println("[BetterCombat] Failed to resolve attributes for: " + itemId);
            e.printStackTrace();
            return null;
        }
    }

    private static WeaponAttributes resolveFallback(Item item, ResourceLocation itemId) {
        String idStr = itemId.toString();
        if (PatternMatching.matches(idStr, fallbackConfig.blacklist_item_id_regex)) {
            return null;
        }

        for (FallbackConfig.CompatibilitySpecifier fallbackOption : fallbackConfig.fallback_compatibility) {
            if (PatternMatching.matches(idStr, fallbackOption.item_id_regex)) {
                ResourceLocation attrId = new ResourceLocation(fallbackOption.weapon_attributes);
                AttributesContainer container = containers.get(attrId);
                if (container != null) {
                    WeaponAttributes attributes = resolveAttributes(itemId, container);
                    if (attributes != null) {
                        registrations.put(itemId, attributes);
                        return attributes;
                    }
                }
            }
        }

        // Generic fallback by item class
        if (item instanceof ItemSword) {
            AttributesContainer swordContainer = containers.get(new ResourceLocation("bettercombat", "sword"));
            if (swordContainer != null) {
                WeaponAttributes attributes = resolveAttributes(itemId, swordContainer);
                if (attributes != null) {
                    registrations.put(itemId, attributes);
                    return attributes;
                }
            }
        } else if (item instanceof ItemAxe) {
            AttributesContainer axeContainer = containers.get(new ResourceLocation("bettercombat", "axe"));
            if (axeContainer != null) {
                WeaponAttributes attributes = resolveAttributes(itemId, axeContainer);
                if (attributes != null) {
                    registrations.put(itemId, attributes);
                    return attributes;
                }
            }
        }

        return null;
    }
}
