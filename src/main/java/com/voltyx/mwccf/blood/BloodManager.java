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

    // Blood gain amounts (gradual progression)
    public static final float MELEE_ATTACKER_GAIN = 0.025f; // player hit someone with melee (~40 hits to max)
    public static final float MELEE_VICTIM_GAIN   = 0.02f;  // player received damage
    public static final float BYSTANDER_GAIN      = 0.01f;  // nearby splash from melee on someone else

    // Natural decay: full clean in ~6 minutes (20 ticks/s × 360 s)
    private static final float DECAY_PER_TICK  = 1.0f / (360 * 20);
    // Water / rain speeds up washing significantly (~18x faster, clean in ~20 sec)
    private static final float WATER_DECAY_MUL = 18.0f;

    // Grace period: blood doesn't start decaying until 40 seconds after taking or dealing damage
    private static final long DECAY_DELAY_MS = 40_000L;

    private static final ConcurrentHashMap<UUID, Float> bloodLevels = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, Long> lastGainTimes = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, Long> lastMeleeAttackTimes = new ConcurrentHashMap<>();

    /** Returns blood level in [0, 1]. */
    public static float getBloodLevel(UUID id) {
        Float f = bloodLevels.get(id);
        return f == null ? 0.0f : f;
    }

    /** Adds blood, clamped to [0, 1], and resets the decay delay timer. */
    public static void addBlood(UUID id, float amount) {
        lastGainTimes.put(id, System.currentTimeMillis());
        bloodLevels.merge(id, amount, (old, delta) -> Math.min(1.0f, old + delta));
    }

    /** Sets blood to a specific level in [0, 1]. */
    public static void setBloodLevel(UUID id, float level) {
        lastGainTimes.put(id, System.currentTimeMillis());
        bloodLevels.put(id, Math.max(0.0f, Math.min(1.0f, level)));
    }

    /**
     * Safely applies melee attack blood gain with a 150ms debounce window
     * to prevent double counting if both AttackEntityEvent and LivingHurtEvent fire.
     */
    public static void onMeleeAttack(UUID id, float amount) {
        long now = System.currentTimeMillis();
        Long last = lastMeleeAttackTimes.get(id);
        if (last != null && (now - last < 150)) {
            return;
        }
        lastMeleeAttackTimes.put(id, now);
        addBlood(id, amount);
    }

    /**
     * Called each CLIENT tick to decay blood for a single player.
     * Removes entry once fully clean.
     */
    public static void tickDecay(EntityPlayer player) {
        UUID id = player.getUniqueID();
        Float cur = bloodLevels.get(id);
        if (cur == null || cur <= 0.0f) return;

        boolean inWaterOrRain = false;
        if (player.isInWater()) {
            inWaterOrRain = true;
        } else if (player.world.isRaining()) {
            BlockPos pos = player.getPosition();
            if (player.world.canSeeSky(pos)) {
                inWaterOrRain = true;
            }
        }

        // In dry conditions, do not decay if recently contaminated
        if (!inWaterOrRain) {
            Long lastGain = lastGainTimes.get(id);
            if (lastGain != null && (System.currentTimeMillis() - lastGain < DECAY_DELAY_MS)) {
                return;
            }
        }

        float decay = DECAY_PER_TICK;
        if (inWaterOrRain) {
            decay *= WATER_DECAY_MUL;
        }

        float next = cur - decay;
        if (next <= 0.0f) {
            bloodLevels.remove(id);
            lastGainTimes.remove(id);
        } else {
            bloodLevels.put(id, next);
        }
    }

    /** Clears blood for a specific player (e.g. on death/respawn). */
    public static void clear(UUID id) {
        if (id == null) return;
        bloodLevels.remove(id);
        lastGainTimes.remove(id);
        lastMeleeAttackTimes.remove(id);
    }

    /** Wipes all data (e.g. on world unload). */
    public static void clear() {
        bloodLevels.clear();
        lastGainTimes.clear();
        lastMeleeAttackTimes.clear();
    }
}
