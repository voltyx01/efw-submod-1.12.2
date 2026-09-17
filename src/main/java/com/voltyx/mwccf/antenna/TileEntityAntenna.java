package com.voltyx.mwccf.antenna;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TileEntityAntenna extends TileEntity implements ITickable {

    public static final float DOOR_ANIM_MAX_TIME = 0.5833f;
    public static final float BUTTON_ANIM_MAX_TIME = 0.3333f;

    private boolean isOpen = false;
    private boolean isUnlocked = false;
    private boolean isLooted = false;
    private float doorAnimTime = 0.0f;
    private float prevDoorAnimTime = 0.0f;

    private String pinCode = "3608";
    private UUID activePlayerUUID = null;

    // Track active button press animation timers (buttonName -> remainingTime)
    private final Map<String, Float> buttonTimers = new HashMap<>();

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public boolean isLooted() {
        return isLooted;
    }

    public void setLooted(boolean looted) {
        if (this.isLooted != looted) {
            this.isLooted = looted;
            markDirty();
            if (world != null && !world.isRemote) {
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            }
        }
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

    public void setUnlocked(boolean unlocked) {
        if (this.isUnlocked != unlocked) {
            this.isUnlocked = unlocked;
            markDirty();
            if (world != null && !world.isRemote) {
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            }
        }
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
        markDirty();
    }

    public void triggerButtonPress(String buttonName) {
        buttonTimers.put(buttonName, BUTTON_ANIM_MAX_TIME);
    }

    public float getButtonAnimTime(String buttonName) {
        Float t = buttonTimers.get(buttonName);
        if (t == null || t <= 0.0f) return 0.0f;
        // Keyframe progress from 0.0 to 0.3333
        return BUTTON_ANIM_MAX_TIME - t;
    }

    public void setActivePlayer(EntityPlayer player) {
        this.activePlayerUUID = (player != null) ? player.getUniqueID() : null;
    }

    public UUID getActivePlayerUUID() {
        return activePlayerUUID;
    }

    public void close() {
        this.activePlayerUUID = null;
    }

    @Override
    public void update() {
        prevDoorAnimTime = doorAnimTime;

        if (isOpen) {
            if (doorAnimTime < DOOR_ANIM_MAX_TIME) {
                doorAnimTime = Math.min(DOOR_ANIM_MAX_TIME, doorAnimTime + 0.05f);
            }
        } else {
            if (doorAnimTime > 0.0f) {
                doorAnimTime = Math.max(0.0f, doorAnimTime - 0.05f);
            }
        }

        // Update button press timers
        if (!buttonTimers.isEmpty()) {
            buttonTimers.entrySet().removeIf(entry -> {
                float remaining = entry.getValue() - 0.05f;
                if (remaining <= 0.0f) {
                    return true;
                } else {
                    entry.setValue(remaining);
                    return false;
                }
            });
        }

        // Server-side: auto release active player if too far or disconnected
        if (!world.isRemote && activePlayerUUID != null) {
            EntityPlayer player = world.getPlayerEntityByUUID(activePlayerUUID);
            if (player == null || player.isDead || player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 16.0) {
                close();
            }
        }
    }

    public float getInterpolatedDoorAnimTime(float partialTicks) {
        return prevDoorAnimTime + (doorAnimTime - prevDoorAnimTime) * partialTicks;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("IsOpen", isOpen);
        compound.setBoolean("IsUnlocked", isUnlocked);
        compound.setBoolean("IsLooted", isLooted);
        compound.setFloat("DoorAnimTime", doorAnimTime);
        compound.setString("PinCode", pinCode);
        if (activePlayerUUID != null) {
            compound.setUniqueId("ActivePlayer", activePlayerUUID);
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.isOpen = compound.getBoolean("IsOpen");
        this.isUnlocked = compound.getBoolean("IsUnlocked");
        this.isLooted = compound.getBoolean("IsLooted");
        this.doorAnimTime = compound.getFloat("DoorAnimTime");
        this.prevDoorAnimTime = this.doorAnimTime;
        if (compound.hasKey("PinCode")) {
            this.pinCode = compound.getString("PinCode");
            if ("1234".equals(this.pinCode)) {
                this.pinCode = "3608";
            }
        } else {
            this.pinCode = "3608";
        }
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
