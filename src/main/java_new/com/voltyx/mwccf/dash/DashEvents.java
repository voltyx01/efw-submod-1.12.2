package com.voltyx.mwccf.dash;

import efw.biomeinfo.MwccfConfig;
import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.network.PacketSyncStamina;
import com.voltyx.mwccf.network.PacketSyncDashCooldown;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class DashEvents {

    @SubscribeEvent
    public void attachCaps(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof EntityPlayer) {
            e.addCapability(DashCapability.DashProvider.KEY, new DashCapability.DashProvider());
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        EntityPlayer player = e.player;
        DashCapability.IDashData cap = player.getCapability(DashCapability.ROLL_CAP, null);
        if (cap == null) return;

        handleStamina(player);

        if (player.world.isRemote) return;

        boolean allowLiquid = MwccfConfig.dashAndStamina.dash.allowDashInLiquids;
        boolean onGround = player.onGround;
        double currentStamina = player.getEntityData().getDouble("stamina");
        boolean hasStamina = currentStamina >= MwccfConfig.dashAndStamina.dash.staminaCost;
        boolean inLiquid = isPlayerInLiquid(player);

        if (!hasStamina || (!onGround && !inLiquid) || (inLiquid && !allowLiquid)) {
            cap.setDashing(false);
        }

        if (cap.isDashing()) {
            Vec3d dir = cap.getDashDir();
            if (dir == null || dir.lengthSquared() == 0) {
                cap.setDashing(false);
                return;
            }

            double newStamina = currentStamina - MwccfConfig.dashAndStamina.dash.staminaCost;
            player.getEntityData().setDouble("stamina", newStamina);
            player.getEntityData().setInteger("stamina_regen_delay", MwccfConfig.dashAndStamina.dash.staminaRegenDelay);

            player.fallDistance = 0;

            player.addPotionEffect(new PotionEffect(
                    MobEffects.RESISTANCE,
                    MwccfConfig.dashAndStamina.dash.resistanceDuration,
                    MwccfConfig.dashAndStamina.dash.resistanceLevel,
                    false, false));

            player.addPotionEffect(new PotionEffect(
                    MobEffects.WEAKNESS,
                    MwccfConfig.dashAndStamina.dash.weaknessDuration,
                    MwccfConfig.dashAndStamina.dash.weaknessLevel,
                    false, false));

            player.world.playSound(null, player.posX, player.posY, player.posZ,
                    com.voltyx.mwccf.ModSounds.DASH,
                    net.minecraft.util.SoundCategory.PLAYERS,
                    (float) MwccfConfig.dashAndStamina.dash.soundVolume,
                    (float) MwccfConfig.dashAndStamina.dash.soundPitch);

            if (player.world instanceof WorldServer) {
                WorldServer ws = (WorldServer) player.world;
                ws.spawnParticle(EnumParticleTypes.CLOUD,
                        player.posX, player.posY + MwccfConfig.dashAndStamina.dash.particleYOffset, player.posZ,
                        MwccfConfig.dashAndStamina.dash.particleCount,
                        dir.x * MwccfConfig.dashAndStamina.dash.particleSpread,
                        MwccfConfig.dashAndStamina.dash.particleHeight,
                        dir.z * MwccfConfig.dashAndStamina.dash.particleSpread,
                        MwccfConfig.dashAndStamina.dash.particleSpeed);
            }

            cap.setDashing(false);
            cap.setDashPerformed(true);
            cap.setPostDashTimer(MwccfConfig.dashAndStamina.dash.postTimerTicks);
            cap.setCooldown(MwccfConfig.dashAndStamina.dash.cooldownTicks);
            cap.setLastDashTick(player.ticksExisted);

            if (player instanceof EntityPlayerMP) {
                MwccfMod.PACKET_HANDLER.sendTo(
                        new PacketSyncStamina(newStamina, MwccfConfig.dashAndStamina.dash.staminaRegenDelay),
                        (EntityPlayerMP) player
                );
            }
        }

        if (cap.getPostDashTimer() > 0) cap.setPostDashTimer(cap.getPostDashTimer() - 1);
        if (cap.getCooldown() > 0) {
            cap.setCooldown(cap.getCooldown() - 1);
            if (player instanceof EntityPlayerMP) {
                MwccfMod.PACKET_HANDLER.sendTo(
                        new PacketSyncDashCooldown(cap.getCooldown()),
                        (EntityPlayerMP) player
                );
            }
        }
    }

    private void handleStamina(EntityPlayer player) {
        final double maxStamina = MwccfConfig.dashAndStamina.stamina.maxStamina;

        if (!player.getEntityData().hasKey("stamina")) {
            player.getEntityData().setDouble("stamina", maxStamina);
            player.getEntityData().setInteger("stamina_tick_counter", 0);
            player.getEntityData().setInteger("stamina_regen_delay", 0);

            if (MwccfConfig.dashAndStamina.stamina.limitByFood) {
                int food = player.getFoodStats().getFoodLevel();
                double initialLimit = (food <= 6) ? 1 : ((food - 6) / 14.0) * maxStamina;
                initialLimit = Math.max(initialLimit, 1);
                player.getEntityData().setDouble("stamina_food_limit", initialLimit);
            } else {
                player.getEntityData().setDouble("stamina_food_limit", maxStamina);
            }
        }

        if (player.isCreative() || player.isSpectator()) {
            player.getEntityData().setDouble("stamina", maxStamina);
            player.getEntityData().setInteger("stamina_tick_counter", 0);
            player.getEntityData().setInteger("stamina_regen_delay", 0);
            player.getEntityData().setDouble("stamina_food_limit", maxStamina);
            return;
        }

        double stamina = player.getEntityData().getDouble("stamina");
        int tickCounter = player.getEntityData().getInteger("stamina_tick_counter");
        int regenDelay = player.getEntityData().getInteger("stamina_regen_delay");

        if (player.isSprinting()) {
            tickCounter++;
            if (tickCounter >= MwccfConfig.dashAndStamina.stamina.sprintDrainInterval) {
                tickCounter = 0;
                stamina = Math.max(stamina - MwccfConfig.dashAndStamina.stamina.staminaDrainAmount, 0);
                regenDelay = MwccfConfig.dashAndStamina.stamina.sprintRegenDelay;
            }
        } else if (regenDelay > 0) {
            regenDelay--;
            tickCounter = 0;
        } else {
            boolean hasEnergyBoost = false;
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getPotion() == PotionEnergyBoost.INSTANCE) {
                    hasEnergyBoost = true;
                    break;
                }
            }

            boolean jumpedLastTick = player.getEntityData().getBoolean("jumped_last_tick");
            if (!player.onGround && !jumpedLastTick) {
                stamina = Math.max(stamina - MwccfConfig.dashAndStamina.stamina.jumpCost, 0);
                regenDelay = MwccfConfig.dashAndStamina.stamina.jumpRegenDelay;
                player.getEntityData().setBoolean("jumped_last_tick", true);
            }

            if (player.onGround) {
                player.getEntityData().setBoolean("jumped_last_tick", false);
            }

            int regenTicks = hasEnergyBoost
                    ? MwccfConfig.dashAndStamina.stamina.regenIntervalBoost
                    : MwccfConfig.dashAndStamina.stamina.regenIntervalNormal;

            tickCounter++;
            if (tickCounter >= regenTicks) {
                tickCounter = 0;
                stamina = Math.min(stamina + MwccfConfig.dashAndStamina.stamina.staminaRegenAmount, maxStamina);
            }
        }

        if (MwccfConfig.dashAndStamina.stamina.limitByFood) {
            boolean hasEnergyBoost = false;
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getPotion() == PotionEnergyBoost.INSTANCE) {
                    hasEnergyBoost = true;
                    break;
                }
            }

            double maxAllowedStamina = maxStamina;
            if (!hasEnergyBoost) {
                int food = player.getFoodStats().getFoodLevel();
                if (food <= 6) {
                    maxAllowedStamina = 1;
                } else {
                    maxAllowedStamina = ((food - 6) / 14.0) * maxStamina;
                    maxAllowedStamina = Math.max(maxAllowedStamina, 1);
                }
            }

            player.getEntityData().setDouble("stamina_food_limit", maxAllowedStamina);
            stamina = Math.min(stamina, maxAllowedStamina);
        } else {
            player.getEntityData().setDouble("stamina_food_limit", maxStamina);
        }

        double lowStaminaThreshold = maxStamina * 0.05;
        if (stamina <= lowStaminaThreshold) {
            player.addPotionEffect(new PotionEffect(
                    MobEffects.SLOWNESS,
                    MwccfConfig.dashAndStamina.lowStaminaEffects.slownessDuration,
                    MwccfConfig.dashAndStamina.lowStaminaEffects.slownessAmplifier,
                    false, false
            ));
            player.addPotionEffect(new PotionEffect(
                    MobEffects.MINING_FATIGUE,
                    MwccfConfig.dashAndStamina.lowStaminaEffects.fatigueDuration,
                    MwccfConfig.dashAndStamina.lowStaminaEffects.fatigueAmplifier,
                    false, false
            ));
            player.addPotionEffect(new PotionEffect(
                    MobEffects.WEAKNESS,
                    MwccfConfig.dashAndStamina.lowStaminaEffects.weaknessDuration,
                    MwccfConfig.dashAndStamina.lowStaminaEffects.weaknessAmplifier,
                    false, false
            ));
        }

        player.getEntityData().setDouble("stamina", stamina);
        player.getEntityData().setInteger("stamina_tick_counter", tickCounter);
        player.getEntityData().setInteger("stamina_regen_delay", regenDelay);

        double lastStamina = player.getEntityData().getDouble("last_stamina_sent");
        int lastDelay = player.getEntityData().getInteger("last_regen_delay_sent");

        if (Math.abs(stamina - lastStamina) > 0.01 || regenDelay != lastDelay) {
            player.getEntityData().setDouble("last_stamina_sent", stamina);
            player.getEntityData().setInteger("last_regen_delay_sent", regenDelay);

            if (!player.world.isRemote && player instanceof EntityPlayerMP) {
                MwccfMod.PACKET_HANDLER.sendTo(
                        new PacketSyncStamina(stamina, regenDelay),
                        (EntityPlayerMP) player
                );
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttackEntity(AttackEntityEvent e) {
        EntityPlayer player = e.getEntityPlayer();
        DashCapability.IDashData cap = player.getCapability(DashCapability.ROLL_CAP, null);
        if (cap == null) return;

        if (cap.getPostDashTimer() > 0) {
            double stamina = player.getEntityData().getDouble("stamina");
            stamina = Math.max(0, stamina - MwccfConfig.dashAndStamina.dash.attackExtraStamina);
            player.getEntityData().setDouble("stamina", stamina);

            if (MwccfConfig.dashAndStamina.dash.attackExtraCooldown) {
                cap.setCooldown(MwccfConfig.dashAndStamina.dash.cooldownTicks);
            }

            if (player instanceof EntityPlayerMP) {
                MwccfMod.PACKET_HANDLER.sendTo(
                        new PacketSyncStamina(stamina, MwccfConfig.dashAndStamina.dash.staminaRegenDelay),
                        (EntityPlayerMP) player
                );
            }

            cap.setPostDashTimer(0);
        }
    }

    @SubscribeEvent
    public void onKnockBack(LivingKnockBackEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            DashCapability.IDashData cap = e.getEntityLiving().getCapability(DashCapability.ROLL_CAP, null);
            if (cap != null && (cap.isDashing() || cap.getPostDashTimer() > 0)) e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            DashCapability.IDashData cap = e.getEntityLiving().getCapability(DashCapability.ROLL_CAP, null);
            if (cap != null && (cap.isDashing() || cap.getPostDashTimer() > 0)) e.setCanceled(true);
        }
    }

    private boolean isPlayerInLiquid(EntityPlayer player) {
        BlockPos posFeet = new BlockPos(player.posX, player.posY + 0.1, player.posZ);
        BlockPos posHead = new BlockPos(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        return player.world.getBlockState(posFeet).getMaterial().isLiquid() || player.world.getBlockState(posHead).getMaterial().isLiquid();
    }
}
