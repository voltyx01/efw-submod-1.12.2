package com.voltyx.mwccf.furniture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class EntitySeat extends Entity {

    public EntitySeat(World worldIn) {
        super(worldIn);
        this.setSize(0.0F, 0.0F);
    }

    public EntitySeat(World worldIn, BlockPos pos, double yOffset) {
        this(worldIn);
        this.setPosition(pos.getX() + 0.5D, pos.getY() + yOffset, pos.getZ() + 0.5D);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            if (this.getPassengers().isEmpty() || this.world.isAirBlock(this.getPosition())) {
                this.setDead();
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    }

    public static boolean sitOnBlock(World worldIn, BlockPos pos, EntityPlayer player, double yOffset) {
        if (!worldIn.isRemote) {
            List<EntitySeat> seats = worldIn.getEntitiesWithinAABB(EntitySeat.class, new net.minecraft.util.math.AxisAlignedBB(pos));
            if (seats.isEmpty()) {
                EntitySeat seat = new EntitySeat(worldIn, pos, yOffset);
                worldIn.spawnEntity(seat);
                player.startRiding(seat);
                return true;
            }
        }
        return false;
    }
}
