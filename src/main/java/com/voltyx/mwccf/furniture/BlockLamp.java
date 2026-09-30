package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockLamp extends BlockFurnitureHorizontal {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0.0D, 0.2D, 0.8D, 0.9375D, 0.8D);

    public BlockLamp(String name) {
        super(Material.GLASS);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(1.0F);
        this.setLightLevel(0.9375F); // Light emission
        this.setSoundType(SoundType.GLASS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }
}
