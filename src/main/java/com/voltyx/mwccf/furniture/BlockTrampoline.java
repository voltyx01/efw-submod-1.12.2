package com.voltyx.mwccf.furniture;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrampoline extends Block {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);

    public BlockTrampoline(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.CLOTH);
        this.setCreativeTab(FurnitureCreativeTab.INSTANCE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn.isSneaking()) {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        } else {
            entityIn.fall(fallDistance, 0.0F);
        }
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        if (entityIn.isSneaking()) {
            super.onLanded(worldIn, entityIn);
        } else if (entityIn.motionY < 0.0D) {
            double bounce = -entityIn.motionY * 1.35D;
            if (bounce > 2.0D) bounce = 2.0D;
            if (bounce < 0.6D) bounce = 0.6D;
            entityIn.motionY = bounce;
            if (entityIn instanceof EntityLivingBase) {
                entityIn.onGround = false;
            }
            if (!worldIn.isRemote && FurnitureSounds.BLOCK_TRAMPOLINE_BOUNCE != null) {
                worldIn.playSound(null, entityIn.posX, entityIn.posY, entityIn.posZ, FurnitureSounds.BLOCK_TRAMPOLINE_BOUNCE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
}
