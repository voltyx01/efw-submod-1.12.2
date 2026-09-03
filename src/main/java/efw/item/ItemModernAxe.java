package efw.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;
import net.minecraftforge.common.util.EnumHelper;

public class ItemModernAxe extends ItemAxe {

    public static final ToolMaterial MATERIAL_MODERN_AXE = EnumHelper.addToolMaterial(
            "MODERN_AXE", 3, 1164, 7.0F, 5.0F, 0
    );

    public ItemModernAxe() {
        super(MATERIAL_MODERN_AXE, 9.0F, -3.1F);
        setTranslationKey("mcore.modern_axe");
        setRegistryName("mwccf", "modern_axe");
        setCreativeTab(CreativeTabs.TOOLS);
    }
}
