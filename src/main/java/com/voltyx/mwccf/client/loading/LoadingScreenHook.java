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
        // Первый перехват — выбираем предмет и предзагружаем текстуру
        if (event.getGui() instanceof GuiScreenWorking
                && !(event.getGui() instanceof LoadingScreenGui)) {
            if (!CustomLoadingScreenRenderer.isRunning()) return; // При выходе из игры ничего не перехватываем!
            com.voltyx.mwccf.ClientProxyMwccfMod.enableStencilEarly();
            ItemLoadingScreenRenderer.pickRandom();
            ItemLoadingScreenRenderer.preloadTexture();
            event.setGui(new LoadingScreenGui());
            return;
        }

        // Второй перехват — предмет уже выбран, просто показываем экран
        if (event.getGui() instanceof GuiDownloadTerrain
                && !(event.getGui() instanceof LoadingScreenGui)) {
            com.voltyx.mwccf.ClientProxyMwccfMod.enableStencilEarly();
            event.setGui(new LoadingScreenGui());
            return;
        }

        // null = загрузка завершена, сбрасываем для следующего входа
        // УБРАНО: сброс здесь вызывал мигание при переходе между стадиями загрузки
        // ItemLoadingScreenRenderer.reset();
    }

    // Перехват грязевого фона Forge
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        com.voltyx.mwccf.ClientProxyMwccfMod.enableStencilEarly();
        if (!(mc.loadingScreen instanceof CustomLoadingScreenRenderer)) {
            mc.loadingScreen = new CustomLoadingScreenRenderer(mc);
        }
    }

    // Сброс иконки предмета только когда игрок уже в мире и закрыл все меню
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            com.voltyx.mwccf.ClientProxyMwccfMod.enableStencilEarly();
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.world != null && mc.currentScreen == null && ItemLoadingScreenRenderer.hasPicked()) {
                ItemLoadingScreenRenderer.reset();
            }
        }
    }
}