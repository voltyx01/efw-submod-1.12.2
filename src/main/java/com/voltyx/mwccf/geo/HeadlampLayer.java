package com.voltyx.mwccf.geo;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class HeadlampLayer implements LayerRenderer<AbstractClientPlayer> {

    private final RenderPlayer renderer;
    private final String skinTypeKey;

    public HeadlampLayer(RenderPlayer renderer, String skinTypeKey) {
        this.renderer = renderer;
        this.skinTypeKey = skinTypeKey;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player,
                              float limbSwing, float limbSwingAmount, float delta,
                              float age, float yaw, float pitch, float scale) {
        if (player == net.minecraft.client.Minecraft.getMinecraft().player && 
            net.minecraft.client.Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            return;
        }

        if (!HeadlampRenderer.hasHeadlampEquipped(player)) return;

        String playerSkin = "slim".equals(player.getSkinType()) ? "slim" : "default";
        if (!playerSkin.equals(this.skinTypeKey)) return;

        GeoArmorModel model = HeadlampRenderer.getHeadlampModel();
        if (model == null) return;

        model.setModelAttributes(this.renderer.getMainModel());
        model.setLivingAnimations(player, limbSwing, limbSwingAmount, delta);

        // Tell GeoArmorModel we are rendering the HEAD slot so it shows the head parts
        model.currentSlot = EntityEquipmentSlot.HEAD;

        this.renderer.bindTexture(HeadlampRenderer.getHeadlampTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();

        model.syncedModel = this.renderer.getMainModel();
        model.render(player, limbSwing, limbSwingAmount, age, yaw, pitch, scale);
        
        // Draw the cone and the decal from the exact model's head bone!
        HeadlampRenderer.renderConeAndDecalFromModel(player, model, scale, delta);
        
        model.syncedModel = null;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
