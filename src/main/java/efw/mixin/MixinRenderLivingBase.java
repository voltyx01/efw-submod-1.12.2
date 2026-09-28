package efw.mixin;

import efw.animation.firstperson.FirstPersonMode;
import java.util.List;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;

@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase {
    @Shadow
    protected List<LayerRenderer<?>> layerRenderers;

    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinRenderLivingBase class loaded!");
    }

    @Redirect(method = "setBrightness(Lnet/minecraft/entity/EntityLivingBase;FZ)Z",
              at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;hurtTime:I"))
    private int redirectHurtTime(EntityLivingBase entity) {
        if (efw.biomeinfo.MwccfConfig.visuals.enableNoHurtFlash) {
            return 0;
        }
        return entity.hurtTime;
    }

    @Redirect(method = "setBrightness(Lnet/minecraft/entity/EntityLivingBase;FZ)Z",
              at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;deathTime:I"))
    private int redirectDeathTime(EntityLivingBase entity) {
        if (efw.biomeinfo.MwccfConfig.visuals.enableNoHurtFlash) {
            return 0;
        }
        return entity.deathTime;
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "doRender", at = @At("HEAD"))
    private void onDoRender(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
    }

    @Redirect(method = "renderLayers(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;layerRenderers:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<LayerRenderer<?>> efw$filterFirstPersonLayers(RenderLivingBase<?> renderer) {
        if (renderer instanceof net.minecraft.client.renderer.entity.RenderPlayer && FirstPersonMode.isFirstPersonPass()) {
            return this.layerRenderers.stream()
                    .filter(layer -> layer instanceof LayerHeldItem)
                    .collect(java.util.stream.Collectors.toList());
        }
        return this.layerRenderers;
    }
}
