package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelpolice_armor extends ModelBiped {

    public ModelRenderer head;
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
    public ModelRenderer body_r10;
    public ModelRenderer body_r11;
    public ModelRenderer body_r12;
    public ModelRenderer body_r13;
    public ModelRenderer body_r14;
    public ModelRenderer body_r15;
    public ModelRenderer body_r16;
    public ModelRenderer body_r17;
    public ModelRenderer left_arm;
    public ModelRenderer right_arm;
    public ModelRenderer left_leg;
    public ModelRenderer right_leg;
    public ModelRenderer right_leg_r1;
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe;

    public Modelpolice_armor() {
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
        this.head.setTextureOffset(0, 12).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(11, 64).addBox(-2.0F, -7.0F, -5.0F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(44, 45).addBox(-1.0F, -8.0F, -5.0F, (int)2.0, (int)1.0, (int)1.0, 0.05F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(0.0F, -8.0313F, -0.0661F);
        setRotationAngle(this.head_r1, -0.0436F, 0.0F, 0.0F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(0, 0).addBox(-5.0F, -1.4687F, -5.0F, (int)10.0, (int)2.0, (int)10.0, 0.05F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(0.0F, -6.5F, -0.75F);
        setRotationAngle(this.head_r2, 0.0436F, 0.0F, 0.0F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(24, 20).addBox(-5.0F, 0.5F, -6.25F, (int)10.0, (int)1.0, (int)8.0, 0.05F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(0, 28).addBox(-4.0F, -1.5F, -3.25F, (int)8.0, (int)2.0, (int)8.0, 0.551F);
        this.head.addChild(this.head_r2);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.mirror = false;
        this.body.setTextureOffset(32, 29).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedBody.addChild(this.body);

        this.body_r1 = new ModelRenderer(this);
        this.body_r1.setRotationPoint(0.0F, 6.5015F, 2.4173F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(48, 45).addBox(-4.0F, -4.5015F, -1.4827F, (int)8.0, (int)9.0, (int)2.0, -0.15F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(38, 64).addBox(-3.0F, -2.0015F, -0.4827F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body_r1.mirror = false;
        this.body_r1.setTextureOffset(36, 16).addBox(-3.0F, -3.5015F, -0.4827F, (int)6.0, (int)1.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r1);

        this.body_r2 = new ModelRenderer(this);
        this.body_r2.setRotationPoint(0.0F, 6.5015F, 2.4173F);
        setRotationAngle(this.body_r2, -0.0436F, 0.0F, 0.0F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(56, 10).addBox(-3.5F, -1.1167F, -0.2176F, (int)7.0, (int)5.0, (int)1.0, 0.05F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(66, 45).addBox(-3.0F, -0.5167F, -0.0176F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(30, 2).addBox(-3.0F, 0.9833F, -0.0176F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body_r2.mirror = false;
        this.body_r2.setTextureOffset(30, 0).addBox(-3.0F, 2.4833F, -0.0176F, (int)6.0, (int)1.0, (int)1.0, -0.01F);
        this.body.addChild(this.body_r2);

        this.body_r3 = new ModelRenderer(this);
        this.body_r3.setRotationPoint(2.0F, 8.0F, -2.9F);
        setRotationAngle(this.body_r3, 3.098F, 0.0F, -3.1416F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(0, 64).addBox(-1.5F, -5.6F, -0.6F, (int)1.0, (int)4.0, (int)1.0, 0.05F);
        this.body_r3.mirror = false;
        this.body_r3.setTextureOffset(36, 12).addBox(-1.5F, -5.6F, -0.6F, (int)1.0, (int)3.0, (int)1.0, 0.2F);
        this.body.addChild(this.body_r3);

        this.body_r4 = new ModelRenderer(this);
        this.body_r4.setRotationPoint(2.5F, 8.1493F, -2.968F);
        setRotationAngle(this.body_r4, 3.0986F, 0.0423F, 3.1246F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(0, 4).addBox(-2.0F, -1.75F, -0.5F, (int)3.0, (int)4.0, (int)1.0, 0.05F);
        this.body_r4.mirror = false;
        this.body_r4.setTextureOffset(24, 32).addBox(-2.0F, -1.75F, -0.5F, (int)3.0, (int)3.0, (int)1.0, 0.2F);
        this.body.addChild(this.body_r4);

        this.body_r5 = new ModelRenderer(this);
        this.body_r5.setRotationPoint(-0.5F, 8.1493F, -2.968F);
        setRotationAngle(this.body_r5, 3.0103F, -0.1308F, -3.1359F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(24, 12).addBox(-1.0F, -1.75F, -0.5F, (int)2.0, (int)4.0, (int)1.0, 0.05F);
        this.body_r5.mirror = false;
        this.body_r5.setTextureOffset(12, 54).addBox(-1.0F, -1.75F, -0.5F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body.addChild(this.body_r5);

        this.body_r6 = new ModelRenderer(this);
        this.body_r6.setRotationPoint(-3.5F, 8.1493F, -2.968F);
        setRotationAngle(this.body_r6, 3.0533F, -0.218F, -3.1319F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(56, 37).addBox(-1.0F, -1.75F, -0.5F, (int)2.0, (int)4.0, (int)1.0, 0.05F);
        this.body_r6.mirror = false;
        this.body_r6.setTextureOffset(54, 56).addBox(-1.0F, -1.75F, -0.5F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.body.addChild(this.body_r6);

        this.body_r7 = new ModelRenderer(this);
        this.body_r7.setRotationPoint(0.0F, 8.0F, -2.9F);
        setRotationAngle(this.body_r7, 3.098F, 0.0F, -3.1416F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(0, 38).addBox(-1.5F, -5.6F, -0.6F, (int)1.0, (int)3.0, (int)1.0, 0.2F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(68, 38).addBox(-1.5F, -5.6F, -0.6F, (int)1.0, (int)4.0, (int)1.0, 0.05F);
        this.body_r7.mirror = false;
        this.body_r7.setTextureOffset(43, 66).addBox(-3.5F, -3.6F, -0.8F, (int)7.0, (int)6.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r7);

        this.body_r8 = new ModelRenderer(this);
        this.body_r8.setRotationPoint(3.0F, 0.95F, 0.0F);
        setRotationAngle(this.body_r8, 0.0F, 0.0F, 0.0873F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(0, 12).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)3.0, (int)2.0, -0.15F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(0, 28).addBox(-1.0F, -0.95F, 1.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r8.mirror = false;
        this.body_r8.setTextureOffset(16, 64).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)2.0, (int)6.0, 0.05F);
        this.body.addChild(this.body_r8);

        this.body_r9 = new ModelRenderer(this);
        this.body_r9.setRotationPoint(0.0F, 7.0208F, 0.0F);
        setRotationAngle(this.body_r9, 0.0F, 0.0F, 0.0436F);
        this.body_r9.mirror = false;
        this.body_r9.setTextureOffset(24, 12).addBox(-4.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r9);

        this.body_r10 = new ModelRenderer(this);
        this.body_r10.setRotationPoint(-3.0F, 7.95F, 0.0F);
        setRotationAngle(this.body_r10, 0.0F, 0.0F, -0.0436F);
        this.body_r10.mirror = false;
        this.body_r10.setTextureOffset(56, 37).addBox(-2.0F, 0.05F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.addChild(this.body_r10);

        this.body_r11 = new ModelRenderer(this);
        this.body_r11.setRotationPoint(-3.0F, 0.95F, 0.0F);
        setRotationAngle(this.body_r11, 0.0F, 0.0F, -0.0873F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(30, 4).addBox(-1.0F, -0.95F, 1.0F, (int)2.0, (int)3.0, (int)2.0, -0.3F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(62, 50).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)2.0, (int)6.0, 0.05F);
        this.body_r11.mirror = false;
        this.body_r11.setTextureOffset(68, 0).addBox(-1.0F, -1.55F, -3.0F, (int)2.0, (int)3.0, (int)1.0, -0.15F);
        this.body.addChild(this.body_r11);

        this.body_r12 = new ModelRenderer(this);
        this.body_r12.setRotationPoint(3.0F, 7.95F, 0.0F);
        setRotationAngle(this.body_r12, 0.0F, 0.0F, 0.0436F);
        this.body_r12.mirror = false;
        this.body_r12.setTextureOffset(42, 56).addBox(-1.0F, 0.05F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.3F);
        this.body.addChild(this.body_r12);

        this.body_r13 = new ModelRenderer(this);
        this.body_r13.setRotationPoint(0.0F, 7.0208F, 0.0F);
        setRotationAngle(this.body_r13, 0.0F, 0.0F, -0.0436F);
        this.body_r13.mirror = false;
        this.body_r13.setTextureOffset(54, 58).addBox(1.9504F, -1.1506F, -3.0F, (int)3.0, (int)2.0, (int)6.0, -0.2F);
        this.body.addChild(this.body_r13);

        this.body_r14 = new ModelRenderer(this);
        this.body_r14.setRotationPoint(-2.0F, 3.75F, -3.1F);
        setRotationAngle(this.body_r14, -3.0543F, -0.0019F, 3.098F);
        this.body_r14.mirror = false;
        this.body_r14.setTextureOffset(24, 29).addBox(-2.0F, -0.75F, -0.4F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r14);

        this.body_r15 = new ModelRenderer(this);
        this.body_r15.setRotationPoint(-1.0F, 3.75F, -3.1F);
        setRotationAngle(this.body_r15, 3.098F, -0.0019F, 3.098F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(28, 38).addBox(-0.5F, 0.25F, -0.4F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body_r15.mirror = false;
        this.body_r15.setTextureOffset(32, 45).addBox(1.5F, 0.25F, -0.4F, (int)1.0, (int)2.0, (int)1.0, 0.05F);
        this.body.addChild(this.body_r15);

        this.body_r16 = new ModelRenderer(this);
        this.body_r16.setRotationPoint(-2.0F, 3.75F, -3.1F);
        setRotationAngle(this.body_r16, 3.098F, -0.0019F, 3.098F);
        this.body_r16.mirror = false;
        this.body_r16.setTextureOffset(0, 0).addBox(-2.0F, -0.75F, -0.4F, (int)4.0, (int)3.0, (int)1.0, -0.15F);
        this.body.addChild(this.body_r16);

        this.body_r17 = new ModelRenderer(this);
        this.body_r17.setRotationPoint(0.0F, 6.5F, -2.55F);
        setRotationAngle(this.body_r17, 0.0F, 3.1416F, 0.0F);
        this.body_r17.mirror = false;
        this.body_r17.setTextureOffset(52, 16).addBox(-4.0F, -4.5F, -1.45F, (int)8.0, (int)9.0, (int)2.0, -0.15F);
        this.body.addChild(this.body_r17);

        this.left_arm = new ModelRenderer(this);
        this.left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_arm.mirror = false;
        this.left_arm.setTextureOffset(16, 38).addBox(-1.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedLeftArm.addChild(this.left_arm);

        this.right_arm = new ModelRenderer(this);
        this.right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_arm.mirror = false;
        this.right_arm.setTextureOffset(32, 45).addBox(-3.0F, -2.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedRightArm.addChild(this.right_arm);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(0, 38).addBox(-1.9F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(40, 0).addBox(-2.1F, 0.0F, -2.0F, (int)4.0, (int)12.0, (int)4.0, 0.1F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(68, 33).addBox(-2.1F, 1.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(68, 23).addBox(-2.1F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.3F);
        this.bipedRightLeg.addChild(this.right_leg);

        this.right_leg_r1 = new ModelRenderer(this);
        this.right_leg_r1.setRotationPoint(-3.1F, 2.9253F, -0.2885F);
        setRotationAngle(this.right_leg_r1, -0.0863F, -1.5272F, 0.001F);
        this.right_leg_r1.mirror = true;
        this.right_leg_r1.setTextureOffset(93, 0).addBox(-1.0F, -1.5075F, -1.3285F, (int)2.0, (int)4.0, (int)2.0, 0.3F);
        this.right_leg_r1.mirror = true;
        this.right_leg_r1.setTextureOffset(89, 3).addBox(-0.5F, -0.5075F, -0.1285F, (int)1.0, (int)2.0, (int)1.0, 0.5F);
        this.right_leg_r1.mirror = true;
        this.right_leg_r1.setTextureOffset(93, 6).addBox(-1.0F, -1.5075F, -1.0285F, (int)2.0, (int)2.0, (int)2.0, 0.6F);
        this.right_leg.addChild(this.right_leg_r1);

        this.left_shoe = new ModelRenderer(this);
        this.left_shoe.setRotationPoint(0.10000000000000009F, 0.0F, 0.0F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(56, 27).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.49F);
        this.left_shoe.mirror = false;
        this.left_shoe.setTextureOffset(11, 67).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.bipedLeftLeg.addChild(this.left_shoe);

        this.right_shoe = new ModelRenderer(this);
        this.right_shoe.setRotationPoint(-0.10000000000000009F, 0.0F, 0.0F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(56, 27).addBox(-2.0F, 6.0F, -2.0F, (int)4.0, (int)6.0, (int)4.0, 0.49F);
        this.right_shoe.mirror = true;
        this.right_shoe.setTextureOffset(11, 67).addBox(-2.0F, 11.0F, -3.0F, (int)4.0, (int)1.0, (int)1.0, 0.4F);
        this.bipedRightLeg.addChild(this.right_shoe);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
