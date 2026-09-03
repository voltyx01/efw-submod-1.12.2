/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumFacing$Axis
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class Util {
    public static final Vec3d ZERO = new Vec3d(0.0, 0.0, 0.0);

    public static boolean isVerticalFace(@Nullable EnumFacing face) {
        return face != null && face.getAxis().isHorizontal();
    }

    public static float randBetween(Random rand, float min, float max) {
        if (rand == null) {
            return min;
        }
        if (max <= min) {
            return min;
        }
        return min + rand.nextFloat() * (max - min);
    }

    public static float clamp01(float v) {
        if (v < 0.0f) {
            return 0.0f;
        }
        return Math.min(v, 1.0f);
    }

    public static double clamp01(double v) {
        if (v < 0.0) {
            return 0.0;
        }
        return Math.min(v, 1.0);
    }

    public static double clamp(double v, double lo, double hi) {
        if (v < lo) {
            return lo;
        }
        return Math.min(v, hi);
    }

    public static int clampInt(int v, int lo, int hi) {
        if (v < lo) {
            return lo;
        }
        return Math.min(v, hi);
    }

    public static float smoothstep01(float t01) {
        float t = Util.clamp01(t01);
        return t * t * (3.0f - 2.0f * t);
    }

    public static float easeOutCubic01(float t01) {
        float t = Util.clamp01(t01);
        float inv = 1.0f - t;
        return 1.0f - inv * inv * inv;
    }

    public static float progress01(int age, int startAge, int durationTicks, int maxAge) {
        if (durationTicks == 0) {
            return 1.0f;
        }
        int elapsed = age - startAge;
        if (elapsed < 0) {
            elapsed = 0;
        }
        int denom = durationTicks < 0 ? Math.max(1, maxAge - startAge) : Math.max(1, durationTicks);
        return Util.clamp01((float)elapsed / (float)denom);
    }

    public static double snap16(double a) {
        double s = (double)Math.round(a * 16.0) / 16.0;
        return Math.abs(a - s) < 1.0E-9 ? s : a;
    }

    public static Vec3d snap16(Vec3d v) {
        return new Vec3d(Util.snap16(v.x), Util.snap16(v.y), Util.snap16(v.z));
    }

    public static double min4(double[] a) {
        return Math.min(Math.min(a[0], a[1]), Math.min(a[2], a[3]));
    }

    public static double max4(double[] a) {
        return Math.max(Math.max(a[0], a[1]), Math.max(a[2], a[3]));
    }

    public static double localP(EnumFacing face, Vertex v) {
        FaceSpace fs = FaceSpace.of(face);
        return fs.p(v.x, v.y, v.z);
    }

    public static double localQ(EnumFacing face, Vertex v) {
        FaceSpace fs = FaceSpace.of(face);
        return fs.q(v.x, v.y, v.z);
    }

    public static double localP(EnumFacing face, Vec3d v) {
        FaceSpace fs = FaceSpace.of(face);
        return fs.p(v.x, v.y, v.z);
    }

    public static double localQ(EnumFacing face, Vec3d v) {
        FaceSpace fs = FaceSpace.of(face);
        return fs.q(v.x, v.y, v.z);
    }

    public static double planeCoord(EnumFacing face, Vec3d pointOnPlane) {
        if (face == null || pointOnPlane == null) {
            return 0.0;
        }
        FaceSpace fs = FaceSpace.of(face);
        return fs.plane(pointOnPlane.x, pointOnPlane.y, pointOnPlane.z);
    }

    public static FaceRect offsetRect(EnumFacing face, FaceRect r, Vec3d off) {
        if (r == null || off == null || face == null) {
            return r;
        }
        FaceSpace fs = FaceSpace.of(face);
        double planeShift = FaceSpace.axisValue(fs.planeAxis, off.x, off.y, off.z);
        double pShift = FaceSpace.axisValue(fs.pAxis, off.x, off.y, off.z);
        double qShift = FaceSpace.axisValue(fs.qAxis, off.x, off.y, off.z);
        return new FaceRect(r.plane + planeShift, r.pMin + pShift, r.pMax + pShift, r.qMin + qShift, r.qMax + qShift);
    }

    public static Vec3d pointFromPlanePQ(EnumFacing face, double plane, double p, double q) {
        return FaceSpace.of(face).point(plane, p, q);
    }

    public static double distanceSqToRect(EnumFacing face, FaceRect r, Vec3d hit) {
        double p = Util.localP(face, hit);
        double q = Util.localQ(face, hit);
        double cp = Util.clamp(p, r.pMin, r.pMax);
        double cq = Util.clamp(q, r.qMin, r.qMax);
        double dp = p - cp;
        double dq = q - cq;
        return dp * dp + dq * dq;
    }

    @Nullable
    public static FaceRect pickBestRect(List<FaceRect> rects, EnumFacing face, Vec3d hit) {
        if (rects == null || rects.isEmpty()) {
            return null;
        }
        if (hit == null) {
            return rects.get(0);
        }
        double p = Util.localP(face, hit);
        double q = Util.localQ(face, hit);
        FaceRect best = null;
        double bestD = Double.POSITIVE_INFINITY;
        for (FaceRect r : rects) {
            double cq;
            double dq;
            double cp = Util.clamp(p, r.pMin, r.pMax);
            double dp = p - cp;
            double d = dp * dp + (dq = q - (cq = Util.clamp(q, r.qMin, r.qMax))) * dq;
            if (!(d < bestD)) continue;
            bestD = d;
            best = r;
        }
        return best;
    }

    public static Vec3d rectCenter(BlockPos bp, EnumFacing face, FaceRect r) {
        double pc = 0.5 * (r.pMin + r.pMax);
        double qc = 0.5 * (r.qMin + r.qMax);
        return Util.pointFromPlanePQ(face, r.plane, pc, qc);
    }

    public static Vec3d clampToRectOnFacePlane(EnumFacing face, FaceRect rect, Vec3d raw) {
        double pad = 5.0E-4;
        double p = Util.localP(face, raw);
        double q = Util.localQ(face, raw);
        p = Util.clamp(p, rect.pMin + 5.0E-4, rect.pMax - 5.0E-4);
        q = Util.clamp(q, rect.qMin + 5.0E-4, rect.qMax - 5.0E-4);
        return Util.pointFromPlanePQ(face, rect.plane, p, q);
    }

    public static int blockCoordForPlane(EnumFacing face, double plane) {
        double eps = 1.0E-6;
        switch (face) {
            case SOUTH: 
            case EAST: 
            case UP: {
                return MathHelper.floor((double)(plane - 1.0E-6));
            }
        }
        return MathHelper.floor((double)(plane + 1.0E-6));
    }

    public static List<Vertex> clipToRect(List<Vertex> poly, EnumFacing face, FaceRect r) {
        List<Vertex> out = poly != null ? poly : new ArrayList<Vertex>(0);
        out = Util.clipBound(out, face, ClipAxis.P, r.pMin, true);
        out = Util.clipBound(out, face, ClipAxis.P, r.pMax, false);
        out = Util.clipBound(out, face, ClipAxis.Q, r.qMin, true);
        out = Util.clipBound(out, face, ClipAxis.Q, r.qMax, false);
        return out;
    }

    private static double coord(EnumFacing face, Vertex v, ClipAxis axis) {
        return axis == ClipAxis.P ? Util.localP(face, v) : Util.localQ(face, v);
    }

    private static List<Vertex> clipBound(List<Vertex> in, EnumFacing face, ClipAxis axis, double bound, boolean keepGreaterOrEqual) {
        ArrayList<Vertex> out = new ArrayList<Vertex>();
        if (in == null || in.isEmpty()) {
            return out;
        }
        Vertex s = in.get(in.size() - 1);
        double sC = Util.coord(face, s, axis);
        boolean sIn = keepGreaterOrEqual ? sC >= bound : sC <= bound;
        for (Vertex e : in) {
            double eC = Util.coord(face, e, axis);
            boolean eIn = keepGreaterOrEqual ? eC >= bound : eC <= bound;
            if (eIn) {
                if (!sIn) {
                    out.add(Util.intersectAtBound(s, e, sC, eC, bound));
                }
                out.add(e);
            } else if (sIn) {
                out.add(Util.intersectAtBound(s, e, sC, eC, bound));
            }
            s = e;
            sC = eC;
            sIn = eIn;
        }
        return out;
    }

    private static Vertex intersectAtBound(Vertex a, Vertex b, double aC, double bC, double bound) {
        double denom = bC - aC;
        double t = denom == 0.0 ? 0.0 : (bound - aC) / denom;
        return Util.lerp(a, b, t);
    }

    private static Vertex lerp(Vertex a, Vertex b, double t) {
        return new Vertex(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t, a.z + (b.z - a.z) * t, (float)((double)a.u + (double)(b.u - a.u) * t), (float)((double)a.v + (double)(b.v - a.v) * t));
    }

    private static enum ClipAxis {
        P,
        Q;

    }

    public static enum FaceSpace {
        UP(EnumFacing.UP, EnumFacing.Axis.Y, EnumFacing.Axis.X, EnumFacing.Axis.Z),
        DOWN(EnumFacing.DOWN, EnumFacing.Axis.Y, EnumFacing.Axis.X, EnumFacing.Axis.Z),
        NORTH(EnumFacing.NORTH, EnumFacing.Axis.Z, EnumFacing.Axis.X, EnumFacing.Axis.Y),
        SOUTH(EnumFacing.SOUTH, EnumFacing.Axis.Z, EnumFacing.Axis.X, EnumFacing.Axis.Y),
        EAST(EnumFacing.EAST, EnumFacing.Axis.X, EnumFacing.Axis.Z, EnumFacing.Axis.Y),
        WEST(EnumFacing.WEST, EnumFacing.Axis.X, EnumFacing.Axis.Z, EnumFacing.Axis.Y);

        public final EnumFacing face;
        public final EnumFacing.Axis planeAxis;
        public final EnumFacing.Axis pAxis;
        public final EnumFacing.Axis qAxis;

        private FaceSpace(EnumFacing face, EnumFacing.Axis planeAxis, EnumFacing.Axis pAxis, EnumFacing.Axis qAxis) {
            this.face = face;
            this.planeAxis = planeAxis;
            this.pAxis = pAxis;
            this.qAxis = qAxis;
        }

        public static FaceSpace of(@Nullable EnumFacing face) {
            if (face == null) {
                return UP;
            }
            switch (face) {
                case DOWN: {
                    return DOWN;
                }
                case NORTH: {
                    return NORTH;
                }
                case SOUTH: {
                    return SOUTH;
                }
                case EAST: {
                    return EAST;
                }
                case WEST: {
                    return WEST;
                }
            }
            return UP;
        }

        public double plane(double x, double y, double z) {
            return FaceSpace.axisValue(this.planeAxis, x, y, z);
        }

        public double p(double x, double y, double z) {
            return FaceSpace.axisValue(this.pAxis, x, y, z);
        }

        public double q(double x, double y, double z) {
            return FaceSpace.axisValue(this.qAxis, x, y, z);
        }

        public Vec3d point(double plane, double p, double q) {
            double x = this.valueForAxis(EnumFacing.Axis.X, plane, p, q);
            double y = this.valueForAxis(EnumFacing.Axis.Y, plane, p, q);
            double z = this.valueForAxis(EnumFacing.Axis.Z, plane, p, q);
            return new Vec3d(x, y, z);
        }

        private static double axisValue(EnumFacing.Axis axis, double x, double y, double z) {
            switch (axis) {
                case X: {
                    return x;
                }
                case Y: {
                    return y;
                }
            }
            return z;
        }

        private double valueForAxis(EnumFacing.Axis axis, double plane, double p, double q) {
            if (axis == this.planeAxis) {
                return plane;
            }
            if (axis == this.pAxis) {
                return p;
            }
            if (axis == this.qAxis) {
                return q;
            }
            return 0.0;
        }
    }

    public static final class SurfaceLayer {
        private static final double SURFACE_EPSILON = 0.001;
        private static final int LAYER_COUNT = 64;
        private static final double LAYER_STEP = 2.0E-5;
        private static int NEXT_LAYER = 0;

        public static double nextSurfaceOffset() {
            int layer = (NEXT_LAYER++ & Integer.MAX_VALUE) % 64;
            return 0.001 + (double)layer * 2.0E-5;
        }
    }

    public static final class RangeF {
        public final float min;
        public final float max;

        private RangeF(float min, float max) {
            this.min = min;
            this.max = max;
        }

        public static RangeF of(float a, float b) {
            return a <= b ? new RangeF(a, b) : new RangeF(b, a);
        }

        public RangeF clampMin(float minClamp) {
            float nMin = Math.max(minClamp, this.min);
            float nMax = Math.max(nMin, this.max);
            if (nMin == this.min && nMax == this.max) {
                return this;
            }
            return new RangeF(nMin, nMax);
        }
    }

    public static final class FaceRect {
        public final double plane;
        public final double pMin;
        public final double pMax;
        public final double qMin;
        public final double qMax;

        public FaceRect(double plane, double pMin, double pMax, double qMin, double qMax) {
            this.plane = plane;
            this.pMin = pMin;
            this.pMax = pMax;
            this.qMin = qMin;
            this.qMax = qMax;
        }
    }

    public static final class Vertex {
        public final double x;
        public final double y;
        public final double z;
        public final float u;
        public final float v;

        public Vertex(double x, double y, double z, float u, float v) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.u = u;
            this.v = v;
        }
    }
}

