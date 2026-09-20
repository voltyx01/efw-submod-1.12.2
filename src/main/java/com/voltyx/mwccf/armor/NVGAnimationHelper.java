package com.voltyx.mwccf.armor;

import net.minecraft.entity.EntityLivingBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NVGAnimationHelper {

    public static class AnimState {
        public boolean targetActive;
        public float currentProgress; // 0.0 = folded up (-1.35 rad), 1.0 = down on eyes (0.0 rad)
        public long lastTimeMs;

        public AnimState(boolean initialActive) {
            this.targetActive = initialActive;
            this.currentProgress = initialActive ? 1.0f : 0.0f;
            this.lastTimeMs = System.currentTimeMillis();
        }
    }

    private static final Map<Integer, AnimState> STATES = new ConcurrentHashMap<>();

    private static final float TRANSITION_DURATION_SECONDS = 0.35f; // 350 ms for smooth mechanical fold
    public static final float ANGLE_UP = -1.35f;
    public static final float ANGLE_DOWN = 0.0f;

    private static AnimState getOrCreate(EntityLivingBase entity, boolean active) {
        int id = entity.getEntityId();
        AnimState state = STATES.get(id);
        if (state == null) {
            state = new AnimState(active);
            STATES.put(id, state);
            return state;
        }

        long now = System.currentTimeMillis();
        float dt = (now - state.lastTimeMs) / 1000.0f;
        if (dt > 0.5f) dt = 0.05f; // clamp lag spikes
        if (dt < 0.0f) dt = 0.0f;
        state.lastTimeMs = now;

        state.targetActive = active;

        float speed = 1.0f / TRANSITION_DURATION_SECONDS;
        if (state.targetActive) {
            state.currentProgress = Math.min(1.0f, state.currentProgress + dt * speed);
        } else {
            state.currentProgress = Math.max(0.0f, state.currentProgress - dt * speed);
        }

        return state;
    }

    public static float getVisorAngle(EntityLivingBase entity, boolean active) {
        if (entity == null) {
            return active ? ANGLE_DOWN : ANGLE_UP;
        }

        AnimState state = getOrCreate(entity, active);
        float t = state.currentProgress;

        // Smooth cosine S-curve easing
        float eased = (float) (0.5f - 0.5f * Math.cos(t * Math.PI));
        return ANGLE_UP + (ANGLE_DOWN - ANGLE_UP) * eased;
    }

    public static float getProgress(EntityLivingBase entity, boolean active) {
        if (entity == null) {
            return active ? 1.0f : 0.0f;
        }
        AnimState state = getOrCreate(entity, active);
        return state.currentProgress;
    }

    public static void cleanup(int entityId) {
        STATES.remove(entityId);
    }
}
