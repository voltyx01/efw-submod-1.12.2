package com.voltyx.mwccf.client.inspect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InspectTransitionHandler {

    private static boolean active = false;
    private static long startTime = 0;
    private static final float HALF_DURATION_MS = 190.0f; // 190мс затемнение + 190мс осветление = 380мс всего
    private static GuiScreen pendingScreen = null;
    private static boolean screenSwapped = false;
    private static boolean justSwapped = false;

    public static void startTransitionToScreen(GuiScreen target, GuiScreen current) {
        pendingScreen = target;
        screenSwapped = false;
        justSwapped = false;
        active = true;
        startTime = System.currentTimeMillis();
    }

    public static void startTransition(ItemStack stack, GuiScreen current) {
        if (stack == null || stack.isEmpty() || ItemInspectConfig.isBlacklisted(stack)) {
            return;
        }
        startTransitionToScreen(new GuiItemInspect(stack, current), current);
    }

    // Рендерим в самом конце каждого кадра (поверх ЛЮБОГО GUI или мира)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !active) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        long now = System.currentTimeMillis();

        if (justSwapped) {
            justSwapped = false;
            startTime = now;
        }

        float elapsed = (now - startTime);

        float alpha;
        if (!screenSwapped) {
            if (elapsed < HALF_DURATION_MS) {
                // Фаза 1: Затемнение (0.0 -> 1.0)
                alpha = elapsed / HALF_DURATION_MS;
            } else {
                // Достигли пика темноты — переключаем экран!
                screenSwapped = true;
                justSwapped = true;
                GuiScreen toOpen = pendingScreen;
                pendingScreen = null;
                mc.displayGuiScreen(toOpen);
                alpha = 1.0f;
            }
        } else {
            // Фаза 2: Осветление (1.0 -> 0.0) в уже открытом GUI
            if (elapsed >= HALF_DURATION_MS) {
                active = false;
                alpha = 0.0f;
            } else {
                alpha = 1.0f - (elapsed / HALF_DURATION_MS);
            }
        }

        if (alpha > 0.0f) {
            ScaledResolution res = new ScaledResolution(mc);
            int width = res.getScaledWidth();
            int height = res.getScaledHeight();

            float clampedAlpha = Math.max(0.0f, Math.min(1.0f, alpha));

            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );
            GlStateManager.disableTexture2D();
            GlStateManager.color(0.0F, 0.0F, 0.0F, clampedAlpha);

            net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
            net.minecraft.client.renderer.BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION);
            bufferbuilder.pos(0.0D, (double) height, 0.0D).endVertex();
            bufferbuilder.pos((double) width, (double) height, 0.0D).endVertex();
            bufferbuilder.pos((double) width, 0.0D, 0.0D).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, 0.0D).endVertex();
            tessellator.draw();

            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }
}