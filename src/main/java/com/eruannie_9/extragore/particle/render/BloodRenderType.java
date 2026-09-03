/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.ActiveRenderInfo
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package com.eruannie_9.extragore.particle.render;

import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.blocksupport.PistonSupport;
import com.eruannie_9.extragore.particle.common.Util;
import com.eruannie_9.extragore.particle.render.BloodRender;
import com.eruannie_9.extragore.particle.render.GlState;
import com.eruannie_9.extragore.particle.render.parts.BloodLavaRendering;
import com.eruannie_9.extragore.particle.state.BloodHotBlocks;
import com.eruannie_9.extragore.particle.state.BloodMagic;
import com.eruannie_9.extragore.particle.state.liquid.lava.BloodLava;
import com.eruannie_9.extragore.particle.state.liquid.water.BloodWater;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public final class BloodRenderType {
    private static final Set<ParticleBlood> DECALS = Collections.newSetFromMap(new IdentityHashMap());
    private static final Set<BloodWater> WATER_TRACKED = Collections.newSetFromMap(new IdentityHashMap());
    private static final Set<BloodWater> WATER_DROPLET_QUEUE = Collections.newSetFromMap(new IdentityHashMap());
    private static final Set<BloodLava> LAVA_TRACKED = Collections.newSetFromMap(new IdentityHashMap());
    private static final Set<BloodLava> LAVA_BILLBOARD_QUEUE = Collections.newSetFromMap(new IdentityHashMap());
    private static final Set<ParticleBlood> MAGIC_BILLBOARD_QUEUE = Collections.newSetFromMap(new IdentityHashMap());
    private static final Set<ParticleBlood> BLOOD_BILLBOARD_QUEUE = Collections.newSetFromMap(new IdentityHashMap());
    private static final float ALPHA_REF_DEFAULT = 0.003921569f;
    private static final float ALPHA_REF_SOFT = 4.8828125E-4f;
    private static final float MAGIC_BASE_DARK_MUL = 0.35f;
    private static final float MAGIC_OVERLAY_ALPHA_MUL = 0.85f;
    private static final float MAGIC_ALPHA_EPS = 1.0E-6f;
    private static final float VISIBLE_ALPHA_EPS = 0.001f;

    public static void track(@Nullable ParticleBlood p) {
        if (p != null) {
            DECALS.add(p);
        }
    }

    public static void trackWater(@Nullable BloodWater p) {
        if (p != null) {
            WATER_TRACKED.add(p);
        }
    }

    public static void untrackWater(@Nullable BloodWater p) {
        if (p != null) {
            WATER_TRACKED.remove((Object)p);
        }
    }

    public static void trackLava(@Nullable BloodLava p) {
        if (p != null) {
            LAVA_TRACKED.add(p);
        }
    }

    public static void untrackLava(@Nullable BloodLava p) {
        if (p != null) {
            LAVA_TRACKED.remove((Object)p);
        }
    }

    public static void queueWaterDroplet(@Nullable BloodWater p) {
        if (p != null) {
            WATER_DROPLET_QUEUE.add(p);
        }
    }

    public static void queueMagicBillboard(@Nullable ParticleBlood p) {
        if (p != null) {
            MAGIC_BILLBOARD_QUEUE.add(p);
        }
    }

    public static void queueLavaBillboard(@Nullable BloodLava p) {
        if (p != null) {
            LAVA_BILLBOARD_QUEUE.add(p);
        }
    }

    public static void queueBloodBillboard(@Nullable ParticleBlood p) {
        if (p != null) {
            BLOOD_BILLBOARD_QUEUE.add(p);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!BloodRenderType.hasPendingWorldLastWork()) {
            return;
        }
        double oldInterpX = 0.0;
        double oldInterpY = 0.0;
        double oldInterpZ = 0.0;
        boolean restoreInterp = false;
        try {
            Minecraft mc = Minecraft.getMinecraft();
            WorldClient world = mc.world;
            Entity view = mc.getRenderViewEntity();
            if (world == null || view == null) {
                return;
            }
            float partialTicks = event.getPartialTicks();
            double camX = BloodRenderType.interp(view.lastTickPosX, view.posX, partialTicks);
            double camY = BloodRenderType.interp(view.lastTickPosY, view.posY, partialTicks);
            double camZ = BloodRenderType.interp(view.lastTickPosZ, view.posZ, partialTicks);
            boolean viewUnderwater = BloodRenderType.isEyeInWater(view, partialTicks);
            oldInterpX = ParticleBlood.getInterpX();
            oldInterpY = ParticleBlood.getInterpY();
            oldInterpZ = ParticleBlood.getInterpZ();
            restoreInterp = true;
            BloodRenderType.setSharedParticleInterp(camX, camY, camZ);
            ArrayList<RenderEntry> entries = BloodRenderType.collectRenderEntries((World)world, camX, camY, camZ, viewUnderwater, partialTicks);
            if (entries.isEmpty()) {
                return;
            }
            BloodRenderType.sortRenderEntries(entries);
            BillboardRotations fallbackRotations = BloodRenderType.captureActiveBillboardRotations();
            boolean wasLightmapEnabled = BloodRenderType.captureLightmapTextureEnabled();
            GlState.pushMatrix();
            GlState.pushAttrib();
            try {
                BloodRenderType.applyWorldLastState(mc);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                BloodRenderType.beginWorldLastBatch(buffer);
                BloodRenderType.renderEntries(entries, buffer, view, partialTicks, viewUnderwater, camX, camY, camZ, fallbackRotations);
                tessellator.draw();
            }
            finally {
                BloodRenderType.restoreWorldLastState(mc, wasLightmapEnabled);
            }
        }
        finally {
            BloodRenderType.clearFrameQueues();
            if (restoreInterp) {
                BloodRenderType.setSharedParticleInterp(oldInterpX, oldInterpY, oldInterpZ);
            }
        }
    }

    private static void beginWorldLastBatch(@Nonnull BufferBuilder buffer) {
        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }

    private static void clearFrameQueues() {
        for (ParticleBlood blood : BLOOD_BILLBOARD_QUEUE) {
            if (blood == null) continue;
            blood.clearQueuedBillboard();
        }
        for (BloodWater water : WATER_TRACKED) {
            if (water == null) continue;
            water.clearQueuedBillboard();
        }
        for (BloodWater water : WATER_DROPLET_QUEUE) {
            if (water == null) continue;
            water.clearQueuedBillboard();
        }
        for (BloodLava lava : LAVA_TRACKED) {
            if (lava == null) continue;
            lava.clearQueuedBillboard();
        }
        for (BloodLava lava : LAVA_BILLBOARD_QUEUE) {
            if (lava == null) continue;
            lava.clearQueuedBillboard();
        }
        for (ParticleBlood magic : MAGIC_BILLBOARD_QUEUE) {
            if (magic == null) continue;
            magic.clearQueuedBillboard();
        }
        BLOOD_BILLBOARD_QUEUE.clear();
        WATER_DROPLET_QUEUE.clear();
        LAVA_BILLBOARD_QUEUE.clear();
        MAGIC_BILLBOARD_QUEUE.clear();
    }

    private static boolean hasPendingWorldLastWork() {
        return !DECALS.isEmpty() || !BLOOD_BILLBOARD_QUEUE.isEmpty() || !WATER_TRACKED.isEmpty() || !WATER_DROPLET_QUEUE.isEmpty() || !LAVA_TRACKED.isEmpty() || !LAVA_BILLBOARD_QUEUE.isEmpty() || !MAGIC_BILLBOARD_QUEUE.isEmpty();
    }

    private static double interp(double prev, double current, float partialTicks) {
        return prev + (current - prev) * (double)partialTicks;
    }

    private static void setSharedParticleInterp(double x, double y, double z) {
        ParticleBlood.setInterp(x, y, z);
        BloodWater.setCamera(x, y, z);
    }

    @Nonnull
    private static BillboardRotations captureActiveBillboardRotations() {
        return new BillboardRotations(ActiveRenderInfo.getRotationX(), ActiveRenderInfo.getRotationZ(), ActiveRenderInfo.getRotationYZ(), ActiveRenderInfo.getRotationXY(), ActiveRenderInfo.getRotationXZ());
    }

    @Nonnull
    private static ArrayList<RenderEntry> collectRenderEntries(@Nonnull World world, double camX, double camY, double camZ, boolean viewUnderwater, float partialTicks) {
        int estimate = DECALS.size() + (viewUnderwater ? 0 : BLOOD_BILLBOARD_QUEUE.size()) + WATER_TRACKED.size() + LAVA_TRACKED.size() + LAVA_BILLBOARD_QUEUE.size() + (viewUnderwater ? 0 : MAGIC_BILLBOARD_QUEUE.size()) + (viewUnderwater ? WATER_DROPLET_QUEUE.size() : 0);
        ArrayList<RenderEntry> out = new ArrayList<RenderEntry>(estimate);
        BloodRenderType.collectDecalEntries(out, world, camX, camY, camZ, partialTicks);
        if (!viewUnderwater) {
            BloodRenderType.collectBloodBillboardEntries(out, world, camX, camY, camZ);
            BloodRenderType.collectMagicEntries(out, world, camX, camY, camZ);
        }
        BloodRenderType.collectWaterEntries(out, world, camX, camY, camZ, viewUnderwater);
        BloodRenderType.collectLavaEntries(out, world, camX, camY, camZ);
        return out;
    }

    private static void collectBloodBillboardEntries(@Nonnull List<RenderEntry> out, @Nonnull World world, double camX, double camY, double camZ) {
        for (ParticleBlood p : BLOOD_BILLBOARD_QUEUE) {
            if (!BloodRenderType.isBloodBillboardQueuedValid(p, world)) continue;
            out.add(RenderEntry.bloodBillboard(p.getDistanceSqTo(camX, camY, camZ), p));
        }
    }

    private static boolean isBloodBillboardQueuedValid(@Nullable ParticleBlood p, @Nonnull World world) {
        return p != null && p.isAlive() && p.getParticleWorld() == world && !p.isStuck && p.getAlpha() > 0.001f;
    }

    private static void collectDecalEntries(@Nonnull List<RenderEntry> out, @Nonnull World world, double camX, double camY, double camZ, float partialTicks) {
        Iterator<ParticleBlood> it = DECALS.iterator();
        while (it.hasNext()) {
            ParticleBlood p = it.next();
            if (!BloodRenderType.isTrackedDecalValid(p, world)) {
                it.remove();
                continue;
            }
            double sortDistSq = p.isStuck && p.stuckFace == EnumFacing.DOWN ? BloodRenderType.computeCeilingDecalSortDistanceSq(p, camX, camY, camZ, partialTicks) : p.getDistanceSqTo(camX, camY, camZ);
            out.add(RenderEntry.decal(sortDistSq, p));
        }
    }

    private static double computeCeilingDecalSortDistanceSq(@Nonnull ParticleBlood p, double camX, double camY, double camZ, float partialTicks) {
        PistonSupport.MovingInfo miNow;
        if (p.cache.shape.polys == null || p.cache.shape.polys.isEmpty()) {
            return p.getDistanceSqTo(camX, camY, camZ);
        }
        double ix = p.prevPosX + (p.posX - p.prevPosX) * (double)partialTicks;
        double iy = p.prevPosY + (p.posY - p.prevPosY) * (double)partialTicks;
        double iz = p.prevPosZ + (p.posZ - p.prevPosZ) * (double)partialTicks;
        double baseX = Double.isNaN(p.cache.view.x) ? p.posX : p.cache.view.x;
        double baseY = Double.isNaN(p.cache.view.y) ? p.posY : p.cache.view.y;
        double baseZ = Double.isNaN(p.cache.view.z) ? p.posZ : p.cache.view.z;
        double dx = ix - baseX;
        double dy = iy - baseY;
        double dz = iz - baseZ;
        World world = p.getParticleWorld();
        if (p.isStuck && p.stuckPos != null && world != null && (miNow = PistonSupport.getMovingInfo(world, p.stuckPos, partialTicks, p)) != null && miNow.offset != null && !miNow.staticBaseNoOffset) {
            PistonSupport.MovingInfo miBuild = PistonSupport.getMovingInfo(world, p.stuckPos, 1.0f, p);
            Vec3d offNow = miNow.offset;
            Vec3d offBuild = miBuild != null && miBuild.offset != null ? miBuild.offset : Util.ZERO;
            dx = offNow.x - offBuild.x;
            dy = offNow.y - offBuild.y;
            dz = offNow.z - offBuild.z;
        }
        double best = Double.POSITIVE_INFINITY;
        for (List<Util.Vertex> poly : p.cache.shape.polys) {
            if (poly == null) continue;
            for (Util.Vertex v : poly) {
                double vz;
                double vy;
                double vx;
                double dsq;
                if (v == null || !((dsq = (vx = v.x + dx - camX) * vx + (vy = v.y + dy - camY) * vy + (vz = v.z + dz - camZ) * vz) < best)) continue;
                best = dsq;
            }
        }
        return best != Double.POSITIVE_INFINITY ? best : p.getDistanceSqTo(camX, camY, camZ);
    }

    private static void collectWaterEntries(@Nonnull List<RenderEntry> out, @Nonnull World world, double camX, double camY, double camZ, boolean viewUnderwater) {
        Iterator<BloodWater> it = WATER_TRACKED.iterator();
        while (it.hasNext()) {
            BloodWater water = it.next();
            if (!BloodRenderType.isWaterTrackedValid(water, world)) {
                it.remove();
                continue;
            }
            double distSq = water.getDistanceSqTo(camX, camY, camZ);
            if (water.isSurfaceDecal()) {
                out.add(RenderEntry.water(distSq, water, RenderKind.WATER_SURFACE));
                continue;
            }
            if (!viewUnderwater || !water.isFloorDecal()) continue;
            out.add(RenderEntry.water(distSq, water, RenderKind.WATER_FLOOR));
        }
        if (!viewUnderwater) {
            return;
        }
        for (BloodWater water : WATER_DROPLET_QUEUE) {
            if (!BloodRenderType.isWaterDropletQueuedValid(water, world)) continue;
            out.add(RenderEntry.water(water.getDistanceSqTo(camX, camY, camZ), water, RenderKind.WATER_DROPLET));
        }
    }

    private static void collectLavaEntries(@Nonnull List<RenderEntry> out, @Nonnull World world, double camX, double camY, double camZ) {
        Iterator<BloodLava> it = LAVA_TRACKED.iterator();
        while (it.hasNext()) {
            BloodLava lava = it.next();
            if (!BloodRenderType.isLavaTrackedValid(lava, world)) {
                it.remove();
                continue;
            }
            if (!lava.isSurfaceDecal()) continue;
            out.add(RenderEntry.lava(lava.getDistanceSqTo(camX, camY, camZ), lava, RenderKind.LAVA_SURFACE));
        }
        for (BloodLava lava : LAVA_BILLBOARD_QUEUE) {
            if (!BloodRenderType.isLavaBillboardQueuedValid(lava, world)) continue;
            out.add(RenderEntry.lava(lava.getDistanceSqTo(camX, camY, camZ), lava, RenderKind.LAVA_BILLBOARD));
        }
    }

    private static void collectMagicEntries(@Nonnull List<RenderEntry> out, @Nonnull World world, double camX, double camY, double camZ) {
        for (ParticleBlood p : MAGIC_BILLBOARD_QUEUE) {
            if (!BloodRenderType.isMagicBillboardQueuedValid(p, world)) continue;
            out.add(RenderEntry.magic(p.getDistanceSqTo(camX, camY, camZ), p));
        }
    }

    private static boolean isTrackedDecalValid(@Nullable ParticleBlood p, @Nonnull World world) {
        return p != null && p.isAlive() && p.getParticleWorld() == world && p.isStuck;
    }

    private static boolean isWaterTrackedValid(@Nullable BloodWater p, @Nonnull World world) {
        return p != null && p.isAlive() && p.getParticleWorld() == world;
    }

    private static boolean isWaterDropletQueuedValid(@Nullable BloodWater p, @Nonnull World world) {
        return p != null && p.isAlive() && p.getParticleWorld() == world && !p.isSurfaceDecal() && !p.isFloorDecal();
    }

    private static boolean isLavaTrackedValid(@Nullable BloodLava p, @Nonnull World world) {
        return p != null && p.isAlive() && p.getParticleWorld() == world;
    }

    private static boolean isLavaBillboardQueuedValid(@Nullable BloodLava p, @Nonnull World world) {
        return p != null && p.isAlive() && p.getParticleWorld() == world && p.getParticleAlpha() > 0.001f;
    }

    private static boolean isMagicBillboardQueuedValid(@Nullable ParticleBlood p, @Nonnull World world) {
        return p != null && p.isAlive() && p.getParticleWorld() == world && !p.isStuckDecal() && BloodMagic.isMagic(p) && p.getAlpha() > 0.001f;
    }

    private static void sortRenderEntries(@Nonnull ArrayList<RenderEntry> entries) {
        entries.sort((a, b) -> {
            int distCmp = Double.compare(b.distSq, a.distSq);
            if (distCmp != 0) {
                return distCmp;
            }
            return Integer.compare(a.kind.sortKey, b.kind.sortKey);
        });
    }

    private static boolean captureLightmapTextureEnabled() {
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        boolean enabled = GL11.glIsEnabled((int)3553);
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        return enabled;
    }

    private static void applyWorldLastState(@Nonnull Minecraft mc) {
        GlState.disableLighting();
        GlState.enableBlend();
        GlState.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlState.enableDepth();
        GlState.depthMask(false);
        GlState.disableCull();
        GlState.enableTexture2D();
        GL11.glAlphaFunc((int)516, (float)4.8828125E-4f);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.entityRenderer.enableLightmap();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    private static void restoreWorldLastState(@Nonnull Minecraft mc, boolean wasLightmapEnabled) {
        GL11.glAlphaFunc((int)516, (float)0.003921569f);
        if (!wasLightmapEnabled) {
            mc.entityRenderer.disableLightmap();
        }
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GlState.depthMask(true);
        GlState.popAttrib();
        GlState.popMatrix();
        
        // MWCCF Fix: Sync GlStateManager cache with OpenGL after GL11.glPopAttrib()
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
    }

    private static void renderEntries(@Nonnull List<RenderEntry> entries, @Nonnull BufferBuilder buffer, @Nonnull Entity view, float partialTicks, boolean viewUnderwater, double camX, double camY, double camZ, @Nonnull BillboardRotations fallbackRotations) {
        block9: for (RenderEntry entry : entries) {
            if (entry == null) continue;
            BloodRenderType.setSharedParticleInterp(camX, camY, camZ);
            switch (entry.kind) {
                case WATER_SURFACE: {
                    BloodRenderType.renderWaterSurfaceEntry(entry.water, buffer, view, partialTicks, viewUnderwater);
                    continue block9;
                }
                case WATER_FLOOR: {
                    BloodRenderType.renderWaterFloorEntry(entry.water, buffer, view, partialTicks, viewUnderwater);
                    continue block9;
                }
                case WATER_DROPLET: {
                    if (entry.water == null) continue block9;
                    entry.water.renderQueuedDroplet(buffer, view, partialTicks);
                    continue block9;
                }
                case LAVA_SURFACE: {
                    if (entry.lava == null) continue block9;
                    entry.lava.renderSurfaceDecal(buffer, partialTicks);
                    continue block9;
                }
                case LAVA_BILLBOARD: {
                    if (entry.lava == null) continue block9;
                    BloodRenderType.renderQueuedLavaBillboard(entry.lava, buffer, view, partialTicks, camX, camY, camZ, fallbackRotations);
                    continue block9;
                }
                case BLOOD_BILLBOARD: {
                    if (entry.blood == null) continue block9;
                    BloodRenderType.renderQueuedBloodBillboard(entry.blood, buffer, view, partialTicks, camX, camY, camZ, fallbackRotations);
                    continue block9;
                }
                case MAGIC_BILLBOARD: {
                    if (entry.blood == null) continue block9;
                    BloodRenderType.renderQueuedMagicBillboard(entry.blood, buffer, view, partialTicks, camX, camY, camZ, fallbackRotations);
                    continue block9;
                }
            }
            if (entry.blood == null) continue;
            entry.blood.renderStuckDecal(buffer, partialTicks);
        }
    }

    private static void renderQueuedBloodBillboard(@Nonnull ParticleBlood p, @Nonnull BufferBuilder buffer, @Nonnull Entity view, float partialTicks, double camX, double camY, double camZ, @Nonnull BillboardRotations fallbackRotations) {
        if (!p.isAlive() || p.getAlpha() <= 0.001f || p.isStuck) {
            p.clearQueuedBillboard();
            return;
        }
        if (p.hasQueuedBillboard()) {
            BloodRenderType.setSharedParticleInterp(p.getQueuedInterpX(), p.getQueuedInterpY(), p.getQueuedInterpZ());
            BloodRender.renderQueuedBloodParticle(p, buffer, view, partialTicks, p.getQueuedRotX(), p.getQueuedRotZ(), p.getQueuedRotYZ(), p.getQueuedRotXY(), p.getQueuedRotXZ());
            p.clearQueuedBillboard();
            return;
        }
        BloodRenderType.setSharedParticleInterp(camX, camY, camZ);
        BloodRender.renderQueuedBloodParticle(p, buffer, view, partialTicks, fallbackRotations.rotationX, fallbackRotations.rotationZ, fallbackRotations.rotationYZ, fallbackRotations.rotationXY, fallbackRotations.rotationXZ);
        p.clearQueuedBillboard();
    }

    private static void renderWaterSurfaceEntry(@Nullable BloodWater water, @Nonnull BufferBuilder buffer, @Nonnull Entity view, float partialTicks, boolean viewUnderwater) {
        if (water == null) {
            return;
        }
        if (viewUnderwater) {
            water.renderQueuedDroplet(buffer, view, partialTicks);
        }
        water.renderSurfaceDecal(buffer, partialTicks);
    }

    private static void renderWaterFloorEntry(@Nullable BloodWater water, @Nonnull BufferBuilder buffer, @Nonnull Entity view, float partialTicks, boolean viewUnderwater) {
        if (water == null) {
            return;
        }
        if (viewUnderwater) {
            water.renderQueuedDroplet(buffer, view, partialTicks);
        }
        water.renderFloorDecal(buffer, partialTicks);
    }

    private static void renderQueuedMagicBillboard(@Nonnull ParticleBlood p, @Nonnull BufferBuilder buffer, @Nonnull Entity view, float partialTicks, double camX, double camY, double camZ, @Nonnull BillboardRotations fallbackRotations) {
        if (!p.isAlive() || !BloodMagic.isMagic(p) || p.getAlpha() <= 0.001f) {
            p.clearQueuedBillboard();
            return;
        }
        if (p.hasQueuedBillboard()) {
            BloodRenderType.setSharedParticleInterp(p.getQueuedInterpX(), p.getQueuedInterpY(), p.getQueuedInterpZ());
            BloodRenderType.renderMagicBillboard(p, buffer, view, partialTicks, p.getQueuedRotX(), p.getQueuedRotZ(), p.getQueuedRotYZ(), p.getQueuedRotXY(), p.getQueuedRotXZ());
            p.clearQueuedBillboard();
            return;
        }
        BloodRenderType.setSharedParticleInterp(camX, camY, camZ);
        BloodRenderType.renderMagicBillboard(p, buffer, view, partialTicks, fallbackRotations.rotationX, fallbackRotations.rotationZ, fallbackRotations.rotationYZ, fallbackRotations.rotationXY, fallbackRotations.rotationXZ);
        p.clearQueuedBillboard();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void renderMagicBillboard(@Nonnull ParticleBlood p, @Nonnull BufferBuilder buffer, @Nonnull Entity view, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (!p.isAlive() || !BloodMagic.isMagic(p)) {
            return;
        }
        if (BloodHotBlocks.shouldRenderHotAirBillboard(p)) {
            BloodLavaRendering.renderHotBillboardFromBlood(p, buffer, view, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            return;
        }
        float oldR = p.getRed();
        float oldG = p.getGreen();
        float oldB = p.getBlue();
        float oldA = p.getAlpha();
        float targetAlpha = Util.clamp01(oldA);
        if (targetAlpha <= 1.0E-6f) {
            return;
        }
        float tintR = p.getTintR();
        float tintG = p.getTintG();
        float tintB = p.getTintB();
        float passA0 = BloodRenderType.solveBasePassAlphaForTarget(targetAlpha, 0.85f);
        float passA1 = Util.clamp01(passA0 * 0.85f);
        try {
            p.setRGBA(Util.clamp01(tintR * 0.35f), Util.clamp01(tintG * 0.35f), Util.clamp01(tintB * 0.35f), passA0);
            p.vanillaRenderParticle(buffer, view, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            if (passA1 > 1.0E-6f) {
                p.setRGBA(Util.clamp01(tintR), Util.clamp01(tintG), Util.clamp01(tintB), passA1);
                p.vanillaRenderParticle(buffer, view, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            }
        }
        finally {
            p.setRGBA(oldR, oldG, oldB, oldA);
        }
    }

    private static void renderQueuedLavaBillboard(@Nonnull BloodLava p, @Nonnull BufferBuilder buffer, @Nonnull Entity view, float partialTicks, double camX, double camY, double camZ, @Nonnull BillboardRotations fallbackRotations) {
        if (!p.isAlive() || p.getParticleAlpha() <= 0.001f) {
            p.clearQueuedBillboard();
            return;
        }
        if (p.hasQueuedBillboard()) {
            BloodRenderType.setSharedParticleInterp(p.getQueuedInterpX(), p.getQueuedInterpY(), p.getQueuedInterpZ());
            BloodLavaRendering.renderParticle(p, buffer, view, partialTicks, p.getQueuedRotX(), p.getQueuedRotZ(), p.getQueuedRotYZ(), p.getQueuedRotXY(), p.getQueuedRotXZ());
            p.clearQueuedBillboard();
            return;
        }
        BloodRenderType.setSharedParticleInterp(camX, camY, camZ);
        BloodLavaRendering.renderParticle(p, buffer, view, partialTicks, fallbackRotations.rotationX, fallbackRotations.rotationZ, fallbackRotations.rotationYZ, fallbackRotations.rotationXY, fallbackRotations.rotationXZ);
        p.clearQueuedBillboard();
    }

    private static float solveBasePassAlphaForTarget(float targetAlpha, float overlayAlphaMul) {
        double x;
        targetAlpha = Util.clamp01(targetAlpha);
        float overlay = Util.clamp01(overlayAlphaMul);
        if (overlay <= 1.0E-6f) {
            return targetAlpha;
        }
        double b = -(1.0 + (double)overlay);
        double a = overlay;
        double c = targetAlpha;
        double disc = b * b - 4.0 * a * c;
        if (disc <= 0.0) {
            return targetAlpha;
        }
        double sqrt = Math.sqrt(disc);
        double x1 = (-b - sqrt) / (2.0 * a);
        double x2 = (-b + sqrt) / (2.0 * a);
        double d = x = x1 >= 0.0 && x1 <= 1.0 ? x1 : x2;
        if (x < 0.0) {
            x = 0.0;
        }
        if (x > 1.0) {
            x = 1.0;
        }
        return (float)x;
    }

    private static boolean isEyeInWater(@Nullable Entity view, float partialTicks) {
        if (view == null || view.world == null) {
            return false;
        }
        World world = view.world;
        try {
            if (view.isInsideOfMaterial(Material.WATER)) {
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            IBlockState viewpointState = ActiveRenderInfo.getBlockStateAtEntityViewpoint((World)world, (Entity)view, (float)partialTicks);
            if (viewpointState != null && viewpointState.getMaterial() == Material.WATER) {
                return true;
            }
            Vec3d eye = view.getPositionEyes(partialTicks);
            BlockPos eyePos = new BlockPos(eye.x, eye.y - 0.02, eye.z);
            if (world.isBlockLoaded(eyePos)) {
                IBlockState eyeState = BloodRenderType.safeGetState(world, eyePos);
                return eyeState != null && eyeState.getMaterial() == Material.WATER;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    @Nullable
    private static IBlockState safeGetState(@Nullable World world, @Nonnull BlockPos pos) {
        try {
            return world != null ? world.getBlockState(pos) : null;
        }
        catch (Throwable ignored) {
            return null;
        }
    }

    private static final class BillboardRotations {
        final float rotationX;
        final float rotationZ;
        final float rotationYZ;
        final float rotationXY;
        final float rotationXZ;

        private BillboardRotations(float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
            this.rotationX = rotationX;
            this.rotationZ = rotationZ;
            this.rotationYZ = rotationYZ;
            this.rotationXY = rotationXY;
            this.rotationXZ = rotationXZ;
        }
    }

    private static final class RenderEntry {
        final double distSq;
        @Nonnull
        final RenderKind kind;
        @Nullable
        final ParticleBlood blood;
        @Nullable
        final BloodWater water;
        @Nullable
        final BloodLava lava;

        private RenderEntry(double distSq, @Nonnull RenderKind kind, @Nullable ParticleBlood blood, @Nullable BloodWater water, @Nullable BloodLava lava) {
            this.distSq = distSq;
            this.kind = kind;
            this.blood = blood;
            this.water = water;
            this.lava = lava;
        }

        static RenderEntry decal(double distSq, @Nonnull ParticleBlood decal) {
            return new RenderEntry(distSq, RenderKind.DECAL, decal, null, null);
        }

        static RenderEntry bloodBillboard(double distSq, @Nonnull ParticleBlood blood) {
            return new RenderEntry(distSq, RenderKind.BLOOD_BILLBOARD, blood, null, null);
        }

        static RenderEntry magic(double distSq, @Nonnull ParticleBlood magic) {
            return new RenderEntry(distSq, RenderKind.MAGIC_BILLBOARD, magic, null, null);
        }

        static RenderEntry water(double distSq, @Nonnull BloodWater water, @Nonnull RenderKind kind) {
            return new RenderEntry(distSq, kind, null, water, null);
        }

        static RenderEntry lava(double distSq, @Nonnull BloodLava lava, @Nonnull RenderKind kind) {
            return new RenderEntry(distSq, kind, null, null, lava);
        }
    }

    private static enum RenderKind {
        DECAL(0),
        BLOOD_BILLBOARD(1),
        WATER_SURFACE(2),
        WATER_DROPLET(3),
        LAVA_SURFACE(4),
        LAVA_BILLBOARD(5),
        WATER_FLOOR(6),
        MAGIC_BILLBOARD(7);

        final int sortKey;

        private RenderKind(int sortKey) {
            this.sortKey = sortKey;
        }
    }
}

