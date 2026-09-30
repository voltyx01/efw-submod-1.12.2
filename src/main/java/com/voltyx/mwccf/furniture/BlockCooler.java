package com.voltyx.mwccf.furniture;

import com.voltyx.mwccf.furniture.tileentity.TileEntityCabinet;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;

public class BlockCooler extends BlockFurnitureHorizontal {

    public static final PropertyBool OPEN = PropertyBool.create("open");
    protected static final AxisAlignedBB AABB_NS = new AxisAlignedBB(0.125D, 0.0D, 0.1875D, 0.875D, 0.625D, 0.8125D);
    protected static final AxisAlignedBB AABB_EW = new AxisAlignedBB(0.1875D, 0.0D, 0.125D, 0.8125D, 0.625D, 0.875D);

    public BlockCooler(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(1.5F);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        return (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) ? AABB_NS : AABB_EW;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING, OPEN });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);
        boolean open = (meta & 4) != 0;
        return this.getDefaultState().withProperty(FACING, facing).withProperty(OPEN, open);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(OPEN)) {
            meta |= 4;
        }
        return meta;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCabinet(18);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityCabinet) {
                playerIn.displayGUIChest((TileEntityCabinet) te);
            }
        }
        return true;
    }
}
