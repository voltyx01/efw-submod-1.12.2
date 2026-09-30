package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBath extends BlockFurnitureHorizontal {

    public static final PropertyBool FILLED = PropertyBool.create("filled");

    public BlockBath(String name) {
        super(Material.ROCK);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FILLED, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING, FILLED });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);
        boolean filled = (meta & 4) != 0;
        return this.getDefaultState().withProperty(FACING, facing).withProperty(FILLED, filled);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(FILLED)) {
            meta |= 4;
        }
        return meta;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack held = playerIn.getHeldItem(hand);

        if (!held.isEmpty()) {
            boolean isFilled = state.getValue(FILLED);
            if (held.getItem() == Items.WATER_BUCKET && !isFilled) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
                }
                worldIn.setBlockState(pos, state.withProperty(FILLED, true), 3);
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
                worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }

        if (!worldIn.isRemote) {
            EntitySeat.sitOnBlock(worldIn, pos, playerIn, 0.25D);
        }
        return true;
    }
}
