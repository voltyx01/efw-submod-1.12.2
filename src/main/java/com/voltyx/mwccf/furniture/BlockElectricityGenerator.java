package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockElectricityGenerator extends BlockFurnitureHorizontal {

    public BlockElectricityGenerator(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(3.0F);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public net.minecraft.tileentity.TileEntity createTileEntity(World world, IBlockState state) {
        return new com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator();
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        net.minecraft.tileentity.TileEntity te = blockAccess.getTileEntity(pos);
        if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator) {
            return ((com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator) te).isGeneratingPower() ? 15 : 0;
        }
        return 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return getWeakPower(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(com.voltyx.mwccf.MwccfMod.instance, com.voltyx.mwccf.furniture.client.gui.FurnitureGuiHandler.GUI_ELECTRICITY_GENERATOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        net.minecraft.tileentity.TileEntity te = worldIn.getTileEntity(pos);
        boolean active = (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator)
                && ((com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator) te).isGeneratingPower();
        if (active && rand.nextInt(3) == 0) {
            double x = pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.3D;
            double y = pos.getY() + 0.9D;
            double z = pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.3D;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.05D, 0.0D);
        }
    }
}
