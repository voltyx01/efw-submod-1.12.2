package com.voltyx.mwccf.geo;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class BraceletSettings {
    public static float inspectVolume = 1.0f;
    public static float backgroundVolume = 0.5f;
    public static float mwcWeaponVolume = 0.2f;

    public static int displayColorR = 255;
    public static int displayColorG = 255;
    public static int displayColorB = 255;

    private static Configuration config;

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
            config.get("color", "displayColorR", 255).set(displayColorR);
            config.get("color", "displayColorG", 255).set(displayColorG);
            config.get("color", "displayColorB", 255).set(displayColorB);
            config.save();
        }
    }
}
