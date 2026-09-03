package efw.mixin;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "biomesoplenty.common.handler.FogEventHandler", remap = false)
public abstract class MixinBoPFogEventHandler {

    /**
     * Disables BiomesOPlenty FogEventHandler completely to eliminate the 12.6% per-frame
     * bottleneck caused by uncached World.getBiome() lookups on every single render frame.
     */
    @Inject(method = "onRenderFog", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderFogHead(EntityViewRenderEvent.RenderFogEvent event, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onGetFogColor", at = @At("HEAD"), cancellable = true, remap = false)
    private void onGetFogColorHead(EntityViewRenderEvent.FogColors event, CallbackInfo ci) {
        ci.cancel();
    }
}
