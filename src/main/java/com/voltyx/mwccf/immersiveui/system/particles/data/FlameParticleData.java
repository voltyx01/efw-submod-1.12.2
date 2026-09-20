package com.voltyx.mwccf.immersiveui.system.particles.data;

import com.voltyx.mwccf.immersiveui.util.Vector2f;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class FlameParticleData extends ParticleData {
    private static final Texture2D TEXTURE = new Texture2D(new ResourceLocation("textures/particle/particles.png"), 0, 0, 8, 8, 8, 8);

    public FlameParticleData(float xStart, float yStart, int lifeTime, ParticleEmitter emitter) {
        super(TEXTURE, 0.5F, lifeTime, xStart, yStart, emitter);
        Random rand = new Random();

        this.enableBlend = false;
        this.size = 1.25F;
        this.startColor = 0xFFFFFFFF;
        this.endColor = 0xFFFFFFFF;
        this.gravityDirection = new Vector2f(0.0F, -1.0F);
        this.gravity = 1.0F;
        this.direction = Vector2f.rotate(new Vector2f(0.0F, -1.0F), (rand.nextFloat() - 0.5F) * 60.0F);
    }
}
