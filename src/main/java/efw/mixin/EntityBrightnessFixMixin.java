package efw.mixin;

import com.paneedah.mwc.network.handlers.MuzzleFlashMessageHandler;
import com.paneedah.mwc.network.messages.MuzzleFlashMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {MuzzleFlashMessageHandler.class}, remap = false)
public class EntityBrightnessFixMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] EntityBrightnessFixMixin class loaded!");
    }

    /**
     * РџРµСЂРµС…РІР°С‚С‹РІР°РµС‚ РѕР±СЂР°Р±РѕС‚РєСѓ СЃРµС‚РµРІРѕРіРѕ СЃРѕРѕР±С‰РµРЅРёСЏ Рѕ РІСЃРїС‹С€РєРµ РїСЂРё РІС‹СЃС‚СЂРµР»Рµ.
     * Р’РѕР·РІСЂР°С‰Р°СЏ null, РјС‹ РѕС‚РјРµРЅСЏРµРј СЃС‚Р°РЅРґР°СЂС‚РЅСѓСЋ Р»РѕРіРёРєСѓ РѕР±СЂР°Р±РѕС‚РєРё.
     */
    @Inject(
        method = {"onMessage"}, 
        at = {@At("HEAD")}, 
        cancellable = true
    )
    private void stopMuzzleFlashMessage(MuzzleFlashMessage muzzleFlashMessage, MessageContext messageContext, CallbackInfoReturnable<IMessage> cir) {
        // РџСЂРёРЅСѓРґРёС‚РµР»СЊРЅРѕ РІРѕР·РІСЂР°С‰Р°РµРј null, С‡С‚РѕР±С‹ РїСЂРµРґРѕС‚РІСЂР°С‚РёС‚СЊ РІС‹РїРѕР»РЅРµРЅРёРµ Р»РѕРіРёРєРё РІСЃРїС‹С€РєРё
        cir.setReturnValue(null);
    }
}