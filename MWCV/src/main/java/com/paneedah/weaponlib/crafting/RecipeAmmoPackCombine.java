package com.paneedah.weaponlib.crafting;

import com.paneedah.mwc.items.equipment.ItemAmmoPack;
import com.paneedah.weaponlib.ItemBullet;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeAmmoPackCombine extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        int count = 0;
        int totalAmmo = 0;
        ItemBullet bulletType = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (!(stack.getItem() instanceof ItemAmmoPack)) {
                    return false; // Found non-ammo pack
                }
                
                ItemBullet currentBullet = ItemAmmoPack.getBullet(stack);
                if (currentBullet == null) {
                    return false; // Invalid ammo pack
                }
                
                if (bulletType == null) {
                    bulletType = currentBullet;
                } else if (bulletType != currentBullet) {
                    return false; // Different caliber
                }
                
                count++;
                totalAmmo += ItemAmmoPack.getAmmo(stack);
            }
        }

        // Need at least 2 packs of the same caliber to combine
        if (count < 2) {
            return false;
        }

        // Must not exceed max capacity
        if (totalAmmo > 50) {
            return false;
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int totalAmmo = 0;
        ItemBullet bulletType = null;
        ItemAmmoPack ammoPackItem = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemAmmoPack) {
                if (ammoPackItem == null) {
                    ammoPackItem = (ItemAmmoPack) stack.getItem();
                }
                if (bulletType == null) {
                    bulletType = ItemAmmoPack.getBullet(stack);
                }
                totalAmmo += ItemAmmoPack.getAmmo(stack);
            }
        }

        if (ammoPackItem != null && bulletType != null) {
            ItemStack result = new ItemStack(ammoPackItem);
            ItemAmmoPack.setBullet(result, bulletType);
            ItemAmmoPack.setAmmo(result, totalAmmo);
            return result;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY; // Dynamic output
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return net.minecraftforge.common.ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
