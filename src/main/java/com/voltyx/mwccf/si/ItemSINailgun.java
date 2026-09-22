package com.voltyx.mwccf.si;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemSINailgun extends Item {

    public ItemSINailgun(String name) {
        super();
        this.setMaxDamage(250);
        this.setMaxStackSize(1);
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("mwccf." + name);
        this.setCreativeTab(net.minecraft.creativetab.CreativeTabs.COMBAT);
    }

    private ItemStack findAmmo(EntityPlayer player) {
        if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == SIItems.NAIL) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == SIItems.NAIL) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() == SIItems.NAIL) {
                    return stack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack held = playerIn.getHeldItem(handIn);
        boolean isCreative = playerIn.capabilities.isCreativeMode;
        ItemStack ammo = this.findAmmo(playerIn);

        if (isCreative || !ammo.isEmpty()) {
            if (!worldIn.isRemote) {
                EntityNail nail = new EntityNail(worldIn, playerIn);
                nail.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.5F, 1.0F);
                if (isCreative) {
                    nail.pickupStatus = net.minecraft.entity.projectile.EntityArrow.PickupStatus.CREATIVE_ONLY;
                } else {
                    ammo.shrink(1);
                    held.damageItem(1, playerIn);
                }
                worldIn.spawnEntity(nail);
            }

            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SISounds.NAILGUN_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            playerIn.getCooldownTracker().setCooldown(this, 3);
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        return new ActionResult<>(EnumActionResult.FAIL, held);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == SIItems.STEELLIUM || super.getIsRepairable(toRepair, repair);
    }
}
