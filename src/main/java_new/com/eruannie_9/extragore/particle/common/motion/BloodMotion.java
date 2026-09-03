/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.motion;

import com.eruannie_9.extragore.particle.ParticleBlood;
import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodMotion {
    static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    static int randBetween(Random rand, int min, int max) {
        int lo = Math.min(min, max);
        int hi = Math.max(min, max);
        return lo + (hi > lo ? rand.nextInt(hi - lo + 1) : 0);
    }

    static int randomInclusive(Random rand, int min, int max) {
        int lo = Math.min(min, max);
        int hi = Math.max(min, max);
        return lo + (hi > lo ? rand.nextInt(hi - lo + 1) : 0);
    }

    static void clampHorizontal(ParticleBlood p, double maxH) {
        double hs = p.motionX * p.motionX + p.motionZ * p.motionZ;
        double maxSq = maxH * maxH;
        if (hs > maxSq && hs > 1.0E-12) {
            double m = maxH / Math.sqrt(hs);
            p.motionX *= m;
            p.motionZ *= m;
        }
    }

    static double randRange(Random rand, double min, double max) {
        if (max <= min) {
            return min;
        }
        return min + rand.nextDouble() * (max - min);
    }

    static double randSigned(Random rand) {
        return rand.nextDouble() * 2.0 - 1.0;
    }

    public static Vec3d normalizeOr(Vec3d v, Vec3d fallback) {
        double lenSq;
        double d = lenSq = v != null ? v.lengthSquared() : 0.0;
        if (lenSq > 1.0E-12) {
            return v.scale(1.0 / Math.sqrt(lenSq));
        }
        double fbLenSq = fallback.lengthSquared();
        if (fbLenSq > 1.0E-12) {
            return fallback.scale(1.0 / Math.sqrt(fbLenSq));
        }
        return new Vec3d(1.0, 0.0, 0.0);
    }

    static Vec3d collisionNormal(double reqX, double reqY, double reqZ, boolean colX, boolean colZ, boolean hitCeiling) {
        Vec3d n;
        double nx = 0.0;
        double ny = 0.0;
        double nz = 0.0;
        if (colX) {
            nx += reqX > 0.0 ? -1.0 : 1.0;
        }
        if (colZ) {
            nz += reqZ > 0.0 ? -1.0 : 1.0;
        }
        if (hitCeiling) {
            ny -= 1.0;
        }
        if ((n = new Vec3d(nx, ny, nz)).lengthSquared() < 1.0E-12) {
            n = Math.abs(reqX) >= Math.abs(reqZ) ? new Vec3d(reqX > 0.0 ? -1.0 : 1.0, 0.0, 0.0) : new Vec3d(0.0, 0.0, reqZ > 0.0 ? -1.0 : 1.0);
        }
        return BloodMotion.normalizeOr(n, new Vec3d(1.0, 0.0, 0.0));
    }

    static Vec3d wallTangent(Vec3d normal, Vec3d tangent, Random rand) {
        Vec3d fallback;
        if (tangent != null && tangent.lengthSquared() > 1.0E-12) {
            return BloodMotion.normalizeOr(tangent, new Vec3d(0.0, 1.0, 0.0));
        }
        Vec3d vec3d = fallback = Math.abs(normal.y) > 0.7 ? new Vec3d(1.0, 0.0, 0.0) : new Vec3d(normal.z, 0.0, -normal.x);
        if (rand.nextBoolean()) {
            fallback = fallback.scale(-1.0);
        }
        return BloodMotion.normalizeOr(fallback, new Vec3d(1.0, 0.0, 0.0));
    }

    static final class BlockContact {
        final BlockPos pos;
        final EnumFacing face;
        final Vec3d hit;

        BlockContact(BlockPos pos, EnumFacing face, Vec3d hit) {
            this.pos = pos;
            this.face = face;
            this.hit = hit;
        }
    }
}

