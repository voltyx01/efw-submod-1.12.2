/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.cache;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodCachesHot {
    private static int hash = 0;
    private static Set<ResourceLocation> set = Collections.emptySet();

    @Nonnull
    public static Set<ResourceLocation> hotSet(boolean enabled, @Nullable String[] rawList) {
        int next = 1;
        next = 31 * next + (enabled ? 1 : 0);
        if ((next = 31 * next + Arrays.hashCode(rawList)) != hash) {
            set = BloodCachesHot.parse(rawList);
            hash = next;
        }
        return set;
    }

    @Nonnull
    private static Set<ResourceLocation> parse(@Nullable String[] rawList) {
        HashSet<ResourceLocation> out = new HashSet<ResourceLocation>();
        if (rawList == null) {
            return out;
        }
        for (String raw : rawList) {
            if (raw == null || (raw = raw.trim()).isEmpty()) continue;
            try {
                out.add(new ResourceLocation(raw.toLowerCase(Locale.ROOT)));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return out;
    }
}

