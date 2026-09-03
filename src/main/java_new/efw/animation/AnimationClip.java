package efw.animation;

import java.util.Map;

public class AnimationClip {
    public final String name;
    public final float length;   // seconds
    public final boolean loop;
    public final Map<String, BoneTrack> bones; // boneName -> track

    public AnimationClip(String name, float length, boolean loop, Map<String, BoneTrack> bones) {
        this.name = name;
        this.length = length;
        this.loop = loop;
        this.bones = bones;
    }
}
