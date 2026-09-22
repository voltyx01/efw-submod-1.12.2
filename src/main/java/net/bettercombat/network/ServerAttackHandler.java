package net.bettercombat.network;

import efw.biomeinfo.MwccfConfig;
import net.bettercombat.api.AttackHand;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.PlayerAttackProperties;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;

import java.util.UUID;

public class ServerAttackHandler {
    private static final UUID COMBO_MODIFIER_ID = UUID.fromString("b8c1a111-1111-1111-1111-111111111111");
    private static final UUID DUAL_MODIFIER_ID = UUID.fromString("b8c1a222-2222-2222-2222-222222222222");
    private static final UUID SWEEPING_MODIFIER_ID = UUID.fromString("b8c1a333-3333-3333-3333-333333333333");

    public static void handleAttack(EntityPlayerMP player, PacketAttackRequest message) {
        if (player == null || player.isDead || player.world == null) {
            return;
        }

        int combo = message.getComboCount();
        AttackHand hand = PlayerAttackHelper.getCurrentAttack(player, combo);
        if (hand == null) {
            return;
        }

        WeaponAttributes.Attack attack = hand.attack();
        WeaponAttributes attributes = hand.attributes();
        double maxAllowedDist = (attributes != null ? attributes.attackRange() : 3.0) + 3.0;
        double maxAllowedDistSq = maxAllowedDist * maxAllowedDist;

        IAttributeInstance damageAttr = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        AttributeModifier comboMod = null;
        AttributeModifier dualMod = null;
        AttributeModifier sweepingMod = null;

        try {
            if (damageAttr != null && attack != null) {
                // Combo damage multiplier
                double comboMult = attack.damageMultiplier() - 1.0;
                if (Math.abs(comboMult) > 0.001) {
                    comboMod = new AttributeModifier(COMBO_MODIFIER_ID, "Combo Multiplier", comboMult, 1); // 1 = MULTIPLY_BASE
                    damageAttr.applyModifier(comboMod);
                }

                // Dual wielding damage multiplier
                double dualMult = PlayerAttackHelper.getDualWieldingAttackDamageMultiplier(player, hand) - 1.0;
                if (Math.abs(dualMult) > 0.001) {
                    dualMod = new AttributeModifier(DUAL_MODIFIER_ID, "Dual Wielding Multiplier", dualMult, 2); // 2 = MULTIPLY_TOTAL
                    damageAttr.applyModifier(dualMod);
                }

                // Reworked sweeping damage modifier for multi-target hits
                int targetCount = message.getTargetEntityIds().length;
                if (MwccfConfig.betterCombat.allowReworkedSweeping && targetCount > 1) {
                    int extraTargets = targetCount - 1;
                    int maxExtra = MwccfConfig.betterCombat.reworkedSweepingExtraTargetCount;
                    double maxPenalty = MwccfConfig.betterCombat.reworkedSweepingMaximumDamagePenalty;

                    double penalty = (maxPenalty / (double) Math.max(1, maxExtra)) * Math.min(maxExtra, extraTargets);
                    int sweepingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, hand.itemStack());
                    double restoreBonus = (sweepingLvl / 3.0) * 0.5;
                    double totalMult = Math.max(0.1, 1.0 - penalty + restoreBonus);

                    sweepingMod = new AttributeModifier(SWEEPING_MODIFIER_ID, "Sweeping Multiplier", totalMult - 1.0, 2);
                    damageAttr.applyModifier(sweepingMod);
                }
            }

            int hitCount = 0;
            for (int entityId : message.getTargetEntityIds()) {
                Entity target = player.world.getEntityByID(entityId);
                if (target == null || target == player || target.isDead) {
                    continue;
                }
                if (target.getDistanceSq(player) > maxAllowedDistSq) {
                    continue;
                }

                if (MwccfConfig.betterCombat.allowFastAttacks && target instanceof EntityLivingBase) {
                    ((EntityLivingBase) target).hurtResistantTime = 0;
                }

                player.attackTargetEntityWithCurrentItem(target);
                hitCount++;
            }

            if (hitCount > 1 && MwccfConfig.betterCombat.allowReworkedSweeping) {
                if (MwccfConfig.betterCombat.reworkedSweepingPlaysSound) {
                    player.world.playSound(null, player.posX, player.posY, player.posZ,
                            SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
                if (MwccfConfig.betterCombat.reworkedSweepingEmitsParticles) {
                    double px = -Math.sin(player.rotationYaw * 0.017453292F);
                    double pz = Math.cos(player.rotationYaw * 0.017453292F);
                    player.getServerWorld().spawnParticle(
                            EnumParticleTypes.SWEEP_ATTACK,
                            player.posX + px,
                            player.posY + player.height * 0.5,
                            player.posZ + pz,
                            1, 0, 0, 0, 0.0);
                }
            }

            PlayerAttackProperties.setComboCount(player, combo + 1);

        } finally {
            if (damageAttr != null) {
                if (comboMod != null) damageAttr.removeModifier(comboMod);
                if (dualMod != null) damageAttr.removeModifier(dualMod);
                if (sweepingMod != null) damageAttr.removeModifier(sweepingMod);
            }
        }
    }
}
