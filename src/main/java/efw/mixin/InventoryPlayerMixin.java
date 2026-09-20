package efw.mixin;

import efw.util.WeaponSlotRestrictions;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class InventoryPlayerMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] InventoryPlayerMixin class loaded!");
    }

    @Shadow
    public IInventory inventory;

    @Shadow
    public int slotIndex;

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void onIsItemValid(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!(this.inventory instanceof InventoryPlayer)) {
            return;
        }

        if (WeaponSlotRestrictions.isForbiddenInSlot(stack, this.slotIndex)) {
            cir.setReturnValue(false);
        }
    }
}
