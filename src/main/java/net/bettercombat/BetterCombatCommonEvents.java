package net.bettercombat;

import efw.biomeinfo.MwccfConfig;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BetterCombatCommonEvents {

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!MwccfConfig.betterCombat.enabled) {
            return;
        }
        if (event.getHand() == EnumHand.OFF_HAND && PlayerAttackHelper.isTwoHandedWielding(event.getEntityPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!MwccfConfig.betterCombat.enabled) {
            return;
        }
        if (event.getHand() == EnumHand.OFF_HAND && PlayerAttackHelper.isTwoHandedWielding(event.getEntityPlayer())) {
            event.setCanceled(true);
        }
    }
}
