package efw.animation;

import com.google.gson.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class AnimationParser {

    // Parse a Bedrock-format animation JSON, returns map of animName -> AnimationClip
    public static Map<String, AnimationClip> parse(InputStream stream, String prefix) {
        Map<String, AnimationClip> result = new HashMap<>();

        JsonObject root = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        
        if (root.has("emote")) {
            return parseEmotecraft(root, prefix);
        }
        
        if (!root.has("animations")) return result;

        JsonObject animations = root.getAsJsonObject("animations");
        for (Map.Entry<String, JsonElement> animEntry : animations.entrySet()) {
            String animName = (prefix == null ? "" : prefix) + animEntry.getKey(); // e.g. "pistol_walking"
            JsonObject animObj = animEntry.getValue().getAsJsonObject();

            boolean hasLength = animObj.has("animation_length");
            float length = hasLength ? animObj.get("animation_length").getAsFloat() : 0.0f;
            boolean loop = animObj.has("loop") && animObj.get("loop").getAsBoolean();

            if (!hasLength && animObj.has("bones")) {
                JsonObject bones = animObj.getAsJsonObject("bones");
                for (Map.Entry<String, JsonElement> boneEntry : bones.entrySet()) {
                    JsonObject boneObj = boneEntry.getValue().getAsJsonObject();
                    length = Math.max(length, getMaxTime(boneObj, "rotation"));
                    length = Math.max(length, getMaxTime(boneObj, "position"));
                }
            }

            Map<String, BoneTrack> boneTracks = new HashMap<>();

            if (animObj.has("bones")) {
                JsonObject bones = animObj.getAsJsonObject("bones");
                for (Map.Entry<String, JsonElement> boneEntry : bones.entrySet()) {
                    String boneName = boneEntry.getKey();
                    JsonObject boneObj = boneEntry.getValue().getAsJsonObject();

                    List<KeyFrame> rotFrames = parseChannel(boneObj, "rotation", length, loop);
                    List<KeyFrame> posFrames = parseChannel(boneObj, "position", length, loop);

                    boneTracks.put(boneName, new BoneTrack(boneName, rotFrames, posFrames));
                }
            }

            result.put(animName, new AnimationClip(animName, length, loop, boneTracks));
        }

        return result;
    }

    private static List<KeyFrame> parseChannel(JsonObject boneObj, String channel, float length, boolean loop) {
        List<KeyFrame> frames = new ArrayList<>();
        if (!boneObj.has(channel)) return frames;

        JsonElement channelElem = boneObj.get(channel);

        // Can be object (keyframes), object with "vector", or array (single value for all time)
        if (channelElem.isJsonArray()) {
            JsonArray vec = channelElem.getAsJsonArray();
            float x = vec.get(0).getAsFloat();
            float y = vec.get(1).getAsFloat();
            float z = vec.get(2).getAsFloat();
            frames.add(new KeyFrame(0.0f, x, y, z, false));
        } else if (channelElem.isJsonObject()) {
            JsonObject keyframes = channelElem.getAsJsonObject();
            
            if (keyframes.has("post")) {
                JsonArray vec = keyframes.getAsJsonArray("post");
                float x = vec.get(0).getAsFloat();
                float y = vec.get(1).getAsFloat();
                float z = vec.get(2).getAsFloat();
                frames.add(new KeyFrame(0.0f, x, y, z, false));
            } else if (keyframes.has("pre")) {
                JsonArray vec = keyframes.getAsJsonArray("pre");
                float x = vec.get(0).getAsFloat();
                float y = vec.get(1).getAsFloat();
                float z = vec.get(2).getAsFloat();
                frames.add(new KeyFrame(0.0f, x, y, z, false));
            } else if (keyframes.has("vector")) {
                JsonArray vec = keyframes.getAsJsonArray("vector");
                float x = vec.get(0).getAsFloat();
                float y = vec.get(1).getAsFloat();
                float z = vec.get(2).getAsFloat();
                frames.add(new KeyFrame(0.0f, x, y, z, false));
            } else {
                for (Map.Entry<String, JsonElement> kfEntry : keyframes.entrySet()) {
                    float time = Float.parseFloat(kfEntry.getKey());
                    JsonElement val = kfEntry.getValue();

                    float x = 0, y = 0, z = 0;
                    boolean linear = false;

                    if (val.isJsonObject()) {
                        JsonObject kfObj = val.getAsJsonObject();
                        // easing field
                        if (kfObj.has("easing")) {
                            String easing = kfObj.get("easing").getAsString();
                            linear = easing.equals("linear");
                        }
                        if (kfObj.has("post")) {
                            JsonElement postEl = kfObj.get("post");
                            JsonArray vec = postEl.isJsonArray() ? postEl.getAsJsonArray() : postEl.getAsJsonObject().getAsJsonArray("vector");
                            x = vec.get(0).getAsFloat();
                            y = vec.get(1).getAsFloat();
                            z = vec.get(2).getAsFloat();
                        } else if (kfObj.has("pre")) {
                            JsonElement preEl = kfObj.get("pre");
                            JsonArray vec = preEl.isJsonArray() ? preEl.getAsJsonArray() : preEl.getAsJsonObject().getAsJsonArray("vector");
                            x = vec.get(0).getAsFloat();
                            y = vec.get(1).getAsFloat();
                            z = vec.get(2).getAsFloat();
                        } else if (kfObj.has("vector")) {
                            JsonArray vec = kfObj.getAsJsonArray("vector");
                            x = vec.get(0).getAsFloat();
                            y = vec.get(1).getAsFloat();
                            z = vec.get(2).getAsFloat();
                        }
                    } else if (val.isJsonArray()) {
                        JsonArray vec = val.getAsJsonArray();
                        x = vec.get(0).getAsFloat();
                        y = vec.get(1).getAsFloat();
                        z = vec.get(2).getAsFloat();
                    }

                    frames.add(new KeyFrame(time, x, y, z, linear));
                }

                // Sort by time
                frames.sort(Comparator.comparingDouble(f -> f.time));

                if (loop && !frames.isEmpty()) {
                    KeyFrame last = frames.get(frames.size() - 1);
                    if (last.time < length) {
                        KeyFrame first = frames.get(0);
                        frames.add(new KeyFrame(length, first.x, first.y, first.z, last.linear));
                    }
                }
            }
        }

        return frames;
    }

    private static float getMaxTime(JsonObject boneObj, String channel) {
        if (!boneObj.has(channel)) return 0.0f;
        JsonElement channelElem = boneObj.get(channel);
        if (!channelElem.isJsonObject()) return 0.0f;
        JsonObject keyframes = channelElem.getAsJsonObject();
        float max = 0.0f;
        for (Map.Entry<String, JsonElement> entry : keyframes.entrySet()) {
            String key = entry.getKey();
            if (key.equals("post") || key.equals("pre") || key.equals("vector")) continue;
            try {
                float time = Float.parseFloat(key);
                if (time > max) max = time;
            } catch (NumberFormatException ignored) {}
        }
        return max;
    }

    private static class EmoteBoneFrame {
        Float pitch, yaw, roll;
        Float x, y, z;
        boolean linear = true;
    }

    private static Map<String, AnimationClip> parseEmotecraft(JsonObject root, String prefix) {
        Map<String, AnimationClip> result = new HashMap<>();
        String rawName = root.has("name") ? root.get("name").getAsString() : "unknown";
        String animName = (prefix == null ? "" : prefix) + rawName;

        JsonObject emote = root.getAsJsonObject("emote");
        boolean isLoop = false;
        if (emote.has("isLoop")) {
            JsonElement loopEl = emote.get("isLoop");
            if (loopEl.isJsonPrimitive() && loopEl.getAsJsonPrimitive().isString()) {
                isLoop = Boolean.parseBoolean(loopEl.getAsString());
            } else {
                isLoop = loopEl.getAsBoolean();
            }
        }
        
        int endTick = emote.has("endTick") ? emote.get("endTick").getAsInt() : 0;
        int stopTick = emote.has("stopTick") ? emote.get("stopTick").getAsInt() : endTick;
        float length = stopTick * 0.05f;
        boolean degrees = emote.has("degrees") && emote.get("degrees").getAsBoolean();

        Map<String, Map<Integer, EmoteBoneFrame>> boneFrames = new HashMap<>();
        
        if (emote.has("moves")) {
            JsonArray moves = emote.getAsJsonArray("moves");
            for (JsonElement moveEl : moves) {
                JsonObject move = moveEl.getAsJsonObject();
                int tick = move.has("tick") ? move.get("tick").getAsInt() : 0;
                boolean linear = true;
                if (move.has("easing")) {
                    linear = move.get("easing").getAsString().equalsIgnoreCase("LINEAR");
                }
                
                for (Map.Entry<String, JsonElement> entry : move.entrySet()) {
                    String key = entry.getKey();
                    if (key.equals("tick") || key.equals("easing") || key.equals("turn")) continue;
                    
                    JsonObject boneObj = entry.getValue().getAsJsonObject();
                    Map<Integer, EmoteBoneFrame> frames = boneFrames.computeIfAbsent(key, k -> new HashMap<>());
                    EmoteBoneFrame frame = frames.computeIfAbsent(tick, k -> new EmoteBoneFrame());
                    frame.linear = linear;
                    
                    if (boneObj.has("pitch")) frame.pitch = boneObj.get("pitch").getAsFloat();
                    if (boneObj.has("yaw")) frame.yaw = boneObj.get("yaw").getAsFloat();
                    if (boneObj.has("roll")) frame.roll = boneObj.get("roll").getAsFloat();
                    
                    if (boneObj.has("x")) frame.x = boneObj.get("x").getAsFloat();
                    if (boneObj.has("y")) frame.y = boneObj.get("y").getAsFloat();
                    if (boneObj.has("z")) frame.z = boneObj.get("z").getAsFloat();


                }
            }
        }
        
        // We do NOT inject a manual stopTick frame here.
        // Instead, the clip's length is set to stopTick * 0.05f.
        // The KeyframeAnimationPlayer will clamp to endTick values,
        // and the AbstractFadeModifier will smoothly blend from those clamped 
        // values back to the vanilla pose (camera rotation, etc.) during fade-out.

        Map<String, BoneTrack> boneTracks = new HashMap<>();
        for (Map.Entry<String, Map<Integer, EmoteBoneFrame>> boneEntry : boneFrames.entrySet()) {
            String rawBoneName = boneEntry.getKey();
            String boneName = "torso".equals(rawBoneName) ? "body" : rawBoneName;
            
            Map<Integer, EmoteBoneFrame> frames = boneEntry.getValue();
            
            List<Integer> ticks = new ArrayList<>(frames.keySet());
            Collections.sort(ticks);
            
            List<KeyFrame> rotKeyFrames = new ArrayList<>();
            List<KeyFrame> posKeyFrames = new ArrayList<>();
            
            float lastPitch = 0, lastYaw = 0, lastRoll = 0;
            float lastX = 0, lastY = 0, lastZ = 0;
            
            for (int tick : ticks) {
                EmoteBoneFrame frame = frames.get(tick);
                boolean hasRot = false;
                boolean hasPos = false;
                
                if (frame.pitch != null) { lastPitch = frame.pitch; hasRot = true; }
                if (frame.yaw != null) { lastYaw = frame.yaw; hasRot = true; }
                if (frame.roll != null) { lastRoll = frame.roll; hasRot = true; }
                
                if (hasRot) {
                    float rotX = degrees ? lastPitch : (float)Math.toDegrees(lastPitch);
                    float rotY = degrees ? lastYaw : (float)Math.toDegrees(lastYaw);
                    float rotZ = degrees ? lastRoll : (float)Math.toDegrees(lastRoll);
                    
                    // Emotecraft JSON natively supports 1.16+ models which have the exact
                    // same rotation axes as 1.12.2 ModelBiped. No manual inversion is needed.
                    rotKeyFrames.add(new KeyFrame(tick * 0.05f, rotX, rotY, rotZ, frame.linear));
                }
                
                if (frame.x != null) { lastX = frame.x - getVanillaBaseX(boneName); hasPos = true; }
                if (frame.y != null) { lastY = -(frame.y - getVanillaBaseY(boneName)); hasPos = true; }
                if (frame.z != null) { lastZ = frame.z - getVanillaBaseZ(boneName); hasPos = true; }
                
                if (hasPos) {
                    posKeyFrames.add(new KeyFrame(tick * 0.05f, lastX, lastY, lastZ, frame.linear));
                }
            }
            
            if (isLoop) {
                if (!rotKeyFrames.isEmpty() && rotKeyFrames.get(rotKeyFrames.size() - 1).time < length) {
                    KeyFrame first = rotKeyFrames.get(0);
                    KeyFrame last = rotKeyFrames.get(rotKeyFrames.size() - 1);
                    rotKeyFrames.add(new KeyFrame(length, first.x, first.y, first.z, last.linear));
                }
                if (!posKeyFrames.isEmpty() && posKeyFrames.get(posKeyFrames.size() - 1).time < length) {
                    KeyFrame first = posKeyFrames.get(0);
                    KeyFrame last = posKeyFrames.get(posKeyFrames.size() - 1);
                    posKeyFrames.add(new KeyFrame(length, first.x, first.y, first.z, last.linear));
                }
            }
            
            boneTracks.put(boneName, new BoneTrack(boneName, rotKeyFrames, posKeyFrames));
        }

        result.put(animName, new AnimationClip(animName, length, isLoop, boneTracks));
        return result;
    }

    private static float getVanillaBaseX(String boneName) {
        if ("rightArm".equals(boneName) || "rightItem".equals(boneName)) return -5.0f;
        if ("leftArm".equals(boneName) || "leftItem".equals(boneName)) return 5.0f;
        if ("rightLeg".equals(boneName)) return -1.9f;
        if ("leftLeg".equals(boneName)) return 1.9f;
        return 0.0f;
    }

    private static float getVanillaBaseY(String boneName) {
        if ("rightArm".equals(boneName) || "leftArm".equals(boneName) || "rightItem".equals(boneName) || "leftItem".equals(boneName)) return 2.0f;
        if ("rightLeg".equals(boneName) || "leftLeg".equals(boneName)) return 12.0f;
        // head and body are 0.0f
        return 0.0f;
    }

    private static float getVanillaBaseZ(String boneName) {
        return 0.0f; // Vanilla base Z is 0.0 for all standard bones
    }
}
