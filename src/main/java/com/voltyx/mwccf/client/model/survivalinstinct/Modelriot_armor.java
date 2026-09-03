package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelriot_armor extends ModelBiped {

    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer body_r1;
    public ModelRenderer body_r2;
    public ModelRenderer body_r3;
    public ModelRenderer body_r4;
    public ModelRenderer left_arm;
    public ModelRenderer left_arm_r1;
    public ModelRenderer left_arm_r2;
    public ModelRenderer right_arm;
    public ModelRenderer right_arm_r1;
    public ModelRenderer right_arm_r2;
    public ModelRenderer left_leg;
    public ModelRenderer left_leg_r1;
    public ModelRenderer right_leg;
    public ModelRenderer right_leg_r1;
    public ModelRenderer right_leg_r2;
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe;

    public Modelriot_armor() {
        this.textureWidth = 128;
        this.textureHeight = 128;

        this.bipedHead = new ModelRenderer(this);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.head = new ModelRenderer(this);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.mirror = false;
        this.head.setTextureOffset(26, 25).addBox(-5.0F, -5.0F, -1.0F, (int)10.0, (int)4.0, (int)6.0, 0.6F);
        this.head.mirror = false;
        this.head.setTextureOffset(24, 15).addBox(-5.0F, -5.5F, -5.0F, (int)10.0, (int)1.0, (int)5.0, 0.3F);
        this.head.mirror = false;
        this.head.setTextureOffset(47, 37).addBox(-5.0F, -4.5F, -5.0F, (int)10.0, (int)4.0, (int)1.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(0, 0).addBox(-5.0F, -9.5F, -5.0F, (int)10.0, (int)5.0, (int)10.0, -0.2F);
        this.bipedHead.addChild(this.head);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 31).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(64, 23).addBox(-4.0F, -0.8F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 63).addBox(-4.0F, -0.8F, -3.0F, (int)3.0, (int)4.0, (int)6.0, -0.65F);
        this.body.mirror = true;
        this.body.setTextureOffset(0, 63).addBox(1.0F, -0.8F, -3.0F, (int)3.0, (int)4.0, (int)6.0, -0.65F);
        this.body.mirror = true;
        this.body.setTextureOffset(64, 23).addBox(1.0F, -0.8F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.mirror = false;
        this.body.setTextureOffset(62, 0).addBox(-4.0F, 2.0F, -3.0F, (int)8.0, (int)10.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(70, 17).addBox(-4.0F, 3.0F, -3.4F, (int)8.0, (int)1.0, (int)2.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(68, 67).addBox(-4.0F, 5.0F, -3.4F, (int)8.0, (int)1.0, (int)2.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(68, 34).addBox(-4.0F, 7.0F, -3.4F, (int)8.0, (int)1.0, (int)2.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(66, 14).addBox(-4.0F, 9.0F, -3.4F, (int)8.0, (int)1.0, (int)2.0, 0.05F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(0.0F, 10.5F, 2.5F);
        setRotationAngle(this.body_r1, 0.3054F, 0.0F, 0.0F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(28, 50).addBox(-2.0F, 4.5F, -0.5F, (int)4.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(40, 13).addBox(-3.0F, 2.5F, -0.5F, (int)6.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(28, 48).addBox(-3.0F, 0.5F, -0.5F, (int)6.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(0, 7).addBox(-2.0F, 4.5F, -0.5F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(12, 64).addBox(-3.0F, 0.5F, -0.5F, (int)6.0, (int)4.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(0.0F, 10.5F, -2.5F);
        setRotationAngle(this.body_r2, -0.2618F, 0.0F, 0.0F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(0, 7).addBox(-2.0F, 4.5F, -0.5F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(28, 50).addBox(-2.0F, 4.5F, -0.5F, (int)4.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(40, 13).addBox(-3.0F, 2.5F, -0.5F, (int)6.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(28, 48).addBox(-3.0F, 0.5F, -0.5F, (int)6.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(12, 64).addBox(-3.0F, 0.5F, -0.5F, (int)6.0, (int)4.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(0.0F, 6.5F, 2.55F);
        setRotationAngle(this.body_r3, 0.0F, 3.1416F, 0.0F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(66, 14).addBox(-4.0F, 2.5F, -0.95F, (int)8.0, (int)1.0, (int)2.0, 0.05F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(68, 34).addBox(-4.0F, 0.5F, -0.95F, (int)8.0, (int)1.0, (int)2.0, 0.05F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(68, 67).addBox(-4.0F, -1.5F, -0.95F, (int)8.0, (int)1.0, (int)2.0, 0.05F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(70, 17).addBox(-4.0F, -3.5F, -0.95F, (int)8.0, (int)1.0, (int)2.0, 0.05F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(62, 0).addBox(-4.0F, -4.5F, -0.55F, (int)8.0, (int)10.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(3.5F, 2.5F, -3.0F);
        setRotationAngle(this.body_r4, -0.0886F, -0.1739F, 0.1899F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(24, 15).addBox(0.5F, -6.5F, 0.0F, (int)1.0, (int)4.0, (int)1.0, 0.05F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(0, 0).addBox(-1.5F, -2.5F, -1.0F, (int)3.0, (int)5.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r4);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(54, 76).addBox(-1.0F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(76, 47).addBox(-1.0F, 6.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(16, 48).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(1.5F, 6.0F, 0.0F);
        setRotationAngle(this.left_arm_r1, 0.0F, 0.0F, 0.0436F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(52, 54).addBox(-0.5F, -2.0F, -3.0F, (int)3.0, (int)5.0, (int)6.0, 0.05F);
        this.left_arm.addChild(this.left_arm_r1);

        this.left_arm_r2 = new ModelRenderer(this);
        this.left_arm_r2.setRotationPoint(2.5F, 1.0F, 0.0F);
        setRotationAngle(this.left_arm_r2, 0.0F, 0.0F, -0.0873F);
        this.left_arm_r2.mirror = true;
        this.left_arm_r2.setTextureOffset(40, 0).addBox(-2.5F, -4.0F, -3.0F, (int)5.0, (int)7.0, (int)6.0, -0.3F);
        this.left_arm.addChild(this.left_arm_r2);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(16, 48).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(76, 57).addBox(-3.0F, -1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(54, 76).addBox(-3.0F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(76, 47).addBox(-3.0F, 6.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-1.5F, 6.0F, 0.0F);
        setRotationAngle(this.right_arm_r1, 0.0F, 0.0F, -0.0436F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(52, 54).addBox(-2.5F, -2.0F, -3.0F, (int)3.0, (int)5.0, (int)6.0, 0.05F);
        this.right_arm.addChild(this.right_arm_r1);

        this.right_arm_r2 = new ModelRenderer(this);
        this.right_arm_r2.setRotationPoint(-2.5F, 1.0F, 0.0F);
        setRotationAngle(this.right_arm_r2, 0.0F, 0.0F, 0.0873F);
        this.right_arm_r2.mirror = false;
        this.right_arm_r2.setTextureOffset(40, 0).addBox(-2.5F, -4.0F, -3.0F, (int)5.0, (int)7.0, (int)6.0, -0.3F);
        this.right_arm.addChild(this.right_arm_r2);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(70, 37).addBox(-1.9F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(30, 0).addBox(-1.9F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(42, 44).addBox(-1.9F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.2F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.left_leg_r1 = new ModelRenderer(this);
        this.left_leg_r1.setRotationPoint(0.1F, 5.0F, -1.5F);
        setRotationAngle(this.left_leg_r1, 0.0873F, 0.0F, 0.0F);
        this.left_leg_r1.mirror = true;
        this.left_leg_r1.setTextureOffset(0, 15).addBox(-1.5F, -2.0F, -1.0F, (int)3.0, (int)4.0, (int)1.0, 0.4F);
        this.left_leg.addChild(this.left_leg_r1);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(42, 44).addBox(-2.1F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(42, 73).addBox(-2.1F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(68, 72).addBox(-2.1F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_leg_r1 = new ModelRenderer(this);
        this.right_leg_r1.setRotationPoint(-0.1F, 5.0F, -1.5F);
        setRotationAngle(this.right_leg_r1, 0.0873F, 0.0F, 0.0F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(0, 15).addBox(-1.5F, -2.0F, -1.0F, (int)3.0, (int)4.0, (int)1.0, 0.4F);
        this.right_leg.addChild(this.right_leg_r1);

        this.right_leg_r2 = new ModelRenderer(this);
        this.right_leg_r2.setRotationPoint(-3.1F, 2.25F, 0.0F);
        setRotationAngle(this.right_leg_r2, 0.0F, 0.0F, -0.0873F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(66, 77).addBox(-1.0F, -2.25F, -2.0F, (int)2.0, (int)3.0, (int)4.0, 0.45F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(30, 69).addBox(-1.0F, -2.25F, -2.0F, (int)2.0, (int)6.0, (int)4.0, 0.2F);
        this.right_leg.addChild(this.right_leg_r2);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(14, 69).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.6F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(24, 21).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(56, 67).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.4F);
        this.bipedLeftLeg.addChild(this.left_shoe);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(24, 21).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(14, 69).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.6F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(56, 67).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.4F);
        this.bipedRightLeg.addChild(this.right_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
