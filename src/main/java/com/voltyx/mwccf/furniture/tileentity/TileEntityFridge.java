package com.voltyx.mwccf.furniture.tileentity;

import com.voltyx.mwccf.furniture.FurnitureSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityFridge extends TileEntityLockableLoot implements ITickable {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(15, ItemStack.EMPTY);
    public float doorAngle = 0.0F;
    public float prevDoorAngle = 0.0F;
    public int numPlayersUsing = 0;

    @Override
    public int getSizeInventory() {
        return 15;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.refurbished_furniture.fridge";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventory);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.inventory);
        }
        return compound;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        this.fillWithLoot(playerIn);
        return new ContainerFridge(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return "mwccf:fridge";
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public void update() {
        this.prevDoorAngle = this.doorAngle;
        if (this.numPlayersUsing > 0 && this.doorAngle < 1.0F) {
            this.doorAngle = Math.min(1.0F, this.doorAngle + 0.1F);
        } else if (this.numPlayersUsing == 0 && this.doorAngle > 0.0F) {
            this.doorAngle = Math.max(0.0F, this.doorAngle - 0.1F);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) this.numPlayersUsing = 0;
            this.numPlayersUsing++;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.playSound(null, this.pos, FurnitureSounds.BLOCK_FRIDGE_OPEN, SoundCategory.BLOCKS, 0.75F, 1.0F);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            this.numPlayersUsing--;
            if (this.numPlayersUsing < 0) this.numPlayersUsing = 0;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.playSound(null, this.pos, FurnitureSounds.BLOCK_FRIDGE_CLOSE, SoundCategory.BLOCKS, 0.75F, 1.0F);
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world == null || this.isInvalid()) return false;
        return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean shouldRefresh(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState oldState, net.minecraft.block.state.IBlockState newState) {
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
}
