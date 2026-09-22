package com.voltyx.mwccf.si;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class SISounds {
    public static final List<SoundEvent> SOUNDS = new ArrayList<>();

    public static final SoundEvent CHAINSAW_SWING = createSound("chainsaw_swing");
    public static final SoundEvent CIRCULARSAW_SWING = createSound("circularsaw_swing");
    public static final SoundEvent CROWBAR_SWING = createSound("crowbar_swing");
    public static final SoundEvent CROWBAR_SLAM = createSound("crowbar_slam");
    public static final SoundEvent PIPE_SLAM = createSound("pipe_slam");
    public static final SoundEvent ELECTRIC_GUITAR_SWING = createSound("electric_guitar_swing");
    public static final SoundEvent ELECTRIC_GUITAR_SMASH = createSound("electric_guitar_smash");
    public static final SoundEvent ELECTRIC_FIST_01 = createSound("electric_fist_01");
    public static final SoundEvent NAILGUN_SHOOT = createSound("nailgun_shoot");
    public static final SoundEvent BEAR_TRAP_CLOSE = createSound("bear_trap_close");

    private static SoundEvent createSound(String name) {
        ResourceLocation loc = new ResourceLocation("mwccf", name);
        SoundEvent sound = new SoundEvent(loc).setRegistryName(loc);
        SOUNDS.add(sound);
        return sound;
    }

    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        for (SoundEvent s : SOUNDS) {
            event.getRegistry().register(s);
        }
    }
}
