package efw.mixin;

import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.WeaponFireAspect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WeaponFireAspect.class})
public abstract class WeaponMessageMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] WeaponMessageMixin class loaded!");
    }
  @Inject(method = {"cannotFire"}, at = {@At("HEAD")}, cancellable = true, remap = false)
  private void silentCannotFire(PlayerWeaponInstance weaponInstance, CallbackInfo ci) {
    ci.cancel();
  }
}