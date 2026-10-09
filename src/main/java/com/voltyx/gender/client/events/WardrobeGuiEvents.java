package com.voltyx.gender.client.event;

import com.voltyx.gender.gui.button.GuiWardrobeButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WardrobeGuiEvents {

    @SubscribeEvent
    public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiInventory || isBaublesExpandedGui(event.getGui())) {
            GuiContainer gui = (GuiContainer) event.getGui();
            event.getButtonList().add(new GuiWardrobeButton(56, gui, 27, 9, 10, 10));
        }
    }

    private static boolean isBaublesExpandedGui(Object gui) {
        if (gui == null) return false;
        Class<?> clazz = gui.getClass();
        while (clazz != null && clazz != Object.class) {
            if ("baubles.client.gui.GuiPlayerExpanded".equals(clazz.getName())) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    @SubscribeEvent
    public void guiButtonClick(GuiScreenEvent.ActionPerformedEvent.Post event) {
        // кнопка обрабатывает клик сама через mousePressed
    }
}