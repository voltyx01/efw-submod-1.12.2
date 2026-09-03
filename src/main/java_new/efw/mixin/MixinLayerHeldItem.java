package efw.mixin;

import com.paneedah.weaponlib.Weapon;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinLayerHeldItem class loaded!");
    }

    @Inject(method = "renderHeldItem", at = @At("HEAD"), cancellable = true)
    private void onRenderHeldItemPre(EntityLivingBase entityLivingBaseIn, ItemStack stack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, CallbackInfo ci) {
        if (entityLivingBaseIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
            ItemStack mainStack = player.getHeldItemMainhand();
            boolean isHoldingWeapon = (mainStack != null && !mainStack.isEmpty() && mainStack.getItem() instanceof Weapon);
            EnumHandSide offhandSide = (player.getPrimaryHand() == EnumHandSide.RIGHT) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;

            // Р•СЃР»Рё РёРіСЂРѕРє РґРµСЂР¶РёС‚ РѕСЂСѓР¶РёРµ MWC РІ РѕСЃРЅРѕРІРЅРѕР№ СЂСѓРєРµ, РїСЂРµРґРјРµС‚ РІРѕ РІС‚РѕСЂРѕР№ СЂСѓРєРµ СЃРєСЂС‹РІР°РµС‚СЃСЏ
            if (isHoldingWeapon && handSide == offhandSide) {
                ci.cancel();
                return;
            }

            // РћСЂСѓР¶РёРµ MWC РІРѕ РІС‚РѕСЂРѕР№ СЂСѓРєРµ РЅРёРєРѕРіРґР° РЅРµ СЂРµРЅРґРµСЂРёС‚СЃСЏ СЃС‚Р°РЅРґР°СЂС‚РЅС‹Рј СЃР»РѕРµРј
            if (stack != null && !stack.isEmpty() && stack.getItem() instanceof Weapon && handSide == offhandSide) {
                ci.cancel();
            }
        }
    }
}