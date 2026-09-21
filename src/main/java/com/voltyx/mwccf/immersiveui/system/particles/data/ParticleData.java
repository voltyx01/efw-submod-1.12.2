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
    private Vector2f oldPos;
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
        float lifePercentage = (float) lifetime / (float) maxLifetime;
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

        float curX = oldPos.x + (position.x - oldPos.x) * partialTick;
        float curY = oldPos.y + (position.y - oldPos.y) * partialTick;
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
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
    }
}
