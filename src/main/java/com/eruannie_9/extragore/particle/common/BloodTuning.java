/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common;

import com.eruannie_9.extragore.ModConfigurationClient;
import com.eruannie_9.extragore.particle.common.Util;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class BloodTuning {
    public static final float DEFAULT_GRAVITY = 0.8f;
    private static final float COLLISION_SIZE_PER_SCALE = 0.2f;
    private static final float COLLISION_SIZE_MIN = 0.01f;
    private static final float COLLISION_SIZE_MAX = 0.12f;
    private static int dripCachedHash = 0;
    private static Set<ResourceLocation> dripCachedSet = Collections.emptySet();
    private static int ceilingAttachCachedHash = 0;
    private static Set<ResourceLocation> ceilingAttachCachedSet = Collections.emptySet();

    public static boolean isWallFace(@Nullable EnumFacing face) {
        return Util.isVerticalFace(face);
    }

    public static float collisionSizeForScale(float scale) {
        float s = 0.2f * scale;
        if (s < 0.01f) {
            s = 0.01f;
        }
        if (s > 0.12f) {
            s = 0.12f;
        }
        return s;
    }

    private static Set<ResourceLocation> parseBlockListToSet(@Nullable String[] arr) {
        HashSet<ResourceLocation> s = new HashSet<ResourceLocation>();
        if (arr == null) {
            return s;
        }
        for (String raw : arr) {
            if (raw == null || (raw = raw.trim()).isEmpty()) continue;
            try {
                s.add(new ResourceLocation(raw.toLowerCase(Locale.ROOT)));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return s;
    }

    private static Set<ResourceLocation> getConfiguredDripSet() {
        Object[] arr = ModConfigurationClient.wall.blockList;
        int hash = 1;
        hash = 31 * hash + ModConfigurationClient.wall.blockListMode.ordinal();
        if ((hash = 31 * hash + Arrays.hashCode(arr)) != dripCachedHash) {
            dripCachedSet = BloodTuning.parseBlockListToSet((String[])arr);
            dripCachedHash = hash;
        }
        return dripCachedSet;
    }

    public static boolean dripEnabledForHost(@Nullable IBlockState host) {
        Block b;
        if (!ModConfigurationClient.wall.drip) {
            return false;
        }
        ModConfigurationClient.BlockListMode mode = ModConfigurationClient.wall.blockListMode;
        if (mode == ModConfigurationClient.BlockListMode.NONE) {
            return true;
        }
        ResourceLocation id = null;
        if (host != null && (b = host.getBlock()) != null) {
            id = b.getRegistryName();
        }
        if (id == null) {
            return mode != ModConfigurationClient.BlockListMode.WHITELIST;
        }
        boolean listed = BloodTuning.getConfiguredDripSet().contains(id);
        switch (mode) {
            case WHITELIST: {
                return listed;
            }
            case BLACKLIST: {
                return !listed;
            }
        }
        return true;
    }

    private static Set<ResourceLocation> getConfiguredCeilingAttachSet() {
        Object[] arr = ModConfigurationClient.ceiling.blockList;
        int hash = 1;
        hash = 31 * hash + ModConfigurationClient.ceiling.blockListMode.ordinal();
        if ((hash = 31 * hash + Arrays.hashCode(arr)) != ceilingAttachCachedHash) {
            ceilingAttachCachedSet = BloodTuning.parseBlockListToSet((String[])arr);
            ceilingAttachCachedHash = hash;
        }
        return ceilingAttachCachedSet;
    }

    private static boolean isValidCeilingHost(@Nullable IBlockState host) {
        if (host == null) {
            return false;
        }
        try {
            if (!host.getMaterial().blocksMovement()) {
                return false;
            }
            if (!host.getMaterial().isOpaque()) {
                return false;
            }
        }
        catch (Throwable t) {
            return false;
        }
        return true;
    }

    public static boolean ceilingAttachEnabledForHost(@Nullable IBlockState host) {
        Block b;
        if (!ModConfigurationClient.ceiling.stick) {
            return false;
        }
        if (!BloodTuning.isValidCeilingHost(host)) {
            return false;
        }
        ModConfigurationClient.BlockListMode mode = ModConfigurationClient.ceiling.blockListMode;
        if (mode == ModConfigurationClient.BlockListMode.NONE) {
            return true;
        }
        ResourceLocation id = null;
        if (host != null && (b = host.getBlock()) != null) {
            id = b.getRegistryName();
        }
        if (id == null) {
            return mode != ModConfigurationClient.BlockListMode.WHITELIST;
        }
        boolean listed = BloodTuning.getConfiguredCeilingAttachSet().contains(id);
        switch (mode) {
            case WHITELIST: {
                return listed;
            }
            case BLACKLIST: {
                return !listed;
            }
        }
        return true;
    }
}

