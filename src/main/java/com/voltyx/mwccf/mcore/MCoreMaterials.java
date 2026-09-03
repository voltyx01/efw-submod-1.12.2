package com.voltyx.mwccf.mcore;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class MCoreMaterials {
    public static final ToolMaterial TOOL_STEEL = EnumHelper.addToolMaterial("TOOL_STEEL", 2, 500, 7.0F, 2.5F, 12);
    public static final ToolMaterial TOOL_TITANIUM = EnumHelper.addToolMaterial("TOOL_TITANIUM", 3, 2000, 9.0F, 3.5F, 15);

    public static final ArmorMaterial ARMOR_STEEL = EnumHelper.addArmorMaterial("ARMOR_STEEL", "mwccf:steel", 20, new int[]{2, 6, 7, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static final ArmorMaterial ARMOR_TITANIUM = EnumHelper.addArmorMaterial("ARMOR_TITANIUM", "mwccf:titanium", 35, new int[]{3, 6, 8, 3}, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);
}
