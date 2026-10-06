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
                    comboMod = new AttributeModifier(COMBO_MODIFIER_ID, "Combo Multiplier", comboMult, 2); // 2 = MULTIPLY_TOTAL
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
            java.util.List<Entity> hitEntities = new java.util.ArrayList<>();
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

                // Ensure full attack cooldown strength (1.0F) so vanilla 1.9 attack cooldown does not slash damage down to 20%
                net.bettercombat.utils.AttackCooldownHelper.setFullAttackStrength(player);

                player.attackTargetEntityWithCurrentItem(target);
                hitCount++;
                hitEntities.add(target);
            }

            player.resetCooldown();

            boolean isSweep = isHorizontalOrSpinAttack(attack);
            boolean isSpin = isSpinAttack(attack);

            if (hitCount > 0 && (isSweep || (hitCount > 1 && MwccfConfig.betterCombat.allowReworkedSweeping))) {
                if (MwccfConfig.betterCombat.reworkedSweepingPlaysSound) {
                    player.world.playSound(null, player.posX, player.posY, player.posZ,
                            SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
                if (MwccfConfig.betterCombat.reworkedSweepingEmitsParticles) {
                    if (isSpin) {
                        for (int i = 0; i < 8; i++) {
                            double rad = Math.toRadians(player.rotationYaw + (i * 45.0));
                            double px = -Math.sin(rad) * 1.35;
                            double pz = Math.cos(rad) * 1.35;
                            player.getServerWorld().spawnParticle(
                                    EnumParticleTypes.SWEEP_ATTACK,
                                    player.posX + px,
                                    player.posY + player.height * 0.5,
                                    player.posZ + pz,
                                    1, 0, 0, 0, 0.0);
                        }
                    } else if (attack != null && attack.angle() >= 150.0) {
                        double[] offsets = new double[] { -40.0, 0.0, 40.0 };
                        for (double off : offsets) {
                            double rad = Math.toRadians(player.rotationYaw + off);
                            double px = -Math.sin(rad) * 1.15;
                            double pz = Math.cos(rad) * 1.15;
                            player.getServerWorld().spawnParticle(
                                    EnumParticleTypes.SWEEP_ATTACK,
                                    player.posX + px,
                                    player.posY + player.height * 0.5,
                                    player.posZ + pz,
                                    1, 0, 0, 0, 0.0);
                        }
                    } else {
                        double rad = Math.toRadians(player.rotationYaw);
                        double px = -Math.sin(rad) * 1.0;
                        double pz = Math.cos(rad) * 1.0;
                        player.getServerWorld().spawnParticle(
                                EnumParticleTypes.SWEEP_ATTACK,
                                player.posX + px,
                                player.posY + player.height * 0.5,
                                player.posZ + pz,
                                1, 0, 0, 0, 0.0);
                    }
                }

                // Sweeping cleave damage for surrounding enemies not directly hit
                if (isSweep) {
                    float baseDamage = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                    float sweepingRatio = EnchantmentHelper.getSweepingDamageRatio(player);
                    float sweepDamage = 1.0F + sweepingRatio * baseDamage;

                    if (isSpin) {
                        double cleaveRange = (attributes != null ? attributes.attackRange() : 2.5) + 0.5;
                        net.minecraft.util.math.AxisAlignedBB spinBox = player.getEntityBoundingBox().grow(cleaveRange, 1.0D, cleaveRange);
                        for (EntityLivingBase living : player.world.getEntitiesWithinAABB(EntityLivingBase.class, spinBox)) {
                            if (living != player && !hitEntities.contains(living) && !player.isOnSameTeam(living)
                                    && living.isEntityAlive() && player.getDistanceSq(living) <= cleaveRange * cleaveRange) {
                                double dx = living.posX - player.posX;
                                double dz = living.posZ - player.posZ;
                                double dist = Math.sqrt(dx * dx + dz * dz);
                                if (dist > 0.001) {
                                    living.knockBack(player, 0.4F, -dx / dist, -dz / dist);
                                } else {
                                    living.knockBack(player, 0.4F, (double) net.minecraft.util.math.MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-net.minecraft.util.math.MathHelper.cos(player.rotationYaw * 0.017453292F)));
                                }
                                living.attackEntityFrom(net.minecraft.util.DamageSource.causePlayerDamage(player), sweepDamage);
                            }
                        }
                    } else if (hitCount == 1 && !hitEntities.isEmpty()) {
                        Entity primary = hitEntities.get(0);
                        net.minecraft.util.math.AxisAlignedBB cleaveBox = primary.getEntityBoundingBox().grow(1.25D, 0.5D, 1.25D);
                        for (EntityLivingBase living : player.world.getEntitiesWithinAABB(EntityLivingBase.class, cleaveBox)) {
                            if (living != player && !hitEntities.contains(living) && !player.isOnSameTeam(living)
                                    && living.isEntityAlive() && player.getDistanceSq(living) <= maxAllowedDistSq) {
                                living.knockBack(player, 0.4F,
                                        (double) net.minecraft.util.math.MathHelper.sin(player.rotationYaw * 0.017453292F),
                                        (double) (-net.minecraft.util.math.MathHelper.cos(player.rotationYaw * 0.017453292F)));
                                living.attackEntityFrom(net.minecraft.util.DamageSource.causePlayerDamage(player), sweepDamage);
                            }
                        }
                    }
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

    public static boolean isHorizontalOrSpinAttack(WeaponAttributes.Attack attack) {
        if (attack == null) return false;
        if (attack.angle() >= 180.0) return true;
        if (attack.hitbox() == WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE) return true;
        String anim = attack.animation();
        if (anim != null) {
            String lower = anim.toLowerCase(java.util.Locale.ROOT);
            return lower.contains("spin") || lower.contains("horizontal") || lower.contains("sweep") || lower.contains("swipe");
        }
        return false;
    }

    public static boolean isSpinAttack(WeaponAttributes.Attack attack) {
        if (attack == null) return false;
        if (attack.angle() >= 300.0) return true;
        String anim = attack.animation();
        return anim != null && anim.toLowerCase(java.util.Locale.ROOT).contains("spin");
    }
}
