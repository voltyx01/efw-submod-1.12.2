package com.voltyx.mwccf.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionSayaBuff extends Potion {
    public static final PotionSayaBuff INSTANCE = new PotionSayaBuff();

    private PotionSayaBuff() {
        super(false, 0xCC1133); // Crimson red
        setRegistryName("mwccf", "saya_buff");
        setPotionName("effect.mwccf.saya_buff");
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return true;
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
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
