package com.voltyx.mwccf.render.doll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Locale;

/**
 * Хранилище настроек куклы Сайи, логика их изменения в игре и дамп.
 */
public class DollSettings {

    // === Позиция (camera space) ===
    public static float posX = 0.0000f;
    public static float posY = 0.2400f;
    public static float posZ = -1.3200f;

    // === Вращение (градусы) ===
    public static float rotX = -8.00f;
    public static float rotY = 180.00f;
    public static float rotZ = 0.00f;

    // === Масштаб (множитель) ===
    public static float scale = 1.0000f;

    // === Скорость анимаций ===
    public static float animSpeed   = 1.000f;  // нормальная скорость (1.0x)
    public static float appearSpeed = 0.1600f; // сбалансированная скорость появления
    public static float hideSpeed   = 0.1450f; // сбалансированная скорость убирания
    public static float slideDist   = 1.7700f; // дистанция смещения вниз при появлении/убирании

    // === Освещение и Физика ===
    public static boolean worldLighting = true;  // Настоящее освещение из мира Minecraft + тени
    public static float   inertia       = 1.00f; // Сила инерции рук и головы при движении/мыши

    // === Бленд и Прозрачность ===
    // 0 = NORMAL (SrcAlpha, OneMinusSrcAlpha с раздельным alpha)
    // 1 = ADDITIVE (SrcAlpha, One)
    // 2 = MULTIPLY (DstColor, Zero)
    // 3 = ALPHA_ONLY (Стандартный blendFunc SrcAlpha, OneMinusSrcAlpha)
    // 4 = OFF (Без бленда)
    public static int blendMode = 0;
    public static float alpha   = 1.00f;

    // === Состояние HUD отладки ===
    public static boolean debugHudEnabled = false;
    public static int selectedIndex = 0;

    public enum Property {
        POS_X("Позиция X", 0.01f, 0.1f, 0.002f),
        POS_Y("Позиция Y", 0.01f, 0.1f, 0.002f),
        POS_Z("Позиция Z", 0.01f, 0.1f, 0.002f),
        ROT_X("Вращение X", 1.0f, 15.0f, 0.2f),
        ROT_Y("Вращение Y", 2.0f, 45.0f, 0.5f),
        ROT_Z("Вращение Z", 1.0f, 15.0f, 0.2f),
        SCALE("Масштаб (Scale)", 0.05f, 0.25f, 0.01f),
        ANIM_SPEED("Скор. анимации (Speed)", 0.05f, 0.25f, 0.01f),
        APPEAR_SPEED("Скор. появления (Appear)", 0.01f, 0.05f, 0.002f),
        HIDE_SPEED("Скор. скрытия (Hide)", 0.01f, 0.05f, 0.002f),
        SLIDE_DIST("Дистанция выезда (Slide)", 0.02f, 0.1f, 0.005f),
        INERTIA("Инерция рук/головы", 0.1f, 0.5f, 0.02f),
        LIGHTING("Освещение мира", 1.0f, 1.0f, 1.0f),
        BLEND_MODE("Режим бленда (Blend)", 1.0f, 1.0f, 1.0f),
        ALPHA("Прозрачность (Alpha)", 0.05f, 0.2f, 0.01f);

        public final String name;
        public final float stepNormal;
        public final float stepFast;
        public final float stepFine;

        Property(String name, float norm, float fast, float fine) {
            this.name = name;
            this.stepNormal = norm;
            this.stepFast = fast;
            this.stepFine = fine;
        }
    }

    public static float getValue(Property prop) {
        switch (prop) {
            case POS_X: return posX;
            case POS_Y: return posY;
            case POS_Z: return posZ;
            case ROT_X: return rotX;
            case ROT_Y: return rotY;
            case ROT_Z: return rotZ;
            case SCALE: return scale;
            case ANIM_SPEED: return animSpeed;
            case APPEAR_SPEED: return appearSpeed;
            case HIDE_SPEED: return hideSpeed;
            case SLIDE_DIST: return slideDist;
            case INERTIA: return inertia;
            case LIGHTING: return worldLighting ? 1f : 0f;
            case BLEND_MODE: return blendMode;
            case ALPHA: return alpha;
            default: return 0f;
        }
    }

    public static void setValue(Property prop, float val) {
        switch (prop) {
            case POS_X: posX = val; break;
            case POS_Y: posY = val; break;
            case POS_Z: posZ = val; break;
            case ROT_X: rotX = val; break;
            case ROT_Y: rotY = val; break;
            case ROT_Z: rotZ = val; break;
            case SCALE: scale = Math.max(0.01f, val); break;
            case ANIM_SPEED: animSpeed = Math.max(0f, val); break;
            case APPEAR_SPEED: appearSpeed = Math.max(0.001f, Math.min(1f, val)); break;
            case HIDE_SPEED: hideSpeed = Math.max(0.001f, Math.min(1f, val)); break;
            case SLIDE_DIST: slideDist = val; break;
            case INERTIA: inertia = Math.max(0f, Math.min(3f, val)); break;
            case LIGHTING: worldLighting = (val > 0.5f); break;
            case BLEND_MODE:
                int m = Math.round(val);
                if (m < 0) m = 4;
                if (m > 4) m = 0;
                blendMode = m;
                break;
            case ALPHA: alpha = Math.max(0f, Math.min(1f, val)); break;
        }
    }

    public static void adjust(Property prop, int direction, boolean fast, boolean fine) {
        float step = fast ? prop.stepFast : (fine ? prop.stepFine : prop.stepNormal);
        if (prop == Property.LIGHTING) {
            worldLighting = !worldLighting;
        } else if (prop == Property.BLEND_MODE) {
            int next = blendMode + direction;
            if (next < 0) next = 4;
            if (next > 4) next = 0;
            blendMode = next;
        } else {
            setValue(prop, getValue(prop) + direction * step);
        }
    }

    public static void reset(Property prop) {
        switch (prop) {
            case POS_X: posX = 0.0000f; break;
            case POS_Y: posY = 0.2400f; break;
            case POS_Z: posZ = -1.3200f; break;
            case ROT_X: rotX = -8.00f; break;
            case ROT_Y: rotY = 180.00f; break;
            case ROT_Z: rotZ = 0.00f; break;
            case SCALE: scale = 1.0000f; break;
            case ANIM_SPEED: animSpeed = 1.000f; break;
            case APPEAR_SPEED: appearSpeed = 0.1600f; break;
            case HIDE_SPEED: hideSpeed = 0.1450f; break;
            case SLIDE_DIST: slideDist = 1.7700f; break;
            case INERTIA: inertia = 1.00f; break;
            case LIGHTING: worldLighting = true; break;
            case BLEND_MODE: blendMode = 0; break;
            case ALPHA: alpha = 1.00f; break;
        }
    }

    public static void resetAll() {
        for (Property p : Property.values()) {
            reset(p);
        }
    }

    public static String getBlendModeName(int mode) {
        switch (mode) {
            case 0: return "NORMAL (Alpha Separate)";
            case 1: return "ADDITIVE (SrcAlpha, One)";
            case 2: return "MULTIPLY (DstColor, Zero)";
            case 3: return "ALPHA_ONLY (Standard Alpha)";
            case 4: return "OFF (Blend disabled)";
            default: return "UNKNOWN";
        }
    }

    public static void applyGLBlend() {
        if (blendMode == 4) {
            GlStateManager.disableBlend();
            return;
        }
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);

        switch (blendMode) {
            case 0: // NORMAL
                GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
                );
                break;
            case 1: // ADDITIVE
                GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE
                );
                break;
            case 2: // MULTIPLY
                GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.DST_COLOR,
                    GlStateManager.DestFactor.ZERO,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
                );
                break;
            case 3: // ALPHA_ONLY
                GlStateManager.blendFunc(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
                );
                break;
        }
    }

    public static String getDumpString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "posX: %.4f\n", posX));
        sb.append(String.format(Locale.US, "posY: %.4f\n", posY));
        sb.append(String.format(Locale.US, "posZ: %.4f\n", posZ));
        sb.append(String.format(Locale.US, "rotX: %.2f\n", rotX));
        sb.append(String.format(Locale.US, "rotY: %.2f\n", rotY));
        sb.append(String.format(Locale.US, "rotZ: %.2f\n", rotZ));
        sb.append(String.format(Locale.US, "scale: %.4f\n", scale));
        sb.append(String.format(Locale.US, "animSpeed: %.3f\n", animSpeed));
        sb.append(String.format(Locale.US, "appearSpeed: %.4f\n", appearSpeed));
        sb.append(String.format(Locale.US, "hideSpeed: %.4f\n", hideSpeed));
        sb.append(String.format(Locale.US, "slideDist: %.4f\n", slideDist));
        sb.append(String.format(Locale.US, "worldLighting: %b\n", worldLighting));
        sb.append(String.format(Locale.US, "inertia: %.2f\n", inertia));
        sb.append(String.format(Locale.US, "blendMode: %d (%s)\n", blendMode, getBlendModeName(blendMode)));
        sb.append(String.format(Locale.US, "alpha: %.2f\n", alpha));
        return sb.toString();
    }

    public static void dumpAndCopy(Minecraft mc) {
        String dump = getDumpString();
        System.out.println("========== [DOLL DEBUG DUMP] ==========\n" + dump + "=======================================");

        try {
            GuiScreen.setClipboardString(dump);
        } catch (Throwable ignored) {}

        if (mc.player != null) {
            mc.player.sendMessage(new TextComponentString(TextFormatting.GOLD + "=== НАСТРОЙКИ КУКЛЫ (СКОПИРОВАНО В БУФЕР ОБМЕНА) ==="));
            String[] lines = dump.split("\n");
            for (String l : lines) {
                mc.player.sendMessage(new TextComponentString(TextFormatting.AQUA + "  " + l));
            }
            mc.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Значения скопированы! Можно сразу вставить (Ctrl+V) в чат."));
        }
    }
}
