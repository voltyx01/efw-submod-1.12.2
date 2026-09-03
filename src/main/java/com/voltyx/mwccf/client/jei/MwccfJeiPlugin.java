package com.voltyx.mwccf.client.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class MwccfJeiPlugin implements IModPlugin {

    public static IJeiRuntime jeiRuntime = null;

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
    }

    @Override
    public void register(IModRegistry registry) {
    }

    public static ItemStack getHoveredStack() {
        if (jeiRuntime == null) return ItemStack.EMPTY;
        try {
            if (jeiRuntime.getIngredientListOverlay() != null) {
                Object ing = jeiRuntime.getIngredientListOverlay().getIngredientUnderMouse();
                if (ing instanceof ItemStack) {
                    return (ItemStack) ing;
                }
            }
            if (jeiRuntime.getBookmarkOverlay() != null) {
                Object ing = jeiRuntime.getBookmarkOverlay().getIngredientUnderMouse();
                if (ing instanceof ItemStack) {
                    return (ItemStack) ing;
                }
            }
        } catch (Throwable ignored) {}
        return ItemStack.EMPTY;
    }
}
