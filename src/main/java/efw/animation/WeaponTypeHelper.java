package efw.animation;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WeaponTypeHelper {

    public enum WeaponType {
        PISTOL,
        RIFLE,
        NONE
    }

    // Здесь мы перечисляем те слова, которые встречаются в registry name или unlocalized name пистолетов.
    private static final Set<String> PISTOL_KEYWORDS = new HashSet<>(Arrays.asList(
            "glock", "glock_18c", "glock_19", "glock_21", "glock_22",
            "python", "taurus", "raging_hunter", "taurus_raging_hunter", "sw_500", "sw_500_magnum", "500_magnum",
            "chiappa", "rhino", "chiappa_rhino", "aps", "makarov", "makarov_pm",
            "desert_eagle", "deagle", "fiveseven", "five_seven", "m9a1", "m9", "beretta",
            "p226", "mp443", "grach", "vp70", "m17", "sccy", "sccy_cpx_2",
            "hk_p12", "usp", "usp45", "usp_45", "mas_21", "g2_contender", "contender",
            "m712", "m1911", "1911", "browning_hi_power", "hi_power",
            "pistol", "revolver", "handgun", "magnum", "walther", "ppk", "p99", "p320", "p250",
            "tt33", "tokarev", "luger", "p08", "webley", "nagant", "mp412",
            "cz75", "cz_75", "fnx", "fnx45", "p88", "derringer"
    ));

    public static WeaponType getWeaponType(ItemStack stack) {
        if (stack.isEmpty())
            return WeaponType.NONE;

        Item item = stack.getItem();

        // 1. Direct check using MWC GunConfigurationGroup if available
        if (item instanceof com.paneedah.weaponlib.Weapon) {
            com.paneedah.weaponlib.Weapon weapon = (com.paneedah.weaponlib.Weapon) item;
            try {
                com.paneedah.weaponlib.config.BalancePackManager.GunConfigurationGroup group = weapon.getConfigurationGroup();
                if (group == com.paneedah.weaponlib.config.BalancePackManager.GunConfigurationGroup.HANDGUN ||
                    group == com.paneedah.weaponlib.config.BalancePackManager.GunConfigurationGroup.SIDEARM ||
                    group == com.paneedah.weaponlib.config.BalancePackManager.GunConfigurationGroup.REVOLVER) {
                    return WeaponType.PISTOL;
                } else if (group != null && group != com.paneedah.weaponlib.config.BalancePackManager.GunConfigurationGroup.NONE) {
                    return WeaponType.RIFLE;
                }
            } catch (Throwable ignored) {}
        }

        // Проверяем, из мода ли предмет (Modern Warfare Cubed)
        boolean isMWCWeapon = false;
        try {
            Class<?> clazz = item.getClass();
            while (clazz != null) {
                String className = clazz.getName();
                if (className.equals("com.paneedah.weaponlib.Weapon") || 
                    className.equals("com.vicmignogna.weaponlib.Weapon") ||
                    className.endsWith(".Weapon")) {
                    isMWCWeapon = true;
                    break;
                }
                clazz = clazz.getSuperclass();
            }
        } catch (Throwable t) {
            // Fallback
        }

        ResourceLocation registryName = item.getRegistryName();
        if (registryName == null)
            return WeaponType.NONE;

        String path = registryName.getPath().toLowerCase();
        String translationKey = item.getTranslationKey().toLowerCase();

        if (isMWCWeapon) {
            // Если в имени или ключе локализации есть ключевые слова пистолета
            for (String keyword : PISTOL_KEYWORDS) {
                if (path.contains(keyword) || translationKey.contains(keyword)) {
                    return WeaponType.PISTOL;
                }
            }
            // Иначе это "rifle" (винтовки, автоматы, дробовики и т.д.)
            return WeaponType.RIFLE;
        }

        return WeaponType.NONE;
    }
}
