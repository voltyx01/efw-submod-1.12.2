package com.teamderpy.shouldersurfing.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FirstPersonFadeManager {
    private static final FirstPersonFadeManager INSTANCE = new FirstPersonFadeManager();

    private static final long FADE_DURATION_MS = 220L;
    private long fadeStartTime = -1L;
    private boolean isRenderingFirstPersonHand = false;
    private float currentFadeAlpha = 1.0F;

    public static FirstPersonFadeManager getInstance() {
        return INSTANCE;
    }

    public void startFadeIn() {
        this.fadeStartTime = System.currentTimeMillis();
        this.currentFadeAlpha = 0.0F;
    }

    public boolean isFadingIn() {
        if (this.fadeStartTime <= 0L) {
            return false;
        }
        long elapsed = System.currentTimeMillis() - this.fadeStartTime;
        if (elapsed >= FADE_DURATION_MS) {
            this.fadeStartTime = -1L;
            this.currentFadeAlpha = 1.0F;
            return false;
        }
        return true;
    }

    public float getCurrentFadeAlpha() {
        if (this.fadeStartTime <= 0L) {
            return 1.0F;
        }
        long elapsed = System.currentTimeMillis() - this.fadeStartTime;
        if (elapsed >= FADE_DURATION_MS) {
            this.fadeStartTime = -1L;
            this.currentFadeAlpha = 1.0F;
            return 1.0F;
        }
        float progress = (float) elapsed / (float) FADE_DURATION_MS;
        progress = MathHelper.clamp(progress, 0.0F, 1.0F);
        // Smoothstep curve for a natural materialization effect
        this.currentFadeAlpha = progress * progress * (3.0F - 2.0F * progress);
        return this.currentFadeAlpha;
    }

    public boolean isRenderingFirstPersonHand() {
        return this.isRenderingFirstPersonHand;
    }

    public void preRenderHand() {
        if (!this.isFadingIn()) {
            this.isRenderingFirstPersonHand = false;
            return;
        }
        this.isRenderingFirstPersonHand = true;
        float alpha = this.getCurrentFadeAlpha();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        );
        GlStateManager.alphaFunc(org.lwjgl.opengl.GL11.GL_GREATER, 0.001F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void postRenderHand() {
        if (this.isRenderingFirstPersonHand) {
            this.isRenderingFirstPersonHand = false;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(org.lwjgl.opengl.GL11.GL_GREATER, 0.1F);
            GlStateManager.depthMask(true);
        }
    }
}
