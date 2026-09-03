package com.voltyx.gender.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Consumer;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class WildfireSlider extends GuiButton {

	private final double minValue;
	private final double maxValue;
	private final Consumer<Float> valueUpdate;
	private final Function<Float, String> messageUpdate;
	private final Consumer<Float> onSave;

	public float sliderValue; // 0.0 to 1.0
	private float lastValue;
	public boolean dragging;
	private boolean changed;

	public WildfireSlider(int id, int xPos, int yPos, int width, int height, double minVal, double maxVal, double currentVal, Consumer<Float> valueUpdate, Function<Float, String> messageUpdate, Consumer<Float> onSave) {
		super(id, xPos, yPos, width, height, "");
		this.minValue = minVal;
		this.maxValue = maxVal;
		this.valueUpdate = valueUpdate;
		this.messageUpdate = messageUpdate;
		this.onSave = onSave;

		setValueInternal(currentVal);
	}

	protected void updateMessage() {
		this.displayString = messageUpdate.apply(lastValue);
	}

	protected void applyValue() {
		float newValue = getFloatValue();
		if (lastValue != newValue) {
			valueUpdate.accept(newValue);
			lastValue = newValue;
			changed = true;
		}
		updateMessage();
	}

	public void save() {
		if (changed) {
			onSave.accept(lastValue);
			changed = false;
		}
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

			if (this.dragging) {
				this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
				this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
				applyValue();
			}

			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.disableDepth();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

			// Outer background
			Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, 0x54000000); // 84 << 24

			// Inner background
			Gui.drawRect(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, 0x80222222);

			// Inner Blue Filler
			int xPos = this.x + 4 + (int) (this.sliderValue * (this.width - 8));
			Gui.drawRect(this.x + 2, this.y + 2, xPos, this.y + this.height - 2, 0xB4222266);

			// White Handle
			int xPos2 = this.x + 2 + (int) (this.sliderValue * (this.width - 4));
			Gui.drawRect(xPos2 - 2, this.y + 1, xPos2, this.y + this.height - 1, 0x78FFFFFF);

			GlStateManager.enableDepth();
			GlStateManager.enableTexture2D();

			FontRenderer font = mc.fontRenderer;
			int textColor = this.hovered || this.changed ? 0xFFFF55 : 0xFFFFFF;
			this.drawCenteredString(font, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, textColor);
		}
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (super.mousePressed(mc, mouseX, mouseY)) {
			this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
			this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
			applyValue();
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		this.dragging = false;
		save();
	}

	public float getFloatValue() {
		return (float) getValue();
	}

	public double getValue() {
		return this.sliderValue * (maxValue - minValue) + minValue;
	}

	private void setValueInternal(double value) {
		this.sliderValue = (float) MathHelper.clamp((value - this.minValue) / (this.maxValue - this.minValue), 0.0, 1.0);
		this.lastValue = (float) value;
		updateMessage();
	}
}