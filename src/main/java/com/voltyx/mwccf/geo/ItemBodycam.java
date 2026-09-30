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

    public static String getCamId(ItemStack stack) {
        if (stack.isEmpty()) return "CAM-0000";
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        if (!tag.hasKey("cam_id") || tag.getString("cam_id").isEmpty()) {
            int rand = 1000 + (int) (Math.random() * 9000);
            tag.setString("cam_id", "CAM-" + rand);
        }
        return tag.getString("cam_id");
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
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote) {
            getCamId(stack); // Ensure ID is generated
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        getCamId(held);

        // Sneak + Right Click = Toggle Power
        if (player.isSneaking()) {
            boolean newState = togglePower(held);
            if (world.isRemote) {
                player.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, 0.7F, newState ? 1.4F : 0.8F);
            } else {
                String status = newState ? "§aВКЛЮЧЕНА (ONLINE)" : "§cВЫКЛЮЧЕНА (OFFLINE)";
                player.sendMessage(new TextComponentString("§7[§b" + getCamId(held) + "§7] Статус: " + status));
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
        return BaubleType.BODY;
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
            getCamId(itemstack);
            if (!isPowerEnabled(itemstack)) {
                return; // Powered off cameras don't drain battery!
            }
            NBTTagCompound tag = itemstack.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
                itemstack.setTagCompound(tag);
            }
            if (player.ticksExisted % 20 == 0) {
                int charge = tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
                if (charge > 0) {
                    charge = Math.max(0, charge - 20);
                    tag.setInteger("battery_charge", charge);
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
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey("battery_charge") && tag.getInteger("battery_charge") < 48000;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        int charge = tag != null && tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
        return 1.0D - ((double) charge / 48000.0D);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        int charge = tag != null && tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
        float percent = charge / 48000.0f;
        if (percent > 0.5f) return 0x00FF00;
        if (percent > 0.2f) return 0xFFFF00;
        return 0xFF0000;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        String camId = getCamId(stack);
        boolean enabled = isPowerEnabled(stack);
        NBTTagCompound tag = stack.getTagCompound();
        int charge = tag != null && tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
        int percent = (int) ((charge / 48000.0f) * 100);

        tooltip.add("§7ID: §b" + camId);
        if (enabled) {
            tooltip.add("§7Статус: §aВКЛЮЧЕНА (ONLINE)");
        } else {
            tooltip.add("§7Статус: §cВЫКЛЮЧЕНА (OFFLINE)");
        }

        if (charge <= 0) {
            tooltip.add("\u00a7c" + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.required"));
        } else {
            String color = percent > 50 ? "\u00a7a" : (percent > 20 ? "\u00a7e" : "\u00a7c");
            tooltip.add(color + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.charge", percent));
        }

        tooltip.add("§8[Shift+ПКМ / X] Вкл/Выкл питание");
        tooltip.add("\u00a77" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.bodycam.desc"));
    }
}
