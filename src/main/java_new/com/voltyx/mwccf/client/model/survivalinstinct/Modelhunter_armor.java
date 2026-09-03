package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelhunter_armor extends ModelBiped {

    public ModelRenderer head;
    public ModelRenderer visor_r1;
    public ModelRenderer visor_r2;
    public ModelRenderer visor_r3;
    public ModelRenderer visor_r4;
    public ModelRenderer visor_r5;
    public ModelRenderer visor_r6;
    public ModelRenderer visor_r7;
    public ModelRenderer visor_r8;
    public ModelRenderer visor_r9;
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

    public Modelhunter_armor() {
        this.textureWidth = 156;
        this.textureHeight = 156;

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
        this.head.setTextureOffset(0, 102).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.1F);
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

        this.visor_r1 = new ModelRenderer(this);
        this.visor_r1.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r1, 0.2182F, 0.0F, 0.0F);
        this.visor_r1.mirror = false;
        this.visor_r1.setTextureOffset(85, 27).addBox(-2.0F, -0.5344F, -4.0628F, (int)4.0, (int)2.0, (int)3.0, -0.5F);
        this.head.addChild(this.visor_r1);

        this.visor_r2 = new ModelRenderer(this);
        this.visor_r2.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r2, 0.0F, 0.3054F, 0.0F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(94, 45).addBox(-2.9109F, 2.0163F, -5.6283F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(118, 14).addBox(-2.9109F, 2.0163F, -2.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(107, 36).addBox(-2.9109F, 2.0163F, -4.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(114, 11).addBox(-2.7809F, 1.9292F, -6.616F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(101, 16).addBox(-2.7809F, 1.9292F, -7.0335F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.head.addChild(this.visor_r2);

        this.visor_r3 = new ModelRenderer(this);
        this.visor_r3.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r3, 0.0F, 0.0873F, 0.0F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(101, 12).addBox(-1.7019F, 1.9292F, -6.3662F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(88, 34).addBox(-1.7019F, 1.9292F, -5.9487F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(121, 27).addBox(-1.7019F, 2.0163F, -3.9525F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(111, 29).addBox(-1.7019F, 2.0163F, -1.9525F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(95, 56).addBox(-1.7019F, 2.0163F, -4.9525F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.head.addChild(this.visor_r3);

        this.visor_r4 = new ModelRenderer(this);
        this.visor_r4.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r4, 0.0F, -0.0873F, 0.0F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(101, 4).addBox(-0.2981F, 1.9292F, -6.3662F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(114, 32).addBox(-0.2981F, 2.0163F, -1.9525F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(96, 66).addBox(-0.2981F, 2.0163F, -4.9525F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(88, 34).addBox(-0.2981F, 1.9292F, -5.9487F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.head.addChild(this.visor_r4);

        this.visor_r5 = new ModelRenderer(this);
        this.visor_r5.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        this.visor_r5.mirror = false;
        this.visor_r5.setTextureOffset(107, 22).addBox(0.0F, 1.9292F, -5.8744F, (int)2.0, (int)2.0, (int)1.0, -0.2F);
        this.visor_r5.mirror = false;
        this.visor_r5.setTextureOffset(116, 21).addBox(0.0F, 2.0163F, -3.8782F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.head.addChild(this.visor_r5);

        this.visor_r6 = new ModelRenderer(this);
        this.visor_r6.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r6, 0.0F, -0.3054F, 0.0F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(101, 0).addBox(0.7809F, 1.9292F, -7.0335F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(106, 29).addBox(0.7809F, 1.9292F, -6.616F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(100, 29).addBox(0.9109F, 2.0163F, -4.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(122, 5).addBox(0.9109F, 2.0163F, -2.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(110, 48).addBox(0.9109F, 2.0163F, -5.6283F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.head.addChild(this.visor_r6);

        this.visor_r7 = new ModelRenderer(this);
        this.visor_r7.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r7, -0.0873F, 0.0F, 0.0F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(108, 0).addBox(-4.0F, 1.3031F, -3.5191F, (int)8.0, (int)2.0, (int)3.0, -0.5F);
        this.head.addChild(this.visor_r7);

        this.visor_r8 = new ModelRenderer(this);
        this.visor_r8.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r8, 0.829F, 0.0F, 0.0F);
        this.visor_r8.mirror = false;
        this.visor_r8.setTextureOffset(127, 30).addBox(-1.0F, -1.4305F, -3.0018F, (int)2.0, (int)2.0, (int)4.0, -0.6F);
        this.head.addChild(this.visor_r8);

        this.visor_r9 = new ModelRenderer(this);
        this.visor_r9.setRotationPoint(0.0F, -7.2628F, -4.1938F);
        setRotationAngle(this.visor_r9, 0.2618F, 0.0F, 0.0F);
        this.visor_r9.mirror = false;
        this.visor_r9.setTextureOffset(114, 38).addBox(-2.0F, -0.713F, -2.0102F, (int)4.0, (int)3.0, (int)3.0, -0.6F);
        this.visor_r9.mirror = false;
        this.visor_r9.setTextureOffset(107, 50).addBox(-2.0F, -0.713F, -2.0102F, (int)4.0, (int)3.0, (int)3.0, -0.6F);
        this.head.addChild(this.visor_r9);

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
        this.head_r13.setRotationPoint(5.7595F, -4.1147F, 0.0F);
        setRotationAngle(this.head_r13, 0.0F, -0.6109F, -0.0873F);
        this.head_r13.mirror = false;
        this.head_r13.setTextureOffset(0, 25).addBox(-1.0F, -2.0F, -0.5F, (int)2.0, (int)4.0, (int)0.0, 0.05F);
        this.head.addChild(this.head_r13);

        this.head_r14 = new ModelRenderer(this);
        this.head_r14.setRotationPoint(4.5907F, -4.5F, -1.0F);
        setRotationAngle(this.head_r14, 0.0F, 0.0F, 0.0436F);
        this.head_r14.mirror = false;
        this.head_r14.setTextureOffset(82, 70).addBox(-0.5F, -0.5F, -2.0F, (int)1.0, (int)1.0, (int)5.0, 0.3F);
        this.head.addChild(this.head_r14);

        this.head_r15 = new ModelRenderer(this);
        this.head_r15.setRotationPoint(4.4163F, -2.7392F, 0.0F);
        setRotationAngle(this.head_r15, 0.0F, 0.0F, -0.0873F);
        this.head_r15.mirror = true;
        this.head_r15.setTextureOffset(16, 41).addBox(-0.4163F, -1.2608F, -2.0F, (int)1.0, (int)3.0, (int)3.0, 0.2F);
        this.head.addChild(this.head_r15);

        this.head_r16 = new ModelRenderer(this);
        this.head_r16.setRotationPoint(-5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r16, 0.0F, 0.0F, 0.0873F);
        this.head_r16.mirror = false;
        this.head_r16.setTextureOffset(28, 52).addBox(-0.5958F, -1.4939F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r16);

        this.head_r17 = new ModelRenderer(this);
        this.head_r17.setRotationPoint(-5.7595F, -4.1147F, 0.0F);
        setRotationAngle(this.head_r17, 0.0F, 0.6109F, 0.0873F);
        this.head_r17.mirror = false;
        this.head_r17.setTextureOffset(24, 16).addBox(-1.0F, -2.0F, -0.5F, (int)2.0, (int)4.0, (int)0.0, 0.05F);
        this.head.addChild(this.head_r17);

        this.head_r18 = new ModelRenderer(this);
        this.head_r18.setRotationPoint(-4.4163F, -4.7392F, -5.0F);
        setRotationAngle(this.head_r18, 0.0F, 0.0F, 0.0873F);
        this.head_r18.mirror = false;
        this.head_r18.setTextureOffset(58, 35).addBox(-1.2837F, -1.2608F, 4.0F, (int)1.0, (int)1.0, (int)1.0, 0.2F);
        this.head_r18.mirror = false;
        this.head_r18.setTextureOffset(47, 55).addBox(-1.2837F, -2.2608F, 2.0F, (int)1.0, (int)3.0, (int)7.0, -0.1F);
        this.head.addChild(this.head_r18);

        this.head_r19 = new ModelRenderer(this);
        this.head_r19.setRotationPoint(-5.2091F, -5.5708F, -1.5F);
        setRotationAngle(this.head_r19, 0.0F, 0.0F, -0.1309F);
        this.head_r19.mirror = false;
        this.head_r19.setTextureOffset(9, 70).addBox(-0.6647F, 0.4848F, -1.5F, (int)1.0, (int)1.0, (int)7.0, 0.05F);
        this.head.addChild(this.head_r19);

        this.head_r20 = new ModelRenderer(this);
        this.head_r20.setRotationPoint(-4.1972F, -6.5038F, -4.87F);
        setRotationAngle(this.head_r20, 0.0F, -0.6545F, 0.0F);
        this.head_r20.mirror = false;
        this.head_r20.setTextureOffset(24, 36).addBox(0.1972F, -1.5F, -0.13F, (int)1.0, (int)3.0, (int)1.0, 0.13F);
        this.head.addChild(this.head_r20);

        this.head_r21 = new ModelRenderer(this);
        this.head_r21.setRotationPoint(-4.5F, -5.0F, 3.5F);
        setRotationAngle(this.head_r21, 0.0F, -0.7418F, 0.0F);
        this.head_r21.mirror = false;
        this.head_r21.setTextureOffset(56, 11).addBox(0.2F, -3.0F, -0.1F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r21);

        this.head_r22 = new ModelRenderer(this);
        this.head_r22.setRotationPoint(3.9658F, -4.5038F, -4.1154F);
        setRotationAngle(this.head_r22, 0.0983F, 0.478F, 0.0453F);
        this.head_r22.mirror = false;
        this.head_r22.setTextureOffset(56, 59).addBox(-0.5F, -0.5F, -0.5F, (int)1.0, (int)1.0, (int)2.0, 0.3F);
        this.head.addChild(this.head_r22);

        this.head_r23 = new ModelRenderer(this);
        this.head_r23.setRotationPoint(4.1972F, -6.5038F, -4.87F);
        setRotationAngle(this.head_r23, 0.0F, 0.6545F, 0.0F);
        this.head_r23.mirror = false;
        this.head_r23.setTextureOffset(0, 41).addBox(-1.1972F, -1.5F, -0.13F, (int)1.0, (int)3.0, (int)1.0, 0.13F);
        this.head.addChild(this.head_r23);

        this.head_r24 = new ModelRenderer(this);
        this.head_r24.setRotationPoint(0.0F, -2.506F, 3.25F);
        setRotationAngle(this.head_r24, -0.1181F, 0.7383F, -0.0797F);
        this.head_r24.mirror = false;
        this.head_r24.setTextureOffset(44, 27).addBox(1.9489F, -0.494F, 3.1245F, (int)1.0, (int)1.0, (int)1.0, 0.3F);
        this.head.addChild(this.head_r24);

        this.head_r25 = new ModelRenderer(this);
        this.head_r25.setRotationPoint(4.5F, -5.0F, 3.5F);
        setRotationAngle(this.head_r25, 0.0F, 0.7418F, 0.0F);
        this.head_r25.mirror = false;
        this.head_r25.setTextureOffset(68, 89).addBox(-1.2F, -3.0F, -0.1F, (int)1.0, (int)6.0, (int)1.0, 0.05F);
        this.head.addChild(this.head_r25);

        this.head_r26 = new ModelRenderer(this);
        this.head_r26.setRotationPoint(-4.1258F, -8.0425F, 0.0F);
        setRotationAngle(this.head_r26, 0.0F, 0.0F, -0.8727F);
        this.head_r26.mirror = false;
        this.head_r26.setTextureOffset(18, 52).addBox(-0.3742F, -0.5F, -4.0F, (int)1.0, (int)1.0, (int)8.0, 0.1F);
        this.head.addChild(this.head_r26);

        this.head_r27 = new ModelRenderer(this);
        this.head_r27.setRotationPoint(-4.4163F, -6.7392F, 2.0F);
        setRotationAngle(this.head_r27, 0.0F, 0.0F, 0.0873F);
        this.head_r27.mirror = false;
        this.head_r27.setTextureOffset(35, 87).addBox(-0.5837F, 0.7392F, -1.0F, (int)1.0, (int)3.0, (int)3.0, -0.01F);
        this.head_r27.mirror = false;
        this.head_r27.setTextureOffset(8, 49).addBox(-0.5837F, -1.2608F, -6.0F, (int)1.0, (int)3.0, (int)8.0, 0.05F);
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

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
