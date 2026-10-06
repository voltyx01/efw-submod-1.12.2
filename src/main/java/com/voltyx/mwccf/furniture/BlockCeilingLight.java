package com.voltyx.mwccf.furniture;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCeilingLight extends Block {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool LIT = PropertyBool.create("lit");

    protected static final AxisAlignedBB AABB_CEILING = new AxisAlignedBB(0.3125D, 0.8125D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
    protected static final AxisAlignedBB AABB_FLOOR   = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.1875D, 0.6875D);
    protected static final AxisAlignedBB AABB_NORTH   = new AxisAlignedBB(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 0.1875D);
    protected static final AxisAlignedBB AABB_SOUTH   = new AxisAlignedBB(0.3125D, 0.3125D, 0.8125D, 0.6875D, 0.6875D, 1.0D);
    protected static final AxisAlignedBB AABB_WEST    = new AxisAlignedBB(0.0D, 0.3125D, 0.3125D, 0.1875D, 0.6875D, 0.6875D);
    protected static final AxisAlignedBB AABB_EAST    = new AxisAlignedBB(0.8125D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D);

    public BlockCeilingLight(String name) {
        super(Material.GLASS);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.GLASS);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.DOWN)
                .withProperty(LIT, true));
        this.setCreativeTab(FurnitureCreativeTab.INSTANCE);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        switch (facing) {
            case DOWN:
                return AABB_CEILING;
            case UP:
                return AABB_FLOOR;
            case NORTH:
                return AABB_NORTH;
            case SOUTH:
                return AABB_SOUTH;
            case WEST:
                return AABB_WEST;
            case EAST:
                return AABB_EAST;
            default:
                return AABB_CEILING;
        }
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing attach = (facing == EnumFacing.DOWN || facing == EnumFacing.UP) ? facing : facing.getOpposite();
        return this.getDefaultState().withProperty(FACING, attach).withProperty(LIT, true);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING, LIT });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byIndex(meta & 7);
        if (facing.getIndex() > 5) facing = EnumFacing.DOWN;
        boolean lit = (meta & 8) != 0;
        return this.getDefaultState().withProperty(FACING, facing).withProperty(LIT, lit);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getIndex();
        if (state.getValue(LIT)) {
            meta |= 8;
        }
        return meta;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        boolean lit = state.getValue(LIT);
        worldIn.setBlockState(pos, state.withProperty(LIT, !lit), 3);
        worldIn.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.5F, lit ? 0.6F : 0.8F);
        return true;
    }
}
