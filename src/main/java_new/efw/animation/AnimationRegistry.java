package efw.animation;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;



public class AnimationRegistry {

    // All loaded clips: animName -> clip
    private static final Map<String, AnimationClip> clips = new HashMap<>();

    // Per-player animation players
    private static final Map<EntityPlayer, AnimationPlayer> players = new WeakHashMap<>();

    // Load a JSON file from resources, e.g. "mwccf:animations/walking.json"
    // Load a JSON file from resources, e.g. "mwccf:animations/walking.json"
    public static void loadFromResource(IResourceManager rm, String path, String prefix) {
        try {
            System.out.println("[DEBUG] Attempting to load: " + path);
            ResourceLocation loc = new ResourceLocation(path);
            IResource res = rm.getResource(loc);

            Map<String, AnimationClip> loaded = AnimationParser.parse(res.getInputStream(), prefix);

            if (loaded == null || loaded.isEmpty()) {
                System.err.println("[ERROR] Parser returned empty for: " + path);
            } else {
                clips.putAll(loaded);
                System.out.println("[SUCCESS] Registered " + loaded.size() + " clips from: " + path);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to load resource: " + path);
            e.printStackTrace(); // This is crucial to see if it's a FileNotFound or a ParseError
        }
    }

    // Register a clip directly (e.g. loaded manually)
    public static void register(AnimationClip clip) {
        clips.put(clip.name, clip);
    }

    public static AnimationClip getClip(String name) {
        return clips.get(name);
    }

    public static AnimationPlayer getPlayer(EntityPlayer player) {
        return players.computeIfAbsent(player, k -> new AnimationPlayer());
    }

    public static void clearPlayer(EntityPlayer player) {
        players.remove(player);
    }
}
