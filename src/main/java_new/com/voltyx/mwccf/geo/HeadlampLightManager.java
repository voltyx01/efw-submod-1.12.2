package com.voltyx.mwccf.geo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeadlampLightManager {

    private static final Map<UUID, EntityHeadlampLight> MARKERS = new HashMap<>();
    private static final Map<UUID, Float> INTENSITIES = new HashMap<>();

    // Shifted another 3 blocks forward as requested
    private static final double FORWARD_OFFSET = 3.6;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;
        World world = player.world;
        if (!world.isRemote) return; // ONLY spawn dummy lights on the client!

        UUID id = player.getUniqueID();
        boolean on = HeadlampState.isOn(player);
        EntityHeadlampLight marker = MARKERS.get(id);

        if (on) {
            Vec3d eyePos = player.getPositionEyes(1.0f);
            Vec3d lookVec = player.getLookVec();
            Vec3d targetPos = new Vec3d(eyePos.x + lookVec.x * FORWARD_OFFSET, eyePos.y + lookVec.y * FORWARD_OFFSET, eyePos.z + lookVec.z * FORWARD_OFFSET);

            // Raytrace to prevent light from clipping through walls
            net.minecraft.util.math.RayTraceResult result = world.rayTraceBlocks(eyePos, targetPos, false, true, false);
            Vec3d lightPos = targetPos;
            if (result != null && result.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
                // Limit the distance to the wall, minus a small margin so the entity stays in air
                double distToHit = eyePos.distanceTo(result.hitVec);
                double safeDist = Math.max(0, distToHit - 0.2); // 0.2 blocks away from the surface
                lightPos = new Vec3d(eyePos.x + lookVec.x * safeDist, eyePos.y + lookVec.y * safeDist, eyePos.z + lookVec.z * safeDist);
            }

            if (marker == null || marker.isDead) {
                marker = new EntityHeadlampLight(world);
                marker.setPosition(lightPos.x, lightPos.y, lightPos.z);
                boolean spawned = world.spawnEntity(marker);
                System.out.println("[HeadlampLightManager] Spawning marker on " + (world.isRemote ? "CLIENT" : "SERVER") + " for player " + player.getName() + " with ID " + marker.getEntityId() + ", Spawned: " + spawned);
                MARKERS.put(id, marker);
            }

            if (player.ticksExisted % 40 == 0) {
                System.out.println("[HeadlampLightManager] Updating marker position " + (world.isRemote ? "CLIENT" : "SERVER") + ": " + lightPos.x + ", " + lightPos.y + ", " + lightPos.z + " (marker dead: " + marker.isDead + ", addedToChunk: " + marker.addedToChunk + ")");
            }
        } else {
            if (marker != null && !marker.isDead) {
                // Force a chunk render update (like breaking a block) to clear OptiFine's ghost dynamic light
                int x = net.minecraft.util.math.MathHelper.floor(marker.posX);
                int y = net.minecraft.util.math.MathHelper.floor(marker.posY);
                int z = net.minecraft.util.math.MathHelper.floor(marker.posZ);
                net.minecraft.client.Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(
                        x - 2, y - 2, z - 2,
                        x + 2, y + 2, z + 2
                );

                marker.setDead();
                world.removeEntity(marker);
            }
            MARKERS.remove(id);
        }

        // Failsafe: if player is dead or removed, kill the marker
        if (player.isDead && marker != null) {
            marker.setDead();
            MARKERS.remove(id);
        }

        // Handle smooth intensity for rendering the overlay
        if (world.isRemote) {
            float target = on ? 1.0f : 0f;
            float current = INTENSITIES.getOrDefault(id, 0f);
            current += (target - current) * 0.15f;
            if (Math.abs(target - current) < 0.02f) current = target;
            INTENSITIES.put(id, current);

            if (!on && current < 0.05f) {
                INTENSITIES.remove(id);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        if (mc.world == null || mc.isGamePaused()) return;

        float partialTicks = event.renderTickTime;
        for (EntityPlayer player : mc.world.playerEntities) {
            UUID id = player.getUniqueID();
            EntityHeadlampLight marker = MARKERS.get(id);
            
            if (marker != null && !marker.isDead && HeadlampState.isOn(player)) {
                Vec3d eyePos = player.getPositionEyes(partialTicks);
                Vec3d lookVec = player.getLook(partialTicks);
                Vec3d targetPos = new Vec3d(eyePos.x + lookVec.x * FORWARD_OFFSET, eyePos.y + lookVec.y * FORWARD_OFFSET, eyePos.z + lookVec.z * FORWARD_OFFSET);

                net.minecraft.util.math.RayTraceResult result = mc.world.rayTraceBlocks(eyePos, targetPos, false, true, false);
                Vec3d lightPos = targetPos;
                if (result != null && result.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
                    double distToHit = eyePos.distanceTo(result.hitVec);
                    double safeDist = Math.max(0, distToHit - 0.2);
                    lightPos = new Vec3d(eyePos.x + lookVec.x * safeDist, eyePos.y + lookVec.y * safeDist, eyePos.z + lookVec.z * safeDist);
                }

                // Force all position variables to the current interpolated position
                // so that OptiFine's own interpolation logic doesn't mess it up.
                marker.prevPosX = lightPos.x;
                marker.prevPosY = lightPos.y;
                marker.prevPosZ = lightPos.z;
                marker.lastTickPosX = lightPos.x;
                marker.lastTickPosY = lightPos.y;
                marker.lastTickPosZ = lightPos.z;
                marker.setPosition(lightPos.x, lightPos.y, lightPos.z);
            }
        }
    }

    private Vec3d getHeadLookVec(EntityPlayer player) {
        float yaw = player.rotationYawHead;
        float pitch = player.rotationPitch;
        double x = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double y = -Math.sin(Math.toRadians(pitch));
        double z = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        return new Vec3d(x, y, z);
    }

    public static float getSmoothIntensity(EntityPlayer player) {
        return INTENSITIES.getOrDefault(player.getUniqueID(), 0f);
    }

    @SubscribeEvent
    public void onPlayerLogout(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        cleanupPlayer(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent event) {
        cleanupPlayer(event.player);
    }

    private void cleanupPlayer(EntityPlayer player) {
        UUID id = player.getUniqueID();
        EntityHeadlampLight marker = MARKERS.get(id);
        if (marker != null && !marker.isDead) {
            marker.setDead();
        }
        MARKERS.remove(id);
        INTENSITIES.remove(id);
    }
}
