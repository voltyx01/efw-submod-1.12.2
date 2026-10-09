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
    // Трейлы / шлейф изображения (Motion Blur / Ghosting Accumulation)
    private static net.minecraft.client.shader.Framebuffer trailFbo = null;
    private static float smoothTrailAlpha = 0f;

    public static void reset() {
        smoothShake = 0f;
        smoothVignette = 0f;
        smoothTrailAlpha = 0f;
    }

    public static void updateCameraOverhaul(float bpm) {
        // Ничего (legacy hook, оставлен для совместимости)
    }

    /**
     * Рендер шлейфа (Motion Blur) в конце кадра мира.
     * Запоминает предыдущий кадр мира и плавно накладывает поверх нового при высоком BPM.
     */
    @SubscribeEvent
    public void onRenderWorldLast(net.minecraftforge.client.event.RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        float bpm = HeartbeatManager.currentBPM;
        boolean hasResistance = com.voltyx.mwccf.doll.SayaDollManager.hasBpmResistance(mc.player);

        // Целевая альфа шлейфа (начинается со 155 BPM, плавно нарастает до ~0.65 при 180 BPM)
        float targetTrail = 0f;
        if (!hasResistance && bpm >= 155f) {
            targetTrail = Math.min(0.68f, (bpm - 155f) / 25f * 0.68f);
        }
        smoothTrailAlpha = lerp(smoothTrailAlpha, targetTrail, 0.08f);

        if (smoothTrailAlpha > 0.01f) {
            renderAndCaptureTrail(mc, smoothTrailAlpha);
        } else if (trailFbo != null) {
            // Если эффект закончился, просто очищаем FBO, чтобы не зависал старый кадр
            trailFbo.framebufferClear();
        }
    }

    private static void renderAndCaptureTrail(Minecraft mc, float alpha) {
        int w = mc.displayWidth;
        int h = mc.displayHeight;
        if (w <= 0 || h <= 0) return;

        if (trailFbo == null) {
            trailFbo = new net.minecraft.client.shader.Framebuffer(w, h, false);
            trailFbo.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
            trailFbo.setFramebufferFilter(org.lwjgl.opengl.GL11.GL_LINEAR);
        } else if (trailFbo.framebufferWidth != w || trailFbo.framebufferHeight != h) {
            trailFbo.createBindFramebuffer(w, h);
            trailFbo.setFramebufferFilter(org.lwjgl.opengl.GL11.GL_LINEAR);
        }

        // 1. Отрисовываем предыдущий сохраненный кадр поверх текущего экрана
        if (trailFbo.framebufferTexture >= 0) {
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(org.lwjgl.opengl.GL11.GL_PROJECTION);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, 1.0D, 1.0D, 0.0D, -100.0D, 100.0D);
            GlStateManager.matrixMode(org.lwjgl.opengl.GL11.GL_MODELVIEW);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
            );
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(trailFbo.framebufferTexture);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);

            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buf = tess.getBuffer();
            buf.begin(7, DefaultVertexFormats.POSITION_TEX);
            buf.pos(0.0D, 1.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
            buf.pos(1.0D, 1.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
            buf.pos(1.0D, 0.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
            buf.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
            tess.draw();

            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.matrixMode(org.lwjgl.opengl.GL11.GL_PROJECTION);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(org.lwjgl.opengl.GL11.GL_MODELVIEW);
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }

        // 2. Копируем получившийся кадр (текущий мир + шлейф) в FBO для следующего кадра
        GlStateManager.bindTexture(trailFbo.framebufferTexture);
        org.lwjgl.opengl.GL11.glCopyTexSubImage2D(
            org.lwjgl.opengl.GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, w, h
        );
        GlStateManager.bindTexture(0);
    }

    /** Виньетка поверх HUD (блэкаут полностью убран в пользу шлейфа) */
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        float bpm = HeartbeatManager.currentBPM;

        // Целевые значения для мягкой виньетки
        float targetVignette = 0f;

        Minecraft mc = Minecraft.getMinecraft();
        boolean hasResistance = mc.player != null && com.voltyx.mwccf.doll.SayaDollManager.hasBpmResistance(mc.player);

        if (hasResistance) {
            targetVignette = 0f;
            smoothVignette = 0f;
        } else {
            // Мягкая виньетка по краям при 165+ BPM (максимум до 0.65, без полного перекрытия)
            if (bpm >= 165f) {
                targetVignette = Math.min(0.65f, (bpm - 165f) / 15f * 0.65f);
            }
            float vignetteIn = (bpm >= 170f) ? 0.08f : 0.03f;
            smoothVignette = lerp(smoothVignette, targetVignette, vignetteIn);
        }

        ScaledResolution res = event.getResolution();

        if (smoothVignette > 0.001f) {
            renderVignette(res, smoothVignette);
        }

        // Рендер потемнения куклы и скрытие интерфейса
        float dollDarkAlpha = com.voltyx.mwccf.doll.SayaDollManager.getDarknessAlpha();
        if (dollDarkAlpha > 0.001f) {
            boolean thirdPerson = mc.gameSettings.thirdPersonView != 0;
            boolean dollFinished = !com.voltyx.mwccf.render.doll.DollRenderer.isDollActive();
            if (thirdPerson || dollFinished) {
                renderDollActivationDarkness(dollDarkAlpha);
            }

            if (com.voltyx.mwccf.doll.SayaDollManager.isActivating(mc.player)) {
                // Полностью отменяем весь игровой интерфейс во время фазы удержания куклы
                event.setCanceled(true);

                // И отображаем только персональную реплику куклы ("Прости.")
                com.voltyx.mwccf.speech.client.SpeechClientEvents.renderPersonalReplica(res, mc);
            }
        }
    }

    // =====================================================================
    //  RENDER HELPERS
    // =====================================================================

    /**
     * Затемнение мира при активации куклы Сайи (плавное потемнение).
     */
    public static void renderDollActivationDarkness(float alpha) {
        if (alpha <= 0.001f) return;
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);

        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(org.lwjgl.opengl.GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, res.getScaledWidth_double(), res.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(org.lwjgl.opengl.GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);

        renderVignette(res, Math.min(1.0f, alpha * 1.15f));
        renderBlackout(res, Math.min(0.92f, alpha * 0.92f));

        GlStateManager.matrixMode(org.lwjgl.opengl.GL11.GL_PROJECTION);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(org.lwjgl.opengl.GL11.GL_MODELVIEW);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    /**
     * Виньетка: затемняет края экрана.
     * Использует ванильную текстуру vignette.png + blendmode ONE_MINUS_SRC_COLOR.
     * opacity: 0=нет эффекта, 1=максимально тёмные края.
     */
    public static void renderVignette(ScaledResolution res, float opacity) {
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
