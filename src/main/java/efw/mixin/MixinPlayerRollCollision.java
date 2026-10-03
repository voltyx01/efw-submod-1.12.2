package efw.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinPlayerRollCollision {
    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    private void mwccf$onPlayerApplyEntityCollision(Entity entityIn, CallbackInfo ci) {
        EntityPlayer self = (EntityPlayer)(Object)this;
        if (com.voltyx.mwccf.dash.DashEvents.isPlayerRolling(self)) {
            ci.cancel();
        } else if (entityIn instanceof EntityPlayer && com.voltyx.mwccf.dash.DashEvents.isPlayerRolling((EntityPlayer) entityIn)) {
            ci.cancel();
        }
    }
}
