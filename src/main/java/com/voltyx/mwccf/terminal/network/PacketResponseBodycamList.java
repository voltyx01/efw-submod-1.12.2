package com.voltyx.mwccf.terminal.network;

import com.voltyx.mwccf.terminal.bodycam.BodycamEntry;
import com.voltyx.mwccf.terminal.client.TerminalSession;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class PacketResponseBodycamList implements IMessage {

    private List<BodycamEntry> cameras = new ArrayList<>();

    public PacketResponseBodycamList() {}

    public PacketResponseBodycamList(List<BodycamEntry> cameras) {
        this.cameras = cameras;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int count = buf.readInt();
        this.cameras = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            this.cameras.add(BodycamEntry.fromBytes(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.cameras.size());
        for (BodycamEntry entry : this.cameras) {
            entry.toBytes(buf);
        }
    }

    public static class Handler implements IMessageHandler<PacketResponseBodycamList, IMessage> {
        @Override
        public IMessage onMessage(PacketResponseBodycamList message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TerminalSession.getInstance().setAvailableCameras(message.cameras);
            });
            return null;
        }
    }
}
