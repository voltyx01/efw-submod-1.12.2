package com.voltyx.mwccf.render.bedrock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BedrockBlockModel {

    public static class FaceUV {
        public final float u1, v1, u2, v2;

        public FaceUV(float u1, float v1, float u2, float v2) {
            this.u1 = u1;
            this.v1 = v1;
            this.u2 = u2;
            this.v2 = v2;
        }
    }

    public static class Cube {
        public final float[] origin = new float[3];
        public final float[] size = new float[3];
        public final float[] cubePivot;
        public final float[] cubeRot;

        // Box UV format
        public float boxU = 0, boxV = 0;
        public boolean hasPerFaceUV = false;

        // Per-face UV format (north, south, east, west, up, down)
        public final Map<String, FaceUV> faceUVMap = new HashMap<>();

        public Cube(float ox, float oy, float oz, float sx, float sy, float sz,
                    float[] cubePivot, float[] cubeRot) {
            this.origin[0] = ox;
            this.origin[1] = oy;
            this.origin[2] = oz;
            this.size[0] = sx;
            this.size[1] = sy;
            this.size[2] = sz;
            this.cubePivot = cubePivot;
            this.cubeRot = cubeRot;
        }
    }

    public static class Bone {
        public final String name;
        public final String parentName;
        public final float[] pivot = new float[3];
        public final float[] rotation = new float[3];

        public final List<Cube> cubes = new ArrayList<>();
        public final List<Bone> children = new ArrayList<>();

        // Animated offsets for current frame
        public final float[] animPos = new float[3];
        public final float[] animRot = new float[3];

        public Bone(String name, String parentName, float[] pivot, float[] rotation) {
            this.name = name;
            this.parentName = parentName;
            System.arraycopy(pivot, 0, this.pivot, 0, 3);
            System.arraycopy(rotation, 0, this.rotation, 0, 3);
        }
    }

    public static class Keyframe {
        public final float time;
        public final float x, y, z;

        public Keyframe(float time, float x, float y, float z) {
            this.time = time;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static class BoneTrack {
        public final List<Keyframe> rotKeyframes = new ArrayList<>();
        public final List<Keyframe> posKeyframes = new ArrayList<>();
    }

    public static class Animation {
        public final String name;
        public final float length;
        public final String loopMode;
        public final Map<String, BoneTrack> tracks = new HashMap<>();

        public Animation(String name, float length, String loopMode) {
            this.name = name;
            this.length = length;
            this.loopMode = loopMode;
        }
    }

    private float textureWidth = 64f;
    private float textureHeight = 64f;

    private final Map<String, Bone> bonesByName = new LinkedHashMap<>();
    private final List<Bone> rootBones = new ArrayList<>();
    private final Map<String, Animation> animations = new HashMap<>();
    private boolean loaded = false;

    public boolean isLoaded() {
        return loaded;
    }

    public void load(ResourceLocation geoLoc, ResourceLocation animLoc) {
        if (loaded) return;
        try {
            // 1. Load Geometry
            InputStream geoStream = null;
            try {
                IResource resGeo = Minecraft.getMinecraft().getResourceManager().getResource(geoLoc);
                geoStream = resGeo.getInputStream();
            } catch (Throwable ignored) {
                geoStream = BedrockBlockModel.class.getResourceAsStream("/assets/" + geoLoc.getNamespace() + "/" + geoLoc.getPath());
            }

            if (geoStream != null) {
                try (InputStream s = geoStream) {
                    String json = IOUtils.toString(s, StandardCharsets.UTF_8);
                    if (json.startsWith("\uFEFF")) json = json.substring(1);
                    parseGeometry(json);
                }
            }

            // 2. Load Animation if provided
            if (animLoc != null) {
                InputStream animStream = null;
                try {
                    IResource resAnim = Minecraft.getMinecraft().getResourceManager().getResource(animLoc);
                    animStream = resAnim.getInputStream();
                } catch (Throwable ignored) {
                    animStream = BedrockBlockModel.class.getResourceAsStream("/assets/" + animLoc.getNamespace() + "/" + animLoc.getPath());
                }

                if (animStream != null) {
                    try (InputStream s = animStream) {
                        String json = IOUtils.toString(s, StandardCharsets.UTF_8);
                        if (json.startsWith("\uFEFF")) json = json.substring(1);
                        parseAnimation(json);
                    }
                }
            }

            loaded = true;
        } catch (Throwable t) {
            System.err.println("[BedrockBlockModel] Failed to load model " + geoLoc + ": " + t.getMessage());
            t.printStackTrace();
        }
    }

    private void parseGeometry(String json) {
        JsonObject root = new JsonParser().parse(json).getAsJsonObject();
        JsonArray geoms = root.getAsJsonArray("minecraft:geometry");
        if (geoms == null || geoms.size() == 0) return;

        JsonObject geom = geoms.get(0).getAsJsonObject();
        if (geom.has("description")) {
            JsonObject desc = geom.getAsJsonObject("description");
            if (desc.has("texture_width")) textureWidth = desc.get("texture_width").getAsFloat();
            if (desc.has("texture_height")) textureHeight = desc.get("texture_height").getAsFloat();
        }

        JsonArray bonesArr = geom.getAsJsonArray("bones");
        if (bonesArr == null) return;

        bonesByName.clear();
        rootBones.clear();

        for (int i = 0; i < bonesArr.size(); i++) {
            JsonObject bObj = bonesArr.get(i).getAsJsonObject();
            String name = bObj.get("name").getAsString();
            String parent = bObj.has("parent") ? bObj.get("parent").getAsString() : null;

            float[] pivot = new float[]{0, 0, 0};
            if (bObj.has("pivot")) {
                JsonArray piv = bObj.getAsJsonArray("pivot");
                pivot = new float[]{piv.get(0).getAsFloat(), piv.get(1).getAsFloat(), piv.get(2).getAsFloat()};
            }

            float[] rotation = new float[]{0, 0, 0};
            if (bObj.has("rotation")) {
                JsonArray rot = bObj.getAsJsonArray("rotation");
                rotation = new float[]{rot.get(0).getAsFloat(), rot.get(1).getAsFloat(), rot.get(2).getAsFloat()};
            }

            Bone bone = new Bone(name, parent, pivot, rotation);

            if (bObj.has("cubes")) {
                JsonArray cubesArr = bObj.getAsJsonArray("cubes");
                for (int c = 0; c < cubesArr.size(); c++) {
                    JsonObject cObj = cubesArr.get(c).getAsJsonObject();

                    JsonArray orig = cObj.getAsJsonArray("origin");
                    float ox = orig.get(0).getAsFloat();
                    float oy = orig.get(1).getAsFloat();
                    float oz = orig.get(2).getAsFloat();

                    JsonArray szArr = cObj.getAsJsonArray("size");
                    float sx = szArr.get(0).getAsFloat();
                    float sy = szArr.get(1).getAsFloat();
                    float sz = szArr.get(2).getAsFloat();

                    float[] cubePivot = null;
                    if (cObj.has("pivot")) {
                        JsonArray cp = cObj.getAsJsonArray("pivot");
                        cubePivot = new float[]{cp.get(0).getAsFloat(), cp.get(1).getAsFloat(), cp.get(2).getAsFloat()};
                    }

                    float[] cubeRot = null;
                    if (cObj.has("rotation")) {
                        JsonArray cr = cObj.getAsJsonArray("rotation");
                        cubeRot = new float[]{cr.get(0).getAsFloat(), cr.get(1).getAsFloat(), cr.get(2).getAsFloat()};
                    }

                    Cube cube = new Cube(ox, oy, oz, sx, sy, sz, cubePivot, cubeRot);

                    if (cObj.has("uv")) {
                        JsonElement uvElem = cObj.get("uv");
                        if (uvElem.isJsonObject()) {
                            cube.hasPerFaceUV = true;
                            JsonObject uvObj = uvElem.getAsJsonObject();
                            for (Map.Entry<String, JsonElement> faceEntry : uvObj.entrySet()) {
                                String faceName = faceEntry.getKey().toLowerCase(Locale.ROOT);
                                if (faceEntry.getValue().isJsonObject()) {
                                    JsonObject faceData = faceEntry.getValue().getAsJsonObject();
                                    float u = 0, v = 0, us = 0, vs = 0;
                                    if (faceData.has("uv")) {
                                        JsonArray uvArr = faceData.getAsJsonArray("uv");
                                        u = uvArr.get(0).getAsFloat();
                                        v = uvArr.get(1).getAsFloat();
                                    }
                                    if (faceData.has("uv_size")) {
                                        JsonArray uvsArr = faceData.getAsJsonArray("uv_size");
                                        us = uvsArr.get(0).getAsFloat();
                                        vs = uvsArr.get(1).getAsFloat();
                                    }
                                    cube.faceUVMap.put(faceName, new FaceUV(u, v, u + us, v + vs));
                                }
                            }
                        } else if (uvElem.isJsonArray()) {
                            cube.hasPerFaceUV = false;
                            JsonArray uvArr = uvElem.getAsJsonArray();
                            cube.boxU = uvArr.get(0).getAsFloat();
                            cube.boxV = uvArr.get(1).getAsFloat();
                        }
                    }

                    bone.cubes.add(cube);
                }
            }

            bonesByName.put(name, bone);
        }

        // Build hierarchy tree
        for (Bone bone : bonesByName.values()) {
            if (bone.parentName != null && bonesByName.containsKey(bone.parentName)) {
                bonesByName.get(bone.parentName).children.add(bone);
            } else {
                rootBones.add(bone);
            }
        }
    }

    private void parseAnimation(String json) {
        JsonObject root = new JsonParser().parse(json).getAsJsonObject();
        if (!root.has("animations")) return;

        JsonObject anims = root.getAsJsonObject("animations");
        for (Map.Entry<String, JsonElement> entry : anims.entrySet()) {
            String name = entry.getKey();
            JsonObject aObj = entry.getValue().getAsJsonObject();

            float length = aObj.has("animation_length") ? aObj.get("animation_length").getAsFloat() : 1.0f;
            String loopMode = "hold_on_last_frame";
            if (aObj.has("loop")) {
                JsonElement lElem = aObj.get("loop");
                if (lElem.isJsonPrimitive()) {
                    loopMode = lElem.getAsString();
                }
            }

            Animation anim = new Animation(name, length, loopMode);

            if (aObj.has("bones")) {
                JsonObject bones = aObj.getAsJsonObject("bones");
                for (Map.Entry<String, JsonElement> bEntry : bones.entrySet()) {
                    String bName = bEntry.getKey();
                    JsonObject bData = bEntry.getValue().getAsJsonObject();

                    BoneTrack track = new BoneTrack();
                    if (bData.has("rotation")) {
                        parseKeyframes(bData.get("rotation"), track.rotKeyframes);
                    }
                    if (bData.has("position")) {
                        parseKeyframes(bData.get("position"), track.posKeyframes);
                    }
                    anim.tracks.put(bName, track);
                }
            }

            animations.put(name, anim);
        }
    }

    private void parseKeyframes(JsonElement elem, List<Keyframe> target) {
        if (elem.isJsonObject()) {
            JsonObject obj = elem.getAsJsonObject();
            if (obj.has("vector")) {
                JsonArray v = obj.getAsJsonArray("vector");
                target.add(new Keyframe(0f, v.get(0).getAsFloat(), v.get(1).getAsFloat(), v.get(2).getAsFloat()));
            } else {
                for (Map.Entry<String, JsonElement> kf : obj.entrySet()) {
                    try {
                        float time = Float.parseFloat(kf.getKey());
                        JsonElement val = kf.getValue();
                        if (val.isJsonObject()) {
                            JsonObject vo = val.getAsJsonObject();
                            if (vo.has("post")) {
                                JsonArray v = vo.getAsJsonArray("post");
                                target.add(new Keyframe(time, v.get(0).getAsFloat(), v.get(1).getAsFloat(), v.get(2).getAsFloat()));
                            } else if (vo.has("vector")) {
                                JsonArray v = vo.getAsJsonArray("vector");
                                target.add(new Keyframe(time, v.get(0).getAsFloat(), v.get(1).getAsFloat(), v.get(2).getAsFloat()));
                            }
                        } else if (val.isJsonArray()) {
                            JsonArray v = val.getAsJsonArray();
                            target.add(new Keyframe(time, v.get(0).getAsFloat(), v.get(1).getAsFloat(), v.get(2).getAsFloat()));
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }
        } else if (elem.isJsonArray()) {
            JsonArray v = elem.getAsJsonArray();
            target.add(new Keyframe(0f, v.get(0).getAsFloat(), v.get(1).getAsFloat(), v.get(2).getAsFloat()));
        }
        target.sort(Comparator.comparingDouble(k -> k.time));
    }

    public void applyAnimation(String animName, float time) {
        Animation anim = animations.get(animName);
        if (anim == null && !animations.isEmpty()) {
            anim = animations.values().iterator().next();
        }
        if (anim == null) return;

        for (Bone bone : bonesByName.values()) {
            bone.animRot[0] = 0;
            bone.animRot[1] = 0;
            bone.animRot[2] = 0;
            bone.animPos[0] = 0;
            bone.animPos[1] = 0;
            bone.animPos[2] = 0;

            BoneTrack track = anim.tracks.get(bone.name);
            if (track != null) {
                evaluateKeyframes(track.rotKeyframes, time, bone.animRot);
                evaluateKeyframes(track.posKeyframes, time, bone.animPos);
            }
        }
    }

    public float getAnimationLength(String animName) {
        Animation anim = animations.get(animName);
        return anim != null ? anim.length : 0.5f;
    }

    private void evaluateKeyframes(List<Keyframe> kfs, float time, float[] out) {
        if (kfs.isEmpty()) return;
        if (kfs.size() == 1 || time <= kfs.get(0).time) {
            out[0] = kfs.get(0).x;
            out[1] = kfs.get(0).y;
            out[2] = kfs.get(0).z;
            return;
        }
        if (time >= kfs.get(kfs.size() - 1).time) {
            Keyframe last = kfs.get(kfs.size() - 1);
            out[0] = last.x;
            out[1] = last.y;
            out[2] = last.z;
            return;
        }

        for (int i = 0; i < kfs.size() - 1; i++) {
            Keyframe k0 = kfs.get(i);
            Keyframe k1 = kfs.get(i + 1);
            if (time >= k0.time && time <= k1.time) {
                float seg = k1.time - k0.time;
                float t = (seg <= 0) ? 0 : (time - k0.time) / seg;
                // Cosine smooth interpolation
                float f = (1.0f - (float) Math.cos(t * Math.PI)) * 0.5f;
                out[0] = k0.x + (k1.x - k0.x) * f;
                out[1] = k0.y + (k1.y - k0.y) * f;
                out[2] = k0.z + (k1.z - k0.z) * f;
                return;
            }
        }
    }

    public void render(float scale) {
        for (Bone root : rootBones) {
            renderBone(root, null, scale);
        }
    }

    private void renderBone(Bone bone, Bone parent, float scale) {
        GlStateManager.pushMatrix();

        float px = bone.pivot[0];
        float py = bone.pivot[1];
        float pz = bone.pivot[2];

        float ox = (parent == null) ? px : (px - parent.pivot[0]);
        float oy = (parent == null) ? py : (py - parent.pivot[1]);
        float oz = (parent == null) ? pz : (pz - parent.pivot[2]);

        float tx = (ox + bone.animPos[0]) * scale;
        float ty = (oy + bone.animPos[1]) * scale;
        float tz = (oz + bone.animPos[2]) * scale;

        GlStateManager.translate(tx, ty, tz);

        float rx = -(bone.rotation[0] + bone.animRot[0]);
        float ry = -(bone.rotation[1] + bone.animRot[1]);
        float rz = -(bone.rotation[2] + bone.animRot[2]);

        if (rz != 0f) GlStateManager.rotate(rz, 0, 0, 1);
        if (ry != 0f) GlStateManager.rotate(ry, 0, 1, 0);
        if (rx != 0f) GlStateManager.rotate(rx, 1, 0, 0);

        // Render all cubes attached to this bone
        for (Cube cube : bone.cubes) {
            renderCube(cube, bone, scale);
        }

        // Render child bones
        for (Bone child : bone.children) {
            renderBone(child, bone, scale);
        }

        GlStateManager.popMatrix();
    }

    private void renderCube(Cube cube, Bone bone, float scale) {
        GlStateManager.pushMatrix();

        if (cube.cubePivot != null && cube.cubeRot != null) {
            float cpx = (cube.cubePivot[0] - bone.pivot[0]) * scale;
            float cpy = (cube.cubePivot[1] - bone.pivot[1]) * scale;
            float cpz = (cube.cubePivot[2] - bone.pivot[2]) * scale;
            GlStateManager.translate(cpx, cpy, cpz);
            if (cube.cubeRot[2] != 0f) GlStateManager.rotate(-cube.cubeRot[2], 0, 0, 1);
            if (cube.cubeRot[1] != 0f) GlStateManager.rotate(-cube.cubeRot[1], 0, 1, 0);
            if (cube.cubeRot[0] != 0f) GlStateManager.rotate(-cube.cubeRot[0], 1, 0, 0);
            GlStateManager.translate(-cpx, -cpy, -cpz);
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();

        float x0 = (cube.origin[0] - bone.pivot[0]) * scale;
        float y0 = (cube.origin[1] - bone.pivot[1]) * scale;
        float z0 = (cube.origin[2] - bone.pivot[2]) * scale;

        float x1 = x0 + cube.size[0] * scale;
        float y1 = y0 + cube.size[1] * scale;
        float z1 = z0 + cube.size[2] * scale;

        float tw = textureWidth;
        float th = textureHeight;

        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        if (cube.hasPerFaceUV) {
            // East (+X)
            FaceUV east = cube.faceUVMap.get("east");
            if (east != null) {
                float u0 = east.u1 / tw, v0 = east.v1 / th;
                float u1 = east.u2 / tw, v1 = east.v2 / th;
                vertex(buf, x1, y1, z1, u0, v0, 1, 0, 0);
                vertex(buf, x1, y1, z0, u1, v0, 1, 0, 0);
                vertex(buf, x1, y0, z0, u1, v1, 1, 0, 0);
                vertex(buf, x1, y0, z1, u0, v1, 1, 0, 0);
            }

            // West (-X)
            FaceUV west = cube.faceUVMap.get("west");
            if (west != null) {
                float u0 = west.u1 / tw, v0 = west.v1 / th;
                float u1 = west.u2 / tw, v1 = west.v2 / th;
                vertex(buf, x0, y1, z0, u0, v0, -1, 0, 0);
                vertex(buf, x0, y1, z1, u1, v0, -1, 0, 0);
                vertex(buf, x0, y0, z1, u1, v1, -1, 0, 0);
                vertex(buf, x0, y0, z0, u0, v1, -1, 0, 0);
            }

            // Up (+Y)
            FaceUV up = cube.faceUVMap.get("up");
            if (up != null) {
                float u0 = up.u1 / tw, v0 = up.v1 / th;
                float u1 = up.u2 / tw, v1 = up.v2 / th;
                vertex(buf, x1, y1, z1, u1, v0, 0, 1, 0);
                vertex(buf, x0, y1, z1, u0, v0, 0, 1, 0);
                vertex(buf, x0, y1, z0, u0, v1, 0, 1, 0);
                vertex(buf, x1, y1, z0, u1, v1, 0, 1, 0);
            }

            // Down (-Y)
            FaceUV down = cube.faceUVMap.get("down");
            if (down != null) {
                float u0 = down.u1 / tw, v0 = down.v1 / th;
                float u1 = down.u2 / tw, v1 = down.v2 / th;
                vertex(buf, x1, y0, z0, u0, v0, 0, -1, 0);
                vertex(buf, x0, y0, z0, u1, v0, 0, -1, 0);
                vertex(buf, x0, y0, z1, u1, v1, 0, -1, 0);
                vertex(buf, x1, y0, z1, u0, v1, 0, -1, 0);
            }

            // North (-Z)
            FaceUV north = cube.faceUVMap.get("north");
            if (north != null) {
                float u0 = north.u1 / tw, v0 = north.v1 / th;
                float u1 = north.u2 / tw, v1 = north.v2 / th;
                vertex(buf, x1, y1, z0, u0, v0, 0, 0, -1);
                vertex(buf, x0, y1, z0, u1, v0, 0, 0, -1);
                vertex(buf, x0, y0, z0, u1, v1, 0, 0, -1);
                vertex(buf, x1, y0, z0, u0, v1, 0, 0, -1);
            }

            // South (+Z)
            FaceUV south = cube.faceUVMap.get("south");
            if (south != null) {
                float u0 = south.u1 / tw, v0 = south.v1 / th;
                float u1 = south.u2 / tw, v1 = south.v2 / th;
                vertex(buf, x0, y1, z1, u0, v0, 0, 0, 1);
                vertex(buf, x1, y1, z1, u1, v0, 0, 0, 1);
                vertex(buf, x1, y0, z1, u1, v1, 0, 0, 1);
                vertex(buf, x0, y0, z1, u0, v1, 0, 0, 1);
            }
        } else {
            // Standard Box UV
            float u = cube.boxU, v = cube.boxV;
            float w = cube.size[0], h = cube.size[1], d = cube.size[2];

            float u0, v0, u1, v1;

            // Up (+Y)
            u0 = (u + d) / tw; v0 = v / th; u1 = (u + d + w) / tw; v1 = (v + d) / th;
            vertex(buf, x1, y1, z1, u1, v0, 0, 1, 0);
            vertex(buf, x0, y1, z1, u0, v0, 0, 1, 0);
            vertex(buf, x0, y1, z0, u0, v1, 0, 1, 0);
            vertex(buf, x1, y1, z0, u1, v1, 0, 1, 0);

            // Down (-Y)
            u0 = (u + d + w) / tw; v0 = v / th; u1 = (u + d + w + w) / tw; v1 = (v + d) / th;
            vertex(buf, x1, y0, z0, u0, v0, 0, -1, 0);
            vertex(buf, x0, y0, z0, u1, v0, 0, -1, 0);
            vertex(buf, x0, y0, z1, u1, v1, 0, -1, 0);
            vertex(buf, x1, y0, z1, u0, v1, 0, -1, 0);

            // North (-Z)
            u0 = (u + d) / tw; v0 = (v + d) / th; u1 = (u + d + w) / tw; v1 = (v + d + h) / th;
            vertex(buf, x1, y1, z0, u0, v0, 0, 0, -1);
            vertex(buf, x0, y1, z0, u1, v0, 0, 0, -1);
            vertex(buf, x0, y0, z0, u1, v1, 0, 0, -1);
            vertex(buf, x1, y0, z0, u0, v1, 0, 0, -1);

            // South (+Z)
            u0 = (u + d + w + d) / tw; v0 = (v + d) / th; u1 = (u + d + w + d + w) / tw; v1 = (v + d + h) / th;
            vertex(buf, x0, y1, z1, u0, v0, 0, 0, 1);
            vertex(buf, x1, y1, z1, u1, v0, 0, 0, 1);
            vertex(buf, x1, y0, z1, u1, v1, 0, 0, 1);
            vertex(buf, x0, y0, z1, u0, v1, 0, 0, 1);

            // West (-X)
            u0 = u / tw; v0 = (v + d) / th; u1 = (u + d) / tw; v1 = (v + d + h) / th;
            vertex(buf, x0, y1, z0, u0, v0, -1, 0, 0);
            vertex(buf, x0, y1, z1, u1, v0, -1, 0, 0);
            vertex(buf, x0, y0, z1, u1, v1, -1, 0, 0);
            vertex(buf, x0, y0, z0, u0, v1, -1, 0, 0);

            // East (+X)
            u0 = (u + d + w) / tw; v0 = (v + d) / th; u1 = (u + d + w + d) / tw; v1 = (v + d + h) / th;
            vertex(buf, x1, y1, z1, u0, v0, 1, 0, 0);
            vertex(buf, x1, y1, z0, u1, v0, 1, 0, 0);
            vertex(buf, x1, y0, z0, u1, v1, 1, 0, 0);
            vertex(buf, x1, y0, z1, u0, v1, 1, 0, 0);
        }

        tessellator.draw();

        GlStateManager.popMatrix();
    }

    private static void vertex(BufferBuilder buf,
                               float x, float y, float z,
                               float u, float v,
                               float nx, float ny, float nz) {
        buf.pos(x, y, z).tex(u, v).normal(nx, ny, nz).endVertex();
    }
}
