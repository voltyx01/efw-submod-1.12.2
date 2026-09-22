package net.bettercombat.api;

import com.google.gson.annotations.SerializedName;
import javax.annotation.Nullable;
import java.util.Objects;

public final class WeaponAttributes {

    private final double attack_range;
    @Nullable
    private final String pose;
    @Nullable
    private final String off_hand_pose;
    private final Boolean two_handed;
    @Nullable
    private final String category;
    private final Attack[] attacks;

    public WeaponAttributes(
            double attack_range,
            @Nullable String pose,
            @Nullable String off_hand_pose,
            Boolean isTwoHanded,
            @Nullable String category,
            Attack[] attacks) {
        this.attack_range = attack_range;
        this.pose = pose;
        this.off_hand_pose = off_hand_pose;
        this.two_handed = isTwoHanded != null && isTwoHanded;
        this.category = category;
        this.attacks = attacks != null ? attacks : new Attack[0];
    }

    public double attackRange() {
        return attack_range;
    }

    @Nullable
    public String pose() {
        return pose;
    }

    @Nullable
    public String offHandPose() {
        return off_hand_pose;
    }

    public boolean isTwoHanded() {
        return two_handed != null && two_handed;
    }

    @Nullable
    public String category() {
        return category;
    }

    public Attack[] attacks() {
        return attacks;
    }

    public static final class Attack {
        private Condition[] conditions;
        private HitBoxShape hitbox = HitBoxShape.HORIZONTAL_PLANE;
        private double damage_multiplier = 1.0;
        private double angle = 0.0;
        private double upswing = 0.5;
        private String animation = null;
        private Sound swing_sound = null;
        private Sound impact_sound = null;

        public Attack() {}

        public Attack(
                Condition[] conditions,
                HitBoxShape hitbox,
                double damage_multiplier,
                double angle,
                double upswing,
                String animation,
                Sound swing_sound,
                Sound impact_sound) {
            this.conditions = conditions;
            this.hitbox = hitbox != null ? hitbox : HitBoxShape.HORIZONTAL_PLANE;
            this.damage_multiplier = damage_multiplier;
            this.angle = angle;
            this.upswing = upswing;
            this.animation = animation;
            this.swing_sound = swing_sound;
            this.impact_sound = impact_sound;
        }

        @Nullable
        public Condition[] conditions() {
            return conditions;
        }

        public HitBoxShape hitbox() {
            return hitbox != null ? hitbox : HitBoxShape.HORIZONTAL_PLANE;
        }

        public double damageMultiplier() {
            return damage_multiplier;
        }

        public double angle() {
            return angle;
        }

        public double upswing() {
            return upswing;
        }

        public double upswingRate() {
            return Math.max(0.05, Math.min(0.95, upswing * efw.biomeinfo.MwccfConfig.betterCombat.upswingMultiplier));
        }

        @Nullable
        public String animation() {
            return animation;
        }

        @Nullable
        public Sound swingSound() {
            return swing_sound;
        }

        @Nullable
        public Sound impactSound() {
            return impact_sound;
        }
    }

    public enum HitBoxShape {
        FORWARD_BOX,
        VERTICAL_PLANE,
        HORIZONTAL_PLANE
    }

    public enum Condition {
        NOT_DUAL_WIELDING,
        DUAL_WIELDING_ANY,
        DUAL_WIELDING_SAME,
        DUAL_WIELDING_SAME_CATEGORY,
        NO_OFFHAND_ITEM,
        OFF_HAND_SHIELD,
        MAIN_HAND_ONLY,
        OFF_HAND_ONLY,
        MOUNTED,
        NOT_MOUNTED
    }

    public static final class Sound {
        private String id = null;
        private Float volume = null;
        private Float pitch = null;
        private Float randomness = null;

        public Sound() {}

        public Sound(String id, Float volume, Float pitch, Float randomness) {
            this.id = id;
            this.volume = volume;
            this.pitch = pitch;
            this.randomness = randomness;
        }

        public String id() {
            return id;
        }

        public float volume() {
            return volume != null ? volume : 1.0f;
        }

        public float pitch() {
            return pitch != null ? pitch : 1.0f;
        }

        public float randomness() {
            return randomness != null ? randomness : 0.1f;
        }
    }
}
