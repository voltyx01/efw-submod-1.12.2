package com.voltyx.mwccf.blood;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.UUID;

/**
 * Listens for melee hits and applies blood contamination to nearby players.
 * Registered for both sides: LivingHurtEvent fires server-side in SP
 * (same JVM as client → shared static BloodManager map works fine).
 */
@Mod.EventBusSubscriber(modid = "mwccf")
public class BloodOverlayEventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.isCanceled()) return;
        float amount = event.getAmount();
        if (amount <= 0.0f) return;

        EntityLivingBase victim = event.getEntityLiving();
        if (victim == null || victim.world == null) return;

        DamageSource source = event.getSource();

        // Determine if this is a melee-like hit
        boolean isMelee = !source.isProjectile()
                && !source.isExplosion()
                && !source.isFireDamage()
                && !source.isMagicDamage()
                && source.getTrueSource() instanceof EntityLivingBase;

        Entity trueSource = source.getTrueSource();
        UUID attackerId = (trueSource instanceof EntityPlayer)
                ? ((EntityPlayer) trueSource).getUniqueID() : null;

        // 1. Player attacked someone with melee → bloody hands / face
        if (isMelee && attackerId != null) {
            BloodManager.addBlood(attackerId, BloodManager.MELEE_ATTACKER_GAIN);
        }

        // 2. Player received damage (any source) → some blood on self
        if (victim instanceof EntityPlayer) {
            BloodManager.addBlood(victim.getUniqueID(), BloodManager.MELEE_VICTIM_GAIN);
        }

        // 3. Nearby players caught in a melee splash (radius 2.5 blocks from victim)
        if (isMelee) {
            AxisAlignedBB splashArea = victim.getEntityBoundingBox().grow(2.5);
            List<EntityPlayer> nearby = victim.world.getEntitiesWithinAABB(EntityPlayer.class, splashArea);
            for (EntityPlayer bystander : nearby) {
                UUID pid = bystander.getUniqueID();
                if (pid.equals(attackerId)) continue; // attacker already counted
                if (bystander == victim) continue;    // victim already counted
                BloodManager.addBlood(pid, BloodManager.BYSTANDER_GAIN);
            }
        }
    }
}
