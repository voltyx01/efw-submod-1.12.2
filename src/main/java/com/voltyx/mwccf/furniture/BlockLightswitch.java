package com.voltyx.mwccf.furniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightswitch extends BlockFurnitureHorizontal {

    public static final PropertyBool POWERED = PropertyBool.create("powered");
    protected static final AxisAlignedBB[] AABBS = FurnitureAABB.createRotated(new AxisAlignedBB(0.3125D, 0.25D, 0.0D, 0.6875D, 0.75D, 0.125D));

    public BlockLightswitch(String name) {
        super(Material.CIRCUITS);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FurnitureAABB.get(AABBS, state.getValue(FACING));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, net.minecraft.entity.EntityLivingBase placer, EnumHand hand) {
        if (facing.getAxis().isHorizontal()) {
            return this.getDefaultState().withProperty(FACING, facing.getOpposite()).withProperty(POWERED, false);
        }
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(POWERED, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING, POWERED });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);
        boolean powered = (meta & 4) != 0;
        return this.getDefaultState().withProperty(FACING, facing).withProperty(POWERED, powered);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(POWERED)) {
            meta |= 4;
        }
        return meta;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public net.minecraft.tileentity.TileEntity createTileEntity(World world, IBlockState state) {
        return new com.voltyx.mwccf.furniture.tileentity.TileEntityLightswitch();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, net.minecraft.entity.EntityLivingBase placer, net.minecraft.item.ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote) {
            net.minecraft.tileentity.TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityLightswitch) {
                ((com.voltyx.mwccf.furniture.tileentity.TileEntityLightswitch) te).initFromPlacedStack(stack, pos);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        net.minecraft.item.ItemStack held = playerIn.getHeldItem(hand);
        net.minecraft.tileentity.TileEntity te = worldIn.getTileEntity(pos);
        com.voltyx.mwccf.furniture.tileentity.TileEntityLightswitch switchTe =
                (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityLightswitch) ?
                (com.voltyx.mwccf.furniture.tileentity.TileEntityLightswitch) te : null;

        if (playerIn.isSneaking() && held.isEmpty()) {
            if (!worldIn.isRemote && switchTe != null) {
                int count = switchTe.getLinkedOffsets().size();
                playerIn.sendStatusMessage(new net.minecraft.util.text.TextComponentTranslation(
                        "message.mwccf.lightswitch.count", count), true);
            }
            return true;
        }

        boolean powered = !state.getValue(POWERED);
        worldIn.setBlockState(pos, state.withProperty(POWERED, powered), 3);
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.playSound(null, pos, FurnitureSounds.BLOCK_LIGHTSWITCH_FLICK, SoundCategory.BLOCKS, 1.0F, powered ? 1.0F : 0.8F);

        if (!worldIn.isRemote) {
            if (switchTe != null) {
                switchTe.toggleLights(powered);
            }
        }
        return true;
    }
}
