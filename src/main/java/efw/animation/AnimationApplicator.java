package efw.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelPlayer;
import efw.animation.layered.TransformType;
import efw.animation.layered.math.Vec3f;
import java.util.HashMap;
import java.util.Map;

public class AnimationApplicator {

    private static final Map<String, String> BONE_TO_FIELD = new HashMap<>();
    static {
        BONE_TO_FIELD.put("rightArm",  "bipedRightArm");
        BONE_TO_FIELD.put("leftArm",   "bipedLeftArm");
        BONE_TO_FIELD.put("rightLeg",  "bipedRightLeg");
        BONE_TO_FIELD.put("leftLeg",   "bipedLeftLeg");
        BONE_TO_FIELD.put("torso",     "bipedBody");
        BONE_TO_FIELD.put("body",      "bipedBody");
        BONE_TO_FIELD.put("root",      "bipedRightArm"); // Add root mapping for weapon animations
        BONE_TO_FIELD.put("right_arm", "bipedRightArm");
        BONE_TO_FIELD.put("left_arm",  "bipedLeftArm");
    }

    public static void applyBone(ModelRenderer bone, ModelRenderer overlay, AnimationPlayer ap, String boneName, float pt) {
        // 1. Rotation — unified through the full layer stack (base + action w/ fade)
        Vec3f vanillaRot = new Vec3f(bone.rotateAngleX, bone.rotateAngleY, bone.rotateAngleZ);
        Vec3f newRot = ap.get3DTransform(boneName, TransformType.ROTATION, pt, vanillaRot);
        bone.rotateAngleX = newRot.getX();
        bone.rotateAngleY = newRot.getY();
        bone.rotateAngleZ = newRot.getZ();

        // 2. Position — unified through the full layer stack
        Vec3f vanillaPos = new Vec3f(bone.rotationPointX, bone.rotationPointY, bone.rotationPointZ);
        Vec3f newPos = ap.get3DTransform(boneName, TransformType.POSITION, pt, vanillaPos);
        bone.rotationPointX = newPos.getX();
        bone.rotationPointY = newPos.getY();
        bone.rotationPointZ = newPos.getZ();

        // 3. Синхронизируем слой одежды с основной костью, если он существует
        if (overlay != null) {
            overlay.rotateAngleX = bone.rotateAngleX;
            overlay.rotateAngleY = bone.rotateAngleY;
            overlay.rotateAngleZ = bone.rotateAngleZ;
            overlay.rotationPointX = bone.rotationPointX;
            overlay.rotationPointY = bone.rotationPointY;
            overlay.rotationPointZ = bone.rotationPointZ;
        }
    }

    public static void applyBone(ModelRenderer bone, ModelRenderer overlay, float[] rot, float[] pos) {
        // Legacy fallback
        if (rot != null && rot.length == 4) {
            float w = rot[3];
            bone.rotateAngleX = lerpAngle(bone.rotateAngleX, rot[0], w);
            bone.rotateAngleY = lerpAngle(bone.rotateAngleY, rot[1], w);
            bone.rotateAngleZ = lerpAngle(bone.rotateAngleZ, rot[2], w);
        }

        if (pos != null && pos.length == 4) {
            float w = pos[3];
            bone.rotationPointX += pos[0] * w;
            bone.rotationPointY += -pos[1] * w;
            bone.rotationPointZ += pos[2] * w;
        }

        if (overlay != null) {
            overlay.rotateAngleX = bone.rotateAngleX;
            overlay.rotateAngleY = bone.rotateAngleY;
            overlay.rotateAngleZ = bone.rotateAngleZ;
            overlay.rotationPointX = bone.rotationPointX;
            overlay.rotationPointY = bone.rotationPointY;
            overlay.rotationPointZ = bone.rotationPointZ;
        }
    }

    public static ModelRenderer getOverlayForBone(ModelRenderer bone, ModelBiped model) {
        if (model instanceof ModelPlayer) {
            ModelPlayer mp = (ModelPlayer) model;
            if (bone == mp.bipedRightArm) return mp.bipedRightArmwear;
            if (bone == mp.bipedLeftArm)  return mp.bipedLeftArmwear;
            if (bone == mp.bipedRightLeg) return mp.bipedRightLegwear;
            if (bone == mp.bipedLeftLeg)  return mp.bipedLeftLegwear;
            if (bone == mp.bipedBody)     return mp.bipedBodyWear;
        }
        if (bone == model.bipedHead) return model.bipedHeadwear;
        return null;
    }
    
    private static float lerpAngle(float a, float b, float t) {
        float delta = (b - a) % ((float) Math.PI * 2f);
        if (delta < -Math.PI) delta += (float) Math.PI * 2f;
        if (delta >= Math.PI) delta -= (float) Math.PI * 2f;
        return a + delta * t;
    }
}