package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modeljuggernaut extends ModelBiped {

    public ModelRenderer head;
    public ModelRenderer head_r1;
    public ModelRenderer head_r2;
    public ModelRenderer head_r3;
    public ModelRenderer head_r4;
    public ModelRenderer head_r5;
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
    public ModelRenderer left_arm;
    public ModelRenderer left_arm_r1;
    public ModelRenderer left_arm_r2;
    public ModelRenderer left_arm_r3;
    public ModelRenderer right_arm;
    public ModelRenderer right_arm_r1;
    public ModelRenderer right_arm_r2;
    public ModelRenderer right_arm_r3;
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe;
    public ModelRenderer right_leg;
    public ModelRenderer left_leg;

    public Modeljuggernaut() {
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
        this.head.setTextureOffset(24, 19).addBox(-4.0F, -9.0F, -4.0F, (int)8.0, (int)4.0, (int)8.0, 0.556F);
        this.head.mirror = false;
        this.head.setTextureOffset(96, 49).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.05F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(0.0F, -4.0F, 0.75F);
        setRotationAngle(this.head_r1, -0.0436F, 0.0F, 0.0F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(0, 31).addBox(-5.0F, -1.0F, -1.75F, (int)10.0, (int)4.0, (int)6.0, 0.2F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(0.0F, -5.0F, 0.75F);
        setRotationAngle(this.head_r2, -0.0436F, 0.0F, 0.0F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(0, 0).addBox(-5.0F, -1.0F, -5.75F, (int)10.0, (int)2.0, (int)9.0, -0.1F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(0.0F, -3.5F, -3.8F);
        setRotationAngle(this.head_r3, 0.0873F, 0.0F, 0.0F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(0, 111).addBox(-5.0F, -1.5F, -1.8F, (int)10.0, (int)1.0, (int)3.0, -0.1F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(0, 111).addBox(-5.0F, 0.5F, -1.8F, (int)10.0, (int)1.0, (int)3.0, -0.1F);
        this.head_r3.mirror = true;
        this.head_r3.setTextureOffset(9, 111).addBox(4.0F, -1.5F, -1.8F, (int)1.0, (int)3.0, (int)3.0, -0.1F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(9, 111).addBox(-5.0F, -1.5F, -1.8F, (int)1.0, (int)3.0, (int)3.0, -0.1F);
        this.head_r3.mirror = false;
        this.head_r3.setTextureOffset(9, 103).addBox(-5.0F, -1.5F, -1.2F, (int)10.0, (int)5.0, (int)3.0, 0.05F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(-4.0F, -3.0F, 0.0F);
        setRotationAngle(this.head_r4, 0.1369F, 0.0354F, 0.1957F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(93, 67).addBox(-1.4F, -2.0F, -3.0F, (int)1.0, (int)2.0, (int)2.0, 0.6F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(4.0F, -3.0F, 0.0F);
        setRotationAngle(this.head_r5, 0.1369F, -0.0354F, -0.1957F);
        this.head_r5.mirror = true;
        this.head_r5.setTextureOffset(93, 67).addBox(0.4F, -2.0F, -3.0F, (int)1.0, (int)2.0, (int)2.0, 0.6F);
        this.head.addChild(this.head_r5);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(32, 31).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.05F);
        this.body.mirror = true;
        this.body.setTextureOffset(103, 0).addBox(2.0F, 3.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = true;
        this.body.setTextureOffset(103, 0).addBox(2.0F, 7.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(32, 69).addBox(-4.0F, 2.0F, -3.0F, (int)8.0, (int)9.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(79, 3).addBox(-4.0F, 2.0F, -3.5F, (int)8.0, (int)2.0, (int)1.0, -0.2F);
        this.body.mirror = false;
        this.body.setTextureOffset(13, 41).addBox(-4.0F, 4.0F, -3.5F, (int)8.0, (int)2.0, (int)1.0, -0.2F);
        this.body.mirror = false;
        this.body.setTextureOffset(79, 0).addBox(-4.0F, 2.0F, 2.5F, (int)8.0, (int)2.0, (int)1.0, -0.2F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 79).addBox(-4.0F, 4.0F, 2.5F, (int)8.0, (int)2.0, (int)1.0, -0.2F);
        this.body.mirror = false;
        this.body.setTextureOffset(73, 9).addBox(-4.0F, 6.0F, 2.5F, (int)8.0, (int)2.0, (int)1.0, -0.2F);
        this.body.mirror = false;
        this.body.setTextureOffset(54, 0).addBox(-4.0F, 8.0F, 2.5F, (int)8.0, (int)2.0, (int)1.0, -0.2F);
        this.body.mirror = false;
        this.body.setTextureOffset(68, 13).addBox(-4.0F, 2.0F, 2.0F, (int)8.0, (int)9.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(69, 55).addBox(1.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.55F);
        this.body.mirror = false;
        this.body.setTextureOffset(67, 0).addBox(-4.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.55F);
        this.body.mirror = false;
        this.body.setTextureOffset(63, 65).addBox(-4.0F, 0.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.8F);
        this.body.mirror = false;
        this.body.setTextureOffset(66, 35).addBox(1.0F, 0.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.8F);
        this.body.mirror = false;
        this.body.setTextureOffset(0, 27).addBox(-5.0F, -3.0F, 5.0F, (int)10.0, (int)3.0, (int)1.0, 0.3F);
        this.body.mirror = false;
        this.body.setTextureOffset(103, 0).addBox(-5.0F, 7.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.body.mirror = false;
        this.body.setTextureOffset(103, 0).addBox(-5.0F, 3.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.7F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(-5.5F, -0.5F, 0.5F);
        setRotationAngle(this.body_r1, 0.1752F, 0.0859F, 0.0152F);
        this.body_r1.mirror = true;
        this.body_r1.setTextureOffset(0, 41).addBox(-0.5F, -1.5F, -5.5F, (int)1.0, (int)3.0, (int)11.0, 0.05F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(-4.0F, 0.5F, -4.5F);
        setRotationAngle(this.body_r2, 0.0F, 0.2618F, 0.4363F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(0, 48).addBox(-2.0F, -1.5F, -0.5F, (int)4.0, (int)3.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(4.0F, 0.5F, -4.5F);
        setRotationAngle(this.body_r3, 0.0F, -0.2618F, -0.4363F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(26, 31).addBox(-2.0F, -1.5F, -0.5F, (int)4.0, (int)3.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(5.5F, -0.5F, 0.5F);
        setRotationAngle(this.body_r4, 0.1752F, -0.0859F, -0.0152F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(0, 41).addBox(-0.5F, -1.5F, -5.5F, (int)1.0, (int)3.0, (int)11.0, 0.05F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(0.0F, 1.5F, -5.5F);
        setRotationAngle(this.body_r5, 0.2618F, 0.0F, 0.0F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(30, 79).addBox(-3.0F, -1.5F, -0.5F, (int)6.0, (int)3.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(0.0F, 0.0F, -2.5F);
        setRotationAngle(this.body_r6, 0.1309F, 0.0F, 0.0F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(26, 13).addBox(-5.0F, -1.0F, -2.5F, (int)10.0, (int)3.0, (int)3.0, -0.3F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(0.0F, -2.0F, 2.5F);
        setRotationAngle(this.body_r7, 0.1309F, 0.0F, 0.0F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(29, 0).addBox(-5.0F, -2.0F, -2.0F, (int)10.0, (int)5.0, (int)5.0, 0.05F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(0.0F, 10.5F, 2.5F);
        setRotationAngle(this.body_r8, 0.3054F, 0.0F, 0.0F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(84, 26).addBox(-2.0F, 0.5F, -0.5F, (int)4.0, (int)5.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(0.0F, 4.0F, 4.1F);
        setRotationAngle(this.body_r9, -0.0873F, 0.0F, 0.0F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(52, 82).addBox(1.0F, -1.0F, -1.4F, (int)2.0, (int)3.0, (int)3.0, -0.2F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(10, 82).addBox(-3.0F, -1.0F, -1.4F, (int)2.0, (int)3.0, (int)3.0, -0.2F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(26, 47).addBox(-3.0F, -2.0F, -1.1F, (int)6.0, (int)4.0, (int)2.0, 0.05F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(48, 77).addBox(-3.0F, -2.0F, -1.1F, (int)6.0, (int)3.0, (int)2.0, 0.2F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(0.0F, 9.0F, 4.1F);
        setRotationAngle(this.body_r10, -0.0873F, 0.0F, 0.0F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(27, 83).addBox(-3.0F, -1.0F, -1.4F, (int)2.0, (int)3.0, (int)3.0, -0.2F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(84, 20).addBox(1.0F, -1.0F, -1.4F, (int)2.0, (int)3.0, (int)3.0, -0.2F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(78, 34).addBox(-3.0F, -2.0F, -1.1F, (int)6.0, (int)3.0, (int)2.0, 0.2F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(68, 23).addBox(-3.0F, -2.0F, -1.1F, (int)6.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(0.0F, 6.75F, -3.5F);
        setRotationAngle(this.body_r11, -0.0873F, 0.0F, 0.0F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(79, 44).addBox(-2.0F, -1.75F, -1.0F, (int)4.0, (int)3.0, (int)2.0, 0.05F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(18, 79).addBox(-2.0F, -1.75F, -1.0F, (int)4.0, (int)4.0, (int)2.0, -0.2F);
        this.body.addChild(this.body_r11);

        this.body_r12 = new ModelRenderer(this);
        this.body_r12.setRotationPoint(3.5F, 7.25F, -3.5F);
        setRotationAngle(this.body_r12, 0.0F, -0.3491F, 0.0F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(0, 82).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)5.0, (int)2.0, -0.2F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(86, 12).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r12);

        this.body_r13 = new ModelRenderer(this);
        this.body_r13.setRotationPoint(4.5F, 11.25F, -1.5F);
        setRotationAngle(this.body_r13, 0.1977F, -1.1278F, -0.1564F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(0, 41).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)5.0, (int)2.0, -0.2F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(81, 55).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r13);

        this.body_r14 = new ModelRenderer(this);
        this.body_r14.setRotationPoint(4.5F, 12.25F, -1.5F);
        setRotationAngle(this.body_r14, 0.1977F, -1.1278F, -0.1564F);
        this.body_r14.mirror = false;
        this.body_r14.setTextureOffset(94, 80).addBox(-1.5F, -1.25F, -1.0F, (int)3.0, (int)1.0, (int)2.0, 0.15F);
        this.body.addChild(this.body_r14);

        this.body_r15 = new ModelRenderer(this);
        this.body_r15.setRotationPoint(2.5F, 12.25F, -3.5F);
        setRotationAngle(this.body_r15, 0.0873F, -0.2618F, 0.0F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(94, 80).addBox(-1.5F, -1.25F, -1.0F, (int)3.0, (int)1.0, (int)2.0, 0.15F);
        this.body.addChild(this.body_r15);

        this.body_r16 = new ModelRenderer(this);
        this.body_r16.setRotationPoint(-2.5F, 12.25F, -3.5F);
        setRotationAngle(this.body_r16, 0.0873F, 0.2618F, 0.0F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(94, 80).addBox(-1.5F, -1.25F, -1.0F, (int)3.0, (int)1.0, (int)2.0, 0.15F);
        this.body.addChild(this.body_r16);

        this.body_r17 = new ModelRenderer(this);
        this.body_r17.setRotationPoint(-4.5F, 12.25F, -1.5F);
        setRotationAngle(this.body_r17, 0.1977F, 1.1278F, 0.1564F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(94, 80).addBox(-1.5F, -1.25F, -1.0F, (int)3.0, (int)1.0, (int)2.0, 0.15F);
        this.body.addChild(this.body_r17);

        this.body_r18 = new ModelRenderer(this);
        this.body_r18.setRotationPoint(2.5F, 11.25F, -3.5F);
        setRotationAngle(this.body_r18, 0.0873F, -0.2618F, 0.0F);
        this.body_r18.mirror = false;
        this.body_r18.setTextureOffset(64, 79).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)5.0, (int)2.0, -0.2F);
        this.body_r18.mirror = false;
        this.body_r18.setTextureOffset(85, 49).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r18);

        this.body_r19 = new ModelRenderer(this);
        this.body_r19.setRotationPoint(-4.5F, 11.25F, -1.5F);
        setRotationAngle(this.body_r19, 0.1977F, 1.1278F, 0.1564F);
        this.body_r19.mirror = false;
        this.body_r19.setTextureOffset(13, 44).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)5.0, (int)2.0, -0.2F);
        this.body_r19.mirror = false;
        this.body_r19.setTextureOffset(81, 69).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r19);

        this.body_r20 = new ModelRenderer(this);
        this.body_r20.setRotationPoint(-2.5F, 11.25F, -3.5F);
        setRotationAngle(this.body_r20, 0.0873F, 0.2618F, 0.0F);
        this.body_r20.mirror = false;
        this.body_r20.setTextureOffset(74, 80).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)5.0, (int)2.0, -0.2F);
        this.body_r20.mirror = false;
        this.body_r20.setTextureOffset(84, 80).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)4.0, (int)2.0, 0.05F);
        this.body.addChild(this.body_r20);

        this.body_r21 = new ModelRenderer(this);
        this.body_r21.setRotationPoint(-3.5F, 7.25F, -3.5F);
        setRotationAngle(this.body_r21, 0.0F, 0.3491F, 0.0F);
        this.body_r21.mirror = false;
        this.body_r21.setTextureOffset(60, 86).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)4.0, (int)2.0, 0.05F);
        this.body_r21.mirror = false;
        this.body_r21.setTextureOffset(42, 82).addBox(-1.5F, -2.25F, -1.0F, (int)3.0, (int)5.0, (int)2.0, -0.2F);
        this.body.addChild(this.body_r21);

        this.body_r22 = new ModelRenderer(this);
        this.body_r22.setRotationPoint(0.0F, 10.5F, -2.5F);
        setRotationAngle(this.body_r22, -0.2618F, 0.0F, 0.0F);
        this.body_r22.mirror = false;
        this.body_r22.setTextureOffset(83, 86).addBox(-2.0F, 0.5F, -0.5F, (int)4.0, (int)5.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r22);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(75, 64).addBox(-1.0F, -1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(62, 74).addBox(-1.0F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.left_arm.mirror = true;
        this.left_arm.setTextureOffset(12, 74).addBox(-1.0F, 5.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.left_arm_r1 = new ModelRenderer(this);
        this.left_arm_r1.setRotationPoint(3.0F, 1.0F, 0.0F);
        setRotationAngle(this.left_arm_r1, 0.0F, 0.0F, -0.1745F);
        this.left_arm_r1.mirror = true;
        this.left_arm_r1.setTextureOffset(51, 41).addBox(-2.0F, -2.0F, -3.0F, (int)4.0, (int)4.0, (int)6.0, -0.4F);
        this.left_arm.addChild(this.left_arm_r1);

        this.left_arm_r2 = new ModelRenderer(this);
        this.left_arm_r2.setRotationPoint(3.0F, -1.0F, 0.0F);
        setRotationAngle(this.left_arm_r2, 0.0F, 0.0F, -0.1745F);
        this.left_arm_r2.mirror = true;
        this.left_arm_r2.setTextureOffset(53, 3).addBox(-2.0F, -2.0F, -3.0F, (int)4.0, (int)4.0, (int)6.0, -0.3F);
        this.left_arm.addChild(this.left_arm_r2);

        this.left_arm_r3 = new ModelRenderer(this);
        this.left_arm_r3.setRotationPoint(2.0F, 6.0F, 0.0F);
        setRotationAngle(this.left_arm_r3, 0.0F, 0.0F, 0.0436F);
        this.left_arm_r3.mirror = true;
        this.left_arm_r3.setTextureOffset(37, 47).addBox(-2.0F, -3.0F, -3.0F, (int)4.0, (int)6.0, (int)6.0, -0.4F);
        this.left_arm.addChild(this.left_arm_r3);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(75, 64).addBox(-3.0F, -1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(62, 74).addBox(-3.0F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(12, 74).addBox(-3.0F, 5.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedRightArm.addChild(this.right_arm);

        this.right_arm_r1 = new ModelRenderer(this);
        this.right_arm_r1.setRotationPoint(-3.0F, 1.0F, 0.0F);
        setRotationAngle(this.right_arm_r1, 0.0F, 0.0F, 0.1745F);
        this.right_arm_r1.mirror = false;
        this.right_arm_r1.setTextureOffset(51, 41).addBox(-2.0F, -2.0F, -3.0F, (int)4.0, (int)4.0, (int)6.0, -0.4F);
        this.right_arm.addChild(this.right_arm_r1);

        this.right_arm_r2 = new ModelRenderer(this);
        this.right_arm_r2.setRotationPoint(-3.0F, -1.0F, 0.0F);
        setRotationAngle(this.right_arm_r2, 0.0F, 0.0F, 0.1745F);
        this.right_arm_r2.mirror = false;
        this.right_arm_r2.setTextureOffset(53, 3).addBox(-2.0F, -2.0F, -3.0F, (int)4.0, (int)4.0, (int)6.0, -0.3F);
        this.right_arm.addChild(this.right_arm_r2);

        this.right_arm_r3 = new ModelRenderer(this);
        this.right_arm_r3.setRotationPoint(-2.0F, 6.0F, 0.0F);
        setRotationAngle(this.right_arm_r3, 0.0F, 0.0F, -0.0436F);
        this.right_arm_r3.mirror = false;
        this.right_arm_r3.setTextureOffset(37, 47).addBox(-2.0F, -3.0F, -3.0F, (int)4.0, (int)6.0, (int)6.0, -0.4F);
        this.right_arm.addChild(this.right_arm_r3);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(53, 55).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.65F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(55, 107).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(44, 117).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.85F);
        this.bipedLeftLeg.addChild(this.left_shoe);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(53, 55).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.65F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(44, 117).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.85F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(55, 107).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.bipedRightLeg.addChild(this.right_shoe);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(84, 105).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)9.0, (int)4.0, 0.1F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(108, 89).addBox(-2.2F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(108, 94).addBox(-2.2F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(116, 109).addBox(-2.0F, 4.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(110, 121).addBox(-2.0F, 3.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(110, 121).addBox(-2.0F, 5.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(116, 112).addBox(-2.0F, 5.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(110, 121).addBox(-2.0F, 3.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(110, 121).addBox(-2.0F, 5.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(116, 109).addBox(-2.0F, 4.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(116, 112).addBox(-2.0F, 5.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(92, 118).addBox(-3.0F, 1.0F, -1.0F, (int)2.0, (int)5.0, (int)2.0, 0.1F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(84, 105).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)9.0, (int)4.0, 0.1F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(108, 89).addBox(-1.8F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(108, 94).addBox(-1.8F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(116, 109).addBox(-2.0F, 4.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(110, 121).addBox(-2.0F, 3.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(116, 112).addBox(-2.0F, 5.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(116, 112).addBox(-2.0F, 4.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(110, 121).addBox(-2.0F, 3.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(116, 112).addBox(-2.0F, 5.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(116, 109).addBox(-2.0F, 4.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(116, 112).addBox(-2.0F, 4.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = true;
        this.left_leg.setTextureOffset(92, 118).addBox(1.0F, 1.0F, -1.0F, (int)2.0, (int)5.0, (int)2.0, 0.1F);
        this.bipedLeftLeg.addChild(this.left_leg);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
