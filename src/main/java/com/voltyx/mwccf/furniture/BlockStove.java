package com.voltyx.mwccf.furniture;

import com.voltyx.mwccf.furniture.tileentity.TileEntityStove;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockStove extends BlockFurnitureHorizontal {

    public BlockStove(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(3.5F);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityStove();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(com.voltyx.mwccf.MwccfMod.instance, com.voltyx.mwccf.furniture.client.gui.FurnitureGuiHandler.GUI_STOVE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityStove && ((TileEntityStove) te).isBurning()) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 1.02D;
            double z = pos.getZ() + 0.5D;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + (rand.nextDouble() - 0.5D) * 0.4D, y, z + (rand.nextDouble() - 0.5D) * 0.4D, 0.0D, 0.02D, 0.0D);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, x + (rand.nextDouble() - 0.5D) * 0.3D, y, z + (rand.nextDouble() - 0.5D) * 0.3D, 0.0D, 0.01D, 0.0D);
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }
}
