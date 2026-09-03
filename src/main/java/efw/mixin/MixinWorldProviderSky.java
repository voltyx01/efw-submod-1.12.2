package efw.mixin;

import com.voltyx.mwccf.sunmoon.RealisticSunMoon;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Mixin on net.minecraft.world.WorldProvider that ports B3M's
 * sunrise/sunset colour intensity adjustment.
 *
 * Corresponds to B3M_Transformer.transform7() patch on:
 *   - calcSunriseSunsetColors(FF)[F  — replaces 0.4f glow factor
 *
 * WorldProvider.calcSunriseSunsetColors() computes the orange/red glow
 * that colours the sky near the horizon at dawn and dusk.
 * Vanilla factor = 0.4f (quite strong).
 * B3M default    = 0.2f (more subtle, realistic).
 * Our default    = 0.3f (balanced).
 *
 * This is a @SideOnly(CLIENT) method, so the mixin must be in "client" section.
 */
@Mixin(WorldProvider.class)
public abstract class MixinWorldProviderSky {

    /**
     * Replace the 0.4f glow intensity constant in calcSunriseSunsetColors.
     *
     * The original formula uses this constant as an upper bound and a sign-flip
     * factor for the sunrise/sunset colour bands:
     * <pre>
     *   if (sunAngle < 0.4f) { ... }   ← threshold, NOT touched (different position)
     *   array[3] = f * 0.4f;           ← THIS is replaced (alpha/intensity)
     * </pre>
     *
     * require=0: if the constant position shifts due to other mods, skip gracefully.
     */
    @ModifyConstant(
        method = "calcSunriseSunsetColors(FF)[F",
        constant = @Constant(floatValue = 0.4f),
        require = 0
    )
    private float b3m$sunriseSunsetMod(float original) {
        return RealisticSunMoon.sunriseSunsetMod;
    }
}
