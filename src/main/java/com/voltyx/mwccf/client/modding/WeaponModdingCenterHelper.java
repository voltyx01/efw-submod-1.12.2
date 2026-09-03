package com.voltyx.mwccf.client.modding;

import com.paneedah.weaponlib.AttachmentCategory;
import com.paneedah.weaponlib.PlayerWeaponInstance;
import efw.animation.WeaponTypeHelper;
import net.minecraft.item.ItemStack;

public class WeaponModdingCenterHelper {

    public static class WeaponBounds {
        public float centerX;
        public float centerY;
        public float scale;
    }

    public enum GunClass {
        PISTOL,
        P90,
        MICRO_SMG,      // MAC10, Uzi, MP7, FMG9, Scorpion, Kedr
        BULLPUP,        // FAMAS, F2000, AUG, Malyuk, Groza
        SMG,            // MP5, Vector, UMP45, MPX, APC9, Thompson
        RIFLE,          // AK, M4, HK416, SCAR, ACR, G36, Shotguns, DMRs like Zbroyar, M110, Mk14
        SNIPER,         // SVD, SV98, L96, M40A6, R700, SSG08, Kar98
        HEAVY_50CAL     // Barrett M82, NTW20, M200, AS50, Minigun, M60, M249
    }

    public static GunClass getGunClass(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return GunClass.RIFLE;

        String name = "";
        if (stack.getItem().getRegistryName() != null) {
            name = stack.getItem().getRegistryName().toString().toLowerCase();
        }
        String transKey = stack.getItem().getTranslationKey() != null ? stack.getItem().getTranslationKey().toLowerCase() : "";
        String combined = name + " " + transKey;

        // 1. P90 (уникальная геометрия и положение точки привязки)
        if (combined.contains("p90")) {
            return GunClass.P90;
        }

        // 2. Extra Heavy / .50 Cal / Heavy Machine Guns / Super-large Snipers
        if (combined.contains("m82") || combined.contains("barrett") || combined.contains("ntw20") ||
            combined.contains("as50") || combined.contains("m200") || combined.contains("dsr1") ||
            combined.contains("m134") || combined.contains("chainsaw") || combined.contains("m202") ||
            combined.contains("m60") || combined.contains("mg42") || combined.contains("mg34") ||
            combined.contains("m249") || combined.contains("dp28") || combined.contains("stoner")) {
            return GunClass.HEAVY_50CAL;
        }

        // 3. Pistols / Revolvers
        if (WeaponTypeHelper.getWeaponType(stack) == WeaponTypeHelper.WeaponType.PISTOL) {
            return GunClass.PISTOL;
        }

        // 4. Micro SMGs / Machine Pistols (очень компактные)
        if (combined.contains("mac10") || combined.contains("uzi") || combined.contains("mp7") ||
            combined.contains("fmg9") || combined.contains("scorpion") || combined.contains("kedr") ||
            combined.contains("aps") || combined.contains("gl06") || combined.contains("m79")) {
            return GunClass.MICRO_SMG;
        }

        // 5. Bullpups (FAMAS, F2000, AUG, Malyuk, Groza, Tavor, L85)
        if (combined.contains("famas") || combined.contains("f2000") || combined.contains("aug") ||
            combined.contains("malyuk") || combined.contains("groza") || combined.contains("tavor") ||
            combined.contains("l85") || combined.contains("m32")) {
            return GunClass.BULLPUP;
        }

        // 6. SMGs (MP5, Vector, UMP45, MPX, APC9, 9A91, Vityaz, Thompson, MP40)
        if (combined.contains("vector") || combined.contains("kriss") || combined.contains("mp5") ||
            combined.contains("ump45") || combined.contains("mpx") || combined.contains("apc9") ||
            combined.contains("9a91") || combined.contains("vityaz") || combined.contains("thompson") ||
            combined.contains("mp40") || combined.contains("pp19") || combined.contains("bizon")) {
            return GunClass.SMG;
        }

        // 7. Long Bolt-Action / Classic Snipers
        if (combined.contains("sv98") || combined.contains("l96") || combined.contains("awp") ||
            combined.contains("m40a6") || combined.contains("remington700") || combined.contains("ssg08") ||
            combined.contains("kar98") || combined.contains("mosin") || combined.contains("svd") ||
            combined.contains("dragunov") || combined.contains("vss")) {
            return GunClass.SNIPER;
        }

        // 8. Default (Assault Rifles, Battle Rifles like Zbroyar/M110/Mk14/FAL/G3, Shotguns, Carbines)
        return GunClass.RIFLE;
    }

    public static float getBaseScale(PlayerWeaponInstance pwi) {
        if (pwi == null || pwi.getItemStack().isEmpty()) return 115.0f;
        GunClass gunClass = getGunClass(pwi.getItemStack());

        switch (gunClass) {
            case PISTOL:
                return 178.0f;     // Пистолеты крупные и четкие
            case P90:
                return 158.0f;     // P90 крупнее
            case MICRO_SMG:
                return 162.0f;     // Микро-ПП (MAC-10, Uzi) крупнее
            case BULLPUP:
                return 142.0f;     // Буллпапы (FAMAS, F2000) крупнее
            case SMG:
                return 148.0f;     // Стандартные ПП (MP5, Vector) крупнее
            case SNIPER:
                return 105.0f;     // Снайперские винтовки
            case HEAVY_50CAL:
                return 100.0f;      // Барретт и тяжелые винтовки (было 62, теперь 100 — хорошо видно и помещается)
            case RIFLE:
            default:
                return 115.0f;     // Калаши, эмки, Zbroyar, M110, SCAR
        }
    }

    /**
     * Вычисляет точное смещение базового центра оружия по X и Y, а также его масштаб.
     * В меню оружие повернуто на +90 вокруг Y (ствол смотрит ВЛЕВО, приклад ВПРАВО).
     */
    public static WeaponBounds computeBounds(PlayerWeaponInstance pwi) {
        WeaponBounds bounds = new WeaponBounds();
        if (pwi == null || pwi.getWeapon() == null) {
            bounds.centerX = 40.0f;
            bounds.centerY = -5.0f;
            bounds.scale = 115.0f;
            return bounds;
        }

        ItemStack stack = pwi.getItemStack();
        GunClass gunClass = getGunClass(stack);
        float scale = getBaseScale(pwi);

        boolean hasSilencer = pwi.getAttachmentItemWithCategory(AttachmentCategory.SILENCER) != null;
        boolean hasStock = pwi.getAttachmentItemWithCategory(AttachmentCategory.STOCK) != null;

        float cx;
        float cy;

        switch (gunClass) {
            case PISTOL:
                cx = 8.0f;
                cy = 0.0f;
                if (hasSilencer) cx += 25.0f;
                break;

            case P90:
                // P90 сильно смещен вправо в оригинальной модели -> сдвигаем влево и немного вниз
                cx = -30.0f;
                cy = 12.0f;
                if (hasSilencer) cx += 24.0f;
                break;

            case MICRO_SMG:
                // MAC10, Uzi, MP7
                cx = -5.0f;
                cy = 8.0f;
                if (hasSilencer) cx += 22.0f;
                if (hasStock)    cx -= 10.0f;
                break;

            case BULLPUP:
                // FAMAS, F2000, AUG
                cx = -15.0f;
                cy = 8.0f;
                if (hasSilencer) cx += 22.0f;
                break;

            case SMG:
                // MP5, Vector, UMP45
                cx = 10.0f;
                cy = 5.0f;
                if (hasSilencer) cx += 22.0f;
                if (hasStock)    cx -= 10.0f;
                break;

            case SNIPER:
                // SVD, L96, SV98
                cx = 38.0f;
                cy = -5.0f;
                if (hasSilencer) cx += 25.0f;
                if (hasStock)    cx -= 10.0f;
                break;

            case HEAVY_50CAL:
                // Barrett M82, NTW-20, Minigun
                cx = 30.0f;
                cy = -5.0f;
                if (hasSilencer) cx += 26.0f;
                if (hasStock)    cx -= 12.0f;
                break;

            case RIFLE:
            default:
                // Калаши, эмки, Zbroyar Z10, M110, SCAR, дробовики
                cx = 40.0f;
                cy = -5.0f;
                if (hasSilencer) cx += 24.0f;
                if (hasStock)    cx -= 12.0f;
                break;
        }

        bounds.centerX = cx;
        bounds.centerY = cy;
        bounds.scale = scale;
        return bounds;
    }

    /**
     * Точное смещение в экранных пикселях при зуме на категорию аттачмента.
     */
    public static float[] getCategoryFocusOffset(PlayerWeaponInstance pwi, AttachmentCategory category) {
        if (pwi == null || category == null) {
            return new float[]{0.0f, 0.0f};
        }

        ItemStack stack = pwi.getItemStack();
        GunClass gunClass = getGunClass(stack);

        float factor = 1.0f;
        switch (gunClass) {
            case PISTOL:
                factor = 0.5f;
                break;
            case P90:
            case MICRO_SMG:
            case BULLPUP:
            case SMG:
                factor = 0.8f;
                break;
            case SNIPER:
            case HEAVY_50CAL:
                factor = 1.25f;
                break;
            case RIFLE:
            default:
                factor = 1.0f;
                break;
        }

        switch (category) {
            case SILENCER:
            case FRONTSIGHT:
                return new float[]{90.0f * factor, 0.0f};   // Ствол / Мушка (слева)

            case GUARD:
            case LASER:
                return new float[]{50.0f * factor, 0.0f};   // Цевьё / ЛЦУ

            case SCOPE:
            case RAILING:
                return new float[]{10.0f * factor, 0.0f};   // Прицел / Планка

            case RECEIVER:
                return new float[]{10.0f * factor, -5.0f};  // Ресивер

            case MAGAZINE:
                return new float[]{20.0f * factor, -10.0f}; // Магазин

            case GRIP:
                return new float[]{35.0f * factor, -8.0f};  // Рукоять

            case BACKGRIP:
                return new float[]{-15.0f * factor, -8.0f}; // Хват

            case STOCK:
                return new float[]{-65.0f * factor, -5.0f}; // Приклад (справа)

            default:
                return new float[]{0.0f, 0.0f};
        }
    }

    /**
     * Вычисляет точное смещение камеры для центрирования конкретной категории / аттачмента.
     * Использует реальную 3D-матрицу позиционирования детали (positioning lambda).
     */
    public static float[] getExactAttachmentOffset(PlayerWeaponInstance pwi, AttachmentCategory category, float guiScaleRatio) {
        if (pwi == null || pwi.getWeapon() == null || category == null) {
            return new float[]{0.0f, 0.0f};
        }

        com.paneedah.weaponlib.ItemAttachment<com.paneedah.weaponlib.Weapon> current = pwi.getAttachmentItemWithCategory(category);
        com.paneedah.weaponlib.CompatibleAttachment<?> targetCompat = null;

        java.util.Collection<com.paneedah.weaponlib.CompatibleAttachment<? extends com.paneedah.weaponlib.AttachmentContainer>> compats =
                pwi.getWeapon().getCompatibleAttachments(category);

        if (compats != null) {
            for (com.paneedah.weaponlib.CompatibleAttachment<?> compat : compats) {
                if (current != null && compat.getAttachment() == current) {
                    targetCompat = compat;
                    break;
                }
            }
            if (targetCompat == null) {
                for (com.paneedah.weaponlib.CompatibleAttachment<?> compat : compats) {
                    targetCompat = compat;
                    if (compat.isDefault()) break;
                }
            }
        }

        if (targetCompat != null && targetCompat.getPositioning() != null) {
            net.minecraft.client.renderer.GlStateManager.pushMatrix();
            net.minecraft.client.renderer.GlStateManager.loadIdentity();
            try {
                Object positioning = targetCompat.getPositioning();
                if (positioning instanceof java.util.function.BiConsumer) {
                    ((java.util.function.BiConsumer) positioning).accept(pwi.getPlayer(), pwi.getItemStack());
                } else if (positioning instanceof java.util.function.Consumer) {
                    com.paneedah.weaponlib.RenderContext<com.paneedah.weaponlib.RenderableState> renderContext =
                            new com.paneedah.weaponlib.RenderContext<>(pwi.getPlayer(), pwi.getItemStack());
                    renderContext.setPlayerItemInstance(pwi);
                    ((java.util.function.Consumer) positioning).accept(renderContext);
                }

                if (targetCompat.getModelPositioning() != null && targetCompat.getAttachment() != null) {
                    java.util.List<?> texturedModels = targetCompat.getAttachment().getTexturedModels();
                    if (texturedModels != null && !texturedModels.isEmpty()) {
                        Object tm = texturedModels.get(0);
                        if (tm instanceof com.paneedah.weaponlib.Tuple) {
                            Object modelObj = ((com.paneedah.weaponlib.Tuple<?, ?>) tm).getU();
                            if (modelObj instanceof net.minecraft.client.model.ModelBase) {
                                ((java.util.function.Consumer) targetCompat.getModelPositioning()).accept((net.minecraft.client.model.ModelBase) modelObj);
                            }
                        }
                    }
                }

                java.nio.FloatBuffer buf = org.lwjgl.BufferUtils.createFloatBuffer(16);
                org.lwjgl.opengl.GL11.glGetFloat(org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX, buf);
                float ty = buf.get(13);
                float tz = buf.get(14);

                // В плоскости GUI:
                // Gun Z (вдоль ствола):
                float camOffsetX = -tz * 20.0f;
                // Gun Y (высота): сбалансированная комфортная высота без задирания рукояти вверх
                float camOffsetY = -ty * 4.0f;

                return new float[]{camOffsetX, camOffsetY};
            } catch (Throwable ignored) {
            } finally {
                net.minecraft.client.renderer.GlStateManager.popMatrix();
            }
        }

        float[] focus = getCategoryFocusOffset(pwi, category);
        return new float[]{focus[0], focus[1]};
    }
}
