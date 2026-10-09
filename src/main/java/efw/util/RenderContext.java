package efw.util;

/**
 * Shared render context flags used across multiple mixins.
 */
public final class RenderContext {

    private RenderContext() {}

    /**
     * Set to true only during GuiInventory.drawEntityOnScreen to allow custom
     * animations for the local player model in inventory without corrupting
     * the first-person arm state.
     */
    public static boolean isRenderingPlayerInGui = false;

    /**
     * Set to true when rendering the player preview inside GuiSevenScreen to force
     * a clean idle standing animation, ignoring active in-game player state.
     */
    public static boolean isRenderingPlayerInSevenScreen = false;

    /**
     * When set, any isSneaking() check on this entity returns false.
     * Used during RenderPlayer.doRender to ensure the entire player model,
     * including ModelPlayer wear layers, Baubles, and armor layers,
     * renders at standing height during Better Combat attack animations.
     */
    public static net.minecraft.entity.Entity suppressedSneakEntity = null;

    /**
     * Set to true when the blink editor is open in GuiSevenScreen to hide
     * the player's outer hat/hair layer for unobstructed face and eye configuration.
     */
    public static boolean isBlinkConfiguring = false;
}

