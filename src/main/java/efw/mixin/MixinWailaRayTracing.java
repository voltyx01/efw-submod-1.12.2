package efw.mixin;

import com.teamderpy.shouldersurfing.client.ShoulderHelper;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.overlay.RayTracing;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RayTracing.class, remap = false)
public class MixinWailaRayTracing {

    @Inject(method = "rayTrace", at = @At("HEAD"), cancellable = true)
    private void onRayTrace(Entity entity, double reach, float partialTicks, CallbackInfoReturnable<RayTraceResult> cir) {
        if (ShoulderInstance.getInstance().doShoulderSurfing() && entity != null) {
            boolean liquid = ConfigHandler.instance().getConfig("general", "waila.cfg.liquid", true);
            float pTicks = Minecraft.getMinecraft().getRenderPartialTicks();
            RayTraceResult result = ShoulderHelper.traceBlocks(entity, liquid, reach, pTicks, true);
            cir.setReturnValue(result);
        }
    }
}
