package efw.animation.layered;

import efw.animation.layered.math.Vec3f;

public interface IAnimation {
    void tick();
    void setupAnim(float tickDelta);
    boolean isActive();
    Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0);
}
