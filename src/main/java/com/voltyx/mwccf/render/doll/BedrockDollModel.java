package com.voltyx.mwccf.render.doll;

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

/**
 * Bedrock-рендерер специально для куклы Сайи.
 *
 * Ключевое отличие от BedrockBlockModel:
 * В формате Bedrock geo все bone.pivot — АБСОЛЮТНЫЕ координаты в пространстве модели.
 * Правильный алгоритм ротации вокруг пивота:
 *   T(pivot) * R(rotation) * T(-pivot)
 * применяется к каждому боне, кубы рисуются в абсолютных координатах модели.
 */
public class BedrockDollModel {

    // =========================================================
    // Data classes
    // =========================================================

    public static class FaceUV {
        public final float u1, v1, u2, v2;
        public FaceUV(float u1, float v1, float u2, float v2) {
            this.u1 = u1; this.v1 = v1; this.u2 = u2; this.v2 = v2;
        }
    }

    public static class Cube {
        public final float[] origin = new float[3];
        public final float[] size   = new float[3];
        public final float[] cubePivot;
        public final float[] cubeRot;
        public boolean hasPerFaceUV = false;
        public final Map<String, FaceUV> faceUVMap = new HashMap<>();
        public float boxU = 0, boxV = 0;

        public Cube(float ox, float oy, float oz,
                    float sx, float sy, float sz,
                    float[] cubePivot, float[] cubeRot) {
            origin[0] = ox; origin[1] = oy; origin[2] = oz;
            size[0]   = sx; size[1]   = sy; size[2]   = sz;
            this.cubePivot = cubePivot;
            this.cubeRot   = cubeRot;
        }
    }

    public static class Bone {
        public final String name;
        public final String parentName;
        public final float[] pivot    = new float[3];
        public final float[] rotation = new float[3];

        public final List<Cube> cubes    = new ArrayList<>();
        public final List<Bone> children = new ArrayList<>();

        // Animated offsets (added on top of static rotation/position)
        public final float[] animRot = new float[3];
        public final float[] animPos = new float[3];

        public Bone(String name, String parentName, float[] pivot, float[] rotation) {
            this.name       = name;
            this.parentName = parentName;
            System.arraycopy(pivot,    0, this.pivot,    0, 3);
            System.arraycopy(rotation, 0, this.rotation, 0, 3);
        }
    }

    // =========================================================
    // Animation
    // =========================================================

    public static class Keyframe {
        public final float time, x, y, z;
        public Keyframe(float time, float x, float y, float z) {
            this.time = time; this.x = x; this.y = y; this.z = z;
        }
    }

    public static class BoneTrack {
        public final List<Keyframe> rotKFs = new ArrayList<>();
        public final List<Keyframe> posKFs = new ArrayList<>();
    }

    public static class Animation {
        public final String name;
        public final float  length;
        public final String loopMode;
        public final Map<String, BoneTrack> tracks = new HashMap<>();
        public Animation(String name, float length, String loopMode) {
            this.name = name; this.length = length; this.loopMode = loopMode;
        }
    }

    // =========================================================
    // Fields
    // =========================================================

    private float textureWidth  = 64f;
    private float textureHeight = 64f;

    private final Map<String, Bone> bonesByName = new LinkedHashMap<>();
    private final List<Bone>        rootBones   = new ArrayList<>();
    private final Map<String, Animation> animations = new HashMap<>();
    private boolean loaded = false;

    public boolean isLoaded() { return loaded; }

    // =========================================================
    // Loading
    // =========================================================

    public void load(ResourceLocation geoLoc, ResourceLocation animLoc) {
        if (loaded) return;
        try {
            // Geo
            InputStream geoStream = openResource(geoLoc);
            if (geoStream != null) {
                try (InputStream s = geoStream) {
                    String json = IOUtils.toString(s, StandardCharsets.UTF_8);
                    if (json.startsWith("\uFEFF")) json = json.substring(1);
                    parseGeometry(json);
                }
            }
            // Animation
            if (animLoc != null) {
                InputStream animStream = openResource(animLoc);
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
            System.err.println("[BedrockDollModel] Failed to load: " + t.getMessage());
            t.printStackTrace();
        }
    }

    private InputStream openResource(ResourceLocation loc) {
        try {
            IResource res = Minecraft.getMinecraft().getResourceManager().getResource(loc);
            return res.getInputStream();
        } catch (Throwable ignored) {
            return BedrockDollModel.class.getResourceAsStream(
                    "/assets/" + loc.getNamespace() + "/" + loc.getPath());
        }
    }

    // =========================================================
    // Geometry parsing
    // =========================================================

    private void parseGeometry(String json) {
        JsonObject root  = new JsonParser().parse(json).getAsJsonObject();
        JsonArray  geoms = root.getAsJsonArray("minecraft:geometry");
        if (geoms == null || geoms.size() == 0) return;

        JsonObject geom = geoms.get(0).getAsJsonObject();
        if (geom.has("description")) {
            JsonObject desc = geom.getAsJsonObject("description");
            if (desc.has("texture_width"))  textureWidth  = desc.get("texture_width").getAsFloat();
            if (desc.has("texture_height")) textureHeight = desc.get("texture_height").getAsFloat();
        }

        JsonArray bonesArr = geom.getAsJsonArray("bones");
        if (bonesArr == null) return;

        bonesByName.clear();
        rootBones.clear();

        for (int i = 0; i < bonesArr.size(); i++) {
            JsonObject bObj   = bonesArr.get(i).getAsJsonObject();
            String     name   = bObj.get("name").getAsString();
            String     parent = bObj.has("parent") ? bObj.get("parent").getAsString() : null;

            float[] pivot = parseVec3(bObj, "pivot",    new float[]{0,0,0});
            float[] rot   = parseVec3(bObj, "rotation", new float[]{0,0,0});

            Bone bone = new Bone(name, parent, pivot, rot);

            if (bObj.has("cubes")) {
                for (JsonElement ce : bObj.getAsJsonArray("cubes")) {
                    JsonObject cObj = ce.getAsJsonObject();

                    JsonArray orig = cObj.getAsJsonArray("origin");
                    float ox = orig.get(0).getAsFloat();
                    float oy = orig.get(1).getAsFloat();
                    float oz = orig.get(2).getAsFloat();

                    JsonArray szArr = cObj.getAsJsonArray("size");
                    float sx = szArr.get(0).getAsFloat();
                    float sy = szArr.get(1).getAsFloat();
                    float sz = szArr.get(2).getAsFloat();

                    float[] cubePivot = cObj.has("pivot")    ? parseVec3Arr(cObj.getAsJsonArray("pivot"))    : null;
                    float[] cubeRot   = cObj.has("rotation") ? parseVec3Arr(cObj.getAsJsonArray("rotation")) : null;

                    Cube cube = new Cube(ox, oy, oz, sx, sy, sz, cubePivot, cubeRot);

                    if (cObj.has("uv")) {
                        JsonElement uvElem = cObj.get("uv");
                        if (uvElem.isJsonObject()) {
                            cube.hasPerFaceUV = true;
                            JsonObject uvObj = uvElem.getAsJsonObject();
                            for (Map.Entry<String, JsonElement> fe : uvObj.entrySet()) {
                                String faceName = fe.getKey().toLowerCase(Locale.ROOT);
                                if (fe.getValue().isJsonObject()) {
                                    JsonObject fd = fe.getValue().getAsJsonObject();
                                    float u = 0, v = 0, us = 0, vs = 0;
                                    if (fd.has("uv")) {
                                        JsonArray a = fd.getAsJsonArray("uv");
                                        u = a.get(0).getAsFloat(); v = a.get(1).getAsFloat();
                                    }
                                    if (fd.has("uv_size")) {
                                        JsonArray a = fd.getAsJsonArray("uv_size");
                                        us = a.get(0).getAsFloat(); vs = a.get(1).getAsFloat();
                                    }
                                    cube.faceUVMap.put(faceName, new FaceUV(u, v, u + us, v + vs));
                                }
                            }
                        } else if (uvElem.isJsonArray()) {
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

        // Build tree
        for (Bone bone : bonesByName.values()) {
            if (bone.parentName != null && bonesByName.containsKey(bone.parentName)) {
                bonesByName.get(bone.parentName).children.add(bone);
            } else {
                rootBones.add(bone);
            }
        }
    }

    // =========================================================
    // Animation parsing
    // =========================================================

    private void parseAnimation(String json) {
        JsonObject root = new JsonParser().parse(json).getAsJsonObject();
        if (!root.has("animations")) return;

        for (Map.Entry<String, JsonElement> e : root.getAsJsonObject("animations").entrySet()) {
            String     animName = e.getKey();
            JsonObject aObj     = e.getValue().getAsJsonObject();

            float  length   = aObj.has("animation_length") ? aObj.get("animation_length").getAsFloat() : 1.0f;
            String loopMode = "hold_on_last_frame";
            if (aObj.has("loop")) {
                JsonElement l = aObj.get("loop");
                if (l.isJsonPrimitive()) loopMode = l.getAsString();
            }

            Animation anim = new Animation(animName, length, loopMode);

            if (aObj.has("bones")) {
                for (Map.Entry<String, JsonElement> be : aObj.getAsJsonObject("bones").entrySet()) {
                    String     bName = be.getKey();
                    JsonObject bData = be.getValue().getAsJsonObject();
                    BoneTrack  track = new BoneTrack();
                    if (bData.has("rotation")) parseKFs(bData.get("rotation"), track.rotKFs);
                    if (bData.has("position")) parseKFs(bData.get("position"), track.posKFs);
                    anim.tracks.put(bName, track);
                }
            }

            animations.put(animName, anim);
        }
    }

    private void parseKFs(JsonElement elem, List<Keyframe> target) {
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
                            JsonArray v = vo.has("vector") ? vo.getAsJsonArray("vector")
                                        : vo.has("post")   ? vo.getAsJsonArray("post") : null;
                            if (v != null)
                                target.add(new Keyframe(time, v.get(0).getAsFloat(), v.get(1).getAsFloat(), v.get(2).getAsFloat()));
                        } else if (val.isJsonArray()) {
                            JsonArray v = val.getAsJsonArray();
                            target.add(new Keyframe(time, v.get(0).getAsFloat(), v.get(1).getAsFloat(), v.get(2).getAsFloat()));
                        }
                    } catch (Throwable ignored) {}
                }
            }
        } else if (elem.isJsonArray()) {
            JsonArray v = elem.getAsJsonArray();
            target.add(new Keyframe(0f, v.get(0).getAsFloat(), v.get(1).getAsFloat(), v.get(2).getAsFloat()));
        }
        target.sort(Comparator.comparingDouble(k -> k.time));
    }

    // =========================================================
    // Animation application
    // =========================================================

    public float getAnimationLength(String name) {
        Animation a = animations.get(name);
        return a != null ? a.length : 0.5f;
    }

    public void applyAnimation(String animName, float time) {
        resetAnim();
        Animation anim = animations.get(animName);
        if (anim == null) return;

        float[] tmp = new float[3];
        for (Map.Entry<String, BoneTrack> e : anim.tracks.entrySet()) {
            Bone bone = bonesByName.get(e.getKey());
            if (bone == null) continue;
            BoneTrack track = e.getValue();
            evalKFs(track.rotKFs, time, tmp);
            bone.animRot[0] += tmp[0];
            bone.animRot[1] += tmp[1];
            bone.animRot[2] += tmp[2];
            evalKFs(track.posKFs, time, tmp);
            bone.animPos[0] += tmp[0];
            bone.animPos[1] += tmp[1];
            bone.animPos[2] += tmp[2];
        }
    }

    private void resetAnim() {
        for (Bone bone : bonesByName.values()) {
            bone.animRot[0] = bone.animRot[1] = bone.animRot[2] = 0;
            bone.animPos[0] = bone.animPos[1] = bone.animPos[2] = 0;
        }
    }

    private void evalKFs(List<Keyframe> kfs, float time, float[] out) {
        out[0] = out[1] = out[2] = 0;
        if (kfs.isEmpty()) return;
        if (kfs.size() == 1 || time <= kfs.get(0).time) {
            Keyframe k = kfs.get(0);
            out[0] = k.x; out[1] = k.y; out[2] = k.z;
            return;
        }
        Keyframe last = kfs.get(kfs.size() - 1);
        if (time >= last.time) {
            out[0] = last.x; out[1] = last.y; out[2] = last.z;
            return;
        }
        for (int i = 0; i < kfs.size() - 1; i++) {
            Keyframe k0 = kfs.get(i), k1 = kfs.get(i + 1);
            if (time >= k0.time && time <= k1.time) {
                float seg = k1.time - k0.time;
                float t   = (seg <= 0) ? 0 : (time - k0.time) / seg;
                float f   = (1f - (float) Math.cos(t * Math.PI)) * 0.5f; // cosine ease
                out[0] = k0.x + (k1.x - k0.x) * f;
                out[1] = k0.y + (k1.y - k0.y) * f;
                out[2] = k0.z + (k1.z - k0.z) * f;
                return;
            }
        }
    }

    // =========================================================
    // Rendering — CORRECT Bedrock hierarchy
    //
    // In Bedrock geo format, bone.pivot is in ABSOLUTE model space.
    // The correct bone transform is:
    //   T(pivot) * R(rotation) * T(-pivot)
    // applied from model origin, with children inheriting the result.
    // Cubes are drawn at their absolute model-space origins.
    // =========================================================

    /**
     * Render the model. Call after binding texture.
     *
     * @param scale pixels-per-block scale, typically 1/16 = 0.0625.
     *              Bedrock units: 1 unit = 1 pixel on a 16px-per-block skin.
     */
    public void render(float scale) {
        GlStateManager.pushMatrix();
        // Bedrock uses left-handed coords (mirror X)
        GlStateManager.scale(-scale, scale, scale);
        GL11.glFrontFace(GL11.GL_CW);

        for (Bone root : rootBones) {
            renderBone(root);
        }

        GL11.glFrontFace(GL11.GL_CCW);
        GlStateManager.popMatrix();
    }

    /**
     * Render a bone and all its children using the correct Bedrock pivot transform.
     * scale has already been applied by the top-level render() call.
     */
    private void renderBone(Bone bone) {
        float px = bone.pivot[0] + bone.animPos[0];
        float py = bone.pivot[1] + bone.animPos[1];
        float pz = bone.pivot[2] + bone.animPos[2];

        float rx = -(bone.rotation[0] + bone.animRot[0]);
        float ry = -(bone.rotation[1] + bone.animRot[1]);
        float rz = -(bone.rotation[2] + bone.animRot[2]);

        GlStateManager.pushMatrix();

        // Bedrock bone transform: T(pivot) * R * T(-pivot)
        GlStateManager.translate(px, py, pz);
        if (rz != 0f) GlStateManager.rotate(rz, 0f, 0f, 1f);
        if (ry != 0f) GlStateManager.rotate(ry, 0f, 1f, 0f);
        if (rx != 0f) GlStateManager.rotate(rx, 1f, 0f, 0f);
        GlStateManager.translate(-px, -py, -pz);

        // Cubes are drawn at their absolute model-space positions
        for (Cube cube : bone.cubes) {
            renderCube(cube, bone);
        }

        // Children inherit the current transform
        for (Bone child : bone.children) {
            renderBone(child);
        }

        GlStateManager.popMatrix();
    }

    private void renderCube(Cube cube, Bone bone) {
        GlStateManager.pushMatrix();

        // Per-cube local rotation around its own pivot (in model space)
        if (cube.cubePivot != null && cube.cubeRot != null) {
            float cpx = cube.cubePivot[0];
            float cpy = cube.cubePivot[1];
            float cpz = cube.cubePivot[2];
            GlStateManager.translate(cpx, cpy, cpz);
            if (cube.cubeRot[2] != 0f) GlStateManager.rotate(-cube.cubeRot[2], 0f, 0f, 1f);
            if (cube.cubeRot[1] != 0f) GlStateManager.rotate(-cube.cubeRot[1], 0f, 1f, 0f);
            if (cube.cubeRot[0] != 0f) GlStateManager.rotate(-cube.cubeRot[0], 1f, 0f, 0f);
            GlStateManager.translate(-cpx, -cpy, -cpz);
        }

        // Cube in absolute model-space coords
        float x0 = cube.origin[0];
        float y0 = cube.origin[1];
        float z0 = cube.origin[2];
        float x1 = x0 + cube.size[0];
        float y1 = y0 + cube.size[1];
        float z1 = z0 + cube.size[2];

        float tw = textureWidth;
        float th = textureHeight;

        Tessellator    tess = Tessellator.getInstance();
        BufferBuilder  buf  = tess.getBuffer();

        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        if (cube.hasPerFaceUV) {
            // East (+X)
            FaceUV east = cube.faceUVMap.get("east");
            if (east != null) drawFace(buf, east, tw, th,
                x1,y1,z0,  x1,y1,z1,  x1,y0,z1,  x1,y0,z0,  1,0,0);
            // West (-X)
            FaceUV west = cube.faceUVMap.get("west");
            if (west != null) drawFace(buf, west, tw, th,
                x0,y1,z1,  x0,y1,z0,  x0,y0,z0,  x0,y0,z1,  -1,0,0);
            // Up (+Y)
            FaceUV up = cube.faceUVMap.get("up");
            if (up != null) drawFace(buf, up, tw, th,
                x1,y1,z0,  x0,y1,z0,  x0,y1,z1,  x1,y1,z1,  0,1,0);
            // Down (-Y)
            FaceUV down = cube.faceUVMap.get("down");
            if (down != null) drawFace(buf, down, tw, th,
                x1,y0,z0,  x0,y0,z0,  x0,y0,z1,  x1,y0,z1,  0,-1,0);
            // North (-Z)
            FaceUV north = cube.faceUVMap.get("north");
            if (north != null) drawFace(buf, north, tw, th,
                x0,y1,z0,  x1,y1,z0,  x1,y0,z0,  x0,y0,z0,  0,0,-1);
            // South (+Z)
            FaceUV south = cube.faceUVMap.get("south");
            if (south != null) drawFace(buf, south, tw, th,
                x1,y1,z1,  x0,y1,z1,  x0,y0,z1,  x1,y0,z1,  0,0,1);
        } else {
            // Box UV mapping
            float u = cube.boxU, v = cube.boxV;
            float w = cube.size[0], h = cube.size[1], d = cube.size[2];
            float u0, v0, u1, v1;

            // Up
            u0=(u+d)/tw; v0=v/th; u1=(u+d+w)/tw; v1=(v+d)/th;
            vert(buf,x1,y1,z0,u1,v1,0,1,0); vert(buf,x0,y1,z0,u0,v1,0,1,0);
            vert(buf,x0,y1,z1,u0,v0,0,1,0); vert(buf,x1,y1,z1,u1,v0,0,1,0);
            // Down
            u0=(u+d+w)/tw; v0=v/th; u1=(u+d+w+w)/tw; v1=(v+d)/th;
            vert(buf,x1,y0,z0,u0,v0,0,-1,0); vert(buf,x0,y0,z0,u1,v0,0,-1,0);
            vert(buf,x0,y0,z1,u1,v1,0,-1,0); vert(buf,x1,y0,z1,u0,v1,0,-1,0);
            // North
            u0=(u+d)/tw; v0=(v+d)/th; u1=(u+d+w)/tw; v1=(v+d+h)/th;
            vert(buf,x0,y1,z0,u0,v0,0,0,-1); vert(buf,x1,y1,z0,u1,v0,0,0,-1);
            vert(buf,x1,y0,z0,u1,v1,0,0,-1); vert(buf,x0,y0,z0,u0,v1,0,0,-1);
            // South
            u0=(u+d+w+d)/tw; v0=(v+d)/th; u1=(u+d+w+d+w)/tw; v1=(v+d+h)/th;
            vert(buf,x1,y1,z1,u0,v0,0,0,1); vert(buf,x0,y1,z1,u1,v0,0,0,1);
            vert(buf,x0,y0,z1,u1,v1,0,0,1); vert(buf,x1,y0,z1,u0,v1,0,0,1);
            // West
            u0=u/tw; v0=(v+d)/th; u1=(u+d)/tw; v1=(v+d+h)/th;
            vert(buf,x0,y1,z1,u0,v0,-1,0,0); vert(buf,x0,y1,z0,u1,v0,-1,0,0);
            vert(buf,x0,y0,z0,u1,v1,-1,0,0); vert(buf,x0,y0,z1,u0,v1,-1,0,0);
            // East
            u0=(u+d+w)/tw; v0=(v+d)/th; u1=(u+d+w+d)/tw; v1=(v+d+h)/th;
            vert(buf,x1,y1,z0,u0,v0,1,0,0); vert(buf,x1,y1,z1,u1,v0,1,0,0);
            vert(buf,x1,y0,z1,u1,v1,1,0,0); vert(buf,x1,y0,z0,u0,v1,1,0,0);
        }

        tess.draw();
        GlStateManager.popMatrix();
    }

    private void drawFace(BufferBuilder buf, FaceUV uv, float tw, float th,
                          float ax, float ay, float az,
                          float bx, float by, float bz,
                          float cx, float cy, float cz,
                          float dx, float dy, float dz,
                          float nx, float ny, float nz) {
        float u0 = uv.u1 / tw, v0 = uv.v1 / th;
        float u1 = uv.u2 / tw, v1 = uv.v2 / th;
        vert(buf,ax,ay,az, u0,v0, nx,ny,nz);
        vert(buf,bx,by,bz, u1,v0, nx,ny,nz);
        vert(buf,cx,cy,cz, u1,v1, nx,ny,nz);
        vert(buf,dx,dy,dz, u0,v1, nx,ny,nz);
    }

    private static void vert(BufferBuilder buf, float x, float y, float z,
                              float u, float v, float nx, float ny, float nz) {
        buf.pos(x, y, z).tex(u, v).normal(nx, ny, nz).endVertex();
    }

    // =========================================================
    // Helpers
    // =========================================================

    private float[] parseVec3(JsonObject obj, String key, float[] def) {
        if (!obj.has(key)) return def;
        return parseVec3Arr(obj.getAsJsonArray(key));
    }

    private float[] parseVec3Arr(JsonArray arr) {
        return new float[]{ arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat() };
    }
}
