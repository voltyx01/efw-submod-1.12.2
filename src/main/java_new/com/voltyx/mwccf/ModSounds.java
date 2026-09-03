package com.voltyx.mwccf;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// Аннотация автоматически зарегистрирует этот класс в шине событий для регистрации
@Mod.EventBusSubscriber(modid = "mwccf")
public class ModSounds {

    public static SoundEvent HEAD_HIT;
    public static SoundEvent FLESH_HIT;
    public static SoundEvent KILL;
    public static SoundEvent LOOTPROG;
    public static SoundEvent DASH;

    // Регистрация звуков
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        HEAD_HIT = createSound("fire.head_hit");
        FLESH_HIT = createSound("fire.flesh_hit");
        KILL = createSound("fire.kill");
        LOOTPROG = createSound("loot.lootprog");
        DASH = createSound("dash");
        event.getRegistry().registerAll(HEAD_HIT, FLESH_HIT, KILL, LOOTPROG, DASH);
    }

    // Вспомогательный метод для удобного создания звуков
    private static SoundEvent createSound(String name) {
        ResourceLocation location = new ResourceLocation("mwccf", name);
        SoundEvent sound = new SoundEvent(location);
        sound.setRegistryName(location);
        return sound;
    }
}