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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

public class BlockKitchenStorageCabinet extends BlockFurnitureHorizontal {

    public static final PropertyBool OPEN = PropertyBool.create("open");
    protected static final AxisAlignedBB[] AABBS = FurnitureAABB.createRotated(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.875D));

    public BlockKitchenStorageCabinet(String name) {
        super(Material.WOOD);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FurnitureAABB.get(AABBS, state.getValue(FACING));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, net.minecraft.entity.EntityLivingBase placer, EnumHand hand) {
        if (facing.getAxis().isHorizontal()) {
            return this.getDefaultState().withProperty(FACING, facing.getOpposite()).withProperty(OPEN, false);
        }
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(OPEN, false);
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
        return new TileEntityCabinet(27);
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
