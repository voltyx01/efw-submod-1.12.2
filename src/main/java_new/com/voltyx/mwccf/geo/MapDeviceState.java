package com.voltyx.mwccf.geo;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;

public class MapDeviceState {

    public static boolean hasActiveMap() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return false;
        return hasActiveMap(mc.player);
    }

    public static boolean hasActiveMap(EntityPlayer player) {
        if (player == null) return false;
        if (!Loader.isModLoaded("baubles")) return false;

        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        if (handler != null) {
            int slots = handler.getSlots();
            for (int i = 0; i < slots; i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemPortableMap) {
                    NBTTagCompound tag = stack.getTagCompound();
                    if (tag != null && tag.hasKey("battery_charge") && tag.getInteger("battery_charge") > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
