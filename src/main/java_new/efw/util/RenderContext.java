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
}
