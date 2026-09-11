package efw.mixin;

import net.minecraft.client.shader.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Framebuffer.class)
public abstract class MixinFramebuffer {

    @Shadow
    public float[] framebufferColor;

    @Shadow
    public abstract void setFramebufferColor(float red, float green, float blue, float alpha);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(int width, int height, boolean useDepthIn, CallbackInfo ci) {
        this.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Inject(method = "framebufferClear", at = @At("HEAD"))
    private void onFramebufferClear(CallbackInfo ci) {
        if (this.framebufferColor != null && this.framebufferColor.length >= 3) {
            // Vanilla defaults framebufferColor to (1.0F, 1.0F, 1.0F, 0.0F), which causes bright white flash
            if (this.framebufferColor[0] == 1.0F && this.framebufferColor[1] == 1.0F && this.framebufferColor[2] == 1.0F) {
                this.framebufferColor[0] = 0.0F;
                this.framebufferColor[1] = 0.0F;
                this.framebufferColor[2] = 0.0F;
                this.framebufferColor[3] = 0.0F;
            }
        }
    }

    @Inject(method = "enableStencil", at = @At("HEAD"), remap = false)
    private void onEnableStencil(CallbackInfoReturnable<Boolean> cir) {
        this.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
    }
}
