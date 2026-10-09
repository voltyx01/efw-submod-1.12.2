package com.voltyx.mwccf.fireweapon.client;

import com.voltyx.mwccf.fireweapon.FireWeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Procedural per-texel weapon texture compositing system.
 *
 * Composites the cloth wrapping from cloth_weapon.png directly into the weapon's pixels in UV space.
 * When ignited, produces:
 * 1. Base diffuse composite texture with slow, silky-smooth animated burning and breathing embers.
 * 2. Emissive glow pass where burning pixels, flame fronts, and living embers SHINE BRIGHTLY IN THE DARK (240/240 lightmap).
 * 3. Soft, majestic animated fiery surface sheen.
 */
@SideOnly(Side.CLIENT)
public class FireWeaponTextureManager {

    private static final ResourceLocation RES_CLOTH_WEAPON = new ResourceLocation("mwccf", "textures/items/cloth_weapon.png");
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private static class TextureHolder {
        final ResourceLocation base;
        final ResourceLocation emissive;
        final List<FireWeaponSparkManager.BurningVoxel> burningVoxels;

        TextureHolder(ResourceLocation base, ResourceLocation emissive, List<FireWeaponSparkManager.BurningVoxel> burningVoxels) {
            this.base = base;
            this.emissive = emissive;
            this.burningVoxels = burningVoxels;
        }
    }

    private static class ProcessedImages {
        final BufferedImage base;
        final BufferedImage emissive;
        final List<FireWeaponSparkManager.BurningVoxel> burningVoxels;

        ProcessedImages(BufferedImage base, BufferedImage emissive, List<FireWeaponSparkManager.BurningVoxel> burningVoxels) {
            this.base = base;
            this.emissive = emissive;
            this.burningVoxels = burningVoxels;
        }
    }

    // Cache: "itemRegistryName#step_X#f_Y" -> TextureHolder
    private static final Map<String, TextureHolder> TEXTURE_CACHE = new ConcurrentHashMap<>();

    private static BufferedImage cachedClothImage = null;

    public static void clearCache() {
        TEXTURE_CACHE.clear();
    }

    /**
     * Renders a wrapped or ignited weapon using its procedurally composited texture.
     * Returns true if custom rendering was performed, false to fallback to vanilla.
     */
    public static boolean renderWrappedItem(RenderItem renderItem, ItemStack stack, IBakedModel model) {
        if (stack.isEmpty() || model == null) return false;

        boolean isIgnited = FireWeaponHelper.isIgnited(stack);
        boolean isCharPhase = FireWeaponHelper.isInCharPhase(stack);
        boolean hasVisibleCloth = isIgnited || isCharPhase || FireWeaponHelper.isWrapped(stack) || FireWeaponHelper.isSoakedReady(stack);
        if (!hasVisibleCloth) return false;

        TextureHolder textures = getOrCreateTextures(stack, model);
        if (textures == null || textures.base == null) return false;

        // Capture initial GL state to prevent leaking into hand / vanilla item rendering
        boolean wasBlend = GL11.glGetBoolean(GL11.GL_BLEND);
        boolean wasLighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
        boolean wasRescaleNormal = GL11.glGetBoolean(0x803A); // GL_RESCALE_NORMAL
        boolean wasAlpha = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);
        boolean wasDepthTest = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        boolean wasDepthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        float origBrightnessX = OpenGlHelper.lastBrightnessX;
        float origBrightnessY = OpenGlHelper.lastBrightnessY;

        GlStateManager.pushMatrix();
        try {
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            // --- PASS 1: Base weapon model with ambient lighting ---
            Minecraft.getMinecraft().getTextureManager().bindTexture(textures.base);
            renderModelQuads(model, true);

            // Restore texture atlas
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            // --- PASS 2: Emissive glow in the dark (only burning pixels shine at full 240/240 brightness!) ---
            if (isIgnited && textures.emissive != null) {
                float lastX = OpenGlHelper.lastBrightnessX;
                float lastY = OpenGlHelper.lastBrightnessY;

                GlStateManager.disableLighting();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                GlStateManager.depthFunc(GL11.GL_EQUAL);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                // Additive glow: burning pixels emit real luminous firelight in darkness!
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);

                Minecraft.getMinecraft().getTextureManager().bindTexture(textures.emissive);
                renderModelQuads(model, true);

                // Restore GL state
                GlStateManager.depthMask(true);
                GlStateManager.depthFunc(GL11.GL_LEQUAL);
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY);
                GlStateManager.enableLighting();
                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            }

            // --- PASS 3: Fire shimmer — only during active burning (not char phase) ---
            if (isIgnited) {
                renderFireShimmer(model);
            }

            // --- PASS 4: Enchantment glint if enchanted ---
            if (stack.hasEffect()) {
                renderGlint(model);
            }

            // --- PASS 5: Sparks — only during active burning (not char phase) ---
            if (isIgnited && textures.burningVoxels != null && !textures.burningVoxels.isEmpty()) {
                FireWeaponSparkManager.renderAndSpawnItemSparks(stack, textures.burningVoxels);
            }
        } finally {
            // Guarantee complete restoration of OpenGL and GlStateManager state
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, origBrightnessX, origBrightnessY);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if (wasBlend) {
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            } else {
                GlStateManager.disableBlend();
            }
            if (wasAlpha) GlStateManager.enableAlpha(); else GlStateManager.disableAlpha();
            if (wasLighting) GlStateManager.enableLighting(); else GlStateManager.disableLighting();
            if (wasRescaleNormal) GlStateManager.enableRescaleNormal(); else GlStateManager.disableRescaleNormal();
            if (wasDepthTest) GlStateManager.enableDepth(); else GlStateManager.disableDepth();
            GlStateManager.depthFunc(GL11.GL_LEQUAL);
            GlStateManager.depthMask(wasDepthMask);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.popMatrix();
        }
        return true;
    }

    private static void renderModelQuads(IBakedModel model, boolean normalizeUV) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

        List<BakedQuad> quads = new ArrayList<>();
        for (EnumFacing facing : EnumFacing.values()) {
            quads.addAll(model.getQuads(null, facing, 0L));
        }
        quads.addAll(model.getQuads(null, null, 0L));

        TextureAtlasSprite defaultSprite = model.getParticleTexture();

        for (BakedQuad quad : quads) {
            int[] data = quad.getVertexData();
            if (data == null || data.length < 28) continue;

            TextureAtlasSprite sprite = quad.getSprite();
            if (sprite == null) sprite = defaultSprite;

            float minU = (sprite != null && normalizeUV) ? sprite.getMinU() : 0.0F;
            float maxU = (sprite != null && normalizeUV) ? sprite.getMaxU() : 1.0F;
            float minV = (sprite != null && normalizeUV) ? sprite.getMinV() : 0.0F;
            float maxV = (sprite != null && normalizeUV) ? sprite.getMaxV() : 1.0F;
            float du = (maxU > minU) ? (maxU - minU) : 1.0F;
            float dv = (maxV > minV) ? (maxV - minV) : 1.0F;

            int[] vertexData = data.clone();
            if (normalizeUV && sprite != null && du > 0.00001F && dv > 0.00001F) {
                for (int v = 0; v < 4; v++) {
                    int off = v * 7;
                    float u = Float.intBitsToFloat(vertexData[off + 4]);
                    float vCoord = Float.intBitsToFloat(vertexData[off + 5]);
                    float finalU = (u - minU) / du;
                    float finalV = (vCoord - minV) / dv;
                    finalU = MathHelper.clamp(finalU, 0.0F, 1.0F);
                    finalV = MathHelper.clamp(finalV, 0.0F, 1.0F);
                    vertexData[off + 4] = Float.floatToRawIntBits(finalU);
                    vertexData[off + 5] = Float.floatToRawIntBits(finalV);
                }
            }

            buffer.addVertexData(vertexData);
        }

        tessellator.draw();
    }

    /**
     * Renders a slow, majestic fiery golden sheen directly over the weapon model quads.
     * Also glows gently in the dark.
     */
    private static void renderFireShimmer(IBakedModel model) {
        float lastX = OpenGlHelper.lastBrightnessX;
        float lastY = OpenGlHelper.lastBrightnessY;

        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(GL11.GL_EQUAL);
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO); // Additive fire glow
        Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT);

        long time = Minecraft.getSystemTime();

        // 1. Slow, silky smooth primary diagonal wave (~7.0 seconds period)
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);

        float f1 = (float) (time % 7000L) / 7000.0F / 8.0F;
        GlStateManager.translate(f1, f1 * 0.5F, 0.0F);
        GlStateManager.rotate(-45.0F, 0.0F, 0.0F, 1.0F);

        // Gentle breathing pulsation
        float flicker = 0.88F + 0.12F * MathHelper.sin((float) (time % 20000L) * 0.003F);
        GlStateManager.color(1.0F * flicker, 0.60F * flicker, 0.12F, 0.50F);
        renderModelQuads(model, false);

        GlStateManager.popMatrix();

        // 2. Slow counter-diagonal heat breath (~9.5 seconds period)
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f2 = (float) (time % 9500L) / 9500.0F / 8.0F;
        GlStateManager.translate(-f2, f2 * 0.35F, 0.0F);
        GlStateManager.rotate(25.0F, 0.0F, 0.0F, 1.0F);

        GlStateManager.color(1.0F * flicker, 0.35F * flicker, 0.05F, 0.35F);
        renderModelQuads(model, false);

        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    private static void renderGlint(IBakedModel model) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(GL11.GL_EQUAL);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        renderModelQuads(model, false);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        renderModelQuads(model, false);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(true);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    private static TextureHolder getOrCreateTextures(ItemStack stack, IBakedModel model) {
        Item item = stack.getItem();
        boolean isIgnited = FireWeaponHelper.isIgnited(stack);
        boolean isCharPhase = FireWeaponHelper.isInCharPhase(stack);
        boolean isSoaked = FireWeaponHelper.isSoaked(stack);
        int step = 0;
        int animFrame = 0;
        float charAlpha = 1.0F;

        if (isIgnited) {
            float prog = FireWeaponHelper.getBurnProgress(stack);
            step = Math.max(1, Math.min(10, (int) Math.ceil(prog * 10.0F)));
            // 24 smooth animation frames at 150ms per frame (3.6s calm, organic cycle)
            animFrame = (int) ((Minecraft.getSystemTime() / 150L) % 24);
        } else if (isCharPhase) {
            // Fully charred appearance (step=10), alpha fades over time
            step = 10;
            charAlpha = FireWeaponHelper.getCharAlpha(stack);
            // Quantize to 8 levels so we don't generate a new texture every tick
            charAlpha = Math.round(charAlpha * 8.0F) / 8.0F;
        }

        ResourceLocation reg = item.getRegistryName();
        String itemId = (reg != null) ? reg.toString() : item.getClass().getName();
        String cacheKey;
        if (isIgnited) {
            if (isSoaked) {
                // In soaked mode all cloth pixels burn simultaneously with flowing flame animation
                cacheKey = itemId + "#soaked_f_" + animFrame;
            } else {
                cacheKey = itemId + "#step_" + step + "#f_" + animFrame;
            }
        } else if (isCharPhase) {
            cacheKey = itemId + "#char_" + (int)(charAlpha * 8);
        } else {
            cacheKey = itemId + (isSoaked ? "#wrapped_soaked" : "#wrapped");
        }

        TextureHolder cached = TEXTURE_CACHE.get(cacheKey);
        if (cached != null) return cached;

        try {
            TextureAtlasSprite sprite = model.getParticleTexture();
            BufferedImage baseImg = loadBaseWeaponImage(item, sprite);
            if (baseImg == null) return null;

            BufferedImage clothImg = getClothImage();
            if (clothImg == null) return null;

            float effectiveProg = (isIgnited || isCharPhase) ? (step / 10.0F) : -1.0F;
            ProcessedImages processed = processClothAndBurn(baseImg, clothImg, effectiveProg, animFrame, charAlpha, isIgnited && isSoaked);

            // Base diffuse composite
            DynamicTexture dynTex = new DynamicTexture(processed.base);
            String dynName = "mwccf_fw_b_" + Math.abs(cacheKey.hashCode());
            ResourceLocation baseLoc = Minecraft.getMinecraft().getTextureManager()
                    .getDynamicTextureLocation(dynName, dynTex);

            // Emissive glow texture (only non-zero pixels glow in darkness)
            ResourceLocation emissiveLoc = null;
            if (isIgnited && processed.emissive != null) {
                DynamicTexture emTex = new DynamicTexture(processed.emissive);
                String emName = "mwccf_fw_e_" + Math.abs(cacheKey.hashCode());
                emissiveLoc = Minecraft.getMinecraft().getTextureManager()
                        .getDynamicTextureLocation(emName, emTex);
            }

            TextureHolder holder = new TextureHolder(baseLoc, emissiveLoc, processed.burningVoxels);
            TEXTURE_CACHE.put(cacheKey, holder);
            return holder;
        } catch (Throwable t) {
            System.err.println("[FireWeaponTextureManager] Error generating texture for " + itemId + ": " + t.getMessage());
            return null;
        }
    }

    private static BufferedImage getClothImage() {
        if (cachedClothImage != null) return cachedClothImage;

        // 1. Try Minecraft ResourceManager
        try {
            IResource res = Minecraft.getMinecraft().getResourceManager().getResource(RES_CLOTH_WEAPON);
            if (res != null) {
                try (InputStream in = res.getInputStream()) {
                    BufferedImage raw = ImageIO.read(in);
                    if (raw != null) {
                        cachedClothImage = toArgb(raw);
                        return cachedClothImage;
                    }
                }
            }
        } catch (Throwable ignored) {}

        // 2. Direct filesystem fallback
        File[] fileCandidates = new File[] {
                new File("C:/Users/reizv/Documents/mwccf/cloth_weapon.png"),
                new File("cloth_weapon.png"),
                new File("src/main/resources/assets/mwccf/textures/items/cloth_weapon.png")
        };
        for (File f : fileCandidates) {
            try {
                if (f.exists() && f.isFile()) {
                    BufferedImage raw = ImageIO.read(f);
                    if (raw != null) {
                        cachedClothImage = toArgb(raw);
                        return cachedClothImage;
                    }
                }
            } catch (Throwable ignored) {}
        }

        // 3. Fallback procedural olive camo cloth pattern
        BufferedImage fallback = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int pattern = ((x * 3 + y * 7) % 5 < 2) ? 91 : 67;
                int r = pattern;
                int g = (int) (pattern * 1.15F);
                int b = (int) (pattern * 0.65F);
                fallback.setRGB(x, y, (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
        cachedClothImage = fallback;
        return cachedClothImage;
    }

    private static BufferedImage loadBaseWeaponImage(Item item, TextureAtlasSprite sprite) {
        // 1. Try loading directly from sprite iconName
        if (sprite != null && sprite.getIconName() != null) {
            String name = sprite.getIconName();
            String domain = "minecraft";
            String path = name;
            if (name.contains(":")) {
                String[] parts = name.split(":");
                domain = parts[0];
                path = parts[1];
            }

            ResourceLocation[] attempts = new ResourceLocation[] {
                    new ResourceLocation(domain, "textures/" + path + ".png"),
                    new ResourceLocation(domain, "textures/items/" + path + ".png"),
                    new ResourceLocation(domain, "textures/item/" + path + ".png")
            };

            for (ResourceLocation loc : attempts) {
                try {
                    IResource res = Minecraft.getMinecraft().getResourceManager().getResource(loc);
                    if (res != null) {
                        try (InputStream in = res.getInputStream()) {
                            BufferedImage img = ImageIO.read(in);
                            if (img != null) return toArgb(img);
                        }
                    }
                } catch (Throwable ignored) {}
            }

            // 2. Fallback: extract directly from the TextureAtlasSprite's uncompressed frame data
            try {
                int w = sprite.getIconWidth();
                int h = sprite.getIconHeight();
                int[][] frames = sprite.getFrameTextureData(0);
                if (frames != null && frames.length > 0 && frames[0] != null) {
                    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    img.setRGB(0, 0, w, h, frames[0], 0, w);
                    return img;
                }
            } catch (Throwable ignored) {}
        }

        // 3. Fallback: try item registry name
        if (item != null && item.getRegistryName() != null) {
            ResourceLocation reg = item.getRegistryName();
            ResourceLocation[] attempts = new ResourceLocation[] {
                    new ResourceLocation(reg.getNamespace(), "textures/items/" + reg.getPath() + ".png"),
                    new ResourceLocation(reg.getNamespace(), "textures/item/" + reg.getPath() + ".png"),
                    new ResourceLocation(reg.getNamespace(), "textures/" + reg.getPath() + ".png")
            };
            for (ResourceLocation loc : attempts) {
                try {
                    IResource res = Minecraft.getMinecraft().getResourceManager().getResource(loc);
                    if (res != null) {
                        try (InputStream in = res.getInputStream()) {
                            BufferedImage img = ImageIO.read(in);
                            if (img != null) return toArgb(img);
                        }
                    }
                } catch (Throwable ignored) {}
            }
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

    /**
     * Composites cloth_weapon.png and creates both:
     * 1. Full composite image for normal ambient lighting pass.
     * 2. Emissive image containing ONLY the glowing pixels (for 240/240 glow in the dark).
     *
     * @param animFrame animation tick frame (0..23) for calm, organic, silky-smooth flame shimmer.
     * @param charAlpha  1.0 = fully visible char, <1.0 = fading (char phase only). Ignored when burning.
     */
    private static ProcessedImages processClothAndBurn(BufferedImage base, BufferedImage cloth, float burnProg, int animFrame, float charAlpha, boolean soakedMode) {
        int w = base.getWidth();
        int h = base.getHeight();
        BufferedImage resultBase = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        BufferedImage resultEmissive = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        List<FireWeaponSparkManager.BurningVoxel> burningVoxels = new ArrayList<>();

        int[] pixels = new int[w * h];
        int[] emissivePixels = new int[w * h]; // defaults to all 0 (transparent)
        base.getRGB(0, 0, w, h, pixels, 0, w);

        int cw = cloth.getWidth();
        int ch = cloth.getHeight();
        boolean isIgnited = burnProg >= 0.0F;

        // Smooth cycle phase: 0.0 to 2*PI across 24 frames
        float phase = animFrame * (float) (Math.PI * 2.0 / 24.0);

        // 1. Find pixel bounding box of non-transparent weapon pixels
        int minX = w, maxX = 0, minY = h, maxY = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int alpha = (pixels[y * w + x] >> 24) & 0xFF;
                if (alpha > 25) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            resultBase.setRGB(0, 0, w, h, pixels, 0, w);
            return new ProcessedImages(resultBase, null, burningVoxels);
        }

        float spanX = Math.max(1, maxX - minX);
        float spanY = Math.max(1, maxY - minY);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int idx = y * w + x;
                int orig = pixels[idx];
                int alpha = (orig >> 24) & 0xFF;
                if (alpha <= 25) {
                    pixels[idx] = 0;
                    emissivePixels[idx] = 0;
                    continue;
                }

                float normX = (float) (x - minX) / spanX;
                float normY = 1.0F - (float) (y - minY) / spanY; // 0.0 at bottom, 1.0 at top

                // Determine orientation: diagonal, vertical, or horizontal
                float axis, perp;
                if (spanX > spanY * 1.5F) {
                    axis = normX;
                    perp = normY - 0.5F;
                } else if (spanY > spanX * 1.5F) {
                    axis = normY;
                    perp = normX - 0.5F;
                } else {
                    // Standard diagonal weapon (bottom-left = hilt, top-right = tip)
                    axis = (normX + normY) * 0.5F;
                    perp = (normX - normY) * 0.5F;
                }

                // Keep the handle clear while extending the wrap close to the blade tip.
                if (axis >= 0.34F && axis <= 0.97F) {
                    float t = (axis - 0.34F) / (0.97F - 0.34F); // 0.0 at wrap base, 1.0 at wrap top

                    // Spiral coils winding around the blade
                    float coils = 6.0F;
                    float wrapPos = t * coils + perp * 2.2F;
                    float coilPhase = wrapPos - (float) Math.floor(wrapPos);

                    // 85% bandage strip, 15% crease/seam
                    if (coilPhase < 0.85F) {
                        // Sample cloth texture directly from cloth_weapon.png
                        int cx = Math.abs(x) % cw;
                        int cy = Math.abs(y) % ch;
                        int clothPixel = cloth.getRGB(cx, cy);

                        int cr = (clothPixel >> 16) & 0xFF;
                        int cg = (clothPixel >> 8) & 0xFF;
                        int cb = clothPixel & 0xFF;

                        // Cylindrical curvature highlight and crease shading
                        float normPhase = coilPhase / 0.85F; // 0.0 to 1.0 across the strip
                        float shade = 0.82F + 0.32F * (float) Math.sin(normPhase * Math.PI);
                        if (normPhase < 0.15F) {
                            shade *= (0.72F + 0.28F * (normPhase / 0.15F));
                        }

                        cr = Math.min(255, (int) (cr * shade));
                        cg = Math.min(255, (int) (cg * shade));
                        cb = Math.min(255, (int) (cb * shade));

                        if (isIgnited) {
                            float x0 = (float) x / (float) w;
                            float x1 = (float) (x + 1) / (float) w;
                            float y0 = (float) (h - 1 - y) / (float) h;
                            float y1 = (float) (h - y) / (float) h;
                            float z0 = 0.46875F;
                            float z1 = 0.53125F;

                            if (soakedMode) {
                                // --- SOAKED MODE: THE ENTIRE WRAPPED CLOTH BURNS SIMULTANEOUSLY! ---
                                // All cloth pixels are active fire with particles emitting across the entire area
                                burningVoxels.add(new FireWeaponSparkManager.BurningVoxel(x0, x1, y0, y1, z0, z1, true));

                                float flameNoise = (float) Math.sin(x * 0.75F + y * 0.85F + phase * 1.5F) * 0.5F
                                                 + (float) Math.cos(x * 0.65F - y * 0.75F - phase * 1.2F) * 0.5F;
                                float colorCycle = (float) Math.sin(phase + (normX * 3.5F + normY * 4.5F) + flameNoise * 1.2F);

                                cr = 255;
                                if (colorCycle > 0.15F) {
                                    // Incandescent radiant yellow/white fire
                                    float k = (colorCycle - 0.15F) / 0.85F;
                                    cg = (int) (190 + 55 * k); // 190 -> 245
                                    cb = (int) (45 + 75 * k);  // 45 -> 120
                                } else if (colorCycle > -0.35F) {
                                    // Blazing bright orange fire
                                    float k = (colorCycle + 0.35F) / 0.50F;
                                    cg = (int) (125 + 65 * k); // 125 -> 190
                                    cb = (int) (15 + 30 * k);  // 15 -> 45
                                } else {
                                    // Molten ruby-red flame base
                                    float k = (colorCycle + 1.0F) / 0.65F;
                                    cg = (int) (70 + 55 * k);  // 70 -> 125
                                    cb = (int) (10 + 10 * k);  // 10 -> 20
                                }

                                // 100% full emissive glow across the entire cloth in the dark
                                int emAlpha = 255;
                                emissivePixels[idx] = (emAlpha << 24) | (cr << 16) | (cg << 8) | cb;
                            } else {
                                // Section progress: burns from blade tip (t=1) down toward hilt (t=0)
                                float burnDist = (1.0F - t); // 0.0 at top, 1.0 at base
                                float waveHalf = 0.09F;

                                // Gentle, silky-smooth low-frequency harmonic waves (NO fast jumping!)
                                float flameNoise = (float) Math.sin(x * 0.45F + y * 0.55F + phase) * 0.45F
                                                 + (float) Math.cos(x * 0.55F - y * 0.45F - phase) * 0.45F;
                                float waveShift = (float) Math.sin(phase + (normX + normY) * 2.5F) * 0.02F;
                                float diff = burnDist - burnProg + flameNoise * 0.035F + waveShift;

                                if (diff < -waveHalf) {
                                    // --- BURNT ASH & BREATHING EMBERS ---
                                    int ashR = Math.max(16, Math.min(36, (int) (22 + flameNoise * 5)));
                                    int ashG = Math.max(14, Math.min(30, (int) (18 + flameNoise * 4)));
                                    int ashB = Math.max(14, Math.min(28, (int) (16 + flameNoise * 4)));

                                    float wake = -diff - waveHalf;
                                    if (wake < 0.22F) {
                                        burningVoxels.add(new FireWeaponSparkManager.BurningVoxel(x0, x1, y0, y1, z0, z1, false));
                                        float emberDistT = 1.0F - (wake / 0.22F);
                                        // Slow, calm breathing ember pulse
                                        float emberBreath = 0.50F + 0.50F * (float) Math.sin(phase + x * 0.6F + y * 0.8F);
                                        float emberHeat = emberDistT * emberBreath;

                                        int eR = (int) (230 * emberHeat);
                                        int eG = (int) ((80 + 35 * (float) Math.sin(phase * 0.5F + x * 0.4F)) * emberHeat);
                                        int eB = (int) (15 * emberHeat);

                                        cr = Math.min(255, ashR + eR);
                                        cg = Math.min(255, ashG + eG);
                                        cb = Math.min(255, ashB + eB);

                                        // Emissive glow for living embers in darkness
                                        int emAlpha = (int) (245 * emberHeat);
                                        if (emAlpha > 15) {
                                            emissivePixels[idx] = (emAlpha << 24) | (cr << 16) | (cg << 8) | cb;
                                        }
                                    } else {
                                        // Occasional calm ember spark in cold ash
                                        float sparkChance = (float) Math.sin(x * 2.2F + y * 1.9F);
                                        if (sparkChance > 0.72F) {
                                            burningVoxels.add(new FireWeaponSparkManager.BurningVoxel(x0, x1, y0, y1, z0, z1, false));
                                            float sparkBlink = 0.5F + 0.5F * (float) Math.sin(phase + x * 1.2F);
                                            cr = Math.min(255, ashR + (int) (160 * sparkBlink));
                                            cg = Math.min(255, ashG + (int) (55 * sparkBlink));
                                            cb = ashB;

                                            int emAlpha = (int) (190 * sparkBlink);
                                            if (emAlpha > 15) {
                                                emissivePixels[idx] = (emAlpha << 24) | (cr << 16) | (cg << 8) | cb;
                                            }
                                        } else {
                                            cr = ashR; cg = ashG; cb = ashB;
                                        }
                                    }
                                } else if (Math.abs(diff) <= waveHalf) {
                                    // --- ACTIVE BURNING FRONT (Gentle iridescent shimmer / плавный перелив огня) ---
                                    burningVoxels.add(new FireWeaponSparkManager.BurningVoxel(x0, x1, y0, y1, z0, z1, true));
                                    float frontT = 1.0F - Math.abs(diff) / waveHalf;
                                    float colorCycle = (float) Math.sin(phase + (normX - normY) * 1.8F + flameNoise * 0.6F);

                                    cr = 255;
                                    if (colorCycle > 0.25F) {
                                        // Incandescent gold/yellow heat peak
                                        float k = (colorCycle - 0.25F) / 0.75F;
                                        cg = (int) (175 + 60 * k); // 175 -> 235
                                        cb = (int) (35 + 50 * k);  // 35 -> 85 (warm glowing core)
                                    } else if (colorCycle > -0.25F) {
                                        // Rich blazing orange flame
                                        float k = (colorCycle + 0.25F) / 0.50F;
                                        cg = (int) (115 + 60 * k); // 115 -> 175
                                        cb = (int) (15 + 20 * k);  // 15 -> 35
                                    } else {
                                        // Deep glowing molten ruby-crimson
                                        float k = (colorCycle + 1.0F) / 0.75F;
                                        cg = (int) (65 + 50 * k);  // 65 -> 115
                                        cb = (int) (10 + 8 * k);   // 10 -> 18
                                    }

                                    // Emissive glow: Active fire front shines at maximum brightness in the dark!
                                    int emAlpha = (int) (225 + 30 * frontT);
                                    emissivePixels[idx] = (emAlpha << 24) | (cr << 16) | (cg << 8) | cb;
                                } else {
                                    // --- UNBURNT CLOTH WITH HEAT LICK ---
                                    float preDist = diff - waveHalf;
                                    if (preDist < 0.12F) {
                                        float heatT = (1.0F - preDist / 0.12F);
                                        float heatPulse = 0.65F + 0.35F * (float) Math.sin(phase - (normX + normY) * 3.0F);
                                        cr = Math.min(255, (int) (cr + 110 * heatT * heatPulse));
                                        cg = Math.min(255, (int) (cg + 35 * heatT * heatPulse));

                                        int emAlpha = (int) (130 * heatT * heatPulse);
                                        if (emAlpha > 15) {
                                            emissivePixels[idx] = (emAlpha << 24) | (cr << 16) | (cg << 8) | cb;
                                        }
                                    }
                                }
                            }
                        }

                        pixels[idx] = (alpha << 24) | (cr << 16) | (cg << 8) | cb;
                        // Apply char-phase alpha fade: blend cloth color with base weapon pixel so weapon stays fully opaque!
                        if (charAlpha < 1.0F) {
                            int baseR = (orig >> 16) & 0xFF;
                            int baseG = (orig >> 8) & 0xFF;
                            int baseB = orig & 0xFF;
                            int blendedR = (int) (cr * charAlpha + baseR * (1.0F - charAlpha));
                            int blendedG = (int) (cg * charAlpha + baseG * (1.0F - charAlpha));
                            int blendedB = (int) (cb * charAlpha + baseB * (1.0F - charAlpha));
                            pixels[idx] = (alpha << 24) | (blendedR << 16) | (blendedG << 8) | blendedB;
                        }
                    } else {
                        // Crease/seam: dark shadow over the blade
                        int origR = (orig >> 16) & 0xFF;
                        int origG = (orig >> 8) & 0xFF;
                        int origB = orig & 0xFF;
                        int seamR = (int) (origR * 0.45F);
                        int seamG = (int) (origG * 0.45F);
                        int seamB = (int) (origB * 0.45F);
                        if (isIgnited) {
                            float burnDist = (1.0F - t);
                            if (burnDist < burnProg) {
                                // Seam is dark charred crack with breathing ember trace
                                float seamEmber = 0.5F + 0.5F * (float) Math.sin(phase + x * 0.8F);
                                seamR = (int) (18 + 40 * seamEmber);
                                seamG = (int) (12 + 14 * seamEmber);
                                seamB = 10;

                                int emAlpha = (int) (150 * seamEmber);
                                if (emAlpha > 15) {
                                    emissivePixels[idx] = (emAlpha << 24) | (seamR << 16) | (seamG << 8) | seamB;
                                }
                            }
                        }
                        if (charAlpha < 1.0F) {
                            int blendedR = (int) (seamR * charAlpha + origR * (1.0F - charAlpha));
                            int blendedG = (int) (seamG * charAlpha + origG * (1.0F - charAlpha));
                            int blendedB = (int) (seamB * charAlpha + origB * (1.0F - charAlpha));
                            pixels[idx] = (alpha << 24) | (blendedR << 16) | (blendedG << 8) | blendedB;
                        } else {
                            pixels[idx] = (alpha << 24) | (seamR << 16) | (seamG << 8) | seamB;
                        }
                    }
                }
            }
        }

        resultBase.setRGB(0, 0, w, h, pixels, 0, w);
        if (isIgnited) {
            resultEmissive.setRGB(0, 0, w, h, emissivePixels, 0, w);
            return new ProcessedImages(resultBase, resultEmissive, burningVoxels);
        } else {
            return new ProcessedImages(resultBase, null, burningVoxels);
        }
    }
}
