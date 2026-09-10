package com.voltyx.mwccf.terminal;

import com.voltyx.mwccf.furniture.BlockFurnitureHorizontal;
import com.voltyx.mwccf.furniture.FurnitureCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTerminal extends BlockFurnitureHorizontal {

    // Hitbox corresponds strictly to the 'display' cube group from terminal.geo.json
    // Display cube: Origin: [-8, 4, 7], Size: [16, 11, 1]
    private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0, 0.25, 0.9375, 1.0, 0.9375, 1.0);
    private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0, 0.25, 0.0, 1.0, 0.9375, 0.0625);
    private static final AxisAlignedBB AABB_WEST  = new AxisAlignedBB(0.9375, 0.25, 0.0, 1.0, 0.9375, 1.0);
    private static final AxisAlignedBB AABB_EAST  = new AxisAlignedBB(0.0, 0.25, 0.0, 0.0625, 0.9375, 1.0);

    public BlockTerminal(String name) {
        super(Material.IRON);
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("mwccf." + name);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(FurnitureCreativeTab.INSTANCE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        switch (facing) {
            case NORTH: return AABB_NORTH;
            case SOUTH: return AABB_SOUTH;
            case WEST:  return AABB_WEST;
            case EAST:  return AABB_EAST;
            default:    return FULL_BLOCK_AABB;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return this.getBoundingBox(blockState, worldIn, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return this.getBoundingBox(state, worldIn, pos).offset(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityTerminal();
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

    private boolean isValidWall(World world, BlockPos pos, EnumFacing side) {
        if (side == null || !side.getAxis().isHorizontal()) {
            return false;
        }
        BlockPos wallPos = pos.offset(side.getOpposite());
        IBlockState wallState = world.getBlockState(wallPos);
        return world.isSideSolid(wallPos, side, true) && wallState.isOpaqueCube() && wallState.isFullCube();
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return isValidWall(worldIn, pos, side);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing side : EnumFacing.HORIZONTALS) {
            if (isValidWall(worldIn, pos, side)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (isValidWall(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        for (EnumFacing side : EnumFacing.HORIZONTALS) {
            if (isValidWall(worldIn, pos, side)) {
                return this.getDefaultState().withProperty(FACING, side);
            }
        }
        return this.getDefaultState();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        EnumFacing facing = state.getValue(FACING);
        if (!isValidWall(worldIn, pos, facing)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // Prevent duplicate trigger from off-hand
        if (hand != EnumHand.MAIN_HAND) {
            return true;
        }

        int playerBlockY = MathHelper.floor(playerIn.posY);
        if (pos.getY() != playerBlockY + 1) {
            return false;
        }

        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityTerminal) {
            TileEntityTerminal terminal = (TileEntityTerminal) te;
            if (worldIn.isRemote) {
                if (com.voltyx.mwccf.terminal.client.TerminalCameraController.isActive()) {
                    com.voltyx.mwccf.terminal.client.TerminalCameraController.close();
                } else {
                    com.voltyx.mwccf.terminal.client.TerminalCameraController.open(pos, state.getValue(FACING), terminal);
                }
            }
            terminal.toggleOpen(playerIn);
            return true;
        }
        return false;
    }
}
