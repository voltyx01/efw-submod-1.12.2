package efw.mixin;

import com.voltyx.mwccf.particle.CustomExplosionSmoke;
import com.voltyx.mwccf.particle.CustomExplosionParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Explosion.class)
public class MixinExplosionParticles {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinExplosionParticles class loaded!");
    }
    @Redirect(method = "doExplosionB", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;DDDDDD[I)V"))
    private void efw_redirectSpawnParticle(World world, EnumParticleTypes particleType,
            double x, double y, double z,
            double xSpeed, double ySpeed, double zSpeed,
            int... extraArgs) {
        if (!world.isRemote) {
            return;
        }

        if (particleType == EnumParticleTypes.SMOKE_NORMAL) {
            CustomExplosionSmoke mySmoke = new CustomExplosionSmoke(world, x, y, z, xSpeed, ySpeed, zSpeed);
            Minecraft.getMinecraft().effectRenderer.addEffect(mySmoke);
        } else if (particleType == EnumParticleTypes.EXPLOSION_NORMAL) {
            CustomExplosionParticle myExplode = new CustomExplosionParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
            Minecraft.getMinecraft().effectRenderer.addEffect(myExplode);
        } else {
            world.spawnParticle(particleType, x, y, z, xSpeed, ySpeed, zSpeed, extraArgs);
        }
    }
}