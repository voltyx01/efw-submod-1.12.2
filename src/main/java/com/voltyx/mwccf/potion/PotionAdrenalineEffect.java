package com.voltyx.mwccf.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

public class PotionAdrenalineEffect extends Potion {
    public static final PotionAdrenalineEffect INSTANCE = new PotionAdrenalineEffect();
    private static final ResourceLocation POTION_ICON = new ResourceLocation("mwccf:textures/mob_effect/adrenaline_effect.png");

    private PotionAdrenalineEffect() {
        super(false, -3355648);
        setRegistryName("mwccf", "adrenaline_effect");
        setPotionName("effect.mwccf.adrenaline_effect");
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return true;
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        if (mc.currentScreen != null) {
            mc.getTextureManager().bindTexture(POTION_ICON);
            Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(POTION_ICON);
        Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
        super.applyAttributesModifiersToEntity(entity, attributeMapIn, amplifier);
        if (!entity.world.isRemote) {
            PotionEffect speed = new PotionEffect(MobEffects.SPEED, 1200, 0, false, false);
            speed.setCurativeItems(Collections.emptyList());
            PotionEffect strength = new PotionEffect(MobEffects.STRENGTH, 1200, 0, false, false);
            PotionEffect haste = new PotionEffect(MobEffects.HASTE, 1200, 0, false, false);
            
            entity.addPotionEffect(speed);
            entity.addPotionEffect(strength);
            entity.addPotionEffect(haste);
        }
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (entity.isPotionActive(MobEffects.SLOWNESS)) {
            entity.removePotionEffect(MobEffects.SLOWNESS);
        }
        if (entity.isPotionActive(MobEffects.MINING_FATIGUE)) {
            entity.removePotionEffect(MobEffects.MINING_FATIGUE);
        }
        if (entity.isPotionActive(MobEffects.WEAKNESS)) {
            entity.removePotionEffect(MobEffects.WEAKNESS);
        }
    }
}
