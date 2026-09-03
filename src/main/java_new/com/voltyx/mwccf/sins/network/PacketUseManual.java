package com.voltyx.mwccf.sins.network;

import com.voltyx.mwccf.sins.capability.ISinCapability;
import com.voltyx.mwccf.sins.capability.SinCapabilityEvents;
import com.voltyx.mwccf.sins.capability.SinCapabilityProvider;
import efw.init.EfwModItems;
import efw.item.ManualItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Отправляется клиентом при нажатии X на предмете-руководстве
 * ({@link ManualItem}) в инвентаре. Сервер сам находит подходящий предмет
 * в инвентаре игрока (по категории), проверяет, что прогресс ещё не
 * максимальный, списывает 1 штуку и повышает соответствующую категорию в
 * {@code ISinCapability#getLoreBooksProgress()}.
 * <p>
 * Категория намеренно не привязывается к конкретному слоту — клиент лишь
 * подсказывает, какую категорию руководства он использовал, а сервер
 * валидирует и списывает предмет самостоятельно (защита от читерства).
 */
public class PacketUseManual implements IMessage {

    public static final int MAX_PROGRESS = 4;

    private int categoryIndex;

    public PacketUseManual() {}

    public PacketUseManual(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.categoryIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.categoryIndex);
    }

    public static class Handler implements IMessageHandler<PacketUseManual, IMessage> {
        @Override
        public IMessage onMessage(PacketUseManual message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null) {
                int categoryIndex = message.categoryIndex;
                player.getServerWorld().addScheduledTask(() -> {
                    ISinCapability cap = player.getCapability(SinCapabilityProvider.SIN_CAP, null);
                    if (cap == null) return;

                    ManualItem expectedItem = EfwModItems.getManualForCategory(categoryIndex);
                    if (expectedItem == null) return;

                    int[] progress = cap.getLoreBooksProgress();
                    int current = (categoryIndex >= 0 && categoryIndex < progress.length) ? progress[categoryIndex] : 0;
                    if (current >= MAX_PROGRESS && !player.isCreative()) {
                        return; // уже прокачано до максимума — не тратим предмет
                    }

                    // Ищем сам предмет руководства нужной категории в инвентаре игрока
                    for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                        ItemStack stack = player.inventory.mainInventory.get(i);
                        if (!stack.isEmpty() && stack.getItem() == expectedItem) {
                            if (!player.isCreative()) {
                                stack.shrink(1);
                                if (stack.isEmpty()) {
                                    player.inventory.mainInventory.set(i, ItemStack.EMPTY);
                                }
                                player.inventoryContainer.detectAndSendChanges();
                            }

                            cap.setLoreBookProgress(categoryIndex, current + 1);
                            SinCapabilityEvents.syncToPlayer(player);

                            player.playSound(efw.init.EfwModSounds.NOTES, 1.0f, 0.9f);
                            return;
                        }
                    }
                });
            }
            return null;
        }
    }
}
