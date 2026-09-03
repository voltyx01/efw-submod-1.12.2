package com.voltyx.mwccf.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomExplosionSmoke extends Particle {
    // путь к твоей текстуре из ресурспака, положи файл в
    // assets/yourmod/textures/particle/
    private static final ResourceLocation CUSTOM_TEXTURE = new ResourceLocation("mwccf",
            "textures/particle/particles.png");
    // ссылка на ванильную текстуру, чтобы вернуть биндинг после отрисовки нашего
    // партикла
    private static final ResourceLocation VANILLA_PARTICLES = new ResourceLocation("textures/particle/particles.png");

    float smokeParticleScale;

    public CustomExplosionSmoke(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_8_,
            double p_10_, double p_12_) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_8_, p_10_, p_12_, 1.0F);
    }

    protected CustomExplosionSmoke(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_8_,
            double p_10_, double p_12_, float p_14_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += p_8_;
        this.motionY += p_10_;
        this.motionZ += p_12_;
        float f = (float) (Math.random() * 0.30000001192092896D);
        this.particleRed = f;
        this.particleGreen = f;
        this.particleBlue = f;
        this.particleScale *= 0.75F;
        this.particleScale *= p_14_;
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int) ((float) this.particleMaxAge * p_14_);
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
            float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        this.particleScale = this.smokeParticleScale * f;

        Tessellator tessellator = Tessellator.getInstance();

        // 1. закрываем текущий батч ванильных партиклов
        tessellator.draw();

        // 2. биндим свою текстуру и открываем новый батч
        Minecraft.getMinecraft().getTextureManager().bindTexture(CUSTOM_TEXTURE);
        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

        // сама геометрия квада — берём готовую логику родителя,
        // она считает только UV/позицию по particleTextureIndex, к текстуре не
        // привязана
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

        // 3. закрываем наш батч
        tessellator.draw();

        // 4. возвращаем ванильную текстуру и открываем новый батч для остальных
        // партиклов
        Minecraft.getMinecraft().getTextureManager().bindTexture(VANILLA_PARTICLES);
        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.motionY += 0.004D;
        this.move(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }
        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;
        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
                double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new CustomExplosionSmoke(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}