package com.voltyx.mwccf;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

@SideOnly(Side.CLIENT)
public class HitboxTunerTool {

    // Текущий моб, которого мы настраиваем
    public static EntityLivingBase activeTarget = null;

    // Наши временные параметры
    public static double tuneY = 1.0;
    public static double tuneForward = 0.0;
    public static double tuneRadius = 0.25;

    /**
     * 1. ВЫДЕЛЕНИЕ МОБА: Shift + Правый клик Палкой
     */
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (!event.getWorld().isRemote)
            return; // Только на клиенте

        if (event.getEntityPlayer().isSneaking() && event.getItemStack().getItem() == Items.STICK) {
            if (event.getTarget() instanceof EntityLivingBase) {
                activeTarget = (EntityLivingBase) event.getTarget();

                // Сбрасываем значения на дефолтные для старта
                tuneY = activeTarget.height / 2.0;
                tuneForward = 0.0;
                tuneRadius = 0.25;

                ResourceLocation regName = EntityList.getKey(activeTarget);
                sendMessage("§d[Тюнер] §aМоб выбран: §e" + (regName != null ? regName.toString() : "Неизвестно"));
                sendMessage("§7Используй СТРЕЛОЧКИ для перемещения. ПЛЮС/МИНУС для размера. ENTER для сохранения.");
            }
        }
    }

    /**
     * 2. УПРАВЛЕНИЕ: Стрелочки и +/-
     */
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (activeTarget == null || activeTarget.isDead)
            return;
        if (!Keyboard.getEventKeyState())
            return; // Реагируем только на нажатие, а не отпускание

        int key = Keyboard.getEventKey();

        // Если зажат Shift, двигаем быстро (по 0.1), иначе точно (по 0.02)
        double step = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 0.1 : 0.02;

        boolean changed = false;

        // ВВЕРХ / ВНИЗ - Высота (Y)
        if (key == Keyboard.KEY_UP) {
            tuneY += step;
            changed = true;
        }
        if (key == Keyboard.KEY_DOWN) {
            tuneY -= step;
            changed = true;
        }

        // ВЛЕВО / ВПРАВО - Выдвижение вперед
        if (key == Keyboard.KEY_RIGHT) {
            tuneForward += step;
            changed = true;
        }
        if (key == Keyboard.KEY_LEFT) {
            tuneForward -= step;
            changed = true;
        }

        // ПЛЮС / МИНУС - Размер хитбокса (Радиус)
        if (key == Keyboard.KEY_ADD || key == Keyboard.KEY_EQUALS) {
            tuneRadius += step;
            changed = true;
        }
        if (key == Keyboard.KEY_SUBTRACT || key == Keyboard.KEY_MINUS) {
            tuneRadius = Math.max(0.05, tuneRadius - step); // Не даем сделать радиус отрицательным
            changed = true;
        }

        // ENTER - Вывести готовую строку
        if (key == Keyboard.KEY_RETURN || key == Keyboard.KEY_NUMPADENTER) {
            ResourceLocation regName = EntityList.getKey(activeTarget);
            String id = regName != null ? regName.toString() : "unknown:mob";

            // Форматируем строку (Locale.US нужен, чтобы были точки, а не запятые в числах)
            String configLine = String.format(Locale.US, "%s, %.2f, %.2f, %.2f",
                    id, tuneForward, tuneY, tuneRadius);

            sendMessage("§a[КОНФИГ СТРОКА] §f(Скопируй из консоли логов):");
            sendMessage("§e" + configLine);

            // Также дублируем в системную консоль (IDE), чтобы удобно было копировать
            System.out.println("====== СКОПИРУЙ ЭТУ СТРОКУ В КОНФИГ ======");
            System.out.println(configLine);
            System.out.println("==========================================");

            changed = true;
        }

        // ESCAPE - Сбросить выделение
        if (key == Keyboard.KEY_ESCAPE) {
            activeTarget = null;
            sendMessage("§c[Тюнер] Выделение сброшено.");
        }
    }

    /**
     * Генератор "живого" хитбокса для дебаггера
     */
    public static AxisAlignedBB getLiveTunedBox() {
        if (activeTarget == null)
            return null;

        Vec3d look = activeTarget.getLook(1.0F);
        double headX = activeTarget.posX + (look.x * tuneForward);
        double headY = activeTarget.posY + tuneY + (look.y * tuneForward);
        double headZ = activeTarget.posZ + (look.z * tuneForward);

        return new AxisAlignedBB(
                headX - tuneRadius, headY - tuneRadius, headZ - tuneRadius,
                headX + tuneRadius, headY + tuneRadius, headZ + tuneRadius);
    }

    private void sendMessage(String text) {
        if (Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(text));
        }
    }
}