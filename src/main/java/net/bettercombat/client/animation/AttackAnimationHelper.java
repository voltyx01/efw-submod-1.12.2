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

        // Calculate speed to match the weapon's attack cooldown
        float clipDurationTicks = clip.length * 20.0f;
        float speed = 1.6f;
        if (cooldownTicks > 0 && clipDurationTicks > 0) {
            speed = clipDurationTicks / cooldownTicks;
            speed = Math.max(0.8f, Math.min(3.5f, speed));
        }

        ap.setAction(clip, 0.0f, speed);
    }
}
