package com.voltyx.mwccf.antenna.client;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.antenna.TileEntityAntenna;
import com.voltyx.mwccf.antenna.network.PacketCloseAntenna;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
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
public class AntennaCameraController {

    private static boolean active = false;
    private static BlockPos antennaPos = null;
    private static EnumFacing antennaFacing = null;
    private static TileEntityAntenna currentAntenna = null;

    private static float transitionProgress = 0.0f; // 0.0 -> 1.0
    private static long lastFrameTime = System.nanoTime();

    private static float targetPlayerYaw = 0.0f;
    private static float targetCameraYaw = 0.0f;
    private static float targetPitch = 0.0f; // Strictly 0.0f (horizontal, no tilt)

    private static double keypadTargetX = 0.0;
    private static double keypadTargetY = 0.0;
    private static double keypadTargetZ = 0.0;

    private static double blockTargetX = 0.0;
    private static double blockTargetY = 0.0;
    private static double blockTargetZ = 0.0;

    private static float successProgress = 0.0f;

    private static double normalX = 0.0;
    private static double normalZ = 0.0;

    private static final double INPUT_DIST = 0.55;
    private static final double SUCCESS_DIST = 1.05;
    private static double standDist = INPUT_DIST;

    private static double targetPlayerX = 0.0;
    private static double targetPlayerZ = 0.0;

    private static int prevThirdPerson = 0;

    public static boolean isActive() {
        return active;
    }

    public static BlockPos getAntennaPos() {
        return antennaPos;
    }

    public static TileEntityAntenna getCurrentAntenna() {
        return currentAntenna;
    }

    public static float getTransitionProgress() {
        return transitionProgress;
    }

    public static void open(BlockPos pos, EnumFacing facing, TileEntityAntenna antenna) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        antennaPos = pos;
        antennaFacing = facing;
        currentAntenna = antenna;
        active = true;
        transitionProgress = 0.0f;
        successProgress = 0.0f;

        double cx = pos.getX() + 0.5;
        double cy = pos.getY();
        double cz = pos.getZ() + 0.5;

        // Keypad center: midpoint between button 5 (Y=8.0) and button 2 (Y=9.2)
        // Model coords: lx = -5.0 / 16.0 = -0.3125, ly = 8.6 / 16.0 = 0.5375, lz = -8.6 / 16.0 = -0.5375
        keypadTargetY = cy + 0.5375;

        // Rotations matching TileEntityAntennaRenderer (NORTH=0, SOUTH=180, WEST=90, EAST=270):
        switch (facing) {
            case NORTH:
                keypadTargetX = cx + 0.3125;
                keypadTargetZ = cz - 0.5375;
                normalX = 0.0;
                normalZ = -1.0;
                targetPlayerYaw = 0.0F;
                targetCameraYaw = 180.0F;
                break;
            case SOUTH:
                keypadTargetX = cx - 0.3125;
                keypadTargetZ = cz + 0.5375;
                normalX = 0.0;
                normalZ = 1.0;
                targetPlayerYaw = 180.0F;
                targetCameraYaw = 0.0F;
                break;
            case WEST:
                keypadTargetX = cx - 0.5375;
                keypadTargetZ = cz - 0.3125;
                normalX = -1.0;
                normalZ = 0.0;
                targetPlayerYaw = 270.0F;
                targetCameraYaw = 90.0F;
                break;
            case EAST:
                keypadTargetX = cx + 0.5375;
                keypadTargetZ = cz + 0.3125;
                normalX = 1.0;
                normalZ = 0.0;
                targetPlayerYaw = 90.0F;
                targetCameraYaw = 270.0F;
                break;
            default:
                break;
        }

        blockTargetX = cx + normalX * 0.5;
        blockTargetY = cy + 0.5;
        blockTargetZ = cz + normalZ * 0.5;

        targetPitch = 0.0f;
        standDist = INPUT_DIST;

        targetPlayerX = keypadTargetX + normalX * standDist;
        targetPlayerZ = keypadTargetZ + normalZ * standDist;

        prevThirdPerson = mc.gameSettings.thirdPersonView;
        mc.gameSettings.thirdPersonView = 0; // Force first person

        lastFrameTime = System.nanoTime();
        AntennaSession.getInstance().reset();
    }

    public static void close() {
        if (!active) return;
        active = false;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiAntenna) {
            mc.displayGuiScreen(null);
        }
        if (mc.gameSettings != null) {
            mc.gameSettings.thirdPersonView = prevThirdPerson;
        }

        if (antennaPos != null) {
            MwccfMod.PACKET_HANDLER.sendToServer(new PacketCloseAntenna(antennaPos));
        }

        if (currentAntenna != null) {
            currentAntenna.close();
            currentAntenna = null;
        }
        antennaPos = null;
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
                    transitionProgress = Math.min(1.0f, transitionProgress + dt * 2.5f);
                }
                if ("ACCESS".equals(AntennaSession.getInstance().getStatusMessage())) {
                    if (successProgress < 1.0f) {
                        successProgress = Math.min(1.0f, successProgress + dt * 1.5f);
                    }
                }
            } else {
                if (transitionProgress > 0.0f) {
                    transitionProgress = Math.max(0.0f, transitionProgress - dt * 3.0f);
                    if (transitionProgress <= 0.0f) {
                        successProgress = 0.0f;
                    }
                }
            }

            AntennaSession.getInstance().update(dt, active);

            if (active && transitionProgress >= 1.0f) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.currentScreen == null) {
                    mc.displayGuiScreen(new GuiAntenna());
                }
            }
        }
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

        if (mc.currentScreen instanceof net.minecraft.client.gui.GuiIngameMenu) {
            mc.displayGuiScreen(null);
            close();
            return;
        }

        if (antennaPos != null && player.getDistanceSq(antennaPos.getX() + 0.5, antennaPos.getY() + 0.5, antennaPos.getZ() + 0.5) > 9.0) {
            close();
            return;
        }

        double destX = targetPlayerX;
        double destZ = targetPlayerZ;
        if (successProgress > 0.0f) {
            float s = easeOutCubic(successProgress);
            double blockPlayerX = blockTargetX + normalX * SUCCESS_DIST;
            double blockPlayerZ = blockTargetZ + normalZ * SUCCESS_DIST;
            destX = targetPlayerX + (blockPlayerX - targetPlayerX) * s;
            destZ = targetPlayerZ + (blockPlayerZ - targetPlayerZ) * s;
        }

        if (transitionProgress < 0.99f || successProgress > 0.0f) {
            player.motionX = (destX - player.posX) * 0.25;
            player.motionZ = (destZ - player.posZ) * 0.25;
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

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            close();
        }
    }

    @SubscribeEvent
    public void onMouseInput(MouseEvent event) {
        if (!active || transitionProgress < 0.5f) return;

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

        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        if (entity != null) {
            float partialTicks = (float) event.getRenderPartialTicks();
            double camX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double camY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + entity.getEyeHeight();
            double camZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;

            float s = easeOutCubic(successProgress);
            double curTargetX = keypadTargetX + (blockTargetX - keypadTargetX) * s;
            double curTargetY = keypadTargetY + (blockTargetY - keypadTargetY) * s;
            double curTargetZ = keypadTargetZ + (blockTargetZ - keypadTargetZ) * s;
            double curDist = INPUT_DIST + (SUCCESS_DIST - INPUT_DIST) * s;

            double desiredCamX = curTargetX + normalX * curDist;
            double desiredCamY = curTargetY;
            double desiredCamZ = curTargetZ + normalZ * curDist;

            double deltaX = desiredCamX - camX;
            double deltaY = desiredCamY - camY;
            double deltaZ = desiredCamZ - camZ;

            double rad = Math.toRadians(newYaw);
            double cos = Math.cos(rad);
            double sin = Math.sin(rad);

            float eyeX = (float) -(deltaX * cos + deltaZ * sin);
            float eyeY = (float) -deltaY;
            float eyeZ = (float) -(-deltaX * sin + deltaZ * cos);

            GlStateManager.translate(eyeX * t, eyeY * t, eyeZ * t);
        }

        if (active && transitionProgress >= 1.0f) {
            event.setYaw(targetCameraYaw);
            event.setPitch(targetPitch);
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
            if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                return;
            }
            switch (event.getType()) {
                case CROSSHAIRS:
                case HOTBAR:
                case HEALTH:
                case ARMOR:
                case FOOD:
                case HEALTHMOUNT:
                case AIR:
                case EXPERIENCE:
                case CHAT:
                case PLAYER_LIST:
                    event.setCanceled(true);
                    break;
                default:
                    break;
            }
        }
    }

    private static float easeOutCubic(float t) {
        float c = Math.max(0.0f, Math.min(1.0f, t));
        return 1.0f - (float) Math.pow(1.0f - c, 3);
    }
}
