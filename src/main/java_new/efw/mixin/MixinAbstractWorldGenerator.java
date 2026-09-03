package efw.mixin;

import net.minecraft.world.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "climateControl.generator.AbstractWorldGenerator", remap = false)
public class MixinAbstractWorldGenerator {

    @Inject(method = "rtgAwareRiverReduction", at = @At("HEAD"), cancellable = true, remap = false)
    private void efw$rtgAwareRiverReduction(int originalReduction, WorldType worldType, CallbackInfoReturnable<Integer> cir) {
        if (worldType != null && worldType.getName() != null) {
            String name = worldType.getName();
            if (name.equalsIgnoreCase("lc_rtgc") || name.equalsIgnoreCase("lostcities_rtgc")) {
                cir.setReturnValue(100);
            }
        }
    }
}
