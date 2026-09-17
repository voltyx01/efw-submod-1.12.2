package efw.animation.layered.modifier;

import efw.animation.layered.IAnimation;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ModifierLayer<T extends IAnimation> extends AbstractModifier {
    private final List<AbstractModifier> modifiers = new ArrayList<>();
    private T animation;

    public ModifierLayer(T animation, AbstractModifier... modifiers) {
        this.animation = animation;
        Collections.addAll(this.modifiers, modifiers);
        for (AbstractModifier modifier : modifiers) {
            modifier.setHost(this);
        }
        this.linkModifiers();
    }

    public ModifierLayer() {
        this(null);
    }

    @Override
    public void tick() {
        for (int i = 0; i < this.modifiers.size(); ++i) {
            if (!this.modifiers.get(i).canRemove()) continue;
            this.removeModifier(i--);
        }
        if (this.modifiers.size() > 0) {
            this.modifiers.get(0).tick();
        } else if (this.animation != null) {
            this.animation.tick();
        }
    }

    public void addModifier(AbstractModifier modifier, int idx) {
        modifier.setHost(this);
        this.modifiers.add(idx, modifier);
        this.linkModifiers();
    }

    public void addModifierBefore(AbstractModifier modifier) {
        this.addModifier(modifier, 0);
    }

    public void addModifierLast(AbstractModifier modifier) {
        this.addModifier(modifier, this.modifiers.size());
    }

    public void removeModifier(int idx) {
        this.modifiers.remove(idx);
        this.linkModifiers();
    }

    public void removeModifier(AbstractModifier modifier) {
        this.modifiers.remove(modifier);
        this.linkModifiers();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setAnimation(IAnimation animation) {
        this.animation = (T) animation;
        this.linkModifiers();
    }

    public void setAnimationTyped(T animation) {
        this.animation = animation;
        this.linkModifiers();
    }

    public void replaceAnimationWithFade(AbstractFadeModifier fadeModifier, T newAnimation) {
        this.replaceAnimationWithFade(fadeModifier, newAnimation, true);
    }

    public void replaceAnimationWithFade(AbstractFadeModifier fadeModifier, T newAnimation, boolean fadeFromNothing) {
        if (fadeFromNothing || (this.getAnimation() != null && this.getAnimation().isActive())) {
            fadeModifier.setBeginAnimation(this.getAnimation());
            this.addModifierLast(fadeModifier);
        }
        this.setAnimation(newAnimation);
    }

    public int size() {
        return this.modifiers.size();
    }

    protected void linkModifiers() {
        Iterator<AbstractModifier> modifierIterator = this.modifiers.iterator();
        if (modifierIterator.hasNext()) {
            AbstractModifier tmp = modifierIterator.next();
            while (modifierIterator.hasNext()) {
                AbstractModifier tmp2 = modifierIterator.next();
                tmp.setAnim(tmp2);
                tmp = tmp2;
            }
            tmp.setAnim(this.animation);
        }
    }

    @Override
    public boolean isActive() {
        if (this.modifiers.size() > 0) {
            return this.modifiers.get(0).isActive();
        }
        if (this.animation != null) {
            return this.animation.isActive();
        }
        return false;
    }

    @Override
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        if (this.modifiers.size() > 0) {
            return this.modifiers.get(0).get3DTransform(modelName, type, tickDelta, value0);
        }
        if (this.animation != null) {
            return this.animation.get3DTransform(modelName, type, tickDelta, value0);
        }
        return value0;
    }

    @Override
    public void setupAnim(float tickDelta) {
        if (this.modifiers.size() > 0) {
            this.modifiers.get(0).setupAnim(tickDelta);
        } else if (this.animation != null) {
            this.animation.setupAnim(tickDelta);
        }
    }

    @Override
    public T getAnimation() {
        return this.animation;
    }
}
