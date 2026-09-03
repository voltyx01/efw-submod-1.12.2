package com.voltyx.mwccf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// Импортируем твой класс пули
import com.paneedah.weaponlib.WeaponSpawnEntity;
import com.voltyx.mwccf.network.HitSoundMessage;

@Mod.EventBusSubscriber(modid = "mwccf")
public class GunSoundEventHandler {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        EntityLivingBase target = event.getEntityLiving();
        DamageSource source = event.getSource();
        World world = target.getEntityWorld();

        // Выполняем только на сервере
        if (world.isRemote)
            return;

        if (isGunDamage(source)) {
            // Если урон смертельный — выходим
            if (target.getHealth() - event.getAmount() <= 0)
                return;

            // Получаем того, кто стрелял (Сам игрок)
            Entity shooter = source.getTrueSource();

            // Если стрелял реальный игрок (не скелет и не турель)
            if (shooter instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) shooter;
                boolean isHeadshot = checkHeadshot(target, source);

                if (isHeadshot) {
                    // Отправляем пакет: 1 = Headshot
                    MwccfMod.NETWORK.sendTo(new HitSoundMessage(1), player);
                } else {
                    // Отправляем пакет: 0 = Flesh
                    MwccfMod.NETWORK.sendTo(new HitSoundMessage(0), player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase target = event.getEntityLiving();
        DamageSource source = event.getSource();
        World world = target.getEntityWorld();

        if (world.isRemote)
            return;

        if (isGunDamage(source)) {
            Entity shooter = source.getTrueSource();

            if (shooter instanceof EntityPlayerMP) {
                // Отправляем пакет: 2 = Kill
                MwccfMod.NETWORK.sendTo(new HitSoundMessage(2), (EntityPlayerMP) shooter);
            }
        }
    }

    /**
     * Точная проверка: является ли урон от пушки из мода.
     */
    private static boolean isGunDamage(DamageSource source) {
        // Проверяем кастомный DamageSource из WeaponSpawnEntity
        if (source instanceof WeaponSpawnEntity.ProjectileDamageSource) {
            return true;
        }

        // Резервная проверка по типу (у тебя в коде он назван "gun")
        if ("gun".equals(source.getDamageType())) {
            return true;
        }

        // Проверка самой пули
        Entity projectile = source.getImmediateSource();
        if (projectile instanceof WeaponSpawnEntity) {
            return true;
        }

        return false;
    }

    /**
     * Проверка на хитбокс головы с помощью RayTrace.
     */
    private static boolean checkHeadshot(EntityLivingBase target, DamageSource source) {
        Entity projectile = source.getImmediateSource();
        if (projectile == null)
            return false;

        // 1. Получаем наш вычисленный куб головы
        AxisAlignedBB headBox = AdvancedHeadshotManager.getHeadBox(target);
        if (headBox == null)
            return false;

        // 2. Строим вектор начала (где пуля была кадр назад)
        Vec3d startVec = new Vec3d(
                projectile.posX - projectile.motionX,
                projectile.posY - projectile.motionY,
                projectile.posZ - projectile.motionZ);

        // 3. Строим вектор конца (куда пуля летит сейчас).
        // Умножаем на 1.5, чтобы луч был чуть длиннее и гарантированно прошил голову
        Vec3d endVec = new Vec3d(
                projectile.posX + (projectile.motionX * 1.5D),
                projectile.posY + (projectile.motionY * 1.5D),
                projectile.posZ + (projectile.motionZ * 1.5D));

        // 4. ПРОВЕРКА ЛУЧОМ: Пересекает ли линия полета пули куб головы?
        RayTraceResult headHit = headBox.calculateIntercept(startVec, endVec);

        if (headHit != null) {
            // Пуля пробила куб лучем
            return true;
        } else {
            // Если пуля очень медленная или застряла — проверяем обычным пересечением
            return headBox.intersects(projectile.getEntityBoundingBox());
        }
    }

    /**
     * Утилита для удобного проигрывания звука.
     */
    private static void playSound(World world, Entity target, SoundEvent sound) {
        if (sound != null) {
            world.playSound(null, target.posX, target.posY + target.getEyeHeight(), target.posZ,
                    sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }
}