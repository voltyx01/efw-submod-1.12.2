package com.voltyx.mwccf.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelBipedSlimArmor extends ModelBiped {

    public static final ModelBipedSlimArmor INSTANCE_LEGGINGS = new ModelBipedSlimArmor(0.5F);
    public static final ModelBipedSlimArmor INSTANCE_ARMOR = new ModelBipedSlimArmor(1.0F);

    public ModelBipedSlimArmor(float modelSize) {
        super(modelSize, 0.0F, 64, 32);
        
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
    }
}
