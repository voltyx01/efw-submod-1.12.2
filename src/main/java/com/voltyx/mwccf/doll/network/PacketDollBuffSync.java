package com.voltyx.mwccf.doll.network;

import com.voltyx.mwccf.doll.SayaDollManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Sent from Server to Client to synchronize doll buff duration and state.
 */
public class PacketDollBuffSync implements IMessage {

    private int entityId;
    private int buffTicks;

    public PacketDollBuffSync() {}

    public PacketDollBuffSync(int entityId, int buffTicks) {
        this.entityId = entityId;
        this.buffTicks = buffTicks;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.buffTicks = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.buffTicks);
    }

    public static class Handler implements IMessageHandler<PacketDollBuffSync, IMessage> {
        @Override
        public IMessage onMessage(PacketDollBuffSync message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                handleClient(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleClient(PacketDollBuffSync message) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                SayaDollManager.handleClientBuffSync(message.entityId, message.buffTicks);
            });
        }
    }
}
