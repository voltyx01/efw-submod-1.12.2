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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDeleteZoneAt implements IMessage {

    private BlockPos pos;

    public PacketDeleteZoneAt() {}

    public PacketDeleteZoneAt(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
    }

    public static class Handler implements IMessageHandler<PacketDeleteZoneAt, IMessage> {
        @Override
        public IMessage onMessage(PacketDeleteZoneAt message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player == null) return null;

            player.getServerWorld().addScheduledTask(() -> {
                if (!player.capabilities.isCreativeMode && !player.canUseCommand(2, "")) {
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "У вас нет прав для удаления защищенных зон!"));
                    return;
                }

                World world = player.world;
                QuestZoneData data = QuestZoneData.get(world);
                if (data == null) return;

                int dim = world.provider.getDimension();
                QuestZone existing = data.findZoneAt(dim, message.pos);
                if (existing != null) {
                    data.removeZone(existing.getId());
                    QuestZoneEventHandler.syncZonesToAll(world);

                    world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.7F, 1.2F);
                    player.sendMessage(new TextComponentString(
                            TextFormatting.GOLD + "[Зоны] " +
                            TextFormatting.YELLOW + "Зона \"" + existing.getName() + "\" [ID: " + existing.getId() + "] удалена!"
                    ));
                } else {
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "[Зоны] В этой точке нет зарегистрированной зоны."));
                }
            });
            return null;
        }
    }
}
