package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockToilet extends BlockFurnitureHorizontal {

    protected static final AxisAlignedBB[] AABBS = FurnitureAABB.createRotated(new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.0625D, 1.0D));

    public BlockToilet(String name) {
        super(Material.ROCK);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, net.minecraft.world.IBlockAccess source, BlockPos pos) {
        return FurnitureAABB.get(AABBS, state.getValue(FACING));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.BLOCKS, 1.0F, 1.0F);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, 1.0F, 0.8F);
            if (!worldIn.isRemote && worldIn instanceof WorldServer) {
                WorldServer ws = (WorldServer) worldIn;
                ws.spawnParticle(EnumParticleTypes.WATER_SPLASH, pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D, 25, 0.2D, 0.1D, 0.2D, 0.05D);
            }
            return true;
        }

        if (!worldIn.isRemote) {
            EntitySeat.sitOnBlock(worldIn, pos, playerIn, 0.35D);
        }
        return true;
    }
}
