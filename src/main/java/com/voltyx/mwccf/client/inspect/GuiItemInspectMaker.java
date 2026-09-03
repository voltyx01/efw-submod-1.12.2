package com.voltyx.mwccf.client.inspect;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiItemInspectMaker extends GuiScreen {

    private final GuiScreen parentScreen;
    private final ItemStack sampleStack;
    private ItemInspectConfig.InspectGroup currentGroup;
    private ItemInspectConfig.GroupTransform currentTransform;
    private final InspectDustManager dustManager = new InspectDustManager();

    public GuiItemInspectMaker(GuiScreen parentScreen, ItemStack sampleStack) {
        this.parentScreen = parentScreen;
        this.sampleStack = sampleStack;
        this.currentGroup = ItemInspectConfig.resolveGroup(sampleStack);
        this.currentTransform = ItemInspectConfig.getTransform(this.currentGroup);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.dustManager.init(this.width, this.height);
        this.buttonList.clear();

        int panelW = 165;
        int startX = 10;
        int startY = 26;
        int gap = 21; // Compact gap so all sliders and buttons fit on any resolution

        // Group selector button
        this.buttonList.add(new GuiButton(100, startX, startY, panelW, 18, "Группа: " + this.currentGroup.displayName));

        if (this.currentGroup == ItemInspectConfig.InspectGroup.TEXT_SETTINGS) {
            ItemInspectConfig.TextSettings ts = ItemInspectConfig.textSettings;

            this.buttonList.add(new GuiSlider(10, startX, startY + gap * 1, panelW, 18, "Title Scale: ", "x", 0.5, 3.0, ts.titleScale, false, true,
                    slider -> ts.titleScale = (float) slider.getValue()));

            this.buttonList.add(new GuiSlider(17, startX, startY + gap * 2, panelW, 18, "Title Pos Y: ", "px", -100.0, 100.0, ts.titleOffsetY, false, true,
                    slider -> ts.titleOffsetY = (int) slider.getValue()));

            this.buttonList.add(new GuiSlider(18, startX, startY + gap * 3, panelW, 18, "Title Pos X: ", "px", -150.0, 150.0, ts.titleOffsetX, false, true,
                    slider -> ts.titleOffsetX = (int) slider.getValue()));

            this.buttonList.add(new GuiSlider(11, startX, startY + gap * 4, panelW, 18, "Desc Scale: ", "x", 0.5, 2.0, ts.descScale, false, true,
                    slider -> ts.descScale = (float) slider.getValue()));

            this.buttonList.add(new GuiSlider(12, startX, startY + gap * 5, panelW, 18, "Block Pos Y: ", "px", -250.0, -10.0, ts.textOffsetY, false, true,
                    slider -> ts.textOffsetY = (int) slider.getValue()));

            this.buttonList.add(new GuiSlider(13, startX, startY + gap * 6, panelW, 18, "Divider W: ", "px", 50.0, 500.0, ts.dividerWidth, false, true,
                    slider -> ts.dividerWidth = (int) slider.getValue()));

            this.buttonList.add(new GuiSlider(14, startX, startY + gap * 7, panelW, 18, "Divider Gap: ", "px", 4.0, 40.0, ts.dividerGap, false, true,
                    slider -> ts.dividerGap = (int) slider.getValue()));

            this.buttonList.add(new GuiSlider(15, startX, startY + gap * 8, panelW, 18, "Desc Gap: ", "px", 2.0, 30.0, ts.descGap, false, true,
                    slider -> ts.descGap = (int) slider.getValue()));

            this.buttonList.add(new GuiSlider(16, startX, startY + gap * 9, panelW, 18, "Max Width: ", "px", 150.0, 600.0, ts.maxTextWidth, false, true,
                    slider -> ts.maxTextWidth = (int) slider.getValue()));

            // Action buttons securely placed inside bottom area
            int bottomBtnY = Math.min(this.height - 24, startY + gap * 10 + 2);
            this.buttonList.add(new GuiButton(200, startX, bottomBtnY, panelW / 2 - 2, 20, "Сохранить"));
            this.buttonList.add(new GuiButton(201, startX + panelW / 2 + 2, bottomBtnY, panelW / 2 - 2, 20, "Закрыть"));
            return;
        } else {
            this.buttonList.add(new GuiSlider(1, startX, startY + gap * 1, panelW, 18, "Scale: ", "x", 0.1, 5.0, currentTransform.scale, false, true,
                    slider -> currentTransform.scale = (float) slider.getValue()));

            this.buttonList.add(new GuiSlider(2, startX, startY + gap * 2, panelW, 18, "Pivot X: ", "", -2.0, 2.0, currentTransform.pivotX, false, true,
                    slider -> currentTransform.pivotX = (float) slider.getValue()));

            this.buttonList.add(new GuiSlider(3, startX, startY + gap * 3, panelW, 18, "Pivot Y: ", "", -2.0, 2.0, currentTransform.pivotY, false, true,
                    slider -> currentTransform.pivotY = (float) slider.getValue()));

            this.buttonList.add(new GuiSlider(4, startX, startY + gap * 4, panelW, 18, "Pivot Z: ", "", -2.0, 2.0, currentTransform.pivotZ, false, true,
                    slider -> currentTransform.pivotZ = (float) slider.getValue()));

            this.buttonList.add(new GuiSlider(5, startX, startY + gap * 5, panelW, 18, "Start Yaw: ", "°", -180.0, 180.0, currentTransform.startYaw, false, true,
                    slider -> currentTransform.startYaw = (float) slider.getValue()));

            this.buttonList.add(new GuiSlider(6, startX, startY + gap * 6, panelW, 18, "Start Pitch: ", "°", -180.0, 180.0, currentTransform.startPitch, false, true,
                    slider -> currentTransform.startPitch = (float) slider.getValue()));

            this.buttonList.add(new GuiSlider(7, startX, startY + gap * 7, panelW, 18, "Start Roll: ", "°", -180.0, 180.0, currentTransform.startRoll, false, true,
                    slider -> currentTransform.startRoll = (float) slider.getValue()));
        }

        // Action buttons
        int bottomBtnY = Math.min(this.height - 24, startY + gap * 8 + 4);
        this.buttonList.add(new GuiButton(200, startX, bottomBtnY, panelW / 2 - 2, 20, "Сохранить"));
        this.buttonList.add(new GuiButton(201, startX + panelW / 2 + 2, bottomBtnY, panelW / 2 - 2, 20, "Закрыть"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 100) {
            // Cycle groups
            ItemInspectConfig.InspectGroup[] all = ItemInspectConfig.InspectGroup.values();
            int nextIdx = (this.currentGroup.ordinal() + 1) % all.length;
            this.currentGroup = all[nextIdx];
            if (this.currentGroup != ItemInspectConfig.InspectGroup.TEXT_SETTINGS) {
                this.currentTransform = ItemInspectConfig.getTransform(this.currentGroup);
            }
            this.initGui();
        } else if (button.id == 200) {
            ItemInspectConfig.save();
        } else if (button.id == 201) {
            ItemInspectConfig.save();
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_M) {
            ItemInspectConfig.save();
            this.mc.displayGuiScreen(this.parentScreen);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Pure solid black background
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        drawRect(0, 0, this.width, this.height, 0xFF000000);

        // Ambient floating ash dust particles in the background (moving up and right)
        this.dustManager.updateAndRender(this.width, this.height, this.mc);

        // Center item preview
        int centerX = (this.width + 185) / 2;
        int centerY = this.height / 2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(centerX, centerY, 0.0F);

        // Render preview behind text
        if (currentTransform != null) {
            Item3DRenderer.renderConfigured3D(this.sampleStack, currentTransform, this.mc);
        }

        GlStateManager.popMatrix();

        // Clear depth so 2D overlay is guaranteed on top
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();

        // Crosshair for alignment
        drawRect(centerX - 15, centerY, centerX + 15, centerY + 1, 0x55FFFFFF);
        drawRect(centerX, centerY - 15, centerX + 1, centerY + 15, 0x55FFFFFF);

        // Black gradient overlay reaching 3/4 of the screen height (starts at 1/4 from top, transparent to solid black at bottom)
        int gradientTop = this.height / 4;
        this.drawGradientRect(0, gradientTop, this.width, this.height, 0x00000000, 0xFF000000);

        // Draw side panel on top
        drawRect(5, 5, 185, this.height - 5, 0x80000000);
        drawRect(5, 5, 185, 6, 0xFF444444);
        drawRect(5, this.height - 6, 185, this.height - 5, 0xFF444444);
        drawRect(5, 5, 6, this.height - 5, 0xFF444444);
        drawRect(184, 5, 185, this.height - 5, 0xFF444444);

        this.fontRenderer.drawStringWithShadow("3D Inspect Maker", 12, 10, 0xC8B86A);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
