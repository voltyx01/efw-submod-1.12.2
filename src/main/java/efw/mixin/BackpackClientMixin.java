package efw.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.quark.oddities.item.ItemBackpack;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.item.IItemPropertyGetter;
import com.voltyx.mwccf.backpack.AssaultBackpack;

@Pseudo
@Mixin(ItemBackpack.class)
public abstract class BackpackClientMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] BackpackClientMixin class loaded!");
    }

    private static AssaultBackpack assaultBackpackModel;

    @Inject(method = "getArmorModel", at = @At("HEAD"), cancellable = true, remap = false)
    public void onGetArmorModel(net.minecraft.entity.EntityLivingBase entityLiving, ItemStack itemStack, net.minecraft.inventory.EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<net.minecraft.client.model.ModelBiped> cir) {
        if (entityLiving != null) {
            boolean hasSurvivalInstinctChest = false;
            ItemStack stack = entityLiving.getItemStackFromSlot(net.minecraft.inventory.EntityEquipmentSlot.CHEST);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof com.voltyx.mwccf.mcore.ItemCustomArmor) {
                    if (!"mwccf:fire_fighter_chestplate".equals(stack.getItem().getRegistryName().toString())) {
                        hasSurvivalInstinctChest = true;
                    }
                }
            }
            if (hasSurvivalInstinctChest) {
                net.minecraft.client.model.ModelBiped emptyModel = new net.minecraft.client.model.ModelBiped();
                emptyModel.bipedBody.showModel = false;
                emptyModel.bipedHead.showModel = false;
                emptyModel.bipedHeadwear.showModel = false;
                emptyModel.bipedRightArm.showModel = false;
                emptyModel.bipedLeftArm.showModel = false;
                emptyModel.bipedRightLeg.showModel = false;
                emptyModel.bipedLeftLeg.showModel = false;
                cir.setReturnValue(emptyModel);
                return;
            }
        }
        if (assaultBackpackModel == null) {
            assaultBackpackModel = new AssaultBackpack();
        }
        cir.setReturnValue(assaultBackpackModel);
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/quark/oddities/item/ItemBackpack;addPropertyOverride(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/item/IItemPropertyGetter;)V"
            )
    )
    private void redirectAddProperty(ItemBackpack instance, ResourceLocation name, IItemPropertyGetter getter) {
        if ("has_items".equals(name.getPath())) {
            ((net.minecraft.item.Item) instance).addPropertyOverride(name, (stack, world, entity) ->
                    ItemBackpack.doesBackpackHaveItems(stack) ? 1.0F : 0.0F
            );
        } else {
            ((net.minecraft.item.Item) instance).addPropertyOverride(name, getter);
        }
    }
}