package com.voltyx.gender.main;

import com.voltyx.gender.api.IGenderArmor;
import com.voltyx.gender.render.armor.SimpleGenderArmor;
import com.voltyx.gender.render.armor.EmptyGenderArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraft.util.EnumFacing;

import java.util.concurrent.ThreadLocalRandom;

public class WildfireHelper {

    // В 1.12.2 Capability инжектится через аннотацию
    @CapabilityInject(IGenderArmor.class)
    public static final Capability<IGenderArmor> GENDER_ARMOR_CAPABILITY = null;

    public static float randFloat(float min, float max) {
        return (float) ThreadLocalRandom.current().nextDouble(min, (double) max + 1.0D);
    }

    public static IGenderArmor getArmorConfig(ItemStack stack) {
        if (stack.isEmpty()) {
            return EmptyGenderArmor.INSTANCE;
        }

        // Проверяем наличие Capability на предмете
        if (GENDER_ARMOR_CAPABILITY != null && stack.hasCapability(GENDER_ARMOR_CAPABILITY, null)) {
            IGenderArmor cap = stack.getCapability(GENDER_ARMOR_CAPABILITY, null);
            if (cap != null) {
                return cap;
            }
        }

        // Fallback-логика, если Capability нет (как в оригинале)
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armorItem = (ItemArmor) stack.getItem();

            if (armorItem.armorType == EntityEquipmentSlot.CHEST) {
                ItemArmor.ArmorMaterial material = armorItem.getArmorMaterial();

                if (material == ItemArmor.ArmorMaterial.LEATHER) {
                    return SimpleGenderArmor.LEATHER;
                } else if (material == ItemArmor.ArmorMaterial.CHAIN) {
                    return SimpleGenderArmor.CHAIN_MAIL;
                } else if (material == ItemArmor.ArmorMaterial.GOLD) {
                    return SimpleGenderArmor.GOLD;
                } else if (material == ItemArmor.ArmorMaterial.IRON) {
                    return SimpleGenderArmor.IRON;
                } else if (material == ItemArmor.ArmorMaterial.DIAMOND) {
                    return SimpleGenderArmor.DIAMOND;
                }
                // В 1.12.2 нет незерита, поэтому сразу возвращаем Fallback
                return SimpleGenderArmor.FALLBACK;
            }
        }

        return EmptyGenderArmor.INSTANCE;
    }
}