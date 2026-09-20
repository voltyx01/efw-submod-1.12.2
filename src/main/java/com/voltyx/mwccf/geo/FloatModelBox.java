package com.voltyx.mwccf.geo;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FloatModelBox extends ModelBox {
    private final TexturedQuad[] quadList;

    private TexturedQuad makeQuad(PositionTextureVertex[] vertices, float u1, float v1, float u2, float v2, float tw, float th) {
        PositionTextureVertex[] v = new PositionTextureVertex[vertices.length];
        v[0] = vertices[0].setTexturePosition(u1 / tw, v1 / th);
        v[1] = vertices[1].setTexturePosition(u2 / tw, v1 / th);
        v[2] = vertices[2].setTexturePosition(u2 / tw, v2 / th);
        v[3] = vertices[3].setTexturePosition(u1 / tw, v2 / th);
        return new TexturedQuad(v);
    }

    public FloatModelBox(ModelRenderer renderer, int texU, int texV, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
        super(renderer, texU, texV, x, y, z, 0, 0, 0, delta, mirror);
        
        this.quadList = new TexturedQuad[6];
        float f = x + dx;
        float f1 = y + dy;
        float f2 = z + dz;
        x = x - delta;
        y = y - delta;
        z = z - delta;
        f = f + delta;
        f1 = f1 + delta;
        f2 = f2 + delta;

        if (mirror) {
            float f3 = f;
            f = x;
            x = f3;
        }

        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);

        float tw = renderer.textureWidth;
        float th = renderer.textureHeight;

        // In Bedrock/Java box UV mapping, texture grids are integer pixel blocks.
        // Round dimensions for UV coordinates so sub-pixel box sizes (e.g. 6.5) do not bleed into transparent/adjacent pixels.
        float uW = Math.round(dx);
        float uH = Math.round(dy);
        float uD = Math.round(dz);

        this.quadList[0] = makeQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5}, (float)texU + uD + uW, (float)texV + uD, (float)texU + uD + uW + uD, (float)texV + uD + uH, tw, th);
        this.quadList[1] = makeQuad(new PositionTextureVertex[] {positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2}, (float)texU, (float)texV + uD, (float)texU + uD, (float)texV + uD + uH, tw, th);
        this.quadList[2] = makeQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex}, (float)texU + uD, (float)texV, (float)texU + uD + uW, (float)texV + uD, tw, th);
        this.quadList[3] = makeQuad(new PositionTextureVertex[] {positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5}, (float)texU + uD + uW, (float)texV + uD, (float)texU + uD + uW + uW, (float)texV, tw, th);
        this.quadList[4] = makeQuad(new PositionTextureVertex[] {positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1}, (float)texU + uD, (float)texV + uD, (float)texU + uD + uW, (float)texV + uD + uH, tw, th);
        this.quadList[5] = makeQuad(new PositionTextureVertex[] {positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6}, (float)texU + uD + uW + uD, (float)texV + uD, (float)texU + uD + uW + uD + uW, (float)texV + uD + uH, tw, th);

        if (mirror) {
            for (TexturedQuad texturedquad : this.quadList) {
                texturedquad.flipFace();
            }
        }
    }

    public FloatModelBox(ModelRenderer renderer, java.util.Map<String, float[]> faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
        super(renderer, 0, 0, x, y, z, 0, 0, 0, delta, mirror);

        float f = x + dx;
        float f1 = y + dy;
        float f2 = z + dz;
        x = x - delta;
        y = y - delta;
        z = z - delta;
        f = f + delta;
        f1 = f1 + delta;
        f2 = f2 + delta;

        // Note: For per-face UV models (Blockbench Bedrock export), cubes already have
        // explicit coordinates and individual face UV definitions. Applying vanilla box 'mirror'
        // swaps X and calls flipFace(), which corrupts face orientation and causes solid/blank textures.
        // We therefore keep the actual 3D box coordinates intact.

        float tw = renderer.textureWidth;
        float th = renderer.textureHeight;

        java.util.List<TexturedQuad> quads = new java.util.ArrayList<>();

        // East (+X)
        if (faceUvs.containsKey("east")) {
            float[] uv = faceUvs.get("east");
            float u0 = uv[0];
            float v0 = uv[1];
            float u1 = uv[0] + uv[2];
            float v1 = uv[1] + uv[3];
            quads.add(makeQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(f, y, f2, 0, 0),
                new PositionTextureVertex(f, y, z, 0, 0),
                new PositionTextureVertex(f, f1, z, 0, 0),
                new PositionTextureVertex(f, f1, f2, 0, 0)
            }, u0, v0, u1, v1, tw, th));
        }

        // West (-X)
        if (faceUvs.containsKey("west")) {
            float[] uv = faceUvs.get("west");
            float u0 = uv[0];
            float v0 = uv[1];
            float u1 = uv[0] + uv[2];
            float v1 = uv[1] + uv[3];
            quads.add(makeQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(x, y, z, 0, 0),
                new PositionTextureVertex(x, y, f2, 0, 0),
                new PositionTextureVertex(x, f1, f2, 0, 0),
                new PositionTextureVertex(x, f1, z, 0, 0)
            }, u0, v0, u1, v1, tw, th));
        }

        // Up (-Y)
        if (faceUvs.containsKey("up")) {
            float[] uv = faceUvs.get("up");
            float u0 = uv[0];
            float v0 = uv[1];
            float u1 = uv[0] + uv[2];
            float v1 = uv[1] + uv[3];
            quads.add(makeQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(f, y, f2, 0, 0),
                new PositionTextureVertex(x, y, f2, 0, 0),
                new PositionTextureVertex(x, y, z, 0, 0),
                new PositionTextureVertex(f, y, z, 0, 0)
            }, u0, v0, u1, v1, tw, th));
        }

        // Down (+Y)
        if (faceUvs.containsKey("down")) {
            float[] uv = faceUvs.get("down");
            float u0 = uv[0];
            float v0 = uv[1];
            float u1 = uv[0] + uv[2];
            float v1 = uv[1] + uv[3];
            quads.add(makeQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(f, f1, z, 0, 0),
                new PositionTextureVertex(x, f1, z, 0, 0),
                new PositionTextureVertex(x, f1, f2, 0, 0),
                new PositionTextureVertex(f, f1, f2, 0, 0)
            }, u0, v0, u1, v1, tw, th));
        }

        // North (-Z)
        if (faceUvs.containsKey("north")) {
            float[] uv = faceUvs.get("north");
            float u0 = uv[0];
            float v0 = uv[1];
            float u1 = uv[0] + uv[2];
            float v1 = uv[1] + uv[3];
            quads.add(makeQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(f, y, z, 0, 0),
                new PositionTextureVertex(x, y, z, 0, 0),
                new PositionTextureVertex(x, f1, z, 0, 0),
                new PositionTextureVertex(f, f1, z, 0, 0)
            }, u0, v0, u1, v1, tw, th));
        }

        // South (+Z)
        if (faceUvs.containsKey("south")) {
            float[] uv = faceUvs.get("south");
            float u0 = uv[0];
            float v0 = uv[1];
            float u1 = uv[0] + uv[2];
            float v1 = uv[1] + uv[3];
            quads.add(makeQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(x, y, f2, 0, 0),
                new PositionTextureVertex(f, y, f2, 0, 0),
                new PositionTextureVertex(f, f1, f2, 0, 0),
                new PositionTextureVertex(x, f1, f2, 0, 0)
            }, u0, v0, u1, v1, tw, th));
        }

        this.quadList = quads.toArray(new TexturedQuad[0]);
        // Also assign super.quadList via reflection or public field so standard ModelBox renderers render it
        try {
            java.lang.reflect.Field fQuad = ModelBox.class.getDeclaredField("quadList");
            fQuad.setAccessible(true);
            fQuad.set(this, this.quadList);
        } catch (Throwable ignored) {}
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(BufferBuilder renderer, float scale) {
        for (TexturedQuad texturedquad : this.quadList) {
            texturedquad.draw(renderer, scale);
        }
    }
}
