package efw.mixin;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.AdvancementToast;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementToast.class)
public class MixinAdvancementToast {

    @Shadow
    @Final
    private Advancement advancement;

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItemAndEffectIntoGUI(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V", shift = At.Shift.BEFORE))
    public void immersiveui$beforeRenderItem(GuiToast toastGui, long delta, CallbackInfoReturnable<IToast.Visibility> cir) {
        if (!ImmersiveUIConfig.enableAdvancementToastItems) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        float time = mc.player.ticksExisted + (this.advancement != null ? this.advancement.getId().hashCode() / 1000.0F : 0.0F);

        GlStateManager.pushMatrix();
        GlStateManager.translate(16.0F, 16.0F, 150.0F);
        GlStateManager.rotate(MathHelper.sin(time * 0.05F) * 8.6F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(MathHelper.cos(time * 0.1F) * 11.5F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(MathHelper.cos(time * 0.075F) * 17.2F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, MathHelper.sin(time * 0.1F) * 1.5F, 0.0F);
        GlStateManager.translate(-16.0F, -16.0F, -150.0F);
    }

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItemAndEffectIntoGUI(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V", shift = At.Shift.AFTER))
    public void immersiveui$afterRenderItem(GuiToast toastGui, long delta, CallbackInfoReturnable<IToast.Visibility> cir) {
        if (!ImmersiveUIConfig.enableAdvancementToastItems) return;
        GlStateManager.popMatrix();
    }
}
