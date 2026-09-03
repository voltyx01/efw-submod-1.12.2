package com.voltyx.mwccf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HeadshotDamageHandler {

    @SubscribeEvent
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

            if (headHit != null) {
                // ПУЛЯ ПРОБИЛА КУБ!
                float baseDamage = event.getAmount();
                event.setAmount(baseDamage * 2.5F); // Множитель урона

                // System.out.println("!!! ХЭДШОТ (Луч пробил голову) !!! Урон: " +
                // event.getAmount());
            } else {
                // Если пуля очень медленная (как шипы паразитов) или уже застряла,
                // проверяем обычным пересечением коробок
                if (headBox.intersects(projectile.getEntityBoundingBox())) {
                    float baseDamage = event.getAmount();
                    event.setAmount(baseDamage * 2.5F);

                    // System.out.println("!!! ХЭДШОТ (Физическое касание) !!! Урон: " +
                    // event.getAmount());
                }
            }
        }
    }
}