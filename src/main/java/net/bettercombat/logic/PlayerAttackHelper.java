package net.bettercombat.logic;

import com.google.common.collect.Multimap;
import efw.biomeinfo.MwccfConfig;
import net.bettercombat.api.AttackHand;
import net.bettercombat.api.ComboState;
import net.bettercombat.api.WeaponAttributes;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PlayerAttackHelper {

    public static float getDualWieldingAttackDamageMultiplier(EntityPlayer player, AttackHand hand) {
        if (!isDualWielding(player)) {
            return 1.0f;
        }
        return hand.isOffHand()
                ? (float) MwccfConfig.betterCombat.dualWieldingOffHandDamageMultiplier
                : 1.0f;
    }

    public static boolean shouldAttackWithOffHand(EntityPlayer player, int comboCount) {
        return isDualWielding(player) && comboCount % 2 == 1;
    }

    public static boolean isDualWielding(EntityPlayer player) {
        if (player == null) return false;
        ItemStack mainStack = player.getHeldItemMainhand();
        ItemStack offStack = player.getHeldItemOffhand();
        if (mainStack.isEmpty() || offStack.isEmpty()) {
            return false;
        }
        WeaponAttributes mainAttributes = WeaponRegistry.getAttributes(mainStack);
        WeaponAttributes offAttributes = WeaponRegistry.getAttributes(offStack);
        return mainAttributes != null && !mainAttributes.isTwoHanded()
                && offAttributes != null && !offAttributes.isTwoHanded();
    }

    public static boolean isTwoHandedWielding(EntityPlayer player) {
        if (player == null) return false;
        WeaponAttributes mainAttributes = WeaponRegistry.getAttributes(player.getHeldItemMainhand());
        return mainAttributes != null && mainAttributes.isTwoHanded();
    }

    public static float getAttackCooldownLengthTicks(EntityPlayer player) {
        return getAttackCooldownLengthTicks(player, player != null ? player.getHeldItemMainhand() : ItemStack.EMPTY);
    }

    public static float getAttackCooldownLengthTicks(EntityPlayer player, ItemStack stack) {
        if (player == null) return 12.5f;
        if (stack == null) stack = ItemStack.EMPTY;

        double speed = getItemAttackSpeed(player, stack);
        if (speed <= 0.0) {
            WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
            if (attributes != null && attributes.category() != null) {
                speed = getCategoryDefaultSpeed(attributes.category());
            } else if (attributes != null && attributes.attacks() != null && attributes.attacks().length > 0) {
                speed = 1.6;
            }
        }

        if (speed <= 0.0) speed = 1.6;
        float baseCooldown = (float) ((1.0 / speed) * 20.0);

        if (isDualWielding(player)) {
            float dualMult = (float) MwccfConfig.betterCombat.dualWieldingAttackSpeedMultiplier;
            if (dualMult > 0.1f) {
                baseCooldown /= dualMult;
            }
        }
        return Math.max(baseCooldown, (float) MwccfConfig.betterCombat.attackIntervalCap);
    }

    public static double getItemAttackSpeed(EntityPlayer player, ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 4.0;
        }
        try {
            Multimap<String, AttributeModifier> modifiers = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            if (modifiers != null && modifiers.containsKey(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
                double speed = 4.0;
                for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
                    if (mod.getOperation() == 0) {
                        speed += mod.getAmount();
                    }
                }
                double baseMultiplier = 0.0;
                for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
                    if (mod.getOperation() == 1) {
                        baseMultiplier += mod.getAmount();
                    }
                }
                speed += speed * baseMultiplier;
                for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
                    if (mod.getOperation() == 2) {
                        speed *= (1.0 + mod.getAmount());
                    }
                }
                if (speed > 0.0) {
                    return speed;
                }
            }
        } catch (Exception ignored) {}

        return -1.0;
    }

    public static double getCategoryDefaultSpeed(String category) {
        if (category == null) return 1.6;
        switch (category.toLowerCase()) {
            case "dagger":
                return 2.2;
            case "fist":
            case "claw":
                return 2.4;
            case "rapier":
            case "cutlass":
                return 1.8;
            case "katana":
                return 2.0;
            case "sword":
            case "twin_blade":
                return 1.6;
            case "spear":
            case "glaive":
            case "halberd":
            case "trident":
                return 1.2;
            case "scythe":
                return 1.1;
            case "claymore":
            case "heavy_axe":
            case "double_axe":
            case "axe":
            case "battleaxe":
            case "battlestaff":
            case "staff":
                return 1.0;
            case "hammer":
            case "mace":
            case "anchor":
                return 0.8;
            default:
                return 1.6;
        }
    }

    @Nullable
    public static AttackHand getCurrentAttack(EntityPlayer player, int comboCount) {
        if (player == null) return null;

        if (isDualWielding(player)) {
            boolean isOffHand = shouldAttackWithOffHand(player, comboCount);
            ItemStack itemStack = isOffHand
                    ? player.getHeldItemOffhand()
                    : player.getHeldItemMainhand();
            WeaponAttributes attributes = WeaponRegistry.getAttributes(itemStack);
            if (attributes != null && attributes.attacks() != null) {
                int handSpecificComboCount = ((isOffHand && comboCount > 0) ? (comboCount - 1) : comboCount) / 2;
                AttackSelection selection = selectAttack(handSpecificComboCount, attributes, player, isOffHand);
                if (selection != null) {
                    return new AttackHand(selection.attack, selection.comboState, isOffHand, attributes, itemStack);
                }
            }
        } else {
            ItemStack itemStack = player.getHeldItemMainhand();
            WeaponAttributes attributes = WeaponRegistry.getAttributes(itemStack);
            if (attributes != null && attributes.attacks() != null) {
                AttackSelection selection = selectAttack(comboCount, attributes, player, false);
                if (selection != null) {
                    return new AttackHand(selection.attack, selection.comboState, false, attributes, itemStack);
                }
            }
        }
        return null;
    }

    private static class AttackSelection {
        public final WeaponAttributes.Attack attack;
        public final ComboState comboState;

        public AttackSelection(WeaponAttributes.Attack attack, ComboState comboState) {
            this.attack = attack;
            this.comboState = comboState;
        }
    }

    @Nullable
    private static AttackSelection selectAttack(int comboCount, WeaponAttributes attributes, EntityPlayer player, boolean isOffHandAttack) {
        WeaponAttributes.Attack[] allAttacks = attributes.attacks();
        if (allAttacks == null || allAttacks.length == 0) {
            return null;
        }

        List<WeaponAttributes.Attack> filtered = new ArrayList<>();
        for (WeaponAttributes.Attack attack : allAttacks) {
            if (attack.conditions() == null
                    || attack.conditions().length == 0
                    || evaluateConditions(attack.conditions(), player, isOffHandAttack)) {
                filtered.add(attack);
            }
        }

        if (filtered.isEmpty()) {
            return null;
        }

        if (comboCount < 0) comboCount = 0;
        int index = comboCount % filtered.size();
        return new AttackSelection(filtered.get(index), new ComboState(index, filtered.size()));
    }

    public static boolean evaluateConditions(WeaponAttributes.Condition[] conditions, EntityPlayer player, boolean isOffHandAttack) {
        if (conditions == null) return true;
        for (WeaponAttributes.Condition condition : conditions) {
            if (!evaluateCondition(condition, player, isOffHandAttack)) {
                return false;
            }
        }
        return true;
    }

    private static boolean evaluateCondition(WeaponAttributes.Condition condition, EntityPlayer player, boolean isOffHandAttack) {
        switch (condition) {
            case NOT_DUAL_WIELDING:
                return !isDualWielding(player);
            case DUAL_WIELDING_ANY:
                return isDualWielding(player);
            case DUAL_WIELDING_SAME:
                return isDualWielding(player)
                        && player.getHeldItemMainhand().getItem() == player.getHeldItemOffhand().getItem();
            case DUAL_WIELDING_SAME_CATEGORY:
                if (!isDualWielding(player)) return false;
                WeaponAttributes a = WeaponRegistry.getAttributes(player.getHeldItemMainhand());
                WeaponAttributes b = WeaponRegistry.getAttributes(player.getHeldItemOffhand());
                return a != null && b != null && a.category() != null && a.category().equals(b.category());
            case NO_OFFHAND_ITEM:
                return player.getHeldItemOffhand().isEmpty();
            case OFF_HAND_SHIELD:
                return player.getHeldItemOffhand().getItem() instanceof ItemShield;
            case MAIN_HAND_ONLY:
                return !isOffHandAttack;
            case OFF_HAND_ONLY:
                return isOffHandAttack;
            case MOUNTED:
                return player.isRiding();
            case NOT_MOUNTED:
                return !player.isRiding();
            default:
                return true;
        }
    }
}
