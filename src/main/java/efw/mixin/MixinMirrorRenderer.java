package efw.mixin;

import efw.util.SubpassRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.mrcrayfish.furniture.render.tileentity.MirrorRenderer", remap = false)
public abstract class MixinMirrorRenderer {

    private static final SubpassRenderState efw$mirrorState = new SubpassRenderState();

    @Redirect(
        method = "onTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/EntityRenderer;func_78471_a(FJ)V"
        ),
        remap = false,
        require = 0
    )
    private void efw$redirectMirrorRenderWorldSrg(EntityRenderer entityRenderer, float partialTicks, long finishTimeNano) {
        efw$runMirrorRenderWorld(entityRenderer, partialTicks, finishTimeNano);
    }

    @Redirect(
        method = "onTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/EntityRenderer;renderWorld(FJ)V"
        ),
        remap = false,
        require = 0
    )
    private void efw$redirectMirrorRenderWorldDeobf(EntityRenderer entityRenderer, float partialTicks, long finishTimeNano) {
        efw$runMirrorRenderWorld(entityRenderer, partialTicks, finishTimeNano);
    }

    private static void efw$runMirrorRenderWorld(EntityRenderer entityRenderer, float partialTicks, long finishTimeNano) {
        if (!SubpassRenderState.mirrorRenderedThisTick) {
            efw$mirrorState.save(entityRenderer);
            SubpassRenderState.mirrorRenderedThisTick = true;
        }
        SubpassRenderState.isMirrorRendering = true;
        try {
            entityRenderer.renderWorld(partialTicks, finishTimeNano);
        } finally {
            SubpassRenderState.isMirrorRendering = false;
        }
    }

    @Inject(
        method = "onTick",
        at = @At("RETURN"),
        remap = false,
        require = 0
    )
    private void efw$onTickReturn(TickEvent.RenderTickEvent event, CallbackInfo ci) {
        if (!SubpassRenderState.mirrorRenderedThisTick) return;
        SubpassRenderState.mirrorRenderedThisTick = false;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) return;

        if (mc.getFramebuffer() != null) {
            mc.getFramebuffer().bindFramebuffer(true);
        }
        if (mc.displayWidth > 0 && mc.displayHeight > 0) {
            GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.bindTexture(0);
        if (mc.entityRenderer != null) {
            efw$mirrorState.restore(mc.entityRenderer);
        }
    }
}
