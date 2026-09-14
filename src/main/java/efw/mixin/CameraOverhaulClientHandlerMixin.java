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

    @Inject(method = "onCameraSetup", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onCameraSetupHead(EntityViewRenderEvent.CameraSetup event, CallbackInfo ci) {
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
