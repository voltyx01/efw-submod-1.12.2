package efw.mixin;

import com.voltyx.mwccf.geo.MapDeviceState;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "xaero.common.minimap.render.MinimapRenderer", remap = false)
public abstract class XaeroMinimapRendererMixin {

    @Inject(method = "renderMinimap", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderMinimap(xaero.hud.minimap.module.MinimapSession minimapSession,
                                xaero.common.minimap.MinimapProcessor minimap,
                                int x, int y, int width, int height,
                                ScaledResolution scaledRes, int size, float partial,
                                CallbackInfo ci) {
        if (!MapDeviceState.hasActiveMap()) {
            ci.cancel();
        }
    }
}
