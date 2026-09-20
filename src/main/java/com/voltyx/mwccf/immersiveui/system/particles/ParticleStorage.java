package com.voltyx.mwccf.immersiveui.system.particles;

import com.voltyx.mwccf.immersiveui.system.particles.data.ParticleData;
import com.voltyx.mwccf.immersiveui.system.particles.data.ParticleEmitter;

import java.util.*;

public class ParticleStorage {
    public static final Map<ParticleEmitter, List<ParticleData>> EMITTERS = new HashMap<>();

    public static List<ParticleData> getParticlesData() {
        List<ParticleData> all = new ArrayList<>();
        for (List<ParticleData> list : EMITTERS.values()) {
            all.addAll(list);
        }
        return all;
    }

    public static void addParticle(ParticleEmitter emitter, ParticleData... data) {
        List<ParticleData> list = EMITTERS.get(emitter);
        if (list == null) {
            list = new ArrayList<>();
            EMITTERS.put(emitter, list);
        }
        Collections.addAll(list, data);
    }

    public static void tickAll() {
        Set<ParticleEmitter> toRemoveSet = new HashSet<>();

        for (Map.Entry<ParticleEmitter, List<ParticleData>> entry : EMITTERS.entrySet()) {
            ParticleEmitter emitter = entry.getKey();
            List<ParticleData> particles = entry.getValue();
            List<ParticleData> toRemove = new ArrayList<>();

            for (ParticleData data : particles) {
                data.tick();
                if (data.getLifetime() <= 0) {
                    toRemove.add(data);
                }
            }
            particles.removeAll(toRemove);

            if (particles.isEmpty()) {
                toRemoveSet.add(emitter);
            }
        }

        for (ParticleEmitter emitter : toRemoveSet) {
            EMITTERS.remove(emitter);
        }
    }

    public static void renderAll(float partialTick) {
        for (ParticleData data : getParticlesData()) {
            data.render(partialTick);
        }
    }

    public static void clear() {
        EMITTERS.clear();
    }
}
