package efw.mixin;

import com.teamderpy.shouldersurfing.client.CameraEntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class MixinGlStateManager {

    /**
     * Overrides alpha on GlStateManager.color(float, float, float, float).
     * Since GlStateManager.color(float, float, float) calls color(r, g, b, 1.0F),
     * this automatically intercepts ALL color calls.
     */
    @ModifyVariable(method = "color(FFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 3)
    private static float efw$overrideAlpha(float alpha) {
        float override = CameraEntityRenderer.getInstance().getCurrentAlphaOverride();
        if (override < 1.0F) {
            return alpha * override;
        }
        return alpha;
    }

    /**
     * Prevents disabling GL blend while transparency override is active
     * (ensures armor layers, baubles, gender breast layers, held items, doll, and guns stay transparent).
     */
    @Inject(method = "disableBlend()V", at = @At("HEAD"), cancellable = true)
    private static void efw$preventDisableBlend(CallbackInfo ci) {
        if (CameraEntityRenderer.getInstance().getCurrentAlphaOverride() < 1.0F) {
            ci.cancel();
        }
    }
}
