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

    @SubscribeEvent
    public void onAnvilUpdate(net.minecraftforge.event.AnvilUpdateEvent event) {
        net.minecraft.item.ItemStack left = event.getLeft();
        net.minecraft.item.ItemStack right = event.getRight();
        if (left == null || left.isEmpty() || right == null || right.isEmpty()) return;
        if (right.getItem() == net.minecraft.init.Items.ENCHANTED_BOOK) {
            java.util.Map<net.minecraft.enchantment.Enchantment, Integer> enchs = net.minecraft.enchantment.EnchantmentHelper.getEnchantments(right);
            if (enchs.containsKey(net.minecraft.init.Enchantments.SWEEPING)) {
                if (!(left.getItem() instanceof net.minecraft.item.ItemSword)) {
                    net.bettercombat.api.WeaponAttributes attr = net.bettercombat.logic.WeaponRegistry.getAttributes(left);
                    boolean canSweep = false;
                    if (attr != null && attr.attacks() != null) {
                        for (net.bettercombat.api.WeaponAttributes.Attack atk : attr.attacks()) {
                            if (net.bettercombat.network.ServerAttackHandler.isHorizontalOrSpinAttack(atk)) {
                                canSweep = true;
                                break;
                            }
                        }
                    }
                    if (canSweep) {
                        net.minecraft.item.ItemStack output = left.copy();
                        java.util.Map<net.minecraft.enchantment.Enchantment, Integer> existing = net.minecraft.enchantment.EnchantmentHelper.getEnchantments(output);
                        int currentLvl = existing.getOrDefault(net.minecraft.init.Enchantments.SWEEPING, 0);
                        int bookLvl = enchs.get(net.minecraft.init.Enchantments.SWEEPING);
                        int newLvl = (currentLvl == bookLvl) ? Math.min(3, currentLvl + 1) : Math.max(currentLvl, bookLvl);
                        existing.put(net.minecraft.init.Enchantments.SWEEPING, newLvl);
                        net.minecraft.enchantment.EnchantmentHelper.setEnchantments(existing, output);
                        event.setOutput(output);
                        event.setCost(Math.max(1, 2 * newLvl));
                    }
                }
            }
        }
    }
}
