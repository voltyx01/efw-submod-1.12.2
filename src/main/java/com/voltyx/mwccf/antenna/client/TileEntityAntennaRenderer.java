package com.voltyx.mwccf.antenna.client;

import com.voltyx.mwccf.antenna.BlockAntenna;
import com.voltyx.mwccf.antenna.TileEntityAntenna;
import com.voltyx.mwccf.render.bedrock.BedrockBlockModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityAntennaRenderer extends TileEntitySpecialRenderer<TileEntityAntenna> {

    private static final BedrockBlockModel MODEL = new BedrockBlockModel();
    private static final ResourceLocation MODEL_GEO = new ResourceLocation("mwccf", "geo/antenna.geo.json");
    private static final ResourceLocation MODEL_ANIM = new ResourceLocation("mwccf", "animations/antenna.animation.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation("mwccf", "textures/blocks/antenna.png");

    private static final String[] BUTTON_NAMES = {
            "one", "two", "three",
            "four", "five", "six",
            "seven", "eight", "nine",
            "clear", "zero", "okay"
    };

    private static final String[] BUTTON_LABELS = {
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "C", "0", "K"
    };

    // Center coordinates in model pixels (Y is vertical, X is horizontal, Z is front-back)
    private static final float[][] BUTTON_COORDS = {
            {6.3F, 9.2F, -8.605F}, // one
            {5.0F, 9.2F, -8.605F}, // two
            {3.7F, 9.2F, -8.605F}, // three
            {6.3F, 8.0F, -8.605F}, // four
            {5.0F, 8.0F, -8.605F}, // five
            {3.7F, 8.0F, -8.605F}, // six
            {6.3F, 6.8F, -8.605F}, // seven
            {5.0F, 6.8F, -8.605F}, // eight
            {3.7F, 6.8F, -8.605F}, // nine
            {6.3F, 5.6F, -8.605F}, // clear (C)
            {5.0F, 5.6F, -8.605F}, // zero (0)
            {3.7F, 5.6F, -8.605F}  // okay (K)
    };

    private static void ensureLoaded() {
        if (!MODEL.isLoaded()) {
            MODEL.load(MODEL_GEO, MODEL_ANIM);
        }
    }

    @Override
    public void render(TileEntityAntenna te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null) return;

        ensureLoaded();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);

        EnumFacing facing = EnumFacing.NORTH;
        if (te.hasWorld() && te.getPos() != null) {
            try {
                facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockAntenna.FACING);
            } catch (Throwable ignored) {
            }
        }

        // Facing rotation: front of antenna faces towards player who placed it
        switch (facing) {
            case NORTH: GlStateManager.rotate(0, 0, 1, 0); break;
            case SOUTH: GlStateManager.rotate(180, 0, 1, 0); break;
            case WEST:  GlStateManager.rotate(90, 0, 1, 0); break;
            case EAST:  GlStateManager.rotate(270, 0, 1, 0); break;
            default: break;
        }

        this.bindTexture(TEXTURE);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableCull();

        // 1. Reset and blend animations
        MODEL.resetAnimations();
        if (te.hasWorld()) {
            float doorTime = te.getInterpolatedDoorAnimTime(partialTicks);
            if (doorTime > 0.0f) {
                MODEL.blendAnimation("dooropen", doorTime);
            }

            for (String btn : BUTTON_NAMES) {
                float btnTime = te.getButtonAnimTime(btn);
                if (btnTime > 0.0f) {
                    MODEL.blendAnimation("press_" + btn, btnTime);
                }
            }
        }

        // 2. Render Bedrock Model
        MODEL.render(1.0F / 16.0F);
        GlStateManager.enableCull();

        // 3. Render Button Labels (1..9, C, 0, K)
        renderButtonLabels(te);

        // 4. Render Dynamic Screen & 3D Indicators
        renderDisplayAndIndicators(te, partialTicks);

        // Fully restore OpenGL states to guarantee no leak into beds or other world renderers
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }

    private void renderButtonLabels(TileEntityAntenna te) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fr = mc.fontRenderer;
        if (fr == null) return;

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        int prevLightX = (int) OpenGlHelper.lastBrightnessX;
        int prevLightY = (int) OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(false);

        for (int i = 0; i < BUTTON_NAMES.length; i++) {
            String btnName = BUTTON_NAMES[i];
            String label = BUTTON_LABELS[i];
            float[] coords = BUTTON_COORDS[i];

            float bx = coords[0] / 16.0F;
            float by = coords[1] / 16.0F;
            float bz = coords[2] / 16.0F;

            // Follow animated button bone position (when pressed, moves +Z)
            BedrockBlockModel.Bone bone = MODEL.getBone(btnName);
            if (bone != null) {
                bz += (bone.animPos[2] / 16.0F);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(bx, by, bz);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

            float textScale = 0.0034F;
            GlStateManager.scale(textScale, -textScale, textScale);

            int strW = fr.getStringWidth(label);
            fr.drawString(label, -strW / 2, -4, 0xFFC8C8C8);

            GlStateManager.popMatrix();
        }

        GlStateManager.depthMask(true);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevLightX, prevLightY);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private void renderDisplayAndIndicators(TileEntityAntenna te, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        int prevLightX = (int) OpenGlHelper.lastBrightnessX;
        int prevLightY = (int) OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();

        // -------------------------------------------------------------
        // A. 3D ALL-SIDES INDICATORS (Full 3D glowing box over cubes)
        // -------------------------------------------------------------
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();

        AntennaSession session = AntennaSession.getInstance();
        boolean isCurrentBlockActive = te.hasWorld()
                && AntennaCameraController.getAntennaPos() != null
                && AntennaCameraController.getAntennaPos().equals(te.getPos());

        float red = 0.55F, green = 0.05F, blue = 0.05F;
        if (te.isUnlocked() || (isCurrentBlockActive && "ACCESS".equals(session.getStatusMessage()))) {
            // Unlocked: soft pleasant indicator green
            red = 0.05F; green = 0.55F; blue = 0.12F;
        } else if (isCurrentBlockActive && "ERROR".equals(session.getStatusMessage())) {
            // Error: flashing red/orange
            float flash = (float) Math.abs(Math.sin(System.currentTimeMillis() * 0.02));
            red = 0.6F; green = 0.1F * flash; blue = 0.04F;
        }

        // Draw 3D Box covering all 6 sides of both indicator cubes
        // Left indicator: X = +6.0 to +7.0
        draw3DBox(buf, tessellator,
                (5.98F) / 16.0F, (7.02F) / 16.0F,
                (12.88F) / 16.0F, (13.92F) / 16.0F,
                (-8.32F) / 16.0F, (-7.28F) / 16.0F,
                red, green, blue, 0.80F);

        // Right indicator: X = +3.0 to +4.0
        draw3DBox(buf, tessellator,
                (2.98F) / 16.0F, (4.02F) / 16.0F,
                (12.88F) / 16.0F, (13.92F) / 16.0F,
                (-8.32F) / 16.0F, (-7.28F) / 16.0F,
                red, green, blue, 0.80F);

        // -------------------------------------------------------------
        // B. DISPLAY SCREEN (Origin: [3.6, 10.3, -8.5], Size: [2.8, 1.2, 0.9])
        // -------------------------------------------------------------
        float dispFade = 0.0F;
        if (isCurrentBlockActive) {
            dispFade = session.getScreenFade();
        }

        float screenZ = (-8.5F - 0.015F) / 16.0F;
        float minX = 3.6F / 16.0F;
        float maxX = 6.4F / 16.0F;
        float minY = 10.3F / 16.0F;
        float maxY = 11.5F / 16.0F;
        float centerX = (minX + maxX) / 2.0F;
        float centerY = (minY + maxY) / 2.0F;

        // Background color:
        // Idle: Deep dark CRT black (0.01, 0.01, 0.02)
        // Active: Deep rich glowing blue (0.04, 0.22, 0.65)
        float sR = 0.01F + 0.03F * dispFade;
        float sG = 0.01F + 0.21F * dispFade;
        float sB = 0.02F + 0.63F * dispFade;

        drawFrontQuad(buf, tessellator, minX, maxX, minY, maxY, screenZ, sR, sG, sB, 1.0F);

        // -------------------------------------------------------------
        // C. DISPLAY TEXT (White digits / Status)
        // -------------------------------------------------------------
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.enableTexture2D();
        if (isCurrentBlockActive && dispFade > 0.05F && mc.fontRenderer != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(centerX, centerY, screenZ - 0.003F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

            float textScale = 0.0036F;
            GlStateManager.scale(textScale, -textScale, textScale);

            FontRenderer fr = mc.fontRenderer;
            String status = session.getStatusMessage();

            if ("ACCESS".equals(status)) {
                int w = fr.getStringWidth("ACCESS");
                fr.drawString("ACCESS", -w / 2, -4, 0xFF55FF55);
            } else if ("ERROR".equals(status)) {
                int w = fr.getStringWidth("ERROR");
                fr.drawString("ERROR", -w / 2, -4, 0xFFFF5555);
            } else {
                String input = session.getInputBuffer();
                boolean blink = session.isCursorVisible();

                int slotSpacing = 7;
                int startX = -((4 - 1) * slotSpacing) / 2;

                for (int slot = 0; slot < 4; slot++) {
                    int slotX = startX + slot * slotSpacing;
                    if (slot < input.length()) {
                        String digit = String.valueOf(input.charAt(slot));
                        int dw = fr.getStringWidth(digit);
                        fr.drawString(digit, slotX - dw / 2, -4, 0xFFFFFFFF);
                    } else {
                        if (blink) {
                            int dw = fr.getStringWidth("_");
                            fr.drawString("_", slotX - dw / 2, -4, 0xFFFFFFFF);
                        }
                    }
                }
            }

            GlStateManager.popMatrix();
        }

        // Cleanly restore GL state
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevLightX, prevLightY);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private static void drawFrontQuad(BufferBuilder buf, Tessellator tessellator,
                                      float x0, float x1, float y0, float y1, float z,
                                      float r, float g, float b, float a) {
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(x0, y0, z).color(r, g, b, a).endVertex();
        buf.pos(x1, y0, z).color(r, g, b, a).endVertex();
        buf.pos(x1, y1, z).color(r, g, b, a).endVertex();
        buf.pos(x0, y1, z).color(r, g, b, a).endVertex();
        tessellator.draw();
    }

    private static void draw3DBox(BufferBuilder buf, Tessellator tessellator,
                                  float x0, float x1, float y0, float y1, float z0, float z1,
                                  float r, float g, float b, float a) {
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // Front face (-Z)
        buf.pos(x0, y0, z0).color(r, g, b, a).endVertex();
        buf.pos(x1, y0, z0).color(r, g, b, a).endVertex();
        buf.pos(x1, y1, z0).color(r, g, b, a).endVertex();
        buf.pos(x0, y1, z0).color(r, g, b, a).endVertex();

        // Back face (+Z)
        buf.pos(x1, y0, z1).color(r, g, b, a).endVertex();
        buf.pos(x0, y0, z1).color(r, g, b, a).endVertex();
        buf.pos(x0, y1, z1).color(r, g, b, a).endVertex();
        buf.pos(x1, y1, z1).color(r, g, b, a).endVertex();

        // Top face (+Y)
        buf.pos(x0, y1, z0).color(r, g, b, a).endVertex();
        buf.pos(x1, y1, z0).color(r, g, b, a).endVertex();
        buf.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        buf.pos(x0, y1, z1).color(r, g, b, a).endVertex();

        // Bottom face (-Y)
        buf.pos(x0, y0, z1).color(r, g, b, a).endVertex();
        buf.pos(x1, y0, z1).color(r, g, b, a).endVertex();
        buf.pos(x1, y0, z0).color(r, g, b, a).endVertex();
        buf.pos(x0, y0, z0).color(r, g, b, a).endVertex();

        // Left face (-X)
        buf.pos(x0, y0, z1).color(r, g, b, a).endVertex();
        buf.pos(x0, y0, z0).color(r, g, b, a).endVertex();
        buf.pos(x0, y1, z0).color(r, g, b, a).endVertex();
        buf.pos(x0, y1, z1).color(r, g, b, a).endVertex();

        // Right face (+X)
        buf.pos(x1, y0, z0).color(r, g, b, a).endVertex();
        buf.pos(x1, y0, z1).color(r, g, b, a).endVertex();
        buf.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        buf.pos(x1, y1, z0).color(r, g, b, a).endVertex();

        tessellator.draw();
    }
}
