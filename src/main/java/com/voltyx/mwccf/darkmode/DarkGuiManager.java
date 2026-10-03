package com.voltyx.mwccf.darkmode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Universal dark mode engine for all GUI screens (vanilla & any mods).
 */
@SideOnly(Side.CLIENT)
public class DarkGuiManager {

    private static boolean enabled = true;
    private static boolean isGenerating = false;
    private static final Map<ResourceLocation, ResourceLocation> CACHE = new ConcurrentHashMap<>();
    private static File darkmodeRootFolder = null;
    private static File overrideAssetsDir = null;

    static {
        locateFolders();
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new DarkGuiManager());
        locateFolders();
        injectResourcePack();
    }

    public static void injectResourcePack() {
        if (darkmodeRootFolder == null || !darkmodeRootFolder.exists()) return;
        try {
            Minecraft mc = Minecraft.getMinecraft();
            List<IResourcePack> defaultPacks = ObfuscationReflectionHelper.getPrivateValue(
                    Minecraft.class, mc, "field_110449_ao");
            if (defaultPacks != null) {
                FolderResourcePack pack = new FolderResourcePack(darkmodeRootFolder);
                defaultPacks.add(pack);
                System.out.println("[DarkGuiManager] Successfully injected !darkmode as system resource pack from: " + darkmodeRootFolder.getAbsolutePath());
            }
        } catch (Throwable t) {
            System.err.println("[DarkGuiManager] Failed to inject system resource pack: " + t.getMessage());
        }
    }

    private static void locateFolders() {
        Minecraft mc = Minecraft.getMinecraft();
        File gameDir = (mc != null && mc.gameDir != null) ? mc.gameDir : new File(".");

        File[] candidates = new File[] {
                new File(gameDir, "!darkmode"),
                new File(gameDir, "../!darkmode"),
                new File("!darkmode"),
                new File("C:/Users/reizv/Documents/mwccf/!darkmode")
        };

        for (File cand : candidates) {
            if (cand.exists() && cand.isDirectory()) {
                File assets = new File(cand, "assets");
                if (assets.exists() && assets.isDirectory()) {
                    darkmodeRootFolder = cand;
                    overrideAssetsDir = assets;
                    System.out.println("[DarkGuiManager] Found darkmode root at: " + cand.getAbsolutePath());
                    break;
                }
            }
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static boolean isContainerOpen() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen == null) return false;
        if (mc.currentScreen instanceof com.voltyx.mwccf.sins.client.GuiSevenScreen) {
            return false;
        }
        return mc.currentScreen instanceof GuiContainer;
    }

    public static ResourceLocation getReplacementTexture(ResourceLocation original) {
        if (!enabled || original == null || isGenerating) {
            return original;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen == null) {
            return original;
        }
        if (mc.currentScreen instanceof com.voltyx.mwccf.sins.client.GuiSevenScreen) {
            return original;
        }

        if (!shouldProcessGuiTexture(original)) {
            return original;
        }

        ResourceLocation cached = CACHE.get(original);
        if (cached != null) {
            return cached;
        }

        return createAndCacheDarkTexture(original);
    }

    private static boolean shouldProcessGuiTexture(ResourceLocation res) {
        String path = res.getPath();
        String domain = res.getNamespace();

        // Non-GUI textures
        if (path.startsWith("textures/font/")) return false;
        if (path.startsWith("textures/particle/")) return false;
        if (path.startsWith("textures/environment/")) return false;
        if (path.startsWith("textures/colormap/")) return false;
        if (path.startsWith("textures/models/")) return false;
        if (path.startsWith("textures/entity/")) return false;
        if (path.startsWith("textures/blocks/")) return false;
        if (path.startsWith("textures/items/")) return false;

        // Skip internal dynamic textures
        if (path.contains("mwccf_darkgui") || path.contains("mwccf_blood")) return false;

        // Never touch GuiSevenScreen or sins UI textures
        if (path.contains("sins") || path.contains("seven") || domain.contains("sins")) return false;

        // In-game HUD & background tiles to leave untouched:
        if (path.contains("icons")) return false;
        if (path.contains("options_background")) return false;
        if (path.contains("dirt")) return false;
        if (path.contains("bars.png")) return false;
        if (path.contains("textures/gui/title/")) return false;
        if (path.contains("textures/gui/toasts.png")) return false;
        if (path.contains("overlay") || path.contains("hud") || path.contains("crosshair")) return false;
        if (path.contains("blood")) return false;

        // Stamina, Dash, Roll overlays & bars:
        if (path.contains("stamina") || path.contains("dash") || path.contains("roll") || path.contains("dodge")) return false;
        if (path.contains("sloi_") || path.contains("altlockndash")) return false;

        // Wildfire Gender & Wardrobe button in mwccf (leave untouched)
        if (path.contains("wardrobe") || path.contains("wildfire") || domain.contains("wildfire")) return false;

        // Specific mod targets
        if (domain.equals("jei") && (path.contains("gui") || path.contains("atlas"))) return true;
        if (domain.equals("baubles")) return true;

        // General GUI / container textures
        if (path.contains("gui") || path.contains("container")) return true;

        return false;
    }

    private static ResourceLocation createAndCacheDarkTexture(ResourceLocation original) {
        isGenerating = true;
        try {
            BufferedImage img = null;

            // 1. Try reading pre-made hand-crafted assets from !darkmode if present
            if (overrideAssetsDir != null) {
                File overrideFile = new File(overrideAssetsDir, original.getNamespace() + "/" + original.getPath());
                if (overrideFile.exists() && overrideFile.isFile()) {
                    try {
                        img = ImageIO.read(overrideFile);
                    } catch (Throwable ignored) {}
                }
            }

            // 2. If not found in custom assets, read original image
            if (img == null) {
                BufferedImage base = loadBaseImage(original);
                if (base != null) {
                    img = DarkGuiFilter.transform(base);
                }
            }

            if (img == null) {
                CACHE.put(original, original);
                return original;
            }

            DynamicTexture dynamicTexture = new DynamicTexture(img);
            String dynName = "mwccf_darkgui_" + Math.abs((original.toString()).hashCode());
            ResourceLocation dynLoc = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(dynName, dynamicTexture);

            System.out.println("[DarkGuiManager] Styled GUI: " + original + " -> " + dynLoc);
            CACHE.put(original, dynLoc);
            return dynLoc;
        } catch (Throwable t) {
            CACHE.put(original, original);
            return original;
        } finally {
            isGenerating = false;
        }
    }

    private static BufferedImage loadBaseImage(ResourceLocation res) {
        // 1. Read via ResourceManager
        try {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(res);
            if (resource != null) {
                try (InputStream in = resource.getInputStream()) {
                    BufferedImage b = ImageIO.read(in);
                    if (b != null) return b;
                }
            }
        } catch (Throwable ignored) {}

        // 2. Fallback: read directly from OpenGL texture memory
        try {
            ITextureObject texObj = Minecraft.getMinecraft().getTextureManager().getTexture(res);
            if (texObj == null) {
                Minecraft.getMinecraft().getTextureManager().loadTexture(res, new SimpleTexture(res));
                texObj = Minecraft.getMinecraft().getTextureManager().getTexture(res);
            }
            if (texObj != null && texObj.getGlTextureId() > 0) {
                return readImageFromGl(texObj.getGlTextureId());
            }
        } catch (Throwable ignored) {}

        return null;
    }

    private static BufferedImage readImageFromGl(int glId) {
        try {
            int prevTex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
            int w = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            int h = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
            if (w <= 0 || h <= 0 || w > 4096 || h > 4096) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTex);
                return null;
            }

            IntBuffer buffer = BufferUtils.createIntBuffer(w * h);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTex);

            int[] pixels = new int[w * h];
            buffer.get(pixels);
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            img.setRGB(0, 0, w, h, pixels, 0, w);
            return img;
        } catch (Throwable t) {
            return null;
        }
    }

    @SubscribeEvent
    public void onTooltipColor(RenderTooltipEvent.Color event) {
        if (!enabled) return;
        event.setBackground(0xF0181818);
        event.setBorderStart(0xFF383838);
        event.setBorderEnd(0xFF383838);
    }

    @SubscribeEvent(priority = net.minecraftforge.fml.common.eventhandler.EventPriority.LOWEST)
    public void onGuiInitPost(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getButtonList() != null) {
            for (net.minecraft.client.gui.GuiButton btn : event.getButtonList()) {
                if (btn != null && (btn.id == 55 || "baubles.client.gui.GuiBaublesButton".equals(btn.getClass().getName()))) {
                    btn.displayString = "";
                }
            }
        }
    }
}
