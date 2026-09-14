package efw.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.common.Loader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class NotesConfig {
    private static Map<String, String> notesDataEN = new HashMap<>();
    private static Map<String, String> notesDataRU = new HashMap<>();
    private static Map<String, String> questNotesDataEN = new HashMap<>();
    private static Map<String, String> questNotesDataRU = new HashMap<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {
        File configDir = new File(Loader.instance().getConfigDir(), "efw");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        // Standard Notes
        File fileEN = new File(configDir, "notes.json");
        notesDataEN = loadFile(fileEN, false, false);
        File fileRU = new File(configDir, "notes_ru.json");
        notesDataRU = loadFile(fileRU, true, false);

        // Quest Notes
        File questFileEN = new File(configDir, "quest_notes.json");
        questNotesDataEN = loadFile(questFileEN, false, true);
        File questFileRU = new File(configDir, "quest_notes_ru.json");
        questNotesDataRU = loadFile(questFileRU, true, true);
    }

    private static Map<String, String> loadFile(File file, boolean isRussian, boolean isQuest) {
        HashMap<String, String> data = new HashMap<>();
        if (!file.exists()) {
            if (isQuest) {
                if (isRussian) {
                    data.put("1", "Квестовая записка #1: В бункере обнаружены следы выживших. Координаты ведут на восток (RU).");
                    data.put("2", "Квестовая записка #2: Пароль от гермодвери: 7492. Не сообщайте посторонним (RU).");
                } else {
                    data.put("1", "Quest Note #1: Traces of survivors discovered in the bunker. Coordinates point east (EN).");
                    data.put("2", "Quest Note #2: Airtight door passcode: 7492. Do not disclose (EN).");
                }
            } else {
                if (isRussian) {
                    data.put("1", "\u041f\u0435\u0440\u0432\u0430\u044f \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0430 \u0434\u043d\u0435\u0432\u043d\u0438\u043a\u0430 (RU)");
                    data.put("2", "\u0417\u0430\u043f\u0438\u0441\u044c \u0432 \u0441\u043f\u0435\u0448\u043a\u0435 (RU)");
                } else {
                    data.put("1", "First page of the ancient diary (EN)");
                    data.put("2", "Someone left this note in a hurry (EN)");
                }
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

    private static String getCurrentLang() {
        String currentLang = "en_us";
        try {
            if (net.minecraftforge.fml.common.FMLCommonHandler.instance().getSide() == net.minecraftforge.fml.relauncher.Side.CLIENT) {
                currentLang = net.minecraft.client.Minecraft.getMinecraft().gameSettings.language;
            }
        } catch (Throwable ignored) {}
        return currentLang;
    }

    public static String getText(int id) {
        return getText(id, false);
    }

    public static String getQuestText(int id) {
        return getText(id, true);
    }

    public static String getText(int id, boolean isQuest) {
        String currentLang = getCurrentLang();
        Map<String, String> activeMap;
        Map<String, String> fallbackMap;

        if (isQuest) {
            activeMap = "ru_ru".equals(currentLang) ? questNotesDataRU : questNotesDataEN;
            fallbackMap = questNotesDataEN;
        } else {
            activeMap = "ru_ru".equals(currentLang) ? notesDataRU : notesDataEN;
            fallbackMap = notesDataEN;
        }

        String idStr = String.valueOf(id);
        if (activeMap.containsKey(idStr)) {
            return activeMap.get(idStr);
        }
        if (fallbackMap != null && fallbackMap.containsKey(idStr)) {
            return fallbackMap.get(idStr);
        }
        return (isQuest ? "Quest Note #" : "Note #") + id + " is missing.";
    }

    public static int getEntriesCount() {
        return Math.max(notesDataEN.size(), notesDataRU.size());
    }

    public static int getQuestEntriesCount() {
        return Math.max(questNotesDataEN.size(), questNotesDataRU.size());
    }

    public static List<Integer> getAvailableIds(boolean isQuest) {
        Set<Integer> ids = new TreeSet<>();
        Map<String, String> mapEN = isQuest ? questNotesDataEN : notesDataEN;
        Map<String, String> mapRU = isQuest ? questNotesDataRU : notesDataRU;

        for (String k : mapEN.keySet()) {
            try { ids.add(Integer.parseInt(k)); } catch (NumberFormatException ignored) {}
        }
        for (String k : mapRU.keySet()) {
            try { ids.add(Integer.parseInt(k)); } catch (NumberFormatException ignored) {}
        }

        if (ids.isEmpty()) {
            ids.add(1);
        }
        return new ArrayList<>(ids);
    }
}
