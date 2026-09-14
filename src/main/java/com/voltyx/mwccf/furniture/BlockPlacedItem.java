package com.voltyx.mwccf.furniture;

import com.voltyx.mwccf.furniture.tileentity.TileEntityPlacedItem;
import com.voltyx.mwccf.item.ItemPlacerTool;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPlacedItem extends BlockContainer {

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.25D, 0.9D);

    public BlockPlacedItem(String registryName) {
        super(Material.CIRCUITS);
        setRegistryName(registryName);
        this.setTranslationKey("mwccf." + registryName);
        setHardness(0.2F);
        setResistance(1.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPlacedItem();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        TileEntity te = source.getTileEntity(pos);
        if (te instanceof TileEntityPlacedItem) {
            TileEntityPlacedItem placed = (TileEntityPlacedItem) te;
            double ox = placed.getOffsetX();
            double oy = placed.getOffsetY();
            double oz = placed.getOffsetZ();
            float scale = placed.getScale();
            double halfXZ = Math.max(0.15D, 0.35D * scale);
            double hY = Math.max(0.15D, 0.35D * scale);
            return new AxisAlignedBB(
                    Math.max(0.0D, ox - halfXZ),
                    Math.max(0.0D, oy - 0.05D),
                    Math.max(0.0D, oz - halfXZ),
                    Math.min(1.0D, ox + halfXZ),
                    Math.min(1.0D, oy + hY),
                    Math.min(1.0D, oz + halfXZ)
            );
        }
        return BOUNDS;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) {
            return false;
        }

        ItemStack held = player.getHeldItem(hand);
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityPlacedItem)) {
            return false;
        }

        TileEntityPlacedItem placedItem = (TileEntityPlacedItem) te;

        // If holding placer tool: open settings GUI
        if (!held.isEmpty() && held.getItem() instanceof ItemPlacerTool) {
            if (world.isRemote) {
                openConfigGui(placedItem);
            }
            return true;
        }

        // Survival / Normal interaction: pick up item
        if (placedItem.isLocked() && !player.capabilities.isCreativeMode) {
            return false;
        }

        if (!world.isRemote) {
            ItemStack item = placedItem.getStack();
            if (!item.isEmpty()) {
                ItemStack toPickup = item.copy();
                placedItem.setStack(ItemStack.EMPTY);
                if (!player.inventory.addItemStackToInventory(toPickup)) {
                    EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.2D, pos.getZ() + 0.5D, toPickup);
                    world.spawnEntity(entityItem);
                }
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                        ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
            world.setBlockToAir(pos);
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    private void openConfigGui(TileEntityPlacedItem placedItem) {
        net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(
                new com.voltyx.mwccf.furniture.client.gui.GuiPlacedItemConfig(placedItem)
        );
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityPlacedItem && !world.isRemote) {
            TileEntityPlacedItem placedItem = (TileEntityPlacedItem) te;
            ItemStack item = placedItem.getStack();
            if (!item.isEmpty() && !placedItem.isLocked()) {
                placedItem.setStack(ItemStack.EMPTY);
                EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.2D, pos.getZ() + 0.5D, item);
                world.spawnEntity(entityItem);
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }
}
