package com.voltyx.mwccf.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EnglishLanguageMap implements IResourceManagerReloadListener {

    private static final Map<String, String> ENGLISH_MAP = new ConcurrentHashMap<>();
    private static volatile boolean initialized = false;
    private static final EnglishLanguageMap INSTANCE = new EnglishLanguageMap();

    public static void ensureInitialized() {
        if (!initialized) {
            synchronized (EnglishLanguageMap.class) {
                if (!initialized) {
                    Minecraft mc = Minecraft.getMinecraft();
                    if (mc != null && mc.getResourceManager() != null) {
                        loadFrom(mc.getResourceManager());
                        if (mc.getResourceManager() instanceof IReloadableResourceManager) {
                            ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(INSTANCE);
                        }
                        initialized = true;
                    }
                }
            }
        }
    }

    public static void reload() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.getResourceManager() != null) {
            loadFrom(mc.getResourceManager());
        }
    }

    private static void loadFrom(IResourceManager resourceManager) {
        ENGLISH_MAP.clear();
        for (String domain : resourceManager.getResourceDomains()) {
            loadDomainLang(resourceManager, domain, "lang/en_us.lang");
            loadDomainLang(resourceManager, domain, "lang/en_US.lang");
        }
    }

    private static void loadDomainLang(IResourceManager resourceManager, String domain, String path) {
        try {
            List<IResource> resources = resourceManager.getAllResources(new ResourceLocation(domain, path));
            for (IResource resource : resources) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) continue;
                        int eq = line.indexOf('=');
                        if (eq > 0) {
                            String key = line.substring(0, eq).trim();
                            String val = line.substring(eq + 1);
                            ENGLISH_MAP.put(key, val);
                        }
                    }
                } catch (Throwable ignored) {}
            }
        } catch (Throwable ignored) {}
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        loadFrom(resourceManager);
    }

    public static String translateKey(String key) {
        ensureInitialized();
        return ENGLISH_MAP.get(key);
    }

    public static String getEnglishName(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return "";
        ensureInitialized();

        Item item = stack.getItem();
        if (item == null) return "";

        try {
            String unloc = item.getUnlocalizedNameInefficiently(stack);
            if (unloc != null) {
                String val = ENGLISH_MAP.get(unloc + ".name");
                if (val != null && !val.trim().isEmpty()) {
                    return val.trim();
                }
            }
        } catch (Throwable ignored) {}

        try {
            String unloc = item.getTranslationKey();
            if (unloc != null) {
                String val = ENGLISH_MAP.get(unloc + ".name");
                if (val != null && !val.trim().isEmpty()) {
                    return val.trim();
                }
            }
        } catch (Throwable ignored) {}

        // Fallback to registry name formatted nicely (e.g. "m4a1" or "iron_ingot" -> "iron ingot")
        ResourceLocation reg = item.getRegistryName();
        if (reg != null) {
            return reg.getPath().replace('_', ' ');
        }

        return "";
    }
}
