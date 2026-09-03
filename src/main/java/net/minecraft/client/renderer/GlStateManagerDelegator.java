package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;
import com.teamderpy.shouldersurfing.client.CameraEntityRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GlStateManagerDelegator
{
	public static boolean GlStateManager_blendFunc(int srcFactor, int dstFactor, GlStateManager.BlendState blendState)
	{
		if(CameraEntityRenderer.getInstance().isRenderingCameraEntity() && CameraEntityRenderer.getInstance().getCameraEntityAlpha() < 1.0F && srcFactor == GL11.GL_ONE && dstFactor == GL11.GL_ZERO && !(blendState.srcFactor == GL11.GL_SRC_COLOR && blendState.dstFactor == GL11.GL_ONE))
		{
			blendState.srcFactor = GL11.GL_SRC_ALPHA;
			blendState.dstFactor = GL11.GL_ONE_MINUS_SRC_ALPHA;
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			return true;
		}
		
		return false;
	}
	
	public static boolean GlStateManager_tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha, GlStateManager.BlendState blendState)
	{
		return false;
	}
}
