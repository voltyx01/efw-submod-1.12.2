package efw.mixin;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngameHotbar extends Gui {

    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinGuiIngameHotbar class loaded!");
    }

    @Shadow
    @Final
    protected Minecraft mc;

    @Unique
    private static double immersiveui$hotbarPos = 0.0D;

    @Unique
    private static long immersiveui$lastNanoTime = 0L;

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 1))
    private void immersiveui$beforeDrawSelector(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
        if (ImmersiveUIConfig.renderHotbarSelectorAboveItems) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 150.0F);
        }
    }

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 1, shift = At.Shift.AFTER))
    private void immersiveui$afterDrawSelector(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
        if (ImmersiveUIConfig.renderHotbarSelectorAboveItems) {
            GlStateManager.popMatrix();
        }
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 1), index = 0)
    private int immersiveui$selectedSlotPositionX(int originalX) {
        if (!ImmersiveUIConfig.enableHotbarSelectorAnimation || this.mc.player == null) {
            return originalX;
        }

        long now = System.nanoTime();
        double deltaTime = immersiveui$lastNanoTime > 0L ? (now - immersiveui$lastNanoTime) / 1_000_000_000.0D : 0.016D;
        immersiveui$lastNanoTime = now;
        if (deltaTime <= 0.0D || deltaTime > 0.1D) {
            deltaTime = 0.016D;
        }

        double speed = ImmersiveUIConfig.hotbarSelectorSpeed;
        int targetSlot = this.mc.player.inventory.currentItem;

        double factor = Math.min(1.0D, 18.0D * deltaTime * speed);
        immersiveui$hotbarPos += (targetSlot - immersiveui$hotbarPos) * factor;

        if (Math.abs(immersiveui$hotbarPos - targetSlot) < 0.01D) {
            immersiveui$hotbarPos = targetSlot;
        }

        ScaledResolution res = new ScaledResolution(this.mc);
        int baseX = res.getScaledWidth() / 2 - 91 - 1;
        return (int) Math.round(baseX + immersiveui$hotbarPos * 20.0D);
    }
}
