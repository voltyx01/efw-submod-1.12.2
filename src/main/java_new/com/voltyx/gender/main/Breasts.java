package com.voltyx.gender.main;

import com.voltyx.gender.main.config.ClientConfiguration;
import com.voltyx.gender.main.config.ConfigKey;
import java.util.function.Consumer;

public class Breasts {

    private float xOffset = ClientConfiguration.BREASTS_OFFSET_X.getDefault();
    private float yOffset = ClientConfiguration.BREASTS_OFFSET_Y.getDefault();
    private float zOffset = ClientConfiguration.BREASTS_OFFSET_Z.getDefault();
    private float cleavage = ClientConfiguration.BREASTS_CLEAVAGE.getDefault();
    private boolean uniboob = ClientConfiguration.BREASTS_UNIBOOB.getDefault();

    private <VALUE> boolean updateValue(ConfigKey<VALUE> key, VALUE value, Consumer<VALUE> setter) {
        if (key.validate(value)) {
            setter.accept(value);
            return true;
        }
        return false;
    }

    public float getXOffset() {
        return xOffset;
    }

    public boolean updateXOffset(float value) {
        return updateValue(ClientConfiguration.BREASTS_OFFSET_X, value, v -> this.xOffset = v);
    }

    public float getYOffset() {
        return yOffset;
    }

    public boolean updateYOffset(float value) {
        return updateValue(ClientConfiguration.BREASTS_OFFSET_Y, value, v -> this.yOffset = v);
    }

    public float getZOffset() {
        return zOffset;
    }

    public boolean updateZOffset(float value) {
        return updateValue(ClientConfiguration.BREASTS_OFFSET_Z, value, v -> this.zOffset = v);
    }

    public float getCleavage() {
        return cleavage;
    }

    public boolean updateCleavage(float value) {
        return updateValue(ClientConfiguration.BREASTS_CLEAVAGE, value, v -> this.cleavage = v);
    }

    public boolean isUniboob() {
        return uniboob;
    }

    public boolean updateUniboob(boolean value) {
        return updateValue(ClientConfiguration.BREASTS_UNIBOOB, value, v -> this.uniboob = v);
    }
}