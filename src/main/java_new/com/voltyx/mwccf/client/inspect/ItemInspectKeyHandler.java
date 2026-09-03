package com.voltyx.mwccf.client.inspect;

import com.voltyx.mwccf.client.jei.MwccfJeiPlugin;
import com.voltyx.mwccf.client.modding.GuiWeaponModding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class ItemInspectKeyHandler {

    @SubscribeEvent
    public static void onKeyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            if (key != Keyboard.KEY_I && key != Keyboard.KEY_X && key != Keyboard.KEY_P)
                return;

            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen == null || mc.currentScreen instanceof GuiItemInspect
                    || mc.currentScreen instanceof GuiWeaponModding) {
                return;
            }

            ItemStack stack = ItemStack.EMPTY;
            int slotId = -1;
            Slot hoveredSlot = null;

            // 1. Check Container Slot Under Mouse
            if (mc.currentScreen instanceof GuiContainer) {
                GuiContainer container = (GuiContainer) mc.currentScreen;
                Slot slot = container.getSlotUnderMouse();
                if (slot != null && slot.getHasStack()) {
                    stack = slot.getStack();
                    slotId = slot.slotNumber;
                    hoveredSlot = slot;
                }
            }

            // 2. Check JEI Ingredient Under Mouse
            if (stack.isEmpty()) {
                stack = MwccfJeiPlugin.getHoveredStack();
            }

            if (stack != null && !stack.isEmpty()) {
                // [P] Toggle laser color
                if (key == Keyboard.KEY_P) {
                    com.paneedah.weaponlib.LaserBeamRenderer lbr = com.voltyx.mwccf.network.PacketToggleLaserColor.Handler
                            .getLaserRenderer(stack);
                    if (lbr != null) {
                        if (slotId >= 0) {
                            com.voltyx.mwccf.MwccfMod.PACKET_HANDLER
                                    .sendToServer(new com.voltyx.mwccf.network.PacketToggleLaserColor(slotId));
                        }
                        com.voltyx.mwccf.network.PacketToggleLaserColor.Handler.toggleColor(stack);
                        event.setCanceled(true);
                        return;
                    }
                }

                // [I] Inspect item
                // Здесь мы возвращаем стандартное открытие GuiItemInspect для ВСЕХ предметов!
                if (key == Keyboard.KEY_I && !ItemInspectConfig.isBlacklisted(stack)) {
                    InspectTransitionHandler.startTransition(stack.copy(), mc.currentScreen);
                    event.setCanceled(true);
                    return;
                }

                // [X] Open Weapon Modding GUI
                if (key == Keyboard.KEY_X && stack.getItem() instanceof com.paneedah.weaponlib.Weapon) {
                    com.paneedah.weaponlib.ClientModContext context = com.paneedah.weaponlib.ClientModContext
                            .getContext();
                    if (context != null) {
                        com.paneedah.weaponlib.PlayerItemInstance<?> instance = context.getPlayerItemInstanceRegistry()
                                .getItemInstance(mc.player, stack);
                        com.paneedah.weaponlib.PlayerWeaponInstance pwi = null;
                        if (instance instanceof com.paneedah.weaponlib.PlayerWeaponInstance) {
                            pwi = (com.paneedah.weaponlib.PlayerWeaponInstance) instance;
                        }
                        if (pwi == null) {
                            pwi = ((com.paneedah.weaponlib.Weapon) stack.getItem()).createItemInstance(mc.player, stack,
                                    -1);
                        }
                        if (pwi != null) {
                            InspectTransitionHandler.startTransitionToScreen(
                                    new GuiWeaponModding(pwi, mc.currentScreen), mc.currentScreen);
                            event.setCanceled(true);
                            return;
                        }
                    }
                }

                // [X] Study a manual
                if (key == Keyboard.KEY_X && stack.getItem() instanceof efw.item.ManualItem
                        && hoveredSlot != null && hoveredSlot.inventory == mc.player.inventory) {
                    efw.item.ManualItem.ManualType type = ((efw.item.ManualItem) stack.getItem()).type;
                    com.voltyx.mwccf.MwccfMod.PACKET_HANDLER
                            .sendToServer(new com.voltyx.mwccf.sins.network.PacketUseManual(type.categoryIndex));
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack != null && !stack.isEmpty()) {
            com.paneedah.weaponlib.LaserBeamRenderer lbr = com.voltyx.mwccf.network.PacketToggleLaserColor.Handler
                    .getLaserRenderer(stack);
            if (lbr != null) {
                boolean isGreenDefault = lbr.isGreenDefault();
                int current = com.paneedah.weaponlib.Tags.hasLaserColor(stack)
                        ? com.paneedah.weaponlib.Tags.getLaserColor(stack)
                        : (isGreenDefault ? 2 : 1);
                if (current == 2) {
                    event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.laser.to_red"));
                } else {
                    event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.laser.to_green"));
                }
            }

            if (stack.getItem() instanceof efw.item.ManualItem) {
                event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.use_manual"));
            }

            // Оставляем тултип Inspect для DporItem
            if (!(stack.getItem() instanceof com.paneedah.weaponlib.Weapon)) {
                if (!ItemInspectConfig.isBlacklisted(stack)) {
                    event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.inspect"));
                }
            }
        }
    }
}