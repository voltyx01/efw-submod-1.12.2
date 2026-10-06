package efw.mixin;

import com.voltyx.mwccf.client.loading.ItemLoadingScreenRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenWorking.class)
public abstract class MixinGuiScreenWorking extends GuiScreen {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinGuiScreenWorking class loaded!");
    }

    @Shadow
    private String title;
    @Shadow
    private String stage;
    @Shadow
    private int progress;
    @Shadow
    private boolean doneWorking;

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.doneWorking) {
            return;
        }

        if (com.voltyx.mwccf.client.loading.CustomLoadingScreenRenderer.isRunning()) {
            ItemLoadingScreenRenderer.pickRandom();
            ItemLoadingScreenRenderer.preloadTexture();
        }
        ItemLoadingScreenRenderer.layoutLoadingButtons(this.buttonList, this.width, this.height);

        ci.cancel();

        String stageText = (this.stage != null && !this.stage.isEmpty())
                ? (this.stage + (this.progress > 0 ? " " + this.progress + "%" : ""))
                : "";
        ItemLoadingScreenRenderer.render(this.width, this.height, this.title, stageText);

        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton btn = this.buttonList.get(i);
            btn.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
    }
}
