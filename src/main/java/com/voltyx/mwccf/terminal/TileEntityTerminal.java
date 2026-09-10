package com.voltyx.mwccf.terminal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.UUID;

public class TileEntityTerminal extends TileEntity implements ITickable {

    public static final float ANIM_MAX_TIME = 0.5f;

    private boolean isOpen = false;
    private float animTime = 0.0f;
    private float prevAnimTime = 0.0f;

    private UUID activePlayerUUID = null;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        if (this.isOpen != open) {
            this.isOpen = open;
            markDirty();
            if (world != null && !world.isRemote) {
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            }
        }
    }

    public void toggleOpen(EntityPlayer player) {
        if (isOpen) {
            if (activePlayerUUID == null || activePlayerUUID.equals(player.getUniqueID())) {
                setOpen(false);
                activePlayerUUID = null;
            }
        } else {
            setOpen(true);
            activePlayerUUID = player.getUniqueID();
        }
    }

    public void close() {
        if (isOpen) {
            setOpen(false);
            activePlayerUUID = null;
        }
    }

    @Override
    public void update() {
        prevAnimTime = animTime;

        if (isOpen) {
            if (animTime < ANIM_MAX_TIME) {
                animTime = Math.min(ANIM_MAX_TIME, animTime + 0.05f);
            }
        } else {
            if (animTime > 0.0f) {
                animTime = Math.max(0.0f, animTime - 0.05f);
            }
        }

        // Server check: auto-close if player walks away or logs off
        if (!world.isRemote && isOpen && activePlayerUUID != null) {
            EntityPlayer player = world.getPlayerEntityByUUID(activePlayerUUID);
            if (player == null || player.isDead || player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 16.0) {
                close();
            }
        }
    }

    public float getInterpolatedAnimTime(float partialTicks) {
        return prevAnimTime + (animTime - prevAnimTime) * partialTicks;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("IsOpen", isOpen);
        compound.setFloat("AnimTime", animTime);
        if (activePlayerUUID != null) {
            compound.setUniqueId("ActivePlayer", activePlayerUUID);
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.isOpen = compound.getBoolean("IsOpen");
        this.animTime = compound.getFloat("AnimTime");
        this.prevAnimTime = this.animTime;
        if (compound.hasUniqueId("ActivePlayer")) {
            this.activePlayerUUID = compound.getUniqueId("ActivePlayer");
        } else {
            this.activePlayerUUID = null;
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }
}
