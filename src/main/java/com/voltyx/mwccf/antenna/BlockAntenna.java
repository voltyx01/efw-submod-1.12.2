package com.voltyx.mwccf.antenna;

import com.voltyx.mwccf.furniture.BlockFurnitureHorizontal;
import com.voltyx.mwccf.furniture.FurnitureCreativeTab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAntenna extends BlockFurnitureHorizontal {

    private static final AxisAlignedBB AABB_BOX = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

    public BlockAntenna(String name) {
        super(Material.IRON);
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("mwccf." + name);
        this.setHardness(3.5F);
        this.setResistance(15.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(FurnitureCreativeTab.INSTANCE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_BOX;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AABB_BOX;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityAntenna();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) {
            return true;
        }

        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityAntenna) {
            TileEntityAntenna antenna = (TileEntityAntenna) te;

            if (worldIn.isRemote) {
                if (com.voltyx.mwccf.antenna.client.AntennaCameraController.isActive()) {
                    com.voltyx.mwccf.antenna.client.AntennaCameraController.close();
                } else {
                    com.voltyx.mwccf.antenna.client.AntennaCameraController.open(pos, state.getValue(FACING), antenna);
                }
            } else {
                antenna.setActivePlayer(playerIn);
            }
            return true;
        }
        return false;
    }
}
