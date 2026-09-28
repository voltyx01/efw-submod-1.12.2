package efw.mixin;

import com.paneedah.weaponlib.Weapon;
import efw.animation.AnimationPlayer;
import efw.animation.AnimationRegistry;
import efw.animation.firstperson.FirstPersonMode;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem {
    @Shadow @Final
    protected net.minecraft.client.renderer.entity.RenderLivingBase<?> livingEntityRenderer;

    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinLayerHeldItem class loaded!");
    }

    @org.spongepowered.asm.mixin.Unique
    private static final ThreadLocal<EntityLivingBase> efw$currentRenderingEntity = new ThreadLocal<>();

    @Inject(method = "doRenderLayer", at = @At("HEAD"))
    private void efw$captureEntity(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        efw$currentRenderingEntity.set(entity);
    }

    @Inject(method = "doRenderLayer", at = @At("RETURN"))
    private void efw$releaseEntity(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        efw$currentRenderingEntity.remove();
    }



    @Inject(method = "renderHeldItem", at = @At("HEAD"), cancellable = true)
    private void onRenderHeldItemPre(EntityLivingBase entityLivingBaseIn, ItemStack stack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, CallbackInfo ci) {
        if (efw.util.RenderContext.isRenderingPlayerInSevenScreen) {
            ci.cancel();
            return;
        }

        if (entityLivingBaseIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
            if (FirstPersonMode.isFirstPersonPass() && player == Minecraft.getMinecraft().player) {
                AnimationPlayer animationPlayer = AnimationRegistry.getPlayer(player);
                boolean showOtherHand = efw.biomeinfo.MwccfConfig.betterCombat.isShowingOtherHandFirstPerson
                        || animationPlayer.getAnimatedHand() == net.bettercombat.logic.AnimatedHand.DUAL_HANDED;
                EnumHandSide attackSide = animationPlayer.getAnimatedHand() == net.bettercombat.logic.AnimatedHand.OFF_HAND
                        ? player.getPrimaryHand().opposite() : player.getPrimaryHand();
                if (!showOtherHand && handSide != attackSide) {
                    ci.cancel();
                    return;
                }
            }
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

    @Inject(method = "translateToHand", at = @At("HEAD"), cancellable = true)
    private void efw$translateToHandFirstPerson(EnumHandSide handSide, CallbackInfo ci) {
        if (FirstPersonMode.isFirstPersonPass() && this.livingEntityRenderer.getMainModel() instanceof ModelBiped) {
            ModelBiped model = (ModelBiped) this.livingEntityRenderer.getMainModel();
            ModelRenderer arm = (handSide == EnumHandSide.LEFT) ? model.bipedLeftArm : model.bipedRightArm;
            // In Minecraft's biped model space, +Z = front of player (face direction).
            // The animation sets rotationPointZ to positive values when the arm extends forward
            // (e.g. z=2.158 in two_handed_slash_vertical_right windup).
            // Use the positive Z as-is so the weapon appears in front of the camera in first-person.
            GlStateManager.translate(arm.rotationPointX * 0.0625F, arm.rotationPointY * 0.0625F, arm.rotationPointZ * 0.0625F);
            if (arm.rotateAngleZ != 0.0F) GlStateManager.rotate(arm.rotateAngleZ * (180.0F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
            if (arm.rotateAngleY != 0.0F) GlStateManager.rotate(arm.rotateAngleY * (180.0F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            if (arm.rotateAngleX != 0.0F) GlStateManager.rotate(arm.rotateAngleX * (180.0F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
            ci.cancel();
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
                Vec3f pos = ap.get3DTransform(boneName, TransformType.POSITION, pt, Vec3f.ZERO);
                Vec3f rot = ap.get3DTransform(boneName, TransformType.ROTATION, pt, Vec3f.ZERO);
                if (pos != null && (pos.getX() != 0.0F || pos.getY() != 0.0F || pos.getZ() != 0.0F)) {
                    GlStateManager.translate(pos.getX() * 0.0625F, pos.getY() * 0.0625F, pos.getZ() * 0.0625F);
                }
                if (rot != null && (rot.getX() != 0.0F || rot.getY() != 0.0F || rot.getZ() != 0.0F)) {
                    if (rot.getZ() != 0.0F) GlStateManager.rotate((float) Math.toDegrees(rot.getZ()), 0.0F, 0.0F, 1.0F);
                    if (rot.getY() != 0.0F) GlStateManager.rotate((float) Math.toDegrees(rot.getY()), 0.0F, 1.0F, 0.0F);
                    if (rot.getX() != 0.0F) GlStateManager.rotate((float) Math.toDegrees(rot.getX()), 1.0F, 0.0F, 0.0F);
                }
            }
        }
    }
}