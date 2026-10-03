package efw.mixin;

import com.paneedah.weaponlib.Weapon;
import efw.animation.AnimationPlayer;
import efw.animation.AnimationRegistry;
import efw.animation.firstperson.FirstPersonMode;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinItemRenderer class loaded!");
    }

    @Shadow
    private float equippedProgressMainHand;
    @Shadow
    private float prevEquippedProgressMainHand;
    @Shadow
    private float equippedProgressOffHand;
    @Shadow
    private float prevEquippedProgressOffHand;

    /**
     * Hides the off-hand item in first-person view when holding an MWC weapon in main hand.
     */
    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At("HEAD"), cancellable = true)
    private void onRenderItemInFirstPerson(AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand, float swingProgress, ItemStack stack, float equipProgress, CallbackInfo ci) {
        com.voltyx.mwccf.blood.BloodTextureManager.setRenderingPlayer(player);
        if (FirstPersonMode.isFirstPersonAttackActive(player)) {
            // Cancel vanilla first-person item/hand rendering during Better Combat attacks.
            // Attack rendering is handled by the world-space player model pass (matching 1.20.1 playerAnim).
            ci.cancel();
            return;
        }

        if (hand == EnumHand.OFF_HAND && player != null) {
            ItemStack mainStack = player.getHeldItemMainhand();
            if (mainStack != null && !mainStack.isEmpty() && mainStack.getItem() instanceof Weapon) {
                // Прячем предмет в левой руке в виде от первого лица при оружии MWC в правой руке
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At("RETURN"))
    private void onRenderItemInFirstPersonReturn(AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand, float swingProgress, ItemStack stack, float equipProgress, CallbackInfo ci) {
        com.voltyx.mwccf.blood.BloodTextureManager.clearRenderingPlayer();
    }

    /**
     * Держим прогресс экипировки рук на нуле, пока кукла в руках или прячется.
     * Когда кукла полностью скрылась за экраном, обычная рука/предмет плавно поднимется снизу.
     */
    @Inject(method = "updateEquippedItem", at = @At("RETURN"))
    private void onUpdateEquippedItem(CallbackInfo ci) {
        if (com.voltyx.mwccf.render.doll.DollRenderer.isDollActive()) {
            this.equippedProgressMainHand = 0.0F;
            this.prevEquippedProgressMainHand = 0.0F;
            this.equippedProgressOffHand = 0.0F;
            this.prevEquippedProgressOffHand = 0.0F;
        }
    }
}
