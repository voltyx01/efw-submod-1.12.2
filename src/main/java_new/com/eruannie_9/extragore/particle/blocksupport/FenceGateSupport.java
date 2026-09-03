/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.block.BlockFenceGate
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumFacing$Axis
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.blocksupport;

import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.common.Util;
import com.eruannie_9.extragore.particle.common.cache.BloodCachesParticle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class FenceGateSupport {
    private static final double POST_THICK = 0.125;
    private static final double POST_C0 = 0.0625;
    private static final double POST_C1 = 0.9375;
    private static final double PLANE_C = 0.5;
    private static final double BAND_MIN = 0.4375;
    private static final double BAND_MAX = 0.5625;
    private static final double EPS = 1.0E-6;
    private static final byte POST_A = 1;
    private static final byte POST_B = 2;
    private static final byte LEAF_A = 3;
    private static final byte LEAF_B = 4;

    public static boolean isGateState(@Nullable IBlockState state) {
        return state != null && state.getBlock() instanceof BlockFenceGate;
    }

    public static void captureOnAttach(@Nonnull ParticleBlood blood) {
        BloodCachesParticle.resetGate(blood);
        World world = blood.getParticleWorld();
        if (world == null || blood.stuckPos == null || blood.stuckLocalOnPlane == null) {
            return;
        }
        IBlockState st = blood.cache.host.base;
        if (!FenceGateSupport.isGateState(st)) {
            return;
        }
        EnumFacing facing = FenceGateSupport.safeGetFacing(st);
        Boolean openObj = FenceGateSupport.safeGetOpen(st);
        if (facing == null || openObj == null) {
            return;
        }
        boolean open = openObj;
        blood.cache.gate.has = true;
        blood.cache.gate.open = open;
        blood.cache.gate.part = FenceGateSupport.classifyPart(blood.stuckLocalOnPlane, facing, open);
    }

    public static void tickGate(@Nonnull ParticleBlood blood) {
        World world = blood.getParticleWorld();
        if (world == null || blood.stuckPos == null || blood.stuckLocalOnPlane == null) {
            FenceGateSupport.reset(blood);
            return;
        }
        IBlockState state = blood.cache.host.base;
        if (!FenceGateSupport.isGateState(state)) {
            FenceGateSupport.reset(blood);
            return;
        }
        EnumFacing facing = FenceGateSupport.safeGetFacing(state);
        Boolean openObj = FenceGateSupport.safeGetOpen(state);
        if (facing == null || openObj == null) {
            return;
        }
        boolean openNow = openObj;
        if (!blood.cache.gate.has) {
            blood.cache.gate.has = true;
            blood.cache.gate.open = openNow;
            blood.cache.gate.part = FenceGateSupport.classifyPart(blood.stuckLocalOnPlane, facing, openNow);
            return;
        }
        if (openNow == blood.cache.gate.open) {
            return;
        }
        if (blood.cache.gate.part == 3 || blood.cache.gate.part == 4) {
            FenceGateSupport.applyPoseChange(blood, facing, openNow, blood.cache.gate.part);
        }
        blood.cache.gate.open = openNow;
    }

    private static void reset(@Nonnull ParticleBlood blood) {
        BloodCachesParticle.resetGate(blood);
    }

    @Nullable
    private static EnumFacing safeGetFacing(@Nonnull IBlockState blockState) {
        try {
            return (EnumFacing)blockState.getValue((IProperty)BlockFenceGate.FACING);
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    @Nullable
    private static Boolean safeGetOpen(@Nonnull IBlockState blockState) {
        try {
            return (Boolean)blockState.getValue((IProperty)BlockFenceGate.OPEN);
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static byte classifyPart(@Nonnull Vec3d local, @Nonnull EnumFacing facing, boolean open) {
        boolean inBand;
        boolean isMinSide;
        EnumFacing.Axis planeAxis = facing.getAxis();
        EnumFacing.Axis sideAxis = planeAxis == EnumFacing.Axis.Z ? EnumFacing.Axis.X : EnumFacing.Axis.Z;
        double side = sideAxis == EnumFacing.Axis.X ? local.x : local.z;
        double plane = planeAxis == EnumFacing.Axis.X ? local.x : local.z;
        side = Util.clamp01(side);
        plane = Util.clamp01(plane);
        boolean bl = isMinSide = side < 0.5;
        if (!open) {
            if (side <= 0.125001) {
                return 1;
            }
            if (side >= 0.874999) {
                return 2;
            }
            return isMinSide ? (byte)3 : 4;
        }
        boolean bl2 = inBand = plane >= 0.437499 && plane <= 0.562501;
        if (inBand) {
            return isMinSide ? (byte)1 : 2;
        }
        return isMinSide ? (byte)3 : 4;
    }

    private static void applyPoseChange(@Nonnull ParticleBlood blood, @Nonnull EnumFacing facing, boolean openNow, byte part) {
        double pz;
        double px;
        boolean isA;
        if (blood.stuckLocalOnPlane == null) {
            return;
        }
        EnumFacing.Axis planeAxis = facing.getAxis();
        EnumFacing.Axis sideAxis = planeAxis == EnumFacing.Axis.Z ? EnumFacing.Axis.X : EnumFacing.Axis.Z;
        boolean bl = isA = part == 3;
        if (sideAxis == EnumFacing.Axis.X) {
            px = isA ? 0.0625 : 0.9375;
            pz = 0.5;
        } else {
            px = 0.5;
            pz = isA ? 0.0625 : 0.9375;
        }
        boolean rotYForward = FenceGateSupport.chooseForwardRotationY(facing, sideAxis, isA);
        boolean rotY = openNow == rotYForward;
        blood.stuckLocalOnPlane = FenceGateSupport.rotateLocalXZ(blood.stuckLocalOnPlane, px, pz, rotY);
        if (blood.stuckFace != null && blood.stuckFace.getAxis().isHorizontal()) {
            blood.stuckFace = rotY ? blood.stuckFace.rotateY() : blood.stuckFace.rotateYCCW();
        }
        BloodCachesParticle.invalidateView(blood);
        blood.cache.host.poseSnapPrev = true;
        blood.cache.host.poseGraceTicks = Math.max(blood.cache.host.poseGraceTicks, 4);
    }

    private static boolean chooseForwardRotationY(@Nonnull EnumFacing facing, @Nonnull EnumFacing.Axis sideAxis, boolean isA) {
        double cx = 0.0;
        double cz = 0.0;
        if (sideAxis == EnumFacing.Axis.X) {
            cx = isA ? 1.0 : -1.0;
        } else {
            cz = isA ? 1.0 : -1.0;
        }
        double ox = 0.0;
        double oz = 0.0;
        switch (facing) {
            case EAST: {
                ox = 1.0;
                break;
            }
            case WEST: {
                ox = -1.0;
                break;
            }
            case SOUTH: {
                oz = 1.0;
                break;
            }
            case NORTH: {
                oz = -1.0;
                break;
            }
        }
        double yx = -cz;
        double yz = cx;
        double dotY = yx * ox + yz * oz;
        double ccx = cz;
        double ccz = -cx;
        double dotCCW = ccx * ox + ccz * oz;
        return dotY >= dotCCW;
    }

    private static Vec3d rotateLocalXZ(@Nonnull Vec3d local, double px, double pz, boolean rotY) {
        double nz;
        double nx;
        double dx = local.x - px;
        double dz = local.z - pz;
        if (rotY) {
            nx = px - dz;
            nz = pz + dx;
        } else {
            nx = px + dz;
            nz = pz - dx;
        }
        nx = Util.snap16(nx);
        nz = Util.snap16(nz);
        return new Vec3d(nx, local.y, nz);
    }
}

