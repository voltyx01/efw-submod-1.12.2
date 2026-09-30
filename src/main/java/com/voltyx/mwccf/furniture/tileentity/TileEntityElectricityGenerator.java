package com.voltyx.mwccf.furniture.tileentity;

import com.voltyx.mwccf.furniture.FurnitureSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityElectricityGenerator extends TileEntityLockableLoot implements ITickable {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    public int burnTime = 0;
    public int currentItemBurnTime = 0;

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.get(0).isEmpty();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.refurbished_furniture.electricity_generator";
    }

    public boolean isGeneratingPower() {
        return this.burnTime > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventory);
        }
        this.burnTime = compound.getInteger("BurnTime");
        this.currentItemBurnTime = compound.getInteger("CurrentItemBurnTime");
        if (this.currentItemBurnTime == 0 && this.burnTime > 0) {
            this.currentItemBurnTime = this.burnTime;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.burnTime);
        compound.setInteger("CurrentItemBurnTime", this.currentItemBurnTime);
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
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0 && TileEntityFurnace.isItemFuel(stack);
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerElectricityGenerator(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return "mwccf:electricity_generator";
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public void update() {
        boolean wasGenerating = this.isGeneratingPower();
        boolean dirty = false;

        if (this.burnTime > 0) {
            --this.burnTime;
        }

        if (!this.world.isRemote) {
            ItemStack fuel = this.inventory.get(0);
            if (this.burnTime <= 0 && !fuel.isEmpty()) {
                int itemBurn = TileEntityFurnace.getItemBurnTime(fuel);
                if (itemBurn > 0) {
                    this.currentItemBurnTime = this.burnTime = itemBurn;
                    fuel.shrink(1);
                    dirty = true;
                }
            }

            if (wasGenerating != this.isGeneratingPower()) {
                dirty = true;
                this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            }

            if (dirty) {
                this.markDirty();
            }
        } else {
            if (this.isGeneratingPower() && this.world.rand.nextInt(3) == 0) {
                double px = this.pos.getX() + 0.5D + (this.world.rand.nextDouble() - 0.5D) * 0.2D;
                double py = this.pos.getY() + 1.05D;
                double pz = this.pos.getZ() + 0.5D + (this.world.rand.nextDouble() - 0.5D) * 0.2D;
                this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, px, py, pz, 0.0D, 0.05D, 0.0D);
            }
        }

        if (this.isGeneratingPower() && this.world.getTotalWorldTime() % 40 == 0) {
            this.world.playSound(null, this.pos, FurnitureSounds.BLOCK_ELECTRICITY_GENERATOR_ENGINE, SoundCategory.BLOCKS, 0.6F, 1.0F);
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world == null || this.isInvalid()) return false;
        return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }
}
