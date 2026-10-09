package com.teamderpy.shouldersurfing.client;

import com.teamderpy.shouldersurfing.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CameraEntityRenderer {
    private static final CameraEntityRenderer INSTANCE = new CameraEntityRenderer();
    private static final float MIN_CAMERA_ENTITY_ALPHA = 0.15F;

    private float cameraEntityAlpha = 1.0F;
    private boolean isRenderingCameraEntity = false;

    public static CameraEntityRenderer getInstance() {
        return INSTANCE;
    }

    public boolean preRenderCameraEntity(EntityPlayer player, float partialTick) {
        if (ShoulderInstance.getInstance().doShoulderSurfing() && (Config.CLIENT.isPlayerTransparencyEnabled() || ShoulderInstance.getInstance().isTransitioningToFirstPerson())) {
            this.cameraEntityAlpha = this.calcCameraEntityAlpha(player, partialTick);
        } else {
            this.cameraEntityAlpha = 1.0F;
        }

        if (this.isCameraEntityRenderingSkipped(player)) {
            this.isRenderingCameraEntity = false;
            return true; // Skip rendering
        }

        this.isRenderingCameraEntity = true;
        return false;
    }

    public void postRenderCameraEntity(EntityPlayer player, float partialTick) {
        this.isRenderingCameraEntity = false;
    }

    public boolean isCameraEntityRenderingSkipped(Entity cameraEntity) {
        if (!ShoulderInstance.getInstance().doShoulderSurfing() || (cameraEntity instanceof EntityPlayer && ((EntityPlayer) cameraEntity).isSpectator())) {
            return false;
        }
        if (ShoulderInstance.getInstance().isTransitioningToFirstPerson() && this.cameraEntityAlpha <= 0.01F) {
            return true;
        }
        return false;
    }

    private float calcCameraEntityAlpha(Entity cameraEntity, float partialTick) {
        ShoulderInstance instance = ShoulderInstance.getInstance();
        if (instance.isTransitioningToFirstPerson()) {
            double curX = ShoulderHelper.lerp(partialTick, instance.getOffsetXOld(), instance.getOffsetX());
            double curY = ShoulderHelper.lerp(partialTick, instance.getOffsetYOld(), instance.getOffsetY());
            double curZ = ShoulderHelper.lerp(partialTick, instance.getOffsetZOld(), instance.getOffsetZ());
            double curDist = Math.sqrt(curX * curX + curY * curY + curZ * curZ);
            double maxDist = instance.getInitialExitDistance();
            float fadeProgress = (float) MathHelper.clamp(curDist / maxDist, 0.0, 1.0);
            float alpha = fadeProgress * fadeProgress;
            return MathHelper.clamp(alpha, 0.0F, 1.0F);
        }

        ShoulderRenderer renderer = ShoulderRenderer.getInstance();
        double cameraDistance = renderer.getCameraDistance();
        double offX = renderer.getCameraOffsetX();
        double offY = renderer.getCameraOffsetY();

        double halfWidth = cameraEntity.width / 2.0D;
        if (Math.abs(offX) < halfWidth) {
            float xAlpha = (float) MathHelper.clamp(Math.abs(offX) / halfWidth, 0.0, 1.0);
            float yAlpha = 0.0F;
            float eyeHeight = cameraEntity.getEyeHeight();
            float heightAboveEye = cameraEntity.height - eyeHeight;

            if (offY > 0) {
                yAlpha = (float) MathHelper.clamp(offY / (heightAboveEye > 0 ? heightAboveEye : 1.0), 0.0, 1.0);
            } else if (offY < 0) {
                yAlpha = (float) MathHelper.clamp(-offY / (eyeHeight > 0 ? eyeHeight : 1.0), 0.0, 1.0);
            }

            float distAlpha = (float) MathHelper.clamp(cameraDistance / 1.2D, MIN_CAMERA_ENTITY_ALPHA, 1.0);
            float offsetAlpha = (float) Math.sqrt(xAlpha * xAlpha + yAlpha * yAlpha);
            float alpha = Math.min(distAlpha, Math.max(offsetAlpha, MIN_CAMERA_ENTITY_ALPHA));

            return MathHelper.clamp(alpha, MIN_CAMERA_ENTITY_ALPHA, 1.0F);
        }

        float distAlpha = (float) MathHelper.clamp(cameraDistance / 1.0D, MIN_CAMERA_ENTITY_ALPHA, 1.0);
        return distAlpha;
    }

    public float getCameraEntityAlpha() {
        return this.cameraEntityAlpha;
    }

    public boolean isRenderingCameraEntity() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.gameSettings == null || mc.gameSettings.thirdPersonView == 0) {
            this.isRenderingCameraEntity = false;
            return false;
        }
        if (!ShoulderInstance.getInstance().doShoulderSurfing()) {
            this.isRenderingCameraEntity = false;
            return false;
        }
        return this.isRenderingCameraEntity;
    }

    public float getCurrentAlphaOverride() {
        if (this.isRenderingCameraEntity()) {
            return Math.max(0.001F, this.cameraEntityAlpha);
        }
        FirstPersonFadeManager fade = FirstPersonFadeManager.getInstance();
        if (fade.isRenderingFirstPersonHand() && fade.isFadingIn()) {
            return Math.max(0.001F, fade.getCurrentFadeAlpha());
        }
        return 1.0F;
    }

    public void reset() {
        this.isRenderingCameraEntity = false;
        this.cameraEntityAlpha = 1.0F;
    }
}
