package efw.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntityCollision {
    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    private void mwccf$onApplyEntityCollision(Entity entityIn, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (self instanceof EntityPlayer && com.voltyx.mwccf.dash.DashEvents.isPlayerRolling((EntityPlayer) self)) {
            ci.cancel();
        } else if (entityIn instanceof EntityPlayer && com.voltyx.mwccf.dash.DashEvents.isPlayerRolling((EntityPlayer) entityIn)) {
            ci.cancel();
        }
    }
}
