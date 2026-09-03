/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.alpha;

import com.eruannie_9.extragore.particle.common.Util;
import com.eruannie_9.extragore.particle.common.alpha.BloodAlphaCommon;
import com.eruannie_9.extragore.particle.state.liquid.water.BloodWater;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodAlphaWater {
    public static float waterCarryAlpha(float initialAlpha, int age, int maxAge) {
        return BloodAlphaCommon.carryAlpha(initialAlpha, age, maxAge, 1.0f, 0.7f);
    }

    public static float waterBaseAlpha(float carriedAlpha, int age, int maxAge) {
        return BloodAlphaCommon.baseAlpha(carriedAlpha, age, maxAge, 0.7f);
    }

    public static boolean updateWaterAlpha(@Nullable BloodWater p) {
        if (p == null) {
            return false;
        }
        float alpha = BloodAlphaWater.waterBaseAlpha(p.getCarriedAlphaMul(), p.getAge(), p.getMaxAge());
        p.setParticleAlpha(Util.clamp01(alpha));
        if (p.getParticleAlpha() <= 0.001f) {
            p.expireAndUntrack();
            return false;
        }
        return true;
    }
}

