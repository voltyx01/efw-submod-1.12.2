package com.voltyx.mwccf.immersiveui.system.particles;

import com.voltyx.mwccf.immersiveui.system.particles.data.ParticleData;
import com.voltyx.mwccf.immersiveui.system.particles.data.ParticleEmitter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class ParticleStorage {
    public static final Map<ParticleEmitter, List<ParticleData>> EMITTERS = new HashMap<>();

    public static List<ParticleData> getParticlesData() {
        List<ParticleData> all = new ArrayList<>();
        for (List<ParticleData> list : EMITTERS.values()) {
            all.addAll(list);
        }
        return all;
    }

    public static void addParticle(ParticleEmitter emitter, ParticleData... data) {
        List<ParticleData> list = EMITTERS.get(emitter);
        if (list == null) {
            list = new ArrayList<>();
            EMITTERS.put(emitter, list);
        }
        Collections.addAll(list, data);
    }

    public static void tickAll() {
        Set<ParticleEmitter> toRemoveSet = new HashSet<>();

        for (Map.Entry<ParticleEmitter, List<ParticleData>> entry : EMITTERS.entrySet()) {
            ParticleEmitter emitter = entry.getKey();
            List<ParticleData> particles = entry.getValue();
            List<ParticleData> toRemove = new ArrayList<>();

            for (ParticleData data : particles) {
                data.tick();
                if (data.getLifetime() <= 0) {
                    toRemove.add(data);
                }
            }
            particles.removeAll(toRemove);

            if (particles.isEmpty()) {
                toRemoveSet.add(emitter);
            }
        }

        for (ParticleEmitter emitter : toRemoveSet) {
            EMITTERS.remove(emitter);
        }
    }

    public static void renderAll(float partialTick) {
        List<ParticleData> list = getParticlesData();
        if (list.isEmpty()) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (ParticleData data : list) {
            try {
                data.render(partialTick);
            } catch (Throwable ignored) {
            }
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        GlStateManager.popAttrib();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.popMatrix();
    }

    public static void clear() {
        EMITTERS.clear();
    }
}
