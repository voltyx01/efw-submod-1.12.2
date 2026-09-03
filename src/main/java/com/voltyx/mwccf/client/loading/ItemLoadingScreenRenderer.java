package com.voltyx.mwccf.client.loading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class ItemLoadingScreenRenderer {

    // ── Настройки ──────────────────────────────────────────────────────────
    private static final float ITEM_SCALE = 2.0f;
    private static final int TITLE_COLOR = 0xC8B86A;
    private static final int DIVIDER_COLOR = 0xFF2E2E2E;
    private static final int DESC_LINE_HEIGHT = 12;
    private static final int DESC_COLOR = 0x888880;
    private static final int LORE_COLOR = 0x555550;
    // ───────────────────────────────────────────────────────────────────────

    private static final Random RANDOM = new Random();

    private static LoadingScreenEntry currentEntry = null;
    private static ItemStack currentStack = null;
    private static boolean picked = false;
    private static boolean texturePreloaded = false;

    /**
     * Вызывать при первом перехвате GuiScreenWorking.
     * Выбирает предмет и предзагружает его текстуру.
     */
    public static void pickRandom() {
        if (picked) return;

        List<LoadingScreenEntry> entries = LoadingScreenConfig.getEntries();
        if (entries.isEmpty()) {
            currentEntry = null;
            currentStack = null;
            picked = true;
            return;
        }

        // Filter for items that can safely render on loading screens without mc.player (avoid MWC weapons on 2D load screen)
        List<LoadingScreenEntry> safeEntries = new java.util.ArrayList<>();
        for (LoadingScreenEntry e : entries) {
            if (e != null && e.item != null && !e.item.startsWith("mwc:")) {
                safeEntries.add(e);
            }
        }

        List<LoadingScreenEntry> pool = safeEntries.isEmpty() ? entries : safeEntries;
        currentEntry = pool.get(RANDOM.nextInt(pool.size()));
        currentStack = buildStack(currentEntry);
        picked = true;
        texturePreloaded = false; // Позволяем preloadTexture отработать при первом рендере
    }

    /**
     * Сбросить после входа в мир — готовимся к следующей загрузке.
     */
    public static void reset() {
        picked = false;
        texturePreloaded = false;
        currentEntry = null;
        currentStack = null;
    }

    public static boolean hasPicked() {
        return picked;
    }

    public static ItemStack getCurrentStack() {
        return currentStack;
    }

    /**
     * Предзагрузка текстуры предмета — вызывать сразу после pickRandom()
     * пока ещё есть нормальный GL контекст.
     */
    public static void preloadTexture() {
        if (texturePreloaded || currentStack == null)
            return;
        try {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.getTextureManager() == null || mc.getRenderItem() == null)
                return;
            // Биндим атлас блоков/предметов — это форсирует его загрузку в GPU
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            // Получаем модель предмета — это форсирует её кэширование
            mc.getRenderItem().getItemModelMesher().getItemModel(currentStack);
            texturePreloaded = true;
        } catch (Exception e) {
            FMLLog.warning("[LoadingScreen] Не удалось предзагрузить текстуру: %s", e.getMessage());
        }
    }

    private static final ResourceLocation LOADING_BACKGROUND_RES = new ResourceLocation("mwccf", "textures/gui/loading_screen.png");
    private static ResourceLocation dynamicBackgroundLoc = null;
    private static int bgImageWidth = 1920;
    private static int bgImageHeight = 1080;
    private static boolean bgDimensionsLoaded = false;

    public static void render(int screenWidth, int screenHeight, String title, String subtitle) {
        Minecraft mc = Minecraft.getMinecraft();

        // Кастомный фон с правильным кадрированием (cover)
        drawCustomBackground(screenWidth, screenHeight);

        if (currentEntry == null || currentStack == null) {
            // Если предмет не выбран (например, при выходе из игры или мира), 
            // рисуем стандартный текст загрузки и возвращаемся.
            FontRenderer font = mc.fontRenderer;
            if (font != null) {
                if (title != null && !title.isEmpty()) {
                    font.drawStringWithShadow(title, (screenWidth - font.getStringWidth(title)) / 2, screenHeight / 2 - 20, 0xFFFFFF);
                }
                if (subtitle != null && !subtitle.isEmpty()) {
                    font.drawStringWithShadow(subtitle, (screenWidth - font.getStringWidth(subtitle)) / 2, screenHeight / 2 - 5, 0xFFFFFF);
                }
            }
            return;
        }

        // Защита — не рендерим предмет если рендерер ещё не готов
        if (mc.fontRenderer == null || mc.getRenderItem() == null
                || mc.getTextureManager() == null)
            return;

        // Пробуем предзагрузить если ещё не успели
        if (!texturePreloaded) {
            preloadTexture();
        }

        // Подготавливаем текст и считаем высоту рамки
        int frameWidth = 260;
        int textWidth = frameWidth - 24;
        int frameHeight = 56; // Отступы + название
        
        List<String> descLines = null;
        if (currentEntry.description != null && !currentEntry.description.isEmpty()) {
            descLines = mc.fontRenderer.listFormattedStringToWidth(currentEntry.description, textWidth);
            frameHeight += descLines.size() * DESC_LINE_HEIGHT + 14;
        }
        
        List<String> loreLines = null;
        if (currentEntry.lore != null && !currentEntry.lore.isEmpty()) {
            loreLines = mc.fontRenderer.listFormattedStringToWidth(currentEntry.lore, textWidth);
            frameHeight += loreLines.size() * DESC_LINE_HEIGHT + 14;
        }

        int startX = (screenWidth - frameWidth) / 2;
        int startY = (screenHeight - frameHeight) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(startX, startY, 0);

        // Рисуем фон рамки (более прозрачный)
        Gui.drawRect(0, 0, frameWidth, frameHeight, 0x90000000);
        // Рисуем границы рамки
        Gui.drawRect(0, 0, frameWidth, 1, 0xFF555555); // Top
        Gui.drawRect(0, frameHeight - 1, frameWidth, frameHeight, 0xFF555555); // Bottom
        Gui.drawRect(0, 0, 1, frameHeight, 0xFF555555); // Left
        Gui.drawRect(frameWidth - 1, 0, frameWidth, frameHeight, 0xFF555555); // Right

        // ── Иконка предмета ───────────────────────────────────────────────
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();

        GlStateManager.translate(16, 13, 0);
        GlStateManager.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        
        net.minecraft.client.renderer.OpenGlHelper.setLightmapTextureCoords(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();

        mc.getRenderItem().renderItemAndEffectIntoGUI(currentStack, 0, 0);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();

        // ── Тексты ────────────────────────────────────────────────────────
        mc.fontRenderer.drawString(currentStack.getDisplayName(), 60, 24, TITLE_COLOR, false);

        int currentY = 56;

        if (descLines != null && !descLines.isEmpty()) {
            Gui.drawRect(12, currentY - 8, frameWidth - 12, currentY - 7, DIVIDER_COLOR);
            for (int i = 0; i < descLines.size(); i++) {
                mc.fontRenderer.drawString(descLines.get(i), 12, currentY + i * DESC_LINE_HEIGHT, DESC_COLOR, false);
            }
            currentY += descLines.size() * DESC_LINE_HEIGHT + 14;
        }

        if (loreLines != null && !loreLines.isEmpty()) {
            Gui.drawRect(12, currentY - 8, frameWidth / 2, currentY - 7, 0xFF282828);
            for (int i = 0; i < loreLines.size(); i++) {
                mc.fontRenderer.drawString(loreLines.get(i), 12, currentY + i * DESC_LINE_HEIGHT, LORE_COLOR, false);
            }
        }

        GlStateManager.popMatrix(); // Выходим из локальных координат рамки
        
        // Колесо загрузки (квадратная змейка) в правом нижнем углу
        drawSquareSnake(screenWidth, screenHeight);
    }

    private static ItemStack buildStack(LoadingScreenEntry entry) {
        try {
            Item item = Item.getByNameOrId(entry.item);
            if (item == null) {
                FMLLog.warning("[LoadingScreen] Предмет не найден: %s", entry.item);
                return null;
            }
            return new ItemStack(item, 1, entry.meta);
        } catch (Exception e) {
            FMLLog.warning("[LoadingScreen] Ошибка создания ItemStack для %s: %s", entry.item, e.getMessage());
            return null;
        }
    }
    public static void warmupAll() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getRenderItem() == null) return;

        for (LoadingScreenEntry entry : LoadingScreenConfig.getEntries()) {
            if (entry == null || entry.item == null || entry.item.startsWith("mwc:")) continue;
            ItemStack stack = buildStack(entry);
            if (stack == null) continue;
            try {
                mc.getRenderItem().getItemModelMesher().getItemModel(stack);
            } catch (Exception e) {
                FMLLog.warning("[LoadingScreen] Не удалось прогреть модель %s: %s",
                        entry.item, e.getMessage());
            }
        }

        // Реальный прогрев GL-состояния рендера предмета
        warmupRenderPipeline(mc);
    }

    private static void warmupRenderPipeline(Minecraft mc) {
        List<LoadingScreenEntry> entries = LoadingScreenConfig.getEntries();
        if (entries.isEmpty()) return;

        ItemStack warmStack = buildStack(entries.get(0));
        if (warmStack == null) return;

        try {
            GlStateManager.pushMatrix();
            // Рисуем далеко за пределами экрана — невидимо, но GL-вызовы реальные
            GlStateManager.translate(-9999, -9999, 0);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();

            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemAndEffectIntoGUI(warmStack, 0, 0);
            RenderHelper.disableStandardItemLighting();

            GlStateManager.disableDepth();
            GlStateManager.popMatrix();

            // форсируем реальное выполнение команд на GPU прямо сейчас,
            // пока не начали грузить мир
            org.lwjgl.opengl.GL11.glFinish();
        } catch (Exception e) {
            FMLLog.warning("[LoadingScreen] Не удалось прогреть рендер-пайплайн: %s", e.getMessage());
        }
    }

    private static void ensureBackgroundLoaded() {
        if (bgDimensionsLoaded) return;
        bgDimensionsLoaded = true;

        try {
            Minecraft mc = Minecraft.getMinecraft();
            File extFile = new File("loading_screen.png");
            if (!extFile.exists() && mc.gameDir != null) {
                extFile = new File(mc.gameDir, "loading_screen.png");
            }
            if (!extFile.exists()) {
                extFile = new File("C:/Users/reizv/Documents/mwccf/loading_screen.png");
            }

            BufferedImage img = null;
            if (extFile.exists()) {
                try (InputStream is = new FileInputStream(extFile)) {
                    img = ImageIO.read(is);
                }
            }
            if (img == null) {
                try (InputStream is = ItemLoadingScreenRenderer.class.getResourceAsStream("/assets/mwccf/textures/gui/loading_screen.png")) {
                    if (is != null) {
                        img = ImageIO.read(is);
                    }
                }
            }

            if (img != null) {
                bgImageWidth = img.getWidth();
                bgImageHeight = img.getHeight();
                if (mc.getTextureManager() != null) {
                    dynamicBackgroundLoc = mc.getTextureManager().getDynamicTextureLocation("loading_screen_bg", new DynamicTexture(img));
                }
            }
        } catch (Throwable t) {
            FMLLog.warning("[LoadingScreen] Не удалось загрузить размеры loading_screen.png: %s", t.getMessage());
            bgImageWidth = 1920;
            bgImageHeight = 1080;
        }
    }

    private static void drawCustomBackground(int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getTextureManager() == null) return;

        ensureBackgroundLoaded();

        ResourceLocation loc = (dynamicBackgroundLoc != null) ? dynamicBackgroundLoc : LOADING_BACKGROUND_RES;
        mc.getTextureManager().bindTexture(loc);

        // Расчет кадрирования с сохранением пропорций (object-fit: cover)
        double imgAspect = (double) bgImageWidth / (double) bgImageHeight;
        double screenAspect = (double) width / (double) height;

        double u0 = 0.0D, u1 = 1.0D;
        double v0 = 0.0D, v1 = 1.0D;

        if (screenAspect > imgAspect) {
            // Экран шире изображения -> обрезаем верх и низ
            double visibleHeightFrac = imgAspect / screenAspect;
            v0 = (1.0D - visibleHeightFrac) / 2.0D;
            v1 = 1.0D - v0;
        } else if (screenAspect < imgAspect) {
            // Экран уже/выше изображения -> обрезаем лево и право
            double visibleWidthFrac = screenAspect / imgAspect;
            u0 = (1.0D - visibleWidthFrac) / 2.0D;
            u1 = 1.0D - u0;
        }

        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double) height, 0.0D).tex(u0, v1).endVertex();
        bufferbuilder.pos((double) width, (double) height, 0.0D).tex(u1, v1).endVertex();
        bufferbuilder.pos((double) width, 0.0D, 0.0D).tex(u1, v0).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(u0, v0).endVertex();
        tessellator.draw();
    }

    private static void drawSquareSnake(int screenWidth, int screenHeight) {
        int cx = screenWidth - 30;
        int cy = screenHeight - 30;
        int size = 10;
        long time = Minecraft.getSystemTime();

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, 
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, 
                GlStateManager.SourceFactor.ONE, 
                GlStateManager.DestFactor.ZERO);

        // Рисуем слабый контур квадрата (фон)
        int bg = 0x22FFFFFF;
        Gui.drawRect(cx - size, cy - size, cx + size, cy - size + 1, bg); // Top
        Gui.drawRect(cx + size - 1, cy - size, cx + size, cy + size, bg); // Right
        Gui.drawRect(cx - size, cy + size - 1, cx + size, cy + size, bg); // Bottom
        Gui.drawRect(cx - size, cy - size, cx - size + 1, cy + size, bg); // Left

        int perimeter = size * 8; // 80
        int pos = (int) ((time / 15) % perimeter);

        // Рисуем змейку как серию затухающих точек по периметру
        for (int i = 0; i < 16; i++) {
            int dotPos = (pos - i * 2 + perimeter) % perimeter;
            int dx, dy;
            
            if (dotPos < size * 2) { // Top edge (left to right)
                dx = -size + dotPos;
                dy = -size;
            } else if (dotPos < size * 4) { // Right edge (top to bottom)
                dx = size;
                dy = -size + (dotPos - size * 2);
            } else if (dotPos < size * 6) { // Bottom edge (right to left)
                dx = size - (dotPos - size * 4);
                dy = size;
            } else { // Left edge (bottom to top)
                dx = -size;
                dy = size - (dotPos - size * 6);
            }

            int alpha = (int) (255.0f * (1.0f - (i / 16.0f)));
            int color = (alpha << 24) | 0xFFFFFF;
            Gui.drawRect(cx + dx - 1, cy + dy - 1, cx + dx + 1, cy + dy + 1, color);
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}