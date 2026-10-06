package com.voltyx.mwccf;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HeadshotDamageHandler {

    private static final Set<DamageSource> HEADSHOT_SOURCES = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
    private static final Map<DamageSource, EntityEquipmentSlot> HIT_SLOTS = Collections.synchronizedMap(new WeakHashMap<>());

    public static void markHeadshot(DamageSource source) {
        if (source != null) {
            HEADSHOT_SOURCES.add(source);
            HIT_SLOTS.put(source, EntityEquipmentSlot.HEAD);
        }
    }

    public static boolean isHeadshot(DamageSource source) {
        return source != null && HEADSHOT_SOURCES.contains(source);
    }

    public static EntityEquipmentSlot getHitSlot(DamageSource source) {
        if (source == null) return null;
        if (isHeadshot(source)) return EntityEquipmentSlot.HEAD;
        return HIT_SLOTS.get(source);
    }

    public static void clearDamageSource(DamageSource source) {
        if (source != null) {
            HEADSHOT_SOURCES.remove(source);
            HIT_SLOTS.remove(source);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityHurt(LivingHurtEvent event) {
        EntityLivingBase target = event.getEntityLiving();
        DamageSource source = event.getSource();

        Entity projectile = source.getImmediateSource();

        // Проверяем, что урон нанесен летящим снарядом
        if (projectile != null && !source.isMagicDamage() && !source.isExplosion()) {

            // 1. Получаем наш вычисленный куб головы
            AxisAlignedBB headBox = AdvancedHeadshotManager.getHeadBox(target);

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

            boolean isHead = headHit != null
                    || headBox.intersects(projectile.getEntityBoundingBox())
                    || headBox.contains(new Vec3d(projectile.posX, projectile.posY, projectile.posZ));

            if (isHead) {
                // ПУЛЯ ПРОБИЛА КУБ ГОЛОВЫ!
                float baseDamage = event.getAmount();
                event.setAmount(baseDamage * 2.5F); // Множитель урона
                markHeadshot(source);
            } else if (target instanceof EntityPlayer && Loader.isModLoaded("firstaid")) {
                try {
                    EntityEquipmentSlot slot = ichttt.mods.firstaid.common.util.PlayerSizeHelper.getSlotTypeForProjectileHit(projectile, (EntityPlayer) target);
                    if (slot != null) {
                        HIT_SLOTS.put(source, slot);
                    }
                } catch (Throwable ignored) {}
            }
        }
    }
}