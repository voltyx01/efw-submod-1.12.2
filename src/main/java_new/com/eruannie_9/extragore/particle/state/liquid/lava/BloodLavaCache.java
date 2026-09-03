/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.material.Material
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.state.liquid.lava;

import com.eruannie_9.extragore.particle.common.surface.BloodSurfaceFluid;
import com.eruannie_9.extragore.particle.state.liquid.BloodFluidSurfaceCache;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class BloodLavaCache
extends BloodFluidSurfaceCache {
    public BloodLavaCache(@Nullable World world) {
        super(world, Material.LAVA, 8, 256, 4, BloodSurfaceFluid.LAVA_OPEN_RULE);
    }
}

