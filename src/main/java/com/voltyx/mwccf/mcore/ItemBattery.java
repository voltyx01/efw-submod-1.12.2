package com.voltyx.mwccf.mcore;

import net.minecraft.item.Item;

public class ItemBattery extends Item {
    public ItemBattery(String name) {
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("mcore." + name);
        this.setCreativeTab(net.minecraft.creativetab.CreativeTabs.MATERIALS);
        this.setMaxStackSize(16);
    }
}
