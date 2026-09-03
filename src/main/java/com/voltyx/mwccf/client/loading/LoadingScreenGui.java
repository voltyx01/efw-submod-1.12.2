package com.voltyx.mwccf.client.loading;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

/**
 * Подменяет стандартный GuiScreenWorking.
 * Forge вызывает drawScreen каждый кадр пока идёт загрузка.
 */
public class LoadingScreenGui extends GuiScreen {

    public LoadingScreenGui() {
        ItemLoadingScreenRenderer.pickRandom();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ItemLoadingScreenRenderer.render(this.width, this.height, "", "");
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // Блокируем любой ввод чтобы случайно не закрыть экран
    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        // глушим
    }
}
