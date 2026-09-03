package efw.animation.layered;

import efw.animation.layered.modifier.ModifierLayer;
import efw.animation.layered.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class AnimationStack implements IAnimation {
    private final List<ModifierLayer<?>> layers = new ArrayList<>();

    public void addLayer(int index, ModifierLayer<?> layer) {
        this.layers.add(index, layer);
    }

    public void addLayer(ModifierLayer<?> layer) {
        this.layers.add(layer);
    }

    public void removeLayer(int index) {
        this.layers.remove(index);
    }

    public void removeLayer(ModifierLayer<?> layer) {
        this.layers.remove(layer);
    }

    @Override
    public void tick() {
        for (int i = 0; i < layers.size(); i++) {
            ModifierLayer<?> layer = layers.get(i);
            if (layer.canRemove()) {
                layers.remove(i);
                i--;
            } else {
                layer.tick();
            }
        }
    }

    @Override
    public void setupAnim(float tickDelta) {
        for (ModifierLayer<?> layer : layers) {
            layer.setupAnim(tickDelta);
        }
    }

    @Override
    public boolean isActive() {
        for (ModifierLayer<?> layer : layers) {
            if (layer.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        Vec3f current = value0;
        for (ModifierLayer<?> layer : layers) {
            if (layer.isActive()) {
                current = layer.get3DTransform(modelName, type, tickDelta, current);
            }
        }
        return current;
    }
}
