package com.voltyx.mwccf.geo;

import com.voltyx.mwccf.battery.DeviceBatteryHelper;
import com.voltyx.mwccf.item.ItemMorphineSyringe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class InventoryChargeHandler {

    private static boolean isChargingClick = false;

    @SubscribeEvent
    public static void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (Mouse.getEventButton() == 1) { // Right click
            if (Mouse.getEventButtonState()) { // Right click down
                if (!net.minecraft.client.gui.GuiScreen.isShiftKeyDown()) {
                    return;
                }
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.currentScreen instanceof GuiContainer) {
                    GuiContainer gui = (GuiContainer) mc.currentScreen;
                    Slot slot = gui.getSlotUnderMouse();
                    if (slot != null && slot.getHasStack()) {
                        ItemStack target = slot.getStack();
                        ItemStack held = mc.player.inventory.getItemStack();

                        // 1. Установка / свап батарейки или аккумулятора
                        if (DeviceBatteryHelper.isPoweredDevice(target) && !held.isEmpty() && DeviceBatteryHelper.isBattery(held)) {
                            isChargingClick = true;
                            com.voltyx.mwccf.MwccfMod.PACKET_HANDLER.sendToServer(new com.voltyx.mwccf.network.PacketChargeDevice(slot.slotNumber));

                            ItemStack oldBattery = DeviceBatteryHelper.getInstalledBattery(target);
                            if (held.getCount() == 1) {
                                DeviceBatteryHelper.setInstalledBattery(target, held);
                                mc.player.inventory.setItemStack(oldBattery);
                            } else {
                                ItemStack single = held.splitStack(1);
                                DeviceBatteryHelper.setInstalledBattery(target, single);
                                mc.player.inventory.setItemStack(held);
                                if (!oldBattery.isEmpty()) {
                                    mc.player.inventory.addItemStackToInventory(oldBattery);
                                }
                            }
                            mc.player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.8F, 1.2F);
                            event.setCanceled(true);
                            return;
                        }

                        // 2. Извлечение батарейки / аккумулятора пустым курсором
                        if (DeviceBatteryHelper.isPoweredDevice(target) && held.isEmpty()) {
                            ItemStack oldBattery = DeviceBatteryHelper.getInstalledBattery(target);
                            if (!oldBattery.isEmpty()) {
                                isChargingClick = true;
                                com.voltyx.mwccf.MwccfMod.PACKET_HANDLER.sendToServer(new com.voltyx.mwccf.network.PacketChargeDevice(slot.slotNumber));

                                DeviceBatteryHelper.removeInstalledBattery(target);
                                mc.player.inventory.setItemStack(oldBattery);
                                mc.player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.8F, 0.9F);
                                event.setCanceled(true);
                                return;
                            }
                        }

                        // 3. Заправка морфина в браслет
                        if (!held.isEmpty() && held.getItem() == ItemMorphineSyringe.INSTANCE && target.getItem() instanceof ItemBracelet) {
                            NBTTagCompound tag = target.getTagCompound();
                            int morphineCount = (tag != null && tag.hasKey("morphine_count")) ? tag.getInteger("morphine_count") : 0;
                            if (morphineCount < 6) {
                                isChargingClick = true;
                                com.voltyx.mwccf.MwccfMod.PACKET_HANDLER.sendToServer(new com.voltyx.mwccf.network.PacketChargeDevice(slot.slotNumber));
                                held.shrink(1);
                                mc.player.inventory.setItemStack(held.isEmpty() ? ItemStack.EMPTY : held);
                                if (tag == null) {
                                    tag = new NBTTagCompound();
                                    target.setTagCompound(tag);
                                }
                                tag.setInteger("morphine_count", morphineCount + 1);
                                event.setCanceled(true);
                                return;
                            }
                        }
                    }
                }
            } else { // Right click up (release)
                if (isChargingClick) {
                    isChargingClick = false;
                    event.setCanceled(true); // Cancel release so container doesn't perform slot swap!
                }
            }
        }
    }
}
