package com.voltyx.mwccf.si;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSIAlcohol extends ItemFood {
    private final String tooltipKey;

    public ItemSIAlcohol(String name, float saturation, String tooltipKey) {
        super(2, saturation, false);
        this.tooltipKey = tooltipKey;
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("mwccf." + name);
        this.setAlwaysEdible();
        this.setMaxStackSize(16);
        this.setCreativeTab(CreativeTabs.FOOD);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);

        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            ItemStack bottle = new ItemStack(SIItems.EMPTY_BOTTLE);
            if (!player.inventory.addItemStackToInventory(bottle)) {
                player.dropItem(bottle, false);
            }
        }

        return stack;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        super.onFoodEaten(stack, world, player);
        if (!world.isRemote) {
            String registryName = getRegistryName() != null ? getRegistryName().getPath() : "";
            switch (registryName) {
                case "beer":
                    // Nausea I for 8 seconds (160 ticks, amplifier 0)
                    player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 160, 0, true, true));
                    break;
                case "wine":
                    // Nausea I for 8 seconds, Regeneration I for 30 seconds (600 ticks, amplifier 0)
                    player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 160, 0, true, true));
                    player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600, 0, true, true));
                    break;
                case "whiskey":
                    // Nausea I for 8 seconds, Absorption II for 60 seconds (1200 ticks, amplifier 1)
                    player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 160, 0, true, true));
                    player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 1200, 1, true, true));
                    break;
                case "tequila":
                    // Nausea II for 8 seconds (160 ticks, amplifier 1), Strength I for 30 seconds (600 ticks, amplifier 0)
                    player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 160, 1, true, true));
                    player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 600, 0, true, true));
                    break;
                default:
                    player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 160, 0, true, true));
                    break;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.mwccf.item_effect"));
        if (tooltipKey != null && !tooltipKey.isEmpty()) {
            tooltip.add(" " + TextFormatting.BLUE + I18n.format(tooltipKey));
        }
        String itemName = getRegistryName() == null ? "" : getRegistryName().getPath();
        if ("whiskey".equals(itemName) || "tequila".equals(itemName)) {
            tooltip.add(I18n.format("tooltip.mwccf.fireweapon.alcohol_hint"));
            if (net.minecraft.client.gui.GuiScreen.isShiftKeyDown()) {
                tooltip.add(I18n.format("tooltip.mwccf.fireweapon.alcohol_instructions"));
            }
        }
    }
}
