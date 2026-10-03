package com.voltyx.mwccf.blood;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @deprecated Replaced by procedural per-texel blood overlay in {@link BloodTextureManager}.
 */
@Deprecated
@SideOnly(Side.CLIENT)
public class LayerBloodOverlay implements LayerRenderer<AbstractClientPlayer> {

    public LayerBloodOverlay(RenderPlayer renderer) {
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player,
                              float limbSwing, float limbSwingAmount, float partialTicks,
                              float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
