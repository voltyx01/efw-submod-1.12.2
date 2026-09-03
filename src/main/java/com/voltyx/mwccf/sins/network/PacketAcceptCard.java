package com.voltyx.mwccf.sins.network;

import com.voltyx.mwccf.sins.ActiveModifier;
import com.voltyx.mwccf.sins.SinCard;
import com.voltyx.mwccf.sins.capability.ISinCapability;
import com.voltyx.mwccf.sins.capability.SinCapabilityEvents;
import com.voltyx.mwccf.sins.capability.SinCapabilityProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAcceptCard implements IMessage {
    private SinCard selectedCard;

    public PacketAcceptCard() {}

    public PacketAcceptCard(SinCard selectedCard) {
        this.selectedCard = selectedCard;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        if (tag != null) {
            this.selectedCard = SinCard.deserializeNBT(tag);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = this.selectedCard != null ? this.selectedCard.serializeNBT() : new NBTTagCompound();
        ByteBufUtils.writeTag(buf, tag);
    }

    public static class Handler implements IMessageHandler<PacketAcceptCard, IMessage> {
        @Override
        public IMessage onMessage(PacketAcceptCard message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null && message.selectedCard != null) {
                player.getServerWorld().addScheduledTask(() -> {
                    ISinCapability cap = player.getCapability(SinCapabilityProvider.SIN_CAP, null);
                    if (cap != null && cap.getChosenSin() != null) {
                        
                        // СЕРВЕРНАЯ ВАЛИДАЦИЯ РАСХОДНИКА:
                        // Ищем артефакт/расходник для левел-апа в инвентаре игрока
                        boolean hasConsumable = consumeLevelUpArtifact(player);
                        
                        if (hasConsumable || player.isCreative()) {
                            // Применяем выбранные бафф и дебафф
                            if (message.selectedCard.getBuff() != null) {
                                cap.addModifier(message.selectedCard.getBuff());
                            }
                            if (message.selectedCard.getDebuff() != null) {
                                cap.addModifier(message.selectedCard.getDebuff());
                            }
                            
                            // Повышаем уровень греха
                            cap.setSinLevel(cap.getSinLevel() + 1);
                            
                            // Синхронизируем обновленные данные клиенту
                            SinCapabilityEvents.syncToPlayer(player);
                        }
                    }
                });
            }
            return null;
        }

        private boolean consumeLevelUpArtifact(EntityPlayerMP player) {
            // В качестве расходника используется "Осквернённая страница откровения" (DPOR)
            for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                ItemStack stack = player.inventory.mainInventory.get(i);
                if (!stack.isEmpty()) {
                    if (stack.getItem() == efw.init.EfwModItems.DPOR) {
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            player.inventory.mainInventory.set(i, ItemStack.EMPTY);
                        }
                        player.inventoryContainer.detectAndSendChanges();
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
