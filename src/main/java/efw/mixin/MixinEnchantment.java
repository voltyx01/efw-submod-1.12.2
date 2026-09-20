package efw.mixin;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import com.voltyx.mwccf.immersiveui.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {

    @Shadow
    public abstract boolean isCurse();

    @Inject(method = "getTranslatedName", at = @At("RETURN"), cancellable = true)
    public void immersiveui$getTranslatedName(int level, CallbackInfoReturnable<String> cir) {
        if (!ImmersiveUIConfig.enableCurseFormatting || !this.isCurse()) return;

        Minecraft mc = Minecraft.getMinecraft();
        int ticks = (mc.player != null ? mc.player.ticksExisted : 0) * 10000 + this.hashCode();
        if (new Random(ticks).nextBoolean()) {
            cir.setReturnValue(RenderUtils.obfuscateCursedText(cir.getReturnValue(), 0.15D, ticks));
        }
    }
}
