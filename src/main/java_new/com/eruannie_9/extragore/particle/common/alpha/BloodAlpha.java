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
import com.eruannie_9.extragore.particle.common.alpha.BloodAlphaMagic;
import com.eruannie_9.extragore.particle.common.alpha.BloodAlphaSlimy;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class BloodAlpha {
    public static float styleAlpha(@Nullable BloodStyle style, int age, int maxAge) {
        BloodStyle s = ParticleBlood.normalizeWeight(style);
        if (s == BloodStyle.MAGIC) {
            return BloodAlphaMagic.magicAlpha(age, maxAge);
        }
        if (s == BloodStyle.SLIMY) {
            return BloodAlphaSlimy.slimyAlpha(age, maxAge);
        }
        return BloodAlphaCommon.lifeAlpha(age, maxAge);
    }

    public static float predictAlpha(@Nullable ParticleBlood p, int age, int maxAge) {
        BloodStyle style = p != null ? p.fluidWeight : BloodStyle.LIGHT;
        return BloodAlpha.styleAlpha(style, age, maxAge);
    }
}

