package com.paneedah.mwc.network.handlers;

import com.paneedah.mwc.network.messages.GrenadeMessage;
import com.paneedah.weaponlib.grenade.GrenadeAttackAspect;
import com.paneedah.weaponlib.grenade.ItemGrenade;
import io.redstudioragnarok.redcore.utils.NetworkUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class GrenadeMessageHandler implements IMessageHandler<GrenadeMessage, IMessage> {

    private GrenadeAttackAspect grenadeAttackAspect;

    @Override
    public IMessage onMessage(final GrenadeMessage grenadeMessage, final MessageContext messageContext) {
        NetworkUtil.processMessage(messageContext, () -> {
            final EntityPlayer player = messageContext.getServerHandler().player;

            if (player.getHeldItemMainhand().getItem() instanceof ItemGrenade) {
                grenadeMessage.getInstance().setPlayer(player);
                grenadeAttackAspect.serverThrowGrenade(player, grenadeMessage.getInstance(), grenadeMessage.getActivationTimestamp());
            }
        });

        return null;
    }

    public GrenadeMessageHandler() {}

    public GrenadeMessageHandler(GrenadeAttackAspect grenadeAttackAspect) {
        this.grenadeAttackAspect = grenadeAttackAspect;
    }

}