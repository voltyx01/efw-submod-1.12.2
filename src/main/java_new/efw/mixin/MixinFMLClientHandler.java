package efw.mixin;

import efw.client.SplashRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FMLClientHandler.class, remap = false)
public abstract class MixinFMLClientHandler {

    @Inject(method = "processWindowMessages", at = @At("HEAD"))
    private void onProcessWindowMessages(CallbackInfo ci) {
        SplashRenderer.onProgressStep();
    }
}
