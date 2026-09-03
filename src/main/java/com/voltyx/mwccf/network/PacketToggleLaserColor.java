package com.voltyx.mwccf.network;

import com.paneedah.weaponlib.CustomRenderer;
import com.paneedah.weaponlib.ItemAttachment;
import com.paneedah.weaponlib.LaserBeamRenderer;
import com.paneedah.weaponlib.Tags;
import com.paneedah.weaponlib.Weapon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketToggleLaserColor implements IMessage {
    private int slotId;

    public PacketToggleLaserColor() {}

    public PacketToggleLaserColor(int slotId) {
        this.slotId = slotId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slotId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slotId);
    }

    public static class Handler implements IMessageHandler<PacketToggleLaserColor, IMessage> {
        @Override
        public IMessage onMessage(PacketToggleLaserColor message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                ItemStack stack = ItemStack.EMPTY;
                Slot targetSlot = null;

                if (message.slotId == -1) {
                    stack = player.getHeldItemMainhand();
                } else if (player.openContainer != null && message.slotId >= 0 && message.slotId < player.openContainer.inventorySlots.size()) {
                    targetSlot = player.openContainer.getSlot(message.slotId);
                    stack = targetSlot.getStack();
                }

                if (!stack.isEmpty() && (stack.getItem() instanceof Weapon || stack.getItem() instanceof ItemAttachment)) {
                    toggleColor(stack);
                    if (targetSlot != null) {
                        targetSlot.onSlotChanged();
                    }
                    player.openContainer.detectAndSendChanges();
                }
            });
            return null;
        }

        public static void toggleColor(ItemStack stack) {
            LaserBeamRenderer lbr = getLaserRenderer(stack);
            if (lbr == null) return;

            boolean isGreenDefault = lbr.isGreenDefault();
            int current = Tags.hasLaserColor(stack) ? Tags.getLaserColor(stack) : (isGreenDefault ? 2 : 1);
            // 1: RED, 2: GREEN
            int next = (current == 1) ? 2 : 1;
            Tags.setLaserColor(stack, next);
        }

        public static LaserBeamRenderer getLaserRenderer(ItemStack stack) {
            if (stack == null || stack.isEmpty()) return null;
            if (stack.getItem() instanceof ItemAttachment) {
                ItemAttachment<?> att = (ItemAttachment<?>) stack.getItem();
                CustomRenderer<?> post = att.getPostRenderer();
                if (post instanceof LaserBeamRenderer) {
                    return (LaserBeamRenderer) post;
                }
            }
            return null;
        }
    }
}
