package efw.client;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ParticleWhiteAsh extends Particle {
    private static final ResourceLocation TEXTURE = new ResourceLocation("mwccf", "textures/particles/particles.png");

    public ParticleWhiteAsh(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.motionX = (Math.random() * 2.0D - 1.0D) * 0.05D;
        this.motionY = (Math.random() * 2.0D - 1.0D) * 0.05D - 0.05D; // Starts falling slightly
        this.motionZ = (Math.random() * 2.0D - 1.0D) * 0.05D;
        this.particleRed = 0.9F;
        this.particleGreen = 0.9F;
        this.particleBlue = 0.9F;
        this.particleScale = 1.0F + (float) Math.random() * 0.5F; // Make them larger
        this.particleMaxAge = (int) (100.0D + Math.random() * 100.0D); // Live much longer (5-10 seconds)
        // particleTextureIndexX and Y default to 0, which is what we want for index 0.
        // setParticleTextureIndex(0) throws an exception if getFXLayer() != 0.
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
            float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(516, 0.003921569F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        Tessellator.getInstance().draw();

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.popMatrix();
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.particleAge++;
        if (this.particleAge >= this.particleMaxAge) {
            this.setExpired();
        }

        // Fade out at the end of life
        int fadeStart = this.particleMaxAge - 20;
        if (this.particleAge > fadeStart) {
            this.particleAlpha = 1.0F - ((float) (this.particleAge - fadeStart) / 20.0F);
        }

        this.motionY -= 0.002D; // Drift down slowly
        this.move(this.motionX, this.motionY, this.motionZ);

        this.motionX *= 0.95D;
        this.motionY *= 0.95D;
        this.motionZ *= 0.95D;

        // Add random horizontal drift
        this.motionX += (Math.random() - 0.5D) * 0.02D;
        this.motionZ += (Math.random() - 0.5D) * 0.02D;

        if (this.onGround) {
            this.motionX *= 0.7D;
            this.motionZ *= 0.7D;
        }
    }
}
