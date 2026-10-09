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

    @Inject(method = "renderItemInFirstPerson(F)V", at = @At("HEAD"))
    private void onRenderFirstPersonPassHead(float partialTicks, CallbackInfo ci) {
        com.teamderpy.shouldersurfing.client.FirstPersonFadeManager.getInstance().preRenderHand();
    }

    @Inject(method = "renderItemInFirstPerson(F)V", at = @At("RETURN"))
    private void onRenderFirstPersonPassReturn(float partialTicks, CallbackInfo ci) {
        com.teamderpy.shouldersurfing.client.FirstPersonFadeManager.getInstance().postRenderHand();
    }

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

    @Shadow
    private ItemStack itemStackMainHand;
    @Shadow
    private ItemStack itemStackOffHand;

    /**
     * Держим прогресс экипировки рук на нуле, пока кукла в руках или прячется.
     * Когда кукла полностью скрылась за экраном, обычная рука/предмет плавно поднимется снизу.
     *
     * Также предотвращаем постоянное дергание/опускание оружия вниз от первого лица,
     * когда на сервере или клиенте тикает NBT горящего оружия:
     * если предмет тот же самый (тот же Item, то же повреждение, слот не менялся),
     * мы синхронизируем сохраненный стек в ItemRenderer ДО проверки vanilla areItemStacksEqual,
     * чтобы ванильный код не считал, что в руку взят совершенно новый предмет,
     * но при этом ванильная анимация удара/свинга и смена слота продолжают нормально работать!
     */
    @Inject(method = "updateEquippedItem", at = @At("HEAD"))
    private void onUpdateEquippedItemHead(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        ItemStack heldMain = mc.player.getHeldItemMainhand();
        if (isSameFireWeapon(this.itemStackMainHand, heldMain)) {
            this.itemStackMainHand = heldMain;
        }

        ItemStack heldOff = mc.player.getHeldItemOffhand();
        if (isSameFireWeapon(this.itemStackOffHand, heldOff)) {
            this.itemStackOffHand = heldOff;
        }
    }

    @Inject(method = "updateEquippedItem", at = @At("RETURN"))
    private void onUpdateEquippedItemReturn(CallbackInfo ci) {
        if (Float.isNaN(this.equippedProgressMainHand)) this.equippedProgressMainHand = 0.0F;
        if (Float.isNaN(this.prevEquippedProgressMainHand)) this.prevEquippedProgressMainHand = 0.0F;
        if (Float.isNaN(this.equippedProgressOffHand)) this.equippedProgressOffHand = 0.0F;
        if (Float.isNaN(this.prevEquippedProgressOffHand)) this.prevEquippedProgressOffHand = 0.0F;
    }

    private static boolean isSameFireWeapon(ItemStack oldStack, ItemStack newStack) {
        if (oldStack == null || newStack == null) return false;
        if (oldStack.isEmpty() || newStack.isEmpty()) return false;
        if (oldStack.getItem() != newStack.getItem()) return false;
        if (oldStack.getItemDamage() != newStack.getItemDamage()) return false;
        return com.voltyx.mwccf.fireweapon.FireWeaponHelper.isWrapped(oldStack)
                || com.voltyx.mwccf.fireweapon.FireWeaponHelper.isWrapped(newStack);
    }
}
