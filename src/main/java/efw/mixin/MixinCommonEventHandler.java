package efw.mixin;

import com.paneedah.weaponlib.CommonEventHandler;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Pseudo;
@Pseudo
@Mixin(CommonEventHandler.class)
public class MixinCommonEventHandler {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinCommonEventHandler class loaded!");
    }

    @Inject(method = "onLivingHurtEvent", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelLivingHurtEvent(LivingHurtEvent event, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onCompatibleLivingDeathEvent", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelLivingDeathEvent(LivingDeathEvent event, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onPlayerCloneEvent", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelPlayerClone(PlayerEvent.Clone event, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onPlayerRespawnEvent", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelPlayerRespawn(PlayerRespawnEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
