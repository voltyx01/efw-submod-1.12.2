package com.teamderpy.shouldersurfing.mixin;

import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ActiveRenderInfo.class)
public class MixinActiveRenderInfo {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinActiveRenderInfo class loaded!");
    }

    @Shadow private static float rotationX;
    @Shadow private static float rotationXZ;
    @Shadow private static float rotationZ;
    @Shadow private static float rotationYZ;
    @Shadow private static float rotationXY;

    @Inject(method = "updateRenderInfo(Lnet/minecraft/entity/player/EntityPlayer;Z)V", at = @At("RETURN"))
    private static void onUpdateRenderInfo(net.minecraft.entity.player.EntityPlayer entity, boolean p_74583_1_, CallbackInfo ci) {
        if (ShoulderInstance.getInstance().doShoulderSurfing()) {
            ShoulderRenderer renderer = ShoulderRenderer.getInstance();

            float pitch = renderer.cameraPitch;
            float yaw = renderer.cameraYaw - 180.0F;

            float radYaw = (float) Math.toRadians(yaw);
            float radPitch = (float) Math.toRadians(pitch);

            int i = p_74583_1_ ? 1 : 0;
            float inv = (float)(1 - i * 2);

            rotationX = MathHelper.cos(radYaw) * inv;
            rotationZ = MathHelper.sin(radYaw) * inv;
            rotationYZ = -rotationZ * MathHelper.sin(radPitch) * inv;
            rotationXY = rotationX * MathHelper.sin(radPitch) * inv;
            rotationXZ = MathHelper.cos(radPitch); // Р—РґРµСЃСЊ РЅРµ РґРѕР»Р¶РЅРѕ Р±С‹С‚СЊ inv!
        }
    }
}