package com.voltyx.mwccf.fireweapon.network;

import com.voltyx.mwccf.fireweapon.smolder.SmolderingClientManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSmolderingSync implements IMessage {

    private int entityId;
    private int durationTicks;
    private float markU;
    private float markV;
    private boolean soaked;
    private int markSeed;

    public PacketSmolderingSync() {}

    public PacketSmolderingSync(int entityId, int durationTicks) {
        this(entityId, durationTicks, 0.4375F, 0.375F, false, entityId);
    }

    public PacketSmolderingSync(int entityId, int durationTicks, float markU, float markV, boolean soaked, int markSeed) {
        this.entityId = entityId;
        this.durationTicks = durationTicks;
        this.markU = markU;
        this.markV = markV;
        this.soaked = soaked;
        this.markSeed = markSeed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.durationTicks = buf.readInt();
        this.markU = buf.readFloat();
        this.markV = buf.readFloat();
        this.soaked = buf.readBoolean();
        this.markSeed = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.durationTicks);
        buf.writeFloat(this.markU);
        buf.writeFloat(this.markV);
        buf.writeBoolean(this.soaked);
        buf.writeInt(this.markSeed);
    }

    public static class Handler implements IMessageHandler<PacketSmolderingSync, IMessage> {
        @Override
        public IMessage onMessage(PacketSmolderingSync msg, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                handleClient(msg);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(PacketSmolderingSync msg) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                SmolderingClientManager.applySmolder(msg.entityId, msg.durationTicks,
                    msg.markU, msg.markV, msg.soaked, msg.markSeed);
            });
        }
    }
}
