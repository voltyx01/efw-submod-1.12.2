package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelhazmat extends ModelBiped {

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
    public ModelRenderer right_shoe;
    public ModelRenderer left_shoe;

    public Modelhazmat() {
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
        this.head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.4F);
        this.head.mirror = false;
        this.head.setTextureOffset(0, 24).addBox(-5.0F, -3.0F, -1.0F, (int)10.0, (int)2.0, (int)6.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(29, 21).addBox(-4.0F, -5.0F, -5.0F, (int)8.0, (int)4.0, (int)3.0, 0.45F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(0.0F, 0.2025F, -4.5425F);
        setRotationAngle(this.head_r1, -1.1111F, 0.4176F, -0.6863F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(99, 49).addBox(-1.6736F, -0.8573F, -1.3264F, (int)3.0, (int)2.0, (int)3.0, 0.05F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(116, 62).addBox(-1.6736F, 0.1427F, -1.3264F, (int)3.0, (int)2.0, (int)3.0, -0.2F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(108, 68).addBox(-1.6736F, 1.6903F, -1.2839F, (int)3.0, (int)1.0, (int)3.0, 0.05F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(108, 74).addBox(-1.6736F, 1.4903F, -1.3264F, (int)3.0, (int)2.0, (int)3.0, -0.4F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(0.0F, -5.5F, -2.5F);
        setRotationAngle(this.head_r2, 0.0436F, 0.0F, 0.0F);
        this.head_r2.mirror = true;
        this.head_r2.setTextureOffset(0, 32).addBox(3.0F, 0.5F, 1.5F, (int)1.0, (int)3.0, (int)1.0, 0.65F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(0, 32).addBox(-4.0F, 0.5F, 1.5F, (int)1.0, (int)3.0, (int)1.0, 0.65F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(0.0F, -1.0F, -2.5F);
        setRotationAngle(this.head_r3, 0.1309F, 0.0F, 0.0F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(24, 0).addBox(-5.0F, -1.0F, -3.5F, (int)10.0, (int)2.0, (int)5.0, 0.05F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(-2.0F, -5.0F, 2.0F);
        setRotationAngle(this.head_r4, 0.0F, 0.0F, 1.5708F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(0, 95).addBox(-4.0F, -1.0F, -7.0F, (int)8.0, (int)2.0, (int)10.0, -0.1F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(2.0F, -5.0F, 2.0F);
        setRotationAngle(this.head_r5, 0.0F, 0.0F, -1.5708F);
        this.head_r5.mirror = true;
        this.head_r5.setTextureOffset(0, 95).addBox(-4.0F, -1.0F, -7.0F, (int)8.0, (int)2.0, (int)10.0, -0.1F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(0.0F, -5.5F, -2.5F);
        setRotationAngle(this.head_r6, -0.1745F, 0.0F, 0.0F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(27, 11).addBox(-5.0F, -1.5F, -3.5F, (int)10.0, (int)2.0, (int)5.0, 0.05F);
        this.head.addChild(this.head_r6);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(28, 28).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.15F);
        this.body.mirror = false;
        this.body.setTextureOffset(26, 18).addBox(-3.0F, 3.0F, 1.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(32, 7).addBox(-3.0F, 5.0F, 1.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(32, 9).addBox(-3.0F, 7.0F, 1.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(39, 19).addBox(-3.0F, 9.0F, 1.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(62, 61).addBox(-5.0F, 6.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(60, 34).addBox(-5.0F, 3.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(60, 34).addBox(2.0F, 3.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(62, 61).addBox(2.0F, 6.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 73).addBox(-3.0F, 9.0F, -2.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(78, 13).addBox(-3.0F, 7.0F, -2.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(80, 66).addBox(-3.0F, 5.0F, -2.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(80, 68).addBox(-3.0F, 3.0F, -2.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(47, 85).addBox(-4.0F, 0.0F, 1.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(47, 85).addBox(1.0F, 0.0F, 1.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(10, 86).addBox(-4.0F, 0.0F, -3.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(10, 86).addBox(1.0F, 0.0F, -3.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(52, 5).addBox(-4.0F, 2.0F, 1.0F, (int)8.0, (int)9.0, (int)2.0, -0.4F);
        this.body.mirror = false;
        this.body.setTextureOffset(66, 10).addBox(1.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = false;
        this.body.setTextureOffset(48, 56).addBox(-4.0F, 2.0F, -3.0F, (int)8.0, (int)9.0, (int)2.0, -0.4F);
        this.body.mirror = true;
        this.body.setTextureOffset(66, 10).addBox(-4.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(-2.0F, 4.5F, 5.0F);
        setRotationAngle(this.body_r1, 0.0F, 0.7854F, 0.0F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(64, 80).addBox(-1.0F, 3.5F, -2.0F, (int)3.0, (int)3.0, (int)3.0, -0.4F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(-2.0F, 8.5F, 4.0F);
        setRotationAngle(this.body_r2, 0.0F, -0.7854F, 0.0F);
        this.body_r2.mirror = true;
        this.body_r2.setTextureOffset(74, 70).addBox(-2.0F, -1.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.body_r2.mirror = true;
        this.body_r2.setTextureOffset(0, 75).addBox(-2.0F, -4.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.5F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(2.0F, 4.5F, 5.0F);
        setRotationAngle(this.body_r3, 0.0F, -0.7854F, 0.0F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(64, 80).addBox(-2.0F, 3.5F, -2.0F, (int)3.0, (int)3.0, (int)3.0, -0.4F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(-2.0F, 4.5F, 4.0F);
        setRotationAngle(this.body_r4, 0.0F, 0.7854F, 0.0F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(16, 56).addBox(-2.0F, -4.5F, -2.0F, (int)4.0, (int)9.0, (int)4.0, 0.05F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(72, 34).addBox(-2.0F, -3.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(2.0F, 2.5F, 4.0F);
        setRotationAngle(this.body_r5, 0.0F, -0.7854F, 0.0F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(72, 34).addBox(-2.0F, -1.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(16, 56).addBox(-2.0F, -2.5F, -2.0F, (int)4.0, (int)9.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(2.0F, 8.5F, 4.0F);
        setRotationAngle(this.body_r6, 0.0F, 0.7854F, 0.0F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(74, 70).addBox(-2.0F, -1.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(0, 75).addBox(-2.0F, -4.5F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.5F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(0.0F, 3.75F, -2.5F);
        setRotationAngle(this.body_r7, 0.0436F, 0.0F, 0.0F);
        this.body_r7.mirror = true;
        this.body_r7.setTextureOffset(84, 54).addBox(1.0F, -0.75F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.1F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(80, 49).addBox(-2.0F, -1.75F, -1.5F, (int)4.0, (int)2.0, (int)3.0, 0.2F);
        this.body_r7.mirror = true;
        this.body_r7.setTextureOffset(85, 83).addBox(-2.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(0, 80).addBox(-4.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(52, 34).addBox(-2.0F, -0.75F, -1.5F, (int)4.0, (int)3.0, (int)3.0, 0.05F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(0.0F, 3.75F, -2.5F);
        setRotationAngle(this.body_r8, 0.0436F, 0.0F, 0.0F);
        this.body_r8.mirror = true;
        this.body_r8.setTextureOffset(0, 80).addBox(1.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.4F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(85, 83).addBox(1.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(84, 54).addBox(-4.0F, -0.75F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.1F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(-3.0F, 9.25F, -3.7F);
        setRotationAngle(this.body_r9, 0.0928F, 0.3477F, 0.0317F);
        this.body_r9.mirror = true;
        this.body_r9.setTextureOffset(46, 7).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body_r9.mirror = true;
        this.body_r9.setTextureOffset(0, 6).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r9.mirror = true;
        this.body_r9.setTextureOffset(0, 16).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body_r9.mirror = true;
        this.body_r9.setTextureOffset(24, 32).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(0.0F, 9.25F, -3.7F);
        setRotationAngle(this.body_r10, 0.0873F, 0.0F, 0.0F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(16, 40).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(0, 24).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(26, 20).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(64, 43).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(3.0F, 9.25F, -3.7F);
        setRotationAngle(this.body_r11, 0.0928F, -0.3477F, -0.0317F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(24, 32).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(0, 16).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(0, 6).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(46, 7).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r11);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(68, 28).addBox(-1.0F, 6.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(52, 18).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(1.0F, 1.5F, 0.0F);
        setRotationAngle(this.left_arm_r1, 0.0F, 0.0F, -0.0436F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(94, 5).addBox(2.3F, -2.5F, -1.5F, (int)2.0, (int)5.0, (int)3.0, 0.05F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(68, 52).addBox(-1.7F, -2.5F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.6F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(107, 5).addBox(2.3F, -2.5F, -1.5F, (int)2.0, (int)3.0, (int)3.0, 0.5F);
        this.left_arm.addChild(this.left_arm_r1);

        this.left_arm_r2 = new ModelRenderer(this);
        this.left_arm_r2.setRotationPoint(1.0F, 0.5F, 0.0F);
        setRotationAngle(this.left_arm_r2, 0.0F, 0.0F, -0.0436F);
        this.left_arm_r2.mirror = true;
        this.left_arm_r2.setTextureOffset(95, 17).addBox(-2.7F, -0.5F, -3.0F, (int)6.0, (int)2.0, (int)6.0, 0.05F);
        this.left_arm.addChild(this.left_arm_r2);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(52, 18).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(68, 28).addBox(-3.0F, 6.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.4F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-1.0F, 1.5F, 0.0F);
        setRotationAngle(this.right_arm_r1, 0.0F, 0.0F, 0.0436F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(107, 5).addBox(-4.3F, -2.5F, -1.5F, (int)2.0, (int)3.0, (int)3.0, 0.5F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(94, 5).addBox(-4.3F, -2.5F, -1.5F, (int)2.0, (int)5.0, (int)3.0, 0.05F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(68, 52).addBox(-2.3F, -2.5F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.6F);
        this.right_arm.addChild(this.right_arm_r1);

        this.right_arm_r2 = new ModelRenderer(this);
        this.right_arm_r2.setRotationPoint(-1.0F, 0.5F, 0.0F);
        setRotationAngle(this.right_arm_r2, 0.0F, 0.0F, 0.0436F);
        this.right_arm_r2.mirror = false;
        this.right_arm_r2.setTextureOffset(95, 17).addBox(-3.3F, -0.5F, -3.0F, (int)6.0, (int)2.0, (int)6.0, 0.05F);
        this.right_arm.addChild(this.right_arm_r2);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(40, 76).addBox(-2.0F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.left_leg_r1 = new ModelRenderer(this);
        this.left_leg_r1.setRotationPoint(2.75F, 2.5F, 0.0F);
        setRotationAngle(this.left_leg_r1, 0.0F, 0.0F, 0.0873F);
        this.left_leg_r1.mirror = true;
        this.left_leg_r1.setTextureOffset(60, 70).addBox(-1.75F, -2.5F, -2.0F, (int)3.0, (int)6.0, (int)4.0, -0.1F);
        this.left_leg_r1.mirror = true;
        this.left_leg_r1.setTextureOffset(52, 77).addBox(-0.75F, -2.5F, -2.0F, (int)2.0, (int)4.0, (int)4.0, 0.1F);
        this.left_leg.addChild(this.left_leg_r1);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(40, 76).addBox(-2.0F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = true;
        this.right_leg.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_leg_r1 = new ModelRenderer(this);
        this.right_leg_r1.setRotationPoint(-2.75F, 2.5F, 0.0F);
        setRotationAngle(this.right_leg_r1, 0.0F, 0.0F, -0.0873F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(52, 77).addBox(-1.25F, -2.5F, -2.0F, (int)2.0, (int)4.0, (int)4.0, 0.1F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(60, 70).addBox(-1.25F, -2.5F, -2.0F, (int)3.0, (int)6.0, (int)4.0, -0.1F);
        this.right_leg.addChild(this.right_leg_r1);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(97, 28).addBox(-2.0F, 5.0F, -2.0F, (int)4.0, (int)7.0, (int)4.0, 0.4F);
        this.bipedRightLeg.addChild(this.right_shoe);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(97, 28).addBox(-2.0F, 5.0F, -2.0F, (int)4.0, (int)7.0, (int)4.0, 0.4F);
        this.bipedLeftLeg.addChild(this.left_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
