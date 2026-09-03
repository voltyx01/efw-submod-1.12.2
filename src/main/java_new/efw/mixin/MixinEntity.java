package efw.mixin;

import com.teamderpy.shouldersurfing.asm.InjectionDelegation;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinEntity class loaded!");
    }

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    private void onTurn(float yaw, float pitch, CallbackInfo ci) {
        if (InjectionDelegation.Entity_turn((Entity)(Object)this, yaw, pitch)) {
            ci.cancel();
        }
    }
}
