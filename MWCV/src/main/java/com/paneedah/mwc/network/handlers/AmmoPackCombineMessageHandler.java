package com.paneedah.mwc.network.handlers;

import com.paneedah.mwc.items.equipment.ItemAmmoPack;
import com.paneedah.mwc.network.messages.AmmoPackCombineMessage;
import com.paneedah.weaponlib.ItemBullet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AmmoPackCombineMessageHandler implements IMessageHandler<AmmoPackCombineMessage, IMessage> {

    @Override
    public IMessage onMessage(AmmoPackCombineMessage message, MessageContext ctx) {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
            EntityPlayerMP player = ctx.getServerHandler().player;
            Container container = player.openContainer;
            if (container == null) {
                return;
            }

            int slotId = message.getSlotId();
            if (slotId < 0 || slotId >= container.inventorySlots.size()) {
                return;
            }

            Slot slot = container.inventorySlots.get(slotId);
            if (slot == null || !slot.getHasStack()) {
                return;
            }

            ItemStack slotStack = slot.getStack();
            ItemStack cursorStack = player.inventory.getItemStack();

            if (!cursorStack.isEmpty() && slotStack.getItem() instanceof ItemAmmoPack && cursorStack.getItem() instanceof ItemAmmoPack) {
                ItemBullet slotBullet = ItemAmmoPack.getBullet(slotStack);
                ItemBullet cursorBullet = ItemAmmoPack.getBullet(cursorStack);

                if (slotBullet != null && slotBullet == cursorBullet) {
                    int slotAmmo = ItemAmmoPack.getAmmo(slotStack);
                    int cursorAmmo = ItemAmmoPack.getAmmo(cursorStack);

                    int totalAmmo = slotAmmo + cursorAmmo;
                    int capacity = 50; // Hardcoded max in ItemAmmoPack

                    if (totalAmmo > capacity) {
                        ItemAmmoPack.setAmmo(slotStack, capacity);
                        ItemAmmoPack.setAmmo(cursorStack, totalAmmo - capacity);
                    } else {
                        ItemAmmoPack.setAmmo(slotStack, totalAmmo);
                        player.inventory.setItemStack(ItemStack.EMPTY);
                    }

                    slot.onSlotChanged();
                    player.updateHeldItem();
                    player.openContainer.detectAndSendChanges();
                }
            }
        });
        return null;
    }

    public AmmoPackCombineMessageHandler() {}

}