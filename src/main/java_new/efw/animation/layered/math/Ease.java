package efw.animation.layered.math;

public class Ease {
    public static float linear(float f) {
        return f;
    }

    public static float inOutSine(float f) {
        return 0.5f * (1f - (float) Math.cos(Math.PI * f));
    }

    public static float outCubic(float f) {
        return (float) (1.0 - Math.pow(1.0 - f, 3.0));
    }

    public static float inCubic(float f) {
        return f * f * f;
    }

    public static float inOutCubic(float f) {
        return f < 0.5 ? 4 * f * f * f : (float) (1 - Math.pow(-2 * f + 2, 3) / 2);
    }
    
    // Add more easings if needed
}
