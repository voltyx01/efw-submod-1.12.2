package com.voltyx.gender.render;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WildfireModelRenderer extends ModelRenderer {

    // Для совместимости с 1.12.2 мы создаем пустую базовую модель
    private static final net.minecraft.client.model.ModelBase DUMMY_MODEL = new net.minecraft.client.model.ModelBase() {};

    public WildfireModelRenderer(int texWidth, int texHeight, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
        super(DUMMY_MODEL, texU, texV);
        this.setTextureSize(texWidth, texHeight);
        this.mirror = mirror;

        this.cubeList.add(new BreastBox(this, texU, texV, x, y, z, dx, dy, dz, delta, mirror));
    }

    // Конструктор для OverlayModelBox (одежда) - matching 1.20.1
    public WildfireModelRenderer(boolean isLeft, int texWidth, int texHeight, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
        super(DUMMY_MODEL, texU, texV);
        this.setTextureSize(texWidth, texHeight);
        this.mirror = mirror;

        this.cubeList.add(new OverlayBox(this, isLeft, texU, texV, x, y, z, dx, dy, dz, delta, mirror));
    }

    public WildfireModelRenderer(int texWidth, int texHeight) {
        super(DUMMY_MODEL, 0, 0);
        this.setTextureSize(texWidth, texHeight);
    }

    public void addBreastBox(int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
        this.cubeList.add(new BreastBox(this, texU, texV, x, y, z, dx, dy, dz, delta, mirror));
    }

    /**
     * Кастомный куб для груди, который повторяет UV развертку из 1.18.2 / 1.20.1
     */
    public static class BreastBox extends ModelBox {
        private TexturedQuad[] customQuads;

        public BreastBox(ModelRenderer renderer, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
            super(renderer, texU, texV, x, y, z, dx, dy, dz, delta, mirror);

            this.customQuads = new TexturedQuad[5];

            float f = x + dx;
            float f1 = y + dy;
            float f2 = z + dz;
            x -= delta;
            y -= delta;
            z -= delta;
            f += delta;
            f1 += delta;
            f2 += delta;

            if (mirror) {
                float f3 = f;
                f = x;
                x = f3;
            }

            PositionTextureVertex vertex7 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
            PositionTextureVertex vertex = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
            PositionTextureVertex vertex1 = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
            PositionTextureVertex vertex2 = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
            PositionTextureVertex vertex3 = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
            PositionTextureVertex vertex4 = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
            PositionTextureVertex vertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
            PositionTextureVertex vertex6 = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);

            // Точная копия развертки из BreastModelBox (1.20.1)
            this.customQuads[0] = new TexturedQuad(new PositionTextureVertex[]{vertex4, vertex, vertex1, vertex5}, texU + 4 + dx, texV + 4, texU + 4 + dx + 4, texV + 4 + dy, renderer.textureWidth, renderer.textureHeight);
            this.customQuads[1] = new TexturedQuad(new PositionTextureVertex[]{vertex7, vertex3, vertex6, vertex2}, texU, texV + 4, texU + 4, texV + 4 + dy, renderer.textureWidth, renderer.textureHeight);
            this.customQuads[2] = new TexturedQuad(new PositionTextureVertex[]{vertex4, vertex3, vertex7, vertex}, texU + 4, texV, texU + 4 + dx, texV + 4, renderer.textureWidth, renderer.textureHeight);
            this.customQuads[3] = new TexturedQuad(new PositionTextureVertex[]{vertex1, vertex2, vertex6, vertex5}, texU + 4, texV + 4 + 4, texU + 4 + dx, texV + 1 + 4 + dy, renderer.textureWidth, renderer.textureHeight);
            this.customQuads[4] = new TexturedQuad(new PositionTextureVertex[]{vertex, vertex7, vertex2, vertex1}, texU + 4, texV + 4, texU + 4 + dx, texV + 4 + dy, renderer.textureWidth, renderer.textureHeight);

            if (mirror) {
                for (TexturedQuad quad : this.customQuads) {
                    if (quad != null) quad.flipFace();
                }
            }
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void render(BufferBuilder renderer, float scale) {
            for (TexturedQuad quad : this.customQuads) {
                if (quad != null) {
                    quad.draw(renderer, scale);
                }
            }
        }
    }

    /**
     * Кастомный куб для одежды (Overlay) - 1.20.1
     */
    public static class OverlayBox extends ModelBox {
        private TexturedQuad[] customQuads;

        public OverlayBox(ModelRenderer renderer, boolean isLeft, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
            super(renderer, texU, texV, x, y, z, dx, dy, dz, delta, mirror);

            this.customQuads = new TexturedQuad[4];

            float f = x + dx;
            float f1 = y + dy;
            float f2 = z + dz;
            x -= delta;
            y -= delta;
            z -= delta;
            f += delta;
            f1 += delta;
            f2 += delta;

            if (mirror) {
                float f3 = f;
                f = x;
                x = f3;
            }

            PositionTextureVertex vertex7 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
            PositionTextureVertex vertex = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
            PositionTextureVertex vertex1 = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
            PositionTextureVertex vertex2 = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
            PositionTextureVertex vertex3 = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
            PositionTextureVertex vertex4 = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
            PositionTextureVertex vertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
            PositionTextureVertex vertex6 = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);

            // Точная копия развертки из OverlayModelBox (1.20.1)
            if (!isLeft) {
                this.customQuads[0] = new TexturedQuad(new PositionTextureVertex[]{vertex4, vertex, vertex1, vertex5}, texU + dz + dx, texV + dz, texU + dz + dx + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
            } else {
                this.customQuads[0] = new TexturedQuad(new PositionTextureVertex[]{vertex7, vertex3, vertex6, vertex2}, texU, texV + dz, texU + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
            }
            this.customQuads[1] = new TexturedQuad(new PositionTextureVertex[]{vertex4, vertex3, vertex7, vertex}, texU + dz, texV, texU + dz + dx, texV + dz, renderer.textureWidth, renderer.textureHeight);
            this.customQuads[2] = new TexturedQuad(new PositionTextureVertex[]{vertex1, vertex2, vertex6, vertex5}, texU + dz, texV + dz + 4, texU + dz + dx, texV + 1 + dz + dy, renderer.textureWidth, renderer.textureHeight);
            this.customQuads[3] = new TexturedQuad(new PositionTextureVertex[]{vertex, vertex7, vertex2, vertex1}, texU + dz, texV + dz, texU + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);

            if (mirror) {
                for (TexturedQuad quad : this.customQuads) {
                    if (quad != null) quad.flipFace();
                }
            }
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void render(BufferBuilder renderer, float scale) {
            for (TexturedQuad quad : this.customQuads) {
                if (quad != null) {
                    quad.draw(renderer, scale);
                }
            }
        }
    }
}