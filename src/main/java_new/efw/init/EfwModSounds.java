package efw.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EfwModSounds {
    public static SoundEvent ITEMSOUND;
    public static SoundEvent NOTES;
    public static SoundEvent DIARYOPEN;
    public static SoundEvent BANDAGE;
    public static SoundEvent MED;

    public static void register() {
        ITEMSOUND  = registerEfw("itemsound");
        NOTES      = registerEfw("notes");
        DIARYOPEN  = registerEfw("diaryopen");
        BANDAGE    = registerMwccf("bandage");
        MED        = registerMwccf("med");
    }

    private static SoundEvent registerEfw(String name) {
        ResourceLocation loc = new ResourceLocation("efw", name);
        SoundEvent event = new SoundEvent(loc).setRegistryName(loc);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }

    private static SoundEvent registerMwccf(String name) {
        ResourceLocation loc = new ResourceLocation("mwccf", name);
        SoundEvent event = new SoundEvent(loc).setRegistryName(loc);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
