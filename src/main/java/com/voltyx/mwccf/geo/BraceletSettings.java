package com.voltyx.mwccf.geo;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class BraceletSettings {
    public static float inspectVolume = 1.0f;
    public static float backgroundVolume = 0.5f;
    public static float mwcWeaponVolume = 0.2f;
    public static float bodyHeartbeatVolume = 1.0f;
    public static float bodyHeartbeatBpmThreshold = 105.0f;

    public static int displayColorR = 255;
    public static int displayColorG = 255;
    public static int displayColorB = 255;

    private static Configuration config;

    public static Configuration getConfig() {
        return config;
    }

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            load();
        }
    }

    public static void load() {
        if (config != null) {
            config.load();
            inspectVolume = config.getFloat("inspectVolume", "volume", 1.0f, 0.0f, 1.0f, "Volume when inspecting the bracelet");
            backgroundVolume = config.getFloat("backgroundVolume", "volume", 0.5f, 0.0f, 1.0f, "Volume when bracelet is running in background");
            mwcWeaponVolume = config.getFloat("mwcWeaponVolume", "volume", 0.2f, 0.0f, 1.0f, "Volume when holding MWC weapon");
            bodyHeartbeatVolume = config.getFloat("bodyHeartbeatVolume", "volume", 1.0f, 0.0f, 1.0f, "Volume of internal body heartbeat sounds at high BPM");
            bodyHeartbeatBpmThreshold = config.getFloat("bodyHeartbeatBpmThreshold", "volume", 105.0f, 60.0f, 200.0f, "BPM threshold to start playing visceral body heartbeat sounds");
            if (Math.abs(bodyHeartbeatBpmThreshold - 135.0f) < 0.01f) {
                bodyHeartbeatBpmThreshold = 105.0f;
                config.get("volume", "bodyHeartbeatBpmThreshold", 105.0f).set(105.0f);
            }
            
            displayColorR = config.getInt("displayColorR", "color", 255, 0, 255, "Display Red Color");
            displayColorG = config.getInt("displayColorG", "color", 255, 0, 255, "Display Green Color");
            displayColorB = config.getInt("displayColorB", "color", 255, 0, 255, "Display Blue Color");

            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static void save() {
        if (config != null) {
            config.get("volume", "inspectVolume", 1.0f).set(inspectVolume);
            config.get("volume", "backgroundVolume", 0.5f).set(backgroundVolume);
            config.get("volume", "mwcWeaponVolume", 0.2f).set(mwcWeaponVolume);
            config.get("volume", "bodyHeartbeatVolume", 1.0f).set(bodyHeartbeatVolume);
            config.get("volume", "bodyHeartbeatBpmThreshold", 105.0f).set(bodyHeartbeatBpmThreshold);
            config.get("color", "displayColorR", 255).set(displayColorR);
            config.get("color", "displayColorG", 255).set(displayColorG);
            config.get("color", "displayColorB", 255).set(displayColorB);
            config.save();
        }
    }
}
