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

public class PacketPersonalSpeech implements IMessage {
    private String translationKey = "";
    private String[] arguments = new String[0];
    private float pitch = 1.0F;

    public PacketPersonalSpeech() {}

    public PacketPersonalSpeech(String translationKey, String[] arguments, float pitch) {
        this.translationKey = translationKey;
        this.arguments = arguments;
        this.pitch = pitch;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        translationKey = ByteBufUtils.readUTF8String(buf);
        int count = Math.max(0, Math.min(8, buf.readInt()));
        arguments = new String[count];
        for (int i = 0; i < count; i++) arguments[i] = ByteBufUtils.readUTF8String(buf);
        pitch = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, translationKey);
        int count = Math.min(8, arguments.length);
        buf.writeInt(count);
        for (int i = 0; i < count; i++) ByteBufUtils.writeUTF8String(buf, arguments[i]);
        buf.writeFloat(pitch);
    }

    public static class Handler implements IMessageHandler<PacketPersonalSpeech, IMessage> {
        @Override
        public IMessage onMessage(PacketPersonalSpeech packet, MessageContext context) {
            if (context.side == Side.CLIENT) handleClient(packet);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(PacketPersonalSpeech packet) {
            Minecraft.getMinecraft().addScheduledTask(() -> SpeechClientManager.receivePersonal(
                    packet.translationKey, packet.arguments, packet.pitch));
        }
    }
}
