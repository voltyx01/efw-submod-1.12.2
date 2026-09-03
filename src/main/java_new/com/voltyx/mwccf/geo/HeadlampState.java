package com.voltyx.mwccf.geo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;

public class HeadlampState {
    public static boolean isOn(EntityPlayer player) {
        if (!net.minecraftforge.fml.common.Loader.isModLoaded("baubles")) return false;
        
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        if (handler != null) {
            ItemStack bauble = handler.getStackInSlot(4); // 4 is HEAD slot in Baubles
            if (!bauble.isEmpty() && bauble.getItem() instanceof ItemHeadlamp) {
                NBTTagCompound tag = bauble.getTagCompound();
                if (tag != null && tag.getBoolean("active")) {
                    return tag.hasKey("battery_charge") && tag.getInteger("battery_charge") > 0;
                }
            }
        }
        return false;
    }
}
