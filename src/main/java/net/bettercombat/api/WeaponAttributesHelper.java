package net.bettercombat.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.io.InvalidObjectException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WeaponAttributesHelper {

    public static WeaponAttributes override(WeaponAttributes a, WeaponAttributes b) {
        double attackRange = b.attackRange() > 0 ? b.attackRange() : a.attackRange();
        String pose = b.pose() != null ? b.pose() : a.pose();
        String off_hand_pose = b.offHandPose() != null ? b.offHandPose() : a.offHandPose();
        boolean isTwoHanded = b.isTwoHanded() || a.isTwoHanded();
        String category = b.category() != null ? b.category() : a.category();
        WeaponAttributes.Attack[] attacks = a.attacks();

        if (b.attacks() != null && b.attacks().length > 0) {
            List<WeaponAttributes.Attack> overrideAttacks = new ArrayList<>();
            for (int i = 0; i < b.attacks().length; ++i) {
                WeaponAttributes.Attack base = (a.attacks() != null && a.attacks().length > i)
                        ? a.attacks()[i]
                        : new WeaponAttributes.Attack(null, null, 1.0, 0, 0.5, null, null, null);
                WeaponAttributes.Attack override = b.attacks()[i];
                WeaponAttributes.Attack attack = new WeaponAttributes.Attack(
                        override.conditions() != null ? override.conditions() : base.conditions(),
                        override.hitbox() != null ? override.hitbox() : base.hitbox(),
                        override.damageMultiplier() != 0 ? override.damageMultiplier() : base.damageMultiplier(),
                        override.angle() != 0 ? override.angle() : base.angle(),
                        override.upswing() != 0 ? override.upswing() : base.upswing(),
                        override.animation() != null ? override.animation() : base.animation(),
                        override.swingSound() != null ? override.swingSound() : base.swingSound(),
                        override.impactSound() != null ? override.impactSound() : base.impactSound());
                overrideAttacks.add(attack);
            }
            attacks = overrideAttacks.toArray(new WeaponAttributes.Attack[0]);
        }
        return new WeaponAttributes(attackRange, pose, off_hand_pose, isTwoHanded, category, attacks);
    }

    public static void validate(WeaponAttributes attributes) throws Exception {
        if (attributes.attacks() == null) {
            return;
        }
        if (attributes.attacks().length > 0) {
            int index = 0;
            for (WeaponAttributes.Attack attack : attributes.attacks()) {
                try {
                    validate(attack);
                } catch (InvalidObjectException exception) {
                    throw new InvalidObjectException("Invalid attack at index: " + index + " - " + exception.getMessage());
                }
                index += 1;
            }
        }
    }

    private static void validate(WeaponAttributes.Attack attack) throws InvalidObjectException {
        if (attack.hitbox() == null) {
            throw new InvalidObjectException("Undefined `hitbox`");
        }
        if (attack.damageMultiplier() < 0) {
            throw new InvalidObjectException("Invalid `damage_multiplier`");
        }
        if (attack.angle() < 0) {
            throw new InvalidObjectException("Invalid `angle`");
        }
        if (attack.upswing() < 0) {
            throw new InvalidObjectException("Invalid `upswing`");
        }
        if (attack.animation() == null || attack.animation().length() == 0) {
            throw new InvalidObjectException("Undefined `animation`");
        }
    }

    public static final String NBT_TAG = "weapon_attributes";

    public static WeaponAttributes readFromNBT(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty() || !itemStack.hasTagCompound()) {
            return null;
        }
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag != null && tag.hasKey(NBT_TAG)) {
            String jsonStr = tag.getString(NBT_TAG);
            if (jsonStr != null && !jsonStr.isEmpty()) {
                ResourceLocation itemId = itemStack.getItem().getRegistryName();
                try {
                    AttributesContainer container = decode(new StringReader(jsonStr));
                    return WeaponRegistry.resolveAttributes(itemId, container);
                } catch (Exception e) {
                    System.err.println("Failed to resolve weapon attributes from NBT on item: " + itemId);
                }
            }
        }
        return null;
    }

    private static final Type ATTRIBUTES_CONTAINER_TYPE = new TypeToken<AttributesContainer>() {}.getType();

    public static AttributesContainer decode(Reader reader) {
        Gson gson = new Gson();
        return gson.fromJson(reader, ATTRIBUTES_CONTAINER_TYPE);
    }

    public static AttributesContainer decode(JsonReader json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ATTRIBUTES_CONTAINER_TYPE);
    }

    public static String encode(AttributesContainer container) {
        Gson gson = new Gson();
        return gson.toJson(container);
    }
}
