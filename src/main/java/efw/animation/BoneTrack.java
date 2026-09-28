package efw.animation;

import java.util.List;

public class BoneTrack {
    public final String boneName;
    public final List<KeyFrame> rotation; // degrees, converted to radians on apply
    public final List<KeyFrame> position; // blocks

    public BoneTrack(String boneName, List<KeyFrame> rotation, List<KeyFrame> position) {
        this.boneName = boneName;
        this.rotation = rotation;
        this.position = position;
    }

    // Interpolate a keyframe list at time t, returns [x, y, z]
    public static float[] interpolate(List<KeyFrame> frames, float time, boolean loop) {
        if (frames == null || frames.isEmpty())
            return new float[] { 0, 0, 0 };

        int size = frames.size();
        KeyFrame first = frames.get(0);
        KeyFrame last = frames.get(size - 1);

        if (time <= first.time) {
            return new float[] { first.x, first.y, first.z };
        }
        if (time >= last.time) {
            return new float[] { last.x, last.y, last.z };
        }

        for (int i = 0; i < size - 1; i++) {
            KeyFrame a = frames.get(i);
            KeyFrame b = frames.get(i + 1);

            if (time >= a.time && time <= b.time) {
                float t = (time - a.time) / (b.time - a.time);

                float eased = a.easing == null ? Float.NaN : applyEasing(a.easing, t);
                if (!Float.isNaN(eased)) {
                    return new float[] {
                            a.x + (b.x - a.x) * eased,
                            a.y + (b.y - a.y) * eased,
                            a.z + (b.z - a.z) * eased
                    };
                }

                if (b.linear) {
                    return new float[] {
                            a.x + (b.x - a.x) * t,
                            a.y + (b.y - a.y) * t,
                            a.z + (b.z - a.z) * t
                    };
                } else {
                    float t2 = t * t;
                    float t3 = t2 * t;
                    float h00 = 2 * t3 - 3 * t2 + 1;
                    float h10 = t3 - 2 * t2 + t;
                    float h01 = -2 * t3 + 3 * t2;
                    float h11 = t3 - t2;

                    // Соседи зацикливаются ТОЛЬКО для loop-анимаций.
                    // Для нециклических (roll и т.п.) на краях клипа используем
                    // clamped tangent (соседом считается сама граничная точка a/b),
                    // а не "перепрыгиваем" на другой конец клипа - именно этот
                    // перепрыг создавал ложный разворот в конце roll.
                    KeyFrame prev;
                    float dt0;
                    if (i > 0) {
                        prev = frames.get(i - 1);
                        dt0 = a.time - prev.time;
                    } else if (loop && size > 2) {
                        prev = frames.get(size - 2);
                        dt0 = frames.get(size - 1).time - prev.time;
                    } else {
                        prev = a; // clamped: нет реального "до начала" соседа
                        dt0 = 0.001f;
                    }

                    KeyFrame next;
                    float dt2;
                    if (i + 2 < size) {
                        next = frames.get(i + 2);
                        dt2 = next.time - b.time;
                    } else if (loop && size > 2) {
                        next = frames.get(1);
                        dt2 = next.time - frames.get(0).time;
                    } else {
                        next = b; // clamped: нет реального "после конца" соседа
                        dt2 = 0.001f;
                    }

                    float dt1 = b.time - a.time;
                    if (dt0 <= 0)
                        dt0 = 0.001f;
                    if (dt1 <= 0)
                        dt1 = 0.001f;
                    if (dt2 <= 0)
                        dt2 = 0.001f;

                    float vx1 = (b.x - prev.x) / (dt0 + dt1);
                    float vy1 = (b.y - prev.y) / (dt0 + dt1);
                    float vz1 = (b.z - prev.z) / (dt0 + dt1);

                    float vx2 = (next.x - a.x) / (dt1 + dt2);
                    float vy2 = (next.y - a.y) / (dt1 + dt2);
                    float vz2 = (next.z - a.z) / (dt1 + dt2);

                    float mx1 = vx1 * dt1;
                    float my1 = vy1 * dt1;
                    float mz1 = vz1 * dt1;

                    float mx2_scaled = vx2 * dt1;
                    float my2_scaled = vy2 * dt1;
                    float mz2_scaled = vz2 * dt1;

                    return new float[] {
                            h00 * a.x + h10 * mx1 + h01 * b.x + h11 * mx2_scaled,
                            h00 * a.y + h10 * my1 + h01 * b.y + h11 * my2_scaled,
                            h00 * a.z + h10 * mz1 + h01 * b.z + h11 * mz2_scaled
                    };
                }
            }
        }
        return new float[] { last.x, last.y, last.z };
    }

    private static float applyEasing(String easing, float t) {
        String name = easing.toUpperCase(java.util.Locale.ROOT);
        if (name.startsWith("EASE")) {
            name = name.substring(4);
        }

        switch (name) {
            case "LINEAR":
                return t;
            case "INQUAD":
                return t * t;
            case "OUTQUAD":
                return t * (2.0F - t);
            case "INOUTQUAD":
                return t < 0.5F
                        ? 2.0F * t * t
                        : 1.0F - ((-2.0F * t + 2.0F) * (-2.0F * t + 2.0F)) * 0.5F;
            case "INSINE":
                return 1.0F - (float) Math.cos(t * Math.PI * 0.5F);
            case "OUTSINE":
                return (float) Math.sin(t * Math.PI * 0.5F);
            case "INOUTSINE":
                return 0.5F * (1.0F - (float) Math.cos(Math.PI * t));
            default:
                return Float.NaN;
        }
    }
}
