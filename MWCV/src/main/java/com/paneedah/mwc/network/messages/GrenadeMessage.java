package com.paneedah.mwc.network.messages;

import com.paneedah.weaponlib.grenade.PlayerGrenadeInstance;
import com.paneedah.mwc.network.TypeRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class GrenadeMessage implements IMessage {

    private PlayerGrenadeInstance instance;
    private long activationTimestamp;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        this.instance = TypeRegistry.getINSTANCE().fromBytes(byteBuf);
        this.activationTimestamp = byteBuf.readLong();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        TypeRegistry.getINSTANCE().toBytes(instance, byteBuf);
        byteBuf.writeLong(activationTimestamp);
    }

    public GrenadeMessage() {}

    public GrenadeMessage(long activationTimestamp) {
        this.activationTimestamp = activationTimestamp;
    }

    public GrenadeMessage(PlayerGrenadeInstance instance, long activationTimestamp) {
        this.instance = instance;
        this.activationTimestamp = activationTimestamp;
    }

    public PlayerGrenadeInstance getInstance() {
        return this.instance;
    }

    public long getActivationTimestamp() {
        return this.activationTimestamp;
    }

}