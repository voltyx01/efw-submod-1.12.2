package com.voltyx.mwccf.si;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBleeding extends Potion {
    public static final PotionBleeding INSTANCE = new PotionBleeding();
    private static final ResourceLocation POTION_ICON = new ResourceLocation("mwccf:textures/mob_effect/bleeding.png");

    private PotionBleeding() {
        super(true, 0x8B0000);
        setRegistryName("mwccf", "bleeding");
        setPotionName("effect.mwccf.bleeding");
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
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (entity.isPotionActive(MobEffects.REGENERATION)) {
            entity.removePotionEffect(MobEffects.REGENERATION);
        }
        if (!entity.world.isRemote && entity.ticksExisted % 30 == 0) {
            entity.attackEntityFrom(DamageSource.GENERIC, 1.0F);
        }
    }
}
