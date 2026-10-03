package com.voltyx.mwccf.furniture.tileentity;

import com.voltyx.mwccf.furniture.client.gui.GeneratorGuiConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerElectricityGenerator extends Container {

    private final TileEntityElectricityGenerator generatorTile;
    private int burnTime;
    private int currentItemBurnTime;

    public ContainerElectricityGenerator(InventoryPlayer playerInventory, TileEntityElectricityGenerator generatorTile) {
        this.generatorTile = generatorTile;

        GeneratorGuiConfig cfg = GeneratorGuiConfig.get();

        // Fuel Slot
        this.addSlotToContainer(new Slot(generatorTile, 0, cfg.fuelSlotX, cfg.fuelSlotY) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return TileEntityFurnace.isItemFuel(stack);
            }
        });

        // Charging Slot 1
        this.addSlotToContainer(new Slot(generatorTile, 1, cfg.chargeSlot1X, cfg.chargeSlot1Y) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return com.voltyx.mwccf.battery.DeviceBatteryHelper.isChargeableItem(stack);
            }
        });

        // Charging Slot 2
        this.addSlotToContainer(new Slot(generatorTile, 2, cfg.chargeSlot2X, cfg.chargeSlot2Y) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return com.voltyx.mwccf.battery.DeviceBatteryHelper.isChargeableItem(stack);
            }
        });

        // Player Inventory (3 rows x 9)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Player Hotbar (9 slots)
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    private int isGenerating;

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, this.generatorTile.burnTime);
        listener.sendWindowProperty(this, 1, this.generatorTile.currentItemBurnTime);
        listener.sendWindowProperty(this, 2, this.generatorTile.isGeneratingPower() ? 1 : 0);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        int genState = this.generatorTile.isGeneratingPower() ? 1 : 0;
        for (IContainerListener listener : this.listeners) {
            if (this.burnTime != this.generatorTile.burnTime) {
                listener.sendWindowProperty(this, 0, this.generatorTile.burnTime);
            }
            if (this.currentItemBurnTime != this.generatorTile.currentItemBurnTime) {
                listener.sendWindowProperty(this, 1, this.generatorTile.currentItemBurnTime);
            }
            if (this.isGenerating != genState) {
                listener.sendWindowProperty(this, 2, genState);
            }
        }

        this.burnTime = this.generatorTile.burnTime;
        this.currentItemBurnTime = this.generatorTile.currentItemBurnTime;
        this.isGenerating = genState;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        if (id == 0) {
            this.generatorTile.burnTime = data;
        } else if (id == 1) {
            this.generatorTile.currentItemBurnTime = data;
        } else if (id == 2) {
            this.generatorTile.isGeneratingClient = (data == 1);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.generatorTile.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index >= 0 && index <= 2) {
                // Move from generator to player inventory
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else {
                // Move from player inventory
                boolean merged = false;
                if (TileEntityFurnace.isItemFuel(itemstack1)) {
                    if (this.mergeItemStack(itemstack1, 0, 1, false)) {
                        merged = true;
                    }
                }
                if (!merged && com.voltyx.mwccf.battery.DeviceBatteryHelper.isChargeableItem(itemstack1)) {
                    if (this.mergeItemStack(itemstack1, 1, 3, false)) {
                        merged = true;
                    }
                }
                if (!merged) {
                    if (index >= 3 && index < 30) {
                        if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
