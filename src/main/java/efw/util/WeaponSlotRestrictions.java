package efw.util;

import com.paneedah.weaponlib.Weapon;
import com.paneedah.weaponlib.config.BalancePackManager;
import efw.animation.WeaponTypeHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import vazkii.quark.oddities.item.ItemBackpack;

public final class WeaponSlotRestrictions {

    private WeaponSlotRestrictions() {}

    /**
     * Checks if the given ItemStack is a pistol.
     */
    public static boolean isPistol(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof Weapon)) return false;

        Weapon weapon = (Weapon) stack.getItem();
        try {
            BalancePackManager.GunConfigurationGroup group = weapon.getConfigurationGroup();
            if (group == BalancePackManager.GunConfigurationGroup.HANDGUN ||
                group == BalancePackManager.GunConfigurationGroup.SIDEARM ||
                group == BalancePackManager.GunConfigurationGroup.REVOLVER) {
                return true;
            }
        } catch (Throwable ignored) {}

        return WeaponTypeHelper.getWeaponType(stack) == WeaponTypeHelper.WeaponType.PISTOL;
    }

    /**
     * Checks if the given ItemStack is any MWC firearm.
     */
    public static boolean isMwcGun(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.getItem() instanceof Weapon;
    }

    /**
     * Checks if the given ItemStack is any other MWC firearm (not a pistol).
     */
    public static boolean isOtherMwcGun(ItemStack stack) {
        return isMwcGun(stack) && !isPistol(stack);
    }

    /**
     * Checks if the given ItemStack is a Quark backpack.
     */
    public static boolean isBackpack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        return stack.getItem() instanceof ItemBackpack;
    }

    /**
     * Checks if placing the stack into a slot with the given inventory slotIndex is forbidden.
     * @param stack The item stack to check
     * @param slotIndex The index in InventoryPlayer:
     *                  0-8: hotbar
     *                  9-35: main inventory
     *                  36-39: armor (36: boots, 37: legs, 38: chest, 39: helmet)
     *                  40: offhand
     * @return true if placing stack in slotIndex is forbidden
     */
    public static boolean isForbiddenInSlot(ItemStack stack, int slotIndex) {
        if (stack == null || stack.isEmpty()) return false;

        // 1. Backpack is completely forbidden in chestplate slot (slot index 38)
        if (slotIndex == 38 && isBackpack(stack)) {
            return true;
        }

        // 2. Offhand slot (slot index 40) cannot hold MWC firearms
        if (slotIndex == 40 && isMwcGun(stack)) {
            return true;
        }

        // 3. Hotbar slot 0 (ID 0): Only other MWC firearms (or non-guns) allowed. Pistols forbidden!
        if (slotIndex == 0 && isPistol(stack)) {
            return true;
        }

        // 4. Hotbar slot 1 (ID 1): Only pistols (or non-guns) allowed. Other MWC firearms forbidden!
        if (slotIndex == 1 && isOtherMwcGun(stack)) {
            return true;
        }

        // 5. Hotbar slots 2..8 (ID 2..8): No MWC firearms allowed at all!
        if (slotIndex >= 2 && slotIndex <= 8 && isMwcGun(stack)) {
            return true;
        }

        return false;
    }

    /**
     * Safely relocates an item from an invalid slot to a valid inventory slot, or drops it.
     */
    public static void relocateOrDrop(EntityPlayer player, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;

        InventoryPlayer inv = player.inventory;

        // 1. Backpack: Try to equip in Baubles body slot first
        if (isBackpack(stack)) {
            try {
                if (!com.voltyx.mwccf.backpack.BackpackBaubles.hasNoBaubleBackpack(player)) {
                    // Bauble already equipped, put in main inventory
                } else {
                    ItemStack bauble = com.voltyx.mwccf.backpack.BackpackBaubles.getBaubleBackpack(player);
                    if (bauble.isEmpty()) {
                        // Attempt to insert into bauble slot via BackpackBaubles
                        // If BackpackBaubles can insert it, great; otherwise continue to main inventory
                    }
                }
            } catch (Throwable ignored) {}

            for (int i = 9; i <= 35; i++) {
                if (inv.mainInventory.get(i).isEmpty()) {
                    inv.mainInventory.set(i, stack);
                    return;
                }
            }
            player.dropItem(stack, false);
            return;
        }

        // 2. Pistol: Try hotbar slot 1 first, then main inventory (9..35)
        if (isPistol(stack)) {
            if (inv.mainInventory.get(1).isEmpty()) {
                inv.mainInventory.set(1, stack);
                return;
            }
            for (int i = 9; i <= 35; i++) {
                if (inv.mainInventory.get(i).isEmpty()) {
                    inv.mainInventory.set(i, stack);
                    return;
                }
            }
            player.dropItem(stack, false);
            return;
        }

        // 3. Other MWC Gun: Try hotbar slot 0 first, then main inventory (9..35)
        if (isOtherMwcGun(stack)) {
            if (inv.mainInventory.get(0).isEmpty()) {
                inv.mainInventory.set(0, stack);
                return;
            }
            for (int i = 9; i <= 35; i++) {
                if (inv.mainInventory.get(i).isEmpty()) {
                    inv.mainInventory.set(i, stack);
                    return;
                }
            }
            player.dropItem(stack, false);
            return;
        }

        // 4. Default: Try any main inventory slot, else drop
        if (!inv.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
        }
    }
}
