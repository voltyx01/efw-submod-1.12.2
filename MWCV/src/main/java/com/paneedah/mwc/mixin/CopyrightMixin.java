package com.paneedah.mwc.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public class CopyrightMixin {

    @Inject(method = "renderString", at = @At("HEAD"), cancellable = true)
    private void blockCopyrightText(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir) {
        // Проверяем, находимся ли мы в главном меню
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) {

            // Отладочный вывод (уберите его, когда убедитесь, что все работает)
            // System.out.println("DEBUG: Menu is drawing text: " + text);

            // Проверяем текст на наличие ключевых слов копирайта
            // Если текст содержит "Copyright" или специфичные для Mojang символы - отменяем
            if (text != null && (text.contains("Copyright") || text.contains("Mojang"))) {
                // Отменяем отрисовку этой строки
                cir.setReturnValue(0);
                cir.cancel();
            }
        }
    }
}