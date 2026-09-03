package com.teamderpy.shouldersurfing.lockon;

import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
public class RenderLockOnHandler {
    private final Minecraft mc = Minecraft.getMinecraft();

    // ВАЖНО: Первый параметр должен совпадать с названием папки внутри assets!
    private final ResourceLocation lockTexture = new ResourceLocation("shouldersurfing", "textures/lock.png");

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!LockOnHandler.lockedOn || LockOnHandler.target == null) return;

        EntityLivingBase e = LockOnHandler.target;
        float pt = event.getPartialTicks();

        double x = (e.lastTickPosX + (e.posX - e.lastTickPosX) * pt) - mc.getRenderManager().viewerPosX;
        double y = (e.lastTickPosY + (e.posY - e.lastTickPosY) * pt) - mc.getRenderManager().viewerPosY + (e.height * 0.66);
        double z = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * pt) - mc.getRenderManager().viewerPosZ;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        // БЕРЕМ УГЛЫ НАПРЯМУЮ ИЗ МОДА (Игнорируем ActiveRenderInfo)
        float yaw = mc.getRenderManager().playerViewY;
        float pitch = mc.getRenderManager().playerViewX;

        if (ShoulderInstance.getInstance().doShoulderSurfing()) {
            yaw = ShoulderRenderer.getInstance().cameraYaw - 180.0F;
            pitch = ShoulderRenderer.getInstance().cameraPitch;
        }

        GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-0.02F, -0.02F, 0.02F);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        mc.getTextureManager().bindTexture(lockTexture);
        Gui.drawModalRectWithCustomSizedTexture(-8, -8, 0, 0, 16, 16, 16, 16);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onRenderCrosshair(RenderGameOverlayEvent.Pre event) {
        // Скрываем ванильный прицел во время лока
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && LockOnHandler.lockedOn) {
            event.setCanceled(true);
        }
    }
}