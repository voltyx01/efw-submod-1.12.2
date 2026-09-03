package efw.sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "mwccf")
public class OtbwgSounds {

    public static final List<SoundEvent> SOUNDS = new ArrayList<>();

    public static final SoundEvent AMBIENT_SOUL_SAND_VALLEY_LOOP = createSound("ambient.soul_sand_valley.loop");

    private static SoundEvent createSound(String name) {
        ResourceLocation loc = new ResourceLocation("mwccf", name);
        SoundEvent sound = new SoundEvent(loc).setRegistryName(loc);
        SOUNDS.add(sound);
        return sound;
    }

    @SubscribeEvent
    public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
        for (SoundEvent sound : SOUNDS) {
            event.getRegistry().register(sound);
        }
    }
}
