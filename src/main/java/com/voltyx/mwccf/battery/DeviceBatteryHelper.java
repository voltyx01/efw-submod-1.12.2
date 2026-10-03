package com.voltyx.mwccf.battery;

import com.voltyx.mwccf.geo.ItemBodycam;
import com.voltyx.mwccf.geo.ItemBracelet;
import com.voltyx.mwccf.geo.ItemHeadlamp;
import com.voltyx.mwccf.geo.ItemPortableMap;
import com.voltyx.mwccf.mcore.MCoreItems;
import com.voltyx.mwccf.walkietalkie.ItemWalkieTalkie;
import com.voltyx.mwccf.armor.SurvivalInstinctArmorHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DeviceBatteryHelper {

    public static final String NBT_INSTALLED = "InstalledBattery";
    public static final String NBT_CHARGE = "battery_charge";
    public static final String NBT_MAX_CHARGE = "battery_max_charge";

    public static boolean isBattery(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (item instanceof ItemBattery) return true;
        if (item == MCoreItems.BATTERY) return true;
        if (item.getRegistryName() != null) {
            String name = item.getRegistryName().toString();
            return name.contains("battery");
        }
        return false;
    }

    public static boolean isRechargeable(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (stack.getItem() instanceof ItemBattery) {
            return ((ItemBattery) stack.getItem()).rechargeable;
        }
        return false;
    }

    public static int getMaxCharge(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 48000;
        if (stack.getItem() instanceof ItemBattery) {
            return ((ItemBattery) stack.getItem()).maxCharge;
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("max_charge")) {
            return stack.getTagCompound().getInteger("max_charge");
        }
        return 48000;
    }

    public static int getCharge(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        if (stack.getItem() instanceof ItemBattery) {
            return ItemBattery.getCharge(stack);
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("charge")) {
            return stack.getTagCompound().getInteger("charge");
        }
        return getMaxCharge(stack);
    }

    public static void setCharge(ItemStack stack, int charge) {
        if (stack == null || stack.isEmpty()) return;
        if (stack.getItem() instanceof ItemBattery) {
            ItemBattery.setCharge(stack, charge);
            return;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        int max = getMaxCharge(stack);
        tag.setInteger("charge", Math.max(0, Math.min(max, charge)));
        tag.setInteger("max_charge", max);
    }

    public static boolean isPoweredDevice(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (item instanceof ItemHeadlamp) return true;
        if (item instanceof ItemBracelet) return true;
        if (item instanceof ItemPortableMap) return true;
        if (item instanceof ItemBodycam) return true;
        if (item instanceof ItemWalkieTalkie) return true;
        if (SurvivalInstinctArmorHandler.isNVGHelmet(item)) return true;
        return false;
    }

    /**
     * Возвращает установленный в устройство элемент питания.
     * Если элемента нет, но в NBT есть legacy battery_charge > 0, создает fallback-батарейку,
     * чтобы игроки не потеряли заряд у старых предметов.
     */
    public static ItemStack getInstalledBattery(ItemStack device) {
        if (device == null || device.isEmpty() || !isPoweredDevice(device)) return ItemStack.EMPTY;
        NBTTagCompound tag = device.getTagCompound();
        if (tag != null) {
            if (tag.hasKey(NBT_INSTALLED, 10)) {
                return new ItemStack(tag.getCompoundTag(NBT_INSTALLED));
            }
            // Fallback для старых предметов
            if (tag.hasKey(NBT_CHARGE)) {
                int oldCharge = tag.getInteger(NBT_CHARGE);
                if (oldCharge > 0) {
                    ItemStack fallback = new ItemStack(MCoreItems.BATTERY);
                    setCharge(fallback, oldCharge);
                    tag.setTag(NBT_INSTALLED, fallback.writeToNBT(new NBTTagCompound()));
                    tag.setInteger(NBT_MAX_CHARGE, 48000);
                    return fallback;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static void setInstalledBattery(ItemStack device, ItemStack battery) {
        if (device == null || device.isEmpty()) return;
        if (battery == null || battery.isEmpty()) {
            removeInstalledBattery(device);
            return;
        }

        NBTTagCompound tag = device.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            device.setTagCompound(tag);
        }

        ItemStack single = battery.copy();
        single.setCount(1);
        tag.setTag(NBT_INSTALLED, single.writeToNBT(new NBTTagCompound()));

        int charge = getCharge(single);
        int maxCharge = getMaxCharge(single);
        tag.setInteger(NBT_CHARGE, charge);
        tag.setInteger(NBT_MAX_CHARGE, maxCharge);
    }

    public static ItemStack removeInstalledBattery(ItemStack device) {
        if (device == null || device.isEmpty()) return ItemStack.EMPTY;
        NBTTagCompound tag = device.getTagCompound();
        if (tag == null) return ItemStack.EMPTY;

        ItemStack installed = ItemStack.EMPTY;
        if (tag.hasKey(NBT_INSTALLED, 10)) {
            installed = new ItemStack(tag.getCompoundTag(NBT_INSTALLED));
            tag.removeTag(NBT_INSTALLED);
        } else if (tag.hasKey(NBT_CHARGE) && tag.getInteger(NBT_CHARGE) > 0) {
            installed = new ItemStack(MCoreItems.BATTERY);
            setCharge(installed, tag.getInteger(NBT_CHARGE));
        }

        tag.setInteger(NBT_CHARGE, 0);
        return installed;
    }

    /**
     * Потребляет заряд устройства.
     * Если заряд закончился:
     * - Одноразовая батарейка исчезает из девайса.
     * - Аккумулятор остается внутри с 0% заряда.
     * Возвращает оставшийся заряд (0 если батарейка села или отсутствует).
     */
    public static int consumeCharge(ItemStack device, int amount) {
        if (device == null || device.isEmpty()) return 0;
        NBTTagCompound tag = device.getTagCompound();
        if (tag == null) return 0;

        ItemStack battery = getInstalledBattery(device);
        if (battery.isEmpty()) {
            tag.setInteger(NBT_CHARGE, 0);
            return 0;
        }

        int current = getCharge(battery);
        if (current <= 0) {
            tag.setInteger(NBT_CHARGE, 0);
            return 0;
        }

        int next = Math.max(0, current - amount);
        setCharge(battery, next);

        if (next <= 0) {
            if (!isRechargeable(battery)) {
                // Одноразовая батарейка исчезает по окончанию лайфтайма!
                removeInstalledBattery(device);
                return 0;
            } else {
                setInstalledBattery(device, battery);
                return 0;
            }
        } else {
            setInstalledBattery(device, battery);
            return next;
        }
    }

    public static int getChargePercent(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        if (isBattery(stack)) {
            int max = getMaxCharge(stack);
            if (max <= 0) return 0;
            return (int) Math.round((getCharge(stack) / (double) max) * 100.0);
        }
        if (isPoweredDevice(stack)) {
            ItemStack bat = getInstalledBattery(stack);
            if (bat.isEmpty()) return 0;
            return getChargePercent(bat);
        }
        return 0;
    }

    public static boolean isChargeableItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (isBattery(stack)) {
            return isRechargeable(stack);
        }
        if (isPoweredDevice(stack)) {
            ItemStack bat = getInstalledBattery(stack);
            return !bat.isEmpty() && isRechargeable(bat);
        }
        return false;
    }

    public static boolean canCharge(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (isBattery(stack)) {
            return isRechargeable(stack) && getCharge(stack) < getMaxCharge(stack);
        }
        if (isPoweredDevice(stack)) {
            ItemStack bat = getInstalledBattery(stack);
            return !bat.isEmpty() && isRechargeable(bat) && getCharge(bat) < getMaxCharge(bat);
        }
        return false;
    }

    public static boolean chargeItem(ItemStack stack, int amount) {
        if (!canCharge(stack)) return false;
        if (isBattery(stack)) {
            int cur = getCharge(stack);
            int max = getMaxCharge(stack);
            if (cur < max) {
                setCharge(stack, Math.min(max, cur + amount));
                return true;
            }
            return false;
        }
        if (isPoweredDevice(stack)) {
            ItemStack bat = getInstalledBattery(stack);
            if (!bat.isEmpty() && isRechargeable(bat)) {
                int cur = getCharge(bat);
                int max = getMaxCharge(bat);
                if (cur < max) {
                    setCharge(bat, Math.min(max, cur + amount));
                    setInstalledBattery(stack, bat);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean showDurabilityBar(ItemStack stack) {
        if (!isPoweredDevice(stack)) return false;
        ItemStack bat = getInstalledBattery(stack);
        if (bat.isEmpty()) return false;
        int max = getMaxCharge(bat);
        return getCharge(bat) < max;
    }

    public static double getDurabilityForDisplay(ItemStack stack) {
        ItemStack bat = getInstalledBattery(stack);
        if (bat.isEmpty()) return 1.0D;
        int max = getMaxCharge(bat);
        if (max <= 0) return 1.0D;
        return 1.0D - ((double) getCharge(bat) / (double) max);
    }

    public static int getRGBDurabilityForDisplay(ItemStack stack) {
        int percent = getChargePercent(stack);
        if (percent > 50) return 0x00FF00;
        if (percent > 20) return 0xFFFF00;
        return 0xFF0000;
    }
}
