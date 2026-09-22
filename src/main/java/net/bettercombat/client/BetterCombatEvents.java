package net.bettercombat.client;

import efw.biomeinfo.MwccfConfig;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BetterCombatEvents {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            BetterCombatClient.onClientTick();
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        if (!MwccfConfig.betterCombat.enabled) {
            return;
        }

        if (BetterCombatClient.isUpswingActive()) {
            float mult = (float) MwccfConfig.betterCombat.movementSpeedWhileAttacking;
            event.getMovementInput().moveForward *= mult;
            event.getMovementInput().moveStrafe *= mult;
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (!MwccfConfig.betterCombat.enabled || !MwccfConfig.betterCombat.isTooltipAttackRangeEnabled) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }

        WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
        if (attributes != null) {
            if (attributes.isTwoHanded()) {
                event.getToolTip().add(TextFormatting.GRAY + I18n.format("tooltip.bettercombat.two_handed"));
            }
            if (attributes.attackRange() > 0) {
                event.getToolTip().add(TextFormatting.DARK_GREEN + "+" + String.format("%.1f", attributes.attackRange()) + " " + I18n.format("tooltip.bettercombat.attack_range"));
            }
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!MwccfConfig.betterCombat.enabled) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        if (event.getHand() == EnumHand.OFF_HAND && PlayerAttackHelper.isTwoHandedWielding(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!MwccfConfig.betterCombat.enabled) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        if (event.getHand() == EnumHand.OFF_HAND && PlayerAttackHelper.isTwoHandedWielding(player)) {
            event.setCanceled(true);
        }
    }
}
