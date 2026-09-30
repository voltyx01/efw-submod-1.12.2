package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class BlockToaster extends BlockFurnitureHorizontal {

    public static final PropertyBool TOASTING = PropertyBool.create("toasting");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0.0D, 0.2D, 0.8D, 0.5D, 0.8D);

    public BlockToaster(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(1.5F);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TOASTING, false));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING, TOASTING });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);
        boolean toasting = (meta & 4) != 0;
        return this.getDefaultState().withProperty(FACING, facing).withProperty(TOASTING, toasting);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(TOASTING)) {
            meta |= 4;
        }
        return meta;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!state.getValue(TOASTING)) {
            ItemStack held = playerIn.getHeldItem(hand);
            if (!held.isEmpty() && (held.getItem() == Items.BREAD || held.getItem().getTranslationKey().contains("bread"))) {
                if (!playerIn.capabilities.isCreativeMode) {
                    held.shrink(1);
                }
                worldIn.setBlockState(pos, state.withProperty(TOASTING, true), 3);
                worldIn.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.7F, 0.8F);
                worldIn.scheduleUpdate(pos, this, 80); // 4 seconds to toast
                return true;
            }
        }
        return true;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && state.getValue(TOASTING)) {
            worldIn.setBlockState(pos, state.withProperty(TOASTING, false), 3);
            worldIn.playSound(null, pos, SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.BLOCKS, 1.0F, 1.5F);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.8F);

            if (worldIn instanceof WorldServer) {
                ((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5D, pos.getY() + 0.55D, pos.getZ() + 0.5D, 8, 0.1D, 0.1D, 0.1D, 0.02D);
            }

            // Spawn golden toast/bread
            ItemStack toast = new ItemStack(Items.BREAD);
            EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D, toast);
            entityItem.motionX = 0.0D;
            entityItem.motionY = 0.25D;
            entityItem.motionZ = 0.0D;
            worldIn.spawnEntity(entityItem);
        }
    }
}
