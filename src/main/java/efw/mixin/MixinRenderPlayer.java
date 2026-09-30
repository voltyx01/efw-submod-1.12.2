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
        efw$firstPersonPassStarted = FirstPersonMode.isFirstPersonAttackActive(player);
        if (efw$firstPersonPassStarted) {
            efw$previousFirstPersonPass = FirstPersonMode.isFirstPersonPass();
            FirstPersonMode.setFirstPersonPass(true);
        }
    }

    @Inject(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At("RETURN"))
    private void efw$endFirstPersonAttackPass(AbstractClientPlayer player, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (efw$firstPersonPassStarted) {
            FirstPersonMode.setFirstPersonPass(efw$previousFirstPersonPass);
            efw$firstPersonPassStarted = false;
        }
    }

    @Inject(method = "setModelVisibilities(Lnet/minecraft/client/entity/AbstractClientPlayer;)V", at = @At("RETURN"))
    private void efw$showOnlyFirstPersonAttackArms(AbstractClientPlayer player, CallbackInfo ci) {
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

    @Inject(method = "renderLeftArm", at = @At("RETURN"))
    private void onRenderLeftArm(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
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

        // Apply body-level world-space transform for rolls and Emotecraft/BetterCombat action animations
        boolean isRoll = ap.isRollActive(partialTicks);
        boolean isBetterCombatAttack = ap.hasActionWeight() && ap.isActionAttack();
        boolean isEmoteAction = ap.hasActionWeight() && ap.getActionClip() != null && ap.getActionClip().isEmotecraft && !isBetterCombatAttack;
        boolean isFP = FirstPersonMode.isFirstPersonPass() && isBetterCombatAttack;

        if (!isRoll && !isEmoteAction && !isBetterCombatAttack) return;

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
        }

        Vec3f pos = isRoll
                ? ap.getRollLayerTransform("body", TransformType.POSITION, partialTicks)
                : (isEmoteAction ? ap.getActionLayerTransform("body", TransformType.POSITION, partialTicks) : Vec3f.ZERO);
        Vec3f rot = isRoll
                ? ap.getRollLayerTransform("body", TransformType.ROTATION, partialTicks)
                : ap.get3DTransform("body", TransformType.ROTATION, partialTicks, Vec3f.ZERO);

        // BetterCombat attack clips store torso Y for local model-space animation,
        // NOT for moving the entire player entity up and down in world space.
        // Applying posY from the layer stack (which includes baseLayer walking/running/breathing bobbing)
        // caused the entire player model to shift up and down ("breathe"/jump).
        // For BetterCombat clips we zero out the world position translation completely!
        float posY = isBetterCombatAttack ? 0.0f : pos.getY();
        float posX = isBetterCombatAttack ? 0.0f : pos.getX();
        float posZ = isBetterCombatAttack ? 0.0f : pos.getZ();
        float rotX = rot.getX();
        float rotY = rot.getY();
        float rotZ = rot.getZ();

        // Only apply if there's meaningful transform (skip if nearly zero)
        boolean hasRot = Math.abs(rotX) > 0.001f || Math.abs(rotY) > 0.001f || Math.abs(rotZ) > 0.001f;
        boolean hasPos = Math.abs(posX) > 0.001f || Math.abs(posY) > 0.001f || Math.abs(posZ) > 0.001f;
        if (!hasRot && !hasPos) return;

        // Pivot at waist (0.7 blocks up from feet), matching 1.20.1 setupRotations.
        GlStateManager.translate(posX, posY + 0.7f, posZ);

        rotX = (float) Math.toDegrees(rotX);
        float rotYDeg = (float) Math.toDegrees(rotY);
        rotZ = (float) Math.toDegrees(rotZ);

        GlStateManager.rotate(rotZ, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(rotYDeg, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(rotX, 1.0f, 0.0f, 0.0f);

        GlStateManager.translate(0.0f, -0.7f, 0.0f);
    }
}
