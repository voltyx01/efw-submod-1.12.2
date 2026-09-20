package com.voltyx.mwccf.immersiveui.util;

public class Vector2f {
    public float x;
    public float y;

    public Vector2f() {
        this(0.0F, 0.0F);
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f set(Vector2f other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }

    public Vector2f add(float dx, float dy) {
        this.x += dx;
        this.y += dy;
        return this;
    }

    public Vector2f add(Vector2f other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2f mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2f normalize() {
        float len = length();
        if (len != 0.0F) {
            this.x /= len;
            this.y /= len;
        }
        return this;
    }

    public Vector2f normalize(float length) {
        normalize();
        mul(length);
        return this;
    }

    public static Vector2f rotate(Vector2f vec, float rotDegrees) {
        float radians = (float) Math.toRadians(rotDegrees);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        float newX = vec.x * cos - vec.y * sin;
        float newY = vec.x * sin + vec.y * cos;

        return new Vector2f(newX, newY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2f vector2f = (Vector2f) o;
        return Float.compare(vector2f.x, x) == 0 && Float.compare(vector2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}
