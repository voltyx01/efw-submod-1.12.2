package com.voltyx.mwccf.item;

import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMorphineSyringe extends Item {
    public static final ItemMorphineSyringe INSTANCE = new ItemMorphineSyringe();

    public ItemMorphineSyringe() {
        this.setRegistryName("mwccf", "morphine_syringe");
        this.setTranslationKey("mwccf.morphine_syringe");
        this.setMaxStackSize(3);
        this.setCreativeTab(CreativeTabs.COMBAT);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 20; // 1 second
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer && !(entityLiving instanceof FakePlayer)) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!world.isRemote) {
                if (Loader.isModLoaded("firstaid")) {
                    applyFirstAidMorphine(player);
                }

                ItemStack emptySyringe = new ItemStack(ItemSyringe.INSTANCE);
                if (!player.inventory.addItemStackToInventory(emptySyringe)) {
                    player.dropItem(emptySyringe, false);
                }

                world.playSound(null, player.posX, player.posY, player.posZ,
                        SoundEvent.REGISTRY.getObject(new ResourceLocation("efw:inject")),
                        SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }
        stack.shrink(1);
        return stack;
    }

    public static void applyFirstAidMorphine(EntityPlayer player) {
        try {
            AbstractPlayerDamageModel damageModel = (AbstractPlayerDamageModel) player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
            if (damageModel != null) {
                damageModel.applyMorphine(player);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GREEN + "Hold RMB for 1 second to use");
        tooltip.add(TextFormatting.GRAY + "Suppresses pain and nullifies fracture debuffs.");
    }
}
