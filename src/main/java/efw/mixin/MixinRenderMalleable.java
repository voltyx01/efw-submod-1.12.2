package efw.mixin;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderLivingBase.class)
public abstract class MixinRenderMalleable {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinRenderMalleable class loaded!");
    }

    // РџРѕР·РІРѕР»СЏРµС‚ РЅР°Рј Р±РµР·РѕРїР°СЃРЅРѕ РІС‹Р·С‹РІР°С‚СЊ Р·Р°С‰РёС‰РµРЅРЅС‹Р№ РјРµС‚РѕРґ РІРЅСѓС‚СЂРё РјРёРєСЃРёРЅР°
    @Shadow
    protected abstract boolean setBrightness(EntityLivingBase entity, float partialTicks, boolean combineTextures);

    // Р’РЅРµРґСЂСЏРµРјСЃСЏ РІ РјРµС‚РѕРґ, РєРѕС‚РѕСЂС‹Р№ Р·Р°РїСѓСЃРєР°РµС‚ РѕРєСЂР°С€РёРІР°РЅРёРµ РјРѕР±Р°
    @Redirect(method = "setDoRenderBrightness", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;setBrightness(Lnet/minecraft/entity/EntityLivingBase;FZ)Z"))
    private boolean disableDamageFlashGlobally(RenderLivingBase<?> instance, EntityLivingBase entity,
            float partialTicks, boolean combineTextures) {

        // 1. РЎРѕС…СЂР°РЅСЏРµРј РѕСЂРёРіРёРЅР°Р»СЊРЅС‹Рµ С‚Р°Р№РјРµСЂС‹ СѓСЂРѕРЅР°
        int oldHurt = entity.hurtTime;
        int oldDeath = entity.deathTime;

        // 2. Р’СЂРµРјРµРЅРЅРѕ РѕР±РЅСѓР»СЏРµРј РёС….
        // РўРµРїРµСЂСЊ Р’РЎР• СЂРµРЅРґРµСЂС‹ (РІРєР»СЋС‡Р°СЏ РєР°СЃС‚РѕРјРЅС‹Р№ СЂРµРЅРґРµСЂ РїР°СЂР°Р·РёС‚РѕРІ) Р±СѓРґСѓС‚ РґСѓРјР°С‚СЊ, С‡С‚Рѕ РјРѕР±
        // Р·РґРѕСЂРѕРІ.
        entity.hurtTime = 0;
        entity.deathTime = 0;

        // 3. Р’С‹Р·С‹РІР°РµРј РЅР°СЃС‚РѕСЏС‰РёР№ РјРµС‚РѕРґ РѕС‚СЂРёСЃРѕРІРєРё (РєСЂР°СЃРЅС‹Р№ С„Р»РµС€ РїСЂРё СЌС‚РѕРј РїСЂРѕРёРіРЅРѕСЂРёСЂСѓРµС‚СЃСЏ)
        boolean result = this.setBrightness(entity, partialTicks, combineTextures);

        // 4. РњРіРЅРѕРІРµРЅРЅРѕ РІРѕР·РІСЂР°С‰Р°РµРј С‚Р°Р№РјРµСЂС‹ РѕР±СЂР°С‚РЅРѕ, С‡С‚РѕР±С‹ РЅРµ СЃР»РѕРјР°С‚СЊ РјРµС…Р°РЅРёРєСѓ РёРіСЂС‹ Рё
        // Р»РѕРіРёРєСѓ СЃРјРµСЂС‚Рё
        entity.hurtTime = oldHurt;
        entity.deathTime = oldDeath;

        return result;
    }
}