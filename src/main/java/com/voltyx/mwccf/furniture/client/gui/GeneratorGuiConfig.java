package com.voltyx.mwccf.furniture.client.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class GeneratorGuiConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;

    // Default positions saved by user
    public int fuelSlotX = 26;
    public int fuelSlotY = 42;

    public int chargeSlot1X = 113;
    public int chargeSlot1Y = 52;

    public int chargeSlot2X = 131;
    public int chargeSlot2Y = 52;

    public int statusX = 65;
    public int statusY = 21;

    public int powerX = 65;
    public int powerY = 31;

    public int fuelTextX = 65;
    public int fuelTextY = 41;

    public int chargeLabelX = 65;
    public int chargeLabelY = 51;

    private static GeneratorGuiConfig INSTANCE = new GeneratorGuiConfig();

    public static GeneratorGuiConfig get() {
        if (configFile == null) {
            initFile();
            load();
        }
        return INSTANCE;
    }

    private static void initFile() {
        File dir = new File(net.minecraftforge.fml.common.Loader.instance().getConfigDir(), "mwccf");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        configFile = new File(dir, "generator_gui.json");
    }

    public static void load() {
        try {
            if (configFile == null) {
                initFile();
            }
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    GeneratorGuiConfig loaded = GSON.fromJson(reader, GeneratorGuiConfig.class);
                    if (loaded != null) {
                        INSTANCE = loaded;
                    }
                }
            } else {
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            if (configFile == null) {
                initFile();
            }
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
