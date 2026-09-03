package efw.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SplashRenderer {

    private static final ResourceLocation LOCATION_MOJANG_PNG = new ResourceLocation("textures/gui/title/mojang.png");

    public static int savedWidth = 854;
    public static int savedHeight = 480;
    public static boolean savedFullscreen = false;

    public static int windowWidth = 512;
    public static int windowHeight = 640;

    public static volatile boolean isSplashPhase = true;

    private static BufferedImage cachedLogo = null;
    private static int logoTexId = 0;
    private static long lastRenderTime = 0;

    // -------------------------------------------------------------------------
    // Called from MixinMinecraftSplash.setInitialDisplayMode
    // -------------------------------------------------------------------------
    public static void initDisplayMode(Minecraft mc) throws LWJGLException {
        // Always write splash.properties with enabled=false so Forge's white screen never shows
        if (mc != null && mc.gameDir != null) {
            File splashCfg = new File(mc.gameDir, "config/splash.properties");
            try {
                splashCfg.getParentFile().mkdirs();
                java.nio.file.Files.write(splashCfg.toPath(), "enabled=false\n".getBytes(StandardCharsets.UTF_8));
            } catch (Throwable ignored) {}
        }

        cachedLogo = findMojangPng(mc);

        int rawLogoW = 256;
        int rawLogoH = 320;
        if (cachedLogo != null) {
            rawLogoW = cachedLogo.getWidth();
            rawLogoH = cachedLogo.getHeight();
        }

        float scale;
        if (rawLogoW >= 600 || rawLogoH >= 600) {
            scale = 1.0f;
        } else if (rawLogoW <= 350 && rawLogoH <= 350) {
            scale = 2.0f;
        } else {
            scale = 1.5f;
        }

        windowWidth = Math.round(rawLogoW * scale);
        windowHeight = Math.round(rawLogoH * scale);

        savedWidth = mc.displayWidth;
        savedHeight = mc.displayHeight;
        savedFullscreen = mc.isFullScreen() || (mc.gameSettings != null && mc.gameSettings.fullScreen);

        mc.displayWidth = windowWidth;
        mc.displayHeight = windowHeight;

        Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
        DisplayMode desktop = Display.getDesktopDisplayMode();
        int posX = Math.max(0, (desktop.getWidth() - windowWidth) / 2);
        int posY = Math.max(0, (desktop.getHeight() - windowHeight) / 2);
        Display.setLocation(posX, posY);
    }

    // -------------------------------------------------------------------------
    // Called after Display window is created — upload texture and draw first frame
    // -------------------------------------------------------------------------
    public static void onDisplayCreated() {
        if (cachedLogo != null) {
            logoTexId = createTexture(cachedLogo);
        }
        renderFrameImmediate();
    }

    // -------------------------------------------------------------------------
    // Called from FMLClientHandler.processWindowMessages on every progress step.
    // Renders on the MAIN thread — no GL context conflicts with other mods.
    // -------------------------------------------------------------------------
    public static void onProgressStep() {
        if (!isSplashPhase) return;
        long now = Minecraft.getSystemTime();
        // Render at most every 16 ms (~60 fps) — avoid hammering on slow steps
        if (now - lastRenderTime >= 16) {
            lastRenderTime = now;
            renderFrameImmediate();
        }
    }

    // -------------------------------------------------------------------------
    // Main-thread render: clears screen, draws logo + spinner, flips buffer
    // -------------------------------------------------------------------------
    public static void renderFrameImmediate() {
        if (!isSplashPhase) return;
        try {
            int dispW = Display.getWidth();
            int dispH = Display.getHeight();
            if (dispW <= 0 || dispH <= 0) return;

            // Ensure we have the texture uploaded
            if (logoTexId == 0 && cachedLogo != null) {
                logoTexId = createTexture(cachedLogo);
            }

            // Save OpenGL state so we don't corrupt the game's state
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix();

            // Setup 2D ortho projection filling the whole window
            GL11.glViewport(0, 0, dispW, dispH);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0, dispW, dispH, 0.0, -1.0, 1.0);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);

            // Clear to black, then draw logo — this covers any JEI/mod renders
            GL11.glClearColor(0f, 0f, 0f, 1f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            // Draw logo edge-to-edge
            if (logoTexId != 0) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, logoTexId);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(0f, 0f); GL11.glVertex2f(0f,     0f);
                GL11.glTexCoord2f(0f, 1f); GL11.glVertex2f(0f,     dispH);
                GL11.glTexCoord2f(1f, 1f); GL11.glVertex2f(dispW,  dispH);
                GL11.glTexCoord2f(1f, 0f); GL11.glVertex2f(dispW,  0f);
                GL11.glEnd();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }

            // Draw loading spinner in the center
            drawLoadingWheel(dispW, dispH);

            // Restore GL state
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();
            GL11.glPopAttrib();

            Display.update();
        } catch (Throwable ignored) {}
    }

    private static int createTexture(BufferedImage img) {
        if (img == null) return 0;
        try {
            int w = img.getWidth();
            int h = img.getHeight();
            int[] pixels = new int[w * h];
            img.getRGB(0, 0, w, h, pixels, 0, w);

            ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = pixels[y * w + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                    buffer.put((byte) ((pixel >> 8)  & 0xFF)); // G
                    buffer.put((byte) (pixel         & 0xFF)); // B
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // A
                }
            }
            buffer.flip();

            int id = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            return id;
        } catch (Throwable e) {
            return 0;
        }
    }

    private static void drawLoadingWheel(int screenWidth, int screenHeight) {
        int cx = screenWidth / 2;
        int cy = screenHeight / 2;
        int size = Math.min(40, Math.max(20, Math.min(screenWidth, screenHeight) / 20));
        long time = Minecraft.getSystemTime();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Faint square outline (track)
        drawRect(cx - size, cy - size, cx + size, cy - size + 2, 0x55FFFFFF);
        drawRect(cx + size - 2, cy - size, cx + size, cy + size, 0x55FFFFFF);
        drawRect(cx - size, cy + size - 2, cx + size, cy + size, 0x55FFFFFF);
        drawRect(cx - size, cy - size, cx - size + 2, cy + size, 0x55FFFFFF);

        int perimeter = size * 8;
        int pos = (int) ((time / 10) % perimeter);
        int dotCount = 24;
        int dotSize = Math.max(2, size / 7);

        for (int i = 0; i < dotCount; i++) {
            int dotPos = (pos - i * 2 + perimeter * 100) % perimeter;
            int dx, dy;
            if (dotPos < size * 2) {
                dx = -size + dotPos; dy = -size;
            } else if (dotPos < size * 4) {
                dx = size; dy = -size + (dotPos - size * 2);
            } else if (dotPos < size * 6) {
                dx = size - (dotPos - size * 4); dy = size;
            } else {
                dx = -size; dy = size - (dotPos - size * 6);
            }
            int alpha = (int) (255.0f * (1.0f - i / (float) dotCount));
            drawRect(cx + dx - dotSize, cy + dy - dotSize, cx + dx + dotSize, cy + dy + dotSize, (alpha << 24) | 0xFFFFFF);
        }
    }

    private static void drawRect(int left, int top, int right, int bottom, int color) {
        float a = ((color >> 24) & 255) / 255.0F;
        float r = ((color >> 16) & 255) / 255.0F;
        float g = ((color >> 8)  & 255) / 255.0F;
        float b = (color         & 255) / 255.0F;
        GL11.glColor4f(r, g, b, a);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(left,  bottom);
        GL11.glVertex2f(right, bottom);
        GL11.glVertex2f(right, top);
        GL11.glVertex2f(left,  top);
        GL11.glEnd();
    }

    // -------------------------------------------------------------------------
    // Resource pack logo discovery
    // -------------------------------------------------------------------------
    private static BufferedImage findMojangPng(Minecraft mc) {
        if (mc != null && mc.gameDir != null) {
            // 1. Active resource packs from options.txt
            File optionsFile = new File(mc.gameDir, "options.txt");
            if (optionsFile.exists()) {
                try {
                    List<String> lines = java.nio.file.Files.readAllLines(optionsFile.toPath(), StandardCharsets.UTF_8);
                    for (String line : lines) {
                        if (line.startsWith("resourcePacks:")) {
                            String str = line.substring("resourcePacks:".length()).trim();
                            if (str.startsWith("[") && str.endsWith("]")) {
                                String[] items = str.substring(1, str.length() - 1).split(",");
                                for (int i = items.length - 1; i >= 0; i--) {
                                    String item = items[i].trim();
                                    if (item.startsWith("\"") && item.endsWith("\"") && item.length() >= 2)
                                        item = item.substring(1, item.length() - 1);
                                    if (item.isEmpty()) continue;
                                    BufferedImage img = readLogoFromPack(new File(mc.gameDir, "resourcepacks/" + item));
                                    if (img != null) return img;
                                }
                            }
                        }
                    }
                } catch (Throwable ignored) {}
            }

            // 2. Any pack in resourcepacks/
            File rpDir = new File(mc.gameDir, "resourcepacks");
            if (rpDir.exists() && rpDir.isDirectory()) {
                File[] files = rpDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        BufferedImage img = readLogoFromPack(f);
                        if (img != null) return img;
                    }
                }
            }

            // 3. gameDir/resources/
            File rf = new File(mc.gameDir, "resources/assets/minecraft/textures/gui/title/mojang.png");
            if (rf.exists()) {
                try (InputStream is = new FileInputStream(rf)) {
                    BufferedImage img = ImageIO.read(is);
                    if (img != null) return img;
                } catch (Throwable ignored) {}
            }
        }

        // 4. Classpath
        try (InputStream is = SplashRenderer.class.getResourceAsStream("/assets/minecraft/textures/gui/title/mojang.png")) {
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                if (img != null) return img;
            }
        } catch (Throwable ignored) {}

        // 5. Default resource pack fallback
        if (mc != null && mc.defaultResourcePack != null) {
            try (InputStream is = mc.defaultResourcePack.getInputStream(LOCATION_MOJANG_PNG)) {
                if (is != null) return ImageIO.read(is);
            } catch (Throwable ignored) {}
        }

        return null;
    }

    private static BufferedImage readLogoFromPack(File packFile) {
        if (packFile == null || !packFile.exists()) return null;
        if (packFile.isDirectory()) {
            File logoFile = new File(packFile, "assets/minecraft/textures/gui/title/mojang.png");
            if (logoFile.exists()) {
                try (InputStream is = new FileInputStream(logoFile)) {
                    return ImageIO.read(is);
                } catch (Throwable ignored) {}
            }
        } else if (packFile.getName().endsWith(".zip") || packFile.getName().endsWith(".jar")) {
            try (ZipFile zip = new ZipFile(packFile)) {
                ZipEntry entry = zip.getEntry("assets/minecraft/textures/gui/title/mojang.png");
                if (entry != null) {
                    try (InputStream is = zip.getInputStream(entry)) {
                        return ImageIO.read(is);
                    }
                }
            } catch (Throwable ignored) {}
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Restore window to game resolution after splash
    // -------------------------------------------------------------------------
    public static void restoreDisplayMode(Minecraft mc) {
        if (!isSplashPhase) return;
        isSplashPhase = false;
        cachedLogo = null;
        if (logoTexId != 0) {
            try { GL11.glDeleteTextures(logoTexId); } catch (Throwable ignored) {}
            logoTexId = 0;
        }
        try {
            if (savedFullscreen) {
                if (mc.gameSettings != null) mc.gameSettings.fullScreen = true;
                if (!mc.isFullScreen()) mc.toggleFullscreen();
            } else {
                Display.setDisplayMode(new DisplayMode(savedWidth, savedHeight));
                mc.displayWidth = savedWidth;
                mc.displayHeight = savedHeight;
                mc.resize(savedWidth, savedHeight);
                DisplayMode desktop = Display.getDesktopDisplayMode();
                int posX = Math.max(0, (desktop.getWidth() - savedWidth) / 2);
                int posY = Math.max(0, (desktop.getHeight() - savedHeight) / 2);
                Display.setLocation(posX, posY);
                // setResizable MUST be last so Windows applies the WS_MAXIMIZEBOX style
                // after all display mode changes, then processMessages flushes it immediately
                Display.setResizable(true);
                Display.processMessages();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
