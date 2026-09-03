package com.voltyx.mwccf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HeadHitboxDebugger {

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        // Защита от краша, если мир еще не загружен
        if (mc.world == null || mc.getRenderManager() == null)
            return;

        // --- НАСТРОЙКА РЕНДЕРА (OpenGL) ---
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting(); // Отключаем тени
        GlStateManager.disableTexture2D(); // Рисуем просто линии, а не текстуры
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(3.0F); // Толщина красной линии

        // Получаем позицию камеры игрока.
        // В Minecraft рендер происходит относительно глаз игрока, а не абсолютных
        // координат мира!
        double viewerX = mc.getRenderManager().viewerPosX;
        double viewerY = mc.getRenderManager().viewerPosY;
        double viewerZ = mc.getRenderManager().viewerPosZ;

        // --- ПЕРЕБИРАЕМ ВСЕХ СУЩЕСТВ ВОКРУГ ---
        for (Object obj : mc.world.loadedEntityList) {
            if (obj instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) obj;

                // Не рисуем коробку у себя на лице от 1-го лица
                if (entity == mc.player && mc.gameSettings.thirdPersonView == 0)
                    continue;

                // 1. Получаем тот самый 3D-куб из нашего менеджера (который мы обсуждали ранее)
                AxisAlignedBB headBox = AdvancedHeadshotManager.getHeadBox(entity);

                // 2. Смещаем координаты куба относительно камеры игрока
                AxisAlignedBB renderBox = headBox.offset(-viewerX, -viewerY, -viewerZ);

                // 3. Рисуем красную коробку (Красный = 1.0F, Зеленый = 0.0F, Синий = 0.0F,
                // Прозрачность = 1.0F)
                RenderGlobal.drawSelectionBoundingBox(renderBox, 1.0F, 0.0F, 0.0F, 1.0F);
            }
        }

        // --- ВОЗВРАЩАЕМ НАСТРОЙКИ РЕНДЕРА ОБРАТНО ---
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        // ... внутри onRenderWorldLast ...
        for (Object obj : mc.world.loadedEntityList) {
            if (obj instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) obj;
                if (entity == mc.player && mc.gameSettings.thirdPersonView == 0)
                    continue;

                AxisAlignedBB headBox;

                // ВАЖНО: Если это моб, которого мы сейчас настраиваем Тюнером, берем коробку от
                // Тюнера!
                if (entity == HitboxTunerTool.activeTarget) {
                    headBox = HitboxTunerTool.getLiveTunedBox();
                } else {
                    // Иначе берем обычную коробку из базы данных
                    headBox = AdvancedHeadshotManager.getHeadBox(entity);
                }

                if (headBox != null) {
                    AxisAlignedBB renderBox = headBox.offset(-viewerX, -viewerY, -viewerZ);

                    // Делаем коробку настраиваемого моба ЗЕЛЕНОЙ, чтобы отличать от остальных
                    // (красных)
                    if (entity == HitboxTunerTool.activeTarget) {
                        RenderGlobal.drawSelectionBoundingBox(renderBox, 0.0F, 1.0F, 0.0F, 1.0F); // Зеленый
                    } else {
                        RenderGlobal.drawSelectionBoundingBox(renderBox, 1.0F, 0.0F, 0.0F, 1.0F); // Красный
                    }
                }
            }
        }
    }
}