package com.voltyx.mwccf.furniture.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;

public class TileEntityMicrowave extends TileEntityLockableLoot implements ITickable {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    public int cookTime = 0;
    public int totalCookTime = 100;
    private int prevCookTime = 0;

    @Override
    public int getSizeInventory() {
        return 2;
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
        return this.hasCustomName() ? this.customName : "container.refurbished_furniture.microwave";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventory);
        }
        this.cookTime = compound.getInteger("CookTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("CookTime", this.cookTime);
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
        return new ContainerMicrowave(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return "mwccf:microwave";
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public void update() {
        if (!this.world.isRemote) {
            boolean wasCooking = this.cookTime > 0;
            if (this.canSmelt()) {
                ++this.cookTime;
                if (this.cookTime >= this.totalCookTime) {
                    this.cookTime = 0;
                    this.smeltItem();
                    this.markDirty();
                    this.world.playSound(null, this.pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.2F);
                }
            } else {
                this.cookTime = 0;
            }

            boolean isCooking = this.cookTime > 0;
            if (wasCooking != isCooking || (isCooking && Math.abs(this.cookTime - this.prevCookTime) >= 20)) {
                this.prevCookTime = this.cookTime;
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            }
        }
    }

    private boolean canSmelt() {
        if (this.inventory.get(0).isEmpty()) {
            return false;
        } else {
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(this.inventory.get(0));
            if (result.isEmpty()) return false;
            ItemStack output = this.inventory.get(1);
            if (output.isEmpty()) return true;
            if (!output.isItemEqual(result)) return false;
            int res = output.getCount() + result.getCount();
            return res <= getInventoryStackLimit() && res <= output.getMaxStackSize();
        }
    }

    private void smeltItem() {
        if (this.canSmelt()) {
            ItemStack input = this.inventory.get(0);
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
            ItemStack output = this.inventory.get(1);

            if (output.isEmpty()) {
                this.inventory.set(1, result.copy());
            } else if (output.getItem() == result.getItem()) {
                output.grow(result.getCount());
            }

            input.shrink(1);
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
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
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }
}
