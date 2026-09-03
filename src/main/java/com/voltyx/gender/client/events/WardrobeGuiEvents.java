package com.voltyx.gender.client.event;

import com.voltyx.gender.gui.button.GuiWardrobeButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WardrobeGuiEvents {

    @SubscribeEvent
    public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiInventory) {
            GuiContainer gui = (GuiContainer) event.getGui();
            event.getButtonList().add(new GuiWardrobeButton(56, gui, 27, 9, 10, 10));
        }
    }

    @SubscribeEvent
    public void guiButtonClick(GuiScreenEvent.ActionPerformedEvent.Post event) {
        // кнопка обрабатывает клик сама через mousePressed
    }
}