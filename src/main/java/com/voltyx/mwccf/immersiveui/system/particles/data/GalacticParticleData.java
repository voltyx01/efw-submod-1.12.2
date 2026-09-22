package com.voltyx.mwccf.immersiveui.system.particles.data;

import com.voltyx.mwccf.immersiveui.util.Vector2f;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class GalacticParticleData extends ParticleData {
    private static final Random RANDOM = new Random();
    private static final ResourceLocation PARTICLES_TEXTURE = new ResourceLocation("textures/particle/particles.png");

    private static Texture2D createRuneTexture() {
        // Standard Galactic Alphabet (SGA) runes in Minecraft 1.12.2 textures/particle/particles.png (128x128)
        // 26 runes located at indices 224 to 249 (rows 14 and 15, 8x8 pixels each)
        int rune = RANDOM.nextInt(26);
        int particleIndex = 224 + rune;
        int col = particleIndex % 16;
        int row = particleIndex / 16;
        return new Texture2D(PARTICLES_TEXTURE, col * 8, row * 8, 128, 128, 8, 8);
    }

    public GalacticParticleData(float maxSpeed, int maxLifetime, float xStart, float yStart, ParticleEmitter emitter) {
        super(createRuneTexture(), maxSpeed, maxLifetime, xStart, yStart, emitter);

        this.angularVelocity = 6.0F;
        this.speed = RANDOM.nextFloat() * 2.0F;
        this.startColor = 0xFF8888FF;
        this.endColor = 0x0000FFFF;
        this.direction = new Vector2f(RANDOM.nextFloat() - 0.5F, RANDOM.nextFloat() - 0.5F).normalize();
        this.resizeWithLifetime = false;
        this.size = 1.0F;
        this.enableBlend = true;
        this.blendSrc = GL11.GL_SRC_ALPHA;
        this.blendDst = GL11.GL_ONE;
    }

    public GalacticParticleData(float xStart, float yStart, int maxLifetime, ParticleEmitter emitter) {
        this(1.5F, maxLifetime, xStart, yStart, emitter);
    }
}
