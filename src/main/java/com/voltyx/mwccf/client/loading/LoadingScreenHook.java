package com.voltyx.mwccf.client.loading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LoadingScreenHook {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiScreenWorking || event.getGui() instanceof GuiDownloadTerrain) {
            if (CustomLoadingScreenRenderer.isRunning()) {
                ItemLoadingScreenRenderer.pickRandom();
                ItemLoadingScreenRenderer.preloadTexture();
            }
        }
    }

    // Перехват грязевого фона Forge
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && !(mc.loadingScreen instanceof CustomLoadingScreenRenderer)) {
            mc.loadingScreen = new CustomLoadingScreenRenderer(mc);
        }
    }

    // Сброс иконки предмета только когда игрок уже реально играет в мире или вышел в главное меню
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc == null) return;
            boolean inGamePlaying = mc.player != null && mc.world != null && mc.player.ticksExisted > 10 && mc.currentScreen == null;
            boolean inMainMenu = mc.currentScreen instanceof net.minecraft.client.gui.GuiMainMenu;
            if ((inGamePlaying || inMainMenu) && ItemLoadingScreenRenderer.hasPicked()) {
                ItemLoadingScreenRenderer.reset();
            }
        }
    }
}