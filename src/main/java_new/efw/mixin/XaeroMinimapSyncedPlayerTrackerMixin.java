package efw.mixin;

import com.voltyx.mwccf.geo.MapDeviceState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "xaero.common.server.radar.tracker.SyncedPlayerTracker", remap = false)
public abstract class XaeroMinimapSyncedPlayerTrackerMixin {

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void onTick(MinecraftServer server, EntityPlayerMP player, xaero.common.server.MinecraftServerData serverData, xaero.common.server.player.ServerPlayerData playerData, CallbackInfo ci) {
        if (!MapDeviceState.hasActiveMap(player)) {
            ci.cancel();
        }
    }
}
