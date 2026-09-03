package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelreaper extends ModelBiped {

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
    public ModelRenderer right_leg;
    public ModelRenderer right_leg_r1;
    public ModelRenderer right_leg_r2;
    public ModelRenderer right_shoe;
    public ModelRenderer left_shoe;

    public Modelreaper() {
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
        this.head.setTextureOffset(0, 82).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.1F);
        this.head.mirror = false;
        this.head.setTextureOffset(7, 37).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)1.0, 0.2F);
        this.head.mirror = false;
        this.head.setTextureOffset(74, 98).addBox(-4.0F, -8.0F, -5.0F, (int)8.0, (int)3.0, (int)1.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(66, 83).addBox(-4.0F, -8.0F, 4.0F, (int)8.0, (int)6.0, (int)1.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(96, 80).addBox(-4.0F, -9.0F, -4.0F, (int)8.0, (int)1.0, (int)8.0, 0.05F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(4.4163F, -6.7392F, 2.0F);
        setRotationAngle(this.head_r1, 0.0F, 0.0F, -0.0873F);
        this.head_r1.mirror = true;
        this.head_r1.setTextureOffset(46, 87).addBox(-0.4163F, 0.7392F, -1.0F, (int)1.0, (int)2.0, (int)3.0, -0.01F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(80, 118).addBox(-0.4163F, 0.7392F, -1.0F, (int)1.0, (int)3.0, (int)3.0, -0.01F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(48, 76).addBox(-0.4163F, -1.2608F, -6.0F, (int)1.0, (int)3.0, (int)8.0, 0.05F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(3.0F, 0.0591F, -4.3377F);
        setRotationAngle(this.head_r2, -0.9092F, -0.6922F, -0.2395F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(67, 9).addBox(-1.5F, -1.0F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.6F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(3.1378F, -0.1838F, -4.9465F);
        setRotationAngle(this.head_r3, 2.3693F, -0.1425F, -0.9337F);
        this.head_r3.mirror = true;
        this.head_r3.setTextureOffset(84, 5).addBox(-1.0F, -1.0F, -1.0F, (int)2.0, (int)2.0, (int)2.0, -0.2F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(-3.6349F, 0.5364F, -3.9451F);
        setRotationAngle(this.head_r4, -1.8662F, 0.8828F, -1.0199F);
        this.head_r4.mirror = true;
        this.head_r4.setTextureOffset(38, 67).addBox(-1.5F, 0.0F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.4F);
        this.head_r4.mirror = true;
        this.head_r4.setTextureOffset(38, 67).addBox(-1.5F, -1.0F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.2F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(-3.0F, 0.0591F, -3.3377F);
        setRotationAngle(this.head_r5, -0.9092F, 0.6922F, 0.2395F);
        this.head_r5.mirror = true;
        this.head_r5.setTextureOffset(67, 9).addBox(-1.5F, -1.0F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.6F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(-4.1258F, -8.0425F, 0.0F);
        setRotationAngle(this.head_r6, 0.0F, 0.0F, -0.8727F);
        this.head_r6.mirror = true;
        this.head_r6.setTextureOffset(54, 97).addBox(-0.3742F, -0.5F, -4.0F, (int)1.0, (int)1.0, (int)8.0, 0.1F);
        this.head.addChild(this.head_r6);

        this.head_r7 = new ModelRenderer(this);
        this.head_r7.setRotationPoint(-4.4163F, -6.7392F, 0.0F);
        setRotationAngle(this.head_r7, 0.0F, 0.0F, 0.0873F);
        this.head_r7.mirror = true;
        this.head_r7.setTextureOffset(48, 76).addBox(-0.5837F, -1.2608F, -4.0F, (int)1.0, (int)3.0, (int)8.0, 0.05F);
        this.head_r7.mirror = false;
        this.head_r7.setTextureOffset(46, 87).addBox(-0.5837F, 0.7392F, 1.0F, (int)1.0, (int)2.0, (int)3.0, -0.01F);
        this.head_r7.mirror = true;
        this.head_r7.setTextureOffset(80, 118).addBox(-0.5837F, 0.7392F, 1.0F, (int)1.0, (int)3.0, (int)3.0, -0.01F);
        this.head.addChild(this.head_r7);

        this.head_r8 = new ModelRenderer(this);
        this.head_r8.setRotationPoint(-5.0F, -3.0F, 1.0F);
        setRotationAngle(this.head_r8, -0.0451F, 0.0434F, 0.0219F);
        this.head_r8.mirror = false;
        this.head_r8.setTextureOffset(86, 77).addBox(-0.2982F, -1.1178F, -2.9116F, (int)1.0, (int)2.0, (int)4.0, 0.6F);
        this.head.addChild(this.head_r8);

        this.head_r9 = new ModelRenderer(this);
        this.head_r9.setRotationPoint(0.0F, -5.0F, -0.25F);
        setRotationAngle(this.head_r9, 0.0436F, 0.0F, 0.0F);
        this.head_r9.mirror = false;
        this.head_r9.setTextureOffset(30, 15).addBox(-5.0F, -0.5F, -4.75F, (int)10.0, (int)1.0, (int)5.0, 0.3F);
        this.head.addChild(this.head_r9);

        this.head_r10 = new ModelRenderer(this);
        this.head_r10.setRotationPoint(5.0F, -3.0F, 1.0F);
        setRotationAngle(this.head_r10, -0.0451F, -0.0434F, -0.0219F);
        this.head_r10.mirror = true;
        this.head_r10.setTextureOffset(86, 77).addBox(-0.7018F, -1.1178F, -2.9116F, (int)1.0, (int)2.0, (int)4.0, 0.6F);
        this.head.addChild(this.head_r10);

        this.head_r11 = new ModelRenderer(this);
        this.head_r11.setRotationPoint(0.0F, -4.5F, -3.5F);
        setRotationAngle(this.head_r11, -0.0436F, 0.0F, 0.0F);
        this.head_r11.mirror = false;
        this.head_r11.setTextureOffset(82, 52).addBox(-4.0F, -0.5872F, -1.4962F, (int)8.0, (int)1.0, (int)1.0, 0.4F);
        this.head.addChild(this.head_r11);

        this.head_r12 = new ModelRenderer(this);
        this.head_r12.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r12, -0.1181F, -0.7383F, 0.0797F);
        this.head_r12.mirror = true;
        this.head_r12.setTextureOffset(44, 59).addBox(-2.9489F, -0.494F, 3.1245F, (int)1.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r12);

        this.head_r13 = new ModelRenderer(this);
        this.head_r13.setRotationPoint(0.0F, -8.2172F, 4.2172F);
        setRotationAngle(this.head_r13, 0.7418F, 0.0F, 0.0F);
        this.head_r13.mirror = false;
        this.head_r13.setTextureOffset(28, 112).addBox(-4.0F, -0.5F, -0.8172F, (int)8.0, (int)1.0, (int)1.0, 0.18F);
        this.head.addChild(this.head_r13);

        this.head_r14 = new ModelRenderer(this);
        this.head_r14.setRotationPoint(4.1258F, -8.0425F, 0.0F);
        setRotationAngle(this.head_r14, 0.0F, 0.0F, 0.8727F);
        this.head_r14.mirror = false;
        this.head_r14.setTextureOffset(54, 97).addBox(-0.6258F, -0.5F, -4.0F, (int)1.0, (int)1.0, (int)8.0, 0.1F);
        this.head.addChild(this.head_r14);

        this.head_r15 = new ModelRenderer(this);
        this.head_r15.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r15, -0.2615F, 0.0076F, 0.0869F);
        this.head_r15.mirror = true;
        this.head_r15.setTextureOffset(29, 122).addBox(-5.0907F, -0.1044F, -2.25F, (int)1.0, (int)1.0, (int)3.0, 0.3F);
        this.head.addChild(this.head_r15);

        this.head_r16 = new ModelRenderer(this);
        this.head_r16.setRotationPoint(0.0F, -8.5F, -4.5F);
        setRotationAngle(this.head_r16, -0.5672F, 0.0F, 0.0F);
        this.head_r16.mirror = false;
        this.head_r16.setTextureOffset(79, 72).addBox(-4.0F, -0.5F, 0.0F, (int)8.0, (int)1.0, (int)1.0, 0.1F);
        this.head.addChild(this.head_r16);

        this.head_r17 = new ModelRenderer(this);
        this.head_r17.setRotationPoint(0.0F, -4.5F, -3.5F);
        setRotationAngle(this.head_r17, -0.0492F, -0.4795F, 0.0227F);
        this.head_r17.mirror = true;
        this.head_r17.setTextureOffset(56, 91).addBox(-4.3006F, -0.5574F, 0.7878F, (int)1.0, (int)1.0, (int)2.0, 0.4F);
        this.head.addChild(this.head_r17);

        this.head_r18 = new ModelRenderer(this);
        this.head_r18.setRotationPoint(4.5F, -5.0F, 3.5F);
        setRotationAngle(this.head_r18, 0.0F, 0.7418F, 0.0F);
        this.head_r18.mirror = true;
        this.head_r18.setTextureOffset(93, 58).addBox(-1.2F, -3.0F, -0.1F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r18);

        this.head_r19 = new ModelRenderer(this);
        this.head_r19.setRotationPoint(4.1972F, -6.5038F, -4.87F);
        setRotationAngle(this.head_r19, 0.0F, 0.6545F, 0.0F);
        this.head_r19.mirror = true;
        this.head_r19.setTextureOffset(49, 96).addBox(-1.1972F, -1.5F, -0.13F, (int)1.0, (int)3.0, (int)1.0, 0.13F);
        this.head.addChild(this.head_r19);

        this.head_r20 = new ModelRenderer(this);
        this.head_r20.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r20, -0.0873F, 0.0F, 0.0F);
        this.head_r20.mirror = false;
        this.head_r20.setTextureOffset(78, 50).addBox(-4.0F, -0.494F, 0.75F, (int)8.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r20);

        this.head_r21 = new ModelRenderer(this);
        this.head_r21.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r21, -0.1181F, 0.7383F, -0.0797F);
        this.head_r21.mirror = false;
        this.head_r21.setTextureOffset(44, 59).addBox(1.9489F, -0.494F, 3.1245F, (int)1.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r21);

        this.head_r22 = new ModelRenderer(this);
        this.head_r22.setRotationPoint(-4.5F, -5.0F, 3.5F);
        setRotationAngle(this.head_r22, 0.0F, -0.7418F, 0.0F);
        this.head_r22.mirror = false;
        this.head_r22.setTextureOffset(93, 58).addBox(0.2F, -3.0F, -0.1F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r22);

        this.head_r23 = new ModelRenderer(this);
        this.head_r23.setRotationPoint(0.0F, -4.5F, -3.5F);
        setRotationAngle(this.head_r23, -0.0492F, 0.4795F, -0.0227F);
        this.head_r23.mirror = false;
        this.head_r23.setTextureOffset(56, 91).addBox(3.3006F, -0.5574F, 0.7878F, (int)1.0, (int)1.0, (int)2.0, 0.4F);
        this.head.addChild(this.head_r23);

        this.head_r24 = new ModelRenderer(this);
        this.head_r24.setRotationPoint(-4.1972F, -6.5038F, -4.87F);
        setRotationAngle(this.head_r24, 0.0F, -0.6545F, 0.0F);
        this.head_r24.mirror = false;
        this.head_r24.setTextureOffset(49, 96).addBox(0.1972F, -1.5F, -0.13F, (int)1.0, (int)3.0, (int)1.0, 0.13F);
        this.head.addChild(this.head_r24);

        this.head_r25 = new ModelRenderer(this);
        this.head_r25.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r25, -0.2615F, -0.0076F, -0.0869F);
        this.head_r25.mirror = false;
        this.head_r25.setTextureOffset(29, 122).addBox(4.0907F, -0.1044F, -2.25F, (int)1.0, (int)1.0, (int)3.0, 0.3F);
        this.head.addChild(this.head_r25);

        this.head_r26 = new ModelRenderer(this);
        this.head_r26.setRotationPoint(-3.0F, 0.0591F, -4.3377F);
        setRotationAngle(this.head_r26, -0.9092F, 0.6922F, 0.2395F);
        this.head_r26.mirror = true;
        this.head_r26.setTextureOffset(67, 9).addBox(-1.5F, -1.0F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.6F);
        this.head.addChild(this.head_r26);

        this.head_r27 = new ModelRenderer(this);
        this.head_r27.setRotationPoint(0.0F, 0.2025F, -5.5425F);
        setRotationAngle(this.head_r27, -1.1111F, -0.4176F, 0.6863F);
        this.head_r27.mirror = true;
        this.head_r27.setTextureOffset(68, 33).addBox(-1.5F, 0.2975F, -1.4575F, (int)3.0, (int)1.0, (int)3.0, 0.05F);
        this.head_r27.mirror = true;
        this.head_r27.setTextureOffset(68, 33).addBox(-1.5F, 0.0975F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.4F);
        this.head_r27.mirror = true;
        this.head_r27.setTextureOffset(85, 9).addBox(-1.5F, -1.25F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.2F);
        this.head_r27.mirror = true;
        this.head_r27.setTextureOffset(47, 70).addBox(-1.5F, -2.25F, -1.5F, (int)3.0, (int)2.0, (int)3.0, 0.05F);
        this.head.addChild(this.head_r27);

        this.head_r28 = new ModelRenderer(this);
        this.head_r28.setRotationPoint(-3.1378F, -0.1838F, -4.9465F);
        setRotationAngle(this.head_r28, 2.3693F, 0.1425F, 0.9337F);
        this.head_r28.mirror = false;
        this.head_r28.setTextureOffset(84, 5).addBox(-1.0F, -1.0F, -1.0F, (int)2.0, (int)2.0, (int)2.0, -0.2F);
        this.head.addChild(this.head_r28);

        this.head_r29 = new ModelRenderer(this);
        this.head_r29.setRotationPoint(0.0F, -6.5F, 0.0F);
        setRotationAngle(this.head_r29, -0.0436F, 0.0F, 0.0F);
        this.head_r29.mirror = false;
        this.head_r29.setTextureOffset(0, 7).addBox(-2.0F, 0.5F, 3.5F, (int)4.0, (int)1.0, (int)1.0, 0.2F);
        this.head.addChild(this.head_r29);

        this.head_r30 = new ModelRenderer(this);
        this.head_r30.setRotationPoint(0.0F, -1.5F, -1.0F);
        setRotationAngle(this.head_r30, -0.3927F, 0.0F, 0.0F);
        this.head_r30.mirror = false;
        this.head_r30.setTextureOffset(40, 21).addBox(-5.0F, -2.5F, -1.0F, (int)10.0, (int)5.0, (int)2.0, -0.5F);
        this.head.addChild(this.head_r30);

        this.head_r31 = new ModelRenderer(this);
        this.head_r31.setRotationPoint(3.6349F, 0.5364F, -3.9451F);
        setRotationAngle(this.head_r31, -1.8662F, -0.8828F, 1.0199F);
        this.head_r31.mirror = false;
        this.head_r31.setTextureOffset(38, 67).addBox(-1.5F, 0.0F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.4F);
        this.head_r31.mirror = false;
        this.head_r31.setTextureOffset(38, 67).addBox(-1.5F, -1.0F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.2F);
        this.head.addChild(this.head_r31);

        this.head_r32 = new ModelRenderer(this);
        this.head_r32.setRotationPoint(3.0F, 0.0591F, -3.3377F);
        setRotationAngle(this.head_r32, -0.9092F, -0.6922F, -0.2395F);
        this.head_r32.mirror = false;
        this.head_r32.setTextureOffset(67, 9).addBox(-1.5F, -1.0F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.6F);
        this.head.addChild(this.head_r32);

        this.head_r33 = new ModelRenderer(this);
        this.head_r33.setRotationPoint(0.0F, 0.0591F, -4.3377F);
        setRotationAngle(this.head_r33, -0.9599F, 0.0F, 0.0F);
        this.head_r33.mirror = false;
        this.head_r33.setTextureOffset(68, 38).addBox(-1.5F, -1.0F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.6F);
        this.head.addChild(this.head_r33);

        this.head_r34 = new ModelRenderer(this);
        this.head_r34.setRotationPoint(0.0F, 0.2025F, -5.5425F);
        setRotationAngle(this.head_r34, -1.1111F, 0.4176F, -0.6863F);
        this.head_r34.mirror = false;
        this.head_r34.setTextureOffset(68, 33).addBox(-1.5F, 0.2975F, -1.4575F, (int)3.0, (int)1.0, (int)3.0, 0.05F);
        this.head_r34.mirror = false;
        this.head_r34.setTextureOffset(68, 33).addBox(-1.5F, 0.0975F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.4F);
        this.head_r34.mirror = false;
        this.head_r34.setTextureOffset(85, 9).addBox(-1.5F, -1.25F, -1.5F, (int)3.0, (int)2.0, (int)3.0, -0.2F);
        this.head_r34.mirror = false;
        this.head_r34.setTextureOffset(47, 70).addBox(-1.5F, -2.25F, -1.5F, (int)3.0, (int)2.0, (int)3.0, 0.05F);
        this.head.addChild(this.head_r34);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(83, 16).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(40, 28).addBox(-3.0F, 3.0F, 1.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(67, 14).addBox(-3.0F, 5.0F, 1.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(70, 0).addBox(-3.0F, 7.0F, 1.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(70, 2).addBox(-3.0F, 9.0F, 1.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(55, 10).addBox(-5.0F, 3.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(54, 52).addBox(-5.0F, 6.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(55, 10).addBox(2.0F, 3.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(72, 24).addBox(-3.0F, 9.0F, -2.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(72, 26).addBox(-3.0F, 7.0F, -3.0F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(72, 48).addBox(-3.0F, 4.0F, -2.8F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(8, 66).addBox(-3.0F, 4.0F, -2.8F, (int)2.0, (int)1.0, (int)1.0, 0.3F);
        this.body.mirror = false;
        this.body.setTextureOffset(73, 16).addBox(-4.0F, 3.0F, -2.8F, (int)3.0, (int)2.0, (int)1.0, 0.2F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 15).addBox(-4.0F, 0.0F, 1.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(0, 15).addBox(1.0F, 0.0F, 1.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(67, 73).addBox(-4.0F, 0.0F, -3.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(67, 73).addBox(1.0F, 0.0F, -3.0F, (int)3.0, (int)4.0, (int)2.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 46).addBox(-4.0F, 2.0F, 1.0F, (int)8.0, (int)9.0, (int)2.0, -0.4F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 57).addBox(1.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = false;
        this.body.setTextureOffset(20, 46).addBox(-4.0F, 2.0F, -3.0F, (int)8.0, (int)9.0, (int)2.0, -0.4F);
        this.body.mirror = true;
        this.body.setTextureOffset(0, 57).addBox(-4.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = true;
        this.body.setTextureOffset(8, 66).addBox(1.0F, 4.0F, -2.8F, (int)2.0, (int)1.0, (int)1.0, 0.3F);
        this.body.mirror = true;
        this.body.setTextureOffset(73, 16).addBox(1.0F, 3.0F, -2.8F, (int)3.0, (int)2.0, (int)1.0, 0.2F);
        this.body.mirror = true;
        this.body.setTextureOffset(54, 52).addBox(2.0F, 6.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(86, 35).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.27F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(-5.0F, 9.0F, -0.05F);
        setRotationAngle(this.body_r1, 0.0F, 0.0F, -0.1309F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(12, 57).addBox(-1.0F, -2.0F, -1.95F, (int)2.0, (int)2.0, (int)4.0, 0.05F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(0, 66).addBox(-1.0F, -2.0F, -1.95F, (int)2.0, (int)4.0, (int)4.0, -0.1F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(48, 58).addBox(-1.0F, -2.0F, -2.45F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(62, 19).addBox(-1.0F, -2.0F, 1.45F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(55, 71).addBox(-1.0F, 0.5F, -1.95F, (int)2.0, (int)1.0, (int)4.0, 0.1F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(-2.5302F, 9.7668F, -2.9139F);
        setRotationAngle(this.body_r2, 0.0873F, 0.0435F, 0.0038F);
        this.body_r2.mirror = true;
        this.body_r2.setTextureOffset(60, 46).addBox(-0.5F, -0.75F, -0.9F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body_r2.mirror = true;
        this.body_r2.setTextureOffset(30, 15).addBox(-0.5F, -0.75F, -0.1F, (int)1.0, (int)3.0, (int)1.0, 0.2F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(-4.5302F, 9.7668F, -2.9139F);
        setRotationAngle(this.body_r3, 0.0928F, 0.3477F, 0.0317F);
        this.body_r3.mirror = true;
        this.body_r3.setTextureOffset(40, 10).addBox(-0.5F, -0.75F, -0.1F, (int)1.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r3.mirror = true;
        this.body_r3.setTextureOffset(0, 66).addBox(-0.5F, -0.75F, -0.9F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(2.0F, 7.25F, -3.7F);
        setRotationAngle(this.body_r4, 0.0876F, 0.0869F, 0.0076F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(58, 76).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(76, 28).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(56, 4).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(12, 57).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(4.0F, 7.25F, -3.7F);
        setRotationAngle(this.body_r5, 0.0226F, -0.5286F, 0.0487F);
        this.body_r5.mirror = true;
        this.body_r5.setTextureOffset(20, 57).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body_r5.mirror = true;
        this.body_r5.setTextureOffset(52, 76).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body_r5.mirror = true;
        this.body_r5.setTextureOffset(62, 61).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r5.mirror = true;
        this.body_r5.setTextureOffset(76, 61).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(5.0F, 9.0F, -0.05F);
        setRotationAngle(this.body_r6, 0.0F, 0.0F, 0.1309F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(12, 57).addBox(-1.0F, -2.0F, -1.95F, (int)2.0, (int)2.0, (int)4.0, 0.05F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(0, 66).addBox(-1.0F, -2.0F, -1.95F, (int)2.0, (int)4.0, (int)4.0, -0.1F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(48, 58).addBox(-1.0F, -2.0F, -2.45F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(62, 19).addBox(-1.0F, -2.0F, 1.45F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(55, 71).addBox(-1.0F, 0.5F, -1.95F, (int)2.0, (int)1.0, (int)4.0, 0.1F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(0.0F, 3.5F, 4.0F);
        setRotationAngle(this.body_r7, 0.0873F, 0.0F, 0.0F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(33, 84).addBox(-2.0F, -3.5F, -1.0F, (int)4.0, (int)3.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(1.0F, 6.5F, 4.0F);
        setRotationAngle(this.body_r8, 0.0873F, 0.0F, 0.0F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(0, 21).addBox(-2.5F, 0.5F, -1.0F, (int)3.0, (int)2.0, (int)2.0, -0.3F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(1.0F, 4.5F, 4.0F);
        setRotationAngle(this.body_r9, 0.0873F, 0.0F, 0.0F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(0, 21).addBox(-2.5F, 0.5F, -1.0F, (int)3.0, (int)2.0, (int)2.0, -0.3F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(30, 21).addBox(-2.5F, -1.5F, -1.0F, (int)3.0, (int)2.0, (int)2.0, -0.3F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(3.15F, 2.4036F, 4.0585F);
        setRotationAngle(this.body_r10, 0.0699F, 0.2129F, 0.0606F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(0, 0).addBox(-1.85F, -0.5F, -1.15F, (int)3.0, (int)5.0, (int)2.0, -0.4F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(40, 46).addBox(-0.15F, -4.5F, -0.35F, (int)1.0, (int)5.0, (int)1.0, -0.3F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(0.0F, 4.5F, 3.0F);
        setRotationAngle(this.body_r11, 0.0873F, 0.0F, 0.0F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(52, 61).addBox(-2.0F, -3.5F, -1.0F, (int)4.0, (int)7.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r11);

        this.body_r12 = new ModelRenderer(this);
        this.body_r12.setRotationPoint(-2.0F, 7.25F, -3.7F);
        setRotationAngle(this.body_r12, 0.0876F, -0.0869F, -0.0076F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(12, 57).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(56, 4).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(76, 28).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(58, 76).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r12);

        this.body_r13 = new ModelRenderer(this);
        this.body_r13.setRotationPoint(-4.0F, 7.25F, -3.7F);
        setRotationAngle(this.body_r13, 0.0226F, 0.5286F, -0.0487F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(76, 61).addBox(-1.0F, -2.25F, -0.9F, (int)2.0, (int)3.0, (int)1.0, 0.05F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(62, 61).addBox(-1.0F, -1.25F, -0.1F, (int)2.0, (int)1.0, (int)1.0, 0.4F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(52, 76).addBox(-1.0F, -2.25F, -0.1F, (int)2.0, (int)4.0, (int)1.0, 0.2F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(20, 57).addBox(-0.5F, -0.25F, -0.7F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r13);

        this.body_r14 = new ModelRenderer(this);
        this.body_r14.setRotationPoint(2.5302F, 9.7668F, -2.9139F);
        setRotationAngle(this.body_r14, 0.0873F, -0.0435F, -0.0038F);
        this.body_r14.mirror = false;
        this.body_r14.setTextureOffset(30, 15).addBox(-0.5F, -0.75F, -0.1F, (int)1.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r14.mirror = false;
        this.body_r14.setTextureOffset(60, 46).addBox(-0.5F, -0.75F, -0.9F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r14);

        this.body_r15 = new ModelRenderer(this);
        this.body_r15.setRotationPoint(4.5302F, 9.7668F, -2.9139F);
        setRotationAngle(this.body_r15, 0.0928F, -0.3477F, -0.0317F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(40, 10).addBox(-0.5F, -0.75F, -0.1F, (int)1.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(0, 66).addBox(-0.5F, -0.75F, -0.9F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r15);

        this.body_r16 = new ModelRenderer(this);
        this.body_r16.setRotationPoint(0.0F, 9.75F, 3.5F);
        setRotationAngle(this.body_r16, -0.1309F, 0.0F, 0.0F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(12, 66).addBox(-2.0F, -0.75F, -1.5F, (int)4.0, (int)3.0, (int)3.0, 0.1F);
        this.body_r16.mirror = true;
        this.body_r16.setTextureOffset(66, 51).addBox(-4.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.3F);
        this.body_r16.mirror = true;
        this.body_r16.setTextureOffset(35, 72).addBox(1.0F, -0.75F, -1.5F, (int)3.0, (int)2.0, (int)3.0, 0.05F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(48, 37).addBox(-2.0F, -1.75F, -1.5F, (int)4.0, (int)2.0, (int)3.0, 0.3F);
        this.body_r16.mirror = true;
        this.body_r16.setTextureOffset(24, 30).addBox(1.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(24, 30).addBox(-2.5F, -1.75F, -1.5F, (int)1.0, (int)3.0, (int)3.0, 0.4F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(66, 51).addBox(1.0F, -1.75F, -1.5F, (int)3.0, (int)4.0, (int)3.0, -0.3F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(35, 72).addBox(-4.0F, -0.75F, -1.5F, (int)3.0, (int)2.0, (int)3.0, 0.05F);
        this.body.addChild(this.body_r16);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = false;
        this.left_arm.setTextureOffset(103, 0).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.2F);
        this.left_arm.mirror = false;
        this.left_arm.setTextureOffset(102, 17).addBox(-0.5F, -1.0F, -2.0F, (int)4.0, (int)4.0, (int)4.0, 0.5F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(1.0F, 1.5F, 0.0F);
        setRotationAngle(this.left_arm_r1, 0.0F, 0.0F, -0.0436F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(100, 53).addBox(2.3F, -2.5F, -1.5F, (int)2.0, (int)5.0, (int)3.0, 0.05F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(113, 53).addBox(2.3F, -2.5F, -1.5F, (int)2.0, (int)3.0, (int)3.0, 0.5F);
        this.left_arm.addChild(this.left_arm_r1);

        this.left_arm_r2 = new ModelRenderer(this);
        this.left_arm_r2.setRotationPoint(1.0F, 0.5F, 0.0F);
        setRotationAngle(this.left_arm_r2, 0.0F, 0.0F, -0.0436F);
        this.left_arm_r2.mirror = true;
        this.left_arm_r2.setTextureOffset(101, 65).addBox(-2.7F, -0.5F, -3.0F, (int)6.0, (int)2.0, (int)6.0, 0.05F);
        this.left_arm.addChild(this.left_arm_r2);

        this.left_arm_r3 = new ModelRenderer(this);
        this.left_arm_r3.setRotationPoint(1.0F, 9.0F, 0.0F);
        setRotationAngle(this.left_arm_r3, 0.0F, 0.0F, 0.0873F);
        this.left_arm_r3.mirror = false;
        this.left_arm_r3.setTextureOffset(102, 17).addBox(-1.7F, -5.0F, -2.0F, (int)4.0, (int)3.0, (int)4.0, 0.3F);
        this.left_arm.addChild(this.left_arm_r3);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = true;
        this.right_arm.setTextureOffset(103, 0).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.2F);
        this.right_arm.mirror = true;
        this.right_arm.setTextureOffset(102, 17).addBox(-3.5F, -1.0F, -2.0F, (int)4.0, (int)4.0, (int)4.0, 0.5F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-1.0F, 9.0F, 0.0F);
        setRotationAngle(this.right_arm_r1, 0.0F, 0.0F, -0.0873F);
        this.right_arm_r1.mirror = true;
        this.right_arm_r1.setTextureOffset(102, 17).addBox(-2.3F, -5.0F, -2.0F, (int)4.0, (int)3.0, (int)4.0, 0.3F);
        this.right_arm.addChild(this.right_arm_r1);

        this.right_arm_r2 = new ModelRenderer(this);
        this.right_arm_r2.setRotationPoint(-1.0F, 0.5F, 0.0F);
        setRotationAngle(this.right_arm_r2, 0.0F, 0.0F, 0.0436F);
        this.right_arm_r2.mirror = false;
        this.right_arm_r2.setTextureOffset(101, 65).addBox(-3.3F, -0.5F, -3.0F, (int)6.0, (int)2.0, (int)6.0, 0.05F);
        this.right_arm.addChild(this.right_arm_r2);

        this.right_arm_r3 = new ModelRenderer(this);
        this.right_arm_r3.setRotationPoint(-1.0F, 1.5F, 0.0F);
        setRotationAngle(this.right_arm_r3, 0.0F, 0.0F, 0.0436F);
        this.right_arm_r3.mirror = false;
        this.right_arm_r3.setTextureOffset(113, 53).addBox(-4.3F, -2.5F, -1.5F, (int)2.0, (int)3.0, (int)3.0, 0.5F);
        this.right_arm_r3.mirror = false;
        this.right_arm_r3.setTextureOffset(100, 53).addBox(-4.3F, -2.5F, -1.5F, (int)2.0, (int)5.0, (int)3.0, 0.05F);
        this.right_arm.addChild(this.right_arm_r3);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(64, 19).addBox(-2.0F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(44, 42).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(64, 61).addBox(-2.0F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.left_leg_r1 = new ModelRenderer(this);
        this.left_leg_r1.setRotationPoint(0.0F, 5.5F, -2.5F);
        setRotationAngle(this.left_leg_r1, 0.0873F, 0.0F, 0.0F);
        this.left_leg_r1.mirror = true;
        this.left_leg_r1.setTextureOffset(52, 10).addBox(-1.47F, -1.5F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.1F);
        this.left_leg.addChild(this.left_leg_r1);

        this.left_leg_r2 = new ModelRenderer(this);
        this.left_leg_r2.setRotationPoint(3.0F, 3.5F, 0.0F);
        setRotationAngle(this.left_leg_r2, 0.0F, 0.0F, 0.0873F);
        this.left_leg_r2.mirror = false;
        this.left_leg_r2.setTextureOffset(26, 66).addBox(-1.0F, -2.5F, -2.0F, (int)2.0, (int)3.0, (int)4.0, 0.2F);
        this.left_leg_r2.mirror = false;
        this.left_leg_r2.setTextureOffset(62, 0).addBox(-1.0F, -2.5F, -2.0F, (int)2.0, (int)5.0, (int)4.0, 0.05F);
        this.left_leg.addChild(this.left_leg_r2);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(44, 42).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(64, 61).addBox(-2.0F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(64, 19).addBox(-2.0F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_leg_r1 = new ModelRenderer(this);
        this.right_leg_r1.setRotationPoint(-3.0F, 3.5F, 0.0F);
        setRotationAngle(this.right_leg_r1, 0.0F, 0.0F, -0.1309F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(0, 30).addBox(-1.0F, -2.5F, -1.0F, (int)2.0, (int)5.0, (int)2.0, -0.1F);
        this.right_leg.addChild(this.right_leg_r1);

        this.right_leg_r2 = new ModelRenderer(this);
        this.right_leg_r2.setRotationPoint(0.0F, 5.5F, -2.5F);
        setRotationAngle(this.right_leg_r2, 0.0873F, 0.0F, 0.0F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(52, 10).addBox(-1.53F, -1.5F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.1F);
        this.right_leg.addChild(this.right_leg_r2);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(60, 24).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.4F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(82, 25).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.6F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(90, 19).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.bipedRightLeg.addChild(this.right_shoe);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(90, 19).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(82, 25).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.6F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(60, 24).addBox(-2.0F, 7.0F, -2.0F, (int)4.0, (int)5.0, (int)4.0, 0.4F);
        this.bipedLeftLeg.addChild(this.left_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
