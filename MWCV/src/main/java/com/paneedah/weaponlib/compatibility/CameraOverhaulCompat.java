package com.paneedah.weaponlib.compatibility;

import net.minecraftforge.fml.common.Loader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CameraOverhaulCompat {
    private static boolean initialized = false;
    private static boolean isLoaded = false;
    private static Method createMethod;
    private static Method recreateMethod;
    private static Method getMethod;
    private static Field traumaField;
    private static Field frequencyField;
    private static Field lengthInSecondsField;

    private static long currentHandle = 0;

    private static void init() {
        if (initialized) return;
        initialized = true;

        if (Loader.isModLoaded("cameraoverhaul")) {
            try {
                Class<?> screenShakesClass = Class.forName("ua.myxazaur.cameraoverhaul.camera.ScreenShakes");
                Class<?> slotClass = Class.forName("ua.myxazaur.cameraoverhaul.camera.ScreenShakes$Slot");

                createMethod = screenShakesClass.getMethod("create");
                recreateMethod = screenShakesClass.getMethod("recreate", long.class);
                getMethod = screenShakesClass.getMethod("get", long.class);

                traumaField = slotClass.getField("trauma");
                frequencyField = slotClass.getField("frequency");
                lengthInSecondsField = slotClass.getField("lengthInSeconds");

                isLoaded = true;
            } catch (Exception e) {
                System.out.println("Failed to initialize CameraOverhaulCompat: " + e.getMessage());
            }
        }
    }

    public static void swayCamera(float trauma, float frequency, float lengthInSeconds) {
        init();
        if (!isLoaded) return;

        try {
            if (currentHandle == 0L) {
                currentHandle = (Long) createMethod.invoke(null);
            } else {
                currentHandle = (Long) recreateMethod.invoke(null, currentHandle);
            }

            Object slot = getMethod.invoke(null, currentHandle);
            traumaField.setFloat(slot, trauma);
            frequencyField.setFloat(slot, frequency);
            lengthInSecondsField.setFloat(slot, lengthInSeconds);
            System.out.println("[CameraOverhaulCompat] swayCamera success: trauma=" + trauma + " freq=" + frequency);
        } catch (Exception e) {
            System.out.println("[CameraOverhaulCompat] Exception in swayCamera:");
            e.printStackTrace();
        }
    }

    public static void directionalSwayCamera(float pitch, float yaw, float roll, float duration) {
        init();
        if (!isLoaded) return;
        try {
            Class<?> cameraSystemClass = Class.forName("ua.myxazaur.cameraoverhaul.camera.CameraSystem");
            Method triggerWeaponSwayMethod = cameraSystemClass.getMethod("triggerWeaponSway", float.class, float.class, float.class, float.class);
            triggerWeaponSwayMethod.invoke(null, pitch, yaw, roll, duration);
        } catch (Exception e) {
            System.out.println("[CameraOverhaulCompat] Exception in directionalSwayCamera:");
            e.printStackTrace();
        }
    }

    private static float targetSwayTrauma = 0.0f;
    private static float currentSwayTrauma = 0.0f;
    private static float currentSwayFrequency = 0.0f;
    private static float currentSwayLength = 0.0f;
    private static boolean isSwayFadingIn = false;
    private static long swayFadeStartTime = 0;
    private static long swayFadeDurationMs = 250;

    public static void smoothSwayCamera(float trauma, float frequency, float lengthInSeconds) {
        init();
        if (!isLoaded) return;
        
        targetSwayTrauma = trauma;
        currentSwayTrauma = 0.0f;
        currentSwayFrequency = frequency;
        currentSwayLength = lengthInSeconds;
        isSwayFadingIn = true;
        swayFadeStartTime = System.currentTimeMillis();
        
        try {
            if (currentHandle == 0L) {
                currentHandle = (Long) createMethod.invoke(null);
            } else {
                currentHandle = (Long) recreateMethod.invoke(null, currentHandle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateSwayFade() {
        if (!isLoaded || currentHandle == 0L || !isSwayFadingIn) return;
        
        long elapsed = System.currentTimeMillis() - swayFadeStartTime;
        if (elapsed >= swayFadeDurationMs) {
            currentSwayTrauma = targetSwayTrauma;
            isSwayFadingIn = false;
        } else {
            float progress = (float)elapsed / swayFadeDurationMs;
            currentSwayTrauma = targetSwayTrauma * (float)Math.sin(progress * Math.PI / 2);
        }

        try {
            Object slot = getMethod.invoke(null, currentHandle);
            traumaField.setFloat(slot, currentSwayTrauma);
            frequencyField.setFloat(slot, currentSwayFrequency);
            lengthInSecondsField.setFloat(slot, currentSwayLength);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
