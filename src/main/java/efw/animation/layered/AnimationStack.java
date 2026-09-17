package efw.animation.layered;

import efw.animation.layered.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class AnimationStack implements IAnimation {
    public static class Entry {
        public final int priority;
        public final IAnimation layer;

        public Entry(int priority, IAnimation layer) {
            this.priority = priority;
            this.layer = layer;
        }
    }

    private final List<Entry> layers = new ArrayList<>();

    public void addAnimLayer(int priority, IAnimation layer) {
        int search;
        for (search = 0; search < this.layers.size() && this.layers.get(search).priority <= priority; ++search) {
        }
        this.layers.add(search, new Entry(priority, layer));
    }

    public void addLayer(int index, IAnimation layer) {
        addAnimLayer(index * 100, layer);
    }

    public void addLayer(IAnimation layer) {
        addAnimLayer(0, layer);
    }

    public void removeLayer(int index) {
        if (index >= 0 && index < this.layers.size()) {
            this.layers.remove(index);
        }
    }

    public void removeLayer(IAnimation layer) {
        this.layers.removeIf(entry -> entry.layer == layer);
    }

    @Override
    public void tick() {
        for (int i = 0; i < layers.size(); i++) {
            Entry entry = layers.get(i);
            if (entry.layer.isActive()) {
                entry.layer.tick();
            }
        }
    }

    @Override
    public void setupAnim(float tickDelta) {
        for (Entry entry : layers) {
            entry.layer.setupAnim(tickDelta);
        }
    }

    @Override
    public boolean isActive() {
        for (Entry entry : layers) {
            if (entry.layer.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        Vec3f current = value0;
        for (Entry entry : layers) {
            if (entry.layer.isActive()) {
                current = entry.layer.get3DTransform(modelName, type, tickDelta, current);
            }
        }
        return current;
    }
}
