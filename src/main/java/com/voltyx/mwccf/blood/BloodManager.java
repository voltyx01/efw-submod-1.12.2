package com.voltyx.mwccf.blood;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks blood contamination level (0.0 to 1.0) for each player.
 * Thread-safe: LivingHurtEvent fires on server thread (SP shared JVM),
 * render reads on client thread.
 */
public class BloodManager {

    // Blood gain amounts
    public static final float MELEE_ATTACKER_GAIN = 0.12f; // player hit someone with melee
    public static final float MELEE_VICTIM_GAIN   = 0.04f; // player received damage
    public static final float BYSTANDER_GAIN      = 0.07f; // nearby splash from melee on someone else

    // Natural decay: full clean in ~2 minutes (20 ticks/s × 120 s)
    private static final float DECAY_PER_TICK  = 1.0f / (120 * 20);
    // Water / rain speeds up washing by 8×
    private static final float WATER_DECAY_MUL = 8.0f;

    private static final ConcurrentHashMap<UUID, Float> bloodLevels = new ConcurrentHashMap<>();

    /** Returns blood level in [0, 1]. */
    public static float getBloodLevel(UUID id) {
        Float f = bloodLevels.get(id);
        return f == null ? 0.0f : f;
    }

    /** Adds blood, clamped to [0, 1]. */
    public static void addBlood(UUID id, float amount) {
        bloodLevels.merge(id, amount, (old, delta) -> Math.min(1.0f, old + delta));
    }

    /**
     * Called each CLIENT tick to decay blood for a single player.
     * Removes entry once fully clean.
     */
    public static void tickDecay(EntityPlayer player) {
        UUID id = player.getUniqueID();
        Float cur = bloodLevels.get(id);
        if (cur == null || cur <= 0.0f) return;

        float decay = DECAY_PER_TICK;

        // In water or standing outside in rain → wash off faster
        if (player.isInWater()) {
            decay *= WATER_DECAY_MUL;
        } else if (player.world.isRaining()) {
            BlockPos pos = player.getPosition();
            if (player.world.canSeeSky(pos)) {
                decay *= WATER_DECAY_MUL;
            }
        }

        float next = cur - decay;
        if (next <= 0.0f) {
            bloodLevels.remove(id);
        } else {
            bloodLevels.put(id, next);
        }
    }

    /** Wipes all data (e.g. on world unload). */
    public static void clear() {
        bloodLevels.clear();
    }
}
