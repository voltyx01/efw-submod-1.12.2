package com.voltyx.mwccf.geo;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.voltyx.mwccf.item.ItemMorphineSyringe;
import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import ichttt.mods.firstaid.api.enums.EnumPlayerPart;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemBracelet extends Item implements IBauble {

    public ItemBracelet(String name) {
        this.setRegistryName(name);
        this.setTranslationKey("mcore." + name);
        this.setMaxStackSize(1);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (!player.world.isRemote) {
            boolean isActive = player.getEntityData().getBoolean("bracelet_active");
            NBTTagCompound tag = itemstack.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
                itemstack.setTagCompound(tag);
            }

            int charge = tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
            if (isActive && charge > 0) {
                charge--;
                tag.setInteger("battery_charge", charge);

                // Auto-injector check every 20 ticks (1 sec)
                if (player.ticksExisted % 20 == 0 && player instanceof EntityPlayer) {
                    checkAndInjectMorphine(tag, (EntityPlayer) player);
                }
            }
        }
    }

    private void checkAndInjectMorphine(NBTTagCompound tag, EntityPlayer player) {
        int morphineCount = tag.hasKey("morphine_count") ? tag.getInteger("morphine_count") : 0;
        if (morphineCount <= 0) return;

        if (Loader.isModLoaded("firstaid")) {
            try {
                AbstractPlayerDamageModel damageModel = (AbstractPlayerDamageModel) player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
                if (damageModel != null) {
                    // Check if morphine effect is already active on player
                    boolean hasMorphineActive = false;
                    for (net.minecraft.potion.PotionEffect effect : player.getActivePotionEffects()) {
                        String name = effect.getPotion().getRegistryName() != null ? effect.getPotion().getRegistryName().toString().toLowerCase() : "";
                        if (name.contains("morphine")) {
                            hasMorphineActive = true;
                            break;
                        }
                    }

                    if (!hasMorphineActive) {
                        boolean shouldInject = false;

                        // 1. Check for severe fractures/injuries
                        for (AbstractDamageablePart part : damageModel) {
                            if (part.currentHealth <= 1.0f) { // broken limb or critical injury
                                shouldInject = true;
                                break;
                            }
                        }

                        // 2. Check for critical BPM or critical health
                        float bpm = player.getEntityData().hasKey("bracelet_bpm") ? player.getEntityData().getFloat("bracelet_bpm") : 0f;
                        float healthRatio = player.getHealth() / player.getMaxHealth();
                        if (bpm >= 160f || healthRatio <= 0.35f) {
                            shouldInject = true;
                        }

                        if (shouldInject) {
                            // Inject morphine
                            tag.setInteger("morphine_count", morphineCount - 1);
                            damageModel.applyMorphine(player);
                            player.world.playSound(null, player.posX, player.posY, player.posZ,
                                    SoundEvent.REGISTRY.getObject(new ResourceLocation("efw:inject")),
                                    SoundCategory.PLAYERS, 1.0f, 1.0f);
                        }
                    }
                }
            } catch (Throwable t) {}
        }
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World worldIn, java.util.List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        NBTTagCompound tag = stack.getTagCompound();
        int charge = tag != null && tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
        int percent = (int) ((charge / 48000.0f) * 100);
        int morphineCount = tag != null && tag.hasKey("morphine_count") ? tag.getInteger("morphine_count") : 0;
        
        if (charge <= 0) {
            tooltip.add("\u00a7c" + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.required"));
        } else {
            String color = percent > 50 ? "\u00a7a" : (percent > 20 ? "\u00a7e" : "\u00a7c");
            tooltip.add(color + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.charge", percent));
        }

        String morphColor = morphineCount > 0 ? "\u00a7b" : "\u00a77";
        tooltip.add(morphColor + net.minecraft.client.resources.I18n.format("tooltip.mwccf.bracelet.morphine", morphineCount, 6));

        String keyName = BraceletInspectHandler.INSPECT_KEY.getDisplayName();
        if (BraceletInspectHandler.INSPECT_KEY.getKeyCode() == 0) {
            keyName = net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.none");
        }

        if (net.minecraft.client.gui.GuiScreen.isShiftKeyDown()) {
            String vText = net.minecraft.client.resources.I18n.format("tooltip.mwccf.bracelet.v");
            tooltip.add("\u00a77" + vText.replace("[V]", "[" + keyName.toUpperCase() + "]"));
            tooltip.add("\u00a77" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.bracelet.click"));
            tooltip.add("\u00a77" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.bracelet.enter"));
            tooltip.add("\u00a77" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.bracelet.autoinjector"));
        } else {
            tooltip.add("\u00a77" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.bracelet.desc"));
            tooltip.add("\u00a78" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.bracelet.manual"));
        }
    }
}
