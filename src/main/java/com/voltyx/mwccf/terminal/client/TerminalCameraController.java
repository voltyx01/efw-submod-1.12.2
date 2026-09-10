package com.voltyx.mwccf.terminal.client;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.terminal.TileEntityTerminal;
import com.voltyx.mwccf.terminal.network.PacketCloseTerminal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class TerminalCameraController {

    private static boolean active = false;
    private static BlockPos terminalPos = null;
    private static EnumFacing terminalFacing = null;
    private static TileEntityTerminal currentTerminal = null;

    private static float transitionProgress = 0.0f; // 0.0 -> 1.0
    private static long lastFrameTime = System.nanoTime();

    private static float targetPlayerYaw = 0.0f;
    private static float targetCameraYaw = 0.0f;
    private static float targetPitch = 3.0f;

    private static double targetPlayerX = 0.0;
    private static double targetPlayerZ = 0.0;

    private static int prevThirdPerson = 0;

    public static boolean isActive() {
        return active;
    }

    public static BlockPos getTerminalPos() {
        return terminalPos;
    }

    public static float getTransitionProgress() {
        return transitionProgress;
    }

    public static void open(BlockPos pos, EnumFacing facing, TileEntityTerminal terminal) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        terminalPos = pos;
        terminalFacing = facing;
        currentTerminal = terminal;
        active = true;
        transitionProgress = 0.0f;

        // In front of the terminal monitor
        float standDist = 0.72f;
        targetPlayerX = pos.getX() + 0.5 + facing.getXOffset() * standDist;
        targetPlayerZ = pos.getZ() + 0.5 + facing.getZOffset() * standDist;

        // Facing points into the room (away from the wall).
        // The player looking AT the terminal must face towards the wall (facing.getOpposite()):
        targetPlayerYaw = facing.getOpposite().getHorizontalAngle();
        // In Minecraft Forge CameraSetup, camera yaw in OpenGL is (playerYaw + 180.0F).
        // Therefore targetCameraYaw must be targetPlayerYaw + 180.0F to look directly at the terminal:
        targetCameraYaw = MathHelper.wrapDegrees(targetPlayerYaw + 180.0F);
        targetPitch = 3.0f; // Eye level towards monitor and keyboard

        prevThirdPerson = mc.gameSettings.thirdPersonView;
        mc.gameSettings.thirdPersonView = 0; // Force first person

        lastFrameTime = System.nanoTime();
        TerminalSession.getInstance().reset();
    }

    public static void close() {
        if (!active) return;
        active = false;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiTerminal) {
            mc.displayGuiScreen(null);
        }
        if (mc.gameSettings != null) {
            mc.gameSettings.thirdPersonView = prevThirdPerson;
        }

        if (terminalPos != null) {
            MwccfMod.PACKET_HANDLER.sendToServer(new PacketCloseTerminal(terminalPos));
        }

        if (currentTerminal != null) {
            currentTerminal.close();
            currentTerminal = null;
        }
        terminalPos = null;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            long now = System.nanoTime();
            float dt = (now - lastFrameTime) / 1_000_000_000.0f;
            lastFrameTime = now;
            dt = Math.max(0.001f, Math.min(0.1f, dt));

            if (active) {
                if (transitionProgress < 1.0f) {
                    transitionProgress = Math.min(1.0f, transitionProgress + dt * 2.5f); // ~0.4s ease in
                }
            } else {
                if (transitionProgress > 0.0f) {
                    transitionProgress = Math.max(0.0f, transitionProgress - dt * 3.0f);
                }
            }

            TerminalSession.getInstance().update(dt, active);

            if (active && transitionProgress >= 1.0f) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.currentScreen == null) {
                    mc.displayGuiScreen(new GuiTerminal());
                }
            }
        } else if (event.phase == TickEvent.Phase.END) {
            // Render terminal exit prompt in Phase.END so it is never cancelled by HUD events
            if (active && transitionProgress >= 0.8f && Minecraft.getMinecraft().currentScreen == null) {
                renderTerminalPrompt();
            }
        }
    }

    public static void renderTerminalPrompt() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.fontRenderer == null || mc.player == null) return;

        // Set up 2D orthographic projection matching the current window resolution
        mc.entityRenderer.setupOverlayRendering();

        ScaledResolution res = new ScaledResolution(mc);
        String hint = "[ESC / ПКМ] Отойти от терминала";
        int w = mc.fontRenderer.getStringWidth(hint);
        int x = (res.getScaledWidth() - w) / 2;
        int y = res.getScaledHeight() - 25;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.disableAlpha();
        net.minecraft.client.gui.Gui.drawRect(x - 8, y - 4, x + w + 8, y + mc.fontRenderer.FONT_HEIGHT + 4, 0xCC07150A);
        mc.fontRenderer.drawStringWithShadow(hint, x, y, 0xFF44FFAA);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !active) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player == null || player.isDead) {
            close();
            return;
        }

        // If pause menu opens from ESC while active in terminal, close terminal and dismiss menu
        if (mc.currentScreen instanceof net.minecraft.client.gui.GuiIngameMenu) {
            mc.displayGuiScreen(null);
            close();
            return;
        }

        // Check if player moved too far away
        if (terminalPos != null && player.getDistanceSq(terminalPos.getX() + 0.5, terminalPos.getY() + 0.5, terminalPos.getZ() + 0.5) > 9.0) {
            close();
            return;
        }

        // Smoothly step player towards target position in front of terminal
        if (transitionProgress < 0.99f) {
            player.motionX = (targetPlayerX - player.posX) * 0.25;
            player.motionZ = (targetPlayerZ - player.posZ) * 0.25;
        } else {
            player.motionX = 0;
            player.motionZ = 0;
            if (player.motionY > 0) {
                player.motionY = 0;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInputUpdate(InputUpdateEvent event) {
        if (active && transitionProgress >= 0.5f) {
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

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!active) return;

        // Pressing ESC exits terminal mode
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            close();
        }
    }

    @SubscribeEvent
    public void onMouseInput(MouseEvent event) {
        if (!active || transitionProgress < 0.5f) return;

        // Button 1 is right mouse button (ПКМ)
        if (event.getButton() == 1 && event.isButtonstate()) {
            close();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderHand(RenderHandEvent event) {
        if (transitionProgress > 0.001f) {
            if (active && transitionProgress >= 1.0f) {
                event.setCanceled(true);
            } else {
                float t = easeOutCubic(transitionProgress);
                GlStateManager.translate(0.0F, -t * 1.5F, 0.0F);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderSpecificHand(RenderSpecificHandEvent event) {
        if (transitionProgress > 0.001f) {
            if (active && transitionProgress >= 1.0f) {
                event.setCanceled(true);
            } else {
                float t = easeOutCubic(transitionProgress);
                GlStateManager.translate(0.0F, -t * 1.5F, 0.0F);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (transitionProgress <= 0.001f) return;

        float t = easeOutCubic(transitionProgress);

        float curYaw = event.getYaw();
        float yawDiff = MathHelper.wrapDegrees(targetCameraYaw - curYaw);
        float newYaw = curYaw + yawDiff * t;

        float curPitch = event.getPitch();
        float newPitch = curPitch + (targetPitch - curPitch) * t;

        event.setYaw(newYaw);
        event.setPitch(newPitch);

        // Keep camera completely static once fully transitioned into terminal
        if (active && transitionProgress >= 1.0f) {
            event.setYaw(targetCameraYaw);
            event.setPitch(targetPitch);
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                mc.player.rotationYaw = targetPlayerYaw;
                mc.player.prevRotationYaw = targetPlayerYaw;
                mc.player.rotationPitch = targetPitch;
                mc.player.prevRotationPitch = targetPitch;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
        if (active && transitionProgress > 0.3f) {
            // Hide all vanilla HUD while viewing terminal
            event.setCanceled(true);
        }
    }

    private static float easeOutCubic(float t) {
        float c = Math.max(0.0f, Math.min(1.0f, t));
        return 1.0f - (float) Math.pow(1.0f - c, 3);
    }
}
