package com.voltyx.gender.api;

/**
 * Expose this as a capability on your chestplates or items that go in the chest slot to configure how it interacts with breast rendering.
 */
public interface IGenderArmor {

    /**
     * Determines whether this IGenderArmor "covers" the breasts or if it has an open front (false) like the elytra.
     *
     * @return true if the breasts are covered.
     */
    default boolean coversBreasts() {
        return true;
    }

    /**
     * Determines if this IGenderArmor should always hide the wearer's breasts when worn even if they have showBreastsInArmor set to true. This is
     * useful for armors that may have custom rendering that is not compatible with how the breasts render and would just lead to clipping.
     *
     * @return true to hide the breasts regardless of what showBreastsInArmor is set to.
     */
    default boolean alwaysHidesBreasts() {
        return false;
    }

    /**
     * The percent of physical resistance this IGenderArmor provides to the wearer's breasts when calculating the corresponding physics.
     *
     * @return Value between 0 (no resistance, full physics) and 1 (total resistance, no physics).
     */
    default float physicsResistance() {
        return 0;
    }

    /**
     * Value representing how "tight" this IGenderArmor is. Tightness "compresses" the breasts against the wearer causing the breasts to appear up to 15%
     * smaller.
     *
     * @return Value between 0 (no tightness, no size reduction) and 1 (full tightness, 15% size reduction).
     */
    default float tightness() {
        return 0;
    }
}