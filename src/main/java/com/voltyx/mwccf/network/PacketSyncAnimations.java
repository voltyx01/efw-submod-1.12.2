package com.voltyx.mwccf.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.voltyx.mwccf.MwccfMod;

public class PacketSyncAnimations implements IMessage {
    public int entityId;
    public String baseAnim;
    public String actionAnim;
    public float actionSpeed;
    public float dashYaw;

    public PacketSyncAnimations() {}

    public PacketSyncAnimations(int entityId, String baseAnim, String actionAnim, float actionSpeed, float dashYaw) {
        this.entityId = entityId;
        this.baseAnim = baseAnim == null ? "" : baseAnim;
        this.actionAnim = actionAnim == null ? "" : actionAnim;
        this.actionSpeed = actionSpeed;
        this.dashYaw = dashYaw;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.baseAnim = ByteBufUtils.readUTF8String(buf);
        this.actionAnim = ByteBufUtils.readUTF8String(buf);
        this.actionSpeed = buf.readFloat();
        this.dashYaw = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        ByteBufUtils.writeUTF8String(buf, baseAnim);
        ByteBufUtils.writeUTF8String(buf, actionAnim);
        buf.writeFloat(actionSpeed);
        buf.writeFloat(dashYaw);
    }

    public static class Handler implements IMessageHandler<PacketSyncAnimations, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncAnimations message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                // Server received from client: broadcast to all tracking clients
                net.minecraft.entity.player.EntityPlayerMP sender = ctx.getServerHandler().player;
                sender.getServerWorld().addScheduledTask(() -> {
                    PacketSyncAnimations forwardPacket = new PacketSyncAnimations(sender.getEntityId(), message.baseAnim, message.actionAnim, message.actionSpeed, message.dashYaw);
                    MwccfMod.PACKET_HANDLER.sendToAllTracking(forwardPacket, sender);
                });
            } else {
                // Client received from server: store it for rendering
                handleClient(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(PacketSyncAnimations message) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
                if (entity instanceof EntityPlayer && entity != Minecraft.getMinecraft().player) {
                    efw.AnimationTickHandler.updateNetworkState((EntityPlayer) entity, 
                        message.baseAnim.isEmpty() ? null : message.baseAnim, 
                        message.actionAnim.isEmpty() ? null : message.actionAnim, 
                        message.actionSpeed,
                        message.dashYaw);
                }
            });
        }
    }
}
