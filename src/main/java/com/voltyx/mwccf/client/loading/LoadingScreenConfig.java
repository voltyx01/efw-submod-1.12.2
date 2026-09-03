package com.voltyx.mwccf.client.loading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLLog;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LoadingScreenConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static List<LoadingScreenEntry> entriesEn = new ArrayList<>();
    private static List<LoadingScreenEntry> entriesRu = new ArrayList<>();

    public static void load(File configDir) {
        File fileEn = new File(configDir, "loading_screen_items_en.json");
        File fileRu = new File(configDir, "loading_screen_items_ru.json");

        if (!fileEn.exists()) {
            writeDefaultEn(fileEn);
        }
        if (!fileRu.exists()) {
            writeDefaultRu(fileRu);
        }

        entriesEn = loadFile(fileEn);
        entriesRu = loadFile(fileRu);
    }

    private static List<LoadingScreenEntry> loadFile(File file) {
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<LoadingScreenEntry>>(){}.getType();
            List<LoadingScreenEntry> loaded = GSON.fromJson(reader, listType);
            if (loaded != null && !loaded.isEmpty()) {
                FMLLog.info("[LoadingScreen] Загружено %d предметов из %s", loaded.size(), file.getName());
                return loaded;
            }
        } catch (Exception e) {
            FMLLog.warning("[LoadingScreen] Ошибка загрузки конфига %s: %s", file.getName(), e.getMessage());
        }
        return new ArrayList<>();
    }

    public static List<LoadingScreenEntry> getEntries() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.gameSettings != null) {
            String lang = mc.gameSettings.language;
            if (lang != null && lang.toLowerCase().startsWith("ru")) {
                if (!entriesRu.isEmpty()) return entriesRu;
            }
        }
        return entriesEn.isEmpty() ? entriesRu : entriesEn;
    }

    public static LoadingScreenEntry getEntryForStack(net.minecraft.item.ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.getItem() == null) return null;
        net.minecraft.util.ResourceLocation regName = stack.getItem().getRegistryName();
        if (regName == null) return null;
        String fullRegName = regName.toString().toLowerCase();
        String pathName = regName.getPath().toLowerCase();
        int meta = stack.getMetadata();

        for (LoadingScreenEntry entry : getEntries()) {
            if (entry != null && entry.item != null) {
                String entryItem = entry.item.toLowerCase();
                if (entryItem.equals(fullRegName) || entryItem.equals(pathName)) {
                    if (entry.meta == meta || entry.meta == 0 || meta == 0) {
                        return entry;
                    }
                }
            }
        }
        return null;
    }

    private static void writeDefaultEn(File file) {
        List<LoadingScreenEntry> defaults = new ArrayList<>();

        LoadingScreenEntry e1 = new LoadingScreenEntry();
        e1.item = "minecraft:iron_sword";
        e1.meta = 0;
        e1.description = "A steel blade found among the ruins.";
        e1.lore = "Its master didn't survive. You are different.";
        defaults.add(e1);

        LoadingScreenEntry e2 = new LoadingScreenEntry();
        e2.item = "minecraft:golden_apple";
        e2.meta = 0;
        e2.description = "A rarity even before the catastrophe.";
        e2.lore = "They say this was worth a fortune in the old world.";
        defaults.add(e2);

        writeToFile(file, defaults);
    }

    private static void writeDefaultRu(File file) {
        List<LoadingScreenEntry> defaults = new ArrayList<>();

        LoadingScreenEntry e1 = new LoadingScreenEntry();
        e1.item = "minecraft:iron_sword";
        e1.meta = 0;
        e1.description = "Стальной клинок, найденный среди руин.";
        e1.lore = "Его хозяин не выжил. Ты — другое дело.";
        defaults.add(e1);

        LoadingScreenEntry e2 = new LoadingScreenEntry();
        e2.item = "minecraft:golden_apple";
        e2.meta = 0;
        e2.description = "Редкость даже до катастрофы.";
        e2.lore = "Говорят, в старом мире это стоило целое состояние.";
        defaults.add(e2);

        writeToFile(file, defaults);
    }

    private static void writeToFile(File file, List<LoadingScreenEntry> list) {
        try {
            file.getParentFile().mkdirs();
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                GSON.toJson(list, writer);
            }
        } catch (Exception e) {
            FMLLog.warning("[LoadingScreen] Не удалось создать конфиг %s: %s", file.getName(), e.getMessage());
        }
    }
}
