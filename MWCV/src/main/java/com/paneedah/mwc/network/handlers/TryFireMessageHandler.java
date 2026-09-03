package com.paneedah.mwc.network.handlers;

import com.paneedah.mwc.network.messages.TryFireMessage;
import com.paneedah.weaponlib.WeaponFireAspect;
import io.redstudioragnarok.redcore.utils.NetworkUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class TryFireMessageHandler implements IMessageHandler<TryFireMessage, IMessage> {

    private WeaponFireAspect fireManager;

    @Override
    public IMessage onMessage(final TryFireMessage tryFireMessage, final MessageContext messageContext) {
        NetworkUtil.processMessage(messageContext, () -> fireManager.serverFire(messageContext.getServerHandler().player, tryFireMessage.isBurst(), tryFireMessage.isAimed()));

        return null;
    }

    public TryFireMessageHandler() {}

    public TryFireMessageHandler(WeaponFireAspect fireManager) {
        this.fireManager = fireManager;
    }

}