/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BlockRendererDispatcher
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.util.EnumBlockRenderType
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common;

import com.eruannie_9.extragore.particle.common.Util;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class SurfaceLookup {
    public static final double PLANAR_EPS = 5.0E-4;

    public static boolean isModelRender(IBlockState st) {
        try {
            return st.getRenderType() == EnumBlockRenderType.MODEL;
        }
        catch (Throwable t) {
            return false;
        }
    }

    public static IBlockState getRenderState(World world, IBlockState base, BlockPos pos) {
        IBlockState st;
        try {
            st = base.getActualState((IBlockAccess)world, pos);
        }
        catch (Throwable t) {
            st = base;
        }
        try {
            st = st.getBlock().getExtendedState(st, (IBlockAccess)world, pos);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return st;
    }

    public static List<Util.FaceRect> collisionRects(World world, BlockPos pos, IBlockState base, EnumFacing face) {
        ArrayList<Util.FaceRect> out = new ArrayList<Util.FaceRect>();
        IBlockState st = base;
        try {
            st = base.getActualState((IBlockAccess)world, pos);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        List<AxisAlignedBB> boxes = new ArrayList<>();
        AxisAlignedBB query = new AxisAlignedBB(pos);
        try {
            st.getBlock().addCollisionBoxToList(st, world, pos, query, boxes, null, false);
        }
        catch (Throwable t) {
            return out;
        }
        block12: for (AxisAlignedBB bb : boxes) {
            double qMax;
            double qMin;
            double pMax;
            double pMin;
            double plane;
            if (bb == null) continue;
            switch (face) {
                case UP: {
                    plane = bb.maxY;
                    pMin = bb.minX;
                    pMax = bb.maxX;
                    qMin = bb.minZ;
                    qMax = bb.maxZ;
                    break;
                }
                case DOWN: {
                    plane = bb.minY;
                    pMin = bb.minX;
                    pMax = bb.maxX;
                    qMin = bb.minZ;
                    qMax = bb.maxZ;
                    break;
                }
                case EAST: {
                    plane = bb.maxX;
                    pMin = bb.minZ;
                    pMax = bb.maxZ;
                    qMin = bb.minY;
                    qMax = bb.maxY;
                    break;
                }
                case WEST: {
                    plane = bb.minX;
                    pMin = bb.minZ;
                    pMax = bb.maxZ;
                    qMin = bb.minY;
                    qMax = bb.maxY;
                    break;
                }
                case SOUTH: {
                    plane = bb.maxZ;
                    pMin = bb.minX;
                    pMax = bb.maxX;
                    qMin = bb.minY;
                    qMax = bb.maxY;
                    break;
                }
                case NORTH: {
                    plane = bb.minZ;
                    pMin = bb.minX;
                    pMax = bb.maxX;
                    qMin = bb.minY;
                    qMax = bb.maxY;
                    break;
                }
                default: {
                    continue block12;
                }
            }
            if (pMax - pMin < 1.0E-6 || qMax - qMin < 1.0E-6) continue;
            out.add(new Util.FaceRect(plane, pMin, pMax, qMin, qMax));
        }
        return out;
    }

    public static List<Util.FaceRect> modelRects(BlockPos pos, IBlockState renderState, EnumFacing face, long seed) {
        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
        IBakedModel model = brd.getModelForState(renderState);
        List<BakedQuad> quads = SurfaceLookup.collectQuadsForFace(model, renderState, face, seed);
        ArrayList<Util.FaceRect> out = new ArrayList<Util.FaceRect>();
        double[] xs = new double[4];
        double[] ys = new double[4];
        double[] zs = new double[4];
        for (BakedQuad q : quads) {
            Util.FaceRect rect;
            if (q == null || q.getFace() != face || !SurfaceLookup.readQuadXYZ(pos, q, xs, ys, zs) || (rect = SurfaceLookup.quadFaceRect(face, xs, ys, zs)) == null) continue;
            out.add(rect);
        }
        return out;
    }

    public static Hit raycastModel(BlockPos pos, IBlockState renderState, long seed, Vec3d rayStart, Vec3d rayEnd, @Nullable EnumFacing onlyFaceOrNull) {
        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
        IBakedModel model = brd.getModelForState(renderState);
        List<BakedQuad> quads = SurfaceLookup.collectQuadsForRaycast(model, renderState, onlyFaceOrNull, seed);
        double dirX = rayEnd.x - rayStart.x;
        double dirY = rayEnd.y - rayStart.y;
        double dirZ = rayEnd.z - rayStart.z;
        double bestT = Double.POSITIVE_INFINITY;
        Hit best = null;
        double[] xs = new double[4];
        double[] ys = new double[4];
        double[] zs = new double[4];
        for (BakedQuad q : quads) {
            double t2;
            Util.FaceRect rect;
            if (q == null) continue;
            EnumFacing face = q.getFace();
            if (onlyFaceOrNull != null && face != onlyFaceOrNull || !SurfaceLookup.readQuadXYZ(pos, q, xs, ys, zs) || (rect = SurfaceLookup.quadFaceRect(face, xs, ys, zs)) == null) continue;
            double t1 = SurfaceLookup.rayTriangleT01(rayStart.x, rayStart.y, rayStart.z, dirX, dirY, dirZ, xs[0], ys[0], zs[0], xs[1], ys[1], zs[1], xs[2], ys[2], zs[2]);
            if (!Double.isNaN(t1) && t1 < bestT) {
                bestT = t1;
                best = new Hit(new Vec3d(rayStart.x + dirX * t1, rayStart.y + dirY * t1, rayStart.z + dirZ * t1), face, rect.plane, rect);
            }
            if (Double.isNaN(t2 = SurfaceLookup.rayTriangleT01(rayStart.x, rayStart.y, rayStart.z, dirX, dirY, dirZ, xs[0], ys[0], zs[0], xs[2], ys[2], zs[2], xs[3], ys[3], zs[3])) || !(t2 < bestT)) continue;
            bestT = t2;
            best = new Hit(new Vec3d(rayStart.x + dirX * t2, rayStart.y + dirY * t2, rayStart.z + dirZ * t2), face, rect.plane, rect);
        }
        return best;
    }

    private static List<BakedQuad> collectQuadsForFace(IBakedModel model, IBlockState state, EnumFacing face, long seed) {
        ArrayList<BakedQuad> out = new ArrayList<BakedQuad>();
        out.addAll(model.getQuads(state, face, seed));
        out.addAll(model.getQuads(state, null, seed));
        return out;
    }

    private static List<BakedQuad> collectQuadsForRaycast(IBakedModel model, IBlockState state, @Nullable EnumFacing onlyFaceOrNull, long seed) {
        ArrayList<BakedQuad> out = new ArrayList<BakedQuad>();
        if (onlyFaceOrNull != null) {
            out.addAll(model.getQuads(state, onlyFaceOrNull, seed));
            out.addAll(model.getQuads(state, null, seed));
        } else {
            for (EnumFacing f : EnumFacing.values()) {
                out.addAll(model.getQuads(state, f, seed));
            }
            out.addAll(model.getQuads(state, null, seed));
        }
        return out;
    }

    private static boolean readQuadXYZ(BlockPos pos, BakedQuad q, double[] xs, double[] ys, double[] zs) {
        int[] vd = q.getVertexData();
        if (vd.length < 12) {
            return false;
        }
        int stride = vd.length / 4;
        for (int i = 0; i < 4; ++i) {
            float lx = Float.intBitsToFloat(vd[i * stride]);
            float ly = Float.intBitsToFloat(vd[i * stride + 1]);
            float lz = Float.intBitsToFloat(vd[i * stride + 2]);
            xs[i] = (double)((net.minecraft.util.math.Vec3i) pos).getX() + (double)lx;
            ys[i] = (double)((net.minecraft.util.math.Vec3i) pos).getY() + (double)ly;
            zs[i] = (double)((net.minecraft.util.math.Vec3i) pos).getZ() + (double)lz;
        }
        return true;
    }

    @Nullable
    private static Util.FaceRect quadFaceRect(EnumFacing face, double[] xs, double[] ys, double[] zs) {
        double qMax;
        double qMin;
        double pMax;
        double pMin;
        double maxA;
        double minA;
        switch (face) {
            case UP: 
            case DOWN: {
                minA = Util.min4(ys);
                maxA = Util.max4(ys);
                break;
            }
            case EAST: 
            case WEST: {
                minA = Util.min4(xs);
                maxA = Util.max4(xs);
                break;
            }
            default: {
                minA = Util.min4(zs);
                maxA = Util.max4(zs);
            }
        }
        if (maxA - minA > 5.0E-4) {
            return null;
        }
        double plane = 0.5 * (minA + maxA);
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            pMin = Util.min4(xs);
            pMax = Util.max4(xs);
            qMin = Util.min4(zs);
            qMax = Util.max4(zs);
        } else if (face == EnumFacing.EAST || face == EnumFacing.WEST) {
            pMin = Util.min4(zs);
            pMax = Util.max4(zs);
            qMin = Util.min4(ys);
            qMax = Util.max4(ys);
        } else {
            pMin = Util.min4(xs);
            pMax = Util.max4(xs);
            qMin = Util.min4(ys);
            qMax = Util.max4(ys);
        }
        if (pMax - pMin < 1.0E-6 || qMax - qMin < 1.0E-6) {
            return null;
        }
        return new Util.FaceRect(plane, pMin, pMax, qMin, qMax);
    }

    private static double rayTriangleT01(double ox, double oy, double oz, double dx, double dy, double dz, double v0x, double v0y, double v0z, double v1x, double v1y, double v1z, double v2x, double v2y, double v2z) {
        double EPS = 1.0E-9;
        double e1x = v1x - v0x;
        double e2z = v2z - v0z;
        double e2y = v2y - v0y;
        double hx = dy * e2z - dz * e2y;
        double e1y = v1y - v0y;
        double e2x = v2x - v0x;
        double hy = dz * e2x - dx * e2z;
        double e1z = v1z - v0z;
        double hz = dx * e2y - dy * e2x;
        double a = e1x * hx + e1y * hy + e1z * hz;
        if (a > -1.0E-9 && a < 1.0E-9) {
            return Double.NaN;
        }
        double f = 1.0 / a;
        double sx = ox - v0x;
        double sy = oy - v0y;
        double sz = oz - v0z;
        double u = f * (sx * hx + sy * hy + sz * hz);
        if (u < 0.0 || u > 1.0) {
            return Double.NaN;
        }
        double qx = sy * e1z - sz * e1y;
        double qy = sz * e1x - sx * e1z;
        double qz = sx * e1y - sy * e1x;
        double v = f * (dx * qx + dy * qy + dz * qz);
        if (v < 0.0 || u + v > 1.0) {
            return Double.NaN;
        }
        double t = f * (e2x * qx + e2y * qy + e2z * qz);
        if (t < 0.0 || t > 1.0) {
            return Double.NaN;
        }
        return t;
    }

    public static final class Hit {
        public final Vec3d hitPos;
        public final EnumFacing face;
        public final double plane;
        public final Util.FaceRect rect;

        public Hit(Vec3d hitPos, EnumFacing face, double plane, Util.FaceRect rect) {
            this.hitPos = hitPos;
            this.face = face;
            this.plane = plane;
            this.rect = rect;
        }
    }
}

