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
    public static final java.util.Map<String, String> itemParticleOverrides = new java.util.HashMap<>();
    public static float particleSpeedMultiplier = 0.4F;
    public static int particleLifetimeMin = 5;
    public static int particleLifetimeMax = 10;

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
        enableRarityParticles = config.getBoolean("enableRarityParticles", inventory, true, "Enables particles for rare items.");
        customItemParticles = config.getStringList("customItemParticles", inventory, customItemParticles,
                "Custom particle types, colors, or item crack textures per item ID. Format: 'modid:item_name=particle_or_item', or 'modid:item_name - particle_or_item'.\n" +
                "Supported particle values:\n" +
                " - Item ID: 'item', 'self', or another item ID like 'minecraft:apple' (spawns flying texture pieces of the item)\n" +
                " - Rarity names: 'epic' (purple), 'rare' (aqua), 'uncommon' (yellow), 'common' / 'white' (white)\n" +
                " - Predefined styles: 'flame' (fire), 'galactic' (enchant runes)\n" +
                " - Hex color: '#RRGGBB' or '0xRRGGBB' (e.g. '#FF0000' for red, '#00FF00' for green)");
        particleSpeedMultiplier = config.getFloat("particleSpeedMultiplier", inventory, 0.4F, 0.01F, 5.0F, "Speed multiplier for GUI rarity/custom particles.");
        particleLifetimeMin = config.getInt("particleLifetimeMin", inventory, 5, 1, 100, "Minimum lifetime in ticks for GUI particles (controls travel distance).");
        particleLifetimeMax = config.getInt("particleLifetimeMax", inventory, 10, 1, 100, "Maximum lifetime in ticks for GUI particles (controls travel distance).");

        itemParticleOverrides.clear();
        for (String entry : customItemParticles) {
            if (entry == null || entry.trim().isEmpty()) continue;
            String trimmed = entry.trim();
            String id = null;
            String particle = null;
            if (trimmed.contains("=")) {
                String[] parts = trimmed.split("=", 2);
                id = parts[0].trim().toLowerCase();
                particle = parts[1].trim().toLowerCase();
            } else if (trimmed.contains(" - ")) {
                String[] parts = trimmed.split(" - ", 2);
                id = parts[0].trim().toLowerCase();
                particle = parts[1].trim().toLowerCase();
            } else if (trimmed.contains("-")) {
                String[] parts = trimmed.split("-", 2);
                id = parts[0].trim().toLowerCase();
                particle = parts[1].trim().toLowerCase();
            } else if (trimmed.contains(":")) {
                int lastColon = trimmed.lastIndexOf(':');
                int firstColon = trimmed.indexOf(':');
                if (lastColon > firstColon) {
                    id = trimmed.substring(0, lastColon).trim().toLowerCase();
                    particle = trimmed.substring(lastColon + 1).trim().toLowerCase();
                }
            }
            if (id != null && particle != null && !id.isEmpty() && !particle.isEmpty()) {
                itemParticleOverrides.put(id, particle);
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
}
