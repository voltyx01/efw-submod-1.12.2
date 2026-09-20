package com.voltyx.mwccf.geo;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;

public class KawaiiEarsAnimationHandler {

    // Target rotations from author's model.animation.json (in degrees, relative deltas to base pose)
    // Sneak
    private static final float SNEAK_EAR2_X = -25.2311f;
    private static final float SNEAK_EAR2_Y = -33.852f;
    private static final float SNEAK_EAR2_Z = 7.7098f;

    private static final float SNEAK_EAR3_X = -31.9642f;
    private static final float SNEAK_EAR3_Y = 35.5497f;
    private static final float SNEAK_EAR3_Z = -12.2912f;

    // Sneakmove
    private static final float SNEAKMOVE_EAR2_X = -55.481f;
    private static final float SNEAKMOVE_EAR2_Y = -24.8891f;
    private static final float SNEAKMOVE_EAR2_Z = 27.2645f;

    private static final float SNEAKMOVE_EAR3_X = -57.0895f;
    private static final float SNEAKMOVE_EAR3_Y = 27.6044f;
    private static final float SNEAKMOVE_EAR3_Z = -30.0239f;

    private static final Map<EntityPlayer, PlayerEarState> STATES = new WeakHashMap<>();
    private static final Random RNG = new Random();

    public static class PlayerEarState {
        // Current and previous tick interpolated rotation deltas (in degrees)
        public float prevEar2X, curEar2X;
        public float prevEar2Y, curEar2Y;
        public float prevEar2Z, curEar2Z;

        public float prevEar3X, curEar3X;
        public float prevEar3Y, curEar3Y;
        public float prevEar3Z, curEar3Z;

        // Inertia tracking
        public float lastHeadYaw;
        public float lastHeadPitch;
        public float yawVelocity;
        public float pitchVelocity;
        public float prevYawInertia, curYawInertia;
        public float prevPitchInertia, curPitchInertia;

        // Smooth state weights
        public float sneakWeight = 0.0f;
        public float sneakMoveWeight = 0.0f;
        public float sprintWeight = 0.0f;
        public float aimWeight = 0.0f;
        public float proneWeight = 0.0f;
        public float swimWeight = 0.0f;
        public float inAirWeight = 0.0f;

        // Landing impact spring
        public boolean wasInAir = false;
        public float landingSpring = 0.0f;

        // Random idle twitch
        public int twitchCooldown = 80 + RNG.nextInt(120);
        public int twitchDuration = 0;
        public int twitchElapsed = 0;
        public int twitchType = 0; // 0 = right flick, 1 = left flick, 2 = both flick

        // Gunshot flinch
        public float shotFlinch = 0.0f;
        public long lastHandledShotTime = 0;

        // Reload state
        public float reloadWeight = 0.0f;

        // Roll state
        public float rollWeight = 0.0f;

        // Linear movement inertia tracking
        public float moveInertiaForward = 0.0f;
        public float moveInertiaStrafe = 0.0f;

        // Idle return flick cooldown and settling
        public int idleChanceCooldown = 0; // cooldown in ticks (60 ticks = 3.0 seconds)
        public boolean wasInActiveState = false; // tracks if player was in an active state before entering idle
        public int idleSettleDelay = 0; // countdown ticks after returning to idle before triggering flick

        public PlayerEarState() {
            resetToBase();
        }

        public void resetToBase() {
            prevEar2X = curEar2X = 0.0f;
            prevEar2Y = curEar2Y = 0.0f;
            prevEar2Z = curEar2Z = 0.0f;

            prevEar3X = curEar3X = 0.0f;
            prevEar3Y = curEar3Y = 0.0f;
            prevEar3Z = curEar3Z = 0.0f;
        }
    }

    public static PlayerEarState getState(EntityPlayer player) {
        return STATES.computeIfAbsent(player, p -> new PlayerEarState());
    }

    /**
     * Called on client tick for each player equipped with Kawaii Ears.
     */
    public static void update(EntityPlayer player) {
        PlayerEarState state = getState(player);

        // Store previous rotations for smooth sub-tick rendering
        state.prevEar2X = state.curEar2X;
        state.prevEar2Y = state.curEar2Y;
        state.prevEar2Z = state.curEar2Z;

        state.prevEar3X = state.curEar3X;
        state.prevEar3Y = state.curEar3Y;
        state.prevEar3Z = state.curEar3Z;

        state.prevYawInertia = state.curYawInertia;
        state.prevPitchInertia = state.curPitchInertia;

        if (state.idleChanceCooldown > 0) {
            state.idleChanceCooldown--;
        }

        // 1. Inertia from head / body rotation
        float headYaw = player.rotationYawHead;
        float headPitch = player.rotationPitch;
        float yawDelta = MathHelper.wrapDegrees(headYaw - state.lastHeadYaw);
        float pitchDelta = headPitch - state.lastHeadPitch;
        state.lastHeadYaw = headYaw;
        state.lastHeadPitch = headPitch;

        // Deadzone micro-movements to avoid jitter when player is breathing/idle
        if (Math.abs(yawDelta) < 0.18f) yawDelta = 0.0f;
        if (Math.abs(pitchDelta) < 0.18f) pitchDelta = 0.0f;

        // Clamp extreme single-tick jumps (e.g. teleporting, respawning)
        yawDelta = MathHelper.clamp(yawDelta, -30.0f, 30.0f);
        pitchDelta = MathHelper.clamp(pitchDelta, -25.0f, 25.0f);

        // Smooth spring lag + recovery (faster and punchier)
        state.yawVelocity += (yawDelta * 0.85f - state.yawVelocity) * 0.40f;
        state.pitchVelocity += (pitchDelta * 0.75f - state.pitchVelocity) * 0.40f;

        state.curYawInertia += ((-state.yawVelocity) - state.curYawInertia) * 0.45f;
        state.curPitchInertia += ((-state.pitchVelocity) - state.curPitchInertia) * 0.45f;

        // Decay velocity
        state.yawVelocity *= 0.70f;
        state.pitchVelocity *= 0.70f;

        // 2. Movement inertia (linear movement lag across all movement directions)
        double dx = player.posX - player.prevPosX;
        double dz = player.posZ - player.prevPosZ;
        float yawRad = (float) Math.toRadians(player.rotationYaw);
        float cosY = MathHelper.cos(yawRad);
        float sinY = MathHelper.sin(yawRad);

        // Transform world velocity into player local space (forward and strafe)
        float forwardVel = (float) (-dx * sinY + dz * cosY);
        float strafeVel = (float) (dx * cosY + dz * sinY);

        // Smoothly lag ears opposite to movement direction with punchier inertia response
        state.moveInertiaForward += (-forwardVel * 36.0f - state.moveInertiaForward) * 0.40f;
        state.moveInertiaStrafe += (-strafeVel * 28.0f - state.moveInertiaStrafe) * 0.40f;

        // 3. State detection
        boolean isMoving = Math.abs(dx) > 0.003 || Math.abs(dz) > 0.003;
        boolean isSneaking = player.isSneaking() || (player == Minecraft.getMinecraft().player && Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown());
        boolean isSprinting = player.isSprinting() && isMoving;
        boolean isInWater = player.isInWater();
        boolean isFlying = player.capabilities.isFlying;
        boolean isProne = player.height < 1.0F && !isInWater;

        // Detect roll / dodge
        boolean isRolling = false;
        try {
            if (efw.AnimationTickHandler.isPlayerRolling(player)) {
                isRolling = true;
            } else {
                efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(player);
                if (ap != null && ap.isRollPlaying()) {
                    isRolling = true;
                }
            }
        } catch (Throwable ignored) {
        }

        // Detect weapon aiming, firing, and reloading
        boolean isAiming = false;
        boolean isReloading = false;
        try {
            com.paneedah.weaponlib.PlayerWeaponInstance weaponInstance = (com.paneedah.weaponlib.PlayerWeaponInstance)
                    com.paneedah.weaponlib.ClientModContext.getContext()
                            .getPlayerItemInstanceRegistry()
                            .getMainHandItemInstance(player, com.paneedah.weaponlib.PlayerWeaponInstance.class);
            if (weaponInstance != null) {
                isAiming = weaponInstance.isAimed() || (player == Minecraft.getMinecraft().player && (com.teamderpy.shouldersurfing.client.ShoulderInstance.getInstance().isAiming() || Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()));
                Object wState = weaponInstance.getState();
                if (wState != null) {
                    String stateName = wState.toString().toUpperCase();
                    if (stateName.contains("SHOOT") || stateName.contains("FIR")) {
                        state.shotFlinch = 1.0f;
                    }
                    if (stateName.contains("RELOAD") || stateName.contains("LOAD") || stateName.contains("UNLOAD")) {
                        isReloading = true;
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        // Additional check for shots from lastShotTimeByEntity (handles fast semi/full-auto shots)
        try {
            Long lastShot = com.paneedah.weaponlib.ClientEventHandler.lastShotTimeByEntity.get(player.getEntityId());
            if (lastShot != null && lastShot > state.lastHandledShotTime) {
                state.lastHandledShotTime = lastShot;
                state.shotFlinch = 1.0f;
            }
        } catch (Throwable ignored) {
        }

        if (!isAiming && player.isHandActive()) {
            net.minecraft.item.ItemStack activeStack = player.getActiveItemStack();
            if (!activeStack.isEmpty() && activeStack.getItem() instanceof net.minecraft.item.ItemBow) {
                isAiming = true;
            }
        }

        // Decay gunshot flinch (sharp kick and quick recovery)
        if (state.shotFlinch > 0.01f) {
            state.shotFlinch *= 0.60f;
        } else {
            state.shotFlinch = 0.0f;
        }

        // Jump & in-air physics
        boolean inAir = !player.onGround && !isInWater && !isFlying && !player.isOnLadder();
        if (inAir) {
            state.inAirWeight += (1.0f - state.inAirWeight) * 0.40f;
            state.wasInAir = true;
        } else {
            state.inAirWeight += (0.0f - state.inAirWeight) * 0.40f;
            if (state.wasInAir) {
                state.wasInAir = false;
                state.landingSpring = 24.0f; // Dip on landing in degrees
            }
        }
        state.landingSpring *= 0.65f;

        // Fast & crisp weight transitions (much faster response)
        state.sneakWeight += ((isSneaking ? 1.0f : 0.0f) - state.sneakWeight) * 0.45f;
        state.sneakMoveWeight += ((isSneaking && isMoving ? 1.0f : 0.0f) - state.sneakMoveWeight) * 0.45f;
        state.sprintWeight += ((isSprinting ? 1.0f : 0.0f) - state.sprintWeight) * 0.40f;
        state.aimWeight += ((isAiming ? 1.0f : 0.0f) - state.aimWeight) * 0.45f;
        state.reloadWeight += ((isReloading ? 1.0f : 0.0f) - state.reloadWeight) * 0.45f;
        state.rollWeight += ((isRolling ? 1.0f : 0.0f) - state.rollWeight) * 0.55f;
        state.proneWeight += ((isProne ? 1.0f : 0.0f) - state.proneWeight) * 0.35f;
        state.swimWeight += ((isInWater ? 1.0f : 0.0f) - state.swimWeight) * 0.35f;

        // Track if player is in an active state (moving, sprinting, aiming, reloading, rolling, in air, prone, swim, sneak, sneakmove)
        boolean isSneakingState = isSneaking;
        boolean isActiveState = isMoving || isSprinting || isAiming || isReloading || isRolling || inAir || isProne || isInWater || isSneakingState || (player.hurtTime > 0) || (state.shotFlinch > 0.1f);
        boolean isPhysicallyIdle = !isActiveState;

        // Ensure all other animation weights have fully settled back to 0 before starting idle twitch
        boolean areAnimationsFullyIdle = state.sneakWeight < 0.05f
                && state.sneakMoveWeight < 0.05f
                && state.sprintWeight < 0.05f
                && state.aimWeight < 0.05f
                && state.reloadWeight < 0.05f
                && state.rollWeight < 0.05f
                && state.proneWeight < 0.05f
                && state.swimWeight < 0.05f
                && state.inAirWeight < 0.05f
                && state.shotFlinch < 0.05f
                && Math.abs(state.moveInertiaForward) < 1.0f
                && Math.abs(state.moveInertiaStrafe) < 1.0f;

        if (isActiveState) {
            state.wasInActiveState = true;
            state.idleSettleDelay = 0;
            // Cancel any idle twitch immediately if an action starts
            state.twitchDuration = 0;
        } else if (isPhysicallyIdle) {
            // Player is resting. Wait until active animation weights have fully returned to baseline.
            if (state.wasInActiveState) {
                if (areAnimationsFullyIdle) {
                    // Settle delay (6 ticks = 0.3s resting in neutral idle before flick)
                    state.idleSettleDelay++;
                    if (state.idleSettleDelay >= 6) {
                        if (state.idleChanceCooldown <= 0) {
                            state.twitchDuration = 12; // Same smooth twitch mechanism
                            state.twitchElapsed = 0;
                            state.twitchType = RNG.nextBoolean() ? 0 : 1; // Randomly choose one ear: 0 = right, 1 = left
                            state.idleChanceCooldown = 60; // 3.0s cooldown
                            state.twitchCooldown = 90 + RNG.nextInt(150); // Postpone ambient twitch
                        }
                        state.wasInActiveState = false;
                        state.idleSettleDelay = 0;
                    }
                }
            } else {
                state.idleSettleDelay = 0;
            }
        }

        // 4. Natural idle twitch (occasional gentle ear flick when resting, or on settling into idle)
        float twitch2X = 0.0f, twitch2Z = 0.0f;
        float twitch3X = 0.0f, twitch3Z = 0.0f;

        if (isPhysicallyIdle) {
            if (state.twitchDuration == 0) {
                state.twitchCooldown--;
                if (state.twitchCooldown <= 0) {
                    state.twitchCooldown = 90 + RNG.nextInt(150); // 4.5 to 12 seconds
                    state.twitchDuration = 10 + RNG.nextInt(6);    // 10 to 16 ticks
                    state.twitchElapsed = 0;
                    state.twitchType = RNG.nextInt(3);
                }
            } else {
                state.twitchElapsed++;
                float progress = (float) state.twitchElapsed / state.twitchDuration;
                if (progress >= 1.0f) {
                    state.twitchDuration = 0;
                } else {
                    float envelope = (float) Math.sin(progress * Math.PI);
                    envelope *= envelope;
                    float wave = (float) Math.sin(progress * Math.PI * 3.0) * envelope * 8.0f;

                    if (state.twitchType == 0) {
                        twitch2Z -= wave; // Right ear flick
                    } else if (state.twitchType == 1) {
                        twitch3Z += wave; // Left ear flick
                    } else {
                        twitch2Z -= wave;
                        twitch3Z += wave;
                    }
                }
            }
        } else {
            state.twitchDuration = 0;
        }

        // 5. Walk & Sprint bounce
        float walkBounce = 0.0f;
        if (isMoving && !inAir && !isProne) {
            float speedMult = isSprinting ? 1.35f : 0.85f;
            walkBounce = (float) Math.sin(player.limbSwing * speedMult) * 3.5f * player.limbSwingAmount;
        }

        // 6. Compose Target Rotation Deltas for Ear2 (Right) and Ear3 (Left) in degrees
        float target2X = twitch2X;
        float target2Y = 0.0f;
        float target2Z = twitch2Z;

        float target3X = twitch3X;
        float target3Y = 0.0f;
        float target3Z = twitch3Z;

        // Apply Rotational Inertia (from mouse / head turns) - more noticeable
        target2Z += state.curYawInertia * 1.3f;
        target3Z += state.curYawInertia * 1.3f;
        target2Y += state.curYawInertia * 0.5f;
        target3Y += state.curYawInertia * 0.5f;

        target2X += state.curPitchInertia * 1.1f;
        target3X += state.curPitchInertia * 1.1f;

        // Apply Linear Movement Inertia (for any movement: forward, backward, strafe)
        target2X += state.moveInertiaForward;
        target3X += state.moveInertiaForward;
        target2Z += state.moveInertiaStrafe;
        target3Z += state.moveInertiaStrafe;

        // Apply Sneak Poses (blend from base -> sneak -> sneakmove)
        if (state.sneakWeight > 0.001f) {
            float staticSneakW = state.sneakWeight * (1.0f - state.sneakMoveWeight);
            float moveSneakW = state.sneakWeight * state.sneakMoveWeight;

            target2X = lerp(target2X, SNEAK_EAR2_X, staticSneakW);
            target2Y = lerp(target2Y, SNEAK_EAR2_Y, staticSneakW);
            target2Z = lerp(target2Z, SNEAK_EAR2_Z, staticSneakW);

            target3X = lerp(target3X, SNEAK_EAR3_X, staticSneakW);
            target3Y = lerp(target3Y, SNEAK_EAR3_Y, staticSneakW);
            target3Z = lerp(target3Z, SNEAK_EAR3_Z, staticSneakW);

            if (moveSneakW > 0.001f) {
                target2X = lerp(target2X, SNEAKMOVE_EAR2_X, moveSneakW);
                target2Y = lerp(target2Y, SNEAKMOVE_EAR2_Y, moveSneakW);
                target2Z = lerp(target2Z, SNEAKMOVE_EAR2_Z, moveSneakW);

                target3X = lerp(target3X, SNEAKMOVE_EAR3_X, moveSneakW);
                target3Y = lerp(target3Y, SNEAKMOVE_EAR3_Y, moveSneakW);
                target3Z = lerp(target3Z, SNEAKMOVE_EAR3_Z, moveSneakW);
            }
        }

        // Apply Sprint (загибаться назад чуть меньше чем sneak) - noticeable & expressive
        if (state.sprintWeight > 0.001f && state.swimWeight < 0.2f) {
            // Fold back ~80% of sneak pose with dynamic walk bounce
            target2X = lerp(target2X, SNEAK_EAR2_X * 0.80f, state.sprintWeight);
            target2Y = lerp(target2Y, SNEAK_EAR2_Y * 0.75f, state.sprintWeight);
            target2Z = lerp(target2Z, SNEAK_EAR2_Z * 0.75f - walkBounce, state.sprintWeight);

            target3X = lerp(target3X, SNEAK_EAR3_X * 0.80f, state.sprintWeight);
            target3Y = lerp(target3Y, SNEAK_EAR3_Y * 0.75f, state.sprintWeight);
            target3Z = lerp(target3Z, SNEAK_EAR3_Z * 0.75f + walkBounce, state.sprintWeight);
        } else if (isMoving && !isSneaking && state.swimWeight < 0.2f) {
            target2X -= walkBounce * 0.5f;
            target3X -= walkBounce * 0.5f;
        }

        // Apply Roll / Dodge (как sneak move - crisp and immediate)
        if (state.rollWeight > 0.001f) {
            target2X = lerp(target2X, SNEAKMOVE_EAR2_X, state.rollWeight);
            target2Y = lerp(target2Y, SNEAKMOVE_EAR2_Y, state.rollWeight);
            target2Z = lerp(target2Z, SNEAKMOVE_EAR2_Z, state.rollWeight);

            target3X = lerp(target3X, SNEAKMOVE_EAR3_X, state.rollWeight);
            target3Y = lerp(target3Y, SNEAKMOVE_EAR3_Y, state.rollWeight);
            target3Z = lerp(target3Z, SNEAKMOVE_EAR3_Z, state.rollWeight);
        }

        // Apply Aiming (загибаются чуть слабее чем при sneak move - clear and noticeable)
        if (state.aimWeight > 0.001f) {
            float aimBlend = state.aimWeight * 0.75f; // ~75% of sneakmove strength
            target2X = lerp(target2X, SNEAKMOVE_EAR2_X * 0.75f, aimBlend);
            target2Y = lerp(target2Y, SNEAKMOVE_EAR2_Y * 0.75f, aimBlend);
            target2Z = lerp(target2Z, SNEAKMOVE_EAR2_Z * 0.75f, aimBlend);

            target3X = lerp(target3X, SNEAKMOVE_EAR3_X * 0.75f, aimBlend);
            target3Y = lerp(target3Y, SNEAKMOVE_EAR3_Y * 0.75f, aimBlend);
            target3Z = lerp(target3Z, SNEAKMOVE_EAR3_Z * 0.75f, aimBlend);
        }

        // Apply Reloading (двигать другое ухо - ear3, в ту же сторону без отзеркаливания знака, более заметно)
        if (state.reloadWeight > 0.001f) {
            target3Z = lerp(target3Z, target3Z - 15.0f, state.reloadWeight);
            target3X = lerp(target3X, target3X - 6.0f, state.reloadWeight);
        }

        // Apply Gunshot Recoil / Flinch (слегка отбрасываются назад при стрельбе - punchy kick)
        if (state.shotFlinch > 0.01f) {
            float flinch = 20.0f * state.shotFlinch;
            target2X -= flinch;
            target3X -= flinch;
            target2Z += 4.5f * state.shotFlinch;
            target3Z -= 4.5f * state.shotFlinch;
        }

        // Apply Jump / Fall / Landing
        if (state.inAirWeight > 0.001f) {
            float vertMotion = (float) player.motionY;
            if (vertMotion > 0.05f) {
                // Rising: ears lift up
                target2Z += vertMotion * 15.0f * state.inAirWeight;
                target3Z -= vertMotion * 15.0f * state.inAirWeight;
            } else if (vertMotion < -0.1f) {
                // Falling: ears trail upward from air resistance
                target2X += vertMotion * 14.0f * state.inAirWeight;
                target3X += vertMotion * 14.0f * state.inAirWeight;
            }
        }
        if (state.landingSpring > 0.001f) {
            target2X -= state.landingSpring * 0.45f; // dip back on impact
            target3X -= state.landingSpring * 0.45f;
        }

        // Apply Prone / Crawling (fold flat against skull)
        if (state.proneWeight > 0.001f) {
            float foldX = -50.0f * state.proneWeight; // negative tilts back against skull
            target2X += foldX;
            target3X += foldX;
            target2Z = lerp(target2Z, -3.0f, state.proneWeight);
            target3Z = lerp(target3Z, 3.0f, state.proneWeight);
        }

        // Apply Swimming (matches sneakmove pose with gentle water current sway)
        if (state.swimWeight > 0.001f) {
            float waterWave = (float) Math.sin(player.ticksExisted * 0.18) * 1.8f;
            target2X = lerp(target2X, SNEAKMOVE_EAR2_X + waterWave, state.swimWeight);
            target2Y = lerp(target2Y, SNEAKMOVE_EAR2_Y, state.swimWeight);
            target2Z = lerp(target2Z, SNEAKMOVE_EAR2_Z, state.swimWeight);

            target3X = lerp(target3X, SNEAKMOVE_EAR3_X + waterWave, state.swimWeight);
            target3Y = lerp(target3Y, SNEAKMOVE_EAR3_Y, state.swimWeight);
            target3Z = lerp(target3Z, SNEAKMOVE_EAR3_Z, state.swimWeight);
        }

        // Apply Hurt (при получении урона загибаются назад как при sneakmove - мгновенно и резко)
        if (player.hurtTime > 0) {
            float hurtFactor = (float) player.hurtTime / (player.maxHurtTime > 0 ? (float) player.maxHurtTime : 10.0f);
            target2X = lerp(target2X, SNEAKMOVE_EAR2_X, hurtFactor);
            target2Y = lerp(target2Y, SNEAKMOVE_EAR2_Y, hurtFactor);
            target2Z = lerp(target2Z, SNEAKMOVE_EAR2_Z, hurtFactor);

            target3X = lerp(target3X, SNEAKMOVE_EAR3_X, hurtFactor);
            target3Y = lerp(target3Y, SNEAKMOVE_EAR3_Y, hurtFactor);
            target3Z = lerp(target3Z, SNEAKMOVE_EAR3_Z, hurtFactor);
        }

        // Smoothly interpolate to targets with fast, responsive step
        state.curEar2X += (target2X - state.curEar2X) * 0.48f;
        state.curEar2Y += (target2Y - state.curEar2Y) * 0.48f;
        state.curEar2Z += (target2Z - state.curEar2Z) * 0.48f;

        state.curEar3X += (target3X - state.curEar3X) * 0.48f;
        state.curEar3Y += (target3Y - state.curEar3Y) * 0.48f;
        state.curEar3Z += (target3Z - state.curEar3Z) * 0.48f;
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    /**
     * Applies smoothly interpolated ear rotations to GeoArmorModel.
     */
    public static void applyAnimations(GeoArmorModel model, EntityPlayer player, float partialTicks) {
        if (model == null || player == null) return;

        PlayerEarState state = getState(player);

        // Frame-interpolated continuous breathing wave:
        float breatheX = 0.0f;
        float breatheZ = 0.0f;
        if (state.sneakWeight < 0.5f && state.proneWeight < 0.5f && state.swimWeight < 0.5f) {
            float breatheTime = (player.ticksExisted + partialTicks) * 0.055f;
            float idleWeight = 1.0f - Math.min(1.0f, (float) Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ) * 4.0f);
            idleWeight *= (1.0f - state.sneakWeight);
            if (idleWeight > 0.01f) {
                breatheX = (float) Math.sin(breatheTime) * 1.0f * idleWeight;
                breatheZ = (float) Math.cos(breatheTime * 0.9f) * 0.6f * idleWeight;
            }
        }

        float ear2X = lerp(state.prevEar2X, state.curEar2X, partialTicks) + breatheX;
        float ear2Y = lerp(state.prevEar2Y, state.curEar2Y, partialTicks);
        float ear2Z = lerp(state.prevEar2Z, state.curEar2Z, partialTicks) - breatheZ;

        float ear3X = lerp(state.prevEar3X, state.curEar3X, partialTicks) + breatheX;
        float ear3Y = lerp(state.prevEar3Y, state.curEar3Y, partialTicks);
        float ear3Z = lerp(state.prevEar3Z, state.curEar3Z, partialTicks) + breatheZ;


        // Base rotations in radians (pointing forward and flared outward: "изнутри вперед")
        float baseEar2X = (float) Math.toRadians(11.76175f);
        float baseEar2Y = (float) Math.toRadians(9.05478f);
        float baseEar2Z = (float) Math.toRadians(25.33761f);

        float baseEar3X = (float) Math.toRadians(11.76175f);
        float baseEar3Y = (float) Math.toRadians(-9.05478f);
        float baseEar3Z = (float) Math.toRadians(-25.33761f);

        net.minecraft.client.model.ModelRenderer ear2 = model.getBone("ear2");
        if (ear2 != null) {
            ear2.rotateAngleX = baseEar2X + (float) Math.toRadians(ear2X);
            ear2.rotateAngleY = baseEar2Y + (float) Math.toRadians(ear2Y);
            ear2.rotateAngleZ = baseEar2Z + (float) Math.toRadians(ear2Z);
        }

        net.minecraft.client.model.ModelRenderer ear3 = model.getBone("ear3");
        if (ear3 != null) {
            ear3.rotateAngleX = baseEar3X + (float) Math.toRadians(ear3X);
            ear3.rotateAngleY = baseEar3Y + (float) Math.toRadians(ear3Y);
            ear3.rotateAngleZ = baseEar3Z + (float) Math.toRadians(ear3Z);
        }
    }
}
