package com.voltyx.mwccf.terminal.network;

import com.voltyx.mwccf.terminal.TileEntityTerminal;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCloseTerminal implements IMessage {

    private BlockPos pos;

    public PacketCloseTerminal() {
    }

    public PacketCloseTerminal(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
    }

    public static class Handler implements IMessageHandler<PacketCloseTerminal, IMessage> {
        @Override
        public IMessage onMessage(PacketCloseTerminal message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null && message.pos != null) {
                player.getServerWorld().addScheduledTask(() -> {
                    TileEntity te = player.getServerWorld().getTileEntity(message.pos);
                    if (te instanceof TileEntityTerminal) {
                        ((TileEntityTerminal) te).close();
                    }
                });
            }
            return null;
        }
    }
}
