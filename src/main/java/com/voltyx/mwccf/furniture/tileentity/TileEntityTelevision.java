package com.voltyx.mwccf.furniture.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTelevision extends TileEntity {

    public static final String[] CHANNELS = new String[] {
            "pong", "villager_news", "ocean_sunset", "block_game", "dance_music",
            "black_noise", "white_noise", "herobrine", "heart_screensaver", "colour_test",
            "rip_blizzard", "silly_face"
    };

    private boolean powered = false;
    private int channel = 0;

    public boolean isPowered() {
        return powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        markDirty();
        if (world != null && !world.isRemote) {
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }

    public void togglePower() {
        setPowered(!this.powered);
    }

    public int getChannel() {
        return channel;
    }

    public void nextChannel() {
        this.channel = (this.channel + 1) % CHANNELS.length;
        markDirty();
        if (world != null && !world.isRemote) {
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }

    public String getCurrentChannelName() {
        return CHANNELS[channel % CHANNELS.length];
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.powered = compound.getBoolean("Powered");
        this.channel = compound.getInteger("Channel");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("Powered", this.powered);
        compound.setInteger("Channel", this.channel);
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }
}
