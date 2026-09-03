package efw.mixin;

import com.tictim.paraglider.item.ItemParaglider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {ItemParaglider.class}, remap = false)
public class ParagliderCrawlingFixMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] ParagliderCrawlingFixMixin class loaded!");
    }

    /**
     * РћС‚РєР»СЋС‡Р°РµС‚ СЂР°Р±РѕС‚Сѓ РїР°СЂР°РїР»Р°РЅР° РЅР° РєР»РёРµРЅС‚Рµ, РµСЃР»Рё РёРіСЂРѕРє РїРѕР»Р·РµС‚.
     */
    @Inject(method = {"isParagliderOnClient"}, at = {@At("HEAD")}, cancellable = true)
    private static void stopParagliderWhileCrawling(EntityPlayer player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        // Р•СЃР»Рё РІС‹СЃРѕС‚Р° РёРіСЂРѕРєР° РјРµРЅСЊС€Рµ 1.0 (СЃС‚Р°РЅРґР°СЂС‚ РґР»СЏ РїРѕР»Р·Р°РЅРёСЏ), РІРѕР·РІСЂР°С‰Р°РµРј false
        if (player.height < 1.0F) {
            cir.setReturnValue(false);
        }
    }

    /**
     * РћС‚РєР»СЋС‡Р°РµС‚ СЂР°Р±РѕС‚Сѓ РїР°СЂР°РїР»Р°РЅР° РЅР° СЃРµСЂРІРµСЂРµ, РµСЃР»Рё РёРіСЂРѕРє РїРѕР»Р·РµС‚.
     */
    @Inject(method = {"isParagliderOn(Lnet/minecraft/entity/player/EntityPlayer;)Z"}, at = {@At("HEAD")}, cancellable = true)
    private static void stopParagliderServerWhileCrawling(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (player.height < 1.0F) {
            cir.setReturnValue(false);
        }
    }
}