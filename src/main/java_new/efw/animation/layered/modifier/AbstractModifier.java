package efw.animation.layered.modifier;

import efw.animation.layered.IAnimation;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;

public abstract class AbstractModifier implements IAnimation {
    protected IAnimation animation;

    public void setAnimation(IAnimation animation) {
        this.animation = animation;
    }

    public IAnimation getAnimation() {
        return this.animation;
    }

    @Override
    public void tick() {
        if (this.animation != null) {
            this.animation.tick();
        }
    }

    @Override
    public void setupAnim(float tickDelta) {
        if (this.animation != null) {
            this.animation.setupAnim(tickDelta);
        }
    }

    @Override
    public boolean isActive() {
        return this.animation != null && this.animation.isActive();
    }

    @Override
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        if (this.animation != null) {
            return this.animation.get3DTransform(modelName, type, tickDelta, value0);
        }
        return value0;
    }

    public boolean canRemove() {
        return false;
    }
}
