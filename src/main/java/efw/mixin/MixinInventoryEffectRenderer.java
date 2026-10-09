package efw.mixin;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinInventoryEffectRenderer extends GuiContainer {

    public MixinInventoryEffectRenderer(net.minecraft.inventory.Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Shadow
    protected boolean hasActivePotionEffects;

    @Shadow
    protected abstract void drawActivePotionEffects();

    /**
     * Draw potion effects BEFORE super.drawScreen (which draws the inventory background and slots).
     * This ensures the potion plaques (drawn at z=0) are painted underneath the inventory background,
     * so when they slide right towards guiLeft, the inventory neatly masks/covers them.
     */
    @Inject(
        method = "drawScreen",
        at = @At("HEAD")
    )
    private void speech$renderPotionEffectsUnderBackground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.hasActivePotionEffects) {
            this.drawActivePotionEffects();
        }
    }

    /**
     * Suppress the vanilla drawActivePotionEffects call in drawScreen, which executes after
     * super.drawScreen (on top of the inventory).
     */
    @Redirect(
        method = "drawScreen",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/InventoryEffectRenderer;drawActivePotionEffects()V"
        )
    )
    private void speech$cancelLatePotionEffectDraw(InventoryEffectRenderer renderer) {
        // No-op: already drawn under background in HEAD
    }

    /**
     * Dynamically shifts potion effect plaques to the right (towards guiLeft) under the inventory
     * when the speech panel in the inventory is opening/open, and smoothly returns them to their
     * vanilla position (guiLeft - 124) when closed.
     */
    @ModifyConstant(
        method = "drawActivePotionEffects",
        constant = @Constant(intValue = 124)
    )
    private int speech$shiftPotionEffectsRight(int originalConstant) {
        float progress = com.voltyx.mwccf.speech.client.SpeechPanelController.getInventoryOpenProgress();
        if (progress <= 0.0F) {
            return 124;
        }
        // Smooth easing: easeOutQuad
        float eased = 1.0F - (1.0F - progress) * (1.0F - progress);
        // At progress 0: delta = 0 -> returns 124 (i = guiLeft - 124)
        // At progress 1: delta = 135 -> returns -11 (i = guiLeft + 11, completely tucked inside/under inventory)
        int delta = Math.round(135.0F * eased);
        return 124 - delta;
    }
}


