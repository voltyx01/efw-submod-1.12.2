package efw.animation;

import efw.animation.layered.AnimationStack;
import efw.animation.layered.IAnimation;
import efw.animation.layered.KeyframeAnimationPlayer;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Ease;
import efw.animation.layered.math.Vec3f;
import efw.animation.layered.modifier.AbstractFadeModifier;
import efw.animation.layered.modifier.AdjustmentModifier;
import efw.animation.layered.modifier.ModifierLayer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Optional;

public class AnimationPlayer {

    private final AnimationStack stack;
    private final ModifierLayer<IAnimation> baseLayer;
    private final ModifierLayer<IAnimation> actionLayer;
    private final ModifierLayer<IAnimation> rollLayer;

    private EntityPlayer player;
    private float lastTickDelta = 0.0f;

    // Helper states for API compatibility
    private AnimationClip currentClip;
    private AnimationClip actionClip;
    private AnimationClip previousActionClip;
    private AnimationClip rollClip;
    private boolean playing = false;
    private boolean actionFadingOut = false;
    private boolean rollFadingOut = false;
    private float actionSpeed = 1.0f;
    private float lastSpeedMult = 1.0f;
    private float fadeWeight = 0f;
    public float rollYawOffset = 0.0f;
    public float rollFade = 0.0f;
    public boolean isHoldingWeapon = false;
    public AnimationClip lastWeaponClip = null;

    private float currentArmPitchWeight = 0.0f;
    private float prevArmPitchWeight = 0.0f;
    private float currentSneakOffsetWeight = 0.0f;
    private float prevSneakOffsetWeight = 0.0f;
    private float currentWeaponSneakWeight = 0.0f;
    private float prevWeaponSneakWeight = 0.0f;
    private float currentRollLookWeight = 1.0f;
    private float prevRollLookWeight = 1.0f;
    private float currentSwimHeadWeight = 0.0f;
    private float prevSwimHeadWeight = 0.0f;
    public boolean isSwimmingHead = false;
    private boolean snapped = false;
    private boolean actionSnapped = false;
    private String lastClipName = null;

    public AnimationPlayer(EntityPlayer player) {
        this.player = player;
        this.stack = new AnimationStack();
        this.baseLayer = new ModifierLayer<>();
        this.actionLayer = new ModifierLayer<>();
        this.rollLayer = new ModifierLayer<>();

        this.stack.addAnimLayer(0, this.baseLayer);
        this.stack.addAnimLayer(100, this.actionLayer);
        this.stack.addAnimLayer(1000, this.rollLayer);
    }

    public AnimationPlayer() {
        this(null);
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
        if (player != null && this.currentWeaponSneakWeight == 0.0f && this.isHoldingWeapon) {
            this.currentWeaponSneakWeight = 1.0f;
            this.prevWeaponSneakWeight = 1.0f;
        }
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public AnimationStack getStack() {
        return stack;
    }

    public ModifierLayer<IAnimation> getBaseLayer() {
        return baseLayer;
    }

    public ModifierLayer<IAnimation> getActionLayer() {
        return actionLayer;
    }

    public ModifierLayer<IAnimation> getRollLayer() {
        return rollLayer;
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

        KeyframeAnimationPlayer playerAnim = new KeyframeAnimationPlayer(clip);
        playerAnim.setSpeed(speed);

        int blendTicks = getActionBlendTicks(clip.name);
        boolean isSwordToSword = false;
        if (this.previousActionClip != null && this.previousActionClip.name != null && clip.name != null) {
            if (this.previousActionClip.name.contains("sword_attack") && clip.name.contains("sword_attack")) {
                blendTicks = 3;
                isSwordToSword = true;
            }
        }
        if (this.actionSnapped || blendTicks <= 0) {
            this.actionSnapped = false;
            this.actionLayer.setAnimation(playerAnim);
        } else if (isSwordToSword) {
            // Sword-to-sword transition: snap the rightArm rotation immediately so it
            // doesn't arc/flip across the ~172° gap between the two attack poses.
            // All other bones (torso, leftArm, body) still blend smoothly via inOutSine.
            final int ticks = blendTicks;
            this.actionLayer.replaceAnimationWithFade(new AbstractFadeModifier(ticks) {
                @Override
                public float getAlpha(String modelName, TransformType type, float progress) {
                    if ("rightArm".equals(modelName) && type == TransformType.ROTATION) {
                        return 1.0f; // skip rotation blend – use target frame immediately
                    }
                    return Ease.inOutSine(progress);
                }
            }, playerAnim, true);
        } else {
            this.actionLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(blendTicks, Ease::inOutSine), playerAnim, true);
        }
    }

    public void stopAction(int blendTicks) {
        if (this.actionFadingOut) {
            return;
        }
        if (this.actionLayer.isActive()) {
            if (this.actionClip != null) {
                this.previousActionClip = this.actionClip;
            }
            this.actionLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(blendTicks, Ease::inOutSine), null, true);
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
            ticks = getActionBlendTicks(this.actionClip.name);
        }
        stopAction(ticks);
    }

    public void snapAction() {
        this.actionClip = null;
        this.actionLayer.setAnimation(null);
        this.actionFadingOut = false;
        this.actionSnapped = true;
    }

    public void cancelAction() {
        stopAction();
    }

    public void resumeActionLoop() {
        // Handled internally
    }

    public void playRoll(AnimationClip clip, float speed) {
        if (clip == null) return;
        this.rollClip = clip;
        this.rollFadingOut = false;
        KeyframeAnimationPlayer playerAnim = new KeyframeAnimationPlayer(clip);
        playerAnim.setSpeed(speed);
        this.rollLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(2, Ease::inOutSine), playerAnim, true);
    }

    public void stopRoll(int blendTicks) {
        if (this.rollLayer.isActive() && !this.rollFadingOut) {
            this.rollFadingOut = true;
            this.rollLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(blendTicks, Ease::inOutSine), null, true);
        }
    }

    public boolean isRollPlaying() {
        return this.rollLayer.isActive();
    }

    public boolean isRollFadingOut() {
        return this.rollFadingOut;
    }

    public boolean isRollActive(float pt) {
        return this.rollLayer.isActive();
    }

    public KeyframeAnimationPlayer getRollPlayer() {
        return findRollPlayer(this.rollLayer.getAnimation());
    }

    private KeyframeAnimationPlayer findRollPlayer(IAnimation anim) {
        if (anim == null) return null;
        if (anim instanceof KeyframeAnimationPlayer) {
            return (KeyframeAnimationPlayer) anim;
        } else if (anim instanceof AbstractFadeModifier) {
            AbstractFadeModifier fm = (AbstractFadeModifier) anim;
            KeyframeAnimationPlayer found = findRollPlayer(fm.getBeginAnimation());
            if (found != null) return found;
            return findRollPlayer(fm.getAnimation());
        }
        return null;
    }

    public float getRollProgress(float tickDelta) {
        KeyframeAnimationPlayer rp = getRollPlayer();
        if (rp != null) {
            return rp.getProgress();
        }
        return this.rollLayer.isActive() ? 0.5f : 1.0f;
    }

    public void snap() {
        this.playing = false;
        this.currentClip = null;
        this.baseLayer.setAnimation(null);
        this.snapped = true;
    }

    public void play(AnimationClip clip) {
        play(clip, 1.0f);
    }

    public void play(AnimationClip clip, float speed) {
        if (clip == currentClip && playing)
            return;

        String oldClipName = this.currentClip != null ? this.currentClip.name : this.lastClipName;
        this.currentClip = clip;
        this.lastClipName = clip != null ? clip.name : null;
        this.playing = true;

        KeyframeAnimationPlayer playerAnim = new KeyframeAnimationPlayer(clip);
        playerAnim.setSpeed(speed);

        int blendTicks = getPoseBlendTicks(clip.name, oldClipName);
        if (this.snapped || blendTicks <= 0) {
            this.snapped = false;
            this.baseLayer.setAnimation(playerAnim);
        } else {
            this.baseLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(blendTicks, Ease::inOutSine), playerAnim, true);
        }
    }

    public void stop() {
        this.playing = false;
        String oldName = this.currentClip != null ? this.currentClip.name : null;
        this.currentClip = null;
        IAnimation oldAnim = this.baseLayer.getAnimation();
        if (oldAnim != null && oldAnim.isActive()) {
            int blendTicks = getPoseBlendTicks(oldName, null);
            if (blendTicks <= 0) {
                this.baseLayer.setAnimation(null);
            } else {
                this.baseLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(blendTicks, Ease::inOutSine), null, true);
            }
        }
    }

    private void updateAnimationSpeed(IAnimation anim, float speed) {
        if (anim == null) return;
        if (anim instanceof KeyframeAnimationPlayer) {
            ((KeyframeAnimationPlayer) anim).setSpeed(speed);
        } else if (anim instanceof efw.animation.layered.modifier.AbstractModifier) {
            updateAnimationSpeed(((efw.animation.layered.modifier.AbstractModifier) anim).getAnimation(), speed);
            if (anim instanceof efw.animation.layered.modifier.AbstractFadeModifier) {
                updateAnimationSpeed(((efw.animation.layered.modifier.AbstractFadeModifier) anim).getBeginAnimation(), speed);
            }
        }
    }

    public void tick(float speedMult) {
        this.lastSpeedMult = speedMult;

        updateAnimationSpeed(this.baseLayer.getAnimation(), speedMult);

        KeyframeAnimationPlayer rp = getRollPlayer();
        if (rp != null && rp.isActive() && !this.rollFadingOut) {
            // roll.json: somersault rotation completes at tick 10 (out of 13), progress ~0.77.
            // Start the 8-tick smooth rise fadeout from the crouched roll landing.
            if (rp.getProgress() >= 0.77f) {
                stopRoll(8);
            }
        }

        this.stack.tick();

        if (this.actionFadingOut && !this.actionLayer.isActive()) {
            this.actionClip = null;
            this.actionFadingOut = false;
        }
        if (this.rollFadingOut && !this.rollLayer.isActive()) {
            this.rollClip = null;
            this.rollFadingOut = false;
        }

        // Smooth arm pitch tracking weight
        this.prevArmPitchWeight = this.currentArmPitchWeight;
        boolean isRollingNow = isRollPlaying();
        String actionName = getCurrentActionName();
        boolean actionBlocksPitch = actionName != null && (actionName.contains("reload") || actionName.contains("run") || actionName.contains("sprint"));
        float targetArmPitch = (this.isHoldingWeapon && !isRollingNow && !actionBlocksPitch) ? 1.0f : 0.0f;
        this.currentArmPitchWeight += (targetArmPitch - this.currentArmPitchWeight) * 0.25f;
        if (Math.abs(targetArmPitch - this.currentArmPitchWeight) < 0.01f) {
            this.currentArmPitchWeight = targetArmPitch;
        }

        // Smooth head/arm look unlock after roll
        this.prevRollLookWeight = this.currentRollLookWeight;
        float targetRollLook = isRollingNow ? 0.0f : 1.0f;
        this.currentRollLookWeight += (targetRollLook - this.currentRollLookWeight) * 0.15f;
        if (Math.abs(targetRollLook - this.currentRollLookWeight) < 0.01f) {
            this.currentRollLookWeight = targetRollLook;
        }

        // Smooth sneak offset weight (for smooth Y transition when entering/exiting sneak)
        this.prevSneakOffsetWeight = this.currentSneakOffsetWeight;
        boolean isSneakingNow = this.player != null && this.player.isSneaking();
        float targetSneak = isSneakingNow ? 1.0f : 0.0f;
        this.currentSneakOffsetWeight += (targetSneak - this.currentSneakOffsetWeight) * 0.25f;
        if (Math.abs(targetSneak - this.currentSneakOffsetWeight) < 0.01f) {
            this.currentSneakOffsetWeight = targetSneak;
        }

        // Smooth weapon hold weight (for smooth arm/head Y transition between weapon and unarmed)
        this.prevWeaponSneakWeight = this.currentWeaponSneakWeight;
        float targetWeaponSneak = this.isHoldingWeapon ? 1.0f : 0.0f;
        this.currentWeaponSneakWeight += (targetWeaponSneak - this.currentWeaponSneakWeight) * 0.2f;
        if (Math.abs(targetWeaponSneak - this.currentWeaponSneakWeight) < 0.01f) {
            this.currentWeaponSneakWeight = targetWeaponSneak;
        }

        // Smooth swimming head lock weight (smooth transition into and out of forward-looking swimming head pose)
        this.prevSwimHeadWeight = this.currentSwimHeadWeight;
        boolean isSwimmingAnim = isPlaying() && "swimming".equals(getCurrentAnimationName());
        boolean isUnderwater = this.player != null && (this.player.isInWater() || this.player.isInsideOfMaterial(net.minecraft.block.material.Material.WATER));
        boolean isUnderwaterSprint = isUnderwater && this.player.isSprinting();
        boolean shouldLockHead = isSwimmingAnim || isUnderwaterSprint || this.isSwimmingHead;
        this.isSwimmingHead = false;

        float targetSwimHead = shouldLockHead ? 1.0f : 0.0f;
        this.currentSwimHeadWeight += (targetSwimHead - this.currentSwimHeadWeight) * 0.25f;
        if (Math.abs(targetSwimHead - this.currentSwimHeadWeight) < 0.005f) {
            this.currentSwimHeadWeight = targetSwimHead;
        }
    }

    private int getPoseBlendTicks(String animName, String otherClipName) {
        boolean isCrawl1 = animName != null && (animName.contains("lie") || animName.contains("crawl"));
        boolean isCrawl2 = otherClipName != null && (otherClipName.contains("lie") || otherClipName.contains("crawl"));
        if ((isCrawl1 || isCrawl2) && !(isCrawl1 && isCrawl2)) {
            return 0;
        }
        return getActionBlendTicks(animName);
    }

    private int getActionBlendTicks(String animName) {
        if (isRollPlaying()) {
            return 3;
        }
        if (animName != null && animName.contains("lie_reload")) {
            return 10;
        }
        if (animName != null && (animName.contains("pistol") || animName.contains("rifle"))) {
            if (animName.endsWith("run_upper")) {
                return 4;
            }
            return 8;
        }
        if ("roll".equals(animName)) {
            return 8;
        }
        if ("idle_standing".equals(animName) || animName != null && animName.startsWith("idle_")) {
            return 10;
        }
        if (animName != null && (animName.startsWith("walking") || animName.equals("running"))) {
            return 8;
        }
        if (animName != null && (animName.equals("axe") || animName.equals("pickaxe")
                || animName.equals("shovel") || animName.equals("hoe"))) {
            return 3;
        }
        if (animName != null && (animName.contains("sword_attack") || animName.contains("fist_attack"))) {
            return 4;
        }
        return 6;
    }

    public void setupAnim(float tickDelta) {
        this.lastTickDelta = tickDelta;
        this.stack.setupAnim(tickDelta);
    }

    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        this.lastTickDelta = tickDelta;
        if (this.isRollPlaying() && this.isHoldingWeapon && ("rightArm".equals(modelName) || "leftArm".equals(modelName))) {
            // When holding a weapon during roll, actionLayer (rifle_run_upper / pistol_run_upper)
            // holds the weapon folded across chest. Do not let roll.json tumble keyframes overwrite arms!
            Vec3f current = value0;
            if (this.baseLayer.isActive()) {
                current = this.baseLayer.get3DTransform(modelName, type, tickDelta, current);
            }
            if (this.actionLayer.isActive()) {
                current = this.actionLayer.get3DTransform(modelName, type, tickDelta, current);
            }
            return current;
        }
        return this.stack.get3DTransform(modelName, type, tickDelta, value0);
    }

    public Vec3f getBaseLayerTransform(String boneName, TransformType type, float partialTick) {
        return this.baseLayer.get3DTransform(boneName, type, partialTick, Vec3f.ZERO);
    }

    public Vec3f getActionLayerTransform(String boneName, TransformType type, float partialTick) {
        return this.actionLayer.get3DTransform(boneName, type, partialTick, Vec3f.ZERO);
    }

    public Vec3f getRollLayerTransform(String boneName, TransformType type, float partialTick) {
        return this.rollLayer.get3DTransform(boneName, type, partialTick, Vec3f.ZERO);
    }

    public float[] getRotation(String boneName, float partialTick) {
        Vec3f rot = get3DTransform(boneName, TransformType.ROTATION, partialTick, Vec3f.ZERO);
        return new float[]{rot.getX(), rot.getY(), rot.getZ(), 1.0f};
    }

    public float[] getPosition(String boneName, float partialTick) {
        Vec3f pos = get3DTransform(boneName, TransformType.POSITION, partialTick, Vec3f.ZERO);
        return new float[]{pos.getX(), pos.getY(), pos.getZ()};
    }

    public boolean isPlaying() {
        return baseLayer.getAnimation() != null && baseLayer.getAnimation().isActive();
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
    }

    public float getRenderArmPitchWeight(float partialTicks) {
        return isHoldingWeapon ? 1.0f : 0.0f;
    }

    public float getActionFadeWeight(float tickDelta) {
        return isActionPlaying() ? 1.0f : 0.0f;
    }

    public float getArmPitchTrackingWeight(float tickDelta, boolean isHoldingWeapon) {
        float w = prevArmPitchWeight + (currentArmPitchWeight - prevArmPitchWeight) * tickDelta;
        return Math.max(0.0f, Math.min(1.0f, w));
    }

    public float getRollLookWeight(float tickDelta) {
        float w = prevRollLookWeight + (currentRollLookWeight - prevRollLookWeight) * tickDelta;
        return Math.max(0.0f, Math.min(1.0f, w));
    }

    public float getSneakOffsetWeight(float tickDelta) {
        float w = prevSneakOffsetWeight + (currentSneakOffsetWeight - prevSneakOffsetWeight) * tickDelta;
        return Math.max(0.0f, Math.min(1.0f, w));
    }

    public float getWeaponSneakWeight(float tickDelta) {
        float w = prevWeaponSneakWeight + (currentWeaponSneakWeight - prevWeaponSneakWeight) * tickDelta;
        return Math.max(0.0f, Math.min(1.0f, w));
    }

    public float getSwimHeadWeight(float tickDelta) {
        float w = prevSwimHeadWeight + (currentSwimHeadWeight - prevSwimHeadWeight) * tickDelta;
        return Math.max(0.0f, Math.min(1.0f, w));
    }

    public float getActionProgress() {
        IAnimation anim = actionLayer.getAnimation();
        if (anim instanceof KeyframeAnimationPlayer) {
            return ((KeyframeAnimationPlayer) anim).getProgress();
        }
        return 0.0f;
    }

    public float getActionSpeed() {
        return actionSpeed;
    }

    public void setActionSpeed(float speed) {
        this.actionSpeed = speed;
        updateAnimationSpeed(this.actionLayer.getAnimation(), speed);
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
}