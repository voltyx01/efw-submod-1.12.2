package efw.mixin;

import com.voltyx.mwccf.geo.MapDeviceState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "xaero.hud.minimap.radar.state.RadarStateUpdater", remap = false)
public abstract class XaeroRadarStateUpdaterMixin {

    @Inject(method = "update", at = @At("HEAD"), cancellable = true, remap = false)
    private void onUpdate(WorldClient world, Entity renderEntity, EntityPlayer player, CallbackInfo ci) {
        if (!MapDeviceState.hasActiveMap(player)) {
            ci.cancel();
        }
    }
}
