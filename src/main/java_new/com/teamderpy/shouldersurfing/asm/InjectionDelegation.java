package com.teamderpy.shouldersurfing.asm;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import com.teamderpy.shouldersurfing.client.CameraEntityRenderer;
import com.teamderpy.shouldersurfing.client.ShoulderHelper;
import com.teamderpy.shouldersurfing.client.ShoulderHelper.ShoulderLook;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import com.teamderpy.shouldersurfing.config.Config;
import com.teamderpy.shouldersurfing.config.Perspective;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class InjectionDelegation
{
	public static Entry<Vec3d, Vec3d> EntityRenderer_getMouseOver(double blockReach)
	{
		Entity cameraEntity = Minecraft.getMinecraft().getRenderViewEntity();
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

		if(ShoulderInstance.getInstance().doShoulderSurfing() && !Config.CLIENT.getCrosshairType().isDynamic())
		{
			ShoulderLook look = ShoulderHelper.shoulderSurfingLook(cameraEntity, partialTicks, blockReach * blockReach);
			// ФИКС: Возвращаем позицию КАМЕРЫ (cameraPos), а не глаз, чтобы луч шел точно из центра экрана!
			return new SimpleEntry<>(look.cameraPos(), look.traceEndPos());
		}

		Vec3d look = cameraEntity.getLook(1.0F);
		Vec3d start = cameraEntity.getPositionEyes(partialTicks);
		Vec3d end = start.add(look.scale(blockReach));
		return new SimpleEntry<>(start, end);
	}
	public static RayTraceResult Item_rayTraceBlocks(World level, Vec3d start, Vec3d end, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
	{
		if(ShoulderInstance.getInstance().doShoulderSurfing() && !Config.CLIENT.getCrosshairType().isDynamic())
		{
			Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
			ShoulderLook look = ShoulderHelper.shoulderSurfingLook(entity, 1.0F, start.squareDistanceTo(end));
			Vec3d eyePosition = entity.getPositionEyes(1.0F);

			// Возвращаем headOffset
			Vec3d from = look.cameraPos();
			return level.rayTraceBlocks(from, look.traceEndPos(), stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		}

		return level.rayTraceBlocks(start, end, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
	}
	
	public static RayTraceResult ItemBoat_rayTraceBlocks(World level, Vec3d start, Vec3d end, boolean stopOnLiquid)
	{
		if(ShoulderInstance.getInstance().doShoulderSurfing() && !Config.CLIENT.getCrosshairType().isDynamic())
		{
			Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
			float partialTick = Minecraft.getMinecraft().getRenderPartialTicks();
			Vec3d cameraOffset = ShoulderHelper.calcCameraOffset(ShoulderRenderer.getInstance().getCameraDistance(), entity.rotationYaw, entity.rotationPitch, partialTick);
			Vec3d headOffset = ShoulderHelper.calcRayTraceHeadOffset(cameraOffset);
			return level.rayTraceBlocks(start.add(headOffset), end.add(headOffset), stopOnLiquid);
		}
		
		return level.rayTraceBlocks(start, end, stopOnLiquid);
	}
	
	public static RayTraceResult EntityRenderer_rayTrace(World world, Vec3d vec1, Vec3d vec2)
	{
		return world.rayTraceBlocks(vec1, vec2, false, true, false);
	}

	public static void EntityRenderer_orientCamera(float x, float y, float z, float yaw, float pitch)
	{
		// ФИКС РАССИНХРОНА №1: Заставляем позицию камеры вращаться вместе с нашей мышью
		if (ShoulderInstance.getInstance().doShoulderSurfing()) {
			ShoulderRenderer renderer = ShoulderRenderer.getInstance();
			yaw = renderer.cameraYaw;
			pitch = renderer.cameraPitch;
		}

		ShoulderRenderer.getInstance().offsetCamera(x, y, z, yaw, pitch);
	}
	public static boolean Entity_turn(Entity entity, float yaw, float pitch)
	{
		if (entity instanceof EntityPlayerSP)
		{
			ShoulderInstance instance = ShoulderInstance.getInstance();
			if (instance.doShoulderSurfing())
			{
				ShoulderRenderer renderer = ShoulderRenderer.getInstance();

				// Чтобы камера не дергалась на Юг при первом включении
				if (renderer.cameraYaw == 0.0F && renderer.cameraPitch == 0.0F) {
					renderer.cameraYaw = entity.rotationYaw + 180.0F;
					renderer.cameraPitch = entity.rotationPitch;
				}

				renderer.cameraYaw += yaw * 0.15F;
				renderer.cameraPitch -= pitch * 0.15F;
				renderer.cameraPitch = MathHelper.clamp(renderer.cameraPitch, -90.0F, 90.0F);

				return true;
			}
		}
		return false;
	}
	
	public static RayTraceResult EntityPlayer_rayTrace(Entity entity, double blockReachDistance, float partialTicks)
	{
		if(ShoulderInstance.getInstance().doShoulderSurfing())
		{
			return ShoulderHelper.traceBlocks(entity, false, blockReachDistance, partialTicks, !Config.CLIENT.getCrosshairType().isDynamic());
		}
		
		Vec3d look = entity.getLook(partialTicks);
		Vec3d start = entity.getPositionEyes(partialTicks);
		Vec3d end = start.add(look.scale(blockReachDistance));
		return entity.world.rayTraceBlocks(start, end, false, false, true);
	}
	
	public static int GuiIngame_renderAttackIndicator()
	{
		return Config.CLIENT.getCrosshairVisibility(Perspective.current()).doRender(Minecraft.getMinecraft().objectMouseOver, ShoulderInstance.getInstance().isAiming()) ? 0 : 1;
	}
	
	public static double ValkyrienSkiesMixinEntityRenderer_orientCamera_cameraDistance()
	{
		return ShoulderRenderer.getInstance().getCameraDistance();
	}
	
	public static float GlStateManager_color(float alpha)
	{
		if (CameraEntityRenderer.getInstance().isRenderingCameraEntity())
		{
			return Math.min(CameraEntityRenderer.getInstance().getCameraEntityAlpha(), alpha);
		}
		return alpha;
	}
	
	public static boolean GlStateManager_depthMask(boolean flag1)
	{
		return flag1;
	}
	
	public static boolean GlStateManager_disableBlend()
	{
		return false;
	}
}
