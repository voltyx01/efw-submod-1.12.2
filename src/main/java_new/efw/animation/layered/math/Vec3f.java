package efw.animation.layered.math;

public class Vec3f {
    public final float x;
    public final float y;
    public final float z;

    public static final Vec3f ZERO = new Vec3f(0f, 0f, 0f);

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f scale(float scalar) {
        return new Vec3f(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vec3f add(Vec3f other) {
        return new Vec3f(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
}
