package efw.animation.firstperson;

import efw.animation.AnimationPlayer;
import efw.animation.AnimationRegistry;
import efw.biomeinfo.MwccfConfig;
import net.bettercombat.client.animation.AttackAnimationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class FirstPersonMode {
    private static final ThreadLocal<Boolean> firstPersonPass = ThreadLocal.withInitial(() -> false);

    public static boolean isFirstPersonPass() {
        return firstPersonPass.get();
    }

    public static void setFirstPersonPass(boolean newValue) {
        firstPersonPass.set(newValue);
    }

    public static boolean isFirstPersonAttackActive(net.minecraft.entity.Entity entity) {
        if (!(entity instanceof EntityPlayer)) return false;
        return isFirstPersonAttackActive((EntityPlayer) entity);
    }

    public static boolean isFirstPersonAttackActive(EntityPlayer player) {
        if (player == null) return false;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings == null || mc.gameSettings.thirdPersonView != 0) return false;
        if (player != mc.player) return false;
        if (efw.util.RenderContext.isRenderingPlayerInGui) return false;
        if (MwccfConfig.betterCombat == null || !MwccfConfig.betterCombat.enabled) {
            return false;
        }
        if (!MwccfConfig.betterCombat.isFirstPersonAttackAnimationsEnabled) {
            return false;
        }
        AnimationPlayer ap = AnimationRegistry.getPlayer(player);
        if (ap == null) return false;
        if (!ap.isActionAttack() || !ap.hasActionWeight()) return false;
        if (!AttackAnimationHelper.isVanillaWeaponAttack(player, ap.getAnimatedHand())) return false;
        if (ap.isActionFadingOut()) {
            float fadeAlpha = ap.getActionFadeAlpha(1.0f);
            if (fadeAlpha <= 0.05f) return false;
        }
        return true;
    }
}
