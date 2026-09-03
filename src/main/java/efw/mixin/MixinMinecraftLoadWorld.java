package efw.mixin;

import com.voltyx.mwccf.client.loading.CustomLoadingScreenRenderer;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Minecraft.class)
public class MixinMinecraftLoadWorld {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinMinecraftLoadWorld class loaded!");
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;loadingScreen:Lnet/minecraft/client/LoadingScreenRenderer;", shift = At.Shift.AFTER))
    private void afterSetLoadingScreen(net.minecraft.client.multiplayer.WorldClient worldClientIn, String loadingMessage, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (worldClientIn == null) {
            mc.loadingScreen = new CustomLoadingScreenRenderer(mc);
        }
    }
}
