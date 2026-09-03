package efw.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.item.Item;
import vazkii.quark.oddities.item.ItemBackpack;

@Pseudo
@Mixin(Item.class)
public abstract class ItemBackpackMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] ItemBackpackMixin class loaded!");
    }
    @Inject(method = "isValidArmor", at = @At("HEAD"), cancellable = true, remap = false)
    private void onIsValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof ItemBackpack) {
            if (!com.voltyx.mwccf.backpack.BackpackBaubles.hasNoBaubleBackpack(entity)) {
                cir.setReturnValue(false);
            }
        }
    }
}
