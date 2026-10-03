package com.voltyx.mwccf.geo;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.BaublesApi;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemBodycam extends Item implements IBauble {

    public ItemBodycam(String name) {
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("mcore." + name);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public static boolean hasCamId(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.hasTagCompound() 
                && stack.getTagCompound().hasKey("cam_id") 
                && !stack.getTagCompound().getString("cam_id").isEmpty();
    }

    public static String getCamId(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return "CAM-0000";
        if (hasCamId(stack)) {
            return stack.getTagCompound().getString("cam_id");
        }
        return "CAM-0000";
    }

    public static String assignUniqueCamId(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return "CAM-0000";
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        int rand = 1000 + new java.util.Random().nextInt(9000);
        String id = "CAM-" + rand;
        tag.setString("cam_id", id);
        return id;
    }

    public static boolean isPowerEnabled(ItemStack stack) {
        if (stack.isEmpty()) return false;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return true;
        return !tag.hasKey("cam_enabled") || tag.getBoolean("cam_enabled");
    }

    public static void setPowerEnabled(ItemStack stack, boolean enabled) {
        if (stack.isEmpty()) return;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setBoolean("cam_enabled", enabled);
    }

    public static boolean togglePower(ItemStack stack) {
        boolean newState = !isPowerEnabled(stack);
        setPowerEnabled(stack, newState);
        return newState;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        if (!worldIn.isRemote && !hasCamId(stack)) {
            assignUniqueCamId(stack);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
            if (!hasCamId(stack)) {
                assignUniqueCamId(stack);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (!world.isRemote && !hasCamId(held)) {
            assignUniqueCamId(held);
        }

        // Sneak + Right Click = Toggle Power
        if (player.isSneaking()) {
            boolean newState = togglePower(held);
            if (world.isRemote) {
                player.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, 0.7F, newState ? 1.4F : 0.8F);
            } else {
                net.minecraft.util.text.ITextComponent statusComp = new net.minecraft.util.text.TextComponentTranslation(
                        newState ? "tooltip.mwccf.bodycam.online_raw" : "tooltip.mwccf.bodycam.offline_raw"
                );
                statusComp.getStyle().setColor(newState ? net.minecraft.util.text.TextFormatting.GREEN : net.minecraft.util.text.TextFormatting.RED);
                player.sendMessage(new net.minecraft.util.text.TextComponentTranslation("message.mwccf.bodycam.status_fmt", getCamId(held), statusComp));
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        // Normal Right Click = Equip into Baubles or Chestplate slot
        if (!world.isRemote && net.minecraftforge.fml.common.Loader.isModLoaded("baubles")) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            if (handler != null) {
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
        if (!world.isRemote) {
            ItemStack chestStack = player.getItemStackFromSlot(net.minecraft.inventory.EntityEquipmentSlot.CHEST);
            if (chestStack.isEmpty()) {
                player.setItemStackToSlot(net.minecraft.inventory.EntityEquipmentSlot.CHEST, held.copy());
                held.shrink(1);
                return new ActionResult<>(EnumActionResult.SUCCESS, held);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, held);
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
        drainBattery(itemstack, player);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemstack) {
        drainBattery(itemstack, player);
    }

    private void drainBattery(ItemStack itemstack, EntityLivingBase player) {
        if (!player.world.isRemote) {
            if (!hasCamId(itemstack)) {
                assignUniqueCamId(itemstack);
            }
            if (!isPowerEnabled(itemstack)) {
                return; // Powered off cameras don't drain battery!
            }
            if (player.ticksExisted % 20 == 0) {
                int remaining = com.voltyx.mwccf.battery.DeviceBatteryHelper.consumeCharge(itemstack, 20);
                if (remaining <= 0) {
                    setPowerEnabled(itemstack, false);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) return true;
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return com.voltyx.mwccf.battery.DeviceBatteryHelper.showDurabilityBar(stack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return com.voltyx.mwccf.battery.DeviceBatteryHelper.getDurabilityForDisplay(stack);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return com.voltyx.mwccf.battery.DeviceBatteryHelper.getRGBDurabilityForDisplay(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        if (hasCamId(stack)) {
            tooltip.add("§7ID: §b" + getCamId(stack));
        } else {
            tooltip.add("§7ID: §8" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.no_id"));
        }

        boolean enabled = isPowerEnabled(stack);
        int percent = com.voltyx.mwccf.battery.DeviceBatteryHelper.getChargePercent(stack);
        ItemStack installed = com.voltyx.mwccf.battery.DeviceBatteryHelper.getInstalledBattery(stack);

        if (enabled) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.status_online"));
        } else {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.status_offline"));
        }

        if (installed.isEmpty() || percent <= 0) {
            tooltip.add("\u00a7c" + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.required"));
        } else {
            String color = percent > 50 ? "\u00a7a" : (percent > 20 ? "\u00a7e" : "\u00a7c");
            tooltip.add(color + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.charge", percent) + " \u00a78(" + installed.getDisplayName() + "\u00a78)");
        }

        tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.toggle_hint"));
        tooltip.add("\u00a77" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.desc"));

        tooltip.add("");
        if (net.minecraft.client.gui.GuiScreen.isShiftKeyDown()) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.manual_title"));
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.manual_step1"));
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.manual_step2"));
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.manual_step3"));
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.manual_step4"));
        } else {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.manual_shift"));
        }
    }

    @net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = "mwccf")
    public static class BodycamEvents {
        @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
        public static void onItemPickup(net.minecraftforge.event.entity.player.EntityItemPickupEvent event) {
            if (event.getEntityPlayer() != null && !event.getEntityPlayer().world.isRemote) {
                ItemStack stack = event.getItem().getItem();
                if (!stack.isEmpty() && stack.getItem() instanceof ItemBodycam) {
                    if (!hasCamId(stack)) {
                        assignUniqueCamId(stack);
                    }
                }
            }
        }
    }
}
