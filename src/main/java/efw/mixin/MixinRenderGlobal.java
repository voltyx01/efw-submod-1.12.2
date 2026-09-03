package efw.mixin;

import com.voltyx.mwccf.sunmoon.RealisticSunMoon;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin on RenderGlobal that applies B3M's latitude tilt to the celestial sphere
 * and customizes sun and moon dimensions.
 */
@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {

    @Shadow
    private WorldClient world;

    /**
     * Intercepts GlStateManager.rotate in RenderGlobal.renderSky:
     *
     * 1. Sun/Moon/Stars orientation: rotate(-90.0F, 0.0F, 1.0F, 0.0F)
     *    Applies original -90° rotation, followed immediately by the B3M latitude tilt:
     *    rotate(-latitude, 0.0F, 0.0F, 1.0F).
     *    This tilts the entire orbital arc (sun, moon, and stars) across the sky.
     *
     * 2. Sunrise/Sunset glow orientation:
     *    Replaces the vanilla 0°/180° flip with the true azimuth sun heading.
     */
    @Redirect(
        method = "renderSky(FI)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V"
        )
    )
    private void b3m$handleRotations(float angle, float x, float y, float z) {
        // 1. Celestial sphere orientation (sun, moon, stars)
        if (angle == -90.0F && x == 0.0F && y == 1.0F && z == 0.0F) {
            GlStateManager.rotate(angle, x, y, z);
            if (this.world != null && this.world.provider.getDimension() == 0) {
                // Tilt orbital plane by observer's latitude around the Z axis
                GlStateManager.rotate(-RealisticSunMoon.latitude, 0.0F, 0.0F, 1.0F);
            }
            return;
        }

        // 2. Sunrise/sunset azimuth heading (vanilla rotates around Z by 180° or 0°)
        if ((angle == 180.0F || angle == 0.0F) && x == 0.0F && y == 0.0F && z == 1.0F) {
            if (this.world != null && this.world.provider.getDimension() == 0) {
                float heading = RealisticSunMoon.getSunHeading(this.world.getCelestialAngle(1.0F));
                GlStateManager.rotate(heading, 0.0F, 0.0F, 1.0F);
                return;
            }
        }

        // Default rotation
        GlStateManager.rotate(angle, x, y, z);
    }

    /**
     * Replace vanilla sun disc half-size (30.0f).
     */
    @ModifyConstant(
        method = "renderSky(FI)V",
        constant = @Constant(floatValue = 30.0f)
    )
    private float b3m$sunSize(float original) {
        return RealisticSunMoon.sunSize;
    }

    /**
     * Replace vanilla moon disc half-size (20.0f).
     */
    @ModifyConstant(
        method = "renderSky(FI)V",
        constant = @Constant(floatValue = 20.0f)
    )
    private float b3m$moonSize(float original) {
        return RealisticSunMoon.moonSize;
    }
}