package com.voltyx.mwccf.terminal.bodycam;

import com.voltyx.mwccf.terminal.client.TerminalSession;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class BodycamFeedRenderer {

    private static final BodycamFeedRenderer INSTANCE = new BodycamFeedRenderer();

    public static BodycamFeedRenderer getInstance() {
        return INSTANCE;
    }

    private static Framebuffer feedFbo = null;
    private static boolean isRendering = false;
    private static final int FBO_W = 320;
    private static final int FBO_H = 200;

    private static BodycamCameraEntity dummyCamera = null;

    private static java.lang.reflect.Method setupCameraTransformMethod = null;
    private static java.lang.reflect.Field lightmapUpdateNeededField = null;
    static {
        try {
            setupCameraTransformMethod = net.minecraft.client.renderer.EntityRenderer.class.getDeclaredMethod("func_78479_a", float.class, int.class);
            setupCameraTransformMethod.setAccessible(true);
        } catch (Throwable e1) {
            try {
                setupCameraTransformMethod = net.minecraft.client.renderer.EntityRenderer.class.getDeclaredMethod("setupCameraTransform", float.class, int.class);
                setupCameraTransformMethod.setAccessible(true);
            } catch (Throwable ignored) {}
        }
        try {
            lightmapUpdateNeededField = net.minecraft.client.renderer.EntityRenderer.class.getDeclaredField("field_78536_aa");
            lightmapUpdateNeededField.setAccessible(true);
        } catch (Throwable e1) {
            try {
                lightmapUpdateNeededField = net.minecraft.client.renderer.EntityRenderer.class.getDeclaredField("lightmapUpdateNeeded");
                lightmapUpdateNeededField.setAccessible(true);
            } catch (Throwable ignored) {}
        }
    }

    private static Entity currentCarrier = null;
    public static boolean carrierRenderedInPass = false;
    public static float lastCarrierTorsoRotateY = 0.0F;

    public static Entity getCurrentCarrier() {
        return currentCarrier;
    }

    private static float interpolateRotation(float prevYaw, float currentYaw, float partialTicks) {
        float diff = currentYaw - prevYaw;
        while (diff < -180.0F) diff += 360.0F;
        while (diff >= 180.0F) diff -= 360.0F;
        return prevYaw + partialTicks * diff;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFOVModifier(net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier event) {
        if (isRendering) {
            event.setFOV(85.0F);
        }
    }

    /**
     * Forces the correct sky/fog colour during the bodycam FBO render.
     *
     * Third-party mods (BoP, Dynamic Surroundings, etc.) subscribe to FogColors and
     * often check instanceof EntityPlayerSP before applying their colour. Because our
     * BodycamCameraEntity is only an EntityLivingBase those handlers skip the
     * colour assignment, leaving fogColor at 0,0,0 → black horizon.
     *
     * By subscribing with HIGHEST priority we run first and seed the event with the
     * correct sky colour; other handlers can then adjust it, but at least start from
     * a non-black baseline.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFogColors(EntityViewRenderEvent.FogColors event) {
        if (!isRendering) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;
        Entity rve = mc.getRenderViewEntity();
        if (rve == null) return;

        // Compute the actual sky colour for the camera's biome/time-of-day.
        // This is the same value EntityRenderer.updateFogColor() would use for a
        // proper EntityPlayerSP, so it gives us the correct horizon colour.
        // Use mc.player (the actual EntityPlayerSP) for the sky-colour lookup.
        // World.getSkyColor may return Vec3d.ZERO for non-EntityPlayer entities
        // (BodycamCameraEntity extends only EntityLivingBase), which leaves the
        // fog black. mc.player is in the same biome as the carrier so its colour
        // is identical to what the bodycam should show.
        Entity lookupEntity = (currentCarrier != null) ? currentCarrier : rve;
        Vec3d skyColor = mc.world.getSkyColor(lookupEntity, (float) event.getRenderPartialTicks());
        event.setRed((float) skyColor.x);
        event.setGreen((float) skyColor.y);
        event.setBlue((float) skyColor.z);
    }

    public static class TacticalRect {
        public final float left;
        public final float right;
        public final float top;
        public final float bottom;

        public TacticalRect(float left, float right, float top, float bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }

    private static final List<TacticalRect> detectedRects = new ArrayList<>();

    public static List<TacticalRect> getDetectedRects() {
        return detectedRects;
    }

    public static int getTextureId() {
        return (feedFbo != null) ? feedFbo.framebufferTexture : -1;
    }

    public static boolean isRendering() {
        return isRendering;
    }

    public static boolean hasValidTexture() {
        return feedFbo != null && feedFbo.framebufferTexture > 0;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.RenderTickEvent.Phase.START) return;
        if (isRendering) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) return;

        TerminalSession session = TerminalSession.getInstance();
        if (session.getStage() != TerminalSession.Stage.BODYCAM_VIEW) {
            detectedRects.clear();
            return;
        }

        BodycamEntry cam = session.getCurrentViewingCamera();
        if (cam == null || !cam.isOnline) {
            detectedRects.clear();
            return;
        }

        // Skip FBO update if the player is far away from the terminal to save performance
        if (session.getTerminalPos() != null) {
            double distSq = mc.player.getDistanceSq(session.getTerminalPos().getX() + 0.5,
                                                    session.getTerminalPos().getY() + 0.5,
                                                    session.getTerminalPos().getZ() + 0.5);
            if (distSq > 48.0 * 48.0) return;
        }

        Entity carrier = mc.world.getEntityByID(cam.carrierEntityId);
        if (carrier == null) {
            carrier = mc.world.getPlayerEntityByName(cam.carrierName);
        }
        if (carrier == null) {
            detectedRects.clear();
            return;
        }

        if (feedFbo == null) {
            feedFbo = new Framebuffer(FBO_W, FBO_H, true);
            feedFbo.setFramebufferColor(0.02F, 0.04F, 0.03F, 1.0F);
            feedFbo.setFramebufferFilter(GL11.GL_LINEAR);
        }

        float pTicks = event.renderTickTime;
        double posX = carrier.prevPosX + (carrier.posX - carrier.prevPosX) * pTicks;
        double posY = carrier.prevPosY + (carrier.posY - carrier.prevPosY) * pTicks;
        double posZ = carrier.prevPosZ + (carrier.posZ - carrier.prevPosZ) * pTicks;

        float bodyYaw;
        float bodyPitch;

        if (carrier instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) carrier;
            bodyYaw = living.prevRenderYawOffset + (living.renderYawOffset - living.prevRenderYawOffset) * pTicks;
            bodyPitch = living.prevRotationPitch + (living.rotationPitch - living.prevRotationPitch) * pTicks;
        } else {
            bodyYaw = carrier.prevRotationYaw + (carrier.rotationYaw - carrier.prevRotationYaw) * pTicks;
            bodyPitch = carrier.prevRotationPitch + (carrier.rotationPitch - carrier.prevRotationPitch) * pTicks;
        }

        boolean isCrawling = false;
        if (carrier instanceof net.minecraft.entity.player.EntityPlayer) {
            net.minecraft.entity.player.EntityPlayer p = (net.minecraft.entity.player.EntityPlayer) carrier;
            if (efw.AnimationTickHandler.isPlayerCrawling(p)) {
                isCrawling = true;
            } else if (p instanceof com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable) {
                com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable res = (com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable) p;
                if (res.isVisuallySwimming() || res.isForcingCrawling() || res.getPose() == com.fuzs.aquaacrobatics.entity.Pose.SWIMMING) {
                    isCrawling = true;
                }
            }
            if (p.getEyeHeight() < 0.8F || p.height < 1.0F) {
                isCrawling = true;
            }
        } else if (carrier instanceof EntityLivingBase) {
            if (((EntityLivingBase) carrier).getEyeHeight() < 0.8F) {
                isCrawling = true;
            }
        }

        boolean sneaking = carrier.isSneaking();
        double heightOffset;
        double forwardOffset;
        double leftOffset;
        float camPitch;

        if (isCrawling) {
            // Lying down / crawling (Aqua Acrobatics): camera lowers to near ground level
            heightOffset = 0.32;
            forwardOffset = 0.35;
            leftOffset = -0.06;
            camPitch = 5.0F;
        } else if (sneaking) {
            // Crouching / sneaking
            heightOffset = 1.25;
            forwardOffset = 0.22;
            leftOffset = -0.06;
            camPitch = 16.0F;
        } else {
            // Standing: collarbone mount (~1.53m) and tactical right-chest offset (-0.06m)
            heightOffset = 1.53;
            forwardOffset = 0.14;
            leftOffset = -0.06;
            camPitch = 10.0F;
        }

        float torsoYaw;
        if (carrier instanceof net.minecraft.entity.player.EntityPlayer) {
            net.minecraft.entity.player.EntityPlayer player = (net.minecraft.entity.player.EntityPlayer) carrier;
            boolean isLocal = (player == mc.player);

            float headYaw = isLocal ?
                    interpolateRotation(player.prevRotationYaw, player.rotationYaw, pTicks) :
                    interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, pTicks);
            float baseBodyYaw = interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, pTicks);

            float weaponHoldWeight = efw.AnimationTickHandler.weaponHoldWeightMap.getOrDefault(player, 0.0f);
            efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(player);
            boolean isLyingAnim = !player.isInWater() && ((ap != null && ap.getCurrentAnimationName() != null &&
                    ap.getCurrentAnimationName().contains("lie"))
                    || efw.AnimationTickHandler.isPlayerCrawling(player) || player.height < 1.0F);
            float effectiveHoldWeight = isLyingAnim ? 1.0f : weaponHoldWeight;

            float effectiveBodyYaw = baseBodyYaw;
            if (effectiveHoldWeight > 0.0f) {
                effectiveBodyYaw += net.minecraft.util.math.MathHelper.wrapDegrees(headYaw - baseBodyYaw) * effectiveHoldWeight;
            }

            // Bone-level torso rotation around Y (from keyframe animations / torso bone)
            float boneTorsoYaw = 0.0F;
            if (ap != null && (ap.isPlaying() || ap.getWeight() > 0f)) {
                efw.animation.layered.math.Vec3f torsoRot = ap.get3DTransform("torso", efw.animation.layered.TransformType.ROTATION, pTicks, new efw.animation.layered.math.Vec3f(0, lastCarrierTorsoRotateY, 0));
                boneTorsoYaw = (float) Math.toDegrees(torsoRot.getY());
            } else {
                boneTorsoYaw = (float) Math.toDegrees(lastCarrierTorsoRotateY);
            }

            String curAnim = ap != null ? ap.getCurrentAnimationName() : null;
            String curAction = ap != null ? ap.getCurrentActionName() : null;
            String fadeAction = ap != null ? ap.getFadeActionName() : null;
            boolean isRifleAnim = (curAnim != null && curAnim.contains("rifle"))
                    || (curAction != null && curAction.contains("rifle"))
                    || (fadeAction != null && fadeAction.contains("rifle"))
                    || (efw.animation.WeaponTypeHelper.getWeaponType(player.getHeldItemMainhand()) == efw.animation.WeaponTypeHelper.WeaponType.RIFLE);

            if (isRifleAnim) {
                boneTorsoYaw = 0.0F;
            }

            torsoYaw = effectiveBodyYaw - boneTorsoYaw;
        } else if (carrier instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) carrier;
            torsoYaw = interpolateRotation(living.prevRenderYawOffset, living.renderYawOffset, pTicks);
        } else {
            torsoYaw = interpolateRotation(carrier.prevRotationYaw, carrier.rotationYaw, pTicks);
        }

        float camYaw = torsoYaw - 3.0F;

        double rad = Math.toRadians(torsoYaw);
        double forwardX = -Math.sin(rad);
        double forwardZ = Math.cos(rad);
        double leftX = Math.cos(rad);
        double leftZ = Math.sin(rad);

        double curCamX = posX + forwardX * forwardOffset + leftX * leftOffset;
        double curCamY = posY + heightOffset;
        double curCamZ = posZ + forwardZ * forwardOffset + leftZ * leftOffset;

        if (dummyCamera == null || dummyCamera.world != mc.world) {
            dummyCamera = new BodycamCameraEntity(mc.world);
        }

        dummyCamera.setPosition(curCamX, curCamY, curCamZ);
        dummyCamera.prevPosX = curCamX;
        dummyCamera.prevPosY = curCamY;
        dummyCamera.prevPosZ = curCamZ;
        dummyCamera.lastTickPosX = curCamX;
        dummyCamera.lastTickPosY = curCamY;
        dummyCamera.lastTickPosZ = curCamZ;
        dummyCamera.rotationYaw = camYaw;
        dummyCamera.prevRotationYaw = camYaw;
        dummyCamera.rotationPitch = camPitch;
        dummyCamera.prevRotationPitch = camPitch;
        dummyCamera.cameraPitch = 0.0F;
        dummyCamera.prevCameraPitch = 0.0F;

        int prevFBO = GL11.glGetInteger(36006); // GL_FRAMEBUFFER_BINDING
        int origW = mc.displayWidth;
        int origH = mc.displayHeight;
        int origThirdPerson = mc.gameSettings.thirdPersonView;
        boolean origHideGui = mc.gameSettings.hideGUI;
        float origFov = mc.gameSettings.fovSetting;
        Entity origRVE = mc.getRenderViewEntity();
        boolean origIgnoreFrustum = carrier.ignoreFrustumCheck;
        float camFov = 85.0F; // Wide-angle bodycam lens

        // ---- Save full OpenGL fog state before FBO render ----
        // renderWorld() internally calls updateFog / sky rendering which permanently
        // mutates fog color and mode in GL state, corrupting the main view sky and
        // producing a black fog overlay on the bodycam feed.
        boolean prevFogEnabled = GL11.glIsEnabled(GL11.GL_FOG);
        int prevFogMode   = GL11.glGetInteger(GL11.GL_FOG_MODE);
        float prevFogStart   = GL11.glGetFloat(GL11.GL_FOG_START);
        float prevFogEnd     = GL11.glGetFloat(GL11.GL_FOG_END);
        float prevFogDensity = GL11.glGetFloat(GL11.GL_FOG_DENSITY);
        FloatBuffer prevFogColor = BufferUtils.createFloatBuffer(16); // LWJGL2 glGetFloat requires ≥16 elements
        GL11.glGetFloat(GL11.GL_FOG_COLOR, prevFogColor);

        try {
            isRendering = true;
            currentCarrier = carrier;
            carrierRenderedInPass = false;
            carrier.ignoreFrustumCheck = true; // Ensure player body is never culled by camera frustum
            feedFbo.bindFramebuffer(true);
            GL11.glViewport(0, 0, FBO_W, FBO_H);

            mc.displayWidth = FBO_W;
            mc.displayHeight = FBO_H;
            mc.gameSettings.thirdPersonView = 0;
            mc.gameSettings.hideGUI = true;
            mc.gameSettings.fovSetting = camFov;
            mc.setRenderViewEntity(dummyCamera);

            GlStateManager.clearColor(0.02F, 0.04F, 0.03F, 1.0F);
            GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            long finishTime = System.nanoTime() + 1000000000L / 60;
            mc.entityRenderer.renderWorld(event.renderTickTime, finishTime);

        } catch (Throwable ignored) {
        } finally {
            carrier.ignoreFrustumCheck = origIgnoreFrustum;
            currentCarrier = null;
            carrierRenderedInPass = false;
            isRendering = false;
            mc.setRenderViewEntity(origRVE);
            mc.gameSettings.fovSetting = origFov;
            mc.gameSettings.thirdPersonView = origThirdPerson;
            mc.gameSettings.hideGUI = origHideGui;
            mc.displayWidth = origW;
            mc.displayHeight = origH;
            if (mc.getFramebuffer() != null) {
                mc.getFramebuffer().bindFramebuffer(true);
            } else {
                OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, prevFBO);
                GlStateManager.viewport(0, 0, origW, origH);
            }

            // ---- Restore OpenGL fog state so main game sky/fog is unchanged ----
            // We restore both the raw GL state AND the GlStateManager cache so that
            // GlStateManager doesn't serve stale bodycam fog colour on the next frame.
            prevFogColor.rewind();
            GL11.glFog(GL11.GL_FOG_COLOR, prevFogColor); // restore fog colour in raw GL
            GL11.glFogi(GL11.GL_FOG_MODE, prevFogMode);
            GL11.glFogf(GL11.GL_FOG_START, prevFogStart);
            GL11.glFogf(GL11.GL_FOG_END, prevFogEnd);
            GL11.glFogf(GL11.GL_FOG_DENSITY, prevFogDensity);
            if (prevFogEnabled) {
                GlStateManager.enableFog();
            } else {
                GlStateManager.disableFog();
            }
            // Sync GlStateManager fog-mode / range caches so it does not re-apply stale values
            GlStateManager.FogMode restoredFogMode = (prevFogMode == GL11.GL_EXP2)
                    ? GlStateManager.FogMode.EXP2
                    : (prevFogMode == GL11.GL_EXP ? GlStateManager.FogMode.EXP : GlStateManager.FogMode.LINEAR);
            GlStateManager.setFog(restoredFogMode);
            GlStateManager.setFogStart(prevFogStart);
            GlStateManager.setFogEnd(prevFogEnd);



            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            GlStateManager.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
            if (mc.entityRenderer != null && lightmapUpdateNeededField != null) {
                try {
                    lightmapUpdateNeededField.setBoolean(mc.entityRenderer, true);
                } catch (Throwable ignored) {}
            }
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(GL11.GL_LEQUAL);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
            );
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (mc.player != null && mc.entityRenderer != null) {
                if (setupCameraTransformMethod != null) {
                    try {
                        setupCameraTransformMethod.invoke(mc.entityRenderer, event.renderTickTime, 0);
                    } catch (Throwable ignored) {}
                }
                try {
                    net.minecraft.client.renderer.ActiveRenderInfo.updateRenderInfo(mc.player, mc.gameSettings.thirdPersonView == 2);
                } catch (Throwable ignored) {}
            }
        }

        // Calculate accurate tactical entity detection rectangles on the camera screen
        updateTacticalRectangles(mc, carrier, curCamX, curCamY, curCamZ, camYaw, camPitch, camFov, pTicks);
    }

    private void updateTacticalRectangles(Minecraft mc, Entity carrier, double camX, double camY, double camZ,
                                          float camYaw, float camPitch, double fov, float pTicks) {
        detectedRects.clear();
        if (mc.world == null) return;

        double tanHalfFov = Math.tan(Math.toRadians(fov / 2.0));
        double aspect = (double) FBO_W / (double) FBO_H;

        double radYaw = Math.toRadians(camYaw);
        double cosYaw = Math.cos(radYaw);
        double sinYaw = Math.sin(radYaw);

        double radP = Math.toRadians(camPitch);
        double cosP = Math.cos(radP);
        double sinP = Math.sin(radP);

        for (Entity ent : mc.world.loadedEntityList) {
            if (!(ent instanceof EntityLivingBase) || !ent.isEntityAlive()) continue;
            if (ent == carrier || ent == dummyCamera) continue;

            double distSq = ent.getDistanceSq(camX, camY, camZ);
            if (distSq < 0.5 || distSq > 28.0 * 28.0) continue;

            double ex = ent.prevPosX + (ent.posX - ent.prevPosX) * pTicks;
            double ey = ent.prevPosY + (ent.posY - ent.prevPosY) * pTicks;
            double ez = ent.prevPosZ + (ent.posZ - ent.prevPosZ) * pTicks;

            double hw = ent.width * 0.5;
            double eh = ent.height;

            double[] cornersX = {ex - hw, ex + hw, ex - hw, ex + hw, ex - hw, ex + hw, ex - hw, ex + hw};
            double[] cornersY = {ey, ey, ey + eh, ey + eh, ey, ey, ey + eh, ey + eh};
            double[] cornersZ = {ez - hw, ez - hw, ez - hw, ez - hw, ez + hw, ez + hw, ez + hw, ez + hw};

            float minU = 2.0f;
            float maxU = -1.0f;
            float minV = 2.0f;
            float maxV = -1.0f;
            int visibleCorners = 0;

            for (int i = 0; i < 8; i++) {
                double dx = cornersX[i] - camX;
                double dy = cornersY[i] - camY;
                double dz = cornersZ[i] - camZ;

                // Step 1: Rotate by Yaw (Minecraft camera looks at Yaw+180)
                double x1 = -dx * cosYaw - dz * sinYaw;
                double z1 = dx * sinYaw - dz * cosYaw;

                // Step 2: Rotate by Pitch
                double x2 = x1;
                double y2 = dy * cosP - z1 * sinP;
                double z2 = dy * sinP + z1 * cosP;

                double depth = -z2;
                if (depth <= 0.4) continue;

                double ndcX = (x2 / depth) / (tanHalfFov * aspect);
                double ndcY = (y2 / depth) / tanHalfFov;

                float u = (float) (0.5 + 0.5 * ndcX);
                float v = (float) (0.5 - 0.5 * ndcY);

                if (u < minU) minU = u;
                if (u > maxU) maxU = u;
                if (v < minV) minV = v;
                if (v > maxV) maxV = v;
                visibleCorners++;
            }

            if (visibleCorners >= 2 && maxU > 0.02f && minU < 0.98f && maxV > 0.02f && minV < 0.98f) {
                float padX = 0.015f;
                float padY = 0.015f;
                float left = Math.max(0.01f, minU - padX);
                float right = Math.min(0.99f, maxU + padX);
                float top = Math.max(0.01f, minV - padY);
                float bottom = Math.min(0.99f, maxV + padY);

                if (right - left > 0.02f && bottom - top > 0.02f) {
                    detectedRects.add(new TacticalRect(left, right, top, bottom));
                }
            }
        }
    }
}
