package com.teamderpy.shouldersurfing.mixin;

import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinWorld {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinWorld class loaded!");
    }

    private float shouldersurfing_savedYaw   = Float.NaN;
    private float shouldersurfing_savedPitch = Float.NaN;

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
        if (ShoulderInstance.getInstance().doShoulderSurfing() && Minecraft.getMinecraft().world != null && x == 0.0F && y == 0.0F && z < 0.0F) {
            Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            float yaw = entity != null ? entity.rotationYaw : 0.0F;
            float pitch = entity != null ? entity.rotationPitch : 0.0F;
            ShoulderRenderer.getInstance().offsetCamera(x, y, z, yaw, pitch);
        } else {
            net.minecraft.client.renderer.GlStateManager.translate(x, y, z);
        }
    }
}