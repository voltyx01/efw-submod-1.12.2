package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelfire_fighter extends ModelBiped {

    public ModelRenderer head;
    public ModelRenderer head_r1;
    public ModelRenderer head_r2;
    public ModelRenderer head_r3;
    public ModelRenderer head_r4;
    public ModelRenderer head_r5;
    public ModelRenderer head_r6;
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
    public ModelRenderer left_arm;
    public ModelRenderer left_arm_r1;
    public ModelRenderer right_arm;
    public ModelRenderer right_arm_r1;
    public ModelRenderer left_leg;
    public ModelRenderer right_leg;
    public ModelRenderer right_shoe;
    public ModelRenderer left_shoe;

    public Modelfire_fighter() {
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
        this.head.setTextureOffset(0, 112).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(24, 22).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)3.0, (int)8.0, 0.6F);
        this.head.mirror = false;
        this.head.setTextureOffset(24, 14).addBox(-5.0F, -9.0F, -1.0F, (int)10.0, (int)4.0, (int)2.0, 0.1F);
        this.head.mirror = false;
        this.head.setTextureOffset(24, 33).addBox(-1.0F, -9.0F, -5.0F, (int)2.0, (int)4.0, (int)10.0, 0.05F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(0.0F, -4.5F, 0.5F);
        setRotationAngle(this.head_r1, -0.0433F, -0.0018F, 0.0052F);
        this.head_r1.mirror = true;
        this.head_r1.setTextureOffset(0, 0).addBox(-5.0F, -0.5F, -6.5F, (int)10.0, (int)1.0, (int)13.0, 0.05F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(5.0529F, -2.2044F, -0.5F);
        setRotationAngle(this.head_r2, -0.0873F, 0.0F, -0.2618F);
        this.head_r2.mirror = true;
        this.head_r2.setTextureOffset(39, 38).addBox(-0.5F, -2.0F, -4.5F, (int)1.0, (int)4.0, (int)9.0, 0.05F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(-5.0529F, -2.2044F, -0.5F);
        setRotationAngle(this.head_r3, -0.0873F, 0.0F, 0.2618F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(39, 38).addBox(-0.5F, -2.0F, -4.5F, (int)1.0, (int)4.0, (int)9.0, 0.05F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(0.0F, -8.0F, 3.5F);
        setRotationAngle(this.head_r4, 0.1745F, 0.0F, 0.0F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(60, 50).addBox(-4.0F, 4.0F, -0.5F, (int)8.0, (int)4.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(0.0F, -4.5F, 0.5F);
        setRotationAngle(this.head_r5, -0.0433F, 0.0018F, -0.0052F);
        this.head_r5.mirror = false;
        this.head_r5.setTextureOffset(0, 0).addBox(-5.0F, -0.5F, -6.5F, (int)10.0, (int)1.0, (int)13.0, 0.05F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(0.0F, -6.8F, -5.0F);
        setRotationAngle(this.head_r6, 0.1745F, 0.0F, 0.0F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(66, 30).addBox(-3.0F, -3.0F, -1.0F, (int)6.0, (int)5.0, (int)2.0, -0.3F);
        this.head.addChild(this.head_r6);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 30).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.3F);
        this.body.mirror = false;
        this.body.setTextureOffset(56, 3).addBox(-5.0F, 6.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.6F);
        this.body.mirror = false;
        this.body.setTextureOffset(44, 0).addBox(-5.0F, 3.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.6F);
        this.body.mirror = true;
        this.body.setTextureOffset(44, 0).addBox(2.0F, 3.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.6F);
        this.body.mirror = true;
        this.body.setTextureOffset(56, 3).addBox(2.0F, 6.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.6F);
        this.body.mirror = false;
        this.body.setTextureOffset(78, 45).addBox(-4.0F, 0.0F, 1.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(80, 26).addBox(-4.0F, 0.0F, -3.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(80, 26).addBox(1.0F, 0.0F, -3.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(12, 63).addBox(-3.0F, 3.0F, -3.0F, (int)6.0, (int)3.0, (int)2.0, -0.6F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 62).addBox(-4.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = false;
        this.body.setTextureOffset(70, 21).addBox(-3.0F, 3.0F, 1.0F, (int)6.0, (int)3.0, (int)2.0, -0.6F);
        this.body.mirror = true;
        this.body.setTextureOffset(0, 62).addBox(1.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = true;
        this.body.setTextureOffset(78, 45).addBox(1.0F, 0.0F, 1.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(-2.0F, 8.75F, -2.5F);
        setRotationAngle(this.body_r1, 0.134F, 0.2608F, 0.0233F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(72, 38).addBox(1.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(2.5F, 5.5F, 2.0F);
        setRotationAngle(this.body_r2, 0.0F, 0.0F, -0.0873F);
        this.body_r2.mirror = true;
        this.body_r2.setTextureOffset(38, 33).addBox(-1.2F, -4.5F, -1.0F, (int)3.0, (int)8.0, (int)2.0, -0.4F);
        this.body_r2.mirror = true;
        this.body_r2.setTextureOffset(0, 0).addBox(-1.2F, -4.5F, -5.0F, (int)3.0, (int)8.0, (int)2.0, -0.4F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(1.5F, 11.5F, 0.0F);
        setRotationAngle(this.body_r3, 0.0F, 0.0F, -0.3491F);
        this.body_r3.mirror = true;
        this.body_r3.setTextureOffset(42, 62).addBox(-0.5F, -0.5F, -2.0F, (int)3.0, (int)7.0, (int)4.0, 0.5F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(3.7553F, 3.1755F, -3.2673F);
        setRotationAngle(this.body_r4, -0.1479F, -0.4025F, -0.0311F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(0, 10).addBox(-0.5F, -1.25F, -1.5F, (int)1.0, (int)1.0, (int)2.0, 0.3F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(80, 16).addBox(-1.5F, -2.25F, -1.5F, (int)3.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(10, 75).addBox(-1.5F, -2.25F, -0.5F, (int)3.0, (int)6.0, (int)2.0, -0.4F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(-2.5F, 5.5F, -2.0F);
        setRotationAngle(this.body_r5, 0.0F, 0.0F, 0.0873F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(0, 0).addBox(-1.8F, -4.5F, -1.0F, (int)3.0, (int)8.0, (int)2.0, -0.4F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(38, 33).addBox(-1.8F, -4.5F, 3.0F, (int)3.0, (int)8.0, (int)2.0, -0.4F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(-0.2443F, 8.75F, 2.4678F);
        setRotationAngle(this.body_r6, -3.0334F, -3.0E-4F, 3.1399F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(27, 73).addBox(1.2443F, -1.7486F, -1.5323F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(57, 82).addBox(1.2443F, -0.7486F, -1.5323F, (int)3.0, (int)2.0, (int)3.0, -0.1F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(70, 55).addBox(-1.7557F, -0.7486F, -1.5323F, (int)4.0, (int)3.0, (int)3.0, 0.05F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(40, 80).addBox(1.7443F, -1.7486F, -1.5323F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(72, 65).addBox(-1.7557F, -1.7486F, -1.5323F, (int)4.0, (int)2.0, (int)3.0, 0.2F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(33, 0).addBox(-2.2557F, -1.7486F, -1.5323F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(72, 38).addBox(-3.7557F, -1.7486F, -1.5323F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(61, 82).addBox(-3.7557F, -0.7486F, -1.5323F, (int)2.0, (int)2.0, (int)3.0, -0.1F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(2.0F, 8.75F, -2.5F);
        setRotationAngle(this.body_r7, 0.134F, -0.2608F, -0.0233F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(27, 73).addBox(1.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(40, 80).addBox(1.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(2.0F, 8.75F, -2.5F);
        setRotationAngle(this.body_r8, 0.134F, -0.2608F, -0.0233F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(61, 82).addBox(-4.0F, -0.75F, -1.5F, (int)2.0, (int)2.0, (int)3.0, -0.1F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(57, 82).addBox(1.0F, -0.75F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.1F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(72, 65).addBox(-2.0F, -1.75F, -1.5F, (int)4.0, (int)2.0, (int)3.0, 0.2F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(33, 0).addBox(-2.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(70, 55).addBox(-2.0F, -0.75F, -1.5F, (int)4.0, (int)3.0, (int)3.0, 0.05F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(72, 38).addBox(-4.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(0.0F, 3.75F, -2.5F);
        setRotationAngle(this.body_r9, 0.0436F, 0.0F, 0.0F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(74, 6).addBox(-2.0F, -1.75F, -1.5F, (int)4.0, (int)2.0, (int)3.0, 0.2F);
        this.body_r9.mirror = true;
        this.body_r9.setTextureOffset(0, 14).addBox(-2.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(51, 74).addBox(-4.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(69, 71).addBox(-2.0F, -0.75F, -1.5F, (int)4.0, (int)3.0, (int)3.0, 0.05F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(0.0F, 3.75F, -2.5F);
        setRotationAngle(this.body_r10, 0.0436F, 0.0F, 0.0F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(0, 14).addBox(1.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(75, 77).addBox(-4.0F, -0.75F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.1F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(-3.0F, 9.25F, -3.7F);
        setRotationAngle(this.body_r11, 0.0983F, 0.478F, 0.0453F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(12, 46).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(4, 10).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(44, 0).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body.addChild(this.body_r11);

        this.body_r12 = new ModelRenderer(this);
        this.body_r12.setRotationPoint(-3.0F, 9.25F, -3.7F);
        setRotationAngle(this.body_r12, 0.0928F, 0.3477F, 0.0317F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(0, 30).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r12);

        this.body_r13 = new ModelRenderer(this);
        this.body_r13.setRotationPoint(-1.5F, 11.5F, 0.0F);
        setRotationAngle(this.body_r13, 0.0F, 0.0F, 0.3491F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(42, 62).addBox(-2.5F, -0.5F, -2.0F, (int)3.0, (int)7.0, (int)4.0, 0.5F);
        this.body.addChild(this.body_r13);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(14, 69).addBox(-1.0F, 7.0F, -2.0F, (int)4.0, (int)2.0, (int)4.0, 0.4F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(50, 33).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)10.0, (int)4.0, 0.1F);
        this.left_arm.mirror = false;
        this.left_arm.setTextureOffset(93, 0).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(1.0F, 1.5F, 0.0F);
        setRotationAngle(this.left_arm_r1, 0.0F, 0.0F, -0.0436F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(56, 65).addBox(-1.7F, -2.5F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.6F);
        this.left_arm.addChild(this.left_arm_r1);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(50, 33).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)10.0, (int)4.0, 0.1F);
        this.right_arm.mirror = true;
        this.right_arm.setTextureOffset(93, 0).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.05F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(14, 69).addBox(-3.0F, 7.0F, -2.0F, (int)4.0, (int)2.0, (int)4.0, 0.4F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-1.0F, 1.5F, 0.0F);
        setRotationAngle(this.right_arm_r1, 0.0F, 0.0F, 0.0436F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(56, 65).addBox(-2.3F, -2.5F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.6F);
        this.right_arm.addChild(this.right_arm_r1);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(0, 46).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.25F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(0, 46).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.25F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(32, 51).addBox(-2.0F, 5.0F, -2.0F, (int)4.0, (int)7.0, (int)4.0, 0.4F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(90, 57).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(86, 34).addBox(-2.0F, 5.0F, -2.0F, (int)4.0, (int)7.0, (int)4.0, 0.49F);
        this.bipedRightLeg.addChild(this.right_shoe);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(86, 34).addBox(-2.0F, 5.0F, -2.0F, (int)4.0, (int)7.0, (int)4.0, 0.49F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(90, 57).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(32, 51).addBox(-2.0F, 5.0F, -2.0F, (int)4.0, (int)7.0, (int)4.0, 0.4F);
        this.bipedLeftLeg.addChild(this.left_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
