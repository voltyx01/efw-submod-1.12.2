package com.voltyx.mwccf.geo;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.BaublesApi;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumActionResult;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemHeadlamp extends Item implements IBauble {

    public ItemHeadlamp(String name) {
        this.setRegistryName(name);
        this.setTranslationKey("mcore." + name);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            com.voltyx.mwccf.geo.HeadlampNetwork.sendTogglePacket();
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.HEAD;
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
        NBTTagCompound tag = itemstack.getTagCompound();
        if (tag != null && tag.getBoolean("active")) {
            if (!player.world.isRemote) { // Only decrease on server
                int charge = tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
                if (charge > 0) {
                    charge--;
                    tag.setInteger("battery_charge", charge);
                    if (charge <= 0) {
                        tag.setBoolean("active", false); // turn off
                    }
                } else {
                    tag.setBoolean("active", false);
                }
            }
        }
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World worldIn, java.util.List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        NBTTagCompound tag = stack.getTagCompound();
        boolean active = tag != null && tag.getBoolean("active");
        int charge = tag != null && tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
        int percent = (int) ((charge / 48000.0f) * 100);
        
        tooltip.add("\u00a77State: " + (active ? "\u00a7aON" : "\u00a7cOFF"));
        
        if (charge <= 0) {
            tooltip.add("\u00a7c" + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.required"));
        } else {
            String color = percent > 50 ? "\u00a7a" : (percent > 20 ? "\u00a7e" : "\u00a7c");
            tooltip.add(color + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.charge", percent));
        }
        
        String keyName = HeadlampKeyHandler.HEADLAMP_TOGGLE_KEY.getDisplayName();
        if (HeadlampKeyHandler.HEADLAMP_TOGGLE_KEY.getKeyCode() == 0) {
            keyName = net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.none");
        }
        tooltip.add("\u00a77Toggle: \u00a7e" + keyName);
    }
}
