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
import com.voltyx.mwccf.terminal.bodycam.BodycamEntry;
import com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TileEntityTerminalRenderer extends TileEntitySpecialRenderer<TileEntityTerminal> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        if (te == null) return;

        ensureLoaded();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);

        EnumFacing facing = EnumFacing.NORTH;
        if (te.hasWorld() && te.getWorld() != null && te.getPos() != null) {
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

        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.disableCull();
        // Reset and apply keyboard animation
        MODEL.resetAnimations();
        float animTime = te.hasWorld() ? te.getInterpolatedAnimTime(partialTicks) : 0.0F;
        MODEL.applyAnimation("keyboardopen", animTime);

        // Render Bedrock Model
        MODEL.render(1.0F / 16.0F);
        GlStateManager.enableCull();

        // Render dynamic glowing CRT screen with smooth fade-in
        if (te.hasWorld() && te.getPos() != null) {
            TerminalSession session = TerminalSession.getInstance();
            boolean isSessionTerminal = (session.getTerminalPos() != null && session.getTerminalPos().equals(te.getPos()));
            boolean isStreamingBodycam = (isSessionTerminal && session.getStage() == TerminalSession.Stage.BODYCAM_VIEW);

            boolean isInteracting = (TerminalCameraController.getTerminalPos() != null
                    && TerminalCameraController.getTerminalPos().equals(te.getPos())
                    && session.getScreenFade() > 0.01F
                    && TerminalCameraController.getTransitionProgress() > 0.08F);

            if (isStreamingBodycam || isInteracting) {
                renderMonitorScreen(te, partialTicks);
            }
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void renderMonitorScreen(TileEntityTerminal te, float partialTicks) {
        TerminalSession session = TerminalSession.getInstance();
        boolean isStreamingBodycam = (session.getStage() == TerminalSession.Stage.BODYCAM_VIEW);
        float fade = isStreamingBodycam ? 1.0F : session.getScreenFade();
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

        TerminalSession.Stage stage = session.getStage();
        BodycamEntry currentCam = session.getCurrentViewingCamera();
        boolean hasFeed = (stage == TerminalSession.Stage.BODYCAM_VIEW
                && currentCam != null
                && currentCam.isOnline
                && BodycamFeedRenderer.hasValidTexture());

        if (hasFeed) {
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(BodycamFeedRenderer.getTextureId());
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            // Opaque bodycam feed: disable blend so internal FBO alpha (e.g. 0.0 on sky/sun/fog) does NOT punch transparent holes
            GlStateManager.disableBlend();
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buf.pos(-halfW, -halfH, 0.0F).tex(0.0, 0.0).endVertex();
            buf.pos(halfW, -halfH, 0.0F).tex(1.0, 0.0).endVertex();
            buf.pos(halfW, halfH, 0.0F).tex(1.0, 1.0).endVertex();
            buf.pos(-halfW, halfH, 0.0F).tex(0.0, 1.0).endVertex();
            tessellator.draw();

            // Re-enable blend for phosphor tint and text HUD
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);

            // Subtle tactical phosphor tint
            GlStateManager.disableTexture2D();
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            buf.pos(-halfW, -halfH, 0.001F).color(0.0F, 0.06F, 0.02F, 0.08F * fade).endVertex();
            buf.pos(halfW, -halfH, 0.001F).color(0.0F, 0.06F, 0.02F, 0.08F * fade).endVertex();
            buf.pos(halfW, halfH, 0.001F).color(0.0F, 0.06F, 0.02F, 0.08F * fade).endVertex();
            buf.pos(-halfW, halfH, 0.001F).color(0.0F, 0.06F, 0.02F, 0.08F * fade).endVertex();
            tessellator.draw();
        } else {
            // Main screen background quad at Z = 0.0F (fading in smoothly, deeper dark CRT black)
            GlStateManager.disableTexture2D();
            float r = (stage == TerminalSession.Stage.BODYCAM_VIEW ? 0.012F : 0.006F) * fade;
            float g = (stage == TerminalSession.Stage.BODYCAM_VIEW ? 0.012F : 0.014F) * fade;
            float b = (stage == TerminalSession.Stage.BODYCAM_VIEW ? 0.014F : 0.008F) * fade;
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            buf.pos(-halfW, -halfH, 0.0F).color(r, g, b, fade).endVertex();
            buf.pos(halfW, -halfH, 0.0F).color(r, g, b, fade).endVertex();
            buf.pos(halfW, halfH, 0.0F).color(r, g, b, fade).endVertex();
            buf.pos(-halfW, halfH, 0.0F).color(r, g, b, fade).endVertex();
            tessellator.draw();
        }

        // Layer 2: Terminal text & prompt (no borders or divider line)
        // Disable depth writing and increase polygon offset so text never Z-fights with background
        GlStateManager.depthMask(false);
        GlStateManager.doPolygonOffset(-3.0F, -30.0F);

        if (mc.fontRenderer != null) {
            GlStateManager.enableTexture2D();
            GlStateManager.pushMatrix();

            // Translate 4mm forward into the room (+Z direction) to avoid depth issues
            GlStateManager.translate(0.0F, 0.0F, 0.004F);

            float textScale = (stage == TerminalSession.Stage.BODYCAM_VIEW) ? 0.0030F : 0.0038F;
            GlStateManager.scale(textScale, -textScale, textScale);

            // Keep fullbright active
            GlStateManager.disableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            FontRenderer fr = mc.fontRenderer;
            float margin = (stage == TerminalSession.Stage.BODYCAM_VIEW) ? 0.024F : 0.018F;
            float startX = (-halfW + margin) / textScale;
            float startY = -(halfH - margin) / textScale;
            int maxW = (int) ((screenW - margin * 2.0F) / textScale);
            int curY = (int) startY;
            int lineSpacing = 10;

            if (stage == TerminalSession.Stage.BODYCAM_VIEW) {
                renderBodycamViewHUD(mc, fr, session, fade, startX, startY, maxW, lineSpacing, halfW, halfH, textScale);
            } else {
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
                    if (i == 2 && session.hasModule()) {
                        bl = new TerminalSession.ConsoleLine("OK", 0xFF00FF66, "Network interface eth0: link UP", 0xFF44FFAA);
                    }
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
            } else if (stage == TerminalSession.Stage.CHAT_MENU) {
                if (session.getSubMenu() == TerminalSession.ChatSubMenu.GROUPS) {
                    curY = drawWrappedString(fr, "=== Groups ===", (int) startX, curY, 0xFF55FFBB, fade, maxW);
                    curY += 2;
                    for (int i = 0; i < TerminalSession.GROUPS_MENU_ITEMS.length; i++) {
                        boolean sel = (i == session.getGroupsMenuIndex());
                        String prefix = sel ? "> " : "  ";
                        int col = sel ? 0xFF00FF66 : 0xFF44FFAA;
                        curY = drawWrappedString(fr, prefix + TerminalSession.GROUPS_MENU_ITEMS[i], (int) startX, curY, col, fade, maxW);
                    }
                } else if (session.getSubMenu() == TerminalSession.ChatSubMenu.PRIVATE) {
                    curY = drawWrappedString(fr, "=== Private Chats ===", (int) startX, curY, 0xFF55FFBB, fade, maxW);
                    curY += 2;
                    java.util.List<String> users = session.getModuleUsers();
                    for (int i = 0; i < users.size(); i++) {
                        boolean sel = (i == session.getPrivateMenuIndex());
                        String prefix = sel ? "> " : "  ";
                        int col = sel ? 0xFF00FF66 : 0xFF44FFAA;
                        curY = drawWrappedString(fr, prefix + users.get(i), (int) startX, curY, col, fade, maxW);
                    }
                } else {
                    curY = drawWrappedString(fr, "=== Terminal Network Chat ===", (int) startX, curY, 0xFF55FFBB, fade, maxW);
                    curY += 2;
                    for (int i = 0; i < TerminalSession.CHAT_MENU_ITEMS.length; i++) {
                        boolean sel = (i == session.getChatMenuIndex());
                        String prefix = sel ? "> " : "  ";
                        int col = sel ? 0xFF00FF66 : 0xFF44FFAA;
                        curY = drawWrappedString(fr, prefix + TerminalSession.CHAT_MENU_ITEMS[i], (int) startX, curY, col, fade, maxW);
                    }
                }
                curY += 2;
                String nav = session.getSubMenu() != TerminalSession.ChatSubMenu.MAIN
                        ? "[Up/Down] Select  [Enter] Open  [Esc] Back"
                        : "[Up/Down] Select  [Enter] Open  [Esc] Close";
                curY = drawWrappedString(fr, nav, (int) startX, curY, 0xFF888888, fade, maxW);
            } else if (stage == TerminalSession.Stage.APP_CHAT) {
                curY = drawWrappedString(fr, "=== " + session.getCurrentChatRoom() + " ===", (int) startX, curY, 0xFF55FFBB, fade, maxW);
                curY = drawWrappedString(fr, "[Ctrl+C] Exit  |  [Esc] Back  |  eth0: UP", (int) startX, curY, 0xFF448866, fade, maxW);
                curY += 1;

                for (TerminalSession.ConsoleLine cl : session.getChatMessages()) {
                    if (cl.tag != null) {
                        curY = drawStatusLine(fr, (int) startX, curY, cl.tag, cl.tagColor, cl.text, cl.textColor, fade, maxW);
                    } else {
                        curY = drawWrappedString(fr, cl.text, (int) startX, curY, cl.textColor, fade, maxW);
                    }
                }

                String prompt = "> " + session.getInputBuffer() + cursor;
                curY = drawWrappedString(fr, prompt, (int) startX, curY, 0xFF00FF66, fade, maxW);
            } else if (stage == TerminalSession.Stage.BODYCAM_LIST) {
                curY = drawWrappedString(fr, "=== BODYCAM SURVEILLANCE v1.2 ===", (int) startX, curY, 0xFF55FFBB, fade, maxW);
                curY += 2;
                java.util.List<com.voltyx.mwccf.terminal.bodycam.BodycamEntry> cams = session.getAvailableCameras();
                if (cams.isEmpty()) {
                    curY = drawWrappedString(fr, "No active cameras detected.", (int) startX, curY, 0xFF888888, fade, maxW);
                    curY = drawWrappedString(fr, "Wear a bodycam and power it ON.", (int) startX, curY, 0xFF666666, fade, maxW);
                } else {
                    int selected = session.getBodycamIndex();
                    for (int i = 0; i < cams.size(); i++) {
                        com.voltyx.mwccf.terminal.bodycam.BodycamEntry c = cams.get(i);
                        boolean isSel = (i == selected);
                        String prefix = isSel ? "> " : "  ";
                        String check = c.isChecked ? "[X] " : "[ ] ";
                        String status = c.isOnline ? "[ONLINE]" : "[OFFLINE]";
                        int statusCol = c.isOnline ? 0xFF00FF66 : 0xFFFF4444;
                        int textCol = isSel ? 0xFF55FFDD : 0xFF33CC88;

                        String lineText = prefix + check + c.camId + " " + c.carrierName;
                        int lineX = (int) startX;
                        fr.drawString(lineText, lineX, curY, applyFade(textCol, fade), false);
                        int statusX = (int) startX + maxW - fr.getStringWidth(status);
                        fr.drawString(status, statusX, curY, applyFade(statusCol, fade), false);
                        curY += lineSpacing;
                    }
                }
                curY += 2;
                String nav = "[Up/Down] Select  [Tab] Check  [Enter] View  [Esc] Exit";
                curY = drawWrappedString(fr, nav, (int) startX, curY, 0xFF888888, fade, maxW);
            }
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

    private void renderBodycamViewHUD(Minecraft mc, FontRenderer fr, TerminalSession session, float fade,
                                      float startX, float startY, int maxW, int lineSpacing,
                                      float halfW, float halfH, float textScale) {
        BodycamEntry cam = session.getCurrentViewingCamera();
        int totalH = (int) ((halfH * 2.0F - 0.048F) / textScale);

        if (cam == null) {
            String msg = "NO ACTIVE CAMERAS FOUND";
            fr.drawString(msg, (int) (startX + (maxW - fr.getStringWidth(msg)) / 2), (int) (startY + totalH / 2 - 5), applyFade(0xFFFF5555, fade), false);
            String nav = "[Esc] Return to Menu";
            fr.drawString(nav, (int) (startX + (maxW - fr.getStringWidth(nav)) / 2), (int) (startY + totalH - 12), applyFade(0xFF888888, fade), false);
            return;
        }

        // Live client-side check if carrier entity is loaded in local world to show realtime battery
        if (mc.world != null && cam.carrierEntityId != -1) {
            net.minecraft.entity.Entity carrierEnt = mc.world.getEntityByID(cam.carrierEntityId);
            if (carrierEnt instanceof net.minecraft.entity.player.EntityPlayer) {
                ItemStack stack = com.voltyx.mwccf.geo.BodycamLayer.getEquippedBodycam((net.minecraft.entity.player.EntityPlayer) carrierEnt);
                if (!stack.isEmpty() && stack.getItem() instanceof com.voltyx.mwccf.geo.ItemBodycam) {
                    net.minecraft.nbt.NBTTagCompound tag = stack.getTagCompound();
                    int charge = (tag != null && tag.hasKey("battery_charge")) ? tag.getInteger("battery_charge") : 0;
                    int percent = Math.min(100, Math.max(0, (int) ((charge / 48000.0f) * 100)));
                    cam.batteryPercent = percent;
                    cam.isOnline = com.voltyx.mwccf.geo.ItemBodycam.isPowerEnabled(stack) && charge > 0;
                }
            }
        }

        // ── Top Bar ──
        String dateStr = DATE_FORMAT.format(new Date());
        String topLeft = dateStr + "  " + cam.camId;
        fr.drawString(topLeft, (int) startX, (int) startY, applyFade(0xFF55FFBB, fade), false);

        boolean blink = (System.currentTimeMillis() / 600) % 2 == 0;

        String bat = cam.batteryPercent + "%";
        int batCol = cam.batteryPercent > 20 ? 0xFF00FF66 : 0xFFFF4444;

        int topRightX = (int) (startX + maxW);
        int recTextW = fr.getStringWidth("REC");
        int dotW = fr.getStringWidth("● ");
        int batW = fr.getStringWidth(bat);

        // Fixed anchors so blinking dot NEVER shifts the battery or REC text
        int recTextX = topRightX - recTextW;
        int dotX = recTextX - dotW;
        int batX = dotX - batW - 8;

        // Draw "REC" text at fixed position
        fr.drawString("REC", recTextX, (int) startY, applyFade(0xFFFF3333, fade), false);

        // Draw blinking red circle at fixed position
        int dotCol = blink ? 0xFFFF2222 : 0xFF551111;
        fr.drawString("●", dotX, (int) startY, applyFade(dotCol, fade), false);

        // Draw battery percentage at fixed anchor
        fr.drawString(bat, batX, (int) startY, applyFade(batCol, fade), false);

        // ── Tactical Entity Detection Rectangles (Pure boxes, no text) ──
        if (cam.isOnline) {
            renderTacticalBoxes(fade, halfW, halfH, textScale);
        }

        // ── Center Status Message ──
        if (cam.isOnline) {
            float connTimer = session.getConnectTimer();
            if (connTimer <= 1.4f) {
                if (((int) (connTimer * 6)) % 2 == 0) {
                    String connMsg = "[ CAM CONNECTED ]";
                    int cw = fr.getStringWidth(connMsg);
                    fr.drawString(connMsg, (int) (startX + (maxW - cw) / 2), (int) (startY + totalH / 2 - 5), applyFade(0xFF00FF66, fade), false);
                }
            }
        } else {
            String disMsg = "[ CAM DISCONNECTED - NO SIGNAL ]";
            int dw = fr.getStringWidth(disMsg);
            int blinkDisCol = blink ? 0xFFFF3333 : 0xFFAA2222;
            fr.drawString(disMsg, (int) (startX + (maxW - dw) / 2), (int) (startY + totalH / 2 - 8), applyFade(blinkDisCol, fade), false);

            String subMsg = "Carrier device powered off or out of range";
            int sw = fr.getStringWidth(subMsg);
            fr.drawString(subMsg, (int) (startX + (maxW - sw) / 2), (int) (startY + totalH / 2 + 4), applyFade(0xFF888888, fade), false);
        }

        // ── Bottom Bar ──
        int bottomY = (int) (startY + totalH - 9);
        String camIndexStr = String.format("CAM %d/%d", session.getBodycamIndex() + 1, session.getAvailableCameras().size());
        if (cam.carrierName != null && !cam.carrierName.isEmpty()) {
            camIndexStr += " [" + cam.carrierName + "]";
        }
        fr.drawString(camIndexStr, (int) startX, bottomY, applyFade(0xFF33CC88, fade), false);

        boolean isInteracting = TerminalCameraController.isActive();
        String navTips = isInteracting ? "[◄/►] Cam  [Esc] List  [E] Exit" : "● LIVE FEED";
        int navW = fr.getStringWidth(navTips);
        int navCol = isInteracting ? 0xFF88AA99 : 0xFF00FF66;
        fr.drawString(navTips, (int) (startX + maxW - navW), bottomY, applyFade(navCol, fade), false);
    }

    private void renderTacticalBoxes(float fade, float halfW, float halfH, float textScale) {
        List<BodycamFeedRenderer.TacticalRect> rects = BodycamFeedRenderer.getDetectedRects();
        if (rects == null || rects.isEmpty()) return;

        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(1.5F);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        // Exact monitor quad bounds in textScale coordinates
        // Under scale(textScale, -textScale, textScale):
        // Top edge (+halfH in model space) is at -halfH / textScale
        // Bottom edge (-halfH in model space) is at +halfH / textScale
        float fullStartX = -halfW / textScale;
        float fullStartY = -halfH / textScale;
        float fullW = (halfW * 2.0F) / textScale;
        float fullH = (halfH * 2.0F) / textScale;

        for (BodycamFeedRenderer.TacticalRect rect : rects) {
            float rLeft = fullStartX + rect.left * fullW;
            float rRight = fullStartX + rect.right * fullW;
            float rTop = fullStartY + rect.top * fullH;
            float rBottom = fullStartY + rect.bottom * fullH;

            buf.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
            buf.pos(rLeft, rTop, 0.0F).color(0.0F, 1.0F, 0.3F, fade).endVertex();
            buf.pos(rRight, rTop, 0.0F).color(0.0F, 1.0F, 0.3F, fade).endVertex();
            buf.pos(rRight, rBottom, 0.0F).color(0.0F, 1.0F, 0.3F, fade).endVertex();
            buf.pos(rLeft, rBottom, 0.0F).color(0.0F, 1.0F, 0.3F, fade).endVertex();
            tess.draw();
        }

        GlStateManager.glLineWidth(1.0F);
        GlStateManager.enableTexture2D();
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
