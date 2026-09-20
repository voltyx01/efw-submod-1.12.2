package com.voltyx.mwccf.immersiveui.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderUtils {

    public static void renderTextureFromCenter(float centerX, float centerY, float width, float height, float scale) {
        renderTextureFromCenter(centerX, centerY, 0.0F, 0.0F, width, height, width, height, scale);
    }

    public static void renderTextureFromCenter(float centerX, float centerY, float texOffX, float texOffY,
                                              float texWidth, float texHeight, float width, float height, float scale) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.translate(centerX, centerY, 0.0F);
        GlStateManager.scale(scale, scale, scale);

        float u1 = texOffX / texWidth;
        float u2 = (texOffX + width) / texWidth;
        float v1 = texOffY / texHeight;
        float v2 = (texOffY + height) / texHeight;

        float w2 = width / 2.0F;
        float h2 = height / 2.0F;

        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(-w2, h2, 0.0D).tex(u1, v2).endVertex();
        builder.pos(w2, h2, 0.0D).tex(u2, v2).endVertex();
        builder.pos(w2, -h2, 0.0D).tex(u2, v1).endVertex();
        builder.pos(-w2, -h2, 0.0D).tex(u1, v1).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();
    }

    public static int lerpColor(int colorStart, int colorEnd, float t) {
        if (t < 0.0F) t = 0.0F;
        if (t > 1.0F) t = 1.0F;

        int aStart = (colorStart >> 24) & 0xFF;
        int rStart = (colorStart >> 16) & 0xFF;
        int gStart = (colorStart >> 8) & 0xFF;
        int bStart = colorStart & 0xFF;

        int aEnd = (colorEnd >> 24) & 0xFF;
        int rEnd = (colorEnd >> 16) & 0xFF;
        int gEnd = (colorEnd >> 8) & 0xFF;
        int bEnd = colorEnd & 0xFF;

        int a = (int) (aStart + t * (aEnd - aStart));
        int r = (int) (rStart + t * (rEnd - rStart));
        int g = (int) (gStart + t * (gEnd - gStart));
        int b = (int) (bStart + t * (bEnd - bStart));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static String obfuscateCursedText(String input, double percentage, long seed) {
        if (input == null || input.isEmpty()) return input;
        Random random = new Random(seed);
        char[] chars = input.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (char c : chars) {
            if (random.nextDouble() < percentage && Character.isLetterOrDigit(c)) {
                // Insert obfuscated symbol formatting §k...§r
                sb.append("§4§k").append(c).append("§c");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
