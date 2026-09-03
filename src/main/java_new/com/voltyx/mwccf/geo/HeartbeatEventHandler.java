package com.voltyx.mwccf.geo;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HeartbeatEventHandler {

    /**
     * Ловим урон локального игрока.
     * LivingHurtEvent на клиенте срабатывает после серверной синхронизации.
     */
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLivingHurt(LivingHurtEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;
        if (event.getEntityLiving() != mc.player) return;

        float amount = event.getAmount();
        String src = event.getSource().getDamageType().toLowerCase();

        if (src.contains("explosion") || src.contains("explode") || src.equals("fireworks")) {
            // Взрыв — спайк из гайда +25, зависит от урона
            HeartbeatManager.notifyExplosion(amount);
        } else {
            // Любой другой урон — +20 (из гайда)
            HeartbeatManager.notifyDamage(amount);
        }
    }
    @SubscribeEvent
    public void onSoundPlay(net.minecraftforge.client.event.sound.PlaySoundEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;
        
        String name = event.getName().toLowerCase();
        if (name.contains("explode") || name.contains("explosion") || name.contains("grenade")) {
            net.minecraft.client.audio.ISound sound = event.getSound();
            net.minecraft.util.math.Vec3d pos = new net.minecraft.util.math.Vec3d(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());
            float size = 5.0f; // Оценочный радиус взрыва (ТНТ = 4.0)
            float f3 = size * 2.0f;
            double distance = mc.player.getDistance(pos.x, pos.y, pos.z);
            
            if (distance < f3) {
                double d12 = distance / (double)f3;
                double d14 = mc.world.getBlockDensity(pos, mc.player.getEntityBoundingBox());
                double d10 = (1.0 - d12) * d14;
                float damage = (int)((d10 * d10 + d10) / 2.0 * 7.0 * (double)f3 + 1.0);
                
                if (damage > 0) {
                    HeartbeatManager.explosionImpulse = 175f;
                }
            }
        }
    }
}
