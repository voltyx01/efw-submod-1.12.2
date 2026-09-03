package efw.core;

import net.minecraft.item.ItemStack;
import java.util.HashMap;
import java.util.Map;

public class WeaponMasterUniqueSettingsCache {

    private static final Map<String, int[]> cache = new HashMap<>();
    private static String lastConfig = null;

    public static int[] get(ItemStack stack, int slot) {
        if (stack == null || stack.isEmpty()) return null;
        if (stack.getItem() == null) return null;
        if (stack.getItem().getRegistryName() == null) return null;

        checkConfigChanged();

        String key = stack.getItem().getRegistryName().getPath() + "_" + slot;
        return cache.get(key); // null если не найдено — тогда оригинальный метод выполнится
    }

    public static void put(int[] result, ItemStack stack, int slot) {
        if (stack == null || stack.isEmpty()) return;
        if (stack.getItem() == null) return;
        if (stack.getItem().getRegistryName() == null) return;

        String key = stack.getItem().getRegistryName().getPath() + "_" + slot;
        cache.putIfAbsent(key, result);
    }

    private static void checkConfigChanged() {
        try {
            String current = com.minecraftserverzone.weaponmaster
                    .setup.events_on_client.ClientOnlyForgeSetup.uniqueItemDisplay;
            if (current != null && !current.equals(lastConfig)) {
                cache.clear();
                lastConfig = current;
            }
        } catch (Exception ignored) {}
    }
}