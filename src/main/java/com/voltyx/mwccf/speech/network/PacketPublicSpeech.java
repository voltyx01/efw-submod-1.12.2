package com.voltyx.mwccf.speech.network;

import com.voltyx.mwccf.speech.client.SpeechClientManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketPublicSpeech implements IMessage {
    private int entityId;
    private String speakerName = "";
    private String message = "";
    private float pitch;

    public PacketPublicSpeech() {}

    public PacketPublicSpeech(int entityId, String speakerName, String message, float pitch) {
        this.entityId = entityId;
        this.speakerName = speakerName;
        this.message = message;
        this.pitch = pitch;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        speakerName = ByteBufUtils.readUTF8String(buf);
        message = ByteBufUtils.readUTF8String(buf);
        pitch = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        ByteBufUtils.writeUTF8String(buf, speakerName);
        ByteBufUtils.writeUTF8String(buf, message);
        buf.writeFloat(pitch);
    }

    public static class Handler implements IMessageHandler<PacketPublicSpeech, IMessage> {
        @Override
        public IMessage onMessage(PacketPublicSpeech packet, MessageContext context) {
            if (context.side == Side.CLIENT) handleClient(packet);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(PacketPublicSpeech packet) {
            Minecraft.getMinecraft().addScheduledTask(() -> SpeechClientManager.receivePublic(
                    packet.entityId, packet.speakerName, packet.message, packet.pitch));
        }
    }
}
