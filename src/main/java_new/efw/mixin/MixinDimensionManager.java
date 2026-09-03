package efw.mixin;

import climateControl.api.ClimateControlSettings;
import net.minecraft.world.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "climateControl.DimensionManager", remap = false)
public class MixinDimensionManager {

    @Inject(method = "ignore", at = @At("HEAD"), cancellable = true, remap = false)
    private void efw$allowLCRTGC(WorldType worldType, ClimateControlSettings settings, CallbackInfoReturnable<Boolean> cir) {
        if (worldType != null && worldType.getName() != null) {
            String name = worldType.getName();
            if (name.equalsIgnoreCase("lc_rtgc") || name.equalsIgnoreCase("lostcities_rtgc") || name.equalsIgnoreCase("lostcities")) {
                cir.setReturnValue(false);
            }
        }
    }
}
