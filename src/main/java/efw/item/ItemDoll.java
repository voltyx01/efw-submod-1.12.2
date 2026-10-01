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
    @Override
    public net.minecraft.item.EnumRarity getRarity(net.minecraft.item.ItemStack stack) {
        return net.minecraft.item.EnumRarity.UNCOMMON;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(net.minecraft.item.ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World worldIn, java.util.List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        String descKey = "tooltip.mwccf.doll.desc";
        if (net.minecraft.client.resources.I18n.hasKey(descKey)) {
            tooltip.add(net.minecraft.client.resources.I18n.format(descKey));
        }
    }
}
