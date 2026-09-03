package com.paneedah.mwc.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class OpenDoorMessage implements IMessage {

    private BlockPos position;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        position = BlockPos.fromLong(byteBuf.readLong());
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeLong(position.toLong());
    }

    public OpenDoorMessage() {}

    public OpenDoorMessage(BlockPos position) {
        this.position = position;
    }

    public BlockPos getPosition() {
        return this.position;
    }

}