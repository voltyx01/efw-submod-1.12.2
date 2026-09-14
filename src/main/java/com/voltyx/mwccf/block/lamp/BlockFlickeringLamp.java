package com.voltyx.mwccf.block.lamp;

import com.voltyx.mwccf.furniture.FurnitureCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFlickeringLamp extends Block {

    public static final PropertyBool POWERED = PropertyBool.create("powered");

    private final LampFlickerType flickerType;

    public BlockFlickeringLamp(String name, LampFlickerType flickerType) {
        super(Material.REDSTONE_LIGHT);
        this.flickerType = flickerType;
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)));
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("mwccf." + name);
        this.setHardness(0.3F);
        this.setLightLevel(1.0F); // Register as light emitter up to level 15
        this.setSoundType(SoundType.GLASS);
        this.setCreativeTab(FurnitureCreativeTab.INSTANCE);
    }

    public LampFlickerType getFlickerType() {
        return this.flickerType;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityFlickeringLamp();
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
        return true;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(POWERED).booleanValue() ? 0 : 15;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state.getValue(POWERED).booleanValue()) {
            return 0;
        }
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityFlickeringLamp) {
            return ((TileEntityFlickeringLamp) te).getEmittedLight();
        }
        return 15;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            boolean powered = worldIn.isBlockPowered(pos);
            if (state.getValue(POWERED).booleanValue() != powered) {
                worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(powered)), 3);
            }
        }
        worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean powered = worldIn.isBlockPowered(pos);
        if (state.getValue(POWERED).booleanValue() != powered) {
            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(powered)), 3);
            worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWERED, Boolean.valueOf((meta & 1) != 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED).booleanValue() ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED);
    }
}
