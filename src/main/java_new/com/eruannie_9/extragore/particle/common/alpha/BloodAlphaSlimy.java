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
import com.eruannie_9.extragore.particle.common.alpha.BloodAlphaCommon;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
final class BloodAlphaSlimy {
    static final float SLIMY_FADE_START = 0.8f;
    static final float SLIMY_WATER_FADE_MUL = 1.35f;

    BloodAlphaSlimy() {
    }

    static float slimyAlpha(int age, int maxAge) {
        return BloodAlphaCommon.envelopeAlpha(age, maxAge, 0.8f);
    }

    static int waterFadeTicks(@Nullable ParticleBlood p, int baseTicks) {
        int base = Math.max(1, baseTicks);
        if (p == null || ParticleBlood.normalizeWeight(p.fluidWeight) != BloodStyle.SLIMY) {
            return base;
        }
        int scaled = Math.round((float)base * 1.35f);
        return Math.max(base, scaled);
    }
}

