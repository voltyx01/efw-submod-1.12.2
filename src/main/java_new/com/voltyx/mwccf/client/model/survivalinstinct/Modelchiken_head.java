package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelchiken_head extends ModelBiped {

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

    public Modelchiken_head() {
        this.textureWidth = 64;
        this.textureHeight = 64;

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
        this.head.setTextureOffset(32, 41).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)10.0, (int)8.0, 0.7F);
        this.head.mirror = false;
        this.head.setTextureOffset(37, 12).addBox(-5.0F, -6.0F, -3.0F, (int)1.0, (int)4.0, (int)4.0, 0.1F);
        this.head.mirror = false;
        this.head.setTextureOffset(30, 34).addBox(4.0F, -6.0F, -3.0F, (int)1.0, (int)4.0, (int)4.0, 0.1F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(4.5F, -5.0F, -1.0F);
        setRotationAngle(this.head_r1, 0.132F, 0.1298F, 0.0172F);
        this.head_r1.mirror = true;
        this.head_r1.setTextureOffset(53, 12).addBox(-0.5F, -1.0F, -2.0F, (int)1.0, (int)2.0, (int)4.0, 0.4F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(-1.0F, -1.0F, -5.25F);
        setRotationAngle(this.head_r2, -0.0462F, 0.0302F, -0.1719F);
        this.head_r2.mirror = true;
        this.head_r2.setTextureOffset(0, 16).addBox(2.0F, -1.0F, -0.75F, (int)2.0, (int)6.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r2);

        this.head_r3 = new ModelRenderer(this);
        this.head_r3.setRotationPoint(0.0F, -2.0F, -5.25F);
        setRotationAngle(this.head_r3, -0.1555F, -0.1642F, -0.3335F);
        this.head_r3.mirror = true;
        this.head_r3.setTextureOffset(40, 34).addBox(2.0F, -1.0F, -0.75F, (int)2.0, (int)5.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r3);

        this.head_r4 = new ModelRenderer(this);
        this.head_r4.setRotationPoint(0.0F, -8.5F, 1.5F);
        setRotationAngle(this.head_r4, -0.6981F, 0.0F, 0.0F);
        this.head_r4.mirror = false;
        this.head_r4.setTextureOffset(10, 32).addBox(-1.0F, -5.5F, -0.5F, (int)2.0, (int)6.0, (int)3.0, 0.05F);
        this.head.addChild(this.head_r4);

        this.head_r5 = new ModelRenderer(this);
        this.head_r5.setRotationPoint(0.0F, -7.5F, 3.5F);
        setRotationAngle(this.head_r5, -1.0472F, 0.0F, 0.0F);
        this.head_r5.mirror = false;
        this.head_r5.setTextureOffset(39, 20).addBox(-1.0F, -3.5F, -0.5F, (int)2.0, (int)3.0, (int)3.0, 0.05F);
        this.head.addChild(this.head_r5);

        this.head_r6 = new ModelRenderer(this);
        this.head_r6.setRotationPoint(0.0F, -8.5F, -1.5F);
        setRotationAngle(this.head_r6, -0.48F, 0.0F, 0.0F);
        this.head_r6.mirror = false;
        this.head_r6.setTextureOffset(0, 32).addBox(-1.0F, -7.5F, -0.5F, (int)2.0, (int)7.0, (int)3.0, 0.05F);
        this.head.addChild(this.head_r6);

        this.head_r7 = new ModelRenderer(this);
        this.head_r7.setRotationPoint(0.0F, -8.5F, -4.5F);
        setRotationAngle(this.head_r7, -0.2618F, 0.0F, 0.0F);
        this.head_r7.mirror = false;
        this.head_r7.setTextureOffset(20, 32).addBox(-1.0F, -5.5F, -0.5F, (int)2.0, (int)6.0, (int)3.0, 0.05F);
        this.head.addChild(this.head_r7);

        this.head_r8 = new ModelRenderer(this);
        this.head_r8.setRotationPoint(0.0F, -2.0F, -5.25F);
        setRotationAngle(this.head_r8, 0.1745F, 0.0F, 0.0F);
        this.head_r8.mirror = false;
        this.head_r8.setTextureOffset(29, 28).addBox(-1.5F, 0.0F, -2.75F, (int)3.0, (int)2.0, (int)4.0, 0.05F);
        this.head_r8.mirror = false;
        this.head_r8.setTextureOffset(24, 16).addBox(-2.0F, -2.0F, -3.75F, (int)4.0, (int)2.0, (int)5.0, 0.05F);
        this.head.addChild(this.head_r8);

        this.head_r9 = new ModelRenderer(this);
        this.head_r9.setRotationPoint(0.0F, -2.0F, -5.25F);
        setRotationAngle(this.head_r9, -0.1555F, 0.1642F, 0.3335F);
        this.head_r9.mirror = false;
        this.head_r9.setTextureOffset(40, 34).addBox(-4.0F, -1.0F, -0.75F, (int)2.0, (int)5.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r9);

        this.head_r10 = new ModelRenderer(this);
        this.head_r10.setRotationPoint(1.0F, -1.0F, -5.25F);
        setRotationAngle(this.head_r10, -0.0462F, -0.0302F, 0.1719F);
        this.head_r10.mirror = false;
        this.head_r10.setTextureOffset(0, 16).addBox(-4.0F, -1.0F, -0.75F, (int)2.0, (int)6.0, (int)2.0, 0.05F);
        this.head.addChild(this.head_r10);

        this.head_r11 = new ModelRenderer(this);
        this.head_r11.setRotationPoint(0.0F, -3.7716F, -5.9335F);
        setRotationAngle(this.head_r11, 0.48F, 0.0F, 0.0F);
        this.head_r11.mirror = false;
        this.head_r11.setTextureOffset(24, 0).addBox(-2.0F, -1.0F, -2.5F, (int)4.0, (int)2.0, (int)5.0, -0.2F);
        this.head.addChild(this.head_r11);

        this.head_r12 = new ModelRenderer(this);
        this.head_r12.setRotationPoint(-4.5F, -5.0F, -1.0F);
        setRotationAngle(this.head_r12, 0.132F, -0.1298F, -0.0172F);
        this.head_r12.mirror = false;
        this.head_r12.setTextureOffset(53, 12).addBox(-0.5F, -1.0F, -2.0F, (int)1.0, (int)2.0, (int)4.0, 0.4F);
        this.head.addChild(this.head_r12);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
