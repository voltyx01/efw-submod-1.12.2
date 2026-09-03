package com.voltyx.mwccf.network;

import com.voltyx.mwccf.ModSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HitSoundMessage implements IMessage {

    private int soundType; // 0 = flesh, 1 = head, 2 = kill

    public HitSoundMessage() {
    } // Обязательный пустой конструктор

    public HitSoundMessage(int soundType) {
        this.soundType = soundType;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.soundType = buf.readInt();
    }

    // ИСПРАВЛЕНО: outBytes -> toBytes
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(soundType);
    }

    // Обработчик пакета на стороне клиента
    public static class Handler implements IMessageHandler<HitSoundMessage, IMessage> {

        @Override
        public IMessage onMessage(HitSoundMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                handleClient(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(HitSoundMessage message) {
            // Выполняем строго в главном потоке клиента
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if (efw.biomeinfo.MwccfConfig.combatFeedback.enableHitSounds) {
                    if (message.soundType == 0) {
                        playSound(ModSounds.FLESH_HIT);
                    } else if (message.soundType == 1) {
                        playSound(ModSounds.HEAD_HIT);
                    } else if (message.soundType == 2) {
                        playSound(ModSounds.KILL);
                    }
                }
                if (efw.biomeinfo.MwccfConfig.combatFeedback.enableHitmarkers) {
                    com.voltyx.mwccf.client.HitmarkerRenderer.trigger(message.soundType);
                }
            });
        }

        @SideOnly(Side.CLIENT)
        private void playSound(net.minecraft.util.SoundEvent sound) {
            net.minecraft.client.entity.EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player != null) {
                player.playSound(sound, 1.0F, 1.0F);
            }
        }
    }
}