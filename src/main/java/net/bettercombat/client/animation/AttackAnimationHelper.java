package net.bettercombat.client.animation;

import efw.animation.AnimationClip;
import efw.animation.AnimationPlayer;
import efw.animation.AnimationRegistry;
import net.bettercombat.api.AttackHand;
import net.bettercombat.logic.AnimatedHand;
import net.minecraft.entity.player.EntityPlayer;

public class AttackAnimationHelper {

    public static void playAttackAnimation(
            EntityPlayer player,
            String animationName,
            AnimatedHand animatedHand,
            float cooldownTicks,
            float upswingRate) {
        if (player == null || animationName == null || animationName.isEmpty()) {
            return;
        }

        AnimationPlayer ap = AnimationRegistry.getPlayer(player);
        if (ap == null) {
            return;
        }

        String clipName = animationName;
        if (clipName.contains(":")) {
            clipName = clipName.substring(clipName.indexOf(':') + 1);
        }

        AnimationClip clip = AnimationRegistry.getClip(clipName);
        if (clip == null) {
            clip = AnimationRegistry.getClip(animationName);
        }

        if (clip == null) {
            return;
        }

        clip.isBetterCombat = true;

        if (player.world != null && player.world.isRemote) {
            player.renderYawOffset = player.rotationYawHead;
            player.prevRenderYawOffset = player.prevRotationYawHead;
        }

        // Exact speed formula from Better Combat 1.20.1 (AbstractClientPlayerEntityMixin):
        // speed = clip.endTick / cooldownTicks
        // upswingSpeed = speed / upswing_multiplier (0.5F) = speed * 2.0F
        // downwindSpeed = speed * (1.0F - upswingRate)
        float baseSpeed = 1.0f;
        if (clip.endTick > 0 && cooldownTicks > 0) {
            baseSpeed = ((float) clip.endTick) / cooldownTicks;
        } else if (clip.length > 0 && cooldownTicks > 0) {
            baseSpeed = (clip.length * 20.0f) / cooldownTicks;
        }

        // upswing_multiplier is 0.5f in BetterCombat default config
        float upswingMultiplier = 0.5f;
        float upswingSpeed = baseSpeed / upswingMultiplier;
        float downwindSpeed = baseSpeed * (1.0f - upswingRate);

        // Blend-in: 3 ticks (150ms) for smooth entry from idle/walk into attack without delay
        int blendIn = 3;

        ap.setActionBetterCombat(clip, upswingSpeed, downwindSpeed, cooldownTicks * upswingRate, cooldownTicks, blendIn);
    }
}
