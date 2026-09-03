package efw.mixin;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({World.class})
public abstract class WeatherDisableMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] WeatherDisableMixin class loaded!");
    }
  @Inject(method = {"getRainStrength"}, at = {@At("HEAD")}, cancellable = true)
  private void onGetRainStrength(float delta, CallbackInfoReturnable<Float> cir) {
    if (isCalledByMWC())
      cir.setReturnValue(Float.valueOf(0.0F)); 
  }
  
  @Inject(method = {"getThunderStrength"}, at = {@At("HEAD")}, cancellable = true)
  private void onGetThunderStrength(float delta, CallbackInfoReturnable<Float> cir) {
    if (isCalledByMWC())
      cir.setReturnValue(Float.valueOf(0.0F)); 
  }
  
  private boolean isCalledByMWC() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    for (StackTraceElement element : stackTrace) {
      if (element.getClassName().contains("com.paneedah.weaponlib.compatibility.CompatibleWorldRenderer"))
        return true; 
    } 
    return false;
  }
}