package com.voltyx.mwccf.dash;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DashCapability {

    @CapabilityInject(IDashData.class)
    public static final Capability<IDashData> ROLL_CAP = null;

    public interface IDashData {
        boolean isDashing();
        void setDashing(boolean value);

        int getDashTicks();
        void setDashTicks(int ticks);

        int getCooldown();
        void setCooldown(int ticks);

        int getPostDashTimer();
        void setPostDashTimer(int ticks);

        int getReactionTimer();
        void setReactionTimer(int ticks);

        boolean wasDashPerformed();
        void setDashPerformed(boolean value);

        int getLastDashTick();
        void setLastDashTick(int tick);

        Vec3d getDashDir();
        void setDashDir(Vec3d dir);
    }

    public static class DashData implements IDashData {
        private boolean dashing;
        private int dashTicks;
        private int cooldown;
        private int postDashTimer = 0;
        private int reactionTimer = 0;
        private boolean dashPerformed = false;
        private int lastDashTick = -1;
        private Vec3d dashDir = Vec3d.ZERO;

        @Override public boolean isDashing() { return dashing; }
        @Override public void setDashing(boolean value) { this.dashing = value; }
        @Override public int getDashTicks() { return dashTicks; }
        @Override public void setDashTicks(int ticks) { this.dashTicks = ticks; }
        @Override public int getCooldown() { return cooldown; }
        @Override public void setCooldown(int ticks) { this.cooldown = ticks; }
        @Override public int getPostDashTimer() { return postDashTimer; }
        @Override public void setPostDashTimer(int ticks) { this.postDashTimer = ticks; }
        @Override public int getReactionTimer() { return reactionTimer; }
        @Override public void setReactionTimer(int ticks) { this.reactionTimer = ticks; }
        @Override public boolean wasDashPerformed() { return dashPerformed; }
        @Override public void setDashPerformed(boolean value) { this.dashPerformed = value; }
        @Override public int getLastDashTick() { return lastDashTick; }
        @Override public void setLastDashTick(int tick) { this.lastDashTick = tick; }
        @Override public Vec3d getDashDir() { return dashDir; }
        @Override public void setDashDir(Vec3d dir) { this.dashDir = dir; }
    }

    public static class DashStorage implements Capability.IStorage<IDashData> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IDashData> capability, IDashData instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("Dashing", instance.isDashing());
            tag.setInteger("DashTicks", instance.getDashTicks());
            tag.setInteger("Cooldown", instance.getCooldown());
            return tag;
        }

        @Override
        public void readNBT(Capability<IDashData> capability, IDashData instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tag = (NBTTagCompound) nbt;
            instance.setDashing(tag.getBoolean("Dashing"));
            instance.setDashTicks(tag.getInteger("DashTicks"));
            instance.setCooldown(tag.getInteger("Cooldown"));
        }
    }

    public static class DashProvider implements ICapabilitySerializable<NBTTagCompound> {
        public static final ResourceLocation KEY = new ResourceLocation("mwccf", "dash");

        private final IDashData instance = new DashData();

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == ROLL_CAP;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == ROLL_CAP ? ROLL_CAP.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) ROLL_CAP.getStorage().writeNBT(ROLL_CAP, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            ROLL_CAP.getStorage().readNBT(ROLL_CAP, instance, null, nbt);
        }
    }
}
