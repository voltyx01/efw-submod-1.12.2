package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBasin extends BlockFurnitureHorizontal {

    protected static final AxisAlignedBB[] AABBS = FurnitureAABB.createRotated(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.875D));

    public BlockBasin(String name) {
        super(Material.WOOD);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, net.minecraft.world.IBlockAccess source, BlockPos pos) {
        return FurnitureAABB.get(AABBS, state.getValue(FACING));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if (!heldItem.isEmpty()) {
            if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                if (!worldIn.isRemote) {
                    heldItem.shrink(1);
                    ItemStack waterBottle = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
                    if (heldItem.isEmpty()) {
                        playerIn.setHeldItem(hand, waterBottle);
                    } else if (!playerIn.inventory.addItemStackToInventory(waterBottle)) {
                        playerIn.dropItem(waterBottle, false);
                    }
                    worldIn.playSound(null, pos, FurnitureSounds.BLOCK_KITCHEN_SINK_FILL, SoundCategory.BLOCKS, 0.8F, 1.0F);
                }
                return true;
            } else if (heldItem.getItem() == Items.BUCKET) {
                if (!worldIn.isRemote) {
                    if (!playerIn.capabilities.isCreativeMode) {
                        heldItem.shrink(1);
                        ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);
                        if (heldItem.isEmpty()) {
                            playerIn.setHeldItem(hand, waterBucket);
                        } else if (!playerIn.inventory.addItemStackToInventory(waterBucket)) {
                            playerIn.dropItem(waterBucket, false);
                        }
                    }
                    worldIn.playSound(null, pos, FurnitureSounds.BLOCK_KITCHEN_SINK_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
