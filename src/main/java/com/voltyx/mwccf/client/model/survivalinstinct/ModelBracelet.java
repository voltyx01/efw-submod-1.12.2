package com.voltyx.mwccf.client.model.survivalinstinct;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBracelet extends ModelBiped {
    private final ModelRenderer bracelet;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer screen;

    public ModelBracelet() {
        super(0.0F, 0.0F, 64, 32);
        this.textureWidth = 64; // Fallback, usually overridden by custom texture
        this.textureHeight = 32;

        // Clear standard biped boxes so they don't render with our small texture
        this.bipedHead.cubeList.clear();
        this.bipedHeadwear.cubeList.clear();
        this.bipedBody.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
        this.bipedRightLeg.cubeList.clear();
        this.bipedLeftLeg.cubeList.clear();

        bracelet = new ModelRenderer(this);
        // Changed from (0.0F, 24.0F, 0.0F) to (-5.0F, 22.0F, 0.0F) to offset relative to bipedLeftArm's pivot (5, 2, 0)
        bracelet.setRotationPoint(-5.0F, 22.0F, 0.0F);
        bracelet.cubeList.add(new ModelBox(bracelet, 21, 3, 3.25F, -17.0F, 1.0F, 1, 2, 1, 0.0F, false));
        bracelet.cubeList.add(new ModelBox(bracelet, 29, 19, 3.25F, -18.0F, -1.6F, 0, 4, 1, 0.0F, false));
        bracelet.cubeList.add(new ModelBox(bracelet, 30, 6, 3.25F, -18.0F, 0.6F, 0, 4, 1, 0.0F, false));
        bracelet.cubeList.add(new ModelBox(bracelet, 22, 4, 3.25F, -18.6F, -1.05F, 0, 1, 2, 0.0F, false));
        bracelet.cubeList.add(new ModelBox(bracelet, 22, 4, 3.25F, -14.4F, -1.05F, 0, 1, 2, 0.0F, false));
        bracelet.cubeList.add(new ModelBox(bracelet, 21, 3, 3.25F, -17.0F, -2.0F, 1, 2, 1, 0.0F, false));
        bracelet.cubeList.add(new ModelBox(bracelet, 17, 2, 4.0F, -17.0F, -2.75F, 4, 2, 1, 0.0F, false));
        bracelet.cubeList.add(new ModelBox(bracelet, 18, 2, 4.0F, -17.0F, 1.75F, 4, 2, 1, 0.0F, false));
        bracelet.cubeList.add(new ModelBox(bracelet, 18, -2, 7.75F, -17.0F, -2.0F, 1, 2, 4, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(7.0F, -15.0F, 3.0F);
        bracelet.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.7854F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 23, 3, 0.91F, -2.0F, -0.325F, 1, 2, 1, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(3.0F, -15.0F, -1.0F);
        bracelet.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.7854F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 23, 3, 0.91F, -2.0F, -0.675F, 1, 2, 1, 0.0F, false));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(7.0F, -15.0F, -3.0F);
        bracelet.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, -0.7854F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 23, 3, 0.91F, -2.0F, -0.675F, 1, 2, 1, 0.0F, false));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(3.0F, -15.0F, 1.0F);
        bracelet.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, -0.7854F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 23, 3, 0.91F, -2.0F, -0.325F, 1, 2, 1, 0.0F, false));

        screen = new ModelRenderer(this);
        screen.setRotationPoint(0.0F, 0.0F, 0.0F);
        bracelet.addChild(screen);
        screen.cubeList.add(new ModelBox(screen, 0, 0, 3.175F, -18.0F, -1.0F, 1, 4, 2, 0.0F, false));

        this.bipedLeftArm.addChild(bracelet);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
