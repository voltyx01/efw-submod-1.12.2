package com.voltyx.mwccf.blood;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Renders procedurally-generated blood splatter overlays on top of player armor/skin.
 *
 * UV coordinates follow the standard Minecraft 64×64 skin layout so blood
 * appears on the correct body parts (face has higher density).
 *
 * Three intensity textures are blended in progressively as blood level rises:
 *   0.00–0.33 → light  (texture 0)
 *   0.33–0.66 → + medium (texture 1)
 *   0.66–1.00 → + heavy  (texture 2)
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
public class LayerBloodOverlay implements LayerRenderer<AbstractClientPlayer> {

    // Threshold boundaries for the three blood layers
    private static final float T1 = 0.33f;
    private static final float T2 = 0.66f;

    // Lazily-initialized dynamic textures (generated on GL thread at first render)
    private static ResourceLocation[] bloodTextures = null;
    private static boolean initDone = false;

    private final RenderPlayer renderer;

    public LayerBloodOverlay(RenderPlayer renderer) {
        this.renderer = renderer;
    }

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
    // Layer rendering
    // -------------------------------------------------------------------------

    @Override
    public void doRenderLayer(AbstractClientPlayer player,
                              float limbSwing, float limbSwingAmount, float partialTicks,
                              float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        // Skip rendering in special contexts
        if (efw.util.RenderContext.isRenderingPlayerInSevenScreen ||
                com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            return;
        }
        // Skip for local player in first-person view
        if (player == Minecraft.getMinecraft().player &&
                Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            return;
        }

        float level = BloodManager.getBloodLevel(player.getUniqueID());
        if (level <= 0.001f) return;

        // Initialize textures lazily on GL thread
        if (!initDone) {
            initTextures();
        }
        if (bloodTextures == null) return;

        ModelBiped mainModel = (ModelBiped) renderer.getMainModel();

        // Polygon offset pushes blood fragments slightly toward camera,
        // putting them in front of armor geometry without disabling depth test.
        // This means: blood renders ON TOP of armor, but NOT through blocks/walls.
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-2.0f, -64.0f); // push blood in front of armor depth

        try {
            // Layer 0: light blood, fades in 0→T1
            float a0 = Math.min(1.0f, level / T1);
            renderLayer(player, mainModel, limbSwing, limbSwingAmount,
                    ageInTicks, netHeadYaw, headPitch, scale, bloodTextures[0], a0);

            // Layer 1: medium blood, fades in T1→T2
            if (level > T1) {
                float a1 = Math.min(1.0f, (level - T1) / (T2 - T1));
                renderLayer(player, mainModel, limbSwing, limbSwingAmount,
                        ageInTicks, netHeadYaw, headPitch, scale, bloodTextures[1], a1);
            }

            // Layer 2: heavy blood, fades in T2→1.0
            if (level > T2) {
                float a2 = Math.min(1.0f, (level - T2) / (1.0f - T2));
                renderLayer(player, mainModel, limbSwing, limbSwingAmount,
                        ageInTicks, netHeadYaw, headPitch, scale, bloodTextures[2], a2);
            }
        } finally {
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(0.0f, 0.0f);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private void renderLayer(AbstractClientPlayer player, ModelBiped model,
                              float limbSwing, float limbSwingAmount, float ageInTicks,
                              float netHeadYaw, float headPitch, float scale,
                              ResourceLocation tex, float alpha) {
        if (alpha <= 0.001f) return;
        renderer.bindTexture(tex);
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        model.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    // -------------------------------------------------------------------------
    // Procedural texture generation (standard 64×64 Minecraft skin UV layout)
    // -------------------------------------------------------------------------

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

    /**
     * Builds a 64×64 ARGB texture with blood drops at body-part UV regions.
     *
     * Standard 64×64 skin UV regions referenced:
     *   Face front     : (8,8)  8×8
     *   Head top       : (8,0)  8×8
     *   Body front     : (20,20) 8×12
     *   Body back      : (32,20) 8×12
     *   Right arm front: (44,20) 4×12
     *   Left arm front : (36,52) 4×12
     *   Right leg front: (4,20)  4×12
     *   Left leg front : (20,52) 4×12
     *
     * @param level 1=light, 2=medium, 3=heavy
     */
    private static ResourceLocation buildBloodTexture(int level) {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        // Deterministic seed per level so pattern is always the same
        Random rng = new Random(level * 0xDEADBEEFL);

        // Base blood colour: dark crimson  (R=140 G=0 B=12)
        int r = 140, g = 0, b = 12;
        // Alpha rises with level so each layer looks independent
        int baseAlpha = 160 + level * 25; // 185 / 210 / 235

        // --- Head: face gets the most blood ---
        // Fewer drops so blood is subtle even at max level
        int face    = 2 + level * 3;  // 5 / 8 / 11 drops
        int faceTop = 1 + level;      // 2 / 3 /  4 drops on top of head
        paintDrops(img, rng, 8,  8, 8, 8,  face,    r, g, b, baseAlpha);
        paintDrops(img, rng, 8,  0, 8, 8,  faceTop, r, g, b, baseAlpha - 40);

        // --- Torso ---
        int body = 1 + level * 2; // 3 / 5 / 7
        paintDrops(img, rng, 20, 20, 8, 12, body,     r, g, b, baseAlpha - 30);
        paintDrops(img, rng, 32, 20, 8, 12, body - 1, r, g, b, baseAlpha - 30);

        // --- Arms ---
        int arm = 1 + level; // 2 / 3 / 4
        paintDrops(img, rng, 44, 20, 4, 12, arm, r, g, b, baseAlpha - 30);
        paintDrops(img, rng, 36, 52, 4, 12, arm, r, g, b, baseAlpha - 30);

        // --- Legs ---
        int leg = 1 + level; // 2 / 3 / 4
        paintDrops(img, rng, 4,  20, 4, 12, leg, r, g, b, baseAlpha - 40);
        paintDrops(img, rng, 20, 52, 4, 12, leg, r, g, b, baseAlpha - 40);

        DynamicTexture dt = new DynamicTexture(img);
        return Minecraft.getMinecraft().getTextureManager()
                .getDynamicTextureLocation("mwccf_blood_" + level, dt);
    }

    /**
     * Paints small round blood drops/smears inside a UV rectangle.
     *
     * @param img       target image
     * @param rng       random source
     * @param sx,sy     top-left UV pixel of the region
     * @param rw,rh     width/height of the UV region
     * @param count     number of drops
     * @param r,g,b     RGB colour components
     * @param baseAlpha base alpha (150-255)
     */
    private static void paintDrops(BufferedImage img, Random rng,
                                    int sx, int sy, int rw, int rh, int count,
                                    int r, int g, int b, int baseAlpha) {
        for (int i = 0; i < count; i++) {
            int px = sx + rng.nextInt(Math.max(1, rw));
            int py = sy + rng.nextInt(Math.max(1, rh));

            // Most drops are 1-pixel; occasionally a 1-radius circle for variety
            int radius = (rng.nextInt(4) == 0) ? 1 : 0;
            int alpha  = Math.min(255, Math.max(0, baseAlpha - 30 + rng.nextInt(60)));

            // Vary colour slightly (darker/lighter blood)
            int cr = clamp(r + rng.nextInt(40) - 20);
            int cg = clamp(g + rng.nextInt(10));
            int cb = clamp(b + rng.nextInt(20) - 10);
            int argb = (alpha << 24) | (cr << 16) | (cg << 8) | cb;

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (dx * dx + dy * dy <= radius * radius + 1) {
                        int nx = px + dx, ny = py + dy;
                        if (nx >= 0 && nx < 64 && ny >= 0 && ny < 64) {
                            // Blend with any existing pixel (accumulate blood)
                            int existing = img.getRGB(nx, ny);
                            int ea = (existing >> 24) & 0xFF;
                            if (ea == 0) {
                                img.setRGB(nx, ny, argb);
                            } else {
                                // Simple max-alpha compositing
                                int na = Math.min(255, ea + ((alpha * 60) >> 8));
                                img.setRGB(nx, ny, (existing & 0x00FFFFFF) | (na << 24));
                            }
                        }
                    }
                }
            }
        }
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }
}
