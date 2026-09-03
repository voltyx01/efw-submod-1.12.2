/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.amalgamation;

import com.eruannie_9.extragore.json.BloodAmalgamationPolicy;
import com.eruannie_9.extragore.json.BloodStyle;
import com.eruannie_9.extragore.particle.ParticleBlood;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
final class BloodAmalgamation {
    static final int MAX_BUCKETS = 4096;

    BloodAmalgamation() {
    }

    static boolean allowDecal(@Nullable BloodAmalgamationPolicy policy, @Nullable BloodStyle style) {
        BloodAmalgamationPolicy p = policy != null ? policy : BloodAmalgamationPolicy.BOTH;
        return p.allowGround() && ParticleBlood.isLightLikeFluid(style);
    }

    static long safeTick(World w) {
        try {
            return w.getTotalWorldTime();
        }
        catch (Throwable ignored) {
            return w.getWorldTime();
        }
    }

    static int safeDim(World w) {
        try {
            return w.provider.getDimension();
        }
        catch (Throwable t) {
            return 0;
        }
    }
}

