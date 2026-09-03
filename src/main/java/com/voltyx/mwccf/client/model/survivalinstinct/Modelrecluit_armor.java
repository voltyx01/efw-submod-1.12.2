package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelrecluit_armor extends ModelBiped {

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
    public ModelRenderer head_r10;
    public ModelRenderer body;
    public ModelRenderer head_r11;
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
    public ModelRenderer body_r16;
    public ModelRenderer body_r17;
    public ModelRenderer body_r18;
    public ModelRenderer body_r19;
    public ModelRenderer body_r20;
    public ModelRenderer body_r21;
    public ModelRenderer body_r22;
    public ModelRenderer body_r23;
    public ModelRenderer body_r24;
    public ModelRenderer body_r25;
    public ModelRenderer body_r26;
    public ModelRenderer body_r27;
    public ModelRenderer body_r28;
    public ModelRenderer body_r29;
    public ModelRenderer body_r30;
    public ModelRenderer body_r31;
    public ModelRenderer body_r32;
    public ModelRenderer body_r33;
    public ModelRenderer body_r34;
    public ModelRenderer body_r35;
    public ModelRenderer body_r36;
    public ModelRenderer left_arm;
    public ModelRenderer left_arm_r1;
    public ModelRenderer right_arm;
    public ModelRenderer right_arm_r1;
    public ModelRenderer left_leg;
    public ModelRenderer left_leg_r1;
    public ModelRenderer right_leg;
    public ModelRenderer right_leg_r1;
    public ModelRenderer right_leg_r2;
    public ModelRenderer right_leg_r3;
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe;

    public Modelrecluit_armor() {
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
        this.head.setTextureOffset(0, 11).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(24, 11).addBox(-5.0F, -9.0F, -1.5F, (int)10.0, (int)3.0, (int)2.0, 0.2F);
        this.head.mirror = false;
        this.head.setTextureOffset(88, 0).addBox(-5.0F, -6.0F, -1.5F, (int)2.0, (int)2.0, (int)2.0, 0.05F);
        this.head.mirror = true;
        this.head.setTextureOffset(88, 0).addBox(3.0F, -6.0F, -1.5F, (int)2.0, (int)2.0, (int)2.0, 0.05F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(5.7595F, -4.1147F, 0.0F);
        setRotationAngle(this.head_r1, 0.0F, -0.6109F, -0.0873F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(0, 27).addBox(-1.0F, -2.0F, -0.5F, (int)2.0, (int)4.0, (int)0.0, 0.05F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(4.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r2, 0.0F, 0.0F, -0.0873F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(73, 60).addBox(-0.4163F, -1.2608F, -2.0F, (int)1.0, (int)3.0, (int)3.0, 0.2F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(-5.7595F, -4.1147F, 0.0F);
        setRotationAngle(this.head_r3, 0.0F, 0.6109F, 0.0873F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(20, 27).addBox(-1.0F, -2.0F, -0.5F, (int)2.0, (int)4.0, (int)0.0, 0.05F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(4.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r4, 0.0F, 0.0F, -0.2182F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(69, 0).addBox(-0.4163F, -1.2608F, -2.0F, (int)2.0, (int)3.0, (int)3.0, -0.2F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(-0.8087F, -6.2093F, -4.0F);
        setRotationAngle(this.head_r5, 0.121F, -0.05F, 0.3897F);
        this.head_r5.mirror = false;
        this.head_r5.setTextureOffset(60, 57).addBox(2.5592F, -0.9076F, -4.2164F, (int)3.0, (int)1.0, (int)5.0, 0.05F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(-0.8087F, -6.2093F, -4.0F);
        setRotationAngle(this.head_r6, 0.121F, 0.05F, -0.3897F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(20, 62).addBox(-4.065F, -0.2887F, -4.2164F, (int)3.0, (int)1.0, (int)5.0, 0.05F);
        this.head.addChild(this.head_r6);

        this.head_r7 = new ModelRenderer(this);
        this.head_r7.setRotationPoint(-0.8087F, -6.2093F, -4.0F);
        setRotationAngle(this.head_r7, 0.1309F, 0.0F, 0.0F);
        this.head_r7.mirror = false;
        this.head_r7.setTextureOffset(54, 9).addBox(-1.1913F, 0.1856F, -4.2164F, (int)4.0, (int)1.0, (int)5.0, 0.05F);
        this.head.addChild(this.head_r7);

        this.head_r8 = new ModelRenderer(this);
        this.head_r8.setRotationPoint(0.0F, -5.75F, 0.25F);
        setRotationAngle(this.head_r8, -0.0436F, 0.0F, 0.0F);
        this.head_r8.mirror = false;
        this.head_r8.setTextureOffset(0, 0).addBox(-5.0F, -0.25F, -4.25F, (int)10.0, (int)2.0, (int)9.0, -0.2F);
        this.head_r8.mirror = false;
        this.head_r8.setTextureOffset(24, 19).addBox(-4.0F, -2.25F, -4.25F, (int)8.0, (int)3.0, (int)8.0, 0.5F);
        this.head.addChild(this.head_r8);

        this.head_r9 = new ModelRenderer(this);
        this.head_r9.setRotationPoint(-4.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r9, 0.0F, 0.0F, 0.2182F);
        this.head_r9.mirror = false;
        this.head_r9.setTextureOffset(58, 69).addBox(-1.5837F, -1.2608F, -2.0F, (int)2.0, (int)3.0, (int)3.0, -0.2F);
        this.head.addChild(this.head_r9);

        this.head_r10 = new ModelRenderer(this);
        this.head_r10.setRotationPoint(-4.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r10, 0.0F, 0.0F, 0.0873F);
        this.head_r10.mirror = false;
        this.head_r10.setTextureOffset(73, 73).addBox(-0.5837F, -1.2608F, -2.0F, (int)1.0, (int)3.0, (int)3.0, 0.2F);
        this.head.addChild(this.head_r10);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 27).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.1F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 95).addBox(-4.0F, 10.0F, -2.0F, (int)8.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedBody.addChild(this.body);

        this.head_r11 = new ModelRenderer(this);
        this.head_r11.setRotationPoint(0.0F, -4.0F, 0.0F);
        setRotationAngle(this.head_r11, 0.0873F, 0.0F, 0.0F);
        this.head_r11.mirror = false;
        this.head_r11.setTextureOffset(3, 88).addBox(-4.0F, 2.0F, -1.0F, (int)8.0, (int)2.0, (int)5.0, 0.35F);
        this.body.addChild(this.head_r11);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(-3.7328F, 4.9271F, 4.5762F);
        setRotationAngle(this.body_r1, 0.0418F, -1.0827F, -0.1807F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(0, 75).addBox(-0.9944F, -1.5065F, -1.3599F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(0, 59).addBox(-0.4944F, -0.5065F, -0.0985F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(28, 46).addBox(-0.9944F, -1.5065F, -1.0985F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(-2.351F, 6.2529F, 3.1113F);
        setRotationAngle(this.body_r2, -0.0452F, -0.2615F, 0.0117F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(36, 30).addBox(-2.1788F, 1.145F, -0.1795F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(32, 57).addBox(-1.6788F, 2.145F, 0.8205F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(74, 36).addBox(-2.1788F, 1.145F, -0.4409F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(-1.0F, 4.5F, 3.9347F);
        setRotationAngle(this.body_r3, 0.0436F, 0.0F, 0.0F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(71, 53).addBox(3.0F, -0.5F, -2.0F, (int)1.0, (int)1.0, (int)4.0, 0.05F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(30, 71).addBox(3.0F, 1.5F, -2.0F, (int)1.0, (int)1.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(0.0F, 4.5F, 3.9347F);
        setRotationAngle(this.body_r4, 0.0436F, 0.0F, 0.0F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(32, 46).addBox(-3.0F, -3.5F, -2.0F, (int)6.0, (int)7.0, (int)4.0, -0.15F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(0.0F, 4.5F, 3.9347F);
        setRotationAngle(this.body_r5, 0.0873F, 0.0F, 0.0F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(48, 18).addBox(-3.0F, -3.4981F, -1.9128F, (int)6.0, (int)2.0, (int)4.0, 0.2F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(1.4469F, 4.4742F, 6.0369F);
        setRotationAngle(this.body_r6, 0.1306F, 0.0076F, 7.0E-4F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(54, 9).addBox(-0.5F, -2.0F, -0.5F, (int)1.0, (int)4.0, (int)1.0, 0.1F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(1.0F, 4.5F, 3.9347F);
        setRotationAngle(this.body_r7, 0.0436F, 0.0F, 0.0F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(40, 71).addBox(-4.0F, 1.5F, -2.0F, (int)1.0, (int)1.0, (int)4.0, 0.05F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(66, 33).addBox(-4.0F, -0.5F, -2.0F, (int)1.0, (int)1.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(-1.4469F, 4.4742F, 6.0369F);
        setRotationAngle(this.body_r8, 0.1306F, -0.0076F, -7.0E-4F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(16, 45).addBox(-0.5F, -2.0F, -0.5F, (int)1.0, (int)4.0, (int)1.0, 0.1F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(-2.351F, 6.2529F, 3.1113F);
        setRotationAngle(this.body_r9, 0.0F, 0.0F, 0.0436F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(0, 0).addBox(-1.8964F, -1.2475F, -1.7887F, (int)2.0, (int)5.0, (int)2.0, -0.2F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(-2.351F, 6.2529F, 3.1113F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(0, 68).addBox(-2.651F, -5.2529F, -1.7887F, (int)3.0, (int)5.0, (int)2.0, -0.15F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(-2.351F, 6.2529F, 3.1113F);
        setRotationAngle(this.body_r11, 0.0F, -0.0873F, 0.0F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(56, 75).addBox(-2.7153F, -5.2529F, -1.6854F, (int)2.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r11);

        this.body_r12 = new ModelRenderer(this);
        this.body_r12.setRotationPoint(3.0F, 9.4161F, -3.2386F);
        setRotationAngle(this.body_r12, 3.0964F, 0.2615F, 3.1299F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(78, 72).addBox(-1.0F, -2.0F, -1.0F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(16, 76).addBox(-1.0F, -2.0F, -1.2614F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(24, 30).addBox(-0.5F, -1.0F, 0.0F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body.addChild(this.body_r12);

        this.body_r13 = new ModelRenderer(this);
        this.body_r13.setRotationPoint(2.8F, 3.5F, -2.1F);
        setRotationAngle(this.body_r13, 3.1416F, 0.0873F, -3.1416F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(32, 76).addBox(-1.5F, -2.5F, -1.0F, (int)2.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r13);

        this.body_r14 = new ModelRenderer(this);
        this.body_r14.setRotationPoint(2.8F, 3.5F, -2.1F);
        setRotationAngle(this.body_r14, 3.1416F, 0.0F, -3.1416F);
        this.body_r14.mirror = false;
        this.body_r14.setTextureOffset(10, 69).addBox(-1.5F, -2.5F, -1.0F, (int)3.0, (int)5.0, (int)2.0, -0.15F);
        this.body.addChild(this.body_r14);

        this.body_r15 = new ModelRenderer(this);
        this.body_r15.setRotationPoint(0.0F, 5.0208F, 0.0F);
        setRotationAngle(this.body_r15, 0.0F, 0.0F, -0.0436F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(42, 10).addBox(1.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r15);

        this.body_r16 = new ModelRenderer(this);
        this.body_r16.setRotationPoint(1.8F, 7.5F, -2.1F);
        setRotationAngle(this.body_r16, 3.1416F, 0.0F, 3.098F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(70, 45).addBox(-1.8F, -2.5F, -1.0F, (int)2.0, (int)5.0, (int)2.0, -0.2F);
        this.body.addChild(this.body_r16);

        this.body_r17 = new ModelRenderer(this);
        this.body_r17.setRotationPoint(5.0309F, 8.9271F, -1.3537F);
        setRotationAngle(this.body_r17, 2.8535F, 1.1758F, 3.0367F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(76, 66).addBox(-0.9944F, -1.5065F, -1.3599F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(48, 18).addBox(-0.4944F, -0.5065F, -0.0985F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(80, 31).addBox(-0.9944F, -1.5065F, -1.0985F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r17);

        this.body_r18 = new ModelRenderer(this);
        this.body_r18.setRotationPoint(2.351F, 6.2529F, 3.1113F);
        setRotationAngle(this.body_r18, -0.0873F, 0.0F, 0.0F);
        this.body_r18.mirror = false;
        this.body_r18.setTextureOffset(24, 16).addBox(-5.149F, 2.7542F, -0.3739F, (int)7.0, (int)1.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r18);

        this.body_r19 = new ModelRenderer(this);
        this.body_r19.setRotationPoint(3.7328F, 4.9271F, 4.5762F);
        setRotationAngle(this.body_r19, 0.0418F, 1.0827F, 0.1807F);
        this.body_r19.mirror = false;
        this.body_r19.setTextureOffset(0, 75).addBox(-1.0056F, -1.5065F, -1.3599F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r19.mirror = false;
        this.body_r19.setTextureOffset(0, 59).addBox(-0.5056F, -0.5065F, -0.0985F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r19.mirror = false;
        this.body_r19.setTextureOffset(28, 46).addBox(-1.0056F, -1.5065F, -1.0985F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r19);

        this.body_r20 = new ModelRenderer(this);
        this.body_r20.setRotationPoint(2.351F, 6.2529F, 3.1113F);
        setRotationAngle(this.body_r20, -0.0452F, 0.2615F, -0.0117F);
        this.body_r20.mirror = false;
        this.body_r20.setTextureOffset(76, 12).addBox(0.1788F, 1.145F, -0.4409F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r20.mirror = false;
        this.body_r20.setTextureOffset(48, 59).addBox(0.6788F, 2.145F, 0.8205F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r20.mirror = false;
        this.body_r20.setTextureOffset(78, 58).addBox(0.1788F, 1.145F, -0.1795F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r20);

        this.body_r21 = new ModelRenderer(this);
        this.body_r21.setRotationPoint(2.351F, 6.2529F, 3.1113F);
        setRotationAngle(this.body_r21, 0.0F, 0.0F, -0.0436F);
        this.body_r21.mirror = false;
        this.body_r21.setTextureOffset(0, 11).addBox(-0.1036F, -1.2475F, -1.7887F, (int)2.0, (int)5.0, (int)2.0, -0.2F);
        this.body.addChild(this.body_r21);

        this.body_r22 = new ModelRenderer(this);
        this.body_r22.setRotationPoint(2.351F, 6.2529F, 3.1113F);
        setRotationAngle(this.body_r22, -0.1309F, 0.0F, 0.0F);
        this.body_r22.mirror = false;
        this.body_r22.setTextureOffset(48, 48).addBox(-5.149F, -2.2002F, -1.0151F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r22);

        this.body_r23 = new ModelRenderer(this);
        this.body_r23.setRotationPoint(2.351F, 6.2529F, 3.1113F);
        this.body_r23.mirror = false;
        this.body_r23.setTextureOffset(48, 68).addBox(-0.349F, -5.2529F, -1.7887F, (int)3.0, (int)5.0, (int)2.0, -0.15F);
        this.body.addChild(this.body_r23);

        this.body_r24 = new ModelRenderer(this);
        this.body_r24.setRotationPoint(2.351F, 6.2529F, 3.1113F);
        setRotationAngle(this.body_r24, 0.0F, 0.0873F, 0.0F);
        this.body_r24.mirror = false;
        this.body_r24.setTextureOffset(8, 76).addBox(0.7153F, -5.2529F, -1.6854F, (int)2.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r24);

        this.body_r25 = new ModelRenderer(this);
        this.body_r25.setRotationPoint(-3.0F, 9.4161F, -3.2386F);
        setRotationAngle(this.body_r25, 3.0964F, -0.2615F, -3.1299F);
        this.body_r25.mirror = false;
        this.body_r25.setTextureOffset(0, 43).addBox(-0.5F, -1.0F, 0.0F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r25.mirror = false;
        this.body_r25.setTextureOffset(24, 76).addBox(-1.0F, -2.0F, -1.2614F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r25.mirror = false;
        this.body_r25.setTextureOffset(79, 0).addBox(-1.0F, -2.0F, -1.0F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r25);

        this.body_r26 = new ModelRenderer(this);
        this.body_r26.setRotationPoint(-5.0309F, 8.9271F, -1.3537F);
        setRotationAngle(this.body_r26, 2.8535F, -1.1758F, -3.0367F);
        this.body_r26.mirror = false;
        this.body_r26.setTextureOffset(0, 81).addBox(-1.0056F, -1.5065F, -1.0985F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body_r26.mirror = false;
        this.body_r26.setTextureOffset(53, 0).addBox(-0.5056F, -0.5065F, -0.0985F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r26.mirror = false;
        this.body_r26.setTextureOffset(77, 4).addBox(-1.0056F, -1.5065F, -1.3599F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body.addChild(this.body_r26);

        this.body_r27 = new ModelRenderer(this);
        this.body_r27.setRotationPoint(0.0F, 11.0F, -2.6F);
        setRotationAngle(this.body_r27, 3.0543F, 0.0F, 3.1416F);
        this.body_r27.mirror = false;
        this.body_r27.setTextureOffset(64, 26).addBox(-3.5F, -2.0F, -0.5F, (int)7.0, (int)1.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r27);

        this.body_r28 = new ModelRenderer(this);
        this.body_r28.setRotationPoint(0.0F, 4.5F, -2.6F);
        setRotationAngle(this.body_r28, 3.0107F, 0.0F, 3.1416F);
        this.body_r28.mirror = false;
        this.body_r28.setTextureOffset(12, 43).addBox(-2.5F, -0.5F, -0.5F, (int)5.0, (int)1.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r28);

        this.body_r29 = new ModelRenderer(this);
        this.body_r29.setRotationPoint(0.0F, 5.0208F, 0.0F);
        setRotationAngle(this.body_r29, 0.0F, 0.0F, 0.0436F);
        this.body_r29.mirror = false;
        this.body_r29.setTextureOffset(50, 40).addBox(-4.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r29);

        this.body_r30 = new ModelRenderer(this);
        this.body_r30.setRotationPoint(-3.0F, 7.95F, 0.0F);
        setRotationAngle(this.body_r30, 0.0F, 0.0F, -0.0436F);
        this.body_r30.mirror = false;
        this.body_r30.setTextureOffset(46, 51).addBox(-2.0F, 0.05F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.addChild(this.body_r30);

        this.body_r31 = new ModelRenderer(this);
        this.body_r31.setRotationPoint(-3.0F, 0.95F, 0.0F);
        setRotationAngle(this.body_r31, 0.0F, 0.0F, -0.0873F);
        this.body_r31.mirror = false;
        this.body_r31.setTextureOffset(77, 50).addBox(-1.0F, -0.95F, 1.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r31.mirror = false;
        this.body_r31.setTextureOffset(78, 26).addBox(-1.0F, -0.95F, -3.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r31.mirror = false;
        this.body_r31.setTextureOffset(56, 32).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)1.0, (int)6.0, 0.05F);
        this.body.addChild(this.body_r31);

        this.body_r32 = new ModelRenderer(this);
        this.body_r32.setRotationPoint(3.0F, 7.95F, 0.0F);
        setRotationAngle(this.body_r32, 0.0F, 0.0F, 0.0436F);
        this.body_r32.mirror = false;
        this.body_r32.setTextureOffset(52, 24).addBox(-1.0F, 0.05F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.addChild(this.body_r32);

        this.body_r33 = new ModelRenderer(this);
        this.body_r33.setRotationPoint(3.0F, 0.95F, -5.0F);
        setRotationAngle(this.body_r33, 0.0F, 0.0F, 0.0873F);
        this.body_r33.mirror = false;
        this.body_r33.setTextureOffset(78, 19).addBox(-1.0F, -0.95F, 2.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r33.mirror = false;
        this.body_r33.setTextureOffset(78, 42).addBox(-1.0F, -0.95F, 6.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r33.mirror = false;
        this.body_r33.setTextureOffset(10, 62).addBox(-1.0F, -1.55F, 2.0F, (int)2.0, (int)1.0, (int)6.0, 0.05F);
        this.body.addChild(this.body_r33);

        this.body_r34 = new ModelRenderer(this);
        this.body_r34.setRotationPoint(-1.8F, 7.5F, -2.1F);
        setRotationAngle(this.body_r34, 3.1416F, 0.0F, -3.098F);
        this.body_r34.mirror = false;
        this.body_r34.setTextureOffset(68, 69).addBox(-0.2F, -2.5F, -1.0F, (int)2.0, (int)5.0, (int)2.0, -0.2F);
        this.body.addChild(this.body_r34);

        this.body_r35 = new ModelRenderer(this);
        this.body_r35.setRotationPoint(-2.8F, 3.5F, -2.1F);
        setRotationAngle(this.body_r35, 3.1416F, -0.0873F, 3.1416F);
        this.body_r35.mirror = false;
        this.body_r35.setTextureOffset(40, 76).addBox(-0.5F, -2.5F, -1.0F, (int)2.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r35);

        this.body_r36 = new ModelRenderer(this);
        this.body_r36.setRotationPoint(-2.8F, 3.5F, -2.1F);
        setRotationAngle(this.body_r36, 3.1416F, 0.0F, 3.1416F);
        this.body_r36.mirror = false;
        this.body_r36.setTextureOffset(20, 69).addBox(-1.5F, -2.5F, -1.0F, (int)3.0, (int)5.0, (int)2.0, -0.15F);
        this.body.addChild(this.body_r36);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(16, 46).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(64, 15).addBox(-1.0F, 6.0F, -2.0F, (int)4.0, (int)2.0, (int)4.0, 0.3F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(1.0F, 3.0F, 0.0F);
        setRotationAngle(this.left_arm_r1, 0.0F, 0.0F, -0.0436F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(58, 48).addBox(-2.0F, -4.0F, -2.0F, (int)4.0, (int)4.0, (int)4.0, 0.5F);
        this.left_arm.addChild(this.left_arm_r1);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(16, 46).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(64, 15).addBox(-3.0F, 6.0F, -2.0F, (int)4.0, (int)2.0, (int)4.0, 0.3F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-1.0F, 3.0F, 0.0F);
        setRotationAngle(this.right_arm_r1, 0.0F, 0.0F, 0.0436F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(58, 48).addBox(-2.0F, -4.0F, -2.0F, (int)4.0, (int)4.0, (int)4.0, 0.5F);
        this.right_arm.addChild(this.right_arm_r1);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(66, 28).addBox(-1.9F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(0, 43).addBox(-1.9F, 0.0F, -2.0F, (int)4.0, (int)8.0, (int)4.0, 0.1F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.left_leg_r1 = new ModelRenderer(this);
        this.left_leg_r1.setRotationPoint(0.1F, 4.5F, -2.1F);
        setRotationAngle(this.left_leg_r1, 0.0873F, -0.0873F, 0.0F);
        this.left_leg_r1.mirror = false;
        this.left_leg_r1.setTextureOffset(70, 79).addBox(-1.5F, -1.5F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.2F);
        this.left_leg_r1.mirror = false;
        this.left_leg_r1.setTextureOffset(60, 15).addBox(-1.5F, -1.0F, -0.5F, (int)3.0, (int)2.0, (int)1.0, 0.4F);
        this.left_leg.addChild(this.left_leg_r1);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(66, 28).addBox(-2.1F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(0, 43).addBox(-2.1F, 0.0F, -2.0F, (int)4.0, (int)8.0, (int)4.0, 0.1F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_leg_r1 = new ModelRenderer(this);
        this.right_leg_r1.setRotationPoint(-0.1F, 4.5F, -2.1F);
        setRotationAngle(this.right_leg_r1, 0.0873F, 0.0873F, 0.0F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(72, 33).addBox(-1.5F, -1.0F, -0.5F, (int)3.0, (int)2.0, (int)1.0, 0.4F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(47, 81).addBox(-1.5F, -1.5F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.2F);
        this.right_leg.addChild(this.right_leg_r1);

        this.right_leg_r2 = new ModelRenderer(this);
        this.right_leg_r2.setRotationPoint(-3.1F, 2.0F, -0.5F);
        setRotationAngle(this.right_leg_r2, 0.0925F, 0.348F, 0.024F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(56, 32).addBox(-0.7412F, 0.0F, -1.4659F, (int)2.0, (int)4.0, (int)1.0, 0.05F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(65, 0).addBox(-0.7412F, 0.0F, -1.4659F, (int)2.0, (int)2.0, (int)1.0, 0.25F);
        this.right_leg.addChild(this.right_leg_r2);

        this.right_leg_r3 = new ModelRenderer(this);
        this.right_leg_r3.setRotationPoint(-3.1F, 2.0F, -0.5F);
        setRotationAngle(this.right_leg_r3, 0.0873F, 0.0873F, 0.0F);
        this.right_leg_r3.mirror = false;
        this.right_leg_r3.setTextureOffset(64, 76).addBox(-1.0F, -3.0F, 0.5F, (int)2.0, (int)7.0, (int)1.0, 0.25F);
        this.right_leg_r3.mirror = false;
        this.right_leg_r3.setTextureOffset(79, 77).addBox(-1.0F, -3.0F, -0.5F, (int)2.0, (int)2.0, (int)2.0, 0.05F);
        this.right_leg_r3.mirror = false;
        this.right_leg_r3.setTextureOffset(67, 6).addBox(-1.0F, -1.0F, -0.5F, (int)2.0, (int)5.0, (int)3.0, 0.05F);
        this.right_leg.addChild(this.right_leg_r3);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(48, 59).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.49F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(48, 24).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.bipedLeftLeg.addChild(this.left_shoe);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(48, 59).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.49F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(48, 24).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.bipedRightLeg.addChild(this.right_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
