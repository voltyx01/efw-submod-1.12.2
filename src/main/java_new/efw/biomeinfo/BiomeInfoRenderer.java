package efw.biomeinfo;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.text.WordUtils;

public class BiomeInfoRenderer {
    public static final int MARGIN = 3;

    public static Biome previousBiome;
    public static int displayTime = 0;
    public static int alpha = 0;
    public static boolean fadingIn = false;

    public static final Map<ResourceLocation, String> NAME_CACHE = new HashMap<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        if (!fadingIn) {
            if (!MwccfConfig.biomeinfo.fadeOut && alpha != 255) {
                alpha = 255;
            } else if (MwccfConfig.biomeinfo.fadeOut) {
                if (displayTime > 0) {
                    displayTime--;
                } else if (alpha > 0) {
                    alpha = Math.max(0, alpha - 10);
                }
            }
        } else {
            alpha += 10;
            if (alpha >= 255) {
                fadingIn = false;
                displayTime = MwccfConfig.biomeinfo.displayTime;
                alpha = 255;
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT)
            return;

        if (MwccfConfig.biomeinfo.enabled) {
            Minecraft mc = Minecraft.getMinecraft();
            if (hideBecauseOfF1(mc) || hideBecauseOfF3(mc))
                return;

            if (mc.world != null && mc.player != null) {
                BlockPos pos = mc.player.getPosition();
                if (mc.world.isBlockLoaded(pos)) {
                    Biome biome = mc.world.getBiome(pos);

                    if (previousBiome != biome) {
                        previousBiome = biome;
                        if (MwccfConfig.biomeinfo.fadeIn) {
                            displayTime = 0;
                            alpha = 0;
                            fadingIn = true;
                        } else {
                            displayTime = MwccfConfig.biomeinfo.displayTime;
                            alpha = 255;
                            fadingIn = false;
                        }
                    }

                    if (alpha > 0) {
                        ResourceLocation key = biome.getRegistryName();
                        if (key != null) {
                            String biomeName = getBiomeName(biome, key);
                            float scale = (float) MwccfConfig.biomeinfo.scale;
                            FontRenderer font = mc.fontRenderer;
                            ScaledResolution res = event.getResolution();

                            PositionPreset positionPreset = MwccfConfig.biomeinfo.positionPreset;
                            int textOffset = positionPreset.textAlignment().getNegativeOffset(font, biomeName, scale);

                            GlStateManager.pushMatrix();
                            GlStateManager.scale(scale, scale, scale);

                            int posX = (int) ((positionPreset.posX(res) - textOffset) / scale);
                            int posY = (int) (positionPreset.posY(res, font, scale) / scale);

                            int clampedAlpha = Math.max(0, Math.min(255, alpha));
                            int color = (clampedAlpha << 24) | (MwccfConfig.biomeinfo.color & 0xFFFFFF);

                            if (MwccfConfig.biomeinfo.textShadow) {
                                font.drawStringWithShadow(biomeName, posX, posY, color);
                            } else {
                                font.drawString(biomeName, posX, posY, color);
                            }

                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
        }
    }

    private static String getBiomeName(Biome biome, ResourceLocation key) {
        return NAME_CACHE.computeIfAbsent(key, k -> {
            String translationKey = "biome." + k.getNamespace() + "." + k.getPath();
            String displayName;

            if (I18n.hasKey(translationKey)) {
                displayName = I18n.format(translationKey);
            } else {
                String vanillaName = biome.getBiomeName();
                if (vanillaName != null && !vanillaName.isEmpty()) {
                    displayName = vanillaName;
                } else if (MwccfConfig.biomeinfo.fallbackOnUntranslatableName) {
                    displayName = snakeCaseToEnglish(k.getPath());
                } else {
                    displayName = translationKey;
                }
            }

            if (MwccfConfig.biomeinfo.appendModName) {
                String modid = k.getNamespace();
                String modName = getModName(modid);
                if (modName != null && !modName.isEmpty()) {
                    displayName += String.format(" (%s)", modName);
                }
            }

            return displayName;
        });
    }

    private static String getModName(String modid) {
        if ("minecraft".equals(modid)) {
            return "Minecraft";
        }
        for (ModContainer mod : Loader.instance().getModList()) {
            if (mod.getModId().equalsIgnoreCase(modid)) {
                return mod.getName();
            }
        }
        return snakeCaseToEnglish(modid);
    }

    private static String snakeCaseToEnglish(String path) {
        String[] words = path.split("_");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            formatted.append(WordUtils.capitalize(word)).append(" ");
        }
        return formatted.toString().trim();
    }

    private static boolean hideBecauseOfF1(Minecraft mc) {
        return mc.gameSettings.hideGUI && MwccfConfig.biomeinfo.hideWithUI;
    }

    private static boolean hideBecauseOfF3(Minecraft mc) {
        return mc.gameSettings.showDebugInfo && MwccfConfig.biomeinfo.hideOnDebugScreen;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals("mwccf")) {
            ConfigManager.sync("mwccf", Config.Type.INSTANCE);
            NAME_CACHE.clear();
        }
    }

    public static void clearCache() {
        NAME_CACHE.clear();
    }
}