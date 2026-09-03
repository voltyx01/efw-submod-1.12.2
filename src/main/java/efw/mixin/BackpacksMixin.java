package efw.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.oddities.feature.Backpacks;
import vazkii.quark.oddities.item.ItemBackpack;

@Pseudo
@Mixin(Backpacks.class)
public abstract class BackpacksMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] BackpacksMixin class loaded!");
    }
    @Inject(method = "isEntityWearingBackpack(Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onIsEntityWearingBackpack(Entity e, CallbackInfoReturnable<Boolean> cir) {
        if (e instanceof net.minecraft.entity.player.EntityPlayer) {
            ItemStack chestArmor = ((EntityLivingBase)e).getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            ItemStack stack = com.voltyx.mwccf.backpack.BackpackBaubles.getBackpackStack(chestArmor, (EntityLivingBase)e);
            if (stack.getItem() instanceof ItemBackpack) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "isEntityWearingBackpack(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onIsEntityWearingBackpackStack(Entity e, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (e instanceof net.minecraft.entity.player.EntityPlayer) {
            ItemStack chestArmor = ((EntityLivingBase)e).getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            ItemStack stack = com.voltyx.mwccf.backpack.BackpackBaubles.getBackpackStack(chestArmor, (EntityLivingBase)e);
            if (stack == itemStack) {
                cir.setReturnValue(true);
            }
        }
    }
}
