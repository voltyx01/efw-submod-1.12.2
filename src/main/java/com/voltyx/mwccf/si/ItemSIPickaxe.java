package com.voltyx.mwccf.si;

import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ItemSIPickaxe extends ItemPickaxe {
    private final float attackDamage;
    private final float attackSpeed;
    private final boolean showScrapTooltip;
    private final SoundEvent hitSound;
    private final Supplier<Item> repairItemSupplier;

    public ItemSIPickaxe(String name, ToolMaterial material, int maxDamage, float attackDamage, float attackSpeed,
                         boolean showScrapTooltip, SoundEvent hitSound, Supplier<Item> repairItemSupplier) {
        super(material);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.showScrapTooltip = showScrapTooltip;
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
        if (!target.world.isRemote && this.hitSound != null) {
            target.world.playSound(null, target.posX, target.posY, target.posZ, this.hitSound, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemstack) {
        ItemStack retval = itemstack.copy();
        retval.setItemDamage(itemstack.getItemDamage() + 1);
        if (retval.getItemDamage() >= retval.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return retval;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (this.showScrapTooltip) {
            tooltip.add("\u00a77Tool Utility:");
            tooltip.add(" \u00a79You can break tools and items to obtain useful scrap");
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
