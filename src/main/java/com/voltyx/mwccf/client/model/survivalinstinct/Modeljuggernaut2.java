package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modeljuggernaut2 extends ModelBiped {

    public ModelRenderer waist;
    public ModelRenderer left_leg;
    public ModelRenderer right_leg;

    public Modeljuggernaut2() {
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
        this.waist.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, -2.0F, (int)8.0, (int)12.0, (int)4.0, 0.1F);
        this.bipedBody.addChild(this.waist);

        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)9.0, (int)4.0, 0.1F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(28, 15).addBox(-2.0F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(24, 10).addBox(-2.0F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(16, 29).addBox(-1.8F, 5.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(26, 29).addBox(-1.8F, 4.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(16, 32).addBox(-1.8F, 3.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(16, 29).addBox(-1.8F, 5.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(26, 29).addBox(-1.8F, 4.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(16, 32).addBox(-1.8F, 3.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.left_leg.mirror = false;
        this.left_leg.setTextureOffset(0, 29).addBox(1.2F, 1.0F, -1.0F, (int)2.0, (int)5.0, (int)2.0, 0.1F);
        this.bipedLeftLeg.addChild(this.left_leg);

        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, (int)4.0, (int)9.0, (int)4.0, 0.1F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(24, 0).addBox(-2.2F, 2.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(24, 5).addBox(-2.2F, 4.0F, -2.0F, (int)4.0, (int)1.0, (int)4.0, 0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(32, 20).addBox(-2.0F, 4.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(26, 32).addBox(-2.0F, 3.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(32, 23).addBox(-2.0F, 5.0F, -3.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(26, 32).addBox(-2.0F, 3.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(32, 20).addBox(-2.0F, 4.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, 0.05F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(32, 23).addBox(-2.0F, 5.0F, 2.0F, (int)4.0, (int)2.0, (int)1.0, -0.2F);
        this.right_leg.mirror = false;
        this.right_leg.setTextureOffset(8, 29).addBox(-3.0F, 1.0F, -1.0F, (int)2.0, (int)5.0, (int)2.0, 0.1F);
        this.bipedRightLeg.addChild(this.right_leg);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
