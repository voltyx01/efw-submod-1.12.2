package com.voltyx.mwccf.geo;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.voltyx.mwccf.MwccfMod;

public class HeadlampNetwork {

    public static void sendTogglePacket() {
        MwccfMod.PACKET_HANDLER.sendToServer(new PacketToggleHeadlamp());
    }

    public static class PacketToggleHeadlamp implements IMessage {
        public PacketToggleHeadlamp() {}

        @Override
        public void fromBytes(ByteBuf buf) {}

        @Override
        public void toBytes(ByteBuf buf) {}
    }

    public static class Handler implements IMessageHandler<PacketToggleHeadlamp, IMessage> {
        @Override
        public IMessage onMessage(PacketToggleHeadlamp message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (!net.minecraftforge.fml.common.Loader.isModLoaded("baubles")) return;
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                if (handler != null) {
                    ItemStack bauble = handler.getStackInSlot(4);
                    if (!bauble.isEmpty() && bauble.getItem() instanceof ItemHeadlamp) {
                        NBTTagCompound tag = bauble.hasTagCompound() ? bauble.getTagCompound() : new NBTTagCompound();
                        boolean current = tag.getBoolean("active");
                        if (!current) { // Turning on
                            if (tag.hasKey("battery_charge") && tag.getInteger("battery_charge") > 0) {
                                tag.setBoolean("active", true);
                            } else {
                                // Can't turn on without battery
                            }
                        } else { // Turning off
                            tag.setBoolean("active", false);
                        }
                        bauble.setTagCompound(tag);
                        handler.setStackInSlot(4, bauble);
                        
                        // Let Baubles handle the sync if possible, but to be sure we could send a manual sync
                        // but usually setChanged(slot) triggers sync to tracking players in Baubles.
                    }
                }
            });
            return null;
        }
    }
}
