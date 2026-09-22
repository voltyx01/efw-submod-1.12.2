package net.bettercombat.client.collision;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class OrientedBoundingBox {

    public Vec3d center;
    public Vec3d extent;

    public Vec3d axisX;
    public Vec3d axisY;
    public Vec3d axisZ;

    public Vec3d scaledAxisX;
    public Vec3d scaledAxisY;
    public Vec3d scaledAxisZ;

    public Vec3d vertex1;
    public Vec3d vertex2;
    public Vec3d vertex3;
    public Vec3d vertex4;
    public Vec3d vertex5;
    public Vec3d vertex6;
    public Vec3d vertex7;
    public Vec3d vertex8;
    public Vec3d[] vertices;

    public OrientedBoundingBox(Vec3d center, double width, double height, double depth, float pitch, float yaw) {
        this.center = center;
        this.extent = new Vec3d(width / 2.0, height / 2.0, depth / 2.0);
        this.axisZ = getVectorForRotation(pitch, yaw).normalize();
        this.axisY = getVectorForRotation(pitch, yaw + 90.0f).normalize();
        this.axisX = axisZ.crossProduct(axisY).normalize();
    }

    public OrientedBoundingBox(Vec3d center, Vec3d size, float pitch, float yaw) {
        this(center, size.x, size.y, size.z, pitch, yaw);
    }

    public OrientedBoundingBox(AxisAlignedBB box) {
        this.center = new Vec3d((box.maxX + box.minX) / 2.0, (box.maxY + box.minY) / 2.0, (box.maxZ + box.minZ) / 2.0);
        this.extent = new Vec3d(Math.abs(box.maxX - box.minX) / 2.0, Math.abs(box.maxY - box.minY) / 2.0, Math.abs(box.maxZ - box.minZ) / 2.0);
        this.axisX = new Vec3d(1, 0, 0);
        this.axisY = new Vec3d(0, 1, 0);
        this.axisZ = new Vec3d(0, 0, 1);
    }

    public OrientedBoundingBox(OrientedBoundingBox obb) {
        this.center = obb.center;
        this.extent = obb.extent;
        this.axisX = obb.axisX;
        this.axisY = obb.axisY;
        this.axisZ = obb.axisZ;
    }

    public OrientedBoundingBox copy() {
        return new OrientedBoundingBox(this);
    }

    public static Vec3d getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public OrientedBoundingBox offsetAlongAxisZ(double offset) {
        this.center = this.center.add(scale(axisZ, offset));
        return this;
    }

    public OrientedBoundingBox offsetAlongAxisX(double offset) {
        this.center = this.center.add(scale(axisX, offset));
        return this;
    }

    public OrientedBoundingBox offsetAlongAxisY(double offset) {
        this.center = this.center.add(scale(axisY, offset));
        return this;
    }

    private static Vec3d scale(Vec3d vec, double s) {
        return new Vec3d(vec.x * s, vec.y * s, vec.z * s);
    }

    public OrientedBoundingBox updateVertex() {
        scaledAxisX = scale(axisX, extent.x);
        scaledAxisY = scale(axisY, extent.y);
        scaledAxisZ = scale(axisZ, extent.z);

        vertex1 = center.subtract(scaledAxisZ).subtract(scaledAxisX).subtract(scaledAxisY);
        vertex2 = center.subtract(scaledAxisZ).add(scaledAxisX).subtract(scaledAxisY);
        vertex3 = center.subtract(scaledAxisZ).add(scaledAxisX).add(scaledAxisY);
        vertex4 = center.subtract(scaledAxisZ).subtract(scaledAxisX).add(scaledAxisY);
        vertex5 = center.add(scaledAxisZ).subtract(scaledAxisX).subtract(scaledAxisY);
        vertex6 = center.add(scaledAxisZ).add(scaledAxisX).subtract(scaledAxisY);
        vertex7 = center.add(scaledAxisZ).add(scaledAxisX).add(scaledAxisY);
        vertex8 = center.add(scaledAxisZ).subtract(scaledAxisX).add(scaledAxisY);

        vertices = new Vec3d[]{
                vertex1, vertex2, vertex3, vertex4,
                vertex5, vertex6, vertex7, vertex8
        };

        return this;
    }

    public boolean intersects(AxisAlignedBB boundingBox) {
        OrientedBoundingBox otherOBB = new OrientedBoundingBox(boundingBox).updateVertex();
        return intersects(otherOBB);
    }

    public boolean intersects(OrientedBoundingBox otherOBB) {
        if (this.vertices == null) updateVertex();
        if (otherOBB.vertices == null) otherOBB.updateVertex();
        return intersects(this, otherOBB);
    }

    public static boolean intersects(OrientedBoundingBox a, OrientedBoundingBox b) {
        if (separated(a.vertices, b.vertices, a.scaledAxisX)) return false;
        if (separated(a.vertices, b.vertices, a.scaledAxisY)) return false;
        if (separated(a.vertices, b.vertices, a.scaledAxisZ)) return false;

        if (separated(a.vertices, b.vertices, b.scaledAxisX)) return false;
        if (separated(a.vertices, b.vertices, b.scaledAxisY)) return false;
        if (separated(a.vertices, b.vertices, b.scaledAxisZ)) return false;

        if (separated(a.vertices, b.vertices, a.scaledAxisX.crossProduct(b.scaledAxisX))) return false;
        if (separated(a.vertices, b.vertices, a.scaledAxisX.crossProduct(b.scaledAxisY))) return false;
        if (separated(a.vertices, b.vertices, a.scaledAxisX.crossProduct(b.scaledAxisZ))) return false;

        if (separated(a.vertices, b.vertices, a.scaledAxisY.crossProduct(b.scaledAxisX))) return false;
        if (separated(a.vertices, b.vertices, a.scaledAxisY.crossProduct(b.scaledAxisY))) return false;
        if (separated(a.vertices, b.vertices, a.scaledAxisY.crossProduct(b.scaledAxisZ))) return false;

        if (separated(a.vertices, b.vertices, a.scaledAxisZ.crossProduct(b.scaledAxisX))) return false;
        if (separated(a.vertices, b.vertices, a.scaledAxisZ.crossProduct(b.scaledAxisY))) return false;
        if (separated(a.vertices, b.vertices, a.scaledAxisZ.crossProduct(b.scaledAxisZ))) return false;

        return true;
    }

    private static boolean separated(Vec3d[] vertsA, Vec3d[] vertsB, Vec3d axis) {
        if (axis.x == 0.0 && axis.y == 0.0 && axis.z == 0.0) {
            return false;
        }

        double aMin = Double.POSITIVE_INFINITY;
        double aMax = Double.NEGATIVE_INFINITY;
        double bMin = Double.POSITIVE_INFINITY;
        double bMax = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < 8; i++) {
            double aDist = vertsA[i].dotProduct(axis);
            if (aDist < aMin) aMin = aDist;
            if (aDist > aMax) aMax = aDist;

            double bDist = vertsB[i].dotProduct(axis);
            if (bDist < bMin) bMin = bDist;
            if (bDist > bMax) bMax = bDist;
        }

        double longSpan = Math.max(aMax, bMax) - Math.min(aMin, bMin);
        double sumSpan = (aMax - aMin) + (bMax - bMin);
        return longSpan >= sumSpan;
    }
}
