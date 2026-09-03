package com.voltyx.mwccf.geo;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

/**
 * Рендерит браслет как слой поверх игрока (3rd person + другие игроки).
 *
 * ВАЖНО: К каждому из двух рендереров (default/slim) прикреплён свой слой.
 * Чтобы не рисовать браслет дважды, каждый слой проверяет тип скина игрока
 * и рендерит только если скин совпадает с типом рендерера.
 */
public class BraceletLayer implements LayerRenderer<AbstractClientPlayer> {

    private final RenderPlayer renderer;
    private final String skinTypeKey; // "default" или "slim"

    public BraceletLayer(RenderPlayer renderer, String skinTypeKey) {
        this.renderer = renderer;
        this.skinTypeKey = skinTypeKey;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player,
                              float limbSwing, float limbSwingAmount, float delta,
                              float age, float yaw, float pitch, float scale) {
        // Не рисуем слой 3-го лица для себя в 1-м лице
        if (player == net.minecraft.client.Minecraft.getMinecraft().player && 
            net.minecraft.client.Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            return;
        }

        if (!BraceletUI.hasBraceletEquipped(player)) return;

        // Только рендерер, соответствующий типу скина игрока, рисует браслет
        String playerSkin = "slim".equals(player.getSkinType()) ? "slim" : "default";
        if (!playerSkin.equals(this.skinTypeKey)) return;

        boolean isSlim = "slim".equals(playerSkin);
        GeoArmorModel model = isSlim
            ? BraceletInspectHandler.getSlimModel()
            : BraceletInspectHandler.getNormalModel();
        if (model == null) return;

        // Синхронизируем анимацию с основной моделью
        model.setModelAttributes(this.renderer.getMainModel());
        model.setLivingAnimations(player, limbSwing, limbSwingAmount, delta);

        // currentSlot = null → GeoArmorModel рендерит только левую руку
        model.currentSlot = null;

        this.renderer.bindTexture(BraceletInspectHandler.getBraceletTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();

        model.syncedModel = this.renderer.getMainModel();
        model.render(player, limbSwing, limbSwingAmount, age, yaw, pitch, scale);
        model.syncedModel = null;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
