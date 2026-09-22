package com.voltyx.mwccf.si;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class SIItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    // --- Materials & Ammo ---
    public static final Item STEELLIUM = createMaterial("steellium", CreativeTabs.MATERIALS);
    public static final Item STEELLIUM_NUGGET = createMaterial("steellium_nugget", CreativeTabs.MATERIALS);
    public static final Item RAW_STEELLIUM = createMaterial("raw_steellium", CreativeTabs.MATERIALS);
    public static final Item ALUMINIUM = createMaterial("aluminium", CreativeTabs.MATERIALS);
    public static final Item ELECTRONIC_SCRAP = createMaterial("electronic_scrap", CreativeTabs.MATERIALS);
    public static final Item GASOLINE_CAN = createMaterial("gasoline_can", CreativeTabs.MATERIALS);
    public static final Item BATTERIES = createMaterial("batteries", CreativeTabs.MATERIALS);
    public static final Item NAIL = createMaterial("nail", CreativeTabs.COMBAT);
    public static final Item NAILS_BOX = createMaterial("nails_box", CreativeTabs.COMBAT);

    // --- 1. Fists ---
    public static final Item AMERICAN_FIST = register(new ItemSIFist("american_fist", SIMaterials.TOOL_STEELLIUM, 310, 5.5F, 2.5F, false, null, () -> Items.IRON_INGOT));
    public static final Item ELECTRIC_FIST = register(new ItemSIFist("electric_fist", SIMaterials.TOOL_STEELLIUM, 412, 7.0F, 2.0F, true, SISounds.ELECTRIC_FIST_01, () -> BATTERIES));

    // --- 2. Knives & Improvised Sharp ---
    public static final Item HUNT_KNIFE = register(new ItemSISword("hunt_knife", SIMaterials.TOOL_STEELLIUM, 1000, 7.0F, 2.0F, 0, false, null, () -> STEELLIUM));
    public static final Item TACTICAL_KNIFE = register(new ItemSISword("tactical_knife", SIMaterials.TOOL_STEELLIUM, 1000, 7.0F, 2.0F, 0, false, null, () -> STEELLIUM));
    public static final Item KITCHEN_KNIFE = register(new ItemSISword("kitchen_knife", SIMaterials.TOOL_STEELLIUM, 200, 5.0F, 2.5F, 0, false, null, () -> Items.IRON_INGOT));
    public static final Item BROKEN_BOTTLE_SWORD = register(new ItemSISword("broken_bottle_sword", SIMaterials.TOOL_GLASS, 32, 4.5F, 2.5F, 1200, false, null, () -> Item.getItemFromBlock(Blocks.GLASS)));
    public static final Item SCREWDRIVER_RAPIER = register(new ItemSISword("screwdriver_rapier", SIMaterials.TOOL_STEELLIUM, 221, 4.0F, 2.5F, 0, false, null, () -> STEELLIUM));

    // --- 3. Swords & Blades ---
    public static final Item KATANA = register(new ItemSISword("katana", SIMaterials.TOOL_STEELLIUM, 300, 8.0F, 2.0F, 1200, false, null, () -> STEELLIUM));
    public static final Item MACHETE = register(new ItemSISword("machete", SIMaterials.TOOL_STEELLIUM, 250, 6.0F, 1.8F, 0, false, null, () -> Items.IRON_INGOT));
    public static final Item STEELLIUM_SWORD = register(new ItemSISword("steellium_sword", SIMaterials.TOOL_STEELLIUM, 821, 6.5F, 1.6F, 0, false, null, () -> STEELLIUM));
    public static final Item ALUMINIUM_SWORD = register(new ItemSISword("aluminium_sword", SIMaterials.TOOL_ALUMINIUM, 194, 6.0F, 1.65F, 0, false, null, () -> ALUMINIUM));
    public static final Item PLANK_SWORD = register(new ItemSISword("plank_sword", SIMaterials.TOOL_WOOD_PLANK, 70, 4.5F, 1.2F, 0, false, null, () -> Item.getItemFromBlock(Blocks.PLANKS)));
    public static final Item PLANK_WITH_NAILS_SWORD = register(new ItemSISword("plank_with_nails_sword", SIMaterials.TOOL_WOOD_PLANK, 52, 6.5F, 1.0F, 1200, false, null, () -> Item.getItemFromBlock(Blocks.PLANKS)));

    // --- 4. Clubs & Bats ---
    public static final Item BASEBALL_BAT_SWORD = register(new ItemSISword("baseball_bat_sword", SIMaterials.TOOL_WOOD_PLANK, 80, 5.0F, 1.8F, 0, false, null, () -> Item.getItemFromBlock(Blocks.PLANKS)));
    public static final Item BASEBALL_BAT_WITH_NAILS_SWORD = register(new ItemSISword("b_aseball_bat_with_nails_sword", SIMaterials.TOOL_WOOD_PLANK, 65, 6.0F, 1.6F, 320, false, null, () -> Item.getItemFromBlock(Blocks.PLANKS)));
    public static final Item METAL_BASEBALLBAT_HAMMER = register(new ItemSISword("metal_baseballbat_hammer", SIMaterials.TOOL_STEELLIUM, 341, 6.5F, 1.7F, 0, false, null, () -> STEELLIUM));
    public static final Item POLICE_BATON_MACE = register(new ItemSISword("police_baton_mace", SIMaterials.TOOL_STEELLIUM, 342, 6.0F, 2.0F, 0, false, null, () -> Items.IRON_INGOT));
    public static final Item ELECTRIC_BATON_SWORD = register(new ItemSISword("electric_baton_sword", SIMaterials.TOOL_STEELLIUM, 321, 6.4F, 1.6F, 0, true, SISounds.ELECTRIC_FIST_01, () -> BATTERIES));

    // --- 5. Hammers & Tools ---
    public static final Item HAND_HAMMER = register(new ItemSIPickaxe("hand_hammer", SIMaterials.TOOL_STEELLIUM, 232, 6.5F, 1.6F, true, null, () -> STEELLIUM));
    public static final Item NAILS_HAMMER = register(new ItemSIPickaxe("nails_hammer", SIMaterials.TOOL_STEELLIUM, 232, 5.6F, 1.8F, true, null, () -> STEELLIUM));
    public static final Item BRICK_HAMMER = register(new ItemSIPickaxe("brick_hammer", SIMaterials.TOOL_WOOD_PLANK, 61, 6.3F, 1.3F, true, null, () -> Items.BRICK));
    public static final Item SLEDGE_GREAT_HAMMER = register(new ItemSIPickaxe("sledge_great_hammer", SIMaterials.TOOL_STEELLIUM, 1241, 11.0F, 0.8F, true, null, () -> STEELLIUM));
    public static final Item LEVER_MACE = register(new ItemSIPickaxe("lever_mace", SIMaterials.TOOL_STEELLIUM, 670, 6.5F, 1.8F, false, SISounds.CROWBAR_SLAM, () -> STEELLIUM));
    public static final Item STEELLIUM_PIPE_MACE = register(new ItemSIPickaxe("steellium_pipe_mace", SIMaterials.TOOL_STEELLIUM, 320, 6.2F, 1.2F, false, SISounds.PIPE_SLAM, () -> STEELLIUM));
    public static final Item PIPE_WRENCH_MACE = register(new ItemSIPickaxe("pipe_wrench_mace", SIMaterials.TOOL_STEELLIUM, 230, 5.0F, 2.0F, false, null, () -> STEELLIUM));
    public static final Item WRENCH_MACE = register(new ItemSIPickaxe("wrench_mace", SIMaterials.TOOL_STEELLIUM, 320, 5.0F, 2.0F, false, null, () -> STEELLIUM));

    // --- 6. Axes & Greataxes ---
    public static final Item HAND_AXE = register(new ItemSIAxe("hand_axe", SIMaterials.TOOL_STEELLIUM, 232, 6.0F, 1.75F, 0, null, () -> STEELLIUM));
    public static final Item FIRE_AXE = register(new ItemSIAxe("fire_axe", SIMaterials.TOOL_STEELLIUM, 800, 10.0F, 1.0F, 0, null, () -> STEELLIUM));
    public static final Item SAW_AXE = register(new ItemSIAxe("saw_axe", SIMaterials.TOOL_STEELLIUM, 210, 7.4F, 1.4F, 0, null, () -> STEELLIUM));
    public static final Item STELLIUM_AXE = register(new ItemSIAxe("stellium_axe", SIMaterials.TOOL_STEELLIUM, 821, 9.0F, 1.0F, 0, null, () -> STEELLIUM));
    public static final Item ALUMINIUM_AXE = register(new ItemSIAxe("aluminium_axe", SIMaterials.TOOL_ALUMINIUM, 194, 8.0F, 1.05F, 0, null, () -> ALUMINIUM));
    public static final Item GREATAXE_STOP_SIGN = register(new ItemSIAxe("greataxe_stop_sign", SIMaterials.TOOL_ALUMINIUM, 210, 7.0F, 1.0F, 0, null, () -> ALUMINIUM));
    public static final Item GREATAXE_STOP_SING_BROKEN = register(new ItemSIAxe("greataxe_stop_sing_broken", SIMaterials.TOOL_ALUMINIUM, 120, 8.0F, 1.2F, 0, null, () -> ALUMINIUM));
    public static final Item ELECTRIC_GUITAR_GREATAXE = register(new ItemSIAxe("electric_guitar_greataxe", SIMaterials.TOOL_WOOD_PLANK, 100, 6.0F, 1.2F, 0, SISounds.ELECTRIC_GUITAR_SMASH, () -> ELECTRONIC_SCRAP));

    // --- 7. Polearms & Glaives ---
    public static final Item LANCE_PITCHFORK = register(new ItemSIPolearm("lance_pitchfork", SIMaterials.TOOL_STEELLIUM, 321, 6.8F, 1.2F, 0.5D, 0, null, () -> STEELLIUM));
    public static final Item LANCE_WITH_KNIFE = register(new ItemSIPolearm("lance_with_knife", SIMaterials.TOOL_STEELLIUM, 160, 6.5F, 1.4F, 0.5D, 0, null, () -> Items.IRON_INGOT));
    public static final Item LANCE_WITH_BROKEN_BOTTLE = register(new ItemSIPolearm("lance_with_broken_bottle", SIMaterials.TOOL_GLASS, 42, 6.0F, 1.8F, 0.5D, 1200, null, () -> null));
    public static final Item LANCE_WIT_SCREWDRIVER = register(new ItemSIPolearm("lance_wit_screwdriver", SIMaterials.TOOL_STEELLIUM, 196, 5.5F, 1.6F, 0.5D, 0, null, () -> STEELLIUM));
    public static final Item GLAIVE_WITH_MACHETE = register(new ItemSIPolearm("glaive_with_machete", SIMaterials.TOOL_STEELLIUM, 200, 7.5F, 1.4F, 0.5D, 0, null, () -> Items.IRON_INGOT));
    public static final Item GLAIVE_CHAINSAW = register(new ItemSIPolearm("glaive_chainsaw", SIMaterials.TOOL_STEELLIUM, 230, 14.0F, 0.7F, 0.75D, 1200, SISounds.CHAINSAW_SWING, () -> GASOLINE_CAN));
    public static final Item GLAIVE_CIRCULAR_SAW = register(new ItemSIPolearm("glaive_circular_saw", SIMaterials.TOOL_STEELLIUM, 190, 10.0F, 0.9F, 0.75D, 1200, SISounds.CIRCULARSAW_SWING, () -> BATTERIES));

    // --- 8. Sickle ---
    public static final Item SICKLE = register(new ItemSISword("sickle", SIMaterials.TOOL_STEELLIUM, 321, 6.0F, 1.8F, 0, false, null, () -> STEELLIUM));

    // --- 9. Shields ---
    public static final Item SWAT_SHIELD = register(new ItemSIShield("swat_shield", 641, () -> STEELLIUM));
    public static final Item STOP_SING_SHIELD = register(new ItemSIShield("stop_sing_shield", 410, () -> ALUMINIUM));

    // --- 10. Ranged ---
    public static final Item NAILGUN = register(new ItemSINailgun("nailgun"));

    private static Item createMaterial(String name, CreativeTabs tab) {
        Item item = new Item().setRegistryName("mwccf", name)
                .setTranslationKey("mwccf." + name)
                .setCreativeTab(tab);
        return register(item);
    }

    private static <T extends Item> T register(T item) {
        ITEMS.add(item);
        return item;
    }
}
