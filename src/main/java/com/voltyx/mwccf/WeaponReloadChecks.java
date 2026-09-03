package com.voltyx.mwccf;

import net.minecraft.item.ItemStack;

public final class WeaponReloadChecks {

    private WeaponReloadChecks() {}

    public static boolean isValidInstance(Object instance) {
        return instance != null;
    }

    public static boolean hasTag(ItemStack stack) {
        return stack != null && stack.getTagCompound() != null;
    }
}
