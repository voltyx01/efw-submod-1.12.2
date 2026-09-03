package com.teamderpy.shouldersurfing.lockon;

import com.teamderpy.shouldersurfing.client.ShoulderHelper;
import com.teamderpy.shouldersurfing.client.ShoulderHelper.ShoulderLook;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class LockOnHandler {

    public static KeyBinding LOCK_ON = new KeyBinding("Lock On", Keyboard.KEY_R, "Simple LockOn");
    public static KeyBinding TAB    = new KeyBinding("Target Switch", Keyboard.KEY_TAB, "Simple LockOn");

    private final Minecraft mc = Minecraft.getMinecraft();

    public static EntityLivingBase target   = null;
    public static boolean           lockedOn = false;

    /** Сущность под перекрестием камеры (обновляется каждый тик). */
    public static EntityLivingBase highlighted = null;

    private int   wallTimer   = 0;
    private float targetYaw;
    private float targetPitch;

    // ────────────────────────────────────────────────────────────
    //  Инициализация
    // ────────────────────────────────────────────────────────────

    public static void init() {
        ClientRegistry.registerKeyBinding(LOCK_ON);
        ClientRegistry.registerKeyBinding(TAB);
    }

    // ────────────────────────────────────────────────────────────
    //  Основной тик
    // ────────────────────────────────────────────────────────────

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START || mc.player == null || mc.currentScreen != null) return;

        if (mc.player.getHealth() <= 0 || mc.player.isDead) {
            if (lockedOn) leaveLockOn();
            return;
        }

        // Всегда обновляем подсветку — моб под лучом камеры
        highlighted = getEntityUnderCameraRay(LockOnConfig.maxRange);

        // ── Обслуживаем активный лок ────────────────────────────
        if (lockedOn && target != null) {
            double distSq = mc.player.getDistanceSq(target);
            if (target.getHealth() <= 0 || distSq > LockOnConfig.maxRange * LockOnConfig.maxRange) {
                autoChangeTarget();
                return;
            }
            if (!canSeeTarget(mc.player, target)) {
                if (++wallTimer >= LockOnConfig.maxWallTimeTicks) {
                    leaveLockOn();
                    return;
                }
            } else {
                wallTimer = 0;
            }
        }

        // ── Нажатие Lock-On ─────────────────────────────────────
        // ── Нажатие Lock-On ─────────────────────────────────────
        if (LOCK_ON.isPressed()) {
            if (lockedOn) {
                leaveLockOn();
            } else {
                // Приоритет: моб под лучом камеры → иначе ближайший враг
                EntityLivingBase candidate = highlighted;
                if (candidate == null) candidate = findBestTarget(mc.player, null, true, LockOnConfig.maxRange);

                if (candidate != null) {
                    target   = candidate;
                    lockedOn = true;
                    wallTimer = 0;

                    // ── ФИКС БОЛЬШОГО РЫВКА КАМЕРЫ ──
                    // Синхронизируем тело с камерой в момент нажатия кнопки.
                    // Теперь плавное наведение начнется ровно оттуда, куда сейчас смотрит перекрестье!
                    ShoulderInstance instance = ShoulderInstance.getInstance();
                    if (instance.doShoulderSurfing()) {
                        ShoulderRenderer renderer = ShoulderRenderer.getInstance();

                        mc.player.rotationYaw = renderer.cameraYaw - 180.0F;
                        mc.player.prevRotationYaw = mc.player.rotationYaw;

                        mc.player.rotationPitch = renderer.cameraPitch;
                        mc.player.prevRotationPitch = mc.player.rotationPitch;
                    }
                }
            }
        }

        // ── Переключение цели ────────────────────────────────────
        if (TAB.isPressed() && lockedOn) {
            target = findBestTarget(mc.player, target, false, LockOnConfig.maxRange);
        }

        // ── Поворот к цели ───────────────────────────────────────
        if (lockedOn && target != null) {
            updateTargetAngles(mc.player, target);
            smoothLook(mc.player);
        }
    }

    // ────────────────────────────────────────────────────────────
    //  Сброс лока
    // ────────────────────────────────────────────────────────────

    private void leaveLockOn() {
        target   = null;
        lockedOn = false;
        wallTimer = 0;
    }

    // ────────────────────────────────────────────────────────────
    //  Определение моба под лучом камеры (shoulder или 1-е лицо)
    // ────────────────────────────────────────────────────────────

    /**
     * Выбрасывает луч из позиции камеры в направлении взгляда камеры
     * (с учётом shoulder-surfing смещения) и возвращает первого живого моба
     * на пути луча.
     */
    private EntityLivingBase getEntityUnderCameraRay(double range) {
        if (mc.world == null || mc.player == null) return null;

        Vec3d from;
        Vec3d to;

        ShoulderInstance instance = ShoulderInstance.getInstance();

        if (instance.doShoulderSurfing()) {
            // ── Режим shoulder-surfing ───────────────────────────
            // Берём позицию камеры из shoulderSurfingLook,
            // но направление строим ЧИСТО по yaw/pitch камеры — без headOffset.
            ShoulderLook look    = ShoulderHelper.shoulderSurfingLook(mc.player, 1.0F, range * range);
            from = look.cameraPos();

            ShoulderRenderer renderer = ShoulderRenderer.getInstance();
            float yawRad   = renderer.cameraYaw   * ShoulderHelper.DEG_TO_RAD;
            float pitchRad = renderer.cameraPitch  * ShoulderHelper.DEG_TO_RAD;
            // Вектор взгляда камеры (та же формула что в shoulderSurfingLook для isDecoupled)
            Vec3d camDir = new Vec3d(
                    MathHelper.sin(yawRad)  * MathHelper.cos(pitchRad),
                    -MathHelper.sin(pitchRad),
                    -MathHelper.cos(yawRad)  * MathHelper.cos(pitchRad)
            );
            to = from.add(camDir.scale(range));
        } else {
            // ── Первое лицо / обычный вид ───────────────────────
            from = mc.player.getPositionEyes(1.0F);
            Vec3d lookVec = mc.player.getLook(1.0F);
            to   = from.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
        }

        Vec3d dir = to.subtract(from);
        AxisAlignedBB searchBox = new AxisAlignedBB(
                Math.min(from.x, to.x) - 1, Math.min(from.y, to.y) - 1, Math.min(from.z, to.z) - 1,
                Math.max(from.x, to.x) + 1, Math.max(from.y, to.y) + 1, Math.max(from.z, to.z) + 1
        );

        List<Entity> entities = mc.world.getEntitiesInAABBexcluding(mc.player, searchBox,
                e -> e instanceof EntityLivingBase && e.canBeCollidedWith() && e != mc.player);

        EntityLivingBase closest  = null;
        double           bestDist = Double.MAX_VALUE;

        for (Entity e : entities) {
            EntityLivingBase living = (EntityLivingBase) e;
            AxisAlignedBB    aabb   = living.getEntityBoundingBox().grow(0.3);
            RayTraceResult   res    = aabb.calculateIntercept(from, to);
            if (res != null) {
                double d = from.distanceTo(res.hitVec);
                if (d < bestDist) {
                    bestDist = d;
                    closest  = living;
                }
            }
        }
        return closest;
    }

    // ────────────────────────────────────────────────────────────
    //  Авто-смена цели
    // ────────────────────────────────────────────────────────────

    private void autoChangeTarget() {
        EntityLivingBase newTarget = findBestTarget(mc.player, null, true, LockOnConfig.autoSwitchRange);
        if (newTarget != null) target = newTarget;
        else leaveLockOn();
    }

    // ────────────────────────────────────────────────────────────
    //  Поиск лучшей цели
    // ────────────────────────────────────────────────────────────

    private EntityLivingBase findBestTarget(EntityPlayer player, EntityLivingBase current,
                                            boolean isAuto, double range) {
        List<EntityLivingBase> list = player.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                player.getEntityBoundingBox().grow(range, range / 2, range),
                e -> e != player && e.getHealth() > 0 && !e.isDead);

        EntityLivingBase best      = null;
        double           bestScore = Double.MAX_VALUE;

        Vec3d look = player.getLookVec();
        Vec3d eye  = player.getPositionEyes(1.0F);

        for (EntityLivingBase e : list) {
            if (e == current || !isHostile(e)) continue;
            Vec3d targetVec = getTargetFocusPoint(e).subtract(eye);
            double dist = Math.sqrt(targetVec.x * targetVec.x + targetVec.y * targetVec.y + targetVec.z * targetVec.z);
            double dot  = look.dotProduct(targetVec.normalize());
            if (dot < 0) continue;
            if (isAuto && Math.toDegrees(Math.acos(MathHelper.clamp((float)dot, -1f, 1f))) > LockOnConfig.maxAutoSwitchAngle) continue;
            if (!canSeeTarget(player, e)) continue;
            double score = isAuto ? dist : (Math.acos(MathHelper.clamp((float)dot, -1f, 1f)) * dist);
            if (score < bestScore) {
                bestScore = score;
                best      = e;
            }
        }
        return best;
    }

    // ────────────────────────────────────────────────────────────
    //  Вспомогательные методы
    // ────────────────────────────────────────────────────────────

    private boolean isHostile(EntityLivingBase e) {
        return e instanceof IMob || e.getTeam() != null;
    }

    private boolean canSeeTarget(EntityPlayer player, EntityLivingBase tgt) {
        return player.world.rayTraceBlocks(
                player.getPositionEyes(1.0F),
                getTargetFocusPoint(tgt),
                false, true, false) == null;
    }

    // Метод для старого кода, чтобы ничего не сломать
    private Vec3d getTargetFocusPoint(EntityLivingBase tgt) {
        return getTargetFocusPoint(tgt, 1.0F);
    }

    // НОВЫЙ МЕТОД: Учитывает плавность между кадрами (partialTicks)
    private Vec3d getTargetFocusPoint(EntityLivingBase tgt, float pt) {
        double x = tgt.lastTickPosX + (tgt.posX - tgt.lastTickPosX) * pt;
        double y = tgt.lastTickPosY + (tgt.posY - tgt.lastTickPosY) * pt;
        double z = tgt.lastTickPosZ + (tgt.posZ - tgt.lastTickPosZ) * pt;
        return new Vec3d(x, y + tgt.height * LockOnConfig.targetHeightMultiplier, z);
    }

    /**
     * Вычисляет углы поворота к цели с учётом текущего режима камеры.
     * В shoulder-режиме берём направление от позиции камеры, а не от глаз,
     * чтобы поворот игрока совпадал с тем, куда смотрит камера.
     */
    private void updateTargetAngles(EntityPlayer player, EntityLivingBase tgt) {
        float pt = Minecraft.getMinecraft().getRenderPartialTicks();
        Vec3d focusPoint = getTargetFocusPoint(tgt, pt);

        // 1. ТЕЛО ВСЕГДА СЧИТАЕТСЯ ОТ ГЛАЗ.
        // Глаза не смещаются по орбите при вращении, поэтому мы раз и навсегда
        // избавляемся от парадокса камеры и тряски вблизи!
        Vec3d origin = player.getPositionEyes(pt);
        Vec3d dir = focusPoint.subtract(origin);

        double horizDistSq = dir.x * dir.x + dir.z * dir.z;

        // Мертвая зона: если мы стоим почти внутри врага, перестаем бешено крутиться
        if (horizDistSq > 0.05) {
            this.targetYaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
        }

        Vec3d normDir = dir.normalize();
        this.targetPitch = (float) Math.toDegrees(-Math.asin(MathHelper.clamp((float) normDir.y, -1f, 1f)));
    }

    private boolean isHoldingRangedWeapon(EntityPlayer player) {
        return com.teamderpy.shouldersurfing.util.WeaponHelper.isPlayerHoldingWeaponOrGrenade(player);
    }

    private void smoothLook(EntityPlayer player) {
        float pt = Minecraft.getMinecraft().getRenderPartialTicks();

        // 1. Считаем плавные углы для ВСЕХ режимов (теперь это безопасно!)
        float newYaw   = interpolate(player.rotationYaw,   targetYaw,   LockOnConfig.rotationSpeed);
        float newPitch = interpolate(player.rotationPitch, targetPitch, LockOnConfig.rotationSpeed);

        // 2. Применяем кинематографичную плавность к телу (отключаем ванильные дергания)
        player.rotationYaw   = newYaw;
        player.rotationPitch = newPitch;

        player.prevRotationYaw   = newYaw;
        player.prevRotationPitch = newPitch;

        player.rotationYawHead     = newYaw;
        player.prevRotationYawHead = newYaw;

        player.renderYawOffset     = newYaw;
        player.prevRenderYawOffset = newYaw;

        // 3. Отдельно настраиваем параллакс перекрестья для Shoulder Surfing
        ShoulderInstance instance = ShoulderInstance.getInstance();
        if (instance.doShoulderSurfing() && target != null) {
            ShoulderRenderer renderer = ShoulderRenderer.getInstance();

            Vec3d eyePos = player.getPositionEyes(pt);
            Vec3d focusPos = getTargetFocusPoint(target, pt);
            double dist = eyePos.distanceTo(focusPos);

            dist = Math.max(dist, 0.3);

            double offX = renderer.getCameraOffsetX();
            double offY = renderer.getCameraOffsetY();

            float yawCorrection   = (float) Math.toDegrees(Math.atan(offX / dist));
            float pitchCorrection = (float) Math.toDegrees(Math.atan(offY / dist));

            renderer.cameraYaw   = player.rotationYaw + 180.0F + yawCorrection;
            renderer.cameraPitch = player.rotationPitch + pitchCorrection;
        }
    }

    private float interpolate(float current, float target, float speed) {
        float diff = MathHelper.wrapDegrees(target - current);

        // Эффект кинематографичного затухания (Ease-Out).
        // Вместо жесткого роботизированного шага, берем 15% от оставшегося угла.
        // Чем ближе перекрестье к цели, тем мягче и точнее движение.
        float step = diff * 0.15F;

        // Ограничиваем максимальную скорость, чтобы при переключении
        // на врага за спиной камера не делала мгновенный оборот на 180.
        return current + MathHelper.clamp(step, -speed, speed);
    }


}