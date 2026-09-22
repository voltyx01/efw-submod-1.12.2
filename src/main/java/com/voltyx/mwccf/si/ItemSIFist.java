package com.voltyx.mwccf.si;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class ItemSIFist extends ItemSword {
    private final float attackDamage;
    private final float attackSpeed;
    private final boolean isElectric;
    private final SoundEvent hitSound;
    private final Supplier<Item> repairItemSupplier;

    public ItemSIFist(String name, ToolMaterial material, int maxDamage, float attackDamage, float attackSpeed,
                      boolean isElectric, SoundEvent hitSound, Supplier<Item> repairItemSupplier) {
        super(material);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.isElectric = isElectric;
        this.hitSound = hitSound;
        this.repairItemSupplier = repairItemSupplier;

        if (maxDamage > 0) {
            this.setMaxDamage(maxDamage);
        }
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("mwccf." + name);
        this.setCreativeTab(net.minecraft.creativetab.CreativeTabs.COMBAT);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!target.world.isRemote) {
            if (this.isElectric) {
                target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 19, false, false));
                target.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 5, 19, false, false));
            }
            if (this.hitSound != null) {
                target.world.playSound(null, target.posX, target.posY, target.posZ, this.hitSound, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (this.repairItemSupplier != null && this.repairItemSupplier.get() != null) {
            return repair.getItem() == this.repairItemSupplier.get();
        }
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
            multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) this.attackSpeed - 4.0D, 0));
        }
        return multimap;
    }
}
