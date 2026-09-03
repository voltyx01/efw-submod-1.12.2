package com.voltyx.mwccf.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSyringe extends Item {
    public static final ItemSyringe INSTANCE = new ItemSyringe();

    public ItemSyringe() {
        this.setRegistryName("mwccf", "syringe");
        this.setTranslationKey("mwccf.syringe");
        this.setMaxStackSize(16);
        this.setCreativeTab(CreativeTabs.MISC);
    }
}
