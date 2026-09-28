package efw.mixin;

import com.voltyx.mwccf.sunmoon.RealisticSunMoon;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin on RenderGlobal that applies B3M's latitude tilt to the celestial sphere
 * and customizes sun and moon dimensions.
 */
@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {

    @Shadow
    private WorldClient world;

    @Shadow
    private net.minecraft.client.renderer.entity.RenderManager renderManager;

    @Inject(
        method = "renderEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V",
        at = @At("RETURN")
    )
    private void efw$renderFirstPersonPlayerAttack(net.minecraft.entity.Entity renderViewEntity, net.minecraft.client.renderer.culling.ICamera camera, float partialTicks, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        if (mc.gameSettings.thirdPersonView == 0 && efw.animation.firstperson.FirstPersonMode.isFirstPersonAttackActive(mc.player)) {
            boolean prevPass = efw.animation.firstperson.FirstPersonMode.isFirstPersonPass();
            efw.animation.firstperson.FirstPersonMode.setFirstPersonPass(true);
            try {
                this.renderManager.renderEntityStatic(mc.player, partialTicks, false);
            } finally {
                efw.animation.firstperson.FirstPersonMode.setFirstPersonPass(prevPass);
            }
        }
    }

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