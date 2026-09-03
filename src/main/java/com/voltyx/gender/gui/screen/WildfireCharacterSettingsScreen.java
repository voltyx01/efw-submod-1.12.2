package com.voltyx.gender.gui.screen;

import com.voltyx.gender.gui.WildfireButton;
import com.voltyx.gender.gui.WildfireSlider;
import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.WildfireGender;
import com.voltyx.gender.main.config.ClientConfiguration;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class WildfireCharacterSettingsScreen extends BaseWildfireScreen {

    private static final String ENABLED = TextFormatting.GREEN + I18n.format("wildfire_gender.label.enabled") + TextFormatting.RESET;
    private static final String DISABLED = TextFormatting.RED + I18n.format("wildfire_gender.label.disabled") + TextFormatting.RESET;

    private WildfireSlider bounceSlider, floppySlider;
    private ResourceLocation BACKGROUND;
    private int yPos = 0;
    private boolean bounceWarning;

    public WildfireCharacterSettingsScreen(GuiScreen parent, UUID uuid) {
        super(I18n.format("wildfire_gender.char_settings.title"), parent, uuid);
    }

    @Override
    public void initGui() {
        super.initGui();
        GenderPlayer aPlr = getPlayer();
        if (aPlr == null) return;

        int x = this.width / 2;
        int y = this.height / 2;

        yPos = y - 47;
        int xPos = x - 156 / 2 - 1;

        this.buttonList.clear();

        // Кнопка "Закрыть" (крестик)
        this.buttonList.add(new WildfireButton(0, this.width / 2 + 73, yPos - 11, 12, 12, "X", () -> {
            this.mc.displayGuiScreen(parent);
        }));

        // Кнопка физики груди
        this.buttonList.add(new WildfireButton(1, xPos, yPos, 157, 20,
                I18n.format("wildfire_gender.char_settings.physics", aPlr.hasBreastPhysics() ? ENABLED : DISABLED), () -> {
            boolean enablePhysics = !aPlr.hasBreastPhysics();
            if (aPlr.updateBreastPhysics(enablePhysics)) {
                updateButtonText(1, I18n.format("wildfire_gender.char_settings.physics", enablePhysics ? ENABLED : DISABLED));
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }));

        // Кнопка физики брони
        this.buttonList.add(new WildfireButton(2, xPos, yPos + 20, 157, 20,
                I18n.format("wildfire_gender.char_settings.armor_physics", aPlr.hasArmorBreastPhysics() ? ENABLED : DISABLED), () -> {
            boolean enablePhysicsArmor = !aPlr.hasArmorBreastPhysics();
            if (aPlr.updateArmorBreastPhysics(enablePhysicsArmor)) {
                updateButtonText(2, I18n.format("wildfire_gender.char_settings.armor_physics", enablePhysicsArmor ? ENABLED : DISABLED));
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }));

        // Кнопка скрытия в броне
        this.buttonList.add(new WildfireButton(3, xPos, yPos + 40, 157, 20,
                I18n.format("wildfire_gender.char_settings.hide_in_armor", aPlr.showBreastsInArmor() ? DISABLED : ENABLED), () -> {
            boolean enableShowInArmor = !aPlr.showBreastsInArmor();
            if (aPlr.updateShowBreastsInArmor(enableShowInArmor)) {
                updateButtonText(3, I18n.format("wildfire_gender.char_settings.hide_in_armor", enableShowInArmor ? DISABLED : ENABLED));
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }));

        // Ползунок отскока (Bounce Multiplier)
        this.buttonList.add(this.bounceSlider = new WildfireSlider(4, xPos, yPos + 60, 158, 22,
                ClientConfiguration.BOUNCE_MULTIPLIER.getMinInclusive(), ClientConfiguration.BOUNCE_MULTIPLIER.getMaxInclusive(), aPlr.getBounceMultiplierRaw(),
                value -> {},
                value -> {
                    float bounceText = 3 * value;
                    float v = Math.round(bounceText * 10) / 10f;
                    bounceWarning = v > 1;
                    if (v == 3) {
                        return I18n.format("wildfire_gender.slider.max_bounce");
                    } else if (Math.round(bounceText * 100) / 100f == 0) {
                        return I18n.format("wildfire_gender.slider.min_bounce");
                    }
                    return I18n.format("wildfire_gender.slider.bounce", v);
                },
                value -> {
                    if (aPlr.updateBounceMultiplier(value)) {
                        GenderPlayer.saveGenderInfo(aPlr);
                    }
                }));

        // Ползунок инерции (Floppiness)
        this.buttonList.add(this.floppySlider = new WildfireSlider(5, xPos, yPos + 80, 158, 22,
                ClientConfiguration.FLOPPY_MULTIPLIER.getMinInclusive(), ClientConfiguration.FLOPPY_MULTIPLIER.getMaxInclusive(), aPlr.getFloppiness(),
                value -> {},
                value -> I18n.format("wildfire_gender.slider.floppy", Math.round(value * 100)),
                value -> {
                    if (aPlr.updateFloppiness(value)) {
                        GenderPlayer.saveGenderInfo(aPlr);
                    }
                }));

        // Кнопка женских звуков (Hurt Sounds)
        this.buttonList.add(new WildfireButton(6, xPos, yPos + 100, 157, 20,
                I18n.format("wildfire_gender.char_settings.hurt_sounds", aPlr.hasHurtSounds() ? ENABLED : DISABLED), () -> {
            boolean enableHurtSounds = !aPlr.hasHurtSounds();
            if (aPlr.updateHurtSounds(enableHurtSounds)) {
                updateButtonText(6, I18n.format("wildfire_gender.char_settings.hurt_sounds", enableHurtSounds ? ENABLED : DISABLED));
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }));

        this.BACKGROUND = new ResourceLocation(WildfireGender.MODID, "textures/gui/settings_bg.png");
    }

    private void updateButtonText(int id, String text) {
        for (GuiButton btn : this.buttonList) {
            if (btn.id == id) {
                btn.displayString = text;
                break;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof WildfireButton) {
            ((WildfireButton) button).press();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        EntityPlayer plrEntity = this.mc.world.getPlayerEntityByUUID(this.playerUUID);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.BACKGROUND != null) {
            this.mc.getTextureManager().bindTexture(this.BACKGROUND);
        }

        int i = (this.width - 172) / 2;
        int j = (this.height - 124) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 172, 144);

        int x = this.width / 2;
        int y = this.height / 2;

        // Рисуем заголовок меню
        this.fontRenderer.drawString(title, x - 79, yPos - 10, 4473924);

        super.drawScreen(mouseX, mouseY, partialTicks); // Отрисовка кнопок

        // Рисуем ник игрока над меню
        if (plrEntity != null) {
            this.drawCenteredString(this.fontRenderer, plrEntity.getDisplayNameString(), x, yPos - 30, 0xFFFFFF);
        }

        // Предупреждение о высоком отскоке
        if (bounceWarning) {
            this.drawCenteredString(this.fontRenderer, TextFormatting.ITALIC + I18n.format("wildfire_gender.tooltip.bounce_warning"), x, y + 90, 0xFF6666);
        }

        // Отрисовка тултипов при наведении
        List<String> tooltip = new ArrayList<>();
        for (GuiButton btn : this.buttonList) {
            if (btn.isMouseOver()) {
                if (btn.id == 1) tooltip.add(I18n.format("wildfire_gender.tooltip.breast_physics"));
                else if (btn.id == 2) tooltip.add(I18n.format("wildfire_gender.tooltip.armor_physics"));
                else if (btn.id == 3) tooltip.add(I18n.format("wildfire_gender.tooltip.hide_in_armor"));
                else if (btn.id == 6) tooltip.add(I18n.format("wildfire_gender.tooltip.hurt_sounds"));
            }
        }
        if (!tooltip.isEmpty()) {
            this.drawHoveringText(tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (bounceSlider != null) bounceSlider.mouseReleased(mouseX, mouseY);
        if (floppySlider != null) floppySlider.mouseReleased(mouseX, mouseY);
    }
}