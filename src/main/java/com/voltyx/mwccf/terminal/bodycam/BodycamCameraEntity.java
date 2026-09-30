package com.voltyx.mwccf.terminal.bodycam;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class BodycamCameraEntity extends EntityLivingBase {

    public BodycamCameraEntity(World worldIn) {
        super(worldIn);
        this.setSize(0.05F, 0.05F);
        this.noClip = true;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    @Override
    public float getEyeHeight() {
        return 0.0F; // Direct eye position at entity coordinate
    }

    @Override
    public void onUpdate() {
        // Dummy entity: no physics or ticks needed
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
    }
}
