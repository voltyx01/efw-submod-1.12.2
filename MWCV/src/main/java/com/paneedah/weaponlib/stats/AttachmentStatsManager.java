package com.paneedah.weaponlib.stats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.paneedah.weaponlib.AttachmentCategory;
import com.paneedah.weaponlib.ItemAttachment;
import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.Weapon;
import net.minecraft.item.Item;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.paneedah.mwc.utils.ModReference.LOG;

/**
 * Manages loading, saving, and querying attachment stats from JSON.
 * Works on both Client and Server environments.
 */
public class AttachmentStatsManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILENAME = "attachment_stats.json";

    // Key can be registry name or attachment unlocalized name / variable name
    private static final Map<String, AttachmentStatData> STATS_MAP = new HashMap<>();

    private static boolean isInitialized = false;

    /**
     * Initializes and loads the attachment stats JSON file from the config directory.
     */
    public static synchronized void init(File configDir) {
        if (configDir == null) {
            configDir = new File(".", "config/mwccf");
        }
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        File configFile = new File(configDir, CONFIG_FILENAME);
        if (!configFile.exists()) {
            // Try fallback location config/
            File fallback = new File(configDir.getParentFile(), CONFIG_FILENAME);
            if (fallback.exists()) {
                configFile = fallback;
            } else {
                // Copy default from jar resources to config
                try (InputStream in = AttachmentStatsManager.class.getResourceAsStream("/assets/mwc/" + CONFIG_FILENAME)) {
                    if (in != null) {
                        try (OutputStream out = new FileOutputStream(configFile)) {
                            byte[] buf = new byte[4096];
                            int read;
                            while ((read = in.read(buf)) != -1) {
                                out.write(buf, 0, read);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Failed to copy bundled attachment_stats.json", e);
                }
            }
        }

        loadConfig(configFile);
        isInitialized = true;
    }

    public static synchronized void loadConfig(File configFile) {
        if (!configFile.exists()) {
            LOG.info("Attachment stats file {} does not exist. Creating template.", configFile.getAbsolutePath());
            saveDefaultTemplate(configFile);
            return;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
            Type type = new TypeToken<Map<String, AttachmentStatData>>() {}.getType();
            Map<String, AttachmentStatData> loaded = GSON.fromJson(reader, type);
            if (loaded != null) {
                STATS_MAP.clear();
                for (Map.Entry<String, AttachmentStatData> entry : loaded.entrySet()) {
                    STATS_MAP.put(normalizeKey(entry.getKey()), entry.getValue());
                }
                LOG.info("Loaded {} attachment stats from {}", STATS_MAP.size(), configFile.getAbsolutePath());
            }
        } catch (Exception e) {
            LOG.error("Failed to load attachment stats from " + configFile.getAbsolutePath(), e);
        }
    }

    private static void saveDefaultTemplate(File configFile) {
        try {
            configFile.getParentFile().mkdirs();
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
                GSON.toJson(STATS_MAP, writer);
            }
        } catch (Exception e) {
            LOG.error("Failed to write default attachment stats template", e);
        }
    }

    private static String normalizeKey(String key) {
        if (key == null) return "";
        key = key.trim().toLowerCase();
        if (key.startsWith("mwc:")) {
            key = key.substring(4);
        }
        if (key.startsWith("item.")) {
            key = key.substring(5);
        }
        if (key.startsWith("mwc_")) {
            key = key.substring(4);
        }
        return key.replaceAll("[^a-z0-9]", "");
    }

    /**
     * Retrieves the stats data for an attachment item.
     */
    public static AttachmentStatData getStats(ItemAttachment<?> attachment) {
        if (attachment == null) {
            return null;
        }

        // Try lookup by registry name
        if (attachment.getRegistryName() != null) {
            String regName = normalizeKey(attachment.getRegistryName().getPath());
            AttachmentStatData data = STATS_MAP.get(regName);
            if (data != null) return data;
        }

        // Try lookup by name
        if (attachment.getName() != null) {
            String name = normalizeKey(attachment.getName());
            AttachmentStatData data = STATS_MAP.get(name);
            if (data != null) return data;
        }

        // Try lookup by translation key
        String unloc = normalizeKey(attachment.getTranslationKey());
        if (unloc.startsWith("item.")) {
            unloc = unloc.substring(5);
        }
        AttachmentStatData data = STATS_MAP.get(unloc);
        if (data != null) return data;

        // Try lookup by texture name
        if (attachment.getTextureName() != null) {
            String tex = normalizeKey(attachment.getTextureName());
            if (tex.endsWith(".png")) tex = tex.substring(0, tex.length() - 4);
            data = STATS_MAP.get(tex);
            if (data != null) return data;
        }

        // Try lookup by class name
        String className = normalizeKey(attachment.getClass().getSimpleName());
        data = STATS_MAP.get(className);
        if (data != null) return data;

        return null;
    }

    /**
     * Calculates the aggregated effective modifiers for a weapon instance with all its equipped attachments.
     */
    public static EffectiveWeaponStats getEffectiveStats(PlayerWeaponInstance pwi) {
        EffectiveWeaponStats eff = new EffectiveWeaponStats();
        if (pwi == null) return eff;

        int[] activeIds = pwi.getActiveAttachmentIds();
        if (activeIds == null) return eff;

        for (int id : activeIds) {
            if (id <= 0) continue;
            Item item = Item.getItemById(id);
            if (item instanceof ItemAttachment) {
                AttachmentStatData stat = getStats((ItemAttachment<?>) item);
                if (stat != null) {
                    eff.recoilMultiplier *= stat.recoilMultiplier;
                    eff.visualRecoilMultiplier *= stat.visualRecoilMultiplier;
                    eff.recoilRecoveryMultiplier *= stat.recoilRecoveryMultiplier;
                    eff.hipSpreadMultiplier *= stat.hipSpreadMultiplier;
                    eff.aimSpreadMultiplier *= stat.aimSpreadMultiplier;
                    eff.adsSpeedMultiplier *= stat.adsSpeedMultiplier;
                    eff.drawSpeedMultiplier *= stat.drawSpeedMultiplier;
                    eff.reloadSpeedMultiplier *= stat.reloadSpeedMultiplier;
                    eff.totalWeight += stat.weight;
                }
            }
        }

        return eff;
    }

    public static class EffectiveWeaponStats {
        public double recoilMultiplier = 1.0;
        public double visualRecoilMultiplier = 1.0;
        public double recoilRecoveryMultiplier = 1.0;
        public double hipSpreadMultiplier = 1.0;
        public double aimSpreadMultiplier = 1.0;
        public double adsSpeedMultiplier = 1.0;
        public double drawSpeedMultiplier = 1.0;
        public double reloadSpeedMultiplier = 1.0;
        public double totalWeight = 0.0;
    }
}
