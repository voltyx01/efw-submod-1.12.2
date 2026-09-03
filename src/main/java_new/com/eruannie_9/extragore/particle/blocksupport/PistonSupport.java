/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockPistonBase
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityPiston
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.blocksupport;

import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.common.Util;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class PistonSupport {
    @Nullable
    public static MovingInfo getMovingInfo(@Nullable World world, @Nullable BlockPos pos, float partialTicks) {
        return PistonSupport.getMovingInfo(world, pos, partialTicks, null);
    }

    private static boolean isPistonBaseState(@Nullable IBlockState blockState) {
        if (blockState == null) {
            return false;
        }
        Block block = blockState.getBlock();
        return block == Blocks.PISTON || block == Blocks.STICKY_PISTON || block instanceof BlockPistonBase;
    }

    @Nullable
    private static IBlockState forceExtendedIfPossible(@Nullable IBlockState blockState) {
        if (blockState == null) {
            return null;
        }
        if (!(blockState.getBlock() instanceof BlockPistonBase)) {
            return blockState;
        }
        try {
            return blockState.withProperty((IProperty)BlockPistonBase.EXTENDED, (Comparable)Boolean.TRUE);
        }
        catch (Throwable throwable) {
            return blockState;
        }
    }

    private static boolean isRetractingBaseTE(TileEntityPiston piston) {
        try {
            return piston.shouldPistonHeadBeRendered() && !piston.isExtending();
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    @Nullable
    public static MovingInfo getMovingInfo(@Nullable World world, @Nullable BlockPos pos, float partialTicks, @Nullable ParticleBlood blood) {
        Vec3d off;
        IBlockState moved;
        TileEntity tileEntity;
        IBlockState blockState;
        if (world == null || pos == null) {
            return null;
        }
        try {
            blockState = world.getBlockState(pos);
        }
        catch (Throwable throwable) {
            return null;
        }
        if (blockState.getBlock() != Blocks.PISTON_EXTENSION) {
            return null;
        }
        try {
            tileEntity = world.getTileEntity(pos);
        }
        catch (Throwable throwable) {
            return null;
        }
        if (!(tileEntity instanceof TileEntityPiston)) {
            return null;
        }
        TileEntityPiston piston = (TileEntityPiston)tileEntity;
        if (blood != null && PistonSupport.isPistonBaseState(blood.cache.host.base) && PistonSupport.isRetractingBaseTE(piston)) {
            IBlockState base = PistonSupport.forceExtendedIfPossible(blood.cache.host.base);
            return new MovingInfo(base, Util.ZERO, true);
        }
        try {
            moved = piston.getPistonState();
        }
        catch (Throwable throwable) {
            return null;
        }
        try {
            off = new Vec3d((double)piston.getOffsetX(partialTicks), (double)piston.getOffsetY(partialTicks), (double)piston.getOffsetZ(partialTicks));
        }
        catch (Throwable throwable) {
            off = Util.ZERO;
        }
        return new MovingInfo(moved, off, false);
    }

    public static final class MovingInfo {
        public final IBlockState movedState;
        public final Vec3d offset;
        public final boolean staticBaseNoOffset;

        public MovingInfo(IBlockState movedState, Vec3d offset, boolean staticBaseNoOffset) {
            this.movedState = movedState;
            this.offset = offset;
            this.staticBaseNoOffset = staticBaseNoOffset;
        }
    }
}

