package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import io.redstudioragnarok.redcore.vectors.Vector3D;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class ExplosionMessage implements IMessage {

    private Vector3D velocity = new Vector3D();
    private float strength;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        velocity.read(byteBuf);
        strength = byteBuf.readFloat();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        velocity.write(byteBuf);
        byteBuf.writeFloat(strength);
    }

    public ExplosionMessage() {}

    public ExplosionMessage(Vector3D velocity, float strength) {
        this.velocity = velocity;
        this.strength = strength;
    }

    public Vector3D getVelocity() {
        return this.velocity;
    }

    public float getStrength() {
        return this.strength;
    }

}