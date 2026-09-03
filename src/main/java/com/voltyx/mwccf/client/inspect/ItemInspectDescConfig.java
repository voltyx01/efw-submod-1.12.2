package com.voltyx.mwccf.client.inspect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.voltyx.mwccf.client.loading.LoadingScreenEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemInspectDescConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static List<LoadingScreenEntry> entriesEn = new ArrayList<>();
    private static List<LoadingScreenEntry> entriesRu = new ArrayList<>();

    public static void load(File configDir) {
        File fileEn = new File(configDir, "inspect_items_en.json");
        File fileRu = new File(configDir, "inspect_items_ru.json");

        syncFromDescFolder(configDir, fileEn, fileRu);

        entriesEn = loadFile(fileEn);
        entriesRu = loadFile(fileRu);
    }

    private static void syncFromDescFolder(File configDir, File fileEn, File fileRu) {
        List<File> searchDirs = Arrays.asList(
                new File(configDir.getParentFile(), "desc"),
                new File(configDir, "desc"),
                new File("desc"),
                new File(Minecraft.getMinecraft().gameDir, "desc"),
                new File(System.getProperty("user.dir"), "desc")
        );

        File ruTxt = null;
        File enTxt = null;

        for (File dir : searchDirs) {
            File r = new File(dir, "GunsRus.txt");
            if (r.exists()) {
                ruTxt = r;
                break;
            }
        }
        for (File dir : searchDirs) {
            File e = new File(dir, "GunsEng.txt");
            if (e.exists()) {
                enTxt = e;
                break;
            }
        }

        List<LoadingScreenEntry> parsedRu = parseDescTxt(ruTxt);
        List<LoadingScreenEntry> parsedEn = parseDescTxt(enTxt);

        if (!parsedRu.isEmpty()) {
            List<LoadingScreenEntry> existingRu = fileRu.exists() ? loadFile(fileRu) : new ArrayList<>();
            List<LoadingScreenEntry> mergedRu = mergeEntries(existingRu, parsedRu);
            writeToFile(fileRu, mergedRu);
        } else if (!fileRu.exists()) {
            writeDefaultRu(fileRu);
        }

        if (!parsedEn.isEmpty()) {
            List<LoadingScreenEntry> existingEn = fileEn.exists() ? loadFile(fileEn) : new ArrayList<>();
            List<LoadingScreenEntry> mergedEn = mergeEntries(existingEn, parsedEn);
            writeToFile(fileEn, mergedEn);
        } else if (!fileEn.exists()) {
            writeDefaultEn(fileEn);
        }
    }

    private static List<LoadingScreenEntry> mergeEntries(List<LoadingScreenEntry> existing, List<LoadingScreenEntry> incoming) {
        Map<String, LoadingScreenEntry> map = new LinkedHashMap<>();
        if (existing != null) {
            for (LoadingScreenEntry e : existing) {
                if (e != null && e.item != null) {
                    map.put(e.item.toLowerCase(), e);
                }
            }
        }
        if (incoming != null) {
            for (LoadingScreenEntry e : incoming) {
                if (e != null && e.item != null) {
                    map.put(e.item.toLowerCase(), e);
                }
            }
        }
        return new ArrayList<>(map.values());
    }

    private static List<LoadingScreenEntry> parseDescTxt(File file) {
        List<LoadingScreenEntry> list = new ArrayList<>();
        if (file == null || !file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            Pattern pattern = Pattern.compile("\\*\\*([a-zA-Z0-9_]+)\\*\\*:\\s*\"([^\"]+)\"");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                if (m.find()) {
                    String gunId = m.group(1).trim().toLowerCase();
                    String desc = m.group(2).trim();
                    LoadingScreenEntry entry = new LoadingScreenEntry();
                    entry.item = "mwc:" + gunId;
                    entry.meta = 0;
                    entry.description = desc;
                    entry.lore = "";
                    list.add(entry);
                }
            }
            FMLLog.info("[ItemInspect] Loaded %d item descriptions from %s", list.size(), file.getName());
        } catch (Exception e) {
            FMLLog.warning("[ItemInspect] Error parsing %s: %s", file.getName(), e.getMessage());
        }
        return list;
    }

    private static List<LoadingScreenEntry> loadFile(File file) {
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<LoadingScreenEntry>>(){}.getType();
            List<LoadingScreenEntry> loaded = GSON.fromJson(reader, listType);
            if (loaded != null && !loaded.isEmpty()) {
                FMLLog.info("[ItemInspect] Загружено %d описаний из %s", loaded.size(), file.getName());
                return loaded;
            }
        } catch (Exception e) {
            FMLLog.warning("[ItemInspect] Ошибка загрузки %s: %s", file.getName(), e.getMessage());
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

    public static LoadingScreenEntry getEntryForStack(ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.getItem() == null) return null;
        ResourceLocation regName = stack.getItem().getRegistryName();
        if (regName == null) return null;
        String fullRegName = regName.toString().toLowerCase();
        String pathName = regName.getPath().toLowerCase();
        int meta = stack.getMetadata();

        for (LoadingScreenEntry entry : getEntries()) {
            if (entry != null && entry.item != null) {
                String entryItem = entry.item.toLowerCase();
                if (entryItem.equals(fullRegName)
                        || entryItem.equals(pathName)
                        || entryItem.equals("mwc:" + pathName)
                        || fullRegName.endsWith(":" + entryItem)
                        || fullRegName.contains(entryItem.replace("mwc:", ""))) {
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
        writeToFile(file, defaults);
    }

    private static void writeDefaultRu(File file) {
        List<LoadingScreenEntry> defaults = new ArrayList<>();
        writeToFile(file, defaults);
    }

    private static void writeToFile(File file, List<LoadingScreenEntry> list) {
        try {
            file.getParentFile().mkdirs();
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                GSON.toJson(list, writer);
            }
        } catch (Exception e) {
            FMLLog.warning("[ItemInspect] Не удалось записать %s: %s", file.getName(), e.getMessage());
        }
    }
}
