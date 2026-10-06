package com.voltyx.mwccf.render.doll;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Bedrock-рендерер куклы Сайи.
 *
 * Использует ту же логику, что GeoArmorModel: куб задаётся относительно своего
 * бона через ModelRenderer. Y-ось инвертирована: Bedrock +Y (вверх) → MC -Y.
 * Иерархия родитель→дитя строится через ModelRenderer.addChild().
 * Анимации применяются как смещения к rotateAngle.
 */
public class BedrockDollModel extends ModelBase {

    // =========================================================
    // Internal data
    // =========================================================

    /** Запись о косточке: хранит ModelRenderer и оригинальный Bedrock-пивот. */
    private static class BoneEntry {
        final ModelRenderer renderer;
        final float pivX, pivY, pivZ;        // абсолютный Bedrock-пивот
        final float baseRotX, baseRotY, baseRotZ; // базовые углы из geo.json (радианы)
        final String parentName;

        BoneEntry(ModelRenderer renderer, float px, float py, float pz,
                  float bRx, float bRy, float bRz, String parentName) {
            this.renderer  = renderer;
            this.pivX = px; this.pivY = py; this.pivZ = pz;
            this.baseRotX = bRx; this.baseRotY = bRy; this.baseRotZ = bRz;
            this.parentName = parentName;
        }
    }

    private int texW = 64, texH = 64;
    private final Map<String, BoneEntry>  bones     = new LinkedHashMap<>();
    private final List<ModelRenderer>     rootBones = new ArrayList<>();

    // =========================================================
    // Animation
    // =========================================================

    private static class KF {
        final float time, x, y, z;
        KF(float t, float x, float y, float z) { time=t; this.x=x; this.y=y; this.z=z; }
    }
    private static class BoneTrack {
        final List<KF> rot = new ArrayList<>(), pos = new ArrayList<>();
    }
    private static class Anim {
        final float length; final String loop;
        final Map<String, BoneTrack> tracks = new HashMap<>();
        Anim(float l, String lp) { length=l; loop=lp; }
    }

    private final Map<String, Anim> animations = new HashMap<>();
    private boolean loaded = false;

    public boolean isLoaded() { return loaded; }
    public float   getAnimationLength(String name) {
        Anim a = animations.get(name);
        return a != null ? a.length : 0.5f;
    }

    // =========================================================
    // Load
    // =========================================================

    public void load(ResourceLocation geoLoc, ResourceLocation animLoc) {
        if (loaded) return;
        try {
            String geo = readRes(geoLoc);
            if (geo != null) parseGeo(geo);
            if (animLoc != null) {
                String anim = readRes(animLoc);
                if (anim != null) parseAnim(anim);
            }
            loaded = true;
        } catch (Throwable t) {
            System.err.println("[BedrockDollModel] load error: " + t.getMessage());
            t.printStackTrace();
        }
    }

    private String readRes(ResourceLocation loc) {
        try {
            IResource r = Minecraft.getMinecraft().getResourceManager().getResource(loc);
            String s = IOUtils.toString(r.getInputStream(), StandardCharsets.UTF_8);
            return s.startsWith("\uFEFF") ? s.substring(1) : s;
        } catch (Throwable t) {
            return null;
        }
    }

    // =========================================================
    // Geo parsing — копирует логику GeoArmorModel
    // =========================================================

    private void parseGeo(String json) {
        JsonObject root  = new JsonParser().parse(json).getAsJsonObject();
        JsonArray  geoms = root.getAsJsonArray("minecraft:geometry");
        if (geoms == null || geoms.size() == 0) return;

        JsonObject geom = geoms.get(0).getAsJsonObject();
        if (geom.has("description")) {
            JsonObject d = geom.getAsJsonObject("description");
            if (d.has("texture_width"))  texW = d.get("texture_width").getAsInt();
            if (d.has("texture_height")) texH = d.get("texture_height").getAsInt();
        }

        this.textureWidth  = texW;
        this.textureHeight = texH;

        JsonArray bonesArr = geom.getAsJsonArray("bones");
        if (bonesArr == null) return;

        // Первый проход: создаём ModelRenderer для каждого бона
        for (JsonElement be : bonesArr) {
            JsonObject bObj  = be.getAsJsonObject();
            String name      = bObj.get("name").getAsString();
            String parent    = bObj.has("parent") ? bObj.get("parent").getAsString() : null;

            float[] piv = vec3(bObj, "pivot", 0, 0, 0);
            float px = piv[0], py = piv[1], pz = piv[2];

            float bRx = 0, bRy = 0, bRz = 0;
            if (bObj.has("rotation")) {
                JsonArray r = bObj.getAsJsonArray("rotation");
                // GeoArmorModel хранит в радианах, Y и Z меняют знак относительно Bedrock
                bRx = (float) Math.toRadians(r.get(0).getAsFloat());
                bRy = (float) Math.toRadians(r.get(1).getAsFloat());
                bRz = (float) Math.toRadians(r.get(2).getAsFloat());
            }

            ModelRenderer mr = new ModelRenderer(this);
            mr.setTextureSize(texW, texH);

            // Rotation-point (rotationPoint) — позиция бона в пространстве РОДИТЕЛЯ.
            // GeoArmorModel использует: если родитель известен:
            //   relX = pivot[0] - parentPivot[0]
            //   relY = parentPivot[1] - pivot[1]   (Y инвертирован)
            //   relZ = pivot[2] - parentPivot[2]
            // Для корневых: rotationPoint = (px, 24-py, pz)  (24 — высота Bedrock персонажа)
            // Мы установим RotationPoint позже во втором проходе.

            // Базовые углы из geo
            mr.rotateAngleX = bRx;
            mr.rotateAngleY = bRy;
            mr.rotateAngleZ = bRz;

            // Кубы
            if (bObj.has("cubes")) {
                for (JsonElement ce : bObj.getAsJsonArray("cubes")) {
                    JsonObject cObj = ce.getAsJsonObject();

                    float[] orig = arr3(cObj.getAsJsonArray("origin"));
                    float[] sz   = arr3(cObj.getAsJsonArray("size"));
                    float ox = orig[0], oy = orig[1], oz = orig[2];
                    float sx = sz[0],  sy = sz[1],   sxi = sz[2];

                    float inflate = cObj.has("inflate") ? cObj.get("inflate").getAsFloat() : 0f;
                    boolean mirror = cObj.has("mirror") && cObj.get("mirror").getAsBoolean();

                    // UV
                    int u = 0, v = 0;
                    Map<String, float[]> faceUVs = null;
                    if (cObj.has("uv")) {
                        JsonElement uvE = cObj.get("uv");
                        if (uvE.isJsonArray()) {
                            JsonArray uva = uvE.getAsJsonArray();
                            u = uva.get(0).getAsInt();
                            v = uva.get(1).getAsInt();
                        } else if (uvE.isJsonObject()) {
                            faceUVs = new HashMap<>();
                            for (Map.Entry<String, JsonElement> fe : uvE.getAsJsonObject().entrySet()) {
                                if (fe.getValue().isJsonObject()) {
                                    JsonObject fo = fe.getValue().getAsJsonObject();
                                    if (fo.has("uv") && fo.has("uv_size")) {
                                        JsonArray ua = fo.getAsJsonArray("uv");
                                        JsonArray us = fo.getAsJsonArray("uv_size");
                                        // Always read uv_rotation from individual face objects
                                        float rot = fo.has("uv_rotation") ? fo.get("uv_rotation").getAsFloat() : 0f;
                                        faceUVs.put(fe.getKey(), new float[]{
                                            ua.get(0).getAsFloat(), ua.get(1).getAsFloat(),
                                            us.get(0).getAsFloat(), us.get(1).getAsFloat(),
                                            rot
                                        });
                                    }
                                }
                            }
                        }
                    }

                    boolean hasCubeRot = cObj.has("rotation") || cObj.has("pivot");

                    if (hasCubeRot) {
                        // Куб с собственной ротацией → дочерний ModelRenderer
                        float[] cp = cObj.has("pivot") ? arr3(cObj.getAsJsonArray("pivot")) : new float[]{ox, oy, oz};

                        // Позиция pivot куба относительно пивота бона (Y инвертирован)
                        float relX = cp[0] - px;
                        float relY = py - cp[1];
                        float relZ = cp[2] - pz;

                        ModelRenderer sub = new ModelRenderer(this, u, v);
                        sub.setTextureSize(texW, texH);
                        sub.mirror = mirror;
                        sub.setRotationPoint(relX, relY, relZ);

                        if (cObj.has("rotation")) {
                            JsonArray cr = cObj.getAsJsonArray("rotation");
                            sub.rotateAngleX = (float) Math.toRadians(cr.get(0).getAsFloat());
                            sub.rotateAngleY = (float) Math.toRadians(cr.get(1).getAsFloat());
                            sub.rotateAngleZ = (float) Math.toRadians(cr.get(2).getAsFloat());
                        }

                        float bx = ox - cp[0];
                        float by = cp[1] - oy - sy;
                        float bz = oz - cp[2];

                        if (faceUVs != null) {
                            sub.cubeList.add(new com.voltyx.mwccf.geo.FloatModelBox(sub, faceUVs, bx, by, bz, sx, sy, sxi, inflate, mirror));
                        } else {
                            sub.cubeList.add(new com.voltyx.mwccf.geo.FloatModelBox(sub, u, v, bx, by, bz, sx, sy, sxi, inflate, mirror));
                        }
                        mr.addChild(sub);
                    } else {
                        // Обычный куб — позиция относительно пивота бона
                        float bx = ox - px;
                        float by = py - oy - sy;   // Y инвертирован
                        float bz = oz - pz;

                        mr.setTextureOffset(u, v);
                        if (faceUVs != null) {
                            mr.cubeList.add(new com.voltyx.mwccf.geo.FloatModelBox(mr, faceUVs, bx, by, bz, sx, sy, sxi, inflate, mirror));
                        } else {
                            mr.cubeList.add(new com.voltyx.mwccf.geo.FloatModelBox(mr, u, v, bx, by, bz, sx, sy, sxi, inflate, mirror));
                        }
                    }
                }
            }

            bones.put(name, new BoneEntry(mr, px, py, pz, bRx, bRy, bRz, parent));
        }

        // Второй проход: расставляем RotationPoint и строим иерархию
        for (Map.Entry<String, BoneEntry> e : bones.entrySet()) {
            BoneEntry bone = e.getValue();
            if (bone.parentName != null && bones.containsKey(bone.parentName)) {
                BoneEntry par = bones.get(bone.parentName);
                float relX = bone.pivX - par.pivX;
                float relY = par.pivY  - bone.pivY; // Y инвертирован
                float relZ = bone.pivZ - par.pivZ;
                bone.renderer.setRotationPoint(relX, relY, relZ);
                par.renderer.addChild(bone.renderer);
            } else {
                // Корневой: относительно центра игрока (24 = высота в Bedrock)
                bone.renderer.setRotationPoint(bone.pivX, 24f - bone.pivY, bone.pivZ);
                rootBones.add(bone.renderer);
            }
        }
    }

    // =========================================================
    // Animation parsing
    // =========================================================

    private void parseAnim(String json) {
        JsonObject root = new JsonParser().parse(json).getAsJsonObject();
        if (!root.has("animations")) return;
        for (Map.Entry<String, JsonElement> ae : root.getAsJsonObject("animations").entrySet()) {
            JsonObject aObj = ae.getValue().getAsJsonObject();
            float  len  = aObj.has("animation_length") ? aObj.get("animation_length").getAsFloat() : 1f;
            String loop = "hold_on_last_frame";
            if (aObj.has("loop")) loop = aObj.get("loop").getAsString();

            Anim anim = new Anim(len, loop);
            if (aObj.has("bones")) {
                for (Map.Entry<String, JsonElement> be : aObj.getAsJsonObject("bones").entrySet()) {
                    BoneTrack track = new BoneTrack();
                    JsonObject bd = be.getValue().getAsJsonObject();
                    if (bd.has("rotation")) parseKFs(bd.get("rotation"), track.rot);
                    if (bd.has("position")) parseKFs(bd.get("position"), track.pos);
                    anim.tracks.put(be.getKey(), track);
                }
            }
            animations.put(ae.getKey(), anim);
        }
    }

    private void parseKFs(JsonElement elem, List<KF> out) {
        if (elem.isJsonArray()) {
            JsonArray a = elem.getAsJsonArray();
            out.add(new KF(0, a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat()));
        } else if (elem.isJsonObject()) {
            JsonObject obj = elem.getAsJsonObject();
            if (obj.has("vector")) {
                JsonArray a = obj.getAsJsonArray("vector");
                out.add(new KF(0, a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat()));
            } else {
                for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                    try {
                        float t = Float.parseFloat(e.getKey());
                        JsonElement val = e.getValue();
                        JsonArray a = null;
                        if (val.isJsonArray()) a = val.getAsJsonArray();
                        else if (val.isJsonObject()) {
                            JsonObject vo = val.getAsJsonObject();
                            if (vo.has("vector")) a = vo.getAsJsonArray("vector");
                            else if (vo.has("post")) a = vo.getAsJsonArray("post");
                        }
                        if (a != null) out.add(new KF(t, a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat()));
                    } catch (Throwable ignored) {}
                }
            }
        }
        out.sort(Comparator.comparingDouble(k -> k.time));
    }

    // =========================================================
    // Apply animation
    // =========================================================

    public void applyAnimation(String animName, float time) {
        // Сбрасываем к базовым
        for (BoneEntry e : bones.values()) {
            e.renderer.rotateAngleX = e.baseRotX;
            e.renderer.rotateAngleY = e.baseRotY;
            e.renderer.rotateAngleZ = e.baseRotZ;
        }

        Anim anim = animations.get(animName);
        if (anim == null) return;

        float[] tmp = new float[3];
        for (Map.Entry<String, BoneTrack> e : anim.tracks.entrySet()) {
            BoneEntry bone = bones.get(e.getKey());
            if (bone == null) continue;
            BoneTrack tr = e.getValue();

            evalKFs(tr.rot, time, tmp);
            // В Bedrock аnim rotation — в градусах, конвертируем в радианы и добавляем к базе
            bone.renderer.rotateAngleX += (float) Math.toRadians(tmp[0]);
            bone.renderer.rotateAngleY += (float) Math.toRadians(tmp[1]);
            bone.renderer.rotateAngleZ += (float) Math.toRadians(tmp[2]);

            // position пока не применяем (смещение boneEntry.renderer.rotationPoint)
        }
    }

    public void applyAnimationBlended(String animA, float timeA, String animB, float timeB, float weightB) {
        // Сбрасываем к базовым
        for (BoneEntry e : bones.values()) {
            e.renderer.rotateAngleX = e.baseRotX;
            e.renderer.rotateAngleY = e.baseRotY;
            e.renderer.rotateAngleZ = e.baseRotZ;
        }

        Anim a = animations.get(animA);
        Anim b = animations.get(animB);
        if (a == null && b == null) return;
        if (a == null) { applyAnimation(animB, timeB); return; }
        if (b == null) { applyAnimation(animA, timeA); return; }

        float wB = Math.max(0f, Math.min(1f, weightB));
        float wA = 1f - wB;

        Set<String> allBones = new HashSet<>(a.tracks.keySet());
        allBones.addAll(b.tracks.keySet());

        float[] tmpA = new float[3];
        float[] tmpB = new float[3];

        for (String boneName : allBones) {
            BoneEntry bone = bones.get(boneName);
            if (bone == null) continue;

            BoneTrack trA = a.tracks.get(boneName);
            BoneTrack trB = b.tracks.get(boneName);

            if (trA != null) evalKFs(trA.rot, timeA, tmpA); else { tmpA[0]=tmpA[1]=tmpA[2]=0; }
            if (trB != null) evalKFs(trB.rot, timeB, tmpB); else { tmpB[0]=tmpB[1]=tmpB[2]=0; }

            float rx = tmpA[0] * wA + tmpB[0] * wB;
            float ry = tmpA[1] * wA + tmpB[1] * wB;
            float rz = tmpA[2] * wA + tmpB[2] * wB;

            bone.renderer.rotateAngleX += (float) Math.toRadians(rx);
            bone.renderer.rotateAngleY += (float) Math.toRadians(ry);
            bone.renderer.rotateAngleZ += (float) Math.toRadians(rz);
        }
    }

    public void addBoneRotation(String boneName, float rotXRad, float rotYRad, float rotZRad) {
        BoneEntry b = bones.get(boneName);
        if (b != null) {
            b.renderer.rotateAngleX += rotXRad;
            b.renderer.rotateAngleY += rotYRad;
            b.renderer.rotateAngleZ += rotZRad;
        }
    }

    private void evalKFs(List<KF> kfs, float time, float[] out) {
        out[0] = out[1] = out[2] = 0;
        if (kfs.isEmpty()) return;
        if (kfs.size() == 1 || time <= kfs.get(0).time) {
            out[0]=kfs.get(0).x; out[1]=kfs.get(0).y; out[2]=kfs.get(0).z; return;
        }
        KF last = kfs.get(kfs.size()-1);
        if (time >= last.time) { out[0]=last.x; out[1]=last.y; out[2]=last.z; return; }
        for (int i = 0; i < kfs.size()-1; i++) {
            KF k0=kfs.get(i), k1=kfs.get(i+1);
            if (time >= k0.time && time <= k1.time) {
                float seg = k1.time - k0.time;
                float t   = (seg<=0) ? 0 : (time-k0.time)/seg;
                float f   = (1f - (float)Math.cos(t * Math.PI)) * 0.5f;
                out[0] = k0.x + (k1.x-k0.x)*f;
                out[1] = k0.y + (k1.y-k0.y)*f;
                out[2] = k0.z + (k1.z-k0.z)*f;
                return;
            }
        }
    }

    // =========================================================
    // Render
    // =========================================================

    public void render(float scale) {
        for (ModelRenderer r : rootBones) {
            r.render(scale);
        }
    }

    // =========================================================
    // Helpers
    // =========================================================

    private float[] vec3(JsonObject o, String key, float dx, float dy, float dz) {
        if (!o.has(key)) return new float[]{dx, dy, dz};
        return arr3(o.getAsJsonArray(key));
    }

    private float[] arr3(JsonArray a) {
        return new float[]{a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat()};
    }
}
