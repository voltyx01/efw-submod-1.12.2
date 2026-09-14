package com.paneedah.weaponlib.perspective;

import com.paneedah.mwc.MWC;
import com.paneedah.weaponlib.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
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
		if (brightness <= 0.0f) {
			brightness = 1.0f;
		}
		int texId = perspective.getTexture(renderContext);

		GlStateManager.pushMatrix();

		positioning.run();

		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		if (texId > 0) {
			GlStateManager.bindTexture(texId);
		} else {
			GlStateManager.bindTexture(0);
		}

		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

		GlStateManager.color(brightness, brightness, brightness, 1.0F);

		model.render(renderContext.getPlayer(),
				renderContext.getLimbSwing(),
				renderContext.getFlimbSwingAmount(),
				renderContext.getAgeInTicks(),
				renderContext.getNetHeadYaw(),
				renderContext.getHeadPitch(),
				renderContext.getScale());

		OpenGlHelper.glUseProgram(0);

		// Clean up GL state using GlStateManager so internal caches remain in sync with hardware
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.bindTexture(0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.enableCull();

		GlStateManager.popMatrix();
	}
}
