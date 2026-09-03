package com.voltyx.gender.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WildfireButton extends GuiButton {

   public boolean transparent = false;
   private Runnable onPress;

   public WildfireButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, Runnable onPress) {
      super(buttonId, x, y, widthIn, heightIn, buttonText);
      this.onPress = onPress;
   }

   // In 1.12.2, drawing is done here
   @Override
   public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         FontRenderer fontrenderer = mc.fontRenderer;
         this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

         int clr = 0x54444444; // 0x444444 + (84 << 24)
         if (this.hovered) clr = 0x54666666;
         if (!this.enabled) clr = 0x54222222;

         if (!transparent) {
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, clr);
            GlStateManager.enableTexture2D();
         }

         int textColor = this.enabled ? 0xFFFFFF : 0x666666;

         this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, textColor);
      }
   }

   // Compatibility method so action can be triggered from GuiScreen
   public void press() {
      if (this.onPress != null) {
         this.onPress.run();
      }
   }

   public WildfireButton setTransparent(boolean b) {
      this.transparent = b;
      return this;
   }
}