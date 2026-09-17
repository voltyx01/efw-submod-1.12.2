package efw.animation.layered.modifier;

import efw.animation.layered.IAnimation;
import efw.animation.layered.KeyframeAnimationPlayer;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class AdjustmentModifier extends AbstractModifier {
    public boolean enabled = true;
    protected Function<String, Optional<PartModifier>> source;
    protected int instructedFadeout = 0;
    private int remainingFadeout = 0;

    public AdjustmentModifier(Function<String, Optional<PartModifier>> source) {
        this.source = source;
    }

    public void setSource(Function<String, Optional<PartModifier>> source) {
        this.source = source;
    }

    public Function<String, Optional<PartModifier>> getSource() {
        return this.source;
    }

    protected float getFadeIn(float delta) {
        return 1.0f;
    }

    @Override
    public boolean isActive() {
        return this.enabled && (this.animation == null || this.animation.isActive());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.remainingFadeout > 0) {
            --this.remainingFadeout;
            if (this.remainingFadeout <= 0) {
                this.instructedFadeout = 0;
            }
        }
    }

    public void fadeOut(int fadeOut) {
        this.instructedFadeout = fadeOut;
        this.remainingFadeout = fadeOut + 1;
    }

    protected float getFadeOut(float delta) {
        if (this.remainingFadeout > 0 && this.instructedFadeout > 0) {
            float current = Math.max((float) this.remainingFadeout - delta, 0.0f);
            float fadeOut = current / (float) this.instructedFadeout;
            return Math.min(fadeOut, 1.0f);
        }
        return 1.0f;
    }

    @Override
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        if (!this.enabled || this.source == null) {
            return super.get3DTransform(modelName, type, tickDelta, value0);
        }
        Optional<PartModifier> partModifier = this.source.apply(modelName);
        float fade = this.getFadeIn(tickDelta) * this.getFadeOut(tickDelta);
        if (partModifier != null && partModifier.isPresent()) {
            Vec3f modifiedVector = super.get3DTransform(modelName, type, tickDelta, value0);
            return this.transformVector(modifiedVector, type, partModifier.get(), fade);
        }
        return super.get3DTransform(modelName, type, tickDelta, value0);
    }

    protected Vec3f transformVector(Vec3f vector, TransformType type, PartModifier partModifier, float fade) {
        switch (type) {
            case POSITION:
                return vector.add(partModifier.offset().scale(fade));
            case ROTATION:
                return vector.add(partModifier.rotation().scale(fade));
            default:
                return vector;
        }
    }

    public static final class PartModifier {
        private final Vec3f rotation;
        private final Vec3f offset;

        public PartModifier(Vec3f rotation, Vec3f offset) {
            this.rotation = rotation != null ? rotation : Vec3f.ZERO;
            this.offset = offset != null ? offset : Vec3f.ZERO;
        }

        public Vec3f rotation() {
            return this.rotation;
        }

        public Vec3f offset() {
            return this.offset;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            PartModifier that = (PartModifier) obj;
            return Objects.equals(this.rotation, that.rotation) && Objects.equals(this.offset, that.offset);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.rotation, this.offset);
        }

        @Override
        public String toString() {
            return "PartModifier[rotation=" + this.rotation + ", offset=" + this.offset + ']';
        }
    }
}
