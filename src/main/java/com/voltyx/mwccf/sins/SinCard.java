package com.voltyx.mwccf.sins;

import net.minecraft.nbt.NBTTagCompound;

public class SinCard {
    private int cardIndex;
    private ActiveModifier buff;
    private ActiveModifier debuff;

    public SinCard() {}

    public SinCard(int cardIndex, ActiveModifier buff, ActiveModifier debuff) {
        this.cardIndex = cardIndex;
        this.buff = buff;
        this.debuff = debuff;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public ActiveModifier getBuff() {
        return buff;
    }

    public ActiveModifier getDebuff() {
        return debuff;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("index", cardIndex);
        if (buff != null) {
            tag.setTag("buff", buff.serializeNBT());
        }
        if (debuff != null) {
            tag.setTag("debuff", debuff.serializeNBT());
        }
        return tag;
    }

    public static SinCard deserializeNBT(NBTTagCompound tag) {
        SinCard card = new SinCard();
        card.cardIndex = tag.getInteger("index");
        if (tag.hasKey("buff")) {
            card.buff = ActiveModifier.deserializeNBT(tag.getCompoundTag("buff"));
        }
        if (tag.hasKey("debuff")) {
            card.debuff = ActiveModifier.deserializeNBT(tag.getCompoundTag("debuff"));
        }
        return card;
    }
}
