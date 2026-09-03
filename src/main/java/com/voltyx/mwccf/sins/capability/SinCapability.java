package com.voltyx.mwccf.sins.capability;

import com.voltyx.mwccf.sins.ActiveModifier;
import com.voltyx.mwccf.sins.SinType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class SinCapability implements ISinCapability {
    private SinType chosenSin = null;
    private int sinLevel = 1;
    private final List<ActiveModifier> activeModifiers = new ArrayList<>();
    private final int[] loreBooks = new int[5]; // 0: Melee, 1: Firearms, 2: Meds, 3: Lockpick, 4: Tools

    @Override
    public SinType getChosenSin() {
        return chosenSin;
    }

    @Override
    public void setChosenSin(SinType sin) {
        this.chosenSin = sin;
        if (activeModifiers.isEmpty() && sin != null) {
            // Add baseline trait depending on sin
            switch (sin) {
                case WRATH:
                    activeModifiers.add(new ActiveModifier("wrath_base_buff", "Урон в ближнем бою", "Melee Damage", 25.0, true));
                    activeModifiers.add(new ActiveModifier("wrath_base_debuff", "Получаемый урон", "Damage Taken", 15.0, false));
                    break;
                case PRIDE:
                    activeModifiers.add(new ActiveModifier("pride_base_buff", "Скидка у торговцев", "Barter Discount", 20.0, true));
                    activeModifiers.add(new ActiveModifier("pride_base_debuff", "Урон в одиночку", "Solo Damage", -12.0, false));
                    break;
                case LUST:
                    activeModifiers.add(new ActiveModifier("lust_base_buff", "Скорость бега", "Sprint Speed", 18.0, true));
                    activeModifiers.add(new ActiveModifier("lust_base_debuff", "Макс. здоровье", "Max Health", -10.0, false));
                    break;
                case ENVY:
                    activeModifiers.add(new ActiveModifier("envy_base_buff", "Шанс доп. лута", "Extra Loot Chance", 22.0, true));
                    activeModifiers.add(new ActiveModifier("envy_base_debuff", "Эффективность ремонта", "Repair Efficiency", -15.0, false));
                    break;
                case GLUTTONY:
                    activeModifiers.add(new ActiveModifier("gluttony_base_buff", "Регенерация HP", "Health Regen", 30.0, true));
                    activeModifiers.add(new ActiveModifier("gluttony_base_debuff", "Скорость перемещения", "Movement Speed", -10.0, false));
                    break;
                case GREED:
                    activeModifiers.add(new ActiveModifier("greed_base_buff", "Вместимость хранилищ", "Storage Boost", 25.0, true));
                    activeModifiers.add(new ActiveModifier("greed_base_debuff", "Стоимость починки", "Mending Cost", 20.0, false));
                    break;
                case SLOTH:
                    activeModifiers.add(new ActiveModifier("sloth_base_buff", "Пассивная выносливость", "Passive Stamina", 35.0, true));
                    activeModifiers.add(new ActiveModifier("sloth_base_debuff", "Урон огнестрела", "Firearms Damage", -14.0, false));
                    break;
            }
        }
    }

    @Override
    public int getSinLevel() {
        return sinLevel;
    }

    @Override
    public void setSinLevel(int level) {
        this.sinLevel = Math.max(1, level);
    }

    @Override
    public List<ActiveModifier> getActiveModifiers() {
        return activeModifiers;
    }

    @Override
    public void addModifier(ActiveModifier modifier) {
        if (modifier != null) {
            activeModifiers.add(modifier);
        }
    }

    @Override
    public void clearModifiers() {
        activeModifiers.clear();
    }

    @Override
    public int[] getLoreBooksProgress() {
        return loreBooks;
    }

    @Override
    public void setLoreBookProgress(int categoryIndex, int count) {
        if (categoryIndex >= 0 && categoryIndex < loreBooks.length) {
            loreBooks[categoryIndex] = Math.max(0, Math.min(4, count));
        }
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("chosenSin", chosenSin != null ? chosenSin.getId() : "");
        nbt.setInteger("sinLevel", sinLevel);
        
        NBTTagList list = new NBTTagList();
        for (ActiveModifier mod : activeModifiers) {
            list.appendTag(mod.serializeNBT());
        }
        nbt.setTag("modifiers", list);
        nbt.setIntArray("loreBooks", loreBooks);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt == null) return;
        String sinId = nbt.getString("chosenSin");
        this.chosenSin = SinType.byId(sinId);
        this.sinLevel = nbt.hasKey("sinLevel") ? Math.max(1, nbt.getInteger("sinLevel")) : 1;
        
        activeModifiers.clear();
        if (nbt.hasKey("modifiers", Constants.NBT.TAG_LIST)) {
            NBTTagList list = nbt.getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.tagCount(); i++) {
                activeModifiers.add(ActiveModifier.deserializeNBT(list.getCompoundTagAt(i)));
            }
        }
        
        if (nbt.hasKey("loreBooks")) {
            int[] arr = nbt.getIntArray("loreBooks");
            for (int i = 0; i < Math.min(arr.length, loreBooks.length); i++) {
                loreBooks[i] = arr[i];
            }
        }
    }

    @Override
    public void copyFrom(ISinCapability other) {
        if (other == null) return;
        this.chosenSin = other.getChosenSin();
        this.sinLevel = other.getSinLevel();
        this.activeModifiers.clear();
        this.activeModifiers.addAll(other.getActiveModifiers());
        int[] otherBooks = other.getLoreBooksProgress();
        System.arraycopy(otherBooks, 0, this.loreBooks, 0, Math.min(otherBooks.length, this.loreBooks.length));
    }
}
