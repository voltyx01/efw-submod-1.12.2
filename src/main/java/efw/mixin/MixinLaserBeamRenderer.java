package efw.mixin;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.paneedah.weaponlib.LaserBeamRenderer", remap = false)
public class MixinLaserBeamRenderer {

    @Inject(method = "render", at = @At("RETURN"), remap = false)
    private void onLaserRenderReturn(CallbackInfo ci) {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GlStateManager.enableRescaleNormal();
    }
}
