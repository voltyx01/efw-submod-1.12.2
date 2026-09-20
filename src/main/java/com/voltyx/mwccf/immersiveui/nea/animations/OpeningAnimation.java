package com.voltyx.mwccf.immersiveui.nea.animations;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import com.voltyx.mwccf.immersiveui.nea.NEAHelper;
import com.voltyx.mwccf.immersiveui.nea.api.IAnimatedScreen;
import com.voltyx.mwccf.immersiveui.nea.util.Interpolations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiOpenEvent;

public class OpeningAnimation {

    public static boolean onGuiOpen(GuiOpenEvent event) {
        if (onGuiOpen(event.getGui())) {
            event.setCanceled(true);
            return true;
        }
        return false;
    }

    public static boolean onGuiOpen(GuiScreen screen) {
        if (screen instanceof IAnimatedScreen) {
            IAnimatedScreen animatedScreen = (IAnimatedScreen) screen;
            if (Minecraft.getMinecraft().currentScreen == null) {
                animate(animatedScreen, true);
            }
        } else if (Minecraft.getMinecraft().currentScreen == lastGui && screen == null && !shouldCloseLast) {
            if (animatedGui == null || getValue(animatedGui) >= 1.0F || startTime > 0) {
                animate(lastGui, false);
            }
            return true;
        }
        return false;
    }

    private static IAnimatedScreen lastGui;
    private static IAnimatedScreen animatedGui;
    private static long startTime = 0;
    private static boolean shouldCloseLast = false;

    public static void animate(IAnimatedScreen container, boolean open) {
        if (ImmersiveUIConfig.openingAnimationTime <= 0 || ImmersiveUIConfig.isBlacklisted(container)) return;
        animatedGui = container;
        lastGui = container;
        startTime = NEAHelper.time();
        if (!open) {
            startTime = -startTime;
            Minecraft.getMinecraft().setIngameFocus();
        }
    }

    public static float getScale(GuiScreen screen) {
        return screen instanceof IAnimatedScreen ? getScale((IAnimatedScreen) screen) : 1.0F;
    }

    public static float getScale(IAnimatedScreen container) {
        float min = ImmersiveUIConfig.openingStartScale, max = 1.0F;
        return Interpolations.lerp(min, max, getValue(container));
    }

    public static float getValue(GuiScreen screen) {
        return screen instanceof IAnimatedScreen ? getValue((IAnimatedScreen) screen) : 1.0F;
    }

    public static float getValue(IAnimatedScreen container) {
        if (shouldCloseLast) return 0.001F;
        if (animatedGui != container) return 1.0F;
        float val = (NEAHelper.time() - Math.abs(startTime)) / (float) ImmersiveUIConfig.openingAnimationTime;
        if (startTime < 0) {
            val = 1.0F - val;
            if (val <= 0) {
                animatedGui = null;
                shouldCloseLast = true;
                return 0.0F;
            }
        } else if (val >= 1.0F) {
            animatedGui = null;
            return 1.0F;
        }

        return ImmersiveUIConfig.openingAnimationCurve.interpolate(0.0F, 1.0F, val);
    }

    public static boolean handleScale(GuiScreen screen, boolean translateToPanel) {
        return screen instanceof IAnimatedScreen && handleScale((IAnimatedScreen) screen, translateToPanel);
    }

    public static boolean handleScale(IAnimatedScreen screen, boolean translateToPanel) {
        float scale = getScale(screen);
        if (scale == 1.0F || ImmersiveUIConfig.openingAnimationTime <= 0) return false;
        if (translateToPanel) GlStateManager.translate(screen.nea$getX(), screen.nea$getY(), 0);
        GlStateManager.translate(screen.nea$getWidth() / 2.0F, screen.nea$getHeight() / 2.0F, 0);
        GlStateManager.scale(scale, scale, 1.0F);
        GlStateManager.translate(-screen.nea$getWidth() / 2.0F, -screen.nea$getHeight() / 2.0F, 0);
        if (translateToPanel) GlStateManager.translate(-screen.nea$getX(), -screen.nea$getY(), 0);
        return true;
    }

    public static boolean isAnimating(GuiScreen screen) {
        return screen instanceof IAnimatedScreen && isAnimating((IAnimatedScreen) screen);
    }

    public static boolean isAnimating(IAnimatedScreen screen) {
        return getValue(screen) < 1.0F;
    }

    public static boolean isAnimatingClose(GuiScreen screen) {
        return screen instanceof IAnimatedScreen && isAnimatingClose((IAnimatedScreen) screen);
    }

    public static boolean isAnimatingClose(IAnimatedScreen screen) {
        return isAnimating(screen) && startTime < 0;
    }

    public static void checkGuiToClose() {
        if (shouldCloseLast && lastGui != null) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            shouldCloseLast = false;
            lastGui = null;
        }
    }
}
