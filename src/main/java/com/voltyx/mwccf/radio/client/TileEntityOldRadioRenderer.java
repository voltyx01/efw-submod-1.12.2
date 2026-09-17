package com.voltyx.mwccf.radio.client;

import com.voltyx.mwccf.radio.BlockOldRadio;
import com.voltyx.mwccf.radio.TileEntityOldRadio;
import com.voltyx.mwccf.render.bedrock.BedrockBlockModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityOldRadioRenderer extends TileEntitySpecialRenderer<TileEntityOldRadio> {

    private static final BedrockBlockModel MODEL = new BedrockBlockModel();
    private static final ResourceLocation MODEL_GEO = new ResourceLocation("mwccf", "geo/oldradio.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation("mwccf", "textures/blocks/oldradio.png");

    private static void ensureLoaded() {
        if (!MODEL.isLoaded()) {
            MODEL.load(MODEL_GEO, null);
        }
    }

    @Override
    public void render(TileEntityOldRadio te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null) return;

        ensureLoaded();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);

        EnumFacing facing = EnumFacing.NORTH;
        if (te.hasWorld() && te.getPos() != null) {
            try {
                facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockOldRadio.FACING);
            } catch (Throwable ignored) {
            }
        }

        switch (facing) {
            case NORTH: GlStateManager.rotate(0, 0, 1, 0); break;
            case SOUTH: GlStateManager.rotate(180, 0, 1, 0); break;
            case WEST:  GlStateManager.rotate(90, 0, 1, 0); break;
            case EAST:  GlStateManager.rotate(270, 0, 1, 0); break;
            default: break;
        }

        this.bindTexture(TEXTURE);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableCull();

        MODEL.render(1.0F / 16.0F);

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
