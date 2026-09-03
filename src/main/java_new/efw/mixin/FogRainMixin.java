package efw.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public abstract class FogRainMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] FogRainMixin class loaded!");
    }

    /**
     * РџРµСЂРµРЅР°СЃС‚СЂР°РёРІР°РµС‚ РїР°СЂР°РјРµС‚СЂС‹ С‚СѓРјР°РЅР° РїСЂРё РґРѕР¶РґРµ, РµСЃР»Рё Р°РєС‚РёРІРµРЅ СЂРµРЅРґРµСЂРёРЅРі WeaponLib.
     */
    @Inject(method = {"setupFog"}, at = {@At("RETURN")})
    private void makeIntenseFogInScope(int startLayer, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        
        // РџСЂРѕРІРµСЂСЏРµРј РЅР°Р»РёС‡РёРµ РјРёСЂР° Рё РёРЅС‚РµРЅСЃРёРІРЅРѕСЃС‚СЊ РґРѕР¶РґСЏ
        if (mc.world != null && mc.world.getRainStrength(partialTicks) > 0.1F) {
            if (isCalledByMWC()) {
                float rain = mc.world.getRainStrength(partialTicks);
                
                // РќР°СЃС‚СЂРѕР№РєР° РїР°СЂР°РјРµС‚СЂРѕРІ С‚СѓРјР°РЅР° (GlStateManager)
                float fogStart = 0.0F;
                float fogEnd = 25.0F / rain * 2.0F;
                
                // РЈСЃС‚Р°РЅРѕРІРєР° РґРёСЃС‚Р°РЅС†РёРё РЅР°С‡Р°Р»Р° Рё РєРѕРЅС†Р° С‚СѓРјР°РЅР°
                GlStateManager.setFogStart(fogStart);
                GlStateManager.setFogEnd(fogEnd);
                
                // РЈСЃС‚Р°РЅРѕРІРєР° РїР»РѕС‚РЅРѕСЃС‚Рё (РїР°СЂР°РјРµС‚СЂ, Р·Р°РІРёСЃСЏС‰РёР№ РѕС‚ СЃРёР»С‹ РґРѕР¶РґСЏ)
                GlStateManager.setFogDensity(0.1F + rain * 0.4F);
            }
        }
    }
    
    /**
     * РџСЂРѕРІРµСЂСЏРµС‚ СЃС‚РµРє РІС‹Р·РѕРІРѕРІ РЅР° РЅР°Р»РёС‡РёРµ РІС‹Р·РѕРІР° РёР· WeaponLib.
     */
    private static boolean isCalledByMWC() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("com.paneedah.weaponlib.compatibility.CompatibleWorldRenderer")) {
                return true;
            }
        }
        return false;
    }
}