package com.teamderpy.shouldersurfing.client;

import com.teamderpy.shouldersurfing.config.Config;
import com.teamderpy.shouldersurfing.config.Perspective;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShoulderInstance
{
	private static final ShoulderInstance INSTANCE = new ShoulderInstance();
	private boolean doShoulderSurfing;
	private boolean doSwitchPerspective;
	private boolean isAiming;
	private double offsetX = Config.CLIENT.getOffsetX();
	private double offsetY = Config.CLIENT.getOffsetY();
	private double offsetZ = Config.CLIENT.getOffsetZ();
	private double lastOffsetX = Config.CLIENT.getOffsetX();
	private double lastOffsetY = Config.CLIENT.getOffsetY();
	private double lastOffsetZ = Config.CLIENT.getOffsetZ();
	private double targetOffsetX = Config.CLIENT.getOffsetX();
	private double targetOffsetY = Config.CLIENT.getOffsetY();
	private double targetOffsetZ = Config.CLIENT.getOffsetZ();
	private int thirdPersonView = Config.CLIENT.getDefaultPerspective().getPointOfView();
	// Углы самой камеры

	// Transition ShoulderSurfing -> First Person
	private boolean isTransitioningToFirstPerson = false;
	private int exitTransitionTicks = 0;
	private double initialExitDistance = 1.0;
	private double startTransitionOffsetX = 0.0;
	private double startTransitionOffsetY = 0.0;
	private double startTransitionOffsetZ = 0.0;
	private static final int TOTAL_EXIT_TICKS = 5;

	private ShoulderInstance()
	{
		super();
	}
	
	public void tick()
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(this.thirdPersonView != mc.gameSettings.thirdPersonView)
		{
			if(this.doShoulderSurfing && mc.gameSettings.thirdPersonView == 0 && !this.isTransitioningToFirstPerson)
			{
				mc.gameSettings.thirdPersonView = 1;
				this.startTransitionToFirstPerson();
				return;
			}
			
			boolean wasSS = this.doShoulderSurfing;
			this.isTransitioningToFirstPerson = false;
			this.doShoulderSurfing = Config.CLIENT.replaceDefaultPerspective() && mc.gameSettings.thirdPersonView == 1;
			this.thirdPersonView = mc.gameSettings.thirdPersonView;
			if(!wasSS && this.doShoulderSurfing)
			{
				this.resetOffsetsForSmoothEntry();
			}
			if(mc.gameSettings.thirdPersonView == 0)
			{
				FirstPersonFadeManager.getInstance().startFadeIn();
			}
		}
		
		if(this.isTransitioningToFirstPerson)
		{
			if(mc.player == null || mc.player.isDead)
			{
				this.finishTransitionToFirstPerson();
				return;
			}
			
			this.exitTransitionTicks++;
			
			this.lastOffsetX = this.offsetX;
			this.lastOffsetY = this.offsetY;
			this.lastOffsetZ = this.offsetZ;
			
			float progress = (float) this.exitTransitionTicks / (float) TOTAL_EXIT_TICKS;
			progress = net.minecraft.util.math.MathHelper.clamp(progress, 0.0F, 1.0F);
			float t = progress * progress * (3.0F - 2.0F * progress);
			
			this.offsetX = this.startTransitionOffsetX + (0.0 - this.startTransitionOffsetX) * t;
			this.offsetY = this.startTransitionOffsetY + (0.0 - this.startTransitionOffsetY) * t;
			this.offsetZ = this.startTransitionOffsetZ + (-0.05 - this.startTransitionOffsetZ) * t;
			
			if(this.exitTransitionTicks >= TOTAL_EXIT_TICKS)
			{
				this.finishTransitionToFirstPerson();
			}
			return;
		}
		
		if(!Perspective.FIRST_PERSON.equals(Perspective.current()))
		{
			this.doSwitchPerspective = false;
		}
		
		this.isAiming = ShoulderHelper.isHoldingAdaptiveItem();
		
		if(this.isAiming && Config.CLIENT.getCrosshairType().doSwitchPerspective() && this.doShoulderSurfing)
		{
			this.changePerspective(Perspective.FIRST_PERSON);
			this.doSwitchPerspective = true;
		}
		else if(!this.isAiming && Perspective.FIRST_PERSON.equals(Perspective.current()) && this.doSwitchPerspective)
		{
			this.changePerspective(Perspective.SHOULDER_SURFING);
		}
		
		this.targetOffsetX = Config.CLIENT.getOffsetX();
		this.targetOffsetY = Config.CLIENT.getOffsetY();
		this.targetOffsetZ = Config.CLIENT.getOffsetZ();
		
		this.lastOffsetX = this.offsetX;
		this.lastOffsetY = this.offsetY;
		this.lastOffsetZ = this.offsetZ;
		
		double transitionSpeed = Math.max(0.55, Config.CLIENT.getCameraTransitionSpeedMultiplier());
		this.offsetX = this.lastOffsetX + (this.targetOffsetX - this.lastOffsetX) * transitionSpeed;		
		this.offsetY = this.lastOffsetY + (this.targetOffsetY - this.lastOffsetY) * transitionSpeed;
		this.offsetZ = this.lastOffsetZ + (this.targetOffsetZ - this.lastOffsetZ) * transitionSpeed;
	}
	
	public void resetOffsetsForSmoothEntry()
	{
		this.isTransitioningToFirstPerson = false;
		this.offsetX = 0.0;
		this.offsetY = 0.0;
		this.offsetZ = -0.05;
		this.lastOffsetX = 0.0;
		this.lastOffsetY = 0.0;
		this.lastOffsetZ = -0.05;
		
		this.targetOffsetX = Config.CLIENT.getOffsetX();
		this.targetOffsetY = Config.CLIENT.getOffsetY();
		this.targetOffsetZ = Config.CLIENT.getOffsetZ();
		
		if(Minecraft.getMinecraft().player != null)
		{
			ShoulderRenderer.getInstance().cameraYaw = Minecraft.getMinecraft().player.rotationYaw + 180.0F;
			ShoulderRenderer.getInstance().cameraPitch = Minecraft.getMinecraft().player.rotationPitch;
		}
	}
	
	public void startTransitionToFirstPerson()
	{
		if(!this.doShoulderSurfing || this.isTransitioningToFirstPerson) return;
		this.isTransitioningToFirstPerson = true;
		this.exitTransitionTicks = 0;
		this.startTransitionOffsetX = this.offsetX;
		this.startTransitionOffsetY = this.offsetY;
		this.startTransitionOffsetZ = this.offsetZ;
		this.initialExitDistance = Math.max(0.3, Math.sqrt(this.offsetX * this.offsetX + this.offsetY * this.offsetY + this.offsetZ * this.offsetZ));
		this.targetOffsetX = 0.0;
		this.targetOffsetY = 0.0;
		this.targetOffsetZ = -0.05;
	}
	
	public void finishTransitionToFirstPerson()
	{
		this.isTransitioningToFirstPerson = false;
		this.doShoulderSurfing = false;
		Minecraft mc = Minecraft.getMinecraft();
		mc.gameSettings.thirdPersonView = 0;
		this.thirdPersonView = 0;
		FirstPersonFadeManager.getInstance().startFadeIn();
		
		if(mc.player != null)
		{
			ShoulderRenderer renderer = ShoulderRenderer.getInstance();
			float camYaw = net.minecraft.util.math.MathHelper.wrapDegrees(renderer.cameraYaw + 180.0F);
			float camPitch = net.minecraft.util.math.MathHelper.clamp(renderer.cameraPitch, -90.0F, 90.0F);
			mc.player.rotationYaw = camYaw;
			mc.player.rotationPitch = camPitch;
			mc.player.prevRotationYaw = camYaw;
			mc.player.prevRotationPitch = camPitch;
			mc.player.rotationYawHead = camYaw;
			mc.player.prevRotationYawHead = camYaw;
			mc.player.renderYawOffset = camYaw;
			mc.player.prevRenderYawOffset = camYaw;
		}
		
		this.targetOffsetX = Config.CLIENT.getOffsetX();
		this.targetOffsetY = Config.CLIENT.getOffsetY();
		this.targetOffsetZ = Config.CLIENT.getOffsetZ();
		this.offsetX = this.targetOffsetX;
		this.offsetY = this.targetOffsetY;
		this.offsetZ = this.targetOffsetZ;
		this.lastOffsetX = this.offsetX;
		this.lastOffsetY = this.offsetY;
		this.lastOffsetZ = this.offsetZ;
	}
	
	public void changePerspective(Perspective perspective)
	{
		boolean wasSS = this.doShoulderSurfing;
		if(wasSS && Perspective.FIRST_PERSON.equals(perspective))
		{
			this.startTransitionToFirstPerson();
			return;
		}
		
		if(this.isTransitioningToFirstPerson)
		{
			this.isTransitioningToFirstPerson = false;
		}
		
		Minecraft.getMinecraft().gameSettings.thirdPersonView = perspective.getPointOfView();
		this.thirdPersonView = perspective.getPointOfView();
		this.doShoulderSurfing = Perspective.SHOULDER_SURFING.equals(perspective);
		if(!wasSS && this.doShoulderSurfing)
		{
			this.resetOffsetsForSmoothEntry();
		}
	}
	
	public boolean isTransitioningToFirstPerson()
	{
		return this.isTransitioningToFirstPerson;
	}
	
	public double getInitialExitDistance()
	{
		return this.initialExitDistance;
	}
	
	public boolean doShoulderSurfing()
	{
		return this.doShoulderSurfing;
	}
	
	public void setShoulderSurfing(boolean doShoulderSurfing)
	{
		this.doShoulderSurfing = doShoulderSurfing;
	}
	
	public boolean isAiming()
	{
		return this.isAiming;
	}
	
	public double getOffsetX()
	{
		return this.offsetX;
	}
	
	public double getOffsetXOld()
	{
		return this.lastOffsetX;
	}
	
	public double getOffsetY()
	{
		return this.offsetY;
	}
	
	public double getOffsetYOld()
	{
		return this.lastOffsetY;
	}
	
	public double getOffsetZ()
	{
		return this.offsetZ;
	}
	
	public double getOffsetZOld()
	{
		return this.lastOffsetZ;
	}
	
	public void setTargetOffsetX(double targetOffsetX)
	{
		this.targetOffsetX = targetOffsetX;
	}
	
	public void setTargetOffsetY(double targetOffsetY)
	{
		this.targetOffsetY = targetOffsetY;
	}
	
	public void setTargetOffsetZ(double targetOffsetZ)
	{
		this.targetOffsetZ = targetOffsetZ;
	}
	
	public static ShoulderInstance getInstance()
	{
		return INSTANCE;
	}
}
