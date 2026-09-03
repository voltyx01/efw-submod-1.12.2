package efw.mixin;

import mcp.mobius.waila.overlay.OverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Pseudo;
@Pseudo
@Mixin(targets = "net.minecraftforge.client.gui.GuiIngameForge")
public abstract class MixinGuiIngameForge {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinGuiIngameForge class loaded!");
    }

    @Inject(method = "renderGameOverlay", at = @At("HEAD"), remap = false)
    private void renderHwylaUnderHUD(float partialTicks, CallbackInfo ci) {
        OverlayRenderer.saveGLState();
        OverlayRenderer.renderOverlay();
        OverlayRenderer.loadGLState();
    }

    @org.spongepowered.asm.mixin.injection.Redirect(
        method = "renderCrosshairs",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/settings/GameSettings;thirdPersonView:I"
        ),
        require = 0,
        remap = false
    )
    private int redirectThirdPersonViewInRenderCrosshairs(net.minecraft.client.settings.GameSettings settings) {
        if (com.teamderpy.shouldersurfing.client.ShoulderInstance.getInstance().doShoulderSurfing()) {
            return 0;
        }
        return settings.thirdPersonView;
    }
}
