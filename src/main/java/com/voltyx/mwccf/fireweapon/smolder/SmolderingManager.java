package com.voltyx.mwccf.fireweapon.smolder;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.fireweapon.network.PacketSmolderingSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side tracker for entities affected by smoldering burn damage over time.
 * "при попадании по мобу, на нем должны появится эти черные обгорающие пятна которые будут тлеть первые несколько сек,
 * и его будет дамажить в это время. реально поджигать моба не нужно."
 */
public class SmolderingManager {

    public static class SmolderEntry {
        public final int entityId;
        public final int dimensionId;
        public int ticksRemaining;
        public final int totalTicks;
        public int ticksUntilDamage = 20;
        public final float damagePerTick;

        public SmolderEntry(int entityId, int dimensionId, int totalTicks, float damagePerTick) {
            this.entityId = entityId;
            this.dimensionId = dimensionId;
            this.totalTicks = totalTicks;
            this.ticksRemaining = totalTicks;
            this.damagePerTick = damagePerTick;
        }
    }

    private static final Map<Integer, List<SmolderEntry>> ACTIVE_SMOLDERS = new ConcurrentHashMap<>();

    public static void applySmolder(EntityLivingBase target, int durationTicks, float damagePerTick,
                                    float markU, float markV, boolean soaked, int markSeed) {
        if (target == null || target.isDead || target.world.isRemote) return;

        SmolderEntry entry = new SmolderEntry(target.getEntityId(), target.dimension, durationTicks, damagePerTick);
        ACTIVE_SMOLDERS.computeIfAbsent(target.getEntityId(), key -> new ArrayList<>()).add(entry);

        // Notify client trackers
        PacketSmolderingSync pkt = new PacketSmolderingSync(target.getEntityId(), durationTicks,
                markU, markV, soaked, markSeed);
        if (target.world instanceof WorldServer) {
            MwccfMod.PACKET_HANDLER.sendToAllTracking(pkt, target);
            if (target instanceof EntityPlayerMP) {
                MwccfMod.PACKET_HANDLER.sendTo(pkt, (EntityPlayerMP) target);
            }
        }
    }

    public static void updateServer(WorldServer world) {
        if (ACTIVE_SMOLDERS.isEmpty()) return;

        int dimId = world.provider.getDimension();
        Iterator<Map.Entry<Integer, List<SmolderEntry>>> it = ACTIVE_SMOLDERS.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, List<SmolderEntry>> e = it.next();
            List<SmolderEntry> entries = e.getValue();
            net.minecraft.entity.Entity entity = world.getEntityByID(e.getKey());
            if (!(entity instanceof EntityLivingBase) || entity.isDead) {
                entries.removeIf(entry -> entry.dimensionId == dimId);
                if (entries.isEmpty()) it.remove();
                continue;
            }

            EntityLivingBase living = (EntityLivingBase) entity;
            Iterator<SmolderEntry> burnIt = entries.iterator();
            while (burnIt.hasNext()) {
                SmolderEntry entry = burnIt.next();
                if (entry.dimensionId != dimId) continue;
                entry.ticksRemaining--;
                entry.ticksUntilDamage--;
                if (entry.ticksUntilDamage <= 0) {
                    living.setHealth(Math.max(0.0F, living.getHealth() - entry.damagePerTick));
                    entry.ticksUntilDamage = 20;
                }
                if (entry.ticksRemaining <= 0) burnIt.remove();
            }
            if (entries.isEmpty()) it.remove();
        }
    }
}
