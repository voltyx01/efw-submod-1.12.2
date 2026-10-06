package com.voltyx.mwccf.si;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class ItemSIAxe extends ItemAxe {
    private final float attackDamage;
    private final float attackSpeed;
    private final int bleedingTicks;
    private final SoundEvent hitSound;
    private final Supplier<Item> repairItemSupplier;

    public ItemSIAxe(String name, ToolMaterial material, int maxDamage, float attackDamage, float attackSpeed,
                     int bleedingTicks, SoundEvent hitSound, Supplier<Item> repairItemSupplier) {
        super(material, attackDamage, attackSpeed - 4.0F);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
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
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, net.minecraft.world.World worldIn, java.util.List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (this.bleedingTicks > 0) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.hit_effect"));
            tooltip.add(" \u00a79" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.effect_bleeding"));
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (this.repairItemSupplier != null && this.repairItemSupplier.get() != null) {
            return repair.getItem() == this.repairItemSupplier.get();
        }
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
        if (enchantment == net.minecraft.init.Enchantments.SWEEPING) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
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
