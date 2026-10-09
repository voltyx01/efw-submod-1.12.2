package com.voltyx.mwccf.fireweapon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Core helper for wrapping weapons with cloth and igniting them.
 *
 * Two burn modes:
 *   CLOTH  (mode=1) – normal burning, cloth% decreases each tick cycle.
 *                     Rain/water/death extinguishes but remaining cloth% is preserved.
 *                     Ends when pct reaches 0. Duration: ~90 seconds at full 100%.
 *   SOAKED (mode=2) – alcohol-soaked cloth, burns on a fixed 60-second timer.
 *                     Rain/water/death destroys ALL remaining cloth immediately.
 *
 * After either mode ends, a CHAR PHASE begins:
 *   The blackened cloth overlay is visible and fades over 400 ticks (20 s).
 *   When char ticks reach 0 the cloth is fully removed.
 */
public class FireWeaponHelper {

    // ── NBT keys ─────────────────────────────────────────────────────────────────
    public static final String NBT_WRAPPED    = "mwccf_cloth_wrapped";    // boolean – cloth exists
    public static final String NBT_CLOTH_PCT  = "mwccf_cloth_pct";        // int 0-100 – durability
    public static final String NBT_IGNITED    = "mwccf_cloth_ignited";    // boolean – currently burning
    public static final String NBT_BURN_MODE  = "mwccf_burn_mode";        // int: 1=CLOTH 2=SOAKED
    public static final String NBT_BURN_TICKS = "mwccf_burn_ticks";       // int – SOAKED timer OR CLOTH sub-tick counter
    public static final String NBT_MAX_TICKS  = "mwccf_max_ticks";        // int – SOAKED reference max
    public static final String NBT_CHAR_TICKS = "mwccf_char_ticks";       // int – char-fade countdown
    public static final String NBT_SOAKED_READY = "mwccf_soaked_ready";   // boolean – soaked but not yet lit

    // ── Duration constants ────────────────────────────────────────────────────────
    /** CLOTH mode: how many game ticks between each –1% decrement. 18 × 100 = 1800 ticks = 90 s. */
    public static final int CLOTH_TICKS_PER_PCT = 18;
    /** SOAKED mode total duration in ticks (60 s). */
    public static final int SOAKED_BURN_TICKS = 1200;
    /** Char phase fade duration in ticks (20 s). */
    public static final int CHAR_FADE_TICKS = 400;

    // ── Legacy constant kept for any surviving external references ────────────────
    public static final int DEFAULT_BURN_TICKS = 1800;

    // ────────────────────────────────────────────────────────────────────────────
    // Weapon detection
    // ────────────────────────────────────────────────────────────────────────────
    public static boolean isWeapon(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (item == null) return false;

        if (item instanceof ItemSword || item instanceof ItemAxe) return true;
        if (item instanceof com.voltyx.mwccf.si.ItemSIPickaxe
            || item instanceof com.voltyx.mwccf.si.ItemSINailgun) return true;

        try {
            ResourceLocation regName = item.getRegistryName();
            if (regName != null && net.bettercombat.logic.WeaponRegistry.getAttributes(regName) != null)
                return true;
        } catch (Throwable ignored) {}

        return false;
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Alcohol detection
    // ────────────────────────────────────────────────────────────────────────────
    /** Only these fluids may be used to soak weapon cloth. */
    public static boolean isAlcohol(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Item item = stack.getItem();
        return item == com.voltyx.mwccf.si.SIItems.WHISKEY
                || item == com.voltyx.mwccf.si.SIItems.TEQUILA
                || item == com.voltyx.mwccf.si.SIItems.GASOLINE_CAN;
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Cloth state
    // ────────────────────────────────────────────────────────────────────────────
    public static boolean isWrapped(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getBoolean(NBT_WRAPPED);
    }

    /** Cloth durability 0–100. 100 = brand new, 0 = fully consumed. */
    public static int getClothPct(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return 0;
        return tag.hasKey(NBT_CLOTH_PCT) ? tag.getInteger(NBT_CLOTH_PCT) : 100;
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Burn state queries
    // ────────────────────────────────────────────────────────────────────────────
    /** 0 = not burning, 1 = CLOTH mode, 2 = SOAKED mode. */
    public static int getBurnMode(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null ? tag.getInteger(NBT_BURN_MODE) : 0;
    }

    public static boolean isIgnited(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getBoolean(NBT_IGNITED) && getBurnMode(stack) > 0;
    }

    public static boolean isSoaked(ItemStack stack) {
        return getBurnMode(stack) == 2;
    }

    public static boolean isSoakedReady(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getBoolean(NBT_WRAPPED) && tag.getBoolean(NBT_SOAKED_READY);
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Char phase
    // ────────────────────────────────────────────────────────────────────────────
    public static boolean isInCharPhase(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getInteger(NBT_CHAR_TICKS) > 0 && !isIgnited(stack);
    }

    public static int getCharTicks(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null ? tag.getInteger(NBT_CHAR_TICKS) : 0;
    }

    /** 1.0 = just charred (fully visible), 0.0 = faded away. */
    public static float getCharAlpha(ItemStack stack) {
        return Math.max(0f, Math.min(1f, (float) getCharTicks(stack) / (float) CHAR_FADE_TICKS));
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Ignition
    // ────────────────────────────────────────────────────────────────────────────
    /** Ignites in CLOTH mode. Cloth% starts from whatever is currently on the weapon. */
    public static void igniteCloth(ItemStack stack) {
        if (stack == null || stack.isEmpty() || isInCharPhase(stack)) return;
        NBTTagCompound tag = ensureTag(stack);
        tag.setBoolean(NBT_WRAPPED, true);
        tag.setBoolean(NBT_IGNITED, true);
        tag.setInteger(NBT_BURN_MODE, 1);
        tag.setInteger(NBT_BURN_TICKS, 0); // sub-tick counter for cloth mode
        tag.removeTag(NBT_SOAKED_READY);
        if (!tag.hasKey(NBT_CLOTH_PCT)) tag.setInteger(NBT_CLOTH_PCT, 100);
        tag.removeTag(NBT_CHAR_TICKS);
        ensureSparkId(tag, stack);
    }

    /** Soaks with alcohol AND immediately ignites in SOAKED mode. Restores cloth to 100%. */
    public static void soakAndIgnite(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        NBTTagCompound tag = ensureTag(stack);
        tag.setBoolean(NBT_WRAPPED, true);
        tag.setBoolean(NBT_IGNITED, true);
        tag.setInteger(NBT_BURN_MODE, 2);
        tag.setInteger(NBT_CLOTH_PCT, 100);
        tag.setInteger(NBT_BURN_TICKS, SOAKED_BURN_TICKS);
        tag.setInteger(NBT_MAX_TICKS, SOAKED_BURN_TICKS);
        tag.removeTag(NBT_SOAKED_READY);
        tag.removeTag(NBT_CHAR_TICKS);
        ensureSparkId(tag, stack);
    }

    /**
     * Marks cloth as soaked (ready for instant ignite) without actually burning yet.
     * Used when player soaks a wrapped weapon that isn't currently ignited.
     */
    public static void markSoaked(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        NBTTagCompound tag = ensureTag(stack);
        tag.setBoolean(NBT_WRAPPED, true);
        tag.setInteger(NBT_CLOTH_PCT, 100);
        tag.setBoolean(NBT_SOAKED_READY, true);
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Extinguish
    // ────────────────────────────────────────────────────────────────────────────
    /**
     * Extinguishes the fire and begins the char-phase fade.
     * @param consumeCloth true → wipes cloth_pct to 0 (SOAKED mode / death rule).
     *                     false → preserves remaining cloth_pct (CLOTH mode rain rule).
     */
    public static void extinguish(ItemStack stack, boolean consumeCloth) {
        if (stack == null || stack.isEmpty()) return;
        NBTTagCompound tag = ensureTag(stack);
        tag.setBoolean(NBT_IGNITED, false);
        tag.setInteger(NBT_BURN_MODE, 0);
        tag.removeTag(NBT_BURN_TICKS);
        tag.removeTag(NBT_MAX_TICKS);
        tag.removeTag("mwccf_spark_id");
        if (consumeCloth) tag.setInteger(NBT_CLOTH_PCT, 0);
        // Start char-fade
        tag.setInteger(NBT_CHAR_TICKS, CHAR_FADE_TICKS);
    }

    /** Fully removes cloth with no char phase. Called when char ticks reach 0. */
    public static void removeCloth(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return;
        tag.setBoolean(NBT_WRAPPED, false);
        tag.setBoolean(NBT_IGNITED, false);
        tag.setInteger(NBT_BURN_MODE, 0);
        tag.removeTag(NBT_BURN_TICKS);
        tag.removeTag(NBT_MAX_TICKS);
        tag.removeTag(NBT_CLOTH_PCT);
        tag.removeTag(NBT_CHAR_TICKS);
        tag.removeTag(NBT_SOAKED_READY);
        tag.removeTag("mwccf_spark_id");
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Per-tick update
    // ────────────────────────────────────────────────────────────────────────────
    /**
     * Tick burning / char logic for one game tick.
     * @return true if the weapon just transitioned into the char phase this tick.
     */
    public static boolean tickBurn(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return false;

        // Char phase
        if (isInCharPhase(stack)) {
            int ct = tag.getInteger(NBT_CHAR_TICKS) - 1;
            if (ct <= 0) removeCloth(stack);
            else tag.setInteger(NBT_CHAR_TICKS, ct);
            return false;
        }

        if (!isIgnited(stack)) return false;
        int mode = getBurnMode(stack);

        if (mode == 1) {
            // CLOTH: decrement pct every CLOTH_TICKS_PER_PCT ticks
            int sub = tag.getInteger(NBT_BURN_TICKS) + 1;
            if (sub >= CLOTH_TICKS_PER_PCT) {
                sub = 0;
                int pct = getClothPct(stack) - 1;
                if (pct <= 0) {
                    extinguish(stack, false);
                    return true;
                }
                tag.setInteger(NBT_CLOTH_PCT, pct);
            }
            tag.setInteger(NBT_BURN_TICKS, sub);

        } else if (mode == 2) {
            // SOAKED: decrement timer
            int ticks = tag.getInteger(NBT_BURN_TICKS) - 1;
            if (ticks <= 0) {
                extinguish(stack, true);
                return true;
            }
            tag.setInteger(NBT_BURN_TICKS, ticks);
        }

        return false;
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Progress & display helpers
    // ────────────────────────────────────────────────────────────────────────────
    /** Burn progress 0.0 (just lit) → 1.0 (nearly out), used by texture compositor. */
    public static float getBurnProgress(ItemStack stack) {
        if (!isIgnited(stack)) return 0.0F;
        int mode = getBurnMode(stack);
        if (mode == 1) {
            return Math.max(0f, Math.min(1f, 1.0F - getClothPct(stack) / 100.0F));
        } else if (mode == 2) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) return 0.0F;
            int remaining = tag.getInteger(NBT_BURN_TICKS);
            int max = tag.hasKey(NBT_MAX_TICKS) ? tag.getInteger(NBT_MAX_TICKS) : SOAKED_BURN_TICKS;
            return max > 0 ? Math.max(0f, Math.min(1f, 1.0F - (float) remaining / max)) : 0.0F;
        }
        return 0.0F;
    }

    /** Remaining burn seconds for tooltip display. */
    public static int getRemainingSeconds(ItemStack stack) {
        if (!isIgnited(stack)) return 0;
        int mode = getBurnMode(stack);
        if (mode == 1) return (getClothPct(stack) * CLOTH_TICKS_PER_PCT) / 20;
        if (mode == 2) {
            NBTTagCompound tag = stack.getTagCompound();
            return tag != null ? tag.getInteger(NBT_BURN_TICKS) / 20 : 0;
        }
        return 0;
    }

    /** Extra fire damage for this mode: +3 (CLOTH) or +5 (SOAKED). */
    public static float getFireDamageBonus(ItemStack stack) {
        int mode = getBurnMode(stack);
        if (mode == 1) return 3.0F;
        if (mode == 2) return 5.0F;
        return 0.0F;
    }

    /** Smoldering duration in ticks: five seconds for either burn mode. */
    public static int getSmolderTicks(ItemStack stack) {
        int mode = getBurnMode(stack);
        if (mode == 1 || mode == 2) return 100;
        return 0;
    }

    /** Health removed once per smoldering second. */
    public static float getSmolderDamage(ItemStack stack) {
        int mode = getBurnMode(stack);
        if (mode == 1) return 1.0F;
        if (mode == 2) return 2.0F;
        return 0;
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Legacy compat shim — kept so callers that haven't been updated yet still compile
    // ────────────────────────────────────────────────────────────────────────────
    public static void setWrapped(ItemStack stack, boolean wrapped) {
        if (stack == null || stack.isEmpty()) return;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null && !wrapped) return;
        if (tag == null) { tag = new NBTTagCompound(); stack.setTagCompound(tag); }
        boolean replacingCharredCloth = wrapped && isInCharPhase(stack);
        if (replacingCharredCloth) {
            tag.setBoolean(NBT_IGNITED, false);
            tag.setInteger(NBT_BURN_MODE, 0);
            tag.removeTag(NBT_BURN_TICKS);
            tag.removeTag(NBT_MAX_TICKS);
            tag.removeTag(NBT_CHAR_TICKS);
            tag.removeTag(NBT_SOAKED_READY);
            tag.removeTag("mwccf_spark_id");
            tag.setInteger(NBT_CLOTH_PCT, 100);
        }
        tag.setBoolean(NBT_WRAPPED, wrapped);
        if (!wrapped) {
            tag.removeTag(NBT_IGNITED);
            tag.removeTag(NBT_BURN_MODE);
            tag.removeTag(NBT_BURN_TICKS);
            tag.removeTag(NBT_MAX_TICKS);
            tag.removeTag(NBT_CLOTH_PCT);
            tag.removeTag(NBT_CHAR_TICKS);
            tag.removeTag(NBT_SOAKED_READY);
            tag.removeTag("mwccf_spark_id");
        } else {
            if (replacingCharredCloth || !tag.hasKey(NBT_CLOTH_PCT)) tag.setInteger(NBT_CLOTH_PCT, 100);
        }
    }

    /** @deprecated Use {@link #igniteCloth} or {@link #soakAndIgnite}. */
    @Deprecated
    public static void setIgnited(ItemStack stack, boolean ignited, int durationTicks) {
        if (ignited) igniteCloth(stack);
        else extinguish(stack, false);
    }

    /** @deprecated Use {@link #tickBurn}. */
    @Deprecated
    public static int getBurnTicks(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null ? tag.getInteger(NBT_BURN_TICKS) : 0;
    }

    /** @deprecated Use {@link #getBurnProgress}. */
    @Deprecated
    public static int getMaxBurnTicks(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return DEFAULT_BURN_TICKS;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(NBT_MAX_TICKS)) {
            int max = tag.getInteger(NBT_MAX_TICKS);
            if (max > 0) return max;
        }
        return DEFAULT_BURN_TICKS;
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ────────────────────────────────────────────────────────────────────────────
    private static NBTTagCompound ensureTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) { tag = new NBTTagCompound(); stack.setTagCompound(tag); }
        return tag;
    }

    private static void ensureSparkId(NBTTagCompound tag, ItemStack stack) {
        if (!tag.hasKey("mwccf_spark_id"))
            tag.setLong("mwccf_spark_id", System.nanoTime() ^ ((long) stack.hashCode() << 32));
    }
}
