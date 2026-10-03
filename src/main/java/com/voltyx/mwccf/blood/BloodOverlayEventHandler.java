package com.voltyx.mwccf.blood;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.UUID;

/**
 * Listens for melee hits and applies blood contamination to players.
 * If damage was dealt using MWC firearms (guns, bullets, grenades),
 * blood does NOT appear on the attacker.
 * Melee weapons (swords, axes, knives, mwccf weapons, bare hands) always apply blood.
 */
@Mod.EventBusSubscriber(modid = "mwccf")
public class BloodOverlayEventHandler {

    /**
     * Catches direct melee attack clicks against living entities (client & server).
     */
    @SubscribeEvent
    public static void onPlayerAttackEntity(AttackEntityEvent event) {
        EntityPlayer attacker = event.getEntityPlayer();
        if (attacker == null || attacker.world == null) return;
        Entity target = event.getTarget();
        if (!(target instanceof EntityLivingBase)) return;

        // If player is holding an MWC firearm/gun, do not treat as melee
        if (isHoldingMwcFirearm(attacker)) {
            return;
        }

        BloodManager.onMeleeAttack(attacker.getUniqueID(), BloodManager.MELEE_ATTACKER_GAIN);
    }

    /**
     * Catches damage instances (server side, covers vanilla, BetterCombat, etc.).
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.isCanceled()) return;
        float amount = event.getAmount();
        if (amount <= 0.0f) return;

        EntityLivingBase victim = event.getEntityLiving();
        if (victim == null || victim.world == null) return;

        DamageSource source = event.getSource();
        Entity trueSource = source.getTrueSource();
        EntityPlayer attacker = (trueSource instanceof EntityPlayer) ? (EntityPlayer) trueSource : null;
        UUID attackerId = (attacker != null) ? attacker.getUniqueID() : null;

        boolean isMwcRanged = isMwcRangedDamage(source, attacker);

        // --- Механика «Эффект Травмы» (плюшевая кукла Сайи) ---
        // Пока игрушка активна (в инвентаре или слоте Baubles), чем сильнее игрок покрыт кровью,
        // тем выше прибавка к наносимому урону (максимум +30%).
        // На огнестрельное оружие MWC не распространяется!
        if (attacker != null && !isMwcRanged) {
            if (efw.item.ItemDoll.hasDoll(attacker)) {
                float blood = BloodManager.getBloodLevel(attackerId);
                if (blood > 0.0f) {
                    float bonus = Math.min(0.30f, 0.30f * blood);
                    amount = amount * (1.0f + bonus);
                    event.setAmount(amount);
                }
            }
        }

        // Determine if this is a melee-like hit (excludes projectiles, explosions, magic, fire, and MWC guns)
        boolean isMelee = !isMwcRanged
                && !source.isProjectile()
                && !source.isExplosion()
                && !source.isFireDamage()
                && !source.isMagicDamage()
                && trueSource instanceof EntityLivingBase;

        // 1. Player attacked someone with melee (non-MWC firearms) -> bloody hands / face
        if (isMelee && attackerId != null) {
            BloodManager.onMeleeAttack(attackerId, BloodManager.MELEE_ATTACKER_GAIN);
        }

        // 2. Player received damage (any source) -> victim gets blood on self
        if (victim instanceof EntityPlayer) {
            BloodManager.addBlood(victim.getUniqueID(), BloodManager.MELEE_VICTIM_GAIN);
        }

        // 3. Nearby players caught in a melee splash (only for non-MWC melee)
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

    /**
     * Checks if player is holding an MWC firearm (gun, rifle, pistol) or grenade.
     * Note: Melee weapons (knives, swords, machetes, axes) are NOT firearms.
     */
    public static boolean isHoldingMwcFirearm(EntityPlayer player) {
        if (player == null) return false;
        ItemStack held = player.getHeldItemMainhand();
        if (held.isEmpty()) return false;

        // com.paneedah.weaponlib.Weapon represents guns/firearms in MWC
        if (held.getItem() instanceof com.paneedah.weaponlib.Weapon) {
            return true;
        }
        if (held.getItem() instanceof com.paneedah.weaponlib.grenade.ItemGrenade) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the damage was caused by an MWC firearm, bullet, or grenade.
     */
    private static boolean isMwcRangedDamage(DamageSource source, EntityPlayer attacker) {
        if (source == null) return false;

        String type = source.getDamageType();
        if ("gun".equals(type) || "bullet".equals(type)) {
            return true;
        }

        if (source instanceof com.paneedah.weaponlib.WeaponSpawnEntity.ProjectileDamageSource) {
            return true;
        }

        Entity immediate = source.getImmediateSource();
        if (immediate instanceof com.paneedah.weaponlib.WeaponSpawnEntity) {
            return true;
        }
        if (immediate instanceof com.paneedah.weaponlib.grenade.EntityGrenade) {
            return true;
        }

        if (isHoldingMwcFirearm(attacker)) {
            return true;
        }

        return false;
    }

    /**
     * Ticks blood decay on the server side so that blood levels
     * stay synchronized and decay naturally outside of client-only ticks.
     */
    @SubscribeEvent
    public static void onPlayerTick(net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent event) {
        if (event.phase != net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END) return;
        if (event.player != null && !event.player.world.isRemote) {
            BloodManager.tickDecay(event.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(net.minecraftforge.event.entity.living.LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            BloodManager.clear(event.getEntityLiving().getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            BloodManager.clear(event.getEntityPlayer().getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        if (event.player != null) {
            BloodManager.clear(event.player.getUniqueID());
        }
    }
}
