/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore;

import com.eruannie_9.extragore.BloodHitScaling;
import com.eruannie_9.extragore.ModConfigurationClient;
import com.eruannie_9.extragore.json.BloodAmalgamationPolicy;
import com.eruannie_9.extragore.json.BloodBrightnessMode;
import com.eruannie_9.extragore.json.BloodEntityConfig;
import com.eruannie_9.extragore.json.BloodStyle;
import com.eruannie_9.extragore.pack.BloodDamageKind;
import com.eruannie_9.extragore.pack.PacketBloodDamage;
import com.eruannie_9.extragore.particle.ParticleBlood;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BloodOnHitHandler {
    private static final BloodOnHitHandler INSTANCE = new BloodOnHitHandler();
    private static final double MAX_SPAWN_DISTANCE_SQ = 2304.0;
    private static final int PLAYER_ATTACK_CONTEXT_TICKS = 8;
    private static final double MAX_DIRECTIONAL_ATTACK_DIST_SQ = 100.0;
    private static final double HIT_RAY_LENGTH = 8.0;
    private static final float MIN_RUNTIME_PARTICLE_SCALE = 0.03f;
    private static final double PARTICLE_COUNT_JITTER_FRAC = 0.08;
    private static final double PARTICLE_COUNT_JITTER_MAX = 0.45;
    private static final double DAMAGE_ABSOLUTE_SOFT_MIN = 0.75;
    private static final double DAMAGE_ABSOLUTE_REFERENCE = 7.5;
    private static final double DAMAGE_FRACTION_REFERENCE = 0.35;
    private static final double DAMAGE_ABSOLUTE_WEIGHT = 0.82;
    private static final double DAMAGE_FRACTION_WEIGHT = 0.18;
    private static final double LIGHT_HIT_RESPONSE_REFERENCE = 1.0;
    private static final double LIGHT_HIT_RESPONSE_MAX = 0.22;
    private static final double OMNI_DAMAGE_RESPONSE_SCALE = 0.56;
    private static final double DIRECTIONAL_SURFACE_INSET_MIN = 0.01;
    private static final double DIRECTIONAL_SURFACE_INSET_MAX = 0.022;
    private static final double DIRECTIONAL_CENTER_PULL_MIN = 0.012;
    private static final double DIRECTIONAL_CENTER_PULL_MAX = 0.038;
    private static final double DIRECTIONAL_BASIS_SIDE_JITTER = 0.026;
    private static final double DIRECTIONAL_BASIS_UP_JITTER = 0.018;
    private static final double DIRECTIONAL_ORIGIN_JITTER_MIN = 0.003;
    private static final double DIRECTIONAL_ORIGIN_JITTER_FRAC = 0.01;
    private static final double DIRECTIONAL_PATCH_RIGHT_MIN = 0.01;
    private static final double DIRECTIONAL_PATCH_RIGHT_FRAC = 0.055;
    private static final double DIRECTIONAL_PATCH_UP_MIN = 0.008;
    private static final double DIRECTIONAL_PATCH_UP_FRAC = 0.045;
    private static final double LOW_DAMAGE_PATCH_SPREAD_BOOST = 1.08;
    private static final double DIRECTIONAL_SPEED_MIN = 0.22;
    private static final double DIRECTIONAL_SPEED_MAX = 0.48;
    private static final double DIRECTIONAL_SPEED_RANDOM = 0.14;
    private static final double DIRECTIONAL_SIDE_SPREAD_MIN = 0.018;
    private static final double DIRECTIONAL_SIDE_SPREAD_MAX = 0.075;
    private static final double DIRECTIONAL_UP_SPREAD_MIN = 0.01;
    private static final double DIRECTIONAL_UP_SPREAD_MAX = 0.036;
    private static final double DIRECTIONAL_UPWARD_BIAS_MIN = 0.006;
    private static final double DIRECTIONAL_UPWARD_BIAS_MAX = 0.018;
    private static final double DIRECTIONAL_AXIS_LIFT_MIN = 0.08;
    private static final double DIRECTIONAL_AXIS_LIFT_MAX = 0.16;
    private static final double DIRECTIONAL_PATCH_PUSH_MIN = 0.004;
    private static final double DIRECTIONAL_PATCH_PUSH_MAX = 0.016;
    private static final double DIRECTIONAL_VERTICAL_KICK_MIN = 0.014;
    private static final double DIRECTIONAL_VERTICAL_KICK_MAX = 0.05;
    private static final double DIRECTIONAL_CONE_MIN_DOT_LOW_DAMAGE = 0.88;
    private static final double DIRECTIONAL_CONE_MIN_DOT_HIGH_DAMAGE = 0.7;
    private static final double DIRECTIONAL_COUNT_SPREAD_REFERENCE = 12.0;
    private static final double DIRECTIONAL_COUNT_SPREAD_MAX_BOOST = 1.6;
    private static final double DIRECTIONAL_COUNT_PATCH_MAX_BOOST = 1.26;
    private static final double DIRECTIONAL_SAMPLE_JITTER = 0.08;
    private static final double GOLDEN_ANGLE = Math.PI * (3.0 - Math.sqrt(5.0));
    private static final double DIRECTIONAL_DISC_RANDOM_BLEND_MIN = 0.16;
    private static final double DIRECTIONAL_DISC_RANDOM_BLEND_MAX = 0.28;
    private static final double IMPACT_DISC_RANDOM_BLEND = 0.24;
    private static final double ORGANIC_DISC_RADIUS_EXPONENT = 1.15;
    private static final double DIRECTIONAL_FORWARD_SPAWN_JITTER_MIN = 0.0015;
    private static final double DIRECTIONAL_FORWARD_SPAWN_JITTER_MAX = 0.008;
    private static final double DIRECTIONAL_SIDE_SWAY_MIN = 0.0015;
    private static final double DIRECTIONAL_SIDE_SWAY_MAX = 0.006;
    private static final int IMPACT_BURST_COUNT_MIN = 1;
    private static final int IMPACT_BURST_COUNT_MAX = 4;
    private static final double IMPACT_BURST_COUNT_FROM_MAIN_COUNT_FRAC = 0.1;
    private static final double IMPACT_BURST_COUNT_FROM_MAIN_COUNT_MAX = 1.75;
    private static final double IMPACT_BURST_COUNT_HEALTH_MUL_MIN = 0.82;
    private static final double IMPACT_BURST_COUNT_HEALTH_MUL_MAX = 1.34;
    private static final double IMPACT_BURST_COUNT_CAP = 5.0;
    private static final double IMPACT_BURST_RADIUS_HEALTH_MUL_MIN = 0.92;
    private static final double IMPACT_BURST_RADIUS_HEALTH_MUL_MAX = 1.22;
    private static final double IMPACT_BURST_ARC_HEALTH_MUL_MIN = 0.96;
    private static final double IMPACT_BURST_ARC_HEALTH_MUL_MAX = 1.3;
    private static final double IMPACT_BURST_UPWARD_HEALTH_MUL_MIN = 1.0;
    private static final double IMPACT_BURST_UPWARD_HEALTH_MUL_MAX = 1.2;
    private static final double IMPACT_BURST_RADIUS_MIN = 0.006;
    private static final double IMPACT_BURST_RADIUS_XZ_FRAC = 0.02;
    private static final double IMPACT_BURST_RADIUS_Y_FRAC = 0.016;
    private static final double IMPACT_BURST_OUT_SPEED_MIN = 0.006;
    private static final double IMPACT_BURST_OUT_SPEED_MAX = 0.02;
    private static final double IMPACT_BURST_TANGENT_SPEED_MIN = 0.008;
    private static final double IMPACT_BURST_TANGENT_SPEED_MAX = 0.03;
    private static final double IMPACT_BURST_ARC_SIDE_MIN = 0.012;
    private static final double IMPACT_BURST_ARC_SIDE_MAX = 0.055;
    private static final double IMPACT_BURST_ARC_UP_MIN = 0.018;
    private static final double IMPACT_BURST_ARC_UP_MAX = 0.085;
    private static final double IMPACT_BURST_MIN_Y_SPEED_MIN = 0.01;
    private static final double IMPACT_BURST_MIN_Y_SPEED_MAX = 0.055;
    private static final double IMPACT_BURST_LOW_COUNT_WIDE_REFERENCE = 4.0;
    private static final double IMPACT_BURST_LOW_COUNT_RANDOM_BLEND_BOOST = 0.22;
    private static final double IMPACT_BURST_LOW_COUNT_ARC_BOOST = 0.55;
    private static final double IMPACT_BURST_HEMISPHERE_MIN_DOT_TIGHT = 0.28;
    private static final double IMPACT_BURST_HEMISPHERE_MIN_DOT_WIDE = 0.04;
    private static final double IMPACT_BURST_ROTATION_YAW_MIN = Math.toRadians(12.0);
    private static final double IMPACT_BURST_ROTATION_YAW_MAX = Math.toRadians(36.0);
    private static final double IMPACT_BURST_ROTATION_PITCH_MIN = Math.toRadians(14.0);
    private static final double IMPACT_BURST_ROTATION_PITCH_MAX = Math.toRadians(42.0);
    private static final double IMPACT_BURST_ROTATION_LOW_COUNT_BOOST = 1.35;
    private static final double IMPACT_BURST_ROTATION_RANDOM_BLEND = 0.3;
    private static final double IMPACT_BURST_ROTATION_ROLL_RANGE = Math.PI;
    private static final double IMPACT_BURST_SURFACE_INSET = 0.003;
    private static final double IMPACT_BURST_CARRY_MUL = 0.3;
    private static final float IMPACT_BURST_SCALE_MUL = 0.72f;
    private static final double IMPACT_POINT_POSITION_JITTER = 0.004;
    private static final double IMPACT_BURST_SOURCE_BIAS_NORMAL = 0.72;
    private static final double IMPACT_BURST_SOURCE_BIAS_TANGENT = 0.34;
    private static final double IMPACT_BURST_SOURCE_BIAS_BLEND = 0.18;
    private static final double IMPACT_BURST_LAUNCH_SIDE_MIN = 0.18;
    private static final double IMPACT_BURST_LAUNCH_SIDE_MAX = 0.58;
    private static final double IMPACT_BURST_LAUNCH_ORTHO_MIN = 0.05;
    private static final double IMPACT_BURST_LAUNCH_ORTHO_MAX = 0.22;
    private static final double IMPACT_BURST_LATERAL_VEL_MIN = 0.01;
    private static final double IMPACT_BURST_LATERAL_VEL_MAX = 0.048;
    private static final double IMPACT_BURST_SWIRL_VEL_MIN = 0.006;
    private static final double IMPACT_BURST_SWIRL_VEL_MAX = 0.03;
    private static final double IMPACT_BURST_AXIS_TWIST_MIN = Math.toRadians(8.0);
    private static final double IMPACT_BURST_AXIS_TWIST_MAX = Math.toRadians(34.0);
    private static final double DIRECTIONAL_COUNT_MUL_MIN = 0.4;
    private static final double DIRECTIONAL_COUNT_MUL_MAX = 1.0;
    private static final float DIRECTIONAL_SCALE_MUL_MIN = 0.72f;
    private static final float DIRECTIONAL_SCALE_MUL_MAX = 1.05f;
    private static final double OMNI_ORIGIN_XZ_MIN = 0.01;
    private static final double OMNI_ORIGIN_XZ_FRAC = 0.07;
    private static final double OMNI_ORIGIN_Y_JITTER_FRAC = 0.055;
    private static final double OMNI_SPAWN_RADIUS_MIN = 0.018;
    private static final double OMNI_SPAWN_RADIUS_XZ_FRAC = 0.095;
    private static final double OMNI_SPAWN_RADIUS_Y_FRAC = 0.11;
    private static final double OMNI_SPEED_MIN = 0.11;
    private static final double OMNI_SPEED_MAX = 0.22;
    private static final double OMNI_SPEED_RANDOM = 0.14;
    private static final double OMNI_RADIAL_PUSH_MIN = 0.008;
    private static final double OMNI_RADIAL_PUSH_MAX = 0.038;
    private static final double OMNI_UPWARD_BIAS_MIN = 0.006;
    private static final double OMNI_UPWARD_BIAS_MAX = 0.02;
    private static final double OMNI_RADIAL_DIR_WEIGHT = 0.7;
    private static final double OMNI_RADIAL_WEIGHT_JITTER = 0.07;
    private static final double OMNI_POSITION_CENTER_BIAS_EXPONENT = 1.08;
    private static final double OMNI_COUNT_MUL_MIN = 0.18;
    private static final double OMNI_COUNT_MUL_MAX = 0.62;
    private static final float OMNI_SCALE_MUL_MIN = 0.68f;
    private static final float OMNI_SCALE_MUL_MAX = 0.9f;
    private static final double OMNI_RADIUS_SCALE_MIN = 0.7;
    private static final double OMNI_RADIUS_SCALE_MAX = 0.92;
    private static final double DROPLET_ENERGY_BASE_MIN = 0.98;
    private static final double DROPLET_ENERGY_BASE_MAX = 1.03;
    private static final double DROPLET_ENERGY_EDGE_MIN = 0.99;
    private static final double DROPLET_ENERGY_EDGE_MAX = 1.05;
    private static final double DROPLET_HEAVY_CHANCE = 0.16;
    private static final double DROPLET_HEAVY_MIN = 0.84;
    private static final double DROPLET_HEAVY_MAX = 0.92;
    private static final double DROPLET_FINE_CHANCE = 0.06;
    private static final double DROPLET_FINE_MIN = 1.05;
    private static final double DROPLET_FINE_MAX = 1.12;
    private static final double TARGET_MOTION_CARRY_XZ = 0.18;
    private static final double TARGET_MOTION_CARRY_Y = 0.08;
    private static final double ATTACKER_MOTION_CARRY_XZ = 0.08;
    private static final double ATTACKER_MOTION_CARRY_Y = 0.04;
    private static final double SPAWN_INSIDE_EPS = 0.001;
    private final Map<Integer, AttackContext> pendingPlayerAttacks = new HashMap<Integer, AttackContext>();

    private BloodOnHitHandler() {
    }

    public static BloodOnHitHandler getInstance() {
        return INSTANCE;
    }

    public static void handleSyncedDamage(@Nonnull PacketBloodDamage message) {
        INSTANCE.handleSyncedDamage0(message);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = event.getEntityPlayer();
        if (player == null || mc.player == null) {
            return;
        }
        if (player != mc.player) {
            return;
        }
        if (player.world == null || !player.world.isRemote) {
            return;
        }
        if (!(event.getTarget() instanceof EntityLivingBase)) {
            return;
        }
        EntityLivingBase target = (EntityLivingBase)event.getTarget();
        if (target.isDead) {
            return;
        }
        AxisAlignedBB bb = target.getEntityBoundingBox();
        if (bb == null) {
            return;
        }
        Vec3d eyePos = player.getPositionEyes(1.0f);
        Vec3d lookDir = BloodOnHitHandler.normalizeOrFallback(player.getLook(1.0f), BloodOnHitHandler.getBoxCenter(bb).subtract(eyePos));
        Vec3d hitPos = BloodOnHitHandler.resolveHitPoint(bb, eyePos, lookDir, null);
        this.pendingPlayerAttacks.put(target.getEntityId(), new AttackContext(eyePos, lookDir, new Vec3d(player.motionX, player.motionY, player.motionZ), hitPos, 8, false));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) {
            this.pendingPlayerAttacks.clear();
            return;
        }
        Iterator<Map.Entry<Integer, AttackContext>> it = this.pendingPlayerAttacks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, AttackContext> e = it.next();
            Entity ent = mc.world.getEntityByID(e.getKey().intValue());
            if (!(ent instanceof EntityLivingBase) || ent.isDead) {
                it.remove();
                continue;
            }
            AttackContext ctx = e.getValue();
            --ctx.ticksRemaining;
            if (ctx.ticksRemaining > 0) continue;
            it.remove();
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            this.pendingPlayerAttacks.clear();
        }
    }

    private void handleSyncedDamage0(@Nonnull PacketBloodDamage message) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) {
            this.pendingPlayerAttacks.remove(message.getEntityId());
            return;
        }
        Entity ent = mc.world.getEntityByID(message.getEntityId());
        if (!(ent instanceof EntityLivingBase) || ent.isDead) {
            this.pendingPlayerAttacks.remove(message.getEntityId());
            return;
        }
        HitDamageContext hitDamage = new HitDamageContext(message.getPreHealth(), message.getPostHealth(), message.getMaxHealth(), message.getIncomingDamage());
        if (hitDamage.impactDamageTaken <= 1.0E-4f) {
            this.pendingPlayerAttacks.remove(message.getEntityId());
            return;
        }
        if (BloodOnHitHandler.shouldSkipBloodForKind(message.getKind())) {
            this.pendingPlayerAttacks.remove(message.getEntityId());
            return;
        }
        AttackContext attackCtx = this.pendingPlayerAttacks.remove(message.getEntityId());
        if (attackCtx == null && !BloodOnHitHandler.isNeutralDamageKind(message.getKind()) && message.hasDirectionalData()) {
            Vec3d sourcePos = message.getSourcePos();
            Vec3d sourceLookDir = message.getSourceLookDir();
            if (sourcePos != null && sourceLookDir != null) {
                attackCtx = new AttackContext(sourcePos, sourceLookDir, message.getSourceMotion(), null, 0, true);
            }
        }
        this.spawnBloodAtEntity((EntityLivingBase)ent, attackCtx, hitDamage, message.getKind());
    }

    private static boolean shouldSkipBloodForKind(@Nonnull BloodDamageKind kind) {
        ModConfigurationClient.DamageSourceSettings cfg = ModConfigurationClient.damageSources;
        switch (kind) {
            case FIRE: {
                return !cfg.fire;
            }
            case DROWNING: {
                return !cfg.drowning;
            }
            case SUFFOCATION: {
                return !cfg.suffocation;
            }
            case CACTUS: {
                return !cfg.cactus;
            }
            case EFFECTS: {
                return !cfg.effects;
            }
            case FALL: {
                return !cfg.fall;
            }
            case LIGHTNING: {
                return !cfg.lightning;
            }
            case VOID: {
                return !cfg.voidDamage;
            }
        }
        return false;
    }

    private static boolean isNeutralDamageKind(@Nonnull BloodDamageKind kind) {
        switch (kind) {
            case FIRE: 
            case DROWNING: 
            case SUFFOCATION: 
            case CACTUS: 
            case EFFECTS: 
            case FALL: 
            case LIGHTNING: 
            case VOID: {
                return true;
            }
        }
        return false;
    }

    private static double buildCountMultiplier(double damage01, double minMul, double maxMul) {
        return BloodOnHitHandler.lerp(minMul, maxMul, BloodOnHitHandler.smooth01(damage01));
    }

    private static double getConeRangeMul() {
        return BloodOnHitHandler.sanitizeDispersionMultiplier(ModConfigurationClient.hitDispersion.coneRange, 1.0);
    }

    private static double getYConeRangeMul() {
        return BloodOnHitHandler.sanitizeDispersionMultiplier(ModConfigurationClient.hitDispersion.yConeRange, 1.0);
    }

    private static double getForceMul() {
        return BloodOnHitHandler.sanitizeDispersionMultiplier(ModConfigurationClient.hitDispersion.force, 1.0);
    }

    private static double sanitizeDispersionMultiplier(double value, double fallback) {
        if (!Double.isFinite(value)) {
            return fallback;
        }
        return Math.max(0.0, value);
    }

    private static DamageResponse buildDamageResponse(HitDamageContext hitDamage) {
        double absolute01 = BloodOnHitHandler.smooth01(BloodOnHitHandler.clamp(((double)hitDamage.impactDamageTaken - 0.75) / Math.max(1.0E-6, 6.75), 0.0, 1.0));
        double fractional01 = BloodOnHitHandler.smooth01(BloodOnHitHandler.clamp(hitDamage.impactDamageFraction / 0.35, 0.0, 1.0));
        double damage01 = BloodOnHitHandler.clamp(absolute01 * 0.82 + fractional01 * 0.18, 0.0, 1.0);
        double lightHitFloor = 0.22 * BloodOnHitHandler.smooth01(BloodOnHitHandler.clamp((double)hitDamage.impactDamageTaken / 1.0, 0.0, 1.0));
        damage01 = Math.max(damage01, lightHitFloor);
        double omni01 = BloodOnHitHandler.clamp(damage01 * 0.56, 0.0, 1.0);
        double coneRangeMul = BloodOnHitHandler.getConeRangeMul();
        double yConeRangeMul = BloodOnHitHandler.getYConeRangeMul();
        double forceMul = BloodOnHitHandler.getForceMul();
        double directionalCountMul = BloodOnHitHandler.buildCountMultiplier(damage01, 0.4, 1.0);
        double omniCountMul = BloodOnHitHandler.buildCountMultiplier(omni01, 0.18, 0.62);
        return new DamageResponse(damage01, directionalCountMul, BloodOnHitHandler.lerpF(0.72f, 1.05f, damage01), BloodOnHitHandler.lerp(0.01, 0.022, damage01), BloodOnHitHandler.lerp(0.012, 0.038, damage01), BloodOnHitHandler.lerp(0.22, 0.48, damage01) * forceMul, BloodOnHitHandler.lerp(0.018, 0.075, damage01) * coneRangeMul, BloodOnHitHandler.lerp(0.01, 0.036, damage01) * yConeRangeMul, BloodOnHitHandler.lerp(0.006, 0.018, damage01) * yConeRangeMul, BloodOnHitHandler.lerp(0.08, 0.16, damage01) * yConeRangeMul, BloodOnHitHandler.lerp(0.004, 0.016, damage01) * coneRangeMul, BloodOnHitHandler.lerp(0.014, 0.05, damage01) * forceMul * yConeRangeMul, BloodOnHitHandler.lerp(0.88, 0.7, damage01), omniCountMul, BloodOnHitHandler.lerpF(0.68f, 0.9f, omni01), BloodOnHitHandler.lerp(0.11, 0.22, omni01), BloodOnHitHandler.lerp(0.008, 0.038, omni01), BloodOnHitHandler.lerp(0.006, 0.02, omni01), BloodOnHitHandler.lerp(0.7, 0.92, omni01));
    }

    private void spawnBloodAtEntity(EntityLivingBase target, @Nullable AttackContext attackCtx, HitDamageContext hitDamage, BloodDamageKind damageKind) {
        ImpactBurstProfile burst;
        ImpactBasis impact;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null) {
            return;
        }
        World world = target.world;
        if (world == null || !world.isRemote) {
            return;
        }
        Entity view = mc.getRenderViewEntity();
        if (view != null && target.getDistanceSq(view) > 2304.0) {
            return;
        }
        BloodEntityConfig.Values cfg = BloodEntityConfig.getValues((Entity)target);
        BloodHitScaling.Resolved resolved = BloodHitScaling.resolve(target, cfg, hitDamage.postHealth, hitDamage.maxHealth);
        if (resolved == null) {
            return;
        }
        AxisAlignedBB bb = target.getEntityBoundingBox();
        if (bb == null) {
            return;
        }
        Random rand = world.rand;
        DamageResponse damage = BloodOnHitHandler.buildDamageResponse(hitDamage);
        boolean directional = attackCtx != null && !BloodOnHitHandler.isNeutralDamageKind(damageKind);
        double motionCarryX = target.motionX * 0.18;
        double motionCarryY = target.motionY * 0.08;
        double motionCarryZ = target.motionZ * 0.18;
        if (directional) {
            motionCarryX += attackCtx.attackerMotion.x * 0.08;
            motionCarryY += attackCtx.attackerMotion.y * 0.04;
            motionCarryZ += attackCtx.attackerMotion.z * 0.08;
        }
        ImpactBasis impactBasis = impact = directional ? BloodOnHitHandler.buildDirectionalImpact(bb, attackCtx, damage, rand) : null;
        if (impact == null) {
            directional = false;
            impact = BloodOnHitHandler.buildOmniBurstImpact(bb, damageKind, rand);
        }
        int rgb = cfg.rgb;
        BloodStyle style = cfg.style;
        BloodAmalgamationPolicy policy = cfg.amalgamation;
        BloodBrightnessMode brightness = cfg.brightness;
        int dripBits = Float.floatToRawIntBits(cfg.dripChance);
        int viscosityBits = Float.floatToRawIntBits(cfg.viscosity);
        double mainCountExact = resolved.countExact * (directional ? damage.directionalCountMultiplier : damage.omniCountMultiplier);
        int particleCount = ModConfigurationClient.hitDispersion.fixedCount > 0 ? ModConfigurationClient.hitDispersion.fixedCount : BloodOnHitHandler.sampleParticleCount(mainCountExact, rand);
        if (particleCount <= 0) {
            return;
        }
        ScaleBits scaleBits = BloodOnHitHandler.buildScaleBits(resolved, directional ? damage.directionalScaleMultiplier : damage.omniScaleMultiplier);
        if (directional && impact.impactPoint != null && impact.impactNormal != null && (burst = BloodOnHitHandler.buildImpactBurstProfile(resolved, hitDamage, damage, mainCountExact)) != null) {
            this.spawnImpactPointBurst(mc, bb, resolved, damage, burst, impact, rgb, style, policy, brightness, cfg.lifeMin, cfg.lifeMax, dripBits, viscosityBits, motionCarryX, motionCarryY, motionCarryZ, rand);
        }
        for (int i = 0; i < particleCount; ++i) {
            ParticleSpec p = directional ? BloodOnHitHandler.createDirectionalParticle(impact, bb, resolved.sizeProfile, damage, motionCarryX, motionCarryY, motionCarryZ, rand, i, particleCount) : BloodOnHitHandler.createOmniParticle(impact, bb, resolved.sizeProfile, damage, motionCarryX, motionCarryY, motionCarryZ, rand);
            int variant = rand.nextInt(4);
            Particle fx = mc.effectRenderer.spawnEffectParticle(2000, p.x, p.y, p.z, p.vx, p.vy, p.vz, new int[]{variant, rgb, style.toArg(), policy.toArg(), brightness.toArg(), scaleBits.minBits, scaleBits.maxBits, cfg.lifeMin, cfg.lifeMax, dripBits, viscosityBits});
            if (!(fx instanceof ParticleBlood)) continue;
            ((ParticleBlood)fx).setExactMotion(p.vx, p.vy, p.vz);
        }
    }

    private static int sampleParticleCount(double exactCount, Random rand) {
        if (exactCount <= 0.0) {
            return 0;
        }
        double jitter = Math.min(0.45, exactCount * 0.08);
        if (jitter > 0.0) {
            exactCount = Math.max(0.0, exactCount + BloodOnHitHandler.randSigned(rand) * jitter);
        }
        int whole = (int)Math.floor(exactCount);
        double fraction = exactCount - (double)whole;
        if (rand.nextDouble() < fraction) {
            ++whole;
        }
        return whole;
    }

    private static ScaleBits buildScaleBits(BloodHitScaling.Resolved resolved, float scaleMul) {
        float min = Math.max(0.03f, resolved.scaleMin * scaleMul);
        float max = Math.max(min, resolved.scaleMax * scaleMul);
        return new ScaleBits(Float.floatToRawIntBits(min), Float.floatToRawIntBits(max));
    }

    @Nullable
    private static ImpactBurstProfile buildImpactBurstProfile(BloodHitScaling.Resolved resolved, HitDamageContext hitDamage, DamageResponse damage, double mainDirectionalCountExact) {
        if (mainDirectionalCountExact <= 1.0E-4) {
            return null;
        }
        double healthFrac = BloodOnHitHandler.clamp(hitDamage.postHealth / Math.max(1.0f, hitDamage.maxHealth), 0.0, 1.0);
        double missingHealth01 = BloodOnHitHandler.smooth01(1.0 - healthFrac);
        double sizeCountMul = 1.0;
        double sizeRadiusMul = 1.0;
        double sizeArcMul = 1.0;
        double sizeUpwardMul = 1.0;
        switch (resolved.sizeProfile) {
            case SMALL: {
                sizeCountMul = 0.88;
                sizeRadiusMul = 0.9;
                sizeArcMul = 0.96;
                sizeUpwardMul = 1.02;
                break;
            }
            case LARGE: {
                sizeCountMul = 1.18;
                sizeRadiusMul = 1.18;
                sizeArcMul = 1.12;
                sizeUpwardMul = 1.1;
                break;
            }
        }
        double baseCount = BloodOnHitHandler.lerp(1.0, 4.0, damage.damage01);
        double countFromMain = Math.min(1.75, mainDirectionalCountExact * 0.1);
        double countExact = BloodOnHitHandler.clamp((baseCount + countFromMain) * BloodOnHitHandler.lerp(0.82, 1.34, missingHealth01) * sizeCountMul, 1.0, 5.0);
        double radiusMultiplier = sizeRadiusMul * BloodOnHitHandler.lerp(0.92, 1.22, missingHealth01);
        double arcMultiplier = sizeArcMul * BloodOnHitHandler.lerp(0.96, 1.3, missingHealth01);
        double upwardMultiplier = sizeUpwardMul * BloodOnHitHandler.lerp(1.0, 1.2, missingHealth01);
        return new ImpactBurstProfile(countExact, radiusMultiplier, arcMultiplier, upwardMultiplier);
    }

    private void spawnImpactPointBurst(Minecraft mc, AxisAlignedBB bb, BloodHitScaling.Resolved resolved, DamageResponse damage, @Nullable ImpactBurstProfile burst, ImpactBasis impact, int rgb, BloodStyle style, BloodAmalgamationPolicy policy, BloodBrightnessMode brightness, int lifeMin, int lifeMax, int dripBits, int viscosityBits, double motionCarryX, double motionCarryY, double motionCarryZ, Random rand) {
        if (burst == null) {
            return;
        }
        if (impact.impactPoint == null || impact.impactNormal == null) {
            return;
        }
        Random burstRand = new Random(BloodOnHitHandler.buildImpactBurstSeed(impact, burst, damage, rand));
        double burstCountExact = Math.max(1.0, burst.countExact + BloodOnHitHandler.randSigned(burstRand) * (0.2 + 0.3 * damage.damage01));
        int impactCount = ModConfigurationClient.hitDispersion.fixedCount > 0 ? 0 : BloodOnHitHandler.sampleParticleCount(burstCountExact, burstRand);
        if (impactCount <= 0) {
            return;
        }
        double burstPhase = burstRand.nextDouble() * (Math.PI * 2);
        int burstIndexOffset = impactCount > 1 ? burstRand.nextInt(impactCount) : 0;
        ScaleBits impactScaleBits = BloodOnHitHandler.buildScaleBits(resolved, damage.directionalScaleMultiplier * 0.72f);
        for (int i = 0; i < impactCount; ++i) {
            int sampleIndex = impactCount > 1 ? (i + burstIndexOffset) % impactCount : 0;
            ParticleSpec p = BloodOnHitHandler.createImpactPointParticle(impact, bb, resolved.sizeProfile, damage, burst, motionCarryX, motionCarryY, motionCarryZ, burstRand, sampleIndex, impactCount, burstPhase);
            int variant = burstRand.nextInt(4);
            Particle fx = mc.effectRenderer.spawnEffectParticle(2000, p.x, p.y, p.z, p.vx, p.vy, p.vz, new int[]{variant, rgb, style.toArg(), policy.toArg(), brightness.toArg(), impactScaleBits.minBits, impactScaleBits.maxBits, lifeMin, lifeMax, dripBits, viscosityBits});
            if (!(fx instanceof ParticleBlood)) continue;
            ((ParticleBlood)fx).setExactMotion(p.vx, p.vy, p.vz);
        }
    }

    @Nullable
    private static ImpactBasis buildDirectionalImpact(AxisAlignedBB bb, @Nullable AttackContext ctx, DamageResponse damage, Random rand) {
        if (ctx == null) {
            return null;
        }
        Vec3d center = BloodOnHitHandler.getBoxCenter(bb);
        if (!ctx.authoritativeDirectional && ctx.attackerEyePos.squareDistanceTo(center) > 100.0 && ctx.hitVec == null) {
            return null;
        }
        HitSample hit = BloodOnHitHandler.resolveHitSample(bb, ctx.attackerEyePos, ctx.attackerLookDir, ctx.hitVec);
        double width = Math.max(0.15, bb.maxX - bb.minX);
        double height = Math.max(0.15, bb.maxY - bb.minY);
        double depth = Math.max(0.15, bb.maxZ - bb.minZ);
        double horizontalSize = Math.max(width, depth);
        double hitHeight01 = BloodOnHitHandler.clamp((hit.hitPos.y - bb.minY) / height, 0.0, 1.0);
        double lowHit01 = 1.0 - hitHeight01;
        Vec3d attackDir = BloodOnHitHandler.normalizeOrFallback(hit.hitPos.subtract(ctx.attackerEyePos), ctx.attackerLookDir);
        Vec3d attackRight = BloodOnHitHandler.normalizeOrFallback(new Vec3d(0.0, 1.0, 0.0).crossProduct(attackDir), new Vec3d(1.0, 0.0, 0.0));
        Vec3d attackUp = BloodOnHitHandler.normalizeOrFallback(attackDir.crossProduct(attackRight), new Vec3d(0.0, 1.0, 0.0));
        Vec3d insideStart = BloodOnHitHandler.clampInsideBox(hit.hitPos.add(attackDir.scale(Math.max(0.003, damage.directionalSurfaceInset * 0.5))), bb);
        Vec3d exitPoint = BloodOnHitHandler.computeExitPoint(bb, insideStart, attackDir);
        double dirSideJitter = 0.026 * (0.85 + 0.35 * damage.damage01);
        double dirUpJitter = 0.018 * (0.85 + 0.35 * damage.damage01);
        Vec3d preferredDir = BloodOnHitHandler.normalizeOrFallback(attackDir.add(new Vec3d(0.0, 0.03 + 0.08 * lowHit01, 0.0)).add(attackRight.scale(BloodOnHitHandler.randSigned(rand) * dirSideJitter)).add(attackUp.scale(BloodOnHitHandler.randSigned(rand) * dirUpJitter)), attackDir);
        double originJitterXZ = Math.max(0.003, horizontalSize * 0.01) * (0.8 + 0.2 * damage.damage01);
        double originJitterY = Math.max(0.003, height * 0.0085) * (0.8 + 0.2 * damage.damage01);
        Vec3d origin = BloodOnHitHandler.clampInsideBox(exitPoint.subtract(attackDir.scale(damage.directionalSurfaceInset + damage.directionalCenterPull)).add(attackRight.scale(BloodOnHitHandler.randSigned(rand) * originJitterXZ)).add(attackUp.scale(BloodOnHitHandler.randSigned(rand) * originJitterY)), bb);
        return BloodOnHitHandler.buildImpactBasis(origin, preferredDir, hit.hitPos, hit.surfaceNormal);
    }

    @Nonnull
    private static ImpactBasis buildOmniBurstImpact(AxisAlignedBB bb, BloodDamageKind kind, Random rand) {
        Vec3d center = BloodOnHitHandler.getBoxCenter(bb);
        double width = Math.max(0.15, bb.maxX - bb.minX);
        double height = Math.max(0.15, bb.maxY - bb.minY);
        double depth = Math.max(0.15, bb.maxZ - bb.minZ);
        double horizontalSize = Math.max(width, depth);
        double minYFrac = 0.3;
        double maxYFrac = 0.72;
        switch (kind) {
            case FALL: {
                minYFrac = 0.1;
                maxYFrac = 0.3;
                break;
            }
            case LIGHTNING: {
                minYFrac = 0.48;
                maxYFrac = 0.92;
                break;
            }
            case EFFECTS: {
                minYFrac = 0.34;
                maxYFrac = 0.68;
                break;
            }
            case DROWNING: {
                minYFrac = 0.34;
                maxYFrac = 0.78;
                break;
            }
            case VOID: {
                minYFrac = 0.18;
                maxYFrac = 0.6;
                break;
            }
            case FIRE: {
                minYFrac = 0.28;
                maxYFrac = 0.72;
                break;
            }
        }
        double originJitterXZ = Math.max(0.01, horizontalSize * 0.07);
        double y = bb.minY + height * BloodOnHitHandler.randRange(rand, minYFrac, maxYFrac);
        Vec3d origin = new Vec3d(center.x + BloodOnHitHandler.randSigned(rand) * originJitterXZ, BloodOnHitHandler.clamp(y += BloodOnHitHandler.randSigned(rand) * (height * 0.055), bb.minY, bb.maxY), center.z + BloodOnHitHandler.randSigned(rand) * originJitterXZ);
        origin = BloodOnHitHandler.clampInsideBox(origin, bb);
        Vec3d forward = BloodOnHitHandler.normalizeOrFallback(new Vec3d(BloodOnHitHandler.randSigned(rand), BloodOnHitHandler.randSigned(rand) * 0.15, BloodOnHitHandler.randSigned(rand)), new Vec3d(0.0, 0.0, 1.0));
        return BloodOnHitHandler.buildImpactBasis(origin, forward);
    }

    private static ImpactBasis buildImpactBasis(Vec3d origin, Vec3d forward) {
        return BloodOnHitHandler.buildImpactBasis(origin, forward, null, null);
    }

    private static ImpactBasis buildImpactBasis(Vec3d origin, Vec3d forward, @Nullable Vec3d impactPoint, @Nullable Vec3d impactNormal) {
        Vec3d dir = BloodOnHitHandler.normalizeOrFallback(forward, new Vec3d(0.0, 0.0, 1.0));
        Vec3d refUp = Math.abs(dir.y) > 0.92 ? new Vec3d(1.0, 0.0, 0.0) : new Vec3d(0.0, 1.0, 0.0);
        Vec3d right = BloodOnHitHandler.normalizeOrFallback(refUp.crossProduct(dir), new Vec3d(1.0, 0.0, 0.0));
        Vec3d up = BloodOnHitHandler.normalizeOrFallback(dir.crossProduct(right), new Vec3d(0.0, 1.0, 0.0));
        return new ImpactBasis(origin, dir, right, up, impactPoint, impactNormal);
    }

    private static ParticleSpec createDirectionalParticle(ImpactBasis impact, AxisAlignedBB bb, BloodHitScaling.SizeProfile sizeProfile, DamageResponse damage, double motionCarryX, double motionCarryY, double motionCarryZ, Random rand, int particleIndex, int particleCount) {
        double width = Math.max(0.15, bb.maxX - bb.minX);
        double height = Math.max(0.15, bb.maxY - bb.minY);
        double depth = Math.max(0.15, bb.maxZ - bb.minZ);
        double horizontalSize = Math.max(width, depth);
        double coneRangeMul = BloodOnHitHandler.getConeRangeMul();
        double yConeRangeMul = BloodOnHitHandler.getYConeRangeMul();
        double count01 = BloodOnHitHandler.smooth01(BloodOnHitHandler.clamp(((double)particleCount - 1.0) / Math.max(1.0, 11.0), 0.0, 1.0));
        double coneSpreadBoost = BloodOnHitHandler.lerp(1.0, 1.6, count01);
        double patchSpreadBoost = BloodOnHitHandler.lerp(1.0, 1.26, count01);
        double patchBoost = BloodOnHitHandler.lerp(1.08, 1.0, damage.damage01) * patchSpreadBoost;
        double patchRight = Math.min(Math.max(0.01, horizontalSize * 0.055) * (double)sizeProfile.splashRadiusMultiplier * patchBoost * coneRangeMul, horizontalSize * 0.22);
        double patchUp = Math.min(Math.max(0.008, height * 0.045) * (double)sizeProfile.splashRadiusMultiplier * patchBoost * yConeRangeMul, height * 0.14);
        double discBlend = BloodOnHitHandler.lerp(0.28, 0.16, count01);
        DiscSample sample = BloodOnHitHandler.sampleOrganicDisc(particleIndex, particleCount, rand, discBlend);
        double patchSide = sample.x * patchRight;
        double patchLift = sample.y * patchUp;
        double side01 = sample.x;
        double up01 = sample.y;
        double radial01 = sample.radial01;
        double forwardJitter = BloodOnHitHandler.randRange(rand, 0.0015, 0.008) * (0.6 + 0.4 * radial01);
        double localSway = BloodOnHitHandler.lerp(0.0015, 0.006, radial01);
        Vec3d spawnPos = BloodOnHitHandler.clampInsideBox(impact.origin.add(impact.right.scale(patchSide + BloodOnHitHandler.randSigned(rand) * localSway)).add(impact.up.scale(patchLift + BloodOnHitHandler.randSigned(rand) * localSway * 0.7)).add(impact.forward.scale(BloodOnHitHandler.randSigned(rand) * forwardJitter)), bb);
        double hitHeight01 = BloodOnHitHandler.clamp((spawnPos.y - bb.minY) / Math.max(0.15, height), 0.0, 1.0);
        double lowHit01 = 1.0 - hitHeight01;
        double sideScatter = side01 * damage.directionalSideSpread * (double)sizeProfile.splashRadiusMultiplier * coneSpreadBoost;
        sideScatter += BloodOnHitHandler.randSigned(rand) * damage.directionalSideSpread * 0.12 * coneSpreadBoost;
        double upScatter = up01 * damage.directionalUpSpread * (double)sizeProfile.splashRadiusMultiplier * coneSpreadBoost * 0.85;
        upScatter += BloodOnHitHandler.randSigned(rand) * damage.directionalUpSpread * 0.1 * coneSpreadBoost;
        Vec3d localPatch = impact.right.scale(side01).add(impact.up.scale(up01 * 0.55));
        Vec3d localPatchDir = BloodOnHitHandler.normalizeOrFallback(localPatch, impact.up);
        Vec3d preferredConeAxis = BloodOnHitHandler.normalizeOrFallback(impact.forward.add(impact.up.scale(damage.directionalAxisLift * (0.7 + 0.35 * lowHit01))), impact.forward);
        Vec3d dir = preferredConeAxis.add(impact.right.scale(sideScatter)).add(impact.up.scale(upScatter += damage.directionalUpwardBias * (0.8 + 0.25 * lowHit01))).add(localPatchDir.scale(damage.directionalPatchPush * radial01));
        dir = BloodOnHitHandler.keepMinimumDot(dir, preferredConeAxis, damage.directionalConeMinDot);
        double dropletEnergy = BloodOnHitHandler.sampleDropletEnergyMultiplier(rand, radial01);
        double speed = damage.directionalSpeed * (double)sizeProfile.splashSpeedMultiplier * BloodOnHitHandler.randRange(rand, 0.86, 1.1400000000000001) * dropletEnergy;
        double verticalKick = damage.directionalVerticalKick * (double)sizeProfile.splashSpeedMultiplier * BloodOnHitHandler.randRange(rand, 0.85, 1.15) * (0.85 + 0.2 * lowHit01) * dropletEnergy;
        Vec3d velocity = dir.scale(speed).add(impact.up.scale(verticalKick)).add(new Vec3d(motionCarryX, motionCarryY, motionCarryZ));
        return new ParticleSpec(spawnPos.x, spawnPos.y, spawnPos.z, velocity.x, velocity.y, velocity.z);
    }

    private static ParticleSpec createImpactPointParticle(ImpactBasis impact, AxisAlignedBB bb, BloodHitScaling.SizeProfile sizeProfile, DamageResponse damage, ImpactBurstProfile burst, double motionCarryX, double motionCarryY, double motionCarryZ, Random rand, int index, int count, double burstPhase) {
        Vec3d sourceBias;
        Vec3d impactPoint = impact.impactPoint != null ? impact.impactPoint : impact.origin;
        Vec3d impactNormal = BloodOnHitHandler.normalizeOrFallback(impact.impactNormal, new Vec3d(0.0, 1.0, 0.0));
        double width = Math.max(0.15, bb.maxX - bb.minX);
        double height = Math.max(0.15, bb.maxY - bb.minY);
        double depth = Math.max(0.15, bb.maxZ - bb.minZ);
        double horizontalSize = Math.max(width, depth);
        double coneRangeMul = BloodOnHitHandler.getConeRangeMul();
        double yConeRangeMul = BloodOnHitHandler.getYConeRangeMul();
        double forceMul = BloodOnHitHandler.getForceMul();
        double lowCountWide01 = 1.0 - BloodOnHitHandler.smooth01(BloodOnHitHandler.clamp(((double)count - 1.0) / 4.0, 0.0, 1.0));
        double wideArcBoost = 1.0 + 0.55 * lowCountWide01;
        double rotationBoost = BloodOnHitHandler.lerp(1.0, 1.35, lowCountWide01);
        DiscSample sample = BloodOnHitHandler.sampleImpactBurstDisc(index, count, rand, damage.damage01, burstPhase);
        Vec3d tangentRight = BloodOnHitHandler.normalizeOrFallback(BloodOnHitHandler.projectOntoPlane(impact.right, impactNormal), impact.right);
        Vec3d tangentUp = BloodOnHitHandler.normalizeOrFallback(impactNormal.crossProduct(tangentRight), impact.up);
        double roll = BloodOnHitHandler.randRange(rand, -Math.PI, Math.PI);
        Vec3d rolledRight = BloodOnHitHandler.normalizeOrFallback(BloodOnHitHandler.rotateAroundAxis(tangentRight, impactNormal, roll), tangentRight);
        Vec3d rolledUp = BloodOnHitHandler.normalizeOrFallback(BloodOnHitHandler.rotateAroundAxis(tangentUp, impactNormal, roll), tangentUp);
        Vec3d forwardTangentRaw = BloodOnHitHandler.projectOntoPlane(impact.forward, impactNormal);
        double forwardTangentLenSq = forwardTangentRaw.lengthSquared();
        if (forwardTangentLenSq > 1.0E-6) {
            Vec3d towardSourceTangent = forwardTangentRaw.scale(-1.0 / Math.sqrt(forwardTangentLenSq));
            double normalBiasWeight = BloodOnHitHandler.randRange(rand, 0.648, 0.792);
            double tangentBiasWeight = BloodOnHitHandler.randRange(rand, 0.238, 0.476);
            sourceBias = BloodOnHitHandler.normalizeOrFallback(impactNormal.scale(normalBiasWeight).add(towardSourceTangent.scale(tangentBiasWeight)).add(rolledRight.scale(BloodOnHitHandler.randSigned(rand) * 0.1)).add(rolledUp.scale(BloodOnHitHandler.randSigned(rand) * 0.08)), impactNormal);
        } else {
            sourceBias = BloodOnHitHandler.normalizeOrFallback(impactNormal.add(rolledRight.scale(BloodOnHitHandler.randSigned(rand) * 0.18)).add(rolledUp.scale(BloodOnHitHandler.randSigned(rand) * 0.14)), impactNormal);
        }
        Vec3d launchRef = Math.abs(sourceBias.y) > 0.86 ? rolledRight : new Vec3d(0.0, 1.0, 0.0);
        Vec3d launchRight = BloodOnHitHandler.normalizeOrFallback(launchRef.crossProduct(sourceBias), rolledRight);
        Vec3d launchUp = BloodOnHitHandler.normalizeOrFallback(sourceBias.crossProduct(launchRight), rolledUp);
        double radiusCountBoost = 1.0 + 0.1 * lowCountWide01;
        double radiusXZ = Math.max(0.006, horizontalSize * 0.02) * (double)sizeProfile.splashRadiusMultiplier * coneRangeMul * burst.radiusMultiplier * radiusCountBoost;
        double radiusY = Math.max(0.006, height * 0.016) * (double)sizeProfile.splashRadiusMultiplier * yConeRangeMul * burst.radiusMultiplier * radiusCountBoost;
        double positionJitter = 0.004 * burst.radiusMultiplier * (0.75 + 0.5 * lowCountWide01) * (0.6 + 0.4 * sample.radial01);
        Vec3d spawnPos = BloodOnHitHandler.clampInsideBox(impactPoint.add(impactNormal.scale(-0.003)).add(rolledRight.scale(sample.x * radiusXZ + BloodOnHitHandler.randSigned(rand) * positionJitter)).add(rolledUp.scale(sample.y * radiusY + BloodOnHitHandler.randSigned(rand) * positionJitter * 0.8)), bb);
        Vec3d randomLaunchDir = BloodOnHitHandler.sampleTangentDirection(launchRight, launchUp, rand);
        Vec3d discDir = BloodOnHitHandler.normalizeOrFallback(launchRight.scale(sample.x).add(launchUp.scale(sample.y)), randomLaunchDir);
        Vec3d discOrtho = BloodOnHitHandler.normalizeOrFallback(sourceBias.crossProduct(discDir), launchUp);
        double hemisphereMinDot = BloodOnHitHandler.lerp(0.28, 0.04, lowCountWide01);
        double yawLimit = BloodOnHitHandler.lerp(IMPACT_BURST_ROTATION_YAW_MIN, IMPACT_BURST_ROTATION_YAW_MAX, damage.damage01) * burst.arcMultiplier * wideArcBoost * rotationBoost;
        double pitchLimit = BloodOnHitHandler.lerp(IMPACT_BURST_ROTATION_PITCH_MIN, IMPACT_BURST_ROTATION_PITCH_MAX, damage.damage01) * burst.upwardMultiplier * wideArcBoost * rotationBoost;
        double coneSide = BloodOnHitHandler.lerp(0.18, 0.58, damage.damage01) * burst.arcMultiplier * wideArcBoost * (0.35 + 0.95 * sample.radial01) * BloodOnHitHandler.randRange(rand, 0.72, 1.28);
        double orthoJitter = BloodOnHitHandler.lerp(0.05, 0.22, damage.damage01) * burst.arcMultiplier * (0.3 + 0.9 * lowCountWide01) * BloodOnHitHandler.randRange(rand, 0.7, 1.3);
        Vec3d baseLaunch = BloodOnHitHandler.normalizeOrFallback(sourceBias.scale(BloodOnHitHandler.randRange(rand, 0.78, 1.05)).add(discDir.scale(coneSide)).add(discOrtho.scale(BloodOnHitHandler.randSigned(rand) * orthoJitter)).add(new Vec3d(0.0, 0.06 * burst.upwardMultiplier, 0.0)), sourceBias);
        double axisTwist = BloodOnHitHandler.randSigned(rand) * BloodOnHitHandler.lerp(IMPACT_BURST_AXIS_TWIST_MIN, IMPACT_BURST_AXIS_TWIST_MAX, damage.damage01);
        Vec3d launchAxis = BloodOnHitHandler.rotateAroundAxis(baseLaunch, sourceBias, axisTwist);
        double yawJitterMul = BloodOnHitHandler.randRange(rand, 0.78, 1.28);
        double pitchJitterMul = BloodOnHitHandler.randRange(rand, 0.78, 1.28);
        launchAxis = BloodOnHitHandler.rotateAroundAxis(launchAxis, launchUp, BloodOnHitHandler.randSigned(rand) * yawLimit * yawJitterMul);
        launchAxis = BloodOnHitHandler.rotateAroundAxis(launchAxis, launchRight, BloodOnHitHandler.randSigned(rand) * pitchLimit * pitchJitterMul);
        Vec3d hemisphereDir = BloodOnHitHandler.sampleHemisphereDirection(impactNormal, rolledRight, rolledUp, rand, hemisphereMinDot);
        launchAxis = BloodOnHitHandler.normalizeOrFallback(launchAxis.add(hemisphereDir.scale(0.3 * wideArcBoost)).add(sourceBias.scale(0.18)).add(discDir.scale(0.1 + 0.16 * sample.radial01)), sourceBias);
        launchAxis = BloodOnHitHandler.keepMinimumDot(launchAxis, impactNormal, hemisphereMinDot);
        double dropletEnergy = BloodOnHitHandler.sampleDropletEnergyMultiplier(rand, sample.radial01);
        double baseSpeed = BloodOnHitHandler.lerp(0.009600000000000001, 0.05, damage.damage01) * forceMul * BloodOnHitHandler.randRange(rand, 0.88, 1.26) * dropletEnergy;
        double lateralSpeed = BloodOnHitHandler.lerp(0.01, 0.048, damage.damage01) * (double)sizeProfile.splashSpeedMultiplier * burst.arcMultiplier * (0.3 + 0.9 * sample.radial01) * BloodOnHitHandler.randRange(rand, 0.72, 1.38);
        double swirlSpeed = BloodOnHitHandler.lerp(0.006, 0.03, damage.damage01) * (double)sizeProfile.splashSpeedMultiplier * burst.arcMultiplier * (0.25 + 0.85 * lowCountWide01) * BloodOnHitHandler.randRange(rand, 0.7, 1.4);
        double planeScatter = BloodOnHitHandler.lerp(0.012, 0.055, damage.damage01) * (double)sizeProfile.splashSpeedMultiplier * burst.arcMultiplier * wideArcBoost * (0.35 + 0.85 * sample.radial01);
        double upwardScatter = BloodOnHitHandler.lerp(0.018, 0.085, damage.damage01) * (double)sizeProfile.splashSpeedMultiplier * burst.upwardMultiplier * wideArcBoost * (0.3 + 0.9 * sample.radial01);
        Vec3d randomTangentDir = BloodOnHitHandler.sampleTangentDirection(rolledRight, rolledUp, rand);
        Vec3d lateralVelocity = discDir.scale(lateralSpeed).add(discOrtho.scale(BloodOnHitHandler.randSigned(rand) * swirlSpeed));
        Vec3d scatter = randomTangentDir.scale(planeScatter * BloodOnHitHandler.randRange(rand, 0.65, 1.45)).add(rolledRight.scale(BloodOnHitHandler.randSigned(rand) * planeScatter * 0.5)).add(rolledUp.scale(BloodOnHitHandler.randSigned(rand) * upwardScatter * 0.5));
        Vec3d velocity = launchAxis.scale(baseSpeed).add(lateralVelocity).add(scatter).add(new Vec3d(0.0, upwardScatter * BloodOnHitHandler.randRange(rand, 0.55, 1.25), 0.0)).add(new Vec3d(motionCarryX * 0.3, motionCarryY * 0.3, motionCarryZ * 0.3));
        double minY = BloodOnHitHandler.lerp(0.01, 0.055, damage.damage01) * burst.upwardMultiplier * BloodOnHitHandler.lerp(1.0, 1.18, lowCountWide01);
        if (velocity.y < minY) {
            velocity = new Vec3d(velocity.x, BloodOnHitHandler.lerp(velocity.y, minY, 0.78), velocity.z);
        }
        return new ParticleSpec(spawnPos.x, spawnPos.y, spawnPos.z, velocity.x, velocity.y, velocity.z);
    }

    private static ParticleSpec createOmniParticle(ImpactBasis impact, AxisAlignedBB bb, BloodHitScaling.SizeProfile sizeProfile, DamageResponse damage, double motionCarryX, double motionCarryY, double motionCarryZ, Random rand) {
        double width = Math.max(0.15, bb.maxX - bb.minX);
        double height = Math.max(0.15, bb.maxY - bb.minY);
        double depth = Math.max(0.15, bb.maxZ - bb.minZ);
        double horizontalSize = Math.max(width, depth);
        double radiusXZ = Math.min(Math.max(0.018, horizontalSize * 0.095) * (double)sizeProfile.splashRadiusMultiplier * damage.omniRadiusScale, horizontalSize * 0.2);
        double radiusY = Math.min(Math.max(0.018, height * 0.11) * (double)sizeProfile.splashRadiusMultiplier * damage.omniRadiusScale, height * 0.18);
        Vec3d shell = BloodOnHitHandler.sampleUnitSphereDirection(rand);
        double shellRadius = Math.pow(rand.nextDouble(), 1.08);
        Vec3d offset = impact.right.scale(shell.x * radiusXZ * shellRadius).add(impact.up.scale(shell.y * radiusY * shellRadius)).add(impact.forward.scale(shell.z * radiusXZ * shellRadius));
        Vec3d spawnPos = BloodOnHitHandler.clampInsideBox(impact.origin.add(offset), bb);
        Vec3d radial = BloodOnHitHandler.normalizeOrFallback(offset, BloodOnHitHandler.sampleUnitSphereDirection(rand));
        Vec3d randomDir = BloodOnHitHandler.sampleUnitSphereDirection(rand);
        double radialWeight = BloodOnHitHandler.clamp(0.7 + BloodOnHitHandler.randSigned(rand) * 0.07, 0.58, 0.82);
        double randomWeight = 1.0 - radialWeight;
        Vec3d dir = BloodOnHitHandler.normalizeOrFallback(radial.scale(radialWeight).add(randomDir.scale(randomWeight)).add(new Vec3d(0.0, damage.omniUpwardBias, 0.0)), radial);
        double dropletEnergy = BloodOnHitHandler.sampleDropletEnergyMultiplier(rand, shellRadius);
        double speed = damage.omniSpeed * (double)sizeProfile.splashSpeedMultiplier * BloodOnHitHandler.randRange(rand, 0.86, 1.1400000000000001) * dropletEnergy;
        Vec3d velocity = dir.scale(speed).add(radial.scale(damage.omniRadialPush * (double)sizeProfile.splashSpeedMultiplier * dropletEnergy)).add(new Vec3d(motionCarryX, motionCarryY, motionCarryZ));
        return new ParticleSpec(spawnPos.x, spawnPos.y, spawnPos.z, velocity.x, velocity.y, velocity.z);
    }

    private static Vec3d resolveHitPoint(AxisAlignedBB bb, Vec3d rayStart, Vec3d rayDir, @Nullable Vec3d fallbackHit) {
        return BloodOnHitHandler.resolveHitSample((AxisAlignedBB)bb, (Vec3d)rayStart, (Vec3d)rayDir, (Vec3d)fallbackHit).hitPos;
    }

    private static HitSample resolveHitSample(AxisAlignedBB bb, Vec3d rayStart, Vec3d rayDir, @Nullable Vec3d fallbackHit) {
        double centerDistance;
        double rayLength;
        Vec3d center = BloodOnHitHandler.getBoxCenter(bb);
        Vec3d dir = BloodOnHitHandler.normalizeOrFallback(rayDir, center.subtract(rayStart));
        RayTraceResult intercept = bb.calculateIntercept(rayStart, rayStart.add(dir.scale(rayLength = Math.max(8.0, (centerDistance = Math.sqrt(rayStart.squareDistanceTo(center))) + 2.0))));
        if (intercept != null && intercept.hitVec != null) {
            Vec3d hitPos = BloodOnHitHandler.clampToBox(intercept.hitVec, bb);
            Vec3d surfaceNormal = intercept.sideHit != null ? BloodOnHitHandler.facingToVec(intercept.sideHit) : BloodOnHitHandler.approximateSurfaceNormal(bb, hitPos, dir.scale(-1.0));
            return new HitSample(hitPos, surfaceNormal);
        }
        Vec3d hitPos = fallbackHit != null ? BloodOnHitHandler.clampToBox(fallbackHit, bb) : BloodOnHitHandler.clampToBox(rayStart, bb);
        Vec3d surfaceNormal = BloodOnHitHandler.approximateSurfaceNormal(bb, hitPos, dir.scale(-1.0));
        return new HitSample(hitPos, surfaceNormal);
    }

    private static Vec3d computeExitPoint(AxisAlignedBB bb, Vec3d insidePoint, Vec3d dir) {
        double t = Double.POSITIVE_INFINITY;
        if (dir.x > 1.0E-6) {
            t = Math.min(t, (bb.maxX - insidePoint.x) / dir.x);
        } else if (dir.x < -1.0E-6) {
            t = Math.min(t, (bb.minX - insidePoint.x) / dir.x);
        }
        if (dir.y > 1.0E-6) {
            t = Math.min(t, (bb.maxY - insidePoint.y) / dir.y);
        } else if (dir.y < -1.0E-6) {
            t = Math.min(t, (bb.minY - insidePoint.y) / dir.y);
        }
        if (dir.z > 1.0E-6) {
            t = Math.min(t, (bb.maxZ - insidePoint.z) / dir.z);
        } else if (dir.z < -1.0E-6) {
            t = Math.min(t, (bb.minZ - insidePoint.z) / dir.z);
        }
        if (!Double.isFinite(t) || t < 0.0) {
            return BloodOnHitHandler.clampInsideBox(insidePoint, bb);
        }
        return BloodOnHitHandler.clampToBox(insidePoint.add(dir.scale(t)), bb);
    }

    private static long buildImpactBurstSeed(ImpactBasis impact, ImpactBurstProfile burst, DamageResponse damage, Random rand) {
        long seed = rand.nextLong();
        seed ^= Double.doubleToLongBits(burst.countExact * 31.0);
        seed ^= Double.doubleToLongBits(damage.damage01 * 17.0);
        if (impact.impactPoint != null) {
            seed ^= Double.doubleToLongBits(impact.impactPoint.x * 31.0);
            seed ^= Double.doubleToLongBits(impact.impactPoint.y * 17.0);
            seed ^= Double.doubleToLongBits(impact.impactPoint.z * 13.0);
        }
        if (impact.impactNormal != null) {
            seed ^= Double.doubleToLongBits(impact.impactNormal.x * 11.0);
            seed ^= Double.doubleToLongBits(impact.impactNormal.y * 7.0);
            seed ^= Double.doubleToLongBits(impact.impactNormal.z * 5.0);
        }
        return seed;
    }

    private static DiscSample sampleImpactBurstDisc(int index, int count, Random rand, double damage01, double burstPhase) {
        double y;
        double x;
        double lowCountWide01 = 1.0 - BloodOnHitHandler.smooth01(BloodOnHitHandler.clamp(((double)count - 1.0) / 4.0, 0.0, 1.0));
        double randomBlend = BloodOnHitHandler.clamp(0.24 + 0.22 * lowCountWide01 + 0.18 * damage01 + BloodOnHitHandler.randRange(rand, -0.1, 0.14), 0.28, 0.92);
        if (count <= 2) {
            double baseAngle = burstPhase + (count <= 1 ? 0.0 : (double)index * Math.PI);
            double angle = baseAngle + BloodOnHitHandler.randRange(rand, -1.1, 1.1);
            double radius = Math.pow(rand.nextDouble(), 1.15 * BloodOnHitHandler.randRange(rand, 0.92, 1.18));
            radius = BloodOnHitHandler.lerp(radius, 0.55 + 0.35 * rand.nextDouble(), 0.35 * lowCountWide01);
            x = Math.cos(angle) * radius;
            y = Math.sin(angle) * radius;
        } else {
            double t = ((double)index + 0.5) / (double)count;
            double r = Math.sqrt(t);
            double angle = GOLDEN_ANGLE * (double)index + burstPhase + BloodOnHitHandler.randRange(rand, -0.95, 0.95) * (0.55 + 0.45 * damage01);
            double spacedX = Math.cos(angle) * r;
            double spacedY = Math.sin(angle) * r;
            double randomAngle = rand.nextDouble() * (Math.PI * 2);
            double randomRadius = Math.pow(rand.nextDouble(), 1.15 * BloodOnHitHandler.randRange(rand, 0.92, 1.18));
            double randomX = Math.cos(randomAngle) * randomRadius;
            double randomY = Math.sin(randomAngle) * randomRadius;
            x = BloodOnHitHandler.lerp(spacedX, randomX, randomBlend);
            y = BloodOnHitHandler.lerp(spacedY, randomY, randomBlend);
        }
        double jitter = 0.04 + 0.08 * lowCountWide01 + 0.05 * damage01;
        double lenSq = (x += BloodOnHitHandler.randSigned(rand) * jitter) * x + (y += BloodOnHitHandler.randSigned(rand) * jitter * 0.85) * y;
        if (lenSq > 1.0) {
            double invLen = 1.0 / Math.sqrt(lenSq);
            x *= invLen;
            y *= invLen;
        }
        return new DiscSample(x, y);
    }

    private static DiscSample sampleSpacedDisc(int index, int count, Random rand) {
        double lenSq;
        if (count <= 1) {
            return new DiscSample(0.0, 0.0);
        }
        double t = ((double)index + 0.5) / (double)count;
        double r = Math.sqrt(t);
        double angle = GOLDEN_ANGLE * (double)index + BloodOnHitHandler.randRange(rand, -0.25, 0.25);
        double x = Math.cos(angle) * r;
        double y = Math.sin(angle) * r;
        double jitter = 0.08 / Math.sqrt(count);
        if ((lenSq = (x += BloodOnHitHandler.randSigned(rand) * jitter) * x + (y += BloodOnHitHandler.randSigned(rand) * (jitter * 0.85)) * y) > 1.0) {
            double invLen = 1.0 / Math.sqrt(lenSq);
            x *= invLen;
            y *= invLen;
        }
        return new DiscSample(x, y);
    }

    private static DiscSample sampleOrganicDisc(int index, int count, Random rand, double randomBlend) {
        double y;
        DiscSample spaced = BloodOnHitHandler.sampleSpacedDisc(index, count, rand);
        double angle = rand.nextDouble() * (Math.PI * 2);
        double radius = Math.pow(rand.nextDouble(), 1.15);
        double randomX = Math.cos(angle) * radius;
        double randomY = Math.sin(angle) * radius;
        double blend = BloodOnHitHandler.clamp(randomBlend, 0.0, 1.0);
        double x = BloodOnHitHandler.lerp(spaced.x, randomX, blend);
        double lenSq = x * x + (y = BloodOnHitHandler.lerp(spaced.y, randomY, blend)) * y;
        if (lenSq > 1.0) {
            double invLen = 1.0 / Math.sqrt(lenSq);
            x *= invLen;
            y *= invLen;
        }
        return new DiscSample(x, y);
    }

    private static Vec3d sampleTangentDirection(Vec3d tangentRight, Vec3d tangentUp, Random rand) {
        double angle = rand.nextDouble() * (Math.PI * 2);
        return BloodOnHitHandler.normalizeOrFallback(tangentRight.scale(Math.cos(angle)).add(tangentUp.scale(Math.sin(angle))), tangentRight);
    }

    private static Vec3d sampleHemisphereDirection(Vec3d normal, Vec3d tangentRight, Vec3d tangentUp, Random rand, double minDot) {
        double clampedMinDot = BloodOnHitHandler.clamp(minDot, 0.0, 0.999);
        double n = BloodOnHitHandler.randRange(rand, clampedMinDot, 1.0);
        double tangentMag = Math.sqrt(Math.max(0.0, 1.0 - n * n));
        double angle = rand.nextDouble() * (Math.PI * 2);
        return BloodOnHitHandler.normalizeOrFallback(tangentRight.scale(Math.cos(angle) * tangentMag).add(tangentUp.scale(Math.sin(angle) * tangentMag)).add(normal.scale(n)), normal);
    }

    private static Vec3d rotateAroundAxis(Vec3d vec, Vec3d axis, double angle) {
        Vec3d n = BloodOnHitHandler.normalizeOrFallback(axis, new Vec3d(0.0, 1.0, 0.0));
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return vec.scale(cos).add(n.crossProduct(vec).scale(sin)).add(n.scale(n.dotProduct(vec) * (1.0 - cos)));
    }

    private static double sampleDropletEnergyMultiplier(Random rand, double radial01) {
        double mul = BloodOnHitHandler.randRange(rand, 0.98, 1.03);
        mul *= BloodOnHitHandler.lerp(0.99, 1.05, BloodOnHitHandler.clamp(radial01, 0.0, 1.0));
        double roll = rand.nextDouble();
        if (roll < 0.16) {
            mul *= BloodOnHitHandler.randRange(rand, 0.84, 0.92);
        } else if (roll > 0.94) {
            mul *= BloodOnHitHandler.randRange(rand, 1.05, 1.12);
        }
        return mul;
    }

    private static Vec3d projectOntoPlane(Vec3d v, Vec3d normal) {
        return v.subtract(normal.scale(v.dotProduct(normal)));
    }

    private static Vec3d facingToVec(EnumFacing facing) {
        switch (facing) {
            case DOWN: {
                return new Vec3d(0.0, -1.0, 0.0);
            }
            case UP: {
                return new Vec3d(0.0, 1.0, 0.0);
            }
            case NORTH: {
                return new Vec3d(0.0, 0.0, -1.0);
            }
            case SOUTH: {
                return new Vec3d(0.0, 0.0, 1.0);
            }
            case WEST: {
                return new Vec3d(-1.0, 0.0, 0.0);
            }
        }
        return new Vec3d(1.0, 0.0, 0.0);
    }

    private static Vec3d approximateSurfaceNormal(AxisAlignedBB bb, Vec3d hitPos, Vec3d fallback) {
        double dMinX = Math.abs(hitPos.x - bb.minX);
        double dMaxX = Math.abs(hitPos.x - bb.maxX);
        double dMinY = Math.abs(hitPos.y - bb.minY);
        double dMaxY = Math.abs(hitPos.y - bb.maxY);
        double dMinZ = Math.abs(hitPos.z - bb.minZ);
        double dMaxZ = Math.abs(hitPos.z - bb.maxZ);
        double best = dMinX;
        Vec3d normal = new Vec3d(-1.0, 0.0, 0.0);
        if (dMaxX < best) {
            best = dMaxX;
            normal = new Vec3d(1.0, 0.0, 0.0);
        }
        if (dMinY < best) {
            best = dMinY;
            normal = new Vec3d(0.0, -1.0, 0.0);
        }
        if (dMaxY < best) {
            best = dMaxY;
            normal = new Vec3d(0.0, 1.0, 0.0);
        }
        if (dMinZ < best) {
            best = dMinZ;
            normal = new Vec3d(0.0, 0.0, -1.0);
        }
        if (dMaxZ < best) {
            normal = new Vec3d(0.0, 0.0, 1.0);
        }
        return BloodOnHitHandler.normalizeOrFallback(normal, fallback);
    }

    private static Vec3d keepMinimumDot(Vec3d dir, Vec3d preferred, double minDot) {
        Vec3d preferredNorm = BloodOnHitHandler.normalizeOrFallback(preferred, new Vec3d(0.0, 0.0, 1.0));
        Vec3d dirNorm = BloodOnHitHandler.normalizeOrFallback(dir, preferredNorm);
        if (dirNorm.dotProduct(preferredNorm) >= minDot) {
            return dirNorm;
        }
        Vec3d tangent = dirNorm.subtract(preferredNorm.scale(dirNorm.dotProduct(preferredNorm)));
        double tangentLenSq = tangent.lengthSquared();
        if (tangentLenSq < 1.0E-6) {
            return preferredNorm;
        }
        tangent = tangent.scale(1.0 / Math.sqrt(tangentLenSq));
        double tangentMag = Math.sqrt(Math.max(0.0, 1.0 - minDot * minDot));
        return BloodOnHitHandler.normalizeOrFallback(preferredNorm.scale(minDot).add(tangent.scale(tangentMag)), preferredNorm);
    }

    private static double randRange(Random rand, double min, double max) {
        if (max <= min) {
            return min;
        }
        return min + rand.nextDouble() * (max - min);
    }

    private static double randSigned(Random rand) {
        return rand.nextDouble() * 2.0 - 1.0;
    }

    private static Vec3d sampleUnitSphereDirection(Random rand) {
        for (int i = 0; i < 12; ++i) {
            double z;
            double y;
            double x = BloodOnHitHandler.randSigned(rand);
            double lenSq = x * x + (y = BloodOnHitHandler.randSigned(rand)) * y + (z = BloodOnHitHandler.randSigned(rand)) * z;
            if (!(lenSq > 1.0E-6) || !(lenSq <= 1.0)) continue;
            return new Vec3d(x, y, z).scale(1.0 / Math.sqrt(lenSq));
        }
        return new Vec3d(0.0, 1.0, 0.0);
    }

    private static Vec3d clampToBox(Vec3d v, AxisAlignedBB bb) {
        return new Vec3d(BloodOnHitHandler.clamp(v.x, bb.minX, bb.maxX), BloodOnHitHandler.clamp(v.y, bb.minY, bb.maxY), BloodOnHitHandler.clamp(v.z, bb.minZ, bb.maxZ));
    }

    private static Vec3d clampInsideBox(Vec3d v, AxisAlignedBB bb) {
        double insetX = Math.min(0.001, Math.max(0.0, (bb.maxX - bb.minX) * 0.49));
        double insetY = Math.min(0.001, Math.max(0.0, (bb.maxY - bb.minY) * 0.49));
        double insetZ = Math.min(0.001, Math.max(0.0, (bb.maxZ - bb.minZ) * 0.49));
        return new Vec3d(BloodOnHitHandler.clamp(v.x, bb.minX + insetX, bb.maxX - insetX), BloodOnHitHandler.clamp(v.y, bb.minY + insetY, bb.maxY - insetY), BloodOnHitHandler.clamp(v.z, bb.minZ + insetZ, bb.maxZ - insetZ));
    }

    private static Vec3d getBoxCenter(AxisAlignedBB bb) {
        return new Vec3d((bb.minX + bb.maxX) * 0.5, (bb.minY + bb.maxY) * 0.5, (bb.minZ + bb.maxZ) * 0.5);
    }

    private static Vec3d normalizeOrFallback(@Nullable Vec3d vec, Vec3d fallback) {
        double lenSq;
        if (vec != null && (lenSq = vec.lengthSquared()) > 1.0E-6) {
            return vec.scale(1.0 / Math.sqrt(lenSq));
        }
        double fallbackLenSq = fallback.lengthSquared();
        if (fallbackLenSq > 1.0E-6) {
            return fallback.scale(1.0 / Math.sqrt(fallbackLenSq));
        }
        return new Vec3d(0.0, 0.0, 1.0);
    }

    private static double smooth01(double t) {
        t = BloodOnHitHandler.clamp(t, 0.0, 1.0);
        return t * t * (3.0 - 2.0 * t);
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * BloodOnHitHandler.clamp(t, 0.0, 1.0);
    }

    private static float lerpF(float a, float b, double t) {
        return (float)((double)a + (double)(b - a) * BloodOnHitHandler.clamp(t, 0.0, 1.0));
    }

    private static double clamp(double v, double min, double max) {
        if (v < min) {
            return min;
        }
        return Math.min(v, max);
    }

    private static float clampF(float v, float min, float max) {
        if (v < min) {
            return min;
        }
        return Math.min(v, max);
    }

    private static final class ParticleSpec {
        final double x;
        final double y;
        final double z;
        final double vx;
        final double vy;
        final double vz;

        ParticleSpec(double x, double y, double z, double vx, double vy, double vz) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
        }
    }

    private static final class DiscSample {
        final double x;
        final double y;
        final double radial01;

        DiscSample(double x, double y) {
            this.x = x;
            this.y = y;
            this.radial01 = BloodOnHitHandler.clamp(Math.sqrt(x * x + y * y), 0.0, 1.0);
        }
    }

    private static final class ImpactBasis {
        final Vec3d origin;
        final Vec3d forward;
        final Vec3d right;
        final Vec3d up;
        @Nullable
        final Vec3d impactPoint;
        @Nullable
        final Vec3d impactNormal;

        ImpactBasis(Vec3d origin, Vec3d forward, Vec3d right, Vec3d up, @Nullable Vec3d impactPoint, @Nullable Vec3d impactNormal) {
            this.origin = origin;
            this.forward = forward;
            this.right = right;
            this.up = up;
            this.impactPoint = impactPoint;
            this.impactNormal = impactNormal;
        }
    }

    private static final class HitSample {
        final Vec3d hitPos;
        final Vec3d surfaceNormal;

        HitSample(Vec3d hitPos, Vec3d surfaceNormal) {
            this.hitPos = hitPos;
            this.surfaceNormal = surfaceNormal;
        }
    }

    private static final class ScaleBits {
        final int minBits;
        final int maxBits;

        ScaleBits(int minBits, int maxBits) {
            this.minBits = minBits;
            this.maxBits = maxBits;
        }
    }

    private static final class ImpactBurstProfile {
        final double countExact;
        final double radiusMultiplier;
        final double arcMultiplier;
        final double upwardMultiplier;

        ImpactBurstProfile(double countExact, double radiusMultiplier, double arcMultiplier, double upwardMultiplier) {
            this.countExact = countExact;
            this.radiusMultiplier = radiusMultiplier;
            this.arcMultiplier = arcMultiplier;
            this.upwardMultiplier = upwardMultiplier;
        }
    }

    private static final class DamageResponse {
        final double damage01;
        final double directionalCountMultiplier;
        final float directionalScaleMultiplier;
        final double directionalSurfaceInset;
        final double directionalCenterPull;
        final double directionalSpeed;
        final double directionalSideSpread;
        final double directionalUpSpread;
        final double directionalUpwardBias;
        final double directionalAxisLift;
        final double directionalPatchPush;
        final double directionalVerticalKick;
        final double directionalConeMinDot;
        final double omniCountMultiplier;
        final float omniScaleMultiplier;
        final double omniSpeed;
        final double omniRadialPush;
        final double omniUpwardBias;
        final double omniRadiusScale;

        DamageResponse(double damage01, double directionalCountMultiplier, float directionalScaleMultiplier, double directionalSurfaceInset, double directionalCenterPull, double directionalSpeed, double directionalSideSpread, double directionalUpSpread, double directionalUpwardBias, double directionalAxisLift, double directionalPatchPush, double directionalVerticalKick, double directionalConeMinDot, double omniCountMultiplier, float omniScaleMultiplier, double omniSpeed, double omniRadialPush, double omniUpwardBias, double omniRadiusScale) {
            this.damage01 = damage01;
            this.directionalCountMultiplier = directionalCountMultiplier;
            this.directionalScaleMultiplier = directionalScaleMultiplier;
            this.directionalSurfaceInset = directionalSurfaceInset;
            this.directionalCenterPull = directionalCenterPull;
            this.directionalSpeed = directionalSpeed;
            this.directionalSideSpread = directionalSideSpread;
            this.directionalUpSpread = directionalUpSpread;
            this.directionalUpwardBias = directionalUpwardBias;
            this.directionalAxisLift = directionalAxisLift;
            this.directionalPatchPush = directionalPatchPush;
            this.directionalVerticalKick = directionalVerticalKick;
            this.directionalConeMinDot = directionalConeMinDot;
            this.omniCountMultiplier = omniCountMultiplier;
            this.omniScaleMultiplier = omniScaleMultiplier;
            this.omniSpeed = omniSpeed;
            this.omniRadialPush = omniRadialPush;
            this.omniUpwardBias = omniUpwardBias;
            this.omniRadiusScale = omniRadiusScale;
        }
    }

    private static final class HitDamageContext {
        final float postHealth;
        final float maxHealth;
        final float realizedDamageTaken;
        final float incomingDamageTaken;
        final float impactDamageTaken;
        final double impactDamageFraction;

        HitDamageContext(float preHealth, float postHealth, float maxHealth, float incomingDamageTaken) {
            this.maxHealth = Math.max(1.0f, maxHealth);
            float clampedPre = BloodOnHitHandler.clampF(preHealth, 0.0f, this.maxHealth);
            this.postHealth = BloodOnHitHandler.clampF(postHealth, 0.0f, this.maxHealth);
            this.realizedDamageTaken = Math.max(0.0f, clampedPre - this.postHealth);
            this.incomingDamageTaken = Math.max(0.0f, incomingDamageTaken);
            this.impactDamageTaken = Math.max(this.realizedDamageTaken, this.incomingDamageTaken);
            this.impactDamageFraction = BloodOnHitHandler.clamp(this.impactDamageTaken / this.maxHealth, 0.0, 1.0);
        }
    }

    private static final class AttackContext {
        final Vec3d attackerEyePos;
        final Vec3d attackerLookDir;
        final Vec3d attackerMotion;
        @Nullable
        final Vec3d hitVec;
        final boolean authoritativeDirectional;
        int ticksRemaining;

        AttackContext(Vec3d attackerEyePos, Vec3d attackerLookDir, @Nullable Vec3d attackerMotion, @Nullable Vec3d hitVec, int ticksRemaining, boolean authoritativeDirectional) {
            this.attackerEyePos = attackerEyePos;
            this.attackerLookDir = attackerLookDir;
            this.attackerMotion = attackerMotion != null ? attackerMotion : new Vec3d(0.0, 0.0, 0.0);
            this.hitVec = hitVec;
            this.ticksRemaining = ticksRemaining;
            this.authoritativeDirectional = authoritativeDirectional;
        }
    }
}

