package efw.mixin;

import com.voltyx.mwccf.client.loading.ItemLoadingScreenRenderer;
import com.voltyx.mwccf.client.loading.LoadingScreenGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinGuiConnecting class loaded!");
    }

    // РџРµСЂРµС…РІР°С‚С‹РІР°РµРј РёРЅРёС†РёР°Р»РёР·Р°С†РёСЋ GuiConnecting вЂ” СЌС‚Рѕ РјРѕРјРµРЅС‚ РїРѕРґРєР»СЋС‡РµРЅРёСЏ Рє СЃРµСЂРІРµСЂСѓ
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        ItemLoadingScreenRenderer.pickRandom();
        ItemLoadingScreenRenderer.preloadTexture();
        Minecraft.getMinecraft().displayGuiScreen(new LoadingScreenGui());
    }
}
