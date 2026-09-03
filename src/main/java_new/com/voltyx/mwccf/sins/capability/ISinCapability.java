package com.voltyx.mwccf.sins.capability;

import com.voltyx.mwccf.sins.ActiveModifier;
import com.voltyx.mwccf.sins.SinType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public interface ISinCapability {
    SinType getChosenSin();
    void setChosenSin(SinType sin);

    int getSinLevel();
    void setSinLevel(int level);

    List<ActiveModifier> getActiveModifiers();
    void addModifier(ActiveModifier modifier);
    void clearModifiers();

    int[] getLoreBooksProgress();
    void setLoreBookProgress(int categoryIndex, int count);

    NBTTagCompound writeToNBT();
    void readFromNBT(NBTTagCompound nbt);
    void copyFrom(ISinCapability other);
}
