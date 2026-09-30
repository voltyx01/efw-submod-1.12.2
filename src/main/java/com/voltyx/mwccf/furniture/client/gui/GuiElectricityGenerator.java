package com.voltyx.mwccf.furniture.client.gui;

import com.voltyx.mwccf.furniture.tileentity.ContainerElectricityGenerator;
import com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiElectricityGenerator extends GuiContainer {

    private static final ResourceLocation GENERATOR_GUI_TEXTURE = new ResourceLocation("refurbished_furniture", "textures/gui/container/electricity_generator.png");
    private final TileEntityElectricityGenerator generatorTile;

    public GuiElectricityGenerator(InventoryPlayer playerInv, TileEntityElectricityGenerator generatorTile) {
        super(new ContainerElectricityGenerator(playerInv, generatorTile));
        this.generatorTile = generatorTile;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format("container.refurbished_furniture.electricity_generator");
        this.fontRenderer.drawString(title, 8, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        boolean active = this.generatorTile.isGeneratingPower();
        this.fontRenderer.drawStringWithShadow(active ? "§aRUNNING" : "§cOFF", 65, 27, 0xFFFFFF);
        this.fontRenderer.drawStringWithShadow("POWER: " + (active ? "§e15" : "§70"), 65, 39, 0xFFFFFF);
        int secs = this.generatorTile.burnTime / 20;
        this.fontRenderer.drawStringWithShadow("FUEL: " + (active ? "§b" + secs + "s" : "§7EMPTY"), 65, 51, 0xFFFFFF);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GENERATOR_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if (this.generatorTile.isGeneratingPower()) {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(i + 27, j + 26 + 12 - k, 176, 12 - k, 14, k + 1);
        }
    }

    private int getBurnLeftScaled(int pixels) {
        int i = this.generatorTile.currentItemBurnTime;
        if (i == 0) {
            i = 200;
        }
        return this.generatorTile.burnTime * pixels / i;
    }
}
