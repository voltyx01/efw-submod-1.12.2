package efw.mixin;

import efw.biomeinfo.MwccfConfig;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.client.BetterCombatClient;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraftAttack {

    @Shadow
    private int leftClickCounter;

    @Inject(method = "processKeyBinds", at = @At("HEAD"))
    private void onProcessKeyBinds(CallbackInfo ci) {
        if (BetterCombatClient.isUpswingActive() || BetterCombatClient.attackCooldown > 0) {
            this.leftClickCounter = Math.max(this.leftClickCounter, BetterCombatClient.attackCooldown);
        }
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void onMouseClick(CallbackInfo ci) {
        if (!MwccfConfig.betterCombat.enabled) return;
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player == null) return;
        ItemStack stack = mc.player.getHeldItemMainhand();
        WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
        if (attributes == null || attributes.attacks() == null || attributes.attacks().length == 0) return;

        if (BetterCombatClient.isTargetingMineableBlock(mc, mc.player)) {
            BetterCombatClient.isHarvesting = true;
            return;
        }
        BetterCombatClient.isHarvesting = false;

        // Block click if upswing is active or weapon is still in cooldown
        if (BetterCombatClient.isUpswingActive() || BetterCombatClient.attackCooldown > 0 || this.leftClickCounter > 0) {
            this.leftClickCounter = Math.max(this.leftClickCounter, BetterCombatClient.attackCooldown);
            ci.cancel();
            return;
        }

        if (!BetterCombatClient.canStartAttack(mc.player)) {
            this.leftClickCounter = Math.max(this.leftClickCounter, BetterCombatClient.attackCooldown);
            ci.cancel();
            return;
        }

        if (BetterCombatClient.onAttackInput()) {
            this.leftClickCounter = Math.max(1, Math.round(BetterCombatClient.lastSwingDuration));
            ci.cancel();
        } else {
            // Even if onAttackInput returned false, NEVER let vanilla clickMouse run on a Better Combat weapon!
            this.leftClickCounter = Math.max(this.leftClickCounter, BetterCombatClient.attackCooldown);
            ci.cancel();
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"), cancellable = true)
    private void onSendClickBlock(boolean leftClick, CallbackInfo ci) {
        if (!leftClick) {
            if (BetterCombatClient.isUpswingActive() || BetterCombatClient.attackCooldown > 0) {
                this.leftClickCounter = Math.max(this.leftClickCounter, BetterCombatClient.attackCooldown);
                ci.cancel();
            }
            return;
        }
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player == null) return;
        if (MwccfConfig.betterCombat != null && MwccfConfig.betterCombat.enabled) {
            ItemStack stack = mc.player.getHeldItemMainhand();
            if (WeaponRegistry.getAttributes(stack) != null) {
                if (BetterCombatClient.isHarvesting || BetterCombatClient.isTargetingMineableBlock(mc, mc.player)) {
                    BetterCombatClient.isHarvesting = true;
                    return;
                }
                if (BetterCombatClient.isUpswingActive() || BetterCombatClient.attackCooldown > 0) {
                    this.leftClickCounter = Math.max(this.leftClickCounter, BetterCombatClient.attackCooldown);
                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    private void onRightClickMouse(CallbackInfo ci) {
        if (BetterCombatClient.isUpswingActive()) {
            ci.cancel();
        }
    }
}
