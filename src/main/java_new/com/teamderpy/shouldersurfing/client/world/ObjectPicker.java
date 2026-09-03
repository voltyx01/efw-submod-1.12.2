package com.teamderpy.shouldersurfing.client.world;

import com.teamderpy.shouldersurfing.client.ShoulderHelper;
import com.teamderpy.shouldersurfing.client.ShoulderHelper.ShoulderLook;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class ObjectPicker {
    private static final ObjectPicker INSTANCE = new ObjectPicker();

    public static ObjectPicker getInstance() {
        return INSTANCE;
    }

    public RayTraceResult pick(double interactionRange, float partialTick) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) {
            return null;
        }

        Entity cameraEntity = mc.getRenderViewEntity();
        if (cameraEntity == null) cameraEntity = mc.player;

        Vec3d from;
        Vec3d to;

        if (ShoulderInstance.getInstance().doShoulderSurfing()) {
            ShoulderLook look = ShoulderHelper.shoulderSurfingLook(cameraEntity, partialTick, interactionRange * interactionRange);
            from = look.cameraPos();
            to = look.traceEndPos();
        } else {
            from = cameraEntity.getPositionEyes(partialTick);
            Vec3d look = cameraEntity.getLook(partialTick);
            to = from.add(look.scale(interactionRange));
        }

        RayTraceResult blockHit = mc.world.rayTraceBlocks(from, to, false, false, true);
        double maxDist = interactionRange;
        Vec3d eyePos = cameraEntity.getPositionEyes(partialTick);

        if (blockHit != null && blockHit.typeOfHit != RayTraceResult.Type.MISS) {
            maxDist = eyePos.distanceTo(blockHit.hitVec);
        }

        Vec3d searchDir = to.subtract(from).normalize();
        Vec3d searchEnd = from.add(searchDir.scale(maxDist));

        AxisAlignedBB searchBox = cameraEntity.getEntityBoundingBox()
                .expand(searchDir.x * maxDist, searchDir.y * maxDist, searchDir.z * maxDist)
                .grow(1.0D);

        Predicate<Entity> entitySelector = e -> e != null
                && !(e instanceof net.minecraft.entity.player.EntityPlayer && ((net.minecraft.entity.player.EntityPlayer) e).isSpectator())
                && e.canBeCollidedWith()
                && e != mc.player;

        List<Entity> list = mc.world.getEntitiesInAABBexcluding(cameraEntity, searchBox, entitySelector::test);

        Entity pointedEntity = null;
        Vec3d hitVec = null;
        double closestDist = maxDist;

        for (Entity entity : list) {
            AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
            RayTraceResult intercept = aabb.calculateIntercept(from, searchEnd);

            if (aabb.contains(from)) {
                if (closestDist >= 0.0D) {
                    pointedEntity = entity;
                    hitVec = intercept == null ? from : intercept.hitVec;
                    closestDist = 0.0D;
                }
            } else if (intercept != null) {
                double dist = from.distanceTo(intercept.hitVec);
                if (dist < closestDist || closestDist == 0.0D) {
                    if (entity.getLowestRidingEntity() == cameraEntity.getLowestRidingEntity() && !entity.canRiderInteract()) {
                        if (closestDist == 0.0D) {
                            pointedEntity = entity;
                            hitVec = intercept.hitVec;
                        }
                    } else {
                        pointedEntity = entity;
                        hitVec = intercept.hitVec;
                        closestDist = dist;
                    }
                }
            }
        }

        if (pointedEntity != null && (closestDist < maxDist || blockHit == null || blockHit.typeOfHit == RayTraceResult.Type.MISS)) {
            return new RayTraceResult(pointedEntity, hitVec);
        }

        return blockHit;
    }
}
