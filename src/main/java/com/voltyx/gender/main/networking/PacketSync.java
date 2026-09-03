package com.voltyx.gender.main.networking;

import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.WildfireGender;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.UUID;

public class PacketSync extends PacketGenderInfo {

    // Обязательный пустой конструктор для 1.12.2
    public PacketSync() {
        super();
    }

    public PacketSync(GenderPlayer plr) {
        super(plr);
    }

    // Обработчик пакета (выполняется на КЛИЕНТЕ)
    public static class Handler implements IMessageHandler<PacketSync, IMessage> {

        @Override
        public IMessage onMessage(final PacketSync message, final MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                handleClient(message);
            }
            return null; // Ответный пакет не нужен
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(final PacketSync message) {
            // Передаем выполнение в основной поток клиента
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer clientPlayer = Minecraft.getMinecraft().player;

                    // Убеждаемся, что игрок загружен и пакет не от нас самих
                    if (clientPlayer != null && !message.uuid.equals(clientPlayer.getUniqueID())) {
                        GenderPlayer plr = WildfireGender.getOrAddPlayerById(message.uuid);
                        message.updatePlayerFromPacket(plr);
                        plr.syncStatus = GenderPlayer.SyncStatus.SYNCED;
                        plr.lockSettings = true; // Блокируем настройки, так как это чужой профиль
                    }
                }
            });
        }
    }

    // --- Методы отправки пакетов (выполняются на СЕРВЕРЕ) ---

    /**
     * Рассылает данные игрока всем остальным на сервере
     */
    public static void sendToOthers(EntityPlayerMP sender, GenderPlayer genderPlayer) {
        if (genderPlayer != null && sender.getServer() != null) {
            PacketSync syncPacket = new PacketSync(genderPlayer);

            // Проходим по всем игрокам на сервере
            for (EntityPlayerMP serverPlayer : sender.getServer().getPlayerList().getPlayers()) {
                // Отправляем всем, кроме самого отправителя
                if (!sender.getUniqueID().equals(serverPlayer.getUniqueID())) {
                    WildfireGender.NETWORK.sendTo(syncPacket, serverPlayer);
                }
            }
        }
    }

    /**
     * Отправляет данные ВСЕХ загруженных игроков конкретному игроку 
     * (используется, когда кто-то только что зашел на сервер)
     */
    public static void sendTo(EntityPlayerMP targetPlayer) {
        for (Map.Entry<UUID, GenderPlayer> entry : WildfireGender.CLOTHING_PLAYERS.entrySet()) {
            UUID uuid = entry.getKey();

            // Не отправляем игроку его же собственные данные (он их и так знает)
            if (!targetPlayer.getUniqueID().equals(uuid)) {
                WildfireGender.NETWORK.sendTo(new PacketSync(entry.getValue()), targetPlayer);
            }
        }
    }
}