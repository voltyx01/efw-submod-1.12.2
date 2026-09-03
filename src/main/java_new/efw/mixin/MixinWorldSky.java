package efw.mixin;

import com.voltyx.mwccf.sunmoon.RealisticSunMoon;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin on net.minecraft.world.World that hooks B3M's celestial angle
 * and sky brightness calculations.
 */
@Mixin(World.class)
public abstract class MixinWorldSky {

    @Shadow
    public WorldProvider provider;

    @Shadow
    public abstract long getWorldTime();

    /**
     * Replaces vanilla getCelestialAngle with B3M's realistic celestial curve.
     * This controls daytime/nighttime progression, skylight, star visibility,
     * and celestial object positions across the entire engine.
     */
    @Inject(
        method = "getCelestialAngle(F)F",
        at = @At("HEAD"),
        cancellable = true
    )
    private void b3m$getCelestialAngle(float partialTicks, CallbackInfoReturnable<Float> cir) {
        World world = (World)(Object)this;
        if (this.provider != null && this.provider.getDimension() == 0) {
            cir.setReturnValue(RealisticSunMoon.getModdedCelestialAngle(world, this.getWorldTime(), partialTicks));
        }
    }

    /**
     * World.getSunBrightnessFactor(float partialTicks) modifier.
     */
    @ModifyConstant(
        method = "getSunBrightnessFactor(F)F",
        constant = @Constant(floatValue = 0.5f),
        require = 0,
        remap = false
    )
    private float b3m$sunBrightnessFactor(float original) {
        return RealisticSunMoon.sunBrightnessFactor;
    }

    /**
     * World.getSkyColorBody(Entity, float) modifier.
     */
    @ModifyConstant(
        method = "getSkyColorBody(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/math/Vec3d;",
        constant = @Constant(floatValue = 0.5f),
        require = 0,
        remap = false
    )
    private float b3m$skyColorMod(float original) {
        return RealisticSunMoon.skyColorMod;
    }
}
