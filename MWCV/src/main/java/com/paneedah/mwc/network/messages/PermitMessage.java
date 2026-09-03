package com.paneedah.mwc.network.messages;

import com.paneedah.weaponlib.PlayerItemInstance;
import com.paneedah.mwc.network.TypeRegistry;
import com.paneedah.weaponlib.state.Permit;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class PermitMessage implements IMessage {

    private Permit<?> permit;
    private PlayerItemInstance<?> playerItemInstance;
    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        final TypeRegistry typeRegistry = TypeRegistry.getINSTANCE();

        playerItemInstance = typeRegistry.fromBytes(byteBuf);
        permit = typeRegistry.fromBytes(byteBuf);
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        final TypeRegistry typeRegistry = TypeRegistry.getINSTANCE();

        typeRegistry.toBytes(playerItemInstance, byteBuf);
        typeRegistry.toBytes(permit, byteBuf);
    }

    public PermitMessage() {}

    public PermitMessage(Permit<?> permit, PlayerItemInstance<?> playerItemInstance) {
        this.permit = permit;
        this.playerItemInstance = playerItemInstance;
    }

    public Permit<?> getPermit() {
        return permit;
    }

    public PlayerItemInstance<?> getPlayerItemInstance() {
        return playerItemInstance;
    }

}