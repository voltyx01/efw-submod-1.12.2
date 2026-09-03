package com.voltyx.gender.render.armor;

import com.voltyx.gender.api.IGenderArmor;

/**
 * Base class to help define default implementations of IGenderArmor.
 */
public class SimpleGenderArmor implements IGenderArmor {

    public static final SimpleGenderArmor FALLBACK = new SimpleGenderArmor(0.5F);
    public static final SimpleGenderArmor LEATHER = new SimpleGenderArmor(0.3F, 0.5F);
    public static final SimpleGenderArmor CHAIN_MAIL = new SimpleGenderArmor(0.5F, 0.2F);
    public static final SimpleGenderArmor GOLD = new SimpleGenderArmor(0.85F);
    public static final SimpleGenderArmor IRON = new SimpleGenderArmor(1);
    public static final SimpleGenderArmor DIAMOND = new SimpleGenderArmor(1);
    // Незерита в 1.12.2 нет, поэтому убираем его

    private final float physicsResistance;
    private final float tightness;

    public SimpleGenderArmor(float physicsResistance, float tightness) {
        this.physicsResistance = physicsResistance;
        this.tightness = tightness;
    }

    public SimpleGenderArmor(float physicsResistance) {
        this(physicsResistance, 0);
    }

    @Override
    public float physicsResistance() {
        return this.physicsResistance;
    }

    @Override
    public float tightness() {
        return this.tightness;
    }
}