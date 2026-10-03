package efw.mixin;

import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "baubles.client.gui.GuiBaublesButton")
public abstract class MixinGuiBaublesButton extends GuiButton {

    public MixinGuiBaublesButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void efw$clearDisplayString(CallbackInfo ci) {
        this.displayString = "";
    }
}
