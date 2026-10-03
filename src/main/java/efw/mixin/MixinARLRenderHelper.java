package efw.mixin;

import com.voltyx.mwccf.darkmode.DarkGuiManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vazkii.arl.util.RenderHelper;

@Pseudo
@Mixin(RenderHelper.class)
public abstract class MixinARLRenderHelper {

    @ModifyVariable(
        method = "renderTooltip(IILjava/util/List;II)V",
        at = @At("HEAD"),
        ordinal = 2,
        argsOnly = true,
        remap = false
    )
    private static int arl$modifyBorderColor(int color) {
        if (DarkGuiManager.isEnabled()) {
            return 0xFF383838;
        }
        return color;
    }

    @ModifyVariable(
        method = "renderTooltip(IILjava/util/List;II)V",
        at = @At("HEAD"),
        ordinal = 3,
        argsOnly = true,
        remap = false
    )
    private static int arl$modifyBackgroundColor(int color2) {
        if (DarkGuiManager.isEnabled()) {
            return 0xF0181818;
        }
        return color2;
    }
}
