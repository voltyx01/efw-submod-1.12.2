package net.bettercombat.client.animation;

import efw.animation.AnimationClip;
import efw.animation.AnimationPlayer;
import efw.animation.AnimationRegistry;
import efw.animation.layered.IAnimation;
import efw.animation.layered.KeyframeAnimationPlayer;
import efw.animation.layered.math.Ease;
import efw.animation.layered.modifier.AbstractFadeModifier;
import efw.animation.layered.modifier.ModifierLayer;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;
import java.util.WeakHashMap;

public class PoseHelper {

    private static class PoseState {
        final ModifierLayer<IAnimation> poseLayer = new ModifierLayer<>();
        String currentPose = null;
        boolean layerAdded = false;
    }

    private static final Map<EntityPlayer, PoseState> POSE_MAP = new WeakHashMap<>();

    public static void update(EntityPlayer player) {
        if (player == null) return;

        AnimationPlayer ap = AnimationRegistry.getPlayer(player);
        if (ap == null) return;

        PoseState state = POSE_MAP.computeIfAbsent(player, p -> new PoseState());

        if (!state.layerAdded) {
            ap.getStack().addAnimLayer(50, state.poseLayer);
            state.layerAdded = true;
        }

        // Determine if pose should be shown
        String targetPose = null;
        if (!player.isRiding() && !player.isHandActive()) {
            WeaponAttributes attributes = WeaponRegistry.getAttributes(player.getHeldItemMainhand());
            if (attributes != null && attributes.pose() != null) {
                targetPose = attributes.pose();
            }
        }

        if (targetPose == null && state.currentPose == null) {
            return;
        }

        if (targetPose != null && targetPose.equals(state.currentPose)) {
            return;
        }

        state.currentPose = targetPose;

        if (targetPose == null) {
            state.poseLayer.replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(5, Ease::inOutSine), null, true);
        } else {
            String clipName = targetPose;
            if (clipName.contains(":")) {
                clipName = clipName.substring(clipName.indexOf(':') + 1);
            }
            AnimationClip clip = AnimationRegistry.getClip(clipName);
            if (clip == null) {
                clip = AnimationRegistry.getClip(targetPose);
            }

            if (clip != null) {
                KeyframeAnimationPlayer playerAnim = new KeyframeAnimationPlayer(clip);
                state.poseLayer.replaceAnimationWithFade(
                        AbstractFadeModifier.standardFadeIn(5, Ease::inOutSine), playerAnim, true);
            } else {
                state.poseLayer.replaceAnimationWithFade(
                        AbstractFadeModifier.standardFadeIn(5, Ease::inOutSine), null, true);
            }
        }
    }
}
