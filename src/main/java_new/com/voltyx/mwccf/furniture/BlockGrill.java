package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockGrill extends BlockFurnitureHorizontal {

    public BlockGrill(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.METAL);
    }
}
