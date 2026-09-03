package com.voltyx.mwccf.sins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.function.Supplier;

/**
 * Thin transition screen: keeps rendering whatever screen was open (e.g. the inventory)
 * behind a black overlay that fades from transparent to fully opaque, then swaps to the
 * target screen (e.g. GuiSevenScreen) once the fade completes.
 *
 * Usage (instead of calling `mc.displayGuiScreen(new GuiSevenScreen(...))` directly):
 *
 *   mc.displayGuiScreen(new GuiFadeToBlackTransition(
 *       mc.currentScreen,
 *       () -> new GuiSevenScreen(mc.currentScreen, playerUUID)
 *   ));
 *
 * The `targetSupplier` is a Supplier so the target screen is only constructed once the
 * fade finishes (avoids building GuiSevenScreen — and its initGui/capability lookups —
 * before it's actually needed).
 */
public class GuiFadeToBlackTransition extends GuiScreen {

    private static final long FADE_DURATION_MS = 260L;

    private final GuiScreen previousScreen;
    private final Supplier<GuiScreen> targetSupplier;
    private long startTime = -1L;
    private boolean switched = false;

    public GuiFadeToBlackTransition(GuiScreen previousScreen, Supplier<GuiScreen> targetSupplier) {
        this.previousScreen = previousScreen;
        this.targetSupplier = targetSupplier;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return previousScreen != null && previousScreen.doesGuiPauseGame();
    }

    @Override
    public void initGui() {
        super.initGui();
        if (this.startTime < 0) {
            this.startTime = System.currentTimeMillis();
        }
        if (this.previousScreen != null) {
            this.previousScreen.setWorldAndResolution(this.mc, this.width, this.height);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw the previous screen underneath so the darkening reads as "this screen
        // fading to black", not a jarring cut to a blank background.
        if (this.previousScreen != null) {
            try {
                this.previousScreen.drawScreen(mouseX, mouseY, partialTicks);
            } catch (Throwable ignored) {
                // If the old screen can't safely redraw itself standalone (e.g. it
                // expects to be the active screen for some state), just skip it —
                // worst case this frame shows solid black a touch earlier.
            }
        }

        long elapsed = System.currentTimeMillis() - this.startTime;
        float progress = Math.min(1.0f, elapsed / (float) FADE_DURATION_MS);
        int alpha = (int) (progress * 255.0f);
        int color = (alpha << 24);
        drawRect(0, 0, this.width, this.height, color);

        if (progress >= 1.0f && !this.switched) {
            this.switched = true;
            GuiScreen target = this.targetSupplier.get();
            Minecraft.getMinecraft().displayGuiScreen(target);
        }
    }
}
