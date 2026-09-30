package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSofa extends BlockFurnitureHorizontal {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockSofa(String name) {
        super(Material.CLOTH);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(1.5F);
        this.setSoundType(SoundType.CLOTH);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean onBlockActivated(net.minecraft.world.World worldIn, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState state, net.minecraft.entity.player.EntityPlayer playerIn, net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntitySeat.sitOnBlock(worldIn, pos, playerIn, 0.35D);
        }
        return true;
    }
}
