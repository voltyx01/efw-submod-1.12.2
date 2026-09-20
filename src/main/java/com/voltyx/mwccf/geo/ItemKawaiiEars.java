package com.voltyx.mwccf.geo;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.BaublesApi;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemKawaiiEars extends Item implements IBauble {

    public ItemKawaiiEars(String name) {
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("mcore." + name);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (!world.isRemote && net.minecraftforge.fml.common.Loader.isModLoaded("baubles")) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            if (handler != null) {
                // Find HEAD slot (usually slot 4 in Baubles 1.12.2)
                for (int i = 0; i < handler.getSlots(); i++) {
                    if (handler.isItemValidForSlot(i, held, player)) {
                        ItemStack inSlot = handler.getStackInSlot(i);
                        if (inSlot.isEmpty()) {
                            handler.setStackInSlot(i, held.copy());
                            held.shrink(1);
                            return new ActionResult<>(EnumActionResult.SUCCESS, held);
                        }
                    }
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, held);
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.kawaii_ears.desc"));
    }
}
