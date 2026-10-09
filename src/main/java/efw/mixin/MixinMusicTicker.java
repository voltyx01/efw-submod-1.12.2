package efw.mixin;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTicker.class)
public abstract class MixinMusicTicker {

    @Shadow @Final private Minecraft mc;
    @Shadow private ISound currentMusic;
    @Shadow private int timeUntilNextMusic;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void mwccf$suppressVanillaMusic(CallbackInfo ci) {
        if (this.mc.player != null && SRPConfig.musicTrue) {
            if (this.currentMusic != null) {
                ResourceLocation loc = this.currentMusic.getSoundLocation();
                if (loc == null || !"srparasites".equals(loc.getNamespace())) {
                    this.mc.getSoundHandler().stopSound(this.currentMusic);
                    this.currentMusic = null;
                }
            }
            this.timeUntilNextMusic = 10000;
            ci.cancel();
        }
    }

    @Inject(method = "playMusic", at = @At("HEAD"), cancellable = true)
    private void mwccf$blockVanillaPlayMusic(MusicTicker.MusicType requestedMusicType, CallbackInfo ci) {
        if (this.mc.player != null && SRPConfig.musicTrue) {
            ci.cancel();
        }
    }
}
