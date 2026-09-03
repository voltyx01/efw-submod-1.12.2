package com.voltyx.mwccf.sins.network;

import com.voltyx.mwccf.sins.capability.ISinCapability;
import com.voltyx.mwccf.sins.capability.SinCapability;
import com.voltyx.mwccf.sins.capability.SinCapabilityProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncSinData implements IMessage {
    private NBTTagCompound nbt;

    public PacketSyncSinData() {}

    public PacketSyncSinData(NBTTagCompound nbt) {
        this.nbt = nbt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.nbt != null ? this.nbt : new NBTTagCompound());
    }

    public static class Handler implements IMessageHandler<PacketSyncSinData, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncSinData message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    handleClient(message);
                });
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(PacketSyncSinData message) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player != null && message.nbt != null) {
                ISinCapability cap = player.getCapability(SinCapabilityProvider.SIN_CAP, null);
                if (cap != null) {
                    cap.readFromNBT(message.nbt);
                }
            }
        }
    }
}
