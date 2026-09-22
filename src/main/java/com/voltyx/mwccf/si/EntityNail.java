package com.voltyx.mwccf.si;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityNail extends EntityArrow {

    public EntityNail(World worldIn) {
        super(worldIn);
        this.setDamage(1.9D);
    }

    public EntityNail(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.setDamage(1.9D);
    }

    public EntityNail(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setDamage(1.9D);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(SIItems.NAIL);
    }
}
