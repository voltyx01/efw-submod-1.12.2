package com.voltyx.mwccf.speech.client;

import com.voltyx.mwccf.speech.SpeechConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

import java.util.ArrayList;
import java.util.List;
import java.nio.FloatBuffer;

@SideOnly(Side.CLIENT)
public class SpeechWorldRenderer {
    private static final ResourceLocation BUTTONS = new ResourceLocation("textures/gui/widgets.png");
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) return;

        RenderManager renderManager = mc.getRenderManager();
        double cameraX = renderManager.viewerPosX;
        double cameraY = renderManager.viewerPosY;
        double cameraZ = renderManager.viewerPosZ;
        long now = System.currentTimeMillis();
        for (SpeechClientManager.SpeechBubble bubble : SpeechClientManager.getBubbles()) {
            Entity entity = mc.world.getEntityByID(bubble.entityId);
            if (!(entity instanceof EntityLivingBase) || entity.isDead) continue;
            double distanceSq = mc.player.getDistanceSq(entity);
            float distance = (float) Math.sqrt(distanceSq);
            if (distance >= SpeechConfig.publicHiddenDistance) continue;

            float distanceAlpha = distance <= SpeechConfig.publicFullAlphaDistance ? 1.0F
                    : 1.0F - (distance - SpeechConfig.publicFullAlphaDistance)
                    / Math.max(0.001F, SpeechConfig.publicHiddenDistance - SpeechConfig.publicFullAlphaDistance);
            float lifeAlpha = getLifeAlpha(bubble, now);
            float alpha = MathHelper.clamp(distanceAlpha * lifeAlpha, 0.0F, 1.0F);
            if (alpha <= 0.005F) continue;

            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - cameraX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks()
                    + entity.height + SpeechConfig.publicBubbleHeight + getStackLevel(bubble, entity, mc.world) * 0.75D - cameraY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - cameraZ;
            renderBubble(bubble, x, y, z, renderManager, alpha, now);
        }
    }

    private static int getStackLevel(SpeechClientManager.SpeechBubble bubble, Entity entity, net.minecraft.world.World world) {
        int level = 0;
        for (SpeechClientManager.SpeechBubble other : SpeechClientManager.getBubbles()) {
            if (other.entityId >= bubble.entityId) continue;
            Entity otherEntity = world.getEntityByID(other.entityId);
            if (otherEntity == null || otherEntity.isDead) continue;
            double dx = entity.posX - otherEntity.posX;
            double dz = entity.posZ - otherEntity.posZ;
            if (dx * dx + dz * dz < 2.25D) level++;
        }
        return Math.min(4, level);
    }

    private static float getLifeAlpha(SpeechClientManager.SpeechBubble bubble, long now) {
        if (bubble.isTyping()) return 1.0F;
        long fadeStart = bubble.expiresAt - SpeechConfig.publicFadeMs;
        if (now <= fadeStart) return 1.0F;
        return MathHelper.clamp((bubble.expiresAt - now) / (float) Math.max(1, SpeechConfig.publicFadeMs), 0.0F, 1.0F);
    }

    private static void renderBubble(SpeechClientManager.SpeechBubble bubble, double x, double y, double z,
                                     RenderManager renderManager, float alpha, long now) {
        Minecraft mc = Minecraft.getMinecraft();
        int maxVisualLines = Math.max(1, (SpeechConfig.publicMaxBubbleHeight - 8) / 10);
        List<String> fullLines = limitLines(wrapText(bubble.getFullText(), SpeechConfig.publicMaxTextWidth), maxVisualLines);
        List<String> visibleLines = limitLines(wrapText(bubble.getVisibleText(), SpeechConfig.publicMaxTextWidth), maxVisualLines);
        int targetWidth = Math.max(38, Math.min(SpeechConfig.publicMaxTextWidth + 12,
            maxLineWidth(mc, fullLines, SpeechConfig.publicMaxTextWidth) + 12));
        int targetHeight = Math.min(SpeechConfig.publicMaxBubbleHeight, Math.max(20, fullLines.size() * 10 + 8));
        float widthProgress = bubble.getWidthProgress(now);
        float heightProgress = bubble.heightAnimationEnd <= bubble.heightAnimationStart || now >= bubble.heightAnimationEnd
            ? 1.0F
            : MathHelper.clamp((now - bubble.heightAnimationStart)
            / (float) (bubble.heightAnimationEnd - bubble.heightAnimationStart), 0.0F, 1.0F);
        int width = Math.max(1, Math.round(bubble.widthAnimationFrom
            + (targetWidth - bubble.widthAnimationFrom) * widthProgress));
        int height = Math.max(12, Math.round(bubble.heightAnimationFrom
            + (targetHeight - bubble.heightAnimationFrom) * heightProgress));
        bubble.renderedWidth = width;
        bubble.renderedHeight = height;

        int previousMatrixMode = GL11.glGetInteger(GL11.GL_MATRIX_MODE);
        int previousActiveTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        int previousTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        boolean wasBlend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean wasLighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean wasCull = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean wasDepthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean wasAlphaTest = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        boolean wasFog = GL11.glIsEnabled(GL11.GL_FOG);
        boolean wasTexture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        boolean wasDepthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        int previousDepthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
        int previousBlendSrc = GL11.glGetInteger(GL11.GL_BLEND_SRC);
        int previousBlendDst = GL11.glGetInteger(GL11.GL_BLEND_DST);
        int previousBlendSrcAlpha = GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA);
        int previousBlendDstAlpha = GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA);
        FloatBuffer previousColor = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, previousColor);
        previousColor.rewind();
        try {
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(SpeechConfig.publicBubbleXOffset, SpeechConfig.publicBubbleYOffset, 0.0F);
            GlStateManager.scale(-SpeechConfig.publicBubbleScale, -SpeechConfig.publicBubbleScale, SpeechConfig.publicBubbleScale);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA,
                    GL11.GL_ONE, GL11.GL_ZERO);
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(GL11.GL_LEQUAL);
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            GlStateManager.disableCull();
            GlStateManager.enableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.depthMask(false);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);

            mc.getTextureManager().bindTexture(BUTTONS);
            drawNineSlice(-width / 2, -height, width, height);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);

            int textY = -height + 4;
            int textColor = (Math.max(0, Math.min(255, (int) (alpha * 255.0F))) << 24) | 0x00FFFF55;
            if (alpha >= 0.25F) {
                GlStateManager.enableAlpha();
                for (String line : visibleLines) {
                    String clean = net.minecraft.util.text.TextFormatting.getTextWithoutFormattingCodes(line);
                    mc.fontRenderer.drawString(net.minecraft.util.text.TextFormatting.YELLOW + clean, -width / 2 + 6, textY, textColor, false);
                    textY += 10;
                }
            }
        } finally {
            GlStateManager.popMatrix();
            GlStateManager.depthMask(wasDepthMask);
            GlStateManager.depthFunc(previousDepthFunc);
            if (wasBlend) {
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(previousBlendSrc, previousBlendDst,
                        previousBlendSrcAlpha, previousBlendDstAlpha);
            } else {
                GlStateManager.disableBlend();
            }
            if (wasLighting) GlStateManager.enableLighting(); else GlStateManager.disableLighting();
            if (wasCull) GlStateManager.enableCull(); else GlStateManager.disableCull();
            if (wasDepthTest) GlStateManager.enableDepth(); else GlStateManager.disableDepth();
            if (wasAlphaTest) GlStateManager.enableAlpha(); else GlStateManager.disableAlpha();
            if (wasFog) GlStateManager.enableFog(); else GlStateManager.disableFog();
            if (wasTexture) GlStateManager.enableTexture2D(); else GlStateManager.disableTexture2D();
            GlStateManager.bindTexture(previousTexture);
            GlStateManager.setActiveTexture(previousActiveTexture);
            GlStateManager.color(previousColor.get(0), previousColor.get(1), previousColor.get(2), previousColor.get(3));
            GlStateManager.matrixMode(previousMatrixMode);
        }
    }

    private static void drawNineSlice(int x, int y, int width, int height) {
        int edgeX = Math.min(4, width / 2);
        int edgeY = Math.min(4, height / 2);
        int middleWidth = Math.max(0, width - edgeX * 2);
        int middleHeight = Math.max(0, height - edgeY * 2);
        int sourceMiddleWidth = 200 - 8;
        int sourceMiddleHeight = 20 - 8;

        drawPatch(x, y, edgeX, edgeY, 0, 46, 4, 4);
        drawPatch(x + edgeX, y, middleWidth, edgeY, 4, 46, sourceMiddleWidth, 4);
        drawPatch(x + edgeX + middleWidth, y, edgeX, edgeY, 196, 46, 4, 4);
        drawPatch(x, y + edgeY, edgeX, middleHeight, 0, 50, 4, sourceMiddleHeight);
        drawPatch(x + edgeX, y + edgeY, middleWidth, middleHeight, 4, 50, sourceMiddleWidth, sourceMiddleHeight);
        drawPatch(x + edgeX + middleWidth, y + edgeY, edgeX, middleHeight, 196, 50, 4, sourceMiddleHeight);
        drawPatch(x, y + edgeY + middleHeight, edgeX, edgeY, 0, 62, 4, 4);
        drawPatch(x + edgeX, y + edgeY + middleHeight, middleWidth, edgeY, 4, 62, sourceMiddleWidth, 4);
        drawPatch(x + edgeX + middleWidth, y + edgeY + middleHeight, edgeX, edgeY, 196, 62, 4, 4);
    }

    private static void drawPatch(int x, int y, int width, int height,
                                  int sourceX, int sourceY, int sourceWidth, int sourceHeight) {
        if (width <= 0 || height <= 0) return;
        float minU = sourceX / 256.0F;
        float maxU = (sourceX + sourceWidth) / 256.0F;
        float minV = sourceY / 256.0F;
        float maxV = (sourceY + sourceHeight) / 256.0F;
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0D).tex(minU, maxV).endVertex();
        buffer.pos(x + width, y + height, 0.0D).tex(maxU, maxV).endVertex();
        buffer.pos(x + width, y, 0.0D).tex(maxU, minV).endVertex();
        buffer.pos(x, y, 0.0D).tex(minU, minV).endVertex();
        Tessellator.getInstance().draw();
    }

    private static int maxLineWidth(Minecraft mc, List<String> lines, int limit) {
        int max = 0;
        for (String line : lines) max = Math.max(max, mc.fontRenderer.getStringWidth(line));
        return Math.min(limit, max);
    }

    private static List<String> wrapText(String text, int width) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) return lines;
        for (String paragraph : text.split("\\n", -1)) {
            if (paragraph.isEmpty()) {
                lines.add("");
            } else {
                lines.addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(paragraph, width));
            }
        }
        return lines;
    }

    private static List<String> limitLines(List<String> lines, int maxLines) {
        if (lines.size() <= maxLines) return lines;
        return new ArrayList<>(lines.subList(lines.size() - maxLines, lines.size()));
    }
}
