package efw.mixin;

import com.dhanantry.scapeandrunparasites.init.SRPMusic;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import net.minecraft.client.audio.MusicTicker;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SRPEventHandlerBus.class, remap = false)
public abstract class MixinSRPEventHandlerBus {

    /**
     * Allow music to play during Phase 0 by treating Phase 0 as active phase (> 0).
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
        if (SRPEventHandlerBus.clientCurrentEvoPhase == 0) {
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
        if (SRPEventHandlerBus.clientCurrentEvoPhase == 0) {
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
        if (cir.getReturnValue() == null && SRPEventHandlerBus.clientCurrentEvoPhase >= 0) {
            cir.setReturnValue(SRPMusic.EVPHASE_1_MUSIC);
        }
    }
}
