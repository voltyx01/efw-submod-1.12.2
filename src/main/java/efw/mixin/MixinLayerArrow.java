package efw.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArrow.class)
public class MixinLayerArrow {

    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinLayerArrow class loaded!");
    }

    /**
     * In vanilla LayerArrow.doRenderLayer, RenderHelper.disableStandardItemLighting() is called.
     * RenderArrow already disables lighting internally, so this call is redundant.
     */
    @Redirect(
        method = "doRenderLayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"
        )
    )
    private void efw$suppressDisableStandardItemLighting() {
        // No-op
    }

    /**
     * In vanilla LayerArrow.doRenderLayer, RenderHelper.enableStandardItemLighting() is called
     * at the end of arrow rendering while the modelview matrix is still transformed by the entity's
     * position, rotation, and scale!
     * 
     * Because glLight multiplies light positions (GL_LIGHT0, GL_LIGHT1) by the current MODELVIEW matrix,
     * this permanently transforms standard lighting by the entity's pose, leaving the player
     * model and all subsequent entities and layers pitch dark or incorrectly shaded until the next world pass.
     */
    @Redirect(
        method = "doRenderLayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderHelper;enableStandardItemLighting()V"
        )
    )
    private void efw$suppressEnableStandardItemLighting() {
        // No-op: Prevent corrupting GL_LIGHT0 and GL_LIGHT1 coordinates
    }

    /**
     * RenderArrow.doRender calls GlStateManager.disableRescaleNormal() and leaves a non-unit
     * normal (0.0F, 0.0F, 0.05625F).
     * We restore rescale normal and a clean unit normal immediately after each arrow is rendered.
     */
    @Redirect(
        method = "doRenderLayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V"
        )
    )
    private void efw$renderArrowSafe(RenderManager rm, Entity entity, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_) {
        rm.renderEntity(entity, x, y, z, yaw, partialTicks, p_188391_10_);
        GlStateManager.enableRescaleNormal();
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
    }

    @Inject(method = "doRenderLayer", at = @At("RETURN"))
    private void efw$onDoRenderLayerReturn(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
    }
}
