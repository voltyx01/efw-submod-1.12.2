package com.voltyx.mwccf.terminal.network;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.geo.BodycamLayer;
import com.voltyx.mwccf.geo.ItemBodycam;
import com.voltyx.mwccf.terminal.bodycam.BodycamEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class PacketRequestBodycamList implements IMessage {

    public PacketRequestBodycamList() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<PacketRequestBodycamList, IMessage> {
        @Override
        public IMessage onMessage(PacketRequestBodycamList message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player == null) return null;

            player.getServerWorld().addScheduledTask(() -> {
                List<BodycamEntry> list = new ArrayList<>();
                net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.common.FMLCommonHandler.instance().getMinecraftServerInstance();
                if (server != null) {
                    for (EntityPlayerMP p : server.getPlayerList().getPlayers()) {
                        ItemStack camStack = BodycamLayer.getEquippedBodycam(p);
                        if (camStack.isEmpty()) {
                            if (p.getHeldItemMainhand().getItem() instanceof ItemBodycam) {
                                camStack = p.getHeldItemMainhand();
                            } else if (p.getHeldItemOffhand().getItem() instanceof ItemBodycam) {
                                camStack = p.getHeldItemOffhand();
                            }
                        }
                        if (!camStack.isEmpty() && camStack.getItem() instanceof ItemBodycam) {
                            String camId = ItemBodycam.getCamId(camStack);
                            boolean enabled = ItemBodycam.isPowerEnabled(camStack);
                            NBTTagCompound tag = camStack.getTagCompound();
                            int charge = (tag != null && tag.hasKey("battery_charge")) ? tag.getInteger("battery_charge") : 0;
                            int percent = Math.min(100, Math.max(0, (int) ((charge / 48000.0f) * 100)));
                            boolean online = enabled && charge > 0;
                            list.add(new BodycamEntry(camId, p.getName(), p.getEntityId(), online, percent));
                        }
                    }
                }
                MwccfMod.PACKET_HANDLER.sendTo(new PacketResponseBodycamList(list), player);
            });
            return null;
        }
    }
}
