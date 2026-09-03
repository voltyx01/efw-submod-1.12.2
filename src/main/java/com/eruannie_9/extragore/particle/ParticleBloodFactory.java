/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.client.particle.IParticleFactory
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle;

import com.eruannie_9.extragore.json.BloodAmalgamationPolicy;
import com.eruannie_9.extragore.json.BloodBrightnessMode;
import com.eruannie_9.extragore.json.BloodEntityConfig;
import com.eruannie_9.extragore.json.BloodStyle;
import com.eruannie_9.extragore.particle.ParticleBlood;
import com.eruannie_9.extragore.particle.common.Util;
import javax.annotation.Nonnull;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class ParticleBloodFactory
implements IParticleFactory {
    public Particle createParticle(int particleID, @Nonnull World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int ... args) {
        int variant = args.length > 0 ? args[0] : worldIn.rand.nextInt(4);
        int rgb = args.length > 1 ? args[1] : BloodEntityConfig.getDefaultRGB();
        int wArg = args.length > 2 ? args[2] : BloodEntityConfig.getDefaultStyle().toArg();
        int aArg = args.length > 3 ? args[3] : BloodEntityConfig.getDefaultAmalgamation().toArg();
        int bArg = args.length > 4 ? args[4] : BloodEntityConfig.getDefaultBrightness().toArg();
        variant = Math.floorMod(variant, 4);
        BloodStyle weight = BloodStyle.fromArg(wArg);
        BloodAmalgamationPolicy policy = BloodAmalgamationPolicy.fromArg(aArg);
        BloodBrightnessMode brightMode = BloodBrightnessMode.fromArg(bArg);
        float sMin = BloodEntityConfig.getDefaultScaleMin();
        float sMax = BloodEntityConfig.getDefaultScaleMax();
        if (args.length > 5) {
            sMin = Float.intBitsToFloat(args[5]);
        }
        if (args.length > 6) {
            sMax = Float.intBitsToFloat(args[6]);
        }
        Util.RangeF sr = BloodEntityConfig.sanitizeScaleRange(sMin, sMax);
        int lifeMin = BloodEntityConfig.getDefaultLifeMin();
        int lifeMax = BloodEntityConfig.getDefaultLifeMax();
        if (args.length > 7) {
            lifeMin = args[7];
        }
        if (args.length > 8) {
            lifeMax = args[8];
        }
        BloodEntityConfig.RangeI lr = BloodEntityConfig.sanitizeLifeRange(lifeMin, lifeMax);
        float dripChance01 = BloodEntityConfig.getDefaultDripChance();
        if (args.length > 9) {
            dripChance01 = Float.intBitsToFloat(args[9]);
        }
        dripChance01 = BloodEntityConfig.sanitizeDripChance(dripChance01, BloodEntityConfig.getDefaultDripChance());
        float viscosity01 = BloodEntityConfig.getDefaultViscosity();
        if (args.length > 10) {
            viscosity01 = Float.intBitsToFloat(args[10]);
        }
        viscosity01 = BloodEntityConfig.sanitizeViscosity(viscosity01, BloodEntityConfig.getDefaultViscosity());
        ParticleBlood p = new ParticleBlood(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, variant, weight, sr.min, sr.max, lr.min, lr.max, viscosity01);
        p.setTintRGB(rgb);
        p.setAmalgamationPolicy(policy);
        p.setDripChance01(dripChance01);
        p.setBrightnessMode(brightMode);
        return p;
    }
}

