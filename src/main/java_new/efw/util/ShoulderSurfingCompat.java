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
}
