package efw.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Loader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class NotesConfig {
    private static Map<String, String> notesDataEN = new HashMap<>();
    private static Map<String, String> notesDataRU = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {
        File configDir = new File(Loader.instance().getConfigDir(), "efw");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        File fileEN = new File(configDir, "notes.json");
        notesDataEN = loadFile(fileEN, false);
        File fileRU = new File(configDir, "notes_ru.json");
        notesDataRU = loadFile(fileRU, true);
    }

    private static Map<String, String> loadFile(File file, boolean isRussian) {
        HashMap<String, String> data = new HashMap<>();
        if (!file.exists()) {
            if (isRussian) {
                data.put("1", "\u041f\u0435\u0440\u0432\u0430\u044f \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0430 \u0434\u043d\u0435\u0432\u043d\u0438\u043a\u0430 (RU)");
                data.put("2", "\u0417\u0430\u043f\u0438\u0441\u044c \u0432 \u0441\u043f\u0435\u0448\u043a\u0435 (RU)");
            } else {
                data.put("1", "First page of the ancient diary (EN)");
                data.put("2", "Someone left this note in a hurry (EN)");
            }
            save(file, data);
            return data;
        }
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            Map<String, String> loaded = GSON.fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
            return loaded != null ? loaded : new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private static void save(File file, Map<String, String> data) {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            GSON.toJson(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getText(int id) {
        String currentLang = "en_us";
        try {
            if (net.minecraftforge.fml.common.FMLCommonHandler.instance().getSide() == net.minecraftforge.fml.relauncher.Side.CLIENT) {
                currentLang = net.minecraft.client.Minecraft.getMinecraft().gameSettings.language;
            }
        } catch (Throwable ignored) {}
        Map<String, String> activeMap = "ru_ru".equals(currentLang) ? notesDataRU : notesDataEN;
        String idStr = String.valueOf(id);
        if (activeMap.containsKey(idStr)) {
            return activeMap.get(idStr);
        }
        return notesDataEN.getOrDefault(idStr, "Text #" + id + " is missing.");
    }

    public static int getEntriesCount() {
        return Math.max(notesDataEN.size(), notesDataRU.size());
    }
}
