package efw.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCloth extends Item {

    public ItemCloth() {
        this("cloth");
    }

    public ItemCloth(String name) {
        super();
        setMaxStackSize(64);
        setTranslationKey("mcore." + name);
        setRegistryName("mwccf", name);
        setCreativeTab(CreativeTabs.MATERIALS);
    }
}
