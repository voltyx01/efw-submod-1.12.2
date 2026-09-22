package net.bettercombat.api;

import net.minecraft.item.ItemStack;

public final class AttackHand {
    private final WeaponAttributes.Attack attack;
    private final ComboState comboState;
    private final boolean isOffHand;
    private final WeaponAttributes attributes;
    private final ItemStack itemStack;

    public AttackHand(
            WeaponAttributes.Attack attack,
            ComboState comboState,
            boolean isOffHand,
            WeaponAttributes attributes,
            ItemStack itemStack) {
        this.attack = attack;
        this.comboState = comboState;
        this.isOffHand = isOffHand;
        this.attributes = attributes;
        this.itemStack = itemStack;
    }

    public WeaponAttributes.Attack attack() {
        return attack;
    }

    public ComboState comboState() {
        return comboState;
    }

    public boolean isOffHand() {
        return isOffHand;
    }

    public WeaponAttributes attributes() {
        return attributes;
    }

    public ItemStack itemStack() {
        return itemStack;
    }

    public double upswingRate() {
        return attack != null ? attack.upswingRate() : 0.5;
    }
}
