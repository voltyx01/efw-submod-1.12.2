package com.voltyx.gender.gui.screen;

import com.voltyx.gender.gui.WildfirePlayerList;
import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.GenderPlayer.Gender;
import com.voltyx.gender.main.WildfireGender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class WildfirePlayerListScreen extends GuiScreen {

	private static final UUID CREATOR_UUID = UUID.fromString("33c937ae-6bfc-423e-a38e-3a613e7c1256");
	private ResourceLocation TXTR_BACKGROUND;

	@Nullable
	private String tooltip = null;

	public static GenderPlayer HOVER_PLAYER;
	private WildfirePlayerList PLAYER_LIST;
	private final Minecraft client;

	public WildfirePlayerListScreen(Minecraft mc) {
		this.client = mc;
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);

		int x = this.width / 2;
		int y = this.height / 2 - 20;

		this.buttonList.clear();

		// 1.12.2 button logic (id, x, y, width, height, text)
		this.buttonList.add(new GuiButton(0, this.width / 2 + 53, y - 74, 12, 12, "X"));

		// Initialize the player list
		this.PLAYER_LIST = new WildfirePlayerList(this, 118, y - 61, y + 71);

		this.TXTR_BACKGROUND = new ResourceLocation(WildfireGender.MODID, "textures/gui/player_list.png");
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		if (this.PLAYER_LIST != null) {
			this.PLAYER_LIST.handleMouseInput();
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.client.displayGuiScreen(null);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (this.PLAYER_LIST != null) {
			this.PLAYER_LIST.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		if (this.PLAYER_LIST != null) {
			this.PLAYER_LIST.mouseReleased(mouseX, mouseY, state);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		HOVER_PLAYER = null;
		this.setTooltip(null);

		this.drawDefaultBackground();

		// Refresh the list logic
		PLAYER_LIST.refreshList();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.TXTR_BACKGROUND != null) {
			this.client.getTextureManager().bindTexture(this.TXTR_BACKGROUND);
		}

		int i = (this.width - 132) / 2;
		int j = (this.height - 156) / 2 - 20;
		this.drawTexturedModalRect(i, j, 0, 0, 192, 174);

		int x = this.width / 2;
		int y = this.height / 2 - 20;

		// Custom Scissor rendering for 1.12.2 to mimic scrolling viewport clipping
		int scale = this.client.gameSettings.guiScale;
		if (scale == 0)
			scale = 1000;
		int scaleFactor = 0;
		while (scaleFactor < scale && this.client.displayWidth / (scaleFactor + 1) >= 320
				&& this.client.displayHeight / (scaleFactor + 1) >= 240) {
			++scaleFactor;
		}
		if (scaleFactor == 0)
			scaleFactor = 1;

		int left = x - 59;
		int bottom = y - 32;
		int width = 118;
		int height = 134;

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(left * scaleFactor, this.client.displayHeight - (bottom + height) * scaleFactor,
				width * scaleFactor, height * scaleFactor);

		PLAYER_LIST.drawScreen(mouseX, mouseY, partialTicks);

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		// Draw Hover Information
		if (HOVER_PLAYER != null) {
			int dialogX = x + 75;
			int dialogY = y - 73;
			EntityPlayer pEntity = this.client.world.getPlayerEntityByUUID(HOVER_PLAYER.uuid);

			if (pEntity != null) {
				this.fontRenderer.drawStringWithShadow(TextFormatting.UNDERLINE + pEntity.getDisplayNameString(),
						dialogX, dialogY - 2, 0xFFFFFF);
			}

			Gender gender = HOVER_PLAYER.getGender();
			this.fontRenderer.drawStringWithShadow(
					I18n.format("wildfire_gender.label.gender") + " " + gender.getDisplayName().getFormattedText(),
					dialogX, dialogY + 10, 0xBBBBBB);

			if (gender.canHaveBreasts()) {
				this.fontRenderer.drawStringWithShadow(I18n.format("wildfire_gender.wardrobe.slider.breast_size",
						Math.round(HOVER_PLAYER.getBustSize() * 100)), dialogX, dialogY + 20, 0xBBBBBB);

				String physicsStatus = HOVER_PLAYER.hasBreastPhysics() ? I18n.format("wildfire_gender.label.enabled")
						: I18n.format("wildfire_gender.label.disabled");
				this.fontRenderer.drawStringWithShadow(
						I18n.format("wildfire_gender.char_settings.physics", physicsStatus), dialogX, dialogY + 40,
						0xBBBBBB);

				this.fontRenderer.drawStringWithShadow(I18n.format("wildfire_gender.player_list.bounce_multiplier",
						HOVER_PLAYER.getBounceMultiplier()), dialogX + 6, dialogY + 50, 0xBBBBBB);
				this.fontRenderer.drawStringWithShadow(I18n.format("wildfire_gender.player_list.breast_momentum",
						Math.round(HOVER_PLAYER.getFloppiness() * 100)), dialogX + 6, dialogY + 60, 0xBBBBBB);

				String soundsStatus = HOVER_PLAYER.hasHurtSounds() ? I18n.format("wildfire_gender.label.enabled")
						: I18n.format("wildfire_gender.label.disabled");
				this.fontRenderer.drawStringWithShadow(
						I18n.format("wildfire_gender.player_list.female_sounds", soundsStatus), dialogX, dialogY + 80,
						0xBBBBBB);
			}

			if (pEntity != null) {
				drawEntityOnScreen(x - 110, y + 45, 45, (x - 300), (y - 26), pEntity);
			}
		}

		this.fontRenderer.drawString(I18n.format("wildfire_gender.player_list.title"), x - 60, y - 73, 4473924);

		boolean withCreator = false;
		Collection<NetworkPlayerInfo> playersC = this.client.getConnection().getPlayerInfoMap();

		for (NetworkPlayerInfo loadedPlayer : playersC) {
			if (loadedPlayer.getGameProfile().getId().equals(CREATOR_UUID)) {
				withCreator = true;
				break;
			}
		}

		if (withCreator) {
			this.drawCenteredString(this.fontRenderer, I18n.format("wildfire_gender.label.with_creator"),
					this.width / 2, y + 100, 0xFF00FF);
		}

		super.drawScreen(mouseX, mouseY, partialTicks); // Renders buttons

		if (tooltip != null) {
			this.drawHoveringText(tooltip, mouseX, mouseY);
		}
	}

	public void setTooltip(@Nullable String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Utility to draw the 3D entity on the GUI in 1.12.2.
	 * Often part of GuiInventory, extracted here for convenience.
	 */
	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityPlayer ent) {
		GlStateManager.enableColorMaterial();

		// ВКЛЮЧАЕМ нормализацию масштаба. Без этого свет "взрывается" и заливает модель
		// белым!
		GlStateManager.enableRescaleNormal();

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) posX, (float) posY, 50.0F);
		// Отрицательный масштаб по X инвертирует winding order полигонов,
		// из-за чего culling начинает вырезать не те грани (видны "внутренние"
		// полигоны). Отключаем cull face на время отрисовки, как это уже
		// сделано в NoteGui/DiaryGui для той же ситуации.
		GlStateManager.disableCull();
		GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;

		// Save the real live animation state so we can restore it after rendering -
		// this GUI must always show idle-standing regardless of what the player is
		// actually doing in-game (walking, sneaking, mining/using an item, etc.).
		boolean wasSneaking = ent.isSneaking();
		float savedLimbSwing = ent.limbSwing;
		float savedLimbSwingAmount = ent.limbSwingAmount;
		float savedPrevLimbSwingAmount = ent.prevLimbSwingAmount;
		float savedSwingProgress = ent.swingProgress;
		float savedPrevSwingProgress = ent.prevSwingProgress;
		float savedSwingProgressLast = getFloatField(ent, "field_184615_bR", "swingProgressLast");
		net.minecraft.item.ItemStack savedActiveItem = ent.getActiveItemStack();

		try {
			ent.setSneaking(false);
			ent.limbSwing = 0.0F;
			ent.limbSwingAmount = 0.0F;
			ent.prevLimbSwingAmount = 0.0F;
			ent.swingProgress = 0.0F;
			ent.prevSwingProgress = 0.0F;
			setFloatField(ent, "field_184615_bR", "swingProgressLast", 0.0F);
			if (!savedActiveItem.isEmpty()) {
				ent.resetActiveHand();
			}
		} catch (Throwable ignored) {
			// best-effort - if a field name doesn't resolve on this mapping/version,
			// just skip resetting that particular part of the pose
		}

		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		ent.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
		ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
		ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;

		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);

		// Отключаем рендер круглой тени ПОД ногами (для меню она не нужна)
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true); // Возвращаем для мира

		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;

		try {
			ent.setSneaking(wasSneaking);
			ent.limbSwing = savedLimbSwing;
			ent.limbSwingAmount = savedLimbSwingAmount;
			ent.prevLimbSwingAmount = savedPrevLimbSwingAmount;
			ent.swingProgress = savedSwingProgress;
			ent.prevSwingProgress = savedPrevSwingProgress;
			setFloatField(ent, "field_184615_bR", "swingProgressLast", savedSwingProgressLast);
		} catch (Throwable ignored) {
		}

		GlStateManager.popMatrix();
		GlStateManager.enableCull();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**
	 * Best-effort reflective field access, trying the SRG (obfuscated) name first
	 * and falling back to the MCP dev name - some of EntityLivingBase's animation
	 * bookkeeping fields aren't public, and exact availability depends on mapping
	 * version, so this fails soft rather than breaking the build.
	 */
	private static float getFloatField(Object obj, String srgName, String mcpName) {
		try {
			java.lang.reflect.Field field = findField(obj.getClass(), srgName, mcpName);
			field.setAccessible(true);
			return field.getFloat(obj);
		} catch (Throwable t) {
			return 0.0F;
		}
	}

	private static void setFloatField(Object obj, String srgName, String mcpName, float value) {
		try {
			java.lang.reflect.Field field = findField(obj.getClass(), srgName, mcpName);
			field.setAccessible(true);
			field.setFloat(obj, value);
		} catch (Throwable ignored) {
		}
	}

	private static java.lang.reflect.Field findField(Class<?> clazz, String srgName, String mcpName)
			throws NoSuchFieldException {
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			try {
				return c.getDeclaredField(srgName);
			} catch (NoSuchFieldException ignored1) {
			}
			try {
				return c.getDeclaredField(mcpName);
			} catch (NoSuchFieldException ignored2) {
			}
		}
		throw new NoSuchFieldException(srgName + "/" + mcpName);
	}
}