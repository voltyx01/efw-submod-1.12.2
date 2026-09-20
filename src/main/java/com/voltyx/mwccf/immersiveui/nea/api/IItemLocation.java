package com.voltyx.mwccf.immersiveui.nea.api;

import com.voltyx.mwccf.immersiveui.nea.NEAHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public interface IItemLocation {

    static IItemLocation of(Slot slot) {
        if (slot == null) return null;
        if (slot.getClass().getName().contains("CreativeSlot")) {
            try {
                java.lang.reflect.Field f = slot.getClass().getDeclaredField("slot");
                f.setAccessible(true);
                Slot underlying = (Slot) f.get(slot);
                if (underlying != null) {
                    return new Impl(slot.xPos, slot.yPos, underlying.slotNumber, underlying.getStack());
                }
            } catch (Throwable ignored) {}
        }
        return (IItemLocation) slot;
    }

    int nea$getX();

    int nea$getY();

    int nea$getSlotNumber();

    ItemStack nea$getStack();

    IItemLocation CURSOR = new IItemLocation() {

        @Override
        public int nea$getX() {
            int guiX = 0;
            if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {
                guiX = ((GuiContainer) Minecraft.getMinecraft().currentScreen).getGuiLeft();
            }
            return NEAHelper.getMouseX() - 8 - guiX;
        }

        @Override
        public int nea$getY() {
            int guiY = 0;
            if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {
                guiY = ((GuiContainer) Minecraft.getMinecraft().currentScreen).getGuiTop();
            }
            return NEAHelper.getMouseY() - 8 - guiY;
        }

        @Override
        public int nea$getSlotNumber() {
            return -1;
        }

        @Override
        public ItemStack nea$getStack() {
            return Minecraft.getMinecraft().player.inventory.getItemStack();
        }
    };

    class Impl implements IItemLocation {

        private final int x, y;
        private final int slotNumber;
        private final ItemStack stack;

        public Impl(int x, int y, ItemStack stack) {
            this(x, y, -2, stack);
        }

        public Impl(int x, int y, int slotNumber, ItemStack stack) {
            this.x = x;
            this.y = y;
            this.slotNumber = slotNumber;
            this.stack = stack;
        }

        @Override
        public int nea$getX() {
            return x;
        }

        @Override
        public int nea$getY() {
            return y;
        }

        @Override
        public int nea$getSlotNumber() {
            return slotNumber;
        }

        @Override
        public ItemStack nea$getStack() {
            return stack;
        }
    }
}
