package efw.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import efw.animation.AnimationPlayer;
import efw.animation.layered.math.Vec3f;
import efw.animation.layered.TransformType;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinRenderPlayer class loaded!");
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
        AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(entityLiving);
        if (ap == null) return;
        
        // Apply body-level world-space transform during roll AND its fade-out
        // This matches 1.20.1: get3DTransform("body") on the full blended stack
        String actionName = ap.getCurrentActionName();
        String baseName = ap.getCurrentAnimationName();
        String fadeActionName = ap.getFadeActionName();
        boolean isRollAction = "roll".equals(actionName) || (ap.isActionFadingOut() && "roll".equals(fadeActionName));
        if (!isRollAction) return;

        // Check if a weapon animation is involved (either as base or action)
        boolean hasWeapon = (actionName != null && (actionName.startsWith("pistol_") || actionName.startsWith("rifle_"))) ||
                            (baseName != null && (baseName.startsWith("pistol_") || baseName.startsWith("rifle_")));

        // Determine if the roll animation's global somersault is actively contributing
        boolean isRollActive = ap.isRollActive(partialTicks);

        if (hasWeapon && !isRollActive) {
            // Normal weapon animation without roll.
            // SKIP applying body transforms globally because weapons use "body" as the local torso, 
            // not the global entity root! (This prevents the character from flying up or twisting globally).
            return;
        }
        
        // Get from full stack (base + action layers blended)
        efw.animation.layered.math.Vec3f pos = ap.get3DTransform("body", efw.animation.layered.TransformType.POSITION, partialTicks, efw.animation.layered.math.Vec3f.ZERO);
        efw.animation.layered.math.Vec3f rot = ap.get3DTransform("body", efw.animation.layered.TransformType.ROTATION, partialTicks, efw.animation.layered.math.Vec3f.ZERO);
        
        if (hasWeapon && isRollActive) {
            // A roll is playing ON TOP of a weapon base layer!
            // The blended pos contains the weapon's massive local Y offset (e.g. Y = -4.5).
            // To prevent flying up during the roll, subtract the base layer's contribution,
            // isolating ONLY the roll's global position and rotation!
            efw.animation.layered.math.Vec3f basePos = ap.getBaseLayerTransform("body", efw.animation.layered.TransformType.POSITION, partialTicks);
            efw.animation.layered.math.Vec3f baseRot = ap.getBaseLayerTransform("body", efw.animation.layered.TransformType.ROTATION, partialTicks);
            pos = new efw.animation.layered.math.Vec3f(pos.getX() - basePos.getX(), pos.getY() - basePos.getY(), pos.getZ() - basePos.getZ());
            rot = new efw.animation.layered.math.Vec3f(rot.getX() - baseRot.getX(), rot.getY() - baseRot.getY(), rot.getZ() - baseRot.getZ());
        }

        // Only apply if there's meaningful transform (skip if nearly zero)
        boolean hasRot = Math.abs(rot.getX()) > 0.001f || Math.abs(rot.getY()) > 0.001f || Math.abs(rot.getZ()) > 0.001f;
        boolean hasPos = Math.abs(pos.getX()) > 0.001f || Math.abs(pos.getY()) > 0.001f || Math.abs(pos.getZ()) > 0.001f;
        if (!hasRot && !hasPos) return;
        
        // Pivot at waist (0.7 blocks up from feet), matching 1.20.1 setupRotations.
        // NOTE: 1.20.1 PlayerRendererMixin does NOT scale the body position by 0.0625f!
        // It passes vec3d directly into translate() which expects blocks, so the JSON 
        // values for torso position in Emotecraft format are authored as blocks.
        GlStateManager.translate(pos.getX(), pos.getY() + 0.7f, pos.getZ());
        
        float rotX = (float) Math.toDegrees(rot.getX());
        float rotY = (float) Math.toDegrees(rot.getY());
        float rotZ = (float) Math.toDegrees(rot.getZ());
        
        GlStateManager.rotate(rotZ, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(rotY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(rotX, 1.0f, 0.0f, 0.0f);
        
        GlStateManager.translate(0.0f, -0.7f, 0.0f);
    }
}
