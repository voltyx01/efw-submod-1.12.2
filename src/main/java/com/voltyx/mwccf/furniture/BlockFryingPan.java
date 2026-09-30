package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFryingPan extends BlockFurnitureHorizontal {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.125D, 0.8125D);

    public BlockFryingPan(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(1.5F);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        worldIn.playSound(null, pos, FurnitureSounds.BLOCK_FRYING_PAN_PLACE_INGREDIENT, SoundCategory.BLOCKS, 0.8F, 1.0F);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        IBlockState below = worldIn.getBlockState(pos.down());
        if (below.getBlock() instanceof BlockStove) {
            double x = pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.4D;
            double y = pos.getY() + 0.15D;
            double z = pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.4D;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.02D, 0.0D);
            if (rand.nextInt(3) == 0) {
                worldIn.playSound(pos.getX() + 0.5D, pos.getY() + 0.2D, pos.getZ() + 0.5D, FurnitureSounds.BLOCK_FRYING_PAN_SIZZLE, SoundCategory.BLOCKS, 0.4F, 1.0F, false);
            }
        }
    }
}
