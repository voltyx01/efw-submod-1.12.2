package com.voltyx.mwccf.immersiveui.util;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.Map;
import java.util.Objects;

public class CommonCode {

    public static void floatingRenderSize(Slot slot, boolean isHovered, Map<Slot, Float> expandingProgress, float deltaTime) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) return;

        ItemStack carried = player.inventory.getItemStack();
        boolean hovering = isHovered && (carried.isEmpty() || (ItemStack.areItemsEqual(slot.getStack(), carried) && ItemStack.areItemStackTagsEqual(slot.getStack(), carried)));

        float currentProg = expandingProgress.containsKey(slot) ? expandingProgress.get(slot) : 0.0F;
        currentProg = MathHelper.clamp(currentProg + deltaTime * 12.0F * (hovering ? 1.0F : -1.0F), 0.0F, 1.0F);
        expandingProgress.put(slot, currentProg);

        float scale = Easing.lerp(1.0F, ImmersiveUIConfig.hoveredItemScale,
                Easing.animate(hovering ? Easing.Type.EASE_OUT : Easing.Type.EASE_IN, currentProg));


        if (!carried.isEmpty() && ItemStack.areItemsEqual(slot.getStack(), carried) && ItemStack.areItemStackTagsEqual(slot.getStack(), carried) && ImmersiveUIConfig.enableMatchingItemHovering) {
            float hash = (float) Objects.hash(slot.xPos, slot.yPos);
            float offsetX = MathHelper.sin(player.ticksExisted * 0.215F + hash) * ImmersiveUIConfig.matchingItemHoverAmplitude;
            float offsetY = MathHelper.cos(player.ticksExisted * 0.13F + hash) * ImmersiveUIConfig.matchingItemHoverAmplitude;
            GlStateManager.translate(offsetX, offsetY, 0.0F);
        }

        GlStateManager.translate(slot.xPos + 8.0F, slot.yPos + 8.0F, 0.0F);
        GlStateManager.scale(scale, scale, 1.0F);
        GlStateManager.translate(-slot.xPos - 8.0F, -slot.yPos - 8.0F, 0.0F);
    }
}
