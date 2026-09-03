package com.voltyx.mwccf.client.inspect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InspectDustManager {

    private static final ResourceLocation PARTICLES_TEXTURE = new ResourceLocation("mwccf", "textures/particles/particles.png");
    private static final Random RANDOM = new Random();
    private static final int DUST_COUNT = 65;

    public static class DustParticle {
        public float x;
        public float y;
        public float vx;
        public float vy;
        public float size;
        public float alpha;
        public float baseAlpha;
        public float phase;
        public float phaseSpeed;

        public DustParticle(int width, int height) {
            respawn(width, height, true);
        }

        public void respawn(int width, int height, boolean randomInitial) {
            if (randomInitial) {
                this.x = RANDOM.nextFloat() * (width + 100) - 50;
                this.y = RANDOM.nextFloat() * (height + 50);
            } else {
                // Spawn either at bottom or on the left border
                if (RANDOM.nextBoolean()) {
                    this.x = RANDOM.nextFloat() * (width + 50) - 50;
                    this.y = height + 10.0f;
                } else {
                    this.x = -15.0f;
                    this.y = RANDOM.nextFloat() * (height + 50);
                }
            }
            this.vx = 8.0f + RANDOM.nextFloat() * 16.0f;   // Drift RIGHT
            this.vy = -(10.0f + RANDOM.nextFloat() * 18.0f); // Float UP
            this.size = 2.5f + RANDOM.nextFloat() * 4.0f;
            this.baseAlpha = 0.25f + RANDOM.nextFloat() * 0.45f;
            this.alpha = this.baseAlpha;
            this.phase = RANDOM.nextFloat() * 6.28f;
            this.phaseSpeed = 1.0f + RANDOM.nextFloat() * 2.0f;
        }

        public void update(float deltaSec, int width, int height) {
            this.phase += this.phaseSpeed * deltaSec;
            float sway = (float) Math.sin(this.phase) * 5.0f;

            this.x += (this.vx + sway) * deltaSec;
            this.y += this.vy * deltaSec;

            // Breathing alpha effect
            this.alpha = this.baseAlpha * (0.7f + 0.3f * (float) Math.cos(this.phase));

            if (this.y < -15 || this.x > width + 25) {
                respawn(width, height, false);
            }
        }
    }

    private final List<DustParticle> particles = new ArrayList<>();
    private long lastTime = 0;

    public void init(int width, int height) {
        particles.clear();
        for (int i = 0; i < DUST_COUNT; i++) {
            particles.add(new DustParticle(width, height));
        }
        lastTime = System.currentTimeMillis();
    }

    public void updateAndRender(int width, int height, Minecraft mc) {
        long now = System.currentTimeMillis();
        float deltaSec = lastTime == 0 ? 0.016f : Math.min((now - lastTime) / 1000.0f, 0.1f);
        lastTime = now;

        if (particles.isEmpty()) {
            init(width, height);
        }

        for (DustParticle p : particles) {
            p.update(deltaSec, width, height);
        }

        // Render particles
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.003921569F);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(PARTICLES_TEXTURE);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

        // Sub-texture index 0 in 16x16 grid of particles.png (u: 0..0.0625, v: 0..0.0625)
        double u1 = 0.0;
        double v1 = 0.0;
        double u2 = 0.0625;
        double v2 = 0.0625;

        for (DustParticle p : particles) {
            float s = p.size;
            int a = (int) (Math.max(0.0f, Math.min(1.0f, p.alpha)) * 255);
            int r = 220;
            int g = 225;
            int b = 230;

            buffer.pos(p.x, p.y + s, 0.0D).tex(u1, v2).color(r, g, b, a).endVertex();
            buffer.pos(p.x + s, p.y + s, 0.0D).tex(u2, v2).color(r, g, b, a).endVertex();
            buffer.pos(p.x + s, p.y, 0.0D).tex(u2, v1).color(r, g, b, a).endVertex();
            buffer.pos(p.x, p.y, 0.0D).tex(u1, v1).color(r, g, b, a).endVertex();
        }

        tessellator.draw();

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
