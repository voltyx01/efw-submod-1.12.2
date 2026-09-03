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

        PositionTextureVertex ptv0 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        PositionTextureVertex ptv1 = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
        PositionTextureVertex ptv2 = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
        PositionTextureVertex ptv3 = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
        PositionTextureVertex ptv4 = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
        PositionTextureVertex ptv5 = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
        PositionTextureVertex ptv6 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
        PositionTextureVertex ptv7 = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);

        // Use exact float dimensions for UV mapping to match .geo.json format
        float w = dx;
        float h = dy;
        float d = dz;

        float tw = renderer.textureWidth;
        float th = renderer.textureHeight;

        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] {
            ptv5.setTexturePosition((texU + d + w) / tw, (texV + d) / th),
            ptv1.setTexturePosition((texU + d + w + d) / tw, (texV + d) / th),
            ptv2.setTexturePosition((texU + d + w + d) / tw, (texV + d + h) / th),
            ptv6.setTexturePosition((texU + d + w) / tw, (texV + d + h) / th)
        });
        
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] {
            ptv0.setTexturePosition((texU) / tw, (texV + d) / th),
            ptv4.setTexturePosition((texU + d) / tw, (texV + d) / th),
            ptv7.setTexturePosition((texU + d) / tw, (texV + d + h) / th),
            ptv3.setTexturePosition((texU) / tw, (texV + d + h) / th)
        });
        
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] {
            ptv5.setTexturePosition((texU + d) / tw, (texV) / th),
            ptv4.setTexturePosition((texU + d + w) / tw, (texV) / th),
            ptv0.setTexturePosition((texU + d + w) / tw, (texV + d) / th),
            ptv1.setTexturePosition((texU + d) / tw, (texV + d) / th)
        });
        
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] {
            ptv2.setTexturePosition((texU + d + w) / tw, (texV + d) / th),
            ptv3.setTexturePosition((texU + d + w + w) / tw, (texV + d) / th),
            ptv7.setTexturePosition((texU + d + w + w) / tw, (texV) / th),
            ptv6.setTexturePosition((texU + d + w) / tw, (texV) / th)
        });
        
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] {
            ptv1.setTexturePosition((texU + d) / tw, (texV + d) / th),
            ptv0.setTexturePosition((texU + d + w) / tw, (texV + d) / th),
            ptv3.setTexturePosition((texU + d + w) / tw, (texV + d + h) / th),
            ptv2.setTexturePosition((texU + d) / tw, (texV + d + h) / th)
        });
        
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] {
            ptv4.setTexturePosition((texU + d + w + d) / tw, (texV + d) / th),
            ptv5.setTexturePosition((texU + d + w + d + w) / tw, (texV + d) / th),
            ptv6.setTexturePosition((texU + d + w + d + w) / tw, (texV + d + h) / th),
            ptv7.setTexturePosition((texU + d + w + d) / tw, (texV + d + h) / th)
        });

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

        if (mirror) {
            float f3 = f;
            f = x;
            x = f3;
        }

        float tw = renderer.textureWidth;
        float th = renderer.textureHeight;

        java.util.List<TexturedQuad> quads = new java.util.ArrayList<>();

        // East (+X)
        if (faceUvs.containsKey("east")) {
            float[] uv = faceUvs.get("east");
            float u0 = uv[0] / tw;
            float v0 = uv[1] / th;
            float u1 = (uv[0] + uv[2]) / tw;
            float v1 = (uv[1] + uv[3]) / th;
            quads.add(new TexturedQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(f, y, f2, u0, v0),
                new PositionTextureVertex(f, y, z, u1, v0),
                new PositionTextureVertex(f, f1, z, u1, v1),
                new PositionTextureVertex(f, f1, f2, u0, v1)
            }));
        }

        // West (-X)
        if (faceUvs.containsKey("west")) {
            float[] uv = faceUvs.get("west");
            float u0 = uv[0] / tw;
            float v0 = uv[1] / th;
            float u1 = (uv[0] + uv[2]) / tw;
            float v1 = (uv[1] + uv[3]) / th;
            quads.add(new TexturedQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(x, y, z, u0, v0),
                new PositionTextureVertex(x, y, f2, u1, v0),
                new PositionTextureVertex(x, f1, f2, u1, v1),
                new PositionTextureVertex(x, f1, z, u0, v1)
            }));
        }

        // Up (-Y)
        if (faceUvs.containsKey("up")) {
            float[] uv = faceUvs.get("up");
            float u0 = uv[0] / tw;
            float v0 = uv[1] / th;
            float u1 = (uv[0] + uv[2]) / tw;
            float v1 = (uv[1] + uv[3]) / th;
            quads.add(new TexturedQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(f, y, f2, u1, v1),
                new PositionTextureVertex(x, y, f2, u0, v1),
                new PositionTextureVertex(x, y, z, u0, v0),
                new PositionTextureVertex(f, y, z, u1, v0)
            }));
        }

        // Down (+Y)
        if (faceUvs.containsKey("down")) {
            float[] uv = faceUvs.get("down");
            float u0 = uv[0] / tw;
            float v0 = uv[1] / th;
            float u1 = (uv[0] + uv[2]) / tw;
            float v1 = (uv[1] + uv[3]) / th;
            quads.add(new TexturedQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(f, f1, z, u0, v0),
                new PositionTextureVertex(x, f1, z, u1, v0),
                new PositionTextureVertex(x, f1, f2, u1, v1),
                new PositionTextureVertex(f, f1, f2, u0, v1)
            }));
        }

        // North (-Z)
        if (faceUvs.containsKey("north")) {
            float[] uv = faceUvs.get("north");
            float u0 = uv[0] / tw;
            float v0 = uv[1] / th;
            float u1 = (uv[0] + uv[2]) / tw;
            float v1 = (uv[1] + uv[3]) / th;
            quads.add(new TexturedQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(f, y, z, u0, v0),
                new PositionTextureVertex(x, y, z, u1, v0),
                new PositionTextureVertex(x, f1, z, u1, v1),
                new PositionTextureVertex(f, f1, z, u0, v1)
            }));
        }

        // South (+Z)
        if (faceUvs.containsKey("south")) {
            float[] uv = faceUvs.get("south");
            float u0 = uv[0] / tw;
            float v0 = uv[1] / th;
            float u1 = (uv[0] + uv[2]) / tw;
            float v1 = (uv[1] + uv[3]) / th;
            quads.add(new TexturedQuad(new PositionTextureVertex[] {
                new PositionTextureVertex(x, y, f2, u0, v0),
                new PositionTextureVertex(f, y, f2, u1, v0),
                new PositionTextureVertex(f, f1, f2, u1, v1),
                new PositionTextureVertex(x, f1, f2, u0, v1)
            }));
        }

        if (mirror) {
            for (TexturedQuad texturedquad : quads) {
                texturedquad.flipFace();
            }
        }

        this.quadList = quads.toArray(new TexturedQuad[0]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(BufferBuilder renderer, float scale) {
        for (TexturedQuad texturedquad : this.quadList) {
            texturedquad.draw(renderer, scale);
        }
    }
}
