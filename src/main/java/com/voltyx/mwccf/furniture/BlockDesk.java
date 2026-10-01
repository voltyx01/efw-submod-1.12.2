package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockDesk extends BlockFurnitureHorizontal {

    public static final PropertyEnum<DeskType> TYPE = PropertyEnum.create("type", DeskType.class);
    protected static final AxisAlignedBB DESK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockDesk(String name) {
        super(Material.WOOD);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(TYPE, DeskType.SINGLE));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return DESK_AABB;
    }

    private boolean isMatchingDesk(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        IBlockState neighborState = world.getBlockState(pos);
        if (neighborState.getBlock() instanceof BlockDesk) {
            return neighborState.getValue(FACING) == facing;
        }
        return false;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        BlockPos leftPos;
        BlockPos rightPos;

        switch (facing) {
            case NORTH:
                leftPos = pos.west();
                rightPos = pos.east();
                break;
            case SOUTH:
                leftPos = pos.east();
                rightPos = pos.west();
                break;
            case WEST:
                leftPos = pos.south();
                rightPos = pos.north();
                break;
            case EAST:
            default:
                leftPos = pos.north();
                rightPos = pos.south();
                break;
        }

        boolean hasLeft = isMatchingDesk(worldIn, leftPos, facing);
        boolean hasRight = isMatchingDesk(worldIn, rightPos, facing);

        DeskType type;
        if (!hasLeft && !hasRight) {
            type = DeskType.SINGLE;
        } else if (!hasLeft && hasRight) {
            type = DeskType.LEFT;
        } else if (hasLeft && !hasRight) {
            type = DeskType.RIGHT;
        } else {
            type = DeskType.MIDDLE;
        }

        return state.withProperty(TYPE, type);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING, TYPE });
    }

    public enum DeskType implements IStringSerializable {
        SINGLE("single"),
        LEFT("left"),
        RIGHT("right"),
        MIDDLE("middle");

        private final String name;

        DeskType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
