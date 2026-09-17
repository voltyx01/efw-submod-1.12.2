package com.voltyx.mwccf.zone;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class LocksCompat {

    public static boolean isDoorLocked(World world, BlockPos pos, BlockPos checkPos) {
        if (!Loader.isModLoaded("locks")) {
            return false;
        }
        try {
            return checkLock(world, pos, checkPos);
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static boolean checkLock(World world, BlockPos pos, BlockPos checkPos) {
        melonslise.locks.common.capability.ILockableHandler handler = world.getCapability(melonslise.locks.common.init.LocksCapabilities.LOCKABLE_HANDLER, null);
        if (handler != null) {
            for (melonslise.locks.common.util.Lockable l : handler.getInChunk(checkPos).values()) {
                if ((l.box.intersects(checkPos) || l.box.intersects(pos)) && l.lock.isLocked()) {
                    return true;
                }
            }
        }
        return false;
    }
}
