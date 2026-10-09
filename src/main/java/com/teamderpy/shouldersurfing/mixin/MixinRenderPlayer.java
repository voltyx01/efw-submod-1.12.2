package com.teamderpy.shouldersurfing.mixin;

import com.teamderpy.shouldersurfing.client.CameraEntityRenderer;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {

    @Inject(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At("HEAD"), cancellable = true)
    private void preDoRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            if (entity == com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.getCurrentCarrier()) {
                com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.carrierRenderedInPass = true;
            }
            return;
        }
        if (entity == Minecraft.getMinecraft().player && ShoulderInstance.getInstance().doShoulderSurfing()) {
            boolean skip = CameraEntityRenderer.getInstance().preRenderCameraEntity(entity, partialTicks);
            if (skip) {
                ci.cancel();
                return;
            }

            float alpha = CameraEntityRenderer.getInstance().getCameraEntityAlpha();
            if (alpha < 1.0F) {
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.alphaFunc(org.lwjgl.opengl.GL11.GL_GREATER, 0.001F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    @Inject(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At("RETURN"))
    private void postDoRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            return;
        }
        if (entity == Minecraft.getMinecraft().player) {
            CameraEntityRenderer.getInstance().postRenderCameraEntity(entity, partialTicks);
            if (ShoulderInstance.getInstance().doShoulderSurfing()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(org.lwjgl.opengl.GL11.GL_GREATER, 0.1F);
                GlStateManager.depthMask(true);
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }
        }
    }

    @org.spongepowered.asm.mixin.injection.Redirect(
        method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isUser()Z")
    )
    private boolean efw$bodycamAllowRenderUser(AbstractClientPlayer entity) {
        if (com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            return false;
        }
        return entity.isUser();
    }
}
