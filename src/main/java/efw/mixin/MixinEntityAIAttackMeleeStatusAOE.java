package efw.mixin;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIAttackMeleeStatusAOE;
import com.voltyx.mwccf.dash.DashEvents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityAIAttackMeleeStatusAOE.class, remap = false)
public abstract class MixinEntityAIAttackMeleeStatusAOE {

    @Inject(method = "checkAndPerformAttack", at = @At("HEAD"), cancellable = true)
    private void mwccf$cancelCheckAndPerformAttackAOE(EntityLivingBase target, double distance, CallbackInfo ci) {
        if (target instanceof EntityPlayer && DashEvents.isPlayerRolling((EntityPlayer) target)) {
            ci.cancel();
        }
    }
}
