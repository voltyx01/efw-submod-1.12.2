package com.voltyx.mwccf.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncStamina implements IMessage {

    private double stamina;
    private int regenDelay;

    public PacketSyncStamina() {}

    public PacketSyncStamina(double stamina, int regenDelay) {
        this.stamina = stamina;
        this.regenDelay = regenDelay;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(stamina);
        buf.writeInt(regenDelay);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stamina = buf.readDouble();
        regenDelay = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketSyncStamina, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncStamina message, MessageContext ctx) {
            if (ctx.side == net.minecraftforge.fml.relauncher.Side.CLIENT) {
                handleClient(message);
            }
            return null;
        }

        @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
        private void handleClient(PacketSyncStamina message) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                EntityPlayer player = Minecraft.getMinecraft().player;
                if (player != null) {
                    player.getEntityData().setDouble("stamina", message.stamina);
                    player.getEntityData().setInteger("stamina_regen_delay", message.regenDelay);
                }
            });
        }
    }
}
