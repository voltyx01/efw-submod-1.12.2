package efw.animation;

public class KeyFrame {
    public final float time;
    public final float x, y, z;
    public final boolean linear; // true = linear, false = smooth (catmullrom/ease)

    public KeyFrame(float time, float x, float y, float z, boolean linear) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
        this.linear = linear;
    }
}
