package com.voltyx.mwccf.mcore;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;

import java.util.ArrayList;
import java.util.List;

public class MCoreItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    // Base Items
    public static final Item STEEL_INGOT = createItem("steel_ingot");
    public static final Item STEEL_SHEET = createItem("steel_sheet");
    public static final Item STEEL_NUGGET = createItem("steel_nugget");
    public static final Item STEEL_SCRAP = createItem("steel_scrap");

    public static final Item TITANIUM_INGOT = createItem("titanium_ingot");
    public static final Item TITANIUM_SHEET = createItem("titanium_sheet");
    public static final Item TITANIUM_NUGGET = createItem("titanium_nugget");
    public static final Item RAW_TITANIUM = createItem("raw_titanium");

    // Steel Tools
    public static final Item STEEL_SWORD = createSword("steel_sword", MCoreMaterials.TOOL_STEEL);
    public static final Item STEEL_PICKAXE = createPickaxe("steel_pickaxe", MCoreMaterials.TOOL_STEEL);
    public static final Item STEEL_AXE = createAxe("steel_axe", MCoreMaterials.TOOL_STEEL, 8.0F, -3.1F);
    public static final Item STEEL_SHOVEL = createSpade("steel_shovel", MCoreMaterials.TOOL_STEEL);
    public static final Item STEEL_HOE = createHoe("steel_hoe", MCoreMaterials.TOOL_STEEL);

    // Titanium Tools
    public static final Item TITANIUM_SWORD = createSword("titanium_sword", MCoreMaterials.TOOL_TITANIUM);
    public static final Item TITANIUM_PICKAXE = createPickaxe("titanium_pickaxe", MCoreMaterials.TOOL_TITANIUM);
    public static final Item TITANIUM_AXE = createAxe("titanium_axe", MCoreMaterials.TOOL_TITANIUM, 8.0F, -3.0F);
    public static final Item TITANIUM_SHOVEL = createSpade("titanium_shovel", MCoreMaterials.TOOL_TITANIUM);
    public static final Item TITANIUM_HOE = createHoe("titanium_hoe", MCoreMaterials.TOOL_TITANIUM);

    // Steel Armor
    public static final Item STEEL_HELMET = createArmor("steel_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD);
    public static final Item STEEL_CHESTPLATE = createArmor("steel_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST);
    public static final Item STEEL_LEGGINGS = createArmor("steel_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS);
    public static final Item STEEL_BOOTS = createArmor("steel_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET);

    // Titanium Armor
    public static final Item TITANIUM_HELMET = createArmor("titanium_helmet", MCoreMaterials.ARMOR_TITANIUM, 1, EntityEquipmentSlot.HEAD);
    public static final Item TITANIUM_CHESTPLATE = createArmor("titanium_chestplate", MCoreMaterials.ARMOR_TITANIUM, 1, EntityEquipmentSlot.CHEST);
    public static final Item TITANIUM_LEGGINGS = createArmor("titanium_leggings", MCoreMaterials.ARMOR_TITANIUM, 2, EntityEquipmentSlot.LEGS);
    public static final Item TITANIUM_BOOTS = createArmor("titanium_boots", MCoreMaterials.ARMOR_TITANIUM, 1, EntityEquipmentSlot.FEET);

    public static final Item WINTER_MILITARY_HELMET = createGeoArmor("winter_military_armor_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "winter_military_armor", "winter_military_armor");
    public static final Item WINTER_MILITARY_CHESTPLATE = createGeoArmor("winter_military_armor_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "winter_military_armor", "winter_military_armor");
    public static final Item WINTER_MILITARY_LEGGINGS = createGeoArmor("winter_military_armor_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "winter_military_armor", "winter_military_armor");
    public static final Item WINTER_MILITARY_BOOTS = createGeoArmor("winter_military_armor_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "winter_military_armor", "winter_military_armor");

    public static final Item ACACIA_GHILLIE_BOOTS = createGeoArmor("acacia_ghillie_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "acacia_ghillie_armor", "ghillie_armor");
    public static final Item ACACIA_GHILLIE_CHESTPLATE = createGeoArmor("acacia_ghillie_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "acacia_ghillie_armor", "ghillie_armor");
    public static final Item ACACIA_GHILLIE_HELMET = createGeoArmor("acacia_ghillie_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "acacia_ghillie_armor", "ghillie_armor");
    public static final Item ACACIA_GHILLIE_LEGGINGS = createGeoArmor("acacia_ghillie_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "acacia_ghillie_armor", "ghillie_armor");
    public static final Item AZALEA_GHILLIE_BOOTS = createGeoArmor("azalea_ghillie_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "azalea_ghillie_armor", "ghillie_armor");
    public static final Item AZALEA_GHILLIE_CHESTPLATE = createGeoArmor("azalea_ghillie_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "azalea_ghillie_armor", "ghillie_armor");
    public static final Item AZALEA_GHILLIE_HELMET = createGeoArmor("azalea_ghillie_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "azalea_ghillie_armor", "ghillie_armor");
    public static final Item AZALEA_GHILLIE_LEGGINGS = createGeoArmor("azalea_ghillie_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "azalea_ghillie_armor", "ghillie_armor");
    public static final Item BIRCH_GHILLIE_BOOTS = createGeoArmor("birch_ghillie_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "birch_ghillie_armor", "ghillie_armor");
    public static final Item BIRCH_GHILLIE_CHESTPLATE = createGeoArmor("birch_ghillie_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "birch_ghillie_armor", "ghillie_armor");
    public static final Item BIRCH_GHILLIE_HELMET = createGeoArmor("birch_ghillie_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "birch_ghillie_armor", "ghillie_armor");
    public static final Item BIRCH_GHILLIE_LEGGINGS = createGeoArmor("birch_ghillie_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "birch_ghillie_armor", "ghillie_armor");
    public static final Item BLACK_GP5_GAS_MASK = createGeoArmor("black_gp5_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "black_gp5", "black_gp5");
    public static final Item BLACK_JUGGERNAUT_ARMOR_BOOTS = createGeoArmor("black_juggernaut_armor_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "black_juggernaut_armor", "juggernaut_armor");
    public static final Item BLACK_JUGGERNAUT_ARMOR_CHESTPLATE = createGeoArmor("black_juggernaut_armor_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "black_juggernaut_armor", "juggernaut_armor");
    public static final Item BLACK_JUGGERNAUT_ARMOR_HELMET = createGeoArmor("black_juggernaut_armor_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "black_juggernaut_armor", "juggernaut_armor");
    public static final Item BLACK_JUGGERNAUT_ARMOR_LEGGINGS = createGeoArmor("black_juggernaut_armor_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "black_juggernaut_armor", "juggernaut_armor");
    public static final Item BLACK_MILITARY_BERET = createGeoArmor("black_military_beret", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "black_military_beret", "military_beret");
    public static final Item BLACK_PLATE_CARRIER_HEAVY = createGeoArmor("black_plate_carrier_heavy", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "black_plate_carrier", "black_plate_carrier");
    public static final Item BLACK_PLATE_CARRIER_LIGHT = createGeoArmor("black_plate_carrier_light", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "black_plate_carrier", "black_plate_carrier");
    public static final Item CHERRY_GHILLIE_BOOTS = createGeoArmor("cherry_ghillie_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "cherry_ghillie_armor", "ghillie_armor");
    public static final Item CHERRY_GHILLIE_CHESTPLATE = createGeoArmor("cherry_ghillie_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "cherry_ghillie_armor", "ghillie_armor");
    public static final Item CHERRY_GHILLIE_HELMET = createGeoArmor("cherry_ghillie_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "cherry_ghillie_armor", "ghillie_armor");
    public static final Item CHERRY_GHILLIE_LEGGINGS = createGeoArmor("cherry_ghillie_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "cherry_ghillie_armor", "ghillie_armor");
    public static final Item CM6M_GAS_MASK = createGeoArmor("cm6m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "cm6m", "cm6m");
    public static final Item CM7M_GAS_MASK = createGeoArmor("cm7m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "cm7m", "cm7m");
    public static final Item CM8M_GAS_MASK = createGeoArmor("cm8m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "cm8m", "cm8m");
    public static final Item COMBAT_HELMET = createGeoArmor("combat_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "combat_helmet", "combat_helmet");
    public static final Item DARK_OAK_GHILLIE_BOOTS = createGeoArmor("dark_oak_ghillie_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "dark_oak_ghillie_armor", "ghillie_armor");
    public static final Item DARK_OAK_GHILLIE_CHESTPLATE = createGeoArmor("dark_oak_ghillie_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "dark_oak_ghillie_armor", "ghillie_armor");
    public static final Item DARK_OAK_GHILLIE_HELMET = createGeoArmor("dark_oak_ghillie_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "dark_oak_ghillie_armor", "ghillie_armor");
    public static final Item DARK_OAK_GHILLIE_LEGGINGS = createGeoArmor("dark_oak_ghillie_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "dark_oak_ghillie_armor", "ghillie_armor");
    public static final Item DESERT_MILITARY_ARMOR_BOOTS = createGeoArmor("desert_military_armor_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "desert_military_armor", "military_armor");
    public static final Item DESERT_MILITARY_ARMOR_CHESTPLATE = createGeoArmor("desert_military_armor_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "desert_military_armor", "military_armor");
    public static final Item DESERT_MILITARY_ARMOR_HELMET = createGeoArmor("desert_military_armor_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "desert_military_armor", "military_armor");
    public static final Item DESERT_MILITARY_ARMOR_LEGGINGS = createGeoArmor("desert_military_armor_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "desert_military_armor", "military_armor");
    public static final Item HAZMAT_ARMOR_BOOTS = createGeoArmor("hazmat_armor_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "hazmat_armor", "hazmat_armor");
    public static final Item HAZMAT_ARMOR_CHESTPLATE = createGeoArmor("hazmat_armor_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "hazmat_armor", "hazmat_armor");
    public static final Item HAZMAT_ARMOR_HELMET = createGeoArmor("hazmat_armor_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "hazmat_armor", "hazmat_armor");
    public static final Item HAZMAT_ARMOR_LEGGINGS = createGeoArmor("hazmat_armor_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "hazmat_armor", "hazmat_armor");
    public static final Item HELMET_CM6M_GAS_MASK = createGeoArmor("helmet_cm6m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "helmet_cm6m", "helmet_cm6m");
    public static final Item HELMET_CM7M_GAS_MASK = createGeoArmor("helmet_cm7m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "helmet_cm7m", "helmet_cm7m");
    public static final Item HELMET_CM8M_GAS_MASK = createGeoArmor("helmet_cm8m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "helmet_cm8m", "helmet_cm8m");
    public static final Item JUNGLE_GHILLIE_BOOTS = createGeoArmor("jungle_ghillie_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "jungle_ghillie_armor", "ghillie_armor");
    public static final Item JUNGLE_GHILLIE_CHESTPLATE = createGeoArmor("jungle_ghillie_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "jungle_ghillie_armor", "ghillie_armor");
    public static final Item JUNGLE_GHILLIE_HELMET = createGeoArmor("jungle_ghillie_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "jungle_ghillie_armor", "ghillie_armor");
    public static final Item JUNGLE_GHILLIE_LEGGINGS = createGeoArmor("jungle_ghillie_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "jungle_ghillie_armor", "ghillie_armor");
    public static final Item MANGROVE_GHILLIE_BOOTS = createGeoArmor("mangrove_ghillie_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "mangrove_ghillie_armor", "ghillie_armor");
    public static final Item MANGROVE_GHILLIE_CHESTPLATE = createGeoArmor("mangrove_ghillie_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "mangrove_ghillie_armor", "ghillie_armor");
    public static final Item MANGROVE_GHILLIE_HELMET = createGeoArmor("mangrove_ghillie_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "mangrove_ghillie_armor", "ghillie_armor");
    public static final Item MANGROVE_GHILLIE_LEGGINGS = createGeoArmor("mangrove_ghillie_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "mangrove_ghillie_armor", "ghillie_armor");
    public static final Item OAK_GHILLIE_BOOTS = createGeoArmor("oak_ghillie_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "oak_ghillie_armor", "ghillie_armor");
    public static final Item OAK_GHILLIE_CHESTPLATE = createGeoArmor("oak_ghillie_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "oak_ghillie_armor", "ghillie_armor");
    public static final Item OAK_GHILLIE_HELMET = createGeoArmor("oak_ghillie_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "oak_ghillie_armor", "ghillie_armor");
    public static final Item OAK_GHILLIE_LEGGINGS = createGeoArmor("oak_ghillie_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "oak_ghillie_armor", "ghillie_armor");
    public static final Item OLIVE_COMBAT_HELMET = createGeoArmor("olive_combat_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "olive_combat_helmet", "combat_helmet");
    public static final Item OLIVE_HELMET_CM6M_GAS_MASK = createGeoArmor("olive_helmet_cm6m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "olive_helmet_cm6m", "helmet_cm6m");
    public static final Item OLIVE_HELMET_CM7M_GAS_MASK = createGeoArmor("olive_helmet_cm7m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "olive_helmet_cm6m", "helmet_cm7m");
    public static final Item OLIVE_HELMET_CM8M_GAS_MASK = createGeoArmor("olive_helmet_cm8m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "olive_helmet_cm6m", "helmet_cm8m");
    public static final Item OLIVE_JUGGERNAUT_ARMOR_BOOTS = createGeoArmor("olive_juggernaut_armor_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "olive_juggernaut_armor", "juggernaut_armor");
    public static final Item OLIVE_JUGGERNAUT_ARMOR_CHESTPLATE = createGeoArmor("olive_juggernaut_armor_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "olive_juggernaut_armor", "juggernaut_armor");
    public static final Item OLIVE_JUGGERNAUT_ARMOR_HELMET = createGeoArmor("olive_juggernaut_armor_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "olive_juggernaut_armor", "juggernaut_armor");
    public static final Item OLIVE_JUGGERNAUT_ARMOR_LEGGINGS = createGeoArmor("olive_juggernaut_armor_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "olive_juggernaut_armor", "juggernaut_armor");
    public static final Item OLIVE_PLATE_CARRIER_HEAVY = createGeoArmor("olive_plate_carrier_heavy", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "olive_plate_carrier", "olive_plate_carrier");
    public static final Item OLIVE_PLATE_CARRIER_LIGHT = createGeoArmor("olive_plate_carrier_light", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "olive_plate_carrier", "olive_plate_carrier");
    public static final Item RED_MILITARY_BERET = createGeoArmor("red_military_beret", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "red_military_beret", "military_beret");
    public static final Item RIOT_ARMOR_BOOTS = createGeoArmor("riot_armor_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "riot_armor", "riot_armor");
    public static final Item RIOT_ARMOR_CHESTPLATE = createGeoArmor("riot_armor_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "riot_armor", "riot_armor");
    public static final Item RIOT_ARMOR_HELMET = createGeoArmor("riot_armor_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "riot_armor", "riot_armor");
    public static final Item RIOT_ARMOR_LEGGINGS = createGeoArmor("riot_armor_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "riot_armor", "riot_armor");
    public static final Item SPRUCE_GHILLIE_BOOTS = createGeoArmor("spruce_ghillie_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "spruce_ghillie_armor", "ghillie_armor");
    public static final Item SPRUCE_GHILLIE_CHESTPLATE = createGeoArmor("spruce_ghillie_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "spruce_ghillie_armor", "ghillie_armor");
    public static final Item SPRUCE_GHILLIE_HELMET = createGeoArmor("spruce_ghillie_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "spruce_ghillie_armor", "ghillie_armor");
    public static final Item SPRUCE_GHILLIE_LEGGINGS = createGeoArmor("spruce_ghillie_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "spruce_ghillie_armor", "ghillie_armor");
    public static final Item SWAT_ARMOR_BOOTS = createGeoArmor("swat_armor_boots", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.FEET, "swat_armor", "swat_armor");
    public static final Item SWAT_ARMOR_CHESTPLATE = createGeoArmor("swat_armor_chestplate", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.CHEST, "swat_armor", "swat_armor");
    public static final Item SWAT_ARMOR_HELMET = createGeoArmor("swat_armor_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "swat_armor", "swat_armor");
    public static final Item SWAT_ARMOR_LEGGINGS = createGeoArmor("swat_armor_leggings", MCoreMaterials.ARMOR_STEEL, 2, EntityEquipmentSlot.LEGS, "swat_armor", "swat_armor");
    public static final Item UN_COMBAT_HELMET = createGeoArmor("un_combat_helmet", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "un_combat_helmet", "un_combat_helmet");
    public static final Item UN_HELMET_CM6M_GAS_MASK = createGeoArmor("un_helmet_cm6m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "olive_helmet_cm6m", "helmet_cm6m");
    public static final Item UN_HELMET_CM7M_GAS_MASK = createGeoArmor("un_helmet_cm7m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "olive_helmet_cm6m", "helmet_cm7m");
    public static final Item UN_HELMET_CM8M_GAS_MASK = createGeoArmor("un_helmet_cm8m_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "olive_helmet_cm6m", "helmet_cm8m");
    public static final Item UN_MILITARY_BERET = createGeoArmor("un_military_beret", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "un_military_beret", "military_beret");
    public static final Item WHITE_GP5_GAS_MASK = createGeoArmor("white_gp5_gas_mask", MCoreMaterials.ARMOR_STEEL, 1, EntityEquipmentSlot.HEAD, "white_gp5", "white_gp5");
    public static final Item BRACELET = createBauble("bracelet");
    public static final Item HEADLAMP = createHeadlamp("headlamp");
    public static final Item PORTABLE_MAP = createPortableMap("portable_map");
    public static final Item BATTERY = createBattery("battery");
    public static final ItemArmor.ArmorMaterial MAT_ARTICGUILLIE = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("ArticGuillie", "mwccf:empty", 23, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
    public static final Item ARTICGUILLIE_HELMET = createCustomArmor("artic_guillie_helmet", MAT_ARTICGUILLIE, 1, EntityEquipmentSlot.HEAD, "artic_guillie_armor", "Modelghillie");
    public static final Item ARTICGUILLIE_CHESTPLATE = createCustomArmor("artic_guillie_chestplate", MAT_ARTICGUILLIE, 1, EntityEquipmentSlot.CHEST, "artic_guillie_armor", "Modelghillie");
    public static final Item ARTICGUILLIE_LEGGINGS = createCustomArmor("artic_guillie_leggings", MAT_ARTICGUILLIE, 2, EntityEquipmentSlot.LEGS, "artic_guillie_armor", "Modelghillie");
    public static final Item ARTICGUILLIE_BOOTS = createCustomArmor("artic_guillie_boots", MAT_ARTICGUILLIE, 1, EntityEquipmentSlot.FEET, "artic_guillie_armor", "Modelghillie");
    public static final ItemArmor.ArmorMaterial MAT_BLACKHUNTER = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("BlackHunter", "mwccf:empty", 15, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
    public static final Item BLACKHUNTER_HELMET = createCustomArmor("black_hunter_helmet", MAT_BLACKHUNTER, 1, EntityEquipmentSlot.HEAD, "hunter_helmet_black", "Modelhunter_armor");
    public static final ItemArmor.ArmorMaterial MAT_BLACKJUGGERNAUT = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("BlackJuggernaut", "mwccf:empty", 32, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.0F);
    public static final Item BLACKJUGGERNAUT_HELMET = createCustomArmor("black_juggernaut_helmet", MAT_BLACKJUGGERNAUT, 1, EntityEquipmentSlot.HEAD, "juggernaut_armor_black", "Modeljuggernaut_armor");
    public static final Item BLACKJUGGERNAUT_CHESTPLATE = createCustomArmor("black_juggernaut_chestplate", MAT_BLACKJUGGERNAUT, 1, EntityEquipmentSlot.CHEST, "juggernaut_armor_black", "Modeljuggernaut_armor");
    public static final Item BLACKJUGGERNAUT_LEGGINGS = createCustomArmor("black_juggernaut_leggings", MAT_BLACKJUGGERNAUT, 2, EntityEquipmentSlot.LEGS, "juggernaut_armor_black", "Modeljuggernaut_armor");
    public static final Item BLACKJUGGERNAUT_BOOTS = createCustomArmor("black_juggernaut_boots", MAT_BLACKJUGGERNAUT, 1, EntityEquipmentSlot.FEET, "juggernaut_armor_black", "Modeljuggernaut_armor");
    public static final ItemArmor.ArmorMaterial MAT_BLACKRECLUITARMOR = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("BlackRecluitArmor", "mwccf:empty", 12, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F);
    public static final Item BLACKRECLUITARMOR_HELMET = createCustomArmor("black_recluit_armor_helmet", MAT_BLACKRECLUITARMOR, 1, EntityEquipmentSlot.HEAD, "recluit_armor_black_no_vest", "Modelrecluit_armor");
    public static final Item BLACKRECLUITARMOR_CHESTPLATE = createCustomArmor("black_recluit_armor_chestplate", MAT_BLACKRECLUITARMOR, 1, EntityEquipmentSlot.CHEST, "recluit_armor_black_no_vest", "Modelrecluit_armor");
    public static final Item BLACKRECLUITARMOR_LEGGINGS = createCustomArmor("black_recluit_armor_leggings", MAT_BLACKRECLUITARMOR, 2, EntityEquipmentSlot.LEGS, "recluit_armor_black_no_vest", "Modelrecluit_armor");
    public static final Item BLACKRECLUITARMOR_BOOTS = createCustomArmor("black_recluit_armor_boots", MAT_BLACKRECLUITARMOR, 1, EntityEquipmentSlot.FEET, "recluit_armor_black_no_vest", "Modelrecluit_armor");
    public static final ItemArmor.ArmorMaterial MAT_BLACKROCKIEARMOR = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("BlackRockieArmor", "mwccf:empty", 15, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
    public static final Item BLACKROCKIEARMOR_HELMET = createCustomArmor("black_rockie_armor_helmet", MAT_BLACKROCKIEARMOR, 1, EntityEquipmentSlot.HEAD, "rockie_armor_black_no_vest", "Modelrockie_armor");
    public static final Item BLACKROCKIEARMOR_CHESTPLATE = createCustomArmor("black_rockie_armor_chestplate", MAT_BLACKROCKIEARMOR, 1, EntityEquipmentSlot.CHEST, "rockie_armor_black_no_vest", "Modelrockie_armor");
    public static final Item BLACKROCKIEARMOR_LEGGINGS = createCustomArmor("black_rockie_armor_leggings", MAT_BLACKROCKIEARMOR, 2, EntityEquipmentSlot.LEGS, "rockie_armor_black_no_vest", "Modelrockie_armor");
    public static final Item BLACKROCKIEARMOR_BOOTS = createCustomArmor("black_rockie_armor_boots", MAT_BLACKROCKIEARMOR, 1, EntityEquipmentSlot.FEET, "rockie_armor_black_no_vest", "Modelrockie_armor");
    public static final ItemArmor.ArmorMaterial MAT_CHICKENHEAD = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("ChickenHead", "mwccf:empty", 10, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static final Item CHICKENHEAD_HELMET = createCustomArmor("chicken_head_helmet", MAT_CHICKENHEAD, 1, EntityEquipmentSlot.HEAD, "chicken_head", "Modelchiken_head");
    public static final ItemArmor.ArmorMaterial MAT_CLOTHBALACLAVA = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("ClothBalaclava", "mwccf:empty", 10, new int[]{13, 15, 16, 11}, 12, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static final Item CLOTHBALACLAVA_HELMET = createCustomArmor("cloth_balaclava_helmet", MAT_CLOTHBALACLAVA, 1, EntityEquipmentSlot.HEAD, "cloth_balaclava", "Modelbalaclava");
    public static final ItemArmor.ArmorMaterial MAT_DESERTHUNTER = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("DesertHunter", "mwccf:empty", 15, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
    public static final Item DESERTHUNTER_HELMET = createCustomArmor("desert_hunter_helmet", MAT_DESERTHUNTER, 1, EntityEquipmentSlot.HEAD, "hunter_helmet_desert", "Modelhunter_armor");
    public static final ItemArmor.ArmorMaterial MAT_DESERTJUGGERNAUT = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("DesertJuggernaut", "mwccf:empty", 32, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.0F);
    public static final Item DESERTJUGGERNAUT_HELMET = createCustomArmor("desert_juggernaut_helmet", MAT_DESERTJUGGERNAUT, 1, EntityEquipmentSlot.HEAD, "juggernaut_armor_desert", "Modeljuggernaut_armor");
    public static final Item DESERTJUGGERNAUT_CHESTPLATE = createCustomArmor("desert_juggernaut_chestplate", MAT_DESERTJUGGERNAUT, 1, EntityEquipmentSlot.CHEST, "juggernaut_armor_desert", "Modeljuggernaut_armor");
    public static final Item DESERTJUGGERNAUT_LEGGINGS = createCustomArmor("desert_juggernaut_leggings", MAT_DESERTJUGGERNAUT, 2, EntityEquipmentSlot.LEGS, "juggernaut_armor_desert", "Modeljuggernaut_armor");
    public static final Item DESERTJUGGERNAUT_BOOTS = createCustomArmor("desert_juggernaut_boots", MAT_DESERTJUGGERNAUT, 1, EntityEquipmentSlot.FEET, "juggernaut_armor_desert", "Modeljuggernaut_armor");
    public static final ItemArmor.ArmorMaterial MAT_DESERTRECLUITARMOR = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("DesertRecluitArmor", "mwccf:empty", 12, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F);
    public static final Item DESERTRECLUITARMOR_HELMET = createCustomArmor("desert_recluit_armor_helmet", MAT_DESERTRECLUITARMOR, 1, EntityEquipmentSlot.HEAD, "recluit_armor_desert_no_vest", "Modelrecluit_armor");
    public static final Item DESERTRECLUITARMOR_CHESTPLATE = createCustomArmor("desert_recluit_armor_chestplate", MAT_DESERTRECLUITARMOR, 1, EntityEquipmentSlot.CHEST, "recluit_armor_desert_no_vest", "Modelrecluit_armor");
    public static final Item DESERTRECLUITARMOR_LEGGINGS = createCustomArmor("desert_recluit_armor_leggings", MAT_DESERTRECLUITARMOR, 2, EntityEquipmentSlot.LEGS, "recluit_armor_desert_no_vest", "Modelrecluit_armor");
    public static final Item DESERTRECLUITARMOR_BOOTS = createCustomArmor("desert_recluit_armor_boots", MAT_DESERTRECLUITARMOR, 1, EntityEquipmentSlot.FEET, "recluit_armor_desert_no_vest", "Modelrecluit_armor");
    public static final ItemArmor.ArmorMaterial MAT_DESERTROCKIEARMOR = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("DesertRockieArmor", "mwccf:empty", 15, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
    public static final Item DESERTROCKIEARMOR_HELMET = createCustomArmor("desert_rockie_armor_helmet", MAT_DESERTROCKIEARMOR, 1, EntityEquipmentSlot.HEAD, "rockie_armor_desert_no_vest", "Modelrockie_armor");
    public static final Item DESERTROCKIEARMOR_CHESTPLATE = createCustomArmor("desert_rockie_armor_chestplate", MAT_DESERTROCKIEARMOR, 1, EntityEquipmentSlot.CHEST, "rockie_armor_desert_no_vest", "Modelrockie_armor");
    public static final Item DESERTROCKIEARMOR_LEGGINGS = createCustomArmor("desert_rockie_armor_leggings", MAT_DESERTROCKIEARMOR, 2, EntityEquipmentSlot.LEGS, "rockie_armor_desert_no_vest", "Modelrockie_armor");
    public static final Item DESERTROCKIEARMOR_BOOTS = createCustomArmor("desert_rockie_armor_boots", MAT_DESERTROCKIEARMOR, 1, EntityEquipmentSlot.FEET, "rockie_armor_desert_no_vest", "Modelrockie_armor");
    public static final ItemArmor.ArmorMaterial MAT_EXOHEAVYBLACK = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("ExoHeavyBlack", "mwccf:empty", 43, new int[]{13, 15, 16, 11}, 16, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 4.0F);
    public static final Item EXOHEAVYBLACK_HELMET = createCustomArmor("exo_heavy_black_helmet", MAT_EXOHEAVYBLACK, 1, EntityEquipmentSlot.HEAD, "exo_heavy_armor_black", "Modelexo_heavy_armor");
    public static final Item EXOHEAVYBLACK_CHESTPLATE = createCustomArmor("exo_heavy_black_chestplate", MAT_EXOHEAVYBLACK, 1, EntityEquipmentSlot.CHEST, "exo_heavy_armor_black", "Modelexo_heavy_armor");
    public static final Item EXOHEAVYBLACK_LEGGINGS = createCustomArmor("exo_heavy_black_leggings", MAT_EXOHEAVYBLACK, 2, EntityEquipmentSlot.LEGS, "exo_heavy_armor_black", "Modelexo_heavy_armor");
    public static final Item EXOHEAVYBLACK_BOOTS = createCustomArmor("exo_heavy_black_boots", MAT_EXOHEAVYBLACK, 1, EntityEquipmentSlot.FEET, "exo_heavy_armor_black", "Modelexo_heavy_armor");
    public static final ItemArmor.ArmorMaterial MAT_EXOHEAVYDESERT = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("ExoHeavyDesert", "mwccf:empty", 43, new int[]{13, 15, 16, 11}, 16, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 4.0F);
    public static final Item EXOHEAVYDESERT_HELMET = createCustomArmor("exo_heavy_desert_helmet", MAT_EXOHEAVYDESERT, 1, EntityEquipmentSlot.HEAD, "exo_heavy_armor_desert", "Modelexo_heavy_armor");
    public static final Item EXOHEAVYDESERT_CHESTPLATE = createCustomArmor("exo_heavy_desert_chestplate", MAT_EXOHEAVYDESERT, 1, EntityEquipmentSlot.CHEST, "exo_heavy_armor_desert", "Modelexo_heavy_armor");
    public static final Item EXOHEAVYDESERT_LEGGINGS = createCustomArmor("exo_heavy_desert_leggings", MAT_EXOHEAVYDESERT, 2, EntityEquipmentSlot.LEGS, "exo_heavy_armor_desert", "Modelexo_heavy_armor");
    public static final Item EXOHEAVYDESERT_BOOTS = createCustomArmor("exo_heavy_desert_boots", MAT_EXOHEAVYDESERT, 1, EntityEquipmentSlot.FEET, "exo_heavy_armor_desert", "Modelexo_heavy_armor");
    public static final ItemArmor.ArmorMaterial MAT_EXOHEAVYGREEN = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("ExoHeavyGreen", "mwccf:empty", 43, new int[]{13, 15, 16, 11}, 16, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 4.0F);
    public static final Item EXOHEAVYGREEN_HELMET = createCustomArmor("exo_heavy_green_helmet", MAT_EXOHEAVYGREEN, 1, EntityEquipmentSlot.HEAD, "exo_heavy_armor", "Modelexo_heavy_armor");
    public static final Item EXOHEAVYGREEN_CHESTPLATE = createCustomArmor("exo_heavy_green_chestplate", MAT_EXOHEAVYGREEN, 1, EntityEquipmentSlot.CHEST, "exo_heavy_armor", "Modelexo_heavy_armor");
    public static final Item EXOHEAVYGREEN_LEGGINGS = createCustomArmor("exo_heavy_green_leggings", MAT_EXOHEAVYGREEN, 2, EntityEquipmentSlot.LEGS, "exo_heavy_armor", "Modelexo_heavy_armor");
    public static final Item EXOHEAVYGREEN_BOOTS = createCustomArmor("exo_heavy_green_boots", MAT_EXOHEAVYGREEN, 1, EntityEquipmentSlot.FEET, "exo_heavy_armor", "Modelexo_heavy_armor");
    public static final ItemArmor.ArmorMaterial MAT_EXO = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("Exo", "mwccf:empty", 33, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
    public static final Item EXO_HELMET = createCustomArmor("exo_helmet", MAT_EXO, 1, EntityEquipmentSlot.HEAD, "exo_suit_armor", "Modelexo_suit_armor");
    public static final Item EXO_CHESTPLATE = createCustomArmor("exo_chestplate", MAT_EXO, 1, EntityEquipmentSlot.CHEST, "exo_suit_armor", "Modelexo_suit_armor");
    public static final Item EXO_LEGGINGS = createCustomArmor("exo_leggings", MAT_EXO, 2, EntityEquipmentSlot.LEGS, "exo_suit_armor", "Modelexo_suit_armor");
    public static final Item EXO_BOOTS = createCustomArmor("exo_boots", MAT_EXO, 1, EntityEquipmentSlot.FEET, "exo_suit_armor", "Modelexo_suit_armor");
    public static final ItemArmor.ArmorMaterial MAT_FIREFIGHTER = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("FireFighter", "mwccf:empty", 26, new int[]{13, 15, 16, 11}, 12, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
    public static final Item FIREFIGHTER_HELMET = createCustomArmor("fire_fighter_helmet", MAT_FIREFIGHTER, 1, EntityEquipmentSlot.HEAD, "fire_fighter", "Modelfire_fighter");
    public static final Item FIREFIGHTER_CHESTPLATE = createCustomArmor("fire_fighter_chestplate", MAT_FIREFIGHTER, 1, EntityEquipmentSlot.CHEST, "fire_fighter", "Modelfire_fighter");
    public static final Item FIREFIGHTER_LEGGINGS = createCustomArmor("fire_fighter_leggings", MAT_FIREFIGHTER, 2, EntityEquipmentSlot.LEGS, "fire_fighter", "Modelfire_fighter");
    public static final Item FIREFIGHTER_BOOTS = createCustomArmor("fire_fighter_boots", MAT_FIREFIGHTER, 1, EntityEquipmentSlot.FEET, "fire_fighter", "Modelfire_fighter");
    public static final ItemArmor.ArmorMaterial MAT_GASMASK = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("GasMask", "mwccf:empty", 17, new int[]{13, 15, 16, 11}, 12, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static final Item GASMASK_HELMET = createCustomArmor("gas_mask_helmet", MAT_GASMASK, 1, EntityEquipmentSlot.HEAD, "gas_mask", "Modelgas_mask");
    public static final ItemArmor.ArmorMaterial MAT_GREENHUNTER = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("GreenHunter", "mwccf:empty", 15, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
    public static final Item GREENHUNTER_HELMET = createCustomArmor("green_hunter_helmet", MAT_GREENHUNTER, 1, EntityEquipmentSlot.HEAD, "hunter_helmet_green", "Modelhunter_armor");
    public static final ItemArmor.ArmorMaterial MAT_GREENJUGGERNAUT = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("GreenJuggernaut", "mwccf:empty", 32, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.0F);
    public static final Item GREENJUGGERNAUT_HELMET = createCustomArmor("green_juggernaut_helmet", MAT_GREENJUGGERNAUT, 1, EntityEquipmentSlot.HEAD, "juggernaut_armor_green", "Modeljuggernaut_armor");
    public static final Item GREENJUGGERNAUT_CHESTPLATE = createCustomArmor("green_juggernaut_chestplate", MAT_GREENJUGGERNAUT, 1, EntityEquipmentSlot.CHEST, "juggernaut_armor_green", "Modeljuggernaut_armor");
    public static final Item GREENJUGGERNAUT_LEGGINGS = createCustomArmor("green_juggernaut_leggings", MAT_GREENJUGGERNAUT, 2, EntityEquipmentSlot.LEGS, "juggernaut_armor_green", "Modeljuggernaut_armor");
    public static final Item GREENJUGGERNAUT_BOOTS = createCustomArmor("green_juggernaut_boots", MAT_GREENJUGGERNAUT, 1, EntityEquipmentSlot.FEET, "juggernaut_armor_green", "Modeljuggernaut_armor");
    public static final ItemArmor.ArmorMaterial MAT_GREENRECLUITARMOR = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("GreenRecluitArmor", "mwccf:empty", 12, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F);
    public static final Item GREENRECLUITARMOR_HELMET = createCustomArmor("green_recluit_armor_helmet", MAT_GREENRECLUITARMOR, 1, EntityEquipmentSlot.HEAD, "recluit_armor_green_no_vest", "Modelrecluit_armor");
    public static final Item GREENRECLUITARMOR_CHESTPLATE = createCustomArmor("green_recluit_armor_chestplate", MAT_GREENRECLUITARMOR, 1, EntityEquipmentSlot.CHEST, "recluit_armor_green_no_vest", "Modelrecluit_armor");
    public static final Item GREENRECLUITARMOR_LEGGINGS = createCustomArmor("green_recluit_armor_leggings", MAT_GREENRECLUITARMOR, 2, EntityEquipmentSlot.LEGS, "recluit_armor_green_no_vest", "Modelrecluit_armor");
    public static final Item GREENRECLUITARMOR_BOOTS = createCustomArmor("green_recluit_armor_boots", MAT_GREENRECLUITARMOR, 1, EntityEquipmentSlot.FEET, "recluit_armor_green_no_vest", "Modelrecluit_armor");
    public static final ItemArmor.ArmorMaterial MAT_GREENROCKIEARMOR = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("GreenRockieArmor", "mwccf:empty", 15, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
    public static final Item GREENROCKIEARMOR_HELMET = createCustomArmor("green_rockie_armor_helmet", MAT_GREENROCKIEARMOR, 1, EntityEquipmentSlot.HEAD, "rockie_armor_green_no_vest", "Modelrockie_armor");
    public static final Item GREENROCKIEARMOR_CHESTPLATE = createCustomArmor("green_rockie_armor_chestplate", MAT_GREENROCKIEARMOR, 1, EntityEquipmentSlot.CHEST, "rockie_armor_green_no_vest", "Modelrockie_armor");
    public static final Item GREENROCKIEARMOR_LEGGINGS = createCustomArmor("green_rockie_armor_leggings", MAT_GREENROCKIEARMOR, 2, EntityEquipmentSlot.LEGS, "rockie_armor_green_no_vest", "Modelrockie_armor");
    public static final Item GREENROCKIEARMOR_BOOTS = createCustomArmor("green_rockie_armor_boots", MAT_GREENROCKIEARMOR, 1, EntityEquipmentSlot.FEET, "rockie_armor_green_no_vest", "Modelrockie_armor");
    public static final ItemArmor.ArmorMaterial MAT_GUILLIE = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("Guillie", "mwccf:empty", 23, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
    public static final Item GUILLIE_HELMET = createCustomArmor("guillie_helmet", MAT_GUILLIE, 1, EntityEquipmentSlot.HEAD, "oak_guillie", "Modelghillie");
    public static final Item GUILLIE_CHESTPLATE = createCustomArmor("guillie_chestplate", MAT_GUILLIE, 1, EntityEquipmentSlot.CHEST, "oak_guillie", "Modelghillie");
    public static final Item GUILLIE_LEGGINGS = createCustomArmor("guillie_leggings", MAT_GUILLIE, 2, EntityEquipmentSlot.LEGS, "oak_guillie", "Modelghillie");
    public static final Item GUILLIE_BOOTS = createCustomArmor("guillie_boots", MAT_GUILLIE, 1, EntityEquipmentSlot.FEET, "oak_guillie", "Modelghillie");
    public static final ItemArmor.ArmorMaterial MAT_HAZMAT = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("Hazmat", "mwccf:empty", 26, new int[]{13, 15, 16, 11}, 12, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.5F);
    public static final Item HAZMAT_HELMET = createCustomArmor("hazmat_helmet", MAT_HAZMAT, 1, EntityEquipmentSlot.HEAD, "hazmat", "Modelhazmat");
    public static final Item HAZMAT_CHESTPLATE = createCustomArmor("hazmat_chestplate", MAT_HAZMAT, 1, EntityEquipmentSlot.CHEST, "hazmat", "Modelhazmat");
    public static final Item HAZMAT_LEGGINGS = createCustomArmor("hazmat_leggings", MAT_HAZMAT, 2, EntityEquipmentSlot.LEGS, "hazmat", "Modelhazmat");
    public static final Item HAZMAT_BOOTS = createCustomArmor("hazmat_boots", MAT_HAZMAT, 1, EntityEquipmentSlot.FEET, "hazmat", "Modelhazmat");
    public static final ItemArmor.ArmorMaterial MAT_MILITARY = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("Military", "mwccf:empty", 25, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.5F);
    public static final Item MILITARY_HELMET = createCustomArmor("military_helmet", MAT_MILITARY, 1, EntityEquipmentSlot.HEAD, "military_armor_no_sleeves", "Modelmilitary_armor");
    public static final Item MILITARY_CHESTPLATE = createCustomArmor("military_chestplate", MAT_MILITARY, 1, EntityEquipmentSlot.CHEST, "military_armor_no_sleeves", "Modelmilitary_armor");
    public static final Item MILITARY_LEGGINGS = createCustomArmor("military_leggings", MAT_MILITARY, 2, EntityEquipmentSlot.LEGS, "military_armor_no_sleeves", "Modelmilitary_armor");
    public static final Item MILITARY_BOOTS = createCustomArmor("military_boots", MAT_MILITARY, 1, EntityEquipmentSlot.FEET, "military_armor_no_sleeves", "Modelmilitary_armor");
    public static final ItemArmor.ArmorMaterial MAT_MOTORCYCLE = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("Motorcycle", "mwccf:empty", 12, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static final Item MOTORCYCLE_HELMET = createCustomArmor("motorcycle_helmet", MAT_MOTORCYCLE, 1, EntityEquipmentSlot.HEAD, "motorcycle_helmet", "Modelmotorcycle_helmet");
    public static final ItemArmor.ArmorMaterial MAT_NIGHTVISIONGOGGLES = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("NightVisionGoggles", "mwccf:empty", 20, new int[]{13, 15, 16, 11}, 6, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static final Item NIGHTVISIONGOGGLES_HELMET = createCustomArmor("night_vision_goggles_helmet", MAT_NIGHTVISIONGOGGLES, 1, EntityEquipmentSlot.HEAD, "night_vision_goggles", "Modelnight_vision_goggles");
    public static final ItemArmor.ArmorMaterial MAT_POLICE = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("Police", "mwccf:empty", 17, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static final Item POLICE_HELMET = createCustomArmor("police_helmet", MAT_POLICE, 1, EntityEquipmentSlot.HEAD, "police_armor_no_sleeves", "Modelpolice_armor");
    public static final Item POLICE_CHESTPLATE = createCustomArmor("police_chestplate", MAT_POLICE, 1, EntityEquipmentSlot.CHEST, "police_armor_no_sleeves", "Modelpolice_armor");
    public static final Item POLICE_LEGGINGS = createCustomArmor("police_leggings", MAT_POLICE, 2, EntityEquipmentSlot.LEGS, "police_armor_no_sleeves", "Modelpolice_armor");
    public static final Item POLICE_BOOTS = createCustomArmor("police_boots", MAT_POLICE, 1, EntityEquipmentSlot.FEET, "police_armor_no_sleeves", "Modelpolice_armor");
    public static final ItemArmor.ArmorMaterial MAT_SPRUCEGUILLIE = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("SpruceGuillie", "mwccf:empty", 23, new int[]{13, 15, 16, 11}, 9, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
    public static final Item SPRUCEGUILLIE_HELMET = createCustomArmor("spruce_guillie_helmet", MAT_SPRUCEGUILLIE, 1, EntityEquipmentSlot.HEAD, "spruce_guillie", "Modelghillie");
    public static final Item SPRUCEGUILLIE_CHESTPLATE = createCustomArmor("spruce_guillie_chestplate", MAT_SPRUCEGUILLIE, 1, EntityEquipmentSlot.CHEST, "spruce_guillie", "Modelghillie");
    public static final Item SPRUCEGUILLIE_LEGGINGS = createCustomArmor("spruce_guillie_leggings", MAT_SPRUCEGUILLIE, 2, EntityEquipmentSlot.LEGS, "spruce_guillie", "Modelghillie");
    public static final Item SPRUCEGUILLIE_BOOTS = createCustomArmor("spruce_guillie_boots", MAT_SPRUCEGUILLIE, 1, EntityEquipmentSlot.FEET, "spruce_guillie", "Modelghillie");
    public static final ItemArmor.ArmorMaterial MAT_WOOLBALACLAVA = net.minecraftforge.common.util.EnumHelper.addArmorMaterial("WoolBalaclava", "mwccf:empty", 10, new int[]{13, 15, 16, 11}, 12, net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static final Item WOOLBALACLAVA_HELMET = createCustomArmor("wool_balaclava_helmet", MAT_WOOLBALACLAVA, 1, EntityEquipmentSlot.HEAD, "wool_balaclava", "Modelbalaclava");


    private static Item createItem(String name) {
        Item item = new Item().setRegistryName("mwccf", name)
                              .setTranslationKey("mcore." + name)
                              .setCreativeTab(net.minecraft.creativetab.CreativeTabs.MATERIALS);
        ITEMS.add(item);
        return item;
    }

    private static Item createGeoArmor(String name, ItemArmor.ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String textureName, String geoModelName) {
        Item item = new com.voltyx.mwccf.geo.ItemGeoArmor(name, material, renderIndex, slot, textureName, geoModelName);
        ITEMS.add(item);
        return item;
    }

    private static Item createBauble(String name) {
        Item item = new com.voltyx.mwccf.geo.ItemBracelet(name);
        ITEMS.add(item);
        return item;
    }

    private static Item createHeadlamp(String name) {
        Item item = new com.voltyx.mwccf.geo.ItemHeadlamp(name);
        ITEMS.add(item);
        return item;
    }

    private static Item createPortableMap(String name) {
        Item item = new com.voltyx.mwccf.geo.ItemPortableMap(name);
        ITEMS.add(item);
        return item;
    }

    private static Item createBattery(String name) {
        Item item = new ItemBattery(name);
        ITEMS.add(item);
        return item;
    }

    private static Item createSword(String name, Item.ToolMaterial material) {
        Item item = new ItemSword(material).setRegistryName("mwccf", name);
        item.setTranslationKey("mcore." + name);
        ITEMS.add(item);
        return item;
    }

    private static Item createPickaxe(String name, Item.ToolMaterial material) {
        Item item = new CustomPickaxe(material).setRegistryName("mwccf", name);
        item.setTranslationKey("mcore." + name);
        ITEMS.add(item);
        return item;
    }

    private static Item createAxe(String name, Item.ToolMaterial material, float damage, float speed) {
        Item item = new CustomAxe(material, damage, speed).setRegistryName("mwccf", name);
        item.setTranslationKey("mcore." + name);
        ITEMS.add(item);
        return item;
    }

    private static Item createSpade(String name, Item.ToolMaterial material) {
        Item item = new CustomSpade(material).setRegistryName("mwccf", name);
        item.setTranslationKey("mcore." + name);
        ITEMS.add(item);
        return item;
    }

    private static Item createHoe(String name, Item.ToolMaterial material) {
        Item item = new CustomHoe(material).setRegistryName("mwccf", name);
        item.setTranslationKey("mcore." + name);
        ITEMS.add(item);
        return item;
    }

    private static Item createArmor(String name, ItemArmor.ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot) {
        Item item = new ItemArmor(material, renderIndex, slot).setRegistryName("mwccf", name);
        item.setTranslationKey("mcore." + name);
        ITEMS.add(item);
        return item;
    }

    private static Item createCustomArmor(String name, ItemArmor.ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String textureName, String customModelClass) {
        Item item = new com.voltyx.mwccf.mcore.ItemCustomArmor(name, material, renderIndex, slot, textureName, customModelClass);
        ITEMS.add(item);
        return item;
    }

    private static class CustomPickaxe extends ItemPickaxe {
        protected CustomPickaxe(ToolMaterial material) { super(material); }
    }
    private static class CustomAxe extends ItemAxe {
        protected CustomAxe(ToolMaterial material, float damage, float speed) { super(material, damage, speed); }
    }
    private static class CustomSpade extends ItemSpade {
        public CustomSpade(ToolMaterial material) { super(material); }
    }
    private static class CustomHoe extends ItemHoe {
        public CustomHoe(ToolMaterial material) { super(material); }
    }
}
