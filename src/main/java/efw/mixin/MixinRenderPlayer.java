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
        if (ap == null || !ap.isActive()) return;
        
        // Apply body-level world-space transform for rolls and Emotecraft/BetterCombat action animations
        boolean isRoll = ap.isRollActive(partialTicks);
        boolean isEmoteAction = ap.hasActionWeight() && ap.getActionClip() != null && ap.getActionClip().isEmotecraft;
        // BetterCombat attack clips also go through actionLayer but must NOT shift entity Y
        boolean isBetterCombatAttack = ap.hasActionWeight() && ap.getActionClip() != null && ap.getActionClip().isBetterCombat;
        if (!isRoll && !isEmoteAction) return;

        efw.animation.layered.math.Vec3f pos = isRoll 
                ? ap.getRollLayerTransform("body", efw.animation.layered.TransformType.POSITION, partialTicks)
                : ap.get3DTransform("body", efw.animation.layered.TransformType.POSITION, partialTicks, efw.animation.layered.math.Vec3f.ZERO);
        efw.animation.layered.math.Vec3f rot = isRoll
                ? ap.getRollLayerTransform("body", efw.animation.layered.TransformType.ROTATION, partialTicks)
                : ap.get3DTransform("body", efw.animation.layered.TransformType.ROTATION, partialTicks, efw.animation.layered.math.Vec3f.ZERO);

        // BetterCombat attack animations have torso.y keyframes but those are meant for the
        // bipedBody ModelRenderer (bone-space), NOT for world-space entity translation.
        // Applying posY here would sink the player's feet into the ground during attacks.
        // For BetterCombat clips we zero out the Y offset entirely.
        float posY = isBetterCombatAttack ? 0.0f : pos.getY();
        float posX = pos.getX();
        float posZ = pos.getZ();

        // Only apply if there's meaningful transform (skip if nearly zero)
        boolean hasRot = Math.abs(rot.getX()) > 0.001f || Math.abs(rot.getY()) > 0.001f || Math.abs(rot.getZ()) > 0.001f;
        boolean hasPos = Math.abs(posX) > 0.001f || Math.abs(posY) > 0.001f || Math.abs(posZ) > 0.001f;
        if (!hasRot && !hasPos) return;
        
        // Pivot at waist (0.7 blocks up from feet), matching 1.20.1 setupRotations.
        GlStateManager.translate(posX, posY + 0.7f, posZ);
        
        float rotX = (float) Math.toDegrees(rot.getX());
        float rotY = (float) Math.toDegrees(rot.getY());
        float rotZ = (float) Math.toDegrees(rot.getZ());
        
        GlStateManager.rotate(rotZ, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(rotY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(rotX, 1.0f, 0.0f, 0.0f);
        
        GlStateManager.translate(0.0f, -0.7f, 0.0f);
    }
}
