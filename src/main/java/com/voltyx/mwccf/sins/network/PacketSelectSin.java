package com.voltyx.mwccf.sins.network;

import com.voltyx.mwccf.sins.SinType;
import com.voltyx.mwccf.sins.capability.ISinCapability;
import com.voltyx.mwccf.sins.capability.SinCapabilityEvents;
import com.voltyx.mwccf.sins.capability.SinCapabilityProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSelectSin implements IMessage {
    private String sinId;

    public PacketSelectSin() {}

    public PacketSelectSin(SinType sin) {
        this.sinId = sin != null ? sin.getId() : "";
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.sinId = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.sinId != null ? this.sinId : "");
    }

    public static class Handler implements IMessageHandler<PacketSelectSin, IMessage> {
        @Override
        public IMessage onMessage(PacketSelectSin message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null) {
                player.getServerWorld().addScheduledTask(() -> {
                    ISinCapability cap = player.getCapability(SinCapabilityProvider.SIN_CAP, null);
                    if (cap != null) {
                        SinType sin = SinType.byId(message.sinId);
                        if (sin != null) {
                            // Сервер присваивает грех и выдает стартовые параметры
                            cap.setChosenSin(sin);
                            SinCapabilityEvents.syncToPlayer(player);
                        }
                    }
                });
            }
            return null;
        }
    }
}
