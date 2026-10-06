package com.teamderpy.shouldersurfing.mixin;

import com.teamderpy.shouldersurfing.client.CameraSoundEntity;
import com.teamderpy.shouldersurfing.client.ShoulderHelper;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundHandler.class)
public abstract class MixinSoundHandler {

    private static CameraSoundEntity CAMERA_SOUND_ENTITY = null;

    @ModifyVariable(method = "setListener(Lnet/minecraft/entity/Entity;F)V", at = @At("HEAD"), argsOnly = true, remap = false)
    private Entity modifyListenerEntity(Entity entity) {
        if (ShoulderInstance.getInstance().doShoulderSurfing() && entity != null && entity.world != null) {
            ShoulderRenderer renderer = ShoulderRenderer.getInstance();
            float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
            ShoulderHelper.ShoulderLook look = ShoulderHelper.shoulderSurfingLook(entity, partialTicks, 0.0D);
            Vec3d cameraPos = look.cameraPos();

            if (CAMERA_SOUND_ENTITY == null || CAMERA_SOUND_ENTITY.world != entity.world) {
                CAMERA_SOUND_ENTITY = new CameraSoundEntity(entity.world);
            }

            CAMERA_SOUND_ENTITY.posX = cameraPos.x;
            CAMERA_SOUND_ENTITY.prevPosX = cameraPos.x;
            CAMERA_SOUND_ENTITY.posY = cameraPos.y;
            CAMERA_SOUND_ENTITY.prevPosY = cameraPos.y;
            CAMERA_SOUND_ENTITY.posZ = cameraPos.z;
            CAMERA_SOUND_ENTITY.prevPosZ = cameraPos.z;

            float yaw = renderer.cameraYaw - 180.0F;
            float pitch = renderer.cameraPitch;

            CAMERA_SOUND_ENTITY.rotationYaw = yaw;
            CAMERA_SOUND_ENTITY.prevRotationYaw = yaw;
            CAMERA_SOUND_ENTITY.rotationPitch = pitch;
            CAMERA_SOUND_ENTITY.prevRotationPitch = pitch;

            return CAMERA_SOUND_ENTITY;
        }
        return entity;
    }

    @Inject(method = "setListener(Lnet/minecraft/entity/player/EntityPlayer;F)V", at = @At("HEAD"), cancellable = true)
    private void onSetListenerPlayer(EntityPlayer player, float partialTicks, CallbackInfo ci) {
        if (ShoulderInstance.getInstance().doShoulderSurfing() && player != null) {
            ((SoundHandler) (Object) this).setListener((Entity) player, partialTicks);
            ci.cancel();
        }
    }
}
