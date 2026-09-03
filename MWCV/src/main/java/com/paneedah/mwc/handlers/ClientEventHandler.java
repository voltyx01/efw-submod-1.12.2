package com.paneedah.mwc.handlers;

import com.paneedah.mwc.ClientTickerController;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayDeque;
import java.util.Queue;

import static com.paneedah.mwc.equipment.inventory.EquipmentSlot.EMPTY_BACKPACK_SLOT_TEXTURE;
import static com.paneedah.mwc.equipment.inventory.EquipmentSlot.EMPTY_BELT_SLOT_TEXTURE;
import static com.paneedah.mwc.equipment.inventory.EquipmentSlot.EMPTY_VEST_SLOT_TEXTURE;
import static com.paneedah.mwc.proxies.ClientProxy.MC;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    // TODO: This is a hack, more info on the hack page of the doc, it's named "Who let her cook" - Luna Lage (Desoroxxx) 2024-03-27
    public static final Queue<Item> COOKING_QUEUE = new ArrayDeque<>();

    private static boolean cooked = false;

    @SubscribeEvent
    public static void onWorldLoadEvent(WorldEvent.Load worldLoadEvent) {
        ClientTickerController.start();
    }

    @SubscribeEvent
    public static void onWorldUnloadEvent(WorldEvent.Unload worldUnloadEvent) {
        ClientTickerController.stop();
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre textureStitchEvent) {
        textureStitchEvent.getMap().registerSprite(EMPTY_BACKPACK_SLOT_TEXTURE);
        textureStitchEvent.getMap().registerSprite(EMPTY_BELT_SLOT_TEXTURE);
        textureStitchEvent.getMap().registerSprite(EMPTY_VEST_SLOT_TEXTURE);
    }

    @SubscribeEvent
    public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre renderGameOverlayEvent) {
        if (cooked)
            return;

        if (COOKING_QUEUE.isEmpty())
            cooked = true;

        for (int i = 0; i < 32 && !COOKING_QUEUE.isEmpty(); i++) { // We are limiting to 32 per frame to reduce "Minecraft is not responding"
            final Item item = COOKING_QUEUE.poll();

            if (item != null)
                MC.getRenderItem().renderItem(new ItemStack(item), ItemCameraTransforms.TransformType.GUI);
        }
    }

    @SubscribeEvent
    public static void onMouseInput(net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent.Pre event) {
        if (org.lwjgl.input.Mouse.getEventButton() == 1 && org.lwjgl.input.Mouse.getEventButtonState()) {
            net.minecraft.client.gui.GuiScreen screen = MC.currentScreen;
            if (screen instanceof net.minecraft.client.gui.inventory.GuiContainer) {
                net.minecraft.client.gui.inventory.GuiContainer gui = (net.minecraft.client.gui.inventory.GuiContainer) screen;
                net.minecraft.inventory.Slot slot = gui.getSlotUnderMouse();
                if (slot != null && slot.getHasStack()) {
                    ItemStack slotStack = slot.getStack();
                    ItemStack cursorStack = MC.player.inventory.getItemStack();
                    if (!cursorStack.isEmpty() && slotStack.getItem() instanceof com.paneedah.mwc.items.equipment.ItemAmmoPack && cursorStack.getItem() instanceof com.paneedah.mwc.items.equipment.ItemAmmoPack) {
                        com.paneedah.weaponlib.ItemBullet slotBullet = com.paneedah.mwc.items.equipment.ItemAmmoPack.getBullet(slotStack);
                        com.paneedah.weaponlib.ItemBullet cursorBullet = com.paneedah.mwc.items.equipment.ItemAmmoPack.getBullet(cursorStack);
                        if (slotBullet != null && slotBullet == cursorBullet) {
                            int slotAmmo = com.paneedah.mwc.items.equipment.ItemAmmoPack.getAmmo(slotStack);
                            if (slotAmmo < 50) {
                                com.paneedah.mwc.MWC.CHANNEL.sendToServer(new com.paneedah.mwc.network.messages.AmmoPackCombineMessage(slot.slotNumber));
                                MC.player.playSound(com.paneedah.weaponlib.UniversalSoundLookup.lookupSound("ammobox"), 1.0F, 1.0F);
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
