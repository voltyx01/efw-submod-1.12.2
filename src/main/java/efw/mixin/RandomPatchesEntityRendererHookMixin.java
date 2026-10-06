package efw.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.therandomlabs.randompatches.hook.client.EntityRendererHook", remap = false)
public abstract class RandomPatchesEntityRendererHookMixin {

    @Inject(method = "orientCamera", at = @At("HEAD"), cancellable = true, remap = false)
    private static void mwccf$cancelOrientCameraForNonPlayer(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.getRenderViewEntity() != mc.player) {
            ci.cancel();
        }
    }

    @Inject(method = "updateRenderer", at = @At("HEAD"), cancellable = true, remap = false)
    private static void mwccf$cancelUpdateRendererForNonPlayer(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.getRenderViewEntity() != mc.player) {
            ci.cancel();
        }
    }
}
