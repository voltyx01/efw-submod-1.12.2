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

import com.eruannie_9.extragore.particle.common.cache.BloodCaches;
import com.eruannie_9.extragore.particle.common.cache.BloodCachesCommon;
import com.eruannie_9.extragore.particle.state.liquid.water.BloodWater;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodCachesWater {
    private static final BloodCaches.WaterFrame FRAME = new BloodCaches.WaterFrame();

    public static void captureBillboard(@Nonnull BloodWater p, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        BloodCachesCommon.setBillboard(p.cache.billboard, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ, BloodWater.getCameraX(), BloodWater.getCameraY(), BloodWater.getCameraZ());
    }

    public static boolean hasBillboard(@Nullable BloodWater p) {
        return p != null && BloodCachesCommon.hasBillboard(p.cache.billboard);
    }

    public static void clearBillboard(@Nullable BloodWater p) {
        if (p == null) {
            return;
        }
        BloodCachesCommon.clearBillboard(p.cache.billboard);
    }

    @Nonnull
    public static BloodCaches.WaterFrame frame() {
        return FRAME;
    }

    public static void clearQuery(@Nonnull BloodCaches.Query q) {
        q.hit.clear();
        q.miss.clear();
    }
}

