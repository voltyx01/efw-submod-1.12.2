package net.bettercombat.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.Field;

public class AttackCooldownHelper {
    private static final Field TICKS_SINCE_LAST_SWING_FIELD;

    static {
        Field f = null;
        try {
            f = EntityLivingBase.class.getDeclaredField("field_184617_aD");
            f.setAccessible(true);
        } catch (NoSuchFieldException e1) {
            try {
                f = EntityLivingBase.class.getDeclaredField("ticksSinceLastSwing");
                f.setAccessible(true);
            } catch (NoSuchFieldException e2) {
                // Not found
            }
        }
        TICKS_SINCE_LAST_SWING_FIELD = f;
    }

    public static void setFullAttackStrength(EntityPlayer player) {
        if (player == null || TICKS_SINCE_LAST_SWING_FIELD == null) return;
        try {
            int fullTicks = (int) Math.ceil(player.getCooldownPeriod()) + 10;
            TICKS_SINCE_LAST_SWING_FIELD.setInt(player, fullTicks);
        } catch (Throwable ignored) {}
    }
}
