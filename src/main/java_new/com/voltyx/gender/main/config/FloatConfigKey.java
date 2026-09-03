package com.voltyx.gender.main.config;

import com.google.gson.JsonPrimitive;

public class FloatConfigKey extends NumberConfigKey<Float> {

    public FloatConfigKey(String key, Float defaultValue) {
        super(key, defaultValue);
    }

    public FloatConfigKey(String key, float defaultValue, float minInclusive, float maxInclusive) {
        super(key, defaultValue, minInclusive, maxInclusive);
    }

    @Override
    protected Float fromPrimitive(JsonPrimitive primitive) {
        return primitive.getAsFloat();
    }

    public float getMinInclusive() {
        return minInclusive == null ? -Float.MAX_VALUE : minInclusive;
    }

    public float getMaxInclusive() {
        return maxInclusive == null ? Float.MAX_VALUE : maxInclusive;
    }
}