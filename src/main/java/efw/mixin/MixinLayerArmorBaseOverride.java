package efw.mixin;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBaseOverride {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinLayerArmorBaseOverride class loaded!");
    }

    @Inject(method = "getArmorModelHook", at = @At("RETURN"), cancellable = true, remap = false)
    public void onGetArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, ModelBase model, CallbackInfoReturnable<ModelBase> cir) {
        ModelBase returnedModel = cir.getReturnValue();
        if (returnedModel != null && returnedModel.getClass() == ModelBiped.class) {
            if (entity instanceof net.minecraft.client.entity.AbstractClientPlayer) {
                if ("slim".equals(((net.minecraft.client.entity.AbstractClientPlayer)entity).getSkinType())) {
                    ModelBase slimModel = slot == EntityEquipmentSlot.LEGS 
                        ? com.voltyx.mwccf.client.model.ModelBipedSlimArmor.INSTANCE_LEGGINGS 
                        : com.voltyx.mwccf.client.model.ModelBipedSlimArmor.INSTANCE_ARMOR;
                    cir.setReturnValue(slimModel);
                }
            }
        }
    }
}
