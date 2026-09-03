package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import io.redstudioragnarok.redcore.vectors.Vector3F;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class SpawnParticleMessage implements IMessage {

    public enum ParticleType {
        SMOKE_GRENADE_SMOKE,
        SMOKE_GRENADE_YELLOW_SMOKE;
    }

    private ParticleType type;
    private int count;
    private Vector3F position = new Vector3F();
    private Vector3F velocity = new Vector3F();

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        type = ParticleType.values()[byteBuf.readInt()];
        count = byteBuf.readInt();
        position.read(byteBuf);
        velocity.read(byteBuf);
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeInt(type.ordinal());
        byteBuf.writeInt(count);
        position.write(byteBuf);
        velocity.write(byteBuf);
    }

    public SpawnParticleMessage() {}

    public SpawnParticleMessage(ParticleType type, int count, Vector3F position, Vector3F velocity) {
        this.type = type;
        this.count = count;
        this.position = position;
        this.velocity = velocity;
    }

    public ParticleType getType() {
        return this.type;
    }

    public int getCount() {
        return this.count;
    }

    public Vector3F getPosition() {
        return this.position;
    }

    public Vector3F getVelocity() {
        return this.velocity;
    }

}