package efw.animation;

public class QuatMath {
    public static class Quat {
        public float x, y, z, w;

        public Quat(float x, float y, float z, float w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }
    }

    // Convert degrees to Quat (Z-Y-X order matching Minecraft's OpenGL glRotate Z,
    // then Y, then X)
    public static Quat eulerToQuat(float xDeg, float yDeg, float zDeg) {
        float cx = (float) Math.cos(Math.toRadians(xDeg) * 0.5);
        float sx = (float) Math.sin(Math.toRadians(xDeg) * 0.5);
        float cy = (float) Math.cos(Math.toRadians(yDeg) * 0.5);
        float sy = (float) Math.sin(Math.toRadians(yDeg) * 0.5);
        float cz = (float) Math.cos(Math.toRadians(zDeg) * 0.5);
        float sz = (float) Math.sin(Math.toRadians(zDeg) * 0.5);

        float w = cx * cy * cz + sx * sy * sz;
        float x = sx * cy * cz - cx * sy * sz;
        float y = cx * sy * cz + sx * cy * sz;
        float z = cx * cy * sz - sx * sy * cz;
        return new Quat(x, y, z, w);
    }

    /**
     * Decompose a quaternion back into ZYX-order Euler angles (degrees).
     * Standard atan2/asin decomposition - unchanged from the original, kept
     * for compatibility with any caller that has no continuity hint.
     */
    public static float[] quatToEuler(Quat q) {
        return quatToEuler(q, null);
    }

    /**
     * Same decomposition, but continuous with a reference frame ({@code hintDeg},
     * degrees, same ZYX convention).
     *
     * Near gimbal lock (pitch close to +-90 deg) roll and yaw individually
     * become numerically unstable even though the rotation they describe
     * changes smoothly - the plain atan2 decomposition can land on a
     * different (still mathematically valid) representation of the same
     * rotation for two very close quaternions, e.g. roll/yaw flipping by
     * ~180 deg between adjacent animation frames. That is what produced the
     * visible body "twist" during animations sweeping pitch through +-90 deg
     * (e.g. a roll/flip).
     *
     * Fix: after decomposing, re-wrap roll and yaw by +-360 deg so they land
     * as close as possible to the hint's values. Two angles differing by
     * exactly 360 deg encode the same rotation component, so this never
     * changes what rotation is produced - it only picks the representation
     * that is continuous with the previous frame.
     *
     * This does not remove the single exact mathematical singularity at
     * pitch == +-90.000...0 deg (roll and yaw are genuinely not individually
     * defined there, only their sum/difference is) - but a slerp over
     * discrete animation frames essentially never lands exactly on that
     * value, and even if it did, the resulting discontinuity is a single
     * frame (1/20s) rather than the multi-frame twist seen before this fix.
     *
     * @param hintDeg previous frame's decoded angles, or null for the plain
     *                non-continuous decomposition (same as
     *                {@link #quatToEuler(Quat)})
     */
    public static float[] quatToEuler(Quat q, float[] hintDeg) {
    float sinp = 2 * (q.w * q.y - q.z * q.x);
    float sinpClamped = Math.max(-1f, Math.min(1f, sinp));
    float sinr_cosp = 2 * (q.w * q.x + q.y * q.z);
    float cosr_cosp = 1 - 2 * (q.x * q.x + q.y * q.y);
    float roll = (float) Math.atan2(sinr_cosp, cosr_cosp);
    float pitch = (float) Math.asin(sinpClamped);
    float siny_cosp = 2 * (q.w * q.z + q.x * q.y);
    float cosy_cosp = 1 - 2 * (q.y * q.y + q.z * q.z);
    float yaw = (float) Math.atan2(siny_cosp, cosy_cosp);

    float rollDeg = (float) Math.toDegrees(roll);
    float pitchDeg = (float) Math.toDegrees(pitch);
    float yawDeg = (float) Math.toDegrees(yaw);

    if (hintDeg == null) {
        return new float[] { rollDeg, pitchDeg, yawDeg };
    }

    // Branch A: каноническая asin-ветка
    float rollA = closestEquivalentDeg(rollDeg, hintDeg[0]);
    float pitchA = closestEquivalentDeg(pitchDeg, hintDeg[1]);
    float yawA = closestEquivalentDeg(yawDeg, hintDeg[2]);

    // Branch B: альтернативная ветка того же физического поворота.
    // Нужна, когда истинный pitch выходит за [-90,90] (кость проходит через
    // 90 град по pitch, например во время кувырка) - без неё roll/yaw скачут
    // на 180 град при пересечении этой зоны, что при блендинге выглядит как
    // разворот анимации в обратную сторону.
    float rollB = closestEquivalentDeg(rollDeg + 180f, hintDeg[0]);
    float pitchB = closestEquivalentDeg(180f - pitchDeg, hintDeg[1]);
    float yawB = closestEquivalentDeg(yawDeg + 180f, hintDeg[2]);

    float errA = angDiff(rollA, hintDeg[0]) + angDiff(pitchA, hintDeg[1]) + angDiff(yawA, hintDeg[2]);
    float errB = angDiff(rollB, hintDeg[0]) + angDiff(pitchB, hintDeg[1]) + angDiff(yawB, hintDeg[2]);

    return (errB < errA)
            ? new float[] { rollB, pitchB, yawB }
            : new float[] { rollA, pitchA, yawA };
}

private static float angDiff(float a, float b) {
    float d = Math.abs(a - b) % 360f;
    if (d > 180f) d = 360f - d;
    return d;
}

    // Shifts `deg` by a multiple of 360 so it lands as close as possible to
    // `hintDeg`.
    // Two Euler angles differing by exactly 360 deg represent the same rotation
    // component; this keeps frame-to-frame output continuous instead of
    // jumping between equivalent representations (e.g. -170 vs 190).
    private static float closestEquivalentDeg(float deg, float hintDeg) {
        float delta = (deg - hintDeg) % 360f;
        if (delta > 180f)
            delta -= 360f;
        if (delta < -180f)
            delta += 360f;
        return hintDeg + delta;
    }

    public static Quat slerp(Quat q1, Quat q2, float t) {
        float dot = q1.x * q2.x + q1.y * q2.y + q1.z * q2.z + q1.w * q2.w;
        Quat q3 = new Quat(q2.x, q2.y, q2.z, q2.w);
        if (dot < 0.0f) {
            dot = -dot;
            q3.x = -q3.x;
            q3.y = -q3.y;
            q3.z = -q3.z;
            q3.w = -q3.w;
        }

        if (dot > 0.9995f) {
            float invT = 1.0f - t;
            Quat res = new Quat(q1.x * invT + q3.x * t, q1.y * invT + q3.y * t, q1.z * invT + q3.z * t,
                    q1.w * invT + q3.w * t);
            float len = (float) Math.sqrt(res.x * res.x + res.y * res.y + res.z * res.z + res.w * res.w);
            res.x /= len;
            res.y /= len;
            res.z /= len;
            res.w /= len;
            return res;
        }

        float theta_0 = (float) Math.acos(dot);
        float theta = theta_0 * t;
        float sin_theta = (float) Math.sin(theta);
        float sin_theta_0 = (float) Math.sin(theta_0);

        float s0 = (float) Math.cos(theta) - dot * sin_theta / sin_theta_0;
        float s1 = sin_theta / sin_theta_0;

        return new Quat(
                s0 * q1.x + s1 * q3.x,
                s0 * q1.y + s1 * q3.y,
                s0 * q1.z + s1 * q3.z,
                s0 * q1.w + s1 * q3.w);
    }

    public static float[] slerpEulerAngles(float[] euler1, float[] euler2, float t) {
        Quat q1 = eulerToQuat(euler1[0], euler1[1], euler1[2]);
        Quat q2 = eulerToQuat(euler2[0], euler2[1], euler2[2]);
        Quat q3 = slerp(q1, q2, t);
        // euler1 is the "from" pose for this blend call - use it as continuity hint
        // so the decoded result doesn't jump branches near gimbal lock or wrap at 180.
        return quatToEuler(q3, euler1);
    }
}