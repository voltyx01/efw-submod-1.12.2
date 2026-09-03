package efw.mixin;

import com.paneedah.weaponlib.Weapon;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinItemRenderer class loaded!");
    }

    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At("HEAD"), cancellable = true)
    private void onRenderItemInFirstPerson(AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand, float swingProgress, ItemStack stack, float equipProgress, CallbackInfo ci) {
        if (hand == EnumHand.OFF_HAND && player != null) {
            ItemStack mainStack = player.getHeldItemMainhand();
            if (mainStack != null && !mainStack.isEmpty() && mainStack.getItem() instanceof Weapon) {
                // РџСЂСЏС‡РµРј РїСЂРµРґРјРµС‚ РІ Р»РµРІРѕР№ СЂСѓРєРµ РІ РІРёРґРµ РѕС‚ РїРµСЂРІРѕРіРѕ Р»РёС†Р° РїСЂРё РѕСЂСѓР¶РёРё MWC РІ РїСЂР°РІРѕР№ СЂСѓРєРµ
                ci.cancel();
            }
        }
    }
}
