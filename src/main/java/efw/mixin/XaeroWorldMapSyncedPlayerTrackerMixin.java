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
@Mixin(targets = "xaero.map.server.radar.tracker.SyncedPlayerTracker", remap = false)
public abstract class XaeroWorldMapSyncedPlayerTrackerMixin {

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void onTick(MinecraftServer server, EntityPlayerMP player, xaero.map.server.MinecraftServerData serverData, xaero.map.server.player.ServerPlayerData playerData, CallbackInfo ci) {
        if (!MapDeviceState.hasActiveMap(player)) {
            ci.cancel();
        }
    }
}
