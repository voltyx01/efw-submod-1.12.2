package efw.animation.layered.modifier;

import efw.animation.layered.IAnimation;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFadeModifier extends AbstractModifier {
    protected int time = 0;
    protected int length;
    protected IAnimation beginAnimation;
    // Замороженный стартовый стейт (см. frozenBegin ниже)
    private final Map<String, Map<TransformType, Vec3f>> frozenBegin = new HashMap<>();
    // Если true — первый tick() не сдвигает time (чтобы первый рендер-кадр видел progress≈0).
    // Используется в play()/setAction(); stopAction (roll exit) НЕ использует — остаётся нетронутым.
    private boolean skipNextTick = false;

    protected AbstractFadeModifier(int length) {
        this.length = length;
    }

    @Override
    public boolean isActive() {
        if (this.time >= this.length) {
            return super.isActive();
        }
        return super.isActive() || (this.beginAnimation != null && this.beginAnimation.isActive()) || this.time < this.length;
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
        if (skipNextTick) {
            // Первый тик после play()/setAction() — не сдвигаем time,
            // чтобы следующий рендер-кадр увидел progress≈0 (полный ease-curve).
            skipNextTick = false;
        } else {
            this.time++;
        }
    }

    @Override
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        return get3DTransformWithBase(modelName, type, tickDelta, value0, value0);
    }

    public Vec3f get3DTransformWithBase(String modelName, TransformType type, float tickDelta, Vec3f vanillaVal, Vec3f baseVal) {
        if (this.calculateProgress(tickDelta) >= 1.0f) {
            if (this.animation != null) {
                if (this.animation instanceof AbstractFadeModifier) {
                    return ((AbstractFadeModifier) this.animation).get3DTransformWithBase(modelName, type, tickDelta, vanillaVal, baseVal);
                }
                return this.animation.get3DTransform(modelName, type, tickDelta, baseVal);
            }
            return baseVal;
        }
        
        Vec3f animatedVec;
        if (this.animation != null) {
            if (this.animation instanceof AbstractFadeModifier) {
                animatedVec = ((AbstractFadeModifier) this.animation).get3DTransformWithBase(modelName, type, tickDelta, vanillaVal, baseVal);
            } else {
                animatedVec = this.animation.get3DTransform(modelName, type, tickDelta, baseVal);
            }
        } else {
            animatedVec = baseVal;
        }
        
        float a = this.getAlpha(modelName, type, this.calculateProgress(tickDelta));

        Vec3f source;
        if (this.beginAnimation != null) {
            if (this.beginAnimation instanceof AbstractFadeModifier) {
                source = ((AbstractFadeModifier) this.beginAnimation).get3DTransformWithBase(modelName, type, tickDelta, vanillaVal, baseVal);
            } else {
                source = this.beginAnimation.get3DTransform(modelName, type, tickDelta, baseVal);
            }
        } else {
            source = frozenBegin
                    .computeIfAbsent(modelName, k -> new HashMap<>())
                    .computeIfAbsent(type, k -> baseVal);
        }

        if (type == TransformType.ROTATION) {
            float x = interpolateAngle(source.getX(), animatedVec.getX(), a);
            float y = interpolateAngle(source.getY(), animatedVec.getY(), a);
            float z = interpolateAngle(source.getZ(), animatedVec.getZ(), a);
            return new Vec3f(x, y, z);
        } else {
            return animatedVec.scale(a).add(source.scale(1.0f - a));
        }
    }

    private float interpolateAngle(float start, float end, float alpha) {
        float PI2 = (float) (2 * Math.PI);
        float diff = (end - start) % PI2;
        if (diff < -Math.PI) diff += PI2;
        if (diff > Math.PI) diff -= PI2;
        return start + diff * alpha;
    }

    public float calculateProgress(float tickDelta) {
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

    /**
     * То же что {@link #standardFadeIn}, но первый тик() не сдвигает время.
     * Используется в play()/setAction() чтобы первый рендер-кадр видел progress≈0,
     * а не 1/blendTicks (что происходит когда tick() вызывается сразу после play()).
     * stopAction() (выход из кувырка) использует обычный {@link #standardFadeIn} — не затронут.
     */
    public static AbstractFadeModifier standardFadeInDelayed(int length, final EasingFunction ease) {
        AbstractFadeModifier mod = new AbstractFadeModifier(length) {
            @Override
            public float getAlpha(String modelName, TransformType type, float progress) {
                return ease.invoke(progress);
            }
        };
        mod.skipNextTick = true;
        return mod;
    }

    @FunctionalInterface
    public interface EasingFunction {
        float invoke(float progress);
    }
}
