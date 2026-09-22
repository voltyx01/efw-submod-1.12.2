package net.bettercombat.logic;

import efw.biomeinfo.MwccfConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;

public class TargetHelper {
    public enum Relation {
        FRIENDLY, NEUTRAL, HOSTILE
    }

    public static Relation getRelation(EntityPlayer attacker, Entity target) {
        if (attacker == target) {
            return Relation.FRIENDLY;
        }

        if (target instanceof EntityTameable) {
            EntityTameable tameable = (EntityTameable) target;
            EntityLivingBase owner = tameable.getOwner();
            if (owner instanceof EntityPlayer) {
                return getRelation(attacker, owner);
            }
        }

        if (target instanceof EntityHanging) {
            return Relation.NEUTRAL;
        }

        Team casterTeam = attacker.getTeam();
        Team targetTeam = target.getTeam();
        if (casterTeam != null && targetTeam != null) {
            return attacker.isOnSameTeam(target) ? Relation.FRIENDLY : Relation.HOSTILE;
        }

        if (target instanceof EntityPlayer) {
            return Relation.NEUTRAL;
        }

        if (target instanceof IMob) {
            return Relation.HOSTILE;
        }

        if (target instanceof IAnimals) {
            return Relation.HOSTILE;
        }

        return Relation.HOSTILE;
    }

    public static boolean isAttackableMount(Entity entity) {
        if (entity instanceof IMob) {
            return true;
        }
        return MwccfConfig.betterCombat.allowAttackingMount;
    }
}
