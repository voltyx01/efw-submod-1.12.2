package com.voltyx.mwccf.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
public class HitmarkerRenderer {

    // РЕГАЕМ ОБЕ ТЕКСТУРЫ
    private static final ResourceLocation TEX_NORMAL = new ResourceLocation("mwccf", "textures/gui/hit.png");
    private static final ResourceLocation TEX_HEAD = new ResourceLocation("mwccf", "textures/gui/headhit.png"); // Твоя
                                                                                                                // новая
                                                                                                                // текстура

    private static long showTime = 0;
    private static int hitType = 0; // 0 = Тело, 1 = Голова, 2 = Килл
    private static final long FADE_DURATION = 500;

    public static void trigger(int type) {
        hitType = type;
        showTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public static void onRenderHUD(RenderGameOverlayEvent.Post event) {
        if (!efw.biomeinfo.MwccfConfig.combatFeedback.enableHitmarkers)
            return;

        if (event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
            return;

        long elapsed = System.currentTimeMillis() - showTime;
        if (elapsed > FADE_DURATION)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = event.getResolution();

        int size = 16;
        int x = res.getScaledWidth() / 2 - (size / 2); // Твоя калибровка
        int y = res.getScaledHeight() / 2 - (size / 2);

        float alpha = 1.0f - ((float) elapsed / FADE_DURATION);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);

        // ЛОГИКА ВЫБОРА ТЕКСТУРЫ И ЦВЕТА
        if (hitType == 1) {
            // ХЭДШОТ: Берем текстуру головы и красим в красный (или оставь 1,1,1 для
            // белого)
            mc.getTextureManager().bindTexture(TEX_HEAD);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);

        } else if (hitType == 2) {
            // КИЛЛ: Берем обычную текстуру и красим в красный
            mc.getTextureManager().bindTexture(TEX_NORMAL);
            GlStateManager.color(1.0F, 0.0F, 0.0F, alpha);

        } else {
            // ТЕЛО: Берем обычную текстуру и оставляем белой
            mc.getTextureManager().bindTexture(TEX_NORMAL);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        }

        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, size, size, size, size);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}