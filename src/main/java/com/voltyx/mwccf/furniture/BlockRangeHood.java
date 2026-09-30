package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRangeHood extends BlockFurnitureHorizontal {

    protected static final net.minecraft.util.math.AxisAlignedBB AABB = new net.minecraft.util.math.AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4375D, 1.0D);

    public BlockRangeHood(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(1.5F);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public net.minecraft.util.math.AxisAlignedBB getBoundingBox(IBlockState state, net.minecraft.world.IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        worldIn.playSound(null, pos, FurnitureSounds.BLOCK_MICROWAVE_FAN, SoundCategory.BLOCKS, 0.6F, 1.0F);
        return true;
    }
}
