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

import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.state.BloodMagic;
import java.util.WeakHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodCachesMagic {
    private static final WeakHashMap<ParticleBlood, BloodMagic.MagicRt> RT = new WeakHashMap();

    @Nullable
    public static BloodMagic.MagicRt get(@Nullable ParticleBlood p) {
        return p != null ? RT.get((Object)p) : null;
    }

    public static void put(@Nonnull ParticleBlood p, @Nonnull BloodMagic.MagicRt rt) {
        RT.put(p, rt);
    }
}

