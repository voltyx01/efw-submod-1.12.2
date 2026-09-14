package com.voltyx.mwccf.zone;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class QuestZone {
    private final String id;
    private final String name;
    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;
    private final int dimension;

    public QuestZone(String id, String name, int dimension, BlockPos pos1, BlockPos pos2) {
        this.id = id;
        this.name = name;
        this.dimension = dimension;
        this.minX = Math.min(pos1.getX(), pos2.getX());
        this.minY = Math.min(pos1.getY(), pos2.getY());
        this.minZ = Math.min(pos1.getZ(), pos2.getZ());
        this.maxX = Math.max(pos1.getX(), pos2.getX());
        this.maxY = Math.max(pos1.getY(), pos2.getY());
        this.maxZ = Math.max(pos1.getZ(), pos2.getZ());
    }

    public QuestZone(String id, String name, int dimension, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.id = id;
        this.name = name;
        this.dimension = dimension;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDimension() {
        return dimension;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public boolean contains(int dim, BlockPos pos) {
        if (this.dimension != dim) {
            return false;
        }
        return pos.getX() >= minX && pos.getX() <= maxX &&
               pos.getY() >= minY && pos.getY() <= maxY &&
               pos.getZ() >= minZ && pos.getZ() <= maxZ;
    }

    public boolean contains(int dim, double x, double y, double z) {
        if (this.dimension != dim) {
            return false;
        }
        return x >= minX && x <= (maxX + 1.0D) &&
               y >= minY && y <= (maxY + 1.0D) &&
               z >= minZ && z <= (maxZ + 1.0D);
    }

    public boolean intersectsOrTouches(int dim, BlockPos pos, int radius) {
        if (this.dimension != dim) {
            return false;
        }
        return pos.getX() >= (minX - radius) && pos.getX() <= (maxX + radius) &&
               pos.getY() >= (minY - radius) && pos.getY() <= (maxY + radius) &&
               pos.getZ() >= (minZ - radius) && pos.getZ() <= (maxZ + radius);
    }

    public AxisAlignedBB getAABB() {
        return new AxisAlignedBB(minX, minY, minZ, maxX + 1.0D, maxY + 1.0D, maxZ + 1.0D);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setString("Id", id);
        nbt.setString("Name", name);
        nbt.setInteger("Dim", dimension);
        nbt.setInteger("MinX", minX);
        nbt.setInteger("MinY", minY);
        nbt.setInteger("MinZ", minZ);
        nbt.setInteger("MaxX", maxX);
        nbt.setInteger("MaxY", maxY);
        nbt.setInteger("MaxZ", maxZ);
        return nbt;
    }

    public static QuestZone readFromNBT(NBTTagCompound nbt) {
        String id = nbt.getString("Id");
        String name = nbt.getString("Name");
        int dim = nbt.getInteger("Dim");
        int minX = nbt.getInteger("MinX");
        int minY = nbt.getInteger("MinY");
        int minZ = nbt.getInteger("MinZ");
        int maxX = nbt.getInteger("MaxX");
        int maxY = nbt.getInteger("MaxY");
        int maxZ = nbt.getInteger("MaxZ");
        return new QuestZone(id, name, dim, minX, minY, minZ, maxX, maxY, maxZ);
    }
}
