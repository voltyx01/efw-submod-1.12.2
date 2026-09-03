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
        if (this.isCameraEntityRenderingSkipped(player)) {
            return true; // Skip rendering
        }

        if (ShoulderInstance.getInstance().doShoulderSurfing() && Config.CLIENT.isPlayerTransparencyEnabled()) {
            this.cameraEntityAlpha = this.calcCameraEntityAlpha(player, partialTick);
        } else {
            this.cameraEntityAlpha = 1.0F;
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
        return false;
    }

    private float calcCameraEntityAlpha(Entity cameraEntity, float partialTick) {
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
        return this.isRenderingCameraEntity;
    }
}
