package com.voltyx.mwccf;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ToolAIFreezer {

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.EntityInteract event) {
        // Выполняем строго на сервере (там, где живет искусственный интеллект)
        if (!event.getWorld().isRemote) {

            // Проверяем: Шифт + Палка
            if (event.getEntityPlayer().isSneaking() && event.getItemStack().getItem() == Items.STICK) {

                // Проверяем, что это моб с ИИ (EntityLiving), а не стойка для брони или другой
                // игрок
                if (event.getTarget() instanceof EntityLiving) {
                    EntityLiving mob = (EntityLiving) event.getTarget();

                    // Читаем нашу кастомную метку из памяти моба
                    boolean isFrozen = mob.getEntityData().getBoolean("TunerFrozen");

                    if (isFrozen) {
                        // Если был заморожен -> Включаем ИИ
                        mob.setNoAI(false);
                        mob.getEntityData().setBoolean("TunerFrozen", false);
                        event.getEntityPlayer().sendMessage(new TextComponentString("§b[Сервер] ИИ моба включен."));
                    } else {
                        // Если двигался -> Отключаем ИИ
                        mob.setNoAI(true);
                        mob.getEntityData().setBoolean("TunerFrozen", true);
                        event.getEntityPlayer().sendMessage(new TextComponentString("§c[Сервер] Моб заморожен!"));
                    }
                }
            }
        }
    }
}