package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelUmbrelaCasco2 extends ModelBiped {

    public ModelRenderer Casco;
    public ModelRenderer group;
    public ModelRenderer Casco_r2;

    public ModelUmbrelaCasco2() {
        this.textureWidth = 200;
        this.textureHeight = 200;

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
        this.Casco = new ModelRenderer(this);
        this.Casco.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Casco.mirror = false;
        this.Casco.setTextureOffset(0, 0).addBox(-4.0F, -6.9F, -4.0F, (int)8.0, (int)7.0, (int)8.0, 0.1F);
        this.Casco.mirror = false;
        this.Casco.setTextureOffset(59, 0).addBox(-3.3F, -4.8F, -4.0F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.Casco.mirror = false;
        this.Casco.setTextureOffset(59, 12).addBox(1.3F, -4.8F, -4.0F, (int)2.0, (int)3.0, (int)1.0, 0.2F);
        this.Casco.mirror = false;
        this.Casco.setTextureOffset(0, 16).addBox(-4.0F, -7.4F, -4.0F, (int)8.0, (int)4.0, (int)8.0, 0.6F);
        this.Casco.mirror = false;
        this.Casco.setTextureOffset(26, 10).addBox(-4.0F, -4.0F, -2.0F, (int)8.0, (int)2.0, (int)6.0, 1.0F);
        this.Casco.mirror = false;
        this.Casco.setTextureOffset(24, 0).addBox(-4.0F, -4.0F, -4.0F, (int)8.0, (int)0.0, (int)7.0, 0.8F);
        this.Casco.mirror = false;
        this.Casco.setTextureOffset(24, 18).addBox(-4.0F, -4.0F, 0.0F, (int)8.0, (int)4.0, (int)1.0, 0.3F);
        this.bipedHead.addChild(this.Casco);

        this.group = new ModelRenderer(this);
        this.group.setRotationPoint(-8.0F, 0.0F, 8.0F);
        setRotationAngle(this.group, -0.8021F, -0.504F, -0.437F);
        this.group.mirror = false;
        this.group.setTextureOffset(0, 53).addBox(2.5F, 2.0F, -0.7F, (int)3.0, (int)1.0, (int)3.0, -0.1F);
        this.group.mirror = false;
        this.group.setTextureOffset(0, 51).addBox(2.5F, 0.0F, -0.7F, (int)3.0, (int)3.0, (int)3.0, -0.2F);
        this.group.mirror = false;
        this.group.setTextureOffset(12, 48).addBox(2.9F, 2.7F, 0.0F, (int)2.3, (int)1.0, (int)2.3, -0.2F);
        this.Casco.addChild(this.group);

        this.Casco_r2 = new ModelRenderer(this);
        this.Casco_r2.setRotationPoint(-8.0F, 0.0F, 8.0F);
        setRotationAngle(this.Casco_r2, -0.9163F, 0.0F, 0.0F);
        this.Casco_r2.mirror = false;
        this.Casco_r2.setTextureOffset(12, 36).addBox(-1.1F, 1.7F, 0.0F, (int)2.3, (int)1.0, (int)2.3, 0.1F);
        this.Casco_r2.mirror = false;
        this.Casco_r2.setTextureOffset(12, 41).addBox(-1.5F, 1.0F, -0.7F, (int)3.0, (int)1.0, (int)3.0, 0.2F);
        this.Casco_r2.mirror = false;
        this.Casco_r2.setTextureOffset(0, 39).addBox(-1.5F, -3.0F, -0.7F, (int)3.0, (int)3.0, (int)3.0, 0.1F);
        this.Casco_r2.mirror = false;
        this.Casco_r2.setTextureOffset(73, 0).addBox(-1.5F, 0.0F, -0.7F, (int)3.0, (int)1.0, (int)3.0, -0.1F);
        this.group.addChild(this.Casco_r2);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
