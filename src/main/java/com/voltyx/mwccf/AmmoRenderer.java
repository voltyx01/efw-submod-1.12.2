package com.voltyx.mwccf;

import com.paneedah.weaponlib.AttachmentCategory;
import com.paneedah.weaponlib.ItemMagazine;
import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.WeaponAttachmentAspect;
import com.paneedah.weaponlib.config.BalancePackManager;
import com.paneedah.weaponlib.config.ModernConfigManager;
import com.paneedah.weaponlib.jim.util.LangTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class AmmoRenderer extends Gui {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final ResourceLocation AMMO_TEX = new ResourceLocation("mwc:textures/hud/ammoiconsheet.png");

    public static void render(PlayerWeaponInstance weaponInstance, double width, double height) {
        if (!ModernConfigManager.enableAmmoCounter) return;

        // Позиционирование HUD
        int posX = 256 + ModernConfigManager.ammoCounterX;
        int posY = 128 + ModernConfigManager.ammoCounterY;
        double scale = ModernConfigManager.ammoCounterSize;
        FontRenderer fontRenderer = mc.fontRenderer;

        GlStateManager.enableBlend();
        GlStateManager.pushMatrix();
        
        // Масштабирование и отрисовка позиции
        GlStateManager.translate(width - posX * scale, height - posY * scale, 0.0D);
        GlStateManager.scale(scale, scale, scale);
        
        mc.getTextureManager().bindTexture(AMMO_TEX);

        // Получение данных о магазине и патронах
        ItemMagazine magazine = (ItemMagazine) WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);
        int totalCapacity = (magazine != null) ? magazine.getCapacity() : weaponInstance.getWeapon().getAmmoCapacity();
        
        String totalCapaString = (totalCapacity == 0) ? "-" : String.valueOf(totalCapacity);
        String currentAmmo = (totalCapacity == 0) ? "-" : String.valueOf(weaponInstance.getAmmo());

        // Отрисовка фона, если включен в конфиге
        if (ModernConfigManager.enableAmmoCounterBackground) {
            drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, 256, 52, 256.0F, 256.0F);
        }

        // Название оружия
        String weaponName = (new TextComponentTranslation(LangTools.formatName(weaponInstance.getWeapon().getTranslationKey()))).getFormattedText();
        drawScaledString(fontRenderer, weaponName, (126 - fontRenderer.getStringWidth(weaponName)), -fontRenderer.FONT_HEIGHT, 2.0D, 0xFECECE);

        // Отрисовка счетчика патронов
        if (!BalancePackManager.isWeaponDisabled(weaponInstance.getWeapon())) {
            String bottomString = String.format("  %s  | %s%s", TextFormatting.GRAY, TextFormatting.WHITE, totalCapaString);
            
            // Размер шрифта и координаты подобраны под текстуру
            drawScaledString(fontRenderer, currentAmmo, 84.0D - fontRenderer.getStringWidth(currentAmmo) * 2.0D, 5.5D, 3.5D, 0xFECECE);
            drawScaledString(fontRenderer, bottomString, 64.0D, 6.625D, 3.0D, 0xFFFFFF);
        }

        GlStateManager.popMatrix();
    }

    private static void drawScaledString(FontRenderer fr, String str, double x, double y, double scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0.0D);
        GlStateManager.scale(scale, scale, scale);
        fr.drawString(str, 0, 0, color);
        GlStateManager.popMatrix();
    }
}