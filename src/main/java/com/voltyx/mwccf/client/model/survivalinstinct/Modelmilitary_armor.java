package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelmilitary_armor extends ModelBiped {

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
    public ModelRenderer left_arm;
    public ModelRenderer left_arm_r1;
    public ModelRenderer right_arm;
    public ModelRenderer right_arm_r1;
    public ModelRenderer left_leg;
    public ModelRenderer left_leg_r1;
    public ModelRenderer right_leg;
    public ModelRenderer right_leg_r1;
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe;

    public Modelmilitary_armor() {
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
        this.head.setTextureOffset(62, 52).addBox(-4.0F, -8.0F, 4.0F, (int)8.0, (int)6.0, (int)1.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(72, 48).addBox(-4.0F, -8.0F, -5.0F, (int)8.0, (int)3.0, (int)1.0, 0.05F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(4.4163F, -6.7392F, 0.0F);
        setRotationAngle(this.head_r1, 0.0F, 0.0F, -0.0873F);
        this.head_r1.mirror = true;
        this.head_r1.setTextureOffset(1, 79).addBox(-0.4163F, 0.7392F, 1.0F, (int)1.0, (int)3.0, (int)3.0, -0.01F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(0, 16).addBox(-0.4163F, 0.7392F, 1.0F, (int)1.0, (int)2.0, (int)3.0, -0.01F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(44, 45).addBox(-0.4163F, -1.2608F, -4.0F, (int)1.0, (int)3.0, (int)8.0, 0.05F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(0.0F, -4.412F, 0.912F);
        setRotationAngle(this.head_r2, -0.6504F, -0.0795F, -0.1041F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(18, 80).addBox(-5.2222F, -0.6075F, -0.5F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(0.0F, -4.412F, 0.912F);
        setRotationAngle(this.head_r3, -0.6504F, 0.0795F, 0.1041F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(22, 80).addBox(4.2222F, -0.6075F, -0.5F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r4, -0.2615F, -0.0076F, -0.0869F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(44, 56).addBox(4.0907F, -0.1044F, -2.25F, (int)1.0, (int)1.0, (int)3.0, 0.3F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r5, 0.0F, 0.0F, 0.1309F);
        this.head_r5.mirror = false;
        this.head_r5.setTextureOffset(66, 62).addBox(-0.3353F, 0.4848F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r6, 0.0F, 0.0F, -0.0873F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(67, 25).addBox(-0.4042F, -1.4939F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r6);

        this.head_r7 = new ModelRenderer(this);
        this.head_r7.setRotationPoint(-4.4163F, -6.7392F, 2.0F);
        setRotationAngle(this.head_r7, 0.0F, 0.0F, 0.0873F);
        this.head_r7.mirror = true;
        this.head_r7.setTextureOffset(0, 16).addBox(-0.5837F, 0.7392F, -1.0F, (int)1.0, (int)2.0, (int)3.0, -0.01F);
        this.head_r7.mirror = false;
        this.head_r7.setTextureOffset(0, 0).addBox(-0.5837F, 0.7392F, -1.0F, (int)1.0, (int)3.0, (int)3.0, -0.01F);
        this.head_r7.mirror = false;
        this.head_r7.setTextureOffset(12, 44).addBox(-0.5837F, -1.2608F, -6.0F, (int)1.0, (int)3.0, (int)8.0, 0.05F);
        this.head.addChild(this.head_r7);

        this.head_r8 = new ModelRenderer(this);
        this.head_r8.setRotationPoint(4.4163F, -4.7392F, -5.0F);
        setRotationAngle(this.head_r8, 0.0F, 0.0F, -0.0873F);
        this.head_r8.mirror = false;
        this.head_r8.setTextureOffset(57, 59).addBox(0.2837F, -2.2608F, 2.0F, (int)1.0, (int)3.0, (int)7.0, -0.1F);
        this.head.addChild(this.head_r8);

        this.head_r9 = new ModelRenderer(this);
        this.head_r9.setRotationPoint(4.5907F, -4.5F, -1.0F);
        setRotationAngle(this.head_r9, 0.0F, 0.0F, 0.0436F);
        this.head_r9.mirror = false;
        this.head_r9.setTextureOffset(42, 66).addBox(-0.5F, -0.5F, -2.0F, (int)1.0, (int)1.0, (int)5.0, 0.3F);
        this.head.addChild(this.head_r9);

        this.head_r10 = new ModelRenderer(this);
        this.head_r10.setRotationPoint(-5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r10, 0.0F, 0.0F, 0.0873F);
        this.head_r10.mirror = false;
        this.head_r10.setTextureOffset(24, 16).addBox(-0.5958F, -1.4939F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r10);

        this.head_r11 = new ModelRenderer(this);
        this.head_r11.setRotationPoint(-4.4163F, -4.7392F, -5.0F);
        setRotationAngle(this.head_r11, 0.0F, 0.0F, 0.0873F);
        this.head_r11.mirror = false;
        this.head_r11.setTextureOffset(58, 21).addBox(-1.2837F, -2.2608F, 2.0F, (int)1.0, (int)3.0, (int)7.0, -0.1F);
        this.head.addChild(this.head_r11);

        this.head_r12 = new ModelRenderer(this);
        this.head_r12.setRotationPoint(-5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r12, 0.0F, 0.0F, -0.1309F);
        this.head_r12.mirror = false;
        this.head_r12.setTextureOffset(48, 66).addBox(-0.6647F, 0.4848F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r12);

        this.head_r13 = new ModelRenderer(this);
        this.head_r13.setRotationPoint(-4.1972F, -6.5038F, -4.87F);
        setRotationAngle(this.head_r13, 0.0F, -0.6545F, 0.0F);
        this.head_r13.mirror = false;
        this.head_r13.setTextureOffset(0, 25).addBox(0.1972F, -1.5F, -0.13F, (int)1.0, (int)3.0, (int)1.0, 0.13F);
        this.head.addChild(this.head_r13);

        this.head_r14 = new ModelRenderer(this);
        this.head_r14.setRotationPoint(-4.5F, -5.0F, 3.5F);
        setRotationAngle(this.head_r14, 0.0F, -0.7418F, 0.0F);
        this.head_r14.mirror = false;
        this.head_r14.setTextureOffset(24, 16).addBox(0.2F, -3.0F, -0.1F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r14);

        this.head_r15 = new ModelRenderer(this);
        this.head_r15.setRotationPoint(3.9658F, -4.5038F, -4.1154F);
        setRotationAngle(this.head_r15, 0.0983F, 0.478F, 0.0453F);
        this.head_r15.mirror = false;
        this.head_r15.setTextureOffset(33, 20).addBox(-0.5F, -0.5F, -0.5F, (int)1.0, (int)1.0, (int)2.0, 0.3F);
        this.head.addChild(this.head_r15);

        this.head_r16 = new ModelRenderer(this);
        this.head_r16.setRotationPoint(4.1972F, -6.5038F, -4.87F);
        setRotationAngle(this.head_r16, 0.0F, 0.6545F, 0.0F);
        this.head_r16.mirror = false;
        this.head_r16.setTextureOffset(32, 0).addBox(-1.1972F, -1.5F, -0.13F, (int)1.0, (int)3.0, (int)1.0, 0.13F);
        this.head.addChild(this.head_r16);

        this.head_r17 = new ModelRenderer(this);
        this.head_r17.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r17, -0.1181F, 0.7383F, -0.0797F);
        this.head_r17.mirror = false;
        this.head_r17.setTextureOffset(0, 6).addBox(1.9489F, -0.494F, 3.1245F, (int)1.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r17);

        this.head_r18 = new ModelRenderer(this);
        this.head_r18.setRotationPoint(4.5F, -5.0F, 3.5F);
        setRotationAngle(this.head_r18, 0.0F, 0.7418F, 0.0F);
        this.head_r18.mirror = false;
        this.head_r18.setTextureOffset(32, 47).addBox(-1.2F, -3.0F, -0.1F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r18);

        this.head_r19 = new ModelRenderer(this);
        this.head_r19.setRotationPoint(-4.1258F, -8.0425F, 0.0F);
        setRotationAngle(this.head_r19, 0.0F, 0.0F, -0.8727F);
        this.head_r19.mirror = false;
        this.head_r19.setTextureOffset(22, 47).addBox(-0.3742F, -0.5F, -4.0F, (int)1.0, (int)1.0, (int)8.0, 0.1F);
        this.head.addChild(this.head_r19);

        this.head_r20 = new ModelRenderer(this);
        this.head_r20.setRotationPoint(-4.5907F, -4.5F, -1.0F);
        setRotationAngle(this.head_r20, 0.0F, 0.0F, -0.0436F);
        this.head_r20.mirror = false;
        this.head_r20.setTextureOffset(24, 66).addBox(-0.5F, -0.5F, -2.0F, (int)1.0, (int)1.0, (int)5.0, 0.3F);
        this.head.addChild(this.head_r20);

        this.head_r21 = new ModelRenderer(this);
        this.head_r21.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r21, -0.2615F, 0.0076F, 0.0869F);
        this.head_r21.mirror = true;
        this.head_r21.setTextureOffset(44, 56).addBox(-5.0907F, -0.1044F, -2.25F, (int)1.0, (int)1.0, (int)3.0, 0.3F);
        this.head.addChild(this.head_r21);

        this.head_r22 = new ModelRenderer(this);
        this.head_r22.setRotationPoint(0.0F, -7.5F, -4.5F);
        setRotationAngle(this.head_r22, -0.1745F, 0.0F, 0.0F);
        this.head_r22.mirror = false;
        this.head_r22.setTextureOffset(56, 21).addBox(-1.5F, 0.0F, -0.5F, (int)3.0, (int)2.0, (int)1.0, 0.2F);
        this.head.addChild(this.head_r22);

        this.head_r23 = new ModelRenderer(this);
        this.head_r23.setRotationPoint(0.0F, -8.5F, -4.5F);
        setRotationAngle(this.head_r23, -0.5672F, 0.0F, 0.0F);
        this.head_r23.mirror = false;
        this.head_r23.setTextureOffset(75, 67).addBox(-4.0F, -0.5F, 0.0F, (int)8.0, (int)1.0, (int)1.0, 0.1F);
        this.head.addChild(this.head_r23);

        this.head_r24 = new ModelRenderer(this);
        this.head_r24.setRotationPoint(0.0F, -4.5F, -4.5F);
        setRotationAngle(this.head_r24, 0.0873F, 0.0F, 0.0F);
        this.head_r24.mirror = false;
        this.head_r24.setTextureOffset(78, 0).addBox(-4.0F, -0.5F, -0.5F, (int)8.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r24);

        this.head_r25 = new ModelRenderer(this);
        this.head_r25.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r25, -0.0873F, 0.0F, 0.0F);
        this.head_r25.mirror = false;
        this.head_r25.setTextureOffset(75, 65).addBox(-4.0F, -0.494F, 0.75F, (int)8.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r25);

        this.head_r26 = new ModelRenderer(this);
        this.head_r26.setRotationPoint(0.0F, -8.2172F, 4.2172F);
        setRotationAngle(this.head_r26, 0.7418F, 0.0F, 0.0F);
        this.head_r26.mirror = false;
        this.head_r26.setTextureOffset(76, 30).addBox(-4.0F, -0.5F, -0.8172F, (int)8.0, (int)1.0, (int)1.0, 0.18F);
        this.head.addChild(this.head_r26);

        this.head_r27 = new ModelRenderer(this);
        this.head_r27.setRotationPoint(4.1258F, -8.0425F, 0.0F);
        setRotationAngle(this.head_r27, 0.0F, 0.0F, 0.8727F);
        this.head_r27.mirror = false;
        this.head_r27.setTextureOffset(48, 24).addBox(-0.6258F, -0.5F, -4.0F, (int)1.0, (int)1.0, (int)8.0, 0.1F);
        this.head.addChild(this.head_r27);

        this.head_r28 = new ModelRenderer(this);
        this.head_r28.setRotationPoint(-3.9658F, -4.5038F, -4.1154F);
        setRotationAngle(this.head_r28, 0.0983F, -0.478F, -0.0453F);
        this.head_r28.mirror = false;
        this.head_r28.setTextureOffset(54, 44).addBox(-0.5F, -0.5F, -0.5F, (int)1.0, (int)1.0, (int)2.0, 0.3F);
        this.head.addChild(this.head_r28);

        this.head_r29 = new ModelRenderer(this);
        this.head_r29.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r29, -0.1181F, -0.7383F, 0.0797F);
        this.head_r29.mirror = false;
        this.head_r29.setTextureOffset(4, 6).addBox(-2.9489F, -0.494F, 3.1245F, (int)1.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r29);

        this.head_r30 = new ModelRenderer(this);
        this.head_r30.setRotationPoint(0.0F, -4.412F, 0.912F);
        setRotationAngle(this.head_r30, -0.6545F, 0.0F, 0.0F);
        this.head_r30.mirror = false;
        this.head_r30.setTextureOffset(18, 41).addBox(-4.0F, 4.9969F, -0.5F, (int)8.0, (int)1.0, (int)1.0, -0.001F);
        this.head.addChild(this.head_r30);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 25).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(0.0F, 6.5015F, 2.4173F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(0, 41).addBox(-4.0F, -4.5015F, -1.4827F, (int)8.0, (int)9.0, (int)2.0, -0.15F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(78, 2).addBox(-3.0F, -2.0015F, -0.4827F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(22, 45).addBox(-3.0F, -3.5015F, -0.4827F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(0.0F, 6.5015F, 2.4173F);
        setRotationAngle(this.body_r2, -0.0436F, 0.0F, 0.0F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(68, 33).addBox(-3.5F, -1.1167F, -0.2176F, (int)7.0, (int)5.0, (int)1.0, 0.05F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(40, 33).addBox(-3.0F, -0.5167F, -0.0176F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(40, 35).addBox(-3.0F, 0.9833F, -0.0176F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(22, 43).addBox(-3.0F, 2.4833F, -0.0176F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(0.0F, 8.0F, -2.9F);
        setRotationAngle(this.body_r3, 3.098F, 0.0F, -3.1416F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(68, 33).addBox(-3.5F, -2.6F, -0.6F, (int)7.0, (int)5.0, (int)1.0, 0.05F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(22, 43).addBox(-3.0F, 1.0F, -0.4F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(40, 33).addBox(-3.0F, -2.0F, -0.4F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(40, 35).addBox(-3.0F, -0.5F, -0.4F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(0.0F, 6.5F, -2.55F);
        setRotationAngle(this.body_r4, 0.0F, 3.1416F, 0.0F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(22, 45).addBox(-3.0F, -3.5F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(78, 2).addBox(-3.0F, -2.0F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(0, 41).addBox(-4.0F, -4.5F, -1.45F, (int)8.0, (int)9.0, (int)2.0, -0.15F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(3.0F, 0.95F, 0.0F);
        setRotationAngle(this.body_r5, 0.0F, 0.0F, 0.0873F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(68, 0).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)2.0, (int)6.0, -0.15F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(22, 47).addBox(-1.0F, -0.95F, -3.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(10, 80).addBox(-1.0F, -0.95F, 1.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(0, 71).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)1.0, (int)6.0, 0.05F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(-3.0F, 5.95F, 1.0F);
        setRotationAngle(this.body_r6, 0.0F, 0.0F, -0.0436F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(66, 39).addBox(-2.0F, 0.05F, -3.5F, (int)3.0, (int)4.0, (int)5.0, -0.3F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(0.0F, 7.0208F, 0.0F);
        setRotationAngle(this.body_r7, 0.0F, 0.0F, 0.0436F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(54, 44).addBox(-4.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(-3.0F, 7.95F, 0.0F);
        setRotationAngle(this.body_r8, 0.0F, 0.0F, -0.0436F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(0, 63).addBox(-2.0F, 0.05F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(-3.0F, 0.95F, 0.0F);
        setRotationAngle(this.body_r9, 0.0F, 0.0F, -0.0873F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(24, 0).addBox(-1.0F, -0.95F, 1.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(48, 11).addBox(-1.0F, -0.95F, -3.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(68, 8).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)1.0, (int)6.0, 0.05F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(58, 70).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)2.0, (int)6.0, -0.15F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(3.0F, 7.95F, 0.0F);
        setRotationAngle(this.body_r10, 0.0F, 0.0F, 0.0436F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(12, 66).addBox(-1.0F, 0.05F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(0.0F, 7.0208F, 0.0F);
        setRotationAngle(this.body_r11, 0.0F, 0.0F, -0.0436F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(30, 66).addBox(1.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r11);

        this.body_r12 = new ModelRenderer(this);
        this.body_r12.setRotationPoint(3.0F, 5.95F, 1.0F);
        setRotationAngle(this.body_r12, 0.0F, 0.0F, 0.0436F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(67, 16).addBox(-1.0F, 0.05F, -3.5F, (int)3.0, (int)4.0, (int)5.0, -0.3F);
        this.body.addChild(this.body_r12);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(40, 16).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(68, 70).addBox(-1.0F, 6.0F, -2.0F, (int)4.0, (int)2.0, (int)4.0, 0.3F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(1.0F, 3.0F, 0.0F);
        setRotationAngle(this.left_arm_r1, 0.0F, 0.0F, -0.0436F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(0, 52).addBox(-2.0F, -4.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.5F);
        this.left_arm.addChild(this.left_arm_r1);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(40, 16).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(68, 70).addBox(-3.0F, 6.0F, -2.0F, (int)4.0, (int)2.0, (int)4.0, 0.3F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-1.0F, 3.0F, 0.0F);
        setRotationAngle(this.right_arm_r1, 0.0F, 0.0F, 0.0436F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(0, 52).addBox(-2.0F, -4.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.5F);
        this.right_arm.addChild(this.right_arm_r1);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(36, 37).addBox(-1.9F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(76, 25).addBox(-1.9F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(76, 25).addBox(-1.9F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.left_leg_r1 = new ModelRenderer(this);
        this.left_leg_r1.setRotationPoint(0.1F, 4.5F, -2.1F);
        setRotationAngle(this.left_leg_r1, 0.0873F, -0.0873F, 0.0F);
        this.left_leg_r1.mirror = true;
        this.left_leg_r1.setTextureOffset(28, 56).addBox(-1.5F, -1.5F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.2F);
        this.left_leg_r1.mirror = true;
        this.left_leg_r1.setTextureOffset(24, 5).addBox(-1.5F, -1.0F, -0.5F, (int)3.0, (int)2.0, (int)1.0, 0.4F);
        this.left_leg.addChild(this.left_leg_r1);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(36, 37).addBox(-2.1F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(76, 25).addBox(-2.1F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(76, 25).addBox(-2.1F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_leg_r1 = new ModelRenderer(this);
        this.right_leg_r1.setRotationPoint(-0.1F, 4.5F, -2.1F);
        setRotationAngle(this.right_leg_r1, 0.0873F, 0.0873F, 0.0F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(24, 5).addBox(-1.5F, -1.0F, -0.5F, (int)3.0, (int)2.0, (int)1.0, 0.4F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(28, 56).addBox(-1.5F, -1.5F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.2F);
        this.right_leg.addChild(this.right_leg_r1);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(48, 56).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.49F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(33, 16).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(32, 56).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.4F);
        this.bipedLeftLeg.addChild(this.left_shoe);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(48, 56).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.49F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(33, 16).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(32, 56).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.4F);
        this.bipedRightLeg.addChild(this.right_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
