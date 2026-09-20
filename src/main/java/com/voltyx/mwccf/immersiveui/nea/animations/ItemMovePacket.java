package com.voltyx.mwccf.immersiveui.nea.animations;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import com.voltyx.mwccf.immersiveui.nea.NEAHelper;
import com.voltyx.mwccf.immersiveui.nea.api.IItemLocation;
import net.minecraft.item.ItemStack;

public class ItemMovePacket {

    private final long time;
    private final IItemLocation source, target;
    private final ItemStack movingStack;
    private final ItemStack targetStack;

    public ItemMovePacket(long time, IItemLocation source, IItemLocation target, ItemStack movingStack) {
        this.time = time;
        this.source = source;
        this.target = target;
        this.targetStack = target.nea$getStack().copy();
        this.movingStack = movingStack;
    }

    public long getTime() {
        return time;
    }

    public ItemStack getMovingStack() {
        return movingStack;
    }

    public ItemStack getTargetStack() {
        return targetStack;
    }

    public IItemLocation getSource() {
        return source;
    }

    public IItemLocation getTarget() {
        return target;
    }

    public float value() {
        if (ImmersiveUIConfig.moveAnimationTime <= 0) return 1.0F;
        return Math.min(1.0F, (NEAHelper.time() - this.time) / (float) ImmersiveUIConfig.moveAnimationTime);
    }

    public int getDrawX(float value) {
        return (int) ImmersiveUIConfig.moveAnimationCurve.interpolate(source.nea$getX(), target.nea$getX(), value);
    }

    public int getDrawY(float value) {
        return (int) ImmersiveUIConfig.moveAnimationCurve.interpolate(source.nea$getY(), target.nea$getY(), value);
    }
}
