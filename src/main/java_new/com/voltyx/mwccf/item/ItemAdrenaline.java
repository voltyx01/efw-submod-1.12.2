package com.voltyx.mwccf.item;

import com.voltyx.mwccf.potion.PotionAdrenalineEffect;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAdrenaline extends Item {
    public static final ItemAdrenaline INSTANCE = new ItemAdrenaline();

    public ItemAdrenaline() {
        this.setRegistryName("mwccf", "adrenaline");
        this.setTranslationKey("mwccf.adrenaline");
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
        if (player.getCooldownTracker().hasCooldown(this)) {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (!(entityLiving instanceof EntityPlayer)) {
            return stack;
        }
        EntityPlayer player = (EntityPlayer) entityLiving;
        if (!world.isRemote) {
            player.addPotionEffect(new PotionEffect(PotionAdrenalineEffect.INSTANCE, 1200, 0, false, false));
            player.getCooldownTracker().setCooldown(this, 1200);

            ItemStack emptySyringe = new ItemStack(ItemSyringe.INSTANCE);
            if (!player.inventory.addItemStackToInventory(emptySyringe)) {
                player.dropItem(emptySyringe, false);
            }

            world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvent.REGISTRY.getObject(new ResourceLocation("efw:inject")),
                    SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
        stack.shrink(1);
        return stack;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GREEN + "Hold RMB for 1 second to use");
        tooltip.add(TextFormatting.GRAY + "Grants immunity to some negative effects and boosts physical stats.");
    }
}
