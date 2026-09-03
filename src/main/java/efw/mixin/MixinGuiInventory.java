package efw.mixin;

import efw.util.RenderContext;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiInventory.class)
public class MixinGuiInventory {

    @Inject(
        method = "drawEntityOnScreen(IIIFFLnet/minecraft/entity/EntityLivingBase;)V",
        at = @At("HEAD")
    )
    private static void beforeDrawEntity(int posX, int posY, int scale, float mouseX, float mouseY,
                                         EntityLivingBase ent, CallbackInfo ci) {
        RenderContext.isRenderingPlayerInGui = true;
    }

    @Inject(
        method = "drawEntityOnScreen(IIIFFLnet/minecraft/entity/EntityLivingBase;)V",
        at = @At("RETURN")
    )
    private static void afterDrawEntity(int posX, int posY, int scale, float mouseX, float mouseY,
                                        EntityLivingBase ent, CallbackInfo ci) {
        RenderContext.isRenderingPlayerInGui = false;
    }
}
