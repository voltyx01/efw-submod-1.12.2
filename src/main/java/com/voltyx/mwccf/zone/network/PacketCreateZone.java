package com.voltyx.mwccf.zone.network;

import com.voltyx.mwccf.zone.QuestZone;
import com.voltyx.mwccf.zone.QuestZoneData;
import com.voltyx.mwccf.zone.QuestZoneEventHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketCreateZone implements IMessage {

    private String name;
    private BlockPos pos1;
    private BlockPos pos2;

    public PacketCreateZone() {}

    public PacketCreateZone(String name, BlockPos pos1, BlockPos pos2) {
        this.name = name != null ? name : "Комната";
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.name = ByteBufUtils.readUTF8String(buf);
        this.pos1 = BlockPos.fromLong(buf.readLong());
        this.pos2 = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, name != null ? name : "Комната");
        buf.writeLong(pos1.toLong());
        buf.writeLong(pos2.toLong());
    }

    public static class Handler implements IMessageHandler<PacketCreateZone, IMessage> {
        @Override
        public IMessage onMessage(PacketCreateZone message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player == null) return null;

            player.getServerWorld().addScheduledTask(() -> {
                // Проверяем права: только в креативе или с OP
                if (!player.capabilities.isCreativeMode && !player.canUseCommand(2, "")) {
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "У вас нет прав для создания защищенных зон!"));
                    return;
                }

                World world = player.world;
                QuestZoneData data = QuestZoneData.get(world);
                if (data == null) return;

                int dim = world.provider.getDimension();
                String id = UUID.randomUUID().toString().substring(0, 8);
                QuestZone zone = new QuestZone(id, message.name, dim, message.pos1, message.pos2);
                data.addZone(zone);

                QuestZoneEventHandler.syncZonesToAll(world);

                world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 0.7F, 1.2F);
                player.sendMessage(new TextComponentString(
                        TextFormatting.GOLD + "[Зоны] " +
                        TextFormatting.GREEN + "Зона \"" + message.name + "\" [ID: " + id + "] успешно создана! Размеры: " +
                        (zone.getMaxX() - zone.getMinX() + 1) + "x" +
                        (zone.getMaxY() - zone.getMinY() + 1) + "x" +
                        (zone.getMaxZ() - zone.getMinZ() + 1)
                ));
            });
            return null;
        }
    }
}
