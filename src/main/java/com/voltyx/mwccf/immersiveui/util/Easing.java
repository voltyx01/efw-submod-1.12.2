package com.voltyx.mwccf.immersiveui.util;

public class Easing {
    public enum Type {
        EASE_IN,
        EASE_OUT,
        EASE_IN_OUT,
        EASE_OUT_BOUNCE,
        LINEAR
    }

    public static float lerp(float start_value, float end_value, float pct) {
        return (start_value + (end_value - start_value) * pct);
    }

    public static float flip(float x) {
        return 1.0F - x;
    }

    public static float easeIn(float t) {
        return t * t;
    }

    public static float easeOut(float t) {
        return flip(easeIn(flip(t)));
    }

    public static float easeInOut(float t) {
        return lerp(easeIn(t), easeOut(t), t);
    }

    public static float easeOutBounce(float x) {
        final float n1 = 7.5625F;
        final float d1 = 2.75F;

        if (x < 1.0F / d1) {
            return n1 * x * x;
        } else if (x < 2.0F / d1) {
            return (float) (n1 * (x -= (float) (1.5F / d1)) * x + 0.75F);
        } else if (x < 2.5F / d1) {
            return (float) (n1 * (x -= (float) (2.25F / d1)) * x + 0.9375F);
        } else {
            return (float) (n1 * (x -= (float) (2.625F / d1)) * x + 0.984375F);
        }
    }

    public static float animate(Type type, float x) {
        switch (type) {
            case EASE_IN:
                return easeIn(x);
            case EASE_OUT:
                return easeOut(x);
            case EASE_IN_OUT:
                return easeInOut(x);
            case EASE_OUT_BOUNCE:
                return easeOutBounce(x);
            case LINEAR:
            default:
                return x;
        }
    }
}
