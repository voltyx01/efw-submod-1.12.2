/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common;

import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.blocksupport.DynamicPoseSupport;
import com.eruannie_9.extragore.particle.blocksupport.FenceGateSupport;
import com.eruannie_9.extragore.particle.blocksupport.PistonSupport;
import com.eruannie_9.extragore.particle.common.BloodTuning;
import com.eruannie_9.extragore.particle.common.Util;
import com.eruannie_9.extragore.particle.common.alpha.BloodAlphaCommon;
import com.eruannie_9.extragore.particle.common.alpha.BloodAlphaParticle;
import com.eruannie_9.extragore.particle.common.amalgamation.BloodAmalgamationGround;
import com.eruannie_9.extragore.particle.common.amalgamation.BloodAmalgamationWall;
import com.eruannie_9.extragore.particle.common.cache.BloodCachesParticle;
import com.eruannie_9.extragore.particle.common.geometry.BloodGeometry;
import com.eruannie_9.extragore.particle.common.motion.BloodMotionHot;
import com.eruannie_9.extragore.particle.common.motion.BloodMotionParticle;
import com.eruannie_9.extragore.particle.common.motion.BloodMotionWall;
import com.eruannie_9.extragore.particle.common.surface.BloodSurfaceAttach;
import com.eruannie_9.extragore.particle.common.surface.BloodSurfaceCeiling;
import com.eruannie_9.extragore.particle.common.surface.BloodSurfaceGround;
import com.eruannie_9.extragore.particle.common.surface.BloodSurfaceWall;
import com.eruannie_9.extragore.particle.state.BloodHotBlocks;
import com.eruannie_9.extragore.particle.state.BloodMagic;
import com.eruannie_9.extragore.particle.state.BloodSlimy;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class BloodParticleHandler {
    private static final int PISTON_RELOCATE_GRACE_TICKS = 4;
    private static final int RAIN_EXTRA_AGE_PER_TICK = 1;
    private static final double RAIN_SAMPLE_OUT = 0.05;
    private static final double RAIN_SAMPLE_UP = 0.02;

    public static void onUpdate(ParticleBlood p) {
        if (p == null) {
            return;
        }
        boolean magicIgnoresLiquids = BloodMagic.treatLiquidBlocksAsAir(p);
        p.syncToVanillaMotionAndPrev();
        BloodMotionParticle.prime(p);
        if (!(magicIgnoresLiquids || p.isExpiredSafe() || p.isStuck)) {
            if (p.isInsideLavaNow()) {
                p.convertToLavaParticle();
                return;
            }
            if (p.isInsideWaterNow()) {
                p.convertToWaterParticle();
                return;
            }
        }
        p.vanillaUpdate();
        if (p.isExpiredSafe()) {
            return;
        }
        if (!magicIgnoresLiquids && !p.isStuck) {
            if (p.isInsideLavaNow()) {
                p.convertToLavaParticle();
                return;
            }
            if (p.isInsideWaterNow()) {
                p.convertToWaterParticle();
                return;
            }
        }
        if (BloodParticleHandler.removeIfCoveredByOpaqueBlock(p)) {
            return;
        }
        BloodParticleHandler.applyRainAgeAdvanceIfNeeded(p);
        if (p.getAge() >= p.getMaxAge()) {
            BloodAlphaParticle.updateParticleAlpha(p);
            if (p.isExpiredSafe()) {
                return;
            }
        }
        if (p.wallAttachCooldownTicks > 0) {
            --p.wallAttachCooldownTicks;
        }
        if (p.hotBounceCooldownTicks > 0) {
            --p.hotBounceCooldownTicks;
        }
        BloodHotBlocks.tickCounters(p);
        if (p.isStuck) {
            BloodParticleHandler.tickStuck(p);
        } else {
            BloodMotionParticle.tick(p);
            BloodParticleHandler.tickFallingDrip(p);
            BloodSlimy.applyScaleForCurrentState(p);
            BloodParticleHandler.rebuildFreeDecalIfNeeded(p);
        }
        if (BloodParticleHandler.removeIfCoveredByOpaqueBlock(p)) {
            return;
        }
        BloodAlphaParticle.updateParticleAlpha(p);
        if (p.isExpiredSafe()) {
            return;
        }
        if (p.isStuck) {
            BloodSurfaceWall.postAlpha(p);
            BloodSurfaceCeiling.postAlpha(p);
        }
        p.syncToVanillaMotionAndPrev();
    }

    private static boolean removeIfCoveredByOpaqueBlock(ParticleBlood p) {
        IBlockState state;
        if (p == null || p.isExpiredSafe()) {
            return false;
        }
        World w = p.getParticleWorld();
        if (w == null) {
            return false;
        }
        Vec3d sample = BloodParticleHandler.coverSamplePos(p);
        if (sample == null) {
            return false;
        }
        BlockPos bp = new BlockPos(sample.x, sample.y, sample.z);
        if (!w.isBlockLoaded(bp)) {
            return false;
        }
        try {
            state = w.getBlockState(bp);
        }
        catch (Throwable t) {
            return false;
        }
        if (!BloodParticleHandler.isOpaqueSolidCover(state)) {
            return false;
        }
        BloodAlphaCommon.expireNow(p);
        return true;
    }

    private static Vec3d coverSamplePos(ParticleBlood p) {
        if (p == null) {
            return null;
        }
        if (p.isStuck && p.stuckFace != null) {
            Vec3d onPlane = BloodSurfaceAttach.anchorPoint(p);
            return onPlane.add((double)p.stuckFace.getXOffset() * 0.01, (double)p.stuckFace.getYOffset() * 0.01, (double)p.stuckFace.getZOffset() * 0.01);
        }
        AxisAlignedBB bb = p.getBoundingBox();
        if (bb != null) {
            return new Vec3d((bb.minX + bb.maxX) * 0.5, (bb.minY + bb.maxY) * 0.5, (bb.minZ + bb.maxZ) * 0.5);
        }
        return new Vec3d(p.posX, p.posY, p.posZ);
    }

    private static boolean isOpaqueSolidCover(@Nullable IBlockState state) {
        if (state == null) {
            return false;
        }
        try {
            if (!state.getMaterial().blocksMovement()) {
                return false;
            }
            if (!state.isFullCube()) {
                return false;
            }
            return state.isOpaqueCube();
        }
        catch (Throwable t) {
            return false;
        }
    }

    private static void applyRainAgeAdvanceIfNeeded(ParticleBlood p) {
        if (p == null) {
            return;
        }
        if (BloodMagic.isMagic(p)) {
            return;
        }
        if (!ParticleBlood.isLightLikeFluid(p.fluidWeight)) {
            return;
        }
        if (!BloodParticleHandler.isUnderRainNow(p)) {
            return;
        }
        p.addAgeTicks(1);
    }

    private static boolean isUnderRainNow(ParticleBlood p) {
        World w = p.getParticleWorld();
        if (w == null || !w.isRaining()) {
            return false;
        }
        Vec3d sample = BloodParticleHandler.rainSamplePos(p);
        BlockPos rainPos = new BlockPos(sample.x, sample.y + 0.02, sample.z);
        if (!w.isBlockLoaded(rainPos)) {
            return false;
        }
        try {
            return w.isRainingAt(rainPos);
        }
        catch (Throwable t) {
            return false;
        }
    }

    private static Vec3d rainSamplePos(ParticleBlood p) {
        if (p.isStuck && p.stuckFace != null) {
            Vec3d onPlane = BloodSurfaceAttach.anchorPoint(p);
            return onPlane.add((double)p.stuckFace.getXOffset() * 0.05, (double)p.stuckFace.getYOffset() * 0.05, (double)p.stuckFace.getZOffset() * 0.05);
        }
        return new Vec3d(p.posX, p.posY, p.posZ);
    }

    private static void tickStuck(ParticleBlood p) {
        if (BloodMagic.isMagic(p)) {
            BloodSurfaceAttach.detach(p);
            return;
        }
        if (BloodMotionHot.tickGround(p)) {
            return;
        }
        p.motionX = 0.0;
        p.motionY = 0.0;
        p.motionZ = 0.0;
        p.setGravity(0.0f);
        if (p.getParticleWorld() == null || p.stuckPos == null || p.stuckFace == null) {
            BloodSurfaceAttach.detach(p);
            return;
        }
        if (!p.getParticleWorld().isBlockLoaded(p.stuckPos)) {
            BloodAlphaCommon.expireNow(p);
            return;
        }
        BloodParticleHandler.updatePistonRelocateWindow(p);
        if (p.getParticleWorld().isAirBlock(p.stuckPos)) {
            if (!BloodParticleHandler.tryRelocatePistonMovedHost(p)) {
                BloodSurfaceAttach.detach(p);
                return;
            }
            if (!p.getParticleWorld().isBlockLoaded(p.stuckPos) || p.getParticleWorld().isAirBlock(p.stuckPos)) {
                BloodSurfaceAttach.detach(p);
                return;
            }
        }
        IBlockState prevBase = p.cache.host.base;
        IBlockState nowEff = BloodSurfaceAttach.baseState(p);
        if (nowEff == null) {
            BloodSurfaceAttach.detach(p);
            return;
        }
        if (prevBase != null && !BloodSurfaceAttach.sameHost(prevBase, nowEff)) {
            if (!BloodParticleHandler.tryRelocatePistonMovedHost(p)) {
                BloodSurfaceAttach.detach(p);
                return;
            }
            nowEff = BloodSurfaceAttach.baseState(p);
            if (nowEff == null) {
                BloodSurfaceAttach.detach(p);
                return;
            }
        }
        p.cache.host.base = nowEff;
        if (prevBase != nowEff) {
            BloodCachesParticle.invalidateShape(p);
            BloodCachesParticle.invalidateView(p);
        }
        if (BloodTuning.isWallFace(p.stuckFace)) {
            BloodMotionWall.slide(p);
            if (!p.isStuck) {
                return;
            }
        }
        FenceGateSupport.tickGate(p);
        if (!FenceGateSupport.isGateState(p.cache.host.base)) {
            DynamicPoseSupport.tick(p);
            if (p.cache.host.piece == null) {
                BloodSurfaceAttach.detach(p);
                return;
            }
        }
        Vec3d onPlane = BloodSurfaceAttach.anchorPoint(p);
        if (p.stuckFace == EnumFacing.UP && BloodAmalgamationGround.enabled() && BloodAmalgamationGround.tryMerge(p, onPlane)) {
            return;
        }
        BloodSurfaceWall.startWaterFade(p, onPlane);
        if (BloodTuning.isWallFace(p.stuckFace) && BloodAmalgamationWall.enabled() && BloodAmalgamationWall.tryMerge(p, onPlane)) {
            return;
        }
        BloodSurfaceAttach.moveStuck(p, onPlane);
        if (p.cache.host.poseSnapPrev) {
            p.prevPosX = p.posX;
            p.prevPosY = p.posY;
            p.prevPosZ = p.posZ;
            p.cache.host.poseSnapPrev = false;
        }
        p.stuckPlane = Util.planeCoord(p.stuckFace, onPlane);
        int oldSlimyGrowthWindow = BloodSlimy.pushRapidGrowthWindow(p);
        if (BloodTuning.isWallFace(p.stuckFace)) {
            BloodSurfaceWall.update(p);
            BloodSurfaceWall.updateDetach(p);
        } else {
            BloodSurfaceGround.update(p);
            if (p.stuckFace == EnumFacing.DOWN) {
                BloodSurfaceCeiling.update(p);
            } else {
                p.dripAmount = 0.0f;
            }
        }
        BloodSlimy.restoreRapidGrowthWindow(p, oldSlimyGrowthWindow);
        BloodSlimy.applyScaleForCurrentState(p);
        if (BloodMotionHot.tickGround(p)) {
            return;
        }
        boolean geomDirty = BloodGeometry.needsGeometryRebuild(p);
        boolean polyDirty = BloodCachesParticle.needsShape(p);
        if (geomDirty || polyDirty) {
            BloodGeometry.rebuildDecalPolys(p);
            p.cache.shape.scale = p.getScale();
            p.cache.shape.drip = p.dripAmount;
            BloodGeometry.cacheRenderPos(p);
            p.cache.shape.amalgam = p.amalgamVisualMass;
            if ((p.cache.shape.polys == null || p.cache.shape.polys.isEmpty()) && p.cache.host.poseGraceTicks <= 0) {
                BloodSurfaceAttach.detach(p);
                return;
            }
        }
        if (BloodSurfaceWall.unsupported(p)) {
            return;
        }
        if (p.cache.host.poseGraceTicks > 0) {
            --p.cache.host.poseGraceTicks;
        }
        p.idleTicks = 0;
    }

    private static void tickFallingDrip(ParticleBlood p) {
        if (p == null) {
            return;
        }
        if (!p.fallingDripActive) {
            return;
        }
        int age = p.getAge();
        int elapsed = age - p.fallingDripStartAge;
        if (elapsed < 0) {
            elapsed = 0;
        }
        int dur = Math.max(1, p.fallingDripShrinkTicks);
        float t = Util.clamp01((float)elapsed / (float)dur);
        float e = Util.easeOutCubic01(t);
        p.fallingDripLen = p.fallingDripStartLen * (1.0f - e);
        if (t >= 0.999999f || p.fallingDripLen <= 1.0E-5f) {
            p.resetFallingDripRuntime();
            p.cache.shape.polys = null;
            BloodCachesParticle.invalidateShape(p);
            BloodCachesParticle.invalidateView(p);
        }
    }

    private static void rebuildFreeDecalIfNeeded(ParticleBlood p) {
        if (p == null) {
            return;
        }
        if (!p.fallingDripActive) {
            return;
        }
        boolean geomDirty = BloodGeometry.needsGeometryRebuild(p);
        boolean polyDirty = BloodCachesParticle.needsShape(p);
        if (geomDirty || polyDirty) {
            BloodGeometry.rebuildDecalPolys(p);
            p.cache.shape.scale = p.getScale();
            p.cache.shape.drip = p.dripAmount;
            BloodGeometry.cacheRenderPos(p);
        }
    }

    private static void updatePistonRelocateWindow(ParticleBlood p) {
        PistonSupport.MovingInfo mi;
        if (p.relocateGraceTicks > 0) {
            --p.relocateGraceTicks;
        }
        if ((mi = PistonSupport.getMovingInfo(p.getParticleWorld(), p.stuckPos, 1.0f, p)) != null && !mi.staticBaseNoOffset) {
            p.relocateGraceTicks = 4;
        }
    }

    private static boolean tryRelocatePistonMovedHost(ParticleBlood p) {
        if (p.relocateGraceTicks <= 0) {
            return false;
        }
        if (!BloodSurfaceAttach.tryRelocate(p)) {
            return false;
        }
        p.relocateGraceTicks = 0;
        IBlockState eff = BloodSurfaceAttach.baseState(p);
        if (eff == null) {
            return false;
        }
        p.cache.host.base = eff;
        FenceGateSupport.captureOnAttach(p);
        if (!FenceGateSupport.isGateState(p.cache.host.base)) {
            DynamicPoseSupport.captureOnAttach(p);
        } else {
            p.cache.host.piece = null;
        }
        p.cache.host.poseSnapPrev = true;
        p.cache.host.poseGraceTicks = Math.max(p.cache.host.poseGraceTicks, 4);
        BloodCachesParticle.invalidateView(p);
        return true;
    }
}

