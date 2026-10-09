package efw.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import efw.animation.firstperson.FirstPersonMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import efw.animation.AnimationPlayer;
import efw.animation.layered.math.Vec3f;
import efw.animation.layered.TransformType;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {
    @Unique
    private boolean efw$firstPersonPassStarted;
    @Unique
    private boolean efw$previousFirstPersonPass;

    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinRenderPlayer class loaded!");
    }

    @Inject(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At("HEAD"))
    private void efw$beginFirstPersonAttackPass(AbstractClientPlayer player, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        com.voltyx.mwccf.blood.BloodTextureManager.setRenderingPlayer(player);
        if (efw.AnimationTickHandler.isBetterCombatAttackActive(player)) {
            efw.util.RenderContext.suppressedSneakEntity = player;
        } else if (efw.util.RenderContext.suppressedSneakEntity == player) {
            efw.util.RenderContext.suppressedSneakEntity = null;
        }
        efw$firstPersonPassStarted = FirstPersonMode.isFirstPersonAttackActive(player);
        if (efw$firstPersonPassStarted) {
            efw$previousFirstPersonPass = FirstPersonMode.isFirstPersonPass();
            FirstPersonMode.setFirstPersonPass(true);
        }
    }

    @Inject(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At("RETURN"))
    private void efw$endFirstPersonAttackPass(AbstractClientPlayer player, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        com.voltyx.mwccf.blood.BloodTextureManager.clearRenderingPlayer();
        if (efw.util.RenderContext.suppressedSneakEntity == player) {
            efw.util.RenderContext.suppressedSneakEntity = null;
        }
        if (efw$firstPersonPassStarted) {
            FirstPersonMode.setFirstPersonPass(efw$previousFirstPersonPass);
            efw$firstPersonPassStarted = false;
        }
    }

    @Redirect(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isSneaking()Z"))
    private boolean efw$suppressSneakInDoRender(AbstractClientPlayer player) {
        if (efw.AnimationTickHandler.isBetterCombatAttackActive(player)) {
            return false;
        }
        return player.isSneaking();
    }

    @Redirect(method = "setModelVisibilities(Lnet/minecraft/client/entity/AbstractClientPlayer;)V",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isSneaking()Z"))
    private boolean efw$suppressSneakInModelVisibilities(AbstractClientPlayer player) {
        if (efw.AnimationTickHandler.isBetterCombatAttackActive(player)) {
            return false;
        }
        return player.isSneaking();
    }

    @Inject(method = "setModelVisibilities(Lnet/minecraft/client/entity/AbstractClientPlayer;)V", at = @At("RETURN"))
    private void efw$showOnlyFirstPersonAttackArms(AbstractClientPlayer player, CallbackInfo ci) {
        if (efw.util.RenderContext.isBlinkConfiguring) {
            ModelPlayer model = ((RenderPlayer) (Object) this).getMainModel();
            model.bipedHeadwear.showModel = false;
        }

        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()
                && player == com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.getCurrentCarrier()) {
            ModelPlayer model = ((RenderPlayer) (Object) this).getMainModel();
            model.setVisible(false);
            model.bipedRightArm.showModel = true;
            model.bipedLeftArm.showModel = true;
            model.bipedRightArmwear.showModel = player.isWearing(net.minecraft.entity.player.EnumPlayerModelParts.RIGHT_SLEEVE);
            model.bipedLeftArmwear.showModel = player.isWearing(net.minecraft.entity.player.EnumPlayerModelParts.LEFT_SLEEVE);
            return;
        }

        if (!FirstPersonMode.isFirstPersonPass()) return;

        ModelPlayer model = ((RenderPlayer) (Object) this).getMainModel();
        model.setVisible(false);
        boolean showArms = efw.biomeinfo.MwccfConfig.betterCombat.isShowingArmsInFirstPerson;
        AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(player);
        boolean showOtherHand = ap != null && (efw.biomeinfo.MwccfConfig.betterCombat.isShowingOtherHandFirstPerson
                || ap.getAnimatedHand() == net.bettercombat.logic.AnimatedHand.DUAL_HANDED);
        net.minecraft.util.EnumHandSide attackSide = (ap != null && ap.getAnimatedHand() == net.bettercombat.logic.AnimatedHand.OFF_HAND)
                ? player.getPrimaryHand().opposite() : player.getPrimaryHand();

        boolean showRightArm = showArms && (attackSide == net.minecraft.util.EnumHandSide.RIGHT || showOtherHand);
        boolean showLeftArm = showArms && (attackSide == net.minecraft.util.EnumHandSide.LEFT || showOtherHand);

        model.bipedRightArm.showModel = showRightArm;
        model.bipedLeftArm.showModel = showLeftArm;
        model.bipedRightArmwear.showModel = showRightArm && player.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
        model.bipedLeftArmwear.showModel = showLeftArm && player.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
    }

    @Inject(method = "renderRightArm", at = @At("HEAD"))
    private void onRenderRightArmHead(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        com.voltyx.mwccf.blood.BloodTextureManager.setRenderingPlayer(clientPlayer);
    }

    @Inject(method = "renderRightArm", at = @At("RETURN"))
    private void onRenderRightArmReturn(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        com.voltyx.mwccf.blood.BloodTextureManager.clearRenderingPlayer();
    }

    @Inject(method = "renderLeftArm", at = @At("HEAD"))
    private void onRenderLeftArmHead(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        com.voltyx.mwccf.blood.BloodTextureManager.setRenderingPlayer(clientPlayer);
    }

    @Inject(method = "renderLeftArm", at = @At("RETURN"))
    private void onRenderLeftArm(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        com.voltyx.mwccf.blood.BloodTextureManager.clearRenderingPlayer();
        if (com.voltyx.mwccf.geo.BraceletUI.hasBraceletEquipped(clientPlayer)) {
            boolean isSlim = "slim".equals(clientPlayer.getSkinType());
            com.voltyx.mwccf.geo.GeoArmorModel bracelet = null;

            if (isSlim) {
                bracelet = com.voltyx.mwccf.geo.BraceletInspectHandler.getSlimModel();
            } else {
                bracelet = com.voltyx.mwccf.geo.BraceletInspectHandler.getNormalModel();
            }

            if (bracelet != null) {
                GlStateManager.pushMatrix();
                Minecraft.getMinecraft().getTextureManager().bindTexture(com.voltyx.mwccf.geo.BraceletInspectHandler.getBraceletTexture());
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                bracelet.bipedLeftArm.rotateAngleX = 0.0F;
                bracelet.bipedLeftArm.rotateAngleY = 0.0F;
                bracelet.bipedLeftArm.rotateAngleZ = 0.0F;
                
                net.minecraft.client.model.ModelPlayer model = ((RenderPlayer)(Object)this).getMainModel();
                bracelet.bipedLeftArm.rotationPointX = model.bipedLeftArm.rotationPointX;
                bracelet.bipedLeftArm.rotationPointY = model.bipedLeftArm.rotationPointY;
                bracelet.bipedLeftArm.rotationPointZ = model.bipedLeftArm.rotationPointZ;

                GlStateManager.disableCull();
                bracelet.bipedLeftArm.render(0.0625F);
                GlStateManager.enableCull();
                GlStateManager.popMatrix();
            }
        }
    }

    @Inject(method = "applyRotations", at = @At("RETURN"))
    protected void applyRotations(AbstractClientPlayer entityLiving, float p_77043_2_, float rotationYaw, float partialTicks, CallbackInfo ci) {
        if (efw.util.RenderContext.isRenderingPlayerInGui || efw.util.RenderContext.isRenderingPlayerInSevenScreen) return;
        AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(entityLiving);
        if (ap == null || !ap.isActive()) return;

        // Apply body-level world-space transform for rolls and Emotecraft action animations
        boolean isRoll = ap.isRollActive(partialTicks);
        boolean isBetterCombatAttack = ap.hasActionWeight() && ap.isActionAttack();
        boolean isEmoteAction = ap.hasActionWeight() && ap.getActionClip() != null && ap.getActionClip().isEmotecraft && !isBetterCombatAttack;
        boolean isFP = FirstPersonMode.isFirstPersonPass() && isBetterCombatAttack;

        if (isFP) {
            float eyeHeight = entityLiving.getEyeHeight();
            // 1. Cancel eye height offset and camera pitch in camera view space,
            // bringing the matrix to the camera eye with identity view orientation.
            GlStateManager.translate(0.0F, eyeHeight, 0.0F);

            float pitch = entityLiving.prevRotationPitch + (entityLiving.rotationPitch - entityLiving.prevRotationPitch) * partialTicks;
            GlStateManager.rotate(-pitch, 1.0F, 0.0F, 0.0F);

            // 2. Align horizontal entity yaw to camera headYaw
            float headYaw = entityLiving.prevRotationYawHead + (entityLiving.rotationYawHead - entityLiving.prevRotationYawHead) * partialTicks;
            GlStateManager.rotate(rotationYaw - headYaw, 0.0F, 1.0F, 0.0F);

            // 3. First-person view offset:
            // Shift the player model forward into view (-Z) so the weapon hilt and arm are in front of the camera,
            // not inside the chest/chin. Raise (+Y) slightly for natural first-person sightlines.
            float fpForward = 0.35F;
            float fpUp = 0.15F;
            GlStateManager.translate(0.0F, -eyeHeight + fpUp, -fpForward);
            return;
        }

        if (!isRoll && !isEmoteAction && !isBetterCombatAttack) return;

        Vec3f pos;
        Vec3f rot;

        if (isRoll) {
            pos = ap.getRollLayerTransform("body", TransformType.POSITION, partialTicks);
            rot = ap.getRollLayerTransform("body", TransformType.ROTATION, partialTicks);
        } else if (isBetterCombatAttack) {
            // For Better Combat attacks, get rotation strictly from actionLayer (isolated from base walk/run layers).
            // This ensures that when the attack ends and fades out to ZERO, it blends smoothly with 0 snap!
            pos = Vec3f.ZERO;
            rot = ap.getActionLayerTransform("body", TransformType.ROTATION, partialTicks);
            java.util.Optional<efw.animation.layered.modifier.AdjustmentModifier.PartModifier> adj = ap.applyAttackAdjustment("body", partialTicks);
            if (adj.isPresent()) {
                float fade = ap.getActionFadeAlpha(partialTicks);
                rot = rot.add(adj.get().rotation().scale(fade));
            }
        } else {
            pos = ap.getActionLayerTransform("body", TransformType.POSITION, partialTicks);
            rot = ap.get3DTransform("body", TransformType.ROTATION, partialTicks, Vec3f.ZERO);
        }

        float posY = pos.getY();
        float posX = pos.getX();
        float posZ = pos.getZ();
        float rotX = rot.getX();
        float rotY = rot.getY();
        float rotZ = rot.getZ();

        // Only apply if there's meaningful transform (skip if nearly zero)
        boolean hasRot = Math.abs(rotX) > 0.001f || Math.abs(rotY) > 0.001f || Math.abs(rotZ) > 0.001f;
        boolean hasPos = Math.abs(posX) > 0.001f || Math.abs(posY) > 0.001f || Math.abs(posZ) > 0.001f;
        if (!hasRot && !hasPos) return;

        // Pivot at waist (0.7 blocks up from feet), matching 1.20.1 setupRotations.
        float pivotY = 0.7f;
        GlStateManager.translate(posX, posY + pivotY, posZ);

        rotX = (float) Math.toDegrees(rotX);
        float rotYDeg = (float) Math.toDegrees(rotY);
        rotZ = (float) Math.toDegrees(rotZ);

        GlStateManager.rotate(rotZ, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(rotYDeg, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(rotX, 1.0f, 0.0f, 0.0f);

        GlStateManager.translate(0.0f, -pivotY, 0.0f);
    }
}
