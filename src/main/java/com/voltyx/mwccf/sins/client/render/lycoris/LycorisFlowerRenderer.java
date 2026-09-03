package com.voltyx.mwccf.render.lycoris;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Renders a {@link ModelLycoris} standalone (no Entity needed) for the skill-tree GUI.
 *
 * Responsibilities:
 *  - idle "breathing" sway of the whole plant
 *  - per-floret blackening (0 = healthy, 1 = fully charred) driven by branch progress
 *  - a smoldering "ember" overlay: a fixed random subset of each floret's smallest
 *    child cubes pulse warm orange once that floret has started burning
 *
 * NOTE ON THE BURN TECHNIQUE:
 * This model reuses only a handful of texture regions across all six florets (it's
 * a flat palette texture, not a hand-painted one), so darkening texture pixels would
 * darken every floret at once. Instead we tint per-floret via glColor before calling
 * that floret's render() (color applies to the whole subtree since it's plain
 * immediate-mode GL), and fake "glowing pixels" by re-rendering a handful of that
 * floret's own tiny leaf cubes a second time with additive orange blending. This
 * reads as small smoldering hot-spots without needing a second texture.
 */
public class LycorisFlowerRenderer {

    private static final int FLORET_COUNT = 6;
    // fraction of each floret's leaf cubes that can ever glow as embers
    private static final float EMBER_FRACTION = 0.12f;
    // a floret must be at least this charred before embers start appearing
    private static final float EMBER_START_THRESHOLD = 0.25f;

    private final ModelLycoris model = new ModelLycoris();
    private final ResourceLocation texture;

    // 0..1 per floret, 0 = fresh, 1 = fully black
    private final float[] targetProgress = new float[FLORET_COUNT];
    private final float[] displayProgress = new float[FLORET_COUNT];

    // precomputed once: every leaf ModelRenderer (no children) under each floret
    private final List<List<ModelRenderer>> floretLeaves = new ArrayList<>(FLORET_COUNT);
    // stable random subset of the above, chosen once so embers don't "jump around"
    private final List<List<ModelRenderer>> floretEmbers = new ArrayList<>(FLORET_COUNT);
    // a per-ember random phase so they don't all pulse in sync
    private final List<float[]> floretEmberPhases = new ArrayList<>(FLORET_COUNT);

    // idle sway timer, advanced every render call by partialTicks-corrected delta
    private float idleTime = 0f;

    public LycorisFlowerRenderer(ResourceLocation texture) {
        this.texture = texture;
        ModelRenderer[] florets = combinedFlorets();
        Random rng = new Random(1234567L); // fixed seed -> stable across restarts
        for (ModelRenderer floret : florets) {
            List<ModelRenderer> leaves = new ArrayList<>();
            collectLeaves(floret, leaves);
            floretLeaves.add(leaves);

            List<ModelRenderer> embers = new ArrayList<>();
            float[] phases = new float[leaves.size()];
            int i = 0;
            for (ModelRenderer leaf : leaves) {
                phases[i++] = rng.nextFloat() * (float) Math.PI * 2f;
                if (rng.nextFloat() < EMBER_FRACTION) {
                    embers.add(leaf);
                }
            }
            floretEmbers.add(embers);
            floretEmberPhases.add(phases);
        }
    }

    // index 0-2 = half1's florets, index 3-5 = half2's florets (fixed, stable order)
    private ModelRenderer[] combinedFlorets() {
        ModelRenderer[] h1 = model.getHalf1Florets();
        ModelRenderer[] h2 = model.getHalf2Florets();
        return new ModelRenderer[] { h1[0], h1[1], h1[2], h2[0], h2[1], h2[2] };
    }

    private static void collectLeaves(ModelRenderer part, List<ModelRenderer> out) {
        if (part.childModels == null || part.childModels.isEmpty()) {
            out.add(part);
            return;
        }
        for (Object childObj : part.childModels) {
            collectLeaves((ModelRenderer) childObj, out);
        }
    }

    /** 0..1, how charred a given branch's floret should be. Call whenever branch state changes. */
    public void setBranchProgress(int branchIndex, float progress) {
        if (branchIndex < 0 || branchIndex >= FLORET_COUNT) return;
        targetProgress[branchIndex] = clamp01(progress);
    }

    /**
     * Renders the flower.
     *
     * @param x, y, z    fine-tune translation in model space
     * @param yaw        Y rotation in degrees (facing)
     * @param pitch      X rotation in degrees - the raw export has the flower head
     *                   sitting at the "far" end of the stem's local coordinates
     *                   (it's not actually a child of the stem), so a base flip is
     *                   usually needed; expose it as a tunable rather than hardcoding it
     * @param scale      uniform scale
     * @param partialTicks used to smooth the idle sway and the charring fade
     */
    public void render(float x, float y, float z, float yaw, float pitch, float scale, float partialTicks) {
        idleTime += 0.05f;

        // smoothly chase target charring so it doesn't pop instantly when a branch unlocks
        for (int i = 0; i < FLORET_COUNT; i++) {
            displayProgress[i] += (targetProgress[i] - displayProgress[i]) * 0.08f;
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(yaw, 0f, 1f, 0f);
        GlStateManager.rotate(pitch, 1f, 0f, 0f);

        // idle breathing: whole plant scales very slightly
        float breathe = (float) Math.sin(idleTime * 0.6) * 0.015f;
        GlStateManager.scale(1f + breathe, 1f - breathe * 0.5f, 1f + breathe);

        float stemSway = (float) Math.sin(idleTime * 0.5) * 0.02f;
        ModelRenderer stem = model.getStem();
        ModelRenderer flower = model.getFlower();
        ModelRenderer half1 = model.getHalf1();
        ModelRenderer half2 = model.getHalf2();
        float stemBaseX = stem.rotateAngleX;
        float stemBaseZ = stem.rotateAngleZ;
        float flowerBaseX = flower.rotateAngleX;
        float flowerBaseZ = flower.rotateAngleZ;
        stem.rotateAngleX = stemBaseX + stemSway;
        stem.rotateAngleZ = stemBaseZ + stemSway * 0.6f;
        flower.rotateAngleX = flowerBaseX - stemSway * 0.5f;
        flower.rotateAngleZ = flowerBaseZ + (float) Math.sin(idleTime * 0.5 + 1.3) * 0.025f;

        GlStateManager.color(1f, 1f, 1f, 1f);
        // stem is a root part - renders itself + all its own bend segments recursively
        stem.render(1f / 16f);

        // flower is a SEPARATE root part (not a child of stem) - we must manually apply
        // its own transform, then each half's transform, before rendering their florets,
        // otherwise the florets render as if flower/half had no offset/rotation at all.
        GlStateManager.pushMatrix();
        flower.postRender(1f / 16f);

        GlStateManager.pushMatrix();
        half1.postRender(1f / 16f);
        ModelRenderer[] h1 = model.getHalf1Florets();
        for (int i = 0; i < h1.length; i++) {
            renderFloret(h1[i], i);
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        half2.postRender(1f / 16f);
        ModelRenderer[] h2 = model.getHalf2Florets();
        for (int i = 0; i < h2.length; i++) {
            renderFloret(h2[i], i + 3);
        }
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        // restore baked pose so state doesn't drift across frames
        stem.rotateAngleX = stemBaseX;
        stem.rotateAngleZ = stemBaseZ;
        flower.rotateAngleX = flowerBaseX;
        flower.rotateAngleZ = flowerBaseZ;

        GlStateManager.popMatrix();
    }

    private void renderFloret(ModelRenderer floret, int index) {
        float p = displayProgress[index];

        // base pass: whole floret tinted from white toward black as it chars
        float shade = 1f - p;
        GlStateManager.color(shade, shade, shade, 1f);
        floret.render(1f / 16f);
        GlStateManager.color(1f, 1f, 1f, 1f);

        if (p < EMBER_START_THRESHOLD) return;

        // ember pass: additive glow on a stable random subset of this floret's leaf cubes
        float emberStrength = (p - EMBER_START_THRESHOLD) / (1f - EMBER_START_THRESHOLD);
        List<ModelRenderer> embers = floretEmbers.get(index);
        float[] phases = floretEmberPhases.get(index);
        List<ModelRenderer> allLeaves = floretLeaves.get(index);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770 /*GL_SRC_ALPHA*/, 1 /*GL_ONE*/);
        GlStateManager.depthMask(false);

        for (ModelRenderer ember : embers) {
            int leafIdx = allLeaves.indexOf(ember);
            float phase = leafIdx >= 0 ? phases[leafIdx] : 0f;
            float pulse = (float) (Math.sin(idleTime * 1.8 + phase) + 1.0) * 0.5f; // 0..1
            float alpha = pulse * emberStrength * 0.9f;
            if (alpha <= 0.02f) continue;
            GlStateManager.color(1.0f, 0.45f, 0.1f, alpha);
            ember.render(1f / 16f);
        }

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    private static float clamp01(float v) {
        return v < 0f ? 0f : (v > 1f ? 1f : v);
    }
}
