/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.cache;

import com.eruannie_9.extragore.particle.common.cache.BloodCachesCommon;
import com.eruannie_9.extragore.particle.state.liquid.lava.BloodLava;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodCachesLava {
    public static void captureBillboard(@Nonnull BloodLava p, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        BloodCachesCommon.setBillboard(p.cache.billboard, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ, BloodLava.getCameraX(), BloodLava.getCameraY(), BloodLava.getCameraZ());
    }

    public static boolean hasBillboard(@Nullable BloodLava p) {
        return p != null && BloodCachesCommon.hasBillboard(p.cache.billboard);
    }

    public static void clearBillboard(@Nullable BloodLava p) {
        if (p == null) {
            return;
        }
        BloodCachesCommon.clearBillboard(p.cache.billboard);
    }
}

