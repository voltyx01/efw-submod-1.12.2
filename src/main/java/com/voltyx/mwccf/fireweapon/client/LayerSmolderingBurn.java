package com.voltyx.mwccf.fireweapon.client;

import com.voltyx.mwccf.fireweapon.smolder.SmolderingClientManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Renders the per-entity UV stain generated from each weapon impact.
 */
@SideOnly(Side.CLIENT)
public class LayerSmolderingBurn implements LayerRenderer<EntityLivingBase> {

    private final RenderLivingBase<?> renderer;

    public LayerSmolderingBurn(RenderLivingBase<?> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount,
                              float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        renderSmolder(this.renderer, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public static void renderSmolder(RenderLivingBase<?> renderer, EntityLivingBase entity, float limbSwing, float limbSwingAmount,
                                     float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        // Disabled burn mark overlay on mobs per user request - sparks are emitted directly in SmolderingClientManager
        if (true) return;
        if (renderer == null || renderer.getMainModel() == null) return;
        ResourceLocation overlay = SmolderingClientManager.getOverlayTexture(entity);
        if (overlay == null) return;
        ResourceLocation emissive = SmolderingClientManager.getEmissiveTexture(entity);
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(overlay);
        renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        if (emissive != null) {
            GlStateManager.disableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.depthFunc(GL11.GL_EQUAL);
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
            net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(emissive);
            renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(GL11.GL_LEQUAL);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        }

        GlStateManager.enableLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
