package com.paneedah.weaponlib;

import com.paneedah.mwc.utils.ModReference;
import com.paneedah.weaponlib.animation.ClientValueRepo;
import com.paneedah.weaponlib.compatibility.FlatSurfaceModelBox;
import com.paneedah.weaponlib.config.ModernConfigManager;
import com.paneedah.weaponlib.perspective.OpticalScopePerspective;
import com.paneedah.weaponlib.render.bgl.PostProcessPipeline;
import com.paneedah.weaponlib.render.scopes.Reticle;
import com.paneedah.weaponlib.shader.jim.Shader;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static com.paneedah.mwc.proxies.ClientProxy.MC;
import static com.paneedah.mwc.utils.ModReference.ID;

public class ViewfinderModel extends ModelBase {

	private static final ResourceLocation SCOPE_GRIME_TEXTURE = new ResourceLocation(
			ID + ":textures/hud/scopedirt.png");

	/**
	 * When true, forces the scope shader to render only the darkLensColor + circular mask,
	 * ignoring scopedirt/grime. Used during unequip when no active perspective is available.
	 */
	private boolean forceDarkMode = false;
	private float darkAlpha = 1.0f;

	public void setForceDarkMode(boolean force) {
		this.forceDarkMode = force;
		this.darkAlpha = force ? 1.0f : 0.0f;
	}

	public void setDarkAlpha(float alpha) {
		this.darkAlpha = alpha;
		this.forceDarkMode = (alpha >= 0.999f);
	}

	private ModelRenderer surfaceRenderer;
	private FlatSurfaceModelBox box;

	public ViewfinderModel() {
		textureWidth = 128;
		textureHeight = 64;

		surfaceRenderer = new ModelRenderer(this, 0, 0);
		box = new FlatSurfaceModelBox(surfaceRenderer, 0, 0, 0f, 0f, 0f, 3, 3, 0, 0.0F);
		surfaceRenderer.cubeList.add(box);
		surfaceRenderer.mirror = true;
		surfaceRenderer.setRotationPoint(0F, -10F, 0F);
		surfaceRenderer.setTextureSize(100, 100);

		setRotation(surfaceRenderer, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {

		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		surfaceRenderer.render(scale);
	}

	public void render(Reticle ret, RenderContext<RenderableState> renderContext, Entity entity, float f5) {

		if (ModernConfigManager.enableAllShaders && ModernConfigManager.enableScopeEffects) {
			renderWithScopeFX(ret, renderContext, entity, f5);
		} else {
			renderDry(ret, entity, f5);
		}

	}

	public void renderDry(Reticle ret, Entity entity, float f5) {
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		surfaceRenderer.render(f5);

		if (ret != null && ret.getReticleTexture() != null) {
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			MC.getTextureManager().bindTexture(ret.getReticleTexture());
			GlStateManager.pushMatrix();

			GlStateManager.pushMatrix();
			double yOff = 0.68;
			double xOff = -0.119;
			GlStateManager.translate(-xOff, -yOff, 0);
			GlStateManager.rotate(180f, 0, 0, 1);
			GlStateManager.translate(xOff, yOff, 0.001);

			surfaceRenderer.render(f5);

			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
		}
		GlStateManager.enableCull();
	}

	public void renderWithScopeFX(Reticle ret, RenderContext<RenderableState> renderContext, Entity entity, float f5) {
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

		boolean isNightVisionOn = false;
		boolean useWhitePhosphor = false;
		if (renderContext != null && renderContext.getWeaponInstance() != null) {
			ItemAttachment<Weapon> scope = renderContext.getWeaponInstance()
					.getAttachmentItemWithCategory(AttachmentCategory.SCOPE);
			if (scope != null) {
				if (scope instanceof ItemScope) {
					isNightVisionOn = ((ItemScope) scope).hasNightVision()
							&& renderContext.getWeaponInstance().isNightVisionOn();
					useWhitePhosphor = ((ItemScope) scope).usesWhitePhosphor();
				}
			}
		}

		Shader scopeShader = OpticalScopePerspective.scope;
		scopeShader.use();

		GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
		scopeShader.uniform1i("tex0", 0);

		boolean shouldDoFog = PostProcessPipeline.shouldDoFog() && PostProcessPipeline.getScopeDepthTexture() != null;
		if (shouldDoFog) {
			GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + 3);
			GlStateManager.bindTexture(PostProcessPipeline.getScopeDepthTexture().getTexture());
			GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
		}

		GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + 4);
		if (ret != null && ret.getReticleTexture() != null) {
			MC.getTextureManager().bindTexture(ret.getReticleTexture());
		} else {
			MC.getTextureManager().bindTexture(new ResourceLocation(ID, "textures/crosshairs/reticle1.png"));
		}
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);

		GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + 6);
		MC.getTextureManager().bindTexture(SCOPE_GRIME_TEXTURE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);

		float pwi = 0f;
		if (!forceDarkMode && ClientModContext.getContext() != null && ClientModContext.getContext().getMainHeldWeapon() != null) {
			PlayerWeaponInstance weapon = ClientModContext.getContext().getMainHeldWeapon();
			pwi = weapon.getZoom();
			if (weapon.state != WeaponState.READY
					&& weapon.state != WeaponState.PAUSED
					&& weapon.state != WeaponState.EJECT_REQUIRED
					&& weapon.state != WeaponState.ALERT) {
				ClientValueRepo.scopeY.currentValue = 1;
			}
		}

		scopeShader.uniform1f("reticleZoom", (pwi + 0.86f));
		// When forceDarkMode=true (unequip / no active perspective), use -1.0:
		// this maximises shadowSensitivity so vignette collapses to 0 -> only darkLensColor + circular mask.
		scopeShader.uniform1f("actualZoom", forceDarkMode ? -1.0f : (1.0f - pwi) - 0.80f);

		if (shouldDoFog) {
			scopeShader.boolean1b("shouldDoFog", true);
			scopeShader.uniform1i("depthTex", 3);

			float fogIntensity = 0.6f;
			if (MC.world != null) {
				fogIntensity *= MC.world.getRainStrength(MC.getRenderPartialTicks());
			}
			scopeShader.uniform1f("fogIntensity", fogIntensity);
			scopeShader.uniform3f("baseFogColor", 0.6f, 0.6f, 0.6f);
		} else {
			scopeShader.boolean1b("shouldDoFog", false);
		}

		scopeShader.uniform1i("reticle", 4);
		scopeShader.uniform1i("dirt", 6);

		scopeShader.uniform2f("resolution", MC.displayWidth, MC.displayHeight);
		scopeShader.boolean1b("isNightVisionOn", isNightVisionOn);
		scopeShader.boolean1b("useWhitePhosphor", useWhitePhosphor);
		scopeShader.uniform1f("time", ClientValueRepo.TICKER.getLerpedFloat());
		scopeShader.boolean1b("isForcedDark", forceDarkMode);
		scopeShader.uniform1f("darkAlpha", darkAlpha);

		GlStateManager.enableBlend();
		surfaceRenderer.render(f5);
		scopeShader.release();
		GlStateManager.enableCull();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
