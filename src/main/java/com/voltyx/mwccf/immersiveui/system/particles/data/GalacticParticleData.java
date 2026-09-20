package com.voltyx.mwccf.immersiveui.system.particles.data;

import com.voltyx.mwccf.immersiveui.util.Vector2f;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class GalacticParticleData extends ParticleData {
    private static final Random RANDOM = new Random();
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public GalacticParticleData(float maxSpeed, int maxLifetime, float xStart, float yStart, ParticleEmitter emitter) {
        super(new Texture2D(new ResourceLocation("textures/particle/particles.png"),
                0, 0, 8, 8, 8, 8), maxSpeed, maxLifetime, xStart, yStart, emitter);

        this.angularVelocity = 6.0F;
        this.speed = RANDOM.nextFloat() * 2.0F;
        this.startColor = 0xFF8888FF;
        this.endColor = 0x0000FFFF;
        this.direction = new Vector2f(RANDOM.nextFloat() - 0.5F, RANDOM.nextFloat() - 0.5F).normalize();
        this.resizeWithLifetime = false;
        this.size = 1.0F;
    }
}
