package efw.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemMachete extends ItemSword {

    public static final ToolMaterial MATERIAL_MACHETE = EnumHelper.addToolMaterial(
            "MACHETE", 3, 786, 6.0F, 3.0F, 0
    );

    public ItemMachete() {
        super(MATERIAL_MACHETE);
        setTranslationKey("mcore.machete");
        setRegistryName("mwccf", "machete");
        setCreativeTab(CreativeTabs.COMBAT);
    }
}
