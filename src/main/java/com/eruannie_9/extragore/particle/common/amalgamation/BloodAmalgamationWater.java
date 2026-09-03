/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.amalgamation;

import com.eruannie_9.extragore.particle.common.amalgamation.BloodAmalgamationLiquid;
import com.eruannie_9.extragore.particle.state.liquid.water.BloodWater;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodAmalgamationWater {
    private static final Map<Long, BloodWater> CACHE = new HashMap<Long, BloodWater>(256);

    public static void tryMerge(@Nullable BloodWater p) {
        long self;
        BloodWater prev;
        float give;
        if (p == null || !p.isAlive()) {
            return;
        }
        if (!BloodAmalgamationLiquid.allow(p.getAmalgamationPolicy(), p.getFluidWeight())) {
            return;
        }
        if (!p.isOnSurface()) {
            return;
        }
        if (p.isAmalgMergingOut()) {
            return;
        }
        if (!BloodAmalgamationLiquid.mature(p.getAge(), p.getSurfaceGrowStartAge(), 6.0f)) {
            return;
        }
        World w = p.getParticleWorld();
        if (w == null) {
            return;
        }
        BloodAmalgamationLiquid.beginTick(w);
        float cell = Math.max(0.05f, 0.25f);
        int cx = MathHelper.floor((double)(p.getPosX() / (double)cell));
        int cz = MathHelper.floor((double)(p.getPosZ() / (double)cell));
        int layer = p.getCache() != null && p.getCache().getCachedTop() != null ? ((net.minecraft.util.math.Vec3i) p.getCache().getCachedTop()).getY() : MathHelper.floor((double)p.getPosY());
        double areaP = BloodAmalgamationLiquid.area(p.getSurfaceScale(), p.getAmalgMul());
        BloodWater best = null;
        double bestD2 = Double.POSITIVE_INFINITY;
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dz = -1; dz <= 1; ++dz) {
                double r;
                double d2;
                double areaQ;
                long key = BloodAmalgamationLiquid.key(layer, cx + dx, cz + dz);
                BloodWater q = CACHE.get(key);
                if (q == null) continue;
                if (!q.isAlive() || !q.isOnSurface() || q.isAmalgMergingOut()) {
                    CACHE.remove(key);
                    continue;
                }
                if (!BloodAmalgamationLiquid.allow(q.getAmalgamationPolicy(), q.getFluidWeight())) {
                    CACHE.remove(key);
                    continue;
                }
                if (!BloodAmalgamationLiquid.mature(q.getAge(), q.getSurfaceGrowStartAge(), 6.0f) || !q.canAcceptAmalgIn() || (areaQ = BloodAmalgamationLiquid.area(q.getSurfaceScale(), q.getAmalgMul())) < areaP * (double)1.1f || !((d2 = BloodAmalgamationLiquid.dist2(p.getPosX(), p.getPosZ(), q.getPosX(), q.getPosZ())) <= (r = BloodAmalgamationLiquid.range(p.getSurfaceScale(), p.getAmalgMul(), q.getSurfaceScale(), q.getAmalgMul(), 0.45f)) * r) || !(d2 < bestD2)) continue;
                bestD2 = d2;
                best = q;
            }
        }
        if (best != null && (give = BloodAmalgamationLiquid.give(best.getSurfaceScale(), best.getAmalgTargetMul(), p.getSurfaceScale(), p.getAmalgMul())) > 1.0E-6f) {
            p.startAmalgMergeOut(best, give);
            return;
        }
        if (p.canAcceptAmalgIn() && ((prev = CACHE.get(self = BloodAmalgamationLiquid.key(layer, cx, cz))) == null || !prev.isAlive() || BloodAmalgamationLiquid.area(prev.getSurfaceScale(), prev.getAmalgMul()) < areaP)) {
            CACHE.put(self, p);
        }
    }

    static void clearCache() {
        CACHE.clear();
    }
}

