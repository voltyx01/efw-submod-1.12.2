package efw.mixin;

import com.voltyx.mwccf.blood.BloodTextureManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextureManager.class)
public class MixinTextureManager {

    @ModifyVariable(method = "bindTexture(Lnet/minecraft/util/ResourceLocation;)V", at = @At("HEAD"), argsOnly = true)
    private ResourceLocation efw$replaceTextureWithBlood(ResourceLocation resource) {
        ResourceLocation dark = com.voltyx.mwccf.darkmode.DarkGuiManager.getReplacementTexture(resource);
        return BloodTextureManager.getReplacementTexture(dark);
    }
}
