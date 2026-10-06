package com.voltyx.mwccf.geo;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiSlider;
import java.io.IOException;

public class BraceletSettingsGui extends GuiScreen {

    private GuiSlider sliderInspect;
    private GuiSlider sliderBackground;
    private GuiSlider sliderMWC;
    private GuiSlider sliderBodyHeartbeat;
    private GuiSlider sliderColorR;
    private GuiSlider sliderColorG;
    private GuiSlider sliderColorB;

    @Override
    public void initGui() {
        super.initGui();
        int cx = this.width / 2;
        int cy = this.height / 2;

        this.sliderInspect = new GuiSlider(0, cx - 205, cy - 50, 200, 20, "Inspect Volume: ", "%", 0.0, 100.0, BraceletSettings.inspectVolume * 100.0, false, true);
        this.sliderBackground = new GuiSlider(1, cx - 205, cy - 25, 200, 20, "Background Volume: ", "%", 0.0, 100.0, BraceletSettings.backgroundVolume * 100.0, false, true);
        this.sliderMWC = new GuiSlider(2, cx - 205, cy, 200, 20, "Weapon Volume: ", "%", 0.0, 100.0, BraceletSettings.mwcWeaponVolume * 100.0, false, true);
        this.sliderBodyHeartbeat = new GuiSlider(7, cx - 205, cy + 25, 200, 20, "Heartbeat Sound: ", "%", 0.0, 100.0, BraceletSettings.bodyHeartbeatVolume * 100.0, false, true);

        this.sliderColorR = new GuiSlider(3, cx + 5, cy - 50, 200, 20, "Red: ", "", 0.0, 255.0, BraceletSettings.displayColorR, false, true);
        this.sliderColorG = new GuiSlider(4, cx + 5, cy - 25, 200, 20, "Green: ", "", 0.0, 255.0, BraceletSettings.displayColorG, false, true);
        this.sliderColorB = new GuiSlider(5, cx + 5, cy, 200, 20, "Blue: ", "", 0.0, 255.0, BraceletSettings.displayColorB, false, true);

        this.buttonList.add(this.sliderInspect);
        this.buttonList.add(this.sliderBackground);
        this.buttonList.add(this.sliderMWC);
        this.buttonList.add(this.sliderBodyHeartbeat);
        this.buttonList.add(this.sliderColorR);
        this.buttonList.add(this.sliderColorG);
        this.buttonList.add(this.sliderColorB);
        
        this.buttonList.add(new GuiButton(6, cx - 100, cy + 65, 200, 20, "Close"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw black background
        drawRect(0, 0, this.width, this.height, 0xFF000000);
        
        // Draw title
        this.drawCenteredString(this.fontRenderer, "Bracelet Settings", this.width / 2, this.height / 2 - 80, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 6) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    public void onGuiClosed() {
        BraceletSettings.inspectVolume = (float) (this.sliderInspect.getValueInt() / 100.0);
        BraceletSettings.backgroundVolume = (float) (this.sliderBackground.getValueInt() / 100.0);
        BraceletSettings.mwcWeaponVolume = (float) (this.sliderMWC.getValueInt() / 100.0);
        BraceletSettings.bodyHeartbeatVolume = (float) (this.sliderBodyHeartbeat.getValueInt() / 100.0);
        BraceletSettings.displayColorR = this.sliderColorR.getValueInt();
        BraceletSettings.displayColorG = this.sliderColorG.getValueInt();
        BraceletSettings.displayColorB = this.sliderColorB.getValueInt();
        BraceletSettings.save();
    }
}
