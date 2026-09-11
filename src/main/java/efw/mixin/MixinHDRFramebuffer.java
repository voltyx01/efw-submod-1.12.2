package efw.mixin;

import com.paneedah.weaponlib.render.HDRFramebuffer;
import net.minecraft.client.shader.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HDRFramebuffer.class, remap = false)
public abstract class MixinHDRFramebuffer extends Framebuffer {

    public MixinHDRFramebuffer(int width, int height, boolean useDepthIn) {
        super(width, height, useDepthIn);
    }

    @Inject(method = "createFramebuffer", at = @At("HEAD"), remap = false)
    private void onCreateFramebuffer(int width, int height, CallbackInfo ci) {
        if (this.framebufferColor != null && this.framebufferColor.length >= 4) {
            this.framebufferColor[0] = 0.0F;
            this.framebufferColor[1] = 0.0F;
            this.framebufferColor[2] = 0.0F;
            this.framebufferColor[3] = 0.0F;
        }
    }

    @Override
    public boolean isStencilEnabled() {
        return true;
    }

    @Override
    public synchronized boolean enableStencil() {
        return false;
    }
}
