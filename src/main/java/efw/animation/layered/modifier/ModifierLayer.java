package efw.animation.layered.modifier;

import efw.animation.layered.IAnimation;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;

public class ModifierLayer<T extends IAnimation> extends AbstractModifier {
    private T animation;

    public ModifierLayer(T animation) {
        this.animation = animation;
        super.setAnimation(animation);
    }

    public ModifierLayer() {
        this(null);
    }

    @Override
    public void setAnimation(IAnimation animation) {
        this.animation = (T) animation;
        super.setAnimation(animation);
    }

    @Override
    public T getAnimation() {
        return this.animation;
    }
}
