package com.voltyx.mwccf.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.voltyx.mwccf.geo.ItemHeadlamp;
import com.voltyx.mwccf.geo.ItemBracelet;
import com.voltyx.mwccf.mcore.MCoreItems;

public class PacketChargeDevice implements IMessage {
    private int slotId;

    public PacketChargeDevice() {}

    public PacketChargeDevice(int slotId) {
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

    public static class Handler implements IMessageHandler<PacketChargeDevice, IMessage> {
        @Override
        public IMessage onMessage(PacketChargeDevice message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (player.openContainer != null && message.slotId >= 0 && message.slotId < player.openContainer.inventorySlots.size()) {
                    Slot slot = player.openContainer.getSlot(message.slotId);
                    ItemStack target = slot.getStack();
                    ItemStack held = player.inventory.getItemStack(); // What is held by the mouse

                    if (!held.isEmpty() && held.getItem() == MCoreItems.BATTERY) {
                        if (!target.isEmpty() && (target.getItem() instanceof ItemHeadlamp || target.getItem() instanceof ItemBracelet || target.getItem() instanceof com.voltyx.mwccf.geo.ItemPortableMap)) {
                            NBTTagCompound tag = target.getTagCompound();
                            int currentCharge = (tag != null && tag.hasKey("battery_charge")) ? tag.getInteger("battery_charge") : 0;
                            // 50% of 48000 is 24000
                            if (currentCharge <= 24000) {
                                if (tag == null) {
                                    tag = new NBTTagCompound();
                                    target.setTagCompound(tag);
                                }
                                tag.setInteger("battery_charge", 48000);
                                
                                // Consume 1 battery
                                held.shrink(1);
                                player.inventory.setItemStack(held.isEmpty() ? ItemStack.EMPTY : held);
                                
                                // Update the client
                                player.updateHeldItem();
                            }
                        }
                    } else if (!held.isEmpty() && held.getItem() == com.voltyx.mwccf.item.ItemMorphineSyringe.INSTANCE) {
                        if (!target.isEmpty() && target.getItem() instanceof ItemBracelet) {
                            NBTTagCompound tag = target.getTagCompound();
                            int morphineCount = (tag != null && tag.hasKey("morphine_count")) ? tag.getInteger("morphine_count") : 0;
                            if (morphineCount < 6) {
                                if (tag == null) {
                                    tag = new NBTTagCompound();
                                    target.setTagCompound(tag);
                                }
                                tag.setInteger("morphine_count", morphineCount + 1);

                                held.shrink(1);
                                player.inventory.setItemStack(held.isEmpty() ? ItemStack.EMPTY : held);
                                player.updateHeldItem();
                            }
                        }
                    }
                }
            });
            return null;
        }
    }
}
