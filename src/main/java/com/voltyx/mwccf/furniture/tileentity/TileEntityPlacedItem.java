package com.voltyx.mwccf.furniture.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPlacedItem extends TileEntity {

    private ItemStack stack = ItemStack.EMPTY;
    private float offsetX = 0.5f;
    private float offsetY = 0.05f;
    private float offsetZ = 0.5f;
    private float rotationYaw = 0.0f;
    private float rotationPitch = 0.0f;
    private float rotationRoll = 0.0f;
    private float scale = 1.0f;
    private boolean locked = false;
    private float iconOffsetX = 0.0f;
    private float iconOffsetY = 0.0f;
    private float iconOffsetZ = 0.0f;

    public TileEntityPlacedItem() {
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack != null ? stack : ItemStack.EMPTY;
        markDirty();
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        markDirty();
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        markDirty();
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(float offsetZ) {
        this.offsetZ = offsetZ;
        markDirty();
    }

    public float getRotationYaw() {
        return rotationYaw;
    }

    public void setRotationYaw(float rotationYaw) {
        this.rotationYaw = rotationYaw;
        markDirty();
    }

    public float getRotationPitch() {
        return rotationPitch;
    }

    public void setRotationPitch(float rotationPitch) {
        this.rotationPitch = rotationPitch;
        markDirty();
    }

    public float getRotationRoll() {
        return rotationRoll;
    }

    public void setRotationRoll(float rotationRoll) {
        this.rotationRoll = rotationRoll;
        markDirty();
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        markDirty();
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        markDirty();
    }

    public float getIconOffsetX() {
        return iconOffsetX;
    }

    public void setIconOffsetX(float iconOffsetX) {
        this.iconOffsetX = iconOffsetX;
        markDirty();
    }

    public float getIconOffsetY() {
        return iconOffsetY;
    }

    public void setIconOffsetY(float iconOffsetY) {
        this.iconOffsetY = iconOffsetY;
        markDirty();
    }

    public float getIconOffsetZ() {
        return iconOffsetZ;
    }

    public void setIconOffsetZ(float iconOffsetZ) {
        this.iconOffsetZ = iconOffsetZ;
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!stack.isEmpty()) {
            compound.setTag("Item", stack.writeToNBT(new NBTTagCompound()));
        }
        compound.setFloat("OffsetX", offsetX);
        compound.setFloat("OffsetY", offsetY);
        compound.setFloat("OffsetZ", offsetZ);
        compound.setFloat("Yaw", rotationYaw);
        compound.setFloat("Pitch", rotationPitch);
        compound.setFloat("Roll", rotationRoll);
        compound.setFloat("Scale", scale);
        compound.setBoolean("Locked", locked);
        compound.setFloat("IconOffsetX", iconOffsetX);
        compound.setFloat("IconOffsetY", iconOffsetY);
        compound.setFloat("IconOffsetZ", iconOffsetZ);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Item", 10)) {
            this.stack = new ItemStack(compound.getCompoundTag("Item"));
        } else {
            this.stack = ItemStack.EMPTY;
        }
        this.offsetX = compound.hasKey("OffsetX") ? compound.getFloat("OffsetX") : 0.5f;
        this.offsetY = compound.hasKey("OffsetY") ? compound.getFloat("OffsetY") : 0.05f;
        this.offsetZ = compound.hasKey("OffsetZ") ? compound.getFloat("OffsetZ") : 0.5f;
        this.rotationYaw = compound.getFloat("Yaw");
        this.rotationPitch = compound.getFloat("Pitch");
        this.rotationRoll = compound.getFloat("Roll");
        this.scale = compound.hasKey("Scale") ? compound.getFloat("Scale") : 1.0f;
        this.locked = compound.getBoolean("Locked");
        this.iconOffsetX = compound.hasKey("IconOffsetX") ? compound.getFloat("IconOffsetX") : 0.0f;
        this.iconOffsetY = compound.hasKey("IconOffsetY") ? compound.getFloat("IconOffsetY") : 0.0f;
        this.iconOffsetZ = compound.hasKey("IconOffsetZ") ? compound.getFloat("IconOffsetZ") : 0.0f;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
    }
}
