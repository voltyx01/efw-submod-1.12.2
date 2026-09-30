package com.voltyx.mwccf.furniture;

import com.voltyx.mwccf.furniture.tileentity.TileEntityCabinet;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockRecycleBin extends BlockFurnitureHorizontal {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.875D, 0.8125D);

    public BlockRecycleBin(String name) {
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
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCabinet(9);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack held = playerIn.getHeldItem(hand);
        if (playerIn.isSneaking() && !held.isEmpty()) {
            if (!worldIn.isRemote) {
                held.shrink(1);
                worldIn.playSound(null, pos, FurnitureSounds.BLOCK_RECYCLE_BIN_ENGINE, SoundCategory.BLOCKS, 0.75F, 1.0F);
                if (worldIn instanceof WorldServer) {
                    ((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5D, pos.getY() + 0.9D, pos.getZ() + 0.5D, 10, 0.15D, 0.1D, 0.15D, 0.02D);
                }
            }
            return true;
        }

        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityCabinet) {
                worldIn.playSound(null, pos, FurnitureSounds.BLOCK_CABINET_OPEN, SoundCategory.BLOCKS, 0.8F, 1.0F);
                playerIn.displayGUIChest((TileEntityCabinet) te);
            }
        }
        return true;
    }
}
