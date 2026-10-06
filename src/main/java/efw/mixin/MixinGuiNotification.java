package efw.mixin;

import com.voltyx.mwccf.client.loading.ItemLoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.GuiConfirmation;
import net.minecraftforge.fml.client.GuiNotification;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNotification.class)
public abstract class MixinGuiNotification extends GuiScreen {

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ci.cancel();

        // 1. Отрисовка нашего кастомного экрана загрузки (фон, карточка предмета, змейка)
        ItemLoadingScreenRenderer.render(this.width, this.height, "", "");

        boolean isConfirmation = ((Object) this) instanceof GuiConfirmation;

        int btnX = 24;
        int btnW = isConfirmation ? 75 : 100;
        int btnH = 20;
        int btnY = this.height - 40;

        // Позиционируем кнопки слева
        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton btn = this.buttonList.get(i);
            btn.width = btnW;
            btn.height = btnH;
            btn.y = btnY;
            if (isConfirmation) {
                if (btn.id == 0) {
                    btn.x = btnX;
                    btn.displayString = net.minecraft.client.resources.I18n.format("gui.yes");
                } else if (btn.id == 1) {
                    btn.x = btnX + btnW + 8;
                    btn.displayString = net.minecraft.client.resources.I18n.format("gui.no");
                }
            } else {
                btn.x = btnX + i * (btnW + 8);
            }
        }

        // Подложка под текст и кнопки
        int panelRight = isConfirmation ? (btnX + btnW * 2 + 16) : (btnX + btnW + 16);
        Gui.drawRect(btnX - 8, btnY - 34, panelRight, btnY + btnH + 6, 0x99000000);
        // Тонкая рамка
        Gui.drawRect(btnX - 8, btnY - 34, panelRight, btnY - 33, 0x33FFFFFF);
        Gui.drawRect(btnX - 8, btnY + btnH + 5, panelRight, btnY + btnH + 6, 0x33FFFFFF);

        // Текст сообщения над кнопками через lang
        if (isConfirmation) {
            String line1 = net.minecraft.client.resources.I18n.format("mwccf.loading.missing_entries.title");
            String line2 = net.minecraft.client.resources.I18n.format("mwccf.loading.missing_entries.confirm");
            this.fontRenderer.drawStringWithShadow(line1, btnX, btnY - 26, 0xFFE08A);
            this.fontRenderer.drawStringWithShadow(line2, btnX, btnY - 14, 0xFFFFFF);
        } else {
            String line1 = net.minecraft.client.resources.I18n.format("mwccf.loading.notification.title");
            this.fontRenderer.drawStringWithShadow(line1, btnX, btnY - 26, 0xFFE08A);
        }

        // Отрисовка кнопок
        for (int i = 0; i < this.buttonList.size(); ++i) {
            this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
    }
}

