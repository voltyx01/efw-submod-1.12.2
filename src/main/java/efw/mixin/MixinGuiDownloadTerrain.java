package efw.mixin;

import com.voltyx.mwccf.client.loading.ItemLoadingScreenRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDownloadTerrain.class)
public abstract class MixinGuiDownloadTerrain extends GuiScreen {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinGuiDownloadTerrain class loaded!");
    }

    @Inject(method = "initGui", at = @At("RETURN"))
    private void onInitGui(CallbackInfo ci) {
        ItemLoadingScreenRenderer.pickRandom();
        ItemLoadingScreenRenderer.preloadTexture();
        ItemLoadingScreenRenderer.layoutLoadingButtons(this.buttonList, this.width, this.height);
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ci.cancel();

        String status = I18n.format("multiplayer.downloadingTerrain");
        ItemLoadingScreenRenderer.render(this.width, this.height, status, "");

        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton btn = this.buttonList.get(i);
            btn.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
    }
}
