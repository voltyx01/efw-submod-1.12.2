package com.voltyx.mwccf;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import java.util.HashMap;
import java.util.Map;
import efw.biomeinfo.MwccfConfig;

public class AdvancedHeadshotManager {

    public static class VirtualHead {
        public final double radius;
        public final double yOffset;
        public final double forwardOffset;

        public VirtualHead(double radius, double yOffset, double forwardOffset) {
            this.radius = radius;
            this.yOffset = yOffset;
            this.forwardOffset = forwardOffset;
        }
    }

    // Словарь теперь использует String формата "modid:entity"
    private static final Map<String, VirtualHead> MODDED_HEADS = new HashMap<>();

    /**
     * Этот метод читает массив из конфига и заполняет словарь.
     * Мы будем вызывать его при старте игры и при изменении конфига в меню.
     */
    public static void reloadConfig() {
        MODDED_HEADS.clear();
        for (String line : MwccfConfig.headshots.customMobs) {
            try {
                // Разбиваем строку по запятой
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    double forward = Double.parseDouble(parts[1].trim());
                    double y = Double.parseDouble(parts[2].trim());
                    double radius = Double.parseDouble(parts[3].trim());

                    MODDED_HEADS.put(name, new VirtualHead(radius, y, forward));
                }
            } catch (Exception e) {
                System.err.println("[MWCCF] Ошибка чтения хитбокса в конфиге: " + line);
            }
        }
    }

    public static AxisAlignedBB getHeadBox(EntityLivingBase target) {
        // Получаем красивое имя моба, например "minecraft:zombie"
        ResourceLocation regName = EntityList.getKey(target);
        String entityId = regName != null ? regName.toString() : "";

        VirtualHead vHead = MODDED_HEADS.get(entityId);

        // FALLBACK: Если моба нет в кастомном списке, используем параметры из конфига
        if (vHead == null) {
            float w = target.width;
            float h = target.height;

            double playerW = 0.6D;
            double playerH = 1.8D;
            double scale = ((w / playerW) + (h / playerH)) / 2.0D;
            if (scale < 0.5D)
                scale = 0.5D;

            // Читаем базовый радиус из конфига
            double radius = MwccfConfig.headshots.general.baseRadius * Math.pow(scale, 0.6D);
            if (radius > w * 0.5D)
                radius = w * 0.5D;

            // ГУМАНОИДЫ
            if (h >= w * 1.5F) {
                // Читаем смещение гуманоида из конфига
                double yOffset = target.getEyeHeight() + MwccfConfig.headshots.general.humanoidYOffset;
                vHead = new VirtualHead(radius, yOffset, 0.0D);
            }
            // ЧЕТВЕРОНОГИЕ
            else {
                // Читаем параметры четвероногих из конфига
                double yOffset = (h * MwccfConfig.headshots.general.quadrupedYOffsetMult)
                        + MwccfConfig.headshots.general.quadrupedYOffsetAdd;
                double forwardOffset = (w * MwccfConfig.headshots.general.quadrupedForwardOffsetMult) + (0.1D * scale);
                vHead = new VirtualHead(radius, yOffset, forwardOffset);
            }
        }

        Vec3d look = target.getLook(1.0F);
        double headX = target.posX + (look.x * vHead.forwardOffset);
        double headY = target.posY + vHead.yOffset + (look.y * vHead.forwardOffset);
        double headZ = target.posZ + (look.z * vHead.forwardOffset);

        return new AxisAlignedBB(
                headX - vHead.radius, headY - vHead.radius, headZ - vHead.radius,
                headX + vHead.radius, headY + vHead.radius, headZ + vHead.radius);
    }
}