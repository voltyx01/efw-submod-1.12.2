package com.voltyx.mwccf.battery;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Базовый класс батареек и Li-ion аккумуляторов.
 * - Одноразовая батарейка (battery): емкость 48 000 (1x), исчезает при полном разряде.
 * - Зеленый Li-ion (lion_battery_1): емкость 120 000 (2.5x), перезаряжаемый в генераторе.
 * - Коричневый Li-ion (lion_battery_2): емкость 250 000 (~5.2x), перезаряжаемый в генераторе.
 * - Розовый Li-ion (lion_battery_3): емкость 500 000 (>10x), самый живучий, перезаряжаемый.
 */
public class ItemBattery extends Item {

    public final int maxCharge;
    public final boolean rechargeable;
    public final int tier; // 0 = standard, 1 = green, 2 = brown, 3 = pink

    public ItemBattery(String name, int maxCharge, boolean rechargeable, int tier) {
        this.setRegistryName("mwccf", name);
        if ("battery".equals(name)) {
            this.setTranslationKey("mcore." + name);
        } else {
            this.setTranslationKey("mwccf." + name);
        }
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.setMaxStackSize(rechargeable ? 1 : 16);
        this.maxCharge = maxCharge;
        this.rechargeable = rechargeable;
        this.tier = tier;
    }

    public static int getCharge(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        if (stack.getItem() instanceof ItemBattery) {
            ItemBattery ib = (ItemBattery) stack.getItem();
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && tag.hasKey("charge")) {
                return Math.max(0, Math.min(ib.maxCharge, tag.getInteger("charge")));
            }
            return ib.maxCharge; // По умолчанию 100% при получении предмета
        }
        return 0;
    }

    public static void setCharge(ItemStack stack, int charge) {
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof ItemBattery)) return;
        ItemBattery ib = (ItemBattery) stack.getItem();
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setInteger("charge", Math.max(0, Math.min(ib.maxCharge, charge)));
        tag.setInteger("max_charge", ib.maxCharge);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        if (this.rechargeable) return 1;
        // Одноразовые батарейки стакаются до 16, только если они полностью заряжены (100%)
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("charge")) {
            int charge = stack.getTagCompound().getInteger("charge");
            if (charge < this.maxCharge) return 1;
        }
        return 16;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        int charge = getCharge(stack);
        return charge < this.maxCharge;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int charge = getCharge(stack);
        if (this.maxCharge <= 0) return 0.0;
        return 1.0 - ((double) charge / (double) this.maxCharge);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        int charge = getCharge(stack);
        float pct = this.maxCharge > 0 ? (float) charge / (float) this.maxCharge : 0f;
        if (pct > 0.5f) return 0x2ECC71; // green
        if (pct > 0.2f) return 0xF1C40F; // yellow
        return 0xE74C3C; // red
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int charge = getCharge(stack);
        int percent = this.maxCharge > 0 ? (int) Math.round((charge / (double) this.maxCharge) * 100.0) : 0;
        String color = percent > 50 ? "§a" : (percent > 20 ? "§e" : "§c");
        
        String chargeStr = String.format("%,d", charge).replace(',', ' ');
        String maxStr = String.format("%,d", this.maxCharge).replace(',', ' ');
        tooltip.add("§7" + I18n.format("tooltip.mwccf.battery.capacity") + ": §f" + chargeStr + " §7/ §f" + maxStr + " §8(" + color + percent + "%§8)");

        if (this.rechargeable) {
            tooltip.add(I18n.format("tooltip.mwccf.battery.rechargeable"));
        } else {
            tooltip.add(I18n.format("tooltip.mwccf.battery.disposable"));
        }
        tooltip.add(I18n.format("tooltip.mwccf.battery.swap_hint"));
    }
}
