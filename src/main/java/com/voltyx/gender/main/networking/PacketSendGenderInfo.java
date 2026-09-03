package com.voltyx.gender.main.networking;

import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.WildfireGender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendGenderInfo extends PacketGenderInfo {

    // В 1.12.2 ОБЯЗАТЕЛЬНО нужен пустой конструктор для рефлексии!
    // Иначе Forge не сможет создать пакет при получении.
    public PacketSendGenderInfo() {
        super();
    }

    public PacketSendGenderInfo(GenderPlayer plr) {
        super(plr);
    }

    // Обработчик пакета выносится во вложенный класс
    public static class Handler implements IMessageHandler<PacketSendGenderInfo, IMessage> {

        @Override
        public IMessage onMessage(final PacketSendGenderInfo message, final MessageContext ctx) {
            // Получаем игрока, который прислал пакет (на стороне сервера)
            final EntityPlayerMP player = ctx.getServerHandler().player;

            // В 1.12.2 пакеты обрабатываются в сетевом потоке.
            // Нам нужно передать выполнение в основной поток сервера!
            ((WorldServer) player.world).addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    if (player == null || !player.getUniqueID().equals(message.uuid)) {
                        // Проверяем, что UUID совпадает с отправителем
                        return;
                    }

                    GenderPlayer plr = WildfireGender.getOrAddPlayerById(message.uuid);
                    message.updatePlayerFromPacket(plr);

                    // Синхронизируем изменения с другими игроками
                    PacketSync.sendToOthers(player, plr);
                }
            });

            // Возвращаем null, так как нам не нужно отправлять пакет в ответ
            return null;
        }
    }

    // Метод для отправки на сервер с клиента
    public static void send(GenderPlayer plr) {
        if (plr == null || !plr.needsSync) return;

        // WildfireGender.NETWORK - это должен быть экземпляр SimpleNetworkWrapper
        WildfireGender.NETWORK.sendToServer(new PacketSendGenderInfo(plr));
        plr.needsSync = false;
    }
}