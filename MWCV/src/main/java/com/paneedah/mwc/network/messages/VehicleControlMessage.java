package com.paneedah.mwc.network.messages;

import com.paneedah.weaponlib.vehicle.network.VehicleDataContainer;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class VehicleControlMessage implements IMessage {

    private VehicleDataContainer serializer;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        serializer = VehicleDataContainer.read(byteBuf);
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        serializer.write(byteBuf, serializer);
    }

    public VehicleControlMessage() {}

    public VehicleControlMessage(VehicleDataContainer serializer) {
        this.serializer = serializer;
    }

    public VehicleDataContainer getSerializer() {
        return this.serializer;
    }

}