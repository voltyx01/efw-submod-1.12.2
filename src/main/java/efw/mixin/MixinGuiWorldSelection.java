package efw.mixin;

import com.voltyx.mwccf.client.loading.ItemLoadingScreenRenderer;
import com.voltyx.mwccf.client.loading.LoadingScreenGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiListWorldSelectionEntry.class)
public class MixinGuiWorldSelection {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinGuiWorldSelection class loaded!");
    }

    @Inject(method = "loadWorld", at = @At("HEAD"))
    private void onLoadWorld(CallbackInfo ci) {
        ItemLoadingScreenRenderer.pickRandom();
        ItemLoadingScreenRenderer.preloadTexture();
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen(new LoadingScreenGui());
        // Р¤РѕСЂСЃРёСЂСѓРµРј РѕРґРёРЅ РєР°РґСЂ С‡С‚РѕР±С‹ GUI СѓСЃРїРµР» РѕС‚СЂРµРЅРґРµСЂРёС‚СЊСЃСЏ РґРѕ Р·Р°РІРёСЃР°РЅРёСЏ
        if (mc.currentScreen != null) {
            net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc);
            mc.currentScreen.setWorldAndResolution(mc, sr.getScaledWidth(), sr.getScaledHeight());
            ItemLoadingScreenRenderer.render(sr.getScaledWidth(), sr.getScaledHeight(), "", "");
            mc.updateDisplay();
        }
    }
}