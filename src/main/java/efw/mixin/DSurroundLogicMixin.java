package efw.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = {"org.orecruncher.dsurround.client.weather.Weather"})
public abstract class DSurroundLogicMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] DSurroundLogicMixin class loaded!");
    }

    /**
     * РџСЂРёРЅСѓРґРёС‚РµР»СЊРЅРѕ СѓСЃС‚Р°РЅР°РІР»РёРІР°РµС‚ РёРЅС‚РµРЅСЃРёРІРЅРѕСЃС‚СЊ РїРѕРіРѕРґС‹ РІ 0.0, 
     * РµСЃР»Рё РІС‹Р·РѕРІ РёРґРµС‚ РёР· WeaponLib.
     */
    @Inject(
        method = {"getIntensityLevel"}, 
        at = {@At("HEAD")}, 
        cancellable = true, 
        remap = false
    )
    private static void forceZeroIntensity(CallbackInfoReturnable<Float> cir) {
        if (isCalledByMWC()) {
            cir.setReturnValue(0.0F);
        }
    }
  
    /**
     * РџСЂРѕРІРµСЂСЏРµС‚ СЃС‚РµРє РІС‹Р·РѕРІРѕРІ, С‡С‚РѕР±С‹ РѕРїСЂРµРґРµР»РёС‚СЊ, РёРЅРёС†РёРёСЂРѕРІР°РЅ Р»Рё РїСЂРѕС†РµСЃСЃ 
     * РєР»Р°СЃСЃРѕРј CompatibleWorldRenderer РёР· WeaponLib.
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