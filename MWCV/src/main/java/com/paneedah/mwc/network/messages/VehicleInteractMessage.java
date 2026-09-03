package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class VehicleInteractMessage implements IMessage {

    private boolean right;
    private int vehicleID;
    private int playerID;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        right = byteBuf.readBoolean();
        vehicleID = byteBuf.readInt();
        playerID = byteBuf.readInt();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeBoolean(right);
        byteBuf.writeInt(vehicleID);
        byteBuf.writeInt(playerID);
    }

    public VehicleInteractMessage() {}

    public VehicleInteractMessage(boolean right, int vehicleID, int playerID) {
        this.right = right;
        this.vehicleID = vehicleID;
        this.playerID = playerID;
    }

    public boolean isRight() {
        return this.right;
    }

    public int getVehicleID() {
        return this.vehicleID;
    }

    public int getPlayerID() {
        return this.playerID;
    }

}