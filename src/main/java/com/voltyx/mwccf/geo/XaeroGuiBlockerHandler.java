package com.voltyx.mwccf.geo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class XaeroGuiBlockerHandler {

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        GuiScreen gui = event.getGui();
        if (gui != null && gui.getClass().getName().startsWith("xaero.")) {
            if (!MapDeviceState.hasActiveMap()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc != null && mc.currentScreen != null) {
                if (mc.currentScreen.getClass().getName().startsWith("xaero.")) {
                    if (!MapDeviceState.hasActiveMap()) {
                        mc.displayGuiScreen(null);
                    }
                }
            }
        }
    }
}
