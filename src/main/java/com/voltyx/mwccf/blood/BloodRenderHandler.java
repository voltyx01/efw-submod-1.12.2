package com.voltyx.mwccf.blood;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.Random;

/**
 * Applies blood overlay via multitexturing on texture unit 2.
 *
 * Unit 0 = skin / armour texture
 * Unit 1 = lightmap (MC default)
 * Unit 2 = blood splatter (we control)
 *
 * Because the setup happens inside RenderPlayerEvent.Pre / Post,
 * ALL geometry rendered for the player (skin, vanilla armour, GeoArmor,
 * SurvivalInstinct armour, custom layers, etc.) automatically gets blood on it.
 *
 * UV coordinates are generated procedurally via GL_OBJECT_LINEAR so blood
 * maps consistently to 3-D body-part positions regardless of model geometry.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
public class BloodRenderHandler {

    private static final float T1 = 0.33f;
    private static final float T2 = 0.66f;

    // Blood texture unit index (0-based offset from GL_TEXTURE0)
    private static final int BLOOD_TEX_UNIT_OFFSET = 2;

    private static ResourceLocation[] bloodTextures = null;
    private static boolean initDone = false;

    // -------------------------------------------------------------------------
    // Client tick: decay blood for every visible player
    // -------------------------------------------------------------------------

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;
        for (EntityPlayer player : mc.world.playerEntities) {
            BloodManager.tickDecay(player);
        }
    }

    // -------------------------------------------------------------------------
    // Pre: activate blood texture on unit 2
    // -------------------------------------------------------------------------

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        AbstractClientPlayer player = event.getEntityPlayer();
        float level = BloodManager.getBloodLevel(player.getUniqueID());
        if (level <= 0.001f) return;

        if (!initDone) initTextures();
        if (bloodTextures == null) return;

        // Choose texture by level band
        ResourceLocation texLoc;
        if (level <= T1) {
            texLoc = bloodTextures[0];
        } else if (level <= T2) {
            texLoc = bloodTextures[1];
        } else {
            texLoc = bloodTextures[2];
        }

        ITextureObject texObj = Minecraft.getMinecraft().getTextureManager().getTexture(texLoc);
        if (texObj == null) return;
        int texId = texObj.getGlTextureId();
        if (texId <= 0) return;

        // ---- Activate texture unit 2 ----------------------------------------
        int bloodUnit = OpenGlHelper.GL_TEXTURE0 + BLOOD_TEX_UNIT_OFFSET;
        GlStateManager.setActiveTexture(bloodUnit);
        GlStateManager.enableTexture2D();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        // Tile the texture so UV out-of-range geometry (e.g. custom bones) still gets blood
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        // ---- Texgen: GL_OBJECT_LINEAR using model-space coords ---------------
        // Calibrated for standard ModelBiped vertex ranges:
        //   Head local Y: -8 .. 0   → V: 0.00 .. 0.25  (unique to head, = face area)
        //   Body/arm/leg local Y: 0 .. 12 → V: 0.25 .. 0.625
        //   Local X: -6 .. 6 → U: 0 .. 1  (centred)
        GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
        GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);

        // S = X / 12 + 0.5
        FloatBuffer sPlane = BufferUtils.createFloatBuffer(4);
        sPlane.put(1.0f / 12.0f).put(0.0f).put(0.0f).put(0.5f).flip();
        GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, sPlane);

        // T = Y / 32 + 0.25
        FloatBuffer tPlane = BufferUtils.createFloatBuffer(4);
        tPlane.put(0.0f).put(1.0f / 32.0f).put(0.0f).put(0.25f).flip();
        GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, tPlane);

        GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
        GL11.glEnable(GL11.GL_TEXTURE_GEN_T);

        // ---- Combine: blend blood over the already-lit skin/armour -----------
        // result_rgb = blood_rgb * blood_alpha + prev_rgb * (1 - blood_alpha)
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_COMBINE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_COMBINE_RGB, GL11.GL_INTERPOLATE);

        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_SOURCE0_RGB,  GL11.GL_TEXTURE);    // blood colour
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);

        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_SOURCE1_RGB,  GL11.GL_PREVIOUS);   // lit surface
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);

        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_SOURCE2_RGB,  GL11.GL_TEXTURE);    // blood alpha = mask
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_OPERAND2_RGB, GL11.GL_SRC_ALPHA);

        // Pass through alpha unchanged
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_SOURCE0_ALPHA,  GL11.GL_PREVIOUS);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);

        // Back to unit 0 so normal rendering continues unaffected
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE0);
    }

    // -------------------------------------------------------------------------
    // Post: deactivate blood texture unit
    // -------------------------------------------------------------------------

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        float level = BloodManager.getBloodLevel(event.getEntityPlayer().getUniqueID());
        if (level <= 0.001f) return;

        int bloodUnit = OpenGlHelper.GL_TEXTURE0 + BLOOD_TEX_UNIT_OFFSET;
        GlStateManager.setActiveTexture(bloodUnit);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        // Restore default combine mode so other rendering isn't affected
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE0);
    }

    // -------------------------------------------------------------------------
    // Blood texture generation
    // -------------------------------------------------------------------------

    /**
     * Generates three 256×256 ARGB splatter textures designed for object-space UV projection.
     *
     * Texture V layout (via tPlane T = Y/32 + 0.25):
     *   Rows   0– 63  (V 0.00–0.25)  → HEAD  – dense blood (face, top)
     *   Rows  64–159  (V 0.25–0.625) → BODY / ARMS / LEGS – scattered blood
     *   Rows 160–255  (V 0.625–1.0)  → spare – very sparse / empty
     */
    private static void initTextures() {
        initDone = true;
        try {
            bloodTextures = new ResourceLocation[3];
            for (int i = 0; i < 3; i++) {
                bloodTextures[i] = buildBloodTexture(i + 1);
            }
        } catch (Throwable e) {
            bloodTextures = null;
        }
    }

    private static ResourceLocation buildBloodTexture(int level) {
        int W = 256, H = 256;
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Random rng = new Random(level * 0xDEADC0DEL);

        // Blood colour: dark crimson
        int r = 138, g = 3, b = 8;

        // HEAD zone (rows 0–63) – concentrated around centre-top (face)
        int headDrops = 5 + level * 5;           // 10 / 15 / 20
        int headAlpha = 120 + level * 30;         // 150 / 180 / 210
        for (int i = 0; i < headDrops; i++) {
            // Gaussian centre around col 128 (U=0.5), tight spread
            int cx = 128 + (int)(rng.nextGaussian() * 38);
            int cy = 5  + rng.nextInt(55);
            int rad = 3 + rng.nextInt(5 + level);
            paintBlob(img, cx, cy, rad, r, g, b, headAlpha, rng);
        }

        // BODY zone (rows 64–159) – scattered across full width
        int bodyDrops = 3 + level * 4;           // 7 / 11 / 15
        int bodyAlpha = 90  + level * 20;         // 110 / 130 / 150
        for (int i = 0; i < bodyDrops; i++) {
            int cx = 20 + rng.nextInt(216);
            int cy = 64 + rng.nextInt(95);
            int rad = 2 + rng.nextInt(4 + level);
            paintBlob(img, cx, cy, rad, r, g, b, bodyAlpha, rng);
        }

        // LOWER zone (rows 160–255) – very sparse
        int lowerDrops = level;                   // 1 / 2 / 3
        int lowerAlpha = 70 + level * 15;
        for (int i = 0; i < lowerDrops; i++) {
            int cx = 30 + rng.nextInt(196);
            int cy = 160 + rng.nextInt(90);
            int rad = 2 + rng.nextInt(4);
            paintBlob(img, cx, cy, rad, r, g, b, lowerAlpha, rng);
        }

        DynamicTexture dt = new DynamicTexture(img);
        return Minecraft.getMinecraft().getTextureManager()
                .getDynamicTextureLocation("mwccf_blood_proj_" + level, dt);
    }

    /** Paints a soft circular blood blob with quadratic alpha falloff. */
    private static void paintBlob(BufferedImage img, int cx, int cy, int radius,
                                   int r, int g, int b, int baseAlpha, Random rng) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist > radius) continue;
                int nx = cx + dx, ny = cy + dy;
                if (nx < 0 || nx >= img.getWidth() || ny < 0 || ny >= img.getHeight()) continue;

                float edge  = 1.0f - (dist / radius);
                int   alpha = (int)(baseAlpha * edge * edge) + rng.nextInt(15) - 7;
                alpha = Math.max(0, Math.min(255, alpha));

                int cr = Math.max(0, Math.min(255, r + rng.nextInt(26) - 13));
                int cg = Math.max(0, Math.min(255, g + rng.nextInt(8)));
                int cb = Math.max(0, Math.min(255, b + rng.nextInt(14)));
                int argb = (alpha << 24) | (cr << 16) | (cg << 8) | cb;

                // Keep brightest alpha per pixel
                if (((img.getRGB(nx, ny) >> 24) & 0xFF) < alpha) {
                    img.setRGB(nx, ny, argb);
                }
            }
        }
    }
}
