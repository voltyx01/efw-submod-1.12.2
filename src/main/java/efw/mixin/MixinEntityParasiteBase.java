package efw.mixin;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.voltyx.mwccf.dash.DashEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityParasiteBase.class, remap = false)
public abstract class MixinEntityParasiteBase {

    @Inject(method = "attackEntityAsMobMinimum", at = @At("HEAD"), cancellable = true)
    private void mwccf$cancelAttackMinimumOnRoll(EntityLivingBase target, float minDamage, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof EntityPlayer && DashEvents.isPlayerRolling((EntityPlayer) target)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "attackEntityAsMobFood", at = @At("HEAD"), cancellable = true)
    private void mwccf$cancelAttackFoodOnRoll(Entity target, boolean flag, int i, double d, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof EntityPlayer && DashEvents.isPlayerRolling((EntityPlayer) target)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = {"attackEntityAsMob", "func_70652_k"}, at = @At("HEAD"), cancellable = true, remap = false)
    private void mwccf$cancelAttackAsMobOnRoll(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof EntityPlayer && DashEvents.isPlayerRolling((EntityPlayer) target)) {
            cir.setReturnValue(false);
        }
    }
}
