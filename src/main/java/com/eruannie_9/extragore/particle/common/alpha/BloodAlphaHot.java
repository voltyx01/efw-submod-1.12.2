/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.alpha;

import com.eruannie_9.extragore.json.BloodStyle;
import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.common.Util;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodAlphaHot {
    public static float hotProgress(@Nullable ParticleBlood p, float partialTicks) {
        if (p == null || p.hotSurfaceStartAge < 0) {
            return 0.0f;
        }
        int start = p.hotSurfaceStartAge;
        int end = Math.max(start + 1, p.getMaxAge());
        float t = ((float)p.getAge() + partialTicks - (float)start) / (float)(end - start);
        return Util.smoothstep01(Util.clamp01(t));
    }

    public static float hotAlpha(@Nullable ParticleBlood p, float partialTicks) {
        if (p == null) {
            return 1.0f;
        }
        float remain = 1.0f - BloodAlphaHot.hotProgress(p, partialTicks);
        if (ParticleBlood.normalizeWeight(p.fluidWeight) == BloodStyle.HEAVY) {
            return Util.clamp01(remain);
        }
        return Util.clamp01(remain * remain);
    }
}

