package com.voltyx.gender.main.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.voltyx.gender.main.GenderPlayer.Gender;

public class GenderConfigKey extends ConfigKey<Gender> {

    private static final Gender[] GENDERS = Gender.values();

    public GenderConfigKey(String key) {
        super(key, Gender.MALE);
    }

    @Override
    protected Gender read(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                int ordinal = primitive.getAsInt();
                if (ordinal >= 0 && ordinal < GENDERS.length) {
                    return GENDERS[ordinal];
                }
            } else {
                return primitive.getAsBoolean() ? Gender.MALE : Gender.FEMALE;
            }
        }
        return defaultValue;
    }

    @Override
    public void save(JsonObject object, Gender value) {
        object.addProperty(key, value.ordinal());
    }
}