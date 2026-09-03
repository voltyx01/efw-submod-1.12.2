/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.util.DamageSource
 */
package com.eruannie_9.extragore.pack;

import com.eruannie_9.extragore.pack.BloodDamageKind;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.DamageSource;

public final class BloodDamageClassifier {
    private BloodDamageClassifier() {
    }

    @Nonnull
    public static BloodDamageKind classify(@Nullable DamageSource source) {
        boolean voidDamage;
        boolean lightning;
        boolean fall;
        boolean effects;
        boolean cactus;
        boolean suffocation;
        boolean drowning;
        boolean fire;
        if (source == null) {
            return BloodDamageKind.UNKNOWN;
        }
        String damageType = source.getDamageType();
        boolean hasCarrier = source.getTrueSource() != null || source.getImmediateSource() != null || source.isProjectile() || source.isExplosion();
        boolean bl = fire = source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE || source == DamageSource.LAVA || source == DamageSource.HOT_FLOOR || BloodDamageClassifier.matchesDamageType(damageType, "inFire", "onFire", "lava", "hotFloor") || !hasCarrier && source.isFireDamage();
        if (fire) {
            return BloodDamageKind.FIRE;
        }
        boolean bl2 = drowning = source == DamageSource.DROWN || BloodDamageClassifier.matchesDamageType(damageType, "drown");
        if (drowning) {
            return BloodDamageKind.DROWNING;
        }
        boolean bl3 = suffocation = source == DamageSource.IN_WALL || source == DamageSource.CRAMMING || BloodDamageClassifier.matchesDamageType(damageType, "inWall", "cramming");
        if (suffocation) {
            return BloodDamageKind.SUFFOCATION;
        }
        boolean bl4 = cactus = source == DamageSource.CACTUS || BloodDamageClassifier.matchesDamageType(damageType, "cactus");
        if (cactus) {
            return BloodDamageKind.CACTUS;
        }
        boolean bl5 = effects = source == DamageSource.MAGIC || source == DamageSource.WITHER || source == DamageSource.DRAGON_BREATH || source == DamageSource.STARVE || source.isMagicDamage() || BloodDamageClassifier.matchesDamageType(damageType, "magic", "indirectMagic", "wither", "dragonBreath", "starve");
        if (effects) {
            return BloodDamageKind.EFFECTS;
        }
        boolean bl6 = fall = source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL || source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK || BloodDamageClassifier.matchesDamageType(damageType, "fall", "flyIntoWall", "anvil", "fallingBlock");
        if (fall) {
            return BloodDamageKind.FALL;
        }
        boolean bl7 = lightning = source == DamageSource.LIGHTNING_BOLT || BloodDamageClassifier.matchesDamageType(damageType, "lightningBolt");
        if (lightning) {
            return BloodDamageKind.LIGHTNING;
        }
        boolean bl8 = voidDamage = source == DamageSource.OUT_OF_WORLD || BloodDamageClassifier.matchesDamageType(damageType, "outOfWorld");
        if (voidDamage) {
            return BloodDamageKind.VOID;
        }
        return hasCarrier ? BloodDamageKind.IMPACT : BloodDamageKind.UNKNOWN;
    }

    private static boolean matchesDamageType(@Nullable String damageType, String ... expected) {
        if (damageType == null || damageType.isEmpty()) {
            return false;
        }
        for (String s : expected) {
            if (!damageType.equals(s)) continue;
            return true;
        }
        return false;
    }
}

