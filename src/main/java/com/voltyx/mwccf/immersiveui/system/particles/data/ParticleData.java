package com.voltyx.mwccf.immersiveui.system.particles.data;

import com.voltyx.mwccf.immersiveui.util.RenderUtils;
import com.voltyx.mwccf.immersiveui.util.Vector2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class ParticleData {

    public static class Texture2D {
        public ResourceLocation rl;
        public float width, height;
        public float texOffX, texOffY;
        public float texWidth, texHeight;

        public Texture2D(ResourceLocation texture, int texOffX, int texOffY, int texWidth, int texHeight, int width, int height) {
            this.rl = texture;
            this.texOffX = texOffX;
            this.texOffY = texOffY;
            this.texWidth = texWidth;
            this.texHeight = texHeight;
            this.width = width;
            this.height = height;
        }

        public Texture2D(ResourceLocation texture, int width, int height) {
            this.rl = texture;
            this.width = width;
            this.height = height;
            this.texWidth = width;
            this.texHeight = height;
        }
    }

    private final Texture2D texture;
    private final float maxSpeed;
    private final int maxLifetime;
    private final Vector2f startPos;
    protected Vector2f oldPos;
    private final ParticleEmitter emitter;

    public Vector2f position;
    public Vector2f direction;
    public Vector2f gravityDirection;
    public float speed;
    public float gravity;
    private float gravityAccel;
    public float friction;
    public float size;
    public float angularVelocity;
    public int lifetime;
    public int startColor;
    public int endColor;
    public int blendSrc;
    public int blendDst;
    public boolean enableBlend;
    public boolean resizeWithLifetime;
    private int tickCount;

    public float waveAmplitude = 0.0F;
    public float waveFrequency = 0.0F;
    public float wavePhase = 0.0F;
    public Vector2f waveNormal = null;

    public ParticleData(Texture2D texture, float maxSpeed, int maxLifetime, float xStart, float yStart, ParticleEmitter emitter) {
        this.texture = texture;
        this.maxSpeed = maxSpeed;
        this.maxLifetime = maxLifetime;
        this.lifetime = maxLifetime;
        this.speed = maxSpeed;
        this.emitter = emitter;
        this.startPos = new Vector2f(xStart, yStart);
        this.position = new Vector2f(startPos);
        this.oldPos = new Vector2f(startPos);
        this.size = 1.0F;
        this.friction = 0.0F;
        this.startColor = 0xFFFFFFFF;
        this.endColor = 0;
        this.direction = new Vector2f(0.0F, 1.0F);
        this.gravityDirection = new Vector2f(0.0F, 1.0F);
        this.angularVelocity = 0.0F;
        this.resizeWithLifetime = true;
        this.enableBlend = true;
        this.blendSrc = GL11.GL_SRC_ALPHA;
        this.blendDst = GL11.GL_ONE;
    }

    public Texture2D getTexture() {
        return texture;
    }

    public ParticleEmitter emitter() {
        return emitter;
    }

    public int getLifetime() {
        return lifetime;
    }

    public int getMaxLifetime() {
        return maxLifetime;
    }

    public void initSubTickMotion(float startX, float startY, Vector2f dir, float spd, float partialTick) {
        this.direction = dir;
        this.speed = spd;
        float vx = dir.x * spd;
        float vy = dir.y * spd;
        this.oldPos.set(startX - vx * partialTick, startY - vy * partialTick);
        this.position.set(oldPos.x + vx, oldPos.y + vy);
    }

    public float getInterpolatedX(float partialTick) {
        float curX = oldPos.x + (position.x - oldPos.x) * partialTick;
        if (waveAmplitude > 0.001F) {
            if (waveNormal == null) {
                waveNormal = new Vector2f(-direction.y, direction.x).normalize();
            }
            float exactTicks = (float) tickCount + partialTick;
            float lifeFactor = maxLifetime > 0 ? Math.max(0.0F, ((float) lifetime - partialTick) / (float) maxLifetime) : 1.0F;
            curX += waveNormal.x * (float) Math.sin(exactTicks * waveFrequency + wavePhase) * (waveAmplitude * lifeFactor);
        }
        return curX;
    }

    public float getInterpolatedY(float partialTick) {
        float curY = oldPos.y + (position.y - oldPos.y) * partialTick;
        if (waveAmplitude > 0.001F) {
            if (waveNormal == null) {
                waveNormal = new Vector2f(-direction.y, direction.x).normalize();
            }
            float exactTicks = (float) tickCount + partialTick;
            float lifeFactor = maxLifetime > 0 ? Math.max(0.0F, ((float) lifetime - partialTick) / (float) maxLifetime) : 1.0F;
            curY += waveNormal.y * (float) Math.sin(exactTicks * waveFrequency + wavePhase) * (waveAmplitude * lifeFactor);
        }
        return curY;
    }

    public void tick() {
        this.oldPos.set(position);

        if (angularVelocity != 0) {
            this.direction = Vector2f.rotate(this.direction.normalize(), angularVelocity);
        } else {
            this.direction.normalize();
        }

        this.speed = MathHelper.clamp(this.speed * (1.0F - friction), 0.0F, maxSpeed);
        this.gravityAccel += gravity / 20.0F;
        this.lifetime = MathHelper.clamp(this.lifetime - 1, 0, maxLifetime);

        this.position.add(direction.x * speed, direction.y * speed);
        if (gravityAccel != 0) {
            this.position.add(gravityDirection.x * gravityAccel, gravityDirection.y * gravityAccel);
        }

        tickCount++;
    }

    public void render(float partialTick) {
        Texture2D tex = getTexture();
        if (tex == null || tex.rl == null) return;

        float lifePercentage = maxLifetime > 0 ? (float) lifetime / (float) maxLifetime : 1.0F;
        int color = RenderUtils.lerpColor(startColor, endColor, 1.0F - lifePercentage);

        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        GlStateManager.pushMatrix();
        GlStateManager.color(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(tex.rl);

        if (enableBlend) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(blendSrc, blendDst);
        }

        float curX = getInterpolatedX(partialTick);
        float curY = getInterpolatedY(partialTick);
        float renderScale = size * (resizeWithLifetime ? lifePercentage : 1.0F);

        RenderUtils.renderTextureFromCenter(curX, curY, tex.texOffX, tex.texOffY,
                tex.texWidth, tex.texHeight, tex.width, tex.height, renderScale);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (enableBlend) {
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );
        }
        GlStateManager.popMatrix();
    }
}
