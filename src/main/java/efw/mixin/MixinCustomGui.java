package efw.mixin;

import com.paneedah.weaponlib.ModContext;
import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.render.ModificationGUI;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.paneedah.weaponlib.CustomGui", remap = false)
public class MixinCustomGui {

    @Inject(method = "handleModificationHUD", at = @At("HEAD"), cancellable = true, remap = false)
    private void onHandleModificationHUD(RenderGameOverlayEvent.Pre event, PlayerWeaponInstance weaponInstance,
            double scaledWidth, double scaledHeight, CallbackInfo ci) {
        // Полностью отменяем старый HUD оверлей модификации MWC
        ci.cancel();
    }
}
