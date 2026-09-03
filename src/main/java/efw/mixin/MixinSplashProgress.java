package efw.mixin;

import efw.client.SplashRenderer;
import net.minecraftforge.fml.client.SplashProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SplashProgress.class, remap = false)
public abstract class MixinSplashProgress {

    /**
     * Cancel Forge's splash thread entirely — we render on the main thread instead.
     * This prevents the white Forge loading screen from appearing.
     */
    @Inject(method = "start", at = @At("HEAD"), cancellable = true)
    private static void onStart(CallbackInfo ci) {
        // Forge's splash thread would create a SharedDrawable context and start rendering
        // its white loading screen. We prevent that here so our main-thread rendering wins.
        ci.cancel();
    }

    /**
     * Cancel Forge's finish — nothing to join/cleanup since we didn't start the thread.
     */
    @Inject(method = "finish", at = @At("HEAD"), cancellable = true)
    private static void onFinish(CallbackInfo ci) {
        ci.cancel();
    }
}
