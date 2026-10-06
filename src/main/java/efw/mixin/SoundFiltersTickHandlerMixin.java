package efw.mixin;

import com.voltyx.mwccf.geo.HeartbeatManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.tmtravlr.soundfilters.SoundTickHandler", remap = false)
public abstract class SoundFiltersTickHandlerMixin {

    @Shadow
    public static float baseLowPassGain;

    @Shadow
    public static float baseLowPassGainHF;

    @Inject(method = "tick", at = @At("RETURN"), remap = false)
    private void mwccf$applyHeartbeatMuffling(TickEvent.ClientTickEvent event, CallbackInfo ci) {
        if (event.phase != TickEvent.Phase.START) return;

        float bpm = HeartbeatManager.currentBPM;
        if (bpm > 140f) {
            // Factor 0.0 at 140 BPM -> 1.0 at 180 BPM
            float factor = Math.min(1.0f, (bpm - 140.0f) / 40.0f);
            
            // Приглушаем высокие частоты (HF) до 0.22 (звук как сквозь толстую стену/ватные уши)
            // И общую громкость до 0.65
            float muffleHF = 1.0f - factor * 0.78f;   // 1.0 -> 0.22
            float muffleGain = 1.0f - factor * 0.35f; // 1.0 -> 0.65

            baseLowPassGain *= muffleGain;
            baseLowPassGainHF *= muffleHF;
        }
    }
}
