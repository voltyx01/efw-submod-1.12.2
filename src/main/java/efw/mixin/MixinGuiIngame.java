package efw.mixin;

import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinGuiIngame class loaded!");
    }

    @Redirect(
        method = "renderAttackIndicator",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/settings/GameSettings;thirdPersonView:I"
        ),
        require = 0
    )
    private int redirectThirdPersonViewInRenderAttackIndicator(GameSettings settings) {
        if (ShoulderInstance.getInstance().doShoulderSurfing()) {
            return 0;
        }
        return settings.thirdPersonView;
    }
}
