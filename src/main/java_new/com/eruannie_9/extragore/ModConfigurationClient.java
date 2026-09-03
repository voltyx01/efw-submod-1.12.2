/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.config.Config
 *  net.minecraftforge.common.config.Config$Comment
 *  net.minecraftforge.common.config.Config$LangKey
 *  net.minecraftforge.common.config.Config$Name
 *  net.minecraftforge.common.config.Config$RangeDouble
 */
package com.eruannie_9.extragore;

import net.minecraftforge.common.config.Config;

@Config(modid="mwccf", name="mwccf_config", category="extragore")
@Config.LangKey(value="config.extragore.client")
public class ModConfigurationClient {
    @Config.Name(value="ceiling")
    @Config.LangKey(value="config.extragore.blood_particles.ceiling")
    public static final CeilingSettings ceiling = new CeilingSettings();
    @Config.Name(value="fade")
    @Config.LangKey(value="config.extragore.blood_particles.fade")
    public static final FadeSettings fade = new FadeSettings();
    @Config.Name(value="health_scale")
    @Config.LangKey(value="config.extragore.blood_particles.health_scale")
    public static final HealthScaleSettings healthScale = new HealthScaleSettings();
    @Config.Name(value="damage_sources")
    @Config.LangKey(value="config.extragore.blood_particles.damage_sources")
    public static final DamageSourceSettings damageSources = new DamageSourceSettings();
    @Config.Name(value="hit_dispersion")
    @Config.LangKey(value="config.extragore.blood_particles.hit_dispersion")
    public static final HitDispersionSettings hitDispersion = new HitDispersionSettings();
    @Config.Name(value="ground")
    @Config.LangKey(value="config.extragore.blood_particles.ground")
    public static final GroundSettings ground = new GroundSettings();
    @Config.Name(value="wall")
    @Config.LangKey(value="config.extragore.blood_particles.wall")
    public static final WallSettings wall = new WallSettings();
    @Config.Name(value="hot_blocks")
    @Config.LangKey(value="config.extragore.blood_particles.hot_blocks")
    public static final HotBlockSettings hotBlocks = new HotBlockSettings();

    public static class HotBlockSettings {
        @Config.LangKey(value="config.extragore.blood_particles.hot_blocks.hot_blocks")
        @Config.Comment(value={"If true, hot blocks, listed in the blockList (Block List), will make the blood particle behave as if they were over the lava surface."})
        public boolean hotBlocks = true;
        @Config.LangKey(value="config.extragore.blood_particles.hot_blocks.block_list")
        @Config.Comment(value={"List of block registry names treated as hot.", "Examples include minecraft:magma, minecraft:lit_furnace, minecraft:lava and minecraft:flowing_lava."})
        public String[] blockList = new String[]{"minecraft:magma"};
    }

    public static class WallSettings {
        @Config.LangKey(value="config.extragore.blood_particles.wall.drip")
        @Config.Comment(value={"If true, blood particles on the wall can drip."})
        public boolean drip = true;
        @Config.LangKey(value="config.extragore.blood_particles.wall.block_list_mode")
        @Config.Comment(value={"Controls how blockList (Block List) is applied. NONE the list is not applied. BLACKLIST excludes listed blocks. WHITELIST allows only listed blocks."})
        public BlockListMode blockListMode = BlockListMode.NONE;
        @Config.LangKey(value="config.extragore.blood_particles.wall.block_list")
        @Config.Comment(value={"List of block registry names which can or cannot attach to the wall. If blockListMode (Block List Mode) is NONE, the list won't be considered.", "Examples include minecraft:glass, minecraft:ice and modid:your_block."})
        public String[] blockList = new String[0];
        @Config.LangKey(value="config.extragore.blood_particles.wall.stretch_min")
        @Config.Comment(value={"Minimum length for wall stretch."})
        @Config.RangeDouble(min=0.0, max=5.0)
        public double stretchMin = 0.75;
        @Config.LangKey(value="config.extragore.blood_particles.wall.stretch_max")
        @Config.Comment(value={"Maximum length for wall stretch."})
        @Config.RangeDouble(min=0.0, max=5.0)
        public double stretchMax = 1.35;
        @Config.LangKey(value="config.extragore.blood_particles.wall.slide_speed")
        @Config.Comment(value={"Slide speed over the wall per ticks. Set to 0.0 to disable sliding."})
        @Config.RangeDouble(min=0.0, max=0.05)
        public double slideSpeed = 0.003;
    }

    public static class GroundSettings {
        @Config.LangKey(value="config.extragore.blood_particles.ground.amalgamation")
        @Config.Comment(value={"If true, blood on top surfaces can merge into larger puddles."})
        public boolean amalgamation = true;
        @Config.LangKey(value="config.extragore.blood_particles.ground.spread_min")
        @Config.Comment(value={"Minimum size for ground and ceiling spread."})
        @Config.RangeDouble(min=0.0, max=5.0)
        public double spreadMin = 0.85;
        @Config.LangKey(value="config.extragore.blood_particles.ground.spread_max")
        @Config.Comment(value={"Maximum size for ground and ceiling spread."})
        @Config.RangeDouble(min=0.0, max=5.0)
        public double spreadMax = 1.25;
    }

    public static class HitDispersionSettings {
        @Config.LangKey(value="config.extragore.blood_particles.hit_dispersion.cone_range")
        @Config.Comment(value={"Horizontal cone width multiplier for directional blood spray. Lower values tighten the cone. Higher values widen it."})
        @Config.RangeDouble(min=0.0, max=5.0)
        public double coneRange = 2.0;
        @Config.LangKey(value="config.extragore.blood_particles.hit_dispersion.y_cone_range")
        @Config.Comment(value={"Vertical cone width multiplier for directional blood spray. Lower values flatten the splash arc. Higher values widen the vertical dispersion."})
        @Config.RangeDouble(min=0.0, max=5.0)
        public double yConeRange = 3.0;
        @Config.LangKey(value="config.extragore.blood_particles.hit_dispersion.force")
        @Config.Comment(value={"Force multiplier for directional blood spray velocity and vertical kick. Lower values soften the hit. Higher values throw particles further."})
        @Config.RangeDouble(min=0.0, max=5.0)
        public double force = 0.7;
        @Config.LangKey(value="config.extragore.blood_particles.hit_dispersion.fixed_count")
        @Config.Comment(value={"Fixed amount of blood particles to spawn per hit. Set to 0 to use dynamic count."})
        @Config.RangeInt(min=0, max=500)
        public int fixedCount = 10;
    }

    public static class DamageSourceSettings {
        @Config.Name(value="fire")
        @Config.LangKey(value="config.extragore.blood_particles.damage_sources.fire")
        @Config.Comment(value={"If true, blood can spawn from fire, lava and hot floor damage."})
        public boolean fire = false;
        @Config.Name(value="drowning")
        @Config.LangKey(value="config.extragore.blood_particles.damage_sources.drowning")
        @Config.Comment(value={"If true, blood can spawn from drowning damage."})
        public boolean drowning = false;
        @Config.Name(value="suffocation")
        @Config.LangKey(value="config.extragore.blood_particles.damage_sources.suffocation")
        @Config.Comment(value={"If true, blood can spawn from suffocation damage."})
        public boolean suffocation = false;
        @Config.Name(value="cactus")
        @Config.LangKey(value="config.extragore.blood_particles.damage_sources.cactus")
        @Config.Comment(value={"If true, blood can spawn from cactus damage."})
        public boolean cactus = false;
        @Config.Name(value="effects")
        @Config.LangKey(value="config.extragore.blood_particles.damage_sources.effects")
        @Config.Comment(value={"If true, blood can spawn from effect based damage such as magic, wither, dragon breath and starvation."})
        public boolean effects = false;
        @Config.Name(value="fall")
        @Config.LangKey(value="config.extragore.blood_particles.damage_sources.fall")
        @Config.Comment(value={"If true, blood can spawn from fall like damage such as fall impact, fly into wall, anvils and falling blocks."})
        public boolean fall = false;
        @Config.Name(value="lightning")
        @Config.LangKey(value="config.extragore.blood_particles.damage_sources.lightning")
        @Config.Comment(value={"If true, blood can spawn from lightning damage."})
        public boolean lightning = false;
        @Config.Name(value="void")
        @Config.LangKey(value="config.extragore.blood_particles.damage_sources.void")
        @Config.Comment(value={"If true, blood can spawn from void damage."})
        public boolean voidDamage = false;
    }

    public static class HealthScaleSettings {
        @Config.LangKey(value="config.extragore.blood_particles.health_scale.enable_size_reduction")
        @Config.Comment(value={"If true, blood particle size on hit is reduced on healthier entities and ramps toward full size as health gets lower."})
        public boolean enableSizeReduction = false;
        @Config.LangKey(value="config.extragore.blood_particles.health_scale.start_health_fraction")
        @Config.Comment(value={"Health fraction where the size ramp starts. At or above this value, sizeAtStart is used."})
        @Config.RangeDouble(min=0.0, max=1.0)
        public double startHealthFraction = 0.8;
        @Config.LangKey(value="config.extragore.blood_particles.health_scale.end_health_fraction")
        @Config.Comment(value={"Health fraction where the size ramp ends. At or below this value, sizeAtLow is used."})
        @Config.RangeDouble(min=0.0, max=1.0)
        public double endHealthFraction = 0.3;
        @Config.LangKey(value="config.extragore.blood_particles.health_scale.size_at_start")
        @Config.Comment(value={"Blood size multiplier used at or above startHealthFraction. Lower values make healthier targets spawn smaller blood particles."})
        @Config.RangeDouble(min=0.0, max=5.0)
        public double sizeAtStart = 0.2;
        @Config.LangKey(value="config.extragore.blood_particles.health_scale.size_at_low")
        @Config.Comment(value={"Blood size multiplier used at or below endHealthFraction."})
        @Config.RangeDouble(min=0.0, max=5.0)
        public double sizeAtLow = 1.0;
    }

    public static class FadeSettings {
        @Config.LangKey(value="config.extragore.blood_particles.fade.start_fade")
        @Config.Comment(value={"Controls when alpha fading starts. 0.0 fades from the initial spawn. 1.0 never fades."})
        @Config.RangeDouble(min=0.0, max=1.0)
        public double startFade = 0.15;
    }

    public static class CeilingSettings {
        @Config.LangKey(value="config.extragore.blood_particles.ceiling.stick")
        @Config.Comment(value={"If true, some blood styles can stick to the ceiling surfaces."})
        public boolean stick = true;
        @Config.LangKey(value="config.extragore.blood_particles.ceiling.block_list_mode")
        @Config.Comment(value={"Controls how blockList (Block List) is applied. NONE the list is not applied. BLACKLIST excludes listed blocks. WHITELIST allows only listed blocks."})
        public BlockListMode blockListMode = BlockListMode.NONE;
        @Config.LangKey(value="config.extragore.blood_particles.ceiling.block_list")
        @Config.Comment(value={"List of block registry names which can or cannot interact with the ceiling. If blockListMode (Block List Mode) is NONE, the list won't be considered.", "Examples include minecraft:stone, minecraft:concrete and modid:your_block."})
        public String[] blockList = new String[0];
    }

    public static enum BlockListMode {
        NONE,
        BLACKLIST,
        WHITELIST;

    }
}

