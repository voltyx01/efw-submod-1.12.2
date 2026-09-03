package efw.mixin;

import com.voltyx.mwccf.client.model.SlimArmorStateManager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelRenderer.class)
public class MixinModelRendererSlim {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinModelRendererSlim class loaded!");
    }

    private boolean pushedMatrixForSlim = false;

    @Inject(method = "render", at = @At("HEAD"))
    public void onRenderHead(float scale, CallbackInfo ci) {
        ModelBiped model = SlimArmorStateManager.CURRENT_MODEL.get();
        Entity entity = SlimArmorStateManager.CURRENT_ENTITY.get();
        
        if (model != null && entity instanceof net.minecraft.client.entity.AbstractClientPlayer) {
            if (!(model instanceof net.minecraft.client.model.ModelPlayer) && 
                !(model.getClass().getName().contains("GeoArmorModel")) && 
                !(model instanceof com.voltyx.mwccf.client.model.ModelBipedSlimArmor)) {
                boolean isSlim = "slim".equals(((net.minecraft.client.entity.AbstractClientPlayer)entity).getSkinType());
                if (isSlim) {
                    if ((Object)this == model.bipedRightArm) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(-0.0625F, 0.0F, 0.0F); // 1 pixel left (away from body to correct scale origin)
                        GlStateManager.scale(0.75F, 1.0F, 1.0F); // 4px -> 3px
                        this.pushedMatrixForSlim = true;
                    } else if ((Object)this == model.bipedLeftArm) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(0.0625F, 0.0F, 0.0F); // 1 pixel right (away from body to correct scale origin)
                        GlStateManager.scale(0.75F, 1.0F, 1.0F); // 4px -> 3px
                        this.pushedMatrixForSlim = true;
                    }
                }
            }
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void onRenderReturn(float scale, CallbackInfo ci) {
        if (this.pushedMatrixForSlim) {
            GlStateManager.popMatrix();
            this.pushedMatrixForSlim = false;
        }
    }
}
