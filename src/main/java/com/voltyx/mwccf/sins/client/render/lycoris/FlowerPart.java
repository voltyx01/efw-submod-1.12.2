package com.voltyx.mwccf.render.lycoris;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link ModelRenderer} whose own geometry supports fractional (float) box
 * dimensions. Vanilla {@code ModelBox} only accepts integer width/height/depth,
 * which is fine for most models but not for this one: it's built almost entirely
 * out of paper-thin "petal plate" boxes with dimensions like 0.115 or 0.155.
 * Blockbench's 1.7-1.12 Java exporter truncates those to int, which silently
 * collapses ~90% of this model's boxes down to zero-area (invisible) geometry.
 *
 * Pivot / rotation-point / rotation-angle / parent-child hierarchy behaviour is
 * completely untouched (all inherited from ModelRenderer, used exactly like
 * vanilla - setRotationPoint, rotateAngleX/Y/Z, addChild all still work as-is).
 * Only the leaf-cube drawing step is replaced with {@link FloatCube}.
 */
public class FlowerPart extends ModelRenderer {

    public final List<FloatCube> floatCubes = new ArrayList<>();

    public FlowerPart(ModelBase model) {
        super(model);
    }

    @Override
    public void render(float scale) {
        if (this.isHidden || !this.showModel) {
            return;
        }

        GlStateManager.pushMatrix();

        // same transform vanilla ModelRenderer.postRender() applies
        GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
        GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
        if (this.rotateAngleZ != 0.0F) {
            GlStateManager.rotate(this.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
        }
        if (this.rotateAngleY != 0.0F) {
            GlStateManager.rotate(this.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        }
        if (this.rotateAngleX != 0.0F) {
            GlStateManager.rotate(this.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
        }

        if (!floatCubes.isEmpty()) {
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buf = tess.getBuffer();
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
            for (FloatCube cube : floatCubes) {
                cube.addToBuffer(buf, scale, this.textureWidth, this.textureHeight);
            }
            tess.draw();
        }

        if (this.childModels != null) {
            for (Object childObj : this.childModels) {
                ((ModelRenderer) childObj).render(scale);
            }
        }

        GlStateManager.popMatrix();
    }
}
