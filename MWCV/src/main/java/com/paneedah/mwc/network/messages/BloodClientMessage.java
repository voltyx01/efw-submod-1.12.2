package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import io.redstudioragnarok.redcore.vectors.Vector3F;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class BloodClientMessage implements IMessage {

    private Vector3F position = new Vector3F();
    private Vector3F velocity = new Vector3F();

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        position.read(byteBuf);
        velocity.read(byteBuf);
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        position.write(byteBuf);
        velocity.write(byteBuf);
    }

    public BloodClientMessage() {}

    public BloodClientMessage(Vector3F position, Vector3F velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Vector3F getPosition() {
        return this.position;
    }

    public Vector3F getVelocity() {
        return this.velocity;
    }

}