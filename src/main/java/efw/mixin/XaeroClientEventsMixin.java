package efw.mixin;

import com.voltyx.mwccf.geo.MapDeviceState;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "xaero.common.events.ClientEvents", remap = false)
public abstract class XaeroClientEventsMixin {

    @Inject(method = "handleRenderGameOverlayEventPreAll", at = @At("HEAD"), cancellable = true, remap = false)
    private void onHandleRenderGameOverlayEventPreAll(float partialTicks, CallbackInfo ci) {
        if (!MapDeviceState.hasActiveMap()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleRenderModOverlay", at = @At("HEAD"), cancellable = true, remap = false)
    private void onHandleRenderModOverlay(float partialTicks, ScaledResolution scaledRes, CallbackInfo ci) {
        if (!MapDeviceState.hasActiveMap()) {
            ci.cancel();
        }
    }
}
