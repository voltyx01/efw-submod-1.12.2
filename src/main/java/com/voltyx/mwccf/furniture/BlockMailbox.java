package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMailbox extends BlockFurnitureHorizontal {

    protected static final AxisAlignedBB[] AABBS = FurnitureAABB.createRotated(new AxisAlignedBB(0.125D, 0.0D, 0.1875D, 0.875D, 1.25D, 0.8125D));

    public BlockMailbox(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FurnitureAABB.get(AABBS, state.getValue(FACING));
    }
}
