package com.voltyx.mwccf.immersiveui;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class ImmersiveUIConfig {
    public static Configuration config;

    // Hotbar
    public static boolean enableHotbarSelectorAnimation = true;
    public static double hotbarSelectorSpeed = 3.0D;
    public static boolean renderHotbarSelectorAboveItems = false;

    // Floating item
    public static boolean enableFloatingItemRotation = true;
    public static float floatingItemEasingSpeed = 0.75F;
    public static float floatingItemRotationAmplitude = 1.0F;
    public static float hoveredItemScale = 1.4F;

    // Inventory & Slots
    public static boolean enableMatchingItemHovering = true;
    public static float matchingItemHoverAmplitude = 0.8F;
    public static boolean enableVanillaSlotHighlighting = false;
    public static boolean enableRarityParticles = true;
    public static String[] customItemParticles = new String[] {
            "minecraft:nether_star=epic",
            "minecraft:golden_apple=rare"
    };
    public static String[] customClassParticles = new String[] {
            "Weapon=flame",
            "ItemSword=epic"
    };
    public static final java.util.Map<String, String> itemParticleOverrides = new java.util.HashMap<>();
    public static final java.util.Map<String, String> classParticleOverrides = new java.util.LinkedHashMap<>();
    private static final java.util.Map<Class<?>, String> classResolvedCache = new java.util.concurrent.ConcurrentHashMap<>();

    public static float particleSpeedMultiplier = 0.4F;
    public static int particleLifetimeMin = 6;
    public static int particleLifetimeMax = 12;
    public static int particleCount = 2;
    public static float particleSpawnRadius = 2.5F;
    public static float particleSpreadAngle = 10.0F;
    public static float particleWaveAmplitude = 1.0F;
    public static float particleWaveFrequency = 0.45F;
    public static float particleScale = 1.0F;
    public static boolean enableIdleParticles = true;
    public static float idleParticleChance = 0.25F;

    // Screen Shake
    public static boolean enableScreenShake = true;
    public static int shakeTimer = 8;
    public static float shakeAmplitude = 1.25F;

    // Enchant options
    public static boolean enableEnchantParticles = true;
    public static boolean enableCurseFormatting = true;

    // Advancement Toasts
    public static boolean enableAdvancementToastItems = true;

    // NeverEnoughAnimation features
    public static int openingAnimationTime = 90;
    public static com.voltyx.mwccf.immersiveui.nea.util.Interpolation openingAnimationCurve = com.voltyx.mwccf.immersiveui.nea.util.Interpolation.SINE_OUT;
    public static float openingStartScale = 0.9F;
    public static boolean animateDarkGuiBackground = true;

    public static int moveAnimationTime = 100;
    public static com.voltyx.mwccf.immersiveui.nea.util.Interpolation moveAnimationCurve = com.voltyx.mwccf.immersiveui.nea.util.Interpolation.SINE_OUT;

    public static int appearAnimationTime = 100;
    public static com.voltyx.mwccf.immersiveui.nea.util.Interpolation appearAnimationCurve = com.voltyx.mwccf.immersiveui.nea.util.Interpolation.SINE_OUT;

    public static String[] guiAnimationBlacklist = {};
    private static final java.util.Map<Class<?>, Boolean> blacklistCache = new java.util.HashMap<>();

    public static boolean isBlacklisted(Object screen) {
        if (screen == null) return true;
        Class<?> clazz = screen.getClass();
        if (blacklistCache.containsKey(clazz)) return blacklistCache.get(clazz);
        String name = clazz.getName();

        if (name.startsWith("gregtech.") || name.startsWith("com.creativemd.creativecore.")) {
            blacklistCache.put(clazz, true);
            return true;
        }

        for (String gui : guiAnimationBlacklist) {
            if (gui.endsWith("*")) {
                if (name.startsWith(gui.substring(0, gui.length() - 1))) {
                    blacklistCache.put(clazz, true);
                    return true;
                }
            } else if (name.equals(gui)) {
                blacklistCache.put(clazz, true);
                return true;
            }
        }
        blacklistCache.put(clazz, false);
        return false;
    }

    public static void init(File configDirectory) {
        File file = new File(configDirectory, "immersiveui.cfg");
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        String general = "general";
        String hotbar = "hotbar";
        String floating = "floating_item";
        String inventory = "inventory";
        String shake = "screen_shake";
        String enchant = "enchantment";
        String toasts = "toasts";
        String nea = "nea_animations";

        // Hotbar
        enableHotbarSelectorAnimation = config.getBoolean("enableHotbarSelectorAnimation", hotbar, true, "Enables the hotbar selector animation.");
        hotbarSelectorSpeed = config.getFloat("hotbarSelectorSpeed", hotbar, 3.0F, 0.1F, 20.0F, "Affects the speed of the hotbar selector.");
        renderHotbarSelectorAboveItems = config.getBoolean("renderHotbarSelectorAboveItems", hotbar, false, "Moves the hotbar selector above the items. You might want to disable this if you use resource packs that change the default selector texture.");

        // Floating item
        enableFloatingItemRotation = config.getBoolean("enableFloatingItemRotation", floating, true, "Enables floating item rotation.");
        floatingItemEasingSpeed = config.getFloat("floatingItemEasingSpeed", floating, 0.75F, 0.05F, 10.0F, "Affects the easing speed that is applied to the rotation of the floating item.");
        floatingItemRotationAmplitude = config.getFloat("floatingItemRotationAmplitude", floating, 1.0F, 0.1F, 5.0F, "Affects the rotation amplitude of the floating item.");
        hoveredItemScale = config.getFloat("hoveredItemScale", floating, 1.4F, 1.0F, 3.0F, "Affects the size of the hovered item.");

        // Inventory
        enableMatchingItemHovering = config.getBoolean("enableMatchingItemHovering", inventory, true, "Enables hovering of matching items.");
        matchingItemHoverAmplitude = config.getFloat("matchingItemHoverAmplitude", inventory, 0.8F, 0.0F, 5.0F, "Affects the hover amplitude of items that match to the item that is carried in the cursor.");
        enableVanillaSlotHighlighting = config.getBoolean("enableVanillaSlotHighlighting", inventory, false, "Enables vanilla slot highlighting.");
        enableRarityParticles = config.getBoolean("enableRarityParticles", inventory, true, "Enables particles for rare items and custom configured items.");

        customItemParticles = config.getStringList("customItemParticles", inventory, customItemParticles,
                "Custom particle types, particle class names, colors, or item crack textures per item registry ID.\n" +
                "Format: 'item_id = particle_or_item'. Examples: 'minecraft:diamond_sword=FlameParticleData', 'minecraft:nether_star=com.example.ParticleName', 'minecraft:golden_apple=minecraft:apple'.\n" +
                "Supported values on the right side:\n" +
                " - Particle Class Name: 'com.example.ParticleName', or simple names like 'FlameParticleData', 'GalacticParticleData', 'ItemCrackParticleData', 'GenericParticleData'\n" +
                " - Item ID for crack texture pieces: 'item', 'self', or another item ID like 'minecraft:apple'\n" +
                " - Predefined styles: 'flame' (fire), 'galactic' (enchant runes)\n" +
                " - Rarity names: 'epic' (purple), 'rare' (aqua), 'uncommon' (yellow), 'common' / 'white' (white)\n" +
                " - Colors: 'red', 'green', 'blue', 'gold' / 'orange'\n" +
                " - Hex color: '#RRGGBB' or '0xRRGGBB'");

        customClassParticles = config.getStringList("customClassParticles", inventory, customClassParticles,
                "Custom particles mapped by Item Java Class name. Matches simple name (e.g. 'Weapon', 'ItemSword'), full name (e.g. 'com.paneedah.weaponlib.Weapon'), or superclasses/interfaces.\n" +
                "Format: 'ItemClassName = particle_or_item'. Examples: 'Weapon=FlameParticleData', 'ItemSword=epic', 'ItemBow=galactic'.");

        particleSpeedMultiplier = config.getFloat("particleSpeedMultiplier", inventory, 0.4F, 0.01F, 5.0F, "Speed multiplier for GUI rarity/custom particles.");
        particleLifetimeMin = config.getInt("particleLifetimeMin", inventory, 6, 1, 100, "Minimum lifetime in ticks for GUI particles.");
        particleLifetimeMax = config.getInt("particleLifetimeMax", inventory, 12, 1, 100, "Maximum lifetime in ticks for GUI particles.");
        particleCount = config.getInt("particleCount", inventory, 2, 1, 20, "Number of particles spawned per emission burst. Increase for a richer, denser cloud/trail ('сплошь').");
        particleSpawnRadius = config.getFloat("particleSpawnRadius", inventory, 2.5F, 0.0F, 32.0F, "Spawn radius in pixels across the item icon where particles can spawn.");
        particleSpreadAngle = config.getFloat("particleSpreadAngle", inventory, 10.0F, 0.0F, 180.0F, "Cone dispersion/spread angle in degrees for particle velocity.");
        particleWaveAmplitude = config.getFloat("particleWaveAmplitude", inventory, 1.0F, 0.0F, 25.0F, "Wave amplitude (undulation strength) perpendicular to motion. Gives particles a flowing wave motion ('волна'). Set to 0 to disable.");
        particleWaveFrequency = config.getFloat("particleWaveFrequency", inventory, 0.45F, 0.01F, 2.0F, "Wave oscillation frequency / speed.");
        particleScale = config.getFloat("particleScale", inventory, 1.0F, 0.1F, 5.0F, "Overall size/scale multiplier for GUI particles.");
        enableIdleParticles = config.getBoolean("enableIdleParticles", inventory, true, "Enables subtle floating particles while holding or hovering an item, even when the cursor is not moving.");
        idleParticleChance = config.getFloat("idleParticleChance", inventory, 0.25F, 0.0F, 1.0F, "Chance per frame to emit gentle particles when holding still.");

        itemParticleOverrides.clear();
        classParticleOverrides.clear();
        classResolvedCache.clear();

        for (String entry : customClassParticles) {
            parseParticleEntry(entry, classParticleOverrides, true);
        }

        for (String entry : customItemParticles) {
            if (entry == null || entry.trim().isEmpty()) continue;
            String trimmed = entry.trim();
            int sepIndex = trimmed.indexOf('=');
            if (sepIndex < 0) sepIndex = trimmed.indexOf(" - ");
            if (sepIndex < 0) sepIndex = trimmed.indexOf('-');
            String keyPart = sepIndex > 0 ? trimmed.substring(0, sepIndex).trim() : "";
            if (keyPart.toLowerCase().startsWith("class:") || (!keyPart.contains(":") && (keyPart.contains(".") || (keyPart.length() > 0 && Character.isUpperCase(keyPart.charAt(0)))))) {
                parseParticleEntry(entry, classParticleOverrides, true);
            } else {
                parseParticleEntry(entry, itemParticleOverrides, false);
            }
        }

        // Screen Shake
        enableScreenShake = config.getBoolean("enableScreenShake", shake, true, "Enables screen shake.");
        shakeTimer = config.getInt("shakeTimer", shake, 8, 1, 60, "Screen shake timer in ticks.");
        shakeAmplitude = config.getFloat("shakeAmplitude", shake, 1.25F, 0.1F, 10.0F, "Screen shake amplitude.");

        // Enchant Options
        enableEnchantParticles = config.getBoolean("enableEnchantParticles", enchant, true, "Enables particles in the enchant(ing/ment) table.");
        enableCurseFormatting = config.getBoolean("enableCurseFormatting", enchant, true, "Enables curse formatting.");

        // Advancement Toasts
        enableAdvancementToastItems = config.getBoolean("enableAdvancementToastItems", toasts, true, "Enables wobbly items in advancement toasts.");

        // NEA Animations
        openingAnimationTime = config.getInt("openingAnimationTime", nea, 90, 0, 500, "How many milliseconds it takes until the GUI is fully opened. 0 to disable.");
        openingStartScale = config.getFloat("openingStartScale", nea, 0.9F, 0.0F, 1.0F, "The scale at which the opening animation starts.");
        animateDarkGuiBackground = config.getBoolean("animateDarkGuiBackground", nea, true, "If the dark background should be animated too during opening animation.");
        moveAnimationTime = config.getInt("moveAnimationTime", nea, 100, 0, 500, "How many milliseconds it takes until an item has moved to its target on shift click. 0 to disable.");
        appearAnimationTime = config.getInt("appearAnimationTime", nea, 100, 0, 500, "How many milliseconds it takes for item (dis)appear / throw animation. 0 to disable.");
        guiAnimationBlacklist = config.getStringList("guiAnimationBlacklist", nea, new String[0], "Add class names (works with * at the end) which should be blacklisted from animations.");
        blacklistCache.clear();

        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void parseParticleEntry(String entry, java.util.Map<String, String> targetMap, boolean isClassMap) {
        if (entry == null || entry.trim().isEmpty()) return;
        String trimmed = entry.trim();
        String id = null;
        String particle = null;

        if (trimmed.contains("=")) {
            String[] parts = trimmed.split("=", 2);
            id = parts[0].trim();
            particle = parts[1].trim().toLowerCase();
        } else if (trimmed.contains(" - ")) {
            String[] parts = trimmed.split(" - ", 2);
            id = parts[0].trim();
            particle = parts[1].trim().toLowerCase();
        } else if (trimmed.contains("-")) {
            String[] parts = trimmed.split("-", 2);
            id = parts[0].trim();
            particle = parts[1].trim().toLowerCase();
        } else if (trimmed.contains(":")) {
            int lastColon = trimmed.lastIndexOf(':');
            int firstColon = trimmed.indexOf(':');
            if (lastColon > firstColon) {
                id = trimmed.substring(0, lastColon).trim();
                particle = trimmed.substring(lastColon + 1).trim().toLowerCase();
            }
        }

        if (id != null && particle != null && !id.isEmpty() && !particle.isEmpty()) {
            if (id.toLowerCase().startsWith("class:")) {
                id = id.substring(6).trim();
            }
            if (isClassMap) {
                targetMap.put(id, particle);
            } else {
                targetMap.put(id.toLowerCase(), particle);
            }
        }
    }

    public static String getParticleOverride(net.minecraft.item.ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.getItem() == null) return null;

        // 1. Direct registry ID match
        if (stack.getItem().getRegistryName() != null) {
            String fullId = stack.getItem().getRegistryName().toString().toLowerCase();
            String res = itemParticleOverrides.get(fullId);
            if (res != null) return res;
            String path = stack.getItem().getRegistryName().getPath().toLowerCase();
            res = itemParticleOverrides.get(path);
            if (res != null) return res;
        }

        // 2. Class-based match
        Class<?> itemClass = stack.getItem().getClass();
        String cached = classResolvedCache.get(itemClass);
        if (cached != null) {
            return cached.isEmpty() ? null : cached;
        }

        String matched = resolveClassParticle(itemClass);
        classResolvedCache.put(itemClass, matched != null ? matched : "");
        return matched;
    }

    private static String resolveClassParticle(Class<?> itemClass) {
        if (classParticleOverrides.isEmpty()) return null;

        java.util.List<Class<?>> hierarchy = new java.util.ArrayList<>();
        Class<?> curr = itemClass;
        while (curr != null && curr != Object.class) {
            hierarchy.add(curr);
            for (Class<?> iface : curr.getInterfaces()) {
                if (!hierarchy.contains(iface)) hierarchy.add(iface);
            }
            curr = curr.getSuperclass();
        }

        for (java.util.Map.Entry<String, String> entry : classParticleOverrides.entrySet()) {
            String pattern = entry.getKey();
            String particle = entry.getValue();

            for (Class<?> c : hierarchy) {
                String simple = c.getSimpleName();
                String full = c.getName();

                if (simple.equalsIgnoreCase(pattern) || full.equalsIgnoreCase(pattern)) {
                    return particle;
                }
                if (pattern.endsWith("*")) {
                    String prefix = pattern.substring(0, pattern.length() - 1).toLowerCase();
                    if (simple.toLowerCase().startsWith(prefix) || full.toLowerCase().startsWith(prefix)) {
                        return particle;
                    }
                }
            }
        }
        return null;
    }
}
