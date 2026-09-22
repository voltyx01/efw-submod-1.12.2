package efw.mixin;

import com.paneedah.weaponlib.Weapon;
import efw.animation.AnimationPlayer;
import efw.animation.AnimationRegistry;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
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
        if (efw.util.RenderContext.isRenderingPlayerInSevenScreen) {
            ci.cancel();
            return;
        }

        if (entityLivingBaseIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
            ItemStack mainStack = player.getHeldItemMainhand();
            boolean isHoldingWeapon = (mainStack != null && !mainStack.isEmpty() && mainStack.getItem() instanceof Weapon);
            EnumHandSide offhandSide = (player.getPrimaryHand() == EnumHandSide.RIGHT) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;

            // Если игрок держит оружие MWC в основной руке, предмет во второй руке скрывается
            if (isHoldingWeapon && handSide == offhandSide) {
                ci.cancel();
                return;
            }

            // Оружие MWC во второй руке никогда не рендерится стандартным слоем
            if (stack != null && !stack.isEmpty() && stack.getItem() instanceof Weapon && handSide == offhandSide) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderHeldItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemSide(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"))
    private void onRenderItemSide(EntityLivingBase entityLivingBaseIn, ItemStack stack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, CallbackInfo ci) {
        if (entityLivingBaseIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
            AnimationPlayer ap = AnimationRegistry.getPlayer(player);
            if (ap != null) {
                String boneName = (handSide == EnumHandSide.RIGHT) ? "rightItem" : "leftItem";
                float pt = Minecraft.getMinecraft().getRenderPartialTicks();
                Vec3f rot = ap.get3DTransform(boneName, TransformType.ROTATION, pt, Vec3f.ZERO);
                Vec3f pos = ap.get3DTransform(boneName, TransformType.POSITION, pt, Vec3f.ZERO);
                if (rot != null && (Math.abs(rot.getX()) > 0.0001f || Math.abs(rot.getY()) > 0.0001f || Math.abs(rot.getZ()) > 0.0001f
                        || Math.abs(pos.getX()) > 0.0001f || Math.abs(pos.getY()) > 0.0001f || Math.abs(pos.getZ()) > 0.0001f)) {
                    GlStateManager.translate(pos.getX() * 0.0625F, pos.getY() * 0.0625F, pos.getZ() * 0.0625F);
                    GlStateManager.rotate((float) Math.toDegrees(rot.getZ()), 0, 0, 1);
                    GlStateManager.rotate((float) Math.toDegrees(rot.getY()), 0, 1, 0);
                    GlStateManager.rotate((float) Math.toDegrees(rot.getX()), 1, 0, 0);
                }
            }
        }
    }
}