package net.bettercombat.network;

import net.bettercombat.client.BetterCombatClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class BetterCombatNetwork {
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("bettercombat");

    public static void init() {
        NETWORK.registerMessage(AttackRequestHandler.class, PacketAttackRequest.class, 0, Side.SERVER);
        NETWORK.registerMessage(AttackAnimationServerHandler.class, PacketAttackAnimation.class, 1, Side.SERVER);
        NETWORK.registerMessage(AttackAnimationClientHandler.class, PacketAttackAnimation.class, 2, Side.CLIENT);
    }

    public static class AttackRequestHandler implements IMessageHandler<PacketAttackRequest, IMessage> {
        @Override
        public IMessage onMessage(PacketAttackRequest message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null) {
                player.getServerWorld().addScheduledTask(() -> {
                    ServerAttackHandler.handleAttack(player, message);
                });
            }
            return null;
        }
    }

    public static class AttackAnimationServerHandler implements IMessageHandler<PacketAttackAnimation, IMessage> {
        @Override
        public IMessage onMessage(PacketAttackAnimation message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null) {
                player.getServerWorld().addScheduledTask(() -> {
                    // Forward animation to all players tracking this player
                    NETWORK.sendToAllTracking(message, player);
                });
            }
            return null;
        }
    }

    public static class AttackAnimationClientHandler implements IMessageHandler<PacketAttackAnimation, IMessage> {
        @Override
        public IMessage onMessage(PacketAttackAnimation message, MessageContext ctx) {
            net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> {
                BetterCombatClient.handleAnimationPacket(message);
            });
            return null;
        }
    }
}
