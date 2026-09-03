package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelgas_mask extends ModelBiped {

    public ModelRenderer head;
    public ModelRenderer head_r1;
    public ModelRenderer head_r2;

    public Modelgas_mask() {
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
        this.head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.4F);
        this.head.mirror = false;
        this.head.setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.5F);
        this.head.mirror = false;
        this.head.setTextureOffset(32, 16).addBox(-4.0F, -8.0F, -4.0F, (int)8.0, (int)8.0, (int)8.0, 0.8F);
        this.head.mirror = false;
        this.head.setTextureOffset(0, 4).addBox(-4.0F, -5.0F, -5.0F, (int)3.0, (int)3.0, (int)1.0, 0.05F);
        this.head.mirror = false;
        this.head.setTextureOffset(0, 0).addBox(1.0F, -5.0F, -5.0F, (int)3.0, (int)3.0, (int)1.0, 0.05F);
        this.bipedHead.addChild(this.head);

        this.head_r1 = new ModelRenderer(this);
        this.head_r1.setRotationPoint(0.0F, 0.5419F, -4.7219F);
        setRotationAngle(this.head_r1, -0.6604F, -0.5727F, 0.4182F);
        this.head_r1.mirror = false;
        this.head_r1.setTextureOffset(9, 21).addBox(-1.5F, 0.4936F, -1.5326F, (int)3.0, (int)2.0, (int)3.0, 0.1F);
        this.head.addChild(this.head_r1);

        this.head_r2 = new ModelRenderer(this);
        this.head_r2.setRotationPoint(0.0F, 0.5419F, -4.7219F);
        setRotationAngle(this.head_r2, -0.8706F, -0.5724F, 0.5713F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(0, 16).addBox(-1.5F, -1.4936F, -1.4674F, (int)3.0, (int)4.0, (int)3.0, -0.23F);
        this.head_r2.mirror = false;
        this.head_r2.setTextureOffset(12, 16).addBox(-1.5F, -2.4936F, -1.4674F, (int)3.0, (int)2.0, (int)3.0, 0.05F);
        this.head.addChild(this.head_r2);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
