package com.teamderpy.shouldersurfing.event;

import java.util.List;
import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;
import com.teamderpy.shouldersurfing.client.KeyHandler;
import com.teamderpy.shouldersurfing.client.ShoulderHelper;
import com.teamderpy.shouldersurfing.client.ShoulderHelper.ShoulderLook;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import com.teamderpy.shouldersurfing.config.Config;
import com.teamderpy.shouldersurfing.config.Perspective;
import com.teamderpy.shouldersurfing.lockon.LockOnHandler;

import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
	private static final float TURN_SPEED = 0.35F;
	private static double smoothedDistance = 10.0;

	public static int     followTimer          = 0;
	public static int     turningLockTime      = 0;
	private static boolean wasMovingWhenSnapped = false;
	private static float   savedWorldMoveX      = 0;
	private static float   savedWorldMoveZ      = 0;

	// Флаг реального взаимодействия с миром в этом тике
	private static boolean interactedThisTick = false;

	// ────────────────────────────────────────────────────────────────────────────
	//  PlayerTick — поворот к цели при атаке / follow-through
	//  Когда активен LockOn, эту логику пропускаем: LockOnHandler сам управляет
	//  поворотом через smoothLook().
	// ────────────────────────────────────────────────────────────────────────────

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		// Consolidate movement handling into onInputUpdate
	}

	// ────────────────────────────────────────────────────────────────────────────
	//  InputUpdate — движение и поворот тела
	// ────────────────────────────────────────────────────────────────────────────

	@SubscribeEvent
	public void onInputUpdate(InputUpdateEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.player == null || event.getEntityPlayer() != mc.player) return;

		ShoulderInstance instance = ShoulderInstance.getInstance();
		if (!instance.doShoulderSurfing()) return;

		ShoulderRenderer renderer = ShoulderRenderer.getInstance();
		boolean isAiming = instance.isAiming();

		float camYaw  = renderer.cameraYaw + 180.0F;
		float forward = event.getMovementInput().moveForward;
		float strafe  = event.getMovementInput().moveStrafe;

		// ── Если активен LockOn — управляем только телом при движении ──────────
		if (LockOnHandler.lockedOn && LockOnHandler.target != null) {
			if (forward != 0.0F || strafe != 0.0F) {
				float camYawRad   = (float) Math.toRadians(camYaw);
				float worldX = -MathHelper.sin(camYawRad) * forward + MathHelper.cos(camYawRad) * strafe;
				float worldZ =  MathHelper.cos(camYawRad) * forward + MathHelper.sin(camYawRad) * strafe;

				float playerYawRad = (float) Math.toRadians(mc.player.rotationYaw);
				float newForward   = worldX * -MathHelper.sin(playerYawRad) + worldZ * MathHelper.cos(playerYawRad);
				float newStrafe    = worldX * MathHelper.cos(playerYawRad) + worldZ * MathHelper.sin(playerYawRad);

				event.getMovementInput().moveForward = newForward;
				event.getMovementInput().moveStrafe  = newStrafe;

				mc.player.renderYawOffset = mc.player.rotationYaw;
			}
			return;
		}

		// ── follow-таймер (без LockOn) ──────────────────────────────────────────
		if (followTimer > 0 && (forward != 0 || strafe != 0)) {
			float yawRad = (float) Math.toRadians(mc.player.rotationYaw);

			if (wasMovingWhenSnapped) {
				float camYawRad   = (float) Math.toRadians(camYaw);
				float curX = -MathHelper.sin(camYawRad) * forward + MathHelper.cos(camYawRad) * strafe;
				float curZ =  MathHelper.cos(camYawRad) * forward + MathHelper.sin(camYawRad) * strafe;
				float len  = MathHelper.sqrt(curX * curX + curZ * curZ);
				if (len > 0) { curX /= len; curZ /= len; }

				float dot = savedWorldMoveX * curX + savedWorldMoveZ * curZ;
				if (dot < 0.5F) {
					followTimer          = 0;
					wasMovingWhenSnapped = false;
				} else {
					float nf = savedWorldMoveX * (-MathHelper.sin(yawRad)) + savedWorldMoveZ * MathHelper.cos(yawRad);
					float ns = savedWorldMoveX * MathHelper.cos(yawRad)  + savedWorldMoveZ * MathHelper.sin(yawRad);
					float origMag = MathHelper.sqrt(forward * forward + strafe * strafe);
					event.getMovementInput().moveForward = nf * origMag;
					event.getMovementInput().moveStrafe  = ns * origMag;

					float bodyTarget = (float) Math.toDegrees(Math.atan2(-savedWorldMoveX, savedWorldMoveZ));
					float bodyDiff   = MathHelper.wrapDegrees(bodyTarget - mc.player.renderYawOffset);
					mc.player.renderYawOffset += bodyDiff * 0.15F;
					return;
				}
			} else {
				float camYawRad = (float) Math.toRadians(camYaw);
				float wx = -MathHelper.sin(camYawRad) * forward + MathHelper.cos(camYawRad) * strafe;
				float wz =  MathHelper.cos(camYawRad) * forward + MathHelper.sin(camYawRad) * strafe;
				float nf = wx * (-MathHelper.sin(yawRad)) + wz * MathHelper.cos(yawRad);
				float ns = wx * MathHelper.cos(yawRad)  + wz * MathHelper.sin(yawRad);
				float origMag = MathHelper.sqrt(forward * forward + strafe * strafe);
				event.getMovementInput().moveForward = nf * origMag;
				event.getMovementInput().moveStrafe  = ns * origMag;
				mc.player.renderYawOffset = mc.player.rotationYaw;
				return;
			}
		}

		// ── Отслеживание взаимодействия (ломание/удар ЛКМ или использование ПКМ) ─
		boolean isAttacking = mc.gameSettings.keyBindAttack.isKeyDown();
		boolean isInteracting = mc.gameSettings.keyBindUseItem.isKeyDown();
		boolean isUsingItem = mc.player.isHandActive();

		if (isAttacking || isInteracting || isUsingItem) {
			turningLockTime = 10;
		} else if (turningLockTime > 0) {
			turningLockTime--;
		}

		boolean isHoldingRanged = com.teamderpy.shouldersurfing.util.WeaponHelper.isPlayerHoldingWeaponOrGrenade(mc.player);
		boolean isHoldingMWC = false;
		if (mc.player != null) {
			ItemStack main = mc.player.getHeldItemMainhand();
			if (!main.isEmpty() && (main.getItem() instanceof com.paneedah.weaponlib.Weapon || main.getItem() instanceof com.paneedah.weaponlib.grenade.ItemGrenade)) {
				isHoldingMWC = true;
			}
		}

		if (isAiming || isHoldingRanged || turningLockTime > 0) {
			followTimer          = 0;
			wasMovingWhenSnapped = false;
			interactedThisTick   = false;

			float tYaw;
			float tPitch;
			float speedMultiplier = 0.9F;

			if (isHoldingMWC) {
				double reach = 150.0;
				RayTraceResult hit = com.teamderpy.shouldersurfing.client.world.ObjectPicker.getInstance().pick(reach, 1.0F);
				if (hit != null && hit.entityHit != null && hit.hitVec != null) {
					// Сохраняем параллакс при наведении на энтити (мобы, игроки)
					Vec3d eyePos = mc.player.getPositionEyes(1.0F);
					Vec3d dir = hit.hitVec.subtract(eyePos).normalize();
					tYaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
					tPitch = (float) Math.toDegrees(-Math.asin(MathHelper.clamp(dir.y, -1.0, 1.0)));
					speedMultiplier = 0.95F;
				} else {
					// Без трекинга на блоки (параллельно направлению камеры)
					tYaw = camYaw;
					tPitch = renderer.cameraPitch;
					speedMultiplier = 1.0F;
				}
			} else {
				double reach = (isAiming || isHoldingRanged) ? 150.0 : 10.0;
				RayTraceResult hit = com.teamderpy.shouldersurfing.client.world.ObjectPicker.getInstance().pick(reach, 1.0F);

				Vec3d targetPoint;
				if (hit != null && hit.hitVec != null) {
					targetPoint = hit.hitVec;
				} else {
					ShoulderLook look = ShoulderHelper.shoulderSurfingLook(mc.player, 1.0F, reach * reach);
					targetPoint = look.traceEndPos();
				}

				Vec3d eyePos = mc.player.getPositionEyes(1.0F);
				Vec3d dir = targetPoint.subtract(eyePos).normalize();
				tYaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
				tPitch = (float) Math.toDegrees(-Math.asin(MathHelper.clamp(dir.y, -1.0, 1.0)));
			}

			float yawDiff = MathHelper.wrapDegrees(tYaw - mc.player.rotationYaw);
			float pitchDiff = MathHelper.wrapDegrees(tPitch - mc.player.rotationPitch);
			mc.player.rotationYaw += yawDiff * speedMultiplier;
			mc.player.rotationPitch += pitchDiff * speedMultiplier;
			mc.player.rotationYawHead = mc.player.rotationYaw;

			if (forward != 0.0F || strafe != 0.0F) {
				double angle = Math.toDegrees(Math.atan2(strafe, forward));
				float bodyTarget = camYaw - (float) angle;
				float bodyDiff = MathHelper.wrapDegrees(bodyTarget - mc.player.renderYawOffset);
				mc.player.renderYawOffset += bodyDiff * TURN_SPEED;

				float camYawRad = (float) Math.toRadians(camYaw);
				float worldX = -MathHelper.sin(camYawRad) * forward + MathHelper.cos(camYawRad) * strafe;
				float worldZ =  MathHelper.cos(camYawRad) * forward + MathHelper.sin(camYawRad) * strafe;

				float playerYawRad = (float) Math.toRadians(mc.player.rotationYaw);
				float newForward = worldX * -MathHelper.sin(playerYawRad) + worldZ * MathHelper.cos(playerYawRad);
				float newStrafe = worldX * MathHelper.cos(playerYawRad) + worldZ * MathHelper.sin(playerYawRad);

				event.getMovementInput().moveForward = newForward;
				event.getMovementInput().moveStrafe  = newStrafe;
			}
		} else {
			// ── Обычный shoulder-surfing без прицела ────────────────────────────
			float pitchDiff = MathHelper.wrapDegrees(renderer.cameraPitch - mc.player.rotationPitch);
			mc.player.rotationPitch += pitchDiff * TURN_SPEED;

			if (forward != 0.0F || strafe != 0.0F) {
				double angle = Math.toDegrees(Math.atan2(strafe, forward));
				float tYaw = camYaw - (float) angle;
				float yawDiff = MathHelper.wrapDegrees(tYaw - mc.player.rotationYaw);
				mc.player.rotationYaw += yawDiff * TURN_SPEED;
				mc.player.renderYawOffset = mc.player.rotationYaw;
				mc.player.rotationYawHead = mc.player.rotationYaw;

				float camYawRad = (float) Math.toRadians(camYaw);
				float worldX = -MathHelper.sin(camYawRad) * forward + MathHelper.cos(camYawRad) * strafe;
				float worldZ =  MathHelper.cos(camYawRad) * forward + MathHelper.sin(camYawRad) * strafe;

				float playerYawRad = (float) Math.toRadians(mc.player.rotationYaw);
				float newForward = worldX * -MathHelper.sin(playerYawRad) + worldZ * MathHelper.cos(playerYawRad);
				float newStrafe = worldX * MathHelper.cos(playerYawRad) + worldZ * MathHelper.sin(playerYawRad);

				event.getMovementInput().moveForward = newForward;
				event.getMovementInput().moveStrafe  = newStrafe;
			}
		}
	}

	// ────────────────────────────────────────────────────────────────────────────
	//  Остальные события
	// ────────────────────────────────────────────────────────────────────────────

	@SubscribeEvent
	public void clientTickEvent(ClientTickEvent event)
	{
		if (event.phase.equals(Phase.START)) {
			ShoulderInstance.getInstance().tick();
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void preRenderLivingEntityEventLast(RenderLivingEvent.Pre<?> event)
	{
		if (event.isCanceled() && event.getEntity() == Minecraft.getMinecraft().getRenderViewEntity()) {
			ShoulderRenderer.getInstance().postRenderCameraEntity();
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void postRenderLivingEntityEvent(RenderLivingEvent.Post<?> event)
	{
		if (event.getEntity() == Minecraft.getMinecraft().getRenderViewEntity()) {
			ShoulderRenderer.getInstance().postRenderCameraEntity();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public void preRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event)
	{
		boolean doRender = Config.CLIENT.getCrosshairVisibility(Perspective.current())
				.doRender(Minecraft.getMinecraft().objectMouseOver, ShoulderInstance.getInstance().isAiming());

		if (event.getType().equals(RenderGameOverlayEvent.ElementType.CROSSHAIRS)) {
			if (doRender) {
				ShoulderRenderer.getInstance().offsetCrosshair(event.getResolution(), event.getPartialTicks());
			} else {
				event.setCanceled(true);
			}
		} else if (doRender && event.getType().equals(RenderGameOverlayEvent.ElementType.BOSSHEALTH)) {
			ShoulderRenderer.getInstance().clearCrosshairOffset();
		}
	}

	@SubscribeEvent
	public void renderWorldLast(RenderWorldLastEvent event)
	{
		ShoulderRenderer.getInstance().updateDynamicRaytrace(event.getPartialTicks());
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event)
	{
		KeyHandler.onInput();
	}

	@SubscribeEvent
	public void onMouseInput(InputEvent.MouseInputEvent event)
	{
		KeyHandler.onInput();
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		Config.CLIENT.sync();
	}

	@SubscribeEvent
	public void onGuiClosed(GuiOpenEvent event)
	{
		if (event.getGui() == null) {
			Keyboard.enableRepeatEvents(true);
		}
	}

	@SubscribeEvent
	public void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
	{
		ShoulderInstance instance = ShoulderInstance.getInstance();
		if (instance.doShoulderSurfing()) {
			ShoulderRenderer renderer = ShoulderRenderer.getInstance();
			event.setYaw(renderer.cameraYaw);
			event.setPitch(renderer.cameraPitch);
		}
	}

	// ────────────────────────────────────────────────────────────────────────────
	//  Обработчики реального взаимодействия с миром
	//  Только эти события выставляют interactedThisTick = true,
	//  что разрешает поворот камеры в onPlayerTick.
	// ────────────────────────────────────────────────────────────────────────────

	/**
	 * Удар по сущности (ЛКМ по мобу/игроку).
	 */
	@SubscribeEvent
	public void onAttackEntity(net.minecraftforge.event.entity.player.AttackEntityEvent event)
	{
		if (event.getEntityPlayer() == Minecraft.getMinecraft().player) {
			interactedThisTick = true;
		}
	}

	/**
	 * ПКМ по сущности (кормёжка, торговля, посадка на лошадь и т.д.).
	 */
	@SubscribeEvent
	public void onEntityInteract(net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract event)
	{
		if (event.getEntityPlayer() == Minecraft.getMinecraft().player) {
			interactedThisTick = true;
		}
	}

	/**
	 * ПКМ по блоку.
	 *
	 * Логика приоритетов повторяет ванильный Minecraft:
	 *   1. Если блок интерактивен (сундук, печь, дверь, рычаг...) — поворачиваем,
	 *      даже если в руке еда или щит (блок всегда получает клик первым).
	 *   2. Если блок не интерактивен — смотрим на предмет в руках:
	 *      - Предмет с длительным использованием (еда, щит, зелье) в любой руке → не поворачиваем.
	 *      - Ставим блок (ItemBlock) → поворачиваем.
	 *      - Пустая рука или инструмент без действия → не поворачиваем.
	 */
	@SubscribeEvent
	public void onRightClickBlock(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event)
	{
		if (event.getEntityPlayer() != Minecraft.getMinecraft().player) return;

		net.minecraft.block.state.IBlockState state = event.getWorld().getBlockState(event.getPos());
		net.minecraft.block.Block block = state.getBlock();

		// Шаг 1: блок интерактивен — поворачиваем независимо от предмета в руке.
		boolean isInteractive = block.hasTileEntity(state)                              // сундуки, печи, воронки, ...
				|| block instanceof net.minecraft.block.BlockWorkbench
				|| block instanceof net.minecraft.block.BlockAnvil
				|| block instanceof net.minecraft.block.BlockDoor
				|| block instanceof net.minecraft.block.BlockTrapDoor
				|| block instanceof net.minecraft.block.BlockFenceGate
				|| block instanceof net.minecraft.block.BlockLever
				|| block instanceof net.minecraft.block.BlockButton
				|| block instanceof net.minecraft.block.BlockBed;

		if (isInteractive) {
			interactedThisTick = true;
			return;
		}

		// Шаг 2: блок не интерактивен — проверяем предмет в руках.
		ItemStack main = event.getEntityPlayer().getHeldItemMainhand();
		ItemStack off  = event.getEntityPlayer().getHeldItemOffhand();

		// Предмет с длительным использованием (еда, щит, зелье, лук) — НЕ ItemBlock.
		boolean mainConsumes = !main.isEmpty()
				&& main.getItem().getMaxItemUseDuration(main) > 0
				&& !(main.getItem() instanceof net.minecraft.item.ItemBlock);
		boolean offConsumes  = !off.isEmpty()
				&& off.getItem().getMaxItemUseDuration(off) > 0
				&& !(off.getItem() instanceof net.minecraft.item.ItemBlock);

		if (mainConsumes || offConsumes) return; // еда/щит — не поворачиваем

		// Ставим блок — поворачиваем.
		boolean placingBlock = (!main.isEmpty() && main.getItem() instanceof net.minecraft.item.ItemBlock)
				|| (!off.isEmpty() && off.getItem() instanceof net.minecraft.item.ItemBlock);

		if (placingBlock) {
			interactedThisTick = true;
		}
		// Пустая рука или инструмент без действия — ничего не делаем.
	}
}