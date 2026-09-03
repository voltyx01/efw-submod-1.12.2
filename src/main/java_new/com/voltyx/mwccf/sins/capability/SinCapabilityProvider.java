package com.voltyx.mwccf.sins.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SinCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {

    @CapabilityInject(ISinCapability.class)
    public static Capability<ISinCapability> SIN_CAP = null;

    public static final ResourceLocation KEY = new ResourceLocation("mwccf", "player_sins");

    private final ISinCapability instance = new SinCapability();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == SIN_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == SIN_CAP ? SIN_CAP.cast(this.instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return this.instance.writeToNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.instance.readFromNBT(nbt);
    }
}
