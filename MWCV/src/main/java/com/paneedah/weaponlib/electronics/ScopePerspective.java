package com.paneedah.weaponlib.electronics;

import com.paneedah.mwc.MWC;
import com.paneedah.weaponlib.ClientModContext;
import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.RenderContext;
import com.paneedah.weaponlib.RenderableState;
import com.paneedah.weaponlib.WeaponState;
import com.paneedah.weaponlib.perspective.Perspective;
import com.paneedah.weaponlib.perspective.PerspectiveRenderer;
import com.paneedah.weaponlib.render.scopes.Reticle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static com.paneedah.mwc.proxies.ClientProxy.MC;
import static com.paneedah.mwc.utils.ModReference.ID;

public class ScopePerspective extends PerspectiveRenderer {

	private static final ResourceLocation DARK_SCREEN = new ResourceLocation(ID + ":textures/hud/dark-screen.png");

	private Reticle reticle;
	public static float darkAlpha = 1.0f;
	private static long lastUpdateTime = System.currentTimeMillis();
	private static boolean lastLoggedAiming = false;
	private static int lastLoggedTexId = -1;

	public ScopePerspective(Runnable positioning, Reticle reticle) {
		super(positioning);
		this.reticle = reticle;
	}

	public static boolean isReloadingOrBusy(WeaponState state) {
		if (state == null) return false;
		switch (state) {
			case DRAWING:
			case TACTICAL_RELOAD:
			case COMPOUND_RELOAD:
			case COMPOUND_RELOAD_EMPTY:
			case COMPOUND_RELOAD_UNLOAD:
			case COMPOUND_RELOAD_FINISH:
			case COMPOUND_RELOAD_FINISHED:
			case COMPOUND_REQUESTED:
			case COMPOUND_EMTPY_REQUESTED:
			case LOAD_REQUESTED:
			case LOAD:
			case LOAD_ITERATION:
			case LOAD_ITERATION_COMPLETED:
			case ALL_LOAD_ITERATIONS_COMPLETED:
			case AWAIT_FURTHER_LOAD_INSTRUCTIONS:
			case UNLOAD_PREPARING:
			case UNLOAD_REQUESTED:
			case UNLOAD:
			case INSPECTING:
			case MODIFYING:
			case MODIFYING_REQUESTED:
				return true;
			default:
				return false;
		}
	}

	@Override
	public void render(RenderContext<RenderableState> renderContext) {

		if (renderContext.getTransformType() != ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
				&& renderContext.getTransformType() != ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
			return;
		}

		ClientModContext clientModContext = (ClientModContext) MWC.modContext;

		Perspective<RenderableState> perspective = null;
		try {
			if (clientModContext != null && clientModContext.getViewManager() != null
					&& renderContext.getPlayerItemInstance() != null) {
				perspective = (Perspective<RenderableState>) clientModContext.getViewManager()
						.getPerspective(renderContext.getPlayerItemInstance(), false);
			}
		} catch (Throwable ignored) {}

		PlayerWeaponInstance weaponInstance = renderContext.getWeaponInstance();
		boolean isAiming = weaponInstance != null 
				&& weaponInstance.isAimed() 
				&& !isReloadingOrBusy(weaponInstance.getState());

		// Smoothly update darkAlpha: dissolves when aiming, fades back in when returning to hipfire or reloading
		long currentTime = System.currentTimeMillis();
		float deltaSeconds = Math.min(0.1f, (currentTime - lastUpdateTime) / 1000.0f);
		lastUpdateTime = currentTime;

		float transitionSpeed = isAiming ? 5.0f : 8.0f; // fast fade out to black on reload
		if (isAiming && perspective != null) {
			darkAlpha = Math.max(0.0f, darkAlpha - deltaSeconds * transitionSpeed);
		} else {
			darkAlpha = Math.min(1.0f, darkAlpha + deltaSeconds * transitionSpeed);
		}

		model.setDarkAlpha(darkAlpha);

		int texId = 0;
		if (perspective != null && darkAlpha < 0.999f) {
			try {
				texId = perspective.getTexture(renderContext);
			} catch (Throwable ignored) {}
		}

		float brightness = 1.0f;
		if (perspective != null) {
			try {
				brightness = perspective.getBrightness(renderContext);
			} catch (Throwable ignored) {}
		}
		if (brightness <= 0.0f) {
			brightness = 1.0f;
		}

		if (isAiming != lastLoggedAiming || (isAiming && texId != lastLoggedTexId)) {
			lastLoggedAiming = isAiming;
			lastLoggedTexId = texId;
			int err = GL11.glGetError();
			System.out.println(String.format("[MWC-SCOPE-DEBUG] ScopePerspective.render: isAiming=%s, texId=%d, darkAlpha=%.2f, brightness=%.2f, glError=%d",
					isAiming, texId, darkAlpha, brightness, err));
		}

		GlStateManager.pushMatrix();

		positioning.run();

		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		if (texId > 0) {
			GlStateManager.bindTexture(texId);
		} else {
			MC.getTextureManager().bindTexture(DARK_SCREEN);
		}

		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

		GlStateManager.color(brightness, brightness, brightness, 1.0F);

		if (model != null && renderContext != null) {
			model.render(this.reticle, renderContext, renderContext.getPlayer(), renderContext.getScale());
		}

		OpenGlHelper.glUseProgram(0);

		int postErr = GL11.glGetError();
		if (postErr != GL11.GL_NO_ERROR) {
			System.err.println("[MWC-SCOPE-DEBUG] GL error in ScopePerspective after render: " + postErr);
		}

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
