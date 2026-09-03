package com.teamderpy.shouldersurfing.util;

import com.paneedah.weaponlib.Weapon;
import com.paneedah.weaponlib.grenade.ItemGrenade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;

public class WeaponHelper {

    public static boolean isPlayerHoldingWeaponOrGrenade(EntityPlayer player) {
        if (player == null) return false;

        ItemStack heldStack = player.getHeldItemMainhand();
        if (heldStack.isEmpty()) return false;

        Object item = heldStack.getItem();

        // 1. Проверка оружия и гранат из MWC (WeaponLib)
        if (item instanceof Weapon || item instanceof ItemGrenade) {
            return true;
        }

        // 2. Проверка ванильных дальнобойных предметов
        if (item instanceof ItemBow ||
                item instanceof ItemSnowball ||
                item instanceof ItemEgg ||
                item instanceof ItemEnderPearl) {
            return true;
        }

        // 3. Проверка метательных зелий (splash potions)
        if (item instanceof ItemPotion) {
            // ItemSplashPotion и ItemLingeringPotion — подклассы ItemPotion
            return (item instanceof net.minecraft.item.ItemSplashPotion)
                    || (item instanceof net.minecraft.item.ItemLingeringPotion);
        }

        // 4. Дополнительная проверка по имени класса (для других модов на метательное)
        String className = item.getClass().getSimpleName().toLowerCase();
        return className.contains("throwable") ||
                className.contains("shootable") ||
                className.contains("ranged");
    }
}