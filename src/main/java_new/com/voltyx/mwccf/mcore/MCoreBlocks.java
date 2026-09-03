package com.voltyx.mwccf.mcore;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class MCoreBlocks {
    public static final Block STEEL_BLOCK = new Block(Material.IRON) {
        {
            this.setSoundType(SoundType.METAL);
        }
    }.setRegistryName("mwccf", "steel_block")
     .setTranslationKey("mcore.steel_block")
     .setHardness(5.0F).setResistance(10.0F)
     .setCreativeTab(net.minecraft.creativetab.CreativeTabs.BUILDING_BLOCKS);

    public static final Block TITANIUM_BLOCK = new Block(Material.IRON) {
        {
            this.setSoundType(SoundType.METAL);
        }
    }.setRegistryName("mwccf", "titanium_block")
     .setTranslationKey("mcore.titanium_block")
     .setHardness(6.0F).setResistance(12.0F)
     .setCreativeTab(net.minecraft.creativetab.CreativeTabs.BUILDING_BLOCKS);

    public static final Block DEEPSLATE_TITANIUM_ORE = new Block(Material.ROCK) {
        {
            this.setSoundType(SoundType.STONE);
        }
    }.setRegistryName("mwccf", "deepslate_titanium_ore")
     .setTranslationKey("mcore.deepslate_titanium_ore")
     .setHardness(4.5F).setResistance(3.0F)
     .setCreativeTab(net.minecraft.creativetab.CreativeTabs.BUILDING_BLOCKS);
}
