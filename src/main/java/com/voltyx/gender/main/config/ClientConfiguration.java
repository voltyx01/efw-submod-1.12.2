package com.voltyx.gender.main.config;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ClientConfiguration extends Configuration {

    public static final UUIDConfigKey USERNAME = new UUIDConfigKey("username", UUID.nameUUIDFromBytes("UNKNOWN".getBytes(StandardCharsets.UTF_8)));
    public static final GenderConfigKey GENDER = new GenderConfigKey("gender");
    public static final FloatConfigKey BUST_SIZE = new FloatConfigKey("bust_size", 0.6F, 0, 1);
    public static final BooleanConfigKey HURT_SOUNDS = new BooleanConfigKey("hurt_sounds", true);
    
    public static final BooleanConfigKey BLINK_ENABLED = new BooleanConfigKey("blink_enabled", false);
    public static final FloatConfigKey EYE_SIZE = new FloatConfigKey("eye_size", 1.0F, 0, 2);
    public static final FloatConfigKey EYE_DISTANCE = new FloatConfigKey("eye_distance", 1.0F, 1, 4);
    public static final FloatConfigKey EYE_HEIGHT = new FloatConfigKey("eye_height", 4.0F, 0, 8);
    public static final FloatConfigKey EYELID_SIZE = new FloatConfigKey("eyelid_size", 1.0F, 0, 2);
    public static final BooleanConfigKey DUAL_EYELID = new BooleanConfigKey("dual_eyelid", false);
    public static final FloatConfigKey BLINK_FREQUENCY = new FloatConfigKey("blink_frequency", 1.15F, 0.80F, 1.35F);
    public static final FloatConfigKey EYELID_OFFSET_X = new FloatConfigKey("eyelid_offset_x", 0.0F, -8, 8);
    public static final FloatConfigKey EYELID_OFFSET_Y = new FloatConfigKey("eyelid_offset_y", -2.0F, -8, 8);

    public static final FloatConfigKey BREASTS_OFFSET_X = new FloatConfigKey("breasts_xOffset", 0.0F, -1, 1);
    public static final FloatConfigKey BREASTS_OFFSET_Y = new FloatConfigKey("breasts_yOffset", 0.0F, -1, 1);
    public static final FloatConfigKey BREASTS_OFFSET_Z = new FloatConfigKey("breasts_zOffset", 0.0F, -1, 0);
    public static final BooleanConfigKey BREASTS_UNIBOOB = new BooleanConfigKey("breasts_uniboob", true);
    public static final FloatConfigKey BREASTS_CLEAVAGE = new FloatConfigKey("breasts_cleavage", 0.05F, 0, 0.1F);

    public static final BooleanConfigKey BREAST_PHYSICS = new BooleanConfigKey("breast_physics", false);
    public static final BooleanConfigKey BREAST_PHYSICS_ARMOR = new BooleanConfigKey("breast_physics_armor", false);
    public static final BooleanConfigKey SHOW_IN_ARMOR = new BooleanConfigKey("show_in_armor", true);
    public static final FloatConfigKey BOUNCE_MULTIPLIER = new FloatConfigKey("bounce_multiplier", 0.34F, 0, 1);
    public static final FloatConfigKey FLOPPY_MULTIPLIER = new FloatConfigKey("floppy_multiplier", 0.95F, 0, 1);

    public ClientConfiguration(String saveLoc, String cfgName) {
        super(saveLoc, cfgName);
    }
}