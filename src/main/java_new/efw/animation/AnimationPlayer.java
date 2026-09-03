package efw.animation;

import efw.animation.layered.AnimationStack;
import efw.animation.layered.KeyframeAnimationPlayer;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;
import efw.animation.layered.modifier.AbstractFadeModifier;
import efw.animation.layered.math.Ease;
import efw.animation.layered.modifier.ModifierLayer;
import efw.animation.layered.IAnimation;

public class AnimationPlayer {

    private final AnimationStack stack;
    private final ModifierLayer<IAnimation> baseLayer;
    private final ModifierLayer<IAnimation> actionLayer;

    // Helper states for API compatibility
    private AnimationClip currentClip;
    private AnimationClip actionClip;
    private AnimationClip previousActionClip;
    private boolean playing = false;
    private boolean actionFadingOut = false;
    private float actionSpeed = 1.0f;
    private float lastSpeedMult = 1.0f;
    private float fadeWeight = 0f; // Mocked for compatibility
    public float rollYawOffset = 0.0f;
    public float rollFade = 0.0f;
    public boolean isHoldingWeapon = false;

    // Сглаженный вес прицеливания рук для оружия — плавная смена между аимом/холдом и перезарядкой/бегом.
    private float prevArmPitchWeight = 0.0f;
    private float armPitchWeight = 0.0f;
    private float targetArmPitchWeight = 0.0f;
    public AnimationClip lastWeaponClip = null;

    public AnimationPlayer() {
        this.stack = new AnimationStack();
        this.baseLayer = new ModifierLayer<>();
        this.actionLayer = new ModifierLayer<>();

        this.stack.addLayer(0, this.baseLayer);
        this.stack.addLayer(1, this.actionLayer);
    }

    public AnimationStack getStack() {
        return stack;
    }

    public void setAction(AnimationClip clip, float time, float speed) {
        if (clip == null) {
            stopAction();
            return;
        }

        if (clip.name != null && (clip.name.startsWith("pistol_") || clip.name.startsWith("rifle_")) && clip.name.endsWith("_upper")) {
            this.lastWeaponClip = clip;
        }

        if (this.actionClip != null && this.actionClip != clip) {
            this.previousActionClip = this.actionClip;
        }
        this.actionClip = clip;
        this.actionSpeed = speed;
        this.actionFadingOut = false;

        KeyframeAnimationPlayer player = new KeyframeAnimationPlayer(clip);
        player.setSpeed(speed);

        IAnimation oldAnim = this.actionLayer.getAnimation();
        int blendTicks = getBlendTicks(clip.name);
        AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeInDelayed(blendTicks, Ease::inOutSine);
        fadeModifier.setAnimation(player);
        if (oldAnim != null) {
            fadeModifier.setBeginAnimation(oldAnim);
        }
        this.actionLayer.setAnimation(fadeModifier);
    }

    public void stopAction(int blendTicks) {
        if (this.actionFadingOut) {
            return;
        }
        IAnimation oldAnim = this.actionLayer.getAnimation();
        if (oldAnim != null) {
            AbstractFadeModifier fadeOut = AbstractFadeModifier.standardFadeIn(blendTicks, Ease::inOutSine);
            fadeOut.setBeginAnimation(oldAnim);
            this.actionLayer.setAnimation(fadeOut);
            this.actionFadingOut = true;
        } else {
            this.actionLayer.setAnimation(null);
            this.actionClip = null;
            this.actionFadingOut = false;
        }
    }

    public void stopAction() {
        int ticks = 6;
        if (this.actionClip != null) {
            ticks = getBlendTicks(this.actionClip.name);
        }
        stopAction(ticks);
    }

    public void snapAction() {
        this.actionClip = null;
        this.actionLayer.setAnimation(null);
        this.actionFadingOut = false;
    }

    public void cancelAction() {
        stopAction();
    }

    public void resumeActionLoop() {
        // Handled internally
    }

    public void snap() {
        this.playing = false;
        this.currentClip = null;
        this.baseLayer.setAnimation(null);
    }

    public void play(AnimationClip clip) {
        play(clip, 1.0f);
    }

    public void play(AnimationClip clip, float speed) {
        if (clip == currentClip && playing)
            return;

        this.currentClip = clip;
        this.playing = true;

        KeyframeAnimationPlayer player = new KeyframeAnimationPlayer(clip);
        player.setSpeed(speed);

        int blendTicks = getBlendTicks(clip.name);
        // standardFadeInDelayed: первый tick() не сдвигает time,
        // чтобы первый рендер-кадр видел progress≈0 (полный ease-curve).
        AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeInDelayed(blendTicks, Ease::inOutSine);
        fadeModifier.setAnimation(player);

        IAnimation oldAnim = this.baseLayer.getAnimation();
        if (oldAnim != null) {
            fadeModifier.setBeginAnimation(oldAnim);
        }

        this.baseLayer.setAnimation(fadeModifier);
    }

    public void stop() {
        this.playing = false;
        // To fade out base layer:
        IAnimation oldAnim = this.baseLayer.getAnimation();
        if (oldAnim != null && oldAnim.isActive()) {
            AbstractFadeModifier fadeOut = AbstractFadeModifier.standardFadeIn(10, Ease::inOutSine);
            fadeOut.setBeginAnimation(oldAnim);
            // No inner animation, so it fades to nothing
            this.baseLayer.setAnimation(fadeOut);
        }
    }

    public void tick(float speedMult) {
        this.lastSpeedMult = speedMult;
        
        // Propagate speed multiplier to the KeyframeAnimationPlayers
        if (this.baseLayer.getAnimation() instanceof AbstractFadeModifier) {
            AbstractFadeModifier fm = (AbstractFadeModifier) this.baseLayer.getAnimation();
            if (fm.getAnimation() instanceof KeyframeAnimationPlayer) {
                ((KeyframeAnimationPlayer) fm.getAnimation()).setSpeed(speedMult);
            }
        } else if (this.baseLayer.getAnimation() instanceof KeyframeAnimationPlayer) {
            ((KeyframeAnimationPlayer) this.baseLayer.getAnimation()).setSpeed(speedMult);
        }

        this.stack.tick();

        this.prevArmPitchWeight = this.armPitchWeight;
        this.armPitchWeight += (this.targetArmPitchWeight - this.armPitchWeight) * 0.25f;

        if (this.actionLayer.getAnimation() != null && this.actionFadingOut && !this.actionLayer.getAnimation().isActive()) {
            this.actionLayer.setAnimation(null);
            this.actionClip = null;
            this.actionFadingOut = false;
        }
    }


    private int getBlendTicks(String animName) {
        if (animName != null && animName.contains("lie_reload")) {
            return 10;  // Оружие лежа: плавный бленд перезарядки
        }
        if (animName != null && (animName.contains("pistol") || animName.contains("rifle"))) {
            return 10;  // Оружие: плавный вход в холд-анимации
        }
        if ("roll".equals(animName)) {
            return 3;   // Кувырок: не меняем — выход всё равно stopAction(12)
        }
        if ("idle_standing".equals(animName) || animName != null && animName.startsWith("idle_")) {
            return 10;  // Идл: плавный переход
        }
        if (animName != null && (animName.startsWith("walking") || animName.equals("running"))) {
            return 8;   // Ходьба / бег
        }
        if (animName != null && (animName.equals("axe") || animName.equals("pickaxe")
                || animName.equals("shovel") || animName.equals("hoe"))) {
            return 3;   // Инструменты: быстро, но с лёгким плавным стартом
        }
        if (animName != null && (animName.contains("sword_attack") || animName.contains("fist_attack"))) {
            return 4;   // Удар: было 1 (мгновенный снап), теперь видный вход
        }
        return 6;   // Остальное (прежне 4)
    }

    // New methods for layered integration
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        Vec3f current = value0;
        
        if (baseLayer.getAnimation() != null) {
            current = baseLayer.get3DTransform(modelName, type, tickDelta, value0);
        }
        
        if (actionLayer.getAnimation() != null) {
            boolean isArm = "rightArm".equals(modelName) || "leftArm".equals(modelName) || "right_arm".equals(modelName) || "left_arm".equals(modelName);
            boolean isLeg = "rightLeg".equals(modelName) || "leftLeg".equals(modelName) || "right_leg".equals(modelName) || "left_leg".equals(modelName);
            String actionName = getCurrentActionName();
            if (actionName == null) {
                actionName = getFadeActionName();
            }
            boolean isRoll = actionName != null && actionName.equals("roll");
            boolean isFullBodyAction = actionName != null && (isRoll || actionName.endsWith("_lower") || actionName.contains("lie"));
            
            if (isRoll && isArm && isHoldingWeapon) {
                // Keep the exact weapon holding pose for the arms.
                // Global roll somersault rotation is already applied to the entire player model in MixinRenderPlayer.
                AnimationClip wClip = this.lastWeaponClip;
                if (wClip == null) {
                    wClip = efw.animation.AnimationRegistry.getClip("rifle_hold_upper");
                    if (wClip == null) wClip = efw.animation.AnimationRegistry.getClip("pistol_hold_upper");
                }
                
                if (wClip != null) {
                    KeyframeAnimationPlayer mock = new KeyframeAnimationPlayer(wClip);
                    current = mock.get3DTransform(modelName, type, 0.0f, value0);
                }
            } else if (!isLeg || isFullBodyAction) {
                IAnimation actionAnim = actionLayer.getAnimation();
                if (actionAnim instanceof AbstractFadeModifier) {
                    current = ((AbstractFadeModifier) actionAnim).get3DTransformWithBase(modelName, type, tickDelta, value0, current);
                } else {
                    current = actionAnim.get3DTransform(modelName, type, tickDelta, value0);
                }
            }
        }
        
        return current;
    }

    private Vec3f combineRotations(Vec3f parent, Vec3f child) {
        org.lwjgl.util.vector.Matrix4f m = new org.lwjgl.util.vector.Matrix4f();
        m.setIdentity();
        
        // Torso rotations (Z, Y, X)
        if (parent.getZ() != 0) m.rotate(parent.getZ(), new org.lwjgl.util.vector.Vector3f(0, 0, 1));
        if (parent.getY() != 0) m.rotate(parent.getY(), new org.lwjgl.util.vector.Vector3f(0, 1, 0));
        if (parent.getX() != 0) m.rotate(parent.getX(), new org.lwjgl.util.vector.Vector3f(1, 0, 0));
        
        // Arm rotations (Z, Y, X)
        if (child.getZ() != 0) m.rotate(child.getZ(), new org.lwjgl.util.vector.Vector3f(0, 0, 1));
        if (child.getY() != 0) m.rotate(child.getY(), new org.lwjgl.util.vector.Vector3f(0, 1, 0));
        if (child.getX() != 0) m.rotate(child.getX(), new org.lwjgl.util.vector.Vector3f(1, 0, 0));
        
        // Extract Euler angles (Z, Y, X order)
        // M20 = m02, M21 = m12, M22 = m22
        // M00 = m00, M10 = m01
        float yaw = (float) Math.asin(Math.max(-1.0f, Math.min(1.0f, -m.m02)));
        float pitch = (float) Math.atan2(m.m12, m.m22);
        float roll = (float) Math.atan2(m.m01, m.m00);
        
        return new Vec3f(pitch, yaw, roll);
    }

    public Vec3f getBaseLayerTransform(String boneName, TransformType type, float partialTick) {
        if (this.baseLayer.getAnimation() == null) return Vec3f.ZERO;
        this.baseLayer.setupAnim(partialTick);
        return this.baseLayer.get3DTransform(boneName, type, partialTick, Vec3f.ZERO);
    }

    public Vec3f getActionLayerTransform(String boneName, TransformType type, float partialTick) {
        if (this.actionLayer.getAnimation() == null) return Vec3f.ZERO;
        this.actionLayer.setupAnim(partialTick);
        return this.actionLayer.get3DTransform(boneName, type, partialTick, Vec3f.ZERO);
    }

    // --- Legacy compatibility methods below ---
    // Since we are changing MixinModelBiped to call get3DTransform, we can remove most of these,
    // but some external code might call them. We can return dummy values or convert them.

    public float[] getRotation(String boneName, float partialTick) {
        Vec3f rot = get3DTransform(boneName, TransformType.ROTATION, partialTick, Vec3f.ZERO);
        return new float[] { rot.getX(), rot.getY(), rot.getZ(), 1.0f }; // Always full weight, assumes blending done in value0
    }

    public float[] getPosition(String boneName, float partialTick) {
        Vec3f pos = get3DTransform(boneName, TransformType.POSITION, partialTick, Vec3f.ZERO);
        return new float[] { pos.getX(), pos.getY(), pos.getZ(), 1.0f };
    }

    public boolean isPlaying() {
        return playing
                || (baseLayer.getAnimation() != null && baseLayer.getAnimation().isActive())
                || isActionPlaying()
                // Если action затухает — нам всё ещё нужно применять applyBone чтобы отрендерить затухание.
                // (Без этого applyBone пропускается и переход снапится к ванилле мгновенно.)
                || (actionFadingOut && actionLayer.getAnimation() != null && actionLayer.getAnimation().isActive());
    }

    public boolean isActionPlaying() {
        return actionLayer.getAnimation() != null && actionLayer.getAnimation().isActive() && !actionFadingOut;
    }

    public boolean isActionFadingOut() {
        return this.actionFadingOut;
    }

    public boolean hasActionWeight() {
        return isActionPlaying();
    }

    public float getActionWeight() {
        return isActionPlaying() ? 1.0f : 0.0f;
    }

    public void setTargetArmPitchWeight(float target) {
        this.targetArmPitchWeight = target;
    }

    public float getRenderArmPitchWeight(float partialTicks) {
        return this.prevArmPitchWeight + (this.armPitchWeight - this.prevArmPitchWeight) * partialTicks;
    }

    public float getActionFadeWeight(float tickDelta) {
        IAnimation anim = actionLayer.getAnimation();
        if (anim == null || !anim.isActive()) return 0.0f;
        if (anim instanceof AbstractFadeModifier) {
            AbstractFadeModifier fm = (AbstractFadeModifier) anim;
            float progress = fm.calculateProgress(tickDelta);
            progress = Math.min(1.0f, Math.max(0.0f, progress));
            float alpha = fm.getAlpha("rightArm", TransformType.ROTATION, progress);
            if (actionFadingOut) {
                return Math.max(0.0f, 1.0f - alpha);
            } else {
                return Math.min(1.0f, Math.max(0.0f, alpha));
            }
        }
        return isActionPlaying() ? 1.0f : 0.0f;
    }

    public float getArmPitchTrackingWeight(float tickDelta, boolean isHoldingWeapon) {
        if (!isHoldingWeapon) {
            if (actionFadingOut && actionClip != null) {
                float clipFactor = getClipPitchFactor(actionClip.name);
                return clipFactor * getActionFadeWeight(tickDelta);
            }
            return 0.0f;
        }

        IAnimation anim = actionLayer.getAnimation();
        if (anim instanceof AbstractFadeModifier) {
            AbstractFadeModifier fm = (AbstractFadeModifier) anim;
            float progress = fm.calculateProgress(tickDelta);
            progress = Math.min(1.0f, Math.max(0.0f, progress));
            float alpha = fm.getAlpha("rightArm", TransformType.ROTATION, progress);

            if (actionFadingOut) {
                float clipFactor = actionClip != null ? getClipPitchFactor(actionClip.name) : 1.0f;
                return clipFactor * Math.max(0.0f, 1.0f - alpha);
            } else {
                float newFactor = actionClip != null ? getClipPitchFactor(actionClip.name) : 1.0f;
                float oldFactor = previousActionClip != null ? getClipPitchFactor(previousActionClip.name) : (currentClip != null ? getClipPitchFactor(currentClip.name) : 1.0f);
                return Math.min(1.0f, Math.max(0.0f, newFactor * alpha + oldFactor * (1.0f - alpha)));
            }
        }

        if (actionClip != null) {
            return getClipPitchFactor(actionClip.name);
        }

        if (currentClip != null) {
            return getClipPitchFactor(currentClip.name);
        }

        return 1.0f;
    }

    private static float getClipPitchFactor(String name) {
        if (name == null) return 1.0f;
        if (name.contains("run") || name.contains("sprint") || name.contains("roll") 
            || name.contains("lie") || name.contains("reload")) {
            return 0.0f;
        }
        return 1.0f;
    }

    public float getActionProgress() {
        IAnimation anim = actionLayer.getAnimation();
        if (anim instanceof KeyframeAnimationPlayer) {
            return ((KeyframeAnimationPlayer) anim).getProgress();
        } else if (anim instanceof AbstractFadeModifier) {
            IAnimation inner = ((AbstractFadeModifier) anim).getAnimation();
            if (inner instanceof KeyframeAnimationPlayer) {
                return ((KeyframeAnimationPlayer) inner).getProgress();
            }
            IAnimation begin = ((AbstractFadeModifier) anim).getBeginAnimation();
            if (begin instanceof KeyframeAnimationPlayer) {
                return ((KeyframeAnimationPlayer) begin).getProgress();
            }
        }
        return 0.0f;
    }

    public float getActionSpeed() {
        return actionSpeed;
    }

    public void setActionSpeed(float speed) {
        this.actionSpeed = speed;
        if (this.actionLayer.getAnimation() instanceof AbstractFadeModifier) {
            AbstractFadeModifier fm = (AbstractFadeModifier) this.actionLayer.getAnimation();
            if (fm.getAnimation() instanceof KeyframeAnimationPlayer) {
                ((KeyframeAnimationPlayer) fm.getAnimation()).setSpeed(speed);
            }
        }
    }

    public float getPrevActionWeight() { return getActionWeight(); }
    public float getCrossfadeWeight() { return 0f; }
    public float getPrevCrossfadeWeight() { return 0f; }
    public float getWeight() { return isPlaying() ? 1.0f : 0.0f; }
    public float getPrevWeight() { return getWeight(); }
    public float getFadeWeight() { return fadeWeight; }
    public float getPrevFadeWeight() { return fadeWeight; }
    public String getCurrentAnimationName() { return currentClip != null ? currentClip.name : null; }
    public String getPrevAnimationName() { return null; }
    public String getCurrentActionName() { return isActionPlaying() && actionClip != null ? actionClip.name : null; }
    public String getFadeActionName() { return actionFadingOut && actionClip != null ? actionClip.name : null; }

    public boolean isRollActive(float pt) {
        if (actionClip != null && "roll".equals(actionClip.name)) {
            return true;
        }
        return getRollWeight(this.actionLayer.getAnimation(), pt) > 0.0f;
    }

    public KeyframeAnimationPlayer getRollPlayer() {
        return findRollPlayer(this.actionLayer.getAnimation());
    }

    private KeyframeAnimationPlayer findRollPlayer(IAnimation anim) {
        if (anim == null) return null;
        if (anim instanceof KeyframeAnimationPlayer) {
            KeyframeAnimationPlayer kap = (KeyframeAnimationPlayer) anim;
            if ("roll".equals(kap.getClip().name)) {
                return kap;
            }
        } else if (anim instanceof AbstractFadeModifier) {
            AbstractFadeModifier fm = (AbstractFadeModifier) anim;
            KeyframeAnimationPlayer found = findRollPlayer(fm.getBeginAnimation());
            if (found != null) return found;
            return findRollPlayer(fm.getAnimation());
        }
        return null;
    }

    public float getRollProgress(float tickDelta) {
        float weight = getRollWeight(this.actionLayer.getAnimation(), tickDelta);
        return 1.0f - weight;
    }

    private float getRollWeight(IAnimation anim, float tickDelta) {
        if (anim == null) return 0f;
        if (anim instanceof KeyframeAnimationPlayer) {
            KeyframeAnimationPlayer kap = (KeyframeAnimationPlayer) anim;
            return "roll".equals(kap.getClip().name) ? 1.0f : 0f;
        } else if (anim instanceof AbstractFadeModifier) {
            AbstractFadeModifier fm = (AbstractFadeModifier) anim;
            float progress = fm.calculateProgress(tickDelta);
            progress = Math.min(1.0f, Math.max(0.0f, progress));
            float alpha = fm.getAlpha("body", TransformType.ROTATION, progress);
            
            float targetWeight = getRollWeight(fm.getAnimation(), tickDelta) * alpha;
            float sourceWeight = getRollWeight(fm.getBeginAnimation(), tickDelta) * (1.0f - alpha);
            return targetWeight + sourceWeight;
        }
        return 0f;
    }
}
