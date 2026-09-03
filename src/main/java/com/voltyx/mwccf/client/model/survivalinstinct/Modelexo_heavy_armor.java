package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelexo_heavy_armor extends ModelBiped {

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
    public ModelRenderer body_r25;
    public ModelRenderer body_r26;
    public ModelRenderer body_r27;
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
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe;

    public Modelexo_heavy_armor() {
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
        this.head.setTextureOffset(0, 107).addBox(-4.0F, -8.0F, -1.0F, (int)8.0, (int)7.0, (int)5.0, 0.58F);
        this.head.mirror = false;
        this.head.setTextureOffset(4, 4).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)5.0, (int)4.0, 0.6F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(5.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r1, 0.0F, 0.0F, -0.5672F);
        this.head_r1.mirror = true;
        this.head_r1.setTextureOffset(92, 31).addBox(-0.9163F, -2.2608F, -2.0F, (int)3.0, (int)3.0, (int)4.0, -0.2F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(5.4163F, -3.7392F, 0.0F);
        setRotationAngle(this.head_r2, 0.0F, 0.0F, -0.0873F);
        this.head_r2.mirror = true;
        this.head_r2.setTextureOffset(94, 41).addBox(-0.4163F, -2.2608F, -2.0F, (int)1.0, (int)4.0, (int)4.0, 0.2F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(4.0F, -3.0F, -2.5F);
        setRotationAngle(this.head_r3, 0.2564F, 0.1714F, 0.2207F);
        this.head_r3.mirror = true;
        this.head_r3.setTextureOffset(71, 95).addBox(0.0F, -2.0F, -2.5F, (int)1.0, (int)3.0, (int)4.0, 0.2F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(2.4485F, -0.4461F, -5.7807F);
        setRotationAngle(this.head_r4, 0.6608F, -0.9278F, -0.2529F);
        this.head_r4.mirror = true;
        this.head_r4.setTextureOffset(93, 81).addBox(-2.5F, -1.0F, -1.0F, (int)5.0, (int)2.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(-2.4485F, -0.4461F, -5.7807F);
        setRotationAngle(this.head_r5, 0.3037F, 1.0517F, -0.1717F);
        this.head_r5.mirror = false;
        this.head_r5.setTextureOffset(0, 32).addBox(-0.5F, -2.0F, 0.0F, (int)1.0, (int)3.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(-2.4485F, -0.4461F, -5.7807F);
        setRotationAngle(this.head_r6, 0.6608F, 0.9278F, 0.2529F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(93, 81).addBox(-2.5F, -1.0F, -1.0F, (int)5.0, (int)2.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r6);

        this.head_r7 = new ModelRenderer(this);
        this.head_r7.setRotationPoint(2.4485F, -0.4461F, -5.7807F);
        setRotationAngle(this.head_r7, 0.3037F, -1.0517F, 0.1717F);
        this.head_r7.mirror = false;
        this.head_r7.setTextureOffset(36, 42).addBox(-0.5F, -2.0F, 0.0F, (int)1.0, (int)3.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r7);

        this.head_r8 = new ModelRenderer(this);
        this.head_r8.setRotationPoint(-5.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r8, 0.0F, 0.0F, 0.5672F);
        this.head_r8.mirror = false;
        this.head_r8.setTextureOffset(92, 31).addBox(-2.0837F, -2.2608F, -2.0F, (int)3.0, (int)3.0, (int)4.0, -0.2F);
        this.head.addChild(this.head_r8);

        this.head_r9 = new ModelRenderer(this);
        this.head_r9.setRotationPoint(-6.2553F, -4.5839F, -0.5F);
        setRotationAngle(this.head_r9, 0.0F, 0.0F, -0.1745F);
        this.head_r9.mirror = false;
        this.head_r9.setTextureOffset(36, 42).addBox(-0.2447F, -1.9161F, -2.5F, (int)1.0, (int)1.0, (int)5.0, 0.05F);
        this.head.addChild(this.head_r9);

        this.head_r10 = new ModelRenderer(this);
        this.head_r10.setRotationPoint(-5.4163F, -3.7392F, 0.0F);
        setRotationAngle(this.head_r10, 0.0F, 0.0F, 0.0873F);
        this.head_r10.mirror = false;
        this.head_r10.setTextureOffset(94, 41).addBox(-0.5837F, -2.2608F, -2.0F, (int)1.0, (int)4.0, (int)4.0, 0.2F);
        this.head.addChild(this.head_r10);

        this.head_r11 = new ModelRenderer(this);
        this.head_r11.setRotationPoint(-4.0F, -3.0F, -2.5F);
        setRotationAngle(this.head_r11, 0.2564F, -0.1714F, -0.2207F);
        this.head_r11.mirror = false;
        this.head_r11.setTextureOffset(71, 95).addBox(-1.0F, -2.0F, -2.5F, (int)1.0, (int)3.0, (int)4.0, 0.2F);
        this.head.addChild(this.head_r11);

        this.head_r12 = new ModelRenderer(this);
        this.head_r12.setRotationPoint(0.0649F, -0.3193F, -7.3675F);
        setRotationAngle(this.head_r12, 0.2182F, 0.0F, 0.0F);
        this.head_r12.mirror = false;
        this.head_r12.setTextureOffset(50, 2).addBox(-1.0F, -1.0F, -1.0F, (int)2.0, (int)2.0, (int)2.0, 0.2F);
        this.head.addChild(this.head_r12);

        this.head_r13 = new ModelRenderer(this);
        this.head_r13.setRotationPoint(0.0156F, -5.8108F, -4.6494F);
        setRotationAngle(this.head_r13, 0.0862F, 0.7732F, 0.0594F);
        this.head_r13.mirror = false;
        this.head_r13.setTextureOffset(42, 42).addBox(-3.0F, -2.0F, -3.0F, (int)6.0, (int)4.0, (int)6.0, 0.2F);
        this.head.addChild(this.head_r13);

        this.head_r14 = new ModelRenderer(this);
        this.head_r14.setRotationPoint(0.0F, -4.0F, -3.0F);
        setRotationAngle(this.head_r14, 0.1717F, 0.7666F, 0.1194F);
        this.head_r14.mirror = false;
        this.head_r14.setTextureOffset(18, 42).addBox(-2.0F, -4.0F, -4.0F, (int)6.0, (int)7.0, (int)6.0, 0.05F);
        this.head.addChild(this.head_r14);

        this.head_r15 = new ModelRenderer(this);
        this.head_r15.setRotationPoint(-6.0F, -6.0F, 0.5F);
        setRotationAngle(this.head_r15, 0.0161F, -0.5664F, -0.0511F);
        this.head_r15.mirror = false;
        this.head_r15.setTextureOffset(13, 63).addBox(0.3916F, -5.9743F, -0.5F, (int)0.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r15);

        this.head_r16 = new ModelRenderer(this);
        this.head_r16.setRotationPoint(0.0F, -5.5F, 0.0F);
        setRotationAngle(this.head_r16, -0.1309F, 0.0F, 0.0F);
        this.head_r16.mirror = false;
        this.head_r16.setTextureOffset(48, 22).addBox(-5.0F, -3.5F, -1.0F, (int)10.0, (int)7.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r16);

        this.head_r17 = new ModelRenderer(this);
        this.head_r17.setRotationPoint(2.5F, -4.5F, -2.5F);
        setRotationAngle(this.head_r17, 0.0F, 0.0F, 0.1745F);
        this.head_r17.mirror = false;
        this.head_r17.setTextureOffset(25, 74).addBox(-0.5F, -4.5F, -2.5F, (int)1.0, (int)1.0, (int)5.0, 0.05F);
        this.head.addChild(this.head_r17);

        this.head_r18 = new ModelRenderer(this);
        this.head_r18.setRotationPoint(-2.5F, -4.5F, -2.5F);
        setRotationAngle(this.head_r18, 0.0F, 0.0F, -0.1745F);
        this.head_r18.mirror = false;
        this.head_r18.setTextureOffset(59, 74).addBox(-0.5F, -4.5F, -2.5F, (int)1.0, (int)1.0, (int)5.0, 0.05F);
        this.head.addChild(this.head_r18);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 32).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(0.0F, 6.5F, -2.55F);
        setRotationAngle(this.body_r1, 0.0F, 3.1416F, 0.0F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(50, 0).addBox(-3.0F, -2.0F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(59, 10).addBox(-4.0F, -4.5F, -1.45F, (int)8.0, (int)7.0, (int)2.0, -0.15F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(93, 68).addBox(-3.0F, -3.5F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(69, 35).addBox(-3.0F, -0.5F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(0.0F, 3.9253F, -3.2885F);
        setRotationAngle(this.body_r2, 2.9671F, 0.0F, -3.1416F);
        this.body_r2.mirror = true;
        this.body_r2.setTextureOffset(0, 0).addBox(-1.0F, -1.5075F, -1.3285F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r2.mirror = true;
        this.body_r2.setTextureOffset(43, 42).addBox(-0.5F, -0.5075F, -0.1285F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(0.0F, 11.0F, -2.9F);
        setRotationAngle(this.body_r3, -2.8798F, 0.0F, 3.1416F);
        this.body_r3.mirror = true;
        this.body_r3.setTextureOffset(93, 92).addBox(-2.5F, -2.6019F, -0.5128F, (int)5.0, (int)7.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(0.0F, 6.5F, -2.55F);
        setRotationAngle(this.body_r4, 0.0F, -3.1416F, 0.0F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(93, 68).addBox(-3.0F, -3.5F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(69, 35).addBox(-3.0F, -0.5F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r4.mirror = true;
        this.body_r4.setTextureOffset(59, 19).addBox(-3.0F, 1.0F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(50, 0).addBox(-3.0F, -2.0F, -0.45F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(0.0F, 10.0F, -2.6F);
        setRotationAngle(this.body_r5, -2.8798F, 0.0F, -3.1416F);
        this.body_r5.mirror = true;
        this.body_r5.setTextureOffset(24, 22).addBox(-2.0F, 3.5F, -0.4F, (int)4.0, (int)1.0, (int)1.0, 0.1F);
        this.body_r5.mirror = true;
        this.body_r5.setTextureOffset(28, 55).addBox(-2.0F, 1.5F, -0.4F, (int)4.0, (int)1.0, (int)1.0, 0.1F);
        this.body_r5.mirror = true;
        this.body_r5.setTextureOffset(28, 57).addBox(-2.0F, -0.5F, -0.4F, (int)4.0, (int)1.0, (int)1.0, 0.1F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(0.0F, 0.0F, 4.4347F);
        setRotationAngle(this.body_r6, 0.2182F, 0.0F, 0.0F);
        this.body_r6.mirror = true;
        this.body_r6.setTextureOffset(32, 59).addBox(-6.0F, -2.0F, -2.5F, (int)1.0, (int)4.0, (int)2.0, 0.05F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(0, 64).addBox(-6.0F, -2.0F, 0.5F, (int)1.0, (int)4.0, (int)2.0, 0.05F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(0, 64).addBox(2.0F, -3.0F, -3.5F, (int)1.0, (int)6.0, (int)7.0, 0.2F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(25, 9).addBox(-5.0F, -3.0F, -3.5F, (int)10.0, (int)6.0, (int)7.0, 0.05F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(-4.0F, 5.5F, 4.4347F);
        setRotationAngle(this.body_r7, 0.0F, 0.0F, -0.1745F);
        this.body_r7.mirror = true;
        this.body_r7.setTextureOffset(10, 96).addBox(-1.0F, -1.5F, -1.5F, (int)2.0, (int)4.0, (int)3.0, 0.05F);
        this.body_r7.mirror = true;
        this.body_r7.setTextureOffset(12, 79).addBox(-1.0F, -1.5F, -1.5F, (int)2.0, (int)2.0, (int)3.0, 0.3F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(0.0F, 0.0F, 4.4347F);
        setRotationAngle(this.body_r8, 0.2182F, 0.0F, 0.0F);
        this.body_r8.mirror = true;
        this.body_r8.setTextureOffset(0, 64).addBox(-3.0F, -3.0F, -3.5F, (int)1.0, (int)6.0, (int)7.0, 0.2F);
        this.body_r8.mirror = true;
        this.body_r8.setTextureOffset(0, 64).addBox(5.0F, -2.0F, 0.5F, (int)1.0, (int)4.0, (int)2.0, 0.05F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(32, 59).addBox(5.0F, -2.0F, -2.5F, (int)1.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(5.3F, -3.6887F, 2.5926F);
        setRotationAngle(this.body_r9, 0.2317F, -0.3405F, -0.0786F);
        this.body_r9.mirror = true;
        this.body_r9.setTextureOffset(16, 51).addBox(0.0F, -3.0F, -0.5F, (int)0.0, (int)6.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(2.5F, 10.3549F, 3.179F);
        setRotationAngle(this.body_r10, -0.2986F, -0.0651F, -0.2084F);
        this.body_r10.mirror = true;
        this.body_r10.setTextureOffset(12, 48).addBox(-0.5F, -1.0F, -1.0F, (int)1.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(0.0F, 9.1338F, 2.5632F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(81, 27).addBox(-4.0F, -1.8698F, -0.23F, (int)8.0, (int)2.0, (int)2.0, 0.3F);
        this.body.addChild(this.body_r11);

        this.body_r12 = new ModelRenderer(this);
        this.body_r12.setRotationPoint(-2.5F, 10.3549F, 3.179F);
        setRotationAngle(this.body_r12, -0.2986F, 0.0651F, 0.2084F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(12, 48).addBox(-0.5F, -1.0F, -1.0F, (int)1.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r12);

        this.body_r13 = new ModelRenderer(this);
        this.body_r13.setRotationPoint(0.0F, 9.1338F, 3.5632F);
        setRotationAngle(this.body_r13, -0.1309F, 0.0F, 0.0F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(72, 19).addBox(-4.0F, -1.7393F, -1.2215F, (int)8.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r13);

        this.body_r14 = new ModelRenderer(this);
        this.body_r14.setRotationPoint(-3.4537F, 7.0208F, 0.0F);
        setRotationAngle(this.body_r14, 0.0F, 0.0F, -0.3927F);
        this.body_r14.mirror = true;
        this.body_r14.setTextureOffset(86, 19).addBox(-1.5F, -1.0F, -3.0F, (int)3.0, (int)2.0, (int)6.0, 0.15F);
        this.body.addChild(this.body_r14);

        this.body_r15 = new ModelRenderer(this);
        this.body_r15.setRotationPoint(0.0F, 3.9253F, -3.2885F);
        setRotationAngle(this.body_r15, 2.9671F, 0.0F, 3.1416F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(0, 0).addBox(-1.0F, -1.5075F, -1.3285F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(43, 42).addBox(-0.5F, -0.5075F, -0.1285F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(50, 52).addBox(-1.0F, -1.5075F, -1.0285F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r15);

        this.body_r16 = new ModelRenderer(this);
        this.body_r16.setRotationPoint(-3.0F, 3.9253F, -3.2885F);
        setRotationAngle(this.body_r16, 3.054F, -0.1308F, -3.1359F);
        this.body_r16.mirror = true;
        this.body_r16.setTextureOffset(0, 16).addBox(-1.0F, -1.5075F, -1.3285F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r16.mirror = true;
        this.body_r16.setTextureOffset(44, 22).addBox(-0.5F, -0.5075F, -0.1285F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r16.mirror = true;
        this.body_r16.setTextureOffset(66, 74).addBox(-1.0F, -1.5075F, -1.0285F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r16);

        this.body_r17 = new ModelRenderer(this);
        this.body_r17.setRotationPoint(3.0F, 3.9253F, -3.2885F);
        setRotationAngle(this.body_r17, 3.054F, 0.1308F, 3.1359F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(0, 16).addBox(-1.0F, -1.5075F, -1.3285F, (int)2.0, (int)4.0, (int)2.0, 0.2F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(44, 22).addBox(-0.5F, -0.5075F, -0.1285F, (int)1.0, (int)2.0, (int)1.0, 0.3F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(66, 74).addBox(-1.0F, -1.5075F, -1.0285F, (int)2.0, (int)2.0, (int)2.0, 0.4F);
        this.body.addChild(this.body_r17);

        this.body_r18 = new ModelRenderer(this);
        this.body_r18.setRotationPoint(-5.3F, -3.6887F, 2.5926F);
        setRotationAngle(this.body_r18, 0.2317F, 0.3405F, 0.0786F);
        this.body_r18.mirror = false;
        this.body_r18.setTextureOffset(16, 51).addBox(0.0F, -3.0F, -0.5F, (int)0.0, (int)6.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r18);

        this.body_r19 = new ModelRenderer(this);
        this.body_r19.setRotationPoint(0.0F, 5.5015F, 7.4173F);
        this.body_r19.mirror = false;
        this.body_r19.setTextureOffset(24, 0).addBox(-5.0F, -3.5015F, -5.4827F, (int)10.0, (int)2.0, (int)6.0, -0.1F);
        this.body_r19.mirror = false;
        this.body_r19.setTextureOffset(59, 69).addBox(2.0F, -3.5015F, -0.4827F, (int)1.0, (int)6.0, (int)1.0, 0.3F);
        this.body.addChild(this.body_r19);

        this.body_r20 = new ModelRenderer(this);
        this.body_r20.setRotationPoint(4.0F, 5.5F, 4.4347F);
        setRotationAngle(this.body_r20, 0.0F, 0.0F, 0.1745F);
        this.body_r20.mirror = false;
        this.body_r20.setTextureOffset(12, 79).addBox(-1.0F, -1.5F, -1.5F, (int)2.0, (int)2.0, (int)3.0, 0.3F);
        this.body_r20.mirror = false;
        this.body_r20.setTextureOffset(10, 96).addBox(-1.0F, -1.5F, -1.5F, (int)2.0, (int)4.0, (int)3.0, 0.05F);
        this.body.addChild(this.body_r20);

        this.body_r21 = new ModelRenderer(this);
        this.body_r21.setRotationPoint(0.0F, 5.5015F, 6.4173F);
        this.body_r21.mirror = false;
        this.body_r21.setTextureOffset(27, 27).addBox(-4.0F, -7.5015F, -4.4827F, (int)8.0, (int)10.0, (int)5.0, 0.05F);
        this.body.addChild(this.body_r21);

        this.body_r22 = new ModelRenderer(this);
        this.body_r22.setRotationPoint(0.0F, 5.5015F, 7.4173F);
        this.body_r22.mirror = true;
        this.body_r22.setTextureOffset(59, 69).addBox(-3.0F, -3.5015F, -0.4827F, (int)1.0, (int)6.0, (int)1.0, 0.3F);
        this.body.addChild(this.body_r22);

        this.body_r23 = new ModelRenderer(this);
        this.body_r23.setRotationPoint(3.0F, 0.95F, 0.0F);
        setRotationAngle(this.body_r23, 0.0F, 0.0F, 0.0873F);
        this.body_r23.mirror = true;
        this.body_r23.setTextureOffset(31, 87).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)2.0, (int)6.0, -0.15F);
        this.body_r23.mirror = false;
        this.body_r23.setTextureOffset(97, 85).addBox(-1.0F, -0.95F, -3.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r23.mirror = false;
        this.body_r23.setTextureOffset(98, 19).addBox(-1.0F, -0.95F, 1.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r23.mirror = false;
        this.body_r23.setTextureOffset(91, 7).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)1.0, (int)6.0, 0.05F);
        this.body.addChild(this.body_r23);

        this.body_r24 = new ModelRenderer(this);
        this.body_r24.setRotationPoint(0.0F, 5.0208F, 0.0F);
        setRotationAngle(this.body_r24, 0.0F, 0.0F, 0.0436F);
        this.body_r24.mirror = true;
        this.body_r24.setTextureOffset(65, 86).addBox(-4.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r24);

        this.body_r25 = new ModelRenderer(this);
        this.body_r25.setRotationPoint(-3.0F, 0.95F, 0.0F);
        setRotationAngle(this.body_r25, 0.0F, 0.0F, -0.0873F);
        this.body_r25.mirror = true;
        this.body_r25.setTextureOffset(98, 19).addBox(-1.0F, -0.95F, 1.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r25.mirror = true;
        this.body_r25.setTextureOffset(97, 85).addBox(-1.0F, -0.95F, -3.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r25.mirror = true;
        this.body_r25.setTextureOffset(91, 7).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)1.0, (int)6.0, 0.05F);
        this.body_r25.mirror = false;
        this.body_r25.setTextureOffset(31, 87).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)2.0, (int)6.0, -0.15F);
        this.body.addChild(this.body_r25);

        this.body_r26 = new ModelRenderer(this);
        this.body_r26.setRotationPoint(3.4537F, 7.0208F, 0.0F);
        setRotationAngle(this.body_r26, 0.0F, 0.0F, 0.3927F);
        this.body_r26.mirror = false;
        this.body_r26.setTextureOffset(86, 19).addBox(-1.5F, -1.0F, -3.0F, (int)3.0, (int)2.0, (int)6.0, 0.15F);
        this.body.addChild(this.body_r26);

        this.body_r27 = new ModelRenderer(this);
        this.body_r27.setRotationPoint(0.0F, 5.0208F, 0.0F);
        setRotationAngle(this.body_r27, 0.0F, 0.0F, -0.0436F);
        this.body_r27.mirror = false;
        this.body_r27.setTextureOffset(65, 86).addBox(1.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r27);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = false;
        this.left_arm.setTextureOffset(38, 52).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(1.9265F, 6.3752F, 0.0F);
        setRotationAngle(this.left_arm_r1, 0.0F, 0.0F, 0.0436F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(46, 71).addBox(-0.9265F, -1.5F, -3.0F, (int)3.0, (int)2.0, (int)6.0, 0.05F);
        this.left_arm.addChild(this.left_arm_r1);

        this.left_arm_r2 = new ModelRenderer(this);
        this.left_arm_r2.setRotationPoint(1.0F, -2.0F, 0.0F);
        setRotationAngle(this.left_arm_r2, 0.0F, 0.0F, -0.1745F);
        this.left_arm_r2.mirror = true;
        this.left_arm_r2.setTextureOffset(52, 2).addBox(-2.0F, 4.0F, -3.0F, (int)5.0, (int)2.0, (int)6.0, 0.05F);
        this.left_arm_r2.mirror = true;
        this.left_arm_r2.setTextureOffset(70, 48).addBox(0.0F, -1.0F, -3.0F, (int)3.0, (int)4.0, (int)6.0, 0.05F);
        this.left_arm_r2.mirror = false;
        this.left_arm_r2.setTextureOffset(0, 87).addBox(-1.0F, 0.0F, -2.0F, (int)3.0, (int)5.0, (int)4.0, 0.6F);
        this.left_arm.addChild(this.left_arm_r2);

        this.left_arm_r3 = new ModelRenderer(this);
        this.left_arm_r3.setRotationPoint(1.9265F, 5.3752F, 0.0F);
        setRotationAngle(this.left_arm_r3, 0.0F, 0.0F, 0.0436F);
        this.left_arm_r3.mirror = false;
        this.left_arm_r3.setTextureOffset(24, 92).addBox(0.0735F, -0.5F, -2.0F, (int)1.0, (int)4.0, (int)4.0, 0.6F);
        this.left_arm.addChild(this.left_arm_r3);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(16, 55).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-1.9265F, 6.3752F, 0.0F);
        setRotationAngle(this.right_arm_r1, 0.0F, 0.0F, -0.0436F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(46, 71).addBox(-2.0735F, -1.5F, -3.0F, (int)3.0, (int)2.0, (int)6.0, 0.05F);
        this.right_arm.addChild(this.right_arm_r1);

        this.right_arm_r2 = new ModelRenderer(this);
        this.right_arm_r2.setRotationPoint(-1.9265F, 5.3752F, 0.0F);
        setRotationAngle(this.right_arm_r2, 0.0F, 0.0F, -0.0436F);
        this.right_arm_r2.mirror = false;
        this.right_arm_r2.setTextureOffset(34, 95).addBox(-1.0735F, -0.5F, -2.0F, (int)1.0, (int)4.0, (int)4.0, 0.6F);
        this.right_arm.addChild(this.right_arm_r2);

        this.right_arm_r3 = new ModelRenderer(this);
        this.right_arm_r3.setRotationPoint(-1.0F, -2.0F, 0.0F);
        setRotationAngle(this.right_arm_r3, 0.0F, 0.0F, 0.1745F);
        this.right_arm_r3.mirror = false;
        this.right_arm_r3.setTextureOffset(52, 2).addBox(-3.0F, 4.0F, -3.0F, (int)5.0, (int)2.0, (int)6.0, 0.05F);
        this.right_arm_r3.mirror = false;
        this.right_arm_r3.setTextureOffset(70, 48).addBox(-3.0F, -1.0F, -3.0F, (int)3.0, (int)4.0, (int)6.0, 0.05F);
        this.right_arm_r3.mirror = false;
        this.right_arm_r3.setTextureOffset(47, 90).addBox(-2.0F, 0.0F, -2.0F, (int)3.0, (int)5.0, (int)4.0, 0.6F);
        this.right_arm.addChild(this.right_arm_r3);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(54, 52).addBox(-1.9F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(85, 76).addBox(-1.9F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.25F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.left_leg_r1 = new ModelRenderer(this);
        this.left_leg_r1.setRotationPoint(0.1F, 4.5F, -2.1F);
        setRotationAngle(this.left_leg_r1, 0.0873F, -0.0873F, 0.0F);
        this.left_leg_r1.mirror = true;
        this.left_leg_r1.setTextureOffset(28, 88).addBox(-1.5F, -1.0F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.4F);
        this.left_leg_r1.mirror = true;
        this.left_leg_r1.setTextureOffset(97, 70).addBox(-1.5F, -1.5F, -0.5F, (int)3.0, (int)4.0, (int)1.0, 0.2F);
        this.left_leg.addChild(this.left_leg_r1);

        this.left_leg_r2 = new ModelRenderer(this);
        this.left_leg_r2.setRotationPoint(1.1F, -0.5F, 0.0F);
        setRotationAngle(this.left_leg_r2, 0.0F, 0.0F, -0.2182F);
        this.left_leg_r2.mirror = true;
        this.left_leg_r2.setTextureOffset(72, 58).addBox(-0.4F, 2.5F, -3.0F, (int)3.0, (int)2.0, (int)6.0, 0.05F);
        this.left_leg_r2.mirror = true;
        this.left_leg_r2.setTextureOffset(10, 71).addBox(-1.4F, -0.5F, -3.0F, (int)4.0, (int)2.0, (int)6.0, 0.05F);
        this.left_leg_r2.mirror = true;
        this.left_leg_r2.setTextureOffset(14, 84).addBox(-1.4F, -1.5F, -2.0F, (int)3.0, (int)8.0, (int)4.0, 0.3F);
        this.left_leg.addChild(this.left_leg_r2);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(54, 52).addBox(-2.1F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(85, 76).addBox(-2.1F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.25F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_leg_r1 = new ModelRenderer(this);
        this.right_leg_r1.setRotationPoint(-0.1F, 4.5F, -2.1F);
        setRotationAngle(this.right_leg_r1, 0.0873F, 0.0873F, 0.0F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(28, 88).addBox(-1.5F, -1.0F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.4F);
        this.right_leg_r1.mirror = false;
        this.right_leg_r1.setTextureOffset(97, 70).addBox(-1.5F, -1.5F, -0.5F, (int)3.0, (int)4.0, (int)1.0, 0.2F);
        this.right_leg.addChild(this.right_leg_r1);

        this.right_leg_r2 = new ModelRenderer(this);
        this.right_leg_r2.setRotationPoint(-1.1F, -0.5F, 0.0F);
        setRotationAngle(this.right_leg_r2, 0.0F, 0.0F, 0.2182F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(72, 58).addBox(-2.6F, 2.5F, -3.0F, (int)3.0, (int)2.0, (int)6.0, 0.05F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(10, 71).addBox(-2.6F, -0.5F, -3.0F, (int)4.0, (int)2.0, (int)6.0, 0.05F);
        this.right_leg_r2.mirror = false;
        this.right_leg_r2.setTextureOffset(14, 84).addBox(-1.6F, -1.5F, -2.0F, (int)3.0, (int)8.0, (int)4.0, 0.3F);
        this.right_leg.addChild(this.right_leg_r2);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(81, 66).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.49F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(64, 0).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.bipedLeftLeg.addChild(this.left_shoe);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(81, 66).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.49F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(64, 0).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.bipedRightLeg.addChild(this.right_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
