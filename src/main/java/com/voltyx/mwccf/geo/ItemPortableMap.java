package com.voltyx.mwccf.geo;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemPortableMap extends Item implements IBauble {

    public ItemPortableMap(String name) {
        this.setRegistryName(name);
        this.setTranslationKey("mcore." + name);
        this.setMaxStackSize(1);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.TRINKET;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (!player.world.isRemote) {
            NBTTagCompound tag = itemstack.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
                itemstack.setTagCompound(tag);
            }
            int charge = tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
            if (charge > 0) {
                charge--;
                tag.setInteger("battery_charge", charge);
            }
        }
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World worldIn, java.util.List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        NBTTagCompound tag = stack.getTagCompound();
        int charge = tag != null && tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
        int percent = (int) ((charge / 48000.0f) * 100);

        if (charge <= 0) {
            tooltip.add("\u00a7c" + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.required"));
        } else {
            String color = percent > 50 ? "\u00a7a" : (percent > 20 ? "\u00a7e" : "\u00a7c");
            tooltip.add(color + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.charge", percent));
        }

        tooltip.add("\u00a77" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.portable_map.desc"));
    }
}
