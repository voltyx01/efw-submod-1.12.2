package com.voltyx.mwccf.speech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class SpeechPanelScreen extends GuiScreen {
    private final long openedAt = System.currentTimeMillis();
    private long closingAt;
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void startClosing() {
        if (closingAt == 0L) {
            closingAt = System.currentTimeMillis();
            SpeechPanelController.blurInput();
        }
    }

    @Override
    public void initGui() {
        org.lwjgl.input.Keyboard.enableRepeatEvents(true);
        SpeechPanelController.focusInput();
    }

    @Override
    public void onGuiClosed() {
        org.lwjgl.input.Keyboard.enableRepeatEvents(false);
        SpeechPanelController.clearInput();
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float progress = Math.min(1.0F, (System.currentTimeMillis() - openedAt) / 180.0F);
        if (closingAt > 0L) {
            progress = 1.0F - Math.min(1.0F, (System.currentTimeMillis() - closingAt) / 160.0F);
        }
        float eased = 1.0F - (1.0F - progress) * (1.0F - progress);
        int slideOffset = -Math.round((width - 8.0F) * (1.0F - eased));
        SpeechPanelUi.drawStandalonePanel(mouseX, mouseY, width, height, slideOffset, eased);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        if (closingAt > 0L && System.currentTimeMillis() - closingAt >= 160L) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (SpeechPanelController.handleStandaloneMouseClick(mouseX, mouseY, mouseButton, width, height)) return;
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        SpeechPanelController.handleStandaloneMouseDrag(mouseY, width, height);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        SpeechPanelController.stopDraggingScrollbar();
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        Minecraft mc = Minecraft.getMinecraft();
        if (wheel != 0 && mc.currentScreen == this) {
            int mouseX = Mouse.getEventX() * width / mc.displayWidth;
            int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
            SpeechPanelController.handleStandaloneMouseWheel(mouseX, mouseY, wheel, width, height);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            startClosing();
            return;
        }
            if (keyCode == org.lwjgl.input.Keyboard.KEY_T && !SpeechPanelController.isInputFocused()) {
            SpeechPanelController.focusInput();
            return;
        }
        if (SpeechPanelController.handleKey(typedChar, keyCode)) return;
        super.keyTyped(typedChar, keyCode);
    }
}
