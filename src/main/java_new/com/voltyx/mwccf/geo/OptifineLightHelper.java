package com.voltyx.mwccf.geo;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderGlobal;
import java.lang.reflect.Method;

public class OptifineLightHelper {
    private static Class<?> dynamicLightsClass;
    private static Method entityAddedMethod;
    private static Method entityRemovedMethod;
    private static boolean initialized = false;

    private static void init() {
        if (initialized) return;
        initialized = true;
        try {
            dynamicLightsClass = Class.forName("net.optifine.DynamicLights");
            for (Method m : dynamicLightsClass.getDeclaredMethods()) {
                if (m.getName().equals("entityAdded") && m.getParameterTypes().length == 2) {
                    entityAddedMethod = m;
                    entityAddedMethod.setAccessible(true);
                } else if (m.getName().equals("entityRemoved") && m.getParameterTypes().length == 2) {
                    entityRemovedMethod = m;
                    entityRemovedMethod.setAccessible(true);
                }
            }
        } catch (Exception e) {
            // OptiFine not installed
        }
    }

    public static void addLight(Entity entity, RenderGlobal renderGlobal) {
        init();
        if (entityAddedMethod != null) {
            try {
                entityAddedMethod.invoke(null, entity, renderGlobal);
            } catch (Exception e) {}
        }
    }

    public static void removeLight(Entity entity, RenderGlobal renderGlobal) {
        init();
        if (entityRemovedMethod != null) {
            try {
                entityRemovedMethod.invoke(null, entity, renderGlobal);
            } catch (Exception e) {}
        }
    }
}
