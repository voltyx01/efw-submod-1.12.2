package efw.mixin;

import efw.biomeinfo.MwccfConfig;
import net.bettercombat.client.BetterCombatClient;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayerClient {

    @Inject(method = "getCooledAttackStrength", at = @At("HEAD"), cancellable = true)
    private void redirectCooledAttackStrength(float adjustTicks, CallbackInfoReturnable<Float> cir) {
        EntityPlayer player = (EntityPlayer) (Object) this;
        if (player.world != null && player.world.isRemote && player == Minecraft.getMinecraft().player) {
            if (MwccfConfig.betterCombat.enabled) {
                if (BetterCombatClient.isPerformingAttack) {
                    cir.setReturnValue(1.0F);
                    return;
                }
                ItemStack stack = player.getHeldItemMainhand();
                if (WeaponRegistry.getAttributes(stack) != null) {
                    cir.setReturnValue(BetterCombatClient.getCooledAttackStrength(player, adjustTicks));
                }
            }
        }
    }
}
