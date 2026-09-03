/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumFacing$Axis
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.blocksupport;

import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.blocksupport.PistonSupport;
import com.eruannie_9.extragore.particle.common.Util;
import com.eruannie_9.extragore.particle.common.cache.BloodCachesParticle;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class DynamicPoseSupport {
    private static final double ON_FACE_EPS = 0.002;
    private static final double AABB_MATCH_EPS = 0.002;
    private static final double TRANSFORM_MAX_SCORE = 0.003;
    private static final double LOCAL_CHANGE_EPS_SQ = 1.0E-10;

    public static void captureOnAttach(@Nonnull ParticleBlood blood) {
        blood.cache.host.piece = null;
        blood.cache.host.poseSnapPrev = false;
        blood.cache.host.poseGraceTicks = 0;
        World world = blood.getParticleWorld();
        if (world == null || blood.stuckPos == null || blood.stuckFace == null || blood.stuckLocalOnPlane == null) {
            return;
        }
        List<AxisAlignedBB> boxes = DynamicPoseSupport.getLocalBoxes(world, blood.stuckPos, blood.cache.host.base);
        blood.cache.host.piece = DynamicPoseSupport.pickBoxForAnchor(blood.stuckLocalOnPlane, blood.stuckFace, boxes);
        if (blood.cache.host.piece == null) {
            blood.cache.host.piece = DynamicPoseSupport.unionOrNull(boxes);
        }
    }

    public static void tick(@Nonnull ParticleBlood p) {
        Vec3d snappedLocal;
        World world = p.getParticleWorld();
        if (world == null || p.stuckPos == null || p.stuckFace == null || p.stuckLocalOnPlane == null) {
            DynamicPoseSupport.markSupportLost(p);
            return;
        }
        List<AxisAlignedBB> boxes = DynamicPoseSupport.getLocalBoxes(world, p.stuckPos, p.cache.host.base);
        if (boxes.isEmpty()) {
            DynamicPoseSupport.markSupportLost(p);
            return;
        }
        AxisAlignedBB oldPiece = p.cache.host.piece;
        AxisAlignedBB direct = DynamicPoseSupport.pickBoxForAnchor(p.stuckLocalOnPlane, p.stuckFace, boxes);
        if (oldPiece == null) {
            AxisAlignedBB init;
            AxisAlignedBB axisAlignedBB = init = direct != null ? direct : DynamicPoseSupport.unionOrNull(boxes);
            if (init == null) {
                DynamicPoseSupport.markSupportLost(p);
                return;
            }
            p.cache.host.piece = init;
            return;
        }
        if (direct != null && DynamicPoseSupport.aabbEqEps(direct, oldPiece, 0.002)) {
            return;
        }
        Candidate best = null;
        for (AxisAlignedBB to : boxes) {
            EnumFacing newFace;
            Vec3d newLocal;
            Transform t = DynamicPoseSupport.solveRigid90(oldPiece, to);
            if (t == null || !DynamicPoseSupport.isOnBoxFace(newLocal = t.apply(p.stuckLocalOnPlane), newFace = t.apply(p.stuckFace), to, 0.002)) continue;
            newLocal = DynamicPoseSupport.projectToFace(newLocal, newFace, to);
            if (best != null && !(t.score < best.score)) continue;
            best = new Candidate(to, t, newLocal, newFace);
        }
        if (best != null && best.score <= 0.003) {
            snappedLocal = Util.snap16(best.newLocal);
            boolean pieceChanged = !DynamicPoseSupport.aabbEqEps(best.to, oldPiece, 0.002);
            boolean faceChanged = best.newFace != p.stuckFace;
            boolean localChanged = DynamicPoseSupport.distSq(snappedLocal, p.stuckLocalOnPlane) > 1.0E-10;
            p.stuckLocalOnPlane = snappedLocal;
            p.stuckFace = best.newFace;
            p.cache.host.piece = best.to;
            if (pieceChanged || faceChanged || localChanged) {
                DynamicPoseSupport.invalidatePoseGeometry(p, true);
            }
            return;
        }
        if (direct != null) {
            snappedLocal = Util.snap16(DynamicPoseSupport.projectToFace(p.stuckLocalOnPlane, p.stuckFace, direct));
            boolean pieceChanged = !DynamicPoseSupport.aabbEqEps(direct, oldPiece, 0.002);
            boolean localChanged = DynamicPoseSupport.distSq(snappedLocal, p.stuckLocalOnPlane) > 1.0E-10;
            p.stuckLocalOnPlane = snappedLocal;
            p.cache.host.piece = direct;
            if (pieceChanged || localChanged) {
                DynamicPoseSupport.invalidatePoseGeometry(p, true);
            }
            return;
        }
        DynamicPoseSupport.markSupportLost(p);
    }

    private static void invalidatePoseGeometry(@Nonnull ParticleBlood blood, boolean snapPrev) {
        BloodCachesParticle.invalidateShape(blood);
        BloodCachesParticle.invalidateView(blood);
        if (snapPrev) {
            blood.cache.host.poseSnapPrev = true;
            blood.cache.host.poseGraceTicks = Math.max(blood.cache.host.poseGraceTicks, 4);
        }
    }

    private static void markSupportLost(@Nonnull ParticleBlood blood) {
        blood.cache.host.piece = null;
        blood.cache.host.poseSnapPrev = false;
        blood.cache.host.poseGraceTicks = 0;
        BloodCachesParticle.invalidateShape(blood);
        BloodCachesParticle.invalidateView(blood);
    }

    private static double distSq(@Nonnull Vec3d a, @Nonnull Vec3d b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        double dz = a.z - b.z;
        return dx * dx + dy * dy + dz * dz;
    }

    @Nonnull
    private static List<AxisAlignedBB> getLocalBoxes(@Nonnull World world, @Nonnull BlockPos pos, @Nullable IBlockState base) {
        PistonSupport.MovingInfo mi;
        ArrayList<AxisAlignedBB> out = new ArrayList<AxisAlignedBB>();
        IBlockState st = base;
        if (st == null) {
            try {
                st = world.getBlockState(pos);
            }
            catch (Throwable t) {
                st = null;
            }
        }
        if ((mi = PistonSupport.getMovingInfo(world, pos, 1.0f)) != null && mi.movedState != null) {
            st = mi.movedState;
        }
        if (st == null) {
            return out;
        }
        try {
            AxisAlignedBB query = new AxisAlignedBB(pos, pos.add(1, 1, 1));
            st.getBlock().addCollisionBoxToList(st, world, pos, query, out, null, false);
        }
        catch (Throwable query) {
            // empty catch block
        }
        for (int i = 0; i < out.size(); ++i) {
            AxisAlignedBB bb = (AxisAlignedBB)out.get(i);
            if (bb == null) continue;
            out.set(i, bb.offset((double)(-((net.minecraft.util.math.Vec3i) pos).getX()), (double)(-((net.minecraft.util.math.Vec3i) pos).getY()), (double)(-((net.minecraft.util.math.Vec3i) pos).getZ())));
        }
        if (out.isEmpty()) {
            try {
                AxisAlignedBB bb = st.getBoundingBox((IBlockAccess)world, pos);
                out.add(bb);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return out;
    }

    @Nullable
    private static AxisAlignedBB pickBoxForAnchor(Vec3d local, EnumFacing face, List<AxisAlignedBB> boxes) {
        AxisAlignedBB best = null;
        double bestD = Double.POSITIVE_INFINITY;
        for (AxisAlignedBB bb : boxes) {
            double d;
            if (bb == null || (d = DynamicPoseSupport.facePlaneDistance(local, face, bb)) < 0.0 || !(d < bestD)) continue;
            bestD = d;
            best = bb;
        }
        return best;
    }

    private static double facePlaneDistance(Vec3d p, EnumFacing facing, AxisAlignedBB bb) {
        double e = 0.002;
        switch (facing) {
            case EAST: {
                if (p.y < bb.minY - 0.002 || p.y > bb.maxY + 0.002 || p.z < bb.minZ - 0.002 || p.z > bb.maxZ + 0.002) {
                    return -1.0;
                }
                return Math.abs(p.x - bb.maxX);
            }
            case WEST: {
                if (p.y < bb.minY - 0.002 || p.y > bb.maxY + 0.002 || p.z < bb.minZ - 0.002 || p.z > bb.maxZ + 0.002) {
                    return -1.0;
                }
                return Math.abs(p.x - bb.minX);
            }
            case UP: {
                if (p.x < bb.minX - 0.002 || p.x > bb.maxX + 0.002 || p.z < bb.minZ - 0.002 || p.z > bb.maxZ + 0.002) {
                    return -1.0;
                }
                return Math.abs(p.y - bb.maxY);
            }
            case DOWN: {
                if (p.x < bb.minX - 0.002 || p.x > bb.maxX + 0.002 || p.z < bb.minZ - 0.002 || p.z > bb.maxZ + 0.002) {
                    return -1.0;
                }
                return Math.abs(p.y - bb.minY);
            }
            case SOUTH: {
                if (p.x < bb.minX - 0.002 || p.x > bb.maxX + 0.002 || p.y < bb.minY - 0.002 || p.y > bb.maxY + 0.002) {
                    return -1.0;
                }
                return Math.abs(p.z - bb.maxZ);
            }
        }
        if (p.x < bb.minX - 0.002 || p.x > bb.maxX + 0.002 || p.y < bb.minY - 0.002 || p.y > bb.maxY + 0.002) {
            return -1.0;
        }
        return Math.abs(p.z - bb.minZ);
    }

    private static boolean isOnBoxFace(Vec3d p, EnumFacing facing, AxisAlignedBB bb, double eps) {
        switch (facing) {
            case EAST: {
                return Math.abs(p.x - bb.maxX) <= eps && p.y >= bb.minY - eps && p.y <= bb.maxY + eps && p.z >= bb.minZ - eps && p.z <= bb.maxZ + eps;
            }
            case WEST: {
                return Math.abs(p.x - bb.minX) <= eps && p.y >= bb.minY - eps && p.y <= bb.maxY + eps && p.z >= bb.minZ - eps && p.z <= bb.maxZ + eps;
            }
            case UP: {
                return Math.abs(p.y - bb.maxY) <= eps && p.x >= bb.minX - eps && p.x <= bb.maxX + eps && p.z >= bb.minZ - eps && p.z <= bb.maxZ + eps;
            }
            case DOWN: {
                return Math.abs(p.y - bb.minY) <= eps && p.x >= bb.minX - eps && p.x <= bb.maxX + eps && p.z >= bb.minZ - eps && p.z <= bb.maxZ + eps;
            }
            case SOUTH: {
                return Math.abs(p.z - bb.maxZ) <= eps && p.x >= bb.minX - eps && p.x <= bb.maxX + eps && p.y >= bb.minY - eps && p.y <= bb.maxY + eps;
            }
        }
        return Math.abs(p.z - bb.minZ) <= eps && p.x >= bb.minX - eps && p.x <= bb.maxX + eps && p.y >= bb.minY - eps && p.y <= bb.maxY + eps;
    }

    private static Vec3d projectToFace(Vec3d p, EnumFacing facing, AxisAlignedBB bb) {
        double x = p.x;
        double y = p.y;
        double z = p.z;
        switch (facing) {
            case EAST: {
                x = bb.maxX;
                break;
            }
            case WEST: {
                x = bb.minX;
                break;
            }
            case UP: {
                y = bb.maxY;
                break;
            }
            case DOWN: {
                y = bb.minY;
                break;
            }
            case SOUTH: {
                z = bb.maxZ;
                break;
            }
            case NORTH: {
                z = bb.minZ;
            }
        }
        x = Util.clamp(x, bb.minX, bb.maxX);
        y = Util.clamp(y, bb.minY, bb.maxY);
        z = Util.clamp(z, bb.minZ, bb.maxZ);
        return new Vec3d(x, y, z);
    }

    @Nullable
    private static AxisAlignedBB unionOrNull(List<AxisAlignedBB> boxes) {
        AxisAlignedBB u = null;
        for (AxisAlignedBB b : boxes) {
            if (b == null) continue;
            u = u == null ? b : u.union(b);
        }
        return u;
    }

    private static boolean aabbEqEps(AxisAlignedBB a, AxisAlignedBB b, double eps) {
        return Math.abs(a.minX - b.minX) <= eps && Math.abs(a.minY - b.minY) <= eps && Math.abs(a.minZ - b.minZ) <= eps && Math.abs(a.maxX - b.maxX) <= eps && Math.abs(a.maxY - b.maxY) <= eps && Math.abs(a.maxZ - b.maxZ) <= eps;
    }

    @Nullable
    private static Transform solveRigid90(AxisAlignedBB from, AxisAlignedBB to) {
        Vec3d pivot = DynamicPoseSupport.intersectionCenter(from, to);
        if (pivot == null) {
            pivot = new Vec3d(0.5, 0.5, 0.5);
        }
        Transform best = null;
        for (EnumFacing.Axis ax : new EnumFacing.Axis[]{EnumFacing.Axis.X, EnumFacing.Axis.Y, EnumFacing.Axis.Z}) {
            for (int steps = 1; steps <= 3; ++steps) {
                AxisAlignedBB r = DynamicPoseSupport.rotateAabb(from, ax, steps, pivot);
                double s = DynamicPoseSupport.scoreAabb(r, to);
                if (best != null && !(s < best.score)) continue;
                best = new Transform(ax, steps, pivot, s);
            }
        }
        return best;
    }

    private static double scoreAabb(AxisAlignedBB a, AxisAlignedBB b) {
        return Math.abs(a.minX - b.minX) + Math.abs(a.minY - b.minY) + Math.abs(a.minZ - b.minZ) + Math.abs(a.maxX - b.maxX) + Math.abs(a.maxY - b.maxY) + Math.abs(a.maxZ - b.maxZ);
    }

    @Nullable
    private static Vec3d intersectionCenter(AxisAlignedBB a, AxisAlignedBB b) {
        double x0 = Math.max(a.minX, b.minX);
        double x1 = Math.min(a.maxX, b.maxX);
        double y0 = Math.max(a.minY, b.minY);
        double y1 = Math.min(a.maxY, b.maxY);
        double z0 = Math.max(a.minZ, b.minZ);
        double z1 = Math.min(a.maxZ, b.maxZ);
        if (x1 <= x0 || y1 <= y0 || z1 <= z0) {
            return null;
        }
        return new Vec3d((x0 + x1) * 0.5, (y0 + y1) * 0.5, (z0 + z1) * 0.5);
    }

    private static AxisAlignedBB rotateAabb(AxisAlignedBB bb, EnumFacing.Axis axis, int steps, Vec3d pivot) {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        double[] xs = new double[]{bb.minX, bb.maxX};
        double[] ys = new double[]{bb.minY, bb.maxY};
        double[] zs = new double[]{bb.minZ, bb.maxZ};
        for (double x : xs) {
            for (double y : ys) {
                for (double z : zs) {
                    Vec3d p = new Vec3d(x, y, z);
                    for (int i = 0; i < (steps & 3); ++i) {
                        p = DynamicPoseSupport.rotatePoint90(p, axis, pivot);
                    }
                    if (p.x < minX) {
                        minX = p.x;
                    }
                    if (p.y < minY) {
                        minY = p.y;
                    }
                    if (p.z < minZ) {
                        minZ = p.z;
                    }
                    if (p.x > maxX) {
                        maxX = p.x;
                    }
                    if (p.y > maxY) {
                        maxY = p.y;
                    }
                    if (!(p.z > maxZ)) continue;
                    maxZ = p.z;
                }
            }
        }
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static Vec3d rotatePoint90(Vec3d p, EnumFacing.Axis axis, Vec3d pivot) {
        double x = p.x - pivot.x;
        double y = p.y - pivot.y;
        double z = p.z - pivot.z;
        double nx = x;
        double ny = y;
        double nz = z;
        switch (axis) {
            case Y: {
                nx = -z;
                nz = x;
                break;
            }
            case X: {
                ny = -z;
                nz = y;
                break;
            }
            case Z: {
                nx = -y;
                ny = x;
            }
        }
        return new Vec3d(nx + pivot.x, ny + pivot.y, nz + pivot.z);
    }

    private static EnumFacing rotateFacing90(EnumFacing facing, EnumFacing.Axis axis) {
        switch (axis) {
            case Y: {
                return facing.getAxis().isVertical() ? facing : facing.rotateY();
            }
            case X: {
                switch (facing) {
                    case UP: {
                        return EnumFacing.SOUTH;
                    }
                    case SOUTH: {
                        return EnumFacing.DOWN;
                    }
                    case DOWN: {
                        return EnumFacing.NORTH;
                    }
                    case NORTH: {
                        return EnumFacing.UP;
                    }
                }
                return facing;
            }
        }
        switch (facing) {
            case UP: {
                return EnumFacing.WEST;
            }
            case WEST: {
                return EnumFacing.DOWN;
            }
            case DOWN: {
                return EnumFacing.EAST;
            }
            case EAST: {
                return EnumFacing.UP;
            }
        }
        return facing;
    }

    private static final class Transform {
        final EnumFacing.Axis axis;
        final int steps;
        final Vec3d pivot;
        final double score;

        Transform(EnumFacing.Axis axis, int steps, Vec3d pivot, double score) {
            this.axis = axis;
            this.steps = steps & 3;
            this.pivot = pivot;
            this.score = score;
        }

        Vec3d apply(Vec3d v) {
            Vec3d out = v;
            for (int i = 0; i < this.steps; ++i) {
                out = DynamicPoseSupport.rotatePoint90(out, this.axis, this.pivot);
            }
            return out;
        }

        EnumFacing apply(EnumFacing facing) {
            EnumFacing out = facing;
            for (int i = 0; i < this.steps; ++i) {
                out = DynamicPoseSupport.rotateFacing90(out, this.axis);
            }
            return out;
        }
    }

    private static final class Candidate {
        final AxisAlignedBB to;
        final Transform transform;
        final Vec3d newLocal;
        final EnumFacing newFace;
        final double score;

        Candidate(AxisAlignedBB to, Transform transform, Vec3d newLocal, EnumFacing newFace) {
            this.to = to;
            this.transform = transform;
            this.newLocal = newLocal;
            this.newFace = newFace;
            this.score = transform.score;
        }
    }
}

