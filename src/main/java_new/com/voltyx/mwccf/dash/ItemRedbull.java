package com.voltyx.mwccf.dash;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemRedbull extends ItemFood {
    public static final ItemRedbull INSTANCE = new ItemRedbull();

    private ItemRedbull() {
        super(0, 0f, false);
        setTranslationKey("redbull");
        setRegistryName("mwccf", "redbull");
        setAlwaysEdible();
        setCreativeTab(CreativeTabs.FOOD);
        setMaxStackSize(2);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        super.onFoodEaten(stack, world, player);
        if (!world.isRemote) {
            player.addPotionEffect(new PotionEffect(PotionEnergyBoost.INSTANCE, 1200, 0, false, false));
        }
    }
}
