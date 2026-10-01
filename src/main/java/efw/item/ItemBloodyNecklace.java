package efw.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Окровавленное ожерелье.
 * Можно надеть как Bauble (амулет).
 * Эффекты:
 * - Снижает урон на 10% (резист к урону).
 * - Понижает повышение сердцебиения от триггеров на 10%.
 * Редкость: UNCOMMON (желтое название).
 */
@Mod.EventBusSubscriber(modid = "mwccf")
@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemBloodyNecklace extends Item implements IBauble {

    public ItemBloodyNecklace() {
        super();
        setMaxStackSize(1);
        setRegistryName("mwccf", "bloody_necklace");
        setTranslationKey("mwccf.bloody_necklace");
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (!world.isRemote && Loader.isModLoaded("baubles")) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            if (handler != null) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    if (handler.isItemValidForSlot(i, held, player)) {
                        ItemStack inSlot = handler.getStackInSlot(i);
                        if (inSlot.isEmpty()) {
                            handler.setStackInSlot(i, held.copy());
                            held.shrink(1);
                            return new ActionResult<>(EnumActionResult.SUCCESS, held);
                        }
                    }
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
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

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String descKey = "tooltip.mwccf.bloody_necklace.desc";
        if (I18n.hasKey(descKey)) {
            tooltip.add(I18n.format(descKey));
        }
        String effectKey = "tooltip.mwccf.bloody_necklace.effect";
        if (I18n.hasKey(effectKey)) {
            tooltip.add(I18n.format(effectKey));
        }
    }

    /** Проверяет, надето ли ожерелье в слоте Baubles */
    public static boolean hasBloodyNecklaceEquipped(EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)) return false;
        EntityPlayer player = (EntityPlayer) entity;
        if (Loader.isModLoaded("baubles")) {
            return checkBaubles(player);
        }
        return false;
    }

    private static boolean checkBaubles(EntityPlayer player) {
        try {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            if (handler != null) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack s = handler.getStackInSlot(i);
                    if (!s.isEmpty() && s.getItem() instanceof ItemBloodyNecklace) {
                        return true;
                    }
                }
            }
        } catch (Throwable ignored) {}
        return false;
    }

    /** Снижение получаемого урона на 10% при надетом ожерелье */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (hasBloodyNecklaceEquipped(player)) {
                event.setAmount(event.getAmount() * 0.90F);
            }
        }
    }
}
