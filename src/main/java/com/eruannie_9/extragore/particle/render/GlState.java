/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class GlState {
    private GlState() {
    }

    public static void pushMatrix() {
        GlStateManager.pushMatrix();
    }

    public static void popMatrix() {
        GlStateManager.popMatrix();
    }

    public static void pushAttrib() {
        GlStateManager.pushAttrib();
    }

    public static void popAttrib() {
        GlStateManager.popAttrib();
    }

    public static void disableLighting() {
        GlStateManager.disableLighting();
    }

    public static void enableBlend() {
        GlStateManager.enableBlend();
    }

    public static void blendFuncSeparate(GlStateManager.SourceFactor srcRgb, GlStateManager.DestFactor dstRgb, GlStateManager.SourceFactor srcA, GlStateManager.DestFactor dstA) {
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)srcRgb, (GlStateManager.DestFactor)dstRgb, (GlStateManager.SourceFactor)srcA, (GlStateManager.DestFactor)dstA);
    }

    public static void enableDepth() {
        GlStateManager.enableDepth();
    }

    public static void depthMask(boolean enable) {
        GlStateManager.depthMask((boolean)enable);
    }

    public static void disableCull() {
        GlStateManager.disableCull();
    }

    public static void enableTexture2D() {
        GlStateManager.enableTexture2D();
    }
}

