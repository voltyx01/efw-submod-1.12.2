package com.voltyx.mwccf.furniture.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityCeilingFan extends TileEntity implements ITickable {

    private boolean powered = false;
    public float fanAngle = 0.0F;
    public float prevFanAngle = 0.0F;
    public float fanSpeed = 0.0F;

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

    @Override
    public void update() {
        this.prevFanAngle = this.fanAngle;
        if (this.powered) {
            this.fanSpeed = Math.min(this.fanSpeed + 0.8F, 18.0F);
            this.fanAngle = (this.fanAngle + this.fanSpeed) % 360.0F;
        } else {
            this.fanSpeed = Math.max(this.fanSpeed - 0.3F, 0.0F);
            if (this.fanSpeed > 0.0F) {
                this.fanAngle = (this.fanAngle + this.fanSpeed) % 360.0F;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.powered = compound.getBoolean("Powered");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("Powered", this.powered);
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
