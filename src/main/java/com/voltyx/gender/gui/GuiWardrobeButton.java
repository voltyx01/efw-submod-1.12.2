package com.voltyx.gender.gui.button;

import com.voltyx.gender.gui.screen.WardrobeBrowserScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiWardrobeButton extends GuiButton {

    private final GuiContainer parentGui;
    private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("mwccf", "textures/gui/wardrobe_button.png");

    public GuiWardrobeButton(int buttonId, GuiContainer parentGui, int x, int y, int width, int height) {
        super(buttonId, x, parentGui.getGuiTop() + y, width, height, "");
        this.parentGui = parentGui;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        int x = this.x + this.parentGui.getGuiLeft();
        boolean pressed = this.enabled && this.visible
                && mouseX >= x && mouseY >= this.y
                && mouseX < x + this.width
                && mouseY < this.y + this.height;

        if (pressed) {
            com.voltyx.mwccf.client.inspect.InspectTransitionHandler.startTransitionToScreen(
                    new com.voltyx.mwccf.sins.client.GuiSevenScreen(this.parentGui, mc.player.getUniqueID()),
                    this.parentGui
            );
        }
        return pressed;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

            int x = this.x + this.parentGui.getGuiLeft();
            this.hovered = mouseX >= x && mouseY >= this.y && mouseX < x + this.width && mouseY < this.y + this.height;

            int textureX = this.hovered ? 10 : 0;

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 200);

            drawModalRectWithCustomSizedTexture(x, this.y, textureX, 0, this.width, this.height, 20, 10);

            if (this.hovered) {
                this.drawCenteredString(
                        mc.fontRenderer,
                        net.minecraft.client.resources.I18n.format("wildfire_gender.buttoninevntory"),
                        x + this.width / 2 - 1,
                        this.y + this.height + 1,
                        0xFFFFFF
                );
            }

            GlStateManager.popMatrix();
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}