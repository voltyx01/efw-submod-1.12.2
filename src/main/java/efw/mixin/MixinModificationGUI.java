package efw.mixin;

import com.paneedah.weaponlib.ModContext;
import com.paneedah.weaponlib.render.ModificationGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.paneedah.weaponlib.render.ModificationGUI", remap = false)
public class MixinModificationGUI {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRender(ModContext modContext, CallbackInfo ci) {
        // Полностью блокируем рендер старого ModificationGUI
        ci.cancel();
    }
}
