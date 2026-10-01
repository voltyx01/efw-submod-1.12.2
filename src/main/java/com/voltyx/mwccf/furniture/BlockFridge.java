package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.voltyx.mwccf.furniture.tileentity.TileEntityFridge;

import java.util.Random;

public class BlockFridge extends BlockFurnitureHorizontal {

    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<FridgePart> PART = PropertyEnum.create("part", FridgePart.class);

    public BlockFridge(String name) {
        super(Material.IRON);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(3.5F);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(OPEN, false)
                .withProperty(PART, FridgePart.LOWER));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING, OPEN, PART });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);
        boolean open = (meta & 4) != 0;
        FridgePart part = (meta & 8) != 0 ? FridgePart.UPPER : FridgePart.LOWER;
        return this.getDefaultState().withProperty(FACING, facing).withProperty(OPEN, open).withProperty(PART, part);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(OPEN)) {
            meta |= 4;
        }
        if (state.getValue(PART) == FridgePart.UPPER) {
            meta |= 8;
        }
        return meta;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote) {
            BlockPos upperPos = pos.up();
            if (worldIn.getBlockState(upperPos).getBlock().isReplaceable(worldIn, upperPos)) {
                worldIn.setBlockState(upperPos, state.withProperty(PART, FridgePart.UPPER).withProperty(OPEN, false), 3);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        FridgePart part = state.getValue(PART);
        BlockPos otherPos = (part == FridgePart.LOWER) ? pos.up() : pos.down();
        IBlockState otherState = worldIn.getBlockState(otherPos);
        if (otherState.getBlock() == this && otherState.getValue(PART) != part) {
            worldIn.setBlockToAir(otherPos);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(PART) == FridgePart.UPPER ? Items.AIR : super.getItemDropped(state, rand, fortune);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(PART) == FridgePart.LOWER;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(PART) == FridgePart.LOWER ? new TileEntityFridge() : null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            BlockPos tePos = (state.getValue(PART) == FridgePart.LOWER) ? pos : pos.down();
            playerIn.openGui(com.voltyx.mwccf.MwccfMod.instance, com.voltyx.mwccf.furniture.client.gui.FurnitureGuiHandler.GUI_FRIDGE, worldIn, tePos.getX(), tePos.getY(), tePos.getZ());
        }
        return true;
    }

    public enum FridgePart implements IStringSerializable {
        LOWER("lower"),
        UPPER("upper");

        private final String name;

        FridgePart(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
