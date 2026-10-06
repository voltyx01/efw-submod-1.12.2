package com.voltyx.mwccf.furniture.client.audio;

import com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.WeakHashMap;

@SideOnly(Side.CLIENT)
public class GeneratorSoundManager {

    private static final Map<TileEntityElectricityGenerator, GeneratorSound> ACTIVE_SOUNDS = new WeakHashMap<>();

    public static void updateGenerator(TileEntityElectricityGenerator generator) {
        if (generator == null || generator.isInvalid()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getSoundHandler() == null) return;

        if (generator.isGeneratingPower()) {
            GeneratorSound sound = ACTIVE_SOUNDS.get(generator);
            if (sound == null || sound.isDonePlaying() || !mc.getSoundHandler().isSoundPlaying(sound)) {
                sound = new GeneratorSound(generator);
                ACTIVE_SOUNDS.put(generator, sound);
                mc.getSoundHandler().playSound(sound);
            }
        } else {
            GeneratorSound sound = ACTIVE_SOUNDS.remove(generator);
            if (sound != null) {
                mc.getSoundHandler().stopSound(sound);
            }
        }
    }

    public static void stopGenerator(TileEntityElectricityGenerator generator) {
        if (generator == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getSoundHandler() == null) return;

        GeneratorSound sound = ACTIVE_SOUNDS.remove(generator);
        if (sound != null) {
            mc.getSoundHandler().stopSound(sound);
        }
    }
}
