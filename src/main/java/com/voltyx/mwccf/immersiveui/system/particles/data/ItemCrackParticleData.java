package com.voltyx.mwccf.immersiveui.system.particles.data;

import com.voltyx.mwccf.immersiveui.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class ItemCrackParticleData extends ParticleData {

    private final TextureAtlasSprite sprite;
    private final float u1, u2, v1, v2;

    public ItemCrackParticleData(TextureAtlasSprite sprite, float speed, int lifeTime, float xStart, float yStart, ParticleEmitter emitter) {
        super(new Texture2D(TextureMap.LOCATION_BLOCKS_TEXTURE, 16, 16), speed, lifeTime, xStart, yStart, emitter);
        this.sprite = sprite;
        this.enableBlend = true;
        this.blendSrc = GL11.GL_SRC_ALPHA;
        this.blendDst = GL11.GL_ONE_MINUS_SRC_ALPHA;
        this.size = 2.5F; // small piece size in GUI pixels

        Random rand = new Random();
        // Pick a small random sub-quad within the 16x16 sprite (e.g. 3x3 to 4x4 texels)
        float subSize = 3.0F;
        float offX = rand.nextFloat() * (16.0F - subSize);
        float offY = rand.nextFloat() * (16.0F - subSize);

        if (sprite != null) {
            this.u1 = sprite.getInterpolatedU(offX);
            this.u2 = sprite.getInterpolatedU(offX + subSize);
            this.v1 = sprite.getInterpolatedV(offY);
            this.v2 = sprite.getInterpolatedV(offY + subSize);
        } else {
            this.u1 = 0.0F;
            this.u2 = 1.0F;
            this.v1 = 0.0F;
            this.v2 = 1.0F;
        }

        this.startColor = 0xFFFFFFFF;
        this.endColor = 0xFFFFFFFF;
    }

    @Override
    public void render(float partialTick) {
        if (sprite == null) return;

        float lifePercentage = (float) lifetime / (float) getMaxLifetime();
        int color = RenderUtils.lerpColor(startColor, endColor, 1.0F - lifePercentage);

        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        GlStateManager.pushMatrix();
        GlStateManager.color(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (enableBlend) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(blendSrc, blendDst);
        }

        float curX = getInterpolatedX(partialTick);
        float curY = getInterpolatedY(partialTick);
        float renderScale = size * (resizeWithLifetime ? Math.max(0.2F, lifePercentage) : 1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        GlStateManager.translate(curX, curY, 0.0F);
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        float w2 = 1.5F;
        float h2 = 1.5F;

        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(-w2, h2, 0.0D).tex(u1, v2).endVertex();
        builder.pos(w2, h2, 0.0D).tex(u2, v2).endVertex();
        builder.pos(w2, -h2, 0.0D).tex(u2, v1).endVertex();
        builder.pos(-w2, -h2, 0.0D).tex(u1, v1).endVertex();
        tessellator.draw();

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
