package efw.animation.firstperson;

import efw.animation.AnimationPlayer;
import efw.animation.AnimationRegistry;
import efw.biomeinfo.MwccfConfig;
import net.bettercombat.client.animation.AttackAnimationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class FirstPersonMode {
    public static boolean isFirstPersonPass() {
        return false;
    }

    public static void setFirstPersonPass(boolean newValue) {
    }

    public static boolean isFirstPersonAttackActive(net.minecraft.entity.Entity entity) {
        return false;
    }

    public static boolean isFirstPersonAttackActive(EntityPlayer player) {
        return false;
    }
}
