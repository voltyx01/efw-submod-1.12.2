package efw.mixin;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.oddities.inventory.ContainerBackpack;

@Pseudo
@Mixin(ContainerBackpack.class)
public abstract class ContainerBackpackMixin extends Container {
    static {
        System.out.println("[EFW-MIXIN-LOAD] ContainerBackpackMixin class loaded!");
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/NonNullList;get(I)Ljava/lang/Object;",
                    remap = false
            ),
            remap = false
    )
    private Object redirectGetArmor(NonNullList<ItemStack> list, int index, EntityPlayer player) {
        if (index == 2) { // CHEST
            ItemStack chest = list.get(index);
            return com.voltyx.mwccf.backpack.BackpackBaubles.getBackpackStack(chest, player);
        }
        return list.get(index);
    }

    @Inject(
            method = {"transferStackInSlot", "func_82846_b"},
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void onTransferStackInSlot(EntityPlayer playerIn, int index, CallbackInfoReturnable<ItemStack> cir) {
        Slot slot = this.inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        ItemStack stack = slot.getStack();
        ItemStack baseStack = stack.copy();
        EntityEquipmentSlot slotType = EntityLiving.getSlotForItemStack(baseStack);
        int equipIndex = 8 - slotType.getIndex();

        if (index == 0) { // Crafting result
            if (!this.mergeItemStack(stack, 9, 45, false) && !this.mergeItemStack(stack, 46, 73, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
            slot.onSlotChange(stack, baseStack);
        } else if (index < 5) { // Crafting matrix 1..4
            if (!this.mergeItemStack(stack, 9, 45, false) && !this.mergeItemStack(stack, 46, 73, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        } else if (index < 9) { // Armor 5..8
            if (!this.mergeItemStack(stack, 9, 45, false) && !this.mergeItemStack(stack, 46, 73, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        } else if (slotType.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !this.inventorySlots.get(equipIndex).getHasStack()) {
            if (!this.mergeItemStack(stack, equipIndex, equipIndex + 1, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        } else if (slotType == EntityEquipmentSlot.OFFHAND && !this.inventorySlots.get(45).getHasStack()) {
            if (!this.mergeItemStack(stack, 45, 46, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        } else if (index < 36) { // Slots 9..35 (Main inventory) -> Backpack (46..73), then Hotbar (36..45)
            boolean moved = this.mergeItemStack(stack, 46, 73, false);
            if (!stack.isEmpty()) {
                moved |= this.mergeItemStack(stack, 36, 45, false);
            }
            if (!moved) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        } else if (index < 46) { // Slots 36..44 (Hotbar) & 45 (Offhand) -> Backpack (46..73), then Main inventory (9..36)
            boolean moved = this.mergeItemStack(stack, 46, 73, false);
            if (!stack.isEmpty()) {
                moved |= this.mergeItemStack(stack, 9, 36, false);
            }
            if (!moved) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        } else if (index < 73) { // Slots 46..72 (Backpack) -> Main inventory (9..36), then Hotbar (36..45)
            boolean moved = this.mergeItemStack(stack, 9, 36, false);
            if (!stack.isEmpty()) {
                moved |= this.mergeItemStack(stack, 36, 45, false);
            }
            if (!moved) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        }

        if (stack.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }

        if (stack.getCount() == baseStack.getCount()) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        ItemStack remainder = slot.onTake(playerIn, stack);
        if (index == 0) {
            playerIn.dropItem(remainder, false);
        }

        com.voltyx.mwccf.backpack.BackpackBaubles.syncBaublesSlot5(playerIn);
        cir.setReturnValue(baseStack);
    }

    @Inject(
            method = {"slotClick", "func_184996_a"},
            at = @At("RETURN"),
            remap = false
    )
    private void onSlotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        com.voltyx.mwccf.backpack.BackpackBaubles.syncBaublesSlot5(player);
    }
}
