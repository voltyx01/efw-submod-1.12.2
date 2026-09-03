package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class OpenCustomPlayerInventoryGuiMessage implements IMessage {

    private int guiInventoryId;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        guiInventoryId = byteBuf.readInt();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeInt(guiInventoryId);
    }

    public OpenCustomPlayerInventoryGuiMessage() {}

    public OpenCustomPlayerInventoryGuiMessage(int guiInventoryId) {
        this.guiInventoryId = guiInventoryId;
    }

    public int getGuiInventoryId() {
        return this.guiInventoryId;
    }

}