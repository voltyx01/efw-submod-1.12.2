package efw.mixin;

import com.voltyx.mwccf.darkmode.DarkGuiManager;
import com.voltyx.mwccf.furniture.client.gui.FurnitureGuiHelper;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @Inject(
        method = "drawString(Ljava/lang/String;FFIZ)I",
        at = @At("HEAD"),
        cancellable = true
    )
    private void furniture$hideInventoryText(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir) {
        if (text != null && FurnitureGuiHelper.isFurnitureContainerOpen() && FurnitureGuiHelper.isInventoryText(text)) {
            cir.setReturnValue(0);
        }
    }

    @ModifyVariable(
        method = "drawString(Ljava/lang/String;FFIZ)I",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true
    )
    private int darkgui$adjustTextColor(int color) {
        if (DarkGuiManager.isEnabled() && DarkGuiManager.isContainerOpen()) {
            int rgb = color & 0x00FFFFFF;
            // 0x404040 = 4210752 (default dark grey vanilla text)
            // 0x373737, 0x303030, 0x222222
            if (rgb == 0x404040 || rgb == 0x373737 || rgb == 0x303030 || rgb == 0x222222) {
                return (color & 0xFF000000) | 0xE0E0E0;
            }
        }
        return color;
    }
}
