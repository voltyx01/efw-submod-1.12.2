package com.voltyx.mwccf.doll.network;

import com.voltyx.mwccf.doll.SayaDollManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from Client to Server when player equips Saya's doll while blood is max.
 */
public class PacketDollActivate implements IMessage {

    public PacketDollActivate() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<PacketDollActivate, IMessage> {
        @Override
        public IMessage onMessage(PacketDollActivate message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null) {
                player.getServerWorld().addScheduledTask(() -> {
                    SayaDollManager.handleClientActivationRequest(player);
                });
            }
            return null;
        }
    }
}
