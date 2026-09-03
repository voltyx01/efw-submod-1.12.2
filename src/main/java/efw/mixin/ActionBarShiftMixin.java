package efw.mixin;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({GuiIngame.class})
public abstract class ActionBarShiftMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] ActionBarShiftMixin class loaded!");
    }

    /**
     * РџРµСЂРµС…РІР°С‚С‹РІР°РµС‚ РІС‹Р·РѕРІ glTranslate РґР»СЏ РёР·РјРµРЅРµРЅРёСЏ РїРѕР·РёС†РёРё ActionBar.
     * РћР±С‹С‡РЅРѕРµ РёРјСЏ РјРµС‚РѕРґР° func_179109_b вЂ” СЌС‚Рѕ GlStateManager.translate(float x, float y, float z).
     */
    @Redirect(
        method = {"renderGameOverlay(F)V"}, 
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", 
            ordinal = 2
        )
    )
    private void redirectActionBarPosition(float x, float y, float z) {
        // Р’РµР»РёС‡РёРЅР° СЃРјРµС‰РµРЅРёСЏ РїР°РЅРµР»Рё РґРµР№СЃС‚РІРёР№ РІРІРµСЂС…
        float upwardShift = 10.0F;
        
        // Р’С‹Р·С‹РІР°РµРј РѕСЂРёРіРёРЅР°Р»СЊРЅС‹Р№ РјРµС‚РѕРґ СЃ РёР·РјРµРЅРµРЅРЅРѕР№ РєРѕРѕСЂРґРёРЅР°С‚РѕР№ Y
        GlStateManager.translate(x, y - upwardShift, z);
    }
}