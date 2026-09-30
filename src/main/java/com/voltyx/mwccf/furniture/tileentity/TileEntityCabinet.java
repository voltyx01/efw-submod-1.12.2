package com.voltyx.mwccf.furniture.tileentity;

import com.voltyx.mwccf.furniture.BlockCooler;
import com.voltyx.mwccf.furniture.BlockCrate;
import com.voltyx.mwccf.furniture.BlockKitchenCabinetry;
import com.voltyx.mwccf.furniture.BlockKitchenStorageCabinet;
import com.voltyx.mwccf.furniture.BlockPostBox;
import com.voltyx.mwccf.furniture.BlockRecycleBin;
import com.voltyx.mwccf.furniture.FurnitureSounds;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class TileEntityCabinet extends TileEntityLockableLoot {

    private int customSize = 27;
    private NonNullList<ItemStack> inventory;
    public int numPlayersUsing = 0;

    public TileEntityCabinet() {
        this.inventory = NonNullList.withSize(27, ItemStack.EMPTY);
    }

    public TileEntityCabinet(int size) {
        this.customSize = size;
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int getSizeInventory() {
        if (this.world != null) {
            Block block = this.world.getBlockState(this.pos).getBlock();
            if (block instanceof BlockCooler) return 18;
            if (block instanceof BlockPostBox) return 9;
            if (block instanceof BlockRecycleBin) return 9;
            if (block instanceof BlockKitchenCabinetry) return 18;
            if (block instanceof BlockCrate) return 27;
            if (block instanceof BlockKitchenStorageCabinet) return 27;
        }
        return this.customSize;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.getItems()) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public String getName() {
        if (this.hasCustomName()) return this.customName;
        if (this.world != null) {
            Block block = this.world.getBlockState(this.pos).getBlock();
            if (block instanceof BlockCooler) return "container.refurbished_furniture.cooler";
            if (block instanceof BlockCrate) return "container.refurbished_furniture.crate";
            if (block instanceof BlockPostBox) return "container.refurbished_furniture.post_box";
            if (block instanceof BlockRecycleBin) return "container.refurbished_furniture.recycle_bin";
            if (block instanceof BlockKitchenCabinetry) return "container.refurbished_furniture.kitchen_drawer";
            if (block instanceof BlockKitchenStorageCabinet) return "container.refurbished_furniture.storage_cabinet";
        }
        return "container.refurbished_furniture.cabinet";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("CustomSize", 3)) {
            this.customSize = compound.getInteger("CustomSize");
        }
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventory);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("CustomSize", this.getSizeInventory());
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.getItems());
        }
        return compound;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new net.minecraft.inventory.ContainerChest(playerInventory, this, playerIn);
    }

    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        int expectedSize = this.getSizeInventory();
        if (this.inventory == null || this.inventory.size() != expectedSize) {
            NonNullList<ItemStack> newInv = NonNullList.withSize(expectedSize, ItemStack.EMPTY);
            if (this.inventory != null) {
                for (int i = 0; i < Math.min(this.inventory.size(), expectedSize); i++) {
                    newInv.set(i, this.inventory.get(i));
                }
            }
            this.inventory = newInv;
        }
        return this.inventory;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            this.numPlayersUsing++;
            if (this.world != null) {
                this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
                this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
                setBlockOpen(true);
            }
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            this.numPlayersUsing--;
            if (this.world != null) {
                this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
                this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
                if (this.numPlayersUsing <= 0) {
                    this.numPlayersUsing = 0;
                    setBlockOpen(false);
                }
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world == null || this.isInvalid()) return false;
        return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean shouldRefresh(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public net.minecraft.network.play.server.SPacketUpdateTileEntity getUpdatePacket() {
        return new net.minecraft.network.play.server.SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    private void setBlockOpen(boolean open) {
        if (this.world != null && !this.world.isRemote) {
            IBlockState state = this.world.getBlockState(this.pos);
            Block block = state.getBlock();
            IProperty<?> prop = null;
            for (IProperty<?> p : state.getPropertyKeys()) {
                if ("open".equals(p.getName()) && p.getValueClass() == Boolean.class) {
                    prop = p;
                    break;
                }
            }
            if (prop != null) {
                @SuppressWarnings("unchecked")
                PropertyBool boolProp = (PropertyBool) prop;
                if (state.getValue(boolProp) != open) {
                    this.world.setBlockState(this.pos, state.withProperty(boolProp, open), 2);
                    SoundEvent sound = null;
                    if (block instanceof BlockCooler) {
                        sound = open ? FurnitureSounds.BLOCK_COOLER_OPEN : FurnitureSounds.BLOCK_COOLER_CLOSE;
                    } else if (block instanceof BlockKitchenCabinetry) {
                        sound = open ? FurnitureSounds.BLOCK_KITCHEN_DRAWER_OPEN : FurnitureSounds.BLOCK_KITCHEN_DRAWER_CLOSE;
                    } else {
                        sound = open ? FurnitureSounds.BLOCK_CABINET_OPEN : FurnitureSounds.BLOCK_CABINET_CLOSE;
                    }
                    if (sound != null) {
                        this.world.playSound(null, this.pos, sound, SoundCategory.BLOCKS, 0.8F, 1.0F);
                    }
                }
            }
        }
    }
}
