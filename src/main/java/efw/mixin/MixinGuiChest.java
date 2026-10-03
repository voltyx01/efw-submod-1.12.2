package efw.mixin;

import com.voltyx.mwccf.furniture.client.gui.FurnitureGuiHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChest.class)
public abstract class MixinGuiChest extends GuiContainer {

    public MixinGuiChest(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void furniture$onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (FurnitureGuiHelper.isFurnitureContainer(this)) {
            FurnitureGuiHelper.disableQuarkSearchBar();
        }
    }

    @Redirect(
        method = "drawGuiContainerForegroundLayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I",
            ordinal = 1
        )
    )
    private int furniture$suppressPlayerInventoryText(FontRenderer fontRenderer, String text, int x, int y, int color) {
        if (FurnitureGuiHelper.isFurnitureContainer(this)) {
            return 0;
        }
        return fontRenderer.drawString(text, x, y, color);
    }
}
