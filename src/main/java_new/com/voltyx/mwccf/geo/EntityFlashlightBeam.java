package com.voltyx.mwccf.geo;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityFlashlightBeam extends EntityItem {
    
    public EntityFlashlightBeam(World worldIn) {
        super(worldIn);
        this.setItem(new ItemStack(Blocks.SEA_LANTERN));
        this.setNoGravity(true);
        this.setInvisible(true);
        this.setPickupDelay(32767);
        this.setSize(0.0F, 0.0F);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {}

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return compound;
    }
}
