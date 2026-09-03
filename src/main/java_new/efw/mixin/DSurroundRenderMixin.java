package efw.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = {"org.orecruncher.dsurround.client.renderer.weather.RenderWeather"})
public abstract class DSurroundRenderMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] DSurroundRenderMixin class loaded!");
    }

    /**
     * РџРѕР»РЅРѕСЃС‚СЊСЋ РѕС‚РјРµРЅСЏРµС‚ СЃС‚Р°РЅРґР°СЂС‚РЅС‹Р№ СЂРµРЅРґРµСЂРёРЅРі РїРѕРіРѕРґС‹ Dynamic Surroundings, 
     * РµСЃР»Рё РІС‹Р·РѕРІ РёРґРµС‚ РёР· WeaponLib.
     */
    @Inject(
        method = {"render"}, 
        at = {@At("HEAD")}, 
        cancellable = true, 
        remap = false
    )
    private void cancelDSurroundRender(CallbackInfo ci) {
        if (isCalledByMWC()) {
            ci.cancel();
        }
    }
  
    /**
     * РџСЂРѕРІРµСЂСЏРµС‚ СЃС‚РµРє РІС‹Р·РѕРІРѕРІ РЅР° РЅР°Р»РёС‡РёРµ РєР»Р°СЃСЃРѕРІ СЂРµРЅРґРµСЂРёРЅРіР° РёР· WeaponLib.
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