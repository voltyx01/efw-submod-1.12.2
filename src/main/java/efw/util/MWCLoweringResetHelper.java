package efw.util;

import java.lang.reflect.Field;

public class MWCLoweringResetHelper {
    private static Field fieldIsLoweringActive = null;
    private static Field fieldPendingDraw = null;
    private static Field fieldLowerStartTime = null;
    private static Field fieldLowerProgress = null;
    private static Field fieldLowerProgressPrev = null;
    private static Field fieldLoweringItemStack = null;
    private static Field fieldLoweringFromSlot = null;
    private static Field fieldTrackedHotbarSlot = null;
    private static Field fieldTrackedHeldItem = null;
    private static Field fieldDrawSoundStartTime = null;
    private static Field fieldHasPlayedEarlyDrawSound = null;
    private static Field fieldLoweringTransformAlreadyApplied = null;
    private static boolean initDone = false;

    private static synchronized void init() {
        if (initDone) return;
        initDone = true;
        try {
            Class<?> clazz = Class.forName("com.paneedah.weaponlib.WeaponRenderer");
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                String name = f.getName();
                if ("isLoweringActive".equals(name)) fieldIsLoweringActive = f;
                else if ("pendingDrawAfterLowering".equals(name)) fieldPendingDraw = f;
                else if ("lowerStartTime".equals(name)) fieldLowerStartTime = f;
                else if ("lowerProgress".equals(name)) fieldLowerProgress = f;
                else if ("lowerProgressPrev".equals(name)) fieldLowerProgressPrev = f;
                else if ("loweringItemStack".equals(name)) fieldLoweringItemStack = f;
                else if ("loweringFromSlot".equals(name)) fieldLoweringFromSlot = f;
                else if ("trackedHotbarSlot".equals(name)) fieldTrackedHotbarSlot = f;
                else if ("trackedHeldItem".equals(name)) fieldTrackedHeldItem = f;
                else if ("drawSoundStartTime".equals(name)) fieldDrawSoundStartTime = f;
                else if ("hasPlayedEarlyDrawSound".equals(name)) fieldHasPlayedEarlyDrawSound = f;
                else if ("loweringTransformAlreadyApplied".equals(name)) fieldLoweringTransformAlreadyApplied = f;
            }
        } catch (Throwable ignored) {}
    }

    public static void resetLoweringState() {
        init();
        try {
            if (fieldIsLoweringActive != null) fieldIsLoweringActive.setBoolean(null, false);
            if (fieldPendingDraw != null) fieldPendingDraw.setBoolean(null, false);
            if (fieldLowerStartTime != null) fieldLowerStartTime.setLong(null, -1L);
            if (fieldLowerProgress != null) fieldLowerProgress.setFloat(null, 0.0f);
            if (fieldLowerProgressPrev != null) fieldLowerProgressPrev.setFloat(null, 0.0f);
            if (fieldLoweringItemStack != null) fieldLoweringItemStack.set(null, null);
            if (fieldLoweringFromSlot != null) fieldLoweringFromSlot.setInt(null, -1);
            if (fieldTrackedHotbarSlot != null) fieldTrackedHotbarSlot.setInt(null, -1);
            if (fieldTrackedHeldItem != null) fieldTrackedHeldItem.set(null, null);
            if (fieldDrawSoundStartTime != null) fieldDrawSoundStartTime.setLong(null, -1L);
            if (fieldHasPlayedEarlyDrawSound != null) fieldHasPlayedEarlyDrawSound.setBoolean(null, false);
            if (fieldLoweringTransformAlreadyApplied != null) fieldLoweringTransformAlreadyApplied.setBoolean(null, false);
        } catch (Throwable ignored) {}
    }
}
