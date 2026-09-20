package com.voltyx.mwccf.immersiveui.nea;

import net.minecraft.client.gui.GuiScreen;
import javax.annotation.Nullable;

public class NEAHelper {

    private static GuiScreen currentDrawnScreen = null;
    private static float openAnimationValue = 1.0F;
    private static int mouseX = 0;
    private static int mouseY = 0;

    public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    public static void setMouse(int x, int y) {
        mouseX = x;
        mouseY = y;
    }

    public static @Nullable GuiScreen getCurrentDrawnScreen() {
        return currentDrawnScreen;
    }

    public static void setCurrentDrawnScreen(@Nullable GuiScreen screen) {
        currentDrawnScreen = screen;
    }

    public static float getCurrentOpenAnimationValue() {
        return openAnimationValue;
    }

    public static void setCurrentOpenAnimationValue(float value) {
        openAnimationValue = value;
    }

    public static boolean isCurrentGuiAnimating() {
        return currentDrawnScreen != null && openAnimationValue < 1.0F;
    }

    public static long time() {
        return System.nanoTime() / 1_000_000L;
    }

    public static int getAlpha(int argb) {
        return (argb >> 24) & 255;
    }

    public static int withAlpha(int argb, int alpha) {
        argb &= ~(0xFF << 24);
        return argb | (alpha << 24);
    }

    public static int withAlpha(int argb, float alpha) {
        return withAlpha(argb, (int) (alpha * 255));
    }
}
