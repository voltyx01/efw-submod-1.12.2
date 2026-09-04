package com.voltyx.mwccf.render.flower;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BedrockFlowerRenderer {

    public static final ResourceLocation MODEL_GEO = new ResourceLocation("mwccf", "geo/flower.geo.json");
    public static final ResourceLocation MODEL_ANIM = new ResourceLocation("mwccf", "animations/flower.animation.json");
    public static final ResourceLocation MODEL_TEX = new ResourceLocation("mwccf", "textures/entity/flower.png");
    public static final ResourceLocation PARTICLES_TEX = new ResourceLocation("mwccf", "textures/particles/particles.png");

    private float textureWidth = 32f;
    private float textureHeight = 32f;

    private final Map<String, Bone> bonesByName = new LinkedHashMap<>();
    private final List<Bone> rootBones = new ArrayList<>();
    private final Map<String, Animation> animations = new HashMap<>();

    private boolean loaded = false;
    private float animTime = 0f;
    private String currentAnimName = "flower_open";
    private boolean animPlaying = true;
    private float animSpeed = 1.0f;

    // === Pixel-level Dynamic Texture System (32x32 Minecraft texels) ===
    private int[] baseTexturePixels = null;
    private DynamicTexture dynamicTexture = null;
    private ResourceLocation dynamicTextureLocation = null;

    // === Petal procedural smolder wave system ===
    public static int debugHighlightPetal = 0; // 0..11 highlighted by default for instant visual clarity
    public static float waveFireWidth = 0.045f;  // thickness of orange burning wave
    public static float waveAshWidth = 0.16f;   // gradient of grey ash ahead
    public static float waveSpeed = 1.0f;       // speed of wave progress
    public static float waveAngle = 20.0f;      // wave angle in degrees relative to petal base (0 = straight, +deg = diagonal)
    public static boolean showTexelDistDebug = false; // colorize petal by distance (base=green, tip=red)

    private final List<Petal> petals = new ArrayList<>();
    private final List<FlowerParticle> particles = new ArrayList<>();
    private final Random rand = new Random();
    private float glowTime = 0f;

    // Mapping: which petal was ignited by which skill (0..12)
    private final int[] skillToPetal = new int[13];
    private boolean petalsInitialized = false;

    private void initPetals() {
        if (!petals.isEmpty()) return;
        Arrays.fill(skillToPetal, -1);

        // --- 6 BIG PETALS (tepalsbig) ---
        // Each petal references the exact bones that belong to it:
        // base (groupX) -> mid (groupY) -> mid2 (groupZ) -> tip (boneW)
        petals.add(new Petal(0, "tepal_big_0", true, 0f, 92f, 200f, "group27", "group28", "group29", "bone2"));
        petals.add(new Petal(1, "tepal_big_1", true, 60f, 135f, 210f, "group30", "group31", "group32", "bone"));
        petals.add(new Petal(2, "tepal_big_2", true, 120f, 140f, 255f, "group33", "group34", "group35", "bone6"));
        petals.add(new Petal(3, "tepal_big_3", true, -60f, 92f, 280f, "group36", "group37", "group38", "bone3"));
        petals.add(new Petal(4, "tepal_big_4", true, -120f, 45f, 255f, "group39", "group40", "group41", "bone4"));
        petals.add(new Petal(5, "tepal_big_5", true, 180f, 50f, 210f, "group42", "group43", "group44", "bone5"));

        // --- 6 SMALL PETALS (tepalssmall) ---
        petals.add(new Petal(6, "tepal_small_0", false, 90f, 115f, 205f, "groupx", "group19"));
        petals.add(new Petal(7, "tepal_small_1", false, 150f, 145f, 235f, "groupx2", "group20"));
        petals.add(new Petal(8, "tepal_small_2", false, 210f, 120f, 275f, "groupx3", "group21"));
        petals.add(new Petal(9, "tepal_small_3", false, 270f, 65f, 275f, "groupx4", "group22"));
        petals.add(new Petal(10, "tepal_small_4", false, 330f, 40f, 235f, "groupx5", "group23"));
        petals.add(new Petal(11, "tepal_small_5", false, 30f, 70f, 205f, "groupx6", "group24"));
    }

    /**
     * Automatically extracts texels for each petal directly from the parsed model geometry cubes.
     * This guarantees 100% exact match with the 3D model vertices, eliminating all UV offset bugs.
     */
    private void buildPetalTexelsFromModel() {
        for (Petal petal : petals) {
            petal.texels.clear();
            int totalBones = petal.boneNames.size();

            for (int bIdx = 0; bIdx < totalBones; bIdx++) {
                String bName = petal.boneNames.get(bIdx);
                Bone bone = bonesByName.get(bName);
                if (bone == null) continue;

                // Normalized segment position along the petal length (0.0 = base bone, 1.0 = tip bone)
                float segStart = (float) bIdx / totalBones;
                float segEnd = (float) (bIdx + 1) / totalBones;

                for (Cube cube : bone.cubes) {
                    float w = cube.size[0];
                    float h = cube.size[1];
                    float d = cube.size[2];
                    int baseU = (int) cube.uv[0];
                    int baseV = (int) cube.uv[1];

                    if (h <= 0.001f) {
                        // Top quad in renderCube uses:
                        //   u: (baseU + d) -> (baseU + d + w) [length along petal X]
                        //   v: baseV -> (baseV + d) [width across petal Z]
                        int uMin = (int) (baseU + d);
                        int uMax = (int) (baseU + d + w);
                        int vMin = baseV;
                        int vMax = (int) (baseV + d);

                        for (int u = uMin; u < uMax; u++) {
                            float uFraction = (uMax - uMin <= 1) ? 0.5f : (float) (u - uMin) / (uMax - uMin - 1);
                            float dist = segStart + (segEnd - segStart) * uFraction;

                            for (int v = vMin; v < vMax; v++) {
                                petal.addTexel(u, v, dist);
                            }
                        }
                    } else if (d <= 0.001f) {
                        // Front quad in renderCube uses:
                        //   u: baseU -> (baseU + w) [width across petal X]
                        //   v: baseV -> (baseV + h) [length along petal Y]
                        int uMin = baseU;
                        int uMax = (int) (baseU + w);
                        int vMin = baseV;
                        int vMax = (int) (baseV + h);

                        for (int v = vMin; v < vMax; v++) {
                            float vFraction = (vMax - vMin <= 1) ? 0.5f : (float) (v - vMin) / (vMax - vMin - 1);
                            float dist = segStart + (segEnd - segStart) * vFraction;

                            for (int u = uMin; u < uMax; u++) {
                                petal.addTexel(u, v, dist);
                            }
                        }
                    } else {
                        // 3D cube (stem/center/thick bones)
                        int uMin = baseU;
                        int vMin = baseV;
                        int uMax = baseU + (int) (w + d * 2);
                        int vMax = baseV + (int) (h + d);
                        for (int u = uMin; u < uMax; u++) {
                            for (int v = vMin; v < vMax; v++) {
                                petal.addTexel(u, v, (segStart + segEnd) * 0.5f);
                            }
                        }
                    }
                }
            }
        }
    }

    // Deterministic hash -> [0,1), used to give each texel its own fixed "personality"
    // (color jitter, breathing phase/speed, flare timing) without needing a Random per pixel.
    private static float hash01(int a, int b, int c, int salt) {
        int h = a * 374761393 + b * 668265263 + c * 2147483647 + salt * 0x85ebca6b;
        h = (h ^ (h >>> 13)) * 1274126177;
        h ^= (h >>> 16);
        return (h & 0x7fffffff) / (float) 0x7fffffff;
    }

    private static int clamp255(int v) {
        return v < 0 ? 0 : (v > 255 ? 255 : v);
    }

    public static class TexelSpot {
        public final int u;
        public final int v;

        // Deterministic per-texel organic variance derived from a hash of (u, v, spotSeed).
        // This is what breaks the "one flat grey color" look: every texel gets its own
        // charcoal tint, its own breathing rhythm, and its own flare schedule.
        public final float charJitterR, charJitterG, charJitterB; // static charcoal color variance
        public final float emberHueShift;   // -1..1, shifts a flare warmer/cooler for variety
        public final float ambientPhase;    // 0..2PI, breathing phase offset
        public final float ambientFreq;     // breathing speed multiplier
        public final float ambientAmp;      // breathing amplitude (baseline glow under flares)

        public float flareTimer;            // countdown to this texel's next flare
        public float flareProgress = -1f;   // -1 = not flaring right now, else 0..1 while flaring
        public float flareDuration = 1f;
        public float flareIntensity = 1f;
        public boolean flareParticleSpawned = false;

        public TexelSpot(int u, int v, int spotSeed) {
            this.u = u;
            this.v = v;

            this.charJitterR = hash01(spotSeed, u, v, 11) - 0.5f;
            this.charJitterG = hash01(spotSeed, u, v, 23) - 0.5f;
            this.charJitterB = hash01(spotSeed, u, v, 37) - 0.5f;
            this.emberHueShift = hash01(spotSeed, u, v, 51) * 2f - 1f;
            this.ambientPhase = hash01(spotSeed, u, v, 67) * (float) (Math.PI * 2.0);
            this.ambientFreq = 0.6f + hash01(spotSeed, u, v, 79) * 0.9f;
            this.ambientAmp = 0.12f + hash01(spotSeed, u, v, 89) * 0.28f;

            // Stagger everyone's first flare so the patch doesn't pulse in lockstep.
            this.flareTimer = 0.15f + hash01(spotSeed, u, v, 97) * 1.6f;
        }
    }

    public static class PetalTexel {
        public final int u, v;
        public final float distance; // 0.0 = base/center, 1.0 = petal tip
        public final float charJitterR, charJitterG, charJitterB;
        public final float emberHueShift;
        public final float ambientPhase;
        public final float ambientFreq;

        public PetalTexel(int u, int v, float distance, int seed) {
            this.u = u;
            this.v = v;
            this.distance = distance;
            this.charJitterR = hash01(seed, u, v, 11) - 0.5f;
            this.charJitterG = hash01(seed, u, v, 23) - 0.5f;
            this.charJitterB = hash01(seed, u, v, 37) - 0.5f;
            this.emberHueShift = hash01(seed, u, v, 51) * 2f - 1f;
            this.ambientPhase = hash01(seed, u, v, 67) * (float) (Math.PI * 2.0);
            this.ambientFreq = 0.8f + hash01(seed, u, v, 79) * 1.2f;
        }
    }

    public static class Petal {
        public final int id;
        public final String name;
        public final boolean isBig;
        public final List<PetalTexel> texels = new ArrayList<>();
        public final List<String> boneNames = new ArrayList<>();
        public final float screenX, screenY;
        public float centerAngleDeg = 0f; // Angular direction (in degrees, XZ plane) around the flower center

        public boolean burning = false;
        public float burnProgress = 0f; // 0.0 to 1.0
        public float burnSpeed = 0.55f; // wave completes in ~1.8 sec
        public boolean flipDirection = false; // if true, reverse wave direction (1.0 -> 0.0)
        public float burnSwayBlend = 0f; // 0.0 = calm sway, 1.0 = intense burning sway (smoothly interpolated)

        public Petal(int id, String name, boolean isBig, float angleDeg, float sx, float sy, String... bones) {
            this.id = id;
            this.name = name;
            this.isBig = isBig;
            this.centerAngleDeg = angleDeg;
            this.screenX = sx;
            this.screenY = sy;
            if (bones != null) {
                for (String b : bones) boneNames.add(b);
            }
        }

        public boolean containsBone(String bName) {
            return boneNames.contains(bName);
        }

        public int getBoneSegmentIndex(String bName) {
            return boneNames.indexOf(bName);
        }

        public void addTexel(int u, int v, float dist) {
            for (PetalTexel t : texels) {
                if (t.u == u && t.v == v) return;
            }
            texels.add(new PetalTexel(u, v, dist, id * 31 + 17));
        }

        public void addBox(int minU, int minV, int w, int h, float distStart, float distEnd) {
            addBox(minU, minV, w, h, distStart, distEnd, false);
        }

        public void addBox(int minU, int minV, int w, int h, float distStart, float distEnd, boolean reverseUV) {
            for (int y = minV; y < minV + h; y++) {
                float vNorm = (h <= 1) ? 0.5f : (float)(y - minV) / (h - 1);
                for (int x = minU; x < minU + w; x++) {
                    float uNorm = (w <= 1) ? 0.5f : (float)(x - minU) / (w - 1);
                    float factor = Math.max(uNorm, vNorm);
                    if (reverseUV) factor = 1.0f - factor;
                    float d = distStart + (distEnd - distStart) * factor;
                    addTexel(x, y, d);
                }
            }
        }
    }

    public Petal getPetalForBone(String boneName) {
        for (Petal p : petals) {
            if (p.containsBone(boneName)) return p;
        }
        return null;
    }

    public static class FlowerParticle {
        public float x, y;
        public float vx, vy;
        public float size;
        public float age, maxAge;
        public float rot, rotSpeed;
        public float phase, phaseSpeed;
        public boolean isSpark; // true = tiny bright fire spark, false = regular ash particle

        /** Regular ash/ember particle spawned from petal screen location */
        public FlowerParticle(float x, float y, Random rand) {
            this.x = x + (rand.nextFloat() - 0.5f) * 6.0f;
            this.y = y + (rand.nextFloat() - 0.5f) * 6.0f;
            this.vx = 2.0f + rand.nextFloat() * 6.0f;       // drift right towards dossier
            this.vy = -(16.0f + rand.nextFloat() * 22.0f);   // float UP
            this.size = 2.5f + rand.nextFloat() * 2.5f;
            this.age = 0f;
            this.maxAge = 1.5f + rand.nextFloat() * 1.2f;    // 1.5s .. 2.7s lifespan
            this.rot = rand.nextFloat() * 360f;
            this.rotSpeed = (rand.nextFloat() - 0.5f) * 120f;
            this.phase = rand.nextFloat() * 6.28f;
            this.phaseSpeed = 1.8f + rand.nextFloat() * 2.2f;
            this.isSpark = false;
        }

        /**
         * Tiny bright fire spark: shoots directly from the burning wave front.
         * Fast, small, sharply orange, short-lived — creates a lively sparking effect.
         */
        public FlowerParticle(float x, float y, Random rand, boolean spark) {
            this.isSpark = spark;
            this.x = x + (rand.nextFloat() - 0.5f) * 3.0f;
            this.y = y + (rand.nextFloat() - 0.5f) * 3.0f;
            // Sparks shoot mostly upward with slight horizontal spread
            float angle = (float) (Math.PI * 1.5f + (rand.nextFloat() - 0.5f) * Math.PI * 0.7f);
            float speed = 18.0f + rand.nextFloat() * 28.0f;
            this.vx = (float) Math.cos(angle) * speed * 0.5f;
            this.vy = (float) Math.sin(angle) * speed;
            this.size = 0.9f + rand.nextFloat() * 1.4f;     // tiny sparks
            this.age = 0f;
            this.maxAge = 0.25f + rand.nextFloat() * 0.35f; // very short: 0.25s .. 0.60s
            this.rot = rand.nextFloat() * 360f;
            this.rotSpeed = (rand.nextFloat() - 0.5f) * 360f;
            this.phase = rand.nextFloat() * 6.28f;
            this.phaseSpeed = 4.0f + rand.nextFloat() * 4.0f;
        }

        public boolean update(float deltaSec) {
            age += deltaSec;
            if (age >= maxAge) return false;

            phase += phaseSpeed * deltaSec;
            float sway = (float) Math.sin(phase) * (isSpark ? 2.0f : 5.0f);

            x += (vx + sway) * deltaSec;
            y += vy * deltaSec;
            rot += rotSpeed * deltaSec;

            // Gravity: sparks decelerate fast, ash drifts gently
            vy += (isSpark ? 28.0f : 2.0f) * deltaSec;
            if (!isSpark) vx *= (1.0f - 0.4f * deltaSec); // ash drag
            return true;
        }

        public int[] getRGB(float t) {
            int r, g, b;
            if (isSpark) {
                // Sparks: stay bright orange/yellow-white the entire life, fade out sharply
                float hotness = 1.0f - t * 0.7f; // still hot at end
                r = 255;
                g = (int) Math.min(255, 180 * hotness + 60);
                b = (int) Math.min(255, 30 * hotness);
            } else if (t < 0.35f) {
                // Bright fiery glowing orange/gold spark
                float p = t / 0.35f;
                r = 255;
                g = (int) (175 - 75 * p); // 175 -> 100
                b = (int) (35 - 20 * p);  // 35 -> 15
            } else if (t < 0.70f) {
                // Cooling from glowing red to warm grey
                float p = (t - 0.35f) / 0.35f;
                r = (int) (255 - 135 * p); // 255 -> 120
                g = (int) (100 + 15 * p);  // 100 -> 115
                b = (int) (15 + 95 * p);   // 15 -> 110
            } else {
                // Cold ash grey
                r = 115;
                g = 115;
                b = 115;
            }
            return new int[]{r, g, b};
        }

        public float getAlpha(float t) {
            if (isSpark) {
                // Flash in quickly, hold, then vanish
                if (t < 0.08f) return t / 0.08f;
                return Math.max(0f, 1.0f - (t - 0.08f) / 0.92f);
            }
            if (t < 0.12f) {
                return (t / 0.12f) * 0.95f;
            } else if (t < 0.60f) {
                return 0.95f - (t - 0.12f) / 0.48f * 0.25f; // 0.95 -> 0.70
            } else {
                float p = (t - 0.60f) / 0.40f;
                return Math.max(0f, 0.70f * (1.0f - p)); // 0.70 -> 0.0
            }
        }
    }

    public void load() {
        if (loaded) return;
        initPetals();
        loadBaseTexture();

        try {
            // 1. Load Geometry
            InputStream geoStream = null;
            try {
                IResource resGeo = Minecraft.getMinecraft().getResourceManager().getResource(MODEL_GEO);
                geoStream = resGeo.getInputStream();
            } catch (Throwable ignored) {
                geoStream = BedrockFlowerRenderer.class.getResourceAsStream("/assets/mwccf/geo/flower.geo.json");
            }

            if (geoStream != null) {
                try (InputStream s = geoStream) {
                    String json = IOUtils.toString(s, StandardCharsets.UTF_8);
                    if (json.startsWith("\uFEFF")) json = json.substring(1);
                    parseGeometry(json);
                    buildPetalTexelsFromModel();
                }
            }

            // 2. Load Animations
            InputStream animStream = null;
            try {
                IResource resAnim = Minecraft.getMinecraft().getResourceManager().getResource(MODEL_ANIM);
                animStream = resAnim.getInputStream();
            } catch (Throwable ignored) {
                animStream = BedrockFlowerRenderer.class.getResourceAsStream("/assets/mwccf/animations/flower.animation.json");
            }

            if (animStream != null) {
                try (InputStream s = animStream) {
                    String json = IOUtils.toString(s, StandardCharsets.UTF_8);
                    if (json.startsWith("\uFEFF")) json = json.substring(1);
                    parseAnimation(json);
                }
            }

            loaded = true;
        } catch (Throwable t) {
            System.err.println("[BedrockFlowerRenderer] Failed to load flower model: " + t.getMessage());
            t.printStackTrace();
        }
    }

    private void loadBaseTexture() {
        try {
            InputStream texStream = null;
            try {
                IResource resTex = Minecraft.getMinecraft().getResourceManager().getResource(MODEL_TEX);
                texStream = resTex.getInputStream();
            } catch (Throwable ignored) {
                texStream = BedrockFlowerRenderer.class.getResourceAsStream("/assets/mwccf/textures/entity/flower.png");
            }

            if (texStream != null) {
                BufferedImage img = ImageIO.read(texStream);
                int w = img.getWidth();
                int h = img.getHeight();
                baseTexturePixels = new int[w * h];
                img.getRGB(0, 0, w, h, baseTexturePixels, 0, w);

                dynamicTexture = new DynamicTexture(w, h);
                dynamicTextureLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("flower_burn_tex", dynamicTexture);
            }
        } catch (Throwable t) {
            System.err.println("[BedrockFlowerRenderer] Failed to load base texture: " + t.getMessage());
        }
    }

    public void triggerPetalBurn(int petalId) {
        initPetals();
        if (petalId >= 0 && petalId < petals.size()) {
            Petal p = petals.get(petalId);
            p.burning = true;
            p.burnProgress = 0.0f;
        } else if (petalId == -1) { // ignite random
            List<Petal> avail = new ArrayList<>();
            for (Petal p : petals) {
                if (!p.burning && p.burnProgress <= 0f) avail.add(p);
            }
            if (!avail.isEmpty()) {
                Petal p = avail.get(rand.nextInt(avail.size()));
                p.burning = true;
                p.burnProgress = 0.0f;
            } else {
                Petal p = petals.get(rand.nextInt(petals.size()));
                p.burning = true;
                p.burnProgress = 0.0f;
            }
        }
    }

    public void resetPetalBurns() {
        initPetals();
        Arrays.fill(skillToPetal, -1);
        for (Petal p : petals) {
            p.burning = false;
            p.burnProgress = 0.0f;
        }
    }

    public Petal getPetal(int id) {
        initPetals();
        if (id >= 0 && id < petals.size()) return petals.get(id);
        return null;
    }

    public int getPetalCount() {
        initPetals();
        return petals.size();
    }

    private void updateTexturePixels() {
        if (dynamicTexture == null || baseTexturePixels == null) return;

        int[] current = dynamicTexture.getTextureData();
        System.arraycopy(baseTexturePixels, 0, current, 0, baseTexturePixels.length);

        // Render all burning / burnt petals or debug views
        for (Petal petal : petals) {
            boolean isHighlighted = (debugHighlightPetal == petal.id);
            if (!petal.burning && petal.burnProgress <= 0.001f && !isHighlighted && !showTexelDistDebug) continue;

            float prog = petal.burnProgress;
            float waveCenter = prog;
            float waveHalfWidth = waveFireWidth;
            float ashAhead = waveAshWidth;

            for (PetalTexel texel : petal.texels) {
                int u = texel.u;
                int v = texel.v;
                if (u < 0 || u >= 32 || v < 0 || v >= 32) continue;

                int idx = v * 32 + u;
                int baseCol = baseTexturePixels[idx];
                int baseA = (baseCol >> 24) & 0xFF;
                if (baseA == 0) continue;

                float d = texel.distance;

                // --- DEBUG: Distance Visualizer (Green=Base 0.0 -> Red=Tip 1.0) ---
                if (showTexelDistDebug) {
                    int dr = clamp255((int) (d * 255));
                    int dg = clamp255((int) ((1.0f - d) * 255));
                    int db = 40;
                    current[idx] = (baseA << 24) | (dr << 16) | (dg << 8) | db;
                    continue;
                }

                // --- DEBUG: Highlight Selected Petal in Cyan/Gold Pulse ---
                if (isHighlighted && !petal.burning && petal.burnProgress <= 0.001f) {
                    float pulse = 0.5f + 0.5f * (float) Math.sin(glowTime * 8.0f);
                    int hr = clamp255((int) (60 + pulse * 180));
                    int hg = clamp255((int) (220 + pulse * 35));
                    int hb = clamp255((int) (255 - pulse * 100));
                    current[idx] = (baseA << 24) | (hr << 16) | (hg << 8) | hb;
                    continue;
                }

                int origR = (baseCol >> 16) & 0xFF;
                int origG = (baseCol >> 8) & 0xFF;
                int origB = baseCol & 0xFF;

                // 1. Charcoal base for everything behind the fire wave
                int cr = clamp255((int) (22 + texel.charJitterR * 20));
                int cg = clamp255((int) (18 + texel.charJitterG * 16));
                int cb = clamp255((int) (15 + texel.charJitterB * 14));

                // Ambient smolder for the black charred area: subtle living ember breathing
                float breathe = 0.5f + 0.5f * (float) Math.sin(glowTime * texel.ambientFreq + texel.ambientPhase);
                if (breathe > 0.65f) {
                    float emberT = (breathe - 0.65f) / 0.35f;
                    cr = clamp255((int) (cr + 45 * emberT));
                    cg = clamp255((int) (cg + 15 * emberT));
                    cb = clamp255((int) (cb + 5 * emberT));
                }

                int finalR = origR;
                int finalG = origG;
                int finalB = origB;

                float diff = d - waveCenter;

                if (diff < -waveHalfWidth) {
                    // --- BEHIND THE WAVE: Completely charred & blackened ---
                    float coolDist = Math.min(1.0f, (-diff - waveHalfWidth) * 4.0f);
                    finalR = cr;
                    finalG = cg;
                    finalB = cb;

                    // Immediately behind the wave: glowing red hot coals fading into charcoal
                    if (coolDist < 0.95f) {
                        float redHeat = 1.0f - coolDist;
                        finalR = clamp255((int) (finalR + (210 - finalR) * redHeat * 0.85f));
                        finalG = clamp255((int) (finalG + (60 - finalG) * redHeat * 0.7f));
                        finalB = clamp255((int) (finalB + (15 - finalB) * redHeat * 0.4f));
                    }
                } else if (Math.abs(diff) <= waveHalfWidth) {
                    // --- THE BURNING FRONT: 1-pixel bright glowing orange fire line ---
                    float frontT = 1.0f - Math.abs(diff) / waveHalfWidth;
                    float flicker = 0.85f + 0.15f * (float) Math.sin(glowTime * 18.0f + texel.ambientPhase * 3.0f);

                    // Hot radiant orange core (#FF8C14 with gold highlight)
                    int fireR = 255;
                    int fireG = clamp255((int) ((130 + 35 * texel.emberHueShift) * flicker));
                    int fireB = clamp255((int) ((18 + 10 * frontT) * flicker));

                    finalR = fireR;
                    finalG = fireG;
                    finalB = fireB;
                } else if (diff <= ashAhead) {
                    // --- AHEAD OF THE WAVE: Light-grey gradient of ash creeping forward ---
                    float ashT = 1.0f - (diff - waveHalfWidth) / (ashAhead - waveHalfWidth);
                    int ashR = 195;
                    int ashG = 192;
                    int ashB = 188;

                    // Blend original texture towards pale grey ash gradient
                    float mix = ashT * 0.85f;
                    finalR = clamp255((int) (origR + (ashR - origR) * mix));
                    finalG = clamp255((int) (origG + (ashG - origG) * mix));
                    finalB = clamp255((int) (origB + (ashB - origB) * mix));

                    // Subtle warm edge right next to the fire line
                    if (ashT > 0.75f) {
                        float warm = (ashT - 0.75f) / 0.25f;
                        finalR = clamp255((int) (finalR + (255 - finalR) * warm * 0.6f));
                        finalG = clamp255((int) (finalG + (140 - finalG) * warm * 0.4f));
                    }
                } else {
                    // Untouched petal
                    finalR = origR;
                    finalG = origG;
                    finalB = origB;
                }

                current[idx] = (baseA << 24) | (finalR << 16) | (finalG << 8) | finalB;
            }
        }

        dynamicTexture.updateDynamicTexture();
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

                    float uvU = 0, uvV = 0;
                    if (cObj.has("uv")) {
                        JsonArray uvArr = cObj.getAsJsonArray("uv");
                        uvU = uvArr.get(0).getAsFloat();
                        uvV = uvArr.get(1).getAsFloat();
                    }

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

                    bone.cubes.add(new Cube(ox, oy, oz, sx, sy, sz, uvU, uvV, cubePivot, cubeRot));
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
                    } catch (Throwable ignored) {}
                }
            }
        } else if (elem.isJsonArray()) {
            JsonArray v = elem.getAsJsonArray();
            target.add(new Keyframe(0f, v.get(0).getAsFloat(), v.get(1).getAsFloat(), v.get(2).getAsFloat()));
        }
        target.sort(Comparator.comparingDouble(k -> k.time));
    }

    public enum AnimMode {
        CLOSED,
        OPENING,
        OPEN_IDLE,
        CLOSING
    }

    private AnimMode animMode = AnimMode.OPEN_IDLE;
    private float closeSpeedMultiplier = 1.8f;
    private float idleSwayTime = 0f;

    public void playOpen() {
        load();
        if (animMode == AnimMode.CLOSED || animTime <= 0f) {
            animTime = 0f;
        }
        animMode = AnimMode.OPENING;
        animPlaying = true;
        onFlowerScreenOpened();
    }

    /**
     * Called whenever the flower tab is opened:
     * Flower appears fresh/healthy, and all petals that are logically unlocked/burned
     * immediately start their burn wave animation from 0.0f.
     */
    public void onFlowerScreenOpened() {
        initPetals();
        for (Petal p : petals) {
            // If petal was burned or currently burning, restart burning wave from 0 so it animates each time screen opens
            if (p.burnProgress > 0.0f || p.burning) {
                p.burning = true;
                p.burnProgress = 0.0f;
            }
        }
    }

    public void playClose(float speedMultiplier) {
        load();
        closeSpeedMultiplier = Math.max(0.1f, speedMultiplier);
        animMode = AnimMode.CLOSING;
        animPlaying = true;
    }

    public AnimMode getAnimMode() {
        return animMode;
    }

    public boolean isFullyClosed() {
        return animMode == AnimMode.CLOSED || animTime <= 0.001f;
    }

    public void update(float deltaSec) {
        update(deltaSec, this.animSpeed, null);
    }

    public void update(float deltaSec, float speed) {
        update(deltaSec, speed, null);
    }

    public void update(float deltaSec, float speed, boolean[] skillUnlocked) {
        load();

        if (animPlaying) {
            Animation anim = animations.get(currentAnimName);
            if (anim == null && !animations.isEmpty()) {
                anim = animations.values().iterator().next();
            }
            if (anim != null) {
                float animLen = anim.length > 0f ? anim.length : 1.375f;
                float curSpeed = Math.max(0.01f, speed);

                if (animMode == AnimMode.OPENING) {
                    animTime += deltaSec * curSpeed;
                    if (animTime >= animLen) {
                        animTime = animLen;
                        animMode = AnimMode.OPEN_IDLE;
                    }
                } else if (animMode == AnimMode.CLOSING) {
                    animTime -= deltaSec * curSpeed * closeSpeedMultiplier;
                    if (animTime <= 0f) {
                        animTime = 0f;
                        animMode = AnimMode.CLOSED;
                    }
                } else if (animMode == AnimMode.OPEN_IDLE) {
                    animTime = animLen;
                } else if (animMode == AnimMode.CLOSED) {
                    animTime = 0f;
                }

                idleSwayTime += deltaSec;

                for (Bone bone : bonesByName.values()) {
                    bone.animRot[0] = 0;
                    bone.animRot[1] = 0;
                    bone.animRot[2] = 0;
                    bone.animPos[0] = 0;
                    bone.animPos[1] = 0;
                    bone.animPos[2] = 0;

                    BoneTrack track = anim.tracks.get(bone.name);
                    if (track != null) {
                        evaluateKeyframes(track.rotKeyframes, animTime, bone.animRot);
                        evaluateKeyframes(track.posKeyframes, animTime, bone.animPos);
                    }
                }

                float openWeight = animLen > 0f ? Math.max(0f, (animTime - animLen * 0.6f) / (animLen * 0.4f)) : 1.0f;
                if (openWeight > 0.001f) {
                    applyProceduralPetalSway(idleSwayTime, openWeight);
                }
            }
        }

        // === Update Burn Spots: continuous multi-ember smolder & particle emitters ===
        glowTime += deltaSec;

        initPetals();

        // Check for newly unlocked skills and assign them to burn random available petals
        if (skillUnlocked != null) {
            for (int sId = 1; sId < skillUnlocked.length && sId < 13; sId++) {
                if (skillUnlocked[sId]) {
                    if (skillToPetal[sId] == -1) {
                        // Find an unburned petal
                        List<Petal> available = new ArrayList<>();
                        for (Petal p : petals) {
                            if (!p.burning && p.burnProgress <= 0f) {
                                available.add(p);
                            }
                        }
                        if (!available.isEmpty()) {
                            Petal chosen = available.get(rand.nextInt(available.size()));
                            chosen.burning = true;
                            skillToPetal[sId] = chosen.id;
                        } else {
                            skillToPetal[sId] = rand.nextInt(petals.size());
                        }
                    }
                }
            }
        }

        // Advance burn wave on all burning petals
        for (Petal petal : petals) {
            if (petal.burning) {
                float prevProg = petal.burnProgress;
                petal.burnProgress += deltaSec * waveSpeed;

                // Spawn flying sparks and ash along the burning front
                if (petal.burnProgress < 1.18f) {
                    // Estimate screen position of the burn wave front
                    float waveX = petal.screenX + (rand.nextFloat() - 0.5f) * 12.0f;
                    float waveY = petal.screenY - (petal.burnProgress * 18.0f) + (rand.nextFloat() - 0.5f) * 6.0f;

                    // Ash particle: slow, large, drifts upward (low rate)
                    if (rand.nextFloat() < 0.35f && particles.size() < 80) {
                        particles.add(new FlowerParticle(waveX, waveY, rand));
                    }
                    // Sparks: tiny bright orange, shoot from the wave front (higher rate for lively fire)
                    if (rand.nextFloat() < 0.75f && particles.size() < 150) {
                        particles.add(new FlowerParticle(waveX, waveY, rand, true));
                    }
                } else {
                    petal.burning = false;
                    petal.burnProgress = 1.20f; // completely charred through and beyond the tip
                }
            }

            // Smoothly blend sway intensity into and out of burning state (no sudden snap)
            float targetBlend = petal.burning ? 1.0f : 0.0f;
            float blendSpeed = petal.burning ? 4.0f : 1.6f; // rapid build-up, gentle smooth fade-out
            petal.burnSwayBlend += (targetBlend - petal.burnSwayBlend) * Math.min(1.0f, deltaSec * blendSpeed);
        }

        // Update active ash/spark particles
        for (Iterator<FlowerParticle> it = particles.iterator(); it.hasNext(); ) {
            FlowerParticle p = it.next();
            if (!p.update(deltaSec)) {
                it.remove();
            }
        }

        // Update 32x32 texture pixels
        updateTexturePixels();
    }

    private void applyProceduralPetalSway(float time, float weight) {
        String[][] bigPetals = {
            {"group27", "group28", "group29"},
            {"group30", "group31", "group32"},
            {"group33", "group34", "group35"},
            {"group36", "group37", "group38"},
            {"group39", "group40", "group41"},
            {"group42", "group43", "group44"}
        };

        for (int p = 0; p < bigPetals.length; p++) {
            float phase = (float) (p * (Math.PI * 2.0 / 6.0));
            Petal petal = (p < petals.size()) ? petals.get(p) : null;
            float blend = (petal != null) ? petal.burnSwayBlend : 0.0f;

            // Smoothly blended sway: transitions gradually without jarring snap
            float swayScale = 1.0f + 1.2f * blend;
            float flutter = blend * ((float) Math.sin(time * 6.5f + phase * 2.0f) * 1.6f);

            Bone b0 = bonesByName.get(bigPetals[p][0]);
            if (b0 != null) {
                b0.animRot[0] += ((float) Math.sin(time * 1.8f + phase) * 1.8f * swayScale + flutter * 0.5f) * weight;
            }
            Bone b1 = bonesByName.get(bigPetals[p][1]);
            if (b1 != null) {
                b1.animRot[2] += ((float) Math.sin(time * 2.2f + phase + 0.6f) * 1.5f * swayScale + flutter * 0.8f) * weight;
            }
            Bone b2 = bonesByName.get(bigPetals[p][2]);
            if (b2 != null) {
                b2.animRot[2] += ((float) Math.sin(time * 2.6f + phase + 1.2f) * 2.2f * swayScale + flutter * 1.2f) * weight;
            }
        }

        String[][] smallPetals = {
            {"groupx", "group19"},
            {"groupx2", "group20"},
            {"groupx3", "group21"},
            {"groupx4", "group22"},
            {"groupx5", "group23"},
            {"groupx6", "group24"}
        };

        for (int p = 0; p < smallPetals.length; p++) {
            float phaseSmall = (float) (p * (Math.PI * 2.0 / 6.0) + 0.8f);
            int smallPetalId = 6 + p;
            Petal petal = (smallPetalId < petals.size()) ? petals.get(smallPetalId) : null;
            float blend = (petal != null) ? petal.burnSwayBlend : 0.0f;

            float swayScale = 1.0f + 1.2f * blend;
            float flutter = blend * ((float) Math.sin(time * 7.2f + phaseSmall * 2.0f) * 1.8f);

            Bone bx = bonesByName.get(smallPetals[p][0]);
            if (bx != null) {
                bx.animRot[0] += ((float) Math.sin(time * 2.1f + phaseSmall) * 2.0f * swayScale + flutter * 0.6f) * weight;
            }
            Bone bt = bonesByName.get(smallPetals[p][1]);
            if (bt != null) {
                bt.animRot[0] += ((float) Math.sin(time * 2.7f + phaseSmall + 0.8f) * 2.5f * swayScale + flutter * 1.2f) * weight;
            }
        }
    }

    public void setAnimationProgress(float progress) {
        load();
        Animation anim = animations.get(currentAnimName);
        if (anim == null && !animations.isEmpty()) {
            anim = animations.values().iterator().next();
        }
        if (anim == null) return;

        animTime = Math.max(0f, Math.min(1f, progress)) * anim.length;
        for (Bone bone : bonesByName.values()) {
            bone.animRot[0] = 0;
            bone.animRot[1] = 0;
            bone.animRot[2] = 0;
            bone.animPos[0] = 0;
            bone.animPos[1] = 0;
            bone.animPos[2] = 0;

            BoneTrack track = anim.tracks.get(bone.name);
            if (track != null) {
                evaluateKeyframes(track.rotKeyframes, animTime, bone.animRot);
                evaluateKeyframes(track.posKeyframes, animTime, bone.animPos);
            }
        }
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
                float f = (1.0f - (float) Math.cos(t * Math.PI)) * 0.5f;
                out[0] = k0.x + (k1.x - k0.x) * f;
                out[1] = k0.y + (k1.y - k0.y) * f;
                out[2] = k0.z + (k1.z - k0.z) * f;
                return;
            }
        }
    }

    public void render(float scale) {
        load();
        Minecraft.getMinecraft().getTextureManager().bindTexture(MODEL_TEX);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableCull();
        GlStateManager.disableLighting();

        for (Bone root : rootBones) {
            renderBone(root, null, scale);
        }

        GlStateManager.enableLighting();
        GlStateManager.enableCull();
    }

    private void renderBone(Bone bone, Bone parent, float scale) {
        GlStateManager.pushMatrix();

        float px = bone.pivot[0];
        float py = bone.pivot[1];
        float pz = bone.pivot[2];

        float ox = (parent == null) ? px : (px - parent.pivot[0]);
        float oy = (parent == null) ? py : (py - parent.pivot[1]);
        float oz = (parent == null) ? pz : (pz - parent.pivot[2]);

        GlStateManager.translate((ox + bone.animPos[0]) * scale,
                                 (oy + bone.animPos[1]) * scale,
                                 (oz + bone.animPos[2]) * scale);

        float rx = -(bone.rotation[0] + bone.animRot[0]);
        float ry = -(bone.rotation[1] + bone.animRot[1]);
        float rz = -(bone.rotation[2] + bone.animRot[2]);

        if (rz != 0f) GlStateManager.rotate(rz, 0, 0, 1);
        if (ry != 0f) GlStateManager.rotate(ry, 0, 1, 0);
        if (rx != 0f) GlStateManager.rotate(rx, 1, 0, 0);

        // Render cubes
        for (Cube cube : bone.cubes) {
            renderCube(cube, bone, scale);
        }

        // Render child bones
        for (Bone child : bone.children) {
            renderBone(child, bone, scale);
        }

        GlStateManager.popMatrix();
    }

    public void renderParticles(Minecraft mc, float fadeAlpha) {
        if (particles.isEmpty() || fadeAlpha <= 0.01f) return;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();

        mc.getTextureManager().bindTexture(PARTICLES_TEX);

        // Ash particle sub-texture 0 in 16x16 grid: u: 0..0.0625, v: 0..0.0625
        double u1 = 0.0;
        double v1 = 0.0;
        double u2 = 0.0625;
        double v2 = 0.0625;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // === PASS 1: Ash particles — normal alpha blending ===
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        for (FlowerParticle p : particles) {
            if (p.isSpark) continue; // sparks rendered in pass 2
            float progress = p.age / p.maxAge;
            float s = p.size;
            float curAlpha = p.getAlpha(progress) * fadeAlpha;
            if (curAlpha <= 0.01f) continue;

            int[] rgb = p.getRGB(progress);
            int a = (int) (curAlpha * 255.0f);

            float rad = (float) Math.toRadians(p.rot);
            float cos = (float) Math.cos(rad) * s;
            float sin = (float) Math.sin(rad) * s;

            float x0 = p.x - cos + sin;  float y0 = p.y - sin - cos;
            float x1 = p.x + cos + sin;  float y1 = p.y + sin - cos;
            float x2 = p.x + cos - sin;  float y2 = p.y + sin + cos;
            float x3 = p.x - cos - sin;  float y3 = p.y - sin + cos;

            buffer.pos(x0, y0, 0.0D).tex(u1, v2).color(rgb[0], rgb[1], rgb[2], a).endVertex();
            buffer.pos(x1, y1, 0.0D).tex(u2, v2).color(rgb[0], rgb[1], rgb[2], a).endVertex();
            buffer.pos(x2, y2, 0.0D).tex(u2, v1).color(rgb[0], rgb[1], rgb[2], a).endVertex();
            buffer.pos(x3, y3, 0.0D).tex(u1, v1).color(rgb[0], rgb[1], rgb[2], a).endVertex();
        }
        tessellator.draw();

        // === PASS 2: Fire sparks — additive blending for hot glowing look ===
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        for (FlowerParticle p : particles) {
            if (!p.isSpark) continue;
            float progress = p.age / p.maxAge;
            float s = p.size;
            float curAlpha = p.getAlpha(progress) * fadeAlpha;
            if (curAlpha <= 0.01f) continue;

            int[] rgb = p.getRGB(progress);
            int a = (int) (curAlpha * 255.0f);

            float rad = (float) Math.toRadians(p.rot);
            float cos = (float) Math.cos(rad) * s;
            float sin = (float) Math.sin(rad) * s;

            float x0 = p.x - cos + sin;  float y0 = p.y - sin - cos;
            float x1 = p.x + cos + sin;  float y1 = p.y + sin - cos;
            float x2 = p.x + cos - sin;  float y2 = p.y + sin + cos;
            float x3 = p.x - cos - sin;  float y3 = p.y - sin + cos;

            buffer.pos(x0, y0, 0.0D).tex(u1, v2).color(rgb[0], rgb[1], rgb[2], a).endVertex();
            buffer.pos(x1, y1, 0.0D).tex(u2, v2).color(rgb[0], rgb[1], rgb[2], a).endVertex();
            buffer.pos(x2, y2, 0.0D).tex(u2, v1).color(rgb[0], rgb[1], rgb[2], a).endVertex();
            buffer.pos(x3, y3, 0.0D).tex(u1, v1).color(rgb[0], rgb[1], rgb[2], a).endVertex();
        }
        tessellator.draw();

        GlStateManager.popAttrib();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
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

        // Origin relative to bone pivot
        float x0 = (cube.origin[0] - bone.pivot[0]) * scale;
        float y0 = (cube.origin[1] - bone.pivot[1]) * scale;
        float z0 = (cube.origin[2] - bone.pivot[2]) * scale;

        float x1 = x0 + cube.size[0] * scale;
        float y1 = y0 + cube.size[1] * scale;
        float z1 = z0 + cube.size[2] * scale;

        float w = cube.size[0];
        float h = cube.size[1];
        float d = cube.size[2];

        float u = cube.uv[0];
        float v = cube.uv[1];

        float tw = textureWidth;
        float th = textureHeight;

        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        if (h <= 0.001f) {
            float u0t = (u + d) / tw;
            float u1t = (u + d + w) / tw;
            float v0t = v / th;
            float v1t = (v + d) / th;

            buf.pos(x0, y0, z0).tex(u0t, v0t).normal(0, 1, 0).endVertex();
            buf.pos(x1, y0, z0).tex(u1t, v0t).normal(0, 1, 0).endVertex();
            buf.pos(x1, y0, z1).tex(u1t, v1t).normal(0, 1, 0).endVertex();
            buf.pos(x0, y0, z1).tex(u0t, v1t).normal(0, 1, 0).endVertex();

        } else if (d <= 0.001f) {
            float u0n = u / tw;
            float u1n = (u + w) / tw;
            float v0n = v / th;
            float v1n = (v + h) / th;

            buf.pos(x0, y1, z0).tex(u0n, v0n).normal(0, 0, -1).endVertex();
            buf.pos(x1, y1, z0).tex(u1n, v0n).normal(0, 0, -1).endVertex();
            buf.pos(x1, y0, z0).tex(u1n, v1n).normal(0, 0, -1).endVertex();
            buf.pos(x0, y0, z0).tex(u0n, v1n).normal(0, 0, -1).endVertex();
        } else {
            float uTop = u + d,          vTop = v;
            float uSide = u,             vSide = v + d;
            float uFront = u + d,        vFront = v + d;
            float uBack = u + d + w + d, vBack = v + d;

            quad(buf, x0, y1, z1, x1, y1, z1, x1, y1, z0, x0, y1, z0, uTop, vTop, uTop + w, vTop + d, tw, th, 0, 1, 0);
            quad(buf, x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1, uTop + w, vTop, uTop + w + w, vTop + d, tw, th, 0, -1, 0);
            quad(buf, x0, y1, z0, x1, y1, z0, x1, y0, z0, x0, y0, z0, uFront, vFront, uFront + w, vFront + h, tw, th, 0, 0, -1);
            quad(buf, x1, y1, z1, x0, y1, z0, x0, y0, z1, x1, y0, z1, uBack, vFront, uBack + w, vFront + h, tw, th, 0, 0, 1);
            quad(buf, x0, y1, z1, x0, y1, z0, x0, y0, z0, x0, y0, z1, uSide, vSide, uSide + d, vSide + h, tw, th, -1, 0, 0);
            quad(buf, x1, y1, z0, x1, y1, z1, x1, y0, z1, x1, y0, z0, uSide + d + w, vSide, uSide + d + w + d, vSide + h, tw, th, 1, 0, 0);
        }

        tessellator.draw();

        // === 3D PIXEL-GRID BURN OVERLAY (Subdivided into 1x1 voxel pixel quads) ===
        Petal petalForBone = getPetalForBone(bone.name);
        if (petalForBone != null && (petalForBone.burning || petalForBone.burnProgress > 0.001f)) {
            int segIdx = petalForBone.getBoneSegmentIndex(bone.name);
            int totalSegs = Math.max(1, petalForBone.boneNames.size());

            // Compute s0 and s1 proportionally to the actual physical length of each bone segment
            float totalPetalLen = 0f;
            float boneStartLen = 0f;
            float thisBoneLen = 0f;
            for (int bi = 0; bi < petalForBone.boneNames.size(); bi++) {
                Bone b = bonesByName.get(petalForBone.boneNames.get(bi));
                float blen = 1.0f;
                if (b != null && !b.cubes.isEmpty()) {
                    Cube c = b.cubes.get(0);
                    blen = (c.size[1] <= 0.001f) ? c.size[0] : c.size[1];
                }
                if (bi < segIdx) {
                    boneStartLen += blen;
                } else if (bi == segIdx) {
                    thisBoneLen = blen;
                }
                totalPetalLen += blen;
            }
            if (totalPetalLen <= 0.001f) totalPetalLen = 1.0f;
            if (thisBoneLen <= 0.001f) thisBoneLen = 1.0f;

            float s0 = boneStartLen / totalPetalLen;
            float s1 = (boneStartLen + thisBoneLen) / totalPetalLen;

            float prog = petalForBone.burnProgress;
            float waveHalf = waveFireWidth;
            float ashAhead = waveAshWidth;

            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(-1.5f, -1.5f);

            BufferBuilder obuf = tessellator.getBuffer();
            obuf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

            // 4 subdivisions per unit for ultra-fine micro-pixels matching exact 32x texel scale
            int stepsX = Math.max(1, Math.round(cube.size[0] * 4.0f));
            int stepsY = Math.max(1, Math.round(cube.size[1] * 4.0f));
            int stepsZ = Math.max(1, Math.round(cube.size[2] * 4.0f));

            float dx = (x1 - x0) / stepsX;
            float dy = (h <= 0.001f) ? 0f : (y1 - y0) / stepsY;
            float dz = (d <= 0.001f) ? 0f : (z1 - z0) / stepsZ;

            // Relative speed and fire thickness adjusted by bone scale
            float boneLen = (h <= 0.001f) ? cube.size[0] : cube.size[1];
            boolean isLargeCube = (boneLen >= 2.8f);
            float currentWaveHalf = isLargeCube ? (waveHalf * 1.55f) : (waveHalf * 0.85f);

            // Fire fading factor as wave finishes the petal (fades at the very end past tip)
            float fireFade = 1.0f;
            if (prog > 1.00f) {
                fireFade = Math.max(0.0f, (1.15f - prog) / 0.15f);
            }

            float angleRad = (float) Math.toRadians(waveAngle);
            float tanAngle = (float) Math.tan(angleRad);

            if (h <= 0.001f) {
                // Flat horizontal petal (big petals: length = X, width = Z)
                for (int ix = 0; ix < stepsX; ix++) {
                    float px0 = x0 + ix * dx;
                    float px1 = (ix == stepsX - 1) ? x1 : (px0 + dx);
                    float uFrac = (stepsX <= 1) ? 0.5f : (float) ix / (stepsX - 1);
                    float dBase = s0 + (s1 - s0) * uFrac;
                    if (petalForBone.flipDirection) dBase = 1.0f - dBase;

                    for (int iz = 0; iz < stepsZ; iz++) {
                        float pz0 = z0 + iz * dz;
                        float pz1 = (iz == stepsZ - 1) ? z1 : (pz0 + dz);

                        // Width fraction across petal: -0.5 to +0.5 relative to petal centerline
                        float wFrac = (stepsZ <= 1) ? 0.0f : ((float) iz / (stepsZ - 1) - 0.5f);
                        float dDist = dBase + wFrac * tanAngle * (s1 - s0);

                        // Exactly 1 pixel wide border on the sides (in 2 times narrower than before)
                        boolean isEdge = (iz == 0 || iz == stepsZ - 1);

                        // Procedural pixel personality & noise
                        int seed = (bone.name.hashCode() * 37 + ix * 53 + iz * 89) & 0x7FFFFFFF;
                        float noise = ((seed % 100) / 100.0f) - 0.5f;
                        float noise2 = (((seed / 100) % 100) / 100.0f) - 0.5f;

                        // Patchy edge: sections grouped along X (along petal length direction) = vertical stripes, matching small petals
                        // Larger section size (ix/8) = fewer but longer ash strips
                        int sectionSeed = (petalForBone.id * 101 + (ix / 8) * 31 + (iz == 0 ? 11 : 73)) & 0x7FFFFFFF;
                        boolean edgeSectionActive = ((sectionSeed % 100) < 55);

                        // Desynchronized ultra-slow breathing per petal & per side (frequency 0.42f)
                        float sidePhase = petalForBone.id * 1.35f + (iz == 0 ? 0.0f : 2.4f) + (iz / 5) * 0.4f;
                        float slowBreathe = 0.5f + 0.5f * (float) Math.sin(glowTime * 0.42f + sidePhase);

                        // Jagged noise for ash boundary towards center of cube
                        float ashBoundaryJitter = noise * 0.032f;
                        float diff = dDist - prog;
                        float alpha = 0.0f;
                        float r = 0.04f, g = 0.04f, b = 0.04f;

                        if (diff < -currentWaveHalf + ashBoundaryJitter) {
                            // --- BURNT SECTION: Very dark matte charcoal ash, NO center breathing ---
                            alpha = 0.96f;
                            float charR = 0.016f + noise * 0.008f;
                            float charG = 0.013f + noise * 0.008f;
                            float charB = 0.013f + noise2 * 0.008f;

                            // Patchy 1-pixel wide edge strip breathing subtle dark graphite
                            if (isEdge && edgeSectionActive) {
                                float greyAdd = 0.085f * slowBreathe + noise * 0.02f;
                                charR = Math.min(1.0f, charR + greyAdd);
                                charG = Math.min(1.0f, charG + greyAdd * 0.96f);
                                charB = Math.min(1.0f, charB + greyAdd * 0.94f);
                            }

                            // Broad noisy trailing fire plume (3 to 5 pixels long scattered embers tail)
                            float wakeDist = (-diff - currentWaveHalf);
                            float plumeLen = currentWaveHalf * 3.8f;
                            if (wakeDist > 0f && wakeDist < plumeLen && fireFade > 0.04f) {
                                float trailT = (1.0f - wakeDist / plumeLen);
                                // Scattered probabilistic trail with organic distribution
                                if (noise > -0.15f * trailT) {
                                    float sparkIntensity = (0.75f + noise * 0.45f) * trailT * fireFade;
                                    charR = Math.min(1.0f, charR + 0.96f * sparkIntensity);
                                    charG = Math.min(1.0f, charG + (0.42f + noise * 0.25f) * sparkIntensity);
                                    charB = Math.min(1.0f, charB + 0.04f * sparkIntensity);
                                }
                            }

                            r = charR; g = charG; b = charB;

                        } else if (Math.abs(diff) <= currentWaveHalf && fireFade > 0.01f) {
                            // --- BURNING FRONT ---
                            // Leading front line (dense, 1-2 pixels)
                            boolean isLeadingEdge = (diff > -currentWaveHalf * 0.35f);
                            float sparkNoise = isLeadingEdge ? 0.0f : (noise * 0.40f);

                            alpha = fireFade;
                            float flicker = 0.85f + 0.15f * (float) Math.sin(glowTime * 18.0f + ix * 2 + iz * 3);
                            r = 1.0f;
                            g = Math.min(1.0f, Math.max(0.22f, (0.52f + sparkNoise) * flicker));
                            b = Math.max(0.02f, 0.08f * flicker);

                        } else if (diff <= 0f || prog >= 1.0f) {
                            // Ash when fire completely fades out or wave has finished
                            alpha = 0.96f;
                            float charR = 0.025f + noise * 0.012f;
                            float charG = 0.020f + noise * 0.012f;
                            float charB = 0.020f + noise2 * 0.012f;
                            if (isEdge && edgeSectionActive) {
                                float greyAdd = 0.13f * slowBreathe;
                                charR += greyAdd; charG += greyAdd; charB += greyAdd;
                            }
                            r = charR; g = charG; b = charB;
                        }
                        // Ahead of fire: clean unburned petal!

                        if (alpha > 0.01f) {
                            int ir = (int)(Math.min(1.0f, Math.max(0.0f, r)) * 255);
                            int ig = (int)(Math.min(1.0f, Math.max(0.0f, g)) * 255);
                            int ib = (int)(Math.min(1.0f, Math.max(0.0f, b)) * 255);
                            int ia = (int)(alpha * 255);

                            // Top quad
                            obuf.pos(px0, y0, pz0).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px1, y0, pz0).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px1, y0, pz1).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px0, y0, pz1).color(ir, ig, ib, ia).endVertex();

                            // Bottom quad
                            obuf.pos(px0, y0, pz1).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px1, y0, pz1).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px1, y0, pz0).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px0, y0, pz0).color(ir, ig, ib, ia).endVertex();
                        }
                    }
                }
            } else if (d <= 0.001f) {
                // Flat vertical petal (small petals: length = Y, width = X)
                for (int iy = 0; iy < stepsY; iy++) {
                    float py0 = y0 + iy * dy;
                    float py1 = (iy == stepsY - 1) ? y1 : (py0 + dy);
                    float vFrac = (stepsY <= 1) ? 0.5f : (float) iy / (stepsY - 1);
                    float dBase = s0 + (s1 - s0) * vFrac;
                    if (petalForBone.flipDirection) dBase = 1.0f - dBase;

                    for (int ix = 0; ix < stepsX; ix++) {
                        float px0 = x0 + ix * dx;
                        float px1 = (ix == stepsX - 1) ? x1 : (px0 + dx);

                        float wFrac = (stepsX <= 1) ? 0.0f : ((float) ix / (stepsX - 1) - 0.5f);
                        float dDist = dBase + wFrac * tanAngle * (s1 - s0);

                        // Exactly 1 pixel wide border on the sides (in 2 times narrower than before)
                        boolean isEdge = (ix == 0 || ix == stepsX - 1);

                        int seed = (bone.name.hashCode() * 37 + ix * 53 + iy * 89) & 0x7FFFFFFF;
                        float noise = ((seed % 100) / 100.0f) - 0.5f;
                        float noise2 = (((seed / 100) % 100) / 100.0f) - 0.5f;

                        // Patchy edge: larger section size (iy/8) = fewer but longer ash strips
                        int sectionSeed = (petalForBone.id * 101 + (iy / 8) * 31 + (ix == 0 ? 11 : 73)) & 0x7FFFFFFF;
                        boolean edgeSectionActive = ((sectionSeed % 100) < 55);

                        // Desynchronized ultra-slow breathing per petal & per side (frequency 0.42f)
                        float sidePhase = petalForBone.id * 1.35f + (ix == 0 ? 0.0f : 2.4f) + (iy / 5) * 0.4f;
                        float slowBreathe = 0.5f + 0.5f * (float) Math.sin(glowTime * 0.42f + sidePhase);

                        float ashBoundaryJitter = noise * 0.032f;
                        float diff = dDist - prog;
                        float alpha = 0.0f;
                        float r = 0.04f, g = 0.04f, b = 0.04f;

                        if (diff < -currentWaveHalf + ashBoundaryJitter) {
                            alpha = 0.96f;
                            float charR = 0.016f + noise * 0.008f;
                            float charG = 0.013f + noise * 0.008f;
                            float charB = 0.013f + noise2 * 0.008f;

                            if (isEdge && edgeSectionActive) {
                                float greyAdd = 0.085f * slowBreathe + noise * 0.02f;
                                charR = Math.min(1.0f, charR + greyAdd);
                                charG = Math.min(1.0f, charG + greyAdd * 0.96f);
                                charB = Math.min(1.0f, charB + greyAdd * 0.94f);
                            }

                            float wakeDist = (-diff - currentWaveHalf);
                            float plumeLen = currentWaveHalf * 3.8f;
                            if (wakeDist > 0f && wakeDist < plumeLen && fireFade > 0.04f) {
                                float trailT = (1.0f - wakeDist / plumeLen);
                                if (noise > -0.15f * trailT) {
                                    float sparkIntensity = (0.75f + noise * 0.45f) * trailT * fireFade;
                                    charR = Math.min(1.0f, charR + 0.96f * sparkIntensity);
                                    charG = Math.min(1.0f, charG + (0.42f + noise * 0.25f) * sparkIntensity);
                                    charB = Math.min(1.0f, charB + 0.04f * sparkIntensity);
                                }
                            }

                            r = charR; g = charG; b = charB;

                        } else if (Math.abs(diff) <= currentWaveHalf && fireFade > 0.01f) {
                            boolean isLeadingEdge = (diff > -currentWaveHalf * 0.35f);
                            float sparkNoise = isLeadingEdge ? 0.0f : (noise * 0.40f);

                            alpha = fireFade;
                            float flicker = 0.85f + 0.15f * (float) Math.sin(glowTime * 18.0f + ix * 2 + iy * 3);
                            r = 1.0f;
                            g = Math.min(1.0f, Math.max(0.22f, (0.52f + sparkNoise) * flicker));
                            b = Math.max(0.02f, 0.08f * flicker);

                        } else if (diff <= 0f || prog >= 1.0f) {
                            alpha = 0.96f;
                            float charR = 0.017f + noise * 0.008f;
                            float charG = 0.013f + noise * 0.008f;
                            float charB = 0.013f + noise2 * 0.008f;
                            if (isEdge && edgeSectionActive) {
                                float greyAdd = 0.085f * slowBreathe;
                                charR += greyAdd; charG += greyAdd; charB += greyAdd;
                            }
                            r = charR; g = charG; b = charB;
                        }

                        if (alpha > 0.01f) {
                            int ir = (int)(Math.min(1.0f, Math.max(0.0f, r)) * 255);
                            int ig = (int)(Math.min(1.0f, Math.max(0.0f, g)) * 255);
                            int ib = (int)(Math.min(1.0f, Math.max(0.0f, b)) * 255);
                            int ia = (int)(alpha * 255);

                            // Front quad
                            obuf.pos(px0, py0, z0).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px1, py0, z0).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px1, py1, z0).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px0, py1, z0).color(ir, ig, ib, ia).endVertex();

                            // Back quad
                            obuf.pos(px0, py1, z0).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px1, py1, z0).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px1, py0, z0).color(ir, ig, ib, ia).endVertex();
                            obuf.pos(px0, py0, z0).color(ir, ig, ib, ia).endVertex();
                        }
                    }
                }
            }

            tessellator.draw();
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }

        // === CENTER CUBE CHARRING: blackens directionally and gradually from the side of ignited petals ===
        if ("center".equals(bone.name)) {
            boolean anyBurning = false;
            for (Petal p : petals) {
                if (p.burnProgress > 0.001f) {
                    anyBurning = true;
                    break;
                }
            }

            if (anyBurning) {
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(-1.5f, -1.5f);

                Tessellator ctess = Tessellator.getInstance();
                BufferBuilder cbuf = ctess.getBuffer();
                cbuf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

                // Subdivide center cube into small pixels for noise
                int csX = Math.max(1, Math.round(cube.size[0] * 4.0f));
                int csY = Math.max(1, Math.round(cube.size[1] * 4.0f));
                int csZ = Math.max(1, Math.round(cube.size[2] * 4.0f));
                float cdx = (x1 - x0) / csX;
                float cdy = (y1 - y0) / csY;
                float cdz = (z1 - z0) / csZ;

                // Center coordinates in local space
                float centerX = (x0 + x1) * 0.5f;
                float centerZ = (z0 + z1) * 0.5f;
                float maxRadius = (float) Math.hypot((x1 - x0) * 0.5f, (z1 - z0) * 0.5f);

                // Top & Bottom faces
                for (int cix = 0; cix < csX; cix++) {
                    float cpx0 = x0 + cix * cdx;
                    float cpx1 = (cix == csX - 1) ? x1 : (cpx0 + cdx);
                    float midX = (cpx0 + cpx1) * 0.5f - centerX;

                    for (int ciz = 0; ciz < csZ; ciz++) {
                        float cpz0 = z0 + ciz * cdz;
                        float cpz1 = (ciz == csZ - 1) ? z1 : (cpz0 + cdz);
                        float midZ = (cpz0 + cpz1) * 0.5f - centerZ;

                        float distFromCenter = (float) Math.hypot(midX, midZ);
                        float radNorm = (maxRadius > 0.001f) ? Math.min(1.0f, distFromCenter / maxRadius) : 1.0f;
                        float pointAngle = (float) Math.toDegrees(Math.atan2(midZ, midX));

                        float localCharLevel = centerCharLevelAt(pointAngle, radNorm);
                        if (localCharLevel <= 0.01f) continue;

                        int cseed = (cix * 53 + ciz * 89 + 1337) & 0x7FFFFFFF;
                        float cnoise = ((cseed % 100) / 100.0f) - 0.5f;
                        float cnoise2 = (((cseed / 100) % 100) / 100.0f) - 0.5f;

                        // Center is lighter/ash-grey, outer edge towards burning petal is darker charcoal
                        float ashCenterMix = (1.0f - radNorm);
                        float cr = (0.020f + ashCenterMix * 0.065f) + cnoise * 0.010f;
                        float cg = (0.016f + ashCenterMix * 0.065f) + cnoise * 0.010f;
                        float cb = (0.016f + ashCenterMix * 0.065f) + cnoise2 * 0.010f;

                        // Edge ash breathing strips on center cube sides
                        boolean cIsEdge = (cix == 0 || cix == csX - 1 || ciz == 0 || ciz == csZ - 1);
                        if (cIsEdge) {
                            int csecSeed = (cix / 8 * 31 + ciz / 8 * 17 + 999) & 0x7FFFFFFF;
                            boolean csecActive = ((csecSeed % 100) < 55);
                            if (csecActive) {
                                float csidePhase = (cix + ciz * 3) * 0.4f + 5.7f;
                                float cBreathe = 0.5f + 0.5f * (float) Math.sin(glowTime * 0.42f + csidePhase);
                                float cgreyAdd = 0.13f * cBreathe;
                                cr += cgreyAdd; cg += cgreyAdd; cb += cgreyAdd;
                            }
                        }

                        float calpha = localCharLevel * 0.97f;
                        int cir = (int)(Math.min(1.0f, Math.max(0.0f, cr)) * 255);
                        int cig = (int)(Math.min(1.0f, Math.max(0.0f, cg)) * 255);
                        int cib = (int)(Math.min(1.0f, Math.max(0.0f, cb)) * 255);
                        int cia = (int)(calpha * 255);

                        // Top face
                        cbuf.pos(cpx0, y1, cpz0).color(cir, cig, cib, cia).endVertex();
                        cbuf.pos(cpx1, y1, cpz0).color(cir, cig, cib, cia).endVertex();
                        cbuf.pos(cpx1, y1, cpz1).color(cir, cig, cib, cia).endVertex();
                        cbuf.pos(cpx0, y1, cpz1).color(cir, cig, cib, cia).endVertex();

                        // Bottom face
                        cbuf.pos(cpx0, y0, cpz1).color(cir, cig, cib, cia).endVertex();
                        cbuf.pos(cpx1, y0, cpz1).color(cir, cig, cib, cia).endVertex();
                        cbuf.pos(cpx1, y0, cpz0).color(cir, cig, cib, cia).endVertex();
                        cbuf.pos(cpx0, y0, cpz0).color(cir, cig, cib, cia).endVertex();
                    }
                }

                // Side faces (X-axis sides)
                for (int ciy = 0; ciy < csY; ciy++) {
                    float cpy0 = y0 + ciy * cdy;
                    float cpy1 = (ciy == csY - 1) ? y1 : (cpy0 + cdy);
                    for (int ciz = 0; ciz < csZ; ciz++) {
                        float cpz0 = z0 + ciz * cdz;
                        float cpz1 = (ciz == csZ - 1) ? z1 : (cpz0 + cdz);
                        float midZ = (cpz0 + cpz1) * 0.5f - centerZ;

                        float angleX0 = (float) Math.toDegrees(Math.atan2(midZ, x0 - centerX));
                        float angleX1 = (float) Math.toDegrees(Math.atan2(midZ, x1 - centerX));
                        float levelX0 = centerCharLevelAt(angleX0, 1.0f);
                        float levelX1 = centerCharLevelAt(angleX1, 1.0f);
                        if (levelX0 <= 0.01f && levelX1 <= 0.01f) continue;

                        int cseed = (ciy * 53 + ciz * 89 + 7777) & 0x7FFFFFFF;
                        float cnoise = ((cseed % 100) / 100.0f) - 0.5f;
                        float cr = 0.020f + cnoise * 0.010f, cg = 0.016f + cnoise * 0.010f, cb = 0.016f + cnoise * 0.010f;
                        int cir = (int)(Math.min(1.0f, cr) * 255), cig = (int)(Math.min(1.0f, cg) * 255), cib = (int)(Math.min(1.0f, cb) * 255);

                        if (levelX0 > 0.01f) {
                            int cia0 = (int)(levelX0 * 0.97f * 255);
                            cbuf.pos(x0, cpy0, cpz0).color(cir, cig, cib, cia0).endVertex();
                            cbuf.pos(x0, cpy1, cpz0).color(cir, cig, cib, cia0).endVertex();
                            cbuf.pos(x0, cpy1, cpz1).color(cir, cig, cib, cia0).endVertex();
                            cbuf.pos(x0, cpy0, cpz1).color(cir, cig, cib, cia0).endVertex();
                        }
                        if (levelX1 > 0.01f) {
                            int cia1 = (int)(levelX1 * 0.97f * 255);
                            cbuf.pos(x1, cpy0, cpz1).color(cir, cig, cib, cia1).endVertex();
                            cbuf.pos(x1, cpy1, cpz1).color(cir, cig, cib, cia1).endVertex();
                            cbuf.pos(x1, cpy1, cpz0).color(cir, cig, cib, cia1).endVertex();
                            cbuf.pos(x1, cpy0, cpz0).color(cir, cig, cib, cia1).endVertex();
                        }
                    }
                }

                // Side faces (Z-axis sides)
                for (int ciy = 0; ciy < csY; ciy++) {
                    float cpy0 = y0 + ciy * cdy;
                    float cpy1 = (ciy == csY - 1) ? y1 : (cpy0 + cdy);
                    for (int cix = 0; cix < csX; cix++) {
                        float cpx0 = x0 + cix * cdx;
                        float cpx1 = (cix == csX - 1) ? x1 : (cpx0 + cdx);
                        float midX = (cpx0 + cpx1) * 0.5f - centerX;

                        float angleZ0 = (float) Math.toDegrees(Math.atan2(z0 - centerZ, midX));
                        float angleZ1 = (float) Math.toDegrees(Math.atan2(z1 - centerZ, midX));
                        float levelZ0 = centerCharLevelAt(angleZ0, 1.0f);
                        float levelZ1 = centerCharLevelAt(angleZ1, 1.0f);
                        if (levelZ0 <= 0.01f && levelZ1 <= 0.01f) continue;

                        int cseed = (ciy * 53 + cix * 89 + 5555) & 0x7FFFFFFF;
                        float cnoise = ((cseed % 100) / 100.0f) - 0.5f;
                        float cr = 0.020f + cnoise * 0.010f, cg = 0.016f + cnoise * 0.010f, cb = 0.016f + cnoise * 0.010f;
                        int cir = (int)(Math.min(1.0f, cr) * 255), cig = (int)(Math.min(1.0f, cg) * 255), cib = (int)(Math.min(1.0f, cb) * 255);

                        if (levelZ0 > 0.01f) {
                            int cia0 = (int)(levelZ0 * 0.97f * 255);
                            cbuf.pos(cpx0, cpy0, z0).color(cir, cig, cib, cia0).endVertex();
                            cbuf.pos(cpx1, cpy0, z0).color(cir, cig, cib, cia0).endVertex();
                            cbuf.pos(cpx1, cpy1, z0).color(cir, cig, cib, cia0).endVertex();
                            cbuf.pos(cpx0, cpy1, z0).color(cir, cig, cib, cia0).endVertex();
                        }
                        if (levelZ1 > 0.01f) {
                            int cia1 = (int)(levelZ1 * 0.97f * 255);
                            cbuf.pos(cpx0, cpy1, z1).color(cir, cig, cib, cia1).endVertex();
                            cbuf.pos(cpx1, cpy1, z1).color(cir, cig, cib, cia1).endVertex();
                            cbuf.pos(cpx1, cpy0, z1).color(cir, cig, cib, cia1).endVertex();
                            cbuf.pos(cpx0, cpy0, z1).color(cir, cig, cib, cia1).endVertex();
                        }
                    }
                }

                ctess.draw();
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GlStateManager.enableTexture2D();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }

        // 3D Visual indicator: OUTLINE ONLY (no solid fill!) for currently highlighted petal
        if (debugHighlightPetal >= 0 && debugHighlightPetal < petals.size()) {
            Petal selPetal = petals.get(debugHighlightPetal);
            if (selPetal != null && selPetal.containsBone(bone.name)) {
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                GlStateManager.glLineWidth(2.5f);

                float pulse = 0.5f + 0.5f * (float) Math.sin(glowTime * 8.0f);
                float hr = 0.2f + 0.7f * pulse;
                float hg = 0.85f + 0.15f * pulse;
                float hb = 1.0f - 0.4f * pulse;
                GlStateManager.color(hr, hg, hb, 0.95f);

                BufferBuilder wbuf = tessellator.getBuffer();
                wbuf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

                // Bottom quad lines
                wbuf.pos(x0, y0, z0).endVertex(); wbuf.pos(x1, y0, z0).endVertex();
                wbuf.pos(x1, y0, z0).endVertex(); wbuf.pos(x1, y0, z1).endVertex();
                wbuf.pos(x1, y0, z1).endVertex(); wbuf.pos(x0, y0, z1).endVertex();
                wbuf.pos(x0, y0, z1).endVertex(); wbuf.pos(x0, y0, z0).endVertex();

                // Top quad lines
                wbuf.pos(x0, y1, z0).endVertex(); wbuf.pos(x1, y1, z0).endVertex();
                wbuf.pos(x1, y1, z0).endVertex(); wbuf.pos(x1, y1, z1).endVertex();
                wbuf.pos(x1, y1, z1).endVertex(); wbuf.pos(x0, y1, z1).endVertex();
                wbuf.pos(x0, y1, z1).endVertex(); wbuf.pos(x0, y1, z0).endVertex();

                // Vertical pillars
                wbuf.pos(x0, y0, z0).endVertex(); wbuf.pos(x0, y1, z0).endVertex();
                wbuf.pos(x1, y0, z0).endVertex(); wbuf.pos(x1, y1, z0).endVertex();
                wbuf.pos(x1, y0, z1).endVertex(); wbuf.pos(x1, y1, z1).endVertex();
                wbuf.pos(x0, y0, z1).endVertex(); wbuf.pos(x0, y1, z1).endVertex();

                tessellator.draw();

                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableTexture2D();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            }
        }

        GlStateManager.popMatrix();
    }

    private static void quad(BufferBuilder buf,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float x3, float y3, float z3,
                             float x4, float y4, float z4,
                             float u1, float v1, float u2, float v2,
                             float tw, float th,
                             float nx, float ny, float nz) {
        float minU = u1 / tw, minV = v1 / th;
        float maxU = u2 / tw, maxV = v2 / th;

        buf.pos(x1, y1, z1).tex(minU, minV).normal(nx, ny, nz).endVertex();
        buf.pos(x2, y2, z2).tex(maxU, minV).normal(nx, ny, nz).endVertex();
        buf.pos(x3, y3, z3).tex(maxU, maxV).normal(nx, ny, nz).endVertex();
        buf.pos(x4, y4, z4).tex(minU, maxV).normal(nx, ny, nz).endVertex();
    }

    // Helper classes
    public static class Bone {
        public final String name;
        public final String parentName;
        public final float[] pivot;
        public final float[] rotation;
        public final List<Cube> cubes = new ArrayList<>();
        public final List<Bone> children = new ArrayList<>();

        public final float[] animRot = new float[3];
        public final float[] animPos = new float[3];

        public Bone(String name, String parentName, float[] pivot, float[] rotation) {
            this.name = name;
            this.parentName = parentName;
            this.pivot = pivot;
            this.rotation = rotation;
        }
    }

    public static class Cube {
        public final float[] origin;
        public final float[] size;
        public final float[] uv;
        public final float[] cubePivot;
        public final float[] cubeRot;

        public Cube(float ox, float oy, float oz, float sx, float sy, float sz, float u, float v, float[] cp, float[] cr) {
            this.origin = new float[]{ox, oy, oz};
            this.size = new float[]{sx, sy, sz};
            this.uv = new float[]{u, v};
            this.cubePivot = cp;
            this.cubeRot = cr;
        }
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

    public static class BoneTrack {
        public final List<Keyframe> rotKeyframes = new ArrayList<>();
        public final List<Keyframe> posKeyframes = new ArrayList<>();
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

    private static float angularDiff(float a, float b) {
        float d = (a - b) % 360f;
        if (d > 180f) d -= 360f;
        if (d < -180f) d += 360f;
        return d;
    }

    /**
     * Calculates the gradual directional charring level (0.0 to 1.0) at a specific point on the center cube.
     * Starts and finishes synchronously with the petal burn animation.
     * @param angleDeg Direction from center cube pivot in XZ plane (-180 to 180 degrees)
     * @param radialDistNorm Normalized distance from center (0.0 = exact center, 1.0 = outer border facing petals)
     */
    private float centerCharLevelAt(float angleDeg, float radialDistNorm) {
        int n = petals.size();
        if (n == 0) return 0f;

        float wedge = 360f / n;          // Exactly 30 degrees per petal (1/12th of circle)
        float halfWedge = wedge * 0.5f;  // 15 degrees
        float blendAngle = 8.0f;         // Clean, narrow seam between sectors

        float combinedLevel = 0f;
        for (Petal p : petals) {
            if (p.burnProgress <= 0.05f) continue;

            // Synchronized smoothly with petal burn wave: 0.0 at prog=0.05, 1.0 at prog=1.0
            float rawT = Math.min(1.0f, Math.max(0.0f, (p.burnProgress - 0.05f) / 0.95f));
            // Smooth ease-in ease-out progression (not instant, ends exactly with wave)
            float burnT = rawT * rawT * (3.0f - 2.0f * rawT);

            float absAngleDiff = Math.abs(angularDiff(angleDeg, p.centerAngleDeg));

            float angularWeight = 0f;
            if (absAngleDiff <= halfWedge) {
                angularWeight = 1.0f - (absAngleDiff / halfWedge) * 0.25f;
            } else if (absAngleDiff <= halfWedge + blendAngle) {
                float t = 1.0f - (absAngleDiff - halfWedge) / blendAngle;
                angularWeight = t * 0.75f;
            }

            if (angularWeight <= 0.001f) continue;

            // Radial gradient: starts from the outer edge (radNorm=1.0) and creeps fully inward when burnT=1.0
            // minRadToBurn=0 when fully burned, so the entire center including the middle gets charred
            float minRadToBurn = Math.max(0.0f, 1.0f - burnT * 1.05f);
            float radialFactor = 0f;
            if (radialDistNorm >= minRadToBurn) {
                float radDepth = (radialDistNorm - minRadToBurn) / (1.0f - minRadToBurn + 0.001f);
                radialFactor = 0.20f + 0.80f * radDepth; // gradient from inner edge to outer boundary
            }

            float petalContrib = burnT * angularWeight * radialFactor;
            if (petalContrib > combinedLevel) {
                combinedLevel = petalContrib;
            }
        }

        return Math.min(1.0f, combinedLevel);
    }
}