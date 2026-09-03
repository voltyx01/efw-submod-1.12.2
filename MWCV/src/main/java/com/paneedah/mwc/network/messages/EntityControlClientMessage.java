package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import static com.paneedah.mwc.proxies.ClientProxy.MC;

public class EntityControlClientMessage implements IMessage {

    private Entity entity;
    private int flags;
    private int values;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        entity = MC.player.world.getEntityByID(byteBuf.readInt());
        flags = byteBuf.readInt();
        values = byteBuf.readInt();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeInt(entity.getEntityId());
        byteBuf.writeInt(flags);
        byteBuf.writeInt(values);
    }

    public EntityControlClientMessage() {}

    public EntityControlClientMessage(Entity entity, int flags, int values) {
        this.entity = entity;
        this.flags = flags;
        this.values = values;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public int getFlags() {
        return this.flags;
    }

    public int getValues() {
        return this.values;
    }

}