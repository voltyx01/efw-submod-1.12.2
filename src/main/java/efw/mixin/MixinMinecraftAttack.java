package efw.mixin;

import net.bettercombat.client.BetterCombatClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraftAttack {

    @Shadow
    private int leftClickCounter;

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void onMouseClick(CallbackInfo ci) {
        if (BetterCombatClient.isUpswingActive() || BetterCombatClient.attackCooldown > 0) {
            this.leftClickCounter = Math.max(this.leftClickCounter, BetterCombatClient.attackCooldown);
            ci.cancel();
            return;
        }
        if (BetterCombatClient.onAttackInput()) {
            this.leftClickCounter = Math.max(this.leftClickCounter, BetterCombatClient.attackCooldown);
            ci.cancel();
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"), cancellable = true)
    private void onSendClickBlock(boolean leftClick, CallbackInfo ci) {
        if (leftClick && (BetterCombatClient.isUpswingActive() || BetterCombatClient.attackCooldown > 0)) {
            this.leftClickCounter = Math.max(this.leftClickCounter, BetterCombatClient.attackCooldown);
            ci.cancel();
        }
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    private void onRightClickMouse(CallbackInfo ci) {
        if (BetterCombatClient.isUpswingActive() || BetterCombatClient.attackCooldown > 0) {
            ci.cancel();
        }
    }
}
