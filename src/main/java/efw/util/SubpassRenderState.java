package efw.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SubpassRenderState {

    public static boolean isMirrorRendering = false;
    public static boolean mirrorRenderedThisTick = false;

    private static Field fogColorRedField = null;
    private static Field fogColorGreenField = null;
    private static Field fogColorBlueField = null;
    private static Field lightmapUpdateNeededField = null;

    private static Field positionField = null;
    private static Field rotationXField = null;
    private static Field rotationXZField = null;
    private static Field rotationZField = null;
    private static Field rotationYZField = null;
    private static Field rotationXYField = null;

    static {
        try {
            fogColorRedField = EntityRenderer.class.getDeclaredField("field_175080_Q");
            fogColorRedField.setAccessible(true);
        } catch (Throwable e) {
            try {
                fogColorRedField = EntityRenderer.class.getDeclaredField("fogColorRed");
                fogColorRedField.setAccessible(true);
            } catch (Throwable ignored) {}
        }

        try {
            fogColorGreenField = EntityRenderer.class.getDeclaredField("field_175082_R");
            fogColorGreenField.setAccessible(true);
        } catch (Throwable e) {
            try {
                fogColorGreenField = EntityRenderer.class.getDeclaredField("fogColorGreen");
                fogColorGreenField.setAccessible(true);
            } catch (Throwable ignored) {}
        }

        try {
            fogColorBlueField = EntityRenderer.class.getDeclaredField("field_175081_S");
            fogColorBlueField.setAccessible(true);
        } catch (Throwable e) {
            try {
                fogColorBlueField = EntityRenderer.class.getDeclaredField("fogColorBlue");
                fogColorBlueField.setAccessible(true);
            } catch (Throwable ignored) {}
        }

        try {
            lightmapUpdateNeededField = EntityRenderer.class.getDeclaredField("field_78536_aa");
            lightmapUpdateNeededField.setAccessible(true);
        } catch (Throwable e) {
            try {
                lightmapUpdateNeededField = EntityRenderer.class.getDeclaredField("lightmapUpdateNeeded");
                lightmapUpdateNeededField.setAccessible(true);
            } catch (Throwable ignored) {}
        }

        try {
            positionField = ActiveRenderInfo.class.getDeclaredField("field_178811_e");
            positionField.setAccessible(true);
        } catch (Throwable e) {
            try {
                positionField = ActiveRenderInfo.class.getDeclaredField("position");
                positionField.setAccessible(true);
            } catch (Throwable ignored) {}
        }

        try {
            rotationXField = ActiveRenderInfo.class.getDeclaredField("field_74588_d");
            rotationXField.setAccessible(true);
        } catch (Throwable e) {
            try {
                rotationXField = ActiveRenderInfo.class.getDeclaredField("rotationX");
                rotationXField.setAccessible(true);
            } catch (Throwable ignored) {}
        }

        try {
            rotationXZField = ActiveRenderInfo.class.getDeclaredField("field_74589_e");
            rotationXZField.setAccessible(true);
        } catch (Throwable e) {
            try {
                rotationXZField = ActiveRenderInfo.class.getDeclaredField("rotationXZ");
                rotationXZField.setAccessible(true);
            } catch (Throwable ignored) {}
        }

        try {
            rotationZField = ActiveRenderInfo.class.getDeclaredField("field_74586_f");
            rotationZField.setAccessible(true);
        } catch (Throwable e) {
            try {
                rotationZField = ActiveRenderInfo.class.getDeclaredField("rotationZ");
                rotationZField.setAccessible(true);
            } catch (Throwable ignored) {}
        }

        try {
            rotationYZField = ActiveRenderInfo.class.getDeclaredField("field_74587_g");
            rotationYZField.setAccessible(true);
        } catch (Throwable e) {
            try {
                rotationYZField = ActiveRenderInfo.class.getDeclaredField("rotationYZ");
                rotationYZField.setAccessible(true);
            } catch (Throwable ignored) {}
        }

        try {
            rotationXYField = ActiveRenderInfo.class.getDeclaredField("field_74596_h");
            rotationXYField.setAccessible(true);
        } catch (Throwable e) {
            try {
                rotationXYField = ActiveRenderInfo.class.getDeclaredField("rotationXY");
                rotationXYField.setAccessible(true);
            } catch (Throwable ignored) {}
        }
    }

    private float savedFogRed;
    private float savedFogGreen;
    private float savedFogBlue;
    private boolean prevFogEnabled;
    private int prevFogMode;
    private float prevFogStart;
    private float prevFogEnd;
    private float prevFogDensity;
    private final FloatBuffer prevFogColor = BufferUtils.createFloatBuffer(16);
    private final FloatBuffer prevClearColor = BufferUtils.createFloatBuffer(16);
    private final IntBuffer prevViewport = BufferUtils.createIntBuffer(16);

    private net.minecraft.util.math.Vec3d savedPosition;
    private float savedRotationX;
    private float savedRotationXZ;
    private float savedRotationZ;
    private float savedRotationYZ;
    private float savedRotationXY;

    public void save(EntityRenderer renderer) {
        if (renderer != null) {
            try {
                if (fogColorRedField != null) savedFogRed = fogColorRedField.getFloat(renderer);
                if (fogColorGreenField != null) savedFogGreen = fogColorGreenField.getFloat(renderer);
                if (fogColorBlueField != null) savedFogBlue = fogColorBlueField.getFloat(renderer);
            } catch (Throwable ignored) {}
        }

        if (positionField != null) {
            try {
                savedPosition = (net.minecraft.util.math.Vec3d) positionField.get(null);
            } catch (Throwable ignored) {}
        }
        try {
            if (rotationXField != null) savedRotationX = rotationXField.getFloat(null);
            if (rotationXZField != null) savedRotationXZ = rotationXZField.getFloat(null);
            if (rotationZField != null) savedRotationZ = rotationZField.getFloat(null);
            if (rotationYZField != null) savedRotationYZ = rotationYZField.getFloat(null);
            if (rotationXYField != null) savedRotationXY = rotationXYField.getFloat(null);
        } catch (Throwable ignored) {}

        prevFogEnabled = GL11.glIsEnabled(GL11.GL_FOG);
        prevFogMode = GL11.glGetInteger(GL11.GL_FOG_MODE);
        prevFogStart = GL11.glGetFloat(GL11.GL_FOG_START);
        prevFogEnd = GL11.glGetFloat(GL11.GL_FOG_END);
        prevFogDensity = GL11.glGetFloat(GL11.GL_FOG_DENSITY);

        prevFogColor.rewind();
        GL11.glGetFloat(GL11.GL_FOG_COLOR, prevFogColor);

        prevClearColor.rewind();
        GL11.glGetFloat(GL11.GL_COLOR_CLEAR_VALUE, prevClearColor);

        prevViewport.rewind();
        GL11.glGetInteger(GL11.GL_VIEWPORT, prevViewport);
    }

    public void restore(EntityRenderer renderer) {
        Minecraft mc = Minecraft.getMinecraft();

        // 1. Restore EntityRenderer internal fog color fields
        if (renderer != null) {
            try {
                if (fogColorRedField != null) fogColorRedField.setFloat(renderer, savedFogRed);
                if (fogColorGreenField != null) fogColorGreenField.setFloat(renderer, savedFogGreen);
                if (fogColorBlueField != null) fogColorBlueField.setFloat(renderer, savedFogBlue);
            } catch (Throwable ignored) {}

            try {
                if (lightmapUpdateNeededField != null) {
                    lightmapUpdateNeededField.setBoolean(renderer, true);
                }
            } catch (Throwable ignored) {}
        }

        // 2. Restore OpenGL clear color
        prevClearColor.rewind();
        GlStateManager.clearColor(
                prevClearColor.get(0),
                prevClearColor.get(1),
                prevClearColor.get(2),
                prevClearColor.get(3)
        );

        // 3. Restore complete OpenGL fog state (prevent subpass dark fog from leaking into main pass sky)
        prevFogColor.rewind();
        GL11.glFog(GL11.GL_FOG_COLOR, prevFogColor);
        GL11.glFogi(GL11.GL_FOG_MODE, prevFogMode);
        GL11.glFogf(GL11.GL_FOG_START, prevFogStart);
        GL11.glFogf(GL11.GL_FOG_END, prevFogEnd);
        GL11.glFogf(GL11.GL_FOG_DENSITY, prevFogDensity);
        if (prevFogEnabled) {
            GlStateManager.enableFog();
        } else {
            GlStateManager.disableFog();
        }

        // 4. Restore viewport
        prevViewport.rewind();
        int vx = prevViewport.get(0);
        int vy = prevViewport.get(1);
        int vw = prevViewport.get(2);
        int vh = prevViewport.get(3);
        if (vw > 0 && vh > 0) {
            GlStateManager.viewport(vx, vy, vw, vh);
        } else if (mc != null && mc.displayWidth > 0 && mc.displayHeight > 0) {
            GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        }

        // 5. Restore ActiveRenderInfo fields directly (never call updateRenderInfo during subpass cleanup!)
        if (positionField != null) {
            try {
                positionField.set(null, savedPosition != null ? savedPosition : new net.minecraft.util.math.Vec3d(0, 0, 0));
            } catch (Throwable ignored) {}
        }
        try {
            if (rotationXField != null) rotationXField.setFloat(null, savedRotationX);
            if (rotationXZField != null) rotationXZField.setFloat(null, savedRotationXZ);
            if (rotationZField != null) rotationZField.setFloat(null, savedRotationZ);
            if (rotationYZField != null) rotationYZField.setFloat(null, savedRotationYZ);
            if (rotationXYField != null) rotationXYField.setFloat(null, savedRotationXY);
        } catch (Throwable ignored) {}
    }

    public static float getFogColorRed(EntityRenderer renderer) {
        if (renderer != null && fogColorRedField != null) {
            try {
                return fogColorRedField.getFloat(renderer);
            } catch (Throwable ignored) {}
        }
        return 1.0F;
    }

    public static float getFogColorGreen(EntityRenderer renderer) {
        if (renderer != null && fogColorGreenField != null) {
            try {
                return fogColorGreenField.getFloat(renderer);
            } catch (Throwable ignored) {}
        }
        return 1.0F;
    }

    public static float getFogColorBlue(EntityRenderer renderer) {
        if (renderer != null && fogColorBlueField != null) {
            try {
                return fogColorBlueField.getFloat(renderer);
            } catch (Throwable ignored) {}
        }
        return 1.0F;
    }
}
