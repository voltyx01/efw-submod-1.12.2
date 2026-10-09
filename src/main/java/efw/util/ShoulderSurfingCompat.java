package efw.util;

import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.config.Perspective;
import net.minecraft.client.Minecraft;

public class ShoulderSurfingCompat {

    private static Perspective previousPerspective = null;
    private static boolean isAutoSwitched = false;

    public static boolean doShoulderSurfing() {
        try {
            return ShoulderInstance.getInstance().doShoulderSurfing();
        } catch (Throwable t) {
            return false;
        }
    }

    public static void updateShoulderSurfingLogic(boolean isModifying, boolean isShiftRightClick) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            ShoulderInstance shoulder = ShoulderInstance.getInstance();
            if (isModifying || isShiftRightClick) {
                if (!isAutoSwitched && (shoulder.doShoulderSurfing() || mc.gameSettings.thirdPersonView != 0)) {
                    previousPerspective = Perspective.current();
                    shoulder.changePerspective(Perspective.FIRST_PERSON);
                    isAutoSwitched = true;
                }
            } else if (isAutoSwitched) {
                resetCamera();
            }
        } catch (Throwable t) {}
    }

    public static void resetCamera() {
        try {
            if (isAutoSwitched) {
                if (previousPerspective != null) {
                    ShoulderInstance.getInstance().changePerspective(previousPerspective);
                }
                isAutoSwitched = false;
                previousPerspective = null;
            }
        } catch (Throwable t) {}
    }

    public static boolean isAutoSwitched() {
        return isAutoSwitched;
    }

    private static Perspective dollPreviousPerspective = null;
    private static boolean isDollAutoSwitched = false;

    /**
     * Dedicated doll perspective switch: immediately switches to first-person cleanly
     * when doll is activated, without conflicting with weapon modification logic.
     */
    public static void switchForDoll() {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc == null || mc.gameSettings == null) return;
            ShoulderInstance shoulder = ShoulderInstance.getInstance();
            if (!isDollAutoSwitched && (shoulder.doShoulderSurfing() || mc.gameSettings.thirdPersonView != 0)) {
                dollPreviousPerspective = Perspective.current();
                if (shoulder.doShoulderSurfing()) {
                    shoulder.changePerspective(Perspective.FIRST_PERSON);
                } else {
                    mc.gameSettings.thirdPersonView = 0;
                }
                isDollAutoSwitched = true;
                com.voltyx.mwccf.render.doll.DollRenderer.resetSway();
            }
        } catch (Throwable t) {}
    }

    /**
     * Restores camera perspective when doll sequence ends or player dies.
     */
    public static void resetDollCamera() {
        try {
            if (isDollAutoSwitched) {
                if (dollPreviousPerspective != null) {
                    ShoulderInstance.getInstance().changePerspective(dollPreviousPerspective);
                }
                isDollAutoSwitched = false;
                dollPreviousPerspective = null;
            }
        } catch (Throwable t) {}
    }
}
