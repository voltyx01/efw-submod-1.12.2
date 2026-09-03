package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockLamp extends BlockFurnitureHorizontal {

    public BlockLamp(String name) {
        super(Material.GLASS);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(1.0F);
        this.setLightLevel(0.9375F); // Light emission
        this.setSoundType(SoundType.GLASS);
    }
}
