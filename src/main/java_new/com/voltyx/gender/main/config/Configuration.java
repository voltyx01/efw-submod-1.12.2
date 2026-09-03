package com.voltyx.gender.main.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Configuration {

	private static final TypeAdapter<JsonObject> ADAPTER = new Gson().getAdapter(JsonObject.class);

	private final File CFG_FILE;
	public JsonObject SAVE_VALUES = new JsonObject();

	public Configuration(String saveLoc, String cfgName) {
		// In 1.12.2, we get the config directory directly from the Loader
		File configDir = Loader.instance().getConfigDir();
		File saveDir = new File(configDir, saveLoc);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		CFG_FILE = new File(saveDir, cfgName + ".json");
	}

	public void finish() {
		if (CFG_FILE.exists()) {
			load();
			updateConfig();
		}
	}

	public <TYPE> void set(ConfigKey<TYPE> key, TYPE value) {
		key.save(SAVE_VALUES, value);
	}

	public <TYPE> void setDefault(ConfigKey<TYPE> key) {
		if (!SAVE_VALUES.has(key.key)) {
			set(key, key.defaultValue);
		}
	}

	public <TYPE> TYPE get(ConfigKey<TYPE> key) {
		return key.read(SAVE_VALUES);
	}

	public void removeParameter(ConfigKey<?> key) {
		removeParameter(key.key);
	}

	public void removeParameter(String key) {
		SAVE_VALUES.remove(key);
	}

	public void updateConfig() {
		JsonObject obj;
		try (FileReader configurationFile = new FileReader(CFG_FILE)) {
			// In 1.12.2, JsonUtils.fromJson is used instead of GsonHelper.parse
			obj = JsonUtils.fromJson(new Gson(), configurationFile, JsonObject.class);
			if (obj == null) obj = new JsonObject();

			// Merge with existing values
			for (Map.Entry<String, JsonElement> entry : SAVE_VALUES.entrySet()) {
				obj.add(entry.getKey(), entry.getValue());
			}
		} catch (Exception ignored) {
			return;
		}
		try (FileWriter writer = new FileWriter(CFG_FILE);
		     JsonWriter jsonWriter = new JsonWriter(writer)) {
			ADAPTER.write(jsonWriter, obj);
		} catch (Exception ignored) {}
	}

	public void save() {
		try (FileWriter writer = new FileWriter(CFG_FILE);
		     JsonWriter jsonWriter = new JsonWriter(writer)) {
			ADAPTER.write(jsonWriter, SAVE_VALUES);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void load() {
		try (FileReader configurationFile = new FileReader(CFG_FILE)) {
			JsonObject obj = JsonUtils.fromJson(new Gson(), configurationFile, JsonObject.class);
			if (obj != null) {
				for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
					String key = entry.getKey();
					SAVE_VALUES.add(key, entry.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}