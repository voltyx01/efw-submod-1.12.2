package com.voltyx.mwccf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWeaponSlotOverlay {

    private static final ResourceLocation ICON_SLOT_0 = new ResourceLocation("mwccf", "textures/gui/0sloticon.png");
    private static final ResourceLocation ICON_SLOT_1 = new ResourceLocation("mwccf", "textures/gui/1sloticon.png");

    @SubscribeEvent
    public void onDrawForeground(GuiContainerEvent.DrawForeground event) {
        GuiContainer gui = event.getGuiContainer();
        if (gui == null || !(gui instanceof GuiInventory)) {
            return;
        }

        if (gui.inventorySlots == null || gui.inventorySlots.inventorySlots == null) {
            return;
        }

        for (Slot slot : gui.inventorySlots.inventorySlots) {
            if (slot != null && slot.inventory instanceof InventoryPlayer) {
                // In InventoryPlayerMixin:
                // slotIndex 0 is for primary weapons (rifles/automatics)
                // slotIndex 1 is for secondary weapons (pistols/handguns)
                if (slot.getSlotIndex() == 0) {
                    if (!slot.getHasStack()) {
                        drawSlotIcon(slot.xPos, slot.yPos, ICON_SLOT_0);
                    }
                } else if (slot.getSlotIndex() == 1) {
                    if (!slot.getHasStack()) {
                        drawSlotIcon(slot.xPos, slot.yPos, ICON_SLOT_1);
                    }
                }
            }
        }
    }

    private void drawSlotIcon(int x, int y, ResourceLocation texture) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(texture);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Scale full 32x32 texture into 16x16 slot area without cropping
        Gui.drawScaledCustomSizeModalRect(x, y, 0.0F, 0.0F, 32, 32, 16, 16, 32.0F, 32.0F);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
