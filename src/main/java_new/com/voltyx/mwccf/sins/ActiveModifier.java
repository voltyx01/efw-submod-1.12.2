package com.voltyx.mwccf.sins;

import net.minecraft.nbt.NBTTagCompound;

public class ActiveModifier {
    private String id;
    private String nameRu;
    private String nameEn;
    private double value;
    private boolean isBuff;

    public ActiveModifier() {}

    public ActiveModifier(String id, String nameRu, String nameEn, double value, boolean isBuff) {
        this.id = id;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.value = value;
        this.isBuff = isBuff;
    }

    public String getId() {
        return id;
    }

    public String getNameRu() {
        return nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public double getValue() {
        return value;
    }

    public boolean isBuff() {
        return isBuff;
    }

    public String getFormattedValue() {
        String sign = value >= 0 ? "+" : "−";
        return sign + String.format("%.0f%%", Math.abs(value));
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("id", id != null ? id : "");
        tag.setString("nameRu", nameRu != null ? nameRu : "");
        tag.setString("nameEn", nameEn != null ? nameEn : "");
        tag.setDouble("value", value);
        tag.setBoolean("isBuff", isBuff);
        return tag;
    }

    public static ActiveModifier deserializeNBT(NBTTagCompound tag) {
        ActiveModifier mod = new ActiveModifier();
        mod.id = tag.getString("id");
        mod.nameRu = tag.getString("nameRu");
        mod.nameEn = tag.getString("nameEn");
        mod.value = tag.getDouble("value");
        mod.isBuff = tag.getBoolean("isBuff");
        return mod;
    }
}
