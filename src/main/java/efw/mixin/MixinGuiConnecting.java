package efw.mixin;

import com.voltyx.mwccf.client.loading.ItemLoadingScreenRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public abstract class MixinGuiConnecting extends GuiScreen {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinGuiConnecting class loaded!");
    }

    @Shadow
    private NetworkManager networkManager;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void onInitGui(CallbackInfo ci) {
        ItemLoadingScreenRenderer.pickRandom();
        ItemLoadingScreenRenderer.preloadTexture();
        ItemLoadingScreenRenderer.layoutLoadingButtons(this.buttonList, this.width, this.height);
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ci.cancel();

        String status = (this.networkManager == null) ? I18n.format("connect.connecting") : I18n.format("connect.authorizing");
        ItemLoadingScreenRenderer.render(this.width, this.height, status, "");

        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton btn = this.buttonList.get(i);
            btn.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
    }
}
