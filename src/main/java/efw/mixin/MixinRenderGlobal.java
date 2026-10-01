package efw.mixin;

import com.voltyx.mwccf.sunmoon.RealisticSunMoon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
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

        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering() && !com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.carrierRenderedInPass) {
            net.minecraft.entity.Entity carrier = com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.getCurrentCarrier();
            if (carrier != null) {
                com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.carrierRenderedInPass = true;
                this.renderManager.renderEntityStatic(carrier, partialTicks, false);
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

    /**
     * Redirects player.getPositionEyes in RenderGlobal.renderSky so that when bodycam is rendering,
     * the dummy camera's eye position is used instead of the player standing at the terminal.
     */
    @Redirect(
        method = "renderSky(FI)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;getPositionEyes(F)Lnet/minecraft/util/math/Vec3d;"
        )
    )
    private net.minecraft.util.math.Vec3d efw$renderSkyEyePos(net.minecraft.client.entity.EntityPlayerSP player, float partialTicks) {
        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            net.minecraft.entity.Entity rve = net.minecraft.client.Minecraft.getMinecraft().getRenderViewEntity();
            if (rve != null) {
                return rve.getPositionEyes(partialTicks);
            }
        }
        return player.getPositionEyes(partialTicks);
    }

    /**
     * Redirects the World.getSkyColor call inside RenderGlobal.renderSky so that
     * during bodycam FBO rendering the sky dome uses mc.player's sky colour instead
     * of BodycamCameraEntity's. World.getSkyColor returns Vec3d.ZERO for non-Player
     * entities, causing the sky dome (and thus the horizon) to render black.
     */
    @Redirect(
        method = "renderSky(FI)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/WorldClient;getSkyColor(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/math/Vec3d;"
        )
    )
    private Vec3d efw$bodycamSkyColor(WorldClient world, Entity entity, float partialTicks) {
        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            Entity carrier = com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.getCurrentCarrier();
            if (carrier != null) {
                return world.getSkyColor(carrier, partialTicks);
            }
            net.minecraft.entity.Entity rve = net.minecraft.client.Minecraft.getMinecraft().getRenderViewEntity();
            if (rve != null) {
                return world.getSkyColor(rve, partialTicks);
            }
        }
        return world.getSkyColor(entity, partialTicks);
    }

    /**
     * Prevents vanilla RenderGlobal from drawing the underground black void box over the horizon
     * during bodycam rendering by setting horizon 64 blocks below camera eye height.
     * This keeps d3 >= 64.0D > 0 so:
     * 1. The black void box is never drawn.
     * 2. The dark bottom dome glSkyList2 is pushed 48 blocks below the camera, never obscuring horizon.
     * 3. Unaffected in the main game pass!
     */
    @Redirect(
        method = "renderSky(FI)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/WorldClient;getHorizon()D"
        )
    )
    private double efw$adjustHorizon(WorldClient world) {
        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            net.minecraft.entity.Entity rve = net.minecraft.client.Minecraft.getMinecraft().getRenderViewEntity();
            if (rve != null) {
                double eyeY = rve.posY + (double) rve.getEyeHeight();
                return eyeY - 64.0D;
            }
        }
        return world.getHorizon();
    }
}