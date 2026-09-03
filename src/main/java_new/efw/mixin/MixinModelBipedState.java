package efw.mixin;

import com.voltyx.mwccf.client.model.SlimArmorStateManager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBipedState {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinModelBipedState class loaded!");
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void onRenderHead(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        SlimArmorStateManager.CURRENT_MODEL.set((ModelBiped) (Object) this);
    }
}
