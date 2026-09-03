package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelexo_suit_armor extends ModelBiped {

    public ModelRenderer head;
    public ModelRenderer head_r1;
    public ModelRenderer head_r2;
    public ModelRenderer head_r3;
    public ModelRenderer head_r4;
    public ModelRenderer head_r5;
    public ModelRenderer head_r6;
    public ModelRenderer head_r7;
    public ModelRenderer head_r8;
    public ModelRenderer head_r9;
    public ModelRenderer body;
    public ModelRenderer body_r1;
    public ModelRenderer body_r2;
    public ModelRenderer body_r3;
    public ModelRenderer body_r4;
    public ModelRenderer body_r5;
    public ModelRenderer body_r6;
    public ModelRenderer body_r7;
    public ModelRenderer body_r8;
    public ModelRenderer body_r9;
    public ModelRenderer body_r10;
    public ModelRenderer body_r11;
    public ModelRenderer body_r12;
    public ModelRenderer body_r13;
    public ModelRenderer body_r14;
    public ModelRenderer body_r15;
    public ModelRenderer left_arm;
    public ModelRenderer left_arm_r1;
    public ModelRenderer left_arm_r2;
    public ModelRenderer left_arm_r3;
    public ModelRenderer left_arm_r4;
    public ModelRenderer left_arm_r5;
    public ModelRenderer left_arm_r6;
    public ModelRenderer left_arm_r7;
    public ModelRenderer right_arm;
    public ModelRenderer right_arm_r1;
    public ModelRenderer right_arm_r2;
    public ModelRenderer right_arm_r3;
    public ModelRenderer right_arm_r4;
    public ModelRenderer right_arm_r5;
    public ModelRenderer right_arm_r6;
    public ModelRenderer right_arm_r7;
    public ModelRenderer left_leg;
    public ModelRenderer left_leg_r1;
    public ModelRenderer left_leg_r2;
    public ModelRenderer left_leg_r3;
    public ModelRenderer left_leg_r4;
    public ModelRenderer left_leg_r5;
    public ModelRenderer left_leg_r6;
    public ModelRenderer right_leg;
    public ModelRenderer right_leg_r1;
    public ModelRenderer right_leg_r2;
    public ModelRenderer right_leg_r3;
    public ModelRenderer right_leg_r4;
    public ModelRenderer right_leg_r5;
    public ModelRenderer right_leg_r6;
    public ModelRenderer right_shoe;
    public ModelRenderer left_shoe_r2;
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe_r2;

    public Modelexo_suit_armor() {
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
        this.head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.05F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(3.4088F, -0.8426F, -1.65F);
        setRotationAngle(this.head_r1, 0.0999F, -0.5148F, 0.1918F);
        this.head_r1.mirror = true;
        this.head_r1.setTextureOffset(24, 4).addBox(-2.827F, -0.2834F, -2.7217F, (int)3.0, (int)1.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(3.4088F, -0.8426F, -1.65F);
        setRotationAngle(this.head_r2, -0.0382F, -0.1434F, 1.8215F);
        this.head_r2.mirror = true;
        this.head_r2.setTextureOffset(23, 7).addBox(-1.0976F, -1.381F, -2.3512F, (int)2.0, (int)2.0, (int)9.0, 0.05F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(4.0F, -3.5F, 0.0F);
        setRotationAngle(this.head_r3, -0.0382F, -0.1434F, 1.1233F);
        this.head_r3.mirror = true;
        this.head_r3.setTextureOffset(46, 52).addBox(-1.0F, -0.5F, 1.0F, (int)2.0, (int)2.0, (int)4.0, 0.05F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(-3.4088F, -0.8426F, -1.65F);
        setRotationAngle(this.head_r4, 0.0999F, 0.5148F, -0.1918F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(24, 4).addBox(-0.173F, -0.2834F, -2.7217F, (int)3.0, (int)1.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(-3.4088F, -0.8426F, -1.65F);
        setRotationAngle(this.head_r5, -0.0382F, 0.1434F, -1.8215F);
        this.head_r5.mirror = false;
        this.head_r5.setTextureOffset(23, 7).addBox(-0.9024F, -1.381F, -2.3512F, (int)2.0, (int)2.0, (int)9.0, 0.05F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(-4.0F, -3.5F, 0.0F);
        setRotationAngle(this.head_r6, -0.0382F, 0.1434F, -1.1233F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(46, 52).addBox(-1.0F, -0.5F, 1.0F, (int)2.0, (int)2.0, (int)4.0, 0.05F);
        this.head.addChild(this.head_r6);

        this.head_r7 = new ModelRenderer(this);
        this.head_r7.setRotationPoint(0.0F, -4.7559F, 5.2365F);
        setRotationAngle(this.head_r7, -0.0436F, 0.0F, 0.0F);
        this.head_r7.mirror = false;
        this.head_r7.setTextureOffset(24, 0).addBox(-3.0F, 0.5F, -1.5F, (int)6.0, (int)2.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r7);

        this.head_r8 = new ModelRenderer(this);
        this.head_r8.setRotationPoint(0.0F, -5.5593F, 4.4589F);
        setRotationAngle(this.head_r8, 0.0873F, 0.0F, 0.0F);
        this.head_r8.mirror = false;
        this.head_r8.setTextureOffset(16, 62).addBox(-1.0F, -1.5F, -1.0F, (int)2.0, (int)4.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r8);

        this.head_r9 = new ModelRenderer(this);
        this.head_r9.setRotationPoint(0.0F, -4.0F, 5.0F);
        setRotationAngle(this.head_r9, -0.2618F, 0.0F, 0.0F);
        this.head_r9.mirror = false;
        this.head_r9.setTextureOffset(8, 61).addBox(-1.0F, 1.0F, -1.0F, (int)2.0, (int)4.0, (int)2.0, -0.2F);
        this.head.addChild(this.head_r9);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 16).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.05F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(3.0F, 8.5F, 0.0F);
        setRotationAngle(this.body_r1, 0.0F, 0.1309F, 0.1309F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(50, 47).addBox(-2.5F, -0.5F, -1.0F, (int)4.0, (int)1.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(-2.5F, 2.9802F, 2.8021F);
        setRotationAngle(this.body_r2, 0.1476F, -0.3562F, -0.2346F);
        this.body_r2.mirror = true;
        this.body_r2.setTextureOffset(67, 18).addBox(-0.5F, -2.0F, -1.0F, (int)2.0, (int)4.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(-3.0F, 8.5F, 0.0F);
        setRotationAngle(this.body_r3, 0.0F, -0.1309F, -0.1309F);
        this.body_r3.mirror = true;
        this.body_r3.setTextureOffset(50, 47).addBox(-1.5F, -0.5F, -1.0F, (int)4.0, (int)1.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(-3.0F, 2.5F, 0.0F);
        setRotationAngle(this.body_r4, 0.0F, -0.2182F, 0.2618F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(52, 0).addBox(-1.5F, -0.5F, -1.0F, (int)4.0, (int)1.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(-3.0F, 5.5F, 0.0F);
        setRotationAngle(this.body_r5, 0.0F, -0.2182F, 0.2618F);
        this.body_r5.mirror = true;
        this.body_r5.setTextureOffset(17, 58).addBox(-1.5F, -0.5F, 0.0F, (int)3.0, (int)1.0, (int)3.0, 0.05F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(3.1746F, 10.523F, -1.1041F);
        setRotationAngle(this.body_r6, -2.9001F, 0.1434F, 2.2786F);
        this.body_r6.mirror = true;
        this.body_r6.setTextureOffset(61, 32).addBox(-2.0F, -0.5F, 0.6041F, (int)4.0, (int)1.0, (int)1.0, 0.2F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(3.0F, 2.5F, 0.0F);
        setRotationAngle(this.body_r7, 0.0F, 0.2182F, -0.2618F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(52, 0).addBox(-2.5F, -0.5F, -1.0F, (int)4.0, (int)1.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(3.0F, 5.5F, 0.0F);
        setRotationAngle(this.body_r8, 0.0F, 0.2182F, -0.2618F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(17, 58).addBox(-1.5F, -0.5F, 0.0F, (int)3.0, (int)1.0, (int)3.0, 0.05F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(0.0F, 4.7114F, 2.9438F);
        setRotationAngle(this.body_r9, 0.3491F, 0.0F, 0.0F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(0, 0).addBox(-1.0F, -5.5709F, -0.1553F, (int)2.0, (int)6.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(0.0F, 4.7114F, 2.9438F);
        setRotationAngle(this.body_r10, -0.3491F, 0.0F, 0.0F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(43, 58).addBox(-1.0F, -0.8706F, -0.316F, (int)2.0, (int)6.0, (int)2.0, -0.1F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(2.5F, 2.9802F, 2.8021F);
        setRotationAngle(this.body_r11, 0.1476F, 0.3562F, 0.2346F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(67, 18).addBox(-1.5F, -2.0F, -1.0F, (int)2.0, (int)4.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r11);

        this.body_r12 = new ModelRenderer(this);
        this.body_r12.setRotationPoint(0.0F, 3.5F, 2.75F);
        setRotationAngle(this.body_r12, 0.1309F, 0.0F, 0.0F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(24, 18).addBox(-1.0F, -4.5F, -2.75F, (int)2.0, (int)2.0, (int)3.0, 0.05F);
        this.body.addChild(this.body_r12);

        this.body_r13 = new ModelRenderer(this);
        this.body_r13.setRotationPoint(0.0F, 4.5F, 2.75F);
        setRotationAngle(this.body_r13, 0.1309F, 0.0F, 0.0F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(0, 53).addBox(-2.0F, -4.5F, -0.75F, (int)4.0, (int)7.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r13);

        this.body_r14 = new ModelRenderer(this);
        this.body_r14.setRotationPoint(-4.2261F, 11.6787F, -0.5F);
        setRotationAngle(this.body_r14, 0.0F, 0.3927F, -0.1309F);
        this.body_r14.mirror = false;
        this.body_r14.setTextureOffset(66, 46).addBox(-1.0F, -2.5F, -0.5F, (int)2.0, (int)4.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r14);

        this.body_r15 = new ModelRenderer(this);
        this.body_r15.setRotationPoint(-3.1746F, 10.523F, -1.1041F);
        setRotationAngle(this.body_r15, -2.9001F, -0.1434F, -2.2786F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(61, 32).addBox(-2.0F, -0.5F, 0.6041F, (int)4.0, (int)1.0, (int)1.0, 0.2F);
        this.body.addChild(this.body_r15);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(33, 30).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(1.0F, 7.5F, 0.0F);
        setRotationAngle(this.left_arm_r1, 0.0F, 0.0F, -0.48F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(43, 40).addBox(-3.0F, -0.5F, -3.0F, (int)6.0, (int)1.0, (int)6.0, 0.05F);
        this.left_arm.addChild(this.left_arm_r1);

        this.left_arm_r2 = new ModelRenderer(this);
        this.left_arm_r2.setRotationPoint(2.8622F, 5.3929F, 0.0143F);
        setRotationAngle(this.left_arm_r2, -0.2736F, -0.1602F, 0.1295F);
        this.left_arm_r2.mirror = true;
        this.left_arm_r2.setTextureOffset(27, 60).addBox(-1.0F, -3.5F, -1.0F, (int)2.0, (int)5.0, (int)2.0, 0.05F);
        this.left_arm.addChild(this.left_arm_r2);

        this.left_arm_r3 = new ModelRenderer(this);
        this.left_arm_r3.setRotationPoint(3.5518F, -1.756F, 0.3796F);
        setRotationAngle(this.left_arm_r3, 1.192F, 0.0519F, -0.4877F);
        this.left_arm_r3.mirror = true;
        this.left_arm_r3.setTextureOffset(44, 28).addBox(-0.75F, -0.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.2F);
        this.left_arm.addChild(this.left_arm_r3);

        this.left_arm_r4 = new ModelRenderer(this);
        this.left_arm_r4.setRotationPoint(3.5518F, -1.756F, 0.3796F);
        setRotationAngle(this.left_arm_r4, 0.5375F, 0.0519F, -0.4877F);
        this.left_arm_r4.mirror = true;
        this.left_arm_r4.setTextureOffset(59, 61).addBox(-0.75F, -1.5F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.3F);
        this.left_arm.addChild(this.left_arm_r4);

        this.left_arm_r5 = new ModelRenderer(this);
        this.left_arm_r5.setRotationPoint(1.0F, 2.5F, 0.0F);
        setRotationAngle(this.left_arm_r5, 0.0F, 0.0F, 0.3054F);
        this.left_arm_r5.mirror = true;
        this.left_arm_r5.setTextureOffset(45, 25).addBox(-3.0F, -0.5F, -3.0F, (int)6.0, (int)1.0, (int)6.0, 0.05F);
        this.left_arm.addChild(this.left_arm_r5);

        this.left_arm_r6 = new ModelRenderer(this);
        this.left_arm_r6.setRotationPoint(0.5F, -2.5F, -0.5F);
        setRotationAngle(this.left_arm_r6, -0.7418F, 0.0F, 0.0F);
        this.left_arm_r6.mirror = true;
        this.left_arm_r6.setTextureOffset(58, 17).addBox(-1.5F, 0.0F, 0.5F, (int)4.0, (int)1.0, (int)1.0, 0.65F);
        this.left_arm.addChild(this.left_arm_r6);

        this.left_arm_r7 = new ModelRenderer(this);
        this.left_arm_r7.setRotationPoint(2.5F, 1.0F, 0.0F);
        setRotationAngle(this.left_arm_r7, 0.1946F, -0.2191F, -0.266F);
        this.left_arm_r7.mirror = true;
        this.left_arm_r7.setTextureOffset(61, 37).addBox(-0.5F, -3.0F, -1.0F, (int)2.0, (int)5.0, (int)2.0, 0.05F);
        this.left_arm.addChild(this.left_arm_r7);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(33, 30).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-2.8622F, 5.3929F, 0.0143F);
        setRotationAngle(this.right_arm_r1, -0.2736F, 0.1602F, -0.1295F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(27, 60).addBox(-1.0F, -3.5F, -1.0F, (int)2.0, (int)5.0, (int)2.0, 0.05F);
        this.right_arm.addChild(this.right_arm_r1);

        this.right_arm_r2 = new ModelRenderer(this);
        this.right_arm_r2.setRotationPoint(-3.5518F, -1.756F, 0.3796F);
        setRotationAngle(this.right_arm_r2, 0.5375F, -0.0519F, 0.4877F);
        this.right_arm_r2.mirror = false;
        this.right_arm_r2.setTextureOffset(59, 61).addBox(-0.25F, -1.5F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.3F);
        this.right_arm.addChild(this.right_arm_r2);

        this.right_arm_r3 = new ModelRenderer(this);
        this.right_arm_r3.setRotationPoint(-3.5518F, -1.756F, 0.3796F);
        setRotationAngle(this.right_arm_r3, 1.192F, -0.0519F, 0.4877F);
        this.right_arm_r3.mirror = false;
        this.right_arm_r3.setTextureOffset(44, 28).addBox(-1.25F, -0.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.2F);
        this.right_arm.addChild(this.right_arm_r3);

        this.right_arm_r4 = new ModelRenderer(this);
        this.right_arm_r4.setRotationPoint(-1.0F, 7.5F, 0.0F);
        setRotationAngle(this.right_arm_r4, 0.0F, 0.0F, 0.48F);
        this.right_arm_r4.mirror = false;
        this.right_arm_r4.setTextureOffset(43, 40).addBox(-3.0F, -0.5F, -3.0F, (int)6.0, (int)1.0, (int)6.0, 0.05F);
        this.right_arm.addChild(this.right_arm_r4);

        this.right_arm_r5 = new ModelRenderer(this);
        this.right_arm_r5.setRotationPoint(-1.0F, 2.5F, 0.0F);
        setRotationAngle(this.right_arm_r5, 0.0F, 0.0F, -0.3054F);
        this.right_arm_r5.mirror = false;
        this.right_arm_r5.setTextureOffset(45, 25).addBox(-3.0F, -0.5F, -3.0F, (int)6.0, (int)1.0, (int)6.0, 0.05F);
        this.right_arm.addChild(this.right_arm_r5);

        this.right_arm_r6 = new ModelRenderer(this);
        this.right_arm_r6.setRotationPoint(-0.5F, -2.5F, -0.5F);
        setRotationAngle(this.right_arm_r6, -0.7418F, 0.0F, 0.0F);
        this.right_arm_r6.mirror = false;
        this.right_arm_r6.setTextureOffset(58, 17).addBox(-2.5F, 0.0F, 0.5F, (int)4.0, (int)1.0, (int)1.0, 0.65F);
        this.right_arm.addChild(this.right_arm_r6);

        this.right_arm_r7 = new ModelRenderer(this);
        this.right_arm_r7.setRotationPoint(-2.5F, 1.0F, 0.0F);
        setRotationAngle(this.right_arm_r7, 0.1946F, 0.2191F, 0.266F);
        this.right_arm_r7.mirror = false;
        this.right_arm_r7.setTextureOffset(61, 37).addBox(-1.5F, -3.0F, -1.0F, (int)2.0, (int)5.0, (int)2.0, 0.05F);
        this.right_arm.addChild(this.right_arm_r7);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(36, 0).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.left_leg_r1 = new ModelRenderer(this);
        this.left_leg_r1.setRotationPoint(3.7993F, 4.2227F, 0.4075F);
        setRotationAngle(this.left_leg_r1, 0.883F, -0.1116F, -0.0857F);
        this.left_leg_r1.mirror = true;
        this.left_leg_r1.setTextureOffset(28, 30).addBox(-1.0F, -0.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.05F);
        this.left_leg.addChild(this.left_leg_r1);

        this.left_leg_r2 = new ModelRenderer(this);
        this.left_leg_r2.setRotationPoint(1.5F, 6.2753F, -0.0298F);
        setRotationAngle(this.left_leg_r2, 0.2285F, -0.1116F, -0.0857F);
        this.left_leg_r2.mirror = true;
        this.left_leg_r2.setTextureOffset(55, 55).addBox(0.5F, -3.2638F, -0.9255F, (int)2.0, (int)3.0, (int)3.0, 0.05F);
        this.left_leg.addChild(this.left_leg_r2);

        this.left_leg_r3 = new ModelRenderer(this);
        this.left_leg_r3.setRotationPoint(0.0F, 9.5F, 0.0F);
        setRotationAngle(this.left_leg_r3, 0.0F, 0.0F, -0.1309F);
        this.left_leg_r3.mirror = true;
        this.left_leg_r3.setTextureOffset(49, 32).addBox(-2.0F, -0.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.left_leg.addChild(this.left_leg_r3);

        this.left_leg_r4 = new ModelRenderer(this);
        this.left_leg_r4.setRotationPoint(0.0F, 2.5F, 0.0F);
        setRotationAngle(this.left_leg_r4, 0.0F, 0.0F, -0.1309F);
        this.left_leg_r4.mirror = true;
        this.left_leg_r4.setTextureOffset(12, 50).addBox(-2.0F, -0.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.left_leg.addChild(this.left_leg_r4);

        this.left_leg_r5 = new ModelRenderer(this);
        this.left_leg_r5.setRotationPoint(0.5F, 5.2753F, -0.0298F);
        setRotationAngle(this.left_leg_r5, 2.9234F, -0.1309F, 0.0F);
        this.left_leg_r5.mirror = true;
        this.left_leg_r5.setTextureOffset(62, 52).addBox(0.5F, -4.7264F, -1.7436F, (int)2.0, (int)4.0, (int)2.0, 0.3F);
        this.left_leg_r5.mirror = true;
        this.left_leg_r5.setTextureOffset(65, 58).addBox(0.5F, -3.7264F, -0.7436F, (int)2.0, (int)4.0, (int)1.0, 0.05F);
        this.left_leg.addChild(this.left_leg_r5);

        this.left_leg_r6 = new ModelRenderer(this);
        this.left_leg_r6.setRotationPoint(0.5F, 5.2753F, -0.0298F);
        setRotationAngle(this.left_leg_r6, 0.2182F, -0.1309F, 0.0F);
        this.left_leg_r6.mirror = true;
        this.left_leg_r6.setTextureOffset(42, 66).addBox(0.5F, -4.2638F, 0.0745F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.left_leg.addChild(this.left_leg_r6);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(36, 0).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_leg_r1 = new ModelRenderer(this);
        this.right_leg_r1.setRotationPoint(-3.7993F, 4.2227F, 0.4075F);
        setRotationAngle(this.right_leg_r1, 0.883F, 0.1116F, 0.0857F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(28, 30).addBox(-1.0F, -0.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.05F);
        this.right_leg.addChild(this.right_leg_r1);

        this.right_leg_r2 = new ModelRenderer(this);
        this.right_leg_r2.setRotationPoint(0.0F, 9.5F, 0.0F);
        setRotationAngle(this.right_leg_r2, 0.0F, 0.0F, 0.1309F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(49, 32).addBox(-2.0F, -0.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.right_leg.addChild(this.right_leg_r2);

        this.right_leg_r3 = new ModelRenderer(this);
        this.right_leg_r3.setRotationPoint(-0.5F, 5.2753F, -0.0298F);
        setRotationAngle(this.right_leg_r3, 2.9234F, 0.1309F, 0.0F);
        this.right_leg_r3.mirror = false;
        this.right_leg_r3.setTextureOffset(62, 52).addBox(-2.5F, -4.7264F, -1.7436F, (int)2.0, (int)4.0, (int)2.0, 0.3F);
        this.right_leg_r3.mirror = false;
        this.right_leg_r3.setTextureOffset(65, 58).addBox(-2.5F, -3.7264F, -0.7436F, (int)2.0, (int)4.0, (int)1.0, 0.05F);
        this.right_leg.addChild(this.right_leg_r3);

        this.right_leg_r4 = new ModelRenderer(this);
        this.right_leg_r4.setRotationPoint(-1.5F, 6.2753F, -0.0298F);
        setRotationAngle(this.right_leg_r4, 0.2285F, 0.1116F, 0.0857F);
        this.right_leg_r4.mirror = false;
        this.right_leg_r4.setTextureOffset(55, 55).addBox(-2.5F, -3.2638F, -0.9255F, (int)2.0, (int)3.0, (int)3.0, 0.05F);
        this.right_leg.addChild(this.right_leg_r4);

        this.right_leg_r5 = new ModelRenderer(this);
        this.right_leg_r5.setRotationPoint(-0.5F, 5.2753F, -0.0298F);
        setRotationAngle(this.right_leg_r5, 0.2182F, 0.1309F, 0.0F);
        this.right_leg_r5.mirror = false;
        this.right_leg_r5.setTextureOffset(42, 66).addBox(-2.5F, -4.2638F, 0.0745F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.right_leg.addChild(this.right_leg_r5);

        this.right_leg_r6 = new ModelRenderer(this);
        this.right_leg_r6.setRotationPoint(0.0F, 2.5F, 0.0F);
        setRotationAngle(this.right_leg_r6, 0.0F, 0.0F, 0.1309F);
        this.right_leg_r6.mirror = false;
        this.right_leg_r6.setTextureOffset(12, 50).addBox(-2.0F, -0.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.right_leg.addChild(this.right_leg_r6);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(2.9F, 0.0F, 0.0F);
        setRotationAngle(this.right_shoe, 0.0F, 0.0F, 0.3927F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(0, 69).addBox(0.3F, -1.0F, -3.0F, (int)1.0, (int)1.0, (int)5.0, 0.05F);
        this.bipedRightLeg.addChild(this.right_shoe);

        this.left_shoe_r2 = new ModelRenderer(this);
        this.left_shoe_r2.setRotationPoint(2.0F, 12.0F, 0.0F);
        setRotationAngle(this.left_shoe_r2, 0.0F, 0.0F, 0.3927F);
        this.left_shoe_r2.mirror = true;
        this.left_shoe_r2.setTextureOffset(46, 22).addBox(-4.0F, 0.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.05F);
        this.right_shoe.addChild(this.left_shoe_r2);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(-3.9F, 0.0F, 0.0F);
        setRotationAngle(this.left_shoe, 0.0F, 0.0F, -0.3927F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(46, 22).addBox(0.0F, 0.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.05F);
        this.bipedLeftLeg.addChild(this.left_shoe);

        this.right_shoe_r2 = new ModelRenderer(this);
        this.right_shoe_r2.setRotationPoint(-1.0F, 12.0F, 0.0F);
        setRotationAngle(this.right_shoe_r2, 0.0F, 0.0F, -0.3927F);
        this.right_shoe_r2.mirror = false;
        this.right_shoe_r2.setTextureOffset(0, 69).addBox(-1.3F, -1.0F, -3.0F, (int)1.0, (int)1.0, (int)5.0, 0.05F);
        this.left_shoe.addChild(this.right_shoe_r2);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
