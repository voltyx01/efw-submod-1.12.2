package efw.animation.layered;

import efw.animation.AnimationClip;
import efw.animation.BoneTrack;
import efw.animation.layered.math.Vec3f;

public class KeyframeAnimationPlayer implements IAnimation {
    private final AnimationClip clip;
    private float currentTime = 0f;
    private float prevTime = 0f;
    private boolean isRunning = true;
    private float speedMult = 1.0f;
    private boolean isLoopingStarted = false;

    // Temporary storage for tickDelta
    private float tickDelta = 0f;

    public KeyframeAnimationPlayer(AnimationClip clip) {
        this.clip = clip;
    }

    public AnimationClip getClip() {
        return this.clip;
    }

    public void setSpeed(float speed) {
        this.speedMult = speed;
    }

    public float getSpeed() {
        return this.speedMult;
    }

    public float getCurrentTime() {
        return this.currentTime;
    }

    public float getProgress() {
        if (clip == null || clip.length <= 0) return 1.0f;
        return Math.min(1.0f, Math.max(0.0f, this.currentTime / clip.length));
    }

    @Override
    public void tick() {
        if (!isRunning) {
            if (prevTime != currentTime) {
                prevTime = currentTime;
            }
            return;
        }

        prevTime = currentTime;
        currentTime += (1f / 20f) * speedMult;

        if (clip.loop) {
            if (currentTime >= clip.length) {
                float overshoot = currentTime - clip.length;
                currentTime = overshoot;
                prevTime = -((1f / 20f) * speedMult) + overshoot;
                isLoopingStarted = true;
            } else if (currentTime < 0) {
                float undershoot = currentTime;
                currentTime = clip.length + undershoot;
                prevTime = clip.length - ((1f / 20f) * speedMult) + undershoot;
            }
        } else {
            if (currentTime >= clip.length) {
                currentTime = clip.length;
                isRunning = false;
            }
        }
    }

    @Override
    public void setupAnim(float tickDelta) {
        this.tickDelta = tickDelta;
    }

    @Override
    public boolean isActive() {
        return this.isRunning;
    }

    @Override
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        if (clip == null || clip.bones == null) return value0;

        BoneTrack track = getTrack(modelName);
        if (track == null) return value0;

        float t = getInterpolatedTime(prevTime, currentTime, tickDelta, clip.length, clip.loop);

        if (type == TransformType.ROTATION) {
            if (track.rotation != null && !track.rotation.isEmpty()) {
                float[] deg = BoneTrack.interpolate(track.rotation, t, clip.loop);
                float x = deg[0];
                float y = deg[1];
                float z = deg[2];
                float rotX = (float) Math.toRadians(x);
                float rotY = (float) Math.toRadians(y);
                float rotZ = (float) Math.toRadians(z);
                return new Vec3f(rotX, rotY, rotZ);
            }
        } else if (type == TransformType.POSITION) {
            if (track.position != null && !track.position.isEmpty()) {
                float[] pos = BoneTrack.interpolate(track.position, t, clip.loop);
                // Position is additive to vanilla base position. Invert Y to match Minecraft's coordinate system.
                return new Vec3f(value0.getX() + pos[0], value0.getY() - pos[1], value0.getZ() + pos[2]);
            }
        }
        return value0;
    }

    private BoneTrack getTrack(String boneName) {
        BoneTrack track = clip.bones.get(boneName);
        if (track != null) return track;

        if ("rightArm".equals(boneName)) {
            track = clip.bones.get("right_arm");
            if (track == null) track = clip.bones.get("root");
            return track;
        }
        if ("leftArm".equals(boneName)) return clip.bones.get("left_arm");
        if ("rightLeg".equals(boneName)) return clip.bones.get("right_leg");
        if ("leftLeg".equals(boneName)) return clip.bones.get("left_leg");
        return null;
    }

    private float getInterpolatedTime(float prev, float curr, float partial, float length, boolean loop) {
        float delta = curr - prev;
        if (loop) {
            if (delta > length / 2f) {
                delta -= length;
            } else if (delta < -length / 2f) {
                delta += length;
            }
        }
        float t = prev + delta * partial;
        if (loop) {
            t = t % length;
            if (t < 0) t += length;
        } else {
            t = Math.max(0, Math.min(t, length));
        }
        return t;
    }
}
