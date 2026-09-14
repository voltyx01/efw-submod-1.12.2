package com.voltyx.mwccf.antenna.client;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiAntenna extends GuiScreen {

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        Mouse.setGrabbed(true);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        Mouse.setGrabbed(false);
        if (AntennaCameraController.isActive()) {
            AntennaCameraController.close();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            AntennaCameraController.close();
            return;
        }
        AntennaSession.getInstance().handleKeyTyped(typedChar, keyCode,
                AntennaCameraController.getCurrentAntenna(), AntennaCameraController.getAntennaPos());
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        // Button 1 is right mouse button (ПКМ)
        if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
            AntennaCameraController.close();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!Mouse.isGrabbed()) {
            Mouse.setGrabbed(true);
        }
        if (this.fontRenderer != null) {
            String hint = "[ESC / ПКМ] Отойти от сейфа";
            int w = this.fontRenderer.getStringWidth(hint);
            int x = (this.width - w) / 2;
            int y = this.height - 25;
            drawRect(x - 8, y - 4, x + w + 8, y + this.fontRenderer.FONT_HEIGHT + 4, 0xCC071520);
            this.fontRenderer.drawStringWithShadow(hint, x, y, 0xFF44AAFF);
        }
    }
}
