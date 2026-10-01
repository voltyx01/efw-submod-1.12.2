package efw.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Любимая игрушка Сайи — плюшевая кукла.
 * В руках рендерится через DollRenderer (кастомная Bedrock geo-модель с анимацией).
 * От первого лица: выдвигается снизу по центру, левая рука визуально убирается.
 */
public class ItemDoll extends Item {

    public ItemDoll() {
        super();
        setMaxStackSize(1);
        setTranslationKey("mwccf.doll");
        setRegistryName("mwccf", "doll");
        setCreativeTab(CreativeTabs.MISC);
    }
}
