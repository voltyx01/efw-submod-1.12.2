package com.voltyx.mwccf.zone.network;

import com.voltyx.mwccf.zone.QuestZone;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncZones implements IMessage {

    private List<QuestZone> zones;

    public PacketSyncZones() {
        this.zones = new ArrayList<>();
    }

    public PacketSyncZones(List<QuestZone> zones) {
        this.zones = zones != null ? zones : new ArrayList<>();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int count = buf.readInt();
        zones = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String id = ByteBufUtils.readUTF8String(buf);
            String name = ByteBufUtils.readUTF8String(buf);
            int dim = buf.readInt();
            int minX = buf.readInt();
            int minY = buf.readInt();
            int minZ = buf.readInt();
            int maxX = buf.readInt();
            int maxY = buf.readInt();
            int maxZ = buf.readInt();
            zones.add(new QuestZone(id, name, dim, minX, minY, minZ, maxX, maxY, maxZ));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(zones.size());
        for (QuestZone z : zones) {
            ByteBufUtils.writeUTF8String(buf, z.getId());
            ByteBufUtils.writeUTF8String(buf, z.getName());
            buf.writeInt(z.getDimension());
            buf.writeInt(z.getMinX());
            buf.writeInt(z.getMinY());
            buf.writeInt(z.getMinZ());
            buf.writeInt(z.getMaxX());
            buf.writeInt(z.getMaxY());
            buf.writeInt(z.getMaxZ());
        }
    }

    public static class Handler implements IMessageHandler<PacketSyncZones, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncZones message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                handleClient(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(PacketSyncZones message) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                com.voltyx.mwccf.zone.client.ClientZoneCache.setZones(message.zones);
            });
        }
    }
}
