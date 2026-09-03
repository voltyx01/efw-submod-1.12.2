package efw.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCloth extends Item {

    public ItemCloth() {
        super();
        setMaxStackSize(64);
        setTranslationKey("mcore.cloth");
        setRegistryName("mwccf", "cloth");
        setCreativeTab(CreativeTabs.MATERIALS);
    }
}
