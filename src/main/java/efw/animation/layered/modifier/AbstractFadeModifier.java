package efw.animation.layered.modifier;

import efw.animation.layered.IAnimation;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;

public abstract class AbstractFadeModifier extends AbstractModifier {
    protected int time = 0;
    protected int length;
    protected IAnimation beginAnimation;

    protected AbstractFadeModifier(int length) {
        this.length = length;
    }

    @Override
    public boolean isActive() {
        if (!canRemove()) {
            return true;
        }
        return super.isActive() || (this.beginAnimation != null && this.beginAnimation.isActive());
    }

    @Override
    public boolean canRemove() {
        return this.length <= this.time;
    }

    @Override
    public void setupAnim(float tickDelta) {
        super.setupAnim(tickDelta);
        if (this.beginAnimation != null) {
            this.beginAnimation.setupAnim(tickDelta);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.beginAnimation != null) {
            this.beginAnimation.tick();
        }
        ++this.time;
    }

    @Override
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        if (this.calculateProgress(tickDelta) >= 1.0f) {
            return super.get3DTransform(modelName, type, tickDelta, value0);
        }
        Vec3f animatedVec = super.get3DTransform(modelName, type, tickDelta, value0);
        float a = this.getAlpha(modelName, type, this.calculateProgress(tickDelta));
        Vec3f source = (this.beginAnimation != null)
                ? this.beginAnimation.get3DTransform(modelName, type, tickDelta, value0)
                : value0;

        if (type == TransformType.ROTATION) {
            float x = shortestAngleLerp(source.getX(), animatedVec.getX(), a);
            float y = shortestAngleLerp(source.getY(), animatedVec.getY(), a);
            float z = shortestAngleLerp(source.getZ(), animatedVec.getZ(), a);
            return new Vec3f(x, y, z);
        }

        return animatedVec.scale(a).add(source.scale(1.0f - a));
    }

    private static float shortestAngleLerp(float from, float to, float alpha) {
        float diff = (to - from) % ((float) Math.PI * 2F);
        if (diff < -(float) Math.PI) diff += ((float) Math.PI * 2F);
        if (diff >= (float) Math.PI) diff -= ((float) Math.PI * 2F);
        return from + diff * alpha;
    }

    public float calculateProgress(float tickDelta) {
        if (this.length <= 0) {
            return 1.0f;
        }
        float actualTime = (float) this.time + tickDelta;
        return actualTime / (float) this.length;
    }

    public abstract float getAlpha(String modelName, TransformType type, float progress);

    public void setBeginAnimation(IAnimation beginAnimation) {
        this.beginAnimation = beginAnimation;
    }

    public IAnimation getBeginAnimation() {
        return this.beginAnimation;
    }

    public static AbstractFadeModifier standardFadeIn(int length, final EasingFunction ease) {
        return new AbstractFadeModifier(length) {
            @Override
            public float getAlpha(String modelName, TransformType type, float progress) {
                return ease.invoke(progress);
            }
        };
    }

    public static AbstractFadeModifier standardFadeInDelayed(int length, final EasingFunction ease) {
        return standardFadeIn(length, ease);
    }

    @FunctionalInterface
    public interface EasingFunction {
        float invoke(float progress);
    }
}