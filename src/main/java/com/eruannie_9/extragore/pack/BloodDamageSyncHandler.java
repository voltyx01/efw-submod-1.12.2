/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.event.entity.living.LivingDamageEvent
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.eruannie_9.extragore.pack;

import com.eruannie_9.extragore.pack.BloodDamageClassifier;
import com.eruannie_9.extragore.pack.BloodDamageKind;
import com.eruannie_9.extragore.pack.ExtraGoreNetwork;
import com.eruannie_9.extragore.pack.PacketBloodDamage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BloodDamageSyncHandler {
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onLivingDamage(LivingDamageEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living == null || living.world == null || living.world.isRemote) {
            return;
        }
        if (living instanceof EntityPlayer) {
            return;
        }
        float incomingDamage = Math.max(0.0f, event.getAmount());
        if (incomingDamage <= 1.0E-4f) {
            return;
        }
        float maxHealth = Math.max(1.0f, living.getMaxHealth());
        float preHealth = MathHelper.clamp((float)living.getHealth(), (float)0.0f, (float)maxHealth);
        float postHealth = MathHelper.clamp((float)(preHealth - incomingDamage), (float)0.0f, (float)maxHealth);
        if (postHealth + 1.0E-4f >= preHealth) {
            return;
        }
        BloodDamageKind kind = BloodDamageClassifier.classify(event.getSource());
        DirectionalSourceData directional = BloodDamageSyncHandler.resolveDirectionalSource(event.getSource(), living);
        ExtraGoreNetwork.CHANNEL.sendToAllTracking((IMessage)(directional != null ? new PacketBloodDamage(living.getEntityId(), kind, preHealth, postHealth, maxHealth, incomingDamage, directional.sourcePos, directional.sourceLookDir, directional.sourceMotion) : new PacketBloodDamage(living.getEntityId(), kind, preHealth, postHealth, maxHealth, incomingDamage)), (Entity)living);
    }

    @Nullable
    private static DirectionalSourceData resolveDirectionalSource(@Nullable DamageSource source, @Nonnull EntityLivingBase target) {
        if (source == null) {
            return null;
        }
        Entity immediate = source.getImmediateSource();
        Entity trueSource = source.getTrueSource();
        Vec3d sourcePos = null;
        Vec3d sourceLookDir = null;
        Vec3d sourceMotion = new Vec3d(0.0, 0.0, 0.0);
        if (immediate != null) {
            sourcePos = BloodDamageSyncHandler.getImpactReferencePosition(immediate);
            Vec3d immediateMotion = BloodDamageSyncHandler.getEntityMotion(immediate);
            if (!(immediate instanceof EntityLivingBase)) {
                Vec3d motionDir = BloodDamageSyncHandler.normalizeOrNull(immediateMotion);
                if (motionDir != null) {
                    sourceLookDir = motionDir;
                }
            } else {
                Vec3d livingLook = BloodDamageSyncHandler.normalizeOrNull(((EntityLivingBase)immediate).getLookVec());
                if (livingLook != null) {
                    sourceLookDir = livingLook;
                }
                sourceMotion = immediateMotion;
            }
        }
        if (trueSource != null) {
            if (sourcePos == null) {
                sourcePos = BloodDamageSyncHandler.getImpactReferencePosition(trueSource);
            }
            if (sourceLookDir == null) {
                Vec3d trueMotionDir;
                Vec3d livingLook;
                if (trueSource instanceof EntityLivingBase && (livingLook = BloodDamageSyncHandler.normalizeOrNull(((EntityLivingBase)trueSource).getLookVec())) != null) {
                    sourceLookDir = livingLook;
                }
                if (sourceLookDir == null && (trueMotionDir = BloodDamageSyncHandler.normalizeOrNull(BloodDamageSyncHandler.getEntityMotion(trueSource))) != null) {
                    sourceLookDir = trueMotionDir;
                }
            }
            if (sourceMotion.lengthSquared() <= 1.0E-6) {
                sourceMotion = BloodDamageSyncHandler.getEntityMotion(trueSource);
            }
        }
        if (sourceLookDir == null && sourcePos != null) {
            sourceLookDir = BloodDamageSyncHandler.normalizeOrNull(BloodDamageSyncHandler.getTargetCenter(target).subtract(sourcePos));
        }
        if (sourcePos == null || sourceLookDir == null) {
            return null;
        }
        return new DirectionalSourceData(sourcePos, sourceLookDir, sourceMotion);
    }

    @Nonnull
    private static Vec3d getImpactReferencePosition(@Nonnull Entity entity) {
        double y = entity.posY + (entity instanceof EntityLivingBase ? (double)((EntityLivingBase)entity).getEyeHeight() : (double)entity.height * 0.5);
        return new Vec3d(entity.posX, y, entity.posZ);
    }

    @Nonnull
    private static Vec3d getEntityMotion(@Nonnull Entity entity) {
        return new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
    }

    @Nonnull
    private static Vec3d getTargetCenter(@Nonnull EntityLivingBase target) {
        AxisAlignedBB bb = target.getEntityBoundingBox();
        if (bb != null) {
            return new Vec3d((bb.minX + bb.maxX) * 0.5, (bb.minY + bb.maxY) * 0.5, (bb.minZ + bb.maxZ) * 0.5);
        }
        return new Vec3d(target.posX, target.posY + (double)target.height * 0.5, target.posZ);
    }

    @Nullable
    private static Vec3d normalizeOrNull(@Nullable Vec3d vec) {
        if (vec == null) {
            return null;
        }
        double lenSq = vec.lengthSquared();
        if (lenSq <= 1.0E-6) {
            return null;
        }
        return vec.scale(1.0 / Math.sqrt(lenSq));
    }

    private static final class DirectionalSourceData {
        final Vec3d sourcePos;
        final Vec3d sourceLookDir;
        final Vec3d sourceMotion;

        DirectionalSourceData(Vec3d sourcePos, Vec3d sourceLookDir, Vec3d sourceMotion) {
            this.sourcePos = sourcePos;
            this.sourceLookDir = sourceLookDir;
            this.sourceMotion = sourceMotion;
        }
    }
}

