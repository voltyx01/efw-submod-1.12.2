package efw.animation;

public class KeyFrame {
    public final float time;
    public final float x, y, z;
    public final boolean linear; // true = linear, false = smooth (catmullrom/ease)
    public final String easing;

    public KeyFrame(float time, float x, float y, float z, boolean linear) {
        this(time, x, y, z, linear, null);
    }

    public KeyFrame(float time, float x, float y, float z, boolean linear, String easing) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
        this.linear = linear;
        this.easing = easing;
    }
}
