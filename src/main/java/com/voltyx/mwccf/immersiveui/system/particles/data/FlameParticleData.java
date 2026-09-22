package com.voltyx.mwccf.immersiveui.system.particles.data;

import com.voltyx.mwccf.immersiveui.util.Vector2f;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class FlameParticleData extends ParticleData {
    private static final ResourceLocation PARTICLES_TEXTURE = new ResourceLocation("textures/particle/particles.png");
    // Index 48 in textures/particle/particles.png (128x128): row 3 (24px), col 0 (0px), 8x8 sprite
    private static final Texture2D TEXTURE = new Texture2D(PARTICLES_TEXTURE, 0, 24, 128, 128, 8, 8);

    public FlameParticleData(float xStart, float yStart, int lifeTime, ParticleEmitter emitter) {
        super(TEXTURE, 0.5F, lifeTime, xStart, yStart, emitter);
        Random rand = new Random();

        this.enableBlend = true;
        this.blendSrc = GL11.GL_SRC_ALPHA;
        this.blendDst = GL11.GL_ONE;
        this.size = 1.25F;
        this.startColor = 0xFFFFFFFF;
        this.endColor = 0x00FFFFFF;
        this.gravityDirection = new Vector2f(0.0F, -1.0F);
        this.gravity = 1.0F;
        this.direction = Vector2f.rotate(new Vector2f(0.0F, -1.0F), (rand.nextFloat() - 0.5F) * 60.0F);
    }

    public FlameParticleData(float speed, int lifeTime, float xStart, float yStart, ParticleEmitter emitter) {
        this(xStart, yStart, lifeTime, emitter);
        this.speed = speed;
    }
}
