package efw.mixin;

import com.voltyx.mwccf.geo.MapDeviceState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "xaero.hud.module.ModuleSession", remap = false)
public abstract class XaeroModuleSessionMixin {

    @Inject(method = "prePotentialRender", at = @At("HEAD"), cancellable = true, remap = false)
    private void onPrePotentialRender(CallbackInfo ci) {
        if (!MapDeviceState.hasActiveMap()) {
            ci.cancel();
        }
    }
}
