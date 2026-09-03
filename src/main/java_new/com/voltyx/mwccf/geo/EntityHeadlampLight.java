package com.voltyx.mwccf.geo;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityHeadlampLight extends EntityItem {

    public EntityHeadlampLight(World world) {
        super(world);
        // OptiFine natively hardcodes MAGMA_CREAM to emit light level 8 (roughly half radius of 15)
        setItem(new ItemStack(net.minecraft.init.Items.MAGMA_CREAM));
        setSize(0.05f, 0.05f);
        noClip = true;
        setInvisible(true);
        ignoreFrustumCheck = true;
        setNoDespawn();
        setInfinitePickupDelay();
        setNoGravity(true);
    }

    @Override
    public void readEntityFromNBT(net.minecraft.nbt.NBTTagCompound compound) {
        // Self-destruct if loaded from disk (prevents orphaned ghosts after restart)
        this.setDead();
    }

    @Override
    public void writeEntityToNBT(net.minecraft.nbt.NBTTagCompound compound) {
        // Do not save to disk
    }

    @Override
    public void onUpdate() {
        // Prevent normal item logic but keep ticksExisted
        this.ticksExisted++;
        this.setPickupDelay(32767); // Never pickup
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        // Prevent pickup
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isEntityInvulnerable(net.minecraft.util.DamageSource source) {
        return true;
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        // Prevent server synchronization from ruining the perfectly smooth client-side render ticks
    }
}
