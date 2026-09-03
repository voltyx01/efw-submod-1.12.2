package com.paneedah.mwc.network.messages;

import com.paneedah.weaponlib.SpreadableExposure;
import com.paneedah.mwc.network.TypeRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class SpreadableExposureMessage implements IMessage {
    
    private SpreadableExposure spreadableExposure;
    private boolean removed;

    public SpreadableExposureMessage(final SpreadableExposure spreadableExposure) {
        this.spreadableExposure = spreadableExposure;
        this.removed = spreadableExposure == null;
    }

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        removed = byteBuf.readBoolean();

        if(!removed)
            spreadableExposure = TypeRegistry.getINSTANCE().fromBytes(byteBuf);
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeBoolean(removed);

        if(!removed)
            TypeRegistry.getINSTANCE().toBytes(spreadableExposure, byteBuf);
    }

    public SpreadableExposureMessage() {}

    public SpreadableExposure getSpreadableExposure() {
        return this.spreadableExposure;
    }

    public boolean isRemoved() {
        return this.removed;
    }

}