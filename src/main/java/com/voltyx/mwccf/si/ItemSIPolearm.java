package com.voltyx.mwccf.si;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ItemSIPolearm extends ItemSword {
    private static final UUID REACH_MODIFIER_UUID = UUID.fromString("c07e8574-8b64-4e4b-9e4a-47fa3a76380c");

    private final float attackDamage;
    private final float attackSpeed;
    private final double reachBonus;
    private final int bleedingTicks;
    private final SoundEvent hitSound;
    private final Supplier<Item> repairItemSupplier;

    public ItemSIPolearm(String name, ToolMaterial material, int maxDamage, float attackDamage, float attackSpeed,
                         double reachBonus, int bleedingTicks, SoundEvent hitSound, Supplier<Item> repairItemSupplier) {
        super(material);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.reachBonus = reachBonus;
        this.bleedingTicks = bleedingTicks;
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
            if (this.bleedingTicks > 0) {
                target.addPotionEffect(new PotionEffect(PotionBleeding.INSTANCE, this.bleedingTicks, 0, true, true));
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

            if (this.reachBonus > 0) {
                multimap.put(EntityPlayer.REACH_DISTANCE.getName(),
                        new AttributeModifier(REACH_MODIFIER_UUID, "Reach distance modifier", this.reachBonus, 0));
            }
        }
        return multimap;
    }
}
