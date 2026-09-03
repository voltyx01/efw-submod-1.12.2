package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class CraftingServerMessage implements IMessage {

    private int playerId;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        playerId = byteBuf.readInt();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeInt(playerId);
    }

    public CraftingServerMessage() {}

    public CraftingServerMessage(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return this.playerId;
    }

}