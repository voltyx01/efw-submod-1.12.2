package com.voltyx.mwccf.network;

import com.voltyx.mwccf.dash.DashCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncDashCooldown implements IMessage {

    private int cooldown;

    public PacketSyncDashCooldown() {}

    public PacketSyncDashCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(cooldown);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        cooldown = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketSyncDashCooldown, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncDashCooldown message, MessageContext ctx) {
            if (ctx.side == net.minecraftforge.fml.relauncher.Side.CLIENT) {
                handleClient(message);
            }
            return null;
        }

        @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
        private void handleClient(PacketSyncDashCooldown message) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if (Minecraft.getMinecraft().player != null) {
                    DashCapability.IDashData cap = Minecraft.getMinecraft().player.getCapability(DashCapability.ROLL_CAP, null);
                    if (cap != null) {
                        cap.setCooldown(message.cooldown);
                    }
                }
            });
        }
    }
}
