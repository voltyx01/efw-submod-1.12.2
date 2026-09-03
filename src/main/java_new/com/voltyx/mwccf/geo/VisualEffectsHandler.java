package com.voltyx.mwccf.geo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Обрабатывает все визуальные эффекты при высоком BPM:
 *  - 120+: лёгкое покачивание
 *  - 150+: тремор камеры + тряска рук
 *  - 170+: виньетка (края → центр)
 *
 * Блэкаут реализован через два слоя:
 *  1) Виньетка (Minecraft texture) — затемняет края с 170 BPM
 *  2) Чёрный экран (центр) — fade-in с 175 BPM, quadratic
 *
 * Все параметры плавно нарастают — нет резких переходов.
 */
@SideOnly(Side.CLIENT)
public class VisualEffectsHandler {

    private static final ResourceLocation VIGNETTE = new ResourceLocation("textures/misc/vignette.png");
    private static final Random rand = new Random();

    // Сглаженные значения для интерполяции (lerp) — исключают резкие скачки
    private static float smoothShake      = 0f; // 0..1
    private static float smoothVignette   = 0f; // 0..1
    private static float smoothBlackout   = 0f; // 0..1

    public static void updateCameraOverhaul(float bpm) {
        // Ничего (legacy hook, оставлен для совместимости)
    }

    /** Тряска камеры (pitch/yaw/roll) — только на 120+ */
    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        float bpm = HeartbeatManager.currentBPM;

        // Целевое значение тряски
        float targetShake = 0f;
        if (bpm >= 120f) {
            targetShake = (bpm - 120f) / 60f; // 0 при 120, 1 при 180
        }
        // Плавный lerp 20%/кадр ≈ ~0.5 сек до полного значения
        smoothShake = lerp(smoothShake, targetShake, 0.06f);

        if (smoothShake > 0.001f) {
            float amp = smoothShake * 0.6f; // макс ±0.6° при 180 BPM
            event.setPitch(event.getPitch() + (rand.nextFloat() - 0.5f) * amp);
            event.setYaw  (event.getYaw()   + (rand.nextFloat() - 0.5f) * amp);
            event.setRoll (event.getRoll()  + (rand.nextFloat() - 0.5f) * amp * 0.5f);
        }
    }

    /** Тряска рук — только при 150+ */
    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        if (smoothShake < 0.3f) return; // под 150 не трясём руки

        float amp = (smoothShake - 0.3f) / 0.7f; // 0..1 только на 150..180
        amp = amp * amp * 0.06f;                  // квадратичная кривая, макс 0.06

        // Translate остаётся в матрице — EntityRenderer сбрасывает её после рендера руки.
        // RenderSpecificHandEvent (V-режим) имеет собственную матрицу и не затрагивается.
        GlStateManager.translate(
            (rand.nextFloat() - 0.5f) * amp,
            (rand.nextFloat() - 0.5f) * amp,
            (rand.nextFloat() - 0.5f) * amp * 0.3f
        );
    }

    /** Виньетка + блэкаут поверх HUD */
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        float bpm = HeartbeatManager.currentBPM;

        // Целевые значения
        float targetVignette = 0f;
        float targetBlackout = 0f;

        if (bpm >= 165f) {
            targetVignette = Math.min(1f, (bpm - 165f) / 15f); // 165→0, 180→1
            targetBlackout = 0f; // blackout приходит чуть позже
        }
        if (bpm >= 170f) {
            targetBlackout = Math.min(0.9f, (bpm - 170f) / 5f * 0.9f);  // 170→0, 175→0.9
        }

        // Плавный lerp: вигнетка быстрее появляется, медленнее исчезает
        float vignetteIn  = bpm >= 170f ? 0.08f : 0.03f;
        float blackoutIn  = bpm >= 170f ? 0.05f : 0.015f; // blackout появляется быстро, исчезает медленно

        smoothVignette = lerp(smoothVignette, targetVignette, vignetteIn);
        smoothBlackout = lerp(smoothBlackout, targetBlackout, blackoutIn);

        ScaledResolution res = event.getResolution();

        if (smoothVignette > 0.001f) {
            renderVignette(res, smoothVignette);
        }
        if (smoothBlackout > 0.001f) {
            renderBlackout(res, smoothBlackout); // Линейная прозрачность
        }
    }

    // =====================================================================
    //  RENDER HELPERS
    // =====================================================================

    /**
     * Виньетка: затемняет края экрана.
     * Использует ванильную текстуру vignette.png + blendmode ONE_MINUS_SRC_COLOR.
     * opacity: 0=нет эффекта, 1=максимально тёмные края.
     */
    private static void renderVignette(ScaledResolution res, float opacity) {
        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableAlpha();

        // ONE_MINUS_SRC_COLOR: edges * (1 - srcColor) → темнее при белой текстуре
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.ZERO,
            GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        );

        GlStateManager.color(opacity, opacity, opacity, 1f);
        mc.getTextureManager().bindTexture(VIGNETTE);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(7, DefaultVertexFormats.POSITION_TEX);
        buf.pos(0,                        res.getScaledHeight(), -90).tex(0, 1).endVertex();
        buf.pos(res.getScaledWidth(),     res.getScaledHeight(), -90).tex(1, 1).endVertex();
        buf.pos(res.getScaledWidth(),     0,                     -90).tex(1, 0).endVertex();
        buf.pos(0,                        0,                     -90).tex(0, 0).endVertex();
        tess.draw();

        // Восстанавливаем blend
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        );
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    /**
     * Блэкаут: чёрный прямоугольник поверх всего.
     * alpha: 0=прозрачный, 1=непрозрачный.
     * Нарастает медленнее чем исчезает (за счёт lerp скорости в onRenderOverlay).
     */
    private static void renderBlackout(ScaledResolution res, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        );
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        GlStateManager.color(0f, 0f, 0f, alpha);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(7, DefaultVertexFormats.POSITION);
        buf.pos(0,                    res.getScaledHeight(), -90).endVertex();
        buf.pos(res.getScaledWidth(), res.getScaledHeight(), -90).endVertex();
        buf.pos(res.getScaledWidth(), 0,                     -90).endVertex();
        buf.pos(0,                    0,                     -90).endVertex();
        tess.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    private static float lerp(float current, float target, float speed) {
        return current + (target - current) * speed;
    }
}
