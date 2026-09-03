package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelrockie_armor extends ModelBiped {

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
    public ModelRenderer head_r11;
    public ModelRenderer head_r12;
    public ModelRenderer head_r13;
    public ModelRenderer head_r14;
    public ModelRenderer head_r15;
    public ModelRenderer head_r16;
    public ModelRenderer head_r17;
    public ModelRenderer head_r18;
    public ModelRenderer head_r19;
    public ModelRenderer head_r20;
    public ModelRenderer head_r21;
    public ModelRenderer head_r22;
    public ModelRenderer head_r23;
    public ModelRenderer head_r24;
    public ModelRenderer head_r25;
    public ModelRenderer head_r26;
    public ModelRenderer head_r27;
    public ModelRenderer head_r28;
    public ModelRenderer head_r29;
    public ModelRenderer head_r30;
    public ModelRenderer head_r31;
    public ModelRenderer head_r32;
    public ModelRenderer head_r33;
    public ModelRenderer head_r34;
    public ModelRenderer head_r35;
    public ModelRenderer head_r36;
    public ModelRenderer head_r37;
    public ModelRenderer head_r38;
    public ModelRenderer head_r39;
    public ModelRenderer head_r40;
    public ModelRenderer head_r41;
    public ModelRenderer head_r42;
    public ModelRenderer visor_r1;
    public ModelRenderer visor_r2;
    public ModelRenderer visor_r3;
    public ModelRenderer visor_r4;
    public ModelRenderer visor_r5;
    public ModelRenderer visor_r6;
    public ModelRenderer visor_r7;
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
    public ModelRenderer body_r16;
    public ModelRenderer body_r17;
    public ModelRenderer body_r18;
    public ModelRenderer body_r19;
    public ModelRenderer body_r20;
    public ModelRenderer body_r21;
    public ModelRenderer body_r22;
    public ModelRenderer body_r23;
    public ModelRenderer body_r24;
    public ModelRenderer left_arm;
    public ModelRenderer left_arm_r1;
    public ModelRenderer left_arm_r2;
    public ModelRenderer left_arm_r3;
    public ModelRenderer right_arm;
    public ModelRenderer right_arm_r1;
    public ModelRenderer right_arm_r2;
    public ModelRenderer right_arm_r3;
    public ModelRenderer left_leg;
    public ModelRenderer left_leg_r1;
    public ModelRenderer left_leg_r2;
    public ModelRenderer left_leg_r3;
    public ModelRenderer right_leg;
    public ModelRenderer right_leg_r1;
    public ModelRenderer right_leg_r2;
    public ModelRenderer right_leg_r3;
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe;

    public Modelrockie_armor() {
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
        this.head.mirror = false;
        this.head.setTextureOffset(0, 16).addBox(-4.0F, -9.0F, -4.0F, (int)8.0, (int)1.0, (int)8.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(66, 51).addBox(-4.0F, -8.0F, 4.0F, (int)8.0, (int)6.0, (int)1.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(0, 0).addBox(-1.5F, -8.0F, 4.0F, (int)3.0, (int)5.0, (int)1.0, 0.3F);
        this.head.mirror = false;
        this.head.setTextureOffset(74, 66).addBox(-4.0F, -8.0F, -5.0F, (int)8.0, (int)3.0, (int)1.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(30, 61).addBox(-1.5F, -8.0F, -5.0F, (int)3.0, (int)3.0, (int)1.0, 0.3F);
        this.head.mirror = false;
        this.head.setTextureOffset(40, 15).addBox(-1.5F, -9.0F, -4.0F, (int)3.0, (int)1.0, (int)8.0, 0.3F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(0.0F, -4.0F, 5.0F);
        setRotationAngle(this.head_r1, -0.1309F, 0.0F, 0.0F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(0, 60).addBox(-2.0F, -1.0F, -1.0F, (int)1.0, (int)2.0, (int)2.0, 0.2F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(68, 0).addBox(1.0F, -1.0F, -1.0F, (int)1.0, (int)2.0, (int)2.0, 0.2F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(2.5F, -4.0F, 5.0F);
        setRotationAngle(this.head_r2, -0.0452F, -0.2615F, 0.0117F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(69, 58).addBox(-0.5F, -1.0F, -1.0F, (int)1.0, (int)2.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(0.0F, -4.412F, 0.912F);
        setRotationAngle(this.head_r3, -0.6903F, 0.0831F, 0.1183F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(56, 90).addBox(4.2222F, -0.6075F, -0.5F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(4.4163F, -4.7392F, -5.0F);
        setRotationAngle(this.head_r4, 0.0F, 0.0F, -0.0873F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(52, 37).addBox(0.2837F, -1.2608F, 4.0F, (int)1.0, (int)1.0, (int)1.0, 0.2F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(54, 11).addBox(0.2837F, -2.2608F, 2.0F, (int)1.0, (int)3.0, (int)7.0, -0.1F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(4.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r5, 0.0F, 0.0F, -0.2182F);
        this.head_r5.mirror = true;
        this.head_r5.setTextureOffset(0, 84).addBox(-0.4163F, -1.2608F, -2.0F, (int)2.0, (int)3.0, (int)3.0, -0.2F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(4.4163F, -6.7392F, 0.0F);
        setRotationAngle(this.head_r6, 0.0F, 0.0F, -0.0873F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(48, 44).addBox(-0.4163F, -1.2608F, -4.0F, (int)1.0, (int)3.0, (int)8.0, 0.05F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(80, 86).addBox(-0.4163F, 0.7392F, 1.0F, (int)1.0, (int)3.0, (int)3.0, -0.01F);
        this.head.addChild(this.head_r6);

        this.head_r7 = new ModelRenderer(this);
        this.head_r7.setRotationPoint(0.0F, -2.412F, 2.912F);
        setRotationAngle(this.head_r7, -1.0435F, -0.1133F, -0.0657F);
        this.head_r7.mirror = false;
        this.head_r7.setTextureOffset(8, 91).addBox(-5.2222F, -0.6075F, -0.5F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r7);

        this.head_r8 = new ModelRenderer(this);
        this.head_r8.setRotationPoint(0.0F, -4.412F, 0.912F);
        setRotationAngle(this.head_r8, -0.6903F, -0.0831F, -0.1183F);
        this.head_r8.mirror = false;
        this.head_r8.setTextureOffset(22, 91).addBox(-5.2222F, -0.6075F, -0.5F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r8);

        this.head_r9 = new ModelRenderer(this);
        this.head_r9.setRotationPoint(0.0F, -2.412F, 2.912F);
        setRotationAngle(this.head_r9, -1.0435F, 0.1133F, 0.0657F);
        this.head_r9.mirror = false;
        this.head_r9.setTextureOffset(72, 92).addBox(4.2222F, -0.6075F, -0.5F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r9);

        this.head_r10 = new ModelRenderer(this);
        this.head_r10.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r10, -0.2615F, -0.0076F, -0.0869F);
        this.head_r10.mirror = false;
        this.head_r10.setTextureOffset(29, 90).addBox(4.0907F, -0.1044F, -2.25F, (int)1.0, (int)1.0, (int)3.0, 0.3F);
        this.head.addChild(this.head_r10);

        this.head_r11 = new ModelRenderer(this);
        this.head_r11.setRotationPoint(5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r11, 0.0F, 0.0F, 0.1309F);
        this.head_r11.mirror = false;
        this.head_r11.setTextureOffset(25, 70).addBox(-0.3353F, 0.4848F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r11);

        this.head_r12 = new ModelRenderer(this);
        this.head_r12.setRotationPoint(5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r12, 0.0F, 0.0F, -0.0873F);
        this.head_r12.mirror = false;
        this.head_r12.setTextureOffset(70, 36).addBox(-0.4042F, -1.4939F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r12);

        this.head_r13 = new ModelRenderer(this);
        this.head_r13.setRotationPoint(-4.4163F, -6.7392F, 2.0F);
        setRotationAngle(this.head_r13, 0.0F, 0.0F, 0.0873F);
        this.head_r13.mirror = false;
        this.head_r13.setTextureOffset(46, 55).addBox(-0.5837F, 0.7392F, -1.0F, (int)1.0, (int)2.0, (int)3.0, -0.01F);
        this.head_r13.mirror = false;
        this.head_r13.setTextureOffset(35, 87).addBox(-0.5837F, 0.7392F, -1.0F, (int)1.0, (int)3.0, (int)3.0, -0.01F);
        this.head_r13.mirror = false;
        this.head_r13.setTextureOffset(8, 49).addBox(-0.5837F, -1.2608F, -6.0F, (int)1.0, (int)3.0, (int)8.0, 0.05F);
        this.head.addChild(this.head_r13);

        this.head_r14 = new ModelRenderer(this);
        this.head_r14.setRotationPoint(5.7595F, -4.1147F, 0.0F);
        setRotationAngle(this.head_r14, 0.0F, -0.6109F, -0.0873F);
        this.head_r14.mirror = false;
        this.head_r14.setTextureOffset(0, 25).addBox(-1.0F, -2.0F, -0.5F, (int)2.0, (int)4.0, (int)0.0, 0.05F);
        this.head.addChild(this.head_r14);

        this.head_r15 = new ModelRenderer(this);
        this.head_r15.setRotationPoint(4.5907F, -4.5F, -1.0F);
        setRotationAngle(this.head_r15, 0.0F, 0.0F, 0.0436F);
        this.head_r15.mirror = false;
        this.head_r15.setTextureOffset(82, 70).addBox(-0.5F, -0.5F, -2.0F, (int)1.0, (int)1.0, (int)5.0, 0.3F);
        this.head.addChild(this.head_r15);

        this.head_r16 = new ModelRenderer(this);
        this.head_r16.setRotationPoint(4.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r16, 0.0F, 0.0F, -0.0873F);
        this.head_r16.mirror = true;
        this.head_r16.setTextureOffset(16, 41).addBox(-0.4163F, -1.2608F, -2.0F, (int)1.0, (int)3.0, (int)3.0, 0.2F);
        this.head.addChild(this.head_r16);

        this.head_r17 = new ModelRenderer(this);
        this.head_r17.setRotationPoint(-5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r17, 0.0F, 0.0F, 0.0873F);
        this.head_r17.mirror = false;
        this.head_r17.setTextureOffset(28, 52).addBox(-0.5958F, -1.4939F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r17);

        this.head_r18 = new ModelRenderer(this);
        this.head_r18.setRotationPoint(-5.7595F, -4.1147F, 0.0F);
        setRotationAngle(this.head_r18, 0.0F, 0.6109F, 0.0873F);
        this.head_r18.mirror = false;
        this.head_r18.setTextureOffset(24, 16).addBox(-1.0F, -2.0F, -0.5F, (int)2.0, (int)4.0, (int)0.0, 0.05F);
        this.head.addChild(this.head_r18);

        this.head_r19 = new ModelRenderer(this);
        this.head_r19.setRotationPoint(-4.4163F, -4.7392F, -5.0F);
        setRotationAngle(this.head_r19, 0.0F, 0.0F, 0.0873F);
        this.head_r19.mirror = false;
        this.head_r19.setTextureOffset(58, 35).addBox(-1.2837F, -1.2608F, 4.0F, (int)1.0, (int)1.0, (int)1.0, 0.2F);
        this.head_r19.mirror = false;
        this.head_r19.setTextureOffset(47, 55).addBox(-1.2837F, -2.2608F, 2.0F, (int)1.0, (int)3.0, (int)7.0, -0.1F);
        this.head.addChild(this.head_r19);

        this.head_r20 = new ModelRenderer(this);
        this.head_r20.setRotationPoint(-5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r20, 0.0F, 0.0F, -0.1309F);
        this.head_r20.mirror = false;
        this.head_r20.setTextureOffset(9, 70).addBox(-0.6647F, 0.4848F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r20);

        this.head_r21 = new ModelRenderer(this);
        this.head_r21.setRotationPoint(-4.1972F, -6.5038F, -4.87F);
        setRotationAngle(this.head_r21, 0.0F, -0.6545F, 0.0F);
        this.head_r21.mirror = false;
        this.head_r21.setTextureOffset(24, 36).addBox(0.1972F, -1.5F, -0.13F, (int)1.0, (int)3.0, (int)1.0, 0.13F);
        this.head.addChild(this.head_r21);

        this.head_r22 = new ModelRenderer(this);
        this.head_r22.setRotationPoint(-4.5F, -5.0F, 3.5F);
        setRotationAngle(this.head_r22, 0.0F, -0.7418F, 0.0F);
        this.head_r22.mirror = false;
        this.head_r22.setTextureOffset(56, 11).addBox(0.2F, -3.0F, -0.1F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r22);

        this.head_r23 = new ModelRenderer(this);
        this.head_r23.setRotationPoint(3.9658F, -4.5038F, -4.1154F);
        setRotationAngle(this.head_r23, 0.0983F, 0.478F, 0.0453F);
        this.head_r23.mirror = false;
        this.head_r23.setTextureOffset(56, 59).addBox(-0.5F, -0.5F, -0.5F, (int)1.0, (int)1.0, (int)2.0, 0.3F);
        this.head.addChild(this.head_r23);

        this.head_r24 = new ModelRenderer(this);
        this.head_r24.setRotationPoint(4.1972F, -6.5038F, -4.87F);
        setRotationAngle(this.head_r24, 0.0F, 0.6545F, 0.0F);
        this.head_r24.mirror = false;
        this.head_r24.setTextureOffset(0, 41).addBox(-1.1972F, -1.5F, -0.13F, (int)1.0, (int)3.0, (int)1.0, 0.13F);
        this.head.addChild(this.head_r24);

        this.head_r25 = new ModelRenderer(this);
        this.head_r25.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r25, -0.1181F, 0.7383F, -0.0797F);
        this.head_r25.mirror = false;
        this.head_r25.setTextureOffset(44, 27).addBox(1.9489F, -0.494F, 3.1245F, (int)1.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r25);

        this.head_r26 = new ModelRenderer(this);
        this.head_r26.setRotationPoint(4.5F, -5.0F, 3.5F);
        setRotationAngle(this.head_r26, 0.0F, 0.7418F, 0.0F);
        this.head_r26.mirror = false;
        this.head_r26.setTextureOffset(68, 89).addBox(-1.2F, -3.0F, -0.1F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r26);

        this.head_r27 = new ModelRenderer(this);
        this.head_r27.setRotationPoint(-4.1258F, -8.0425F, 0.0F);
        setRotationAngle(this.head_r27, 0.0F, 0.0F, -0.8727F);
        this.head_r27.mirror = false;
        this.head_r27.setTextureOffset(18, 52).addBox(-0.3742F, -0.5F, -4.0F, (int)1.0, (int)1.0, (int)8.0, 0.1F);
        this.head.addChild(this.head_r27);

        this.head_r28 = new ModelRenderer(this);
        this.head_r28.setRotationPoint(-4.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r28, 0.0F, 0.0F, 0.0873F);
        this.head_r28.mirror = false;
        this.head_r28.setTextureOffset(16, 41).addBox(-0.5837F, -1.2608F, -2.0F, (int)1.0, (int)3.0, (int)3.0, 0.2F);
        this.head.addChild(this.head_r28);

        this.head_r29 = new ModelRenderer(this);
        this.head_r29.setRotationPoint(-4.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r29, 0.0F, 0.0F, 0.2182F);
        this.head_r29.mirror = false;
        this.head_r29.setTextureOffset(0, 84).addBox(-1.5837F, -1.2608F, -2.0F, (int)2.0, (int)3.0, (int)3.0, -0.2F);
        this.head.addChild(this.head_r29);

        this.head_r30 = new ModelRenderer(this);
        this.head_r30.setRotationPoint(-4.5907F, -4.5F, -1.0F);
        setRotationAngle(this.head_r30, 0.0F, 0.0F, -0.0436F);
        this.head_r30.mirror = false;
        this.head_r30.setTextureOffset(18, 70).addBox(-0.5F, -0.5F, -2.0F, (int)1.0, (int)1.0, (int)5.0, 0.3F);
        this.head.addChild(this.head_r30);

        this.head_r31 = new ModelRenderer(this);
        this.head_r31.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r31, -0.2615F, 0.0076F, 0.0869F);
        this.head_r31.mirror = false;
        this.head_r31.setTextureOffset(48, 90).addBox(-5.0907F, -0.1044F, -2.25F, (int)1.0, (int)1.0, (int)3.0, 0.3F);
        this.head.addChild(this.head_r31);

        this.head_r32 = new ModelRenderer(this);
        this.head_r32.setRotationPoint(0.0F, -7.5F, -5.5F);
        setRotationAngle(this.head_r32, -0.1745F, 0.0F, 0.0F);
        this.head_r32.mirror = false;
        this.head_r32.setTextureOffset(87, 91).addBox(-1.5F, 0.0F, 0.0F, (int)3.0, (int)2.0, (int)1.0, 0.2F);
        this.head.addChild(this.head_r32);

        this.head_r33 = new ModelRenderer(this);
        this.head_r33.setRotationPoint(-1.0F, -8.5F, -4.5F);
        setRotationAngle(this.head_r33, -0.5672F, 0.0F, 0.0F);
        this.head_r33.mirror = false;
        this.head_r33.setTextureOffset(32, 23).addBox(-0.5F, -0.5F, 0.0F, (int)3.0, (int)1.0, (int)1.0, 0.3F);
        this.head_r33.mirror = false;
        this.head_r33.setTextureOffset(79, 40).addBox(-3.0F, -0.5F, 0.0F, (int)8.0, (int)1.0, (int)1.0, 0.1F);
        this.head.addChild(this.head_r33);

        this.head_r34 = new ModelRenderer(this);
        this.head_r34.setRotationPoint(0.0F, -4.5F, -4.5F);
        setRotationAngle(this.head_r34, 0.0873F, 0.0F, 0.0F);
        this.head_r34.mirror = false;
        this.head_r34.setTextureOffset(82, 20).addBox(-4.0F, -0.5F, -0.5F, (int)8.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r34);

        this.head_r35 = new ModelRenderer(this);
        this.head_r35.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r35, -0.0873F, 0.0F, 0.0F);
        this.head_r35.mirror = false;
        this.head_r35.setTextureOffset(78, 18).addBox(-4.0F, -0.494F, 0.75F, (int)8.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r35);

        this.head_r36 = new ModelRenderer(this);
        this.head_r36.setRotationPoint(-2.5F, -4.0F, 5.0F);
        setRotationAngle(this.head_r36, -0.0452F, 0.2615F, -0.0117F);
        this.head_r36.mirror = false;
        this.head_r36.setTextureOffset(25, 70).addBox(-0.5F, -1.0F, -1.0F, (int)1.0, (int)2.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r36);

        this.head_r37 = new ModelRenderer(this);
        this.head_r37.setRotationPoint(0.0F, -4.0F, 5.0F);
        setRotationAngle(this.head_r37, -0.0436F, 0.0F, 0.0F);
        this.head_r37.mirror = false;
        this.head_r37.setTextureOffset(24, 0).addBox(-2.0F, -2.0F, -1.0F, (int)4.0, (int)1.0, (int)2.0, 0.3F);
        this.head_r37.mirror = false;
        this.head_r37.setTextureOffset(14, 83).addBox(-2.0F, -2.0F, -1.0F, (int)4.0, (int)3.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r37);

        this.head_r38 = new ModelRenderer(this);
        this.head_r38.setRotationPoint(-1.0F, -8.2172F, 4.2172F);
        setRotationAngle(this.head_r38, 0.7418F, 0.0F, 0.0F);
        this.head_r38.mirror = false;
        this.head_r38.setTextureOffset(16, 47).addBox(-0.5F, -0.5F, -0.8172F, (int)3.0, (int)1.0, (int)1.0, 0.3F);
        this.head_r38.mirror = false;
        this.head_r38.setTextureOffset(28, 80).addBox(-3.0F, -0.5F, -0.8172F, (int)8.0, (int)1.0, (int)1.0, 0.18F);
        this.head.addChild(this.head_r38);

        this.head_r39 = new ModelRenderer(this);
        this.head_r39.setRotationPoint(4.1258F, -8.0425F, 0.0F);
        setRotationAngle(this.head_r39, 0.0F, 0.0F, 0.8727F);
        this.head_r39.mirror = false;
        this.head_r39.setTextureOffset(36, 52).addBox(-0.6258F, -0.5F, -4.0F, (int)1.0, (int)1.0, (int)8.0, 0.1F);
        this.head.addChild(this.head_r39);

        this.head_r40 = new ModelRenderer(this);
        this.head_r40.setRotationPoint(-3.9658F, -4.5038F, -4.1154F);
        setRotationAngle(this.head_r40, 0.0983F, -0.478F, -0.0453F);
        this.head_r40.mirror = false;
        this.head_r40.setTextureOffset(59, 82).addBox(-0.5F, -0.5F, -0.5F, (int)1.0, (int)1.0, (int)2.0, 0.3F);
        this.head.addChild(this.head_r40);

        this.head_r41 = new ModelRenderer(this);
        this.head_r41.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r41, -0.1181F, -0.7383F, 0.0797F);
        this.head_r41.mirror = false;
        this.head_r41.setTextureOffset(52, 35).addBox(-2.9489F, -0.494F, 3.1245F, (int)1.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r41);

        this.head_r42 = new ModelRenderer(this);
        this.head_r42.setRotationPoint(0.0F, -4.412F, 0.912F);
        setRotationAngle(this.head_r42, -0.6545F, 0.0F, 0.0F);
        this.head_r42.mirror = false;
        this.head_r42.setTextureOffset(76, 9).addBox(-4.0F, 4.9969F, -0.5F, (int)8.0, (int)1.0, (int)1.0, -0.001F);
        this.head.addChild(this.head_r42);

        this.visor_r1 = new ModelRenderer(this);
        this.visor_r1.setRotationPoint(0.0F, -7.2031F, -5.9513F);
        setRotationAngle(this.visor_r1, -1.1829F, -0.0968F, -0.0964F);
        this.visor_r1.mirror = false;
        this.visor_r1.setTextureOffset(114, 102).addBox(-2.0F, -0.5049F, -2.1728F, (int)4.0, (int)2.0, (int)3.0, -0.5F);
        this.head.addChild(this.visor_r1);

        this.visor_r2 = new ModelRenderer(this);
        this.visor_r2.setRotationPoint(0.0F, -7.2031F, -5.9513F);
        setRotationAngle(this.visor_r2, -2.289F, -0.2046F, -0.2284F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(112, 120).addBox(-2.514F, 1.636F, -4.5617F, (int)2.0, (int)2.0, (int)6.0, -0.2F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(116, 55).addBox(-2.514F, 1.636F, -0.5617F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(88, 104).addBox(-2.514F, 1.636F, -2.5617F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(112, 52).addBox(-2.3839F, 1.5488F, -4.5494F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(106, 110).addBox(-2.3839F, 1.5488F, -4.9668F, (int)2.0, (int)2.0, (int)1.0, -0.3F);
        this.head.addChild(this.visor_r2);

        this.visor_r3 = new ModelRenderer(this);
        this.visor_r3.setRotationPoint(0.0F, -7.2031F, -5.9513F);
        setRotationAngle(this.visor_r3, -2.3126F, 0.0F, 0.0F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(106, 110).addBox(-1.0F, 1.5488F, -4.5146F, (int)2.0, (int)2.0, (int)1.0, -0.3F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(74, 101).addBox(-1.0F, 1.5488F, -4.0971F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(110, 84).addBox(-1.0F, 1.636F, -2.1009F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(92, 97).addBox(-1.0F, 1.636F, -0.1009F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(77, 119).addBox(-1.0F, 1.636F, -4.1009F, (int)2.0, (int)2.0, (int)6.0, -0.2F);
        this.head.addChild(this.visor_r3);

        this.visor_r4 = new ModelRenderer(this);
        this.visor_r4.setRotationPoint(0.0F, -7.2031F, -5.9513F);
        setRotationAngle(this.visor_r4, -2.3998F, 0.0F, 0.0F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(51, 108).addBox(-4.0F, 0.7628F, -1.7077F, (int)8.0, (int)2.0, (int)3.0, -0.5F);
        this.head.addChild(this.visor_r4);

        this.visor_r5 = new ModelRenderer(this);
        this.visor_r5.setRotationPoint(0.0F, -5.6502F, -5.0344F);
        setRotationAngle(this.visor_r5, -0.5236F, 0.0F, 0.0F);
        this.visor_r5.mirror = false;
        this.visor_r5.setTextureOffset(98, 99).addBox(-1.0F, -1.2087F, -3.041F, (int)2.0, (int)2.0, (int)4.0, -0.6F);
        this.head.addChild(this.visor_r5);

        this.visor_r6 = new ModelRenderer(this);
        this.visor_r6.setRotationPoint(0.0F, -7.2628F, -4.1938F);
        setRotationAngle(this.visor_r6, 0.2618F, 0.0F, 0.0F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(114, 91).addBox(-2.0F, -0.713F, -2.0102F, (int)4.0, (int)3.0, (int)3.0, -0.6F);
        this.head.addChild(this.visor_r6);

        this.visor_r7 = new ModelRenderer(this);
        this.visor_r7.setRotationPoint(0.0F, -7.2031F, -5.9513F);
        setRotationAngle(this.visor_r7, -2.289F, 0.2046F, 0.2284F);
        this.visor_r7.mirror = true;
        this.visor_r7.setTextureOffset(50, 120).addBox(0.514F, 1.636F, -4.5617F, (int)2.0, (int)2.0, (int)6.0, -0.2F);
        this.visor_r7.mirror = true;
        this.visor_r7.setTextureOffset(116, 55).addBox(0.514F, 1.636F, -0.5617F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r7.mirror = true;
        this.visor_r7.setTextureOffset(88, 104).addBox(0.514F, 1.636F, -2.5617F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r7.mirror = true;
        this.visor_r7.setTextureOffset(112, 52).addBox(0.3839F, 1.5488F, -4.5494F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r7.mirror = true;
        this.visor_r7.setTextureOffset(106, 110).addBox(0.3839F, 1.5488F, -4.9668F, (int)2.0, (int)2.0, (int)1.0, -0.3F);
        this.head.addChild(this.visor_r7);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 25).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(0.0F, 3.5F, 3.9347F);
        setRotationAngle(this.body_r1, 0.0436F, 0.0F, 0.0F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(42, 80).addBox(-3.0F, -0.5F, -2.0F, (int)1.0, (int)4.0, (int)4.0, 0.1F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(52, 35).addBox(-4.0F, -0.5F, -2.0F, (int)1.0, (int)1.0, (int)4.0, 0.05F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(6, 86).addBox(-4.0F, 1.5F, -2.0F, (int)1.0, (int)1.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(0.0F, 9.1338F, 3.5632F);
        setRotationAngle(this.body_r2, -0.1309F, 0.0F, 0.0F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(28, 52).addBox(-2.0F, -1.7393F, -1.2215F, (int)1.0, (int)4.0, (int)2.0, 0.2F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(0.0F, 9.1338F, 3.5632F);
        setRotationAngle(this.body_r3, -0.1334F, -0.2333F, -0.101F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(70, 44).addBox(-4.9848F, -1.2657F, -0.3434F, (int)1.0, (int)2.0, (int)2.0, 0.25F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(89, 70).addBox(-4.9848F, -1.2657F, -0.3434F, (int)2.0, (int)3.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(4.0F, 8.0F, -2.9F);
        setRotationAngle(this.body_r4, 3.098F, 0.0F, -3.1416F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(44, 0).addBox(3.5F, 0.4F, 0.3F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(3.0F, 8.0F, -2.9F);
        setRotationAngle(this.body_r5, 3.098F, 0.0F, -3.1416F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(18, 50).addBox(2.0F, -0.6F, -0.9F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(34, 82).addBox(2.0F, -0.6F, -0.6F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(3.0F, 9.4161F, -3.2386F);
        setRotationAngle(this.body_r6, 3.0964F, 0.2615F, 3.1299F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(68, 35).addBox(-1.0F, -2.0F, -1.2614F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(44, 16).addBox(-0.5F, -1.0F, 0.0F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(58, 67).addBox(-1.0F, -2.0F, -1.0F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(0.0F, 8.0F, -2.9F);
        setRotationAngle(this.body_r7, 3.098F, 0.0F, 3.1416F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(0, 78).addBox(-3.5F, -2.6F, -0.6F, (int)7.0, (int)5.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(0.0F, 3.5F, 3.9347F);
        setRotationAngle(this.body_r8, 0.0436F, 0.0F, 0.0F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(28, 82).addBox(2.0F, -0.5F, -2.0F, (int)1.0, (int)4.0, (int)4.0, 0.1F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(24, 25).addBox(-4.0F, -3.5F, -2.0F, (int)8.0, (int)7.0, (int)4.0, -0.15F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(12, 60).addBox(3.0F, 1.5F, -2.0F, (int)1.0, (int)1.0, (int)4.0, 0.05F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(22, 86).addBox(3.0F, -0.5F, -2.0F, (int)1.0, (int)1.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(0.0F, 3.5F, 3.9347F);
        setRotationAngle(this.body_r9, 0.0873F, 0.0F, 0.0F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(24, 16).addBox(-4.0F, -3.4981F, -1.9128F, (int)8.0, (int)3.0, (int)4.0, 0.2F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(0.0F, 6.5015F, 2.4173F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(48, 0).addBox(-4.0F, -4.5015F, -1.4827F, (int)8.0, (int)9.0, (int)2.0, -0.15F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(84, 52).addBox(-3.0F, -2.0015F, -0.4827F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(84, 33).addBox(-3.0F, -3.5015F, -0.4827F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(0.0F, 9.1338F, 3.5632F);
        setRotationAngle(this.body_r11, -0.1309F, 0.0F, 0.0F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(79, 27).addBox(-3.0F, -1.7393F, -1.2215F, (int)6.0, (int)2.0, (int)2.0, 0.3F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(51, 76).addBox(-3.0F, -1.7393F, -1.2215F, (int)6.0, (int)4.0, (int)2.0, 0.05F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(37, 52).addBox(1.0F, -1.7393F, -1.2215F, (int)1.0, (int)4.0, (int)2.0, 0.2F);
        this.body.addChild(this.body_r11);

        this.body_r12 = new ModelRenderer(this);
        this.body_r12.setRotationPoint(0.0F, 9.1338F, 3.5632F);
        setRotationAngle(this.body_r12, -0.1334F, 0.2333F, 0.101F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(34, 72).addBox(3.9848F, -1.2657F, -0.3434F, (int)1.0, (int)2.0, (int)2.0, 0.25F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(0, 90).addBox(2.9848F, -1.2657F, -0.3434F, (int)2.0, (int)3.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r12);

        this.body_r13 = new ModelRenderer(this);
        this.body_r13.setRotationPoint(0.0F, 6.5015F, 2.4173F);
        setRotationAngle(this.body_r13, -0.0436F, 0.0F, 0.0F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(76, 44).addBox(-3.5F, -1.1167F, -0.2176F, (int)7.0, (int)5.0, (int)1.0, 0.05F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(84, 54).addBox(-3.0F, -0.5167F, -0.0176F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(84, 31).addBox(-3.0F, 0.9833F, -0.0176F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(83, 50).addBox(-3.0F, 2.4833F, -0.0176F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body.addChild(this.body_r13);

        this.body_r14 = new ModelRenderer(this);
        this.body_r14.setRotationPoint(-3.0F, 9.4161F, -3.2386F);
        setRotationAngle(this.body_r14, 3.0964F, -0.2615F, -3.1299F);
        this.body_r14.mirror = false;
        this.body_r14.setTextureOffset(90, 0).addBox(-1.0F, -2.0F, -1.0F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body_r14.mirror = false;
        this.body_r14.setTextureOffset(44, 24).addBox(-0.5F, -1.0F, 0.0F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r14.mirror = false;
        this.body_r14.setTextureOffset(72, 86).addBox(-1.0F, -2.0F, -1.2614F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body.addChild(this.body_r14);

        this.body_r15 = new ModelRenderer(this);
        this.body_r15.setRotationPoint(0.0F, 8.0F, -2.9F);
        setRotationAngle(this.body_r15, 3.098F, 0.0F, -3.1416F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(86, 42).addBox(-3.0F, 1.0F, -0.4F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(84, 56).addBox(-3.0F, -2.0F, -0.4F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(83, 84).addBox(-3.0F, -0.5F, -0.4F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body.addChild(this.body_r15);

        this.body_r16 = new ModelRenderer(this);
        this.body_r16.setRotationPoint(0.0F, 6.5F, -2.55F);
        setRotationAngle(this.body_r16, 0.0F, 3.1416F, 0.0F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(69, 84).addBox(-3.0F, -3.5F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(84, 64).addBox(-3.0F, -2.0F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(48, 24).addBox(-4.0F, -4.5F, -1.45F, (int)8.0, (int)9.0, (int)2.0, -0.15F);
        this.body.addChild(this.body_r16);

        this.body_r17 = new ModelRenderer(this);
        this.body_r17.setRotationPoint(3.0F, 0.95F, 0.0F);
        setRotationAngle(this.body_r17, 0.0F, 0.0F, 0.0873F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(35, 72).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)2.0, (int)6.0, -0.15F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(88, 86).addBox(-1.0F, -0.95F, -3.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(60, 89).addBox(-1.0F, -0.95F, 1.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(69, 58).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)1.0, (int)6.0, 0.05F);
        this.body.addChild(this.body_r17);

        this.body_r18 = new ModelRenderer(this);
        this.body_r18.setRotationPoint(-3.0F, 5.95F, 1.0F);
        setRotationAngle(this.body_r18, 0.0F, 0.0F, -0.0436F);
        this.body_r18.mirror = false;
        this.body_r18.setTextureOffset(0, 68).addBox(-2.0F, 0.05F, -3.5F, (int)3.0, (int)4.0, (int)5.0, -0.3F);
        this.body.addChild(this.body_r18);

        this.body_r19 = new ModelRenderer(this);
        this.body_r19.setRotationPoint(0.0F, 7.0208F, 0.0F);
        setRotationAngle(this.body_r19, 0.0F, 0.0F, 0.0436F);
        this.body_r19.mirror = false;
        this.body_r19.setTextureOffset(56, 35).addBox(-4.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r19);

        this.body_r20 = new ModelRenderer(this);
        this.body_r20.setRotationPoint(-3.0F, 7.95F, 0.0F);
        setRotationAngle(this.body_r20, 0.0F, 0.0F, -0.0436F);
        this.body_r20.mirror = false;
        this.body_r20.setTextureOffset(58, 43).addBox(-2.0F, 0.05F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.addChild(this.body_r20);

        this.body_r21 = new ModelRenderer(this);
        this.body_r21.setRotationPoint(-3.0F, 0.95F, 0.0F);
        setRotationAngle(this.body_r21, 0.0F, 0.0F, -0.0873F);
        this.body_r21.mirror = false;
        this.body_r21.setTextureOffset(43, 88).addBox(-1.0F, -0.95F, 1.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r21.mirror = false;
        this.body_r21.setTextureOffset(14, 89).addBox(-1.0F, -0.95F, -3.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r21.mirror = false;
        this.body_r21.setTextureOffset(68, 0).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)1.0, (int)6.0, 0.05F);
        this.body_r21.mirror = false;
        this.body_r21.setTextureOffset(72, 70).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)2.0, (int)6.0, -0.15F);
        this.body.addChild(this.body_r21);

        this.body_r22 = new ModelRenderer(this);
        this.body_r22.setRotationPoint(3.0F, 7.95F, 0.0F);
        setRotationAngle(this.body_r22, 0.0F, 0.0F, 0.0436F);
        this.body_r22.mirror = false;
        this.body_r22.setTextureOffset(57, 59).addBox(-1.0F, 0.05F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.addChild(this.body_r22);

        this.body_r23 = new ModelRenderer(this);
        this.body_r23.setRotationPoint(0.0F, 7.0208F, 0.0F);
        setRotationAngle(this.body_r23, 0.0F, 0.0F, -0.0436F);
        this.body_r23.mirror = false;
        this.body_r23.setTextureOffset(0, 60).addBox(1.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r23);

        this.body_r24 = new ModelRenderer(this);
        this.body_r24.setRotationPoint(3.0F, 5.95F, 1.0F);
        setRotationAngle(this.body_r24, 0.0F, 0.0F, 0.0436F);
        this.body_r24.mirror = false;
        this.body_r24.setTextureOffset(68, 26).addBox(-1.0F, 0.05F, -3.5F, (int)3.0, (int)4.0, (int)5.0, -0.3F);
        this.body.addChild(this.body_r24);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(0, 41).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(78, 12).addBox(-1.0F, 6.0F, -2.0F, (int)4.0, (int)2.0, (int)4.0, 0.3F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(1.0F, 4.5F, 0.95F);
        setRotationAngle(this.left_arm_r1, -3.1416F, 0.0F, -3.1416F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(79, 22).addBox(-2.0F, -0.5F, -0.95F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.left_arm.addChild(this.left_arm_r1);

        this.left_arm_r2 = new ModelRenderer(this);
        this.left_arm_r2.setRotationPoint(1.0F, 3.0F, 0.0F);
        setRotationAngle(this.left_arm_r2, 0.0F, 0.0F, -0.0436F);
        this.left_arm_r2.mirror = true;
        this.left_arm_r2.setTextureOffset(34, 61).addBox(-2.0F, -4.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.5F);
        this.left_arm.addChild(this.left_arm_r2);

        this.left_arm_r3 = new ModelRenderer(this);
        this.left_arm_r3.setRotationPoint(1.0F, 4.5F, 0.95F);
        setRotationAngle(this.left_arm_r3, -3.0543F, 0.0873F, 3.1416F);
        this.left_arm_r3.mirror = false;
        this.left_arm_r3.setTextureOffset(0, 57).addBox(-1.5915F, -1.0912F, -1.542F, (int)3.0, (int)2.0, (int)1.0, 0.4F);
        this.left_arm_r3.mirror = false;
        this.left_arm_r3.setTextureOffset(20, 25).addBox(-1.5915F, -1.5912F, -1.542F, (int)3.0, (int)3.0, (int)1.0, 0.2F);
        this.left_arm.addChild(this.left_arm_r3);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(0, 41).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(78, 12).addBox(-3.0F, 6.0F, -2.0F, (int)4.0, (int)2.0, (int)4.0, 0.3F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-1.0F, 4.5F, 0.95F);
        setRotationAngle(this.right_arm_r1, -3.0543F, -0.0873F, -3.1416F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(48, 11).addBox(-1.4085F, -1.5912F, -1.542F, (int)3.0, (int)3.0, (int)1.0, 0.2F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(91, 35).addBox(-1.4085F, -1.0912F, -1.542F, (int)3.0, (int)2.0, (int)1.0, 0.4F);
        this.right_arm.addChild(this.right_arm_r1);

        this.right_arm_r2 = new ModelRenderer(this);
        this.right_arm_r2.setRotationPoint(-1.0F, 4.5F, 0.95F);
        setRotationAngle(this.right_arm_r2, -3.1416F, 0.0F, 3.1416F);
        this.right_arm_r2.mirror = false;
        this.right_arm_r2.setTextureOffset(79, 22).addBox(-2.0F, -0.5F, -0.95F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_arm.addChild(this.right_arm_r2);

        this.right_arm_r3 = new ModelRenderer(this);
        this.right_arm_r3.setRotationPoint(-1.0F, 3.0F, 0.0F);
        setRotationAngle(this.right_arm_r3, 0.0F, 0.0F, 0.0436F);
        this.right_arm_r3.mirror = false;
        this.right_arm_r3.setTextureOffset(34, 61).addBox(-2.0F, -4.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.5F);
        this.right_arm.addChild(this.right_arm_r3);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(32, 0).addBox(-1.9F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(79, 35).addBox(-1.9F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(63, 78).addBox(-1.9F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.left_leg_r1 = new ModelRenderer(this);
        this.left_leg_r1.setRotationPoint(0.1F, 4.5F, -2.1F);
        setRotationAngle(this.left_leg_r1, 0.0873F, -0.0873F, 0.0F);
        this.left_leg_r1.mirror = false;
        this.left_leg_r1.setTextureOffset(36, 36).addBox(-1.5F, -1.5F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.2F);
        this.left_leg_r1.mirror = false;
        this.left_leg_r1.setTextureOffset(91, 22).addBox(-1.5F, -1.0F, -0.5F, (int)3.0, (int)2.0, (int)1.0, 0.4F);
        this.left_leg.addChild(this.left_leg_r1);

        this.left_leg_r2 = new ModelRenderer(this);
        this.left_leg_r2.setRotationPoint(3.1F, 4.9253F, -0.2885F);
        setRotationAngle(this.left_leg_r2, -0.0863F, 1.5272F, -0.001F);
        this.left_leg_r2.mirror = false;
        this.left_leg_r2.setTextureOffset(44, 0).addBox(-0.5F, -0.5075F, -0.1285F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.left_leg_r2.mirror = false;
        this.left_leg_r2.setTextureOffset(34, 82).addBox(-1.0F, -1.5075F, -1.0285F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.left_leg_r2.mirror = false;
        this.left_leg_r2.setTextureOffset(18, 50).addBox(-1.0F, -1.5075F, -1.3285F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.left_leg.addChild(this.left_leg_r2);

        this.left_leg_r3 = new ModelRenderer(this);
        this.left_leg_r3.setRotationPoint(3.1F, 0.9253F, -0.2885F);
        setRotationAngle(this.left_leg_r3, -0.0863F, 1.5272F, -0.001F);
        this.left_leg_r3.mirror = false;
        this.left_leg_r3.setTextureOffset(44, 0).addBox(-0.5F, -0.5075F, -0.1285F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.left_leg_r3.mirror = false;
        this.left_leg_r3.setTextureOffset(34, 82).addBox(-1.0F, -1.5075F, -1.0285F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.left_leg_r3.mirror = false;
        this.left_leg_r3.setTextureOffset(18, 50).addBox(-1.0F, -1.5075F, -1.3285F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.left_leg.addChild(this.left_leg_r3);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(75, 79).addBox(-2.1F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(79, 58).addBox(-2.1F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(24, 36).addBox(-2.1F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_leg_r1 = new ModelRenderer(this);
        this.right_leg_r1.setRotationPoint(-0.1F, 4.5F, -2.1F);
        setRotationAngle(this.right_leg_r1, 0.0873F, 0.0873F, 0.0F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(91, 58).addBox(-1.5F, -1.0F, -0.5F, (int)3.0, (int)2.0, (int)1.0, 0.4F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(90, 11).addBox(-1.5F, -1.5F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.2F);
        this.right_leg.addChild(this.right_leg_r1);

        this.right_leg_r2 = new ModelRenderer(this);
        this.right_leg_r2.setRotationPoint(-3.1F, 2.0F, -0.5F);
        setRotationAngle(this.right_leg_r2, 0.0925F, 0.348F, 0.024F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(58, 43).addBox(-0.7412F, 0.0F, -1.4659F, (int)2.0, (int)4.0, (int)1.0, 0.05F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(12, 41).addBox(-0.7412F, 0.0F, -1.4659F, (int)2.0, (int)2.0, (int)1.0, 0.25F);
        this.right_leg.addChild(this.right_leg_r2);

        this.right_leg_r3 = new ModelRenderer(this);
        this.right_leg_r3.setRotationPoint(-3.1F, 2.0F, -0.5F);
        setRotationAngle(this.right_leg_r3, 0.0873F, 0.0873F, 0.0F);
        this.right_leg_r3.mirror = false;
        this.right_leg_r3.setTextureOffset(0, 16).addBox(-1.0F, -3.0F, 0.5F, (int)2.0, (int)7.0, (int)1.0, 0.25F);
        this.right_leg_r3.mirror = false;
        this.right_leg_r3.setTextureOffset(56, 55).addBox(-1.0F, -3.0F, -0.5F, (int)2.0, (int)2.0, (int)2.0, 0.05F);
        this.right_leg_r3.mirror = false;
        this.right_leg_r3.setTextureOffset(52, 82).addBox(-1.0F, -1.0F, -0.5F, (int)2.0, (int)5.0, (int)3.0, 0.05F);
        this.right_leg.addChild(this.right_leg_r3);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(66, 17).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.49F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(54, 21).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.bipedLeftLeg.addChild(this.left_shoe);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(66, 17).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.49F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(54, 21).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(64, 7).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.4F);
        this.bipedRightLeg.addChild(this.right_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
