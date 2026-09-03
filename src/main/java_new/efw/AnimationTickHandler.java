package efw;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;

// И импорты ваших классов, если они лежат в другом пакете:
import efw.animation.AnimationRegistry;
import efw.animation.AnimationPlayer;
import efw.animation.AnimationClip;
import java.util.WeakHashMap;
import java.util.Map;
import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class AnimationTickHandler {

    // Used to detect when a dash starts on the client side
    public static final Map<EntityPlayer, Integer> dashTicksRemaining = new WeakHashMap<>();

    public static boolean isPlayerRolling(EntityPlayer player) {
        Integer remaining = dashTicksRemaining.get(player);
        return remaining != null && remaining > 2;
    }

    public static void triggerRoll(EntityPlayer player, net.minecraft.util.math.Vec3d dir) {
        if (player == null) return;
        efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(player);
        if (ap != null) {
            Integer currentRemaining = dashTicksRemaining.get(player);
            if (currentRemaining != null && currentRemaining > 2 && "roll".equals(ap.getCurrentActionName()) && !ap.isActionFadingOut()) {
                return; // Already rolling, do not restart
            }
            dashTicksRemaining.put(player, 7);
            efw.animation.AnimationClip rollClip = efw.animation.AnimationRegistry.getClip("roll");
            if (rollClip != null) {
                ap.setAction(rollClip, 0.0f, 1.3f);
            }
            float rollYaw;
            if (dir != null && dir.lengthSquared() > 0.001) {
                rollYaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
            } else if (player.motionX * player.motionX + player.motionZ * player.motionZ > 0.001) {
                rollYaw = (float) Math.toDegrees(Math.atan2(-player.motionX, player.motionZ));
            } else {
                rollYaw = player.rotationYaw;
            }
            dashWorldYaws.put(player, rollYaw);
        }
    }

    // Stores the target yaw for the dash camera offset
    public static final Map<EntityPlayer, Float> dashWorldYaws = new WeakHashMap<>();
    private static final Map<EntityPlayer, Integer> lastCooldowns = new WeakHashMap<>();
    private static final Map<java.util.UUID, Integer> turnCooldowns = new java.util.HashMap<>();
    private static final Map<EntityPlayer, Boolean> isTacticalReloadMap = new WeakHashMap<>();
    private static final java.util.Set<EntityPlayer> pushedPlayers = java.util.Collections
            .newSetFromMap(new WeakHashMap<>());

    public static class NetworkAnimState {
        public String baseAnim;
        public String actionAnim;
        public float actionSpeed;
        public float dashYaw = Float.NaN;
    }
    private static final Map<EntityPlayer, NetworkAnimState> networkAnimStates = new WeakHashMap<>();

    public static void updateNetworkState(EntityPlayer player, String baseAnim, String actionAnim, float actionSpeed, float dashYaw) {
        NetworkAnimState state = new NetworkAnimState();
        state.baseAnim = baseAnim;
        state.actionAnim = actionAnim;
        state.actionSpeed = actionSpeed;
        state.dashYaw = dashYaw;
        networkAnimStates.put(player, state);

        if ("roll".equals(actionAnim) || "roll".equals(baseAnim)) {
            dashTicksRemaining.put(player, 7);
            if (!Float.isNaN(dashYaw)) {
                dashWorldYaws.put(player, dashYaw);
            } else {
                dashWorldYaws.put(player, player.rotationYawHead);
            }
        }
    }

    // --- НАСТРОЙКИ СКОРОСТИ АНИМАЦИЙ ---
    // Здесь вы можете настроить скорость воспроизведения для каждой отдельной
    // анимации.
    // 1.0f = нормальная скорость (как в Blockbench), 2.0f = в два раза быстрее и
    // т.д.
    // Счетчик тиков в воздухе (для задержки анимации падения)
    private static int airTicks = 0;
    private static int outOfWaterTicks = 100;

    public static float getAnimationSpeed(String animName) {
        if (animName == null)
            return 1.0f;
        if (animName.contains("lie")) {
            if (animName.contains("reload")) {
                return 0.83f;
            }
            return 0.5f; // Slower lie animations
        }
        if (animName.contains("reload")) {
            return 0.83f;
        }
        // Weapon _lower animations base speed
        if (animName.endsWith("_lower")) {
            if (animName.contains("run"))
                return 1.5f;
            if (animName.contains("crouch_walk") || animName.contains("sneak"))
                return 1.0f;
            if (animName.contains("walk"))
                return 1.0f;
        }
        switch (animName) {
            case "running":
                return 3.0f; // Старая скорость для бега
            case "walking":
                return 10.0f; // Старая скорость для ходьбы 
            case "idle_standing":
                return 1.0f; // Faster standing idle
            case "idle_sneak":
                return 0.1f;
            case "walking_backwards":
                return 3.0f; // Уменьшено, так как 10 было слишком быстро, а 1 слишком медленно
            case "walking_sneak":
                return 1.0f; // ИСПРАВЛЕНО: было 0.1, из-за чего анимация длилась 40 секунд!
            case "walking_sneak_backwards":
                return 1.0f; // ИСПРАВЛЕНО
            case "falling":
                return 0.1f;
            case "swimming":
            case "up_in_water":
            case "backwards_in_water":
            case "forward_in_water":
                return 1.8f;
            case "idle_in_water":
                return 1.3f;
            case "roll":
                return 1.3f;
            default:
                return 1.0f;
        }
    }

    private static boolean alternateSwordAnim = false;
    private static boolean wasReloadingAnimPlaying = false;
    private static int postReloadTimer = 0;
    private static boolean ignoreReloadState = false;
    private static int lieFireTicks = 0;
    private static int recentlyHitBlockTicks = 0;
    private static net.minecraft.item.Item lastHeldItem = null;
    // Last mining tool animation played — used to detect block-break false-sword
    // trigger
    private static String lastMiningToolAnim = null;
    private static int recentlyMinedTicks = 0; // Grace period after block was broken

    private int ticksSinceLastSwing = 100;

    private static Object mwcRegistry = null;
    private static java.lang.reflect.Method getMainHandItemInstanceMethod = null;

    public static final Map<EntityPlayer, Float> weaponHoldWeightMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Float> prevWeaponHoldWeightMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Float> storedRenderYawOffsetMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Float> storedPrevRenderYawOffsetMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Boolean> didOverrideYawMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Float> postRollYawOffsetMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Float> prevPostRollYawOffsetMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Float> postRollHeadYawOffsetMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Float> prevPostRollHeadYawOffsetMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Boolean> wasRollingMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Boolean> wasRollingRenderMap = new WeakHashMap<>();

    private float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
        }
        while (f >= 180.0F) {
            f -= 360.0F;
        }
        return prevYawOffset + partialTicks * f;
    }

    private static final Map<EntityPlayer, Float> storedRotationYawHeadMap = new WeakHashMap<>();
    private static final Map<EntityPlayer, Float> storedPrevRotationYawHeadMap = new WeakHashMap<>();

    @SubscribeEvent
    public void onRenderPlayerPre(net.minecraftforge.client.event.RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        boolean isLocal = (player == Minecraft.getMinecraft().player);

        float partialTicks = Minecraft.getMinecraft().isGamePaused() ? 1.0f : event.getPartialRenderTick();
        float prevWeaponHoldWeight = prevWeaponHoldWeightMap.getOrDefault(player, 0.0f);
        float weaponHoldWeight = weaponHoldWeightMap.getOrDefault(player, 0.0f);
        float interpWeight = prevWeaponHoldWeight + (weaponHoldWeight - prevWeaponHoldWeight) * partialTicks;

        efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(player);
        boolean isLyingAnim = ap != null && ap.getCurrentAnimationName() != null && ap.getCurrentAnimationName().contains("lie");
        float effectiveHoldWeight = isLyingAnim ? 1.0f : interpWeight;

        // Use rotationYaw for local player (updates every frame at full FPS by mouse), rotationYawHead for remote
        float headYaw = isLocal ?
                interpolateRotation(player.prevRotationYaw, player.rotationYaw, partialTicks) :
                interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
        float bodyYaw = interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);

        float newBodyYaw = bodyYaw;
        if (effectiveHoldWeight > 0.0f) {
            newBodyYaw += net.minecraft.util.math.MathHelper.wrapDegrees(headYaw - bodyYaw) * effectiveHoldWeight;
        }

        Integer remaining = dashTicksRemaining.get(player);
        float smoothElapsed = remaining != null ? (7.0f - (remaining - partialTicks)) * 108.0f : 0.0f;
        
        boolean isRolling = remaining != null && remaining > 2;

        float progress = isRolling ? (smoothElapsed / 500.0f) : 1.0f;

        if (isRolling) {
            if (isLocal) {
                double dx = player.posX - player.prevPosX;
                double dz = player.posZ - player.prevPosZ;
                if (dx * dx + dz * dz > 0.001) {
                    float currentWorldYaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
                    dashWorldYaws.put(player, currentWorldYaw);
                } else if (player.motionX * player.motionX + player.motionZ * player.motionZ > 0.001) {
                    float currentWorldYaw = (float) Math.toDegrees(Math.atan2(-player.motionX, player.motionZ));
                    dashWorldYaws.put(player, currentWorldYaw);
                }
            }

            Float dashWorldYaw = dashWorldYaws.get(player);
            if (dashWorldYaw != null) {
                // Smoothly blend into the turn over the first 15% of the roll
                float blend = Math.min(1.0f, progress / 0.15f);
                blend = blend * blend * (3.0f - 2.0f * blend); // Smoothstep

                float diff = net.minecraft.util.math.MathHelper.wrapDegrees(dashWorldYaw - newBodyYaw);
                newBodyYaw += diff * blend;

                float headYawDiff = net.minecraft.util.math.MathHelper.wrapDegrees(newBodyYaw - headYaw);
                headYaw += headYawDiff * blend;
            }
        }

        boolean wasRollingRender = wasRollingRenderMap.getOrDefault(player, false);
        if (!isRolling && wasRollingRender) {
            Float dwy = dashWorldYaws.get(player);
            if (dwy != null) {
                float targetYaw = player.renderYawOffset;
                float weaponWeight = isLyingAnim ? 1.0f : weaponHoldWeightMap.getOrDefault(player, 0.0f);
                if (weaponWeight > 0.0f) {
                    targetYaw = isLocal ? player.rotationYaw : player.rotationYawHead;
                }
                float bodyOffset = net.minecraft.util.math.MathHelper.wrapDegrees(dwy - targetYaw);
                postRollYawOffsetMap.put(player, bodyOffset);
                prevPostRollYawOffsetMap.put(player, bodyOffset);

                float lookYaw = isLocal ? player.rotationYaw : player.rotationYawHead;
                float headOffset = net.minecraft.util.math.MathHelper.wrapDegrees(dwy - lookYaw);
                float diff = headOffset - bodyOffset;
                while (diff > 180f) diff -= 360f;
                while (diff < -180f) diff += 360f;
                headOffset = bodyOffset + diff;
                postRollHeadYawOffsetMap.put(player, headOffset);
                prevPostRollHeadYawOffsetMap.put(player, headOffset);
            }
        }
        wasRollingRenderMap.put(player, isRolling);

        // Smooth post-roll body turn: ADD offset to both prev and current
        // (preserves their difference, so no interpolation jitter when it decays)
        float prevPostRoll = prevPostRollYawOffsetMap.getOrDefault(player, 0f);
        float currPostRoll = postRollYawOffsetMap.getOrDefault(player, 0f);
        float interpPostRoll = prevPostRoll + (currPostRoll - prevPostRoll) * partialTicks;

        float prevPostRollHead = prevPostRollHeadYawOffsetMap.getOrDefault(player, 0f);
        float currPostRollHead = postRollHeadYawOffsetMap.getOrDefault(player, 0f);
        float interpPostRollHead = prevPostRollHead + (currPostRollHead - prevPostRollHead) * partialTicks;

        boolean needsOverride = effectiveHoldWeight > 0.0f || isRolling || Math.abs(interpPostRoll) > 0.05f || Math.abs(interpPostRollHead) > 0.05f;

        if (needsOverride) {
            storedRenderYawOffsetMap.put(player, player.renderYawOffset);
            storedPrevRenderYawOffsetMap.put(player, player.prevRenderYawOffset);
            storedRotationYawHeadMap.put(player, player.rotationYawHead);
            storedPrevRotationYawHeadMap.put(player, player.prevRotationYawHead);

            if (isRolling) {
                // During roll: replace with dashWorldYaw direction
                // Here we actually DO want to snap the body and head strictly to the roll direction
                player.renderYawOffset = newBodyYaw;
                player.prevRenderYawOffset = newBodyYaw;
                player.rotationYawHead = headYaw;
                player.prevRotationYawHead = headYaw;
            } else {
                // Post-roll / weapon hold: preserve Minecraft's frame-to-frame delta to prevent twitching in other animations (like arms)
                // We compute how much we want to shift the body and head, then apply that shift to BOTH prev and current.
                float bodyShift = (newBodyYaw + interpPostRoll) - bodyYaw;
                player.renderYawOffset += bodyShift;
                player.prevRenderYawOffset += bodyShift;
                
                // Head only needs shifting for the post-roll smooth return. Weapon hold doesn't affect head rotation.
                float headShift = interpPostRollHead;
                player.rotationYawHead += headShift;
                player.prevRotationYawHead += headShift;
            }
            didOverrideYawMap.put(player, true);
        }

        if (isRolling) {
            float renderYaw = 180.0f - newBodyYaw;

            // Make the player physically lower to the ground during the roll (like a ball)
            float drop = (float) Math.sin(progress * Math.PI) * 0.8f;

            // Drop down towards ground
            // Handled directly in MixinRenderPlayer now using global offsets!

        }
    }

    @SubscribeEvent
    public void onRenderPlayerPost(net.minecraftforge.client.event.RenderPlayerEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();

        if (didOverrideYawMap.getOrDefault(player, false)) {
            player.renderYawOffset = storedRenderYawOffsetMap.getOrDefault(player, player.renderYawOffset);
            player.prevRenderYawOffset = storedPrevRenderYawOffsetMap.getOrDefault(player, player.prevRenderYawOffset);
            player.rotationYawHead = storedRotationYawHeadMap.getOrDefault(player, player.rotationYawHead);
            player.prevRotationYawHead = storedPrevRotationYawHeadMap.getOrDefault(player, player.prevRotationYawHead);
            didOverrideYawMap.put(player, false);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player == null || !event.player.world.isRemote)
            return;

        net.minecraft.entity.player.EntityPlayer player = event.player;
        efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(player);

        // --- REMOTE PLAYER LOGIC ---
        if (player != net.minecraft.client.Minecraft.getMinecraft().player) {
            NetworkAnimState state = networkAnimStates.get(player);
            
            Integer remaining = dashTicksRemaining.getOrDefault(player, -20);
            if (remaining > -20) {
                dashTicksRemaining.put(player, remaining - 1);
            }
            boolean isRolling = remaining > 2;

            boolean isAiming = false;
            efw.animation.WeaponTypeHelper.WeaponType weaponType = efw.animation.WeaponTypeHelper.WeaponType.NONE;
            boolean isFrozen = false;
            if (state != null) {
                if (state.baseAnim != null && state.baseAnim.endsWith("_FROZEN")) {
                    isFrozen = true;
                    state.baseAnim = state.baseAnim.substring(0, state.baseAnim.length() - 7);
                }
                
                if (state.baseAnim != null && !state.baseAnim.isEmpty()) {
                    if (!state.baseAnim.equals(ap.getCurrentAnimationName())) {
                        boolean wasWeapon = ap.getCurrentAnimationName() != null && 
                            (ap.getCurrentAnimationName().contains("pistol_") || ap.getCurrentAnimationName().contains("rifle_"));
                        boolean wasLying = ap.getCurrentAnimationName() != null && ap.getCurrentAnimationName().contains("lie");
                        boolean isLyingAnim = state.baseAnim.contains("lie");

                        if (wasLying != isLyingAnim) {
                            ap.snap();
                            ap.snapAction();
                        }
                    }

                    AnimationClip clip = AnimationRegistry.getClip(state.baseAnim);
                    if (clip != null) {
                        if (state.baseAnim.equals("roll")) {
                            ap.play(clip, 2.16f);
                        } else {
                            ap.play(clip);
                        }
                    }
                    if (state.baseAnim.equals("roll")) {
                        Integer r = dashTicksRemaining.get(player);
                        if (r == null || r <= 0) {
                            dashTicksRemaining.put(player, 7);
                        }
                    }
                    if (state.baseAnim.startsWith("rifle_")) weaponType = efw.animation.WeaponTypeHelper.WeaponType.RIFLE;
                    if (state.baseAnim.startsWith("pistol_")) weaponType = efw.animation.WeaponTypeHelper.WeaponType.PISTOL;
                } else {
                    ap.stop();
                }

                if (state.actionAnim != null && !state.actionAnim.isEmpty()) {
                    float remoteActionSpeed = state.actionSpeed;
                    if (state.actionAnim.endsWith("_lie") || state.actionAnim.endsWith("_lie_move") || state.actionAnim.equals("lie") || state.actionAnim.equals("lie_move")) {
                        remoteActionSpeed *= 3.0f;
                    }
                    if (!state.actionAnim.equals(ap.getCurrentActionName()) || !ap.isActionPlaying()) {
                        AnimationClip actionClip = AnimationRegistry.getClip(state.actionAnim);
                        if (actionClip != null) ap.setAction(actionClip, 0f, remoteActionSpeed);
                    } else {
                        ap.resumeActionLoop();
                        ap.setActionSpeed(remoteActionSpeed);
                    }
                    if (state.actionAnim.contains("aim")) isAiming = true;
                    if (state.actionAnim.startsWith("rifle_")) weaponType = efw.animation.WeaponTypeHelper.WeaponType.RIFLE;
                    if (state.actionAnim.startsWith("pistol_")) weaponType = efw.animation.WeaponTypeHelper.WeaponType.PISTOL;
                } else {
                    if ("roll".equals(ap.getCurrentActionName()) && !ap.isActionFadingOut()) {
                        ap.stopAction(12);
                    } else if (!"roll".equals(ap.getCurrentActionName())) {
                        ap.cancelAction();
                    }
                }
            } else {
                ap.stop();
                if ("roll".equals(ap.getCurrentActionName()) && !ap.isActionFadingOut()) {
                    ap.stopAction(12);
                } else if (!"roll".equals(ap.getCurrentActionName())) {
                    ap.cancelAction();
                }
            }

            if (!isRolling && "roll".equals(ap.getCurrentActionName()) && !ap.isActionFadingOut()) {
                ap.stopAction(12);
            }

            // Post-roll smooth body turn decay for remote player
            boolean wasRolling = wasRollingMap.getOrDefault(player, false);
            if (!isRolling) {
                prevPostRollYawOffsetMap.put(player, postRollYawOffsetMap.getOrDefault(player, 0f));
                float cur = postRollYawOffsetMap.getOrDefault(player, 0f);
                postRollYawOffsetMap.put(player, cur * 0.65f);
                prevPostRollHeadYawOffsetMap.put(player, postRollHeadYawOffsetMap.getOrDefault(player, 0f));
                float curHead = postRollHeadYawOffsetMap.getOrDefault(player, 0f);
                postRollHeadYawOffsetMap.put(player, curHead * 0.65f);
            }
            wasRollingMap.put(player, isRolling);

            // Sync weaponHoldWeight
            prevWeaponHoldWeightMap.put(player, weaponHoldWeightMap.getOrDefault(player, 0.0f));
            if (weaponType != efw.animation.WeaponTypeHelper.WeaponType.NONE) {
                weaponHoldWeightMap.put(player, Math.min(1.0f, weaponHoldWeightMap.getOrDefault(player, 0.0f) + 0.1f));
            } else {
                weaponHoldWeightMap.put(player, Math.max(0.0f, weaponHoldWeightMap.getOrDefault(player, 0.0f) - 1.0f)); // Instant
            }

            // Sync speed
            float baseSpeed = getAnimationSpeed(ap.getCurrentAnimationName());
            boolean isLying = state != null && state.baseAnim != null && state.baseAnim.contains("lie");
            boolean isLyingAction = state != null && state.baseAnim != null && (state.baseAnim.contains("reload") || state.baseAnim.contains("fire"));
            double moveDx = player.posX - player.prevPosX;
            double moveDz = player.posZ - player.prevPosZ;
            boolean isMoving = (moveDx * moveDx + moveDz * moveDz) > 0.0015;
            
            if (isLying) {
                if (isLyingAction) {
                    baseSpeed = 1.0f;
                } else {
                    if (weaponType == efw.animation.WeaponTypeHelper.WeaponType.NONE) {
                        baseSpeed *= 4.5f; // x3
                    } else {
                        baseSpeed *= 1.5f; // x3
                    }
                    
                    // Freeze crawling if the local player explicitly told us they are frozen
                    if (isFrozen) {
                        baseSpeed = 0f;
                        if (ap.getCurrentActionName() != null) {
                            String actName = ap.getCurrentActionName();
                            if (actName.endsWith("_lie") || actName.endsWith("_lie_move") || actName.equals("lie") || actName.equals("lie_move")) {
                                ap.setActionSpeed(0f);
                            }
                        }
                    } else if (!isMoving) {
                        baseSpeed = 0f; // Fallback for general movement
                    }
                }
            }
            
            boolean isWalkRunAnim = state != null && state.baseAnim != null && (state.baseAnim.startsWith("walking") || "running".equals(state.baseAnim));
            boolean isWeaponLowerWalkRun = state != null && state.baseAnim != null && state.baseAnim.endsWith("_lower") &&
                    (state.baseAnim.contains("walk") || state.baseAnim.contains("run") || state.baseAnim.contains("crouch_walk"));
            
            if (isWalkRunAnim || isWeaponLowerWalkRun) {
                double dx = player.posX - player.prevPosX;
                double dz = player.posZ - player.prevPosZ;
                double actualSpeed = Math.sqrt(dx * dx + dz * dz);
                double expectedSpeed;
                if (state.baseAnim.equals("running") || (state.baseAnim.contains("run") && state.baseAnim.endsWith("_lower"))) {
                    expectedSpeed = 0.28;
                } else if (state.baseAnim.contains("sneak") || state.baseAnim.contains("crouch")) {
                    expectedSpeed = 0.065;
                } else {
                    expectedSpeed = 0.215;
                }

                if (actualSpeed > 0.01) {
                    float speedRatio = (float) (actualSpeed / expectedSpeed);
                    speedRatio = Math.max(0.2f, Math.min(speedRatio, 3.0f));
                    baseSpeed *= speedRatio;
                }
            }
            
            float airMult = !player.onGround ? 0.33f : 1.0f;
            if (player.isOnLadder()) airMult = 0.33f;
            if (state != null && "roll".equals(state.baseAnim)) {
                airMult = 1.0f;
            }
            
            ap.isHoldingWeapon = weaponHoldWeightMap.getOrDefault(player, 0.0f) > 0.01f;
            ap.tick(baseSpeed * airMult);
            return;
        }
        // ---------------------------

        boolean isSprinting = player.isSprinting();

        net.minecraft.item.ItemStack currentStack = player.getHeldItemMainhand();
        net.minecraft.item.Item currentItem = currentStack.isEmpty() ? null : currentStack.getItem();
        if (currentItem != lastHeldItem) {
            if (efw.animation.WeaponTypeHelper.getWeaponType(currentStack) == efw.animation.WeaponTypeHelper.WeaponType.NONE) {
                ap.stopAction(10); // Плавный переход вместо мгновенного сброса
            }
            lastHeldItem = currentItem;
            wasReloadingAnimPlaying = false;
            postReloadTimer = 0;
            ignoreReloadState = false;
            isTacticalReloadMap.remove(player);
        }

        if (lieFireTicks > 0)
            lieFireTicks--;

        boolean currentlyReloadingAnim = ap.isActionPlaying() && ap.getCurrentActionName() != null
                && ap.getCurrentActionName().contains("reload");

        if (wasReloadingAnimPlaying && !currentlyReloadingAnim) {
            postReloadTimer = 10; // 0.5s timer
        }
        wasReloadingAnimPlaying = currentlyReloadingAnim;

        if (postReloadTimer > 0) {
            postReloadTimer--;
            if (postReloadTimer == 0) {
                ignoreReloadState = true;
            }
        }

        boolean isCurrentlyHitting = net.minecraft.client.Minecraft.getMinecraft().playerController != null
                && net.minecraft.client.Minecraft.getMinecraft().playerController.getIsHittingBlock();
        if (isCurrentlyHitting) {
            recentlyHitBlockTicks = 15; // Extend grace so block-break swing never triggers sword
        } else if (recentlyHitBlockTicks > 0) {
            recentlyHitBlockTicks--;
        }

        // Track how recently a block was being mined (to block false sword anim on
        // block-break)
        if (isCurrentlyHitting) {
            recentlyMinedTicks = 8;
        } else if (recentlyMinedTicks > 0) {
            recentlyMinedTicks--;
        }

        boolean isMoving = (player.moveForward != 0 || player.moveStrafing != 0);
        boolean isSneaking = player.isSneaking();
        boolean isCrawlingOrSwimming = false; // Убираем старую переменную, так как теперь у нас свои анимации для этого
        boolean isMovingBackwards = player.moveForward < 0;
        boolean inAir = !player.onGround;

        // --- MWC WEAPON CHECK FOR HOLD WEIGHT ---
        boolean isHoldingWeapon = false;
        efw.animation.WeaponTypeHelper.WeaponType weaponType = efw.animation.WeaponTypeHelper.WeaponType.NONE;
        try {
            com.paneedah.weaponlib.PlayerWeaponInstance weaponInstance = (com.paneedah.weaponlib.PlayerWeaponInstance)
                com.paneedah.weaponlib.ClientModContext.getContext()
                    .getPlayerItemInstanceRegistry()
                    .getMainHandItemInstance(player, com.paneedah.weaponlib.PlayerWeaponInstance.class);
            if (weaponInstance != null) {
                isHoldingWeapon = true;
            }
        } catch (Throwable t) {
        }

        // Fallback for non-MWC or if reflection failed
        if (!isHoldingWeapon) {
            weaponType = efw.animation.WeaponTypeHelper.getWeaponType(currentStack);
            if (weaponType != efw.animation.WeaponTypeHelper.WeaponType.NONE) {
                isHoldingWeapon = true;
            }
        }

        // Плавное изменение интерполяции "прицеливания" для поворота корпуса
        prevWeaponHoldWeightMap.put(player, weaponHoldWeightMap.getOrDefault(player, 0.0f));
        if (isHoldingWeapon) {
            weaponHoldWeightMap.put(player, Math.min(1.0f, weaponHoldWeightMap.getOrDefault(player, 0.0f) + 0.1f));
        } else {
            weaponHoldWeightMap.put(player, Math.max(0.0f, weaponHoldWeightMap.getOrDefault(player, 0.0f) - 0.2f)); // Un-equip speed increased for faster response
        }

        boolean isLocal = (player == net.minecraft.client.Minecraft.getMinecraft().player);
        boolean isRightClickHeld = isLocal && net.minecraft.client.Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown();
        boolean isUsingItemOrRightClick = player.isHandActive() || player.getItemInUseCount() > 0 || isRightClickHeld;

        if (isUsingItemOrRightClick) {
            String curAct = ap.getCurrentActionName();
            if (curAct != null && (curAct.startsWith("sword_") || curAct.startsWith("fist_") 
                    || curAct.equals("pickaxe") || curAct.equals("axe") 
                    || curAct.equals("shovel") || curAct.equals("hoe"))) {
                ap.stopAction();
            }
        }

        // --- УДАР РУКОЙ (кулак, мечи и т.д.) ---
        // Защита: не запускаем анимацию атаки при ПКМ (использование предметов, аптечки, установка блоков)
        if (player.isSwingInProgress && !isUsingItemOrRightClick && player.swingingHand == net.minecraft.util.EnumHand.MAIN_HAND) {
            ticksSinceLastSwing = 0;
            // Запускаем анимацию только в момент НАЧАЛА удара
            if (player.swingProgressInt == 1) {
                // Логика выбора анимации
                String actionAnim = null;
                net.minecraft.item.ItemStack heldItem = player.getHeldItemMainhand();
                boolean isMiningBlock = isCurrentlyHitting || recentlyHitBlockTicks > 0;

                if (!heldItem.isEmpty()) {
                    net.minecraft.item.Item item = heldItem.getItem();
                    if (item instanceof net.minecraft.item.ItemBlock) {
                        actionAnim = null; // Ванильная анимация для блоков
                    } else if (isMiningBlock) {
                        // Копаем блоки инструментом - используем кастомные анимации копания
                        String itemName = item.getRegistryName() != null
                                ? item.getRegistryName().getPath().toLowerCase()
                                : "";
                        if (itemName.contains("pickaxe") || itemName.contains("pick_axe"))
                            actionAnim = "pickaxe";
                        else if (itemName.contains("axe") || itemName.contains("hatchet") || itemName.contains("paxel")
                                || itemName.contains("tomahawk"))
                            actionAnim = "axe";
                        else if (itemName.contains("shovel") || itemName.contains("spade"))
                            actionAnim = "shovel";
                        else if (itemName.contains("hoe"))
                            actionAnim = "hoe";
                        else if (item instanceof net.minecraft.item.ItemPickaxe)
                            actionAnim = "pickaxe";
                        else if (item instanceof net.minecraft.item.ItemAxe)
                            actionAnim = "axe";
                        else if (item instanceof net.minecraft.item.ItemSpade)
                            actionAnim = "shovel";
                        else if (item instanceof net.minecraft.item.ItemHoe)
                            actionAnim = "hoe";
                        else {
                            // Unknown tool hitting block — still protect from sword anim
                            // if recently mined (block-break false-trigger guard)
                            if (recentlyMinedTicks == 0) {
                                alternateSwordAnim = !alternateSwordAnim;
                                actionAnim = isSneaking
                                        ? (alternateSwordAnim ? "sword_attack_sneak2" : "sword_attack_sneak")
                                        : (alternateSwordAnim ? "sword_attack2" : "sword_attack");
                            }
                        }
                        if (actionAnim != null) {
                            lastMiningToolAnim = actionAnim; // Remember last tool anim
                        }
                    } else {
                        // Бьем в воздухе/сущность
                        // Guard: if we recently finished mining, this swing is the block-break
                        // final swing — skip sword anim for tool items
                        boolean isTool = item instanceof net.minecraft.item.ItemTool
                                || item instanceof net.minecraft.item.ItemPickaxe
                                || item instanceof net.minecraft.item.ItemAxe
                                || item instanceof net.minecraft.item.ItemSpade
                                || item instanceof net.minecraft.item.ItemHoe;
                        if (isTool && recentlyMinedTicks > 0) {
                            // Block just broke — this swing is a false trigger, skip it
                            actionAnim = null;
                        } else {
                            alternateSwordAnim = !alternateSwordAnim;
                            actionAnim = isSneaking
                                    ? (alternateSwordAnim ? "sword_attack_sneak2" : "sword_attack_sneak")
                                    : (alternateSwordAnim ? "sword_attack2" : "sword_attack");
                        }
                    }
                } else {
                    // Пустая рука
                    if (isMiningBlock) {
                        actionAnim = null; // Для копания оставляем ванильную анимацию
                    } else {
                        // Для ударов пустой рукой используем анимацию (которая сейчас sword)
                        alternateSwordAnim = !alternateSwordAnim;
                        actionAnim = isSneaking ? (alternateSwordAnim ? "sword_attack_sneak2" : "sword_attack_sneak")
                                : (alternateSwordAnim ? "sword_attack2" : "sword_attack");
                    }
                }
                if (actionAnim != null) {
                    boolean isMiningAnim = actionAnim.equals("pickaxe") || actionAnim.equals("axe")
                            || actionAnim.equals("shovel") || actionAnim.equals("hoe");

                    if (!ap.isActionPlaying() || !actionAnim.equals(ap.getCurrentActionName())) {
                        efw.animation.AnimationClip clip = efw.animation.AnimationRegistry.getClip(actionAnim);
                        if (clip != null) {
                            float speed;
                            if (isMiningAnim) {
                                // Fixed speed for all digging tool animations — never affected by cooldown
                                speed = 1.3f;
                            } else {
                                speed = 1.6f;
                            }
                            ap.setAction(clip, 0f, speed);
                        }
                    } else {
                        ap.resumeActionLoop();
                    }
                }
            }
        } else {
            ticksSinceLastSwing++;
            if (ticksSinceLastSwing > 3) {
                String currentAction = ap.getCurrentActionName();
                if (currentAction != null && !currentAction.equals("roll") 
                        && !currentAction.startsWith("pistol_") && !currentAction.startsWith("rifle_")
                        && !currentAction.contains("aim") && !currentAction.contains("reload")) {
                    ap.stopAction();
                }
            }
        }
        // Если игрок прекратил махать руками (например, отменил удар), мы не обрываем
        // анимацию жестко,
        // она сама доиграет до конца в AnimationPlayer.tickAction()
        // ----------------------------------------

        if (inAir && player.motionY < -0.1) {
            airTicks++;
        } else {
            airTicks = 0;
        }

        boolean isFalling = airTicks > 8; // 8 тиков = 0.4 секунды падения

        boolean isParagliding = false;
        if (player.getEntityData().hasKey("paragliding") && player.getEntityData().getBoolean("paragliding")) {
            isParagliding = true;
        } else {
            try {
                if (player.isPotionActive(
                        net.minecraft.potion.Potion.getPotionFromResourceLocation("paraglider:paragliding"))) {
                    isParagliding = true;
                }
            } catch (Exception e) {
            }
        }

        // Надежная эвристика для параплана: если мы падаем уже давно (>6 тиков),
        // но скорость падения очень низкая (медленнее чем -0.2), значит мы планируем!
        if (!isParagliding && airTicks > 6 && player.motionY > -0.2 && player.motionY < -0.01) {
            isParagliding = true;
        }

        String animName = null;
        float ladderSpeedMult = 1.0f;

        // Определяем приоритеты состояний
        if (player.isInWater()) {
            outOfWaterTicks = 0;
        } else {
            outOfWaterTicks++;
        }

        boolean wasInWaterAnimation = false;
        String currentAnim = ap.getCurrentAnimationName();
        if (currentAnim != null && (currentAnim.equals("swimming") || currentAnim.equals("up_in_water") ||
                currentAnim.equals("backwards_in_water") || currentAnim.equals("forward_in_water")
                || currentAnim.equals("idle_in_water"))) {
            wasInWaterAnimation = true;
        }

        boolean isRolling = false;
        try {
            if (com.voltyx.mwccf.dash.DashCapability.ROLL_CAP != null) {
                com.voltyx.mwccf.dash.DashCapability.IDashData cap = player
                        .getCapability(com.voltyx.mwccf.dash.DashCapability.ROLL_CAP, null);
                if (cap != null) {
                    int currentCooldown = cap.getCooldown();
                    int lastCooldown = lastCooldowns.getOrDefault(player, 0);

                    // If cooldown suddenly jumps up, it means a remote player just dashed (fallback for multiplayer)
                    if (player != net.minecraft.client.Minecraft.getMinecraft().player
                            && currentCooldown > lastCooldown
                            && (!"roll".equals(ap.getCurrentActionName()) || ap.isActionFadingOut())) {
                        triggerRoll(player, null);
                    }
                    lastCooldowns.put(player, currentCooldown);

                    Integer remaining = dashTicksRemaining.getOrDefault(player, -20);
                    if (remaining > -20) {
                        dashTicksRemaining.put(player, remaining - 1);
                    }
                    if (remaining > 2) {
                        isRolling = true;
                    }
                }
            }
        } catch (Throwable t) {
            System.out.println("Error checking roll capability:");
            t.printStackTrace();
        }

        // Post-roll smooth body turn: decay each tick
        boolean wasRolling = wasRollingMap.getOrDefault(player, false);
        if (!isRolling) {
            prevPostRollYawOffsetMap.put(player, postRollYawOffsetMap.getOrDefault(player, 0f));
            // Decay smoothly every tick (0.65 factor ≈ 250ms to reach ~5%)
            float cur = postRollYawOffsetMap.getOrDefault(player, 0f);
            postRollYawOffsetMap.put(player, cur * 0.65f);
            // Head yaw decays at same rate
            prevPostRollHeadYawOffsetMap.put(player, postRollHeadYawOffsetMap.getOrDefault(player, 0f));
            float curHead = postRollHeadYawOffsetMap.getOrDefault(player, 0f);
            postRollHeadYawOffsetMap.put(player, curHead * 0.65f);
        }
        wasRollingMap.put(player, isRolling);

        if (player.isPlayerSleeping()) {
            animName = "sleeping";
        } else if (player.isElytraFlying() || isParagliding) {
            animName = player.isElytraFlying() ? "elytra" : "paraglider";
        } else if (player.isRiding()) {
            net.minecraft.entity.Entity mount = player.getRidingEntity();
            if (mount instanceof net.minecraft.entity.item.EntityMinecart) {
                animName = "minecart_idle";
            } else if (mount instanceof net.minecraft.entity.item.EntityBoat) {
                animName = "boat1";
            } else if (mount instanceof net.minecraft.entity.passive.AbstractHorse) {
                if (isMoving)
                    animName = "horse_running";
                else
                    animName = "horse_idle";
            } else {
                animName = "horse_idle"; // Фолбек для других маунтов
            }
        } else if (player.isInWater() || (outOfWaterTicks < 8 && wasInWaterAnimation)) {
            // Hard block check: look at the block at floor(posY) - 1
            // Also verify no water/liquid at Y+1 — if submerged, force swimming
            boolean wadingInShallowWater = false;
            if (player.isInWater()) {
                net.minecraft.util.math.BlockPos floorBelow = new net.minecraft.util.math.BlockPos(
                        player.posX,
                        Math.floor(player.posY) - 1,
                        player.posZ);
                net.minecraft.block.material.Material matBelow = player.world.getBlockState(floorBelow).getMaterial();
                boolean solidFloor = matBelow.isSolid() && matBelow != net.minecraft.block.material.Material.WATER;

                // Check Y+1: if there's liquid above, the player is submerged — no wading
                net.minecraft.util.math.BlockPos abovePos = new net.minecraft.util.math.BlockPos(
                        player.posX,
                        player.posY + 1,
                        player.posZ);
                boolean liquidAbove = player.world.getBlockState(abovePos).getMaterial().isLiquid();

                wadingInShallowWater = solidFloor && !liquidAbove;
            }

            if (wadingInShallowWater) {
                // Standing in shallow water: use normal movement animations
                if (isSneaking) {
                    animName = isMoving ? (isMovingBackwards ? "walking_sneak_backwards" : "walking_sneak")
                            : "idle_sneak";
                } else if (isSprinting && isMoving) {
                    animName = "running";
                } else if (isMoving) {
                    animName = isMovingBackwards ? "walking_backwards" : "walking";
                } else {
                    animName = "idle_standing";
                }
            } else {
                // Плавание
                if (isMoving && isSprinting) {
                    animName = "swimming";
                } else if (player.motionY > 0.15) {
                    animName = "up_in_water";
                } else if (isMovingBackwards) {
                    animName = "backwards_in_water";
                } else if (isMoving) {
                    animName = "forward_in_water";
                } else {
                    animName = "idle_in_water";
                }
            }
        } else if (player.isOnLadder() && !player.onGround) {
            double actualMotionY = player.posY - player.prevPosY;
            if (Math.abs(actualMotionY) > 0.01) {
                animName = "climbing";
                if (actualMotionY > 0) {
                    ladderSpeedMult = 2.0f;
                } else {
                    ladderSpeedMult = -2.0f;
                }
            } else {
                animName = "climbing"; // Ставим последний кадр (пауза)
                ladderSpeedMult = 0.0f;
            }
        } else if (player.capabilities.isFlying) {
            animName = "idle_creative_flying";
        } else if (player.height < 1.0F) { // Ползание (AquaAcrobatics)
            if (isMovingBackwards) {
                animName = "lie_move";
            } else if (isMoving) {
                animName = "lie_move";
            } else {
                animName = "lie";
            }
        } else if (isFalling) {
            animName = "falling";
        } else {
            // Обычные стойки и передвижение на земле
            net.minecraft.item.ItemStack activeItem = player.getActiveItemStack();
            boolean isUsingItem = player.isHandActive() && !activeItem.isEmpty();
            if (isUsingItem) {
                net.minecraft.item.Item item = activeItem.getItem();
                if (item instanceof net.minecraft.item.ItemBow) {
                    animName = isSneaking ? "bow_sneak" : "bow_idle";
                } else if (activeItem.getItemUseAction() == net.minecraft.item.EnumAction.BLOCK) {
                    if (player.getActiveHand() == net.minecraft.util.EnumHand.OFF_HAND) {
                        animName = isSneaking ? "shield_left_sneak" : "shield_left";
                    } else {
                        animName = isSneaking ? "shield_sneak" : "shield";
                    }
                }
            } else {
                if (ap.getCurrentActionName() != null && ap.getCurrentActionName().startsWith("eating")) {
                    ap.cancelAction();
                }
            }

            // Если анимация предмета не переопределила, ставим передвижение
            if (animName == null) {
                if (isSneaking) {
                    if (isMoving) {
                        animName = isMovingBackwards ? "walking_sneak_backwards" : "walking_sneak";
                    } else {
                        animName = "idle_sneak";
                    }
                } else {
                    if (isSprinting) {
                        animName = "running";
                    } else if (isMoving) {
                        animName = isMovingBackwards ? "walking_backwards" : "walking";
                    } else {
                        float yawDiff = net.minecraft.util.math.MathHelper.wrapDegrees(player.renderYawOffset - player.prevRenderYawOffset);
                        int turnCooldown = turnCooldowns.getOrDefault(player.getUniqueID(), 0);
                        if (yawDiff > 0.5f) {
                            animName = "turn_right";
                            turnCooldowns.put(player.getUniqueID(), 5);
                        } else if (yawDiff < -0.5f) {
                            animName = "turn_left";
                            turnCooldowns.put(player.getUniqueID(), 5);
                        } else {
                            if (turnCooldown > 0) {
                                turnCooldowns.put(player.getUniqueID(), turnCooldown - 1);
                                animName = ap.getCurrentAnimationName() != null && ap.getCurrentAnimationName().startsWith("turn_") ? ap.getCurrentAnimationName() : "idle_standing";
                            } else {
                                animName = "idle_standing";
                            }
                        }
                    }
                }
            }
        }

        // --- WEAPON ANIMATION OVERRIDE ---
        weaponType = efw.animation.WeaponTypeHelper.getWeaponType(currentStack);

        boolean isLying = "lie".equals(animName) || "lie_move".equals(animName);

        if (weaponType != efw.animation.WeaponTypeHelper.WeaponType.NONE && animName != null
                && !"roll".equals(animName)) {
            String prefix = (weaponType == efw.animation.WeaponTypeHelper.WeaponType.PISTOL) ? "pistol_" : "rifle_";
            String mapped = animName;
            String upperMapped = mapped;
            String lowerMapped = mapped;

            if ("running".equals(animName)) {
                upperMapped = "run";
                lowerMapped = "run";
            } else if ("walking_sneak".equals(animName) || "walking_sneak_backwards".equals(animName)) {
                upperMapped = "walk";
                lowerMapped = "crouch_walk";
            } else if ("idle_sneak".equals(animName)) {
                upperMapped = "hold";
                lowerMapped = "crouch";
            } else if ("walking".equals(animName) || "walking_backwards".equals(animName)) {
                upperMapped = "walk";
                lowerMapped = "walk";
            } else if ("idle_standing".equals(animName)) {
                upperMapped = "hold";
                lowerMapped = "hold";
            } else if ("turn_right".equals(animName) || "turn_left".equals(animName)) {
                upperMapped = "hold";
            }

            // Check if player is aiming or reloading (from MWC)
            boolean isAiming = false;
            boolean isReloading = false;

            try {
                com.paneedah.weaponlib.PlayerWeaponInstance weaponInstance = (com.paneedah.weaponlib.PlayerWeaponInstance)
                    com.paneedah.weaponlib.ClientModContext.getContext()
                        .getPlayerItemInstanceRegistry()
                        .getMainHandItemInstance(player, com.paneedah.weaponlib.PlayerWeaponInstance.class);
                if (weaponInstance != null) {
                    isAiming = weaponInstance.isAimed() || (player == Minecraft.getMinecraft().player && (com.teamderpy.shouldersurfing.client.ShoulderInstance.getInstance().isAiming() || Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()));
                    Object state = weaponInstance.getState();
                    if (state != null) {
                        String stateName = state.toString().toUpperCase();
                        boolean currentlyReloading = ap.isActionPlaying() && ap.getCurrentActionName() != null
                                && ap.getCurrentActionName().contains("reload");

                        boolean isReloadState = stateName.contains("RELOAD") || stateName.contains("LOAD") || stateName.contains("UNLOAD");

                        boolean isCompoundOrTactical = stateName.contains("TACTICAL")
                                || (stateName.contains("COMPOUND") && !stateName.contains("EMPTY"))
                                || (!weaponInstance.wasReloadedFromEmpty && isReloadState);

                        if (isReloadState && !currentlyReloading) {
                            isTacticalReloadMap.put(player, isCompoundOrTactical);
                        } else if (isCompoundOrTactical) {
                            isTacticalReloadMap.put(player, true);
                        }

                        boolean isTacticalReload = isTacticalReloadMap.getOrDefault(player, false);

                        // If compound or tactical reload, cut off at 50% of the animation and smoothly blend out
                        if (isTacticalReload && currentlyReloading && ap.getActionProgress() >= 0.5f) {
                            currentlyReloading = false;
                            isReloadState = false;
                        }

                        if (!isReloadState && postReloadTimer == 0) {
                            ignoreReloadState = false;
                        }

                        if ((isReloadState && !ignoreReloadState && postReloadTimer == 0)
                                || currentlyReloading) {
                            isReloading = true;
                        } else {
                            isTacticalReloadMap.put(player, false);
                        }

                        if (stateName.contains("SHOOT") || stateName.contains("FIR")) {
                            lieFireTicks = 20; // 1 second
                        }
                    }
                }
            } catch (Throwable t) {
            }

            // Fallback to vanilla
            if (!isAiming) {
                net.minecraft.item.ItemStack activeItem = player.getActiveItemStack();
                isAiming = player.isHandActive() && !activeItem.isEmpty()
                        && activeItem.getItem() == player.getHeldItemMainhand().getItem();
            }

            if (isLying) {
                if (isReloading) {
                    String actionCandidate = prefix + "lie_reload";
                    efw.animation.AnimationClip reloadClip = efw.animation.AnimationRegistry.getClip(actionCandidate);
                    if (reloadClip != null && !isRolling) {
                        if (!ap.isActionPlaying() || !actionCandidate.equals(ap.getCurrentActionName())) {
                            ap.setAction(reloadClip, 0f, 0.83f);
                        } else {
                            ap.resumeActionLoop();
                        }
                    }
                } else if (!"roll".equals(ap.getCurrentActionName())) {
                    ap.cancelAction();
                }

                if (isAiming && !isMoving) {
                    upperMapped = "lie_aim";
                } else {
                    upperMapped = mapped; // "lie" or "lie_move"
                }

                String baseCandidate = prefix + upperMapped; // e.g. rifle_lie_aim, rifle_lie_move, rifle_lie
                if (efw.animation.AnimationRegistry.getClip(baseCandidate) == null) {
                    baseCandidate = prefix + mapped;
                }

                if (efw.animation.AnimationRegistry.getClip(baseCandidate) != null) {
                    animName = baseCandidate;
                }
            } else {
                // NORMAL NON-LYING LOGIC (has _upper / _lower suffixes)
                if (isReloading) {
                    upperMapped = "reload";
                } else if (isAiming) {
                    upperMapped = "aim";
                }

                String actionCandidate = prefix + upperMapped + "_upper";
                String baseCandidate = prefix + lowerMapped + "_lower";

                efw.animation.AnimationClip weaponClipUpper = efw.animation.AnimationRegistry.getClip(actionCandidate);
                if (weaponClipUpper != null) {
                    if (!isRolling) {
                        if (!ap.isActionPlaying() || !actionCandidate.equals(ap.getCurrentActionName())) {
                            float speed = actionCandidate.contains("reload") ? 0.83f : 1.0f;
                            ap.setAction(weaponClipUpper, 0f, speed);
                        } else {
                            ap.resumeActionLoop();
                        }
                    }
                } else {
                    if (!"roll".equals(ap.getCurrentActionName())) ap.cancelAction();
                }

                if (efw.animation.AnimationRegistry.getClip(baseCandidate) != null) {
                    animName = baseCandidate;
                }
            }
        } else {
            // Cancel weapon actions immediately when weapon is unequipped
            if (ap.getCurrentActionName() != null &&
                    (ap.getCurrentActionName().startsWith("pistol_")
                            || ap.getCurrentActionName().startsWith("rifle_"))) {
                ap.cancelAction();
            }

            if (isLying && animName != null) {
                // The user wants pistol_lie_move for empty hands always, frozen when not moving
                String fallbackCandidate = "pistol_lie_move";
                if (efw.animation.AnimationRegistry.getClip(fallbackCandidate) != null) {
                    animName = fallbackCandidate;
                }
            }
        }
        // ---------------------------------
        if (isRolling) {
            efw.animation.AnimationClip rollClip = efw.animation.AnimationRegistry.getClip("roll");
            if (rollClip != null) {
                if (!"roll".equals(ap.getCurrentActionName()) || ap.isActionFadingOut()) {
                    ap.setAction(rollClip, 0f, 1.0f);
                }
            }
        } else if ("roll".equals(ap.getCurrentActionName()) && !ap.isActionFadingOut()) {
            ap.stopAction(12);
        }

        if (animName != null && !animName.equals(ap.getCurrentAnimationName())) {
            boolean wasLying = ap.getCurrentAnimationName() != null && ap.getCurrentAnimationName().contains("lie");
            boolean isLyingAnim = animName.contains("lie");
            
            if (wasLying != isLyingAnim) {
                ap.snap();
                ap.snapAction();
            }

            efw.animation.AnimationClip clip = efw.animation.AnimationRegistry.getClip(animName);
            if (clip != null) {
                ap.play(clip);
            } else {
                ap.stop();
            }
        } else if (animName == null) {
            ap.stop();
        }

        // Множитель скорости (воздушное замедление + базовая скорость конкретной
        // анимации)
        float airMult = inAir ? 0.33f : 1.0f; // Замедляем в прыжке

        if (player.isOnLadder()) {
            airMult = ladderSpeedMult;
        }

        if (isRolling) {
            airMult = 1.0f; // Roll speed shouldn't be affected by being slightly in the air
            // Action is managed above, don't cancel it here
        }

        boolean isLyingAction = animName != null && (animName.contains("reload") || animName.contains("fire"));
        float baseSpeed = getAnimationSpeed(animName);

        // Crawl animation speed adjustments
        if (isLying) {
            if (isLyingAction) {
                baseSpeed = 1.0f;
            } else {
                if (weaponType == efw.animation.WeaponTypeHelper.WeaponType.NONE) {
                    baseSpeed *= 1.5f; // +50% faster without weapon
                } else {
                    baseSpeed *= 0.5f; // -50% slower with weapon
                }

                // Freeze crawling if not moving (both with and without weapons)
                if (!isMoving) {
                    baseSpeed = 0f;
                }
            }
        }

        // Динамическая скорость для ходьбы/бега
        // Applies to both normal walk/run animations AND weapon _lower animations
        // (legs)
        boolean isWalkRunAnim = animName != null && (animName.startsWith("walking") || "running".equals(animName));
        boolean isWeaponLowerWalkRun = animName != null && animName.endsWith("_lower") &&
                (animName.contains("walk") || animName.contains("run") || animName.contains("crouch_walk"));

        if ((isWalkRunAnim || isWeaponLowerWalkRun) && !inAir && isMoving) {
            double dx = player.posX - player.prevPosX;
            double dz = player.posZ - player.prevPosZ;
            double actualSpeed = Math.sqrt(dx * dx + dz * dz);

            double expectedSpeed;
            if ("running".equals(animName) || (animName != null && animName.contains("run") && animName.endsWith("_lower"))) {
                expectedSpeed = 0.28;   // Скорость спринта на земле
            } else if (animName != null && (animName.contains("sneak") || animName.contains("crouch"))) {
                expectedSpeed = 0.065;  // Скорость крадущегося
            } else {
                expectedSpeed = 0.215;  // Нормальная ходьба
            }

            if (actualSpeed > 0.01) {
                float speedRatio = (float) (actualSpeed / expectedSpeed);
                speedRatio = Math.max(0.2f, Math.min(speedRatio, 3.0f)); // Ограничиваем от крайностей
                baseSpeed *= speedRatio;
            }
        }

        float finalSpeed = baseSpeed * airMult;

        // Freeze crawling action layer if not moving
        if (isLying && ap.getCurrentActionName() != null) {
            String actName = ap.getCurrentActionName();
            if (actName.endsWith("_lie") || actName.endsWith("_lie_move") || actName.equals("lie") || actName.equals("lie_move")) {
                if (!isMoving) {
                    ap.setActionSpeed(0f);
                } else if (ap.getActionSpeed() == 0f) {
                    ap.setActionSpeed(1.0f);
                }
            }
        }

        ap.isHoldingWeapon = weaponHoldWeightMap.getOrDefault(player, 0.0f) > 0.01f;
        ap.tick(finalSpeed);

        // --- NETWORK SYNC FOR LOCAL PLAYER ---
        String currentBaseAnim = ap.getCurrentAnimationName();
        if (currentBaseAnim != null && isLying && !isMoving && !isLyingAction) {
            currentBaseAnim += "_FROZEN";
        }
        
        String currentActionAnim = ap.isActionPlaying() ? ap.getCurrentActionName() : null;
        float currentActionSpeed = ap.getActionSpeed();
        float currentDashYaw = isRolling ? dashWorldYaws.getOrDefault(player, player.rotationYaw) : Float.NaN;

        boolean changed = false;
        if (currentBaseAnim == null && lastSentBaseAnim != null) changed = true;
        if (currentBaseAnim != null && !currentBaseAnim.equals(lastSentBaseAnim)) changed = true;
        
        if (currentActionAnim == null && lastSentActionAnim != null) changed = true;
        if (currentActionAnim != null && !currentActionAnim.equals(lastSentActionAnim)) changed = true;

        if (currentActionAnim != null && Math.abs(currentActionSpeed - lastSentActionSpeed) > 0.01f) changed = true;
        if (isRolling && (Float.isNaN(lastSentDashYaw) || Math.abs(currentDashYaw - lastSentDashYaw) > 0.1f)) changed = true;
        if (!isRolling && !Float.isNaN(lastSentDashYaw)) changed = true;

        if (changed) {
            lastSentBaseAnim = currentBaseAnim;
            lastSentActionAnim = currentActionAnim;
            lastSentActionSpeed = currentActionSpeed;
            lastSentDashYaw = currentDashYaw;
            com.voltyx.mwccf.MwccfMod.PACKET_HANDLER.sendToServer(new com.voltyx.mwccf.network.PacketSyncAnimations(
                player.getEntityId(), currentBaseAnim, currentActionAnim, currentActionSpeed, currentDashYaw
            ));
        }
    }

    private static String lastSentBaseAnim = null;
    private static String lastSentActionAnim = null;
    private static float lastSentActionSpeed = -1f;
    private static float lastSentDashYaw = Float.NaN;
}
