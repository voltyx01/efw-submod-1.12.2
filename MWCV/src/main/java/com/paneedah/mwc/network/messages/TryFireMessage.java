package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class TryFireMessage implements IMessage {

	private boolean isBurst;
	private boolean isAimed;

	@Override
	public void fromBytes(final ByteBuf byteBuf) {
		isBurst = byteBuf.readBoolean();
		isAimed = byteBuf.readBoolean();
	}

	@Override
	public void toBytes(final ByteBuf byteBuf) {
		byteBuf.writeBoolean(isBurst);
		byteBuf.writeBoolean(isAimed);
	}

    public TryFireMessage() {}

    public TryFireMessage(boolean isBurst, boolean isAimed) {
        this.isBurst = isBurst;
        this.isAimed = isAimed;
    }

    public boolean isBurst() {
        return this.isBurst;
    }

    public boolean isAimed() {
        return this.isAimed;
    }

}