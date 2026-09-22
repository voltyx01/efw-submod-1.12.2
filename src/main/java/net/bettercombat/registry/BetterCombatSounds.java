package net.bettercombat.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "mwccf")
public class BetterCombatSounds {
    private static final Map<String, SoundEvent> SOUND_MAP = new HashMap<>();

    public static final String[] SOUND_NAMES = {
            "anchor_slam",
            "axe_slash",
            "claymore_swing",
            "claymore_stab",
            "claymore_slam",
            "dagger_slash",
            "double_axe_swing",
            "fist_punch",
            "glaive_slash_quick",
            "glaive_slash_slow",
            "hammer_slam",
            "katana_slash",
            "mace_slam",
            "mace_slash",
            "pickaxe_swing",
            "rapier_slash",
            "rapier_stab",
            "scythe_slash",
            "spear_stab",
            "staff_slam",
            "staff_slash",
            "staff_spin",
            "staff_stab",
            "sword_slash",
            "wand_swing"
    };

    static {
        for (String name : SOUND_NAMES) {
            ResourceLocation loc = new ResourceLocation("bettercombat", name);
            SoundEvent event = new SoundEvent(loc).setRegistryName(loc);
            SOUND_MAP.put(loc.toString(), event);
            SOUND_MAP.put(name, event);
        }
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        for (String name : SOUND_NAMES) {
            ResourceLocation loc = new ResourceLocation("bettercombat", name);
            SoundEvent sound = SOUND_MAP.get(name);
            if (sound != null) {
                event.getRegistry().register(sound);
            }
        }
        System.out.println("[BetterCombat] Registered " + SOUND_NAMES.length + " sound events.");
    }

    public static SoundEvent getSound(String soundId) {
        if (soundId == null) return null;
        SoundEvent event = SOUND_MAP.get(soundId);
        if (event == null && !soundId.contains(":")) {
            event = SOUND_MAP.get("bettercombat:" + soundId);
        }
        return event;
    }
}
