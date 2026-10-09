package efw.mixin;

import com.dhanantry.scapeandrunparasites.init.SRPMusic;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.voltyx.mwccf.srp.SRPMusicTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(value = SRPEventHandlerBus.class, remap = false)
public abstract class MixinSRPEventHandlerBus {

    @Shadow
    private ArrayList<ISound> musicToRemove;

    @Inject(
        method = "onWorldLoad",
        at = @At("HEAD"),
        remap = false
    )
    private void mwccf$onWorldUnload(WorldEvent.Unload event, CallbackInfo ci) {
        SRPMusicTracker.reset();
        SRPEventHandlerBus.musicTimer = 0;
    }

    /**
     * Monitor music playback and start SRP music immediately upon world join.
     */
    @Inject(
        method = "soundThree",
        at = @At("HEAD"),
        remap = false
    )
    private void mwccf$onSoundThreeHead(TickEvent.PlayerTickEvent event, CallbackInfo ci) {
        if (event.phase != TickEvent.Phase.START || event.side != Side.CLIENT) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.player == null || mc.getSoundHandler() == null) {
            return;
        }

        if (this.musicToRemove != null && !this.musicToRemove.isEmpty()) {
            this.musicToRemove.clear();
        }

        if (SRPMusicTracker.currentSrpMusic != null) {
            if (mc.getSoundHandler().isSoundPlaying(SRPMusicTracker.currentSrpMusic)) {
                // Keep timer positive so soundThree does not overlap another track while this one plays
                if (SRPEventHandlerBus.musicTimer <= 0) {
                    SRPEventHandlerBus.musicTimer = 40;
                }
            } else {
                // Track just finished playing; take a brief 5-second pause before starting the next track
                SRPMusicTracker.reset();
                SRPEventHandlerBus.musicTimer = 100;
            }
        } else {
            // No SRP music is currently playing: force timer to 0 so it plays immediately
            if (SRPEventHandlerBus.musicTimer > 20) {
                SRPEventHandlerBus.musicTimer = 0;
            }
        }
    }

    /**
     * Intercept SRP's 14420-18200 tick delay reset so music does not go silent for 15 minutes.
     */
    @Redirect(
        method = "soundThree",
        at = @At(
            value = "INVOKE",
            target = "Lcom/dhanantry/scapeandrunparasites/util/handlers/SRPEventHandlerBus;resetSouncTicker(I)V"
        ),
        remap = false
    )
    private void mwccf$redirectResetSoundTicker(int p) {
        // Managed dynamically in mwccf$onSoundThreeHead
    }

    /**
     * Capture the currently playing SRP music track so we know when it starts and ends.
     */
    @Redirect(
        method = "soundThree",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/audio/SoundHandler;playSound(Lnet/minecraft/client/audio/ISound;)V"
        ),
        remap = false
    )
    private void mwccf$captureAndPlaySrpMusic(SoundHandler soundHandler, ISound sound) {
        SRPMusicTracker.setPlayingSrpMusic(sound);
        soundHandler.playSound(sound);
    }

    /**
     * Allow music to play during Phase 0 by treating Phase <= 0 as active phase (> 0).
     */
    @Redirect(
        method = "soundThree",
        at = @At(
            value = "FIELD",
            target = "Lcom/dhanantry/scapeandrunparasites/util/handlers/SRPEventHandlerBus;clientCurrentEvoPhase:B",
            opcode = Opcodes.GETSTATIC
        ),
        remap = false
    )
    private byte mwccf$redirectPhaseInSoundThree() {
        if (SRPEventHandlerBus.clientCurrentEvoPhase <= 0) {
            return 1;
        }
        return SRPEventHandlerBus.clientCurrentEvoPhase;
    }

    /**
     * Suppress vanilla background music during Phase 0 as well.
     */
    @Redirect(
        method = "soundTwo",
        at = @At(
            value = "FIELD",
            target = "Lcom/dhanantry/scapeandrunparasites/util/handlers/SRPEventHandlerBus;clientCurrentEvoPhase:B",
            opcode = Opcodes.GETSTATIC,
            ordinal = 1
        ),
        remap = false
    )
    private byte mwccf$redirectPhaseInSoundTwo() {
        if (SRPEventHandlerBus.clientCurrentEvoPhase <= 0) {
            return 1;
        }
        return SRPEventHandlerBus.clientCurrentEvoPhase;
    }

    /**
     * When getMusicPhase returns null (because phase is 0 and player is not in a parasite biome),
     * play Phase 1 music pool (the_call, prey, relentless, parasite).
     */
    @Inject(
        method = "getMusicPhase",
        at = @At("RETURN"),
        cancellable = true,
        remap = false
    )
    private void mwccf$getMusicPhaseZero(CallbackInfoReturnable<MusicTicker.MusicType> cir) {
        if (cir.getReturnValue() == null) {
            cir.setReturnValue(SRPMusic.EVPHASE_1_MUSIC);
        }
    }
}
