package com.voltyx.mwccf.furniture;

import com.voltyx.mwccf.furniture.tileentity.TileEntityStorageJar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStorageJar extends BlockFurnitureHorizontal {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.625D, 0.75D);

    public BlockStorageJar(String name) {
        super(Material.GLASS);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.GLASS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityStorageJar();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileEntityStorageJar)) return true;
        TileEntityStorageJar jar = (TileEntityStorageJar) te;

        ItemStack held = playerIn.getHeldItem(hand);

        if (!jar.isEmpty()) {
            if (playerIn.isSneaking() || held.isEmpty()) {
                if (!worldIn.isRemote) {
                    ItemStack extracted = jar.getItem().copy();
                    jar.setItem(ItemStack.EMPTY);
                    if (!playerIn.inventory.addItemStackToInventory(extracted)) {
                        playerIn.dropItem(extracted, false);
                    }
                    worldIn.playSound(null, pos, FurnitureSounds.BLOCK_STORAGE_JAR_INSERT, SoundCategory.BLOCKS, 0.8F, 1.2F);
                }
                return true;
            } else if (ItemStack.areItemsEqual(held, jar.getItem()) && ItemStack.areItemStackTagsEqual(held, jar.getItem())) {
                int space = jar.getItem().getMaxStackSize() - jar.getItem().getCount();
                if (space > 0) {
                    int toAdd = Math.min(space, held.getCount());
                    if (!worldIn.isRemote) {
                        jar.getItem().grow(toAdd);
                        held.shrink(toAdd);
                        jar.setItem(jar.getItem());
                        worldIn.playSound(null, pos, FurnitureSounds.BLOCK_STORAGE_JAR_INSERT, SoundCategory.BLOCKS, 0.8F, 1.0F);
                    }
                    return true;
                }
            }
        } else if (!held.isEmpty()) {
            if (!worldIn.isRemote) {
                ItemStack inserted = held.copy();
                jar.setItem(inserted);
                held.setCount(0);
                worldIn.playSound(null, pos, FurnitureSounds.BLOCK_STORAGE_JAR_INSERT, SoundCategory.BLOCKS, 0.8F, 1.0F);
            }
            return true;
        }

        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityStorageJar) {
            TileEntityStorageJar jar = (TileEntityStorageJar) te;
            if (!jar.isEmpty()) {
                spawnAsEntity(worldIn, pos, jar.getItem());
            }
        }
        super.breakBlock(worldIn, pos, state);
    }
}
