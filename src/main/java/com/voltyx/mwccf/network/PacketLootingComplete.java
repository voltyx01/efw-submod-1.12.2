package com.voltyx.mwccf.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketLootingComplete implements IMessage {
    private BlockPos pos;

    public PacketLootingComplete() {
    }

    public PacketLootingComplete(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
    }

    public static class Handler implements IMessageHandler<PacketLootingComplete, IMessage> {
        @Override
        public IMessage onMessage(PacketLootingComplete message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {

                // Проверяем, что игрок не читер и стоит рядом с блоком
                if (player.getDistanceSqToCenter(message.pos) > 64.0)
                    return;

                TileEntity tile = player.world.getTileEntity(message.pos);
                if (tile != null) {
                    // Разблокируем навсегда
                    tile.getTileData().setBoolean("LootUnlocked", true);
                    tile.markDirty();

                    // Имитируем клик игрока, чтобы открыть блок по-настоящему
                    IBlockState state = player.world.getBlockState(message.pos);
                    state.getBlock().onBlockActivated(
                            player.world, message.pos, state, player,
                            EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);
                }
            });
            return null;
        }
    }
}