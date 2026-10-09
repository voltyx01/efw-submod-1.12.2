package com.voltyx.mwccf.blood;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Procedural per-texel blood overlay system.
 *
 * Rather than rendering an external 2D sprite or stencil bounding box, this system
 * modifies the texture itself (skin, vanilla armor, 3D mod armor like Survival Instinct,
 * GeoArmor, etc.) by compositing realistic blood splatters directly in UV space,
 * exactly like the flower burn system in SevenScreen.
 *
 * Bloody textures are generated on-demand and cached per (ResourceLocation, bloodStage).
 * Once cached, binding is instant with zero CPU/GL overhead.
 */
@SideOnly(Side.CLIENT)
public class BloodTextureManager {

    private static EntityPlayer currentRenderingPlayer = null;
    private static boolean isGenerating = false;

    // Cache: "resource_string#stage" -> DynamicTexture ResourceLocation
    private static final Map<String, ResourceLocation> CACHE = new ConcurrentHashMap<>();
    // In-memory cache for base textures so ImageIO / GL11.glGetTexImage only run once
    private static final Map<ResourceLocation, BufferedImage> BASE_IMAGE_CACHE = new ConcurrentHashMap<>();

    public static void setRenderingPlayer(EntityPlayer player) {
        currentRenderingPlayer = player;
    }

    public static void clearRenderingPlayer() {
        currentRenderingPlayer = null;
    }

    public static boolean isRenderingPlayer() {
        return currentRenderingPlayer != null;
    }

    public static EntityPlayer getRenderingPlayer() {
        return currentRenderingPlayer;
    }

    public static void clearCache() {
        CACHE.clear();
        BASE_IMAGE_CACHE.clear();
    }

    /**
     * Intercepts TextureManager.bindTexture calls.
     * If currently rendering a player who has blood, replaces the bound texture
     * with a bloody cached version.
     */
    public static ResourceLocation getReplacementTexture(ResourceLocation original) {
        if (original == null) return null;
        if (isGenerating) return original;

        EntityPlayer target = currentRenderingPlayer;
        if (target == null) return original;

        float bloodLevel = BloodManager.getBloodLevel(target.getUniqueID());
        if (bloodLevel <= 0.001f) return original;

        if (!shouldBloodyTexture(original, target)) return original;

        // 20-step smooth quantization (5% per step):
        // 0.05, 0.10, 0.15, ... 1.00
        int step = (int) Math.ceil(bloodLevel * 20.0f);
        step = Math.max(1, Math.min(20, step));

        String cacheKey = original.toString() + "#step_" + step;
        ResourceLocation cached = CACHE.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        float effectiveLevel = step / 20.0f;
        return createAndCacheBloodyTexture(original, effectiveLevel, cacheKey);
    }

    /**
     * Filters out non-player/armor textures (GUI, fonts, particles, environment, glint, block atlas, mobs, tile entities, etc.)
     */
    private static boolean shouldBloodyTexture(ResourceLocation res, EntityPlayer target) {
        String path = res.getPath();

        // Never bloody dynamic blood textures, UI, fonts, particles, environment, glint, block atlas
        if (path.contains("mwccf_blood") || path.contains("dynamic/mwccf_blood")) return false;
        if (path.startsWith("textures/gui/")) return false;
        if (path.startsWith("textures/font/")) return false;
        if (path.startsWith("textures/particle/")) return false;
        if (path.startsWith("textures/environment/")) return false;
        if (path.startsWith("textures/colormap/")) return false;
        if (path.startsWith("textures/misc/") && !path.contains("backpack")) return false;
        if (path.contains("glint")) return false;
        if (path.contains("shadow")) return false;
        if (res.equals(TextureMap.LOCATION_BLOCKS_TEXTURE)) return false;

        // Player skin is ALWAYS bloodied (both in 3rd person and 1st person)
        if (target instanceof AbstractClientPlayer
                && res.equals(((AbstractClientPlayer) target).getLocationSkin())) {
            return true;
        }

        // Vanilla armor: textures/models/armor/...
        if (path.startsWith("textures/models/armor/")) return true;

        // Modded player armor & clothing textures (Survival Instinct, GeoArmor, Geckolib, sleeves, vests, backpacks)
        if (path.contains("armor") || path.contains("vest") || path.contains("helmet")
                || path.contains("suit") || path.contains("cloth") || path.contains("sleeve")
                || path.contains("geo/") || path.contains("backpack")) {
            return true;
        }

        return false;
    }

    private static ResourceLocation createAndCacheBloodyTexture(ResourceLocation original, float effectiveLevel, String cacheKey) {
        isGenerating = true;
        try {
            BufferedImage base = loadBaseImage(original);
            if (base == null) {
                // If loading failed, fallback to original to prevent retrying every frame
                CACHE.put(cacheKey, original);
                return original;
            }

            BufferedImage bloody = applyBloodToImage(base, original, effectiveLevel);
            DynamicTexture dynamicTexture = new DynamicTexture(bloody);
            String dynName = "mwccf_blood_" + Math.abs(cacheKey.hashCode());
            ResourceLocation dynLoc = Minecraft.getMinecraft().getTextureManager()
                    .getDynamicTextureLocation(dynName, dynamicTexture);

            CACHE.put(cacheKey, dynLoc);
            return dynLoc;
        } catch (Throwable t) {
            System.err.println("[BloodTextureManager] Error generating blood texture for " + original + ": " + t.getMessage());
            CACHE.put(cacheKey, original);
            return original;
        } finally {
            isGenerating = false;
        }
    }

    private static BufferedImage loadBaseImage(ResourceLocation res) {
        BufferedImage cached = BASE_IMAGE_CACHE.get(res);
        if (cached != null) {
            return toArgb(cached);
        }

        // 1. Try reading via Minecraft ResourceManager
        try {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(res);
            if (resource != null) {
                try (InputStream in = resource.getInputStream()) {
                    BufferedImage img = ImageIO.read(in);
                    if (img != null) {
                        BufferedImage argb = toArgb(img);
                        BASE_IMAGE_CACHE.put(res, argb);
                        return argb;
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        // 2. If not found in ResourceManager (e.g. downloaded player skins), read from OpenGL texture
        try {
            ITextureObject texObj = Minecraft.getMinecraft().getTextureManager().getTexture(res);
            if (texObj == null) {
                Minecraft.getMinecraft().getTextureManager().loadTexture(res, new SimpleTexture(res));
                texObj = Minecraft.getMinecraft().getTextureManager().getTexture(res);
            }
            if (texObj != null) {
                int glId = texObj.getGlTextureId();
                if (glId > 0) {
                    BufferedImage img = readImageFromGl(glId);
                    if (img != null) {
                        BufferedImage argb = toArgb(img);
                        BASE_IMAGE_CACHE.put(res, argb);
                        return argb;
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        return null;
    }

    private static BufferedImage toArgb(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        if (src.getType() == BufferedImage.TYPE_INT_ARGB) {
            BufferedImage copy = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            int[] data = new int[w * h];
            src.getRGB(0, 0, w, h, data, 0, w);
            copy.setRGB(0, 0, w, h, data, 0, w);
            return copy;
        }
        BufferedImage argb = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        int[] data = new int[w * h];
        src.getRGB(0, 0, w, h, data, 0, w);
        argb.setRGB(0, 0, w, h, data, 0, w);
        return argb;
    }

    private static BufferedImage readImageFromGl(int glId) {
        try {
            int prevTex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
            int w = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            int h = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
            if (w <= 0 || h <= 0 || w > 2048 || h > 2048) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTex);
                return null;
            }

            IntBuffer buffer = BufferUtils.createIntBuffer(w * h);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTex);

            int[] pixels = new int[w * h];
            buffer.get(pixels);
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            img.setRGB(0, 0, w, h, pixels, 0, w);
            return img;
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Composites realistic blood splatters directly into the texture's UV space.
     * - Only modifies non-transparent pixels (alpha > 20), preserving transparent borders.
     * - Face UV zones get a slightly higher drop count, fulfilling:
     *   "с большим шансом кровь будет на лице, но не намного больше чем на других, слегка больше шанс на лице."
     * - Torso, arms, and custom 3D armor surfaces receive natural organic blood stains.
     */
    private static BufferedImage applyBloodToImage(BufferedImage base, ResourceLocation original, float effectiveLevel) {
        int w = base.getWidth();
        int h = base.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = new int[w * h];
        base.getRGB(0, 0, w, h, pixels, 0, w);
        result.setRGB(0, 0, w, h, pixels, 0, w);

        long baseSeed = (long) original.toString().hashCode() * 374761393L;
        float scale = Math.max(1.0f, w / 64.0f);
        float fadeRange = 0.15f; // Smooth fade-in window for each spot

        // 1. Face / Head spots (18 total deterministic spots)
        final int TOTAL_FACE_SPOTS = 18;
        for (int i = 0; i < TOTAL_FACE_SPOTS; i++) {
            float startLevel = ((float) i / TOTAL_FACE_SPOTS) * 0.85f;
            if (effectiveLevel <= startLevel) continue;
            float fade = Math.min(1.0f, (effectiveLevel - startLevel) / fadeRange);

            Random rng = new Random(baseSeed + i * 10007L + 11L);
            boolean isOuterLayer = rng.nextBoolean();
            float uMin = isOuterLayer ? 40f / 64f : 8f / 64f;
            float uMax = isOuterLayer ? 48f / 64f : 16f / 64f;
            float vMin = 8f / 64f;
            float vMax = 16f / 64f;

            int cx = (int) ((uMin + rng.nextFloat() * (uMax - uMin)) * w);
            int cy = (int) ((vMin + rng.nextFloat() * (vMax - vMin)) * h);
            float rad = (1.0f + rng.nextFloat() * 2.0f) * scale;
            paintSplatter(result, base, cx, cy, rad, 0.92f * fade, rng, true);
        }

        // 2. Torso spots (20 total deterministic spots)
        final int TOTAL_TORSO_SPOTS = 20;
        for (int i = 0; i < TOTAL_TORSO_SPOTS; i++) {
            float startLevel = ((float) i / TOTAL_TORSO_SPOTS) * 0.85f;
            if (effectiveLevel <= startLevel) continue;
            float fade = Math.min(1.0f, (effectiveLevel - startLevel) / fadeRange);

            Random rng = new Random(baseSeed + i * 10007L + 23L);
            float uMin = 18f / 64f;
            float uMax = 32f / 64f;
            float vMin = 20f / 64f;
            float vMax = (h <= 32) ? 1.0f : 32f / 64f;

            int cx = (int) ((uMin + rng.nextFloat() * (uMax - uMin)) * w);
            int cy = (int) ((vMin + rng.nextFloat() * (vMax - vMin)) * h);
            float rad = (1.1f + rng.nextFloat() * 2.2f) * scale;
            paintSplatter(result, base, cx, cy, rad, 0.90f * fade, rng, true);
        }

        // 3. Arms, Hands, Wrists & Sleeves spots (24 total deterministic spots)
        final int TOTAL_ARM_SPOTS = 24;
        for (int i = 0; i < TOTAL_ARM_SPOTS; i++) {
            float startLevel = ((float) i / TOTAL_ARM_SPOTS) * 0.85f;
            if (effectiveLevel <= startLevel) continue;
            float fade = Math.min(1.0f, (effectiveLevel - startLevel) / fadeRange);

            Random rng = new Random(baseSeed + i * 10007L + 37L);
            // Alternate arms: even i = right arm (main hand in 1st person!), odd i = left arm
            boolean rightArm = (i % 2 == 0);

            // Paint primarily on the base arm layer (guaranteed opaque on all skins)
            float uMin, uMax, vMin, vMax;
            if (rightArm) {
                uMin = 40f / 64f; uMax = 56f / 64f;
                // Focus on forearm / wrist / hand (V 22..32) so it's directly visible in 1st person
                vMin = (h <= 32) ? 18f / 32f : 22f / 64f;
                vMax = (h <= 32) ? 32f / 32f : 32f / 64f;
            } else {
                uMin = 32f / 64f; uMax = 48f / 64f;
                vMin = (h <= 32) ? 18f / 32f : 54f / 64f;
                vMax = (h <= 32) ? 32f / 32f : 64f / 64f;
            }

            // Drops directly on hands / palms
            if (h >= 64 && (i == 0 || rng.nextFloat() < 0.35f)) {
                if (rightArm) {
                    uMin = 44f / 64f; uMax = 52f / 64f;
                    vMin = 16f / 64f; vMax = 20f / 64f;
                } else {
                    uMin = 36f / 64f; uMax = 44f / 64f;
                    vMin = 48f / 64f; vMax = 52f / 64f;
                }
            }

            int cx = (int) ((uMin + rng.nextFloat() * (uMax - uMin)) * w);
            int cy = (int) ((vMin + rng.nextFloat() * (vMax - vMin)) * h);
            float rad = (1.3f + rng.nextFloat() * 1.8f) * scale;
            paintSplatter(result, base, cx, cy, rad, 0.92f * fade, rng, true);

            // If 64x64 skin has sleeve outer layer, also paint corresponding sleeve spot
            if (h >= 64) {
                int scx, scy;
                if (rightArm) {
                    scx = cx;
                    scy = cy + (int) ((16f / 64f) * h); // offset from base arm (V 20..32) to sleeve (V 36..48)
                } else {
                    scx = cx + (int) ((16f / 64f) * w); // offset from base arm (U 32..48) to sleeve (U 48..64)
                    scy = cy;
                }
                if (scx >= 0 && scx < w && scy >= 0 && scy < h) {
                    int p = base.getRGB(scx, scy);
                    if (((p >> 24) & 0xFF) >= 20) {
                        paintSplatter(result, base, scx, scy, rad, 0.92f * fade, rng, true);
                    }
                }
            }
        }

        // 4. Organic surface scatter for 3D armor, vests, helmets (36 total deterministic spots)
        final int TOTAL_SCATTER_SPOTS = 36;
        for (int i = 0; i < TOTAL_SCATTER_SPOTS; i++) {
            float startLevel = ((float) i / TOTAL_SCATTER_SPOTS) * 0.85f;
            if (effectiveLevel <= startLevel) continue;
            float fade = Math.min(1.0f, (effectiveLevel - startLevel) / fadeRange);

            Random rng = new Random(baseSeed + i * 10007L + 53L);
            for (int attempt = 0; attempt < 25; attempt++) {
                int rx = rng.nextInt(w);
                int ry = rng.nextInt(h);
                int baseP = base.getRGB(rx, ry);
                if (((baseP >> 24) & 0xFF) > 40) {
                    float rad = (1.0f + rng.nextFloat() * 2.0f) * scale;
                    paintSplatter(result, base, rx, ry, rad, 0.86f * fade, rng, false);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Paints an organic blood splatter centered at (cx, cy).
     * Features darker, richer multi-tone blood shades:
     * - Coagulated dark maroon
     * - Deep venous burgundy
     * - Saturated dark crimson
     * - Dried brownish-red stains
     */
    private static void paintSplatter(BufferedImage result, BufferedImage base,
                                      int cx, int cy, float radius, float intensity,
                                      Random rng, boolean canDrip) {
        int w = base.getWidth();
        int h = base.getHeight();

        int intRad = (int) Math.ceil(radius) + 1;

        // Rich multi-tone blood palette selection per droplet
        int shadeType = rng.nextInt(4);
        int baseR, baseG, baseB;

        switch (shadeType) {
            case 0: // Coagulated dark maroon/black-red
                baseR = 56 + rng.nextInt(18); // 56 - 74
                baseG = 3 + rng.nextInt(6);   // 3 - 9
                baseB = 5 + rng.nextInt(9);   // 5 - 14
                break;
            case 1: // Deep burgundy / venous dark red
                baseR = 74 + rng.nextInt(22); // 74 - 96
                baseG = 2 + rng.nextInt(6);   // 2 - 8
                baseB = 10 + rng.nextInt(14); // 10 - 24
                break;
            case 2: // Rich dark crimson
                baseR = 92 + rng.nextInt(26); // 92 - 118
                baseG = 4 + rng.nextInt(7);   // 4 - 11
                baseB = 6 + rng.nextInt(12);  // 6 - 18
                break;
            default: // Dried brownish-umber stain
                baseR = 66 + rng.nextInt(20); // 66 - 86
                baseG = 10 + rng.nextInt(10); // 10 - 20
                baseB = 6 + rng.nextInt(8);   // 6 - 14
                break;
        }

        // 1. Main droplet
        for (int dy = -intRad; dy <= intRad; dy++) {
            for (int dx = -intRad; dx <= intRad; dx++) {
                int px = cx + dx;
                int py = cy + dy;
                if (px < 0 || px >= w || py < 0 || py >= h) continue;

                int basePixel = base.getRGB(px, py);
                int baseAlpha = (basePixel >> 24) & 0xFF;
                if (baseAlpha < 20) continue; // Skip transparent space!

                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist > radius) continue;

                float normDist = dist / radius;
                float falloff = (1.0f - normDist * normDist);
                float jitter = 0.85f + rng.nextFloat() * 0.3f;
                float alphaFactor = Math.min(1.0f, Math.max(0.0f, falloff * jitter * intensity));

                // Thick center is coagulated and darker
                int curR = (normDist < 0.40f) ? (int)(baseR * 0.75f) : baseR;
                int curG = (normDist < 0.40f) ? (int)(baseG * 0.70f) : baseG;
                int curB = (normDist < 0.40f) ? (int)(baseB * 0.75f) : baseB;

                blendPixel(result, px, py, basePixel, curR, curG, curB, alphaFactor);
            }
        }

        // 2. Micro-satellite droplets
        int satellites = 1 + rng.nextInt(3);
        for (int s = 0; s < satellites; s++) {
            float ang = rng.nextFloat() * (float) (Math.PI * 2.0);
            float dist = radius * (1.2f + rng.nextFloat() * 1.5f);
            int sx = cx + (int) (Math.cos(ang) * dist);
            int sy = cy + (int) (Math.sin(ang) * dist);
            if (sx >= 0 && sx < w && sy >= 0 && sy < h) {
                int basePixel = base.getRGB(sx, sy);
                if (((basePixel >> 24) & 0xFF) >= 20) {
                    blendPixel(result, sx, sy, basePixel, baseR, baseG, baseB, 0.75f * intensity);
                }
            }
        }

        // 3. Small downward drip/streak
        if (canDrip && rng.nextFloat() < 0.35f) {
            int dripLen = 1 + rng.nextInt(3);
            for (int dy = 1; dy <= dripLen; dy++) {
                int py = cy + (int) radius + dy;
                int px = cx + (rng.nextInt(3) - 1);
                if (px >= 0 && px < w && py >= 0 && py < h) {
                    int basePixel = base.getRGB(px, py);
                    if (((basePixel >> 24) & 0xFF) >= 20) {
                        float dripAlpha = (1.0f - (float) dy / (dripLen + 1)) * 0.70f * intensity;
                        blendPixel(result, px, py, basePixel, baseR, baseG, baseB, dripAlpha);
                    }
                }
            }
        }
    }

    private static void blendPixel(BufferedImage result, int x, int y, int basePixel,
                                   int bloodR, int bloodG, int bloodB, float alphaFactor) {
        int origA = (basePixel >> 24) & 0xFF;
        int origR = (basePixel >> 16) & 0xFF;
        int origG = (basePixel >> 8) & 0xFF;
        int origB = basePixel & 0xFF;

        float factor = Math.min(1.0f, alphaFactor);

        // Darker, richer tint/stain blending:
        // Blood darkens underlying surface effectively while staining with multi-tone crimson
        int outR = Math.min(255, (int) (origR * (1.0f - factor * 0.62f) + bloodR * factor * 0.85f));
        int outG = Math.max(0,   (int) (origG * (1.0f - factor * 0.92f) + bloodG * factor * 0.08f));
        int outB = Math.max(0,   (int) (origB * (1.0f - factor * 0.90f) + bloodB * factor * 0.10f));

        // Preserve original alpha completely: zero transparency bugs or wall-see-through!
        result.setRGB(x, y, (origA << 24) | (outR << 16) | (outG << 8) | outB);
    }
}
