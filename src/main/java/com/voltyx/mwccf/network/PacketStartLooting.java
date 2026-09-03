package com.voltyx.mwccf.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.voltyx.mwccf.client.ClientLootingManager;

public class PacketStartLooting implements IMessage {
    private BlockPos pos;

    public PacketStartLooting() {
    }

    public PacketStartLooting(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
    }

    public static class Handler implements IMessageHandler<PacketStartLooting, IMessage> {
        @Override
        public IMessage onMessage(PacketStartLooting message, MessageContext ctx) {
            if (ctx.side == net.minecraftforge.fml.relauncher.Side.CLIENT) {
                handleClient(message);
            }
            return null;
        }

        @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
        private void handleClient(PacketStartLooting message) {
            net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> {
                ClientLootingManager.startLooting(message.pos);
            });
        }
    }
}