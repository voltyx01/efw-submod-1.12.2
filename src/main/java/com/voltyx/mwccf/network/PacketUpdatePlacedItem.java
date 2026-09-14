package com.voltyx.mwccf.network;

import com.voltyx.mwccf.furniture.tileentity.TileEntityPlacedItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdatePlacedItem implements IMessage {

    private BlockPos pos;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float yaw;
    private float pitch;
    private float roll;
    private float scale;
    private boolean locked;
    private float iconOffsetX;
    private float iconOffsetY;
    private float iconOffsetZ;

    public PacketUpdatePlacedItem() {
    }

    public PacketUpdatePlacedItem(BlockPos pos, float offsetX, float offsetY, float offsetZ,
                                  float yaw, float pitch, float roll, float scale, boolean locked,
                                  float iconOffsetX, float iconOffsetY, float iconOffsetZ) {
        this.pos = pos;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.scale = scale;
        this.locked = locked;
        this.iconOffsetX = iconOffsetX;
        this.iconOffsetY = iconOffsetY;
        this.iconOffsetZ = iconOffsetZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.offsetX = buf.readFloat();
        this.offsetY = buf.readFloat();
        this.offsetZ = buf.readFloat();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.roll = buf.readFloat();
        this.scale = buf.readFloat();
        this.locked = buf.readBoolean();
        if (buf.isReadable()) {
            this.iconOffsetX = buf.readFloat();
            this.iconOffsetY = buf.readFloat();
            this.iconOffsetZ = buf.readFloat();
        } else {
            this.iconOffsetX = 0.0f;
            this.iconOffsetY = 0.0f;
            this.iconOffsetZ = 0.0f;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeFloat(this.offsetX);
        buf.writeFloat(this.offsetY);
        buf.writeFloat(this.offsetZ);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeFloat(this.roll);
        buf.writeFloat(this.scale);
        buf.writeBoolean(this.locked);
        buf.writeFloat(this.iconOffsetX);
        buf.writeFloat(this.iconOffsetY);
        buf.writeFloat(this.iconOffsetZ);
    }

    public static class Handler implements IMessageHandler<PacketUpdatePlacedItem, IMessage> {
        @Override
        public IMessage onMessage(PacketUpdatePlacedItem message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (player.world.isBlockLoaded(message.pos)) {
                    TileEntity te = player.world.getTileEntity(message.pos);
                    if (te instanceof TileEntityPlacedItem) {
                        TileEntityPlacedItem placed = (TileEntityPlacedItem) te;
                        placed.setOffsetX(message.offsetX);
                        placed.setOffsetY(message.offsetY);
                        placed.setOffsetZ(message.offsetZ);
                        placed.setRotationYaw(message.yaw);
                        placed.setRotationPitch(message.pitch);
                        placed.setRotationRoll(message.roll);
                        placed.setScale(message.scale);
                        placed.setLocked(message.locked);
                        placed.setIconOffsetX(message.iconOffsetX);
                        placed.setIconOffsetY(message.iconOffsetY);
                        placed.setIconOffsetZ(message.iconOffsetZ);
                        placed.markDirty();

                        IBlockState state = player.world.getBlockState(message.pos);
                        player.world.notifyBlockUpdate(message.pos, state, state, 3);
                    }
                }
            });
            return null;
        }
    }
}
