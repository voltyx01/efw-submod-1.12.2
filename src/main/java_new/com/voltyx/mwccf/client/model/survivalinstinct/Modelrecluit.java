package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelrecluit extends ModelBiped {

    public ModelRenderer head;
    public ModelRenderer head_r1;
    public ModelRenderer head_r2;
    public ModelRenderer head_r3;
    public ModelRenderer head_r4;
    public ModelRenderer head_r5;
    public ModelRenderer head_r6;
    public ModelRenderer head_r7;
    public ModelRenderer body;
    public ModelRenderer body_r1;
    public ModelRenderer body_r2;
    public ModelRenderer body_r3;
    public ModelRenderer body_r4;
    public ModelRenderer body_r5;
    public ModelRenderer body_r6;
    public ModelRenderer left_arm;
    public ModelRenderer right_arm;
    public ModelRenderer left_leg;
    public ModelRenderer right_leg;
    public ModelRenderer right_shoe;
    public ModelRenderer left_shoe;

    public Modelrecluit() {
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
        this.head.setTextureOffset(0, 27).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(0, 82).addBox(-5.0F, -9.0F, -2.0F, (int)10.0, (int)5.0, (int)3.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(0, 27).addBox(-5.5F, -4.5F, -2.0F, (int)1.0, (int)3.0, (int)3.0, 0.3F);
        this.head.mirror = false;
        this.head.setTextureOffset(76, 2).addBox(4.5F, -4.5F, -2.0F, (int)1.0, (int)3.0, (int)3.0, 0.2F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(5.5F, -3.0F, -0.5F);
        setRotationAngle(this.head_r1, 0.0F, 0.0F, -0.5236F);
        this.head_r1.mirror = true;
        this.head_r1.setTextureOffset(76, 8).addBox(-1.0F, -0.5F, -1.5F, (int)1.0, (int)2.0, (int)3.0, 0.2F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(0.1913F, -5.2093F, -4.0F);
        setRotationAngle(this.head_r2, 0.0807F, 0.0334F, -0.3914F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(18, 66).addBox(-4.0676F, -1.3313F, -5.0F, (int)3.0, (int)1.0, (int)6.0, 0.05F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(0.1913F, -5.2093F, -4.0F);
        setRotationAngle(this.head_r3, 0.0807F, -0.0334F, 0.3914F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(0, 66).addBox(1.0968F, -1.261F, -5.0F, (int)3.0, (int)1.0, (int)6.0, 0.05F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(0.1913F, -5.2093F, -4.0F);
        setRotationAngle(this.head_r4, 0.0873F, 0.0F, 0.0F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(24, 59).addBox(-2.1913F, -0.7907F, -5.0F, (int)4.0, (int)1.0, (int)6.0, 0.05F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(0.0F, -8.5F, -17.0F);
        setRotationAngle(this.head_r5, -0.0436F, 0.0F, 0.0F);
        this.head_r5.mirror = false;
        this.head_r5.setTextureOffset(0, 8).addBox(-2.0F, 0.5F, 12.4F, (int)4.0, (int)1.0, (int)1.0, 0.2F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(0.0F, -6.5F, 0.0F);
        setRotationAngle(this.head_r6, -0.0436F, 0.0F, 0.0F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(0, 21).addBox(-2.0F, 0.5F, 3.5F, (int)4.0, (int)1.0, (int)1.0, 0.2F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(0, 15).addBox(-5.0F, 0.5F, -5.0F, (int)10.0, (int)2.0, (int)10.0, -0.2F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(0, 0).addBox(-5.0F, -2.5F, -5.0F, (int)10.0, (int)5.0, (int)10.0, -0.6F);
        this.head.addChild(this.head_r6);

        this.head_r7 = new ModelRenderer(this);
        this.head_r7.setRotationPoint(-5.5F, -3.0F, -0.5F);
        setRotationAngle(this.head_r7, 0.0F, 0.0F, 0.5236F);
        this.head_r7.mirror = false;
        this.head_r7.setTextureOffset(76, 8).addBox(0.0F, -0.5F, -1.5F, (int)1.0, (int)2.0, (int)3.0, 0.2F);
        this.head.addChild(this.head_r7);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(32, 27).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(56, 41).addBox(-3.0F, 9.0F, 1.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(86, 0).addBox(-5.0F, 6.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(86, 0).addBox(2.0F, 6.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 97).addBox(-4.0F, 0.0F, 1.0F, (int)3.0, (int)9.0, (int)2.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(42, 98).addBox(-4.0F, 0.0F, -3.0F, (int)3.0, (int)9.0, (int)2.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(27, 98).addBox(-3.0F, 1.0F, -3.0F, (int)6.0, (int)3.0, (int)2.0, -0.8F);
        this.body.mirror = false;
        this.body.setTextureOffset(27, 98).addBox(-3.0F, 6.0F, -3.0F, (int)6.0, (int)3.0, (int)2.0, -0.8F);
        this.body.mirror = false;
        this.body.setTextureOffset(27, 98).addBox(-3.0F, 1.0F, 1.0F, (int)6.0, (int)3.0, (int)2.0, -0.8F);
        this.body.mirror = true;
        this.body.setTextureOffset(92, 45).addBox(1.0F, 1.0F, 1.0F, (int)3.0, (int)4.0, (int)2.0, -0.4F);
        this.body.mirror = false;
        this.body.setTextureOffset(58, 53).addBox(-4.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = false;
        this.body.setTextureOffset(92, 45).addBox(-4.0F, 1.0F, -3.0F, (int)3.0, (int)4.0, (int)2.0, -0.4F);
        this.body.mirror = false;
        this.body.setTextureOffset(92, 45).addBox(-4.0F, 1.0F, 1.0F, (int)3.0, (int)4.0, (int)2.0, -0.4F);
        this.body.mirror = true;
        this.body.setTextureOffset(0, 97).addBox(1.0F, 0.0F, 1.0F, (int)3.0, (int)9.0, (int)2.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(58, 53).addBox(1.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = true;
        this.body.setTextureOffset(92, 45).addBox(1.0F, 1.0F, -3.0F, (int)3.0, (int)4.0, (int)2.0, -0.4F);
        this.body.mirror = true;
        this.body.setTextureOffset(42, 98).addBox(1.0F, 0.0F, -3.0F, (int)3.0, (int)9.0, (int)2.0, -0.7F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(5.0F, 9.0F, -0.05F);
        setRotationAngle(this.body_r1, 0.0F, 0.0F, 0.1309F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(12, 66).addBox(-1.0F, -2.0F, -1.95F, (int)2.0, (int)2.0, (int)4.0, 0.05F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(40, 68).addBox(-1.0F, -2.0F, -1.95F, (int)2.0, (int)4.0, (int)4.0, -0.1F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(44, 43).addBox(-1.0F, -2.0F, -2.45F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(68, 24).addBox(-1.0F, -2.0F, 1.45F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(74, 24).addBox(-1.0F, 0.5F, -1.95F, (int)2.0, (int)1.0, (int)4.0, 0.1F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(-3.0F, 9.25F, -3.7F);
        setRotationAngle(this.body_r2, 0.0928F, 0.3477F, 0.0317F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(76, 34).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(30, 27).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(0, 66).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(52, 0).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(3.0F, 2.25F, -3.7F);
        setRotationAngle(this.body_r3, 0.0873F, -0.0435F, -0.0038F);
        this.body_r3.mirror = true;
        this.body_r3.setTextureOffset(30, 27).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r3.mirror = true;
        this.body_r3.setTextureOffset(0, 66).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(3.0F, 9.25F, -3.7F);
        setRotationAngle(this.body_r4, 0.0928F, -0.3477F, -0.0317F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(52, 0).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(0, 66).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(30, 27).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(76, 34).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(-5.0F, 9.0F, -0.05F);
        setRotationAngle(this.body_r5, 0.0F, 0.0F, -0.1309F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(40, 68).addBox(-1.0F, -2.0F, -1.95F, (int)2.0, (int)4.0, (int)4.0, -0.1F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(12, 66).addBox(-1.0F, -2.0F, -1.95F, (int)2.0, (int)2.0, (int)4.0, 0.05F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(68, 24).addBox(-1.0F, -2.0F, 1.45F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(44, 43).addBox(-1.0F, -2.0F, -2.45F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(74, 24).addBox(-1.0F, 0.5F, -1.95F, (int)2.0, (int)1.0, (int)4.0, 0.1F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(0.0F, 9.75F, 3.5F);
        setRotationAngle(this.body_r6, -0.1309F, 0.0F, 0.0F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(30, 66).addBox(-2.0F, -0.75F, -1.5F, (int)4.0, (int)3.0, (int)3.0, 0.05F);
        this.body_r6.mirror = true;
        this.body_r6.setTextureOffset(52, 71).addBox(-4.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(24, 73).addBox(1.0F, -0.75F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.1F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(70, 53).addBox(-2.0F, -1.75F, -1.5F, (int)4.0, (int)2.0, (int)3.0, 0.2F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(33, 75).addBox(1.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r6.mirror = true;
        this.body_r6.setTextureOffset(33, 75).addBox(-2.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(52, 71).addBox(1.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body_r6.mirror = true;
        this.body_r6.setTextureOffset(24, 73).addBox(-4.0F, -0.75F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.1F);
        this.body.addChild(this.body_r6);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(-10.0F, 0.0F, 0.0F);
        this.left_arm.mirror = false;
        this.left_arm.setTextureOffset(52, 12).addBox(9.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(10.0F, 0.0F, 0.0F);
        this.right_arm.mirror = true;
        this.right_arm.setTextureOffset(52, 12).addBox(-13.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.bipedRightArm.addChild(this.right_arm);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(40, 0).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(74, 65).addBox(-2.0F, 0.0F, 0.0F, (int)4.0, (int)4.0, (int)1.0, 0.25F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(40, 0).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(74, 65).addBox(-2.0F, 0.0F, 0.0F, (int)4.0, (int)4.0, (int)1.0, 0.25F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(64, 48).addBox(-2.0F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(64, 11).addBox(-2.0F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(0, 0).addBox(-4.0F, 1.0F, -1.0F, (int)2.0, (int)5.0, (int)3.0, -0.1F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(24, 27).addBox(-4.0F, 2.0F, -2.0F, (int)2.0, (int)4.0, (int)2.0, -0.2F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(0, 43).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.4F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(90, 19).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(107, 27).addBox(-2.0F, 5.0F, -2.0F, (int)4.0, (int)7.0, (int)4.0, 0.6F);
        this.bipedRightLeg.addChild(this.right_shoe);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(0, 43).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.4F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(90, 19).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(107, 27).addBox(-2.0F, 5.0F, -2.0F, (int)4.0, (int)7.0, (int)4.0, 0.6F);
        this.bipedLeftLeg.addChild(this.left_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
