package efw.mixin;

import com.voltyx.mwccf.furniture.client.gui.FurnitureGuiHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.client.feature.ChestSearchBar;

@Pseudo
@Mixin(ChestSearchBar.class)
public abstract class MixinQuarkChestSearchBar {

    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true, remap = false)
    private void furniture$cancelSearchBarInit(GuiScreenEvent.InitGuiEvent.Post event, CallbackInfo ci) {
        if (FurnitureGuiHelper.isFurnitureContainer(event.getGui())) {
            ChestSearchBar.searchBar = null;
            ci.cancel();
        }
    }

    @Inject(method = "renderElements", at = @At("HEAD"), cancellable = true, remap = false)
    private void furniture$cancelSearchBarRender(GuiScreen gui, CallbackInfo ci) {
        if (FurnitureGuiHelper.isFurnitureContainer(gui) || FurnitureGuiHelper.isFurnitureContainerOpen()) {
            ChestSearchBar.searchBar = null;
            ci.cancel();
        }
    }

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true, remap = false)
    private void furniture$cancelSearchBarBackground(GuiScreen gui, int x, int y, CallbackInfo ci) {
        if (FurnitureGuiHelper.isFurnitureContainer(gui) || FurnitureGuiHelper.isFurnitureContainerOpen()) {
            ChestSearchBar.searchBar = null;
            ci.cancel();
        }
    }
}
