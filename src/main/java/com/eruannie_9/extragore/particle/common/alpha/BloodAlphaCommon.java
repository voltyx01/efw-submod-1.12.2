/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.alpha;

import com.eruannie_9.extragore.ModConfigurationClient;
import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.common.Util;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodAlphaCommon {
    static final float EXPIRE_EPS = 1.0E-4f;

    public static void expireNow(@Nullable ParticleBlood p) {
        if (p == null) {
            return;
        }
        p.setAlpha(0.0f);
        p.alphaMonotonic = 0.0f;
        p.setExpired();
    }

    public static float lifeAlpha(int age, int maxAge) {
        float start = Util.clamp01((float)ModConfigurationClient.fade.startFade);
        return BloodAlphaCommon.envelopeAlpha(age, maxAge, start);
    }

    public static float lifeRemain(int age, int maxAge, int startAge) {
        if (startAge < 0) {
            return 1.0f;
        }
        int end = Math.max(startAge + 1, maxAge);
        float t = (float)(age - startAge) / (float)(end - startAge);
        t = Util.clamp01(t);
        return Util.clamp01(1.0f - Util.smoothstep01(t));
    }

    public static float carryAlpha(float initialAlpha, int age, int maxAge, float sourceMul, float cap) {
        float now;
        float init = Util.clamp01(initialAlpha) * sourceMul;
        if (init > cap) {
            init = cap;
        }
        return (now = BloodAlphaCommon.lifeAlpha(age, maxAge)) > 1.0E-6f ? init / now : init;
    }

    public static float baseAlpha(float carriedAlpha, int age, int maxAge, float cap) {
        float alpha = Util.clamp01(carriedAlpha * BloodAlphaCommon.lifeAlpha(age, maxAge));
        if (alpha > cap) {
            alpha = cap;
        }
        return alpha;
    }

    public static float envelopeAlpha(int age, int maxAge, float fadeStart) {
        int m = Math.max(1, maxAge);
        int a = Math.max(0, age);
        float t = Util.clamp01((float)a / (float)m);
        float start = Util.clamp01(fadeStart);
        if (start >= 0.9999f) {
            start = 0.9999f;
        }
        if (t <= start) {
            return 1.0f;
        }
        float f = Util.clamp01((t - start) / (1.0f - start));
        return 1.0f - Util.smoothstep01(f);
    }
}

