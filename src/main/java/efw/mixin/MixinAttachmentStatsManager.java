package efw.mixin;

import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.stats.AttachmentStatsManager;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Accelerates MWC firearm reload speed by 1.6x when Saya's doll active ability buff is running.
 */
@Pseudo
@Mixin(value = AttachmentStatsManager.class, remap = false)
public class MixinAttachmentStatsManager {

    @Inject(method = "getEffectiveStats", at = @At("RETURN"), cancellable = true)
    private static void onGetEffectiveStats(PlayerWeaponInstance weaponInstance,
                                            CallbackInfoReturnable<AttachmentStatsManager.EffectiveWeaponStats> cir) {
        if (weaponInstance != null && weaponInstance.getPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) weaponInstance.getPlayer();
            if (com.voltyx.mwccf.doll.SayaDollManager.isBuffActive(player)) {
                AttachmentStatsManager.EffectiveWeaponStats stats = cir.getReturnValue();
                if (stats != null) {
                    stats.reloadSpeedMultiplier *= 1.6;
                }
            }
        }
    }
}
