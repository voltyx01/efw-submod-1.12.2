package efw.mixin;

import com.fuzs.aquaacrobatics.proxy.ClientProxy;
import com.tictim.paraglider.item.ItemParaglider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ClientProxy.class}, remap = false)
public class AquaAcrobaticsParagliderMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] AquaAcrobaticsParagliderMixin class loaded!");
    }

    /**
     * РџСЂРµРґРѕС‚РІСЂР°С‰Р°РµС‚ РІС‹РїРѕР»РЅРµРЅРёРµ Р»РѕРіРёРєРё РЅР°Р¶Р°С‚РёСЏ РєР»Р°РІРёС€Рё РІ Aqua Acrobatics, 
     * РµСЃР»Рё Р°РєС‚РёРІРµРЅ РїР°СЂР°РїР»Р°РЅ.
     */
    @Inject(method = {"onKeyPress"}, at = {@At("HEAD")}, cancellable = true)
    private static void cancelCrawlingOnParaglider(InputEvent.KeyInputEvent event, CallbackInfo ci) {
        // РџРѕР»СѓС‡Р°РµРј С‚РµРєСѓС‰РµРіРѕ РёРіСЂРѕРєР°
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        
        if (player != null) {
            // РџРѕР»СѓС‡Р°РµРј РїСЂРµРґРјРµС‚ РІ РіР»Р°РІРЅРѕР№ СЂСѓРєРµ
            ItemStack heldItem = player.getHeldItemMainhand();
            
            // РџСЂРѕРІРµСЂСЏРµРј: РїСЂРµРґРјРµС‚ РЅРµ РїСѓСЃС‚ Р СЏРІР»СЏРµС‚СЃСЏ РїР°СЂР°РїР»Р°РЅРѕРј
            if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemParaglider) {
                // Р•СЃР»Рё РїР°СЂР°РїР»Р°РЅ СЃРµР№С‡Р°СЃ Р°РєС‚РёРІРµРЅ (РёРіСЂРѕРє Р»РµС‚РёС‚), РѕС‚РјРµРЅСЏРµРј РѕР±СЂР°Р±РѕС‚РєСѓ СЃРѕР±С‹С‚РёСЏ РЅР°Р¶Р°С‚РёСЏ РєР»Р°РІРёС€Рё
                if (ItemParaglider.isParagliderOnClient((EntityPlayer) player, heldItem)) {
                    ci.cancel();
                }
            }
        }
    }
}