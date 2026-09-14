package com.voltyx.mwccf.antenna.network;

import com.voltyx.mwccf.antenna.TileEntityAntenna;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCloseAntenna implements IMessage {

    private BlockPos pos;

    public PacketCloseAntenna() {
    }

    public PacketCloseAntenna(BlockPos pos) {
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

    public static class Handler implements IMessageHandler<PacketCloseAntenna, IMessage> {
        @Override
        public IMessage onMessage(PacketCloseAntenna message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null && message.pos != null) {
                player.getServerWorld().addScheduledTask(() -> {
                    TileEntity te = player.getServerWorld().getTileEntity(message.pos);
                    if (te instanceof TileEntityAntenna) {
                        ((TileEntityAntenna) te).close();
                    }
                });
            }
            return null;
        }
    }
}
