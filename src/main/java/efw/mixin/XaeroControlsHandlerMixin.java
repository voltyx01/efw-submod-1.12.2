package efw.mixin;

import com.voltyx.mwccf.geo.MapDeviceState;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "xaero.map.controls.ControlsHandler", remap = false)
public abstract class XaeroControlsHandlerMixin {

    @Inject(method = "keyDown", at = @At("HEAD"), cancellable = true, remap = false)
    private void onKeyDown(KeyBinding kb, boolean tickEnd, boolean isRepeat, CallbackInfo ci) {
        if (!tickEnd && kb == xaero.map.controls.ControlsRegister.keyOpenMap) {
            if (!MapDeviceState.hasActiveMap()) {
                ci.cancel();
            }
        }
    }
}
