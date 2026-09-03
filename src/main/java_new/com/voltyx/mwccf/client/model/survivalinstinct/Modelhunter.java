package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelhunter extends ModelBiped {

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
    public ModelRenderer left_arm;
    public ModelRenderer right_arm;
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe;
    public ModelRenderer left_leg;
    public ModelRenderer right_leg;

    public Modelhunter() {
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
        this.head.setTextureOffset(0, 12).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.1F);
        this.head.mirror = false;
        this.head.setTextureOffset(17, 91).addBox(-5.0F, -9.0F, -5.0F, (int)10.0, (int)5.0, (int)10.0, -0.2F);
        this.head.mirror = false;
        this.head.setTextureOffset(24, 106).addBox(-5.0F, -9.0F, -2.0F, (int)10.0, (int)5.0, (int)3.0, 0.2F);
        this.head.mirror = false;
        this.head.setTextureOffset(74, 26).addBox(-3.0F, -8.0F, -5.0F, (int)6.0, (int)4.0, (int)3.0, -0.6F);
        this.head.mirror = false;
        this.head.setTextureOffset(28, 51).addBox(-6.0F, -7.0F, -2.5F, (int)2.0, (int)3.0, (int)8.0, -0.4F);
        this.head.mirror = false;
        this.head.setTextureOffset(50, 47).addBox(4.0F, -7.0F, -2.5F, (int)2.0, (int)3.0, (int)8.0, -0.4F);
        this.head.mirror = false;
        this.head.setTextureOffset(0, 69).addBox(3.5F, -5.0F, -3.0F, (int)3.0, (int)5.0, (int)5.0, -0.6F);
        this.head.mirror = false;
        this.head.setTextureOffset(55, 67).addBox(-6.5F, -5.0F, -3.0F, (int)3.0, (int)5.0, (int)5.0, -0.6F);
        this.head.mirror = false;
        this.head.setTextureOffset(0, 0).addBox(-5.0F, -5.8F, -5.0F, (int)10.0, (int)1.0, (int)10.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(30, 0).addBox(-5.0F, -5.2F, 0.0F, (int)10.0, (int)4.0, (int)5.0, -0.4F);
        this.bipedHead.addChild(this.head);

        this.visor_r1 = new ModelRenderer(this);
        this.visor_r1.setRotationPoint(0.0F, -7.2628F, -4.1938F);
        setRotationAngle(this.visor_r1, 0.2618F, 0.0F, 0.0F);
        this.visor_r1.mirror = false;
        this.visor_r1.setTextureOffset(62, 49).addBox(-2.0F, -0.713F, -2.0102F, (int)4.0, (int)3.0, (int)3.0, -0.6F);
        this.head.addChild(this.visor_r1);

        this.visor_r2 = new ModelRenderer(this);
        this.visor_r2.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r2, 0.8727F, 0.0F, 0.0F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(54, 58).addBox(-1.0F, -1.4305F, -3.0018F, (int)2.0, (int)2.0, (int)4.0, -0.6F);
        this.head.addChild(this.visor_r2);

        this.visor_r3 = new ModelRenderer(this);
        this.visor_r3.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r3, -0.0436F, 0.0F, 0.0F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(24, 12).addBox(-4.0F, 1.3031F, -3.5191F, (int)8.0, (int)2.0, (int)3.0, -0.5F);
        this.head.addChild(this.visor_r3);

        this.visor_r4 = new ModelRenderer(this);
        this.visor_r4.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r4, 0.0457F, -0.3051F, -0.0138F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(76, 75).addBox(0.9109F, 2.0163F, -5.6283F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(38, 17).addBox(0.9109F, 2.0163F, -2.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(12, 44).addBox(0.9109F, 2.0163F, -4.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(18, 44).addBox(0.7809F, 1.9292F, -6.616F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(0, 0).addBox(0.7809F, 1.9292F, -7.0335F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.head.addChild(this.visor_r4);

        this.visor_r5 = new ModelRenderer(this);
        this.visor_r5.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r5, 0.0438F, -0.0872F, -0.0038F);
        this.visor_r5.mirror = false;
        this.visor_r5.setTextureOffset(55, 77).addBox(-0.2981F, 2.0163F, -4.9525F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.visor_r5.mirror = false;
        this.visor_r5.setTextureOffset(46, 36).addBox(-0.2981F, 2.0163F, -1.9525F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r5.mirror = false;
        this.visor_r5.setTextureOffset(0, 4).addBox(-0.2981F, 1.9292F, -6.3662F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.visor_r5.mirror = false;
        this.visor_r5.setTextureOffset(22, 59).addBox(-0.2981F, 1.9292F, -5.9487F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.head.addChild(this.visor_r5);

        this.visor_r6 = new ModelRenderer(this);
        this.visor_r6.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r6, 0.0436F, 0.0F, 0.0F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(48, 25).addBox(0.0F, 2.0163F, -3.8782F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(36, 48).addBox(0.0F, 1.9292F, -5.8744F, (int)2.0, (int)2.0, (int)1.0, -0.2F);
        this.head.addChild(this.visor_r6);

        this.visor_r7 = new ModelRenderer(this);
        this.visor_r7.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r7, 0.0438F, 0.0872F, 0.0038F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(27, 78).addBox(-1.7019F, 2.0163F, -4.9525F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(40, 55).addBox(-1.7019F, 2.0163F, -1.9525F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(58, 42).addBox(-1.7019F, 2.0163F, -3.9525F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(22, 59).addBox(-1.7019F, 1.9292F, -5.9487F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(0, 12).addBox(-1.7019F, 1.9292F, -6.3662F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.head.addChild(this.visor_r7);

        this.visor_r8 = new ModelRenderer(this);
        this.visor_r8.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r8, 0.0457F, 0.3051F, 0.0138F);
        this.visor_r8.mirror = false;
        this.visor_r8.setTextureOffset(0, 16).addBox(-2.7809F, 1.9292F, -7.0335F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.visor_r8.mirror = false;
        this.visor_r8.setTextureOffset(60, 10).addBox(-2.7809F, 1.9292F, -6.616F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r8.mirror = false;
        this.visor_r8.setTextureOffset(36, 62).addBox(-2.9109F, 2.0163F, -4.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r8.mirror = false;
        this.visor_r8.setTextureOffset(64, 13).addBox(-2.9109F, 2.0163F, -2.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r8.mirror = false;
        this.visor_r8.setTextureOffset(0, 79).addBox(-2.9109F, 2.0163F, -5.6283F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.head.addChild(this.visor_r8);

        this.visor_r9 = new ModelRenderer(this);
        this.visor_r9.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r9, 0.2618F, 0.0F, 0.0F);
        this.visor_r9.mirror = false;
        this.visor_r9.setTextureOffset(0, 60).addBox(-2.0F, -0.5344F, -4.0628F, (int)4.0, (int)2.0, (int)3.0, -0.5F);
        this.head.addChild(this.visor_r9);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(-5.5F, -2.5F, -0.5F);
        setRotationAngle(this.head_r1, 0.0F, 0.0F, 0.5236F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(44, 73).addBox(-1.0F, -0.5F, -2.5F, (int)3.0, (int)3.0, (int)5.0, -0.7F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(5.5F, -2.5F, -0.5F);
        setRotationAngle(this.head_r2, -0.001F, 0.0089F, -0.5236F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(23, 64).addBox(-2.0F, -2.5F, -2.5F, (int)3.0, (int)5.0, (int)5.0, -0.7F);
        this.head.addChild(this.head_r2);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(70, 19).addBox(-3.0F, 3.0F, -3.3F, (int)6.0, (int)1.0, (int)4.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(55, 30).addBox(-4.0F, 2.0F, -3.0F, (int)8.0, (int)9.0, (int)1.0, -0.2F);
        this.body.mirror = false;
        this.body.setTextureOffset(72, 4).addBox(-3.0F, 5.0F, -3.3F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(70, 19).addBox(-3.0F, 3.0F, -3.0F, (int)6.0, (int)1.0, (int)4.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(72, 4).addBox(-3.0F, 5.0F, -3.0F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(65, 71).addBox(3.0F, 3.0F, -3.0F, (int)2.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = false;
        this.body.setTextureOffset(70, 49).addBox(3.0F, 7.0F, -3.0F, (int)2.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = false;
        this.body.setTextureOffset(64, 10).addBox(-5.0F, 3.0F, -3.0F, (int)2.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = false;
        this.body.setTextureOffset(33, 69).addBox(-5.0F, 7.0F, -3.0F, (int)2.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = false;
        this.body.setTextureOffset(72, 0).addBox(-3.0F, 7.0F, -3.0F, (int)6.0, (int)3.0, (int)1.0, 0.05F);
        this.body.mirror = false;
        this.body.setTextureOffset(62, 40).addBox(-4.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.body.mirror = false;
        this.body.setTextureOffset(60, 0).addBox(-4.0F, -1.0F, -3.0F, (int)3.0, (int)4.0, (int)6.0, -0.65F);
        this.body.mirror = false;
        this.body.setTextureOffset(10, 59).addBox(1.0F, -1.0F, -3.0F, (int)3.0, (int)4.0, (int)6.0, -0.65F);
        this.body.mirror = false;
        this.body.setTextureOffset(60, 58).addBox(1.0F, -1.0F, -3.0F, (int)3.0, (int)3.0, (int)6.0, -0.5F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(3.0F, 4.5F, -3.1F);
        setRotationAngle(this.body_r1, 0.0481F, -0.4359F, -0.0203F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(55, 0).addBox(-1.0F, -2.5F, -0.5F, (int)2.0, (int)4.0, (int)1.0, 0.1F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(42, 62).addBox(-1.0F, 0.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(0, 65).addBox(-1.0F, -1.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.15F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(-3.25F, 2.2267F, -3.0832F);
        setRotationAngle(this.body_r2, 0.0426F, 0.0094F, -0.218F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(0, 0).addBox(-0.75F, -2.2704F, 0.1159F, (int)1.0, (int)2.0, (int)0.0, 0.05F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(43, 9).addBox(-0.75F, -0.2296F, -0.6159F, (int)2.0, (int)3.0, (int)1.0, 0.1F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(4.0F, 9.5F, -3.1F);
        setRotationAngle(this.body_r3, 0.0517F, -0.5666F, -0.0278F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(55, 0).addBox(-1.0F, -2.5F, -0.5F, (int)2.0, (int)4.0, (int)1.0, 0.1F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(60, 4).addBox(-1.0F, 0.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(34, 65).addBox(-1.0F, -1.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.15F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(-4.0F, 9.5F, -3.1F);
        setRotationAngle(this.body_r4, 0.0569F, 0.6973F, 0.0366F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(0, 8).addBox(-1.0F, 0.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(0, 67).addBox(-1.0F, -1.5F, -0.5F, (int)2.0, (int)1.0, (int)1.0, 0.15F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(55, 0).addBox(-1.0F, -2.5F, -0.5F, (int)2.0, (int)4.0, (int)1.0, 0.1F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(-4.9924F, 8.1632F, 0.0F);
        setRotationAngle(this.body_r5, -3.1416F, 0.0F, 2.9671F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(42, 58).addBox(-1.5F, -2.25F, -3.0F, (int)3.0, (int)5.0, (int)6.0, -0.6F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(58, 19).addBox(-1.5F, -2.25F, -3.0F, (int)3.0, (int)4.0, (int)6.0, -0.5F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(5.5F, 8.25F, 0.0F);
        setRotationAngle(this.body_r6, 0.0F, 0.0F, 0.0873F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(58, 19).addBox(-2.0F, -2.25F, -3.0F, (int)3.0, (int)4.0, (int)6.0, -0.5F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(42, 58).addBox(-2.0F, -2.25F, -3.0F, (int)3.0, (int)5.0, (int)6.0, -0.6F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(0.5F, 9.25F, -4.0F);
        setRotationAngle(this.body_r7, 1.5765F, 1.3092F, 1.5651F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(42, 58).addBox(-2.0F, -2.25F, -3.5F, (int)3.0, (int)5.0, (int)6.0, -0.6F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(97, 4).addBox(-2.0F, -2.25F, -3.5F, (int)3.0, (int)4.0, (int)2.0, -0.3F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(58, 19).addBox(-2.0F, -2.25F, -3.5F, (int)3.0, (int)4.0, (int)6.0, -0.5F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(4.5F, 9.25F, -4.0F);
        setRotationAngle(this.body_r8, 1.5765F, 1.3092F, 1.5651F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(97, 4).addBox(-2.0F, -2.25F, -3.5F, (int)3.0, (int)4.0, (int)2.0, -0.3F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(0.0F, 9.0F, 2.25F);
        setRotationAngle(this.body_r9, 0.0F, 3.1416F, 0.0F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(72, 4).addBox(-3.0F, 0.0F, -1.25F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(70, 19).addBox(-3.0F, -2.0F, -1.25F, (int)6.0, (int)1.0, (int)4.0, 0.05F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(72, 4).addBox(-3.0F, -4.0F, -1.25F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(55, 30).addBox(-4.0F, -7.0F, -1.05F, (int)8.0, (int)9.0, (int)1.0, -0.2F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(70, 19).addBox(-3.0F, -6.0F, -1.25F, (int)6.0, (int)1.0, (int)4.0, 0.05F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(72, 4).addBox(-3.0F, -4.0F, -1.25F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(70, 19).addBox(-3.0F, -6.0F, -1.25F, (int)6.0, (int)1.0, (int)4.0, 0.05F);
        this.body.addChild(this.body_r9);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = false;
        this.left_arm.setTextureOffset(48, 9).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(0, 44).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedRightArm.addChild(this.right_arm);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = true;
        this.left_shoe.setTextureOffset(20, 43).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.4F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(0, 110).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(0, 112).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.6F);
        this.bipedLeftLeg.addChild(this.left_shoe);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = false;
        this.right_shoe.setTextureOffset(20, 43).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.4F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(0, 112).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.6F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(0, 110).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.3F);
        this.bipedRightLeg.addChild(this.right_shoe);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(93, 101).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(93, 101).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(87, 117).addBox(-2.0F, 0.0F, 0.0F, (int)4.0, (int)4.0, (int)1.0, 0.25F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(101, 90).addBox(-2.0F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(101, 85).addBox(-2.0F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(106, 114).addBox(-4.0F, 1.0F, -1.5F, (int)2.0, (int)5.0, (int)3.0, -0.1F);
        this.bipedRightLeg.addChild(this.right_leg);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
