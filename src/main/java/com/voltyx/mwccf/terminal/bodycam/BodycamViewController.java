package com.voltyx.mwccf.terminal.bodycam;

import com.voltyx.mwccf.terminal.client.GuiTerminal;
import com.voltyx.mwccf.terminal.client.TerminalCameraController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SideOnly(Side.CLIENT)
public class BodycamViewController {

    private static final BodycamViewController INSTANCE = new BodycamViewController();

    public static BodycamViewController getInstance() {
        return INSTANCE;
    }

    private static boolean active = false;
    private static BlockPos terminalPos = null;
    private static final List<BodycamEntry> cameras = new ArrayList<>();
    private static int currentIndex = 0;
    private static float connectTimer = 0.0f;
    private static long lastFrameNano = System.nanoTime();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Matrix buffers for 3D to 2D projection
    private static final FloatBuffer MODELVIEW = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer PROJECTION = BufferUtils.createFloatBuffer(16);
    private static final IntBuffer VIEWPORT = BufferUtils.createIntBuffer(16);
    private static final FloatBuffer WIN_POS = BufferUtils.createFloatBuffer(3);
    private static boolean matricesCaptured = false;

    public static boolean isActive() {
        return active;
    }

    public static BodycamEntry getCurrentCamera() {
        if (!active || cameras.isEmpty() || currentIndex < 0 || currentIndex >= cameras.size()) {
            return null;
        }
        return cameras.get(currentIndex);
    }

    public static void startView(BlockPos pos, List<BodycamEntry> availableCameras, int startIndex) {
        if (availableCameras == null || availableCameras.isEmpty()) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        terminalPos = pos;
        cameras.clear();
        cameras.addAll(availableCameras);
        currentIndex = Math.max(0, Math.min(startIndex, cameras.size() - 1));
        active = true;
        connectTimer = 0.0f;
        lastFrameNano = System.nanoTime();

        // Close terminal GUI screen so game world renders
        if (mc.currentScreen instanceof GuiTerminal) {
            mc.displayGuiScreen(null);
        }

        applyCameraViewEntity();
    }

    public static void stopView() {
        if (!active) return;
        active = false;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            mc.setRenderViewEntity(mc.player);
        }
        // Return to terminal screen
        if (TerminalCameraController.isActive()) {
            mc.displayGuiScreen(new GuiTerminal());
        }
    }

    public static void nextCamera() {
        if (cameras.isEmpty()) return;
        currentIndex = (currentIndex + 1) % cameras.size();
        connectTimer = 0.0f;
        playSwitchSound();
        applyCameraViewEntity();
    }

    public static void previousCamera() {
        if (cameras.isEmpty()) return;
        currentIndex = (currentIndex - 1 + cameras.size()) % cameras.size();
        connectTimer = 0.0f;
        playSwitchSound();
        applyCameraViewEntity();
    }

    private static void playSwitchSound() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            mc.player.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, 0.8F, 1.2F);
        }
    }

    private static void applyCameraViewEntity() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) return;

        BodycamEntry cam = getCurrentCamera();
        if (cam != null && cam.isOnline) {
            Entity carrier = mc.world.getEntityByID(cam.carrierEntityId);
            if (carrier == null) {
                carrier = mc.world.getPlayerEntityByName(cam.carrierName);
            }
            if (carrier != null) {
                mc.setRenderViewEntity(carrier);
                return;
            }
        }
        // Fallback if offline or carrier not found in client world
        mc.setRenderViewEntity(mc.player);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!active) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) {
            stopView();
            return;
        }

        // Auto-close if terminal position invalid or too far
        if (terminalPos != null && mc.player.getDistanceSq(terminalPos.getX() + 0.5, terminalPos.getY() + 0.5, terminalPos.getZ() + 0.5) > 16.0) {
            stopView();
            return;
        }

        long now = System.nanoTime();
        float dt = (now - lastFrameNano) / 1_000_000_000.0f;
        lastFrameNano = now;
        if (dt > 0.1f) dt = 0.1f;
        connectTimer += dt;

        applyCameraViewEntity();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!active) return;

        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            if (key == Keyboard.KEY_ESCAPE || key == Keyboard.KEY_Q) {
                stopView();
            } else if (key == Keyboard.KEY_LEFT || key == Keyboard.KEY_A) {
                previousCamera();
            } else if (key == Keyboard.KEY_RIGHT || key == Keyboard.KEY_D) {
                nextCamera();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInputUpdate(InputUpdateEvent event) {
        if (active) {
            MovementInput input = event.getMovementInput();
            input.moveForward = 0.0f;
            input.moveStrafe = 0.0f;
            input.forwardKeyDown = false;
            input.backKeyDown = false;
            input.leftKeyDown = false;
            input.rightKeyDown = false;
            input.jump = false;
            input.sneak = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseInput(MouseEvent event) {
        if (active) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderHand(RenderHandEvent event) {
        if (active) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!active) return;
        // Capture matrices in 3D world space
        MODELVIEW.clear();
        PROJECTION.clear();
        VIEWPORT.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, MODELVIEW);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, PROJECTION);
        GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT);
        matricesCaptured = true;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (!active || event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = event.getResolution();
        int w = sr.getScaledWidth();
        int h = sr.getScaledHeight();
        FontRenderer fr = mc.fontRenderer;
        BodycamEntry cam = getCurrentCamera();

        // 1. If camera is offline, draw black screen with static noise
        if (cam == null || !cam.isOnline) {
            Gui.drawRect(0, 0, w, h, 0xFF0A0D0E);
            renderStaticNoise(w, h);
            String discText = "[ CAM DISCONNECTED - NO SIGNAL ]";
            int tw = fr.getStringWidth(discText);
            fr.drawString(discText, (w - tw) / 2, h / 2 - 4, 0xFFFF4444, true);
        } else {
            // Camera online: render Tactical Entity Detection Boxes
            if (matricesCaptured) {
                renderEntityDetectionBoxes(mc, sr, fr);
            }

            // CRT scanlines overlay
            renderScanlines(w, h);

            // First second: [ CAM CONNECTED ]
            if (connectTimer <= 1.0f) {
                String connText = "[ CAM CONNECTED ]";
                int tw = fr.getStringWidth(connText);
                int cx = (w - tw) / 2;
                int cy = h / 2 - 12;
                Gui.drawRect(cx - 8, cy - 4, cx + tw + 8, cy + 14, 0xAA003311);
                fr.drawString(connText, cx, cy, 0xFF00FF66, true);
            }
        }

        // 2. Top Header Bar
        Gui.drawRect(0, 0, w, 20, 0xCC11161B);
        Gui.drawRect(0, 20, w, 21, 0xFF00FF66);

        String dateStr = DATE_FORMAT.format(new Date()) + " UTC";
        String camIdStr = cam != null ? "ID: " + cam.camId : "ID: CAM-0000";
        String carrierStr = cam != null ? "USER: " + cam.carrierName : "USER: UNKNOWN";
        String batStr = cam != null ? "BAT: " + cam.batteryPercent + "%" : "BAT: 0%";
        boolean recBlink = (mc.player != null && (mc.player.ticksExisted / 15) % 2 == 0);
        String recStr = recBlink ? "● REC" : "  REC";

        int curX = 6;
        fr.drawString(dateStr, curX, 6, 0xFFAABBCC, true);
        curX += fr.getStringWidth(dateStr) + 12;

        fr.drawString(camIdStr, curX, 6, 0xFF00FF66, true);
        curX += fr.getStringWidth(camIdStr) + 12;

        fr.drawString(carrierStr, curX, 6, 0xFF55FFDD, true);

        // Right side of top bar
        int rightX = w - 6;
        fr.drawString(recStr, rightX - fr.getStringWidth("● REC"), 6, 0xFFFF3333, true);
        rightX -= fr.getStringWidth("● REC") + 16;
        fr.drawString(batStr, rightX - fr.getStringWidth(batStr), 6, 0xFFDDDD88, true);

        // 3. Bottom Navigation Bar
        Gui.drawRect(0, h - 18, w, h, 0xCC11161B);
        Gui.drawRect(0, h - 19, w, h - 18, 0xFF005533);
        String navText = "◄ [LEFT] Previous Camera   |   [RIGHT] Next Camera ►   |   [ESC] Return to Terminal";
        int nw = fr.getStringWidth(navText);
        fr.drawString(navText, (w - nw) / 2, h - 13, 0xFF88DDAA, true);
    }

    private static void renderEntityDetectionBoxes(Minecraft mc, ScaledResolution sr, FontRenderer fr) {
        if (mc.world == null) return;
        Entity viewEntity = mc.getRenderViewEntity();
        if (viewEntity == null) viewEntity = mc.player;
        if (viewEntity == null) return;

        float pTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        double camX = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * pTicks;
        double camY = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * pTicks + viewEntity.getEyeHeight();
        double camZ = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * pTicks;

        for (Entity ent : mc.world.loadedEntityList) {
            if (!(ent instanceof EntityLivingBase) || !ent.isEntityAlive() || ent == viewEntity) {
                continue;
            }

            double dist = viewEntity.getDistance(ent);
            if (dist > 36.0 || dist < 0.5) continue;

            double entX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks;
            double entY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks;
            double entZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks;

            // Project bottom (feet) and top (head)
            float relX = (float) (entX - camX);
            float relY = (float) (entY - camY);
            float relZ = (float) (entZ - camZ);

            WIN_POS.clear();
            GLU.gluProject(relX, relY, relZ, MODELVIEW, PROJECTION, VIEWPORT, WIN_POS);
            float botX = WIN_POS.get(0);
            float botY = WIN_POS.get(1);
            float botZ = WIN_POS.get(2);

            WIN_POS.clear();
            GLU.gluProject(relX, relY + ent.height, relZ, MODELVIEW, PROJECTION, VIEWPORT, WIN_POS);
            float topX = WIN_POS.get(0);
            float topY = WIN_POS.get(1);
            float topZ = WIN_POS.get(2);

            // If behind camera plane
            if (botZ < 0.0f || botZ > 1.0f || topZ < 0.0f || topZ > 1.0f) {
                continue;
            }

            float scale = sr.getScaleFactor();
            float vpH = VIEWPORT.get(3);

            float sBotX = botX / scale;
            float sBotY = (vpH - botY) / scale;
            float sTopX = topX / scale;
            float sTopY = (vpH - topY) / scale;

            float boxH = Math.abs(sBotY - sTopY);
            if (boxH < 4.0f) continue;
            float boxW = Math.max(8.0f, boxH * (ent.width / Math.max(0.1f, ent.height)) * 0.9f);

            float cx = (sBotX + sTopX) * 0.5f;
            float minY = Math.min(sTopY, sBotY);
            float maxY = Math.max(sTopY, sBotY);
            float minX = cx - boxW * 0.5f;
            float maxX = cx + boxW * 0.5f;

            // Tactical Green Corners / Box
            drawTacticalBox((int) minX, (int) minY, (int) maxX, (int) maxY, 0xFF00FF66);

            // Target Label
            String name = ent.getName();
            String label = "[" + name + " " + (int) dist + "m]";
            int lw = fr.getStringWidth(label);
            Gui.drawRect((int) (cx - lw / 2 - 2), (int) (minY - 10), (int) (cx + lw / 2 + 2), (int) minY, 0x99002211);
            fr.drawString(label, (int) (cx - lw / 2), (int) (minY - 9), 0xFF00FF66, false);
        }
    }

    private static void drawTacticalBox(int minX, int minY, int maxX, int maxY, int color) {
        int w = maxX - minX;
        int h = maxY - minY;
        int cornerLen = Math.max(3, Math.min(8, Math.min(w, h) / 3));

        // Top-left corner
        Gui.drawRect(minX, minY, minX + cornerLen, minY + 1, color);
        Gui.drawRect(minX, minY, minX + 1, minY + cornerLen, color);

        // Top-right corner
        Gui.drawRect(maxX - cornerLen, minY, maxX, minY + 1, color);
        Gui.drawRect(maxX - 1, minY, maxX, minY + cornerLen, color);

        // Bottom-left corner
        Gui.drawRect(minX, maxY - 1, minX + cornerLen, maxY, color);
        Gui.drawRect(minX, maxY - cornerLen, minX + 1, maxY, color);

        // Bottom-right corner
        Gui.drawRect(maxX - cornerLen, maxY - 1, maxX, maxY, color);
        Gui.drawRect(maxX - 1, maxY - cornerLen, maxX, maxY, color);
    }

    private static void renderScanlines(int w, int h) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.12f);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        for (int y = 0; y < h; y += 3) {
            buf.pos(0, y, 0).endVertex();
            buf.pos(w, y, 0).endVertex();
        }
        tess.draw();

        GlStateManager.enableTexture2D();
    }

    private static void renderStaticNoise(int w, int h) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < 60; i++) {
            int rx = rnd.nextInt(w);
            int ry = rnd.nextInt(h);
            int rw = 4 + rnd.nextInt(20);
            int rh = 1 + rnd.nextInt(3);
            float alpha = 0.08f + rnd.nextFloat() * 0.15f;
            buf.pos(rx, ry + rh, 0).color(1.0f, 1.0f, 1.0f, alpha).endVertex();
            buf.pos(rx + rw, ry + rh, 0).color(1.0f, 1.0f, 1.0f, alpha).endVertex();
            buf.pos(rx + rw, ry, 0).color(1.0f, 1.0f, 1.0f, alpha).endVertex();
            buf.pos(rx, ry, 0).color(1.0f, 1.0f, 1.0f, alpha).endVertex();
        }
        tess.draw();

        GlStateManager.enableTexture2D();
    }
}
