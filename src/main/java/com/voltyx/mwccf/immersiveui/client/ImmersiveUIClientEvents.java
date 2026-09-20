package com.voltyx.mwccf.immersiveui.client;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import com.voltyx.mwccf.immersiveui.nea.NEAHelper;
import com.voltyx.mwccf.immersiveui.nea.animations.ItemMoveAnimation;
import com.voltyx.mwccf.immersiveui.nea.animations.ItemPickupThrowAnimation;
import com.voltyx.mwccf.immersiveui.nea.animations.OpeningAnimation;
import com.voltyx.mwccf.immersiveui.nea.api.IAnimatedScreen;
import com.voltyx.mwccf.immersiveui.system.particles.ParticleStorage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ImmersiveUIClientEvents {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ParticleStorage.tickAll();
        } else {
            OpeningAnimation.checkGuiToClose();
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (OpeningAnimation.onGuiOpen(event)) return;
        ItemMoveAnimation.onGuiOpen(event);
        ItemPickupThrowAnimation.onGuiOpen(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onGuiDrawPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        NEAHelper.setMouse(event.getMouseX(), event.getMouseY());
        NEAHelper.setCurrentDrawnScreen(event.getGui());
        NEAHelper.setCurrentOpenAnimationValue(OpeningAnimation.getValue(event.getGui()));
        if (ImmersiveUIConfig.openingAnimationTime > 0 && event.getGui() instanceof IAnimatedScreen) {
            GlStateManager.pushMatrix();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onGuiDrawPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (ImmersiveUIConfig.openingAnimationTime > 0 && event.getGui() instanceof IAnimatedScreen) {
            GlStateManager.popMatrix();
            OpeningAnimation.getScale((IAnimatedScreen) event.getGui());
        }
        NEAHelper.setCurrentDrawnScreen(null);
        NEAHelper.setCurrentOpenAnimationValue(1.0F);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onGuiBackgroundDrawn(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (ImmersiveUIConfig.openingAnimationTime > 0) {
            OpeningAnimation.handleScale(event.getGui(), true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (OpeningAnimation.isAnimatingClose(event.getGui())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKeyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (OpeningAnimation.isAnimatingClose(event.getGui())) {
            event.setCanceled(true);
        }
    }
}

