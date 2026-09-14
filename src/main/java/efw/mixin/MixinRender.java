package efw.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public abstract class MixinRender {

    @ModifyVariable(method = "renderShadow", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float mwccf$adjustShadowAlpha(float shadowAlpha, Entity entityIn) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (player.height < 1.0F) {
                // When crawling, prevent 100% opaque shadow by scaling alpha to a smooth, semi-transparent value
                return shadowAlpha * 0.4F;
            }
        }
        return shadowAlpha;
    }

    @Inject(method = "renderShadow", at = @At("HEAD"))
    private void mwccf$fixShadowGLState(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks, CallbackInfo ci) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (player.height < 1.0F) {
                // Ensure proper blend function and alpha state are clean before rendering shadow quads
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(
                        GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.05F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
