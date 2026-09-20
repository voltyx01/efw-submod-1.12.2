package efw.mixin;

import efw.util.WeaponSlotRestrictions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public abstract class ContainerSlotRestrictionsMixin {

    @Inject(method = "slotClick", at = @At("HEAD"), cancellable = true)
    private void onSlotClickRestrictions(int slotId, int dragType, ClickType clickTypeIn,
                                         EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        if (slotId < 0) return;

        Container container = (Container) (Object) this;
        if (slotId >= container.inventorySlots.size()) return;

        Slot slot = container.inventorySlots.get(slotId);
        if (slot == null) return;

        // 1. Normal click / pickup with cursor (ClickType.PICKUP)
        if (clickTypeIn == ClickType.PICKUP) {
            ItemStack cursor = player.inventory.getItemStack();
            if (!cursor.isEmpty() && slot.inventory instanceof InventoryPlayer) {
                int targetSlot = slot.getSlotIndex();
                if (WeaponSlotRestrictions.isForbiddenInSlot(cursor, targetSlot)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                    cir.cancel();
                    resync(player, slotId, slot);
                    return;
                }
            }
        }

        // 2. Hotbar swap with number keys 1-9 (ClickType.SWAP, dragType = 0..8)
        else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            ItemStack itemInHovered = slot.getStack();
            ItemStack itemInHotbar = player.inventory.getStackInSlot(dragType);

            // Item in hovered slot would move to hotbar slot dragType
            if (!itemInHovered.isEmpty() && WeaponSlotRestrictions.isForbiddenInSlot(itemInHovered, dragType)) {
                cir.setReturnValue(ItemStack.EMPTY);
                cir.cancel();
                resync(player, slotId, slot);
                return;
            }

            // Item in hotbar slot would move to hovered slot (if it's player inventory)
            if (!itemInHotbar.isEmpty() && slot.inventory instanceof InventoryPlayer) {
                int targetSlot = slot.getSlotIndex();
                if (WeaponSlotRestrictions.isForbiddenInSlot(itemInHotbar, targetSlot)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                    cir.cancel();
                    resync(player, slotId, slot);
                    return;
                }
            }
        }
    }

    private static void resync(EntityPlayer player, int slotId, Slot slot) {
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            EntityPlayerMP mp = (EntityPlayerMP) player;
            mp.connection.sendPacket(new SPacketSetSlot(player.openContainer.windowId, slotId, slot.getStack()));
            mp.connection.sendPacket(new SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
            player.openContainer.detectAndSendChanges();
        }
    }
}
