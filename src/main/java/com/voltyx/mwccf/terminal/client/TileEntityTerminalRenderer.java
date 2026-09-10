package com.voltyx.mwccf.terminal.client;

import com.voltyx.mwccf.furniture.BlockFurnitureHorizontal;
import com.voltyx.mwccf.render.bedrock.BedrockBlockModel;
import com.voltyx.mwccf.terminal.TileEntityTerminal;
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
import java.util.List;

@SideOnly(Side.CLIENT)
public class TileEntityTerminalRenderer extends TileEntitySpecialRenderer<TileEntityTerminal> {

    private static final BedrockBlockModel MODEL = new BedrockBlockModel();
    private static final ResourceLocation MODEL_GEO = new ResourceLocation("mwccf", "geo/terminal.geo.json");
    private static final ResourceLocation MODEL_ANIM = new ResourceLocation("mwccf", "animations/terminal.animation.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation("mwccf", "textures/blocks/terminal.png");

    private static void ensureLoaded() {
        if (!MODEL.isLoaded()) {
            MODEL.load(MODEL_GEO, MODEL_ANIM);
        }
    }

    @Override
    public void render(TileEntityTerminal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null || !te.hasWorld()) return;

        ensureLoaded();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);

        EnumFacing facing = EnumFacing.NORTH;
        if (te.getWorld() != null && te.getPos() != null) {
            try {
                facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockFurnitureHorizontal.FACING);
            } catch (Throwable ignored) {
            }
        }

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
        // Apply keyboard animation
        float animTime = te.getInterpolatedAnimTime(partialTicks);
        MODEL.applyAnimation("keyboardopen", animTime);

        // Render Bedrock Model
        MODEL.render(1.0F / 16.0F);
        GlStateManager.enableCull();

        // Render dynamic glowing CRT screen with smooth fade-in
        // ONLY for the local player who entered this terminal.
        // Other nearby players see only the dark inactive monitor!
        if (TerminalCameraController.getTerminalPos() != null
                && TerminalCameraController.getTerminalPos().equals(te.getPos())
                && TerminalSession.getInstance().getScreenFade() > 0.01F
                && TerminalCameraController.getTransitionProgress() > 0.08F) {
            renderMonitorScreen(te, partialTicks);
        }

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void renderMonitorScreen(TileEntityTerminal te, float partialTicks) {
        float fade = TerminalSession.getInstance().getScreenFade();
        if (fade <= 0.01F) return;

        GlStateManager.pushMatrix();

        // Position directly on the front surface of the display monitor
        // In local model coordinates, the display box is at Z: 7, X: -7..7, Y: 4..15
        // We place screen quad slightly forward towards the room / player at Z = (7.0F - 0.015F) / 16.0F
        float screenZ = (7.0F - 0.015F) / 16.0F;
        GlStateManager.translate(0.0F, 9.5F / 16.0F, screenZ);
        // Face the front (towards player)
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

        // Make screen 100% fullbright and immune to any world lighting
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.entityRenderer != null) {
            mc.entityRenderer.disableLightmap();
        }
        GlStateManager.disableLighting();
        int prevLightX = (int) OpenGlHelper.lastBrightnessX;
        int prevLightY = (int) OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        float screenW = 14.0F / 16.0F;
        float screenH = 9.0F / 16.0F;
        float halfW = screenW / 2.0F;
        float halfH = screenH / 2.0F;

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.disableCull();

        // Layer 1: Inky dark CRT background quad (deep black with faint phosphor glow) with fade-in
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-1.0F, -10.0F);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(GL11.GL_LEQUAL);

        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();

        // Main screen background quad at Z = 0.0F (fading in smoothly, deeper dark CRT black)
        float r = 0.006F * fade;
        float g = 0.014F * fade;
        float b = 0.008F * fade;
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(-halfW, -halfH, 0.0F).color(r, g, b, fade).endVertex();
        buf.pos(halfW, -halfH, 0.0F).color(r, g, b, fade).endVertex();
        buf.pos(halfW, halfH, 0.0F).color(r, g, b, fade).endVertex();
        buf.pos(-halfW, halfH, 0.0F).color(r, g, b, fade).endVertex();
        tessellator.draw();

        // Layer 2: Terminal text & prompt (no borders or divider line)
        // Disable depth writing and increase polygon offset so text never Z-fights with background
        GlStateManager.depthMask(false);
        GlStateManager.doPolygonOffset(-3.0F, -30.0F);

        if (mc.fontRenderer != null) {
            GlStateManager.enableTexture2D();
            GlStateManager.pushMatrix();

            // Translate 4mm forward into the room (+Z direction) to avoid depth issues
            GlStateManager.translate(0.0F, 0.0F, 0.004F);

            float textScale = 0.0038F;
            GlStateManager.scale(textScale, -textScale, textScale);

            // Keep fullbright active
            GlStateManager.disableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            FontRenderer fr = mc.fontRenderer;
            float startX = (-halfW + 0.018F) / textScale;
            float startY = -(halfH - 0.018F) / textScale;
            int maxW = (int) ((screenW - 0.036F) / textScale);
            int curY = (int) startY;
            int lineSpacing = 10;

            TerminalSession session = TerminalSession.getInstance();
            TerminalSession.Stage stage = session.getStage();

            // Header
            curY = drawWrappedString(fr, "MW-OS 5.15.0-mw (tty1)", (int) startX, curY, 0xFF55FFBB, fade, maxW);
            curY += 2;

            String cursor = session.isCursorVisible() ? "_" : " ";

            if (stage == TerminalSession.Stage.LOGIN_USER) {
                String prompt = "mw-terminal login: " + session.getInputBuffer() + cursor;
                curY = drawWrappedString(fr, prompt, (int) startX, curY, 0xFF44FFAA, fade, maxW);
            } else if (stage == TerminalSession.Stage.LOGIN_PASS) {
                curY = drawWrappedString(fr, "mw-terminal login: " + session.getEnteredUser(), (int) startX, curY, 0xFF44FFAA, fade, maxW);
                StringBuilder stars = new StringBuilder();
                for (int i = 0; i < session.getInputBuffer().length(); i++) {
                    stars.append("*");
                }
                String prompt = "Password: " + stars + cursor;
                curY = drawWrappedString(fr, prompt, (int) startX, curY, 0xFF44FFAA, fade, maxW);
            } else if (stage == TerminalSession.Stage.LOGIN_ERROR) {
                curY = drawWrappedString(fr, "mw-terminal login: " + session.getEnteredUser(), (int) startX, curY, 0xFF44FFAA, fade, maxW);
                curY = drawWrappedString(fr, "Password: ****", (int) startX, curY, 0xFF44FFAA, fade, maxW);
                curY += 2;
                curY = drawWrappedString(fr, "Login incorrect", (int) startX, curY, 0xFFFF5555, fade, maxW);
            } else if (stage == TerminalSession.Stage.BOOT_SEQUENCE) {
                curY = drawWrappedString(fr, "Last login: root on tty1", (int) startX, curY, 0xFF33CC88, fade, maxW);
                curY += 2;

                int step = session.getBootStep();
                for (int i = 0; i < step && i < TerminalSession.BOOT_LINES.length; i++) {
                    TerminalSession.ConsoleLine bl = TerminalSession.BOOT_LINES[i];
                    if (bl.tag != null) {
                        curY = drawStatusLine(fr, (int) startX, curY, bl.tag, bl.tagColor, bl.text, bl.textColor, fade, maxW);
                    } else {
                        curY = drawWrappedString(fr, bl.text, (int) startX, curY, bl.textColor, fade, maxW);
                    }
                }
            } else if (stage == TerminalSession.Stage.SHELL) {
                String netStatus = session.isEth0Up() ? "eth0: UP" : "eth0: DOWN";
                int netColor = session.isEth0Up() ? 0xFF00FF66 : 0xFFFF4444;

                int curX = (int) startX;
                fr.drawString("status: READY | ", curX, curY, applyFade(0xFF33CC88, fade), false);
                curX += fr.getStringWidth("status: READY | ");
                fr.drawString(netStatus, curX, curY, applyFade(netColor, fade), false);
                curX += fr.getStringWidth(netStatus);
                fr.drawString(" | user: root", curX, curY, applyFade(0xFF33CC88, fade), false);
                curY += lineSpacing + 2;

                for (TerminalSession.ConsoleLine cl : session.getShellOutput()) {
                    if (cl.tag != null) {
                        curY = drawStatusLine(fr, (int) startX, curY, cl.tag, cl.tagColor, cl.text, cl.textColor, fade, maxW);
                    } else {
                        curY = drawWrappedString(fr, cl.text, (int) startX, curY, cl.textColor, fade, maxW);
                    }
                }

                String prompt = "root@mw-terminal:~# " + session.getInputBuffer() + cursor;
                curY = drawWrappedString(fr, prompt, (int) startX, curY, 0xFF44FFAA, fade, maxW);
            }

            GlStateManager.popMatrix();
        }

        // Restore OpenGL states
        GlStateManager.disablePolygonOffset();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevLightX, prevLightY);
        if (mc.entityRenderer != null) {
            mc.entityRenderer.enableLightmap();
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.popMatrix();
    }

    private int applyFade(int color, float fade) {
        int a = (color >> 24) & 0xFF;
        if (a == 0) a = 255;
        int fa = Math.max(0, Math.min(255, (int) (a * fade)));
        return (fa << 24) | (color & 0x00FFFFFF);
    }

    private int drawStatusLine(FontRenderer fr, int x, int y, String status, int statusColor, String msg, int msgColor, float fade, int maxW) {
        String tag = "[" + status + "] ";
        int tagW = fr.getStringWidth(tag);
        int curX = x;

        fr.drawString("[", curX, y, applyFade(0xFF88AA99, fade), false);
        curX += fr.getStringWidth("[");
        fr.drawString(status, curX, y, applyFade(statusColor, fade), false);
        curX += fr.getStringWidth(status);
        fr.drawString("] ", curX, y, applyFade(0xFF88AA99, fade), false);
        curX += fr.getStringWidth("] ");

        int avail = maxW - tagW;
        if (fr.getStringWidth(msg) <= avail) {
            fr.drawString(msg, curX, y, applyFade(msgColor, fade), false);
            return y + 10;
        }

        List<String> wrapped = fr.listFormattedStringToWidth(msg, avail);
        if (!wrapped.isEmpty()) {
            fr.drawString(wrapped.get(0), curX, y, applyFade(msgColor, fade), false);
            y += 10;
            for (int i = 1; i < wrapped.size(); i++) {
                fr.drawString(wrapped.get(i), x + 8, y, applyFade(msgColor, fade), false);
                y += 10;
            }
        }
        return y;
    }

    private int drawWrappedString(FontRenderer fr, String text, int x, int y, int color, float fade, int maxW) {
        if (fr.getStringWidth(text) <= maxW) {
            fr.drawString(text, x, y, applyFade(color, fade), false);
            return y + 10;
        }
        List<String> wrapped = fr.listFormattedStringToWidth(text, maxW);
        for (String line : wrapped) {
            fr.drawString(line, x, y, applyFade(color, fade), false);
            y += 10;
        }
        return y;
    }
}
