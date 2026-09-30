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
    public static SoundEvent BLOCK_CEILING_FAN_SPIN = register("block.ceiling_fan.spin");
    public static SoundEvent BLOCK_TRAMPOLINE_BOUNCE = register("block.trampoline.bounce");
    public static SoundEvent BLOCK_TRAMPOLINE_SUPER_BOUNCE = register("block.trampoline.super_bounce");
    public static SoundEvent BLOCK_STORAGE_JAR_INSERT = register("block.storage_jar.insert_item");
    public static SoundEvent BLOCK_RECYCLE_BIN_ENGINE = register("block.recycle_bin.engine");
    public static SoundEvent BLOCK_ELECTRICITY_GENERATOR_ENGINE = register("block.electricity_generator.engine");
    public static SoundEvent BLOCK_TOASTER_DOWN = register("block.toaster.down");
    public static SoundEvent BLOCK_TOASTER_POP = register("block.toaster.pop");
    public static SoundEvent BLOCK_TOASTER_INSERT = register("block.toaster.insert");
    public static SoundEvent BLOCK_CUTTING_BOARD_PLACE = register("block.cutting_board.place_ingredient");
    public static SoundEvent BLOCK_FRYING_PAN_PLACE_INGREDIENT = register("block.frying_pan.place_ingredient");
    public static SoundEvent BLOCK_FRYING_PAN_SIZZLE = register("block.frying_pan.sizzling");
    public static SoundEvent BLOCK_WORKBENCH_CRAFT = register("block.workbench.craft");
    public static SoundEvent BLOCK_KITCHEN_DRAWER_OPEN = register("block.kitchen_drawer.open");
    public static SoundEvent BLOCK_KITCHEN_DRAWER_CLOSE = register("block.kitchen_drawer.close");
    public static SoundEvent BLOCK_KITCHEN_SINK_FILL = register("block.kitchen_sink.fill");

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
