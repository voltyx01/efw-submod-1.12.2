package com.voltyx.mwccf.geo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import com.voltyx.mwccf.mcore.MCoreItems;

@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class InventoryChargeHandler {

    @SubscribeEvent
    public static void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) { // Right click down
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen instanceof GuiContainer) {
                GuiContainer gui = (GuiContainer) mc.currentScreen;
                Slot slot = gui.getSlotUnderMouse();
                if (slot != null && slot.getHasStack()) {
                    ItemStack target = slot.getStack();
                    ItemStack held = mc.player.inventory.getItemStack();
                    
                    if (!held.isEmpty() && held.getItem() == MCoreItems.BATTERY) {
                        if (target.getItem() instanceof ItemHeadlamp || target.getItem() instanceof ItemBracelet || target.getItem() instanceof ItemPortableMap) {
                            net.minecraft.nbt.NBTTagCompound tag = target.getTagCompound();
                            int currentCharge = (tag != null && tag.hasKey("battery_charge")) ? tag.getInteger("battery_charge") : 0;
                            // 50% of 48000 is 24000
                            if (currentCharge <= 24000) {
                                // Send packet to charge the item at the given slot
                                com.voltyx.mwccf.MwccfMod.PACKET_HANDLER.sendToServer(new com.voltyx.mwccf.network.PacketChargeDevice(slot.slotNumber));
                                event.setCanceled(true); // Prevent default action
                            }
                        }
                    } else if (!held.isEmpty() && held.getItem() == com.voltyx.mwccf.item.ItemMorphineSyringe.INSTANCE) {
                        if (target.getItem() instanceof ItemBracelet) {
                            net.minecraft.nbt.NBTTagCompound tag = target.getTagCompound();
                            int morphineCount = (tag != null && tag.hasKey("morphine_count")) ? tag.getInteger("morphine_count") : 0;
                            if (morphineCount < 6) {
                                com.voltyx.mwccf.MwccfMod.PACKET_HANDLER.sendToServer(new com.voltyx.mwccf.network.PacketChargeDevice(slot.slotNumber));
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
