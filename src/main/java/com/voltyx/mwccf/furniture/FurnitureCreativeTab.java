package com.voltyx.mwccf.furniture;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FurnitureCreativeTab extends CreativeTabs {

    public static final FurnitureCreativeTab INSTANCE = new FurnitureCreativeTab();

    public FurnitureCreativeTab() {
        super("refurbished_furniture");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon() {
        if (FurnitureBlocks.WORKBENCH != null) {
            return new ItemStack(FurnitureBlocks.WORKBENCH);
        }
        return new ItemStack(Blocks.CRAFTING_TABLE);
    }
}
