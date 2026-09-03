package com.voltyx.gender.gui.screen;

import com.voltyx.gender.gui.WildfireButton;
import com.voltyx.gender.gui.WildfireSlider;
import com.voltyx.gender.main.Breasts;
import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.config.ClientConfiguration;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class WildfireBreastCustomizationScreen extends BaseWildfireScreen {

    private WildfireSlider breastSlider, xOffsetBoobSlider, yOffsetBoobSlider, zOffsetBoobSlider;
    private WildfireSlider cleavageSlider;

    public WildfireBreastCustomizationScreen(GuiScreen parent, UUID uuid) {
        super(I18n.format("wildfire_gender.appearance_settings.title"), parent, uuid);
    }

    @Override
    public void initGui() {
        super.initGui();
        int j = this.height / 2;

        GenderPlayer plr = getPlayer();
        if (plr == null) return;
        Breasts breasts = plr.getBreasts();

        // Колбек для сохранения настроек
        Consumer<Float> onSave = value -> {
            GenderPlayer.saveGenderInfo(plr);
        };

        this.buttonList.clear();

        // Кнопка "Выход"
        this.buttonList.add(new WildfireButton(0, this.width / 2 + 178, j - 61, 12, 12, "X", () -> {
            this.mc.displayGuiScreen(parent);
        }));

        // Ползунок размера
        this.buttonList.add(this.breastSlider = new WildfireSlider(1, this.width / 2 + 30, j - 48, 158, 20,
                ClientConfiguration.BUST_SIZE.getMinInclusive(), ClientConfiguration.BUST_SIZE.getMaxInclusive(), plr.getBustSize(),
                plr::updateBustSize,
                value -> I18n.format("wildfire_gender.wardrobe.slider.breast_size", Math.round(value * 100)),
                onSave));

        // Смещение по X (Separation)
        this.buttonList.add(this.xOffsetBoobSlider = new WildfireSlider(2, this.width / 2 + 30, j - 27, 158, 20,
                ClientConfiguration.BREASTS_OFFSET_X.getMinInclusive(), ClientConfiguration.BREASTS_OFFSET_X.getMaxInclusive(), breasts.getXOffset(),
                breasts::updateXOffset,
                value -> I18n.format("wildfire_gender.wardrobe.slider.separation", Math.round((Math.round(value * 100f) / 100f) * 10)),
                onSave));

        // Смещение по Y (Height)
        this.buttonList.add(this.yOffsetBoobSlider = new WildfireSlider(3, this.width / 2 + 30, j - 6, 158, 20,
                ClientConfiguration.BREASTS_OFFSET_Y.getMinInclusive(), ClientConfiguration.BREASTS_OFFSET_Y.getMaxInclusive(), breasts.getYOffset(),
                breasts::updateYOffset,
                value -> I18n.format("wildfire_gender.wardrobe.slider.height", Math.round((Math.round(value * 100f) / 100f) * 10)),
                onSave));

        // Смещение по Z (Depth)
        this.buttonList.add(this.zOffsetBoobSlider = new WildfireSlider(4, this.width / 2 + 30, j + 15, 158, 20,
                ClientConfiguration.BREASTS_OFFSET_Z.getMinInclusive(), ClientConfiguration.BREASTS_OFFSET_Z.getMaxInclusive(), breasts.getZOffset(),
                breasts::updateZOffset,
                value -> I18n.format("wildfire_gender.wardrobe.slider.depth", Math.round((Math.round(value * 100f) / 100f) * 10)),
                onSave));

        // Поворот / Ложбинка (Cleavage)
        this.buttonList.add(this.cleavageSlider = new WildfireSlider(5, this.width / 2 + 30, j + 36, 158, 20,
                ClientConfiguration.BREASTS_CLEAVAGE.getMinInclusive(), ClientConfiguration.BREASTS_CLEAVAGE.getMaxInclusive(), breasts.getCleavage(),
                breasts::updateCleavage,
                value -> I18n.format("wildfire_gender.wardrobe.slider.rotation", Math.round((Math.round(value * 100f) / 100f) * 100)),
                onSave));

        // Кнопка объединения физики (Uniboob)
        this.buttonList.add(new WildfireButton(6, this.width / 2 + 30, j + 57, 158, 20,
                I18n.format("wildfire_gender.breast_customization.dual_physics", I18n.format(breasts.isUniboob() ? "wildfire_gender.label.no" : "wildfire_gender.label.yes")),
                () -> {
                    boolean isUniboob = !breasts.isUniboob();
                    if (breasts.updateUniboob(isUniboob)) {
                        for (GuiButton btn : this.buttonList) {
                            if (btn.id == 6) {
                                btn.displayString = I18n.format("wildfire_gender.breast_customization.dual_physics", I18n.format(isUniboob ? "wildfire_gender.label.no" : "wildfire_gender.label.yes"));
                            }
                        }
                        GenderPlayer.saveGenderInfo(plr);
                    }
                }));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof WildfireButton) {
            ((WildfireButton) button).press();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GenderPlayer plr = getPlayer();
        this.drawDefaultBackground();

        if (plr == null) return;

        try {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int xP = this.width / 2 - 102;
            int yP = this.height / 2 + 275; // Обрати внимание, в 1.18.2 тут огромный сдвиг yP
            EntityPlayer ent = this.mc.world.getPlayerEntityByUUID(this.playerUUID);
            if (ent != null) {
                // Масштаб увеличен до 200, чтобы было лучше видно детали вблизи
                WildfirePlayerListScreen.drawEntityOnScreen(xP, yP, 200, -20f, -20f, ent);
            } else {
                this.mc.displayGuiScreen(new WildfirePlayerListScreen(this.mc));
            }
        } catch (Exception e) {
            this.mc.displayGuiScreen(new WildfirePlayerListScreen(this.mc));
        }

        // Скрываем ползунки, если у пола не может быть груди
        boolean canHaveBreasts = plr.getGender().canHaveBreasts();
        breastSlider.visible = canHaveBreasts;
        xOffsetBoobSlider.visible = canHaveBreasts;
        yOffsetBoobSlider.visible = canHaveBreasts;
        zOffsetBoobSlider.visible = canHaveBreasts;
        cleavageSlider.visible = canHaveBreasts;
        // Для кнопки Uniboob (id 6) тоже управляем видимостью
        for (GuiButton btn : this.buttonList) {
            if (btn.id == 6) btn.visible = canHaveBreasts;
        }

        int x = this.width / 2;
        int y = this.height / 2;

        // Рисуем затемненные плашки (подложку)
        drawRect(x + 28, y - 64, x + 190, y + 79, 0x55000000);
        drawRect(x + 29, y - 63, x + 189, y - 50, 0x55000000);

        // Заголовок
        this.fontRenderer.drawStringWithShadow(this.title, x + 32, y - 60, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        // В 1.12.2 нужно вручную сообщать ползункам об отжатии кнопки мыши, чтобы они сохранились
        if (breastSlider != null) breastSlider.mouseReleased(mouseX, mouseY);
        if (xOffsetBoobSlider != null) xOffsetBoobSlider.mouseReleased(mouseX, mouseY);
        if (yOffsetBoobSlider != null) yOffsetBoobSlider.mouseReleased(mouseX, mouseY);
        if (zOffsetBoobSlider != null) zOffsetBoobSlider.mouseReleased(mouseX, mouseY);
        if (cleavageSlider != null) cleavageSlider.mouseReleased(mouseX, mouseY);
    }
}