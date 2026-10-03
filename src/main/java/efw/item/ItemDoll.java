package efw.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Любимая игрушка Сайи — плюшевая кукла.
 * В руках рендерится через DollRenderer (кастомная Bedrock geo-модель с анимацией).
 * При ношении в инвентаре, рюкзаке или слоте Baubles рендерится как 3D-баубл на поясе/бедре.
 * Поддерживает загрязнение кровью в обоих вариантах.
 * 
 * Механика «Эффект Травмы»:
 * Пока игрушка активна (в инвентаре или слоте Baubles), чем сильнее игрок покрыт кровью,
 * тем выше бонус к наносимому урону (вплоть до +30%).
 * На огнестрельное оружие бонус не действует.
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemDoll extends Item implements IBauble {

    public ItemDoll() {
        super();
        setMaxStackSize(1);
        setTranslationKey("mwccf.doll");
        setRegistryName("mwccf", "doll");
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public net.minecraft.item.EnumRarity getRarity(ItemStack stack) {
        return net.minecraft.item.EnumRarity.UNCOMMON;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        // При Shift+ПКМ экипируем в свободный слот Baubles
        if (player.isSneaking() && Loader.isModLoaded("baubles")) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            if (handler != null) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    if (handler.isItemValidForSlot(i, held, player)) {
                        ItemStack inSlot = handler.getStackInSlot(i);
                        if (inSlot.isEmpty()) {
                            if (!world.isRemote) {
                                handler.setStackInSlot(i, held.copy());
                                held.shrink(1);
                            }
                            return new ActionResult<>(EnumActionResult.SUCCESS, held);
                        }
                    }
                }
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, held);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        // TRINKET может быть надет в АБСОЛЮТНО ЛЮБОЙ слот Baubles
        return BaubleType.TRINKET;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    /**
     * Проверяет, держит ли игрок куклу в руках (в правой или левой).
     */
    public static boolean isHoldingDoll(EntityPlayer player) {
        if (player == null) return false;
        ItemStack main = player.getHeldItemMainhand();
        if (isDollItem(main)) return true;
        ItemStack off = player.getHeldItemOffhand();
        if (isDollItem(off)) return true;
        return false;
    }

    /**
     * Проверяет наличие куклы в инвентаре, слотах Baubles или внутри рюкзака.
     */
    public static boolean hasDoll(EntityPlayer player) {
        if (player == null) return false;

        // 1. Проверяем слоты Baubles
        if (Loader.isModLoaded("baubles")) {
            try {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (checkStackOrContainerForDoll(stack)) {
                            return true;
                        }
                    }
                }
            } catch (Throwable ignored) {}
        }

        // 2. Проверяем нагрудник (например, надетый рюкзак Quark)
        ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (checkStackOrContainerForDoll(chestStack)) {
            return true;
        }

        // 3. Проверяем весь инвентарь игрока (хотбар, основной инвентарь, левая рука)
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (checkStackOrContainerForDoll(stack)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isDollItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (stack.getItem() instanceof ItemDoll) return true;
        if (stack.getItem().getRegistryName() != null) {
            return "mwccf:doll".equals(stack.getItem().getRegistryName().toString());
        }
        return false;
    }

    public static boolean checkStackOrContainerForDoll(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (isDollItem(stack)) return true;

        // Проверяем, является ли предмет контейнером (рюкзак, сумка и т.д.)
        try {
            if (stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack inner = handler.getStackInSlot(i);
                        if (!inner.isEmpty() && isDollItem(inner)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Throwable ignored) {}

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable World worldIn, java.util.List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        String descKey = "tooltip.mwccf.doll.desc";
        if (net.minecraft.client.resources.I18n.hasKey(descKey)) {
            tooltip.add(net.minecraft.client.resources.I18n.format(descKey));
        }

        tooltip.add("");
        if (net.minecraft.client.gui.GuiScreen.isShiftKeyDown()) {
            String traumaTitleKey = "tooltip.mwccf.doll.trauma_title";
            if (net.minecraft.client.resources.I18n.hasKey(traumaTitleKey)) {
                tooltip.add(net.minecraft.client.resources.I18n.format(traumaTitleKey));
            }

            String traumaDescKey = "tooltip.mwccf.doll.trauma_desc";
            if (net.minecraft.client.resources.I18n.hasKey(traumaDescKey)) {
                tooltip.add(net.minecraft.client.resources.I18n.format(traumaDescKey));
            }
        } else {
            String traumaShiftKey = "tooltip.mwccf.doll.trauma_shift";
            if (net.minecraft.client.resources.I18n.hasKey(traumaShiftKey)) {
                tooltip.add(net.minecraft.client.resources.I18n.format(traumaShiftKey));
            }
        }
    }
}
