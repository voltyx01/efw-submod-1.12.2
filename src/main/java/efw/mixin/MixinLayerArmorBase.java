package efw.mixin;

import com.voltyx.mwccf.client.model.SlimArmorStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinLayerArmorBase class loaded!");
    }

    @Inject(method = "renderArmorLayer", at = @At("HEAD"))
    public void onRenderArmorLayerHead(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn, CallbackInfo ci) {
        SlimArmorStateManager.CURRENT_ENTITY.set(entityLivingBaseIn);
    }

    @Inject(method = "renderArmorLayer", at = @At("RETURN"))
    public void onRenderArmorLayerReturn(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn, CallbackInfo ci) {
        SlimArmorStateManager.CURRENT_ENTITY.remove();
        SlimArmorStateManager.CURRENT_MODEL.remove();
    }
}
