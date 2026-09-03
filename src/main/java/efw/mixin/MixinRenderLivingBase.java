package efw.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;

@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase {
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
}
