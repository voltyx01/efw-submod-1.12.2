package com.paneedah.weaponlib.perspective;

import com.paneedah.mwc.proxies.ClientProxy;
import com.paneedah.weaponlib.RenderingPhase;
import com.paneedah.weaponlib.render.bgl.PostProcessPipeline;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.paneedah.mwc.proxies.ClientProxy.MC;

public class FirstPersonPerspective<S> extends Perspective<S> {

    private long renderEndNanoTime;

    public FirstPersonPerspective() {
        this.renderEndNanoTime = System.nanoTime();
        this.width = MC.displayWidth;
        this.height = MC.displayHeight;
    }
    
    protected void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(TickEvent.RenderTickEvent event) {
        ClientProxy.renderingPhase = RenderingPhase.RENDER_PERSPECTIVE;
        long p_78471_2_ = this.renderEndNanoTime + (long)(1000000000 / 60);
        int origDisplayWidth = MC.displayWidth;
        int origDisplayHeight = MC.displayHeight;

        //RenderGlobal origRenderGlobal = MC.renderGlobal;
        EntityRenderer origEntityRenderer = MC.entityRenderer;
        net.minecraft.client.particle.ParticleManager origEffectRenderer = MC.effectRenderer;

        framebuffer.bindFramebuffer(true);

        MC.displayWidth = width;
        MC.displayHeight = height;

        MC.entityRenderer = this.entityRenderer;
        MC.effectRenderer = this.effectRenderer;

        this.entityRenderer.setPrepareTerrain(true);
        this.entityRenderer.updateRenderer();

        prepareRenderWorld(event);

        this.entityRenderer.renderWorld(event.renderTickTime, p_78471_2_);
     
        if(PostProcessPipeline.shouldDoFog()) {
        	// Blits onto custom scope depth texture
        	// TO-DO: Just use a depth-texture compatible framebuffer w/ the scope. more efficient.
        	PostProcessPipeline.blitScopeDepthTexture(framebuffer);
        }
        
        postRenderWorld(event);
        
        GlStateManager.disableFog();
        MC.entityRenderer = origEntityRenderer;
        MC.effectRenderer = origEffectRenderer;

        MC.getFramebuffer().bindFramebuffer(true);
        MC.displayWidth = origDisplayWidth;
        MC.displayHeight = origDisplayHeight;
        GlStateManager.viewport(0, 0, origDisplayWidth, origDisplayHeight);

        if (MC.player != null) {
            ActiveRenderInfo.updateRenderInfo(MC.player, MC.gameSettings.thirdPersonView == 2);

            double d0 = MC.player.lastTickPosX + (MC.player.posX - MC.player.lastTickPosX) * (double)event.renderTickTime;
            double d1 = MC.player.lastTickPosY + (MC.player.posY - MC.player.lastTickPosY) * (double)event.renderTickTime;
            double d2 = MC.player.lastTickPosZ + (MC.player.posZ - MC.player.lastTickPosZ) * (double)event.renderTickTime;

            net.minecraft.client.renderer.culling.ICamera mainCamera = new net.minecraft.client.renderer.culling.Frustum();
            mainCamera.setPosition(d0, d1, d2);

            if (MC.renderGlobal != null) {
                MC.renderGlobal.setupTerrain(MC.player, (double)event.renderTickTime, mainCamera, 0, MC.player.isSpectator());
                MC.renderGlobal.setDisplayListEntitiesDirty();
            }
        }

        try {
            // Restore normal player camera in RenderLib so main world rendering does not use scope's narrow frustum
            Class<?> renderUtilClass = Class.forName("meldexun.renderlib.util.RenderUtil");
            renderUtilClass.getMethod("update", double.class).invoke(null, (double)event.renderTickTime);
            renderUtilClass.getMethod("updateCamera").invoke(null);
        } catch (Throwable ignored) {}

        try {
            // Restore Nothirium ChunkRenderManager with full player view frustum
            Class<?> chunkRenderManagerClass = Class.forName("meldexun.nothirium.mc.renderer.ChunkRenderManager");
            chunkRenderManagerClass.getMethod("setup").invoke(null);
        } catch (Throwable ignored) {}

        this.renderEndNanoTime = System.nanoTime();

        ClientProxy.renderingPhase = RenderingPhase.NORMAL;
    }

    protected void prepareRenderWorld(TickEvent.RenderTickEvent event) {
    }

    protected void postRenderWorld(TickEvent.RenderTickEvent event) {
    }

}
