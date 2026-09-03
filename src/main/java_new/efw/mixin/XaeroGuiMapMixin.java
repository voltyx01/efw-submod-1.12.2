package efw.mixin;

import com.voltyx.mwccf.geo.MapDeviceState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "xaero.map.gui.GuiMap")
public abstract class XaeroGuiMapMixin extends GuiScreen {

    @Inject(method = "func_73866_w_", at = @At("HEAD"), cancellable = true) // initGui
    private void onInitGui(CallbackInfo ci) {
        if (!MapDeviceState.hasActiveMap()) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            ci.cancel();
        }
    }

    @Inject(method = "func_73876_c", at = @At("HEAD"), cancellable = true) // updateScreen
    private void onUpdateScreen(CallbackInfo ci) {
        if (!MapDeviceState.hasActiveMap()) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            ci.cancel();
        }
    }
}
