package com.voltyx.mwccf.client.loading;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.MinecraftError;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLLog;

import java.io.IOException;
import java.lang.reflect.Field;

public class CustomLoadingScreenRenderer extends LoadingScreenRenderer {

    private final Minecraft mc;
    private final Framebuffer framebuffer;
    private long systemTime = Minecraft.getSystemTime();
    private boolean loadingSuccess;
    private String message = "";
    private String currentlyDisplayedText = "";

    private static Field runningField = null;
    static {
        try {
            runningField = Minecraft.class.getDeclaredField("field_71425_J");
            runningField.setAccessible(true);
        } catch (Throwable t1) {
            try {
                runningField = Minecraft.class.getDeclaredField("running");
                runningField.setAccessible(true);
            } catch (Throwable ignored) {}
        }
    }

    public static boolean isRunning() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) return false;
        if (runningField != null) {
            try {
                return runningField.getBoolean(mc);
            } catch (Throwable ignored) {}
        }
        return true;
    }

    public CustomLoadingScreenRenderer(Minecraft mc) {
        super(mc);
        this.mc = mc;
        // ВАЖНО: Включаем буфер глубины (true), иначе 3D предметы (например из MWC)
        // не будут рендериться или будут отсекаться!
        this.framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        this.framebuffer.setFramebufferFilter(9728);
    }

    @Override
    public void resetProgressAndMessage(String message) {
        this.loadingSuccess = false;
        this.currentlyDisplayedText = message;
        // Выбираем предмет сразу, чтобы не было "голого" фона перед появлением нашей менюшки
        // Но только если игра не закрывается (чтобы при выходе из игры не мелькало)
        if (isRunning()) {
            ItemLoadingScreenRenderer.pickRandom();
        }
    }

    @Override
    public void displaySavingString(String message) {
        this.loadingSuccess = true;
        this.currentlyDisplayedText = message;
        if (isRunning()) {
            ItemLoadingScreenRenderer.pickRandom();
        }
    }

    @Override
    public void displayLoadingString(String message) {
        if (!isRunning()) {
            if (!this.loadingSuccess)
                throw new MinecraftError();
            return;
        }
        this.systemTime = 0L;
        this.message = message;
        ItemLoadingScreenRenderer.pickRandom(); // На случай если это вызвали раньше остальных
        this.setLoadingProgress(-1);
        this.systemTime = 0L;
    }

    @Override
    public void setLoadingProgress(int progress) {
        if (!isRunning()) {
            if (!this.loadingSuccess)
                throw new MinecraftError();
            return;
        }

        long now = Minecraft.getSystemTime();
        if (now - this.systemTime < 100L) return;
        this.systemTime = now;

        ScaledResolution res = new ScaledResolution(this.mc);
        int scaleFactor = res.getScaleFactor();
        int screenW = res.getScaledWidth();
        int screenH = res.getScaledHeight();

        if (OpenGlHelper.isFramebufferEnabled()) {
            this.framebuffer.framebufferClear();
        } else {
            GlStateManager.clear(256);
        }

        this.framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        // ВАЖНО: Используем Z-clip от 1000 до 3000, как в обычных GuiScreen,
        // чтобы 3D предметы не отсекались по глубине.
        GlStateManager.ortho(0.0D, res.getScaledWidth_double(), res.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);

        if (!OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.clear(16640);
        }

        try {
            if (!FMLClientHandler.instance().handleLoadingScreen(res)) {
                ItemLoadingScreenRenderer.render(screenW, screenH, this.message, this.currentlyDisplayedText);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.framebuffer.unbindFramebuffer();

        if (OpenGlHelper.isFramebufferEnabled()) {
            this.framebuffer.framebufferRender(screenW * scaleFactor, screenH * scaleFactor);
        }

        org.lwjgl.opengl.GL11.glFlush();
        this.mc.updateDisplay();

        try {
            Thread.yield();
        } catch (Exception ignored) {}
    }

    @Override
    public void setDoneWorking() {
    }
}