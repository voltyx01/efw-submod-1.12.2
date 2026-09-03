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
            "chiappa", "rhino", "chiappa_rhino", "aps", "makarov", "makarov_pm", "pm",
            "desert_eagle", "deagle", "fiveseven", "five_seven", "m9a1", "m9", "beretta",
            "p226", "mp443", "grach", "vp70", "m17", "sig", "sccy", "sccy_cpx_2", "cpx",
            "hk_p12", "p12", "usp", "usp45", "usp_45", "mas_21", "g2_contender", "contender",
            "m712", "mauser", "m1911", "1911", "colt", "browning", "browning_hi_power", "hi_power",
            "pistol", "revolver", "handgun", "magnum", "walther", "ppk", "p99", "p320", "p250",
            "tt", "tt33", "tokarev", "luger", "p08", "webley", "nagant", "ots", "mp412", "rex",
            "cz75", "cz", "fnx", "fnx45", "p88", "derringer"
    ));

    public static WeaponType getWeaponType(ItemStack stack) {
        if (stack.isEmpty())
            return WeaponType.NONE;

        Item item = stack.getItem();

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
