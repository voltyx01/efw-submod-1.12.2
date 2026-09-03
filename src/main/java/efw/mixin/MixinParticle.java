package efw.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.particle.Particle.class)
public class MixinParticle {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinParticle class loaded!");
    }

    private static final ResourceLocation MWCCF_PARTICLES = new ResourceLocation("mwccf", "textures/particles/particles.png");
    private static final ResourceLocation VANILLA_PARTICLES = new ResourceLocation("textures/particle/particles.png");

    @Inject(method = "renderParticle", at = @At("HEAD"))
    private void preRenderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        if (this.getClass().getName().equals("org.orecruncher.dsurround.client.fx.particle.ParticleFrostBreath")) {
            Tessellator.getInstance().draw();
            Minecraft.getMinecraft().getTextureManager().bindTexture(MWCCF_PARTICLES);
            buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        }
    }

    @Inject(method = "renderParticle", at = @At("RETURN"))
    private void postRenderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        if (this.getClass().getName().equals("org.orecruncher.dsurround.client.fx.particle.ParticleFrostBreath")) {
            Tessellator.getInstance().draw();
            Minecraft.getMinecraft().getTextureManager().bindTexture(VANILLA_PARTICLES);
            buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        }
    }
}
