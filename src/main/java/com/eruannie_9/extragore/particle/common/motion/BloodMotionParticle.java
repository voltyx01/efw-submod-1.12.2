/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.motion;

import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.common.Util;
import com.eruannie_9.extragore.particle.common.motion.BloodMotion;
import com.eruannie_9.extragore.particle.common.motion.BloodMotionHeavy;
import com.eruannie_9.extragore.particle.common.motion.BloodMotionHot;
import com.eruannie_9.extragore.particle.common.motion.BloodMotionMagic;
import com.eruannie_9.extragore.particle.common.motion.BloodMotionSlimy;
import com.eruannie_9.extragore.particle.common.surface.BloodSurfaceAttach;
import com.eruannie_9.extragore.particle.state.BloodMagic;
import com.eruannie_9.extragore.particle.state.BloodSlimy;
import java.util.ArrayList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodMotionParticle {
    private static final int IDLE_TICKS_TO_FIX = 2;
    private static final double IDLE_MOVE_SQ = 1.0E-12;
    private static final double IDLE_SPEED_SQ_AIR = 1.0E-12;
    private static final double IDLE_SPEED_SQ_GROUND = 1.0E-8;
    private static final double KICK_PUSH_MIN = 0.07;
    private static final double KICK_PUSH_MAX = 0.14;
    private static final double KICK_JITTER = 0.02;
    private static final double KICK_Y = -0.07;
    private static final double KICK_NUDGE = 0.015;
    private static final double RAY_EXT = 0.01;

    public static void move(ParticleBlood p, double x, double y, double z) {
        boolean colZ;
        if (p.isStuck) {
            return;
        }
        double oldX = p.posX;
        double oldY = p.posY;
        double oldZ = p.posZ;
        Vec3d start = new Vec3d(oldX, oldY, oldZ);
        double reqX = x;
        double reqY = y;
        double reqZ = z;
        Vec3d end = start.add(reqX, reqY, reqZ);
        RayTraceResult best = BloodMotionParticle.findHit(p, start, end);
        if (best != null && best.typeOfHit == RayTraceResult.Type.BLOCK && best.sideHit != null) {
            boolean deferSlimyWallAttach = BloodSlimy.isSlimy(p) && best.sideHit.getAxis().isHorizontal() && Math.max(Math.abs(reqX), Math.abs(reqZ)) > 0.01;
            Vec3d hit = best.hitVec;
            if (!deferSlimyWallAttach && hit != null && BloodSurfaceAttach.tryAttach(p, best.getBlockPos(), best.sideHit, start, end, hit)) {
                return;
            }
        }
        p.vanillaMove(reqX, reqY, reqZ);
        double dx = p.posX - oldX;
        double dy = p.posY - oldY;
        double dz = p.posZ - oldZ;
        double bx = Math.abs(reqX - dx);
        double by = Math.abs(reqY - dy);
        double bz = Math.abs(reqZ - dz);
        double eps = 1.0E-7;
        boolean colX = bx > 1.0E-7;
        boolean colY = by > 1.0E-7;
        boolean bl = colZ = bz > 1.0E-7;
        if (!(colX || colY || colZ)) {
            return;
        }
        if (BloodMotionHeavy.handleCollision(p, reqX, reqY, reqZ, colX, colY, colZ)) {
            return;
        }
        ArrayList<EnumFacing> faces = new ArrayList<EnumFacing>(3);
        if (colX) {
            faces.add(reqX > 0.0 ? EnumFacing.WEST : EnumFacing.EAST);
        }
        if (colZ) {
            faces.add(reqZ > 0.0 ? EnumFacing.NORTH : EnumFacing.SOUTH);
        }
        if (colY) {
            faces.add(reqY < 0.0 ? EnumFacing.UP : EnumFacing.DOWN);
        }
        for (EnumFacing face : faces) {
            Vec3d at;
            RayTraceResult hit;
            boolean deferSlimyWallAttach = BloodSlimy.isSlimy(p) && face.getAxis().isHorizontal() && Math.max(Math.abs(reqX), Math.abs(reqZ)) > 0.01;
            if (deferSlimyWallAttach || (hit = BloodMotionParticle.rayFace(p, face, 0.8)) == null || hit.typeOfHit != RayTraceResult.Type.BLOCK || hit.sideHit == null) continue;
            Vec3d vec3d = at = hit.hitVec != null ? hit.hitVec : new Vec3d(p.posX, p.posY, p.posZ);
            if (!BloodSurfaceAttach.tryAttach(p, hit.getBlockPos(), hit.sideHit, start, new Vec3d(p.posX, p.posY, p.posZ), at)) continue;
            return;
        }
        if (BloodSlimy.isSlimy(p)) {
            BloodMotionSlimy.collide(p, reqX, reqY, reqZ, colX, colY, colZ);
        }
    }

    public static void prime(ParticleBlood p) {
        BloodMotionSlimy.prime(p);
        BloodMotionMagic.prime(p);
    }

    public static void tick(ParticleBlood p) {
        if (p == null) {
            return;
        }
        if (BloodMagic.isMagic(p)) {
            p.setCanCollide(true);
            BloodMotionMagic.tick(p);
            BloodMotionHot.tickBurst(p);
            BloodMotionHot.tickLift(p);
            return;
        }
        p.setCanCollide(true);
        BloodMotionParticle.tickIdle(p);
        BloodMotionHot.tickBurst(p);
    }

    static void tickIdle(ParticleBlood p) {
        boolean idle;
        BloodMotionParticle.tickWobble(p);
        BloodMotionParticle.tickFall(p);
        double speedSq = p.motionX * p.motionX + p.motionY * p.motionY + p.motionZ * p.motionZ;
        double dx = p.posX - p.prevPosX;
        double dy = p.posY - p.prevPosY;
        double dz = p.posZ - p.prevPosZ;
        double movedSq = dx * dx + dy * dy + dz * dz;
        double speedLimit = p.isOnGroundFlag() ? 1.0E-8 : 1.0E-12;
        boolean bl = idle = speedSq < speedLimit && movedSq < 1.0E-12;
        if (!idle) {
            p.idleTicks = 0;
            return;
        }
        ++p.idleTicks;
        if (p.idleTicks < 2) {
            return;
        }
        if (BloodMotionParticle.tryAttach(p)) {
            p.idleTicks = 0;
            return;
        }
        BloodMotion.BlockContact down = BloodMotionParticle.rayDown(p, 0.06, 0.9);
        if (BloodSlimy.isSlimy(p)) {
            EnumFacing side = down != null ? BloodMotionParticle.pickPush(p, down.pos) : null;
            BloodMotionSlimy.idleKick(p, side);
        } else if (down != null) {
            EnumFacing side = BloodMotionParticle.pickPush(p, down.pos);
            BloodMotionParticle.kick(p, side);
        } else {
            BloodMotionParticle.kick(p, null);
        }
        p.idleTicks = 0;
    }

    private static RayTraceResult rayFace(ParticleBlood p, EnumFacing face, double dist) {
        if (face == null) {
            return null;
        }
        Vec3d start = new Vec3d(p.posX, p.posY, p.posZ);
        Vec3d end = start.add((double)(-face.getXOffset()) * dist, (double)(-face.getYOffset()) * dist, (double)(-face.getZOffset()) * dist);
        return p.getParticleWorld().rayTraceBlocks(start, end, false, true, true);
    }

    private static RayTraceResult findHit(ParticleBlood p, Vec3d start, Vec3d end) {
        Vec3d d = end.subtract(start);
        double len = d.length();
        Vec3d endExt = end;
        if (len > 1.0E-9) {
            endExt = end.add(d.x / len * 0.01, d.y / len * 0.01, d.z / len * 0.01);
        }
        AxisAlignedBB bb = p.getBoundingBox();
        double hx = (bb.maxX - bb.minX) * 0.5;
        double hy = (bb.maxY - bb.minY) * 0.5;
        double hz = (bb.maxZ - bb.minZ) * 0.5;
        Vec3d[] offs = new Vec3d[]{new Vec3d(0.0, 0.0, 0.0), new Vec3d(hx, 0.0, hz), new Vec3d(hx, 0.0, -hz), new Vec3d(-hx, 0.0, hz), new Vec3d(-hx, 0.0, -hz), new Vec3d(0.0, hy * 0.5, 0.0), new Vec3d(0.0, -hy * 0.5, 0.0)};
        RayTraceResult best = null;
        double bestSq = Double.POSITIVE_INFINITY;
        for (Vec3d off : offs) {
            double distSq;
            Vec3d s = start.add(off);
            Vec3d e = endExt.add(off);
            RayTraceResult hit = p.getParticleWorld().rayTraceBlocks(s, e, false, true, true);
            if (hit == null || hit.typeOfHit != RayTraceResult.Type.BLOCK || hit.hitVec == null || !((distSq = hit.hitVec.squareDistanceTo(s)) < bestSq)) continue;
            bestSq = distSq;
            best = hit;
        }
        return best;
    }

    private static void tickWobble(ParticleBlood p) {
        if (p.detachWobbleTicks <= 0) {
            return;
        }
        if (p.isOnGroundFlag()) {
            p.detachWobbleTicks = 0;
            return;
        }
        double k = Util.clamp((double)p.detachWobbleTicks / 20.0, 0.0, 1.0);
        double t = ((double)p.getAge() + (double)(p.dripSeed * 20.0f)) * 0.35 + (double)p.groundRot;
        double amp = 0.00135 * k;
        p.motionX += Math.cos(t) * amp;
        p.motionZ += Math.sin(t) * amp;
        double t2 = t * 1.7 + 1.0;
        p.motionX += Math.cos(t2) * (amp * 0.3);
        p.motionZ += Math.sin(t2) * (amp * 0.3);
        p.motionX *= 0.975;
        p.motionZ *= 0.975;
        double hClamp = BloodMotionHeavy.wobbleClamp(p);
        BloodMotion.clampHorizontal(p, hClamp);
        --p.detachWobbleTicks;
    }

    private static void tickFall(ParticleBlood p) {
        if (p == null) {
            return;
        }
        if (p.isOnGroundFlag()) {
            return;
        }
        if (p.motionY >= 0.0) {
            return;
        }
        if (BloodMotionHeavy.airFall(p)) {
            return;
        }
        if (BloodMotionSlimy.tickAir(p)) {
            return;
        }
        if (p.noAirFlutter) {
            return;
        }
    }

    private static BloodMotion.BlockContact rayDown(ParticleBlood p, double up, double down) {
        Vec3d start = new Vec3d(p.posX, p.posY + up, p.posZ);
        Vec3d end = new Vec3d(p.posX, p.posY - down, p.posZ);
        RayTraceResult hit = p.getParticleWorld().rayTraceBlocks(start, end, false, true, true);
        if (hit == null || hit.typeOfHit != RayTraceResult.Type.BLOCK || hit.sideHit == null) {
            return null;
        }
        Vec3d at = hit.hitVec != null ? hit.hitVec : end;
        return new BloodMotion.BlockContact(hit.getBlockPos(), hit.sideHit, at);
    }

    private static EnumFacing pickPush(ParticleBlood p, BlockPos pos) {
        if (pos == null) {
            return null;
        }
        double cx = (double)((net.minecraft.util.math.Vec3i) pos).getX() + 0.5;
        double cz = (double)((net.minecraft.util.math.Vec3i) pos).getZ() + 0.5;
        double vx = p.posX - cx;
        double vz = p.posZ - cz;
        if (Math.abs(vx) < 1.0E-6 && Math.abs(vz) < 1.0E-6) {
            return null;
        }
        if (Math.abs(vx) >= Math.abs(vz)) {
            return vx >= 0.0 ? EnumFacing.EAST : EnumFacing.WEST;
        }
        return vz >= 0.0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
    }

    private static void kick(ParticleBlood p, EnumFacing side) {
        double vx = 0.0;
        double vz = 0.0;
        if (side != null) {
            vx = side.getXOffset();
            vz = side.getZOffset();
        }
        if (Math.abs(vx) < 1.0E-6 && Math.abs(vz) < 1.0E-6) {
            vx = p.getRand().nextDouble() - 0.5;
            double len = Math.sqrt(vx * vx + (vz = p.getRand().nextDouble() - 0.5) * vz);
            if (len < 1.0E-6) {
                vx = 1.0;
                vz = 0.0;
                len = 1.0;
            }
            vx /= len;
            vz /= len;
        }
        double push = 0.07 + p.getRand().nextDouble() * 0.07;
        p.motionX = vx * push + (p.getRand().nextDouble() - 0.5) * 0.02;
        p.motionZ = vz * push + (p.getRand().nextDouble() - 0.5) * 0.02;
        p.motionY = -0.07;
        p.setOnGroundFlag(false);
        p.setPositionSafe(p.posX + vx * 0.015, p.posY, p.posZ + vz * 0.015);
        p.prevPosX = p.posX;
        p.prevPosY = p.posY;
        p.prevPosZ = p.posZ;
    }

    private static boolean tryAttach(ParticleBlood p) {
        Vec3d[] ends;
        Vec3d start = new Vec3d(p.posX, p.posY + 0.02, p.posZ);
        for (Vec3d end : ends = new Vec3d[]{start.add(0.0, -0.65, 0.0), start.add(0.0, 0.35, 0.0), start.add(0.45, 0.0, 0.0), start.add(-0.45, 0.0, 0.0), start.add(0.0, 0.0, 0.45), start.add(0.0, 0.0, -0.45)}) {
            Vec3d at;
            RayTraceResult hit = p.getParticleWorld().rayTraceBlocks(start, end, false, true, true);
            if (hit == null || hit.typeOfHit != RayTraceResult.Type.BLOCK || hit.sideHit == null) continue;
            Vec3d vec3d = at = hit.hitVec != null ? hit.hitVec : end;
            if (!BloodSurfaceAttach.tryAttach(p, hit.getBlockPos(), hit.sideHit, start, end, at)) continue;
            return true;
        }
        return false;
    }
}

