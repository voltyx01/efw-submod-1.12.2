package net.bettercombat.client.animation;

import com.paneedah.weaponlib.Weapon;
import efw.animation.AnimationClip;
import efw.animation.AnimationPlayer;
import efw.animation.AnimationRegistry;
import net.bettercombat.api.AttackHand;
import net.bettercombat.logic.AnimatedHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.client.Minecraft;

import net.minecraft.util.math.MathHelper;

import java.util.Locale;

public class AttackAnimationHelper {

    public static void playAttackAnimation(
            EntityPlayer player,
            String animationName,
            AnimatedHand animatedHand,
            float cooldownTicks,
            float upswingRate) {
        playAttackAnimationInternal(player, animationName, animatedHand, cooldownTicks, upswingRate);
    }

    public static void playAttackAnimation(
            EntityPlayer player,
            String animationName,
            AnimatedHand animatedHand,
            int swingTimerCap,
            int strikeTicks) {
        float length = Math.max(1.0F, (float) swingTimerCap);
        float upswingRate = MathHelper.clamp(strikeTicks / length, 0.05F, 0.95F);
        playAttackAnimationInternal(player, animationName, animatedHand, length, upswingRate);
    }

    private static void playAttackAnimationInternal(
            EntityPlayer player,
            String animationName,
            AnimatedHand animatedHand,
            float lengthTicks,
            float upswingRate) {
        if (player == null || animationName == null || animationName.isEmpty()) {
            return;
        }

        if (!isVanillaWeaponAttack(player, animatedHand)) {
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

        float duration = Math.max(1.0F, lengthTicks);
        float upswing = MathHelper.clamp(upswingRate, 0.05F, 0.95F);
        float speed = (clip.endTick > 0 ? clip.endTick : clip.length * 20.0F) / duration;
        float upswingMultiplier = Math.max(0.05F, (float) efw.biomeinfo.MwccfConfig.betterCombat.upswingMultiplier);
        float upswingSpeed = speed / upswingMultiplier;
        float blendFactor = MathHelper.clamp((upswingMultiplier - 0.5F) / 0.5F, 0.0F, 1.0F);
        float downwindStart = 1.0F - upswing;
        float downwindEnd = upswing / (1.0F - upswing);
        float downwindRatio = downwindStart + (downwindEnd - downwindStart) * blendFactor;
        float downwindSpeed = speed * downwindRatio;
        int blendIn = 2;

        ap.setAnimatedHand(animatedHand);
        ap.setActionBetterCombat(clip, upswingSpeed, downwindSpeed, duration * upswing, duration, blendIn);
    }

    public static boolean isVanillaWeaponAttack(EntityPlayer player, AnimatedHand animatedHand) {
        if (player == null) return false;

        EnumHand hand = animatedHand == AnimatedHand.OFF_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty() || stack.getItem() instanceof Weapon) return false;

        net.minecraft.util.ResourceLocation itemId = stack.getItem().getRegistryName();
        return itemId != null && !itemId.toString().startsWith("mwc:")
                && net.bettercombat.logic.WeaponRegistry.getAttributes(stack) != null;
    }
}
