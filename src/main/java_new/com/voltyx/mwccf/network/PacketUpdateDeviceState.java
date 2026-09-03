package com.voltyx.mwccf.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateDeviceState implements IMessage {
    private boolean active;
    private float bpm;

    public PacketUpdateDeviceState() {}

    public PacketUpdateDeviceState(boolean active, float bpm) {
        this.active = active;
        this.bpm = bpm;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.active = buf.readBoolean();
        this.bpm = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.active);
        buf.writeFloat(this.bpm);
    }

    public static class Handler implements IMessageHandler<PacketUpdateDeviceState, IMessage> {
        @Override
        public IMessage onMessage(PacketUpdateDeviceState message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                player.getEntityData().setBoolean("bracelet_active", message.active);
                player.getEntityData().setFloat("bracelet_bpm", message.bpm);
            });
            return null;
        }
    }
}
