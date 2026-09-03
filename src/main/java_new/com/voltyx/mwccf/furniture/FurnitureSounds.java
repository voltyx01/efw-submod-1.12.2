package com.voltyx.mwccf.furniture;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class FurnitureSounds {

    public static final List<SoundEvent> SOUNDS = new ArrayList<>();

    public static SoundEvent BLOCK_CHAIR_SLIDE = register("block.chair.slide");
    public static SoundEvent BLOCK_COOLER_OPEN = register("block.cooler.open");
    public static SoundEvent BLOCK_COOLER_CLOSE = register("block.cooler.close");
    public static SoundEvent BLOCK_MICROWAVE_OPEN = register("block.microwave.open");
    public static SoundEvent BLOCK_MICROWAVE_CLOSE = register("block.microwave.close");
    public static SoundEvent BLOCK_MICROWAVE_FAN = register("block.microwave.fan");
    public static SoundEvent BLOCK_FRIDGE_OPEN = register("block.fridge.open");
    public static SoundEvent BLOCK_FRIDGE_CLOSE = register("block.fridge.close");
    public static SoundEvent BLOCK_STOVE_OPEN = register("block.stove.open");
    public static SoundEvent BLOCK_STOVE_CLOSE = register("block.stove.close");
    public static SoundEvent BLOCK_CABINET_OPEN = register("block.cabinet.open");
    public static SoundEvent BLOCK_CABINET_CLOSE = register("block.cabinet.close");
    public static SoundEvent BLOCK_LIGHTSWITCH_FLICK = register("block.lightswitch.flick");
    public static SoundEvent BLOCK_DOORBELL_CHIME = register("block.doorbell.chime");

    private static SoundEvent register(String name) {
        ResourceLocation loc = new ResourceLocation("refurbished_furniture", name);
        SoundEvent sound = new SoundEvent(loc).setRegistryName(loc);
        SOUNDS.add(sound);
        return sound;
    }

    @Mod.EventBusSubscriber(modid = "mwccf")
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
            for (SoundEvent sound : SOUNDS) {
                event.getRegistry().register(sound);
            }
        }
    }
}
