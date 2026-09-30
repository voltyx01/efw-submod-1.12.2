package com.voltyx.mwccf.furniture;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class FurnitureAABB {

    public static AxisAlignedBB rotate(AxisAlignedBB aabb, EnumFacing facing) {
        switch (facing) {
            case SOUTH:
                return new AxisAlignedBB(1.0 - aabb.maxX, aabb.minY, 1.0 - aabb.maxZ, 1.0 - aabb.minX, aabb.maxY, 1.0 - aabb.minZ);
            case WEST:
                return new AxisAlignedBB(aabb.minZ, aabb.minY, 1.0 - aabb.maxX, aabb.maxZ, aabb.maxY, 1.0 - aabb.minX);
            case EAST:
                return new AxisAlignedBB(1.0 - aabb.maxZ, aabb.minY, aabb.minX, 1.0 - aabb.minZ, aabb.maxY, aabb.maxX);
            default: // NORTH
                return aabb;
        }
    }

    public static AxisAlignedBB[] createRotated(AxisAlignedBB northAABB) {
        AxisAlignedBB[] aabbs = new AxisAlignedBB[4];
        aabbs[EnumFacing.NORTH.getHorizontalIndex()] = northAABB;
        aabbs[EnumFacing.SOUTH.getHorizontalIndex()] = rotate(northAABB, EnumFacing.SOUTH);
        aabbs[EnumFacing.WEST.getHorizontalIndex()] = rotate(northAABB, EnumFacing.WEST);
        aabbs[EnumFacing.EAST.getHorizontalIndex()] = rotate(northAABB, EnumFacing.EAST);
        return aabbs;
    }

    public static AxisAlignedBB get(AxisAlignedBB[] aabbs, EnumFacing facing) {
        if (facing == null || facing.getHorizontalIndex() < 0) return aabbs[0];
        return aabbs[facing.getHorizontalIndex()];
    }
}
