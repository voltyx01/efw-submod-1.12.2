package com.voltyx.mwccf.render.lycoris;

import net.minecraft.client.renderer.BufferBuilder;

/**
 * Equivalent of vanilla {@code ModelBox}, except width/height/depth are floats.
 * Needed because this model is built almost entirely out of paper-thin plates
 * (dimensions like 0.115) that vanilla's int-only ModelBox would truncate to 0.
 *
 * UV footprint uses the same "cross unwrap" layout Minecraft uses for entity
 * model boxes, so if you later paint a real texture (instead of a flat fill)
 * it will still map sensibly. Backface culling is not enabled anywhere in this
 * render path, so exact winding order does not affect visibility - only shading.
 */
public class FloatCube {

    private final int texU, texV;
    private final float x, y, z, w, h, d;
    private final boolean mirror;

    public FloatCube(int texU, int texV, float x, float y, float z, float w, float h, float d,
                      float delta, boolean mirror) {
        this.texU = texU;
        this.texV = texV;
        this.mirror = mirror;
        this.x = x - delta;
        this.y = y - delta;
        this.z = z - delta;
        this.w = w + delta * 2f;
        this.h = h + delta * 2f;
        this.d = d + delta * 2f;
    }

    public void addToBuffer(BufferBuilder buf, float scale, float texWidth, float texHeight) {
        float x0 = this.x * scale, y0 = this.y * scale, z0 = this.z * scale;
        float x1 = (this.x + this.w) * scale, y1 = (this.y + this.h) * scale, z1 = (this.z + this.d) * scale;

        float w = this.w, h = this.h, d = this.d;
        float tw = Math.max(texWidth, 1f), th = Math.max(texHeight, 1f);

        // standard Mojang box-unwrap footprints (u,v in texture pixels)
        float uTop = texU + d,           vTop = texV;
        float uSide = texU,              vSide = texV + d;
        float uFront = texU + d,         vFront = texV + d;
        float uBack = texU + d + w + d,  vBack = texV + d;

        boolean mir = mirror;

        // +Y is "down" in Minecraft model space
        quad(buf, x0, y1, z0, x1, y1, z0, x1, y1, z1, x0, y1, z1, uFront, vTop, uFront + w, vTop + d, tw, th, 0, 1, 0, mir); // down
        quad(buf, x0, y0, z1, x1, y0, z1, x1, y0, z0, x0, y0, z0, uFront + w, vTop, uFront + w + w, vTop + d, tw, th, 0, -1, 0, mir); // up
        quad(buf, x0, y0, z0, x1, y0, z0, x1, y1, z0, x0, y1, z0, uFront, vFront, uFront + w, vFront + h, tw, th, 0, 0, -1, mir); // north
        quad(buf, x1, y0, z1, x0, y0, z1, x0, y1, z1, x1, y1, z1, uBack, vFront, uBack + w, vFront + h, tw, th, 0, 0, 1, mir); // south
        quad(buf, x1, y0, z0, x1, y0, z1, x1, y1, z1, x1, y1, z0, uSide, vSide, uSide + d, vSide + h, tw, th, 1, 0, 0, mir); // east
        quad(buf, x0, y0, z1, x0, y0, z0, x0, y1, z0, x0, y1, z1, uSide + d + w, vSide, uSide + d + w + d, vSide + h, tw, th, -1, 0, 0, mir); // west
    }

    private static void quad(BufferBuilder buf,
                              float x1, float y1, float z1,
                              float x2, float y2, float z2,
                              float x3, float y3, float z3,
                              float x4, float y4, float z4,
                              float u0, float v0, float u1, float v1,
                              float tw, float th,
                              float nx, float ny, float nz,
                              boolean mirror) {
        float uA = mirror ? u1 : u0;
        float uB = mirror ? u0 : u1;

        buf.pos(x1, y1, z1).tex(uA / tw, v0 / th).normal(nx, ny, nz).endVertex();
        buf.pos(x2, y2, z2).tex(uB / tw, v0 / th).normal(nx, ny, nz).endVertex();
        buf.pos(x3, y3, z3).tex(uB / tw, v1 / th).normal(nx, ny, nz).endVertex();
        buf.pos(x4, y4, z4).tex(uA / tw, v1 / th).normal(nx, ny, nz).endVertex();
    }
}
