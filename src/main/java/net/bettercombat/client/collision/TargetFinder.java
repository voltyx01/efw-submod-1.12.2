package net.bettercombat.client.collision;

import efw.biomeinfo.MwccfConfig;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.logic.TargetHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TargetFinder {

    public static List<Entity> findAttackTargets(EntityPlayer player, Entity cursorTarget, WeaponAttributes.Attack attack, double attackRange) {
        if (player == null || player.world == null || attack == null) {
            return Collections.emptyList();
        }

        Vec3d origin = getInitialTracingPoint(player);
        List<Entity> initialTargets = getInitialTargets(player, cursorTarget, attackRange);

        boolean isSpinAttack = attack.angle() > 180.0;
        Vec3d size = WeaponHitBoxes.createHitbox(attack.hitbox(), attackRange, isSpinAttack);
        OrientedBoundingBox obb = new OrientedBoundingBox(origin, size, player.rotationPitch, player.rotationYaw);
        if (!isSpinAttack) {
            obb = obb.offsetAlongAxisZ(size.z / 2.0);
        }
        obb.updateVertex();

        List<Entity> collided = new ArrayList<>();
        for (Entity entity : initialTargets) {
            AxisAlignedBB bb = entity.getEntityBoundingBox();
            if (bb != null && obb.intersects(bb)) {
                collided.add(entity);
            }
        }

        // Angle filter
        double maxHalfAngle = attack.angle() / 2.0;
        List<Entity> radialFiltered = new ArrayList<>();
        for (Entity entity : collided) {
            if (attack.angle() <= 0.0 || attack.angle() >= 360.0) {
                radialFiltered.add(entity);
            } else {
                Vec3d targetCenter = new Vec3d(
                        entity.posX,
                        entity.posY + entity.height / 2.0,
                        entity.posZ);
                Vec3d toTarget = targetCenter.subtract(origin);
                double dist = toTarget.length();
                if (dist > 0.001) {
                    Vec3d dir = new Vec3d(toTarget.x / dist, toTarget.y / dist, toTarget.z / dist);
                    double dot = obb.axisZ.dotProduct(dir);
                    dot = Math.max(-1.0, Math.min(1.0, dot));
                    double angleDeg = Math.toDegrees(Math.acos(dot));
                    if (angleDeg <= maxHalfAngle) {
                        radialFiltered.add(entity);
                    }
                } else {
                    radialFiltered.add(entity);
                }
            }
        }

        // Raycast visibility check (blocks wall hacking)
        List<Entity> visible = new ArrayList<>();
        for (Entity entity : radialFiltered) {
            Vec3d targetCenter = new Vec3d(
                    entity.posX,
                    entity.posY + entity.height / 2.0,
                    entity.posZ);
            RayTraceResult hit = player.world.rayTraceBlocks(origin, targetCenter, false, true, false);
            if (hit == null || hit.typeOfHit == RayTraceResult.Type.MISS) {
                visible.add(entity);
            }
        }

        // Sort by distance to player
        visible.sort(Comparator.comparingDouble(e -> e.getDistanceSq(player)));

        // If cursorTarget was explicitly hovered and is in range, prioritize it at index 0
        if (cursorTarget != null && !visible.contains(cursorTarget)) {
            double distSq = cursorTarget.getDistanceSq(player);
            if (distSq <= attackRange * attackRange) {
                visible.add(0, cursorTarget);
            }
        }

        return visible;
    }

    public static Vec3d getInitialTracingPoint(EntityPlayer player) {
        double shoulderHeight = player.height * 0.15;
        return player.getPositionEyes(1.0F).subtract(0, shoulderHeight, 0);
    }

    public static List<Entity> getInitialTargets(EntityPlayer player, Entity cursorTarget, double attackRange) {
        double searchMult = MwccfConfig.betterCombat.targetSearchRangeMultiplier;
        AxisAlignedBB box = player.getEntityBoundingBox().grow(attackRange * searchMult + 1.0);
        List<Entity> list = player.world.getEntitiesWithinAABB(Entity.class, box);

        List<Entity> result = new ArrayList<>();
        for (Entity entity : list) {
            if (entity == player || entity.isDead) continue;
            if (entity.isRidingSameEntity(player)) continue;
            if (entity.equals(player.getRidingEntity()) && !TargetHelper.isAttackableMount(entity)) continue;

            if (entity.canBeCollidedWith() || entity.canBePushed()) {
                TargetHelper.Relation relation = TargetHelper.getRelation(player, entity);
                if (relation == TargetHelper.Relation.HOSTILE || entity == cursorTarget) {
                    result.add(entity);
                }
            }
        }
        return result;
    }
}
