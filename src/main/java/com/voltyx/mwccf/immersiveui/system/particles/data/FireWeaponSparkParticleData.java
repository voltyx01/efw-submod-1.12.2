package com.voltyx.mwccf.immersiveui.system.particles.data;

import com.voltyx.mwccf.immersiveui.util.Vector2f;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Immersive UI GUI spark particle matching the burning flower in GuiSevenScreen.
 * Used when dragging or hovering burning fire weapons in inventory.
 */
public class FireWeaponSparkParticleData extends ParticleData {

    private static final Texture2D TEXTURE = new Texture2D(new ResourceLocation("mwccf", "textures/gui/spark.png"), 8, 8);

    public FireWeaponSparkParticleData(float xStart, float yStart, int lifeTime, ParticleEmitter emitter) {
        super(TEXTURE, 0.65F, lifeTime, xStart, yStart, emitter);
        Random rand = new Random();

        this.enableBlend = true;
        this.blendSrc = GL11.GL_SRC_ALPHA;
        this.blendDst = GL11.GL_ONE; // Additive glowing blend
        this.size = 1.35F;
        this.resizeWithLifetime = true;

        // Radiant white-yellow core fading to fiery orange / charcoal ember
        this.startColor = 0xFFFFF6C0;
        this.endColor = 0x00FF5500;

        // Upward heat buoyancy & air drift
        this.gravityDirection = new Vector2f(0.0F, -1.0F);
        this.gravity = 0.85F;
        this.friction = 0.96F;

        // Gentle spread upwards
        float angle = (rand.nextFloat() - 0.5F) * 75.0F;
        this.direction = Vector2f.rotate(new Vector2f(0.0F, -1.0F), angle);
    }

    public FireWeaponSparkParticleData(float speed, int lifeTime, float xStart, float yStart, ParticleEmitter emitter) {
        this(xStart, yStart, lifeTime, emitter);
        this.speed = speed;
    }
}
