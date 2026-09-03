package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelrecon_layer_2 extends ModelBiped {

    public ModelRenderer waist;
    public ModelRenderer left_leg;
    public ModelRenderer right_leg;

    public Modelrecon_layer_2() {
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
        this.waist = new ModelRenderer(this);
        this.waist.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.waist.mirror = false;
        this.waist.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.4F);
        this.bipedBody.addChild(this.waist);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(16, 32).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)10.0, (int)4.0, 0.3F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(0, 25).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)10.0, (int)4.0, 0.3F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(24, 0).addBox(-4.0F, 1.0F, -1.0F, (int)3.0, (int)6.0, (int)2.0, 0.05F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(18, 10).addBox(-3.0F, 1.0F, -3.0F, (int)6.0, (int)3.0, (int)6.0, -0.5F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(18, 19).addBox(-2.0F, -1.0F, -3.0F, (int)3.0, (int)7.0, (int)6.0, -0.6F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(0, 16).addBox(-3.0F, 4.0F, -3.0F, (int)6.0, (int)3.0, (int)6.0, -0.5F);
        this.bipedRightLeg.addChild(this.right_leg);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
