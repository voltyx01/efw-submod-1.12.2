package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class EntityPickupMessage implements IMessage {

	private int playerID;
	private int entityID;

	@Override
	public void fromBytes(final ByteBuf byteBuf) {
		playerID = byteBuf.readInt();
        entityID = byteBuf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf byteBuf) {
		byteBuf.writeInt(playerID);
        byteBuf.writeInt(entityID);
	}

    public EntityPickupMessage() {}

    public EntityPickupMessage(int playerID, int entityID) {
        this.playerID = playerID;
        this.entityID = entityID;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public int getEntityID() {
        return this.entityID;
    }

}