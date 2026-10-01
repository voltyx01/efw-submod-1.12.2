package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBath extends BlockFurnitureHorizontal {

    public static final PropertyBool FILLED = PropertyBool.create("filled");
    public static final PropertyEnum<BathPart> PART = PropertyEnum.create("part", BathPart.class);

    public BlockBath(String name) {
        super(Material.ROCK);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(FILLED, false)
                .withProperty(PART, BathPart.FOOT));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING, FILLED, PART });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);
        boolean filled = (meta & 4) != 0;
        BathPart part = (meta & 8) != 0 ? BathPart.HEAD : BathPart.FOOT;
        return this.getDefaultState().withProperty(FACING, facing).withProperty(FILLED, filled).withProperty(PART, part);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(FILLED)) {
            meta |= 4;
        }
        if (state.getValue(PART) == BathPart.HEAD) {
            meta |= 8;
        }
        return meta;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote) {
            EnumFacing facing = state.getValue(FACING);
            BlockPos headPos = pos.offset(facing);
            if (worldIn.getBlockState(headPos).getBlock().isReplaceable(worldIn, headPos)) {
                worldIn.setBlockState(headPos, state.withProperty(PART, BathPart.HEAD).withProperty(FILLED, false), 3);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        BathPart part = state.getValue(PART);
        BlockPos otherPos = (part == BathPart.FOOT) ? pos.offset(facing) : pos.offset(facing.getOpposite());
        IBlockState otherState = worldIn.getBlockState(otherPos);
        if (otherState.getBlock() == this && otherState.getValue(PART) != part) {
            worldIn.setBlockToAir(otherPos);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(PART) == BathPart.HEAD ? Items.AIR : super.getItemDropped(state, rand, fortune);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack held = playerIn.getHeldItem(hand);
        BathPart part = state.getValue(PART);
        EnumFacing bathFacing = state.getValue(FACING);
        BlockPos otherPos = (part == BathPart.FOOT) ? pos.offset(bathFacing) : pos.offset(bathFacing.getOpposite());
        IBlockState otherState = worldIn.getBlockState(otherPos);

        if (!held.isEmpty()) {
            boolean isFilled = state.getValue(FILLED);
            if (held.getItem() == Items.WATER_BUCKET && !isFilled) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
                }
                worldIn.setBlockState(pos, state.withProperty(FILLED, true), 3);
                if (otherState.getBlock() == this) {
                    worldIn.setBlockState(otherPos, otherState.withProperty(FILLED, true), 3);
                }
                worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            } else if (held.getItem() == Items.BUCKET && isFilled) {
                if (!playerIn.capabilities.isCreativeMode) {
                    held.shrink(1);
                    if (held.isEmpty()) {
                        playerIn.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                    } else if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                        playerIn.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                    }
                }
                worldIn.setBlockState(pos, state.withProperty(FILLED, false), 3);
                if (otherState.getBlock() == this) {
                    worldIn.setBlockState(otherPos, otherState.withProperty(FILLED, false), 3);
                }
                worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }

        if (!worldIn.isRemote) {
            EntitySeat.sitOnBlock(worldIn, pos, playerIn, 0.25D);
        }
        return true;
    }

    public enum BathPart implements IStringSerializable {
        FOOT("foot"),
        HEAD("head");

        private final String name;

        BathPart(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
