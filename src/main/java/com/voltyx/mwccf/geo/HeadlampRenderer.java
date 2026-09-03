package com.voltyx.mwccf.geo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeadlampRenderer {

    private static GeoArmorModel cachedHeadlampModel;
    private static final ResourceLocation HEADLAMP_TEXTURE = new ResourceLocation("mwccf", "textures/models/armor/headlight.png");
    private static final ResourceLocation HEADLAMP_GEO = new ResourceLocation("mwccf", "geo/headlight.geo.json");
    private static final Map<UUID, java.nio.FloatBuffer> savedMatrices = new HashMap<>();
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("mwccf", "textures/entity/beam.png");

    public static GeoArmorModel getHeadlampModel() {
        if (cachedHeadlampModel == null) {
            cachedHeadlampModel = new GeoArmorModel(HEADLAMP_GEO);
            cachedHeadlampModel.bipedHead.cubeList.clear(); // Ensure no extra cubes
        }
        return cachedHeadlampModel;
    }

    public static ResourceLocation getHeadlampTexture() {
        return HEADLAMP_TEXTURE;
    }

    public static boolean hasHeadlampEquipped(EntityPlayer player) {
        if (!net.minecraftforge.fml.common.Loader.isModLoaded("baubles")) return false;
        baubles.api.cap.IBaublesItemHandler handler = baubles.api.BaublesApi.getBaublesHandler(player);
        if (handler != null) {
            net.minecraft.item.ItemStack bauble = handler.getStackInSlot(4);
            return !bauble.isEmpty() && bauble.getItem() instanceof ItemHeadlamp;
        }
        return false;
    }

    public static void drawLightCone(float length, float endRadius, float intensity) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        
        boolean cullEnabled = org.lwjgl.opengl.GL11.glGetBoolean(org.lwjgl.opengl.GL11.GL_CULL_FACE);
        boolean texEnabled = org.lwjgl.opengl.GL11.glGetBoolean(org.lwjgl.opengl.GL11.GL_TEXTURE_2D);
        int previousShadeModel = org.lwjgl.opengl.GL11.glGetInteger(org.lwjgl.opengl.GL11.GL_SHADE_MODEL);
        
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        
        int segments = 4; // 4-sided pyramid
        
        float rStart = 0.0f;
        float rEnd = endRadius;
        float zStart = 0.0f;
        float zEnd = length;
        float aStart = 0.8f * intensity; // Brighter start
        float aEnd = 0.0f;
        
        // Warm color matching OptiFine blocklight (R=1.0, G=0.85, B=0.6)
        float r = 1.0f;
        float g = 0.85f;
        float b = 0.6f;
        
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        
        for (int i = 0; i < segments; i++) {
            // Offset by 45 degrees so flat sides align with axes
            double angle1 = 2 * Math.PI * i / segments + (Math.PI / 4.0);
            double angle2 = 2 * Math.PI * (i + 1) / segments + (Math.PI / 4.0);
            
            float x1_norm = (float) Math.sin(angle1);
            float y1_norm = (float) Math.cos(angle1);
            float x2_norm = (float) Math.sin(angle2);
            float y2_norm = (float) Math.cos(angle2);
            
            buf.pos(x1_norm * rStart, y1_norm * rStart, zStart).color(r, g, b, aStart).endVertex();
            buf.pos(x1_norm * rEnd, y1_norm * rEnd, zEnd).color(r, g, b, aEnd).endVertex();
            buf.pos(x2_norm * rEnd, y2_norm * rEnd, zEnd).color(r, g, b, aEnd).endVertex();
            buf.pos(x2_norm * rStart, y2_norm * rStart, zStart).color(r, g, b, aStart).endVertex();
        }
        
        tess.draw();
        
        GlStateManager.shadeModel(previousShadeModel);
        if (texEnabled) GlStateManager.enableTexture2D(); else GlStateManager.disableTexture2D();
        if (cullEnabled) GlStateManager.enableCull(); else GlStateManager.disableCull();
    }

    public static void drawLightDecal(net.minecraft.util.math.Vec3d hitVec, net.minecraft.util.EnumFacing side, float dist, float intensity) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.translate(hitVec.x, hitVec.y, hitVec.z);

        // Orient to face the normal
        if (side == net.minecraft.util.EnumFacing.UP) {
            GlStateManager.rotate(-90, 1, 0, 0);
        } else if (side == net.minecraft.util.EnumFacing.DOWN) {
            GlStateManager.rotate(90, 1, 0, 0);
        } else {
            GlStateManager.rotate(-side.getHorizontalAngle(), 0, 1, 0);
        }
        
        // Offset slightly to prevent Z-fighting
        GlStateManager.translate(0, 0, 0.01);

        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE); // Additive blending for pure light

        buf.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

        // Brighter when closer
        float centerAlpha = Math.max(0.1f, 1.0f - (dist / 30f)) * intensity;
        float edgeAlpha = 0.0f;
        float radius = 1.5f + (dist * 0.1f); // Spread out light the further it goes

        // Warm color matching OptiFine blocklight (R=1.0, G=0.85, B=0.6)
        float r = 1.0f;
        float g = 0.85f;
        float b = 0.6f;

        buf.pos(0, 0, 0).color(r, g, b, centerAlpha).endVertex();
        
        int segments = 16;
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            float sx = (float) Math.sin(angle) * radius;
            float sy = (float) Math.cos(angle) * radius;
            buf.pos(sx, sy, 0).color(r, g, b, edgeAlpha).endVertex();
        }

        tess.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onRenderWorldLast(net.minecraftforge.client.event.RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        for (EntityPlayer player : mc.world.playerEntities) {
            float intensity = HeadlampLightManager.getSmoothIntensity(player);
            if (intensity <= 0.01f) continue;
            
            java.nio.FloatBuffer buf = savedMatrices.get(player.getUniqueID());
            if (buf == null) continue;

            GlStateManager.pushMatrix();
            org.lwjgl.opengl.GL11.glLoadMatrix(buf);

            // Save states
            boolean blendEnabled = org.lwjgl.opengl.GL11.glGetBoolean(org.lwjgl.opengl.GL11.GL_BLEND);
            boolean lightingEnabled = org.lwjgl.opengl.GL11.glGetBoolean(org.lwjgl.opengl.GL11.GL_LIGHTING);
            boolean alphaTestEnabled = org.lwjgl.opengl.GL11.glGetBoolean(org.lwjgl.opengl.GL11.GL_ALPHA_TEST);
            boolean depthMaskEnabled = org.lwjgl.opengl.GL11.glGetBoolean(org.lwjgl.opengl.GL11.GL_DEPTH_WRITEMASK);
            
            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit);
            boolean lightmapEnabled = org.lwjgl.opengl.GL11.glGetBoolean(org.lwjgl.opengl.GL11.GL_TEXTURE_2D);
            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.defaultTexUnit);

            // Apply states for beam
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(org.lwjgl.opengl.GL11.GL_SRC_ALPHA, org.lwjgl.opengl.GL11.GL_ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableAlpha();
            
            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.defaultTexUnit);
            
            // Draw smaller beam (length 0.2)
            drawLightCone(0.2f, 0.25f, intensity);
            
            // Restore states
            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit);
            if (lightmapEnabled) GlStateManager.enableTexture2D(); else GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.defaultTexUnit);
            
            if (alphaTestEnabled) GlStateManager.enableAlpha(); else GlStateManager.disableAlpha();
            GlStateManager.depthMask(depthMaskEnabled);
            if (lightingEnabled) GlStateManager.enableLighting(); else GlStateManager.disableLighting();
            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.blendFunc(org.lwjgl.opengl.GL11.GL_SRC_ALPHA, org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (!blendEnabled) GlStateManager.disableBlend();

            GlStateManager.popMatrix();
        }
        
        // Clear matrices for the next frame
        savedMatrices.clear();
    }

    public static void renderConeAndDecalFromModel(EntityPlayer player, GeoArmorModel model, float scale, float partialTicks) {
        float intensity = HeadlampLightManager.getSmoothIntensity(player);
        if (intensity <= 0.01f) return;

        Minecraft mc = Minecraft.getMinecraft();
        
        GlStateManager.pushMatrix();
        
        if (player.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F); // ModelBiped does this
        }
        
        model.bipedHead.postRender(scale);

        // Position of lightsource: 
        // Start from right temple (-0.3125f, -0.375f, -0.21875f).
        // Move 0.5 pixels forward (-0.03125f to Z).
        // Move 0.5 pixels down (+0.03125f to Y).
        // Move 0.5 pixels left (+0.03125f to X).
        // New translation:
        GlStateManager.translate(-0.28125f, -0.34375f, -0.25f);
        GlStateManager.rotate(180, 0, 1, 0);
        
        // Shift start by 0.2 pixels right (X) and 0.3 pixels forward (Z)
        GlStateManager.translate(0.2f * 0.0625f, 0.0f, 0.3f * 0.0625f);

        net.minecraft.item.ItemStack headSlot = player.getItemStackFromSlot(net.minecraft.inventory.EntityEquipmentSlot.HEAD);
        if (!headSlot.isEmpty() && (headSlot.getItem() instanceof com.voltyx.mwccf.mcore.ItemCustomArmor || headSlot.getItem() instanceof com.voltyx.mwccf.geo.ItemGeoArmor)) {
            // Shift forward by 0.7 blocks (or pixels, using standard block scaling)
            GlStateManager.translate(0.0f, 0.0f, 0.7f * 0.0625f);
        }

        // Save the exact modelview matrix from the entity rendering pass
        java.nio.FloatBuffer buf = org.lwjgl.BufferUtils.createFloatBuffer(16);
        org.lwjgl.opengl.GL11.glGetFloat(org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX, buf);
        savedMatrices.put(player.getUniqueID(), buf);
        
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        EntityPlayer self = Minecraft.getMinecraft().player;
        if (self == null || self.world == null) return;

        float maxGlare = 0f;
        for (EntityPlayer other : self.world.playerEntities) {
            if (other == self) continue;
            float intensity = HeadlampLightManager.getSmoothIntensity(other);
            if (intensity <= 0.01f) continue;

            Vec3d toOther = new Vec3d(other.posX - self.posX, other.posY + other.getEyeHeight() - (self.posY + self.getEyeHeight()), other.posZ - self.posZ).normalize();
            Vec3d otherLook = other.getLookVec();
            Vec3d selfLook = self.getLookVec();

            double facingSelf = -toOther.dotProduct(otherLook);
            double lookingAtSource = toOther.dotProduct(selfLook);
            double dist = self.getDistance(other);

            if (facingSelf > 0.97 && lookingAtSource > 0.97 && dist < 15) {
                float strength = (float) ((facingSelf - 0.97) / 0.03) * (1f - (float) dist / 15f) * intensity;
                maxGlare = Math.max(maxGlare, strength);
            }
        }

        if (maxGlare > 0.01f) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            // Using warm color R=255, G=217, B=153 instead of FFFFFF
            Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ((int)(maxGlare * 220) << 24) | 0xFFD999);
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
        }
    }
}
