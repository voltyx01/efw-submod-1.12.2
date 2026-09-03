package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class MuzzleFlashMessage implements IMessage {

    private int entityID;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        entityID = byteBuf.readInt();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeInt(entityID);
    }

    public MuzzleFlashMessage() {}

    public MuzzleFlashMessage(int entityID) {
        this.entityID = entityID;
    }

    public int getEntityID() {
        return this.entityID;
    }

}