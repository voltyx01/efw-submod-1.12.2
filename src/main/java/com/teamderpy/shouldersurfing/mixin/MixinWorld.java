package com.teamderpy.shouldersurfing.mixin;

import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinWorld {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinWorld class loaded!");
    }

    @Shadow private float fogColorRed;
    @Shadow private float fogColorGreen;
    @Shadow private float fogColorBlue;
    @Shadow private float fogColor2;
    @Shadow protected abstract java.nio.FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha);

    private float shouldersurfing_savedYaw   = Float.NaN;
    private float shouldersurfing_savedPitch = Float.NaN;

    private float efw$skyFogRed = 1.0F;
    private float efw$skyFogGreen = 1.0F;
    private float efw$skyFogBlue = 1.0F;
    private boolean efw$hasSkyFogColor = false;
    private int efw$currentStartCoords = 0;

    /**
     * updateFogColor РІС‹С‡РёСЃР»СЏРµС‚ СѓРіРѕР» РјРµР¶РґСѓ РІР·РіР»СЏРґРѕРј РёРіСЂРѕРєР° Рё СЃРѕР»РЅС†РµРј С‡РµСЂРµР·
     * entity.getLook(partialTicks), РєРѕС‚РѕСЂС‹Р№ Р±РµСЂС‘С‚ rotationYaw/rotationPitch РёРіСЂРѕРєР°.
     * РџСЂРё shoulder-surfing РєР°РјРµСЂР° РѕС‚РІСЏР·Р°РЅР° РѕС‚ РіРѕР»РѕРІС‹ вЂ” РѕС‚СЃСЋРґР° РјРµСЂС†Р°РЅРёРµ С†РІРµС‚Р° С‚СѓРјР°РЅР°
     * РЅР° РіРѕСЂРёР·РѕРЅС‚Рµ РїСЂРё Р·Р°РєР°С‚Рµ/СЂР°СЃСЃРІРµС‚Рµ.
     *
     * РџРѕРґРјРµРЅСЏРµРј rotationYaw/Pitch РёРіСЂРѕРєР° РЅР° СѓРіР»С‹ РєР°РјРµСЂС‹ РЅР° РІСЂРµРјСЏ РІС‹Р·РѕРІР° РјРµС‚РѕРґР°.
     */
    @Inject(method = "updateFogColor", at = @At("HEAD"))
    private void onUpdateFogColorHead(float partialTicks, CallbackInfo ci) {
        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering() || efw.util.SubpassRenderState.isMirrorRendering) return;
        if (!ShoulderInstance.getInstance().doShoulderSurfing()) return;

        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        if (entity == null || entity != mc.player) return;

        ShoulderRenderer renderer = ShoulderRenderer.getInstance();
        shouldersurfing_savedYaw   = entity.rotationYaw;
        shouldersurfing_savedPitch = entity.rotationPitch;
        entity.rotationYaw   = renderer.cameraYaw;
        entity.rotationPitch = renderer.cameraPitch;
    }

    @Inject(method = "updateFogColor", at = @At("RETURN"))
    private void onUpdateFogColorReturn(float partialTicks, CallbackInfo ci) {
        if (Float.isNaN(shouldersurfing_savedYaw)) return;

        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        if (entity != null) {
            entity.rotationYaw   = shouldersurfing_savedYaw;
            entity.rotationPitch = shouldersurfing_savedPitch;
        }
        shouldersurfing_savedYaw   = Float.NaN;
        shouldersurfing_savedPitch = Float.NaN;
    }

    @org.spongepowered.asm.mixin.injection.Redirect(
        method = "orientCamera",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"
        )
    )
    private void redirectOrientCameraTranslate(float x, float y, float z) {
        if (!com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()
                && !efw.util.SubpassRenderState.isMirrorRendering
                && ShoulderInstance.getInstance().doShoulderSurfing()
                && Minecraft.getMinecraft().world != null && x == 0.0F && y == 0.0F && z < 0.0F) {
            Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            float yaw = entity != null ? entity.rotationYaw : 0.0F;
            float pitch = entity != null ? entity.rotationPitch : 0.0F;
            ShoulderRenderer.getInstance().offsetCamera(x, y, z, yaw, pitch);
        } else {
            net.minecraft.client.renderer.GlStateManager.translate(x, y, z);
        }
    }

    @Inject(method = "getFOVModifier", at = @At("HEAD"), cancellable = true)
    private void efw$bodycamLockFOV(float partialTicks, boolean useFOVSetting, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Float> cir) {
        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            cir.setReturnValue(85.0F);
        }
    }

    @org.spongepowered.asm.mixin.injection.Redirect(
        method = "updateFogColor",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/EntityRenderer;fogColor2:F",
            opcode = org.objectweb.asm.Opcodes.GETFIELD,
            ordinal = 0
        )
    )
    private float efw$captureSkyFogAndReadFogColor2(EntityRenderer renderer) {
        this.efw$skyFogRed = this.fogColorRed;
        this.efw$skyFogGreen = this.fogColorGreen;
        this.efw$skyFogBlue = this.fogColorBlue;
        this.efw$hasSkyFogColor = true;
        return this.fogColor2;
    }

    @Inject(method = "setupFog", at = @At("HEAD"))
    private void efw$onSetupFogHead(int startCoords, float partialTicks, CallbackInfo ci) {
        this.efw$currentStartCoords = startCoords;
    }

    @Inject(method = "setupFogColor", at = @At("HEAD"), cancellable = true)
    private void efw$onSetupFogColorHead(boolean black, CallbackInfo ci) {
        if (!black && this.efw$currentStartCoords == -1 && this.efw$hasSkyFogColor) {
            net.minecraft.client.renderer.GlStateManager.glFog(2918, this.setFogColorBuffer(this.efw$skyFogRed, this.efw$skyFogGreen, this.efw$skyFogBlue, 1.0F));
            ci.cancel();
        }
    }
}