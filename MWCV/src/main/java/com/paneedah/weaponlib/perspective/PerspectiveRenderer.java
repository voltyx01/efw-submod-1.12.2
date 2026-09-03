package com.paneedah.weaponlib.perspective;

import com.paneedah.mwc.MWC;
import com.paneedah.weaponlib.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import static com.paneedah.mwc.proxies.ClientProxy.MC;

public class PerspectiveRenderer implements CustomRenderer<RenderableState> {

    private static class StaticTexturePerspective extends Perspective<RenderableState> {

        private Integer textureId;

        @Override
        public void update(TickEvent.RenderTickEvent event) {}

        @Override
        public int getTexture(RenderContext<RenderableState> context) {
            if(textureId == null || textureId <= 0 || !org.lwjgl.opengl.GL11.glIsTexture(textureId)) {
                try {
                    java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                    java.awt.Graphics2D g = img.createGraphics();
                    g.setColor(new java.awt.Color(10, 10, 10, 255));
                    g.fillRect(0, 0, 16, 16);
                    g.dispose();
                    net.minecraft.client.renderer.texture.DynamicTexture dyn = new net.minecraft.client.renderer.texture.DynamicTexture(img);
                    textureId = dyn.getGlTextureId();
                } catch (Throwable t) {
                    textureId = 0;
                }
            }

            return textureId != null ? textureId : 0;
        }

        @Override
        public float getBrightness(RenderContext<RenderableState> context) {
            return 0f;
        }
    }

    protected static Perspective<RenderableState> STATIC_TEXTURE_PERSPECTIVE = new StaticTexturePerspective();

	protected ViewfinderModel model = new ViewfinderModel();
	protected Runnable positioning;


	public PerspectiveRenderer(Runnable positioning) {
		this.positioning = positioning;
	}

	@Override
	public void render(RenderContext<RenderableState> renderContext) {

		if(renderContext.getTransformType() != ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND && renderContext.getTransformType() != ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
			return;
		}

		ClientModContext clientModContext = (ClientModContext) MWC.modContext;

		
        Perspective<RenderableState> perspective = (Perspective<RenderableState>) clientModContext.getViewManager().getPerspective(renderContext.getPlayerItemInstance(), false);
		if(perspective == null) {
		    perspective = STATIC_TEXTURE_PERSPECTIVE;
		}

		float brightness = perspective.getBrightness(renderContext);
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_CURRENT_BIT);

		

		
		positioning.run();
		
		
		
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, perspective.getTexture(renderContext));
		MC.entityRenderer.disableLightmap();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		//GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);

		
		
	
		
		
		GL11.glColor4f(brightness, brightness, brightness, 1f);
		
	
		model.render(renderContext.getPlayer(),
				renderContext.getLimbSwing(),
				renderContext.getFlimbSwingAmount(),
				renderContext.getAgeInTicks(),
				renderContext.getNetHeadYaw(),
				renderContext.getHeadPitch(),
				renderContext.getScale());


        MC.entityRenderer.enableLightmap();
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
}
