package com.teamderpy.shouldersurfing.lockon;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.io.File;

public class LockOnConfig {
    public static Configuration config;
    public static double maxRange = 24.0D;
    public static double autoSwitchRange = 12.0D;
    public static float rotationSpeed = 2.5F;
    public static int maxWallTimeTicks = 20;
    public static float maxAutoSwitchAngle = 90.0F;
    public static float targetHeightMultiplier = 0.66F;
    public static boolean disableMwcAutoSwitch = true;

    // Измените метод init
    public static void init(File configDirectory) { // Принимаем File, а не Event
        File file = new File(configDirectory, "lockon.cfg");
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        String general = "general";
        String mwcCategory = "mwc_integration";

        maxRange = config.getFloat("Max Range", general, 24.0F, 1.0F, 128.0F, "Maximum distance to lock on target");
        autoSwitchRange = config.getFloat("Auto Switch Range", general, 12.0F, 1.0F, 64.0F, "Distance to find next target after kill");
        rotationSpeed = config.getFloat("Rotation Speed", general, 2.5F, 0.1F, 10.0F, "How fast camera follows the target");
        maxWallTimeTicks = config.getInt("Wall Timeout", general, 20, 0, 4000, "Ticks before losing lock if target is behind wall");
        maxAutoSwitchAngle = config.getFloat("Auto Switch Angle", general, 90.0F, 0.0F, 180.0F, "Max angle for auto-switching to next target");
        targetHeightMultiplier = config.getFloat("Target Height Multiplier", general, 0.66F, 0.0F, 1.0F, "Height offset for focus (0 = feet, 1 = head)");
        disableMwcAutoSwitch = config.getBoolean("Disable MWC Auto-Switch", mwcCategory, true, "If true, auto-target switching will be disabled when holding a Vic's Modern Warfare gun");

        if (config.hasChanged()) config.save();
    }
}