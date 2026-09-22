package net.bettercombat.logic;

import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import java.util.WeakHashMap;

public class PlayerAttackProperties {
    private static final Map<EntityPlayer, Integer> COMBO_MAP = new WeakHashMap<>();

    public static int getComboCount(EntityPlayer player) {
        if (player == null) return 0;
        Integer count = COMBO_MAP.get(player);
        return count != null ? count : 0;
    }

    public static void setComboCount(EntityPlayer player, int comboCount) {
        if (player == null) return;
        COMBO_MAP.put(player, Math.max(0, comboCount));
    }
}
