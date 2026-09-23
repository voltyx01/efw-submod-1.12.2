package efw.animation;

import java.util.Map;

public class AnimationClip {
    public final String name;
    public final float length;   // seconds
    public final boolean loop;
    public final Map<String, BoneTrack> bones; // boneName -> track
    public final int beginTick;
    public final int endTick;
    public final int stopTick;
    public final float startTime;
    public final boolean isEmotecraft;
    public boolean isBetterCombat = false;

    public AnimationClip(String name, float length, boolean loop, Map<String, BoneTrack> bones) {
        this(name, length, loop, bones, 0, 0, 0, false);
    }

    public AnimationClip(String name, float length, boolean loop, Map<String, BoneTrack> bones, float startTime) {
        this(name, length, loop, bones, 0, 0, 0, false);
    }

    public AnimationClip(String name, float length, boolean loop, Map<String, BoneTrack> bones, float startTime, boolean isEmotecraft) {
        this(name, length, loop, bones, 0, 0, 0, isEmotecraft);
    }

    public AnimationClip(String name, float length, boolean loop, Map<String, BoneTrack> bones, int beginTick, int endTick, int stopTick, boolean isEmotecraft) {
        this.name = name;
        this.length = length;
        this.loop = loop;
        this.bones = bones;
        this.beginTick = beginTick;
        this.endTick = endTick;
        this.stopTick = stopTick;
        this.startTime = 0.0f;
        this.isEmotecraft = isEmotecraft;
        if (name != null && (name.contains("slash") || name.contains("stab") || name.contains("punch")
                || name.contains("slam") || name.contains("spin") || name.contains("swipe") || name.contains("uppercut")
                || name.startsWith("dual_handed_") || name.startsWith("one_handed_") || name.startsWith("two_handed_"))) {
            this.isBetterCombat = true;
        }
    }
}

