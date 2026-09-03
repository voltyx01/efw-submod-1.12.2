package efw.mixin;

import com.paneedah.weaponlib.Weapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {Weapon.class}, remap = false)
public class MWCSmokeOffsetMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MWCSmokeOffsetMixin class loaded!");
    }

    /**
     * РџРµСЂРµС…РІР°С‚С‹РІР°РµС‚ РїРѕР»СѓС‡РµРЅРёРµ РїРѕР·РёС†РёРё РґСѓР»Р° РѕСЂСѓР¶РёСЏ, С‡С‚РѕР±С‹ СЃРєРѕСЂСЂРµРєС‚РёСЂРѕРІР°С‚СЊ РµС‘ РїСЂРё РїРѕР»Р·Р°РЅРёРё.
     */
    @Inject(method = {"getMuzzlePosition"}, at = {@At("RETURN")}, cancellable = true)
    private void offsetMuzzleWhileCrawling(CallbackInfoReturnable<Vec3d> cir) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            applyCrawlingOffset(cir);
        }
    }
  
    private void applyCrawlingOffset(CallbackInfoReturnable<Vec3d> cir) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        
        // player.height (field_70131_O) РѕР±С‹С‡РЅРѕ РјРµРЅСЊС€Рµ 1.0F, РєРѕРіРґР° РёРіСЂРѕРє РїРѕР»Р·РµС‚
        if (player != null && player.height < 1.0F) {
            Vec3d pos = cir.getReturnValue();
            
            // РЎРјРµС‰Р°РµРј РїРѕР·РёС†РёСЋ РґСѓР»Р° РЅР° 1 Р±Р»РѕРє РІРЅРёР·, С‡С‚РѕР±С‹ РѕРЅР° СЃРѕРѕС‚РІРµС‚СЃС‚РІРѕРІР°Р»Р° РїРѕР»РѕР¶РµРЅРёСЋ РёРіСЂРѕРєР° РЅР° Р·РµРјР»Рµ
            cir.setReturnValue(new Vec3d(pos.x, pos.y - 1.0D, pos.z));
        } 
    }
}