package efw.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityPlayer {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinEntityPlayer class loaded!");
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void preventJumpWhileCrawling(CallbackInfo ci) {
        EntityLivingBase entity = (EntityLivingBase)(Object)this;
        if (entity instanceof EntityPlayer && entity.height < 1.0F) {
            ci.cancel();
        }
    }
}