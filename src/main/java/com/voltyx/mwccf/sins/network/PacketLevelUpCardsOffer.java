package com.voltyx.mwccf.sins.network;

import com.voltyx.mwccf.sins.SinCard;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class PacketLevelUpCardsOffer implements IMessage {
    private List<SinCard> cards = new ArrayList<>();

    public PacketLevelUpCardsOffer() {}

    public PacketLevelUpCardsOffer(List<SinCard> cards) {
        if (cards != null) {
            this.cards = cards;
        }
    }

    public List<SinCard> getCards() {
        return cards;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        cards.clear();
        if (tag != null && tag.hasKey("cards", Constants.NBT.TAG_LIST)) {
            NBTTagList list = tag.getTagList("cards", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.tagCount(); i++) {
                cards.add(SinCard.deserializeNBT(list.getCompoundTagAt(i)));
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (SinCard card : cards) {
            list.appendTag(card.serializeNBT());
        }
        tag.setTag("cards", list);
        ByteBufUtils.writeTag(buf, tag);
    }

    public static class Handler implements IMessageHandler<PacketLevelUpCardsOffer, IMessage> {
        @Override
        public IMessage onMessage(PacketLevelUpCardsOffer message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    handleClient(message);
                });
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(PacketLevelUpCardsOffer message) {
            if (Minecraft.getMinecraft().currentScreen instanceof com.voltyx.mwccf.sins.client.GuiSevenScreen) {
                ((com.voltyx.mwccf.sins.client.GuiSevenScreen) Minecraft.getMinecraft().currentScreen).receiveLevelUpCards(message.getCards());
            }
        }
    }
}
