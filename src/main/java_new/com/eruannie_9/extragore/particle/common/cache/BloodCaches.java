/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.common.cache;

import com.eruannie_9.extragore.particle.common.Util;
import com.eruannie_9.extragore.particle.state.liquid.BloodFluidSurfaceCache;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class BloodCaches {

    public static final class WaterFrame {
        @Nullable
        public Entity view = null;
        @Nullable
        public World world = null;
        public long worldTime = Long.MIN_VALUE;
        public int partialTicksBits = Integer.MIN_VALUE;
        public boolean eyeInWater = false;
        public int waterTintRgb = 4159204;
    }

    public static final class Query {
        public final Map<Long, BloodFluidSurfaceCache.SurfaceData> hit = new HashMap<Long, BloodFluidSurfaceCache.SurfaceData>();
        public final Set<Long> miss = new HashSet<Long>();
    }

    public static final class WaterState
    extends LiquidState {
    }

    public static final class LavaState
    extends LiquidState {
    }

    public static class LiquidState {
        public final Billboard billboard = new Billboard();
        public final Track track = new Track();
    }

    public static final class Track {
        public int mask = 0;
        public boolean tracked = false;
    }

    public static final class Fade {
        public int modelStartAge = -1;
        public int modelTicks = 0;
        public int waterStartAge = -1;
        public int waterTicks = 0;
    }

    public static final class Support {
        public float frac = 1.0f;
        public boolean airBelow = false;
    }

    public static final class Gate {
        public boolean has = false;
        public boolean open = false;
        public byte part = 0;
    }

    public static final class Host {
        @Nullable
        public IBlockState base = null;
        @Nullable
        public AxisAlignedBB piece = null;
        public boolean poseSnapPrev = false;
        public int poseGraceTicks = 0;
    }

    public static final class View {
        public double x = Double.NaN;
        public double y = Double.NaN;
        public double z = Double.NaN;
    }

    public static final class Shape {
        @Nullable
        public List<List<Util.Vertex>> polys = null;
        public float scale = Float.NaN;
        public float drip = Float.NaN;
        public float amalgam = Float.NaN;
    }

    public static final class Billboard {
        public boolean valid = false;
        public float rotX;
        public float rotZ;
        public float rotYZ;
        public float rotXY;
        public float rotXZ;
        public double interpX;
        public double interpY;
        public double interpZ;
    }

    public static final class State {
        public final Billboard billboard = new Billboard();
        public final Shape shape = new Shape();
        public final View view = new View();
        public final Host host = new Host();
        public final Gate gate = new Gate();
        public final Support support = new Support();
        public final Fade fade = new Fade();
    }
}

