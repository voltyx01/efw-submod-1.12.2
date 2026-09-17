package com.voltyx.mwccf.walkietalkie;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;

/**
 * Utility methods for searching a player's inventory + baubles for a walkie-talkie.
 */
public final class WalkieTalkieUtil {

    private WalkieTalkieUtil() {}

    /**
     * Find an ACTIVE (powered on, has battery) walkie-talkie
     * anywhere in the player's hands or baubles.
     */
    @Nullable
    public static ItemStack getActiveWalkieTalkie(EntityPlayer player) {
        ItemStack stack = findWalkieTalkie(player);
        if (stack != null && ItemWalkieTalkie.isActive(stack)) {
            return stack;
        }
        return null;
    }

    /**
     * Find a walkie-talkie (active or not) anywhere on the player.
     */
    @Nullable
    public static ItemStack findWalkieTalkie(EntityPlayer player) {
        // Check both hands first
        for (net.minecraft.util.EnumHand hand : net.minecraft.util.EnumHand.values()) {
            ItemStack held = player.getHeldItem(hand);
            if (held.getItem() instanceof ItemWalkieTalkie) return held;
        }

        // Check baubles slots
        if (Loader.isModLoaded("baubles")) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
            if (baubles != null) {
                for (int i = 0; i < baubles.getSlots(); i++) {
                    ItemStack s = baubles.getStackInSlot(i);
                    if (!s.isEmpty() && s.getItem() instanceof ItemWalkieTalkie) {
                        return s;
                    }
                }
            }
        }
        return null;
    }
}
