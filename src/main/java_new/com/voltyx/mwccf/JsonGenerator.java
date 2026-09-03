package com.voltyx.mwccf;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;
import com.google.gson.*;

public class JsonGenerator {
    public static void main(String[] args) throws Exception {
        File ruTxt = new File("desc/GunsRus.txt");
        File enTxt = new File("desc/GunsEng.txt");

        List<Map<String, Object>> gunsRu = parse(ruTxt);
        List<Map<String, Object>> gunsEn = parse(enTxt);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String[] dirs = {"config", "run/config"};
        for (String d : dirs) {
            new File(d).mkdirs();
            save(new File(d, "inspect_items_ru.json"), gson.toJson(gunsRu));
            save(new File(d, "inspect_items_en.json"), gson.toJson(gunsEn));

            List<Map<String, Object>> loadRu = new ArrayList<>();
            Map<String, Object> r1 = new LinkedHashMap<>();
            r1.put("item", "minecraft:iron_sword");
            r1.put("meta", 0);
            r1.put("description", "Стальной клинок, найденный среди руин.");
            r1.put("lore", "Его хозяин не выжил. Ты — другое дело.");
            loadRu.add(r1);

            Map<String, Object> r2 = new LinkedHashMap<>();
            r2.put("item", "minecraft:golden_apple");
            r2.put("meta", 0);
            r2.put("description", "Редкость даже до катастрофы.");
            r2.put("lore", "Говорят, в старом мире это стоило целое состояние.");
            loadRu.add(r2);

            save(new File(d, "loading_screen_items_ru.json"), gson.toJson(loadRu));

            List<Map<String, Object>> loadEn = new ArrayList<>();
            Map<String, Object> e1 = new LinkedHashMap<>();
            e1.put("item", "minecraft:iron_sword");
            e1.put("meta", 0);
            e1.put("description", "A steel blade found among the ruins.");
            e1.put("lore", "Its master didn't survive. You are different.");
            loadEn.add(e1);

            Map<String, Object> e2 = new LinkedHashMap<>();
            e2.put("item", "minecraft:golden_apple");
            e2.put("meta", 0);
            e2.put("description", "A rarity even before the catastrophe.");
            e2.put("lore", "They say this was worth a fortune in the old world.");
            loadEn.add(e2);

            save(new File(d, "loading_screen_items_en.json"), gson.toJson(loadEn));
        }

        System.out.println("All 4 JSON files created successfully!");
    }

    private static List<Map<String, Object>> parse(File f) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            Pattern p = Pattern.compile("\\*\\*([a-zA-Z0-9_]+)\\*\\*:\\s*\"([^\"]+)\"");
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("item", "mwc:" + m.group(1).trim().toLowerCase());
                    map.put("meta", 0);
                    map.put("description", m.group(2).trim());
                    map.put("lore", "");
                    list.add(map);
                }
            }
        }
        return list;
    }

    private static void save(File f, String content) throws Exception {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8)) {
            w.write(content);
        }
    }
}
