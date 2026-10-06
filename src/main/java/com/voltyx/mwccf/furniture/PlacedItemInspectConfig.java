package com.voltyx.mwccf.furniture;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class PlacedItemInspectConfig {

    public static Configuration config;
    public static boolean enabled = true;
    public static String[] autoInspectItems = new String[] {
            "mwccf:doll",
            "efw:doll",
            "mwccf:bloody_necklace",
            "efw:bloody_necklace"
    };

    private static final Set<String> itemSet = new HashSet<>();

    public static void init(File configDirectory) {
        File file = new File(configDirectory, "placed_item_inspect.cfg");
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        if (config == null) return;
        config.load();

        enabled = config.getBoolean("enabled", Configuration.CATEGORY_GENERAL, true,
                "Enable automatic inspect GUI when picking up placed items");
        autoInspectItems = config.getStringList("autoInspectItems", Configuration.CATEGORY_GENERAL,
                new String[] {
                        "mwccf:doll",
                        "efw:doll",
                        "mwccf:bloody_necklace",
                        "efw:bloody_necklace"
                },
                "Registry names of items that trigger the inspect GUI when picked up as a placed item.");

        itemSet.clear();
        for (String s : autoInspectItems) {
            if (s != null && !s.trim().isEmpty()) {
                itemSet.add(s.trim().toLowerCase());
            }
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static boolean isAutoInspect(ItemStack stack) {
        if (!enabled || stack == null || stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (item == null) return false;

        ResourceLocation reg = item.getRegistryName();
        if (reg != null) {
            String full = reg.toString().toLowerCase();
            String path = reg.getPath().toLowerCase();
            if (itemSet.contains(full) || itemSet.contains(path)) {
                return true;
            }
        }

        // Direct class name check as safety fallback
        String className = item.getClass().getName();
        if (className.contains("ItemDoll") || className.contains("ItemBloodyNecklace")) {
            return true;
        }

        return false;
    }
}
