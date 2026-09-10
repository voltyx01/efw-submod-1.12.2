package com.voltyx.mwccf.terminal.client;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiTerminal extends GuiScreen {

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
        if (TerminalCameraController.isActive()) {
            TerminalCameraController.close();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            TerminalCameraController.close();
            return;
        }
        TerminalSession.getInstance().handleKeyTyped(typedChar, keyCode);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        // Button 1 is right mouse button (ПКМ)
        if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
            TerminalCameraController.close();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Transparent overlay: world and CRT monitor remain visible
        if (!Mouse.isGrabbed()) {
            Mouse.setGrabbed(true);
        }
        TerminalCameraController.renderTerminalPrompt();
    }
}
