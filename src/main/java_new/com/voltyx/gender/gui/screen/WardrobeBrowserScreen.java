package com.voltyx.gender.gui.screen;

import com.voltyx.gender.gui.WildfireButton;
import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.GenderPlayer.Gender;
import com.voltyx.gender.main.WildfireGender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class WardrobeBrowserScreen extends BaseWildfireScreen {

	private ResourceLocation BACKGROUND;
	public static float modelRotation = 0.5F;
	public static int savedMouseX = 0;
	public static int savedMouseY = 0;

	public WardrobeBrowserScreen(GuiScreen parent, UUID uuid) {
		super(I18n.format("wildfire_gender.wardrobe.title"), parent, uuid);
	}

	@Override
	public void initGui() {
		super.initGui();
		int j = this.height / 2;

		GenderPlayer plr = getPlayer();
		if (plr == null) return;

		this.buttonList.clear();

		this.buttonList.add(new WildfireButton(1, this.width / 2 - 42, j - 52, 158, 20, getGenderLabel(plr.getGender()), () -> {
			Gender gender;
			switch (plr.getGender()) {
				case MALE: gender = Gender.FEMALE; break;
				case FEMALE: gender = Gender.OTHER; break;
				case OTHER: gender = Gender.MALE; break;
				default: gender = Gender.FEMALE;
			}
			if (plr.updateGender(gender)) {
				for (GuiButton btn : this.buttonList) {
					if (btn.id == 1) {
						btn.displayString = getGenderLabel(gender);
						break;
					}
				}
				GenderPlayer.saveGenderInfo(plr);
			}
		}));

		this.buttonList.add(new WildfireButton(2, this.width / 2 - 42, j - 32, 158, 20, I18n.format("wildfire_gender.appearance_settings.title") + "...", () -> {
			this.mc.displayGuiScreen(new WildfireBreastCustomizationScreen(this, this.playerUUID));
		}));

		this.buttonList.add(new WildfireButton(3, this.width / 2 - 42, j - 12, 158, 20, I18n.format("wildfire_gender.char_settings.title") + "...", () -> {
			this.mc.displayGuiScreen(new WildfireCharacterSettingsScreen(this, this.playerUUID));
		}));

		this.buttonList.add(new WildfireButton(4, this.width / 2 + 111, j - 63, 12, 12, "X", () -> {
			mc.addScheduledTask(() -> {
				mc.player.openContainer = mc.player.inventoryContainer;
				mc.displayGuiScreen(new net.minecraft.client.gui.inventory.GuiInventory(mc.player));
			});
		}));

		modelRotation = 0.6F;
		this.BACKGROUND = new ResourceLocation(WildfireGender.MODID, "textures/gui/wardrobe_bg.png");
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button instanceof WildfireButton) {
			((WildfireButton) button).press();
		}
	}

	private String getGenderLabel(Gender gender) {
		return I18n.format("wildfire_gender.label.gender") + " - " + gender.getDisplayName().getFormattedText();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GenderPlayer plr = getPlayer();

		this.drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (this.BACKGROUND != null) {
			this.mc.getTextureManager().bindTexture(this.BACKGROUND);
		}

		int i = (this.width - 248) / 2;
		int j = (this.height - 134) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, 248, 156);

		if (plr == null) return;

		int x = this.width / 2;
		int y = this.height / 2;

		this.fontRenderer.drawString(this.title, x - 42, y - 62, 4473924);

		modelRotation = 0.6f;

		try {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			int xP = this.width / 2 - 82;
			int yP = this.height / 2 + 32;
			EntityPlayer ent = this.mc.world.getPlayerEntityByUUID(this.playerUUID);

			if (ent != null) {
				WildfirePlayerListScreen.drawEntityOnScreen(xP, yP, 45, (float)(xP - mouseX), (float)(yP - 76 - mouseY), ent);
			} else {
				this.mc.displayGuiScreen(new WildfirePlayerListScreen(this.mc));
			}
		} catch (Exception e) {
			this.mc.displayGuiScreen(new WildfirePlayerListScreen(this.mc));
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}