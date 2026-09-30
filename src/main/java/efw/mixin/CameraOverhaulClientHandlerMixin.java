package efw.mixin;

import com.voltyx.mwccf.antenna.client.AntennaCameraController;
import com.voltyx.mwccf.terminal.client.TerminalCameraController;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "ua.myxazaur.cameraoverhaul.client.ClientHandler", remap = false)
public class CameraOverhaulClientHandlerMixin {

    private static long suppressUntilTime = 0L;

    private static ua.myxazaur.cameraoverhaul.camera.CameraSystem bodycamCamera;
    private static ua.myxazaur.cameraoverhaul.camera.CameraContext bodycamContext;
    private static final float INTENSITY_MULTIPLIER = 1.75F;

    @Inject(method = "onCameraSetup", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onCameraSetupHead(EntityViewRenderEvent.CameraSetup event, CallbackInfo ci) {
        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            net.minecraft.entity.Entity carrier = com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.getCurrentCarrier();
            if (carrier == null) {
                event.setRoll(0.0F);
                ci.cancel();
                return;
            }

            if (bodycamCamera == null) {
                bodycamCamera = new ua.myxazaur.cameraoverhaul.camera.CameraSystem();
                bodycamContext = new ua.myxazaur.cameraoverhaul.camera.CameraContext();
            }

            net.minecraft.entity.Entity vehicle = carrier.getRidingEntity();
            net.minecraft.entity.Entity controlled = vehicle != null ? vehicle : carrier;

            bodycamContext.isRiding = vehicle != null;
            bodycamContext.isRidingMount = vehicle instanceof net.minecraft.entity.passive.EntityAnimal;
            bodycamContext.isRidingVehicle = bodycamContext.isRiding && !(vehicle instanceof net.minecraft.entity.EntityLivingBase);

            double velX = controlled.motionX;
            double velY = controlled.motionY;
            double velZ = controlled.motionZ;
            if (Math.abs(velX) < 0.0001 && Math.abs(velZ) < 0.0001) {
                velX = controlled.posX - controlled.prevPosX;
                velY = controlled.posY - controlled.prevPosY;
                velZ = controlled.posZ - controlled.prevPosZ;
            }
            bodycamContext.velocity.set(velX, velY, velZ);

            float pTicks = (float) event.getRenderPartialTicks();
            bodycamContext.transform.position.set(
                    carrier.prevPosX + (carrier.posX - carrier.prevPosX) * pTicks,
                    carrier.prevPosY + (carrier.posY - carrier.prevPosY) * pTicks,
                    carrier.prevPosZ + (carrier.posZ - carrier.prevPosZ) * pTicks
            );

            float origPitch = event.getPitch();
            float origYaw = event.getYaw();
            bodycamContext.transform.eulerRot.set(origPitch, origYaw, 0.0);

            bodycamContext.perspective = ua.myxazaur.cameraoverhaul.camera.CameraContext.Perspective.FIRST_PERSON;

            if (carrier instanceof net.minecraft.entity.EntityLivingBase) {
                net.minecraft.entity.EntityLivingBase living = (net.minecraft.entity.EntityLivingBase) carrier;
                bodycamContext.isFlying = living.isElytraFlying();
                bodycamContext.isSprinting = living.isSprinting();
                if (living instanceof com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable) {
                    com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable res = (com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable) living;
                    bodycamContext.isSwimming = res.isActuallySwimming() || res.getPose() == com.fuzs.aquaacrobatics.entity.Pose.SWIMMING;
                } else {
                    bodycamContext.isSwimming = living.isInWater() && living.isSprinting();
                }
            }

            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
            if (!mc.isGamePaused()) {
                ua.myxazaur.cameraoverhaul.camera.TimeSystem.update();
                bodycamCamera.onCameraUpdate(bodycamContext, ua.myxazaur.cameraoverhaul.camera.TimeSystem.getDeltaTime());
            }

            bodycamCamera.modifyCameraTransform(bodycamContext.transform);

            float deltaPitch = (float) (bodycamContext.transform.eulerRot.x - origPitch);
            float deltaYaw = net.minecraft.util.math.MathHelper.wrapDegrees((float) (bodycamContext.transform.eulerRot.y - origYaw));
            float deltaRoll = (float) -bodycamContext.transform.eulerRot.z;

            event.setPitch(origPitch + deltaPitch * INTENSITY_MULTIPLIER);
            event.setYaw(origYaw + deltaYaw * INTENSITY_MULTIPLIER);
            event.setRoll(deltaRoll * INTENSITY_MULTIPLIER);

            ci.cancel();
            return;
        }

        boolean antennaActive = AntennaCameraController.isActive() || AntennaCameraController.getTransitionProgress() > 0.001f;
        boolean terminalActive = TerminalCameraController.isActive() || TerminalCameraController.getTransitionProgress() > 0.001f;

        long now = System.currentTimeMillis();
        if (antennaActive || terminalActive) {
            suppressUntilTime = now + 150L;
            event.setRoll(0.0F);
            try {
                ua.myxazaur.cameraoverhaul.CameraOverhaul.camera = new ua.myxazaur.cameraoverhaul.camera.CameraSystem();
            } catch (Throwable ignored) {
            }
            ci.cancel();
            return;
        }

        if (now < suppressUntilTime) {
            event.setRoll(0.0F);
            try {
                ua.myxazaur.cameraoverhaul.CameraOverhaul.camera = new ua.myxazaur.cameraoverhaul.camera.CameraSystem();
            } catch (Throwable ignored) {
            }
            ci.cancel();
        }
    }
}
