package com.paneedah.mwc.items.equipment;

import com.paneedah.weaponlib.ItemBullet;
import com.paneedah.mwc.MWC;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAmmoPack extends Item {

    public ItemAmmoPack() {
        setRegistryName("ammo_pack");
        setTranslationKey("ammo_pack");
        setCreativeTab(MWC.AMMUNITION_AND_MAGAZINES_TAB);
        setMaxStackSize(1);
    }

    public static ItemBullet getBullet(ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        String bulletName = stack.getTagCompound().getString("Caliber");
        if (bulletName.isEmpty()) return null;
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(bulletName));
        return item instanceof ItemBullet ? (ItemBullet) item : null;
    }

    public static void setBullet(ItemStack stack, ItemBullet bullet) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        if (bullet != null) {
            stack.getTagCompound().setString("Caliber", bullet.getRegistryName().toString());
        } else {
            stack.getTagCompound().removeTag("Caliber");
        }
    }

    public static int getAmmo(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("Ammo")) {
            // Random ammo between 6 and 27, skewed towards lower values
            int minAmmo = 6;
            int maxAmmo = 27;
            int range = maxAmmo - minAmmo;
            // Math.pow(..., 2) skews towards 0
            int randomAmmo = minAmmo + (int)(Math.pow(Math.random(), 2.0) * (range + 1));
            setAmmo(stack, randomAmmo);
            return randomAmmo;
        }
        return stack.getTagCompound().getInteger("Ammo");
    }

    public static void setAmmo(ItemStack stack, int ammo) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger("Ammo", Math.max(0, Math.min(ammo, 50)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        ItemBullet bullet = getBullet(stack);
        int ammo = getAmmo(stack);
        
        if (bullet != null) {
            tooltip.add(TextFormatting.GOLD + "Caliber: " + TextFormatting.WHITE + bullet.getItemStackDisplayName(new ItemStack(bullet)));
        } else {
            tooltip.add(TextFormatting.GOLD + "Caliber: " + TextFormatting.GRAY + "Unknown");
        }
        
        tooltip.add(TextFormatting.RED + "Ammo: " + TextFormatting.GRAY + ammo + "/50");
    }

    @Override
    public void getSubItems(net.minecraft.creativetab.CreativeTabs tab, net.minecraft.util.NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
                if (item instanceof ItemBullet) {
                    ItemStack stack = new ItemStack(this);
                    setBullet(stack, (ItemBullet) item);
                    setAmmo(stack, 50);
                    items.add(stack);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static int getColorForBullet(ItemBullet bullet) {
        if (bullet == null) return 0xFFFFFF;
        
        String name = bullet.getRegistryName().getPath().toLowerCase();
        
        // Pistols & SMG (Blue/Light Blue)
        if (name.contains("9x19mm") || name.contains("9x18mm") || name.contains("45acp") || 
            name.contains("380acp") || name.contains("10mm") || name.contains("763x25") || 
            name.contains("765x21") || name.contains("57x28mm") || name.contains("46x30mm")) {
            return 0x3498DB; // Blue
        }
        
        // ARs (Green/Olive)
        if (name.contains("556x45") || name.contains("545x39") || name.contains("762x39") || 
            name.contains("792x33") || name.contains("762x35") || name.contains("473x33mm")) {
            return 0x27AE60; // Green
        }
        
        // Rifle/MG (Red/Brown)
        if (name.contains("762x51") || name.contains("762x54") || name.contains("308") || 
            name.contains("30_06") || name.contains("792x57") || name.contains("8x58") || 
            name.contains("65") || name.contains("6.5")) {
            return 0xC0392B; // Red
        }
        
        // Heavy/Sniper (Black/Dark Grey)
        if (name.contains("50bmg") || name.contains("20x82mm") || name.contains("408ct")) {
            return 0x34495E; // Dark Grey
        }
        
        // Shotguns (Orange)
        if (name.contains("12gauge") || name.contains("4g")) {
            return 0xE67E22; // Orange
        }
        
        // Magnums/DMRs (Purple/Burgundy)
        if (name.contains("357") || name.contains("44") || name.contains("500") || 
            name.contains("50ae") || name.contains("50beowulf") || name.contains("45_70") || 
            name.contains("44_40") || name.contains("9x39mm")) {
            return 0x8E44AD; // Purple
        }
        
        return 0xFFFFFF; // Default White
    }
}
