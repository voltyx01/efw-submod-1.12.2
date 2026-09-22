package net.bettercombat.logic;

public enum AnimatedHand {
    MAIN_HAND,
    OFF_HAND,
    DUAL_HANDED;

    public static AnimatedHand from(boolean isOffHand, boolean isTwoHanded) {
        if (isTwoHanded) {
            return DUAL_HANDED;
        }
        return isOffHand ? OFF_HAND : MAIN_HAND;
    }
}
