package com.voltyx.mwccf.fireweapon.client;

import com.voltyx.mwccf.fireweapon.FireWeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Custom spark particle system matching BedrockFlowerRenderer in GuiSevenScreen.
 *
 * Emits glowing incandescent line sparks directly from the exact burning pixel-cubes (voxels)
 * of the weapon model, both in the 3D world (held item) and in the 2D inventory GUI slots.
 *
 * Automatically tracks the real World UP direction using the active ModelView matrix and
 * player camera pitch, ensuring sparks always rise up towards the sky in-game regardless
 * of the weapon's rotation (e.g. held blade downwards).
 *
 * Bugfixes applied:
 *  1. Sparks on the 3D player-preview model in the inventory (isPlayerInGui) no longer fall
 *     downward: that preview's ModelView matrix encodes a mirrored basis, detected via a
 *     negative determinant and compensated for, instead of blindly using a fixed up vector.
 *  2. Sparks render at full brightness regardless of ambient light level, with the lightmap
 *     texture-unit switch properly restored afterward so it doesn't corrupt later rendering.
 *  3. In-game (3D world) sparks are thicker and shorter per the tuning request.
 *  4. GL state touched by the custom line renderer is captured and restored exactly (both the
 *     real GL state via push/popAttrib AND GlStateManager's internal cache), instead of
 *     assuming a fixed "default" — the earlier attempts at this caused translucent/full-bright
 *     world geometry and black/white cutout textures.
 *  5. Held-item (true 3D world) sparks no longer visually "follow" the player. Previously they
 *     were stored and simulated in the item's own local model space and rendered inside the
 *     item's per-frame transform, so walking away from where a spark was emitted dragged it
 *     along. They are now converted to absolute world coordinates once, at the moment they
 *     spawn (via gluProject/gluUnProject against a camera-only matrix captured once per frame),
 *     and handed off to the existing WORLD_SPARKS system, which is fully decoupled from the
 *     item/player transform — exactly like the sparks already used for smoldering mobs. GUI and
 *     inventory player-preview sparks are unaffected and remain local/item-relative, which is
 *     correct for them since there's no "world" to decouple from there.
 */
@SideOnly(Side.CLIENT)
public class FireWeaponSparkManager {

    private static final Random RAND = new Random();
    private static final FloatBuffer MV_BUFFER = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer PROJ_BUFFER = BufferUtils.createFloatBuffer(16);

    // --- Buffers used to convert held-item local-space spawn points into absolute world
    // coordinates (see bugfix #5 above), so sparks stop following the player. ---
    private static final FloatBuffer CAM_MV_BUFFER = BufferUtils.createFloatBuffer(16);
    private static final IntBuffer VIEWPORT_BUFFER = BufferUtils.createIntBuffer(16);
    private static final FloatBuffer WIN_POS_BUFFER = BufferUtils.createFloatBuffer(3);
    private static final FloatBuffer WORLD_POS_BUFFER = BufferUtils.createFloatBuffer(3);
    private static boolean camMatrixValid = false;
    private static long lastWorldWeaponSpawnNanos = 0L;

    // --- Weapon swing-velocity tracking (for adding directional impulse to spawned sparks) ---
    // Centroid of the burning voxels projected to camera-relative world space, sampled each
    // render frame. The frame-over-frame delta divided by elapsed time gives the weapon's
    // instantaneous world-space velocity, which is then blended into each new spark so it
    // "flies in the direction of the swing" and then naturally arcs upward via gravity.
    private static float[] prevWeaponCentroid = null;
    private static long prevWeaponCentroidNanos = 0L;
    /** Current frame's weapon swing velocity in world units/sec, camera-relative. */
    private static float weaponSwingVX = 0f, weaponSwingVY = 0f, weaponSwingVZ = 0f;
    private static long lastWorldRenderNanos = 0L;

    /**
     * Bounding box of a burning voxel (кубик-пиксель) on the weapon model in local [0..1] item space.
     */
    public static class BurningVoxel {
        public final float xMin, xMax;
        public final float yMin, yMax;
        public final float zMin, zMax;
        public final boolean isFront;

        public BurningVoxel(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, boolean isFront) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            this.zMin = zMin;
            this.zMax = zMax;
            this.isFront = isFront;
        }
    }

    /**
     * Custom 3D Spark matching BedrockFlowerRenderer.Spark3D from GuiSevenScreen.
     */
    public static class Spark3D {
        public float x, y, z;
        public float vx, vy, vz;
        public float upX, upY, upZ;
        public boolean isGui;
        public float length;
        public float age, maxAge;

        public Spark3D(float x, float y, float z, float upX, float upY, float upZ, boolean isGui, Random rand) {
            this.x = x + (rand.nextFloat() - 0.5f) * 0.015f;
            this.y = y + (rand.nextFloat() - 0.5f) * 0.015f;
            this.z = z + (rand.nextFloat() - 0.5f) * 0.015f;
            this.upX = upX;
            this.upY = upY;
            this.upZ = upZ;
            this.isGui = isGui;

            // Construct perpendicular basis around (upX, upY, upZ) for natural outward spread
            float refX = 0, refY = 1, refZ = 0;
            if (Math.abs(upY) > 0.85f) {
                refX = 1; refY = 0; refZ = 0;
            }
            float rx = upY * refZ - upZ * refY;
            float ry = upZ * refX - upX * refZ;
            float rz = upX * refY - upY * refX;
            float rLen = (float) Math.sqrt(rx * rx + ry * ry + rz * rz);
            if (rLen > 0.0001f) {
                rx /= rLen; ry /= rLen; rz /= rLen;
            } else {
                rx = 1; ry = 0; rz = 0;
            }
            float fx = ry * upZ - rz * upY;
            float fy = rz * upX - rx * upZ;
            float fz = rx * upY - ry * upX;

            float theta = rand.nextFloat() * (float) (Math.PI * 2.0);
            float spreadOut = isGui ? (0.15f + rand.nextFloat() * 0.35f) : (0.14f + rand.nextFloat() * 0.32f);
            float cosT = (float) Math.cos(theta) * spreadOut;
            float sinT = (float) Math.sin(theta) * spreadOut;

            float speedUp = isGui ? (0.85f + rand.nextFloat() * 1.35f) : (1.20f + rand.nextFloat() * 1.65f);

            this.vx = upX * speedUp + rx * cosT + fx * sinT;
            this.vy = upY * speedUp + ry * cosT + fy * sinT;
            this.vz = upZ * speedUp + rz * cosT + fz * sinT;

            if (isGui) {
                this.length = 0.22f + rand.nextFloat() * 0.30f;
                this.maxAge = 0.35f + rand.nextFloat() * 0.35f; // 0.35s - 0.70s in GUI
            } else {
                // In-game: thicker lines are applied at render time (see renderParticles calls);
                // here we shorten the spark length/lifetime per the latest tuning pass.
                this.length = 0.26f + rand.nextFloat() * 0.28f; // was 0.35f + rand*0.45f
                this.maxAge = 0.80f + rand.nextFloat() * 0.75f; // 0.80s - 1.55s in-game
            }
            this.age = 0f;
        }

        /**
         * World-space constructor (for smoldering mobs, combat strike bursts, and now also
         * held-weapon in-game sparks — see bugfix #5).
         */
        public Spark3D(float x, float y, float z, float vx, float vy, float vz, Random rand) {
            this.x = x + (rand.nextFloat() - 0.5f) * 0.015f;
            this.y = y + (rand.nextFloat() - 0.5f) * 0.015f;
            this.z = z + (rand.nextFloat() - 0.5f) * 0.015f;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            this.upX = 0f;
            this.upY = 1f;
            this.upZ = 0f;
            this.isGui = false;
            this.length = 0.26f + rand.nextFloat() * 0.28f; // was 0.35f + rand.nextFloat() * 0.45f
            this.age = 0f;
            this.maxAge = 0.80f + rand.nextFloat() * 0.70f;
        }

        public boolean update(float deltaSec) {
            age += deltaSec;
            if (age >= maxAge) return false;

            x += vx * deltaSec;
            y += vy * deltaSec;
            z += vz * deltaSec;

            // Gravity pulls opposite to world UP; air drag softly decays velocity
            float g = isGui ? 1.6f : 0.85f;
            float drag = isGui ? 0.80f : 0.40f;

            vx -= upX * (g * deltaSec);
            vy -= upY * (g * deltaSec);
            vz -= upZ * (g * deltaSec);

            vx *= (1.0f - drag * deltaSec);
            vy *= (1.0f - drag * deltaSec);
            vz *= (1.0f - drag * deltaSec);
            return true;
        }

        public void getColor(float[] out) {
            float t = age / maxAge;
            float alpha = t < 0.2f ? (t / 0.2f) : (1.0f - (t - 0.2f) / 0.8f);
            out[3] = Math.max(0.0f, Math.min(1.0f, alpha));

            if (t < 0.25f) {
                // Hot white-yellow core
                float p = t / 0.25f;
                out[0] = 1.0f;
                out[1] = 0.95f - 0.2f * p;
                out[2] = 0.8f - 0.6f * p;
            } else if (t < 0.65f) {
                // Fiery vibrant orange
                float p = (t - 0.25f) / 0.40f;
                out[0] = 1.0f;
                out[1] = 0.75f - 0.45f * p;
                out[2] = 0.1f * (1.0f - p);
            } else {
                // Cooling dark embers / ash
                float p = (t - 0.65f) / 0.35f;
                out[0] = 0.9f - 0.5f * p;
                out[1] = 0.25f - 0.15f * p;
                out[2] = 0.05f + 0.1f * p;
            }
        }
    }

    /**
     * Renders a list of sparks using glowing lines matching BedrockFlowerRenderer.renderParticles.
     */
    public static void renderParticles(List<Spark3D> sparks, float tailScaleMult, float lineWidth) {
        if (sparks == null || sparks.isEmpty()) return;

        // Capture the REAL pre-existing GL state before we touch anything. We can't assume
        // a fixed "default" here (e.g. alpha test is often already ON during world rendering,
        // to cut out transparent leaves/glass/etc. pixels) — forcing a fixed default caused a
        // regression where cutout textures rendered black/white. Instead we read the true
        // prior state and restore exactly that afterward.
        boolean wasBlend = GL11.glGetBoolean(GL11.GL_BLEND);
        boolean wasAlphaTest = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);
        boolean wasLighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
        boolean wasCull = GL11.glGetBoolean(GL11.GL_CULL_FACE);
        boolean wasDepthTest = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        boolean wasTexture2D = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
        boolean wasDepthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;

        // glPushAttrib/glPopAttrib correctly restores the exact real GL values (blend func,
        // alpha threshold, line width, etc.) — that part was never the problem. The problem
        // was that GlStateManager's own cache doesn't know popAttrib happened, so it goes
        // stale. We fix that below by resyncing the cache to the captured real state,
        // instead of assuming what the state "should" be.
        GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE); // additive glow
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GL11.glLineWidth(lineWidth);

        // Force the lightmap to full brightness so sparks glow correctly in the dark.
        // IMPORTANT: this switches the ACTIVE texture unit to the lightmap unit; that is NOT
        // saved/restored by pushAttrib/popAttrib, so we must switch it back ourselves.
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        float[] col = new float[4];
        for (Spark3D s : sparks) {
            s.getColor(col);
            float a = col[3];
            if (a <= 0.01f) continue;

            int iaHead = (int) (a * 255.0f);
            int irHead = (int) (col[0] * 255.0f);
            int igHead = (int) (col[1] * 255.0f);
            int ibHead = (int) (col[2] * 255.0f);

            // Tail is stretched backwards along velocity with cooler color (orange/ash)
            float speed = (float) Math.sqrt(s.vx * s.vx + s.vy * s.vy + s.vz * s.vz);
            float tailScale = (speed > 0.001f) ? (s.length * tailScaleMult) : 0.01f;
            float tailX = s.x - s.vx * tailScale;
            float tailY = s.y - s.vy * tailScale;
            float tailZ = s.z - s.vz * tailScale;

            int iaTail = (int) (a * 0.45f * 255.0f);
            int irTail = (int) (Math.max(0f, col[0] - 0.2f) * 255.0f);
            int igTail = (int) (Math.max(0f, col[1] - 0.3f) * 255.0f);
            int ibTail = (int) (col[2] * 255.0f);

            // Tail vertex
            buffer.pos(tailX, tailY, tailZ).color(irTail, igTail, ibTail, iaTail).endVertex();
            // Head vertex (bright tip)
            buffer.pos(s.x, s.y, s.z).color(irHead, igHead, ibHead, iaHead).endVertex();
        }
        tessellator.draw();

        // Restore the EXACT real GL state (blend func/alpha threshold/line width included).
        GlStateManager.popAttrib();

        // Restore lightmap coords to previous brightness so subsequent GUI rendering is not over-brightened!
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.bindTexture(0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        // Now resync GlStateManager's cache to match the real state we just captured and
        // restored — using the captured truth, not a guessed default.
        if (wasBlend) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        } else {
            GlStateManager.disableBlend();
        }
        if (wasAlphaTest) GlStateManager.enableAlpha(); else GlStateManager.disableAlpha();
        if (wasLighting) GlStateManager.enableLighting(); else GlStateManager.disableLighting();
        if (wasCull) GlStateManager.enableCull(); else GlStateManager.disableCull();
        if (wasDepthTest) GlStateManager.enableDepth(); else GlStateManager.disableDepth();
        if (wasTexture2D) GlStateManager.enableTexture2D(); else GlStateManager.disableTexture2D();
        GlStateManager.depthMask(wasDepthMask);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Per-item spark particle system tracking sparks in local item model space.
     * Used ONLY for the 2D GUI icon and the 3D inventory player-preview — both have no
     * real "world" to decouple from, so staying item-relative is correct for them.
     */
    public static class ItemSparkSystem {
        public final List<Spark3D> sparks = new ArrayList<>(60);
        public long lastRenderNanos = 0L;
        private long lastUpdateNanos = 0L;

        public void updateAndRender(List<BurningVoxel> voxels, boolean isGui, float upX, float upY, float upZ, boolean soaked) {
            long now = System.nanoTime();
            lastRenderNanos = now;
            if (lastUpdateNanos == 0L) {
                lastUpdateNanos = now;
            }
            long elapsed = now - lastUpdateNanos;
            // Throttle physics & spawning to ~60-100 FPS (at least 10ms between updates)
            if (elapsed >= 10_000_000L) {
                float deltaSec = Math.min(0.05f, elapsed / 1_000_000_000.0f);
                lastUpdateNanos = now;

                // 1. Update existing sparks
                for (Iterator<Spark3D> it = sparks.iterator(); it.hasNext(); ) {
                    Spark3D s = it.next();
                    if (!s.update(deltaSec)) {
                        it.remove();
                    }
                }

                // 2. Spawn new sparks directly from burning pixel-cubes
                // SOAKED mode: all pixels burn simultaneously, so spawn capacity is increased
                int maxSparks = soaked ? 160 : 45;
                float frontChance = soaked ? 0.08f : 0.08f;
                float backChance  = soaked ? 0.04f : 0.02f;

                if (voxels != null && !voxels.isEmpty() && sparks.size() < maxSparks) {
                    for (BurningVoxel v : voxels) {
                        float chance = v.isFront ? frontChance : backChance;
                        if (RAND.nextFloat() < chance && sparks.size() < maxSparks) {
                            float sx = v.xMin + RAND.nextFloat() * (v.xMax - v.xMin);
                            float sy = v.yMin + RAND.nextFloat() * (v.yMax - v.yMin);
                            float sz = v.zMin + RAND.nextFloat() * (v.zMax - v.zMin);
                            sparks.add(new Spark3D(sx, sy, sz, upX, upY, upZ, true, RAND));
                        }
                    }
                }
            }

            // 3. Render sparks directly within the active item model matrix
            renderParticles(sparks, 0.06f, 2.2f);
        }
    }

    private static final Map<String, ItemSparkSystem> ITEM_SPARKS = new ConcurrentHashMap<>();

    private static String getSparkKey(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("mwccf_spark_id")) {
            return "id_" + tag.getLong("mwccf_spark_id");
        }
        return "hash_" + System.identityHashCode(stack);
    }

    /**
     * Called during weapon rendering in both 3D World and 2D GUI inventory.
     * Computes the true World UP direction and renders custom sparks emitted directly
     * from the burning pixel-cubes.
     */
    public static void renderAndSpawnItemSparks(ItemStack stack, List<BurningVoxel> voxels) {
        if (stack.isEmpty() || voxels == null || voxels.isEmpty()) return;

        // Read active OpenGL Projection matrix
        PROJ_BUFFER.rewind();
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, PROJ_BUFFER);
        float m11 = PROJ_BUFFER.get(11);

        // Read active OpenGL ModelView matrix
        MV_BUFFER.rewind();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, MV_BUFFER);
        float[] m = new float[16];
        MV_BUFFER.get(m);

        float scaleSq = m[0] * m[0] + m[1] * m[1] + m[2] * m[2];
        boolean isPlayerInGui = efw.util.RenderContext.isRenderingPlayerInGui || efw.util.RenderContext.isRenderingPlayerInSevenScreen;
        boolean isGui = (Math.abs(m11) < 0.001f && scaleSq > 40.0f) && !isPlayerInGui;

        float upX = 0.0f, upY = 1.0f, upZ = 0.0f;
        if (!isGui) {
            Minecraft mc = Minecraft.getMinecraft();
            float pitch = (mc.player != null && !isPlayerInGui) ? mc.player.rotationPitch : 0.0f;
            float pitchRad = pitch * 0.017453292F;
            float cosP = MathHelper.cos(pitchRad);
            float sinP = MathHelper.sin(pitchRad);

            upX = m[1] * cosP + m[2] * sinP;
            upY = m[5] * cosP + m[6] * sinP;
            upZ = m[9] * cosP + m[10] * sinP;

            if (isPlayerInGui) {
                // Mirrored-basis detection/compensation (see bugfix #1 note above).
                float det3 = m[0] * (m[5] * m[10] - m[6] * m[9])
                           - m[1] * (m[4] * m[10] - m[6] * m[8])
                           + m[2] * (m[4] * m[9] - m[5] * m[8]);
                if (det3 < 0.0f) {
                    upX = -upX;
                    upY = -upY;
                    upZ = -upZ;
                }
            }

            float len = (float) Math.sqrt(upX * upX + upY * upY + upZ * upZ);
            if (len > 0.0001f) {
                upX /= len;
                upY /= len;
                upZ /= len;
            } else {
                upX = 0.0f;
                upY = 1.0f;
                upZ = 0.0f;
            }
        }

        if (isGui || isPlayerInGui) {
            // Flat 2D icon and 3D inventory player-preview: no real "world" to decouple
            // from, so item-local simulation/rendering is correct here.
            boolean soaked = FireWeaponHelper.isSoaked(stack);
            String baseKey = getSparkKey(stack);
            String key = isGui ? (baseKey + "_gui") : (baseKey + "_player_gui");
            ItemSparkSystem sys = ITEM_SPARKS.computeIfAbsent(key, k -> new ItemSparkSystem());
            sys.updateAndRender(voxels, isGui, upX, upY, upZ, soaked);
            return;
        }

        // True 3D world case (held item, first/third person): convert spawn points to
        // absolute world coordinates right now and hand them to the independent WORLD_SPARKS
        // system, so they stop following the player — see bugfix #5 above.
        boolean soaked = FireWeaponHelper.isSoaked(stack);
        spawnWorldSparksFromVoxels(voxels, upX, upY, upZ, soaked);
    }

    /**
     * Converts a point in the current item's local model space into world-relative-to-camera
     * float coordinates: projects it through the item's own (animated, player-following)
     * ModelView matrix into window space, then unprojects that same window position through
     * a camera-only matrix (captured once per frame in onRenderWorldLast, at a point where all
     * per-entity matrix pushes/pops are balanced back to the pure camera view). Returns null
     * if either GLU call fails or no camera-only matrix has been captured yet this session.
     */
    private static float[] localToWorldRelative(float lx, float ly, float lz) {
        if (!camMatrixValid) return null;

        VIEWPORT_BUFFER.clear();
        GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT_BUFFER);
        VIEWPORT_BUFFER.rewind();

        MV_BUFFER.rewind();
        PROJ_BUFFER.rewind();
        WIN_POS_BUFFER.clear();
        boolean okProj = GLU.gluProject(lx, ly, lz, MV_BUFFER, PROJ_BUFFER, VIEWPORT_BUFFER, WIN_POS_BUFFER);
        if (!okProj) return null;
        WIN_POS_BUFFER.rewind();
        float winX = WIN_POS_BUFFER.get(0);
        float winY = WIN_POS_BUFFER.get(1);
        float winZ = WIN_POS_BUFFER.get(2);

        VIEWPORT_BUFFER.rewind();
        CAM_MV_BUFFER.rewind();
        PROJ_BUFFER.rewind();
        WORLD_POS_BUFFER.clear();
        boolean okUnproj = GLU.gluUnProject(winX, winY, winZ, CAM_MV_BUFFER, PROJ_BUFFER, VIEWPORT_BUFFER, WORLD_POS_BUFFER);
        if (!okUnproj) return null;
        WORLD_POS_BUFFER.rewind();
        return new float[] { WORLD_POS_BUFFER.get(0), WORLD_POS_BUFFER.get(1), WORLD_POS_BUFFER.get(2) };
    }

    /**
     * Spawns held-weapon sparks directly into the decoupled WORLD_SPARKS system using
     * absolute world coordinates, instead of the item-local ItemSparkSystem. Throttled the
     * same way the old per-item system was (~every 10ms) to keep the spawn rate comparable.
     *
     * Each frame (regardless of the spawn throttle) this method projects the centroid of all
     * burning voxels into camera-relative world space and diffs it against the previous frame
     * to derive the weapon's instantaneous swing velocity. A fraction of that velocity is
     * added to each spawned spark, so sparks "fly away in the direction of the swing" and
     * then naturally arc upward due to the existing gravity / drag physics.
     */
    private static void spawnWorldSparksFromVoxels(List<BurningVoxel> voxels, float upX, float upY, float upZ, boolean soaked) {
        int maxWorldSparks = soaked ? 900 : 600;
        if (voxels == null || voxels.isEmpty() || WORLD_SPARKS.size() >= maxWorldSparks) return;

        long now = System.nanoTime();

        // ── Swing-velocity tracking ──────────────────────────────────────────────────────────
        // Project the centre of every burning voxel into camera-relative world space and
        // accumulate a centroid. We do this EVERY call (not just when spawning) so we
        // always have a fresh velocity estimate even during throttled frames.
        {
            float cxSum = 0f, cySum = 0f, czSum = 0f;
            int cnt = 0;
            for (BurningVoxel v : voxels) {
                float cx = (v.xMin + v.xMax) * 0.5f;
                float cy = (v.yMin + v.yMax) * 0.5f;
                float cz = (v.zMin + v.zMax) * 0.5f;
                float[] wp = localToWorldRelative(cx, cy, cz);
                if (wp != null) { cxSum += wp[0]; cySum += wp[1]; czSum += wp[2]; cnt++; }
            }
            if (cnt > 0) {
                float invCnt = 1.0f / cnt;
                float[] centroid = { cxSum * invCnt, cySum * invCnt, czSum * invCnt };
                if (prevWeaponCentroid != null && prevWeaponCentroidNanos != 0L) {
                    float dt = (now - prevWeaponCentroidNanos) / 1_000_000_000.0f;
                    if (dt > 0.001f && dt < 0.5f) {
                        weaponSwingVX = (centroid[0] - prevWeaponCentroid[0]) / dt;
                        weaponSwingVY = (centroid[1] - prevWeaponCentroid[1]) / dt;
                        weaponSwingVZ = (centroid[2] - prevWeaponCentroid[2]) / dt;
                        // Clamp: avoids huge impulse on teleport / fast camera cuts
                        float swingLen = (float) Math.sqrt(
                                weaponSwingVX * weaponSwingVX +
                                weaponSwingVY * weaponSwingVY +
                                weaponSwingVZ * weaponSwingVZ);
                        float maxSwing = 12.0f; // world units/sec
                        if (swingLen > maxSwing) {
                            float s = maxSwing / swingLen;
                            weaponSwingVX *= s;
                            weaponSwingVY *= s;
                            weaponSwingVZ *= s;
                        }
                    }
                }
                prevWeaponCentroid = centroid;
                prevWeaponCentroidNanos = now;
            }
        }
        // ── end swing tracking ───────────────────────────────────────────────────────────────

        // Throttle actual spark spawning to ~10ms
        if (lastWorldWeaponSpawnNanos != 0L && (now - lastWorldWeaponSpawnNanos) < 10_000_000L) return;
        lastWorldWeaponSpawnNanos = now;

        Minecraft mc = Minecraft.getMinecraft();
        RenderManager rm = mc.getRenderManager();
        double camX = rm.viewerPosX, camY = rm.viewerPosY, camZ = rm.viewerPosZ;

        // How much of the weapon's swing velocity the spark inherits on spawn.
        // 0.40 = 40% — noticeable directional kick without overshadowing the
        // "upward rising ember" arc that gravity/drag produces over the lifetime.
        final float SWING_INHERIT = 0.40f;

        // SOAKED mode: all pixels burn simultaneously, higher particle density
        float frontChance = soaked ? 0.045f : 0.035f;
        float backChance  = soaked ? 0.020f : 0.010f;

        for (BurningVoxel v : voxels) {
            if (WORLD_SPARKS.size() >= maxWorldSparks) break;
            if (RAND.nextFloat() >= (v.isFront ? frontChance : backChance)) continue;

            float sx = v.xMin + RAND.nextFloat() * (v.xMax - v.xMin);
            float sy = v.yMin + RAND.nextFloat() * (v.yMax - v.yMin);
            float sz = v.zMin + RAND.nextFloat() * (v.zMax - v.zMin);

            // Use Spark3D's outward cone velocity math purely to get a local direction,
            // then convert that direction into world space by diffing two projected points.
            Spark3D template = new Spark3D(sx, sy, sz, upX, upY, upZ, false, RAND);

            float[] worldA = localToWorldRelative(sx, sy, sz);
            if (worldA == null) continue;
            float epsilon = 0.05f;
            float[] worldB = localToWorldRelative(
                    sx + template.vx * epsilon,
                    sy + template.vy * epsilon,
                    sz + template.vz * epsilon);
            if (worldB == null) continue;

            // Base cone velocity in world space + inherited swing impulse.
            // The spark now flies away in the direction the weapon is swinging and then
            // gradually arcs upward as gravity decelerates the lateral component.
            float wvx = (worldB[0] - worldA[0]) / epsilon + weaponSwingVX * SWING_INHERIT;
            float wvy = (worldB[1] - worldA[1]) / epsilon + weaponSwingVY * SWING_INHERIT;
            float wvz = (worldB[2] - worldA[2]) / epsilon + weaponSwingVZ * SWING_INHERIT;
            if (mc.player != null) {
                wvx -= (float) mc.player.motionX * 0.7F;
                wvy += Math.max(0.0F, (float) mc.player.motionY) * 0.25F;
                wvz -= (float) mc.player.motionZ * 0.7F;
            }

            spawnSpark(
                    (float) (camX + worldA[0]),
                    (float) (camY + worldA[1]),
                    (float) (camZ + worldA[2]),
                    wvx, wvy, wvz);
        }
    }

    // --- 3D World Sparks (used for smoldering mobs, combat strike bursts, and now also
    //     held-weapon in-game sparks — see bugfix #5) ---
    private static final List<Spark3D> WORLD_SPARKS = new ArrayList<>(600);

    public static void spawnSpark(float x, float y, float z, float vx, float vy, float vz) {
        if (WORLD_SPARKS.size() < 600) {
            WORLD_SPARKS.add(new Spark3D(x, y, z, vx, vy, vz, RAND));
        }
    }

    public static void spawnBurst(Vec3d pos, int count, float speed) {
        if (pos == null) return;
        for (int i = 0; i < count; i++) {
            float theta = RAND.nextFloat() * (float) (Math.PI * 2.0);
            float phi = RAND.nextFloat() * (float) (Math.PI * 0.5);
            float spd = (0.7f + RAND.nextFloat() * 1.1f) * speed;

            float vx = (float) (Math.cos(theta) * Math.cos(phi)) * spd;
            float vz = (float) (Math.sin(theta) * Math.cos(phi)) * spd;
            float vy = (float) Math.sin(phi) * spd + 0.9f;

            spawnSpark((float) pos.x, (float) pos.y, (float) pos.z, vx, vy, vz);
        }
    }

    /**
     * Spawns a directional impact burst of sparks when striking an entity.
     * Sparks fly predominantly in the impact direction (plus outwards conical spread).
     */
    public static void spawnImpactBurst(Vec3d pos, float dirX, float dirY, float dirZ, int count, float speed) {
        if (pos == null) return;

        // Orthogonal basis to create a cone around (dirX, dirY, dirZ)
        float refX = 0, refY = 1, refZ = 0;
        if (Math.abs(dirY) > 0.85f) {
            refX = 1; refY = 0; refZ = 0;
        }
        float rx = dirY * refZ - dirZ * refY;
        float ry = dirZ * refX - dirX * refZ;
        float rz = dirX * refY - dirY * refX;
        float rLen = (float) Math.sqrt(rx * rx + ry * ry + rz * rz);
        if (rLen > 0.0001f) {
            rx /= rLen; ry /= rLen; rz /= rLen;
        } else {
            rx = 1; ry = 0; rz = 0;
        }
        float fx = ry * dirZ - rz * dirY;
        float fy = rz * dirX - rx * dirZ;
        float fz = rx * dirY - ry * dirX;

        for (int i = 0; i < count; i++) {
            float theta = RAND.nextFloat() * (float) (Math.PI * 2.0);
            // Outward conical spread angle
            float spread = 0.35f + RAND.nextFloat() * 0.65f;
            float cosT = (float) Math.cos(theta) * spread;
            float sinT = (float) Math.sin(theta) * spread;

            float spd = (0.8f + RAND.nextFloat() * 1.2f) * speed;
            // Strong forward momentum along strike direction + sideways burst + small upward bounce
            float forward = 0.9f + RAND.nextFloat() * 0.7f;
            float vx = (dirX * forward + rx * cosT + fx * sinT) * spd;
            float vy = (dirY * forward + ry * cosT + fy * sinT) * spd + 0.4f;
            float vz = (dirZ * forward + rz * cosT + fz * sinT) * spd;
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                vx -= (float) mc.player.motionX * 0.7F;
                vy += Math.max(0.0F, (float) mc.player.motionY) * 0.25F;
                vz -= (float) mc.player.motionZ * 0.7F;
            }

            spawnSpark((float) pos.x, (float) pos.y, (float) pos.z, vx, vy, vz);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) {
            com.voltyx.mwccf.fireweapon.smolder.SmolderingClientManager.updateClient();
            return;
        }
        if (mc.isGamePaused()) return;

        // 1. Tick smoldering mobs visuals (game-tick rate is fine for mob logic)
        com.voltyx.mwccf.fireweapon.smolder.SmolderingClientManager.updateClient();

        // 2. Clean up stale GUI/player-preview item spark systems not rendered for > 3 seconds
        long now = System.nanoTime();
        long expireThreshold = 3_000_000_000L;
        ITEM_SPARKS.entrySet().removeIf(e -> (now - e.getValue().lastRenderNanos) > expireThreshold);
        // NOTE: WORLD_SPARKS are now updated inside onRenderWorldLast with real per-frame
        // delta time, which gives smooth motion at the actual rendering FPS instead of the
        // jerky 20 Hz step that the fixed dt=0.05f tick-based update produced.
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        // Capture the camera-only ModelView matrix for THIS frame before we push our own
        // translate below. At this point all per-entity matrix pushes/pops from world
        // rendering have been balanced back to the pure camera view, which is exactly what
        // localToWorldRelative() needs to convert held-item local points into world space
        // later this frame (first-person hand renders after this event) or next frame (other
        // players' held items, rendered during the main entity pass).
        CAM_MV_BUFFER.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, CAM_MV_BUFFER);
        camMatrixValid = true;

        // Update WORLD_SPARKS physics here (per render frame) instead of in onClientTick
        // (per game tick). This gives smooth continuous motion at the real rendering FPS
        // rather than the jerky 20 Hz step the tick-based fixed dt=0.05s produced.
        long nowNanos = System.nanoTime();
        if (lastWorldRenderNanos != 0L && !WORLD_SPARKS.isEmpty()) {
            float deltaSec = Math.min(0.1f, (nowNanos - lastWorldRenderNanos) / 1_000_000_000.0f);
            for (Iterator<Spark3D> it = WORLD_SPARKS.iterator(); it.hasNext(); ) {
                if (!it.next().update(deltaSec)) {
                    it.remove();
                }
            }
        }
        lastWorldRenderNanos = nowNanos;

        if (WORLD_SPARKS.isEmpty()) return;

        Minecraft mc = Minecraft.getMinecraft();
        RenderManager rm = mc.getRenderManager();
        double camX = rm.viewerPosX;
        double camY = rm.viewerPosY;
        double camZ = rm.viewerPosZ;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-camX, -camY, -camZ);

        // Render world sparks using the same custom glowing line renderer
        renderParticles(WORLD_SPARKS, 0.085f, 4.2f);

        GlStateManager.popMatrix();
    }
}