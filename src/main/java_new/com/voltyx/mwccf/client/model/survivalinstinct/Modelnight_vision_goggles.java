package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelnight_vision_goggles extends ModelBiped {

    public ModelRenderer head;
    public ModelRenderer visor_r1;
    public ModelRenderer visor_r2;
    public ModelRenderer visor_r3;
    public ModelRenderer visor_r4;
    public ModelRenderer visor_r5;
    public ModelRenderer visor_r6;
    public ModelRenderer visor_r7;
    public ModelRenderer visor_r8;

    public Modelnight_vision_goggles() {
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
        this.head.setTextureOffset(0, 0).addBox(-5.0F, -5.8F, -5.0F, (int)10.0, (int)1.0, (int)10.0, 0.05F);
        this.bipedHead.addChild(this.head);

        this.visor_r1 = new ModelRenderer(this);
        this.visor_r1.setRotationPoint(-1.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r1, 0.0457F, -0.3051F, -0.0138F);
        this.visor_r1.mirror = true;
        this.visor_r1.setTextureOffset(0, 16).addBox(0.7809F, 1.9292F, -7.0335F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.visor_r1.mirror = true;
        this.visor_r1.setTextureOffset(60, 10).addBox(0.7809F, 1.9292F, -6.616F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r1.mirror = true;
        this.visor_r1.setTextureOffset(36, 62).addBox(0.9109F, 2.0163F, -4.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r1.mirror = true;
        this.visor_r1.setTextureOffset(64, 13).addBox(0.9109F, 2.0163F, -2.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r1.mirror = true;
        this.visor_r1.setTextureOffset(0, 79).addBox(0.9109F, 2.0163F, -5.6283F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.head.addChild(this.visor_r1);

        this.visor_r2 = new ModelRenderer(this);
        this.visor_r2.setRotationPoint(0.0F, -7.2628F, -4.1938F);
        setRotationAngle(this.visor_r2, 0.2618F, 0.0F, 0.0F);
        this.visor_r2.mirror = false;
        this.visor_r2.setTextureOffset(62, 49).addBox(-2.0F, -0.713F, -2.0102F, (int)4.0, (int)3.0, (int)3.0, -0.6F);
        this.head.addChild(this.visor_r2);

        this.visor_r3 = new ModelRenderer(this);
        this.visor_r3.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r3, 0.8727F, 0.0F, 0.0F);
        this.visor_r3.mirror = false;
        this.visor_r3.setTextureOffset(54, 58).addBox(-1.0F, -1.4305F, -3.0018F, (int)2.0, (int)2.0, (int)4.0, -0.6F);
        this.head.addChild(this.visor_r3);

        this.visor_r4 = new ModelRenderer(this);
        this.visor_r4.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r4, -0.0436F, 0.0F, 0.0F);
        this.visor_r4.mirror = false;
        this.visor_r4.setTextureOffset(24, 12).addBox(-4.0F, 1.3031F, -3.5191F, (int)8.0, (int)2.0, (int)3.0, -0.5F);
        this.head.addChild(this.visor_r4);

        this.visor_r5 = new ModelRenderer(this);
        this.visor_r5.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r5, 0.0436F, 0.0F, 0.0F);
        this.visor_r5.mirror = false;
        this.visor_r5.setTextureOffset(36, 48).addBox(0.0F, 1.9292F, -5.8744F, (int)2.0, (int)2.0, (int)1.0, -0.2F);
        this.head.addChild(this.visor_r5);

        this.visor_r6 = new ModelRenderer(this);
        this.visor_r6.setRotationPoint(0.2981F, -2.3571F, -8.2073F);
        setRotationAngle(this.visor_r6, 0.0436F, 0.0F, 0.0F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(27, 78).addBox(-1.2981F, -0.9564F, -1.5019F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(40, 55).addBox(-1.2981F, -0.9564F, 1.4981F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(58, 42).addBox(-1.2981F, -0.9564F, -0.5019F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(22, 59).addBox(-1.2981F, -1.0436F, -2.4981F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r6.mirror = false;
        this.visor_r6.setTextureOffset(0, 12).addBox(-1.2981F, -1.0436F, -2.9156F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.head.addChild(this.visor_r6);

        this.visor_r7 = new ModelRenderer(this);
        this.visor_r7.setRotationPoint(1.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r7, 0.0457F, 0.3051F, 0.0138F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(0, 16).addBox(-2.7809F, 1.9292F, -7.0335F, (int)2.0, (int)2.0, (int)2.0, -0.3F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(60, 10).addBox(-2.7809F, 1.9292F, -6.616F, (int)2.0, (int)2.0, (int)1.0, 0.05F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(36, 62).addBox(-2.9109F, 2.0163F, -4.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(64, 13).addBox(-2.9109F, 2.0163F, -2.6283F, (int)2.0, (int)2.0, (int)1.0, 0.1F);
        this.visor_r7.mirror = false;
        this.visor_r7.setTextureOffset(0, 79).addBox(-2.9109F, 2.0163F, -5.6283F, (int)2.0, (int)2.0, (int)5.0, -0.2F);
        this.head.addChild(this.visor_r7);

        this.visor_r8 = new ModelRenderer(this);
        this.visor_r8.setRotationPoint(0.0F, -5.4775F, -4.8897F);
        setRotationAngle(this.visor_r8, 0.2618F, 0.0F, 0.0F);
        this.visor_r8.mirror = false;
        this.visor_r8.setTextureOffset(0, 60).addBox(-2.0F, -0.5344F, -4.0628F, (int)4.0, (int)2.0, (int)3.0, -0.5F);
        this.head.addChild(this.visor_r8);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
