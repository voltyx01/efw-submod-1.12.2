package efw.mixin;

import efw.client.SplashRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraftSplash {

    private static boolean resizeFixApplied = false;

    @Inject(method = "setInitialDisplayMode", at = @At("HEAD"), cancellable = true)
    private void onSetInitialDisplayMode(CallbackInfo ci) throws LWJGLException {
        SplashRenderer.initDisplayMode((Minecraft) (Object) this);
        ci.cancel();
    }

    @Inject(method = "createDisplay", at = @At("RETURN"))
    private void onDisplayCreated(CallbackInfo ci) {
        SplashRenderer.onDisplayCreated();
    }

    /**
     * @author Antigravity
     * @reason Render splash frame with logo and center animated snake
     */
    @Overwrite
    public void drawSplashScreen(TextureManager textureManagerInstance) throws LWJGLException {
        SplashRenderer.renderFrameImmediate();
    }

    @Inject(method = "displayGuiScreen", at = @At("HEAD"))
    private void onDisplayGuiScreen(GuiScreen guiScreenIn, CallbackInfo ci) {
        SplashRenderer.restoreDisplayMode((Minecraft) (Object) this);
        applyResizeFixOnce((Minecraft) (Object) this);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", shift = At.Shift.BEFORE))
    private void onBeforeFirstGui(CallbackInfo ci) {
        SplashRenderer.restoreDisplayMode((Minecraft) (Object) this);
        applyResizeFixOnce((Minecraft) (Object) this);
    }

    /**
     * Forge fix for MC-68754 / LWJGL2 bug (lwjgl/lwjgl#142):
     * the native window's maximize button doesn't send a resize event
     * to LWJGL until Display.setResizable() is toggled off/on after
     * the window has actually been created. Vanilla only hits this code
     * path inside toggleFullscreen() (i.e. after pressing F11), which is
     * exactly why manually entering/exiting fullscreen "fixes" it.
     * Since our custom splash screen bypasses that path entirely, we
     * need to apply it manually, once, right before the first GUI shows.
     */
    private static void applyResizeFixOnce(Minecraft mc) {
        if (resizeFixApplied) {
            return;
        }
        resizeFixApplied = true;

        if (!mc.isFullScreen()) {
            try {
                Display.setResizable(false);
                Display.setResizable(true);
            } catch (Exception ignored) {
                // Display might not be fully ready in edge cases — safe to ignore,
                // worst case the bug persists until next window event.
            }
        }
    }
}