package efw.mixin;

import com.voltyx.mwccf.fireweapon.FireWeaponHelper;
import com.voltyx.mwccf.fireweapon.client.FireWeaponTextureManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class MixinRenderItem {

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V",
            at = @At("HEAD"), cancellable = true)
    private void fireweapon$onRenderItem(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        if (FireWeaponHelper.isWrapped(stack)) {
            if (FireWeaponTextureManager.renderWrappedItem((RenderItem) (Object) this, stack, model)) {
                ci.cancel();
            }
        }
    }
}
